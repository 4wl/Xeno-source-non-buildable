/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Size;

final class GtkCursor
extends Cursor {
    GtkCursor(int n) {
        super(n);
    }

    GtkCursor(int n, int n2, Pixels pixels) {
        super(n, n2, pixels);
    }

    @Override
    protected native long _createCursor(int var1, int var2, Pixels var3);

    static native Size _getBestSize(int var0, int var1);
}

