"""Reconstructed metadata-only companion to the historical BLE probe.

Uses the same documented getters as the successful feature inventory. It does
not read logs or serial/MAC values. Authorization remains explicitly opt-in.
"""

import argparse
import asyncio
import json
import os
from pathlib import Path

from bleak import BleakClient, BleakScanner

from megablast_probe import (
    ADVERTISEMENT,
    AUTHORIZE,
    Diagnostics,
    NotAuthorized,
    ProtocolError,
    firmware_string,
)


def parse_feature_page(payload, start, total):
    if (
        not payload
        or payload[0] == 0
        or len(payload) != 1 + 4 * payload[0]
        or start + payload[0] > total
    ):
        raise ProtocolError("Invalid feature page")
    result = []
    for position in range(payload[0]):
        record = payload[1 + position * 4 : 5 + position * 4]
        result.append(
            {
                "index": start + position,
                "feature_id": f"0x{int.from_bytes(record[:2], 'big'):04x}",
                "type": record[2],
                "version": record[3],
            }
        )
    return result


async def collect(args, evidence):
    devices = await BleakScanner.discover(
        timeout=8, return_adv=True, service_uuids=[ADVERTISEMENT]
    )
    candidates = [
        device for device, adv in devices.values() if 0x0103 in adv.manufacturer_data
    ]
    if len(candidates) != 1:
        raise ProtocolError(f"Expected one MEGABLAST, found {len(candidates)}")
    async with BleakClient(candidates[0], timeout=15) as client:
        evidence["gatt"] = [
            {
                "uuid": service.uuid,
                "characteristics": [
                    {"uuid": item.uuid, "properties": item.properties}
                    for item in service.characteristics
                ],
            }
            for service in client.services
        ]
        diagnostics = Diagnostics(client, evidence)
        await diagnostics.subscribe()
        if args.authorize:
            if not args.client_id_file.exists():
                descriptor = os.open(
                    args.client_id_file, os.O_WRONLY | os.O_CREAT | os.O_EXCL, 0o600
                )
                with os.fdopen(descriptor, "wb") as stream:
                    stream.write(os.urandom(6))
            identifier = args.client_id_file.read_bytes()
            if len(identifier) != 6:
                raise ProtocolError("Client ID must contain six bytes")
            await client.write_gatt_char(AUTHORIZE, identifier + b"\x42", response=True)
            evidence["authorization_handshake_sent"] = True
        try:
            index, _ = await diagnostics.feature(0x0100)
        except NotAuthorized:
            if not args.authorize:
                raise
            await diagnostics.wait_for_authorization(identifier)
            index, _ = await diagnostics.feature(0x0100)
        evidence["firmware_version"] = firmware_string(
            await diagnostics.request(index, 1)
        )
        hardware = await diagnostics.request(index, 0)
        if len(hardware) != 8:
            raise ProtocolError("Unexpected hardware response")
        evidence["hardware"] = {
            "model": hardware[0],
            "revision": hardware[1],
            "color": int.from_bytes(hardware[2:4], "big"),
            "sku_attributes": int.from_bytes(hardware[4:], "big"),
        }
        feature_index, _ = await diagnostics.feature(1)
        count = await diagnostics.request(feature_index, 0)
        if len(count) != 1 or count[0] > 128:
            raise ProtocolError("Invalid feature count")
        evidence["feature_table"] = []
        while len(evidence["feature_table"]) < count[0]:
            start = len(evidence["feature_table"])
            page = await diagnostics.request(feature_index, 1, bytes([start]))
            evidence["feature_table"].extend(parse_feature_page(page, start, count[0]))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--evidence", required=True, type=Path)
    parser.add_argument("--authorize", action="store_true")
    parser.add_argument("--client-id-file", type=Path)
    args = parser.parse_args()
    if args.authorize and args.client_id_file is None:
        parser.error("--authorize requires --client-id-file")
    evidence = {
        "transport": "BLE",
        "operations": [],
        "authorization_handshake_sent": False,
    }
    try:
        asyncio.run(collect(args, evidence))
    except Exception as error:
        evidence["error"] = f"{type(error).__name__}: {error}"
        raise
    finally:
        args.evidence.write_text(json.dumps(evidence, indent=2) + "\n")


if __name__ == "__main__":
    main()
