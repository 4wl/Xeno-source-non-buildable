/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CharacterDataImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

public class TextImpl
extends CharacterDataImpl
implements Text {
    TextImpl(long l) {
        super(l);
    }

    static Text getImpl(long l) {
        return (Text)TextImpl.create(l);
    }

    @Override
    public String getWholeText() {
        return TextImpl.getWholeTextImpl(this.getPeer());
    }

    static native String getWholeTextImpl(long var0);

    @Override
    public Text splitText(int n) throws DOMException {
        return TextImpl.getImpl(TextImpl.splitTextImpl(this.getPeer(), n));
    }

    static native long splitTextImpl(long var0, int var2);

    @Override
    public Text replaceWholeText(String string) throws DOMException {
        return TextImpl.getImpl(TextImpl.replaceWholeTextImpl(this.getPeer(), string));
    }

    static native long replaceWholeTextImpl(long var0, String var2);

    @Override
    public boolean isElementContentWhitespace() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

