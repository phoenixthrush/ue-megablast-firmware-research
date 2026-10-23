(function () {
  'use strict';
  const nativeRequire = window.require;
  const nativeFs = nativeRequire('fs');
  const nativePath = nativeRequire('path');
  const nativeProcess = window.process;
  const nativeNW = window.nw;
  const quit = nativeNW.App.quit.bind(nativeNW.App);
  const nativeTimeout = window.setTimeout.bind(window);
  const directory = '@REPLAY_ROOT@/harness';
  const output = nativePath.resolve(directory, '../output/harness.json');
  const result = {versions: nativeProcess.versions, writes: [], constructors: []};
  function finish(error) {
    if (error) result.error = String(error.stack || error);
    nativeFs.writeFileSync(output, JSON.stringify(result, null, 2));
    quit();
  }
  nativeTimeout(function () { finish(new Error('Harness deadline')); }, 5000);
  try {
    const mode = JSON.parse(nativeFs.readFileSync(nativePath.join(directory, 'mode.json'), 'utf8'));
    result.phase = mode.phase;
    if (mode.phase === 'preflight') {
      result.authored_fixture_only = true;
      finish();
      return;
    }
    if (mode.phase !== 'load') throw new Error('Unknown harness phase');
    const caches = ['njch.bin', 'njcc.bin'].map(function (name) {
      const buffer = nativeFs.readFileSync(nativePath.join(directory, 'inputs', name));
      return buffer.buffer.slice(buffer.byteOffset, buffer.byteOffset + buffer.byteLength);
    });
    const evalCache = nativeNW.Window.get().evalNWBin.bind(nativeNW.Window.get());
    class FakeHID {
      constructor() { result.constructors.push(Array.from(arguments)); }
      write(data) {
        result.writes.push(Array.from(data));
        throw new Error('FAKE_HID_WRITE_CAPTURED');
      }
      readTimeout() { throw new Error('FAKE_HID_READ_BLOCKED'); }
      read() { throw new Error('FAKE_HID_READ_BLOCKED'); }
      close() {}
      on() { return this; }
      setNonBlocking() {}
    }
    const fakeHID = Object.freeze({
      HID: FakeHID,
      devices: function () {
        return [{vendorId: 1133, productId: 2666, path: 'FAKE_MEGABLAST', product: 'MEGABLAST'}];
      }
    });
    const fakeFs = new Proxy({}, {get: function (_, name) {
      return function () { throw new Error('TARGET_FS_BLOCKED:' + String(name)); };
    }});
    window.require = function (name) {
      if (name === 'node-hid') return fakeHID;
      if (name === 'fs') return fakeFs;
      throw new Error('TARGET_REQUIRE_BLOCKED:' + name);
    };
    window.process = Object.freeze({argv: [], env: {}, platform: 'darwin', arch: 'x64', versions: result.versions});
    window.nw = undefined;
    window.setTimeout = window.setInterval = function () { return 0; };
    const before = new Set(Object.getOwnPropertyNames(window));
    result.loaded = [];
    for (let i = 0; i < caches.length; i++) {
      evalCache(null, caches[i]);
      result.loaded.push(i === 0 ? 'njch.bin' : 'njcc.bin');
    }
    result.newGlobals = Object.getOwnPropertyNames(window).filter(function (key) {
      return !before.has(key);
    }).map(function (key) {
      const descriptor = Object.getOwnPropertyDescriptor(window, key);
      const value = descriptor.value;
      const entry = {name: key, type: typeof value};
      if (typeof value === 'function') entry.arity = value.length;
      if (value && (typeof value === 'object' || typeof value === 'function')) {
        entry.ownKeys = Object.getOwnPropertyNames(value);
      }
      return entry;
    });
    finish();
  } catch (error) {
    finish(error);
  }
}());
