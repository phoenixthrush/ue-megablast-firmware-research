package com.logitech.ue.centurion.wifi;

/* JADX INFO: loaded from: classes2.dex */
public class WiFiConnection extends com.logitech.ue.centurion.connection.CPLConnection {
    private static final int BUFFER_SIZE = 2048;
    private static final java.lang.String TAG = com.logitech.ue.centurion.utils.LogUtils.makeLogTag(com.logitech.ue.centurion.wifi.WiFiConnection.class);
    protected java.net.InetSocketAddress mAddress;
    protected com.logitech.ue.centurion.wifi.WiFiConnection.SessionThread mSessionThread;
    protected java.net.DatagramSocket mSocket;

    static /* synthetic */ void lambda$sendData$2(java.lang.Void r0) {
    }

    static /* synthetic */ void lambda$sendData$3(java.lang.Throwable th) {
    }

    public WiFiConnection(java.net.InetSocketAddress inetSocketAddress) {
        this.mAddress = inetSocketAddress;
    }

    @Override // com.logitech.ue.centurion.connection.IConnection
    public java.lang.String getAddress() {
        return this.mAddress.toString();
    }

    public java.net.InetSocketAddress getInetAddress() {
        return this.mAddress;
    }

    @Override // com.logitech.ue.centurion.connection.AbstractConnection
    protected java.lang.String getTAG() {
        return TAG;
    }

    @Override // com.logitech.ue.centurion.connection.IConnection
    public rx.Observable<java.lang.Void> connect() {
        return rx.Observable.defer(new rx.functions.Func0() { // from class: com.logitech.ue.centurion.wifi.WiFiConnection$$ExternalSyntheticLambda2
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public final java.lang.Object call() {
                return this.f$0.m660lambda$connect$0$comlogitechuecenturionwifiWiFiConnection();
            }
        });
    }

    /* JADX INFO: renamed from: lambda$connect$0$com-logitech-ue-centurion-wifi-WiFiConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m660lambda$connect$0$comlogitechuecenturionwifiWiFiConnection() {
        if (this.mConnectionState != 3) {
            return rx.Observable.error(new java.lang.IllegalStateException(com.logitech.ue.centurion.exceptions.ConnectionException.MESSAGE_CONNECTION_ALREADY_CONNECTED));
        }
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Connect to device");
        try {
            java.net.DatagramSocket datagramSocket = new java.net.DatagramSocket((java.net.SocketAddress) null);
            this.mSocket = datagramSocket;
            datagramSocket.setReuseAddress(true);
            this.mSocket.bind(new java.net.InetSocketAddress(com.logitech.ue.centurion.wifi.WiFiDiscoverer.DEFAULT_SCAN_PORT));
            this.mSocket.connect(this.mAddress);
            com.logitech.ue.centurion.wifi.WiFiConnection.SessionThread sessionThread = new com.logitech.ue.centurion.wifi.WiFiConnection.SessionThread();
            this.mSessionThread = sessionThread;
            sessionThread.start();
        } catch (java.io.IOException e) {
            com.logitech.ue.centurion.utils.LogUtils.LOGE(TAG, "Socket creation failed", e);
            rx.Observable.error(new com.logitech.ue.centurion.exceptions.ConnectionException(com.logitech.ue.centurion.exceptions.ConnectionException.MESSAGE_CONNECTION_FAILED));
        }
        setConnectionState(1);
        return rx.Observable.just(null);
    }

    @Override // com.logitech.ue.centurion.connection.IConnection
    public com.logitech.ue.centurion.connection.ConnectionType getConnectionType() {
        return com.logitech.ue.centurion.connection.ConnectionType.WIFI;
    }

    @Override // com.logitech.ue.centurion.connection.IConnection
    public rx.Observable<java.lang.Void> disconnect() {
        return rx.Observable.defer(new rx.functions.Func0() { // from class: com.logitech.ue.centurion.wifi.WiFiConnection$$ExternalSyntheticLambda3
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public final java.lang.Object call() {
                return this.f$0.m661xcec0326c();
            }
        });
    }

    /* JADX INFO: renamed from: lambda$disconnect$1$com-logitech-ue-centurion-wifi-WiFiConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m661xcec0326c() {
        if (this.mConnectionState != 1) {
            return rx.Observable.error(new java.lang.IllegalStateException("Connection is not connected"));
        }
        try {
            this.mSocket.close();
        } catch (java.lang.Exception unused) {
        }
        this.mSessionThread.interrupt();
        this.mSessionThread = null;
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Disconnected from device");
        setConnectionState(3);
        return rx.Observable.just(null);
    }

    @Override // com.logitech.ue.centurion.connection.AbstractConnection, com.logitech.ue.centurion.connection.IConnection
    public boolean isConnected() {
        return this.mConnectionState == 1;
    }

    @Override // com.logitech.ue.centurion.connection.IConnection
    public rx.Observable<java.lang.Void> sendData(final byte[] bArr) {
        com.google.common.base.Preconditions.checkNotNull(bArr);
        return rx.Observable.defer(new rx.functions.Func0() { // from class: com.logitech.ue.centurion.wifi.WiFiConnection$$ExternalSyntheticLambda4
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public final java.lang.Object call() {
                return this.f$0.m662lambda$sendData$4$comlogitechuecenturionwifiWiFiConnection(bArr);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$sendData$4$com-logitech-ue-centurion-wifi-WiFiConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m662lambda$sendData$4$comlogitechuecenturionwifiWiFiConnection(byte[] bArr) {
        try {
            checkConnection();
            try {
                for (byte[] bArr2 : wrapCPLData(bArr)) {
                    this.mSocket.send(new java.net.DatagramPacket(bArr2, bArr2.length, this.mAddress));
                    onDataSent(bArr2);
                }
                return rx.Observable.just(null);
            } catch (java.io.IOException unused) {
                com.logitech.ue.centurion.utils.LogUtils.LOGW(TAG, "Connection lost while sending data");
                disconnect().compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.wifi.WiFiConnection$$ExternalSyntheticLambda1
                    @Override // rx.functions.Action1
                    public final void call(java.lang.Object obj) {
                        com.logitech.ue.centurion.wifi.WiFiConnection.lambda$sendData$2((java.lang.Void) obj);
                    }
                }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.wifi.WiFiConnection$$ExternalSyntheticLambda0
                    @Override // rx.functions.Action1
                    public final void call(java.lang.Object obj) {
                        com.logitech.ue.centurion.wifi.WiFiConnection.lambda$sendData$3((java.lang.Throwable) obj);
                    }
                });
                return rx.Observable.error(new com.logitech.ue.centurion.exceptions.ConnectionException(com.logitech.ue.centurion.exceptions.ConnectionException.MESSAGE_STREAM_WRITE_FAILED));
            }
        } catch (java.lang.Exception e) {
            return rx.Observable.error(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SessionThread extends java.lang.Thread {
        static /* synthetic */ void lambda$run$0(java.lang.Void r0) {
        }

        static /* synthetic */ void lambda$run$1(java.lang.Throwable th) {
        }

        static /* synthetic */ void lambda$run$2(java.lang.Void r0) {
        }

        static /* synthetic */ void lambda$run$3(java.lang.Throwable th) {
        }

        public SessionThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.wifi.WiFiConnection.TAG, "Begin session thread");
            byte[] bArr = new byte[2048];
            try {
                java.net.DatagramPacket datagramPacket = new java.net.DatagramPacket(bArr, 2048, com.logitech.ue.centurion.wifi.WiFiConnection.this.mAddress);
                while (!isInterrupted()) {
                    com.logitech.ue.centurion.wifi.WiFiConnection.this.mSocket.receive(datagramPacket);
                    int length = datagramPacket.getLength();
                    byte[] bArr2 = new byte[length];
                    java.lang.System.arraycopy(bArr, 0, bArr2, 0, length);
                    com.logitech.ue.centurion.wifi.WiFiConnection.this.onDataReceived(bArr2);
                }
                com.logitech.ue.centurion.wifi.WiFiConnection.this.disconnect().compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.wifi.WiFiConnection$SessionThread$$ExternalSyntheticLambda2
                    @Override // rx.functions.Action1
                    public final void call(java.lang.Object obj) {
                        com.logitech.ue.centurion.wifi.WiFiConnection.SessionThread.lambda$run$0((java.lang.Void) obj);
                    }
                }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.wifi.WiFiConnection$SessionThread$$ExternalSyntheticLambda0
                    @Override // rx.functions.Action1
                    public final void call(java.lang.Object obj) {
                        com.logitech.ue.centurion.wifi.WiFiConnection.SessionThread.lambda$run$1((java.lang.Throwable) obj);
                    }
                });
            } catch (java.io.IOException unused) {
                com.logitech.ue.centurion.utils.LogUtils.LOGW(com.logitech.ue.centurion.wifi.WiFiConnection.TAG, "Connection lost while receiving data");
                com.logitech.ue.centurion.wifi.WiFiConnection.this.disconnect().compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.wifi.WiFiConnection$SessionThread$$ExternalSyntheticLambda3
                    @Override // rx.functions.Action1
                    public final void call(java.lang.Object obj) {
                        com.logitech.ue.centurion.wifi.WiFiConnection.SessionThread.lambda$run$2((java.lang.Void) obj);
                    }
                }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.wifi.WiFiConnection$SessionThread$$ExternalSyntheticLambda1
                    @Override // rx.functions.Action1
                    public final void call(java.lang.Object obj) {
                        com.logitech.ue.centurion.wifi.WiFiConnection.SessionThread.lambda$run$3((java.lang.Throwable) obj);
                    }
                });
            }
            com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.wifi.WiFiConnection.TAG, "Session thread stopped");
        }
    }
}
