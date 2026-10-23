package com.logitech.ue.centurion.wifi;

/* JADX INFO: loaded from: classes2.dex */
public class WiFiDiscoverer implements com.logitech.ue.centurion.discovery.IDiscoverer {
    public static final int DEFAULT_SCAN_PORT = 26756;
    private static final int WIFI_LIFE_TIMEOUT = 10000;
    private android.os.Handler mConnectionChecker;
    private android.content.Context mContext;
    private final java.util.List<com.logitech.ue.centurion.wifi.WiFiDiscoverer.WiFiDevice> mDiscoveredDevices = java.util.Collections.synchronizedList(new java.util.ArrayList());
    private volatile boolean mIsInited;
    private volatile boolean mIsSearching;
    private com.logitech.ue.centurion.wifi.WiFiDiscoverer.ListenerThread mListenerThread;
    private java.net.MulticastSocket mSocket;
    private android.net.wifi.WifiManager.WifiLock mWifiLock;
    private static final java.lang.String TAG = com.logitech.ue.centurion.utils.LogUtils.makeLogTag(com.logitech.ue.centurion.wifi.WiFiDiscoverer.class);
    private static com.logitech.ue.centurion.wifi.WiFiDiscoverer mInstance = null;

    static /* synthetic */ void lambda$handleLifeCheckMessage$0(java.lang.Void r0) {
    }

    static /* synthetic */ void lambda$handleLifeCheckMessage$1(java.lang.Throwable th) {
    }

    public static com.logitech.ue.centurion.wifi.WiFiDiscoverer getInstance() {
        if (mInstance == null) {
            synchronized (com.logitech.ue.centurion.wifi.WiFiDiscoverer.class) {
                if (mInstance == null) {
                    mInstance = new com.logitech.ue.centurion.wifi.WiFiDiscoverer();
                }
            }
        }
        return mInstance;
    }

    private WiFiDiscoverer() {
    }

    public synchronized void init(android.content.Context context) {
        com.google.common.base.Preconditions.checkNotNull(context);
        if (isReady()) {
            com.logitech.ue.centurion.utils.LogUtils.LOGW(TAG, "Discovery manager has already been initialised");
        } else {
            this.mContext = context;
            this.mIsInited = true;
            android.os.HandlerThread handlerThread = new android.os.HandlerThread("wifi_connection_checker");
            handlerThread.start();
            this.mConnectionChecker = new android.os.Handler(handlerThread.getLooper(), new android.os.Handler.Callback() { // from class: com.logitech.ue.centurion.wifi.WiFiDiscoverer$$ExternalSyntheticLambda0
                @Override // android.os.Handler.Callback
                public final boolean handleMessage(android.os.Message message) {
                    return this.f$0.handleLifeCheckMessage(message);
                }
            });
            com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Begin discovery manager initialisation");
        }
    }

    public boolean isReady() {
        return this.mIsInited;
    }

    @Override // com.logitech.ue.centurion.discovery.IDiscoverer
    public boolean isSearching() {
        return this.mIsSearching;
    }

    @Override // com.logitech.ue.centurion.discovery.IDiscoverer
    public void beginDeviceSearch() {
        if (this.mListenerThread != null) {
            com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Manager is already scanning");
            return;
        }
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Begin wifi scanning of the port 26756");
        this.mIsSearching = true;
        com.logitech.ue.centurion.wifi.WiFiDiscoverer.ListenerThread listenerThread = new com.logitech.ue.centurion.wifi.WiFiDiscoverer.ListenerThread();
        this.mListenerThread = listenerThread;
        listenerThread.start();
    }

    @Override // com.logitech.ue.centurion.discovery.IDiscoverer
    public void stopDeviceSearch() {
        if (!this.mIsSearching) {
            com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Manager is not searching");
            return;
        }
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Stop search");
        this.mListenerThread.interrupt();
        this.mWifiLock.release();
        this.mSocket.close();
        this.mIsSearching = false;
        this.mListenerThread = null;
    }

    @Override // com.logitech.ue.centurion.discovery.IDiscoverer
    public java.util.List<com.logitech.ue.centurion.discovery.DiscoveryInfo> getFoundDevices() {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.util.Iterator<com.logitech.ue.centurion.wifi.WiFiDiscoverer.WiFiDevice> it = this.mDiscoveredDevices.iterator();
        while (it.hasNext()) {
            arrayList.add(new com.logitech.ue.centurion.discovery.GenericDiscoveryInfo(com.logitech.ue.centurion.connection.ConnectionType.WIFI, it.next().connection.getAddress(), 256, 6, "Emulator", java.lang.System.currentTimeMillis(), true, 1));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.logitech.ue.centurion.wifi.WiFiConnection getConnection(java.net.InetSocketAddress inetSocketAddress) {
        com.google.common.base.Preconditions.checkNotNull(inetSocketAddress);
        for (com.logitech.ue.centurion.wifi.WiFiDiscoverer.WiFiDevice wiFiDevice : this.mDiscoveredDevices) {
            if (wiFiDevice.connection.getInetAddress().equals(inetSocketAddress)) {
                return wiFiDevice.connection;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDevice(com.logitech.ue.centurion.wifi.WiFiConnection wiFiConnection) {
        com.google.common.base.Preconditions.checkNotNull(wiFiConnection);
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "New device found. Register the device. Address: " + wiFiConnection.getAddress());
        com.logitech.ue.centurion.wifi.WiFiDiscoverer.WiFiDevice wiFiDevice = new com.logitech.ue.centurion.wifi.WiFiDiscoverer.WiFiDevice();
        wiFiDevice.connection = wiFiConnection;
        wiFiDevice.lifeTimestamp = java.lang.System.currentTimeMillis();
        this.mDiscoveredDevices.add(wiFiDevice);
        android.os.Handler handler = this.mConnectionChecker;
        handler.sendMessageDelayed(android.os.Message.obtain(handler, 0, wiFiDevice), 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleLifeCheckMessage(android.os.Message message) {
        com.logitech.ue.centurion.wifi.WiFiDiscoverer.WiFiDevice wiFiDevice = (com.logitech.ue.centurion.wifi.WiFiDiscoverer.WiFiDevice) message.obj;
        synchronized (this.mDiscoveredDevices) {
            if (this.mDiscoveredDevices.contains(wiFiDevice)) {
                long jCurrentTimeMillis = java.lang.System.currentTimeMillis() - wiFiDevice.lifeTimestamp;
                if (jCurrentTimeMillis > 10000) {
                    com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Device connection timeout. Address: " + wiFiDevice.connection.getAddress());
                    this.mDiscoveredDevices.remove(wiFiDevice);
                    if (wiFiDevice.connection.isConnected()) {
                        wiFiDevice.connection.disconnect().compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.wifi.WiFiDiscoverer$$ExternalSyntheticLambda2
                            @Override // rx.functions.Action1
                            public final void call(java.lang.Object obj) {
                                com.logitech.ue.centurion.wifi.WiFiDiscoverer.lambda$handleLifeCheckMessage$0((java.lang.Void) obj);
                            }
                        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.wifi.WiFiDiscoverer$$ExternalSyntheticLambda1
                            @Override // rx.functions.Action1
                            public final void call(java.lang.Object obj) {
                                com.logitech.ue.centurion.wifi.WiFiDiscoverer.lambda$handleLifeCheckMessage$1((java.lang.Throwable) obj);
                            }
                        });
                    }
                } else {
                    com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Device connection FINE. Address: " + wiFiDevice.connection.getAddress());
                    this.mConnectionChecker.removeMessages(0, wiFiDevice);
                    android.os.Handler handler = this.mConnectionChecker;
                    handler.sendMessageDelayed(android.os.Message.obtain(handler, 0, wiFiDevice), jCurrentTimeMillis);
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void extendConnectionLifeTime(com.logitech.ue.centurion.wifi.WiFiConnection wiFiConnection) {
        synchronized (this.mDiscoveredDevices) {
            for (com.logitech.ue.centurion.wifi.WiFiDiscoverer.WiFiDevice wiFiDevice : this.mDiscoveredDevices) {
                if (wiFiDevice.connection == wiFiConnection) {
                    com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Extend connection lifetime. Address: " + wiFiConnection.getAddress());
                    wiFiDevice.lifeTimestamp = java.lang.System.currentTimeMillis();
                }
            }
        }
    }

    private class ListenerThread extends java.lang.Thread {
        private ListenerThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.wifi.WiFiDiscoverer.TAG, "Start scanner thread");
            byte[] bArr = new byte[1024];
            com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.wifi.WiFiDiscoverer.TAG, "Acquire wifi lock");
            com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mWifiLock = ((android.net.wifi.WifiManager) com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mContext.getApplicationContext().getSystemService("wifi")).createWifiLock(1, com.logitech.ue.centurion.wifi.WiFiDiscoverer.TAG);
            com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mWifiLock.setReferenceCounted(false);
            try {
                try {
                    com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mWifiLock.acquire();
                    com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mSocket = new java.net.MulticastSocket((java.net.SocketAddress) null);
                    com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mSocket.setReuseAddress(true);
                    com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mSocket.setBroadcast(true);
                    com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mSocket.bind(new java.net.InetSocketAddress(com.logitech.ue.centurion.wifi.WiFiDiscoverer.DEFAULT_SCAN_PORT));
                    java.net.DatagramPacket datagramPacket = new java.net.DatagramPacket(bArr, 1024);
                    while (!java.lang.Thread.interrupted()) {
                        com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mSocket.receive(datagramPacket);
                        if (com.logitech.ue.centurion.utils.CenturionUtils.isNopBroadcastMessage(bArr, datagramPacket.getLength())) {
                            com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.wifi.WiFiDiscoverer.TAG, "Device valid package received. Package: " + com.logitech.ue.centurion.utils.Utils.byteArrayToFancyHexString(bArr, 0, datagramPacket.getLength()));
                            com.logitech.ue.centurion.wifi.WiFiConnection connection = com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.getConnection(new java.net.InetSocketAddress(datagramPacket.getAddress(), datagramPacket.getPort()));
                            if (connection != null) {
                                com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.extendConnectionLifeTime(connection);
                            } else {
                                com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.registerDevice(new com.logitech.ue.centurion.wifi.WiFiConnection(new java.net.InetSocketAddress(datagramPacket.getAddress(), datagramPacket.getPort())));
                                com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.stopDeviceSearch();
                                com.logitech.ue.centurion.eventbus.CenturionEventBus.get().post(new com.logitech.ue.centurion.eventbus.event.DeviceFoundEvent(new com.logitech.ue.centurion.discovery.GenericDiscoveryInfo(com.logitech.ue.centurion.connection.ConnectionType.WIFI, java.lang.String.format(java.util.Locale.US, "%s:%d", datagramPacket.getAddress().getHostAddress(), java.lang.Integer.valueOf(datagramPacket.getPort())), 256, 6, "Emulator", java.lang.System.currentTimeMillis(), true, 1)));
                            }
                        } else {
                            com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.wifi.WiFiDiscoverer.TAG, "Device invalid package received. Package: " + com.logitech.ue.centurion.utils.Utils.byteArrayToFancyHexString(bArr, 0, datagramPacket.getLength()));
                        }
                        datagramPacket.setLength(1024);
                    }
                    com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.wifi.WiFiDiscoverer.TAG, "Scanning ended successfully");
                } catch (java.lang.Exception e) {
                    com.logitech.ue.centurion.utils.LogUtils.LOGE(com.logitech.ue.centurion.wifi.WiFiDiscoverer.TAG, "Scanning error. Stop scan", e);
                    com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.stopDeviceSearch();
                    com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mListenerThread = null;
                }
            } finally {
                com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.wifi.WiFiDiscoverer.TAG, "Release wifi lock");
                com.logitech.ue.centurion.wifi.WiFiDiscoverer.this.mWifiLock.release();
            }
        }
    }

    private class WiFiDevice {
        com.logitech.ue.centurion.wifi.WiFiConnection connection;
        long lifeTimestamp;

        private WiFiDevice() {
        }
    }
}
