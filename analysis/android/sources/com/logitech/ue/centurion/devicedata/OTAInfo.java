package com.logitech.ue.centurion.devicedata;

/* JADX INFO: loaded from: classes2.dex */
public class OTAInfo {
    private int mParam;
    private int mStatus;

    public OTAInfo(int i, int i2) {
        this.mStatus = i;
        this.mParam = i2;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public int getParam() {
        return this.mParam;
    }

    public static com.logitech.ue.centurion.devicedata.OTAInfo buildFromPaydata(byte[] bArr) {
        com.logitech.ue.centurion.DataUnpacker dataUnpacker = new com.logitech.ue.centurion.DataUnpacker((byte[]) com.google.common.base.Preconditions.checkNotNull(bArr));
        return new com.logitech.ue.centurion.devicedata.OTAInfo(dataUnpacker.getUnsignedByte(), dataUnpacker.getInt());
    }
}
