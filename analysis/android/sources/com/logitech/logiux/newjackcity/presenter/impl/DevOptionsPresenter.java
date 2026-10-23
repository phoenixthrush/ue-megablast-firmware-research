package com.logitech.logiux.newjackcity.presenter.impl;

/* JADX INFO: loaded from: classes2.dex */
public class DevOptionsPresenter extends com.logitech.logiux.newjackcity.presenter.base.Presenter<com.logitech.logiux.newjackcity.view.IDevOptionsView> implements com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter {
    private static final java.lang.String LANGUAGE_SYSTEM = "System";
    private static final java.lang.String SPEAKER_LOG_FILE_NAME = "speaker_logs.txt";
    private static final int SPEAKER_LOG_FILE_NUMBER = 66;
    public static final int UNLOCK_DEV_OPTIONS_COUNT = 10;
    private java.util.List<java.lang.String> mAlexaLanguageTags = new java.util.ArrayList();

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onBtAddressChanged(java.lang.String str) {
    }

    @Override // com.logitech.logiux.newjackcity.presenter.base.Presenter, com.logitech.logiux.newjackcity.presenter.base.IPresenter
    public void onStart() {
        super.onStart();
        this.mAlexaLanguageTags = new java.util.ArrayList<java.lang.String>() { // from class: com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.1
            {
                add(java.util.Locale.US.toLanguageTag());
                add(java.util.Locale.UK.toLanguageTag());
                add(java.util.Locale.GERMANY.toLanguageTag());
                add(com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.LANGUAGE_SYSTEM);
            }
        };
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setAlexaLanguageItems(this.mAlexaLanguageTags);
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setSelectedAlexaLanguageIndex(getSelectedIndexByTag(com.logitech.logiux.newjackcity.manager.Prefs.get().isUsePhoneLocale() ? LANGUAGE_SYSTEM : com.logitech.logiux.newjackcity.manager.Prefs.get().getAlexaLocale()));
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setForceOnboardingOn(com.logitech.logiux.newjackcity.manager.Prefs.get().isForceOnboarding());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setBleOnboarding(com.logitech.logiux.newjackcity.manager.Prefs.get().isUseBLEOnboarding());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setDiscoveryFilteringCheck(com.logitech.logiux.newjackcity.manager.Prefs.get().isUseDiscoveryFiltering());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setBtAddress(com.logitech.logiux.newjackcity.manager.Prefs.get().getBtAddressFilter());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setBtAddressViewEnabled(com.logitech.logiux.newjackcity.manager.Prefs.get().isUseDiscoveryFiltering());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setAlexaLogsOn(com.logitech.logiux.newjackcity.manager.Prefs.get().isAlexaLogsEnabled());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).showAlexaLogFileSize(getLogsSizeString());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setHfpSupport(com.logitech.logiux.newjackcity.manager.Prefs.get().isUseHfp());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setAlexaWithoutSpeaker(com.logitech.logiux.newjackcity.manager.Prefs.get().isAlexaWithoutSpeaker());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setSkipOTA(com.logitech.logiux.newjackcity.manager.Prefs.get().isSkipOTAEnabled());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setEmulator(com.logitech.logiux.newjackcity.manager.Prefs.get().isEmulatorEnabled());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setAVSWild(com.logitech.logiux.newjackcity.manager.Prefs.get().isAVSInWildEnabled());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setAlexaBeta(com.logitech.logiux.newjackcity.manager.Prefs.get().isAlexaBeta());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setOtaServers(com.logitech.logiux.newjackcity.helper.OTAServer.shownOtaServers());
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setCurrentOtaServer(com.logitech.logiux.newjackcity.manager.Prefs.get().getOTAServer());
        updateSpeakerLogsSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpeakerLogsSection() {
        if (this.mView != 0) {
            java.io.File speakerLogFile = getSpeakerLogFile();
            boolean z = speakerLogFile != null && speakerLogFile.exists();
            ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setSpeakerLogFile(speakerLogFile, humanReadableByteCount(getLogSizeInB(speakerLogFile)));
            ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setSendSpeakerLogFileEnabled(z);
        }
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onAlexaLanguageChanged(java.lang.String str) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Changed Alexa language to: %s.", str);
        if (LANGUAGE_SYSTEM.equals(str) && !com.logitech.logiux.newjackcity.manager.Prefs.get().isUsePhoneLocale()) {
            com.logitech.logiux.newjackcity.manager.Prefs.get().setUsePhoneLocale(true);
            com.logitech.logiux.newjackcity.eventbus.NJCEventBus.get().post(new com.logitech.logiux.newjackcity.eventbus.event.AlexaLocaleChangedEvent());
        } else {
            if (com.logitech.logiux.newjackcity.manager.Prefs.get().getAlexaLocale().equals(str)) {
                return;
            }
            com.logitech.logiux.newjackcity.manager.Prefs.get().setAlexaLocale(str);
            com.logitech.logiux.newjackcity.manager.Prefs.get().setUsePhoneLocale(false);
            com.logitech.logiux.newjackcity.eventbus.NJCEventBus.get().post(new com.logitech.logiux.newjackcity.eventbus.event.AlexaLocaleChangedEvent());
        }
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onForceOnboardingToggled(boolean z) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Changed Force Onboarding on Start to " + z);
        com.logitech.logiux.newjackcity.manager.Prefs.get().setForceOnboarding(z);
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onBLEOnboardingToggled(boolean z) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Changed BLE Onboarding to " + z);
        com.logitech.logiux.newjackcity.manager.Prefs.get().setUseBLEOnboarding(z);
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onDiscoveryFilteringToggled(boolean z) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Changed Discovery Filtering to " + z);
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onAlexaLogsToggled(boolean z) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Changed Alexa logs enabled to " + z);
        com.logitech.ue.avs.tools.CSVLogger.setEnabled(z);
        com.logitech.logiux.newjackcity.manager.Prefs.get().setAlexaLogsEnabled(z);
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onHfpSupportToggled(boolean z) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Changed HFP supported to " + z);
        com.logitech.logiux.newjackcity.manager.Prefs.get().setUseHfp(z);
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onAlexaWithoutSpeakerToggled(boolean z) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Changed Alexa Without Speaker to " + z);
        com.logitech.logiux.newjackcity.manager.Prefs.get().setAlexaWithoutSpeaker(z);
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onAlexaLogsClearPressed() {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Clear Alexa logs pressed");
        com.logitech.ue.avs.tools.CSVLogger.clear();
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).showAlexaLogFileSize(getLogsSizeString());
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onAlexaSendLogsPressed() {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Send Alexa logs pressed");
        java.io.File file = new java.io.File(com.logitech.ue.avs.tools.CSVLogger.getLogFilePath());
        if (!file.exists()) {
            ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).showErrorToast("No data to send");
        } else {
            sendFileViaEmail(android.net.Uri.fromFile(file));
        }
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onSkipOTAToggled(boolean z) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - skip Onboarding OTA: " + z);
        com.logitech.logiux.newjackcity.manager.Prefs.get().setSkipOta(z);
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onEmulatorToggled(boolean z) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Emulator Enabled: " + z);
        com.logitech.logiux.newjackcity.manager.Prefs.get().setEmulator(z);
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onAVSInWildToggeled(boolean z) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Enable AVS In Wild: " + z);
        com.logitech.logiux.newjackcity.manager.Prefs.get().setAvsInWild(z);
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onDemoModePressed() {
        showTelcoConfirmation();
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onPullSpeakerLogFilePressed() {
        com.logitech.ue.centurion.device.Device connectedDevice = com.logitech.ue.centurion.DeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null) {
            if (connectedDevice.isFeatureSupported(261)) {
                pullSpeakerLogFile(connectedDevice, 66);
                return;
            } else {
                if (this.mView != 0) {
                    ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).showErrorToast("Feature not supported.");
                    return;
                }
                return;
            }
        }
        if (this.mView != 0) {
            ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).showErrorToast("Device not connected.");
        }
    }

    private void pullSpeakerLogFile(com.logitech.ue.centurion.device.Device device, int i) {
        com.logitech.logiux.newjackcity.utils.SpeakerFileReader speakerFileReader = new com.logitech.logiux.newjackcity.utils.SpeakerFileReader(i);
        speakerFileReader.setOnEventListener(new com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener() { // from class: com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.2
            @Override // com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener
            public void onStart() {
                if (com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this.mView != null) {
                    com.logitech.logiux.newjackcity.logging.FireLog.d(com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this, "Start log file downloading");
                    ((com.logitech.logiux.newjackcity.view.IDevOptionsView) com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this.mView).showProgressDialog("Downloading");
                }
                com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter devOptionsPresenter = com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this;
                devOptionsPresenter.removeFile(devOptionsPresenter.getSpeakerLogFile());
            }

            @Override // com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener
            public void onChunk(byte[] bArr) throws java.lang.Throwable {
                com.logitech.logiux.newjackcity.logging.FireLog.d(com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this, "Downloaded chunk size: " + bArr.length);
                try {
                    org.apache.commons.io.FileUtils.writeByteArrayToFile(com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this.getSpeakerLogFile(), bArr, true);
                    com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter devOptionsPresenter = com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this;
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    sb.append("Log file size on phone: ");
                    com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter devOptionsPresenter2 = com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this;
                    sb.append(com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.humanReadableByteCount(devOptionsPresenter2.getLogSizeInB(devOptionsPresenter2.getSpeakerLogFile())));
                    com.logitech.logiux.newjackcity.logging.FireLog.d(devOptionsPresenter, sb.toString());
                } catch (java.io.IOException e) {
                    com.logitech.logiux.newjackcity.logging.FireLog.e(com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this, e, "Can't save speaker log file.");
                }
            }

            @Override // com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener
            public void onComplete() {
                com.logitech.logiux.newjackcity.logging.FireLog.d(com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this, "Complete log file downloading");
                com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this.updateSpeakerLogsSection();
                if (com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this.mView != null) {
                    ((com.logitech.logiux.newjackcity.view.IDevOptionsView) com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this.mView).dismissProgressDialog();
                }
            }

            @Override // com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener
            public void onError(java.lang.Throwable th) {
                com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this.updateSpeakerLogsSection();
                com.logitech.logiux.newjackcity.presenter.impl.DevOptionsPresenter.this.notifyOnError(th, "Can't pull log file.");
            }
        });
        speakerFileReader.readFile(device);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnError(java.lang.Throwable th, java.lang.String str) {
        com.logitech.logiux.newjackcity.logging.FireLog.e(this, th, str);
        if (this.mView != 0) {
            ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).showErrorToast(str);
            ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).dismissProgressDialog();
        }
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onSendSpeakerLogFilePressed() {
        java.io.File speakerLogFile = getSpeakerLogFile();
        if (speakerLogFile == null || !speakerLogFile.exists()) {
            ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).showErrorToast("No data to send");
        } else {
            sendFileViaEmail(androidx.core.content.FileProvider.getUriForFile(com.logitech.logiux.newjackcity.NJCApplication.getInstance(), "com.logitech.newjackcity.provider", speakerLogFile));
        }
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onSkipOnboardingPressed() {
        com.logitech.logiux.newjackcity.eventbus.NJCEventBus.get().post(new com.logitech.logiux.newjackcity.eventbus.event.SkipOnboardingEvent());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.io.File getSpeakerLogFile() {
        return new java.io.File(com.logitech.logiux.newjackcity.NJCApplication.getInstance().getFilesDir().getAbsolutePath() + java.io.File.separator + SPEAKER_LOG_FILE_NAME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFile(java.io.File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onAlexaBetaToggled(boolean z) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Enable Alexa Beta Product IDs: " + z);
        com.logitech.logiux.newjackcity.manager.Prefs.get().setAlexaBeta(z);
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onOtaServerSelected(com.logitech.logiux.newjackcity.helper.OTAServer oTAServer) {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Selected OTA Server: " + oTAServer.name());
        com.logitech.logiux.newjackcity.manager.Prefs.get().setOtaServer(oTAServer);
    }

    private void showTelcoConfirmation() {
        if (this.mView != 0) {
            ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).showDemoDisclaimerDialog();
        }
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onTelcoConfirmed() {
        com.logitech.logiux.newjackcity.logging.FireLog.i(this, "User - Telco Confirmed");
        com.logitech.logiux.newjackcity.manager.Prefs.get().setOtaServer(com.logitech.logiux.newjackcity.helper.OTAServer.TELCO);
        if (this.mView != 0) {
            ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).restartInDemoMode();
        }
    }

    @Override // com.logitech.logiux.newjackcity.presenter.IDevOptionsPresenter
    public void onTelcoCanceled() {
        if (this.mView != 0) {
            ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).setCurrentOtaServer(com.logitech.logiux.newjackcity.manager.Prefs.get().getOTAServer());
        }
    }

    private int getSelectedIndexByTag(java.lang.String str) {
        if (this.mAlexaLanguageTags.contains(str)) {
            return this.mAlexaLanguageTags.indexOf(str);
        }
        return this.mAlexaLanguageTags.indexOf(java.util.Locale.US.toLanguageTag());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getLogSizeInB(java.io.File file) {
        if (file == null || !file.exists()) {
            return 0L;
        }
        return file.length();
    }

    public static java.lang.String humanReadableByteCount(long j) {
        if (j < 1024) {
            return j + " B";
        }
        double d = j;
        double d2 = 1024;
        int iLog = (int) (java.lang.Math.log(d) / java.lang.Math.log(d2));
        return java.lang.String.format("%.1f %sB", java.lang.Double.valueOf(d / java.lang.Math.pow(d2, iLog)), java.lang.String.valueOf("kMGTPE".charAt(iLog - 1)));
    }

    private java.lang.String getLogsSizeString() {
        return humanReadableByteCount(getLogSizeInB(new java.io.File(com.logitech.ue.avs.tools.CSVLogger.getLogFilePath())));
    }

    private void sendFileViaEmail(android.net.Uri uri) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.SEND", android.net.Uri.parse("mailto"));
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "Logs");
        intent.putExtra("android.intent.extra.EMAIL", new java.lang.String[]{"achakravarthy@logitech.com"});
        intent.putExtra("android.intent.extra.TEXT", "PFA");
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.addFlags(1);
        ((com.logitech.logiux.newjackcity.view.IDevOptionsView) this.mView).openIntent(android.content.Intent.createChooser(intent, "Email:"));
    }
}
