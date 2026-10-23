package com.logitech.ue.centurion.ble;

/* JADX INFO: loaded from: classes2.dex */
public class BLEAdvertisementInfo {
    public static final int ADVERTISEMENT_PACKAGE_SIZE = 27;
    public static final int BROOKLYN_PID = 770;
    public static final int MANHATTAN_PID = 769;
    public static final int MAXIMUS_PID = 768;
    public static final android.util.SparseArray<java.lang.String> sNameMap;
    private com.logitech.ue.centurion.utils.MAC broadcasterMAC;
    private int mAdvertisementFlags;
    private int mBattery;
    private int mColor;
    private int mEvents;
    private int mExtraFlags;
    private boolean mIsBTClassicConnected;
    private boolean mIsBroadcasting;
    private boolean mIsButtonPressed;
    private boolean mIsInternetConnected;
    private boolean mIsPowered;
    private int mName;
    private int mPID;
    private int mStreamStatus;
    private int mUUID;
    private int mVendorData;
    private int mVersion;
    private int mVolume;
    public static final byte[] ADVERTISEMENT_FLAGS = {2, 1, com.google.common.base.Ascii.DC2};
    public static final byte[] NAME = {2, 8, 0};
    public static final byte[] UUID = {3, 3, 97, -2};
    public static final byte[] VENDOR_DATA = {com.google.common.base.Ascii.DLE, -1};

    static {
        android.util.SparseArray<java.lang.String> sparseArray = new android.util.SparseArray<>();
        sNameMap = sparseArray;
        sparseArray.put(769, "MEGABLAST");
        sparseArray.put(770, "BLAST");
    }

    public static java.lang.String getName(int i) {
        return sNameMap.get(i, "SPEAKER");
    }

    public BLEAdvertisementInfo(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z, boolean z2, boolean z3, int i9, boolean z4, boolean z5, int i10, byte[] bArr, int i11, int i12) {
        this.mAdvertisementFlags = i;
        this.mName = i2;
        this.mUUID = i3;
        this.mVendorData = i4;
        this.mPID = i5;
        this.mBattery = i7;
        this.mColor = i6;
        this.mVolume = i8;
        this.mEvents = i10;
        this.mIsPowered = z;
        this.mIsBTClassicConnected = z2;
        this.mIsInternetConnected = z3;
        this.mStreamStatus = i9;
        this.mIsBroadcasting = z4;
        this.mIsButtonPressed = z5;
        this.broadcasterMAC = new com.logitech.ue.centurion.utils.MAC(bArr);
        this.mVersion = i11;
        this.mExtraFlags = i12;
    }

    public static boolean validatePackage(byte[] bArr) {
        if (bArr.length < 27) {
            return false;
        }
        byte[] bArr2 = ADVERTISEMENT_FLAGS;
        if (!com.logitech.ue.centurion.utils.Utils.compareByteArraySubSet(bArr, 0, bArr2, 0, bArr2.length)) {
            return false;
        }
        byte[] bArr3 = NAME;
        if (!com.logitech.ue.centurion.utils.Utils.compareByteArraySubSet(bArr, 3, bArr3, 0, bArr3.length)) {
            return false;
        }
        byte[] bArr4 = UUID;
        if (!com.logitech.ue.centurion.utils.Utils.compareByteArraySubSet(bArr, 6, bArr4, 0, bArr4.length)) {
            return false;
        }
        byte[] bArr5 = VENDOR_DATA;
        return com.logitech.ue.centurion.utils.Utils.compareByteArraySubSet(bArr, 10, bArr5, 0, bArr5.length);
    }

    public static com.logitech.ue.centurion.ble.BLEAdvertisementInfo buildFromAdvertisementData(byte[] bArr) {
        return new com.logitech.ue.centurion.ble.BLEAdvertisementInfo(com.logitech.ue.centurion.utils.Utils.bytesToInt((byte) 0, bArr[0], bArr[1], bArr[2]), com.logitech.ue.centurion.utils.Utils.bytesToInt((byte) 0, bArr[3], bArr[4], bArr[5]), com.logitech.ue.centurion.utils.Utils.bytesToInt(bArr[6], bArr[7], bArr[8], bArr[9]), com.logitech.ue.centurion.utils.Utils.twoBytesToInt(bArr[10], bArr[11]), com.logitech.ue.centurion.utils.Utils.twoBytesToInt(bArr[12], bArr[13]), com.logitech.ue.centurion.utils.Utils.twoBytesToInt(bArr[14], bArr[15]), com.logitech.ue.centurion.utils.Utils.byteToUnsigned(bArr[16]), bArr[17], (bArr[18] & 128) != 0, (bArr[18] & com.google.common.primitives.SignedBytes.MAX_POWER_OF_TWO) != 0, (bArr[18] & 32) != 0, (bArr[18] & com.google.common.base.Ascii.CAN) >> 3, (bArr[18] & 4) != 0, (bArr[18] & 2) != 0, com.logitech.ue.centurion.utils.Utils.byteToUnsigned(bArr[19]), java.util.Arrays.copyOfRange(bArr, 20, 26), com.logitech.ue.centurion.utils.Utils.getLeastSignificantHalfByte(bArr[26]), com.logitech.ue.centurion.utils.Utils.getMostSignificantHalfByte(bArr[26]));
    }

    public int getAdvertisementFlags() {
        return this.mAdvertisementFlags;
    }

    public void setAdvertisementFlags(int i) {
        this.mAdvertisementFlags = i;
    }

    public int getModel() {
        return this.mName;
    }

    public void setName(int i) {
        this.mName = i;
    }

    public int getUUID() {
        return this.mUUID;
    }

    public void setUUID(int i) {
        this.mUUID = i;
    }

    public int getVendorData() {
        return this.mVendorData;
    }

    public void setVendorData(int i) {
        this.mVendorData = i;
    }

    public int getPID() {
        return this.mPID;
    }

    public void setPID(int i) {
        this.mPID = i;
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int i) {
        this.mColor = i;
    }

    public int getBattery() {
        return this.mBattery;
    }

    public void setBattery(int i) {
        this.mBattery = i;
    }

    public int getVolume() {
        return this.mVolume;
    }

    public void setVolume(int i) {
        this.mVolume = i;
    }

    public int getStreamingStatus() {
        return this.mStreamStatus;
    }

    public void setStreamingStatus(int i) {
        this.mStreamStatus = i;
    }

    public int getEvents() {
        return this.mEvents;
    }

    public void setEvents(int i) {
        this.mEvents = i;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public void setVersion(int i) {
        this.mVersion = i;
    }

    public int getExtraFlags() {
        return this.mExtraFlags;
    }

    public void setExtraFlags(int i) {
        this.mExtraFlags = i;
    }

    public boolean isPowered() {
        return this.mIsPowered;
    }

    public void setIsPowered(boolean z) {
        this.mIsPowered = z;
    }

    public boolean isIsBTClassicConnected() {
        return this.mIsBTClassicConnected;
    }

    public void setIsBTClassicConnected(boolean z) {
        this.mIsBTClassicConnected = z;
    }

    public boolean isInternetConnected() {
        return this.mIsInternetConnected;
    }

    public void setIsInternetConnected(boolean z) {
        this.mIsInternetConnected = z;
    }

    public boolean isBroadcasting() {
        return this.mIsBroadcasting;
    }

    public void setIsBroadcasting(boolean z) {
        this.mIsBroadcasting = z;
    }

    public boolean isButtonPressed() {
        return this.mIsButtonPressed;
    }

    public void setIsButtonPressed(boolean z) {
        this.mIsButtonPressed = z;
    }

    public com.logitech.ue.centurion.utils.MAC getBroadcasterMAC() {
        return this.broadcasterMAC;
    }
}
