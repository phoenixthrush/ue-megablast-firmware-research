package com.logitech.ue.centurion.devicedata.parcel;

/* JADX INFO: loaded from: classes2.dex */
public class FeatureListResponseParcel implements com.logitech.ue.centurion.devicedata.parcel.IResponseParcel<java.util.List<com.logitech.ue.centurion.feature.Feature>> {
    @Override // com.logitech.ue.centurion.devicedata.parcel.IResponseParcel
    public java.util.List<com.logitech.ue.centurion.feature.Feature> parce(byte[] bArr) {
        return parcePayload(bArr);
    }

    public static java.util.List<com.logitech.ue.centurion.feature.Feature> parcePayload(byte[] bArr) {
        com.logitech.ue.centurion.DataUnpacker dataUnpacker = new com.logitech.ue.centurion.DataUnpacker((byte[]) com.google.common.base.Preconditions.checkNotNull(bArr));
        int unsignedByte = dataUnpacker.getUnsignedByte();
        java.util.ArrayList arrayList = new java.util.ArrayList(unsignedByte);
        for (int i = 0; i < unsignedByte; i++) {
            arrayList.add(new com.logitech.ue.centurion.feature.Feature(dataUnpacker.getShort(), dataUnpacker.getUnsignedByte(), dataUnpacker.getUnsignedByte()));
        }
        return arrayList;
    }
}
