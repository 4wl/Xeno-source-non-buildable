/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.DOMWindowImpl;
import com.sun.webkit.dom.DocumentImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.views.AbstractView;

public class HTMLIFrameElementImpl
extends HTMLElementImpl
implements HTMLIFrameElement {
    HTMLIFrameElementImpl(long l) {
        super(l);
    }

    static HTMLIFrameElement getImpl(long l) {
        return (HTMLIFrameElement)HTMLIFrameElementImpl.create(l);
    }

    @Override
    public String getAlign() {
        return HTMLIFrameElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLIFrameElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public String getFrameBorder() {
        return HTMLIFrameElementImpl.getFrameBorderImpl(this.getPeer());
    }

    static native String getFrameBorderImpl(long var0);

    @Override
    public void setFrameBorder(String string) {
        HTMLIFrameElementImpl.setFrameBorderImpl(this.getPeer(), string);
    }

    static native void setFrameBorderImpl(long var0, String var2);

    @Override
    public String getHeight() {
        return HTMLIFrameElementImpl.getHeightImpl(this.getPeer());
    }

    static native String getHeightImpl(long var0);

    @Override
    public void setHeight(String string) {
        HTMLIFrameElementImpl.setHeightImpl(this.getPeer(), string);
    }

    static native void setHeightImpl(long var0, String var2);

    @Override
    public String getLongDesc() {
        return HTMLIFrameElementImpl.getLongDescImpl(this.getPeer());
    }

    static native String getLongDescImpl(long var0);

    @Override
    public void setLongDesc(String string) {
        HTMLIFrameElementImpl.setLongDescImpl(this.getPeer(), string);
    }

    static native void setLongDescImpl(long var0, String var2);

    @Override
    public String getMarginHeight() {
        return HTMLIFrameElementImpl.getMarginHeightImpl(this.getPeer());
    }

    static native String getMarginHeightImpl(long var0);

    @Override
    public void setMarginHeight(String string) {
        HTMLIFrameElementImpl.setMarginHeightImpl(this.getPeer(), string);
    }

    static native void setMarginHeightImpl(long var0, String var2);

    @Override
    public String getMarginWidth() {
        return HTMLIFrameElementImpl.getMarginWidthImpl(this.getPeer());
    }

    static native String getMarginWidthImpl(long var0);

    @Override
    public void setMarginWidth(String string) {
        HTMLIFrameElementImpl.setMarginWidthImpl(this.getPeer(), string);
    }

    static native void setMarginWidthImpl(long var0, String var2);

    @Override
    public String getName() {
        return HTMLIFrameElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLIFrameElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    public String getSandbox() {
        return HTMLIFrameElementImpl.getSandboxImpl(this.getPeer());
    }

    static native String getSandboxImpl(long var0);

    public void setSandbox(String string) {
        HTMLIFrameElementImpl.setSandboxImpl(this.getPeer(), string);
    }

    static native void setSandboxImpl(long var0, String var2);

    @Override
    public String getScrolling() {
        return HTMLIFrameElementImpl.getScrollingImpl(this.getPeer());
    }

    static native String getScrollingImpl(long var0);

    @Override
    public void setScrolling(String string) {
        HTMLIFrameElementImpl.setScrollingImpl(this.getPeer(), string);
    }

    static native void setScrollingImpl(long var0, String var2);

    @Override
    public String getSrc() {
        return HTMLIFrameElementImpl.getSrcImpl(this.getPeer());
    }

    static native String getSrcImpl(long var0);

    @Override
    public void setSrc(String string) {
        HTMLIFrameElementImpl.setSrcImpl(this.getPeer(), string);
    }

    static native void setSrcImpl(long var0, String var2);

    public String getSrcdoc() {
        return HTMLIFrameElementImpl.getSrcdocImpl(this.getPeer());
    }

    static native String getSrcdocImpl(long var0);

    public void setSrcdoc(String string) {
        HTMLIFrameElementImpl.setSrcdocImpl(this.getPeer(), string);
    }

    static native void setSrcdocImpl(long var0, String var2);

    @Override
    public String getWidth() {
        return HTMLIFrameElementImpl.getWidthImpl(this.getPeer());
    }

    static native String getWidthImpl(long var0);

    @Override
    public void setWidth(String string) {
        HTMLIFrameElementImpl.setWidthImpl(this.getPeer(), string);
    }

    static native void setWidthImpl(long var0, String var2);

    @Override
    public Document getContentDocument() {
        return DocumentImpl.getImpl(HTMLIFrameElementImpl.getContentDocumentImpl(this.getPeer()));
    }

    static native long getContentDocumentImpl(long var0);

    public AbstractView getContentWindow() {
        return DOMWindowImpl.getImpl(HTMLIFrameElementImpl.getContentWindowImpl(this.getPeer()));
    }

    static native long getContentWindowImpl(long var0);
}

