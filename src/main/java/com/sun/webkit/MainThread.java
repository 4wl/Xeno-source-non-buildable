/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.Invoker;

final class MainThread {
    MainThread() {
    }

    private static void fwkScheduleDispatchFunctions() {
        Invoker.getInvoker().postOnEventThread(() -> MainThread.twkScheduleDispatchFunctions());
    }

    private static native void twkScheduleDispatchFunctions();
}

