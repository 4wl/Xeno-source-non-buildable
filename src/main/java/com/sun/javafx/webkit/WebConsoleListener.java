/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.web.WebView
 */
package com.sun.javafx.webkit;

import com.sun.javafx.webkit.WebPageClientImpl;
import javafx.scene.web.WebView;

public interface WebConsoleListener {
    public static void setDefaultListener(WebConsoleListener webConsoleListener) {
        WebPageClientImpl.setConsoleListener(webConsoleListener);
    }

    public void messageAdded(WebView var1, String var2, int var3, String var4);
}

