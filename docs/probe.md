# MEGABLAST diagnostic probe

Experimental Python client reconstructed from the original Android companion app
2.2.121. It connects only when exactly one matching MEGABLAST advertisement is
found. This is a protocol research tool, not a firmware updater or dumper.

Implemented operations: DeviceInfo feature discovery and firmware getter; OTA
status/install-mode getters; optional read of the app's documented diagnostic log
66. It contains no reset, update-check, install, power, file-write, erase, or
arbitrary-file enumeration operation.

Authorization requires `--authorize` and a persistent six-byte client ID file. It
sends the app's seven-byte handshake, which may register a controller on the
speaker. The extent/persistence of registration has not been established. A new
controller may require a brief press of the speaker's Bluetooth button. Without
`--authorize`, an authorization error ends the probe.

## Running

Use Python 3.11 or newer with `pip install -r requirements.txt`. macOS requires
Bluetooth access for the host application. Installed/tested here: Python 3.12.14,
Bleak 3.0.2. The current Mac requires shell access outside the execution sandbox.

From the repository root, after creating the environment described in the README:

```sh
mkdir -p private
.venv/bin/python megablast_probe.py \
  --evidence private/session.json
```

After explicit approval for the normal app authorization handshake:

```sh
.venv/bin/python megablast_probe.py \
  --authorize --client-id-file private/client-id.bin \
  --log-file private/speaker-log-66.bin --evidence private/session.json
```

Keep the client ID and speaker log private. The log may contain credentials or
device identifiers. Evidence excludes client-ID bytes and file-read payloads.

## Compatibility and verification

The actual speaker advertises command `write`/`notify`, long-response
`read`/`notify`, and authorization `read`/`write`, with no CCCDs. Standard macOS
notification subscription therefore fails with ATT error 10. In Bleak 3.0.2, the
local callback remains registered after that particular failure and observed
Centurion replies arrive through it. This fallback depends on Bleak's current
implementation; other subscription failures are fatal.

The Android app chooses unacknowledged writes. On this Mac, an unacknowledged
command trial produced no reply; the probe uses the actual device's advertised
acknowledged-write property. This difference remains relevant to authorization
troubleshooting.

Live access succeeded on firmware **3.4.216**. Its filesystem feature version 3
returns an empty SIZE acknowledgement, followed by event 3 with the size and CRC.
The probe handles that observed format and acquired all 193,321 bytes of log 66.
The returned CRC `965c786d` equals the bitwise inverse of the file's standard
CRC-32 `69a38792`. Two-byte long-response notices are distinguished from full
GATT reads with Bleak's CoreBluetooth notification discriminator.

Authorization waits for a root status event after an initial denial, allows one
renewal following a timeout, and waits the original app's 250 ms after success.
Subsequent connections using the same client ID succeeded without another button
press; authorization persistence across speaker power cycles remains untested.

Offline verification: `ruff format .`, `ruff check .`, and `pytest` from the
repository root. Tests cover CPL fragmentation, response matching, authorization errors,
notification classification, version decoding, and invalid file response lengths.
They do not establish successful firmware/log access on the device. Consult the
[research ledger](findings.md) for the recorded live result.
