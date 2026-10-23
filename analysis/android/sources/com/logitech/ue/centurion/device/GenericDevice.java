package com.logitech.ue.centurion.device;

/* JADX INFO: loaded from: classes2.dex */
public class GenericDevice implements com.logitech.ue.centurion.device.Device {
    private static final java.lang.String TAG = "GenericDevice";
    protected final com.logitech.ue.centurion.conductor.IConductor mConductor;
    protected final com.logitech.ue.centurion.connection.IConnection mConnection;
    protected final java.util.Date mCreated = new java.util.Date();
    protected final com.logitech.ue.centurion.device.IFeatureMapper mFeatureMapper = new com.logitech.ue.centurion.device.CenturionFeatureMapper();
    protected final com.logitech.ue.centurion.caching.Cacher mCacher = new com.logitech.ue.centurion.caching.Cacher();

    static /* synthetic */ java.lang.Object lambda$executeMessage$5(com.logitech.ue.centurion.device.IMessage iMessage) {
        return null;
    }

    public GenericDevice(com.logitech.ue.centurion.connection.IConnection iConnection, com.logitech.ue.centurion.conductor.IConductor iConductor) {
        this.mConnection = (com.logitech.ue.centurion.connection.IConnection) com.google.common.base.Preconditions.checkNotNull(iConnection);
        this.mConductor = (com.logitech.ue.centurion.conductor.IConductor) com.google.common.base.Preconditions.checkNotNull(iConductor);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public com.logitech.ue.centurion.connection.IConnection getConnection() {
        return this.mConnection;
    }

    @Override // com.logitech.ue.centurion.device.Device
    public com.logitech.ue.centurion.conductor.IConductor getConductor() {
        return this.mConductor;
    }

    @Override // com.logitech.ue.centurion.device.Device
    public com.logitech.ue.centurion.device.IFeatureMapper getFeatureMapper() {
        return this.mFeatureMapper;
    }

    @Override // com.logitech.ue.centurion.device.Device
    public java.util.Date getCreationDate() {
        return this.mCreated;
    }

    @Override // com.logitech.ue.centurion.device.Device
    public java.lang.String getAddress() {
        return getConnection().getAddress();
    }

    @Override // com.logitech.ue.centurion.device.Device
    public boolean isFeatureSupported(int i) {
        return this.mFeatureMapper.isFeatureSupported(i);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.FeatureInfo> getFeature(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get features");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(0), com.logitech.ue.centurion.feature.Features.Root.Functions.GET_FEATURE, new com.logitech.ue.centurion.DataPacker().add((short) i).build()), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda6
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.FeatureInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.ProtocolInfo> getProtocolVersion() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get protocol version");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(0), com.logitech.ue.centurion.feature.Features.Root.Functions.GET_PROTOCOL_VERSION), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda13
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.ProtocolInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.device.IFeatureMapper> discoverFeatures() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Discover features");
        this.mFeatureMapper.invalidate();
        return getFeature(1).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda21
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m642x8b1389ac((com.logitech.ue.centurion.devicedata.FeatureInfo) obj);
            }
        }).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda30
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m643xc4de2b8b((java.lang.Integer) obj);
            }
        }).map(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda31
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m644xfea8cd6a((java.util.List) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$discoverFeatures$0$com-logitech-ue-centurion-device-GenericDevice, reason: not valid java name */
    /* synthetic */ rx.Observable m642x8b1389ac(com.logitech.ue.centurion.devicedata.FeatureInfo featureInfo) {
        this.mFeatureMapper.addFeatureMapping(new com.logitech.ue.centurion.feature.Feature(1, featureInfo.getType(), featureInfo.getVersion()), featureInfo.getIndex());
        return getFeatureCount();
    }

    /* JADX INFO: renamed from: lambda$discoverFeatures$1$com-logitech-ue-centurion-device-GenericDevice, reason: not valid java name */
    /* synthetic */ rx.Observable m643xc4de2b8b(java.lang.Integer num) {
        return fetchFeaturesList(0, num.intValue());
    }

    /* JADX INFO: renamed from: lambda$discoverFeatures$2$com-logitech-ue-centurion-device-GenericDevice, reason: not valid java name */
    /* synthetic */ com.logitech.ue.centurion.device.IFeatureMapper m644xfea8cd6a(java.util.List list) {
        for (int i = 0; i < list.size(); i++) {
            this.mFeatureMapper.addFeatureMapping((com.logitech.ue.centurion.feature.Feature) list.get(i), i);
        }
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Update features mapper. Features: " + this.mFeatureMapper.toString());
        return this.mFeatureMapper;
    }

    private rx.Observable<java.util.List<com.logitech.ue.centurion.feature.Feature>> fetchFeaturesList(final int i, final int i2) {
        final java.util.ArrayList arrayList = new java.util.ArrayList();
        return getFeatures(i).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda32
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m646xc2a893b4(arrayList, i, i2, (java.util.List) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$fetchFeaturesList$3$com-logitech-ue-centurion-device-GenericDevice, reason: not valid java name */
    /* synthetic */ rx.Observable m646xc2a893b4(java.util.List list, int i, int i2, java.util.List list2) {
        list.addAll(list2);
        if (list2.size() + i < i2) {
            return fetchFeaturesList(i + list2.size(), i2);
        }
        return rx.Observable.just(list);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getFeatureCount() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get features count");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1), com.logitech.ue.centurion.feature.Features.FeatureSet.Functions.GET_FEATURE_COUNT), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE, com.logitech.ue.centurion.caching.CacheKeys.FeatureCount.name(), 1);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.util.List<com.logitech.ue.centurion.feature.Feature>> getFeatures(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. get the feature list");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1), com.logitech.ue.centurion.feature.Features.FeatureSet.Functions.GET_FEATURE_ID, new com.logitech.ue.centurion.DataPacker().addUnsignedByte(i).build()), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda20
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.parcel.FeatureListResponseParcel.parcePayload((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.HardwareInfo> getHardwareInfo() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get hardware info");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(256), com.logitech.ue.centurion.feature.Features.DeviceInfo.Functions.GET_HARDWARE_INFO), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda9
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.HardwareInfo.buildFromPaydata((byte[]) obj);
            }
        }, com.logitech.ue.centurion.caching.CacheKeys.HardwareInfo.name(), 1);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.String> getFirmwareRevision() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get firmware revision");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(256), com.logitech.ue.centurion.feature.Features.DeviceInfo.Functions.GET_FIRMWARE_VERSION), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda28
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.parcel.VersionResponseParcel.parcePayload((byte[]) obj);
            }
        }, com.logitech.ue.centurion.caching.CacheKeys.FirmwareRevision.name(), 1);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.String> getSerialNumber() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get serial number");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(256), com.logitech.ue.centurion.feature.Features.DeviceInfo.Functions.GET_SERIAL_NUMBER), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda26
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.parcel.SerialResponseParcel.parcePayload((byte[]) obj);
            }
        }, com.logitech.ue.centurion.caching.CacheKeys.SerialNumber.name(), 1);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.String> getName() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get name");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(257), com.logitech.ue.centurion.feature.Features.DeviceName.Functions.GET_NAME), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda25
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.parcel.NameResponseParcel.parcePayload((byte[]) obj);
            }
        }, com.logitech.ue.centurion.caching.CacheKeys.Name.name(), 2);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setName(java.lang.String str) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set name");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(257), com.logitech.ue.centurion.feature.Features.DeviceName.Functions.SET_NAME, new com.logitech.ue.centurion.DataPacker().add((java.lang.String) com.google.common.base.Preconditions.checkNotNull(str)).build()), (rx.functions.Func0) null, (rx.functions.Func1) null, new com.logitech.ue.centurion.caching.SetterUpdateRule(str, com.logitech.ue.centurion.caching.CacheKeys.Name.name(), 2, this.mCacher));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.String> getDefaultName() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get default");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(257), com.logitech.ue.centurion.feature.Features.DeviceName.Functions.GET_DEFAULT_NAME), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda27.INSTANCE, com.logitech.ue.centurion.caching.CacheKeys.DefaultName.name(), 1);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.String> getMaxNameLength() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get max name length");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(257), com.logitech.ue.centurion.feature.Features.DeviceName.Functions.GET_NAME_MAX_LENGTH), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda27.INSTANCE, com.logitech.ue.centurion.caching.CacheKeys.NameMaxLength.name(), 1);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.BatteryStatus> getBatteryStatus() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get battery ");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(260), com.logitech.ue.centurion.feature.Features.BatterySOC.Functions.GET_BATTERY_STATUS), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda1
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.BatteryStatus.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Boolean> getBLERemoteOn() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get Ble power on");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(262), com.logitech.ue.centurion.feature.Features.RemotePowerSet.Functions.GET_BLE_REMOTE_ON), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda17
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.parcel.BlePowerOnResponseParcel.parcePayload((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setBLERemoteOn(boolean z) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set ble remote on " + z);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(262), com.logitech.ue.centurion.feature.Features.RemotePowerSet.Functions.SET_BLE_REMOTE_ON, new com.logitech.ue.centurion.DataPacker().add(!z).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setVoiceCommand(boolean z) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set voice command " + z);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(262), com.logitech.ue.centurion.feature.Features.RemotePowerSet.Functions.SET_VOICE_COMMAND, new com.logitech.ue.centurion.DataPacker().add(z).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Boolean> getPowerState() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get power state ");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(263), com.logitech.ue.centurion.feature.Features.PowerState.Functions.GET_POWER_STATE), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda23
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.parcel.InvertedBooleanResponseParcel.parcePayload((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getVolumeLevel() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get volume level ");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(512), com.logitech.ue.centurion.feature.Features.VolumeSet.Functions.GET_VOLUME_LEVEL), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setVolumeLevel(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set battery to " + i);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(512), com.logitech.ue.centurion.feature.Features.VolumeSet.Functions.SET_VOLUME_LEVEL, new com.logitech.ue.centurion.DataPacker().add((byte) i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.EQInfo> getEQ() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get eq");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(513), com.logitech.ue.centurion.feature.Features.EQSet.Functions.GET_EQ), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda5
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.EQInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setEQ(int i, int i2, int i3, int i4, int i5, int i6) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set EQ mode: " + i);
        int[] iArr = {i2, i3, i4, i5, i6};
        for (int i7 = 0; i7 < 5; i7++) {
            int i8 = iArr[i7];
            com.google.common.base.Preconditions.checkArgument(i8 >= -128 && i8 <= 127);
        }
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(513), com.logitech.ue.centurion.feature.Features.EQSet.Functions.SET_EQ, new com.logitech.ue.centurion.DataPacker().add((byte) i).add((byte) i2).add((byte) i3).add((byte) i4).add((byte) i5).add((byte) i6).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setVoiceAssistantMode(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set voice assistant mode to " + com.logitech.ue.centurion.device.VoiceAssistantMode.getName(i));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(768), com.logitech.ue.centurion.feature.Features.VoiceAssistant.Functions.SET_MODE, new com.logitech.ue.centurion.DataPacker().add((byte) i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getVoiceAssistantMode() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get voice assistant");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(768), com.logitech.ue.centurion.feature.Features.VoiceAssistant.Functions.GET_MODE), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda22.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> startVoiceAssistantSession() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Start voice assistant session");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(768), com.logitech.ue.centurion.feature.Features.VoiceAssistant.Functions.START_SESSION));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> stopVoiceAssistantSession() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Stop voice assistant session");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(768), com.logitech.ue.centurion.feature.Features.VoiceAssistant.Functions.STOP_SESSION));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setAVSRefreshToken(java.lang.String str) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set AVS refreshToken");
        byte[] bytes = ((java.lang.String) com.google.common.base.Preconditions.checkNotNull(str)).getBytes(com.google.common.base.Charsets.UTF_8);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.SET_AVS_REFRESH_TOKEN, new com.logitech.ue.centurion.DataPacker().add(com.logitech.ue.centurion.utils.Utils.intToByte(bytes.length >> 8)).add(com.logitech.ue.centurion.utils.Utils.intToByte(bytes.length)).add(bytes).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.String> getAVSRefreshToken() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get AVS token");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.GET_AVS_REFRESH_TOKEN), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda24
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.parcel.LongStringResponseParcel.parcePayload((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setAVSID(java.lang.String str) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Create request. Set AVS client ID: %s", com.google.common.base.Preconditions.checkNotNull(str)));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.SET_AVS_ID, new com.logitech.ue.centurion.DataPacker().add(str).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.String> getAVSID() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get AVS ID");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.GET_AVS_ID), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda27.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setAudioStreamData(java.lang.String str, int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set AVS stream token");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.SET_AVS_AUDIO_STREAM_DATA, new com.logitech.ue.centurion.DataPacker().add((java.lang.String) com.google.common.base.Preconditions.checkNotNull(str)).add(i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.TokenInfo> getAudioStreamData() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get AVS audio stream token");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.GET_AVS_AUDIO_STREAM_DATA), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda15
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.TokenInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setAVSLocale(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set AVS Locale " + i);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.SET_LOCALE, new com.logitech.ue.centurion.DataPacker().addUnsignedByte(i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getAVSLocale() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get AVS Locale");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.GET_LOCALE), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> triggerVoiceCommand() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Trigger AVS voice command");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.TRIGGER_VOICE_COMMAND));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setVoiceCommandListen(boolean z) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set voice command listen. Enable mute: " + z);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.SET_VOICE_COMMAND_LISTEN));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getAVSClientState() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get AVS client state");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.GET_AVS_CLIENT_STATE), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> logoutAVS() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Logout AVS");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.SET_AVS_REFRESH_TOKEN, new com.logitech.ue.centurion.DataPacker().add(com.logitech.ue.centurion.utils.Utils.intToByte(0)).add(com.logitech.ue.centurion.utils.Utils.intToByte(0)).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setTriggerWordLanguage(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set AVS Trigger Word Language: " + i);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(770), com.logitech.ue.centurion.feature.Features.FarField.Functions.SET_TRIG_WORD_LANGUAGE, new com.logitech.ue.centurion.DataPacker().addUnsignedByte(i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getTriggerWordLanguage() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get AVS Trigger Word Language");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(770), com.logitech.ue.centurion.feature.Features.FarField.Functions.GET_TRIG_WORD_LANGUAGE), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setFarFieldState(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set far field state to" + java.lang.String.format("%8s", java.lang.Integer.toBinaryString(i)).replace(' ', '0'));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(770), com.logitech.ue.centurion.feature.Features.FarField.Functions.SET_STATE, new com.logitech.ue.centurion.DataPacker().addUnsignedByte(i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getFarFieldState() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get far field state");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(770), com.logitech.ue.centurion.feature.Features.FarField.Functions.GET_STATE), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setDeviceDiscoverable() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Start device discoverable");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.BluetoothCtrl.ID), com.logitech.ue.centurion.feature.Features.BluetoothCtrl.Functions.SET_DEVICE_DISCOVERABLE));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getBTState() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get device bluetooth state");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.BluetoothCtrl.ID), com.logitech.ue.centurion.feature.Features.BluetoothCtrl.Functions.GET_BT_STATE), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> startWiFiScanning() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Start WiFi scanning");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1024), com.logitech.ue.centurion.feature.Features.WiFiScan.Functions.START_SCANNING), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.WiFiRegionInfo> getWiFiRegion() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get WiFi Region");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1024), com.logitech.ue.centurion.feature.Features.WiFiScan.Functions.GET_REGION), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda16
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.WiFiRegionInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setWiFiRegion(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set WiFi Region");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1024), com.logitech.ue.centurion.feature.Features.WiFiScan.Functions.SET_REGION, new com.logitech.ue.centurion.DataPacker().add(i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getAssociatedCount() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get WiFi associated count");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1025), com.logitech.ue.centurion.feature.Features.WiFiAssociate.Functions.GET_ASSOCIATED_COUNT), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.AssociationInfo> getAssociatedInfo(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get WiFi associated info");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1025), com.logitech.ue.centurion.feature.Features.WiFiAssociate.Functions.GET_ASSOCIATED_INFO, new byte[]{(byte) i}), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda36
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.AssociationInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setAuthentication(int i, java.lang.String str) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set Authentication");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1025), com.logitech.ue.centurion.feature.Features.WiFiAssociate.Functions.SET_AUTHENTICATION, new com.logitech.ue.centurion.DataPacker().add((byte) i).add((java.lang.String) com.google.common.base.Preconditions.checkNotNull(str)).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> associateWiFi(int i, java.lang.String str) {
        if (((java.lang.String) com.google.common.base.Preconditions.checkNotNull(str)).length() > 32) {
            throw new java.lang.IllegalArgumentException("SSID can't contain more then 32 characters");
        }
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Associate WiFi");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1025), com.logitech.ue.centurion.feature.Features.WiFiAssociate.Functions.ASSOCIATE, new com.logitech.ue.centurion.DataPacker().add((byte) i).add(str).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> associateWiFi(int i, java.lang.String str, int i2, byte[] bArr) {
        if (((java.lang.String) com.google.common.base.Preconditions.checkNotNull(str)).length() > 32) {
            throw new java.lang.IllegalArgumentException("SSID can't contain more then 32 characters");
        }
        if (((byte[]) com.google.common.base.Preconditions.checkNotNull(bArr)).length != 6) {
            throw new java.lang.IllegalArgumentException("BSSID must be 6 byte array");
        }
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Associate WiFi");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1025), com.logitech.ue.centurion.feature.Features.WiFiAssociate.Functions.ASSOCIATE_BSSID, new com.logitech.ue.centurion.DataPacker().add((byte) i).add(str).add((byte) i2).add(bArr).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> forgetWifi(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Forget WiFi");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1025), com.logitech.ue.centurion.feature.Features.WiFiAssociate.Functions.FORGET, new com.logitech.ue.centurion.DataPacker().add((byte) i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> associateWiFi(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Associate WiFi by index " + i);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1025), com.logitech.ue.centurion.feature.Features.WiFiAssociate.Functions.CONNECT_BY_INDEX, new com.logitech.ue.centurion.DataPacker().add((byte) i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.ConnectionStatus> getConnectionStatus() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get connection status");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.WiFiConnectionStatus.ID), com.logitech.ue.centurion.feature.Features.WiFiConnectionStatus.Functions.GET_STATUS), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda4
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.ConnectionStatus.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.ConnectionInfo> getConnectionInfo() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get connection info");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.WiFiConnectionStatus.ID), com.logitech.ue.centurion.feature.Features.WiFiConnectionStatus.Functions.GET_CONNECTED_SSID), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda3
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.ConnectionInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.IPInfo> getCurrentIP() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get IP");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.WiFiConnectionStatus.ID), com.logitech.ue.centurion.feature.Features.WiFiConnectionStatus.Functions.GET_CURRENT_IP), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda11
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.IPInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.utils.MAC> getMAC() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get MAC");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.WiFiConnectionStatus.ID), com.logitech.ue.centurion.feature.Features.WiFiConnectionStatus.Functions.GET_MAC), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda29.INSTANCE, com.logitech.ue.centurion.caching.CacheKeys.WiFiMAC.name(), 1);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.utils.MAC> getAPMAC() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get AP MAC");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.WiFiConnectionStatus.ID), com.logitech.ue.centurion.feature.Features.WiFiConnectionStatus.Functions.GET_AP_MAC), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda29.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.OTAInfo> getOTAInfo() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get OTA info");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.OTA.ID), com.logitech.ue.centurion.feature.Features.OTA.Functions.GET_OTA_STATUS), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda12
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.OTAInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> initiateOTACheck() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. InitiateOTACheck");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.OTA.ID), com.logitech.ue.centurion.feature.Features.OTA.Functions.INITIATE_OTA_CHECK));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setOTAInstallMode(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set OTA install mode " + com.logitech.ue.centurion.device.OTAInstallMode.getName(i));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.OTA.ID), com.logitech.ue.centurion.feature.Features.OTA.Functions.SET_OTA_INSTALL_MODE, new com.logitech.ue.centurion.DataPacker().add((byte) i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getOTAInstallMode() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get OTA install mode");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.OTA.ID), com.logitech.ue.centurion.feature.Features.OTA.Functions.GET_OTA_INSTALL_MODE), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> installOTA() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Start OTA installation");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.OTA.ID), com.logitech.ue.centurion.feature.Features.OTA.Functions.INSTALL_OTA));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setOTAServerHostname(java.lang.String str) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set OTA server host to " + ((java.lang.String) com.google.common.base.Preconditions.checkNotNull(str)));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.OTA.ID), com.logitech.ue.centurion.feature.Features.OTA.Functions.SET_UPDATE_SERVER_HOSTNAME, new com.logitech.ue.centurion.DataPacker().add((java.lang.String) com.google.common.base.Preconditions.checkNotNull(str)).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> disableOTA() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Disable OTA");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.OTA.ID), com.logitech.ue.centurion.feature.Features.OTA.Functions.DISABLE_OTA));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setAlert(int i, int i2, java.lang.String str, java.util.Date date) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Create request. Set alert. Time: %d Type: %s ID: %s Time: %s", java.lang.Integer.valueOf(i), com.logitech.ue.centurion.device.AlertType.getName(i2), com.google.common.base.Preconditions.checkNotNull(str), com.google.common.base.Preconditions.checkNotNull(date)));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.AVSAlert.ID), com.logitech.ue.centurion.feature.Features.AVSAlert.Functions.SET_ALERT, new com.logitech.ue.centurion.DataPacker().add(i).addUnsignedByte(i2).add(str).add(com.logitech.ue.centurion.utils.Utils.getISO8601StringForDate(date)).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> deleteAlert(java.lang.String str) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Create request. Delete alert. ID: %s", com.google.common.base.Preconditions.checkNotNull(str)));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.AVSAlert.ID), com.logitech.ue.centurion.feature.Features.AVSAlert.Functions.DELETE_ALERT, new com.logitech.ue.centurion.DataPacker().add(str).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getAlertCount() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get alert count");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.AVSAlert.ID), com.logitech.ue.centurion.feature.Features.AVSAlert.Functions.GET_ALERT_COUNT), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.AlertInfo> getAlertInfo(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get alert info. Index: " + i);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.AVSAlert.ID), com.logitech.ue.centurion.feature.Features.AVSAlert.Functions.GET_ALERT_INFO, new com.logitech.ue.centurion.DataPacker().addUnsignedByte(i).build()), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda35
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.AlertInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.device.Device> disconnect() {
        return com.logitech.ue.centurion.DeviceManager.getInstance().disconnectDevice(this, null);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> eraseClientId() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Erasing clientID ");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(769), com.logitech.ue.centurion.feature.Features.AVS.Functions.SET_AVS_ID, new com.logitech.ue.centurion.DataPacker().add(" ").build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setLanguage(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create Request Set earconlanguage : " + i);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(265), com.logitech.ue.centurion.feature.Features.Earcon.Functions.SET_LANGUAGE, new com.logitech.ue.centurion.DataPacker().addUnsignedByte(i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getLanguage() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Get language");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(265), com.logitech.ue.centurion.feature.Features.Earcon.Functions.GET_LANGUAGE), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setEarcon(boolean z) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Set earcon to " + z);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(265), com.logitech.ue.centurion.feature.Features.Earcon.Functions.ENABLE_SOUND, new com.logitech.ue.centurion.DataPacker().add(z).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Boolean> getEarconState() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "create request. Get Earcon state");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(265), com.logitech.ue.centurion.feature.Features.Earcon.Functions.GET_EARCON_STATE), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda18
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.parcel.BooleanResponseParcel.parcePayload((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> GetLanguageCapability() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Get language capability");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(265), com.logitech.ue.centurion.feature.Features.Earcon.Functions.GET_LANGUAGE_CAPABILITY), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda22.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setAutoSleepTime(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set Sleep Timer to " + i);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(264), com.logitech.ue.centurion.feature.Features.AutoSleep.Functions.SET_SLEEP_TIMER, new com.logitech.ue.centurion.DataPacker().addUnsignedByte(com.logitech.ue.centurion.utils.Utils.byteToUnsigned((byte) i)).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getAutoSleepTime() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. SGet Sleep Timer");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(264), com.logitech.ue.centurion.feature.Features.AutoSleep.Functions.GET_SLEEP_TIMER), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> stopWifiScan() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. stop the WifiScan ");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(1024), com.logitech.ue.centurion.feature.Features.WiFiScan.Functions.STOP_SCANNING));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> fileOpen(int i, int i2) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Create request. Open file: %d, access: %s", java.lang.Integer.valueOf(i), com.logitech.ue.centurion.device.FileAccess.getName(i2)));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(261), com.logitech.ue.centurion.feature.Features.FileSystem.Functions.FILE_OPEN, new com.logitech.ue.centurion.DataPacker().add((short) i).addUnsignedByte(i2).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.FileReadData> fileRead(int i, int i2) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Create request. Open file: %d, bytesToRead: %d", java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(i2)));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(261), com.logitech.ue.centurion.feature.Features.FileSystem.Functions.FILE_READ, new com.logitech.ue.centurion.DataPacker().add((short) i).add((short) i2).build()), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda7
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.FileReadData.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> fileWrite(int i, int i2, byte[] bArr) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Create request. Write file: %d, bytesToWrite: %d", java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(i2)));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(261), com.logitech.ue.centurion.feature.Features.FileSystem.Functions.FILE_WRITE, new com.logitech.ue.centurion.DataPacker().add((short) i).add(i2).add(bArr).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> fileClose(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Create request. Close file: %d", java.lang.Integer.valueOf(i)));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(261), com.logitech.ue.centurion.feature.Features.FileSystem.Functions.FILE_CLOSE, new com.logitech.ue.centurion.DataPacker().add((short) i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> fileErase(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Create request. Erase file: %d", java.lang.Integer.valueOf(i)));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(261), com.logitech.ue.centurion.feature.Features.FileSystem.Functions.FILE_ERASE, new com.logitech.ue.centurion.DataPacker().add((short) i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.FileSizeData> fileSize(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "Create request. Sile of file: %d", java.lang.Integer.valueOf(i)));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(261), com.logitech.ue.centurion.feature.Features.FileSystem.Functions.FILE_SIZE, new com.logitech.ue.centurion.DataPacker().add((short) i).build()), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda8
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.FileSizeData.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> powerOffSpeaker(byte[] bArr) {
        return this.mConnection.powerOffSpeaker(bArr);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> startBroadcast(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, " create Request startBroadcast, channelPos is " + i);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.ID), com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.Functions.START, new com.logitech.ue.centurion.DataPacker().add((byte) i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> stopBroadcast() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, " create Request stop Broadcast");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.ID), com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.Functions.STOP));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.BroadcasterInfo> getBroadcasterInfo() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get Broadcaster Info");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.ID), com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.Functions.GET_BROADCAST_INFO), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda2
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.BroadcasterInfo.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.BroadcasterStatus> getBroadcasterStatus() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get Broadcaster Status");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.ID), com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.Functions.GET_STATUS), com.logitech.ue.centurion.SimpleDeviceFactory$$ExternalSyntheticLambda13.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Integer> getReceiverCount() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, " create Request get receiver count");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.ID), com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.Functions.GET_RECEIVER_COUNT), com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda19.INSTANCE);
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setAudioMode(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Set Audio mode: " + i);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.ID), com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.Functions.SET_AUDIO_MODE, new com.logitech.ue.centurion.DataPacker().add((byte) i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setStereoBalance(byte b) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, " create Request setStereoBalance, weight is " + ((int) b));
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.ID), com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.Functions.SET_STEREO_BALANCE, new com.logitech.ue.centurion.DataPacker().add(b).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> resetVolume() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, " create Request reset volume ");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.ID), com.logitech.ue.centurion.feature.Features.iApolloBroadcaster.Functions.RESET_VOLUME));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> startReceiver(com.logitech.ue.centurion.devicedata.BroadcasterInfo broadcasterInfo, int i) {
        byte[] bytes = broadcasterInfo.getSSID().getBytes();
        byte[] bytes2 = broadcasterInfo.getSecurityKey().getBytes();
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, " create Request start Receiver, channelPos is " + i + " ssid length: " + bytes.length + " securitykey length: " + bytes2.length);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloReceiver.ID), com.logitech.ue.centurion.feature.Features.iApolloReceiver.Functions.START, new com.logitech.ue.centurion.DataPacker().add((byte) i).add((byte) broadcasterInfo.getSecurityType()).add((byte) bytes.length).add(bytes).add((byte) bytes2.length).add(bytes2).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> stopReceiver() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, " create Request stop receiver");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloReceiver.ID), com.logitech.ue.centurion.feature.Features.iApolloReceiver.Functions.STOP));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<com.logitech.ue.centurion.devicedata.ReceiverStatus> getReceiverStatus() {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Create request. Get Receiver Status");
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.iApolloReceiver.ID), com.logitech.ue.centurion.feature.Features.iApolloReceiver.Functions.GET_STATUS), new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda14
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return com.logitech.ue.centurion.devicedata.ReceiverStatus.buildFromPaydata((byte[]) obj);
            }
        });
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setUploadMode(int i) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "create request. Set LOG Upload Mode: " + i);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.EventLog.ID), com.logitech.ue.centurion.feature.Features.EventLog.Functions.SET_UPLOAD_MODE, new com.logitech.ue.centurion.DataPacker().add((byte) i).build()));
    }

    @Override // com.logitech.ue.centurion.device.Device
    public rx.Observable<java.lang.Void> setCrashLogHost(java.lang.String str) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "create request. Set Crash log host name: " + str);
        byte[] bytes = ((java.lang.String) com.google.common.base.Preconditions.checkNotNull(str)).getBytes(com.google.common.base.Charsets.UTF_8);
        return executeMessage(new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureMapper.getFeatureIndex(com.logitech.ue.centurion.feature.Features.EventLog.ID), com.logitech.ue.centurion.feature.Features.EventLog.Functions.SET_HOST_NAME, new com.logitech.ue.centurion.DataPacker().add(com.logitech.ue.centurion.utils.Utils.intToByte(bytes.length)).add(bytes).build()));
    }

    private <T> rx.Observable<T> executeMessage(com.logitech.ue.centurion.device.IMessage iMessage, rx.functions.Func0<T> func0, rx.functions.Func1<byte[], T> func1, final rx.functions.Func1<T, java.lang.Void> func2) {
        rx.Observable<T> map;
        T tCall;
        int featureID = this.mFeatureMapper.getFeatureID(iMessage.getFeature());
        if (!this.mFeatureMapper.isFeatureSupported(featureID)) {
            return rx.Observable.error(new com.logitech.ue.centurion.exceptions.UnsupportedCommandException(iMessage.getFeature()));
        }
        int version = this.mFeatureMapper.getFeature(featureID).getVersion();
        int minSupportedVersion = iMessage.getFunction().getMinSupportedVersion();
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "current feature version is: " + version + " minimum supported version is: " + minSupportedVersion);
        if (minSupportedVersion > version) {
            return rx.Observable.error(new com.logitech.ue.centurion.exceptions.UnsupportedCommandException(iMessage.getFunction()));
        }
        if (func0 != null && (tCall = func0.call()) != null) {
            return rx.Observable.just(tCall);
        }
        rx.Observable observableFlatMap = rx.Observable.just(iMessage).flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda10
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m645x91b50bdc((com.logitech.ue.centurion.device.IMessage) obj);
            }
        });
        if (func1 != null) {
            map = observableFlatMap.map(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda34
                @Override // rx.functions.Func1
                public final java.lang.Object call(java.lang.Object obj) {
                    return ((com.logitech.ue.centurion.device.IMessage) obj).getPaydata();
                }
            }).map(func1);
        } else {
            map = observableFlatMap.map(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda33
                @Override // rx.functions.Func1
                public final java.lang.Object call(java.lang.Object obj) {
                    return com.logitech.ue.centurion.device.GenericDevice.lambda$executeMessage$5((com.logitech.ue.centurion.device.IMessage) obj);
                }
            });
        }
        if (func2 != null) {
            java.util.Objects.requireNonNull(func2);
            map.doOnNext(new rx.functions.Action1() { // from class: com.logitech.ue.centurion.device.GenericDevice$$ExternalSyntheticLambda0
                @Override // rx.functions.Action1
                public final void call(java.lang.Object obj) {
                    func2.call(obj);
                }
            });
        }
        return map;
    }

    /* JADX INFO: renamed from: lambda$executeMessage$4$com-logitech-ue-centurion-device-GenericDevice, reason: not valid java name */
    /* synthetic */ rx.Observable m645x91b50bdc(com.logitech.ue.centurion.device.IMessage iMessage) {
        return getConductor().executeCommand(iMessage, com.logitech.ue.centurion.device.Priority.Normal);
    }

    private <T> rx.Observable<T> executeMessage(com.logitech.ue.centurion.device.IMessage iMessage) {
        return executeMessage(iMessage, (rx.functions.Func0) null, (rx.functions.Func1) null, (rx.functions.Func1) null);
    }

    private <T> rx.Observable<T> executeMessage(com.logitech.ue.centurion.device.IMessage iMessage, rx.functions.Func1<byte[], T> func1) {
        return executeMessage(iMessage, (rx.functions.Func0) null, func1, (rx.functions.Func1) null);
    }

    private <T> rx.Observable<T> executeMessage(com.logitech.ue.centurion.device.IMessage iMessage, rx.functions.Func1<byte[], T> func1, java.lang.String str, int i) {
        return executeMessage(iMessage, new com.logitech.ue.centurion.caching.SimpleCheckRule(str, this.mCacher), func1, new com.logitech.ue.centurion.caching.SimpleUpdateRule(str, i, this.mCacher));
    }
}
