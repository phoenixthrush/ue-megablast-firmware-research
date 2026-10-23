package com.logitech.ue.centurion.device;

/* JADX INFO: loaded from: classes2.dex */
public class CenturionError {
    public static final int BUSY = 9;
    public static final int FAILED = 11;
    public static final int INVALID_FEATURE = 1;
    public static final int INVALID_FUNCTION = 2;
    public static final int INVALID_PARAM = 5;
    public static final int INVALID_SIZE = 3;
    public static final int INVALID_SWID = 4;
    public static final int LOW_RESOURCE = 7;
    public static final int MALFORMED = 12;
    public static final int NOT_AUTHORIZED = 14;
    public static final int NOT_AVAILABLE = 10;
    public static final int NOT_CONNECTED = 8;
    public static final int TIMEOUT = 6;
    public static final android.util.SparseArray<java.lang.String> sNameMap;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Code {
    }

    static {
        android.util.SparseArray<java.lang.String> sparseArray = new android.util.SparseArray<>();
        sNameMap = sparseArray;
        sparseArray.put(1, "INVALID_FEATURE");
        sparseArray.put(2, "INVALID_FUNCTION");
        sparseArray.put(3, "INVALID_SIZE");
        sparseArray.put(4, "INVALID_SWID");
        sparseArray.put(5, "INVALID_PARAM");
        sparseArray.put(6, com.google.android.gms.iid.InstanceID.ERROR_TIMEOUT);
        sparseArray.put(7, "LOW_RESOURCE");
        sparseArray.put(8, "NOT_CONNECTED");
        sparseArray.put(9, "BUSY");
        sparseArray.put(10, "NOT_AVAILABLE");
        sparseArray.put(11, "FAILED");
        sparseArray.put(12, "MALFORMED");
        sparseArray.put(14, "NOT_AUTHORIZED");
    }

    public static java.lang.String getName(int i) {
        return sNameMap.get(i, "UNKNOWN");
    }
}
