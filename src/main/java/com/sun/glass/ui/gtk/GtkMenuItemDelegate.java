/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuItemDelegate;

class GtkMenuItemDelegate
implements MenuItemDelegate {
    @Override
    public boolean createMenuItem(String string, MenuItem.Callback callback, int n, int n2, Pixels pixels, boolean bl, boolean bl2) {
        return true;
    }

    @Override
    public boolean setTitle(String string) {
        return true;
    }

    @Override
    public boolean setCallback(MenuItem.Callback callback) {
        return true;
    }

    @Override
    public boolean setShortcut(int n, int n2) {
        return true;
    }

    @Override
    public boolean setPixels(Pixels pixels) {
        return true;
    }

    @Override
    public boolean setEnabled(boolean bl) {
        return true;
    }

    @Override
    public boolean setChecked(boolean bl) {
        return true;
    }
}

