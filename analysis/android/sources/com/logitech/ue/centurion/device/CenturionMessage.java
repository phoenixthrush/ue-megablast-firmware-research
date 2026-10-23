package com.logitech.ue.centurion.device;

/* JADX INFO: loaded from: classes2.dex */
public class CenturionMessage implements com.logitech.ue.centurion.device.IMessage {
    public static final int COMMAND_HEADER_LENGTH = 2;
    public static final int ERROR_CODE = -1;
    public static final byte[] NOP_MESSAGE = {0, 0, 0};
    private com.logitech.ue.centurion.device.CenturionFunction mCenturionFunction;
    private int mFeature;
    private int mFunctionId;
    private byte[] mPaydata;
    private int mSWId;

    public CenturionMessage(byte[] bArr) {
        this.mFeature = bArr[0];
        this.mFunctionId = com.logitech.ue.centurion.utils.Utils.getMostSignificantHalfByte(bArr[1]);
        this.mSWId = com.logitech.ue.centurion.utils.Utils.getLeastSignificantHalfByte(bArr[1]);
        byte[] bArr2 = new byte[bArr.length - 2];
        this.mPaydata = bArr2;
        java.lang.System.arraycopy(bArr, 2, bArr2, 0, bArr2.length);
    }

    public CenturionMessage(int i, com.logitech.ue.centurion.device.CenturionFunction centurionFunction) {
        this.mFeature = i;
        this.mCenturionFunction = centurionFunction;
        this.mFunctionId = centurionFunction.getId();
    }

    public CenturionMessage(int i, com.logitech.ue.centurion.device.CenturionFunction centurionFunction, byte[] bArr) {
        this.mFeature = i;
        this.mCenturionFunction = centurionFunction;
        this.mFunctionId = centurionFunction.getId();
        this.mPaydata = bArr;
    }

    @Override // com.logitech.ue.centurion.device.IMessage
    public byte[] buildData() {
        byte[] bArr;
        byte[] bArr2 = this.mPaydata;
        if (bArr2 == null) {
            bArr = new byte[2];
        } else {
            byte[] bArr3 = new byte[bArr2.length + 2];
            java.lang.System.arraycopy(bArr2, 0, bArr3, 2, bArr2.length);
            bArr = bArr3;
        }
        bArr[0] = (byte) this.mFeature;
        bArr[1] = (byte) (((this.mFunctionId << 4) & com.google.android.exoplayer2.extractor.ts.PsExtractor.VIDEO_STREAM_MASK) | (this.mSWId & 15));
        return bArr;
    }

    @Override // com.logitech.ue.centurion.device.IMessage
    public java.lang.String getName() {
        return java.lang.String.format(java.util.Locale.US, "Feature: 0x%02X Function: 0x%02X", java.lang.Integer.valueOf(this.mFeature), java.lang.Integer.valueOf(this.mFunctionId));
    }

    @Override // com.logitech.ue.centurion.device.IMessage
    public int getFeature() {
        return this.mFeature;
    }

    @Override // com.logitech.ue.centurion.device.IMessage
    public int getFunctionId() {
        return this.mFunctionId;
    }

    @Override // com.logitech.ue.centurion.device.IMessage
    public int getSWId() {
        return this.mSWId;
    }

    @Override // com.logitech.ue.centurion.device.IMessage
    public byte[] getPaydata() {
        return this.mPaydata;
    }

    @Override // com.logitech.ue.centurion.device.IMessage
    public void setSWId(int i) {
        this.mSWId = i;
    }

    @Override // com.logitech.ue.centurion.device.IMessage
    public com.logitech.ue.centurion.device.IMessage buildResponse(byte[] bArr) {
        return new com.logitech.ue.centurion.device.CenturionMessage(bArr);
    }

    @Override // com.logitech.ue.centurion.device.IMessage
    public com.logitech.ue.centurion.device.CenturionFunction getFunction() {
        return this.mCenturionFunction;
    }

    public static class Builder {
        int mFeatureIndex;
        com.logitech.ue.centurion.device.CenturionFunction mFunction;
        byte[] mPayload;

        public com.logitech.ue.centurion.device.CenturionMessage.Builder feature(int i) {
            this.mFeatureIndex = i;
            return this;
        }

        public com.logitech.ue.centurion.device.CenturionMessage.Builder function(com.logitech.ue.centurion.device.CenturionFunction centurionFunction) {
            this.mFunction = centurionFunction;
            return this;
        }

        public com.logitech.ue.centurion.device.CenturionMessage.Builder payload(byte[] bArr) {
            this.mPayload = bArr;
            return this;
        }

        public com.logitech.ue.centurion.device.IMessage build() {
            return new com.logitech.ue.centurion.device.CenturionMessage(this.mFeatureIndex, this.mFunction, this.mPayload);
        }
    }
}
