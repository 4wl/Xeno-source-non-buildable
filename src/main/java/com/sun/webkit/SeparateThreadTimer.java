/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.Invoker;
import com.sun.webkit.Timer;

final class SeparateThreadTimer
extends Timer
implements Runnable {
    private final Invoker invoker = Invoker.getInvoker();
    private final FireRunner fireRunner = new FireRunner();
    private final Thread thread = new Thread((Runnable)this, "WebPane-Timer");

    SeparateThreadTimer() {
        this.thread.setDaemon(true);
    }

    @Override
    synchronized void setFireTime(long l) {
        super.setFireTime(l);
        if (this.thread.getState() == Thread.State.NEW) {
            this.thread.start();
        }
        this.notifyAll();
    }

    @Override
    public synchronized void run() {
        try {
            while (true) {
                if (this.fireTime > 0L) {
                    long l = System.currentTimeMillis();
                    while (this.fireTime > l) {
                        this.wait(this.fireTime - l);
                        l = System.currentTimeMillis();
                    }
                    if (this.fireTime > 0L) {
                        this.invoker.invokeOnEventThread(this.fireRunner.forTime(this.fireTime));
                    }
                }
                this.wait();
            }
        }
        catch (InterruptedException interruptedException) {
            return;
        }
    }

    @Override
    public void notifyTick() {
        assert (false);
    }

    private final class FireRunner
    implements Runnable {
        private volatile long time;

        private FireRunner() {
        }

        private Runnable forTime(long l) {
            this.time = l;
            return this;
        }

        @Override
        public void run() {
            SeparateThreadTimer.this.fireTimerEvent(this.time);
        }
    }
}

