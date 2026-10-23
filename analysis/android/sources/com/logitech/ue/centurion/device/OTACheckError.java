package com.logitech.ue.centurion.device;

/* JADX INFO: loaded from: classes2.dex */
public class OTACheckError {
    public static final int NETWORK_NOT_CONNECTED = 1;
    public static final int OTA_SERVER_DOWN = 2;
    public static final int PENDING = 0;
    public static final android.util.SparseArray<java.lang.String> sNameMap;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Code {
    }

    static {
        android.util.SparseArray<java.lang.String> sparseArray = new android.util.SparseArray<>();
        sNameMap = sparseArray;
        sparseArray.put(0, "PENDING");
        sparseArray.put(1, "NETWORK_NOT_CONNECTED");
        sparseArray.put(2, "OTA_SERVER_DOWN");
    }

    public static java.lang.String getName(int i) {
        return sNameMap.get(i, "UNKNOWN");
    }
}
