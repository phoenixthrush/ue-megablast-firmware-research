# MEGABLAST Centurion protocol notes

Source: offline decompilation of Android app `com.logitech.newjackcity` 2.2.121. APK SHA-256: `83a0fe9b01a8e82b9ac91ac3672d9654b25d4c2f59048fcf6759028a577c0396`.

These definitions were recovered statically from the app, followed by live BLE tests on the owner's speaker. The firmware getter **confirmed 3.4.216**. The APK itself was never installed or executed; a separate minimal Python probe sent only the explicitly approved authorization handshake and diagnostic reads.

## Inner message layout

```text
feature_index_u8 | (function_id << 4) OR software_id | payload
```

`software_id` ranges from 1 to 15. A feature index is discovered dynamically and is not interchangeable with a 16-bit feature ID. Replies must be matched against feature, function, and software ID. An error reply begins with `ff`, followed by the original feature index and function/software-ID byte; the application defines `NOT_AUTHORIZED = 14`.

Static source references, relative to `analysis/android/sources/`:

- `com/logitech/ue/centurion/device/CenturionMessage.java:37`
- `com/logitech/ue/centurion/conductor/CenturionConductor.java:212`
- `com/logitech/ue/centurion/utils/CenturionUtils.java:26`
- `com/logitech/ue/centurion/device/CenturionError.java:14`

## Read-only firmware getter

Root feature index is initially zero. Root function 0 looks up a feature using its big-endian 16-bit ID. For DeviceInfo, the inner request is:

```text
00 | 0s | 01 00
```

Here `s` is the software ID. The lookup response payload supplies feature index, type, and version. After validating that reply, DeviceInfo feature **0x0100**, function **1**, takes an empty payload:

```text
discovered_DeviceInfo_index | (10 OR software_id)
```

Firmware-version response payload:

```text
major_u8 | minor_u8 | build_u16_big_endian
```

The actual speaker returned `03 04 00 d8`, confirming `3.4.216`. Its DeviceInfo index was 2, feature version 2. These bytes describe the payload, not a complete HID report.

References:

- `com/logitech/ue/centurion/device/CenturionFeatureMapper.java:55`
- `com/logitech/ue/centurion/device/GenericDevice.java:52`
- `com/logitech/ue/centurion/feature/Features.java:97`
- `com/logitech/ue/centurion/device/GenericDevice.java:163`
- `com/logitech/ue/centurion/devicedata/parcel/VersionResponseParcel.java:10`

## Numbered-file read interface

`iFileSystem` is feature **0x0105**. Its index also requires discovery. The minimum feature version for the functions below is 1.

| Function | Function ID | Request payload |
| --- | --- | --- |
| OPEN for reading | 0 | file number BE16, access byte `01` |
| READ | 1 | file number BE16, requested length BE16 |
| CLOSE | 3 | file number BE16 |
| SIZE | 5 | file number BE16 |

The class also defines WRITE=2 and ERASE=4. They were not invoked. READ responses begin with a returned-length BE16, followed by that many bytes.

The demonstrated app workflow is `SIZE → OPEN(READ) → READ` in chunks no larger than **4000 bytes** `→ CLOSE`. Its diagnostic reader uses **file 66 / 0x0042**, explicitly described as the speaker log. No directory listing, path API, seek operation, firmware-image file number, or unrestricted flash read is established by this inspection. Do not enumerate guessed file numbers on the speaker.

**Live version-3 difference:** this speaker's FileSystem index is 5, version 3. SIZE replies with an empty matching acknowledgement and subsequently emits feature event 3 (`index | 30`, SWID 0). That event's payload is BE32 size followed by BE32 CRC. One observed payload was `00 02 f3 29 96 5c 78 6d` (193,321 bytes). A 64-byte log read succeeded using the original BE16-length-plus-data response layout. No such event-3 definition appears in the inspected Android enum; this is runtime evidence for this firmware, not an inferred universal protocol rule.

The entire 193,321-byte log was subsequently read and closed. Its standard CRC-32 `69a38792` is the bitwise inverse of the reported `965c786d`. SHA-256 and content-free metadata are in [the log summary](../evidence/speaker-log-summary.json); the private raw log is not a firmware image. Historical records identify `MAN-ZSPEAKER` builds and a 3.3.213 → 3.4.216 update, but no firmware download URL.

For long BLE replies, the notification carries BE16 buffer length, then the long-response characteristic is read and truncated to that length before CPL decoding. Actual GATT reads returned a 512-byte buffer. On macOS, use `cb={"notification_discriminator": ...}` to classify two-byte notifications separately from those full reads; otherwise a notice arriving during a read can be misclassified as its result. This was observed during a failed full-log attempt and corrected before successful acquisition.

References:

- `com/logitech/ue/centurion/feature/Features.java:775`
- `com/logitech/ue/centurion/device/GenericDevice.java:700`
- `com/logitech/ue/centurion/device/FileAccess.java:4`
- `com/logitech/ue/centurion/devicedata/FileReadData.java:13`
- `com/logitech/logiux/newjackcity/utils/SpeakerFileReader.java:26`
- `com/logitech/logiux/newjackcity/presenter/impl/DevOptionsPresenter.java:150`

## Transport boundaries

- BLE primary service: `7FD36E38-4680-4A77-BEA3-BEEAEECBF592`.
- BLE command/notification characteristic: `748E2216-8B9A-4598-A0C6-2DEE1AF454DB`.
- A short BLE message prepends one CPL byte `00` to the inner message. Longer messages use CPL segmentation.
- SPP adds a framing header, length, and CRC16MCRF4XX to CPL messages.
- Wi-Fi transport uses UDP CPL segments and listens for discovery on port **26756**. Its discovered-device description says **Emulator**; retail MEGABLAST support for this UDP transport has not been observed.
- The APK does not implement USB. The actual speaker's `046d:0a6a` HID interface and 64-byte reports do not establish how these inner messages should be wrapped for USB.
- The app contains authorization and remote-power operations. The owner subsequently approved the normal seven-byte authorization handshake and physically confirmed it. No remote-power command was performed. Unauthorized getters initially returned error 14; later authorized getters succeeded. Do not substitute guessed authorization operations or assume that button confirmation events identify a specific client.

References:

- `com/logitech/ue/centurion/ble/BLEConnection.java`
- `com/logitech/ue/centurion/connection/CPLConnection.java:18`
- `com/logitech/ue/centurion/spp/CenturionSPPConnection.java:88`
- `com/logitech/ue/centurion/wifi/WiFiConnection.java`
- `com/logitech/ue/centurion/wifi/WiFiDiscoverer.java:5`

## OTA implications

The production hostname declared in `BuildConfig.java:21` is `uemobileservices.logitech.com`. `OTAServer.java` also declares test and certification choices. These choices establish that the app can select an update hostname; they do not establish that an arbitrary image will be accepted.

`GenericDevice.java` separates OTA status reads from state-changing operations: initiate a check, change install mode, install, change hostname, and disable OTA. None of these state-changing operations was invoked.

The live OTA index was 17, feature version 3. Raw GET_OTA_STATUS (function 0) returned status 5 (`CHECK_FAIL`) and parameter `0x01000000`. GET_OTA_INSTALL_MODE (function 2) returned 0 (`AUTOMATIC`). The status getter returns five payload bytes: status followed by BE32 parameter. It does not initiate an update check. The app's higher-level settings presenter may initiate a check in response to status 9; that presenter was not executed.

`OTAValidationError.java` defines size, magic-string, platform, component-count, header/component CRC, and authentication failures. The authentication failure is code 9. This is an app-side error vocabulary; determining actual signature verification, keys, encryption, or secure boot requires the firmware itself or stronger runtime evidence.

No matching firmware payload or manifest for 3.4.216 has been acquired. The authorized BLE log read succeeded; the acquired log contained historical update records but no firmware payload URL.

## Reproduction and limits

The public APK was downloaded through APKMirror's normal browser download. Its file hash and embedded certificate fingerprint matched the mirror's metadata. JADX 1.5.6 decoded it with the existing Homebrew `openjdk@25`. The full pass reported 16 errors elsewhere; the getter, packet builder, feature definitions, and log-reader methods inspected for these notes have complete decompiled bodies. Decompiled Java is a reconstruction, not original source or proof that a particular operation succeeds on firmware 3.4.216.

Relevant decoded-source hashes are recorded in [the Android evidence manifest](../evidence/android-analysis-manifest.json). Selected reconstructed sources are included under `analysis/android/sources/`; originals and the complete local decompilation are excluded from this archive.
