/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.DocumentImpl;
import com.sun.webkit.dom.ElementImpl;
import com.sun.webkit.dom.HTMLCollectionImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;

public class HTMLDocumentImpl
extends DocumentImpl
implements HTMLDocument {
    HTMLDocumentImpl(long l) {
        super(l);
    }

    static HTMLDocument getImpl(long l) {
        return (HTMLDocument)HTMLDocumentImpl.create(l);
    }

    public HTMLCollection getEmbeds() {
        return HTMLCollectionImpl.getImpl(HTMLDocumentImpl.getEmbedsImpl(this.getPeer()));
    }

    static native long getEmbedsImpl(long var0);

    public HTMLCollection getPlugins() {
        return HTMLCollectionImpl.getImpl(HTMLDocumentImpl.getPluginsImpl(this.getPeer()));
    }

    static native long getPluginsImpl(long var0);

    public HTMLCollection getScripts() {
        return HTMLCollectionImpl.getImpl(HTMLDocumentImpl.getScriptsImpl(this.getPeer()));
    }

    static native long getScriptsImpl(long var0);

    public int getWidth() {
        return HTMLDocumentImpl.getWidthImpl(this.getPeer());
    }

    static native int getWidthImpl(long var0);

    public int getHeight() {
        return HTMLDocumentImpl.getHeightImpl(this.getPeer());
    }

    static native int getHeightImpl(long var0);

    public String getDir() {
        return HTMLDocumentImpl.getDirImpl(this.getPeer());
    }

    static native String getDirImpl(long var0);

    public void setDir(String string) {
        HTMLDocumentImpl.setDirImpl(this.getPeer(), string);
    }

    static native void setDirImpl(long var0, String var2);

    public String getDesignMode() {
        return HTMLDocumentImpl.getDesignModeImpl(this.getPeer());
    }

    static native String getDesignModeImpl(long var0);

    public void setDesignMode(String string) {
        HTMLDocumentImpl.setDesignModeImpl(this.getPeer(), string);
    }

    static native void setDesignModeImpl(long var0, String var2);

    @Override
    public String getCompatMode() {
        return HTMLDocumentImpl.getCompatModeImpl(this.getPeer());
    }

    static native String getCompatModeImpl(long var0);

    public Element getActiveElement() {
        return ElementImpl.getImpl(HTMLDocumentImpl.getActiveElementImpl(this.getPeer()));
    }

    static native long getActiveElementImpl(long var0);

    public String getBgColor() {
        return HTMLDocumentImpl.getBgColorImpl(this.getPeer());
    }

    static native String getBgColorImpl(long var0);

    public void setBgColor(String string) {
        HTMLDocumentImpl.setBgColorImpl(this.getPeer(), string);
    }

    static native void setBgColorImpl(long var0, String var2);

    public String getFgColor() {
        return HTMLDocumentImpl.getFgColorImpl(this.getPeer());
    }

    static native String getFgColorImpl(long var0);

    public void setFgColor(String string) {
        HTMLDocumentImpl.setFgColorImpl(this.getPeer(), string);
    }

    static native void setFgColorImpl(long var0, String var2);

    public String getAlinkColor() {
        return HTMLDocumentImpl.getAlinkColorImpl(this.getPeer());
    }

    static native String getAlinkColorImpl(long var0);

    public void setAlinkColor(String string) {
        HTMLDocumentImpl.setAlinkColorImpl(this.getPeer(), string);
    }

    static native void setAlinkColorImpl(long var0, String var2);

    public String getLinkColor() {
        return HTMLDocumentImpl.getLinkColorImpl(this.getPeer());
    }

    static native String getLinkColorImpl(long var0);

    public void setLinkColor(String string) {
        HTMLDocumentImpl.setLinkColorImpl(this.getPeer(), string);
    }

    static native void setLinkColorImpl(long var0, String var2);

    public String getVlinkColor() {
        return HTMLDocumentImpl.getVlinkColorImpl(this.getPeer());
    }

    static native String getVlinkColorImpl(long var0);

    public void setVlinkColor(String string) {
        HTMLDocumentImpl.setVlinkColorImpl(this.getPeer(), string);
    }

    static native void setVlinkColorImpl(long var0, String var2);

    @Override
    public void open() {
        HTMLDocumentImpl.openImpl(this.getPeer());
    }

    static native void openImpl(long var0);

    @Override
    public void close() {
        HTMLDocumentImpl.closeImpl(this.getPeer());
    }

    static native void closeImpl(long var0);

    @Override
    public void write(String string) {
        HTMLDocumentImpl.writeImpl(this.getPeer(), string);
    }

    static native void writeImpl(long var0, String var2);

    @Override
    public void writeln(String string) {
        HTMLDocumentImpl.writelnImpl(this.getPeer(), string);
    }

    static native void writelnImpl(long var0, String var2);

    public void clear() {
        HTMLDocumentImpl.clearImpl(this.getPeer());
    }

    static native void clearImpl(long var0);

    public void captureEvents() {
        HTMLDocumentImpl.captureEventsImpl(this.getPeer());
    }

    static native void captureEventsImpl(long var0);

    public void releaseEvents() {
        HTMLDocumentImpl.releaseEventsImpl(this.getPeer());
    }

    static native void releaseEventsImpl(long var0);

    public boolean hasFocus() {
        return HTMLDocumentImpl.hasFocusImpl(this.getPeer());
    }

    static native boolean hasFocusImpl(long var0);
}

