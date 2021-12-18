/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.Pasteboard;
import com.sun.webkit.Utilities;
import com.sun.webkit.graphics.WCImageFrame;
import java.util.logging.Level;
import java.util.logging.Logger;

final class WCPasteboard {
    private static final Logger log = Logger.getLogger(WCPasteboard.class.getName());
    private static final Pasteboard pasteboard = Utilities.getUtilities().createPasteboard();

    private WCPasteboard() {
    }

    private static String getPlainText() {
        log.fine("getPlainText()");
        return pasteboard.getPlainText();
    }

    private static String getHtml() {
        log.fine("getHtml()");
        return pasteboard.getHtml();
    }

    private static void writePlainText(String string) {
        log.log(Level.FINE, "writePlainText(): text = {0}", new Object[]{string});
        pasteboard.writePlainText(string);
    }

    private static void writeSelection(boolean bl, String string, String string2) {
        log.log(Level.FINE, "writeSelection(): canSmartCopyOrDelete = {0},\n text = \n{1}\n html=\n{2}", new Object[]{bl, string, string2});
        pasteboard.writeSelection(bl, string, string2);
    }

    private static void writeImage(WCImageFrame wCImageFrame) {
        log.log(Level.FINE, "writeImage(): img = {0}", new Object[]{wCImageFrame});
        pasteboard.writeImage(wCImageFrame);
    }

    private static void writeUrl(String string, String string2) {
        log.log(Level.FINE, "writeUrl(): url = {0}, markup = {1}", new Object[]{string, string2});
        pasteboard.writeUrl(string, string2);
    }
}

