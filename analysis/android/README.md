# Selected Android analysis artifacts

Input: `com.logitech.newjackcity` **2.2.121**, APK SHA-256
`83a0fe9b01a8e82b9ac91ac3672d9654b25d4c2f59048fcf6759028a577c0396`.

These selected reconstructed Java files support the protocol findings. They are
not a complete source project and are not expected to compile here. Full JADX
output included 6,597 classes and 16 errors; relevant inspected methods had
complete bodies. `evidence/attempts/android-decompile.log` preserves that result.

Original file hashes are recorded in `evidence/android-analysis-manifest.json`
and `evidence/archive-source-manifest.json` at the repository root. Hashes refer
to original analysis inputs; BuildConfig vendor identifiers and local paths may
have been redacted in the archived copies. No embedded asset API key is included.

The APK was never installed or executed, and the full app was never connected to
the speaker. The minimal Python probe was used instead, because the full app's
connect flow also sets the device's OTA hostname.

See [the protocol notes](../../docs/protocol.md) and [provenance](../../NOTICE.md).
