"""MEGABLAST BLE diagnostics recovered from companion app 2.2.121.

Authorization is opt-in. No update, reset, remote-power, or file-write commands.
"""

import argparse
import asyncio
import json
import os
import time
import zlib
from pathlib import Path

from bleak import BleakClient, BleakScanner
from bleak.exc import BleakError

ADVERTISEMENT = "0000fe61-0000-1000-8000-00805f9b34fb"
COMMAND = "748e2216-8b9a-4598-a0c6-2dee1af454db"
LONG_RESPONSE = "8f83dd07-e637-5cea-a223-f1f3b64b73ee"
AUTHORIZE = "5e9bf2a8-f93f-4481-a67e-3b2f4a07891a"


class ProtocolError(RuntimeError):
    pass


class NotAuthorized(ProtocolError):
    pass


class CPLDecoder:
    def __init__(self):
        self.buffer = bytearray()
        self.expected = 0

    def feed(self, packet):
        if not packet or packet[0] >> 1 != self.expected:
            self.buffer.clear()
            self.expected = 0
            raise ProtocolError("Empty or out-of-order CPL segment")
        self.buffer.extend(packet[1:])
        self.expected += 1
        if len(self.buffer) > 8192:
            raise ProtocolError("CPL response exceeds diagnostic limit")
        if packet[0] & 1:
            return None
        message = bytes(self.buffer)
        self.buffer.clear()
        self.expected = 0
        if len(message) < 2:
            raise ProtocolError("Truncated Centurion message")
        return message


def reply_payload(message, request):
    if len(message) >= 4 and message[0] == 255 and message[1:3] == request[:2]:
        if message[3] == 14:
            raise NotAuthorized("Centurion error 14 (NOT_AUTHORIZED)")
        raise ProtocolError(f"Centurion error {message[3]} (14 = NOT_AUTHORIZED)")
    if message[:2] == request[:2]:
        return message[2:]
    return None


def authorization_status(message):
    if len(message) >= 3 and message[:2] == b"\x00\x10":
        return message[2]
    return None


def firmware_string(payload):
    if len(payload) != 4:
        raise ProtocolError("Firmware version must contain four bytes")
    return f"{payload[0]}.{payload[1]}.{int.from_bytes(payload[2:], 'big')}"


def file_data(payload, requested):
    if len(payload) < 2:
        raise ProtocolError("Truncated file response")
    length = int.from_bytes(payload[:2], "big")
    if not 0 < length <= requested or len(payload) != length + 2:
        raise ProtocolError("Invalid file response length")
    return payload[2:]


def file_size(payload):
    if len(payload) != 8:
        raise ProtocolError("Unexpected file-size response")
    return int.from_bytes(payload[:4], "big"), int.from_bytes(payload[4:], "big")


def is_long_response_notice(data):
    # Notices contain a BE16 length; actual GATT reads return a 512-byte buffer.
    return len(data) == 2


class Diagnostics:
    def __init__(self, client, evidence):
        self.client = client
        self.evidence = evidence
        self.queue = asyncio.Queue()
        self.decoder = CPLDecoder()
        self.swid = 0

    def receive(self, characteristic, data):
        self.queue.put_nowait((characteristic.uuid, bytes(data)))

    async def subscribe(self):
        for uuid in (COMMAND, LONG_RESPONSE):
            try:
                options = (
                    {"notification_discriminator": is_long_response_notice}
                    if uuid == LONG_RESPONSE
                    else {}
                )
                await self.client.start_notify(uuid, self.receive, cb=options)
            except BleakError as error:
                # This device omits CCCDs. Bleak 3.0.2 retains its local callback
                # after this particular CoreBluetooth failure; replies still arrive.
                if "Code=10" not in str(error):
                    raise
                self.evidence.setdefault("compatibility", []).append(
                    {"characteristic": uuid, "missing_cccd": True}
                )

    async def request(self, feature, function, payload=b"", *, record_reply=True):
        self.swid = self.swid % 15 + 1
        request = bytes((feature, function << 4 | self.swid)) + payload
        operation = {"request": (b"\x00" + request).hex()}
        self.evidence["operations"].append(operation)
        async with asyncio.timeout(10):
            await self.client.write_gatt_char(COMMAND, b"\x00" + request, response=True)
            while True:
                message = await self.next_message()
                if message[0] == 255:
                    operation["error_reply"] = message.hex()
                result = reply_payload(message, request)
                if result is not None:
                    if record_reply:
                        operation["reply"] = message.hex()
                    else:
                        operation["reply_bytes"] = len(message)
                    return result

    async def next_message(self):
        while True:
            uuid, data = await self.queue.get()
            if uuid == LONG_RESPONSE:
                if len(data) < 2:
                    raise ProtocolError("Truncated long-response notice")
                length = int.from_bytes(data[:2], "big")
                if not 0 < length <= 512:
                    raise ProtocolError("Invalid long-response length")
                data = bytes(await self.client.read_gatt_char(LONG_RESPONSE))
                if len(data) < length:
                    raise ProtocolError("Truncated long-response data")
                data = data[:length]
            message = self.decoder.feed(data)
            if message is not None:
                return message

    async def wait_event(self, feature, event):
        async with asyncio.timeout(10):
            while True:
                message = await self.next_message()
                if message[:2] == bytes((feature, event << 4)):
                    self.evidence.setdefault("events", []).append(message.hex())
                    return message[2:]

    async def feature(self, feature_id):
        info = await self.request(0, 0, feature_id.to_bytes(2, "big"))
        if len(info) != 3 or info[0] == 0:
            raise ProtocolError(f"Feature {feature_id:#06x} unsupported or malformed")
        self.evidence.setdefault("features", {})[f"{feature_id:#06x}"] = {
            "index": info[0],
            "type": info[1],
            "version": info[2],
        }
        return info[0], info[2]

    async def wait_for_authorization(self, identifier):
        print(
            "Press the speaker's Bluetooth button once to confirm this client.",
            flush=True,
        )
        renewed = False
        started = time.monotonic()
        async with asyncio.timeout(120):
            while True:
                uuid, data = await self.queue.get()
                if uuid != COMMAND:
                    continue
                message = self.decoder.feed(data)
                if message is None:
                    continue
                status = authorization_status(message)
                if status is None:
                    continue
                self.evidence["authorization_status"] = status
                self.evidence.setdefault("authorization_events", []).append(
                    {
                        "status": status,
                        "seconds_waiting": round(time.monotonic() - started, 3),
                    }
                )
                if status != 0:
                    if status == 1 and not renewed:
                        renewed = True
                        await self.client.write_gatt_char(
                            AUTHORIZE, identifier + b"\x42", response=True
                        )
                        print(
                            "Authorization window renewed once; press Bluetooth now.",
                            flush=True,
                        )
                        continue
                    raise ProtocolError(f"Authorization failed with status {status}")
                # The original app waits 250 ms after this notification.
                await asyncio.sleep(0.25)
                return

    async def read_speaker_log(self, path):
        index, version = await self.feature(0x0105)
        if version < 1:
            raise ProtocolError("Unsupported filesystem feature version")
        number = b"\x00\x42"
        size_reply = await self.request(index, 5, number)
        # Observed on feature v3: empty ACK, then event 3 (SWID 0).
        if not size_reply and version == 3:
            size_reply = await self.wait_event(index, 3)
        size, checksum = file_size(size_reply)
        if size > 1024 * 1024:
            raise ProtocolError("Log exceeds one MiB diagnostic limit")
        result = bytearray()
        await self.request(index, 0, number + b"\x01")
        try:
            while len(result) < size:
                requested = min(4000, size - len(result))
                response = await self.request(
                    index, 1, number + requested.to_bytes(2, "big"), record_reply=False
                )
                result.extend(file_data(response, requested))
        finally:
            await self.request(index, 3, number)
        path.write_bytes(result)
        path.chmod(0o600)
        self.evidence["speaker_log"] = {"file_number": 66, "bytes": len(result)}
        self.evidence["speaker_log"].update(
            {
                "size_crc32": f"{checksum:08x}",
                "computed_crc32": f"{zlib.crc32(result):08x}",
                "standard_crc32_matches": zlib.crc32(result) == checksum,
                "inverted_crc32_matches": (zlib.crc32(result) ^ 0xFFFFFFFF) == checksum,
            }
        )


async def run(args, evidence):
    devices = await BleakScanner.discover(
        timeout=8, return_adv=True, service_uuids=[ADVERTISEMENT]
    )
    # Android advertises big-endian PID 0x0301 where CoreBluetooth exposes
    # little-endian manufacturer ID 0x0103. Do not connect on name alone.
    candidates = [d for d, a in devices.values() if 0x0103 in a.manufacturer_data]
    if len(candidates) != 1:
        raise ProtocolError(f"Expected one MEGABLAST, found {len(candidates)}")
    async with BleakClient(candidates[0], timeout=15) as client:
        evidence["gatt"] = [
            {"uuid": s.uuid, "characteristics": [c.uuid for c in s.characteristics]}
            for s in client.services
        ]
        diagnostics = Diagnostics(client, evidence)
        await diagnostics.subscribe()
        if args.authorize:
            if not args.client_id_file.exists():
                args.client_id_file.write_bytes(os.urandom(6))
                args.client_id_file.chmod(0o600)
            identifier = args.client_id_file.read_bytes()
            if len(identifier) != 6:
                raise ProtocolError("Client ID file must contain exactly six bytes")
            # Unlike Android's app, use the device's advertised write property:
            # a macOS command-write trial produced no reply.
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
        print("Firmware:", evidence["firmware_version"], flush=True)
        index, _ = await diagnostics.feature(0x0403)
        status = await diagnostics.request(index, 0)
        if len(status) != 5:
            raise ProtocolError("Unexpected OTA status length")
        mode = await diagnostics.request(index, 2)
        if len(mode) != 1:
            raise ProtocolError("Unexpected OTA install-mode length")
        evidence["ota"] = {
            "status": status[0],
            "parameter": int.from_bytes(status[1:], "big"),
            "install_mode": mode[0],
        }
        print("OTA:", evidence["ota"], flush=True)
        if args.log_file:
            await diagnostics.read_speaker_log(args.log_file)
            print("Log bytes:", evidence["speaker_log"]["bytes"], flush=True)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--authorize",
        action="store_true",
        help="Send normal app authorization; may register this client",
    )
    parser.add_argument("--client-id-file", type=Path)
    parser.add_argument(
        "--log-file", type=Path, help="Save diagnostic log 66; may contain private data"
    )
    parser.add_argument("--evidence", required=True, type=Path)
    args = parser.parse_args()
    if args.authorize and args.client_id_file is None:
        parser.error("--authorize requires --client-id-file")
    evidence = {
        "transport": "BLE",
        "operations": [],
        "authorization_handshake_sent": False,
    }
    try:
        asyncio.run(run(args, evidence))
    except Exception as error:
        evidence["error"] = f"{type(error).__name__}: {error}"
        raise
    finally:
        args.evidence.write_text(json.dumps(evidence, indent=2) + "\n")


if __name__ == "__main__":
    main()
