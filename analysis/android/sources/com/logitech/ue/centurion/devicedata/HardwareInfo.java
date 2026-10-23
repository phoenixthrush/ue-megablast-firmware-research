package com.logitech.ue.centurion.devicedata;

/* JADX INFO: loaded from: classes2.dex */
public class HardwareInfo {
    private int mColor;
    private int mHardwareRevision;
    private int mModelId;
    private int mSKUAttributes;

    public HardwareInfo(int i, int i2, int i3, int i4) {
        this.mHardwareRevision = i2;
        this.mModelId = i;
        this.mColor = i3;
        this.mSKUAttributes = i4;
    }

    public int getHardwareRevision() {
        return this.mHardwareRevision;
    }

    public int getModel() {
        return this.mModelId;
    }

    public int getColor() {
        return this.mColor;
    }

    public int getSKUAttributes() {
        return this.mSKUAttributes;
    }

    public static com.logitech.ue.centurion.devicedata.HardwareInfo buildFromPaydata(byte[] bArr) {
        com.logitech.ue.centurion.DataUnpacker dataUnpacker = new com.logitech.ue.centurion.DataUnpacker((byte[]) com.google.common.base.Preconditions.checkNotNull(bArr));
        return new com.logitech.ue.centurion.devicedata.HardwareInfo(dataUnpacker.getUnsignedByte(), dataUnpacker.getUnsignedByte(), dataUnpacker.getShort(), dataUnpacker.getInt());
    }
}
