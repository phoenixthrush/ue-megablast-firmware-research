package com.logitech.logiux.newjackcity.utils;

/* JADX INFO: loaded from: classes2.dex */
public class SpeakerFileReader {
    private com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener eventListener;
    private final int fileNumber;

    public interface OnEventListener {
        void onChunk(byte[] bArr);

        void onComplete();

        void onError(java.lang.Throwable th);

        void onStart();
    }

    public SpeakerFileReader(int i) {
        this.fileNumber = i;
    }

    public void setOnEventListener(com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener onEventListener) {
        this.eventListener = onEventListener;
    }

    public void readFile(final com.logitech.ue.centurion.device.Device device) {
        com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener onEventListener = this.eventListener;
        if (onEventListener != null) {
            onEventListener.onStart();
        }
        device.fileSize(this.fileNumber).compose(com.logitech.logiux.newjackcity.utils.schedulers.SchedulerProvider.get().applyBackgroundTaskSchedulers()).subscribe((rx.functions.Action1<? super R>) new rx.functions.Action1() { // from class: com.logitech.logiux.newjackcity.utils.SpeakerFileReader$$ExternalSyntheticLambda6
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m596x8168db77(device, (com.logitech.ue.centurion.devicedata.FileSizeData) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.logiux.newjackcity.utils.SpeakerFileReader$$ExternalSyntheticLambda3
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m597xa6fce478((java.lang.Throwable) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$readFile$0$com-logitech-logiux-newjackcity-utils-SpeakerFileReader, reason: not valid java name */
    /* synthetic */ void m596x8168db77(com.logitech.ue.centurion.device.Device device, com.logitech.ue.centurion.devicedata.FileSizeData fileSizeData) {
        com.logitech.logiux.newjackcity.logging.FireLog.d(this, "File size is: " + fileSizeData.fileSize);
        openFile(device, fileSizeData.fileSize);
    }

    private void openFile(final com.logitech.ue.centurion.device.Device device, final int i) {
        device.fileOpen(this.fileNumber, 1).compose(com.logitech.logiux.newjackcity.utils.schedulers.SchedulerProvider.get().applyBackgroundTaskSchedulers()).subscribe((rx.functions.Action1<? super R>) new rx.functions.Action1() { // from class: com.logitech.logiux.newjackcity.utils.SpeakerFileReader$$ExternalSyntheticLambda7
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m592x625bb72d(device, i, (java.lang.Void) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.logiux.newjackcity.utils.SpeakerFileReader$$ExternalSyntheticLambda1
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m593x87efc02e((java.lang.Throwable) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$openFile$2$com-logitech-logiux-newjackcity-utils-SpeakerFileReader, reason: not valid java name */
    /* synthetic */ void m592x625bb72d(com.logitech.ue.centurion.device.Device device, int i, java.lang.Void r3) {
        com.logitech.logiux.newjackcity.logging.FireLog.d(this, "File open to read.");
        readChunk(device, i);
    }

    private void readChunk(final com.logitech.ue.centurion.device.Device device, final int i) {
        final int i2 = com.logitech.ue.centurion.devicedata.FileSizeData.FILE_CHUNK_IN_BYTES;
        if (i <= 4000) {
            i2 = i;
        }
        com.logitech.logiux.newjackcity.logging.FireLog.d(this, "Chunk size = " + i2);
        device.fileRead(this.fileNumber, i2).compose(com.logitech.logiux.newjackcity.utils.schedulers.SchedulerProvider.get().applyBackgroundTaskSchedulers()).subscribe((rx.functions.Action1<? super R>) new rx.functions.Action1() { // from class: com.logitech.logiux.newjackcity.utils.SpeakerFileReader$$ExternalSyntheticLambda5
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m594xaf5d8926(i, i2, device, (com.logitech.ue.centurion.devicedata.FileReadData) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.logiux.newjackcity.utils.SpeakerFileReader$$ExternalSyntheticLambda2
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m595xd4f19227((java.lang.Throwable) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$readChunk$4$com-logitech-logiux-newjackcity-utils-SpeakerFileReader, reason: not valid java name */
    /* synthetic */ void m594xaf5d8926(int i, int i2, com.logitech.ue.centurion.device.Device device, com.logitech.ue.centurion.devicedata.FileReadData fileReadData) {
        com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener onEventListener = this.eventListener;
        if (onEventListener != null) {
            onEventListener.onChunk(fileReadData.fileData);
        }
        int i3 = i - i2;
        if (i3 > 0) {
            readChunk(device, i3);
        } else {
            closeSpeakerLogFile(device);
        }
    }

    private void closeSpeakerLogFile(com.logitech.ue.centurion.device.Device device) {
        device.fileClose(this.fileNumber).compose(com.logitech.logiux.newjackcity.utils.schedulers.SchedulerProvider.get().applyBackgroundTaskSchedulers()).subscribe((rx.functions.Action1<? super R>) new rx.functions.Action1() { // from class: com.logitech.logiux.newjackcity.utils.SpeakerFileReader$$ExternalSyntheticLambda4
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m590x74abac6a((java.lang.Void) obj);
            }
        }, new rx.functions.Action1() { // from class: com.logitech.logiux.newjackcity.utils.SpeakerFileReader$$ExternalSyntheticLambda0
            @Override // rx.functions.Action1
            public final void call(java.lang.Object obj) {
                this.f$0.m591x9a3fb56b((java.lang.Throwable) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$closeSpeakerLogFile$6$com-logitech-logiux-newjackcity-utils-SpeakerFileReader, reason: not valid java name */
    /* synthetic */ void m590x74abac6a(java.lang.Void r1) {
        com.logitech.logiux.newjackcity.logging.FireLog.d(this, "File closed.");
        com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener onEventListener = this.eventListener;
        if (onEventListener != null) {
            onEventListener.onComplete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: notifyOnError, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void m597xa6fce478(java.lang.Throwable th) {
        com.logitech.logiux.newjackcity.utils.SpeakerFileReader.OnEventListener onEventListener = this.eventListener;
        if (onEventListener != null) {
            onEventListener.onError(th);
        }
    }
}
