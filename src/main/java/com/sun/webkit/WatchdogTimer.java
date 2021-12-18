/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

final class WatchdogTimer {
    private static final Logger logger = Logger.getLogger(WatchdogTimer.class.getName());
    private static final ThreadFactory threadFactory = new CustomThreadFactory();
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory);
    private final Runnable runnable;
    private ScheduledFuture<?> future;

    private WatchdogTimer(long l) {
        this.executor.setRemoveOnCancelPolicy(true);
        this.runnable = () -> {
            try {
                this.twkFire(l);
            }
            catch (Throwable throwable) {
                logger.log(Level.WARNING, "Error firing watchdog timer", throwable);
            }
        };
    }

    private static WatchdogTimer fwkCreate(long l) {
        return new WatchdogTimer(l);
    }

    private void fwkStart(double d) {
        if (this.future != null) {
            throw new IllegalStateException();
        }
        this.future = this.executor.schedule(this.runnable, (long)(d * 1000.0) + 50L, TimeUnit.MILLISECONDS);
    }

    private void fwkStop() {
        if (this.future == null) {
            throw new IllegalStateException();
        }
        this.future.cancel(false);
        this.future = null;
    }

    private void fwkDestroy() {
        this.executor.shutdownNow();
        while (true) {
            try {
                while (!this.executor.awaitTermination(1L, TimeUnit.SECONDS)) {
                }
            }
            catch (InterruptedException interruptedException) {
                continue;
            }
            break;
        }
    }

    private native void twkFire(long var1);

    private static final class CustomThreadFactory
    implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger index = new AtomicInteger(1);

        private CustomThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.group, runnable, "Watchdog-Timer-" + this.index.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    }
}

