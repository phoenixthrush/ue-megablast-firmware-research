package com.logitech.ue.centurion.devicedata.parcel;

/* JADX INFO: loaded from: classes2.dex */
public class VersionResponseParcel implements com.logitech.ue.centurion.devicedata.parcel.IResponseParcel {
    @Override // com.logitech.ue.centurion.devicedata.parcel.IResponseParcel
    public java.lang.String parce(byte[] bArr) {
        return parcePayload(bArr);
    }

    public static java.lang.String parcePayload(byte[] bArr) {
        com.logitech.ue.centurion.DataUnpacker dataUnpacker = new com.logitech.ue.centurion.DataUnpacker((byte[]) com.google.common.base.Preconditions.checkNotNull(bArr), 0);
        return java.lang.String.format(java.util.Locale.US, "%d.%d.%d", java.lang.Integer.valueOf(dataUnpacker.getUnsignedByte()), java.lang.Integer.valueOf(dataUnpacker.getUnsignedByte()), java.lang.Integer.valueOf(dataUnpacker.getShort()));
    }
}
