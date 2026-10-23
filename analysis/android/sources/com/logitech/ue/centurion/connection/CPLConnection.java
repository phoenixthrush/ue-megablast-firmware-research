package com.logitech.ue.centurion.connection;

/* JADX INFO: loaded from: classes2.dex */
public abstract class CPLConnection extends com.logitech.ue.centurion.connection.AbstractConnection {
    private static final int BEGGING_MESSAGE_NUMBER = 0;
    private static final int BUFFER_SIZE = 512;
    private static final int HEADER_SIZE = 6;
    private static final int MAX_MESSAGE_NUMBER = 128;
    private int mSenderMessageNumber = 0;
    private int mExpectedMessageNumber = 0;
    private java.util.List<byte[]> mMessageList = new java.util.ArrayList();
    private final rx.subjects.PublishSubject<byte[]> mCPLMessageReceived = rx.subjects.PublishSubject.create();

    public int getCPLBufferSize() {
        return 512;
    }

    public byte[][] wrapCPLData(byte[] bArr) {
        com.google.common.base.Preconditions.checkNotNull(bArr);
        int cPLBufferSize = getCPLBufferSize() - 6;
        int length = bArr.length / cPLBufferSize;
        if (bArr.length % cPLBufferSize != 0) {
            length++;
        }
        byte[][] bArr2 = new byte[length][];
        if (length == 1) {
            byte[] bArr3 = new byte[bArr.length + 1];
            bArr3[0] = 0;
            java.lang.System.arraycopy(bArr, 0, bArr3, 1, bArr.length);
            bArr2[0] = bArr3;
            return bArr2;
        }
        int length2 = bArr.length;
        int i = 0;
        int i2 = 0;
        while (length > 0) {
            int i3 = length2 > cPLBufferSize ? cPLBufferSize : length2;
            length2 -= i3;
            byte[] bArr4 = new byte[i3 + 1];
            int i4 = i << 1;
            if (length2 > 0) {
                i4 |= 1;
            }
            bArr4[0] = (byte) i4;
            java.lang.System.arraycopy(bArr, i2, bArr4, 1, i3);
            bArr2[i] = bArr4;
            i2 += i3;
            i++;
            length--;
        }
        return bArr2;
    }

    protected synchronized void postCPLData(byte[] bArr) {
        com.google.common.base.Preconditions.checkNotNull(bArr);
        if (getMessageNumber(bArr) == this.mExpectedMessageNumber) {
            if (getMessageNumber(bArr) == 0) {
                dropDataAndWaitForNextMessage();
            }
            this.mExpectedMessageNumber = getMessageNumber(bArr) + 1;
            this.mMessageList.add(bArr);
            if (isMessageEnded(bArr)) {
                postMessageToSubscriber();
            }
        } else {
            dropDataAndWaitForNextMessage();
        }
    }

    private boolean isMessageEnded(byte[] bArr) {
        return (bArr[0] & 1) == 0;
    }

    private int getMessageNumber(byte[] bArr) {
        return bArr[0] >> 1;
    }

    private void dropDataAndWaitForNextMessage() {
        this.mMessageList.clear();
        this.mExpectedMessageNumber = 0;
    }

    private void postMessageToSubscriber() {
        java.util.Iterator<byte[]> it = this.mMessageList.iterator();
        int length = 0;
        int length2 = 0;
        while (it.hasNext()) {
            length2 += it.next().length - 1;
        }
        byte[] bArr = new byte[length2];
        for (byte[] bArr2 : this.mMessageList) {
            java.lang.System.arraycopy(bArr2, 1, bArr, length, bArr2.length - 1);
            length += bArr2.length - 1;
        }
        dropDataAndWaitForNextMessage();
        onCPLMessageReceived(bArr);
    }

    public rx.subjects.PublishSubject<byte[]> observeCPLMessageReceived() {
        return this.mCPLMessageReceived;
    }

    private void incrementExpectedNumber() {
        this.mExpectedMessageNumber = (this.mExpectedMessageNumber + 1) % 128;
    }

    private void incrementSenderNumber() {
        this.mSenderMessageNumber = (this.mSenderMessageNumber + 1) % 128;
    }

    @Override // com.logitech.ue.centurion.connection.AbstractConnection
    protected void onDataReceived(byte[] bArr) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(getTAG(), java.lang.String.format("Address: %s CPL Received: %s", getAddress(), com.logitech.ue.centurion.utils.Utils.byteArrayToFancyHexString(bArr)));
        this.mCPLMessageReceived.onNext(bArr);
        postCPLData(bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.logitech.ue.centurion.connection.AbstractConnection
    public void onDataSent(byte[] bArr) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(getTAG(), java.lang.String.format("Address: %s Sent: %s", getAddress(), com.logitech.ue.centurion.utils.Utils.byteArrayToFancyHexString(bArr)));
        this.mDataSentSubject.onNext(bArr);
    }

    protected void onCPLMessageReceived(byte[] bArr) {
        com.logitech.ue.centurion.utils.LogUtils.LOGD(getTAG(), java.lang.String.format("Address: %s Received: %s", getAddress(), com.logitech.ue.centurion.utils.Utils.byteArrayToFancyHexString(bArr)));
        this.mDataReceiveSubject.onNext(bArr);
    }
}
