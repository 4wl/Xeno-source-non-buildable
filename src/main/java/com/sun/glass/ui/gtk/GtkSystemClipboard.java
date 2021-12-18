/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.SystemClipboard;
import java.util.HashMap;

final class GtkSystemClipboard
extends SystemClipboard {
    public GtkSystemClipboard() {
        super("SYSTEM");
        this.init();
    }

    @Override
    protected void close() {
        super.close();
        this.dispose();
    }

    private native void init();

    private native void dispose();

    @Override
    protected native boolean isOwner();

    @Override
    protected native void pushToSystem(HashMap<String, Object> var1, int var2);

    @Override
    protected native void pushTargetActionToSystem(int var1);

    @Override
    protected native Object popFromSystem(String var1);

    @Override
    protected native int supportedSourceActionsFromSystem();

    @Override
    protected native String[] mimesFromSystem();
}

