package com.logitech.ue.centurion;

/* JADX INFO: loaded from: classes2.dex */
public class AuthorizationRetry implements rx.functions.Func1<rx.Observable<? extends java.lang.Throwable>, rx.Observable<?>> {
    public static final long INIT_WAIT_TIME = 250;
    private java.util.concurrent.CountDownLatch authWaitingLatch = new java.util.concurrent.CountDownLatch(1);
    private com.logitech.ue.centurion.eventbus.event.AuthorizationStatus event;

    public AuthorizationRetry() {
        com.logitech.ue.centurion.eventbus.CenturionEventBus.get().register(this);
    }

    @Override // rx.functions.Func1
    public rx.Observable<?> call(final rx.Observable<? extends java.lang.Throwable> observable) {
        return observable.flatMap(new rx.functions.Func1() { // from class: com.logitech.ue.centurion.AuthorizationRetry$$ExternalSyntheticLambda0
            @Override // rx.functions.Func1
            public final java.lang.Object call(java.lang.Object obj) {
                return this.f$0.m600lambda$call$0$comlogitechuecenturionAuthorizationRetry(observable, (java.lang.Throwable) obj);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$call$0$com-logitech-ue-centurion-AuthorizationRetry, reason: not valid java name */
    /* synthetic */ rx.Observable m600lambda$call$0$comlogitechuecenturionAuthorizationRetry(rx.Observable observable, java.lang.Throwable th) {
        if ((th.getCause() instanceof com.logitech.ue.centurion.exceptions.MessageErrorResultException) && ((com.logitech.ue.centurion.exceptions.CenturionException) th.getCause()).getErrorCode() == 14) {
            try {
                this.authWaitingLatch.await();
                java.lang.Thread.sleep(250L);
            } catch (java.lang.InterruptedException unused) {
            }
            com.logitech.ue.centurion.eventbus.event.AuthorizationStatus authorizationStatus = this.event;
            if (authorizationStatus != null && authorizationStatus.status == 0) {
                unregister();
                return observable;
            }
        }
        unregister();
        return rx.Observable.error(th);
    }

    @org.greenrobot.eventbus.Subscribe(priority = 3, threadMode = org.greenrobot.eventbus.ThreadMode.BACKGROUND)
    public void onAuthorizationStatus(com.logitech.ue.centurion.eventbus.event.AuthorizationStatus authorizationStatus) {
        this.event = authorizationStatus;
        java.util.concurrent.CountDownLatch countDownLatch = this.authWaitingLatch;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    private void unregister() {
        if (com.logitech.ue.centurion.eventbus.CenturionEventBus.get().isRegistered(this)) {
            com.logitech.ue.centurion.eventbus.CenturionEventBus.get().unregister(this);
        }
    }
}
