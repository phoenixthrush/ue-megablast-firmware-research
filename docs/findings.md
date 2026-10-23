# MEGABLAST firmware investigation

Updated: 23 October 2026. Scope: identify and acquire the firmware, investigate read access, and assess whether modification is feasible. The Spotify bitrate investigation has been dropped at the owner's request.

## Current result

The owner's speaker now has a **working, authorized Bluetooth diagnostic connection** from this Mac. The decoded getter directly confirmed firmware **3.4.216**. The USB HID interface and original downloadable diagnostic packages were also identified. This establishes diagnostic access, **not** a firmware dump, shell, jailbreak, or ability to install modified firmware.

Firmware **3.4.216**, initially reported from the iPhone app, was independently corroborated over BLE with payload `03 04 00 d8`. The operating system, flash layout, signature enforcement, and recovery procedure remain unverified. No firmware image has been acquired. An initial exact-version public search did not yield a usable matching update image or manifest; this does not establish that none is hosted.

## Observations from the actual speaker

These are observations from the Mac's IORegistry after the owner connected the speaker, rather than claims copied from another hardware revision.

| Field | Observed value | Interpretation |
| --- | --- | --- |
| Manufacturer / product | Logitech / MEGABLAST | USB device identity |
| UE firmware | `3.4.216` | iPhone-app report corroborated by the actual BLE firmware getter |
| USB VID:PID | `046d:0a6a` | Confirmed for this unit in its present state |
| `bcdDevice` | `0x0403` (decimal 1027) | USB device-release descriptor; **not independently confirmed as the complete speaker firmware version** |
| Interface | 0, class 3, subclass 0, protocol 0 | HID interface |
| HID usage page / usage | `0xffa0` / `0x0001` | Vendor-specific HID usage |
| Input / output report sizes | 64 / 64 bytes | Report descriptor and host properties agree |
| Feature-report size | 0 | No HID feature reports declared in the observed descriptor |
| Interface endpoint count | 1 | From `bNumEndpoints`; does not establish endpoint address/type or a firmware protocol |

HID report descriptor:

```text
06a0ff0901a1010901150026ff00750896400091000902750895408100c0
```

Redacted evidence: [USB descriptors](../evidence/megablast-usb-descriptors.txt). Serial numbers and MAC addresses are omitted from the deliverables.

## Network observations

Bonjour advertised `_spotify-connect._tcp` at `speaker-0.local.:9595`, with `CPath=/zc/0`, `VERSION=1.0`, and `Stack=SP`. IPv4 resolution returned `192.0.2.10`.

The owner successfully made this read-only request from Terminal:

```sh
curl --noproxy '*' --connect-timeout 3 --max-time 8 \
  'http://192.0.2.10:9595/zc/0?action=getInfo' \
  | jq '{version,libraryVersion,resolverVersion,brandDisplayName,modelDisplayName}'
```

The owner supplied this output:

```json
{
  "version": "2.7.1",
  "libraryVersion": "3.34.35-g41bd76c2",
  "resolverVersion": "0",
  "brandDisplayName": "UltimateEars",
  "modelDisplayName": "<speaker-name>"
}
```

`version` is the ZeroConf API version and `libraryVersion` identifies Spotify's embedded library. Neither identifies the complete UE firmware release. The displayed model string also reflects this device's name, rather than independently establishing a hardware model number.

The agent process could discover Bonjour and inspect USB metadata, but its HTTP connections failed even when the owner's equivalent Terminal request succeeded. An earlier route lookup selected VPN interface `utun7` for the speaker's subnet; the owner subsequently enabled local network access. The remaining process-specific networking restriction is unresolved. Failed agent connections must **not** be interpreted as proof that ports are closed. A small `nc` check stopped at its first routing error and did not produce a useful service inventory. SSH, Telnet, other HTTP ports, and UDP services remain unverified.

The initial ZeroConf query intentionally omitted the optional API-version parameter. A TXT `VERSION` value should not be assumed to be the query parameter or the firmware version. See [Spotify's ZeroConf guide](https://developer.spotify.com/documentation/commercial-hardware/implementation/guides/zeroconf) and [FAQ](https://developer.spotify.com/documentation/commercial-hardware/implementation/faqs).

## Recovered official tools

The download URLs were recovered from URI annotations in historical UE support PDFs, then tested directly against Logitech's servers. HTTPS was used in place of the PDFs' original HTTP diagnostic links. HTTP success and source provenance do not substitute for code-signature verification.

| Package | Result on 23 October 2026 | Bytes | SHA-256 |
| --- | --- | --- | --- |
| [macOS diagnostic DMG](https://files.logitech.com/audio/mobileApps/resources/tools/BLAST_and_MEGABLAS_%20Diagnostic_Tool.dmg) | HTTP 200; downloaded | 96,234,145 | `00158207751ab1257f4b8c0f2040782f4e26be1267c2657f68c2aa950d0d6423` |
| [Windows diagnostic MSI](https://files.logitech.com/audio/mobileApps/resources/tools/BLAST_%26_MEGABLAST_Diagnostic_Tool.msi) | HTTP 200; downloaded | 66,347,008 | `6b742031e330ce7c9e416d108fdd31f2e6c0f8315d152b00671fcab494f5c253` |
| [macOS Update Assistant](https://download01.logitech.com/web/ftp/pub/ue/UltimateEars_UpdateAssistant.dmg) | HTTP 404 on HEAD | — | — |
| [Windows Update Assistant](https://download01.logitech.com/web/ftp/pub/ue/UltimateEars_UpdateAssistant.exe) | HTTP 404 on HEAD | — | — |

Sources: [diagnostic FAQ, page 1](https://m.media-amazon.com/images/I/A1KtnBF%2BupS.pdf) and [updater FAQ, pages 1 and 3](https://images-eu.ssl-images-amazon.com/images/I/A1VznU2mAOS.pdf). The diagnostic DMG filename really is `MEGABLAS` without the final T, followed by an encoded space.

The original packages were retained privately in the investigation workspace and are excluded from this archive. They have not been installed, launched, patched, or sent to the speaker.

## Diagnostic application findings

The macOS app contains `Contents/Resources/app.nw/`. Its package manifest declares `ue_diag_tool`, version `1.0.0`, and dependency `node-hid` `^0.7.2`. Its HTML interface contains a firmware-version field and loads `gui.bin` through `nw.Window.get().evalNWBin`.

The bundled runtime contains the literal versions **NW.js 0.31.1** and **V8 6.7.288.44**. Its Chromium directory is `67.0.3396.79`. The [official NW.js 0.31.1 release note](https://nwjs.io/blog/v0.31.1/) corroborates the Chromium version. These are versions of the desktop tool, not the speaker.

The application-specific logic is compiled NW.js/V8 data, not plain JavaScript source. Static strings in the macOS files include:

| File and string offset | Observation | Limit |
| --- | --- | --- |
| `app/njch.bin`, `0x0e4a` | `getFirmware` | Its name does not prove image readout; it may retrieve version information |
| `app/njcc.bin`, `0x1bcb` | `getOTAStatus` | No OTA server or payload URL was recovered |
| `app/njcc.bin`, `0x1e5a` / `0x20bc` | `iFileSystem_FileOpen` / `iFileSystem_FileClose` | Exact protocol and access permissions are unknown |
| `app/njcc.bin`, `0x299e` | `iFileSystem_FileRead` | No demonstrated ability to read arbitrary flash or a firmware partition |
| `app/njcc.bin`, `0x3fde` | `iFileSystem_FileWrite` | A write-related function name, not proof that modified firmware is accepted |
| `app/njch.bin`, `0x3a6f` | `factoryReset` | Destructive operation; not invoked |
| `app/njch.bin`, `0x21fb` / `0x236b` | `setBootModeFactory` / `setBootModeUser` | State-changing operations; not invoked |

Other strings refer to crash logs, diagnostic JSON, serial/MAC information in flash and OTP, and `Manhattan` / `Brooklyn` names associated with MEGABLAST / BLAST. Their presence is evidence for further investigation; control flow, request bytes, target files, and device support have not been established.

The Windows MSI contains a CAB; its diagnostic EXE contains an appended ZIP with compiled application files. Static inspection found corresponding function names there too. The `.bin` filenames in these app directories are compiled desktop application logic. They have **not** been identified as speaker firmware images. No obvious standalone firmware payload was found in the inspected package inventories; this is not an exhaustive absence proof for every resource.

Evidence: [per-file hashes](../evidence/diagnostic-file-manifest.json), [HID-module strings](../evidence/app-njch.bin.strings.txt), [filesystem-module strings](../evidence/app-njcc.bin.strings.txt), [GUI strings](../evidence/gui.bin.strings.txt), and [REA evidence export](../evidence/rea-diagnostic-analysis.json).

REA's selected-source analysis covered the package manifest and HTML only, under parent Evidence ID `ev_869d908a40460817a6b091e90d8af43a0f930d17789994eb354782382b276c71`. It did not decode the compiled NWBin files or analyze the inline `evalNWBin` body. String-offset observations above were collected separately from the immutable extracted bytes. No target code was executed.

## Execution and analysis blockers

- `codesign --verify --deep --strict` failed on the 7-Zip-extracted macOS app. Verification of its extracted `nwjs` executable also reported an invalid signature. This does not establish whether the original distributed image, its old signature, or extraction behavior is responsible. The app was not launched, re-signed, or given a security exception.
- A native read-only DMG mount presented an EULA and canceled. It left no mounted image. No agreement was accepted automatically.
- The documented [ghidra_nodejs support](https://github.com/PositiveTechnologies/ghidra_nodejs) is for V8 `6.2.414.77`; that is not this tool's V8 `6.7.288.44`. A version-compatible decoder or another controlled recovery method is needed before claiming to know HID request bytes. No decoder adaptation has been implemented or validated.
- [Theia](https://github.com/Llamaware/Theia) is a possible static-parser starting point, but targets NW.js 0.29.0 x86 / V8 6.5.254.31. Its loader accepts cache magic `0xC0DE03D2`, whereas `njch.bin` begins with `0xC0DE03A9`, and it selects a 32-bit language. Adapting the magic alone is insufficient: object layouts, pointer size, roots, builtins, and bytecodes require validation against V8 6.7.288.44 x64. [View8](https://github.com/suleram/View8) likewise does not supply a matching 6.7 disassembler among its documented versions. No numeric HID requests were recovered by these checks.
- Internet Archive's updater-availability API returned HTTP 429. Archive recovery remains unverified, not disproved.
- The REA Android provider is not configured (`REA_JADX_MCP_JAR` is unset). The subsequent Android analysis used the official standalone JADX CLI instead, without changing REA configuration.

## Android companion app follow-up

The owner uses an iPhone. The corresponding Android app was obtained independently for offline protocol research: [APKMirror's 2.2.121 listing](https://www.apkmirror.com/apk/logitech-europe-s-a/blast-megablast-by-ultimate-ears/blast-megablast-by-ultimate-ears-2-2-121-release/blast-megablast-by-ultimate-ears-2-2-121-android-apk-download/), package `com.logitech.newjackcity`. [Logitech's official app entry](https://support.logi.com/hc/en-ch/articles/25360486357399-UE-BLAST-MEGABLAST) links to that package on Google Play. No phone access, app installation, or app execution was used.

- Downloaded size: **25,215,524 bytes**.
- Observed APK SHA-256: `83a0fe9b01a8e82b9ac91ac3672d9654b25d4c2f59048fcf6759028a577c0396`, matching the mirror's published value.
- Embedded signer certificate subject: `O=Logitech`; observed certificate SHA-256: `4280fe1b2225084a160045067dff31b6d05836e5809eb1905c5b548f7b5f8713`, also matching the mirror. This is consistency with the published artifact, not an independent trust anchor from the owner's installed app.
- Java `jarsigner` reported `jar verified`, with self-signed certificate, missing timestamp, and ZIP/JarFile-versus-JarInputStream interpretation warnings. Android APK v2/v3 signature validation has not been performed.
- Decompiled with [official JADX 1.5.6](https://github.com/skylot/jadx/releases/tag/v1.5.6), using the owner's existing Homebrew **openjdk@25 25.0.5.0.2**. `/usr/bin/java` could not discover that keg, but its direct Homebrew executable worked. No system Java or REA configuration was changed. A separately downloaded portable JRE remained unused in `work/`.
- JADX emitted **16 global decompilation errors**. Relevant inspected Centurion getter, message-builder, feature-definition, and file-reader methods have complete bodies; this does not establish complete recovery of every app method.

The detailed result is in [Centurion protocol notes](protocol.md):

1. `iDeviceInfo` feature ID **0x0100**, function **1**, is a firmware-version getter. The feature's on-wire index must be discovered first. Version `3.4.216` is encoded as payload **`03 04 00 d8`**.
2. `iFileSystem` feature ID **0x0105** implements numbered files. The app's demonstrated diagnostic reader accesses **file 66 / 0x0042**, described as the speaker log. No firmware-image file number or directory-listing operation was established.
3. The app implements BLE/SPP and an optional Wi-Fi transport. The decoded BLE firmware getter subsequently succeeded on the speaker. These transports do **not** establish USB HID framing; no HID output command has been sent.
4. Production OTA hostname: **`uemobileservices.logitech.com`**. The app sends a hostname-setting command to the speaker and asks the speaker to check/install an update; the inspected updater flow does not provide a standalone firmware payload URL.
5. The OTA validation enum includes file-size, magic, platform, header/component CRC, and **`AUTHENTICATION_FAILED`** errors. This supports investigating update authentication. It does **not** prove a particular signature scheme, secure boot, encryption, or enforcement in firmware 3.4.216.

The public [service-locator XML](https://files.logitech.com/audio/mobileApps/resources/UEServiceLocator.xml) remains downloadable. It names historical firmware endpoints, but this APK's located consumer uses it for geolocation; it should not be treated as proof of the current MEGABLAST update protocol. The XML's `getFWUpdateInfo.php` endpoint returned HTTP 403 over HTTPS. Unauthenticated reads of the production host's root and `/ue/speaker` both returned HTTP 200 with `{}`. No device identifiers or account credentials were sent, and no firmware image or manifest was returned.

## Hardware evidence and limits

The [original teardown](https://www.edn.com/teardown-megablast-speaker-leads-voice-controlled-charge-at-ces-2018/) identifies CYW43907, CYW40707, DBMD5, and MX25L25645G serial NOR flash in its specimen. The flash capacity is 256 Mbit, or 32 MiB. This unit has not been opened to verify its internal revision.

[Infineon's hardware guide](https://community.infineon.com/t5/Knowledge-Base-Articles/CYW43907-Product-Guide-Hardware-Interfaces/ta-p/246294) documents Cortex-R4, loading an application from serial flash into RAM, UART, and JTAG/SWD support at the chip-family level. This supports investigating a WICED/embedded-RTOS firmware route. It does not establish the MEGABLAST's particular RTOS, debug accessibility, security configuration, or which processor terminates USB. Linux has not been established.

## What is needed to assess modification

1. Target UE firmware **3.4.216** is now independently confirmed by BLE; retain this identity when acquiring an image.
2. Recover the USB HID transport layout from a compatible desktop-bytecode decoder or carefully controlled diagnostic trace. The mobile app now supplies verified static inner Centurion definitions, but not the USB wrapper.
3. Determine whether the file-read interface exposes only logs or also firmware/update partitions. Begin with documented getters; do not test guessed file IDs or commands on the speaker.
4. Acquire an original firmware image through official update assets, app analysis, or a verified read-only extraction method.
5. Analyze partition boundaries, CPU/RTOS components, compression, checksums, signature verification, encryption, and device-specific secrets.
6. Establish a validated backup and recovery route before proposing a write. A byte patch on a computer is not proof that the speaker will accept or boot it.

No reset, firmware update/check, recovery command, HID output command, remote-power command, file-write command, exploit, or hardware modification was performed. The owner explicitly approved the normal Bluetooth authorization handshake and diagnostic reads. The available evidence is insufficient to promise custom firmware.

## Workspace status

The original investigation workspace was not a Git repository. The [Python diagnostic probe](probe.md) passed formatting, linting, and 18 offline tests there. This separate redacted archive adds a reconstructed metadata collector, its offline parser tests, and the consolidated evidence. Current archive checks are recorded in [verification.md](verification.md). Hardware observations below were collected during the original investigation; packaging did not repeat device operations.

## Live Bluetooth diagnostics

The sandboxed Python process reported Bluetooth unsupported; the explicitly approved process outside the sandbox could scan and connect. A task-local environment uses Bleak 3.0.2 with the bundled Python 3.12.14. No global packages or Bluetooth configuration were changed by the agent.

The speaker advertises FE61 and manufacturer-header bytes corresponding to PID `0x0301`. Its observed services/characteristics are retained in [the GATT inventory](../evidence/ble-gatt-services.json). Both diagnostic notification characteristics omit CCCDs. Standard subscription reports ATT error 10, but Bleak 3.0.2 retains the local callback, through which actual replies were observed. Long-response two-byte notifications must be distinguished from 512-byte GATT read responses using Bleak's documented CoreBluetooth discriminator.

Initially the root lookup returned `NOT_AUTHORIZED` (14). After the owner's approval, a stable six-byte local client ID plus flag `42` was sent to the app's authorization characteristic. The owner confirmed using the speaker's Bluetooth button. Several attempts timed out; a successful event arrived after 7.144 seconds in the recorded session. Subsequent connections with the same client ID obtained getters without another button press. Persistence across speaker power cycles is untested. The client-ID bytes remain private in `work/`.

[The captured session](../evidence/ble-firmware-confirmed.json) establishes:

| Feature | Discovered index | Version | Result |
| --- | --- | --- | --- |
| DeviceInfo `0x0100` | 2 | 2 | Firmware payload `03 04 00 d8` = **3.4.216** |
| OTA `0x0403` | 17 | 3 | Status 5 (`CHECK_FAIL`), parameter `0x01000000`; install mode 0 (`AUTOMATIC`) |
| FileSystem `0x0105` | 5 | 3 | Log 66 SIZE initially returns an empty ACK, followed by event 3 carrying BE32 size and BE32 CRC |

The current OTA state was read; no new check or installation was initiated. The parameter's meaning is status-dependent and has not been independently established here.

A further capture established a **193,321-byte** size for diagnostic log 66 and a successful 64-byte `OPEN(READ) → READ → CLOSE` sample. The original Android synchronous SIZE parser did not handle the version-3 event, so the probe was adapted to the observed event rather than guessing another file number or function. The full log was then acquired and the file closed. Its standard CRC-32 is `69a38792`; the device supplied `965c786d`, the exact bitwise inverse. SHA-256: `063130a23cc81e79b1eb56e88d62ecabe1441b391b4d978b62e31fdbe19026cd`. The raw log stays private in `work/`; [the public summary](../evidence/speaker-log-summary.json) and [read-session metadata](../evidence/ble-log-read.json) exclude its contents.

The log contains historical `MAN-ZSPEAKER` crash reports for 3.2.200, 3.3.213, and 3.4.216. It records an attempted update from **3.3.213 to 3.4.216** at device-logged timestamp **2020-10-11T13:26:05.026248Z**, followed by `OTA Update Successful`. This is this unit's recorded history, not a verified worldwide release date. No manufacturer firmware URL or domain was found in this log. Some HTTP responses and authentication-related content are present, so the log was not included in deliverables.

[Feature discovery](../evidence/ble-feature-inventory.json) returned 24 entries, all corresponding to names in the companion app. No additional feature explicitly identified as raw flash or memory access appeared in that table. This does not rule out undocumented functions, files, or USB commands. DeviceInfo hardware payload `05 01 01 0b 00 00 00 00` decodes to model ID 5, hardware revision 1, color `0x010b`, SKU attributes 0. An empty-payload Root GET_PROTOCOL_VERSION returned error 3 (`INVALID_SIZE`); that one definition is not compatible with this root version 2 as sent, and was not retried with guessed parameters.
