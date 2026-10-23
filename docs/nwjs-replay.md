# Unfinished desktop-tool replay

**Result: no USB packets recovered; no vendor bytecode executed.** This avenue was stopped after it consumed disproportionate effort relative to the successful BLE work.

The original diagnostic app uses NW.js 0.31.1 and V8 6.7.288.44. Its manifest/HTML are readable, while `gui.bin`, `app/njch.bin`, and `app/njcc.bin` are compiled V8 caches. String offsets identify getters and filesystem helpers, but not their packet values.

The cache header begins `a9 03 de c0 ef 32 8a 6d`. Observed cache magic is `0xC0DE03A9` and V8 version hash `0x6d8a32ef`. These observations, plus embedded runtime strings, identify a decoder compatibility problem; they do not decode the cache. Theia/ghidra_nodejs/View8 checks did not establish a compatible decoder.

## Proposed experiment

Use a fresh matching runtime and a new page, preload only the two module caches, replace target-visible HID/filesystem/module access with mocks, and inspect exported globals. A fake HID write records an array then throws a sentinel. Do not load the original GUI: it owns automatic discovery and reset/boot-mode controls.

`evalNWBin` executes deserialized scripts. It is not a compile-only inspection API. Stock bytecode-print flags do not establish disassembly of every already-cached function. The relevant upstream references are in [sources.md](sources.md).

The authored harness, fixture, and profile templates are preserved under [experiments/nw-replay](../experiments/nw-replay/). Paths were redacted for the archive. The default mode remains **preflight**, and no vendor input files are included. These experimental files are not a finished or portable isolation product.

## What actually ran

1. An independently authored Node 26.12.1 fixture ran under `sandbox-exec` with a deny-default profile. Six checks passed: own-file read/output write allowed; private-home read, outside-output write, unrelated program spawn, and loopback TCP denied with `EPERM`.
2. A separately adapted profile attempted to start the official, unsigned 2018 x64 NW.js SDK under Rosetta. The original Logitech app was never launched or re-signed.
3. Startup failures were diagnosed before the page: system dyld-cache directory reads, Rosetta metadata, OS random source, bundled crash-helper execution, resource extension, and profile IPC/socket handling.
4. Later rules kept IP networking denied by default, explicitly denied IOKit, allowed only task-private Unix sockets, restricted program execution to named runtime helpers, and confined writes to scratch/profile/output paths. These changes were not equivalent to a successful runtime isolation validation.
5. The final attempt exited **21**, reporting failure to create its ProcessSingleton socket directory. No `harness.json` result was produced. The authored page did not reach its preflight success branch, and no cache-load phase ran.

The initial Node preflight passing does **not** prove that the more permissive NW-specific profile is safe or complete. The templates intentionally retain that limitation. There was no blanket `allow default`, `--no-sandbox`, code-signature bypass, USB access, or vendor-cache execution.

## Relevant inputs

- Official SDK URL: `https://dl.nwjs.io/v0.31.1/nwjs-sdk-v0.31.1-osx-x64.zip` (redirected to `dl.node-webkit.org`).
- Observed bytes: **98,615,988**.
- Observed SHA-256: `58921e3a2475741e6de38c359f0c645ffc03c91811df4cfde550068b8ca804fc`.
- Published `SHASUMS256.txt` retrieved at that directory listed only the headers archive, not this SDK. Therefore the SDK hash above is locally measured, not independently matched to a published SDK checksum.
- `codesign` reported the pristine SDK app was **not signed**. The extracted Logitech app had separately reported an invalid signature. Those are different observations.

The experiment did not produce a firmware image or a new device-access capability.
