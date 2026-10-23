# Commands and reproduction

Commands below describe the research workflow. Outputs, errors, and success limits
are preserved in the findings/timeline and evidence. Paths are generic. These
instructions do not automatically run manufacturer applications or update a speaker.

## USB on the actual Mac

```sh
ioreg -p IOUSB -n MEGABLAST -r -l -w 0
ioreg -r -n MEGABLAST -l -w 0
```

The second view exposed the HID child properties and report descriptor. Raw host
output is excluded because it contained identifiers and unrelated devices. The
redacted result is [retained here](../evidence/megablast-usb-descriptors.txt).

`lsusb` was not installed on this Mac. On Linux, `lsusb` and `lsusb -v -d 046d:0a6a`
are equivalent enumeration leads, not commands run successfully in this session.
`hidutil list` with a product match unexpectedly listed unrelated devices and was
not used as evidence of exclusive device filtering.

## Bonjour and ZeroConf

```sh
dns-sd -B _spotify-connect._tcp local.
dns-sd -L '<advertised-service>' _spotify-connect._tcp local.
dns-sd -G v4 '<advertised-host>.local.'
```

These browse commands remain running until interrupted. The observed service was
on port 9595, path `/zc/0`. Device name/address are redacted.

```sh
curl --noproxy '*' --connect-timeout 3 --max-time 8 \
  'http://192.0.2.10:9595/zc/0?action=getInfo' \
  | jq '{version,libraryVersion,resolverVersion,brandDisplayName,modelDisplayName}'
```

Replace the documentation-only address with the actual advertised address. The
owner's Terminal call succeeded; agent calls failed. `route -n get`, `ifconfig`,
and `arp -n` helped identify VPN routing, but raw network/host identifiers are
excluded. A trial `curl --interface en0` did not resolve the agent restriction.
A small `nc -G 2 -vz` check stopped at its first route error; it was not a complete
port scan. No SSH/Telnet access was established.

## Manufacturer packages

Download privately from the exact URLs/hashes in [sources.md](sources.md).

```sh
shasum -a 256 '<downloaded-file>'
7zz l '<diagnostic.dmg>'
7zz x '<diagnostic.dmg>' -o'<new-extraction-directory>'
7zz l '<diagnostic.msi>'
7zz x '<diagnostic.msi>' -o'<new-msi-directory>'
7zz x '<extracted-disk1.cab>' -o'<new-cab-directory>'
7zz l '<extracted-diagnostic.exe>'
7zz x '<extracted-diagnostic.exe>' -o'<new-app-directory>'
codesign --verify --deep --strict '<extracted-app.app>'
```

The Windows EXE had an appended ZIP. Extracted Mac signature verification failed;
no re-signing or security exception followed. The native read-only DMG mount was
canceled at its EULA. No original diagnostic application was launched.

`strings -a -t x` provided the archived ASCII string offsets. The REA analysis
selected `package.json` and `index.html`; it inventoried more material but did not
decode the compiled caches. See its retained limitations in the JSON export.

## Android protocol analysis

The public APK was obtained through the normal mirror browser download, preserved
privately, and hashed. Java's JAR verification checked the JAR signature with
warnings; Android v2/v3 signature validation was not performed.

```sh
shasum -a 256 '<companion.apk>'
/opt/homebrew/opt/openjdk@25/bin/java -version
/opt/homebrew/opt/openjdk@25/bin/jarsigner -verify '<companion.apk>'
/opt/homebrew/opt/openjdk@25/bin/java -Xmx2g \
  -cp '<jadx-1.5.6>/lib/*' jadx.cli.JadxCLI \
  --config none -d '<new-decompilation-directory>' '<companion.apk>'
```

The direct Homebrew Java path worked, while `/usr/bin/java` did not locate it.
Inspect getters, transport framing, feature IDs/versions, parcel decoders,
authorization flow, and the app's `SpeakerFileReader(66)` call. Do not run the full
app against the speaker merely to obtain getters: its connect flow also writes
the selected OTA hostname.

The selected reconstructed sources are in `analysis/android/sources/`. Original
hashes and the JADX error log support reproducing the bounded analysis.

## Successful BLE workflow

Follow the root README's environment setup, then use `megablast_probe.py`.
Its default operation does not authorize a client. Explicit `--authorize` uses
the normal app handshake; physical confirmation may be needed. `--log-file`
reads only documented log 66, limits the size to one MiB, and closes it.

For a reconstructed metadata-only equivalent of the inventory experiment:

```sh
mkdir -p private
.venv/bin/python collect_metadata.py \
  --authorize --client-id-file private/client-id.bin \
  --evidence private/metadata.json
```

That archived convenience script reuses the tested protocol code and has offline
parser tests. It was reconstructed for the archive and was not separately rerun
against the speaker during packaging. The original live inventory is retained
in [the evidence](../evidence/ble-feature-inventory.json).

Early command variants and their failures are recorded in `evidence/attempts/`:
normal subscription failure, authorization denial/timeouts, unacknowledged-write
timeout, empty SIZE ACK, and long-read race. The canonical probe incorporates the
subsequent observed fixes. No device commands were rerun just to build this archive.

## Offline legacy runtime work

See [nwjs-replay.md](nwjs-replay.md). The templates are historical experiments,
not a working standalone execution recipe. Only the authored Node fixture passed.
The NW application page never reached success and no vendor cache was loaded.

## Archive validation

```sh
.venv/bin/ruff format .
.venv/bin/ruff check .
.venv/bin/pytest
node --check experiments/nw-replay/harness/harness.js
node --check experiments/nw-replay/sandbox-preflight.mjs
gitleaks dir . --redact --no-banner
```

The JavaScript checks validate syntax only; they do not launch NW.js, load vendor
code, or validate a sandbox. Gitleaks checks current files; the private-data review
and exclusions remain necessary for hex payloads, identifiers, and binary captures.
