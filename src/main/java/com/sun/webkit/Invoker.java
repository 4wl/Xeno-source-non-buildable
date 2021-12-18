/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.perf.PerfLogger;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Invoker {
    private static Invoker instance;
    private static final PerfLogger locksLog;

    public static synchronized void setInvoker(Invoker invoker) {
        instance = invoker;
    }

    public static synchronized Invoker getInvoker() {
        return instance;
    }

    protected boolean lock(ReentrantLock reentrantLock) {
        if (reentrantLock.getHoldCount() == 0) {
            reentrantLock.lock();
            locksLog.resumeCount(this.isEventThread() ? "EventThread" : "RenderThread");
            return true;
        }
        return false;
    }

    protected boolean unlock(ReentrantLock reentrantLock) {
        if (reentrantLock.getHoldCount() != 0) {
            locksLog.suspendCount(this.isEventThread() ? "EventThread" : "RenderThread");
            reentrantLock.unlock();
            return true;
        }
        return false;
    }

    protected abstract boolean isEventThread();

    public void checkEventThread() {
        if (!this.isEventThread()) {
            throw new IllegalStateException("Current thread is not event thread, current thread: " + Thread.currentThread().getName());
        }
    }

    public abstract void invokeOnEventThread(Runnable var1);

    public abstract void postOnEventThread(Runnable var1);

    static {
        locksLog = PerfLogger.getLogger("Locks");
    }
}

