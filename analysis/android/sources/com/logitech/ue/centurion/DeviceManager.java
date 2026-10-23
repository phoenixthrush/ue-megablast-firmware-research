package com.logitech.ue.centurion;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceManager {
    private static final java.lang.String TAG = com.logitech.ue.centurion.utils.LogUtils.makeLogTag(com.logitech.ue.centurion.DeviceManager.class);
    private static volatile com.logitech.ue.centurion.DeviceManager mInstance = null;
    private android.content.Context context;
    private com.logitech.ue.centurion.device.DisconnectionInstructions disconnectionInstructions;
    private com.logitech.ue.centurion.device.Device mConnectedDevice;
    private rx.subscriptions.CompositeSubscription mConnectedDeviceSubscriptions;
    private com.logitech.ue.centurion.connection.IConnection mConnection;
    private java.lang.String mDeepSleepConnectedDeviceAddress;
    private com.logitech.ue.centurion.connection.ConnectionType mDeepSleepConnectedDeviceConnectionType;
    private com.logitech.ue.centurion.IDeviceFactory mDeviceFactory;
    private boolean mIsInited = false;
    private int connectionState = -1;

    static /* synthetic */ void lambda$establishDeviceConnection$10(java.lang.Throwable th) {
    }

    static /* synthetic */ void lambda$onA2DPDisconnected$14(com.logitech.ue.centurion.device.Device device) {
    }

    static /* synthetic */ void lambda$onA2DPDisconnected$15(java.lang.Throwable th) {
    }

    static /* synthetic */ void lambda$processDeviceDisconnection$0(java.lang.Void r0) {
    }

    static /* synthetic */ void lambda$processDeviceDisconnection$1(java.lang.Throwable th) {
    }

    public static com.logitech.ue.centurion.DeviceManager getInstance() {
        if (mInstance == null) {
            synchronized (com.logitech.ue.centurion.DeviceManager.class) {
                if (mInstance == null) {
                    mInstance = new com.logitech.ue.centurion.DeviceManager();
                }
            }
        }
        return mInstance;
    }

    protected DeviceManager() {
    }

    public synchronized void init(android.content.Context context, com.logitech.ue.centurion.IDeviceFactory iDeviceFactory) {
        com.google.common.base.Preconditions.checkNotNull(context);
        com.google.common.base.Preconditions.checkNotNull(iDeviceFactory);
        if (isReady()) {
            com.logitech.ue.centurion.utils.LogUtils.LOGW(TAG, "Can't start initialised, because it is already initialised");
            return;
        }
        java.lang.String str = TAG;
        com.logitech.ue.centurion.utils.LogUtils.LOGD(str, "Begin initialisation");
        this.mDeviceFactory = iDeviceFactory;
        this.mIsInited = true;
        this.context = context;
        com.logitech.ue.centurion.utils.LogUtils.LOGD(str, "DeviceManager initialisation finished");
    }

    private rx.Observable<com.logitech.ue.centurion.device.Device> connectToDevice(com.logitech.ue.centurion.connection.IConnection iConnection) {
        return establishDeviceConnection((com.logitech.ue.centurion.connection.IConnection) com.google.common.base.Preconditions.checkNotNull(iConnection));
    }

    public rx.Observable<com.logitech.ue.centurion.device.Device> disconnectDevice(com.logitech.ue.centurion.device.Device device, com.logitech.ue.centurion.device.DisconnectionInstructions disconnectionInstructions) {
        this.disconnectionInstructions = disconnectionInstructions;
        return disconnectFromDevice(device);
    }

    private void processDeviceDisconnection(com.logitech.ue.centurion.connection.IConnection iConnection) {
        iConnection.disconnect().compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda15
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.DeviceManager.lambda$processDeviceDisconnection$0((java.lang.Void) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda14
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.DeviceManager.lambda$processDeviceDisconnection$1((java.lang.Throwable) obj);
            }
        });
    }

    private rx.Observable<com.logitech.ue.centurion.device.Device> disconnectFromDevice(final com.logitech.ue.centurion.device.Device device) {
        return rx.Observable.defer(new rx.functions.Func0() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda1
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public final java.lang.Object call() {
                return com.logitech.ue.centurion.DeviceManager.lambda$disconnectFromDevice$3(device);
            }
        });
    }

    static /* synthetic */ rx.Observable lambda$disconnectFromDevice$3(final com.logitech.ue.centurion.device.Device device) {
        rx.subjects.PublishSubject publishSubjectCreate = rx.subjects.PublishSubject.create();
        device.getConnection().disconnect().compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda6
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return rx.Observable.just(device);
            }
        }).subscribe(publishSubjectCreate);
        return publishSubjectCreate;
    }

    private rx.Observable<com.logitech.ue.centurion.device.Device> establishDeviceConnection(final com.logitech.ue.centurion.connection.IConnection iConnection) {
        return rx.Observable.defer(new rx.functions.Func0() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda16
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public final java.lang.Object call() {
                return this.f$0.m605xe1fe6e88(iConnection);
            }
        }).compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers());
    }

    /* JADX INFO: renamed from: lambda$establishDeviceConnection$13$com-logitech-ue-centurion-DeviceManager, reason: not valid java name */
    /* synthetic */ rx.Observable m605xe1fe6e88(final com.logitech.ue.centurion.connection.IConnection iConnection) {
        rx.Observable<com.logitech.ue.centurion.device.Device> observableJust;
        com.logitech.ue.centurion.device.Device device = this.mConnectedDevice;
        if (device != null) {
            if (device.getConnection().getConnectionType() == iConnection.getConnectionType() && this.mConnectedDevice.getAddress().equals(iConnection.getAddress())) {
                com.logitech.ue.centurion.utils.LogUtils.LOGW(TAG, "Device is already connected?");
                return rx.Observable.just(this.mConnectedDevice);
            }
            com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Device is connected. Dropping old device");
            observableJust = this.mConnectedDevice.disconnect().onErrorResumeNext(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda7
                @Override // rx.functions.Func1
                public final java.lang.Object call(java.lang.Object obj) {
                    return rx.Observable.just(null);
                }
            });
        } else {
            observableJust = rx.Observable.just(null);
        }
        this.connectionState = 0;
        return observableJust.flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda5
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return iConnection.connect();
            }
        }).doOnSubscribe(new rx.functions.Action0() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda0
            @Override // rx.functions.Action0
            public final void call() {
                com.logitech.ue.centurion.connection.IConnection iConnection2 = iConnection;
                com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.DeviceManager.TAG, java.lang.String.format("Connect new device. Type: %s Address: %s", iConnection2.getConnectionType(), iConnection2.getAddress()));
            }
        }).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda3
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m606xb042bff5(iConnection, (java.lang.Void) obj);
            }
        }).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda4
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m607x3ce2eaf6(iConnection, (java.lang.Void) obj);
            }
        }).doOnNext(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda8
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m603xc8be1886(iConnection, (com.logitech.ue.centurion.device.Device) obj);
            }
        }).doOnError(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda10
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m604x555e4387(iConnection, (java.lang.Throwable) obj);
            }
        }).compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers());
    }

    /* JADX INFO: renamed from: lambda$establishDeviceConnection$7$com-logitech-ue-centurion-DeviceManager, reason: not valid java name */
    /* synthetic */ rx.Observable m606xb042bff5(com.logitech.ue.centurion.connection.IConnection iConnection, java.lang.Void r3) {
        this.mConnection = iConnection;
        if (!com.logitech.ue.centurion.eventbus.CenturionEventBus.get().isRegistered(this)) {
            com.logitech.ue.centurion.eventbus.CenturionEventBus.get().register(this);
        }
        this.connectionState = 1;
        if (iConnection.getConnectionType() == com.logitech.ue.centurion.connection.ConnectionType.BLE) {
            return iConnection.authorize(com.logitech.ue.centurion.utils.CenturionUtils.getPhoneSecureId(this.context));
        }
        return rx.Observable.just(null);
    }

    /* JADX INFO: renamed from: lambda$establishDeviceConnection$8$com-logitech-ue-centurion-DeviceManager, reason: not valid java name */
    /* synthetic */ rx.Observable m607x3ce2eaf6(com.logitech.ue.centurion.connection.IConnection iConnection, java.lang.Void r2) {
        return this.mDeviceFactory.buildDevice(iConnection);
    }

    /* JADX INFO: renamed from: lambda$establishDeviceConnection$11$com-logitech-ue-centurion-DeviceManager, reason: not valid java name */
    /* synthetic */ void m603xc8be1886(final com.logitech.ue.centurion.connection.IConnection iConnection, com.logitech.ue.centurion.device.Device device) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Device successfully connected. Features: " + device.getFeatureMapper());
        this.mConnectedDevice = device;
        rx.subscriptions.CompositeSubscription compositeSubscription = new rx.subscriptions.CompositeSubscription();
        this.mConnectedDeviceSubscriptions = compositeSubscription;
        compositeSubscription.add(iConnection.observeConnectionStateChanges().subscribeOn(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().io()).observeOn(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().connection()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda9
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m608xc98315f7(iConnection, (java.lang.Integer) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda12
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.DeviceManager.lambda$establishDeviceConnection$10((java.lang.Throwable) obj);
            }
        }));
        com.logitech.ue.centurion.eventbus.CenturionEventBus.get().post(new com.logitech.ue.centurion.eventbus.event.DeviceConnectedEvent(this.mConnectedDevice));
    }

    /* JADX INFO: renamed from: lambda$establishDeviceConnection$9$com-logitech-ue-centurion-DeviceManager, reason: not valid java name */
    /* synthetic */ void m608xc98315f7(com.logitech.ue.centurion.connection.IConnection iConnection, java.lang.Integer num) {
        connectionStateChanged(iConnection, num.intValue());
    }

    /* JADX INFO: renamed from: lambda$establishDeviceConnection$12$com-logitech-ue-centurion-DeviceManager, reason: not valid java name */
    /* synthetic */ void m604x555e4387(com.logitech.ue.centurion.connection.IConnection iConnection, java.lang.Throwable th) {
        if (iConnection.isConnected()) {
            this.mConnection = null;
            processDeviceDisconnection(iConnection);
        }
        com.logitech.ue.centurion.utils.LogUtils.LOGW(TAG, "Device failed to connect", th);
    }

    private void connectionStateChanged(com.logitech.ue.centurion.connection.IConnection iConnection, int i) {
        java.lang.String address;
        com.logitech.ue.centurion.connection.ConnectionType connectionType;
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Connection %s(%s) connection state change %s(%d)", iConnection.getAddress(), iConnection.getConnectionType(), com.logitech.ue.centurion.connection.ConnectionState.getName(i), java.lang.Integer.valueOf(i)));
        if (i == 3) {
            if (this.connectionState == 4) {
                address = this.mDeepSleepConnectedDeviceAddress;
                connectionType = this.mDeepSleepConnectedDeviceConnectionType;
            } else if (this.mConnectedDevice.getConnection() == iConnection) {
                if (com.logitech.ue.centurion.eventbus.CenturionEventBus.get().isRegistered(this)) {
                    com.logitech.ue.centurion.eventbus.CenturionEventBus.get().unregister(this);
                }
                address = this.mConnectedDevice.getAddress();
                connectionType = this.mConnectedDevice.getConnection().getConnectionType();
            } else {
                address = null;
                connectionType = null;
            }
            this.connectionState = 3;
            this.mConnectedDevice = null;
            this.mConnectedDeviceSubscriptions.unsubscribe();
            this.mConnectedDeviceSubscriptions = null;
            com.logitech.ue.centurion.eventbus.CenturionEventBus.get().post(createDisConnectedEvent(address, connectionType));
        } else if (i == 4) {
            this.connectionState = 4;
            this.mDeepSleepConnectedDeviceAddress = this.mConnectedDevice.getAddress();
            this.mDeepSleepConnectedDeviceConnectionType = this.mConnectedDevice.getConnection().getConnectionType();
            this.mConnectedDevice = null;
            com.logitech.ue.centurion.eventbus.CenturionEventBus.get().post(new com.logitech.ue.centurion.eventbus.event.DeviceDeepSleepEvent(this.mDeepSleepConnectedDeviceAddress, this.mDeepSleepConnectedDeviceConnectionType));
        }
        if (i == 3) {
            this.mConnection = null;
        }
    }

    private com.logitech.ue.centurion.eventbus.event.DeviceDisconnectedEvent createDisConnectedEvent(java.lang.String str, com.logitech.ue.centurion.connection.ConnectionType connectionType) {
        com.logitech.ue.centurion.eventbus.event.DeviceDisconnectedEvent deviceDisconnectedEvent = new com.logitech.ue.centurion.eventbus.event.DeviceDisconnectedEvent(str, connectionType, this.disconnectionInstructions);
        this.disconnectionInstructions = null;
        return deviceDisconnectedEvent;
    }

    public boolean isReady() {
        return this.mIsInited;
    }

    public void debugAddDummyDevice(com.logitech.ue.centurion.device.DummyDevice dummyDevice) {
        this.mConnectedDevice = dummyDevice;
    }

    public rx.Observable<com.logitech.ue.centurion.device.Device> connectToDevice(com.logitech.ue.centurion.discovery.IDiscoveryInfo iDiscoveryInfo) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "connecting to device: " + iDiscoveryInfo.getConnectionType() + " address: " + iDiscoveryInfo.getAddress());
        return connectToDevice(com.logitech.ue.centurion.connection.ConnectionFactory.getConnection(iDiscoveryInfo));
    }

    public rx.Observable<com.logitech.ue.centurion.device.Device> connectToDevice(com.logitech.ue.centurion.connection.ConnectionType connectionType, java.lang.String str) {
        return connectToDevice(com.logitech.ue.centurion.connection.ConnectionFactory.getConnection(new com.logitech.ue.centurion.discovery.DiscoveryInfo(connectionType, str, java.lang.System.currentTimeMillis(), true, 1)));
    }

    public com.logitech.ue.centurion.device.Device getConnectedDevice() {
        return this.mConnectedDevice;
    }

    @org.greenrobot.eventbus.Subscribe
    public void onA2DPDisconnected(com.logitech.ue.centurion.eventbus.event.A2dpProfileDisconnectedEvent a2dpProfileDisconnectedEvent) {
        com.logitech.ue.centurion.device.Device device;
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "received a2dp disconnected event for device: " + a2dpProfileDisconnectedEvent.address);
        com.logitech.ue.centurion.device.Device device2 = this.mConnectedDevice;
        if (device2 == null || device2.getConnection().getConnectionType() != com.logitech.ue.centurion.connection.ConnectionType.SPP || !this.mConnectedDevice.getAddress().equalsIgnoreCase(a2dpProfileDisconnectedEvent.address) || (device = this.mConnectedDevice) == null) {
            return;
        }
        disconnectDevice(device, null).subscribeOn(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().connection()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda11
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.DeviceManager.lambda$onA2DPDisconnected$14((com.logitech.ue.centurion.device.Device) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda13
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.DeviceManager.lambda$onA2DPDisconnected$15((java.lang.Throwable) obj);
            }
        });
    }

    public rx.Observable<java.lang.Void> cancelPairing() {
        com.logitech.ue.centurion.connection.IConnection iConnection = this.mConnection;
        if (iConnection != null) {
            return iConnection.disconnect().compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers());
        }
        return rx.Observable.just(null);
    }

    public rx.Observable<java.lang.Void> connectToSpeakerInPowerOffMode(final java.lang.String str, int i) {
        com.logitech.ue.centurion.connection.IConnection iConnection = this.mConnection;
        if (iConnection != null && iConnection.isConnected()) {
            return rx.Observable.just(null);
        }
        final com.logitech.ue.centurion.ble.BLEConnection bLEConnection = (com.logitech.ue.centurion.ble.BLEConnection) com.logitech.ue.centurion.connection.ConnectionFactory.getConnection(new com.logitech.ue.centurion.discovery.DiscoveryInfo(com.logitech.ue.centurion.connection.ConnectionType.BLE, str, java.lang.System.currentTimeMillis(), false, i));
        return bLEConnection.connectInSpeakerOffMode().flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.DeviceManager$$ExternalSyntheticLambda2
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m602xa988a172(bLEConnection, str, (java.lang.Void) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$connectToSpeakerInPowerOffMode$16$com-logitech-ue-centurion-DeviceManager, reason: not valid java name */
    /* synthetic */ rx.Observable m602xa988a172(com.logitech.ue.centurion.ble.BLEConnection bLEConnection, java.lang.String str, java.lang.Void r4) {
        this.mConnection = bLEConnection;
        this.connectionState = 4;
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "connected to speaker in deep sleep mode");
        com.logitech.ue.centurion.eventbus.CenturionEventBus.get().post(new com.logitech.ue.centurion.eventbus.event.DeviceDeepSleepEvent(str, com.logitech.ue.centurion.connection.ConnectionType.BLE));
        return rx.Observable.just(null);
    }

    public com.logitech.ue.centurion.connection.IConnection getConnection() {
        return this.mConnection;
    }

    public int updateConnectionStatus(com.logitech.ue.centurion.discovery.IDiscoveryInfo iDiscoveryInfo, java.lang.String str) {
        int i = this.connectionState;
        com.logitech.ue.centurion.device.Device device = this.mConnectedDevice;
        if (device != null) {
            this.connectionState = 1;
            if (!str.equalsIgnoreCase(device.getAddress())) {
                this.connectionState = 3;
            } else if (com.logitech.ue.centurion.utils.CenturionUtils.isRemotePowerOnSupported(iDiscoveryInfo) && iDiscoveryInfo.getAddress().equalsIgnoreCase(this.mConnectedDevice.getAddress())) {
                if (iDiscoveryInfo.isPowerOn()) {
                    this.connectionState = 1;
                } else {
                    this.connectionState = 4;
                    connectionStateChanged(this.mConnection, 4);
                }
                java.lang.String str2 = TAG;
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append("update connection state selected address: ");
                sb.append(str);
                sb.append(" old state: ");
                sb.append(i);
                sb.append(" new state: ");
                sb.append(this.connectionState);
                sb.append(" isConnected: ");
                sb.append(this.mConnection != null);
                sb.append(" is speaker powered: ");
                sb.append(iDiscoveryInfo.isPowerOn());
                com.logitech.ue.centurion.utils.LogUtils.LOGD(str2, sb.toString());
            }
        } else if (iDiscoveryInfo != null && str.equalsIgnoreCase(iDiscoveryInfo.getAddress()) && com.logitech.ue.centurion.utils.CenturionUtils.isRemotePowerOnSupported(iDiscoveryInfo)) {
            if (this.connectionState == 4 && iDiscoveryInfo.isPowerOn() && this.mConnection != null) {
                com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "speaker waken up from deep sleep");
                this.connectionState = 6;
            } else if (iDiscoveryInfo.isPowerOn() && this.mConnection == null) {
                this.connectionState = 3;
            } else if (this.mConnection != null && !iDiscoveryInfo.isPowerOn()) {
                this.connectionState = 4;
            } else if (this.connectionState != 6) {
                this.connectionState = 5;
            }
            java.lang.String str3 = TAG;
            java.lang.StringBuilder sb2 = new java.lang.StringBuilder();
            sb2.append("update connection state selected address: ");
            sb2.append(str);
            sb2.append(" old state: ");
            sb2.append(i);
            sb2.append(" new state: ");
            sb2.append(this.connectionState);
            sb2.append(" isConnected: ");
            sb2.append(this.mConnection != null);
            sb2.append(" is speaker powered: ");
            sb2.append(iDiscoveryInfo.isPowerOn());
            com.logitech.ue.centurion.utils.LogUtils.LOGD(str3, sb2.toString());
        } else {
            this.connectionState = 3;
        }
        return this.connectionState;
    }

    public boolean isConnectedInOffMode() {
        return this.connectionState == 4;
    }
}
