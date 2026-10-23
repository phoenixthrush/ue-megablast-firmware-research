package com.logitech.ue.centurion.spp;

/* JADX INFO: loaded from: classes2.dex */
public class CenturionSPPConnection extends com.logitech.ue.centurion.spp.SPPConnection {
    private static final int BUFFER_TIMEOUT = 500;
    private static final int SOF = 314;
    private static final int SPP_HEADER_SIZE = 3;
    private static final int SPP_TAIL_SIZE = 2;
    private static final java.lang.String TAG = com.logitech.ue.centurion.utils.LogUtils.makeLogTag(com.logitech.ue.centurion.spp.CenturionSPPConnection.class);
    private long mBufferRefreshTime;
    private java.io.ByteArrayOutputStream mBufferStream;

    private boolean validateSOF(byte b, byte b2) {
        return b == 19 && (b2 & 240) == 160;
    }

    public CenturionSPPConnection(com.logitech.ue.centurion.utils.MAC mac) {
        super(mac);
        this.mBufferRefreshTime = 0L;
        this.mBufferStream = new java.io.ByteArrayOutputStream(512);
    }

    @Override // com.logitech.ue.centurion.spp.SPPConnection
    protected void sendDataToSPP(byte[] bArr) throws java.io.IOException {
        super.sendDataToSPP(wrapPayload(bArr));
    }

    @Override // com.logitech.ue.centurion.spp.SPPConnection, com.logitech.ue.centurion.connection.CPLConnection, com.logitech.ue.centurion.connection.AbstractConnection
    protected void onDataReceived(byte[] bArr) {
        java.lang.String str = TAG;
        com.logitech.ue.centurion.utils.LogUtils.LOGD(str, "Received: " + com.logitech.ue.centurion.utils.Utils.byteArrayToFancyHexString(bArr));
        if (java.lang.System.currentTimeMillis() - this.mBufferRefreshTime > 500) {
            com.logitech.ue.centurion.utils.LogUtils.LOGD(str, java.lang.String.format(java.util.Locale.US, "Buffer timeout. Drop old buffer( %d bytes)", java.lang.Integer.valueOf(this.mBufferStream.size())));
            this.mBufferStream.reset();
        }
        try {
            this.mBufferStream.write(bArr);
            this.mBufferRefreshTime = java.lang.System.currentTimeMillis();
        } catch (java.io.IOException unused) {
        }
        processBuffer(this.mBufferStream);
    }

    @Override // com.logitech.ue.centurion.connection.AbstractConnection
    protected java.lang.String getTAG() {
        return TAG;
    }

    private void processBuffer(java.io.ByteArrayOutputStream byteArrayOutputStream) {
        if (byteArrayOutputStream.size() < 5) {
            com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Buffer is too small to be processed");
            return;
        }
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Process buffer");
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        int length = 0;
        while (byteArray.length > length + 5) {
            if (!validateSOF(byteArray[length], byteArray[length + 1])) {
                length = byteArray.length;
                break;
            }
            int iExtractPayloadSize = extractPayloadSize(byteArray, length);
            int i = (((length + 3) + 2) + iExtractPayloadSize) - 1;
            if (byteArray.length < i) {
                com.logitech.ue.centurion.utils.LogUtils.LOGW(TAG, java.lang.String.format(java.util.Locale.US, "invalid message. payload is corrupted. Start: %d end: %d payload size: %d", java.lang.Integer.valueOf(length), java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(iExtractPayloadSize)));
                break;
            }
            if (validateCRC(byteArray, length, iExtractPayloadSize)) {
                com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "Valid message detected. Size " + ((i - length) + 1));
                super.onDataReceived(extractPayload(byteArray, length, iExtractPayloadSize));
            } else {
                com.logitech.ue.centurion.utils.LogUtils.LOGW(TAG, "Invalid message detected. Size " + (i - length));
            }
            length = i + 1;
        }
        if (length != 0) {
            byteArrayOutputStream.reset();
            com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, java.lang.String.format(java.util.Locale.US, "End buffer process and drop %d bytes", java.lang.Integer.valueOf(length)));
            if (length < byteArray.length) {
                byteArrayOutputStream.write(byteArray, length, byteArray.length - length);
                return;
            }
            return;
        }
        com.logitech.ue.centurion.utils.LogUtils.LOGD(TAG, "End buffer process");
    }

    private byte[] wrapPayload(byte[] bArr) {
        int length = bArr.length;
        int i = length + 3;
        byte[] bArr2 = new byte[i + 2];
        bArr2[0] = 19;
        bArr2[1] = com.logitech.ue.centurion.utils.Utils.intToByte(((length & 3840) >> 8) + 160);
        bArr2[2] = com.logitech.ue.centurion.utils.Utils.intToByte(length & 255);
        java.lang.System.arraycopy(bArr, 0, bArr2, 3, length);
        int iCRC16MCRF4XX = com.logitech.ue.centurion.utils.Utils.CRC16MCRF4XX(bArr2, 0, i);
        bArr2[i] = com.logitech.ue.centurion.utils.Utils.getByteFromInt(iCRC16MCRF4XX, 0);
        bArr2[i + 1] = com.logitech.ue.centurion.utils.Utils.getByteFromInt(iCRC16MCRF4XX, 1);
        return bArr2;
    }

    private byte[] extractPayload(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        java.lang.System.arraycopy(bArr, i + 3, bArr2, 0, i2);
        return bArr2;
    }

    private int extractPayloadSize(byte[] bArr, int i) {
        return com.logitech.ue.centurion.utils.Utils.twoBytesToInt(com.logitech.ue.centurion.utils.Utils.intToByte(bArr[i + 1] & com.google.common.base.Ascii.SI), bArr[i + 2]);
    }

    private boolean validateCRC(byte[] bArr, int i, int i2) {
        int iCRC16MCRF4XX = com.logitech.ue.centurion.utils.Utils.CRC16MCRF4XX(bArr, i, i2 + 3);
        int i3 = i + 3 + i2;
        return com.logitech.ue.centurion.utils.Utils.twoBytesToInt(bArr[i3 + 1], bArr[i3]) == iCRC16MCRF4XX;
    }
}
