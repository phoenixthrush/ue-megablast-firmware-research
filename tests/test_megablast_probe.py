import asyncio

import pytest

from megablast_probe import (
    CPLDecoder,
    Diagnostics,
    ProtocolError,
    authorization_status,
    file_data,
    file_size,
    firmware_string,
    is_long_response_notice,
    reply_payload,
)


def test_distinguish_long_response_notice_from_concurrent_read():
    assert is_long_response_notice(bytes.fromhex("0200"))
    assert not is_long_response_notice(b"\x01" + b"x" * 511)


def test_v3_file_size_event_after_empty_ack():
    async def scenario():
        diagnostics = Diagnostics(None, {"operations": []})
        diagnostics.queue.put_nowait(("command", bytes.fromhex("00051000000001")))
        diagnostics.queue.put_nowait(
            ("command", bytes.fromhex("0005300002f329965c786d"))
        )
        payload = await diagnostics.wait_event(5, 3)
        assert file_size(payload) == (193321, 0x965C786D)

    asyncio.run(scenario())


@pytest.mark.parametrize("payload", [b"", b"\x00" * 4, b"\x00" * 9])
def test_reject_incomplete_file_size(payload):
    with pytest.raises(ProtocolError):
        file_size(payload)


def test_authorization_notification_requires_root_event_and_zero_software_id():
    assert authorization_status(b"\x00\x10\x00") == 0
    assert authorization_status(b"\x00\x10\x01") == 1
    assert authorization_status(b"\x00\x11\x00") is None
    assert authorization_status(b"\x00\x10") is None


def test_reassemble_long_response_and_reset():
    decoder = CPLDecoder()
    assert decoder.feed(b"\x01\x07\x12\x03") is None
    assert decoder.feed(b"\x02\x04\x00\xd8") == b"\x07\x12\x03\x04\x00\xd8"
    assert decoder.feed(b"\x00\x00\x01\x07\x00\x01") == b"\x00\x01\x07\x00\x01"


@pytest.mark.parametrize("packet", [b"", b"\x02\x07\x12", b"\x00\x07"])
def test_reject_malformed_cpl(packet):
    with pytest.raises(ProtocolError):
        CPLDecoder().feed(packet)


def test_authorization_error_is_not_a_success():
    with pytest.raises(ProtocolError, match="14"):
        reply_payload(bytes.fromhex("ff00010e01"), bytes.fromhex("00010100"))


def test_ignore_unrelated_response():
    assert reply_payload(b"\x07\x13\x03\x04\x00\xd8", b"\x07\x12") is None
    assert reply_payload(b"\x07\x12\x03\x04\x00\xd8", b"\x07\x12") == bytes.fromhex(
        "030400d8"
    )


def test_version_encoding():
    assert firmware_string(bytes.fromhex("030400d8")) == "3.4.216"
    with pytest.raises(ProtocolError):
        firmware_string(b"\x03\x04")


@pytest.mark.parametrize(
    "payload,requested",
    [(b"", 4), (b"\x00\x04abc", 4), (b"\x00\x04abcd", 3), (b"\x00\x00", 4)],
)
def test_reject_invalid_log_length(payload, requested):
    with pytest.raises(ProtocolError):
        file_data(payload, requested)


def test_valid_log_chunk():
    assert file_data(b"\x00\x04abcd", 4) == b"abcd"
