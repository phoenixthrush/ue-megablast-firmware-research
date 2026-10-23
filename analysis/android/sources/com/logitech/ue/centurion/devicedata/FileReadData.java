package com.logitech.ue.centurion.devicedata;

/* JADX INFO: loaded from: classes2.dex */
public class FileReadData {
    public final int bytesRead;
    public final byte[] fileData;

    public FileReadData(int i, byte[] bArr) {
        this.bytesRead = i;
        this.fileData = bArr;
    }

    public static com.logitech.ue.centurion.devicedata.FileReadData buildFromPaydata(byte[] bArr) {
        com.logitech.ue.centurion.DataUnpacker dataUnpacker = new com.logitech.ue.centurion.DataUnpacker((byte[]) com.google.common.base.Preconditions.checkNotNull(bArr));
        int i = dataUnpacker.getShort();
        return new com.logitech.ue.centurion.devicedata.FileReadData(i, dataUnpacker.getByteArray(i));
    }
}
