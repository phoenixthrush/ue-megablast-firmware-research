package com.logitech.ue.centurion.device;

/* JADX INFO: loaded from: classes2.dex */
public class OTAInstallMode {
    public static final int AUTOMATIC = 0;
    public static final int MANUAL = 1;
    public static final android.util.SparseArray<java.lang.String> sNameMap;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Code {
    }

    static {
        android.util.SparseArray<java.lang.String> sparseArray = new android.util.SparseArray<>();
        sNameMap = sparseArray;
        sparseArray.put(0, "AUTOMATIC");
        sparseArray.put(1, "MANUAL");
    }

    public static java.lang.String getName(int i) {
        return sNameMap.get(i, "UNKNOWN");
    }
}
