package com.logitech.ue.centurion.device;

/* JADX INFO: loaded from: classes2.dex */
public class FileAccess {
    public static final int APPEND = 3;
    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final android.util.SparseArray<java.lang.String> sNameMap;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Code {
    }

    static {
        android.util.SparseArray<java.lang.String> sparseArray = new android.util.SparseArray<>();
        sNameMap = sparseArray;
        sparseArray.put(1, "READ");
        sparseArray.put(2, "WRITE");
        sparseArray.put(3, "APPEND");
    }

    public static java.lang.String getName(int i) {
        return sNameMap.get(i, "UNKNOWN");
    }
}
