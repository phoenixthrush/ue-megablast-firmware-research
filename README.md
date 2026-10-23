# Ultimate Ears MEGABLAST firmware research

Investigation snapshot: **23 October 2026**. Device: **MEGABLAST**, not MEGABOOM.

## What we found

- **Firmware 3.4.216**, confirmed directly through the speaker's Bluetooth diagnostic protocol after initially reading it in the iPhone app.
- **Working authorized BLE diagnostic access**, including feature discovery, firmware and hardware getters, OTA status, and the documented speaker-log read.
- **A complete 193,321-byte diagnostic log**. Its checksum matches the device's inverted CRC representation. The private log records an update from 3.3.213 to 3.4.216 in October 2020.
- **24 advertised Centurion features** and a filesystem-version-3 difference: SIZE returns an empty acknowledgement followed by a separate size/CRC event.
- **USB HID identity `046d:0a6a`**, vendor usage page `0xffa0`, and 64-byte input/output reports.
- **Original official Mac and Windows diagnostic tools**, their download URLs and hashes, plus useful protocol definitions recovered from Android companion app 2.2.121.

## What we did not find or achieve

- **No complete speaker firmware image or flash dump.** The diagnostic log is not a firmware image.
- **No demonstrated custom firmware, patch installation, jailbreak, or shell access.** Firmware modification remains unproven.
- No established partition map, actual RTOS, signature/encryption enforcement, secure-boot configuration, or validated recovery procedure.
- No recovered numeric USB command framing. The isolated legacy NW.js replay did not reach the authored page or execute the vendor caches.
- No firmware payload URL in the acquired log or inspected app update flow. Bounded unsuccessful searches do not prove that an image is unavailable elsewhere.
- No reset, flash, update check/install, remote-power command, guessed numbered-file enumeration, exploit, or hardware disassembly was performed.

The original Spotify bitrate question was dropped during the investigation. This archive does not claim to fix it.

## Archive contents

| Path | Contents |
| --- | --- |
| [docs/findings.md](docs/findings.md) | Detailed findings, sources, uncertainty, and live observations |
| [docs/timeline.md](docs/timeline.md) | Investigation history, failed attempts, corrections, and the abandoned detour |
| [docs/protocol.md](docs/protocol.md) | Centurion framing, getters, authorization, log access, and transport limits |
| [docs/commands.md](docs/commands.md) | Reproduction commands and their observed results |
| [docs/probe.md](docs/probe.md) | Diagnostic client usage and compatibility notes |
| [docs/nwjs-replay.md](docs/nwjs-replay.md) | Unfinished USB-tool replay and sandbox experiments |
| [docs/sources.md](docs/sources.md) | Artifact sources, versions, hashes, and reproduction boundaries |
| [docs/privacy.md](docs/privacy.md) | Redactions, excluded sensitive material, and publication checks |
| [docs/original-research-handoff.md](docs/original-research-handoff.md) | Redacted historical starting material; not all initial leads were verified |
| [megablast_probe.py](megablast_probe.py) | The BLE client used for successful diagnostic reads |
| [tests/](tests/) | Offline protocol regression tests |
| [evidence/](evidence/) | Redacted observations, request/reply metadata, failed sessions, inventories, hashes, and analysis export |
| [analysis/android/](analysis/android/) | Selected relevant decompiled protocol sources with provenance |
| [experiments/nw-replay/](experiments/nw-replay/) | Authored fixtures, fake-HID harness, and redacted profile templates |

This preserves the investigation's findings, surviving test code, evidence, and unsuccessful avenues. It is a redacted research archive, not a verbatim chat export or a mirror of every third-party installation. Raw logs, client IDs, embedded application credentials, proprietary application binaries, and unrelated personal material are excluded; their relevant provenance and conclusions are retained. See [the exclusion record](docs/privacy.md).

## Run the offline tests

Python 3.11 or newer:

```sh
python3 -m venv .venv
.venv/bin/python -m pip install -r requirements-dev.txt
.venv/bin/ruff format .
.venv/bin/ruff check .
.venv/bin/pytest
```

The historical live environment used Python 3.12.14, Bleak 3.0.2, and macOS. Dependencies are deliberately unpinned; the CCCD workaround depends on Bleak 3.0.2 behavior and may require revalidation with other versions. Passing offline tests does not establish compatibility with another speaker or operating system.

## Live diagnostics

Only use this on a speaker you own or are authorized to investigate. A log may contain credentials and identifiers. Keep generated files in the ignored `private/` directory.

```sh
mkdir -p private
.venv/bin/python megablast_probe.py --evidence private/session.json
```

The default sends getters without changing authorization. If authorization is required, the probe stops. The separately selected `--authorize` option sends the normal app handshake and may register a controller; this was explicitly approved during the original investigation:

```sh
.venv/bin/python megablast_probe.py \
  --authorize --client-id-file private/client-id.bin \
  --log-file private/speaker-log-66.bin --evidence private/session.json
```

Briefly press the Bluetooth button when prompted. This is different from holding it for ordinary Bluetooth pairing. Do not translate the BLE bytes into USB reports without establishing the USB wrapper.

## Status

This is an archived investigation snapshot. The substantive remaining requirement for firmware modification is an original firmware image, followed by analysis of its format and verification/recovery mechanisms. The USB-runtime experiments are preserved as unfinished work, not a recommended prerequisite for using the successful BLE client.

Newly authored work is released under [the Unlicense](LICENSE). Third-party material retains its original ownership; see [NOTICE.md](NOTICE.md). Publication checks are recorded in [docs/verification.md](docs/verification.md).
