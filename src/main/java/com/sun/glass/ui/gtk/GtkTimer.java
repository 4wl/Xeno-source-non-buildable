/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.Timer;

final class GtkTimer
extends Timer {
    public GtkTimer(Runnable runnable) {
        super(runnable);
    }

    @Override
    protected long _start(Runnable runnable) {
        throw new RuntimeException("vsync timer not supported");
    }

    @Override
    protected native long _start(Runnable var1, int var2);

    @Override
    protected native void _stop(long var1);
}

