/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;

public interface WebPageClient<T> {
    public void setCursor(long var1);

    public void setFocus(boolean var1);

    public void transferFocus(boolean var1);

    public void setTooltip(String var1);

    public WCRectangle getScreenBounds(boolean var1);

    public int getScreenDepth();

    public T getContainer();

    public WCPoint screenToWindow(WCPoint var1);

    public WCPoint windowToScreen(WCPoint var1);

    public WCPageBackBuffer createBackBuffer();

    public boolean isBackBufferSupported();

    public void addMessageToConsole(String var1, int var2, String var3);

    public void didClearWindowObject(long var1, long var3);
}

