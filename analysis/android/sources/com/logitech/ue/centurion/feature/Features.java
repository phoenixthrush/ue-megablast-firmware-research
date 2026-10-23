package com.logitech.ue.centurion.feature;

/* JADX INFO: loaded from: classes2.dex */
public class Features {
    private static final android.util.SparseArray<java.lang.String> sNameMap = new android.util.SparseArray<>();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Code {
    }

    static {
        for (java.lang.Class<?> cls : com.logitech.ue.centurion.feature.Features.class.getDeclaredClasses()) {
            try {
                if (!cls.equals(com.logitech.ue.centurion.feature.Features.Code.class)) {
                    sNameMap.put(cls.getDeclaredField("ID").getInt(null), (java.lang.String) cls.getDeclaredField(com.amazon.identity.auth.map.device.token.MAPCookie.KEY_NAME).get(null));
                }
            } catch (java.lang.Exception unused) {
                throw new java.lang.IllegalArgumentException();
            }
        }
    }

    public static java.lang.String getName(int i) {
        return sNameMap.get(i, "UNKNOWN");
    }

    public static class Root {
        public static final int ID = 0;
        public static final java.lang.String Name = "iRoot";

        public static class Events {
            public static final int AUTHORIZED = 1;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_FEATURE(0, 1),
            GET_PROTOCOL_VERSION(1, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class FeatureSet {
        public static final int ID = 1;
        public static final java.lang.String Name = "iFeatureSet";

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_FEATURE_COUNT(0, 1),
            GET_FEATURE_ID(1, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class DeviceInfo {
        public static final int ID = 256;
        public static final java.lang.String Name = "iDeviceInfo";

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_HARDWARE_INFO(0, 1),
            GET_FIRMWARE_VERSION(1, 1),
            GET_SERIAL_NUMBER(2, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class DeviceName {
        public static final int ID = 257;
        public static final java.lang.String Name = "iDeviceName";

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_NAME(0, 1),
            SET_NAME(1, 1),
            GET_DEFAULT_NAME(2, 1),
            GET_NAME_MAX_LENGTH(3, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class BatterySOC {
        public static final int ID = 260;
        public static final java.lang.String Name = "iBatterySOC";

        public static class Events {
            public static final int STATUS_CHANGE = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_BATTERY_STATUS(0, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class RemotePowerSet {
        public static final int ID = 262;
        public static final java.lang.String Name = "iRemotePowerSet";

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_BLE_REMOTE_ON(0, 1),
            SET_BLE_REMOTE_ON(1, 2),
            SET_VOICE_COMMAND(2, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class PowerState {
        public static final int ID = 263;
        public static final java.lang.String Name = "iPowerState";

        public static class Events {
            public static final int POWER_STATE_CHANGE = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_POWER_STATE(0, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class VolumeSet {
        public static final int ID = 512;
        public static final java.lang.String Name = "iVolumeSet";

        public static class Events {
            public static final int VOLUME_LEVEL_CHANGE = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_VOLUME_LEVEL(0, 1),
            SET_VOLUME_LEVEL(1, 2);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class EQSet {
        public static final int ID = 513;
        public static final java.lang.String Name = "iEQSet";

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_EQ(0, 2),
            SET_EQ(1, 2);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class VoiceAssistant {
        public static final int ID = 768;
        public static final java.lang.String Name = "iVoiceAssistant";

        public static class Events {
            public static final int VOICE_TRIGGER = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            SET_MODE(0, 1),
            GET_MODE(1, 1),
            START_SESSION(2, 1),
            STOP_SESSION(3, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class AVS {
        public static final int ID = 769;
        public static final java.lang.String Name = "iAVS";

        public static class Events {
            public static final int AVS_CLIENT_STATUS = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            SET_AVS_REFRESH_TOKEN(0, 1),
            GET_AVS_REFRESH_TOKEN(1, 1),
            SET_AVS_ID(2, 1),
            GET_AVS_ID(3, 1),
            SET_AVS_AUDIO_STREAM_DATA(4, 1),
            GET_AVS_AUDIO_STREAM_DATA(5, 1),
            SET_LOCALE(6, 2),
            GET_LOCALE(7, 2),
            TRIGGER_VOICE_COMMAND(8, 1),
            SET_VOICE_COMMAND_LISTEN(9, 1),
            GET_AVS_CLIENT_STATE(10, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class FarField {
        public static final int ID = 770;
        public static final java.lang.String Name = "iFarField";

        public static class Events {
            public static final int STATE_CHANGED = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            SET_STATE(0, 1),
            GET_STATE(1, 1),
            SET_TRIG_WORD_LANGUAGE(2, 1),
            GET_TRIG_WORD_LANGUAGE(3, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class BluetoothCtrl {
        public static final int ID = 771;
        public static final java.lang.String Name = "iBluetoothCtrl";

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            SET_DEVICE_DISCOVERABLE(0, 1),
            GET_BT_STATE(1, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class AutoSleep {
        public static final int ID = 264;
        public static final java.lang.String Name = "iAutoSleep";

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_SLEEP_TIMER(0, 1),
            SET_SLEEP_TIMER(1, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class Earcon {
        public static final int ID = 265;
        public static final java.lang.String Name = "iEarcons";

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            SET_LANGUAGE(0, 1),
            GET_LANGUAGE(1, 1),
            ENABLE_SOUND(2, 2),
            GET_EARCON_STATE(3, 1),
            GET_LANGUAGE_CAPABILITY(4, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class WiFiScan {
        public static final int ID = 1024;
        public static final java.lang.String Name = "iWiFiScan";

        public static class Events {
            public static final int NETWORK_INFO = 0;
            public static final int SCAN_COMPLETED = 1;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            START_SCANNING(0, 1),
            GET_REGION(1, 1),
            SET_REGION(2, 1),
            STOP_SCANNING(3, 2);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class WiFiAssociate {
        public static final int ID = 1025;
        public static final java.lang.String Name = "iWiFiAssociate";

        public static class Events {
            public static final int ASSOCIATE_STATUS = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_ASSOCIATED_COUNT(0, 1),
            GET_ASSOCIATED_INFO(1, 1),
            SET_AUTHENTICATION(2, 1),
            ASSOCIATE(3, 1),
            ASSOCIATE_BSSID(4, 1),
            FORGET(5, 1),
            CONNECT_BY_INDEX(6, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class WiFiConnectionStatus {
        public static final int ID = 1026;
        public static final java.lang.String Name = "iWiFiConnectionStatus";

        public static class Events {
            public static final int STATUS_CHANGE = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_STATUS(0, 1),
            GET_CONNECTED_SSID(1, 1),
            GET_CURRENT_IP(2, 1),
            GET_MAC(3, 1),
            GET_BSSID_CHANNEL(4, 1),
            GET_AP_MAC(5, 4);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class OTA {
        public static final int ID = 1027;
        public static final java.lang.String Name = "iOTA";

        public static class Events {
            public static final int OTA_STATUS = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            GET_OTA_STATUS(0, 1),
            SET_OTA_INSTALL_MODE(1, 1),
            GET_OTA_INSTALL_MODE(2, 1),
            INSTALL_OTA(3, 1),
            INITIATE_OTA_CHECK(4, 1),
            SET_UPDATE_SERVER_HOSTNAME(5, 1),
            DISABLE_OTA(6, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class AVSAlert {
        public static final int ID = 1028;
        public static final java.lang.String Name = "AVSAlert";

        public static class Events {
            public static final int ALERT_BACKGROUND = 3;
            public static final int ALERT_FOREGROUND = 2;
            public static final int ALERT_STARTED = 0;
            public static final int ALERT_STOPPED = 1;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            SET_ALERT(0, 1),
            DELETE_ALERT(1, 1),
            GET_ALERT_COUNT(2, 1),
            GET_ALERT_INFO(3, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class FileSystem {
        public static final int ID = 261;
        public static final java.lang.String Name = "iFileSystem";

        public static class Events {
            public static final int FILE_READ_PROGRESS = 0;
            public static final int FILE_WRITE_PROGRESS = 1;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            FILE_OPEN(0, 1),
            FILE_READ(1, 1),
            FILE_WRITE(2, 1),
            FILE_CLOSE(3, 1),
            FILE_ERASE(4, 1),
            FILE_SIZE(5, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class iApolloBroadcaster {
        public static final int ID = 514;
        public static final java.lang.String Name = "iApolloBroadcaster";

        public static class Events {
            public static final int RECEIVER_COUNT = 1;
            public static final int STATUS_CHANGE = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            START(0, 1),
            STOP(1, 1),
            GET_BROADCAST_INFO(2, 1),
            SET_AUDIO_MODE(3, 1),
            SET_STEREO_BALANCE(4, 1),
            RESET_VOLUME(5, 1),
            GET_STATUS(6, 1),
            GET_RECEIVER_COUNT(7, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class iApolloReceiver {
        public static final int ID = 515;
        public static final java.lang.String Name = "iApolloReceiver";

        public static class Events {
            public static final int STATUS_CHANGE = 0;
        }

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            START(0, 1),
            STOP(1, 1),
            GET_STATUS(2, 1);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }

    public static class EventLog {
        public static final int ID = 1029;
        public static final java.lang.String Name = "EventLog";

        public enum Functions implements com.logitech.ue.centurion.device.CenturionFunction {
            SET_UPLOAD_MODE(0, 1),
            GET_UPLOAD_MODE(1, 1),
            SET_HOST_NAME(2, 2),
            GET_HOST_NAME(3, 2);

            private final int functionId;
            private final int minSupportedVersion;

            Functions(int i, int i2) {
                this.functionId = i;
                this.minSupportedVersion = i2;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getId() {
                return this.functionId;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public int getMinSupportedVersion() {
                return this.minSupportedVersion;
            }

            @Override // com.logitech.ue.centurion.device.CenturionFunction
            public java.lang.String getName() {
                return name();
            }
        }
    }
}
