/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 */
package com.sun.javafx.webkit;

import com.sun.javafx.tk.Toolkit;
import com.sun.webkit.EventLoop;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

public final class EventLoopImpl
extends EventLoop {
    private static final long DELAY = 20L;
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void cycle() {
        final Object object = new Object();
        executor.schedule(() -> Platform.runLater((Runnable)new Runnable(){

            @Override
            public void run() {
                Toolkit.getToolkit().exitNestedEventLoop(object, null);
            }
        }), 20L, TimeUnit.MILLISECONDS);
        Toolkit.getToolkit().enterNestedEventLoop(object);
    }
}

