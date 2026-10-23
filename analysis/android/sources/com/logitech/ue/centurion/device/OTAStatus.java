package com.logitech.ue.centurion.device;

/* JADX INFO: loaded from: classes2.dex */
public class OTAStatus {
    public static final int CHECKING_FOR_UPDATE = 1;
    public static final int CHECK_FAIL = 5;
    public static final int DOWNLOADING = 2;
    public static final int DOWNLOAD_FAIL = 6;
    public static final int FIRMWARE_UP_TO_DATE = 0;
    public static final int INSTALLING = 4;
    public static final int LOW_BATTERY = 8;
    public static final int NEW_FIRMWARE_AVAILABLE = 3;
    public static final int NOT_CONNECTED = 9;
    public static final int VALIDATION_FAIL = 7;
    public static final android.util.SparseArray<java.lang.String> sNameMap;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Code {
    }

    static {
        android.util.SparseArray<java.lang.String> sparseArray = new android.util.SparseArray<>();
        sNameMap = sparseArray;
        sparseArray.put(0, "FIRMWARE_UP_TO_DATE");
        sparseArray.put(1, "CHECKING_FOR_UPDATE");
        sparseArray.put(2, "DOWNLOADING");
        sparseArray.put(3, "NEW_FIRMWARE_AVAILABLE");
        sparseArray.put(4, "INSTALLING");
        sparseArray.put(5, "CHECK_FAIL");
        sparseArray.put(6, "DOWNLOAD_FAIL");
        sparseArray.put(7, "VALIDATION_FAIL");
        sparseArray.put(8, "LOW_BATTERY");
        sparseArray.put(9, "NOT_CONNECTED");
    }

    public static java.lang.String getName(int i) {
        return sNameMap.get(i, "UNKNOWN");
    }
}
