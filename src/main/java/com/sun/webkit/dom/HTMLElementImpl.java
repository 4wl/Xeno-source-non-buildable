/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.ElementImpl;
import com.sun.webkit.dom.HTMLCollectionImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;

public class HTMLElementImpl
extends ElementImpl
implements HTMLElement {
    HTMLElementImpl(long l) {
        super(l);
    }

    static HTMLElement getImpl(long l) {
        return (HTMLElement)HTMLElementImpl.create(l);
    }

    @Override
    public String getTitle() {
        return HTMLElementImpl.getTitleImpl(this.getPeer());
    }

    static native String getTitleImpl(long var0);

    @Override
    public void setTitle(String string) {
        HTMLElementImpl.setTitleImpl(this.getPeer(), string);
    }

    static native void setTitleImpl(long var0, String var2);

    @Override
    public String getLang() {
        return HTMLElementImpl.getLangImpl(this.getPeer());
    }

    static native String getLangImpl(long var0);

    @Override
    public void setLang(String string) {
        HTMLElementImpl.setLangImpl(this.getPeer(), string);
    }

    static native void setLangImpl(long var0, String var2);

    public boolean getTranslate() {
        return HTMLElementImpl.getTranslateImpl(this.getPeer());
    }

    static native boolean getTranslateImpl(long var0);

    public void setTranslate(boolean bl) {
        HTMLElementImpl.setTranslateImpl(this.getPeer(), bl);
    }

    static native void setTranslateImpl(long var0, boolean var2);

    @Override
    public String getDir() {
        return HTMLElementImpl.getDirImpl(this.getPeer());
    }

    static native String getDirImpl(long var0);

    @Override
    public void setDir(String string) {
        HTMLElementImpl.setDirImpl(this.getPeer(), string);
    }

    static native void setDirImpl(long var0, String var2);

    public int getTabIndex() {
        return HTMLElementImpl.getTabIndexImpl(this.getPeer());
    }

    static native int getTabIndexImpl(long var0);

    public void setTabIndex(int n) {
        HTMLElementImpl.setTabIndexImpl(this.getPeer(), n);
    }

    static native void setTabIndexImpl(long var0, int var2);

    public boolean getDraggable() {
        return HTMLElementImpl.getDraggableImpl(this.getPeer());
    }

    static native boolean getDraggableImpl(long var0);

    public void setDraggable(boolean bl) {
        HTMLElementImpl.setDraggableImpl(this.getPeer(), bl);
    }

    static native void setDraggableImpl(long var0, boolean var2);

    public String getWebkitdropzone() {
        return HTMLElementImpl.getWebkitdropzoneImpl(this.getPeer());
    }

    static native String getWebkitdropzoneImpl(long var0);

    public void setWebkitdropzone(String string) {
        HTMLElementImpl.setWebkitdropzoneImpl(this.getPeer(), string);
    }

    static native void setWebkitdropzoneImpl(long var0, String var2);

    public boolean getHidden() {
        return HTMLElementImpl.getHiddenImpl(this.getPeer());
    }

    static native boolean getHiddenImpl(long var0);

    public void setHidden(boolean bl) {
        HTMLElementImpl.setHiddenImpl(this.getPeer(), bl);
    }

    static native void setHiddenImpl(long var0, boolean var2);

    public String getAccessKey() {
        return HTMLElementImpl.getAccessKeyImpl(this.getPeer());
    }

    static native String getAccessKeyImpl(long var0);

    public void setAccessKey(String string) {
        HTMLElementImpl.setAccessKeyImpl(this.getPeer(), string);
    }

    static native void setAccessKeyImpl(long var0, String var2);

    public String getInnerHTML() {
        return HTMLElementImpl.getInnerHTMLImpl(this.getPeer());
    }

    static native String getInnerHTMLImpl(long var0);

    public void setInnerHTML(String string) throws DOMException {
        HTMLElementImpl.setInnerHTMLImpl(this.getPeer(), string);
    }

    static native void setInnerHTMLImpl(long var0, String var2);

    public String getInnerText() {
        return HTMLElementImpl.getInnerTextImpl(this.getPeer());
    }

    static native String getInnerTextImpl(long var0);

    public void setInnerText(String string) throws DOMException {
        HTMLElementImpl.setInnerTextImpl(this.getPeer(), string);
    }

    static native void setInnerTextImpl(long var0, String var2);

    public String getOuterHTML() {
        return HTMLElementImpl.getOuterHTMLImpl(this.getPeer());
    }

    static native String getOuterHTMLImpl(long var0);

    public void setOuterHTML(String string) throws DOMException {
        HTMLElementImpl.setOuterHTMLImpl(this.getPeer(), string);
    }

    static native void setOuterHTMLImpl(long var0, String var2);

    public String getOuterText() {
        return HTMLElementImpl.getOuterTextImpl(this.getPeer());
    }

    static native String getOuterTextImpl(long var0);

    public void setOuterText(String string) throws DOMException {
        HTMLElementImpl.setOuterTextImpl(this.getPeer(), string);
    }

    static native void setOuterTextImpl(long var0, String var2);

    public HTMLCollection getChildren() {
        return HTMLCollectionImpl.getImpl(HTMLElementImpl.getChildrenImpl(this.getPeer()));
    }

    static native long getChildrenImpl(long var0);

    public String getContentEditable() {
        return HTMLElementImpl.getContentEditableImpl(this.getPeer());
    }

    static native String getContentEditableImpl(long var0);

    public void setContentEditable(String string) throws DOMException {
        HTMLElementImpl.setContentEditableImpl(this.getPeer(), string);
    }

    static native void setContentEditableImpl(long var0, String var2);

    public boolean getIsContentEditable() {
        return HTMLElementImpl.getIsContentEditableImpl(this.getPeer());
    }

    static native boolean getIsContentEditableImpl(long var0);

    public boolean getSpellcheck() {
        return HTMLElementImpl.getSpellcheckImpl(this.getPeer());
    }

    static native boolean getSpellcheckImpl(long var0);

    public void setSpellcheck(boolean bl) {
        HTMLElementImpl.setSpellcheckImpl(this.getPeer(), bl);
    }

    static native void setSpellcheckImpl(long var0, boolean var2);

    public Element insertAdjacentElement(String string, Element element) throws DOMException {
        return ElementImpl.getImpl(HTMLElementImpl.insertAdjacentElementImpl(this.getPeer(), string, ElementImpl.getPeer(element)));
    }

    static native long insertAdjacentElementImpl(long var0, String var2, long var3);

    public void insertAdjacentHTML(String string, String string2) throws DOMException {
        HTMLElementImpl.insertAdjacentHTMLImpl(this.getPeer(), string, string2);
    }

    static native void insertAdjacentHTMLImpl(long var0, String var2, String var3);

    public void insertAdjacentText(String string, String string2) throws DOMException {
        HTMLElementImpl.insertAdjacentTextImpl(this.getPeer(), string, string2);
    }

    static native void insertAdjacentTextImpl(long var0, String var2, String var3);

    public void click() {
        HTMLElementImpl.clickImpl(this.getPeer());
    }

    static native void clickImpl(long var0);
}

