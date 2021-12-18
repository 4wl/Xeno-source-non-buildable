/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;

class GtkMenuBarDelegate
implements MenuBarDelegate {
    @Override
    public boolean createMenuBar() {
        return true;
    }

    @Override
    public boolean insert(MenuDelegate menuDelegate, int n) {
        return true;
    }

    @Override
    public boolean remove(MenuDelegate menuDelegate, int n) {
        return true;
    }

    @Override
    public long getNativeMenu() {
        return 0L;
    }
}

