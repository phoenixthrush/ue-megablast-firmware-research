package com.logitech.logiux.newjackcity.helper;

/* JADX INFO: loaded from: classes2.dex */
public enum OTAServer {
    PRODUCTION(com.logitech.logiux.newjackcity.BuildConfig.OTA_DEFAULT_ADDRESS, 2131755181, true),
    STAGING(com.logitech.logiux.newjackcity.BuildConfig.OTA_STAGING_ADDRESS, 2131755183, true),
    TELCO(com.logitech.logiux.newjackcity.BuildConfig.OTA_DEMO_ADDRESS, 2131755184, false),
    AMAZON(com.logitech.logiux.newjackcity.BuildConfig.OTA_AMAZON_ADDRESS, 2131755179, true),
    SPOTIFY_CERTIFICATION(com.logitech.logiux.newjackcity.BuildConfig.OTA_SPOTIFY_CERTIFICATION_ADDRESS, 2131755182, true),
    BETA(com.logitech.logiux.newjackcity.BuildConfig.OTA_BETA_ADDRESS, 2131755180, true);

    final boolean show;
    public final int titleId;
    public final java.lang.String url;

    OTAServer(java.lang.String str, int i, boolean z) {
        this.url = str;
        this.titleId = i;
        this.show = z;
    }

    public static com.logitech.logiux.newjackcity.helper.OTAServer[] shownOtaServers() {
        java.util.ArrayList arrayList = new java.util.ArrayList();
        for (com.logitech.logiux.newjackcity.helper.OTAServer oTAServer : values()) {
            if (oTAServer.show) {
                arrayList.add(oTAServer);
            }
        }
        return (com.logitech.logiux.newjackcity.helper.OTAServer[]) arrayList.toArray(new com.logitech.logiux.newjackcity.helper.OTAServer[arrayList.size()]);
    }
}
