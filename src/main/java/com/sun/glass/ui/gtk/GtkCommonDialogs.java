/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Window;
import java.io.File;

final class GtkCommonDialogs {
    GtkCommonDialogs() {
    }

    private static native CommonDialogs.FileChooserResult _showFileChooser(long var0, String var2, String var3, String var4, int var5, boolean var6, CommonDialogs.ExtensionFilter[] var7, int var8);

    private static native String _showFolderChooser(long var0, String var2, String var3);

    static CommonDialogs.FileChooserResult showFileChooser(Window window, String string, String string2, String string3, int n, boolean bl, CommonDialogs.ExtensionFilter[] arrextensionFilter, int n2) {
        if (window != null) {
            window.setEnabled(false);
        }
        CommonDialogs.FileChooserResult fileChooserResult = GtkCommonDialogs._showFileChooser(window == null ? 0L : window.getNativeHandle(), string, string2, string3, n, bl, arrextensionFilter, n2);
        if (window != null) {
            window.setEnabled(true);
        }
        return fileChooserResult;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static File showFolderChooser(Window window, String string, String string2) {
        if (window != null) {
            window.setEnabled(false);
        }
        try {
            String string3 = GtkCommonDialogs._showFolderChooser(window != null ? window.getNativeHandle() : 0L, string, string2);
            File file = string3 != null ? new File(string3) : null;
            return file;
        }
        finally {
            if (window != null) {
                window.setEnabled(true);
            }
        }
    }
}

