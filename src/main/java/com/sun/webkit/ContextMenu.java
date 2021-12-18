/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.ContextMenuItem;
import com.sun.webkit.Utilities;
import com.sun.webkit.WebPage;

public abstract class ContextMenu {
    protected abstract void show(ShowContext var1, int var2, int var3);

    protected abstract void appendItem(ContextMenuItem var1);

    protected abstract void insertItem(ContextMenuItem var1, int var2);

    protected abstract int getItemCount();

    private static ContextMenu fwkCreateContextMenu() {
        return Utilities.getUtilities().createContextMenu();
    }

    private void fwkShow(WebPage webPage, long l, int n, int n2) {
        this.show(new ShowContext(webPage, l), n, n2);
    }

    private void fwkAppendItem(ContextMenuItem contextMenuItem) {
        this.appendItem(contextMenuItem);
    }

    private void fwkInsertItem(ContextMenuItem contextMenuItem, int n) {
        this.insertItem(contextMenuItem, n);
    }

    private int fwkGetItemCount() {
        return this.getItemCount();
    }

    private native void twkHandleItemSelected(long var1, int var3);

    public final class ShowContext {
        private final WebPage page;
        private final long pdata;

        private ShowContext(WebPage webPage, long l) {
            this.page = webPage;
            this.pdata = l;
        }

        public WebPage getPage() {
            return this.page;
        }

        public void notifyItemSelected(int n) {
            ContextMenu.this.twkHandleItemSelected(this.pdata, n);
        }
    }
}

