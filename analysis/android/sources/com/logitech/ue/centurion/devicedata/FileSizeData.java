package com.logitech.ue.centurion.devicedata;

/* JADX INFO: loaded from: classes2.dex */
public class FileSizeData {
    public static final int FILE_CHUNK_IN_BYTES = 4000;
    public final java.util.zip.CRC32 crc32;
    public final int fileSize;

    public FileSizeData(int i, java.util.zip.CRC32 crc32) {
        this.fileSize = i;
        this.crc32 = crc32;
    }

    public static com.logitech.ue.centurion.devicedata.FileSizeData buildFromPaydata(byte[] bArr) {
        com.logitech.ue.centurion.DataUnpacker dataUnpacker = new com.logitech.ue.centurion.DataUnpacker((byte[]) com.google.common.base.Preconditions.checkNotNull(bArr));
        int iIntValue = com.logitech.ue.centurion.devicedata.parcel.IntegerResponseParcel.parcePayload(dataUnpacker.getByteArray(4)).intValue();
        java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
        crc32.update(com.logitech.ue.centurion.devicedata.parcel.IntegerResponseParcel.parcePayload(dataUnpacker.getByteArray(4)).intValue());
        return new com.logitech.ue.centurion.devicedata.FileSizeData(iIntValue, crc32);
    }
}
