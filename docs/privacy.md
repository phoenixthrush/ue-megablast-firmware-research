# Publication boundary

This archive was assembled from an explicit allowlist rather than by adding the original workspace wholesale.

## Excluded material

| Material | Why excluded | Preserved replacement |
| --- | --- | --- |
| Raw log 66 and its sample payloads | Authentication-related values, identifiers, HTTP cookies, and memory/stack dump content may expose secrets, including when hex encoded | Size, hashes, CRC comparison, redacted event summary, control-packet metadata |
| Six-byte authorization client ID | Controller identity used for live authorization | Handshake shape, approval/confirmation history, result status |
| Device Bluetooth UUID, serial/MAC/BSSID, speaker name, LAN/VPN addresses | Identifies the owner's device or network | Placeholders, documentation-reserved IP examples, descriptor-only USB output |
| Full IORegistry and broad host/HID output | Contains unrelated connected-device and host information | Relevant MEGABLAST descriptor fields |
| Original APK and complete decompilation | Includes embedded API keys/analytics identifiers and unrelated third-party code | Download URL/hash, certificate metadata, selected reviewed protocol sources |
| Original DMG/MSI/EXE, NW.js SDK, JRE and JADX archives | Large third-party installations; originals should be acquired from their recorded sources | URLs, observed versions/sizes/hashes, package inventories |
| Raw HTTP cookie/request-ID headers | Transient values have no bearing on the findings | Status, relevant public headers, endpoint result |
| Browser profiles, cookies, downloads, user home files, environment credentials, and chat/tool session IDs | Private or unrelated to the reproducible findings | No replacement needed |
| Personal profile/AGENTS material outside the device investigation | Unrelated to the research | Not included |

Selected `BuildConfig.java` vendor sender/analytics identifiers are explicitly redacted. Public protocol UUIDs, model IDs, firmware versions, source hashes, known manufacturer URLs, and synthetic test bytes are retained.

`evidence/attempts/log-*-capture.json` contains derived metadata with raw payload fields replaced by length/redaction markers. The original control requests and non-sensitive replies remain where useful. The REA export has local paths replaced; its original evidence IDs identify the original analysis, not a hash of the redacted JSON.

`${WORKSPACE}`, `${HOME}`, `@REPLAY_ROOT@`, `@RUNTIME_ROOT@`, `@PRIVATE_TMP@`, `<USER>`, and other marked values are substitutions, not literal owner paths. `192.0.2.0/24` addresses are examples, not the original LAN. Historical sandbox profiles are `.template` files and cannot be run verbatim without replacing their paths and reassessing their boundaries.

## Generated files

The Git ignore rules exclude `private/`, downloads, browser/runtime profiles, binary captures, credentials, and caches. This protects against accidental additions; it is not a substitute for reviewing a diff. Probe-generated log and client-ID files should stay in `private/` and must never be force-added.

The initial archive is checked using Gitleaks with default rules and redacted findings, plus explicit checks for original private identifiers, local paths, and decoded/hex-encoded sensitive values. No broad allowlist is used to hide scanner findings. Results and limits are recorded in [verification.md](verification.md).

This preserves the material needed to understand the investigation without publishing the excluded private data. It does not claim that an automated scanner can prove the absence of every possible secret in arbitrary future files.
