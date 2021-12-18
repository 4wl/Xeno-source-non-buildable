/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.graphics.WCImageFrame;

public interface Pasteboard {
    public String getPlainText();

    public String getHtml();

    public void writePlainText(String var1);

    public void writeSelection(boolean var1, String var2, String var3);

    public void writeImage(WCImageFrame var1);

    public void writeUrl(String var1, String var2);
}

