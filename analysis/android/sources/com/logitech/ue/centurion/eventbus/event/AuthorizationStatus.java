package com.logitech.ue.centurion.eventbus.event;

/* JADX INFO: loaded from: classes2.dex */
public class AuthorizationStatus extends com.logitech.ue.centurion.notification.DeviceNotification {
    public static final int AUTHORIZED = 0;
    public static final int TIMED_OUT = 1;
    public int status;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AuthrizationType {
    }

    public AuthorizationStatus(com.logitech.ue.centurion.device.Device device, int i) {
        super(device);
        this.status = i;
    }

    public AuthorizationStatus(int i) {
        this(null, i);
    }

    public static com.logitech.ue.centurion.eventbus.event.AuthorizationStatus buildFromPaydata(byte[] bArr) {
        return new com.logitech.ue.centurion.eventbus.event.AuthorizationStatus(com.logitech.ue.centurion.devicedata.parcel.ByteResponseParcel.parcePayload(bArr).intValue());
    }

    public static boolean isAuthorizationResponse(byte[] bArr) {
        if (bArr.length < 2 || !com.logitech.ue.centurion.utils.CenturionUtils.checkExpectedMessage(bArr, 0, 1)) {
            return false;
        }
        com.logitech.ue.centurion.device.CenturionMessage centurionMessage = new com.logitech.ue.centurion.device.CenturionMessage(bArr);
        if (centurionMessage.getSWId() == 0) {
            com.logitech.ue.centurion.eventbus.CenturionEventBus.get().post(buildFromPaydata(centurionMessage.getPaydata()));
        }
        return true;
    }

    public boolean isAuthorized() {
        return this.status == 0;
    }
}
