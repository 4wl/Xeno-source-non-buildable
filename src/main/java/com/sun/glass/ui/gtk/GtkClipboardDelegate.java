/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.gtk.GtkDnDClipboard;
import com.sun.glass.ui.gtk.GtkSystemClipboard;

class GtkClipboardDelegate
implements ClipboardDelegate {
    @Override
    public Clipboard createClipboard(String string) {
        if ("SYSTEM".equals(string)) {
            return new GtkSystemClipboard();
        }
        if ("DND".equals(string)) {
            return new GtkDnDClipboard();
        }
        return null;
    }
}

