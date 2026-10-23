# Historical experiment files

These are the authored files from an unfinished attempt to inspect the legacy
desktop tool. Read [the outcome and limits](../../docs/nwjs-replay.md) first.

- `sandbox-preflight.mjs` was the independent Node fixture; its six checks passed
  in the original environment. The archived copy has its private home path
  replaced with the literal `${HOME}` placeholder and has not been rerun.
- `harness/` is the authored NW.js page. `mode.json` selects `preflight`; no vendor
  cache files are included. NW.js failed during startup before producing a page
  result. The fake-HID/cache-loading branch was never executed.
- The `.sb.template` files preserve the attempted profiles with redacted paths.
  `@REPLAY_ROOT@`, `@RUNTIME_ROOT@`, `@PRIVATE_TMP@`, and `${HOME}` must be replaced
  deliberately before any reproduction. Replacing placeholders does not establish
  that the profiles are complete or provide safe isolation.

Only JavaScript syntax checks were repeated while assembling this archive. No
runtime, vendor code, network experiment, or speaker operation was rerun.
