package com.logitech.ue.centurion.ble;

/* JADX INFO: loaded from: classes2.dex */
public class BLEConnection extends com.logitech.ue.centurion.connection.CPLConnection {
    private static final byte FLAG_AUTHENTICATE = 66;
    private static final byte FLAG_POWER_OFF = -86;
    private static final byte FLAG_POWER_ON = -1;
    private static final int MTU_SIZE = 512;
    private com.polidea.rxandroidble.RxBleConnection mConnection;
    private rx.subscriptions.CompositeSubscription mConnectionSubscription = new rx.subscriptions.CompositeSubscription();
    private com.logitech.ue.centurion.utils.MAC mMac;
    private static final java.lang.String TAG = com.logitech.ue.centurion.utils.LogUtils.makeLogTag(com.logitech.ue.centurion.ble.BLEConnection.class);
    private static final java.util.UUID UUID_PRIMARY_SERVICE = java.util.UUID.fromString("7FD36E38-4680-4A77-BEA3-BEEAEECBF592");
    private static final java.util.UUID UUID_POWER_SERVICE = java.util.UUID.fromString("1b7e8251-2877-41c3-b46e-cf057c562023");
    private static final java.util.UUID WRITE_CHARACTERISTIC = java.util.UUID.fromString("748E2216-8B9A-4598-A0C6-2DEE1AF454DB");
    private static final java.util.UUID AUTH0RIZE_AND_REMOTE_POWER = java.util.UUID.fromString("5E9BF2A8-F93F-4481-A67E-3B2F4A07891A");
    private static final java.util.UUID LONG_RESPONSE_CHARACTERISTIC = java.util.UUID.fromString("8F83DD07-E637-5CEA-A223-F1F3B64B73EE");

    static /* synthetic */ void lambda$connect$0(java.lang.Throwable th) {
    }

    static /* synthetic */ rx.Observable lambda$connect$1(rx.Observable observable) {
        return observable;
    }

    static /* synthetic */ void lambda$connect$10(java.lang.Throwable th) {
    }

    static /* synthetic */ void lambda$connect$12(java.lang.Void r0) {
    }

    static /* synthetic */ void lambda$connect$13(java.lang.Throwable th) {
    }

    static /* synthetic */ rx.Observable lambda$connect$3(rx.Observable observable) {
        return observable;
    }

    static /* synthetic */ rx.Observable lambda$connect$5(rx.Observable observable) {
        return observable;
    }

    static /* synthetic */ void lambda$connect$7(java.lang.Void r0) {
    }

    static /* synthetic */ void lambda$connect$8(java.lang.Throwable th) {
    }

    static /* synthetic */ rx.Observable lambda$connectInSpeakerOffMode$29(rx.Observable observable) {
        return observable;
    }

    static /* synthetic */ void lambda$connectInSpeakerOffMode$31(java.lang.Void r0) {
    }

    static /* synthetic */ void lambda$connectInSpeakerOffMode$32(java.lang.Throwable th) {
    }

    static /* synthetic */ void lambda$connectInSpeakerOffMode$34(java.lang.Throwable th) {
    }

    static /* synthetic */ void lambda$readLongResponseData$22(java.lang.Throwable th) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAuthorizeCharactersticChanged(byte[] bArr) {
    }

    public BLEConnection(com.logitech.ue.centurion.utils.MAC mac) {
        this.mMac = mac;
    }

    @Override // com.logitech.ue.centurion.connection.IConnection
    public java.lang.String getAddress() {
        return this.mMac.toString();
    }

    public com.logitech.ue.centurion.utils.MAC getMAC() {
        return this.mMac;
    }

    @Override // com.logitech.ue.centurion.connection.AbstractConnection
    protected java.lang.String getTAG() {
        return TAG;
    }

    @Override // com.logitech.ue.centurion.connection.IConnection
    public rx.Observable<java.lang.Void> connect() {
        return rx.Observable.defer(new rx.functions.Func0() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda19
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public final java.lang.Object call() {
                return this.f$0.m615lambda$connect$15$comlogitechuecenturionbleBLEConnection();
            }
        });
    }

    /* JADX INFO: renamed from: lambda$connect$15$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m615lambda$connect$15$comlogitechuecenturionbleBLEConnection() {
        if (this.mConnectionState != 3) {
            return rx.Observable.error(new java.lang.IllegalStateException(com.logitech.ue.centurion.exceptions.ConnectionException.MESSAGE_CONNECTION_ALREADY_CONNECTED));
        }
        final rx.subjects.PublishSubject publishSubjectCreate = rx.subjects.PublishSubject.create();
        this.mConnectionState = 0;
        final com.polidea.rxandroidble.RxBleDevice bleDevice = com.logitech.ue.centurion.ble.BLERxClient.get().getBleDevice(this.mMac.toString());
        this.mConnectionSubscription.add(bleDevice.establishConnection(false).retryWhen(new com.logitech.ue.centurion.utils.RetryWithDelay(this.connectionParams.getReconnectionTryCount(), this.connectionParams.getReconnectionDelay()) { // from class: com.logitech.ue.centurion.ble.BLEConnection.1
            @Override // com.logitech.ue.centurion.utils.RetryWithDelay
            public boolean onRetry(int i, java.lang.Throwable th) {
                com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.ble.BLEConnection.TAG, "retrying BLE connection: " + bleDevice.getMacAddress());
                android.bluetooth.BluetoothAdapter.getDefaultAdapter().startDiscovery();
                return super.onRetry(i, th);
            }
        }).compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).subscribe((rx.functions.Action1<? super R>) new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda42
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m613lambda$connect$11$comlogitechuecenturionbleBLEConnection(bleDevice, publishSubjectCreate, (com.polidea.rxandroidble.RxBleConnection) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda43
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m614lambda$connect$14$comlogitechuecenturionbleBLEConnection(publishSubjectCreate, (java.lang.Throwable) obj);
            }
        }));
        return publishSubjectCreate;
    }

    /* JADX INFO: renamed from: lambda$connect$11$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ void m613lambda$connect$11$comlogitechuecenturionbleBLEConnection(com.polidea.rxandroidble.RxBleDevice rxBleDevice, rx.subjects.PublishSubject publishSubject, com.polidea.rxandroidble.RxBleConnection rxBleConnection) {
        this.mConnection = rxBleConnection;
        rxBleConnection.requestMtu(512).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda22
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.onMtuChange((java.lang.Integer) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda4
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.ble.BLEConnection.lambda$connect$0((java.lang.Throwable) obj);
            }
        });
        this.mConnectionSubscription.add(rxBleConnection.setupNotification(WRITE_CHARACTERISTIC, com.polidea.rxandroidble.NotificationSetupMode.COMPAT).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda28
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.ble.BLEConnection.lambda$connect$1((rx.Observable) obj);
            }
        }).subscribe((rx.functions.Action1<? super R>) new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda40
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.onWriteCharacteristicNotification((byte[]) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda7
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.ble.BLEConnection.TAG, "Subscription error");
            }
        }));
        this.mConnectionSubscription.add(rxBleConnection.setupNotification(LONG_RESPONSE_CHARACTERISTIC, com.polidea.rxandroidble.NotificationSetupMode.COMPAT).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda29
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.ble.BLEConnection.lambda$connect$3((rx.Observable) obj);
            }
        }).subscribe((rx.functions.Action1<? super R>) new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda38
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.onLongResponseCharacteristicNotification((byte[]) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda8
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.ble.BLEConnection.TAG, "Subscription error");
            }
        }));
        this.mConnectionSubscription.add(rxBleConnection.setupNotification(AUTH0RIZE_AND_REMOTE_POWER, com.polidea.rxandroidble.NotificationSetupMode.COMPAT).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda30
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.ble.BLEConnection.lambda$connect$5((rx.Observable) obj);
            }
        }).subscribe(new com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda39(this), new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda9
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.ble.BLEConnection.TAG, "Subscription error");
            }
        }));
        this.mConnectionSubscription.add(rxBleDevice.observeConnectionStateChanges().subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda0
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m616lambda$connect$9$comlogitechuecenturionbleBLEConnection((com.polidea.rxandroidble.RxBleConnection.RxBleConnectionState) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda5
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.ble.BLEConnection.lambda$connect$10((java.lang.Throwable) obj);
            }
        }));
        setConnectionState(1);
        publishSubject.onNext(null);
    }

    /* JADX INFO: renamed from: lambda$connect$9$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ void m616lambda$connect$9$comlogitechuecenturionbleBLEConnection(com.polidea.rxandroidble.RxBleConnection.RxBleConnectionState rxBleConnectionState) {
        if (rxBleConnectionState == com.polidea.rxandroidble.RxBleConnection.RxBleConnectionState.DISCONNECTED) {
            com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "GATT connection disconnected");
            disconnect().compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda17
                @Override // rx.functions.Action1
                public final void call(java.lang.Object obj) {
                    com.logitech.ue.centurion.ble.BLEConnection.lambda$connect$7((java.lang.Void) obj);
                }
            }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda10
                @Override // rx.functions.Action1
                public final void call(java.lang.Object obj) {
                    com.logitech.ue.centurion.ble.BLEConnection.lambda$connect$8((java.lang.Throwable) obj);
                }
            });
        }
    }

    /* JADX INFO: renamed from: lambda$connect$14$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ void m614lambda$connect$14$comlogitechuecenturionbleBLEConnection(rx.subjects.PublishSubject publishSubject, java.lang.Throwable th) {
        com.logitech.ue.centurion.utils.LogUtils.LOGW(TAG, "GATT connection failed");
        publishSubject.onError(th);
        this.mConnectionSubscription.unsubscribe();
        if (this.mConnectionState == 1) {
            disconnect().compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda16
                @Override // rx.functions.Action1
                public final void call(java.lang.Object obj) {
                    com.logitech.ue.centurion.ble.BLEConnection.lambda$connect$12((java.lang.Void) obj);
                }
            }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda6
                @Override // rx.functions.Action1
                public final void call(java.lang.Object obj) {
                    com.logitech.ue.centurion.ble.BLEConnection.lambda$connect$13((java.lang.Throwable) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMtuChange(java.lang.Integer num) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "MTU size changed: " + num);
    }

    @Override // com.logitech.ue.centurion.connection.IConnection
    public rx.Observable<java.lang.Void> disconnect() {
        return rx.Observable.defer(new rx.functions.Func0() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda20
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public final java.lang.Object call() {
                return this.f$0.m620lambda$disconnect$16$comlogitechuecenturionbleBLEConnection();
            }
        });
    }

    /* JADX INFO: renamed from: lambda$disconnect$16$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m620lambda$disconnect$16$comlogitechuecenturionbleBLEConnection() {
        this.mConnectionSubscription.unsubscribe();
        if (this.mConnectionState == 3) {
            return rx.Observable.error(new java.lang.IllegalStateException("Connection is not connected"));
        }
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Disconnected from device");
        setConnectionState(3);
        return rx.Observable.just(null);
    }

    @Override // com.logitech.ue.centurion.connection.IConnection
    public rx.Observable<java.lang.Void> sendData(final byte[] bArr) {
        com.google.common.base.Preconditions.checkNotNull(bArr);
        return rx.Observable.defer(new rx.functions.Func0() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda21
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public final java.lang.Object call() {
                return this.f$0.m626lambda$sendData$20$comlogitechuecenturionbleBLEConnection(bArr);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$sendData$20$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m626lambda$sendData$20$comlogitechuecenturionbleBLEConnection(final byte[] bArr) {
        try {
            checkConnection();
            return rx.Observable.combineLatest(this.mConnection.getCharacteristic(WRITE_CHARACTERISTIC), rx.Observable.from(wrapCPLData(bArr)), new rx.functions.Func2() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda37
                @Override // rx.functions.Func2
                public final java.lang.Object call(java.lang.Object obj, java.lang.Object obj2) {
                    return new android.util.Pair((android.bluetooth.BluetoothGattCharacteristic) obj, (byte[]) obj2);
                }
            }).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda23
                @Override // rx.functions.Func1
                public final java.lang.Object call(java.lang.Object obj) {
                    return this.f$0.m624lambda$sendData$17$comlogitechuecenturionbleBLEConnection((android.util.Pair) obj);
                }
            }).doOnNext(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda33
                @Override // rx.functions.Action1
                public final void call(java.lang.Object obj) {
                    this.f$0.onDataSent((byte[]) obj);
                }
            }).doOnError(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda2
                @Override // rx.functions.Action1
                public final void call(java.lang.Object obj) {
                    this.f$0.m625lambda$sendData$18$comlogitechuecenturionbleBLEConnection(bArr, (java.lang.Throwable) obj);
                }
            }).map(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda36
                @Override // rx.functions.Func1
                public final java.lang.Object call(java.lang.Object obj) {
                    return com.logitech.ue.centurion.ble.BLEConnection.lambda$sendData$19((byte[]) obj);
                }
            });
        } catch (java.lang.Exception e) {
            return rx.Observable.error(e);
        }
    }

    /* JADX INFO: renamed from: lambda$sendData$17$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m624lambda$sendData$17$comlogitechuecenturionbleBLEConnection(android.util.Pair pair) {
        ((android.bluetooth.BluetoothGattCharacteristic) pair.first).setWriteType(1);
        return this.mConnection.writeCharacteristic(WRITE_CHARACTERISTIC, (byte[]) pair.second);
    }

    /* JADX INFO: renamed from: lambda$sendData$18$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ void m625lambda$sendData$18$comlogitechuecenturionbleBLEConnection(byte[] bArr, java.lang.Throwable th) {
        com.logitech.ue.centurion.utils.LogUtils.LOGW(TAG, java.lang.String.format("Failed to send data. Address: %s Sent: %s", this.mMac, com.logitech.ue.centurion.utils.Utils.byteArrayToFancyHexString(bArr)));
    }

    static /* synthetic */ java.lang.Void lambda$sendData$19(byte[] bArr) {
        return null;
    }

    @Override // com.logitech.ue.centurion.connection.IConnection
    public com.logitech.ue.centurion.connection.ConnectionType getConnectionType() {
        return com.logitech.ue.centurion.connection.ConnectionType.BLE;
    }

    private void readLongResponseData(final int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Address: %s Read long response: %d", this.mMac, java.lang.Integer.valueOf(i)));
        this.mConnection.readCharacteristic(LONG_RESPONSE_CHARACTERISTIC).subscribeOn(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().io()).observeOn(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().io()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda41
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m623xec950a8b(i, (byte[]) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda15
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.ble.BLEConnection.lambda$readLongResponseData$22((java.lang.Throwable) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$readLongResponseData$21$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ void m623xec950a8b(int i, byte[] bArr) {
        onDataReceived(java.util.Arrays.copyOf(bArr, i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onWriteCharacteristicNotification(byte[] bArr) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format("Address: %s Write characteristic notified: %s", this.mMac, com.logitech.ue.centurion.utils.Utils.byteArrayToFancyHexString(bArr)));
        onDataReceived(bArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLongResponseCharacteristicNotification(byte[] bArr) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format("Address: %s Long response characteristic notified: %s", this.mMac, com.logitech.ue.centurion.utils.Utils.byteArrayToFancyHexString(bArr)));
        readLongResponseData(com.logitech.ue.centurion.utils.Utils.bytesToInt((byte) 0, (byte) 0, bArr[0], bArr[1]));
    }

    @Override // com.logitech.ue.centurion.connection.AbstractConnection, com.logitech.ue.centurion.connection.IConnection
    public rx.Observable<java.lang.Void> authorize(final byte[] bArr) {
        com.google.common.base.Preconditions.checkNotNull(bArr);
        return this.mConnection.getCharacteristic(AUTH0RIZE_AND_REMOTE_POWER).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda25
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m612lambda$authorize$23$comlogitechuecenturionbleBLEConnection(bArr, (android.bluetooth.BluetoothGattCharacteristic) obj);
            }
        }).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda32
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.ble.BLEConnection.lambda$authorize$24((byte[]) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$authorize$23$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m612lambda$authorize$23$comlogitechuecenturionbleBLEConnection(byte[] bArr, android.bluetooth.BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] bArr2 = new byte[bArr.length + 1];
        java.lang.System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        bArr2[bArr.length] = FLAG_AUTHENTICATE;
        bluetoothGattCharacteristic.setWriteType(1);
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "authorize speaker: " + com.logitech.ue.centurion.utils.Utils.byteArrayToFancyHexString(bArr2));
        return this.mConnection.writeCharacteristic(bluetoothGattCharacteristic, bArr2);
    }

    static /* synthetic */ rx.Observable lambda$authorize$24(byte[] bArr) {
        return rx.Observable.just(null);
    }

    @Override // com.logitech.ue.centurion.connection.AbstractConnection, com.logitech.ue.centurion.connection.IConnection
    public rx.Observable<java.lang.Void> powerOnSpeaker(final byte[] bArr) {
        com.google.common.base.Preconditions.checkNotNull(bArr);
        return this.mConnection.getCharacteristic(AUTH0RIZE_AND_REMOTE_POWER).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda27
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m622x633aa351(bArr, (android.bluetooth.BluetoothGattCharacteristic) obj);
            }
        }).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda35
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.ble.BLEConnection.lambda$powerOnSpeaker$26((byte[]) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$powerOnSpeaker$25$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m622x633aa351(byte[] bArr, android.bluetooth.BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] bArr2 = new byte[bArr.length + 1];
        java.lang.System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        bArr2[bArr.length] = -1;
        bluetoothGattCharacteristic.setWriteType(1);
        return this.mConnection.writeCharacteristic(bluetoothGattCharacteristic, bArr2);
    }

    static /* synthetic */ rx.Observable lambda$powerOnSpeaker$26(byte[] bArr) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "success writing power characterstic: " + bArr);
        return rx.Observable.just(null);
    }

    @Override // com.logitech.ue.centurion.connection.AbstractConnection, com.logitech.ue.centurion.connection.IConnection
    public rx.Observable<java.lang.Void> powerOffSpeaker(final byte[] bArr) {
        com.google.common.base.Preconditions.checkNotNull(bArr);
        return this.mConnection.getCharacteristic(AUTH0RIZE_AND_REMOTE_POWER).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda26
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m621xa3d060d3(bArr, (android.bluetooth.BluetoothGattCharacteristic) obj);
            }
        }).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda34
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.ble.BLEConnection.lambda$powerOffSpeaker$28((byte[]) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$powerOffSpeaker$27$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m621xa3d060d3(byte[] bArr, android.bluetooth.BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "uuid of power characterstic " + bluetoothGattCharacteristic.getUuid().toString());
        byte[] bArr2 = new byte[bArr.length + 1];
        java.lang.System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        bArr2[bArr.length] = FLAG_POWER_OFF;
        bluetoothGattCharacteristic.setWriteType(1);
        return this.mConnection.writeCharacteristic(bluetoothGattCharacteristic, bArr2);
    }

    static /* synthetic */ rx.Observable lambda$powerOffSpeaker$28(byte[] bArr) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "success writing power characterstic: " + bArr);
        return rx.Observable.just(null);
    }

    public rx.Observable<java.lang.Void> connectInSpeakerOffMode() {
        this.mConnectionState = 0;
        final com.polidea.rxandroidble.RxBleDevice bleDevice = com.logitech.ue.centurion.ble.BLERxClient.get().getBleDevice(this.mMac.toString());
        final rx.subjects.PublishSubject publishSubjectCreate = rx.subjects.PublishSubject.create();
        bleDevice.establishConnection(false).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda24
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m618xab0eab03(publishSubjectCreate, bleDevice, (com.polidea.rxandroidble.RxBleConnection) obj);
            }
        }).compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda3
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.ble.BLEConnection.TAG, "success connecting");
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda1
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m619xe0503005(publishSubjectCreate, (java.lang.Throwable) obj);
            }
        });
        return publishSubjectCreate;
    }

    /* JADX INFO: renamed from: lambda$connectInSpeakerOffMode$35$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ rx.Observable m618xab0eab03(rx.subjects.PublishSubject publishSubject, com.polidea.rxandroidble.RxBleDevice rxBleDevice, com.polidea.rxandroidble.RxBleConnection rxBleConnection) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "connected");
        publishSubject.onNext(null);
        this.mConnection = rxBleConnection;
        this.mConnectionSubscription.add(rxBleConnection.setupNotification(AUTH0RIZE_AND_REMOTE_POWER, com.polidea.rxandroidble.NotificationSetupMode.COMPAT).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda31
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.ble.BLEConnection.lambda$connectInSpeakerOffMode$29((rx.Observable) obj);
            }
        }).subscribe(new com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda39(this), new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda12
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.utils.LogUtils.LOGD(com.logitech.ue.centurion.ble.BLEConnection.TAG, "Subscription error");
            }
        }));
        this.mConnectionSubscription.add(rxBleDevice.observeConnectionStateChanges().subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda11
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m617x75cd2601((com.polidea.rxandroidble.RxBleConnection.RxBleConnectionState) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda14
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                com.logitech.ue.centurion.ble.BLEConnection.lambda$connectInSpeakerOffMode$34((java.lang.Throwable) obj);
            }
        }));
        return rx.Observable.just(rxBleConnection);
    }

    /* JADX INFO: renamed from: lambda$connectInSpeakerOffMode$33$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ void m617x75cd2601(com.polidea.rxandroidble.RxBleConnection.RxBleConnectionState rxBleConnectionState) {
        if (rxBleConnectionState == com.polidea.rxandroidble.RxBleConnection.RxBleConnectionState.DISCONNECTED) {
            com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "GATT connection disconnected");
            disconnect().compose(com.logitech.ue.centurion.threading.CenturionSchedulerProvider.get().applyConnectivityTaskSchedulers()).subscribe(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda18
                @Override // rx.functions.Action1
                public final void call(java.lang.Object obj) {
                    com.logitech.ue.centurion.ble.BLEConnection.lambda$connectInSpeakerOffMode$31((java.lang.Void) obj);
                }
            }, new rx.functions.Action1() { // from class: com.logitech.ue.centurion.ble.BLEConnection$$ExternalSyntheticLambda13
                @Override // rx.functions.Action1
                public final void call(java.lang.Object obj) {
                    com.logitech.ue.centurion.ble.BLEConnection.lambda$connectInSpeakerOffMode$32((java.lang.Throwable) obj);
                }
            });
        }
    }

    /* JADX INFO: renamed from: lambda$connectInSpeakerOffMode$37$com-logitech-ue-centurion-ble-BLEConnection, reason: not valid java name */
    /* synthetic */ void m619xe0503005(rx.subjects.PublishSubject publishSubject, java.lang.Throwable th) {
        this.mConnectionSubscription.unsubscribe();
        publishSubject.onError(th);
        com.logitech.ue.centurion.utils.LogUtils.LOGE(TAG, "Connection fail", th);
    }
}
