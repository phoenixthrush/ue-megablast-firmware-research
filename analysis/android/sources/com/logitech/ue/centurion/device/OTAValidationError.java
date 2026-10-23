package com.logitech.ue.centurion.device;

/* JADX INFO: loaded from: classes2.dex */
public class OTAValidationError {
    public static final int AUTHENTICATION_FAILED = 9;
    public static final int COMPONENT_COUNT_EXCEEDS = 6;
    public static final int COMPONENT_CRC_CHECK_FAILED = 8;
    public static final int FILE_SIZE_MISMATCH = 2;
    public static final int INTERNAL_ERROR = 1;
    public static final int MAGIC_STRING_MISMATCH = 3;
    public static final int NONE = 0;
    public static final int OTA_HEADER_CRC_CHECK_FAILED = 7;
    public static final int OTA_VERSION_MISMATCH = 4;
    public static final int PLATFORM_MISMATCH = 5;
    public static final android.util.SparseArray<java.lang.String> sNameMap;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Code {
    }

    static {
        android.util.SparseArray<java.lang.String> sparseArray = new android.util.SparseArray<>();
        sNameMap = sparseArray;
        sparseArray.put(0, "NONE");
        sparseArray.put(1, "INTERNAL_ERROR");
        sparseArray.put(2, "FILE_SIZE_MISMATCH");
        sparseArray.put(3, "MAGIC_STRING_MISMATCH");
        sparseArray.put(4, "OTA_VERSION_MISMATCH");
        sparseArray.put(5, "PLATFORM_MISMATCH");
        sparseArray.put(6, "COMPONENT_COUNT_EXCEEDS");
        sparseArray.put(7, "OTA_HEADER_CRC_CHECK_FAILED");
        sparseArray.put(8, "COMPONENT_CRC_CHECK_FAILED");
        sparseArray.put(9, "AUTHENTICATION_FAILED");
    }

    public static java.lang.String getName(int i) {
        return sNameMap.get(i, "UNKNOWN");
    }
}
