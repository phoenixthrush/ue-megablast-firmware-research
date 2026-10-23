# Sources and artifact identity

Availability results below were observed on **23 October 2026**. They are not a
promise that an endpoint will remain available. Original binaries are excluded.

## Acquired inputs

| Input | Source | Observed SHA-256 |
| --- | --- | --- |
| Mac diagnostic DMG; 96,234,145 bytes | [Official download](https://files.logitech.com/audio/mobileApps/resources/tools/BLAST_and_MEGABLAS_%20Diagnostic_Tool.dmg) | `00158207751ab1257f4b8c0f2040782f4e26be1267c2657f68c2aa950d0d6423` |
| Windows diagnostic MSI; 66,347,008 bytes | [Official download](https://files.logitech.com/audio/mobileApps/resources/tools/BLAST_%26_MEGABLAST_Diagnostic_Tool.msi) | `6b742031e330ce7c9e416d108fdd31f2e6c0f8315d152b00671fcab494f5c253` |
| Android companion 2.2.121; 25,215,524 bytes | [Mirror listing](https://www.apkmirror.com/apk/logitech-europe-s-a/blast-megablast-by-ultimate-ears/blast-megablast-by-ultimate-ears-2-2-121-release/blast-megablast-by-ultimate-ears-2-2-121-android-apk-download/) | `83a0fe9b01a8e82b9ac91ac3672d9654b25d4c2f59048fcf6759028a577c0396` |
| NW.js SDK 0.31.1 macOS x64; 98,615,988 bytes | [Official download](https://dl.nwjs.io/v0.31.1/nwjs-sdk-v0.31.1-osx-x64.zip) | `58921e3a2475741e6de38c359f0c645ffc03c91811df4cfde550068b8ca804fc` |

The DMG name really contains `MEGABLAS` without the final T and an encoded space.
The APK signer certificate fingerprint observed in the file was
`4280fe1b2225084a160045067dff31b6d05836e5809eb1905c5b548f7b5f8713`.
APK hash/certificate matched mirror metadata, rather than an independent copy of
the owner's iPhone application. `jarsigner` reported verification with self-signed,
timestamp, and ZIP/JAR interpretation warnings.

Additional input identities, including the two PDFs, JADX archive, and unused
portable JRE, are recorded in [the input manifest](../evidence/input-artifacts.json).
They are not firmware images. JADX used the existing Homebrew JDK; the additionally
downloaded portable JRE was unused.

## Manufacturer documentation and services

- [Diagnostic FAQ PDF](https://m.media-amazon.com/images/I/A1KtnBF%2BupS.pdf): original tool-link annotations, page 1.
- [Update Assistant FAQ PDF](https://images-eu.ssl-images-amazon.com/images/I/A1VznU2mAOS.pdf): updater links, pages 1 and 3.
- [Official companion-app support entry](https://support.logi.com/hc/en-ch/articles/25360486357399-UE-BLAST-MEGABLAST): identifies the Android app package.
- [Service-locator XML](https://files.logitech.com/audio/mobileApps/resources/UEServiceLocator.xml): preserved in `analysis/manufacturer/`; its historical firmware/checker entries do not establish the present MEGABLAST update protocol.
- Mac [Update Assistant URL](https://download01.logitech.com/web/ftp/pub/ue/UltimateEars_UpdateAssistant.dmg) and Windows [URL](https://download01.logitech.com/web/ftp/pub/ue/UltimateEars_UpdateAssistant.exe): HEAD returned 404.
- `https://files.logitech.com/audio/mobileApps/resources/getFWUpdateInfo.php`: returned 403.
- `https://uemobileservices.logitech.com/` and `/ue/speaker`: unauthenticated reads returned `{}`. No firmware returned.

## Hardware and original technical references

- [EDN original MEGABLAST teardown](https://www.edn.com/teardown-megablast-speaker-leads-voice-controlled-charge-at-ces-2018/): CYW43907, CYW40707, DBMD5, MX25L25645G in the teardown specimen. Owner's PCB was not opened.
- [Infineon CYW43907 interface guide](https://community.infineon.com/t5/Knowledge-Base-Articles/CYW43907-Product-Guide-Hardware-Interfaces/ta-p/246294): chip-family boot/debug/interface capabilities, not proof of retail debug access.
- [Spotify ZeroConf guide](https://developer.spotify.com/documentation/commercial-hardware/implementation/guides/zeroconf) and [FAQ](https://developer.spotify.com/documentation/commercial-hardware/implementation/faqs): SDK/API identification boundaries.
- [JADX 1.5.6](https://github.com/skylot/jadx/releases/tag/v1.5.6): standalone decompiler used; 16 reported errors retained.
- [Bleak macOS documentation](https://bleak.readthedocs.io/en/latest/backends/macos.html): CoreBluetooth permission behavior and notification discriminator used to fix long-read ambiguity. The observed release was 3.0.2.
- [NW.js 0.31.1 release](https://nwjs.io/blog/v0.31.1/): runtime/Chromium version context.

## Desktop decoder and replay research

- [ghidra_nodejs](https://github.com/PositiveTechnologies/ghidra_nodejs): documented V8 version differs from target.
- [Theia](https://github.com/Llamaware/Theia): older NW/V8 and x86 assumptions differ from target.
- [View8](https://github.com/suleram/View8): inspected supplied versions did not establish V8 6.7 support.
- [NW31 evalNWBin implementation](https://github.com/nwjs/nw.js/blob/nw31/src/nw_custom_bindings.cc#L277) and [ArrayBuffer wrapper](https://github.com/nwjs/nw.js/blob/nw31/src/resources/api_nw_window.js#L435): proposed mock harness, not proof of replay success.
- [NW31 cache-loader sanity behavior](https://github.com/nwjs/v8/blob/nw31/src/snapshot/code-serializer.cc#L470).
- [V8 6.7 interpreter bytecode printing](https://github.com/v8/v8/blob/6.7.288.44/src/interpreter/interpreter.cc#L196) and [debug intrinsics](https://github.com/v8/v8/blob/6.7.288.44/src/runtime/runtime-test.cc#L675): limits of a stock compile-only shortcut.

The initial handoff also referenced BLUFFS, evaluation-board JTAG/SWD/UART
documentation, and possible physical SPI readout. Those were background leads;
none was demonstrated as a firmware-extraction or modification route on this unit.
They are preserved with that status in the original handoff.

## Archive identity

`evidence/archive-source-manifest.json` records hashes of originals used when
assembling this redacted archive. Redacted or reformatted copies have different
bytes. `evidence/archive-files.sha256` separately identifies the publication
snapshot, excluding itself and Git/cache/private files.
Historical analysis hashes do not make the transformed copies byte-identical to
the originals.
