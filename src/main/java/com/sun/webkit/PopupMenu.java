/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.Utilities;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCFont;

public abstract class PopupMenu {
    private long pdata;

    protected abstract void show(WebPage var1, int var2, int var3, int var4);

    protected abstract void hide();

    protected abstract void setSelectedItem(int var1);

    protected abstract void appendItem(String var1, boolean var2, boolean var3, boolean var4, int var5, int var6, WCFont var7);

    protected void notifySelectionCommited(int n) {
        this.twkSelectionCommited(this.pdata, n);
    }

    protected void notifyPopupClosed() {
        this.twkPopupClosed(this.pdata);
    }

    private static PopupMenu fwkCreatePopupMenu(long l) {
        PopupMenu popupMenu = Utilities.getUtilities().createPopupMenu();
        popupMenu.pdata = l;
        return popupMenu;
    }

    private void fwkShow(WebPage webPage, int n, int n2, int n3) {
        assert (webPage != null);
        this.show(webPage, n, n2, n3);
    }

    private void fwkHide() {
        this.hide();
    }

    private void fwkSetSelectedItem(int n) {
        this.setSelectedItem(n);
    }

    private void fwkAppendItem(String string, boolean bl, boolean bl2, boolean bl3, int n, int n2, WCFont wCFont) {
        this.appendItem(string, bl, bl2, bl3, n, n2, wCFont);
    }

    private void fwkDestroy() {
        this.pdata = 0L;
    }

    private native void twkSelectionCommited(long var1, int var3);

    private native void twkPopupClosed(long var1);
}

