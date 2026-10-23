# Archive verification

Checked on **23 October 2026**. These checks validate the redacted archive. They
do not repeat or extend the historical speaker experiments.

| Check | Result |
| --- | --- |
| `ruff format .` | Completed; two newly added Python files formatted, remaining files unchanged |
| `ruff check .` | Passed with Ruff 0.17.2 |
| `python -m pytest` | **24 passed** with Python 3.12.14 and pytest 9.1.1 |
| `node --check` on both authored JavaScript files | Passed with Node 26.12.1; syntax only |
| JSON and XML parsing | All included JSON/XML parsed successfully |
| Relative Markdown links | All local targets resolve |
| File-type review | No symlinks, binary captures, original application packages, private log, or client-ID file included |
| Gitleaks directory scan | No leaks found with 8.30.1 default rules, decoding enabled, output redacted; no custom allowlist |
| Targeted private-data comparison | No matches for 27 values from private controller-ID, APK key, vendor identifiers, IORegistry/log identifiers, owner paths/names, and session identifiers, including literal, hex, spaced hex, and base64 forms |
| Original/publication hash separation | Original-input manifests retained; `evidence/archive-files.sha256` hashes the redacted publication files separately |

The metadata collector and its six parser tests were added while assembling the
archive. Eighteen tests cover the canonical BLE probe, including framing,
authorization errors, response matching, version decoding, long-response notice
classification, and file-response length handling. The metadata parser test
decodes the captured feature pages and compares all 24 entries with the recorded
inventory. No live device commands were sent during packaging.

Gitleaks was acquired from its official v8.30.1 GitHub release, and its downloaded
Darwin ARM64 archive matched the release checksum
`b40ab0ae55c505963e365f271a8d3846efbc170aa17f2607f13df610a9aeb6a5`.
The scanner and reports are not bundled in this repository. Private comparison
inputs and the checker containing owner-specific comparison values also remain
outside the archive. Reproducing those exact comparisons requires the private
originals; they cannot be supplied without undoing the redactions.

`git diff --cached --check` reports trailing whitespace in original XML, string
extractions, package listings/extraction logs, and HTTP headers. Those evidence
bytes are retained rather than silently normalized; source hashes distinguish
any other redactions or transformations. Authored Python, tests, and documentation
were reviewed separately.

Historical limits remain: JADX reported 16 decompilation errors; the extracted
Mac app failed signature verification; the fresh SDK was unsigned; the NW page
never produced a successful harness result; no vendor cache execution or firmware
image acquisition occurred. The earlier six-check Node sandbox result applies
only to its original profile and environment.

Secret scanning supplements the explicit exclusions and manual review described
in [privacy.md](privacy.md); it cannot prove that arbitrary future additions are
free of secrets. Keep future diagnostic captures in the ignored `private/` folder.
