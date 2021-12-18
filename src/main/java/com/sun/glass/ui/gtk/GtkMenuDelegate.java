/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;

class GtkMenuDelegate
implements MenuDelegate {
    @Override
    public boolean createMenu(String string, boolean bl) {
        return true;
    }

    @Override
    public boolean setTitle(String string) {
        return true;
    }

    @Override
    public boolean setEnabled(boolean bl) {
        return true;
    }

    @Override
    public boolean setPixels(Pixels pixels) {
        return false;
    }

    @Override
    public boolean insert(MenuDelegate menuDelegate, int n) {
        return true;
    }

    @Override
    public boolean insert(MenuItemDelegate menuItemDelegate, int n) {
        return true;
    }

    @Override
    public boolean remove(MenuDelegate menuDelegate, int n) {
        return true;
    }

    @Override
    public boolean remove(MenuItemDelegate menuItemDelegate, int n) {
        return true;
    }
}

