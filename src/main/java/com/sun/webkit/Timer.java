/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.SeparateThreadTimer;
import com.sun.webkit.WebPage;
import java.security.AccessController;

public class Timer {
    private static Timer instance;
    private static Mode mode;
    long fireTime;

    Timer() {
    }

    public static synchronized Mode getMode() {
        if (mode == null) {
            mode = Boolean.valueOf(AccessController.doPrivileged(() -> System.getProperty("com.sun.webkit.platformticks", "true"))) != false ? Mode.PLATFORM_TICKS : Mode.SEPARATE_THREAD;
        }
        return mode;
    }

    public static synchronized Timer getTimer() {
        if (instance == null) {
            instance = Timer.getMode() == Mode.PLATFORM_TICKS ? new Timer() : new SeparateThreadTimer();
        }
        return instance;
    }

    public synchronized void notifyTick() {
        if (this.fireTime > 0L && this.fireTime <= System.currentTimeMillis()) {
            this.fireTimerEvent(this.fireTime);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void fireTimerEvent(long l) {
        boolean bl = false;
        Timer timer = this;
        synchronized (timer) {
            if (l == this.fireTime) {
                bl = true;
                this.fireTime = 0L;
            }
        }
        if (bl) {
            WebPage.lockPage();
            try {
                Timer.twkFireTimerEvent();
            }
            finally {
                WebPage.unlockPage();
            }
        }
    }

    synchronized void setFireTime(long l) {
        this.fireTime = l;
    }

    private static void fwkSetFireTime(double d) {
        Timer.getTimer().setFireTime((long)Math.ceil(d * 1000.0));
    }

    private static void fwkStopTimer() {
        Timer.getTimer().setFireTime(0L);
    }

    private static native void twkFireTimerEvent();

    public static enum Mode {
        PLATFORM_TICKS,
        SEPARATE_THREAD;

    }
}

