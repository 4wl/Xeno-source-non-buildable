/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;

class GtkWindow
extends Window {
    public GtkWindow(Window window, Screen screen, int n) {
        super(window, screen, n);
    }

    protected GtkWindow(long l) {
        super(l);
    }

    @Override
    protected native long _createWindow(long var1, long var3, int var5);

    @Override
    protected native long _createChildWindow(long var1);

    @Override
    protected native boolean _close(long var1);

    @Override
    protected native boolean _setView(long var1, View var3);

    @Override
    protected boolean _setMenubar(long l, long l2) {
        return true;
    }

    private native void minimizeImpl(long var1, boolean var3);

    private native void maximizeImpl(long var1, boolean var3, boolean var4);

    private native void setBoundsImpl(long var1, int var3, int var4, boolean var5, boolean var6, int var7, int var8, int var9, int var10);

    private native void setVisibleImpl(long var1, boolean var3);

    @Override
    protected native boolean _setResizable(long var1, boolean var3);

    @Override
    protected native boolean _requestFocus(long var1, int var3);

    @Override
    protected native void _setFocusable(long var1, boolean var3);

    @Override
    protected native boolean _grabFocus(long var1);

    @Override
    protected native void _ungrabFocus(long var1);

    @Override
    protected native boolean _setTitle(long var1, String var3);

    @Override
    protected native void _setLevel(long var1, int var3);

    @Override
    protected native void _setAlpha(long var1, float var3);

    @Override
    protected native boolean _setBackground(long var1, float var3, float var4, float var5);

    @Override
    protected native void _setEnabled(long var1, boolean var3);

    @Override
    protected native boolean _setMinimumSize(long var1, int var3, int var4);

    @Override
    protected native boolean _setMaximumSize(long var1, int var3, int var4);

    @Override
    protected native void _setIcon(long var1, Pixels var3);

    @Override
    protected native void _toFront(long var1);

    @Override
    protected native void _toBack(long var1);

    @Override
    protected native void _enterModal(long var1);

    @Override
    protected native void _enterModalWithWindow(long var1, long var3);

    @Override
    protected native void _exitModal(long var1);

    protected native long _getNativeWindowImpl(long var1);

    private native boolean isVisible(long var1);

    @Override
    protected boolean _setVisible(long l, boolean bl) {
        this.setVisibleImpl(l, bl);
        return this.isVisible(l);
    }

    @Override
    protected boolean _minimize(long l, boolean bl) {
        this.minimizeImpl(l, bl);
        this.notifyStateChanged(531);
        return bl;
    }

    @Override
    protected boolean _maximize(long l, boolean bl, boolean bl2) {
        this.maximizeImpl(l, bl, bl2);
        this.notifyStateChanged(532);
        return bl;
    }

    private native void _showOrHideChildren(long var1, boolean var3);

    protected void notifyStateChanged(int n) {
        if (n == 531) {
            this._showOrHideChildren(this.getNativeHandle(), false);
        } else if (n == 533) {
            this._showOrHideChildren(this.getNativeHandle(), true);
        }
        switch (n) {
            case 531: 
            case 532: 
            case 533: {
                this.notifyResize(n, this.getWidth(), this.getHeight());
                break;
            }
            default: {
                System.err.println("Unknown window state: " + n);
            }
        }
    }

    @Override
    protected void _setCursor(long l, Cursor cursor) {
        if (cursor.getType() == 0) {
            this._setCustomCursor(l, cursor);
        } else {
            this._setCursorType(l, cursor.getType());
        }
    }

    private native void _setCursorType(long var1, int var3);

    private native void _setCustomCursor(long var1, Cursor var3);

    @Override
    protected native int _getEmbeddedX(long var1);

    @Override
    protected native int _getEmbeddedY(long var1);

    @Override
    public long getNativeWindow() {
        return this._getNativeWindowImpl(super.getNativeWindow());
    }

    private native void _setGravity(long var1, float var3, float var4);

    @Override
    protected void _setBounds(long l, int n, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, float f, float f2) {
        this._setGravity(l, f, f2);
        this.setBoundsImpl(l, n, n2, bl, bl2, n3, n4, n5, n6);
        if (n3 <= 0 && n5 > 0 || n4 <= 0 && n6 > 0) {
            int[] arrn = new int[4];
            this.getFrameExtents(l, arrn);
            this.notifyResize(511, n3 <= 0 && n5 > 0 ? n5 + arrn[0] + arrn[1] : n3, n4 <= 0 && n6 > 0 ? n6 + arrn[2] + arrn[3] : n4);
        }
    }

    private native void getFrameExtents(long var1, int[] var3);

    @Override
    protected void _requestInput(long l, String string, int n, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void _releaseInput(long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

