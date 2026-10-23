package com.logitech.ue.centurion.devicedata;

/* JADX INFO: loaded from: classes2.dex */
public class FeatureInfo {
    private int mIndex;
    private int mType;
    private int mVersion;

    public FeatureInfo(int i, int i2, int i3) {
        this.mIndex = i;
        this.mType = i2;
        this.mVersion = i3;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public int getType() {
        return this.mType;
    }

    public java.lang.String getName() {
        return com.logitech.ue.centurion.feature.Features.getName(this.mIndex);
    }

    public static com.logitech.ue.centurion.devicedata.FeatureInfo buildFromPaydata(byte[] bArr) {
        com.logitech.ue.centurion.DataUnpacker dataUnpacker = new com.logitech.ue.centurion.DataUnpacker((byte[]) com.google.common.base.Preconditions.checkNotNull(bArr));
        return new com.logitech.ue.centurion.devicedata.FeatureInfo(dataUnpacker.getUnsignedByte(), dataUnpacker.getUnsignedByte(), dataUnpacker.getUnsignedByte());
    }
}
