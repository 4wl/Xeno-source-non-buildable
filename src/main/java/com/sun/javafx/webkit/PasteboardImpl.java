/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.Image
 *  javafx.scene.input.Clipboard
 *  javafx.scene.input.ClipboardContent
 */
package com.sun.javafx.webkit;

import com.sun.webkit.Pasteboard;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImageFrame;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

final class PasteboardImpl
implements Pasteboard {
    private final Clipboard clipboard = Clipboard.getSystemClipboard();

    PasteboardImpl() {
    }

    @Override
    public String getPlainText() {
        return this.clipboard.getString();
    }

    @Override
    public String getHtml() {
        return this.clipboard.getHtml();
    }

    @Override
    public void writePlainText(String string) {
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(string);
        this.clipboard.setContent((Map)clipboardContent);
    }

    @Override
    public void writeSelection(boolean bl, String string, String string2) {
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(string);
        clipboardContent.putHtml(string2);
        this.clipboard.setContent((Map)clipboardContent);
    }

    @Override
    public void writeImage(WCImageFrame wCImageFrame) {
        Object object = WCGraphicsManager.getGraphicsManager().toPlatformImage(wCImageFrame.getFrame());
        Image image = Image.impl_fromPlatformImage((Object)object);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putImage(image);
        this.clipboard.setContent((Map)clipboardContent);
    }

    @Override
    public void writeUrl(String string, String string2) {
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(string);
        clipboardContent.putHtml(string2);
        clipboardContent.putUrl(string);
        this.clipboard.setContent((Map)clipboardContent);
    }
}

