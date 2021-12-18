/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import com.sun.glass.ui.gtk.GtkApplication;
import com.sun.glass.ui.gtk.GtkClipboardDelegate;
import com.sun.glass.ui.gtk.GtkMenuBarDelegate;
import com.sun.glass.ui.gtk.GtkMenuDelegate;
import com.sun.glass.ui.gtk.GtkMenuItemDelegate;

public final class GtkPlatformFactory
extends PlatformFactory {
    @Override
    public Application createApplication() {
        return new GtkApplication();
    }

    @Override
    public MenuBarDelegate createMenuBarDelegate(MenuBar menuBar) {
        return new GtkMenuBarDelegate();
    }

    @Override
    public MenuDelegate createMenuDelegate(Menu menu) {
        return new GtkMenuDelegate();
    }

    @Override
    public MenuItemDelegate createMenuItemDelegate(MenuItem menuItem) {
        return new GtkMenuItemDelegate();
    }

    @Override
    public ClipboardDelegate createClipboardDelegate() {
        return new GtkClipboardDelegate();
    }
}

