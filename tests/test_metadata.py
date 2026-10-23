import json
from pathlib import Path

import pytest

from collect_metadata import parse_feature_page
from megablast_probe import ProtocolError


def test_decode_recorded_feature_inventory_pages():
    path = Path(__file__).parents[1] / "evidence/ble-feature-inventory.json"
    captured = json.loads(path.read_text())
    expected = captured["feature_table"]
    actual = []
    for operation in captured["operations"]:
        request = bytes.fromhex(operation["request"])
        if len(request) == 4 and request[1] == 1 and request[2] >> 4 == 1:
            payload = bytes.fromhex(operation["reply"])[2:]
            actual.extend(parse_feature_page(payload, request[3], len(expected)))
    assert actual == [
        {k: v for k, v in item.items() if k != "name"} for item in expected
    ]
    assert len(actual) == 24


@pytest.mark.parametrize(
    "payload", [b"", b"\x00", b"\x01\x00", b"\x02\x01\x00\x00\x02"]
)
def test_reject_malformed_feature_pages(payload):
    with pytest.raises(ProtocolError):
        parse_feature_page(payload, 0, 24)


def test_reject_feature_page_exceeding_advertised_count():
    with pytest.raises(ProtocolError):
        parse_feature_page(bytes.fromhex("0101000002"), 24, 24)
