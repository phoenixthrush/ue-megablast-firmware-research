package com.logitech.ue.centurion.utils;

/* JADX INFO: loaded from: classes2.dex */
public class CenturionUtils {
    private static final int REMOTE_POWER_SUPPORT_VERSIONl = 2;

    private static int hexToBin(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        }
        char c2 = 'A';
        if ('A' > c || c > 'F') {
            c2 = 'a';
            if ('a' > c || c > 'f') {
                return -1;
            }
        }
        return (c - c2) + 10;
    }

    public static boolean isRemotePowerOnSupported(int i) {
        return false;
    }

    public static boolean checkExpectedMessage(byte[] bArr, int i, int i2, int i3) {
        com.google.common.base.Preconditions.checkNotNull(bArr);
        return bArr[0] == i && checkFunctionAndSWIF(bArr[1], i2, i3);
    }

    public static boolean checkExpectedMessage(byte[] bArr, int i, int i2) {
        com.google.common.base.Preconditions.checkNotNull(bArr);
        return bArr[0] == i && com.logitech.ue.centurion.utils.Utils.getMostSignificantHalfByte(bArr[1]) == i2;
    }

    public static boolean checkErrorMessage(byte[] bArr, int i, int i2, int i3) {
        com.google.common.base.Preconditions.checkNotNull(bArr);
        return bArr[0] == -1 && bArr[1] == i && checkFunctionAndSWIF(bArr[2], i2, i3);
    }

    public static boolean checkFunctionAndSWIF(byte b, int i, int i2) {
        return com.logitech.ue.centurion.utils.Utils.getMostSignificantHalfByte(b) == i && com.logitech.ue.centurion.utils.Utils.getLeastSignificantHalfByte(b) == i2;
    }

    public static byte[] exctractPaydataFromMessage(byte[] bArr) {
        int length = bArr.length - 2;
        byte[] bArr2 = new byte[length];
        java.lang.System.arraycopy(bArr, 2, bArr2, 0, length);
        return bArr2;
    }

    public static boolean isNopBroadcastMessage(byte[] bArr, int i) {
        return i == com.logitech.ue.centurion.device.CenturionMessage.NOP_MESSAGE.length && bArr[0] == com.logitech.ue.centurion.device.CenturionMessage.NOP_MESSAGE[0] && bArr[1] == com.logitech.ue.centurion.device.CenturionMessage.NOP_MESSAGE[1] && bArr[2] == com.logitech.ue.centurion.device.CenturionMessage.NOP_MESSAGE[2];
    }

    public static boolean isNopBroadcastMessage(byte[] bArr) {
        return isNopBroadcastMessage(bArr, bArr.length);
    }

    public static boolean isFunctionSupported(com.logitech.ue.centurion.device.Device device, int i, com.logitech.ue.centurion.device.CenturionFunction centurionFunction) {
        com.logitech.ue.centurion.device.IFeatureMapper featureMapper = device.getFeatureMapper();
        if (featureMapper.isFeatureSupported(i)) {
            return centurionFunction.getMinSupportedVersion() <= featureMapper.getFeature(i).getVersion();
        }
        return false;
    }

    public static int getFeatureVersion(com.logitech.ue.centurion.device.Device device, int i) {
        if (device.isFeatureSupported(i)) {
            return device.getFeatureMapper().getFeature(i).getVersion();
        }
        return -1;
    }

    public static byte[] getPhoneSecureId(android.content.Context context) {
        return parseHexBinary(android.provider.Settings.Secure.getString(context.getApplicationContext().getContentResolver(), "android_id").substring(0, 12));
    }

    private static byte[] parseHexBinary(java.lang.String str) {
        int length = str.length();
        if (length % 2 != 0) {
            throw new java.lang.IllegalArgumentException("hexBinary needs to be even-length: " + str);
        }
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            int iHexToBin = hexToBin(str.charAt(i));
            int iHexToBin2 = hexToBin(str.charAt(i + 1));
            if (iHexToBin == -1 || iHexToBin2 == -1) {
                throw new java.lang.IllegalArgumentException("contains illegal character for hexBinary: " + str);
            }
            bArr[i / 2] = (byte) ((iHexToBin * 16) + iHexToBin2);
        }
        return bArr;
    }

    public static boolean isRemotePowerOnSupported(com.logitech.ue.centurion.discovery.IDiscoveryInfo iDiscoveryInfo) {
        return isRemotePowerOnSupported(iDiscoveryInfo.getVersion());
    }
}
