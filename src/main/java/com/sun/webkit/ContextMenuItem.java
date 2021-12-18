/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.ContextMenu;

public final class ContextMenuItem {
    public static final int ACTION_TYPE = 0;
    public static final int SEPARATOR_TYPE = 1;
    public static final int SUBMENU_TYPE = 2;
    private String title;
    private int action;
    private boolean isEnabled;
    private boolean isChecked;
    private int type;
    private ContextMenu submenu;

    public String getTitle() {
        return this.title;
    }

    public int getAction() {
        return this.action;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public int getType() {
        return this.type;
    }

    public ContextMenu getSubmenu() {
        return this.submenu;
    }

    public String toString() {
        return String.format("%s[title='%s', action=%d, enabled=%b, checked=%b, type=%d]", super.toString(), this.title, this.action, this.isEnabled, this.isChecked, this.type);
    }

    private static ContextMenuItem fwkCreateContextMenuItem() {
        return new ContextMenuItem();
    }

    private void fwkSetTitle(String string) {
        this.title = string;
    }

    private String fwkGetTitle() {
        return this.getTitle();
    }

    private void fwkSetAction(int n) {
        this.action = n;
    }

    private int fwkGetAction() {
        return this.getAction();
    }

    private void fwkSetEnabled(boolean bl) {
        this.isEnabled = bl;
    }

    private boolean fwkIsEnabled() {
        return this.isEnabled();
    }

    private void fwkSetChecked(boolean bl) {
        this.isChecked = bl;
    }

    private void fwkSetType(int n) {
        this.type = n;
    }

    private int fwkGetType() {
        return this.getType();
    }

    private void fwkSetSubmenu(ContextMenu contextMenu) {
        this.submenu = contextMenu;
    }

    private ContextMenu fwkGetSubmenu() {
        return this.getSubmenu();
    }
}

