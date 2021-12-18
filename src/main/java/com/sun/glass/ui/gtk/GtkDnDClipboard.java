/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.SystemClipboard;
import java.util.HashMap;

final class GtkDnDClipboard
extends SystemClipboard {
    public GtkDnDClipboard() {
        super("DND");
    }

    @Override
    protected void pushToSystem(HashMap<String, Object> hashMap, int n) {
        int n2 = this.pushToSystemImpl(hashMap, n);
        this.actionPerformed(n2);
    }

    @Override
    protected native boolean isOwner();

    protected native int pushToSystemImpl(HashMap<String, Object> var1, int var2);

    @Override
    protected native void pushTargetActionToSystem(int var1);

    @Override
    protected native Object popFromSystem(String var1);

    @Override
    protected native int supportedSourceActionsFromSystem();

    @Override
    protected native String[] mimesFromSystem();
}

