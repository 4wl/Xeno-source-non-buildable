/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.web;

import javafx.util.Callback;

public interface Debugger {
    public boolean isEnabled();

    public void setEnabled(boolean var1);

    public void sendMessage(String var1);

    public Callback<String, Void> getMessageCallback();

    public void setMessageCallback(Callback<String, Void> var1);
}

