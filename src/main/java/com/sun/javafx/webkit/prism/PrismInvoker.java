/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.Toolkit;
import com.sun.webkit.Invoker;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantLock;

public final class PrismInvoker
extends Invoker {
    @Override
    protected boolean lock(ReentrantLock reentrantLock) {
        return false;
    }

    @Override
    protected boolean unlock(ReentrantLock reentrantLock) {
        return false;
    }

    @Override
    protected boolean isEventThread() {
        return PrismInvoker.isEventThreadPrivate();
    }

    private static boolean isEventThreadPrivate() {
        return Toolkit.getToolkit().isFxUserThread();
    }

    @Override
    public void checkEventThread() {
        Toolkit.getToolkit().checkFxUserThread();
    }

    @Override
    public void invokeOnEventThread(Runnable runnable) {
        if (this.isEventThread()) {
            runnable.run();
        } else {
            PlatformImpl.runLater(runnable);
        }
    }

    @Override
    public void postOnEventThread(Runnable runnable) {
        PlatformImpl.runLater(runnable);
    }

    static void invokeOnRenderThread(Runnable runnable) {
        Toolkit.getToolkit().addRenderJob(new RenderJob(runnable));
    }

    static void runOnRenderThread(Runnable runnable) {
        if (Thread.currentThread().getName().startsWith("QuantumRenderer")) {
            runnable.run();
        } else {
            FutureTask<Object> futureTask = new FutureTask<Object>(runnable, null);
            Toolkit.getToolkit().addRenderJob(new RenderJob(futureTask));
            try {
                futureTask.get();
            }
            catch (ExecutionException executionException) {
                throw new AssertionError((Object)executionException);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }
}

