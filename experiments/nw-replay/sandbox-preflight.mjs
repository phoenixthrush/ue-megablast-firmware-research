import { readFileSync, writeFileSync } from 'node:fs';
import { dirname, join } from 'node:path';
import { fileURLToPath } from 'node:url';
import { createConnection } from 'node:net';
import { spawnSync } from 'node:child_process';

// This is authored test code. It does not load any vendor application or cache.
const fixturePath = fileURLToPath(import.meta.url);
const fixtureRoot = dirname(fixturePath);
const outputRoot = join(fixtureRoot, 'output');
const results = { node: process.version, fixture: fixturePath, checks: {} };
const isPermissionDenial = (error) => ['EPERM', 'EACCES'].includes(error?.code);
const record = (name, success, details) => {
  results.checks[name] = { success, ...details };
};

try {
  const ownBytes = readFileSync(fixturePath);
  record('own_fixture_read', ownBytes.length > 0, { bytes: ownBytes.length });
} catch (error) {
  record('own_fixture_read', false, { code: error.code });
}

try {
  writeFileSync(join(outputRoot, 'allowed-write.txt'), 'authored fixture output\n');
  record('output_write', true, {});
} catch (error) {
  record('output_write', false, { code: error.code });
}

try {
  // Intentionally do not display or persist the contents if isolation fails.
  readFileSync('${HOME}/.zshrc');
  record('home_read_denied', false, { outcome: 'unexpectedly allowed' });
} catch (error) {
  record('home_read_denied', isPermissionDenial(error), { code: error.code });
}

try {
  writeFileSync(join(fixtureRoot, 'should-not-write.txt'), 'unexpected write\n');
  record('outside_output_write_denied', false, { outcome: 'unexpectedly allowed' });
} catch (error) {
  record('outside_output_write_denied', isPermissionDenial(error), { code: error.code });
}

const child = spawnSync('/bin/echo', ['sandbox-preflight-child'], { encoding: 'utf8' });
record('process_spawn_denied', isPermissionDenial(child.error), {
  code: child.error?.code ?? null,
  status: child.status,
});

await new Promise((resolve) => {
  const socket = createConnection({ host: '127.0.0.1', port: 9 });
  let finished = false;
  const finish = (success, details) => {
    if (finished) return;
    finished = true;
    record('loopback_network_denied', success, details);
    socket.destroy();
    resolve();
  };
  socket.once('error', (error) => finish(isPermissionDenial(error), { code: error.code }));
  socket.once('connect', () => finish(false, { outcome: 'unexpectedly connected' }));
  socket.setTimeout(2000, () => finish(false, { outcome: 'timeout is not proof of denial' }));
});

results.passed = Object.values(results.checks).every((check) => check.success);
const serialized = JSON.stringify(results, null, 2) + '\n';
writeFileSync(join(outputRoot, 'sandbox-preflight-result.json'), serialized);
process.stdout.write(serialized);
process.exitCode = results.passed ? 0 : 1;
