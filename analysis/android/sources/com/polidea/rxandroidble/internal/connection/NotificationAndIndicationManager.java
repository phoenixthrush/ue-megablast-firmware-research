package com.polidea.rxandroidble.internal.connection;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes2.dex */
@com.polidea.rxandroidble.internal.connection.ConnectionScope
public class NotificationAndIndicationManager {
    static final java.util.UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = java.util.UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private final java.util.Map<com.polidea.rxandroidble.internal.util.CharacteristicNotificationId, com.polidea.rxandroidble.internal.util.ActiveCharacteristicNotification> activeNotificationObservableMap = new java.util.HashMap();
    private final android.bluetooth.BluetoothGatt bluetoothGatt;
    private final byte[] configDisable;
    private final byte[] configEnableIndication;
    private final byte[] configEnableNotification;
    private final com.polidea.rxandroidble.internal.connection.DescriptorWriter descriptorWriter;
    private final com.polidea.rxandroidble.internal.connection.RxBleGattCallback gattCallback;

    @javax.inject.Inject
    NotificationAndIndicationManager(@javax.inject.Named(com.polidea.rxandroidble.ClientComponent.BluetoothConstants.ENABLE_NOTIFICATION_VALUE) byte[] bArr, @javax.inject.Named(com.polidea.rxandroidble.ClientComponent.BluetoothConstants.ENABLE_INDICATION_VALUE) byte[] bArr2, @javax.inject.Named(com.polidea.rxandroidble.ClientComponent.BluetoothConstants.DISABLE_NOTIFICATION_VALUE) byte[] bArr3, android.bluetooth.BluetoothGatt bluetoothGatt, com.polidea.rxandroidble.internal.connection.RxBleGattCallback rxBleGattCallback, com.polidea.rxandroidble.internal.connection.DescriptorWriter descriptorWriter) {
        this.configEnableNotification = bArr;
        this.configEnableIndication = bArr2;
        this.configDisable = bArr3;
        this.bluetoothGatt = bluetoothGatt;
        this.gattCallback = rxBleGattCallback;
        this.descriptorWriter = descriptorWriter;
    }

    rx.Observable<rx.Observable<byte[]>> setupServerInitiatedCharacteristicRead(final android.bluetooth.BluetoothGattCharacteristic bluetoothGattCharacteristic, final com.polidea.rxandroidble.NotificationSetupMode notificationSetupMode, final boolean z) {
        return rx.Observable.defer(new rx.functions.Func0<rx.Observable<rx.Observable<byte[]>>>() { // from class: com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.1
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public rx.Observable<rx.Observable<byte[]>> call() {
                synchronized (com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.activeNotificationObservableMap) {
                    final com.polidea.rxandroidble.internal.util.CharacteristicNotificationId characteristicNotificationId = new com.polidea.rxandroidble.internal.util.CharacteristicNotificationId(bluetoothGattCharacteristic.getUuid(), java.lang.Integer.valueOf(bluetoothGattCharacteristic.getInstanceId()));
                    com.polidea.rxandroidble.internal.util.ActiveCharacteristicNotification activeCharacteristicNotification = (com.polidea.rxandroidble.internal.util.ActiveCharacteristicNotification) com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.activeNotificationObservableMap.get(characteristicNotificationId);
                    boolean z2 = true;
                    if (activeCharacteristicNotification != null) {
                        if (activeCharacteristicNotification.isIndication == z) {
                            return activeCharacteristicNotification.notificationObservable;
                        }
                        java.util.UUID uuid = bluetoothGattCharacteristic.getUuid();
                        if (z) {
                            z2 = false;
                        }
                        return rx.Observable.error(new com.polidea.rxandroidble.exceptions.BleConflictingNotificationAlreadySetException(uuid, z2));
                    }
                    byte[] bArr = z ? com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.configEnableIndication : com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.configEnableNotification;
                    final rx.subjects.PublishSubject publishSubjectCreate = rx.subjects.PublishSubject.create();
                    rx.Observable<rx.Observable<byte[]>> observableRefCount = com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.setCharacteristicNotification(com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.bluetoothGatt, bluetoothGattCharacteristic, true).compose(com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.setupModeTransformer(com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.descriptorWriter, bluetoothGattCharacteristic, bArr, notificationSetupMode)).andThen(com.polidea.rxandroidble.internal.util.ObservableUtil.justOnNext(com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.observeOnCharacteristicChangeCallbacks(com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.gattCallback, characteristicNotificationId).takeUntil(publishSubjectCreate))).doOnUnsubscribe(new rx.functions.Action0() { // from class: com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.1.1
                        @Override // rx.functions.Action0
                        public void call() {
                            publishSubjectCreate.onCompleted();
                            synchronized (com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.activeNotificationObservableMap) {
                                com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.activeNotificationObservableMap.remove(characteristicNotificationId);
                            }
                            com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.setCharacteristicNotification(com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.bluetoothGatt, bluetoothGattCharacteristic, false).compose(com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.setupModeTransformer(com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.descriptorWriter, bluetoothGattCharacteristic, com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.configDisable, notificationSetupMode)).subscribe(rx.functions.Actions.empty(), rx.functions.Actions.toAction1(rx.functions.Actions.empty()));
                        }
                    }).mergeWith(com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.gattCallback.observeDisconnect()).replay(1).refCount();
                    com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.this.activeNotificationObservableMap.put(characteristicNotificationId, new com.polidea.rxandroidble.internal.util.ActiveCharacteristicNotification(observableRefCount, z));
                    return observableRefCount;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static rx.Completable setCharacteristicNotification(final android.bluetooth.BluetoothGatt bluetoothGatt, final android.bluetooth.BluetoothGattCharacteristic bluetoothGattCharacteristic, final boolean z) {
        return rx.Completable.fromAction(new rx.functions.Action0() { // from class: com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.2
            @Override // rx.functions.Action0
            public void call() {
                if (!bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, z)) {
                    throw new com.polidea.rxandroidble.exceptions.BleCannotSetCharacteristicNotificationException(bluetoothGattCharacteristic, 1, null);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static rx.Completable.Transformer setupModeTransformer(final com.polidea.rxandroidble.internal.connection.DescriptorWriter descriptorWriter, final android.bluetooth.BluetoothGattCharacteristic bluetoothGattCharacteristic, final byte[] bArr, final com.polidea.rxandroidble.NotificationSetupMode notificationSetupMode) {
        return new rx.Completable.Transformer() { // from class: com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.3
            @Override // rx.functions.Func1
            public rx.Completable call(rx.Completable completable) {
                return notificationSetupMode == com.polidea.rxandroidble.NotificationSetupMode.DEFAULT ? completable.andThen(com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.writeClientCharacteristicConfig(bluetoothGattCharacteristic, descriptorWriter, bArr)) : completable;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static rx.Observable<byte[]> observeOnCharacteristicChangeCallbacks(com.polidea.rxandroidble.internal.connection.RxBleGattCallback rxBleGattCallback, final com.polidea.rxandroidble.internal.util.CharacteristicNotificationId characteristicNotificationId) {
        return rxBleGattCallback.getOnCharacteristicChanged().filter(new rx.functions.Func1<com.polidea.rxandroidble.internal.util.CharacteristicChangedEvent, java.lang.Boolean>() { // from class: com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.5
            @Override // rx.functions.Func1
            public java.lang.Boolean call(com.polidea.rxandroidble.internal.util.CharacteristicChangedEvent characteristicChangedEvent) {
                return java.lang.Boolean.valueOf(characteristicChangedEvent.equals(characteristicNotificationId));
            }
        }).map(new rx.functions.Func1<com.polidea.rxandroidble.internal.util.CharacteristicChangedEvent, byte[]>() { // from class: com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.4
            @Override // rx.functions.Func1
            public byte[] call(com.polidea.rxandroidble.internal.util.CharacteristicChangedEvent characteristicChangedEvent) {
                return characteristicChangedEvent.data;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static rx.Completable writeClientCharacteristicConfig(final android.bluetooth.BluetoothGattCharacteristic bluetoothGattCharacteristic, com.polidea.rxandroidble.internal.connection.DescriptorWriter descriptorWriter, byte[] bArr) {
        android.bluetooth.BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
        if (descriptor == null) {
            return rx.Completable.error(new com.polidea.rxandroidble.exceptions.BleCannotSetCharacteristicNotificationException(bluetoothGattCharacteristic, 2, null));
        }
        return descriptorWriter.writeDescriptor(descriptor, bArr).toCompletable().onErrorResumeNext(new rx.functions.Func1<java.lang.Throwable, rx.Completable>() { // from class: com.polidea.rxandroidble.internal.connection.NotificationAndIndicationManager.6
            @Override // rx.functions.Func1
            public rx.Completable call(java.lang.Throwable th) {
                return rx.Completable.error(new com.polidea.rxandroidble.exceptions.BleCannotSetCharacteristicNotificationException(bluetoothGattCharacteristic, 3, th));
            }
        });
    }
}
