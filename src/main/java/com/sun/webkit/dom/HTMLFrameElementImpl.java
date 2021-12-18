/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.DOMWindowImpl;
import com.sun.webkit.dom.DocumentImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFrameElement;
import org.w3c.dom.views.AbstractView;

public class HTMLFrameElementImpl
extends HTMLElementImpl
implements HTMLFrameElement {
    HTMLFrameElementImpl(long l) {
        super(l);
    }

    static HTMLFrameElement getImpl(long l) {
        return (HTMLFrameElement)HTMLFrameElementImpl.create(l);
    }

    @Override
    public String getFrameBorder() {
        return HTMLFrameElementImpl.getFrameBorderImpl(this.getPeer());
    }

    static native String getFrameBorderImpl(long var0);

    @Override
    public void setFrameBorder(String string) {
        HTMLFrameElementImpl.setFrameBorderImpl(this.getPeer(), string);
    }

    static native void setFrameBorderImpl(long var0, String var2);

    @Override
    public String getLongDesc() {
        return HTMLFrameElementImpl.getLongDescImpl(this.getPeer());
    }

    static native String getLongDescImpl(long var0);

    @Override
    public void setLongDesc(String string) {
        HTMLFrameElementImpl.setLongDescImpl(this.getPeer(), string);
    }

    static native void setLongDescImpl(long var0, String var2);

    @Override
    public String getMarginHeight() {
        return HTMLFrameElementImpl.getMarginHeightImpl(this.getPeer());
    }

    static native String getMarginHeightImpl(long var0);

    @Override
    public void setMarginHeight(String string) {
        HTMLFrameElementImpl.setMarginHeightImpl(this.getPeer(), string);
    }

    static native void setMarginHeightImpl(long var0, String var2);

    @Override
    public String getMarginWidth() {
        return HTMLFrameElementImpl.getMarginWidthImpl(this.getPeer());
    }

    static native String getMarginWidthImpl(long var0);

    @Override
    public void setMarginWidth(String string) {
        HTMLFrameElementImpl.setMarginWidthImpl(this.getPeer(), string);
    }

    static native void setMarginWidthImpl(long var0, String var2);

    @Override
    public String getName() {
        return HTMLFrameElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLFrameElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    @Override
    public boolean getNoResize() {
        return HTMLFrameElementImpl.getNoResizeImpl(this.getPeer());
    }

    static native boolean getNoResizeImpl(long var0);

    @Override
    public void setNoResize(boolean bl) {
        HTMLFrameElementImpl.setNoResizeImpl(this.getPeer(), bl);
    }

    static native void setNoResizeImpl(long var0, boolean var2);

    @Override
    public String getScrolling() {
        return HTMLFrameElementImpl.getScrollingImpl(this.getPeer());
    }

    static native String getScrollingImpl(long var0);

    @Override
    public void setScrolling(String string) {
        HTMLFrameElementImpl.setScrollingImpl(this.getPeer(), string);
    }

    static native void setScrollingImpl(long var0, String var2);

    @Override
    public String getSrc() {
        return HTMLFrameElementImpl.getSrcImpl(this.getPeer());
    }

    static native String getSrcImpl(long var0);

    @Override
    public void setSrc(String string) {
        HTMLFrameElementImpl.setSrcImpl(this.getPeer(), string);
    }

    static native void setSrcImpl(long var0, String var2);

    @Override
    public Document getContentDocument() {
        return DocumentImpl.getImpl(HTMLFrameElementImpl.getContentDocumentImpl(this.getPeer()));
    }

    static native long getContentDocumentImpl(long var0);

    public AbstractView getContentWindow() {
        return DOMWindowImpl.getImpl(HTMLFrameElementImpl.getContentWindowImpl(this.getPeer()));
    }

    static native long getContentWindowImpl(long var0);

    public String getLocation() {
        return HTMLFrameElementImpl.getLocationImpl(this.getPeer());
    }

    static native String getLocationImpl(long var0);

    public void setLocation(String string) {
        HTMLFrameElementImpl.setLocationImpl(this.getPeer(), string);
    }

    static native void setLocationImpl(long var0, String var2);

    public int getWidth() {
        return HTMLFrameElementImpl.getWidthImpl(this.getPeer());
    }

    static native int getWidthImpl(long var0);

    public int getHeight() {
        return HTMLFrameElementImpl.getHeightImpl(this.getPeer());
    }

    static native int getHeightImpl(long var0);
}

