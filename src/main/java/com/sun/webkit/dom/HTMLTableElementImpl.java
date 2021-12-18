/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLCollectionImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLTableCaptionElementImpl;
import com.sun.webkit.dom.HTMLTableSectionElementImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCaptionElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class HTMLTableElementImpl
extends HTMLElementImpl
implements HTMLTableElement {
    HTMLTableElementImpl(long l) {
        super(l);
    }

    static HTMLTableElement getImpl(long l) {
        return (HTMLTableElement)HTMLTableElementImpl.create(l);
    }

    @Override
    public HTMLTableCaptionElement getCaption() {
        return HTMLTableCaptionElementImpl.getImpl(HTMLTableElementImpl.getCaptionImpl(this.getPeer()));
    }

    static native long getCaptionImpl(long var0);

    @Override
    public void setCaption(HTMLTableCaptionElement hTMLTableCaptionElement) throws DOMException {
        HTMLTableElementImpl.setCaptionImpl(this.getPeer(), HTMLTableCaptionElementImpl.getPeer(hTMLTableCaptionElement));
    }

    static native void setCaptionImpl(long var0, long var2);

    @Override
    public HTMLTableSectionElement getTHead() {
        return HTMLTableSectionElementImpl.getImpl(HTMLTableElementImpl.getTHeadImpl(this.getPeer()));
    }

    static native long getTHeadImpl(long var0);

    @Override
    public void setTHead(HTMLTableSectionElement hTMLTableSectionElement) throws DOMException {
        HTMLTableElementImpl.setTHeadImpl(this.getPeer(), HTMLTableSectionElementImpl.getPeer(hTMLTableSectionElement));
    }

    static native void setTHeadImpl(long var0, long var2);

    @Override
    public HTMLTableSectionElement getTFoot() {
        return HTMLTableSectionElementImpl.getImpl(HTMLTableElementImpl.getTFootImpl(this.getPeer()));
    }

    static native long getTFootImpl(long var0);

    @Override
    public void setTFoot(HTMLTableSectionElement hTMLTableSectionElement) throws DOMException {
        HTMLTableElementImpl.setTFootImpl(this.getPeer(), HTMLTableSectionElementImpl.getPeer(hTMLTableSectionElement));
    }

    static native void setTFootImpl(long var0, long var2);

    @Override
    public HTMLCollection getRows() {
        return HTMLCollectionImpl.getImpl(HTMLTableElementImpl.getRowsImpl(this.getPeer()));
    }

    static native long getRowsImpl(long var0);

    @Override
    public HTMLCollection getTBodies() {
        return HTMLCollectionImpl.getImpl(HTMLTableElementImpl.getTBodiesImpl(this.getPeer()));
    }

    static native long getTBodiesImpl(long var0);

    @Override
    public String getAlign() {
        return HTMLTableElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLTableElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public String getBgColor() {
        return HTMLTableElementImpl.getBgColorImpl(this.getPeer());
    }

    static native String getBgColorImpl(long var0);

    @Override
    public void setBgColor(String string) {
        HTMLTableElementImpl.setBgColorImpl(this.getPeer(), string);
    }

    static native void setBgColorImpl(long var0, String var2);

    @Override
    public String getBorder() {
        return HTMLTableElementImpl.getBorderImpl(this.getPeer());
    }

    static native String getBorderImpl(long var0);

    @Override
    public void setBorder(String string) {
        HTMLTableElementImpl.setBorderImpl(this.getPeer(), string);
    }

    static native void setBorderImpl(long var0, String var2);

    @Override
    public String getCellPadding() {
        return HTMLTableElementImpl.getCellPaddingImpl(this.getPeer());
    }

    static native String getCellPaddingImpl(long var0);

    @Override
    public void setCellPadding(String string) {
        HTMLTableElementImpl.setCellPaddingImpl(this.getPeer(), string);
    }

    static native void setCellPaddingImpl(long var0, String var2);

    @Override
    public String getCellSpacing() {
        return HTMLTableElementImpl.getCellSpacingImpl(this.getPeer());
    }

    static native String getCellSpacingImpl(long var0);

    @Override
    public void setCellSpacing(String string) {
        HTMLTableElementImpl.setCellSpacingImpl(this.getPeer(), string);
    }

    static native void setCellSpacingImpl(long var0, String var2);

    @Override
    public String getFrame() {
        return HTMLTableElementImpl.getFrameImpl(this.getPeer());
    }

    static native String getFrameImpl(long var0);

    @Override
    public void setFrame(String string) {
        HTMLTableElementImpl.setFrameImpl(this.getPeer(), string);
    }

    static native void setFrameImpl(long var0, String var2);

    @Override
    public String getRules() {
        return HTMLTableElementImpl.getRulesImpl(this.getPeer());
    }

    static native String getRulesImpl(long var0);

    @Override
    public void setRules(String string) {
        HTMLTableElementImpl.setRulesImpl(this.getPeer(), string);
    }

    static native void setRulesImpl(long var0, String var2);

    @Override
    public String getSummary() {
        return HTMLTableElementImpl.getSummaryImpl(this.getPeer());
    }

    static native String getSummaryImpl(long var0);

    @Override
    public void setSummary(String string) {
        HTMLTableElementImpl.setSummaryImpl(this.getPeer(), string);
    }

    static native void setSummaryImpl(long var0, String var2);

    @Override
    public String getWidth() {
        return HTMLTableElementImpl.getWidthImpl(this.getPeer());
    }

    static native String getWidthImpl(long var0);

    @Override
    public void setWidth(String string) {
        HTMLTableElementImpl.setWidthImpl(this.getPeer(), string);
    }

    static native void setWidthImpl(long var0, String var2);

    @Override
    public HTMLElement createTHead() {
        return HTMLElementImpl.getImpl(HTMLTableElementImpl.createTHeadImpl(this.getPeer()));
    }

    static native long createTHeadImpl(long var0);

    @Override
    public void deleteTHead() {
        HTMLTableElementImpl.deleteTHeadImpl(this.getPeer());
    }

    static native void deleteTHeadImpl(long var0);

    @Override
    public HTMLElement createTFoot() {
        return HTMLElementImpl.getImpl(HTMLTableElementImpl.createTFootImpl(this.getPeer()));
    }

    static native long createTFootImpl(long var0);

    @Override
    public void deleteTFoot() {
        HTMLTableElementImpl.deleteTFootImpl(this.getPeer());
    }

    static native void deleteTFootImpl(long var0);

    public HTMLElement createTBody() {
        return HTMLElementImpl.getImpl(HTMLTableElementImpl.createTBodyImpl(this.getPeer()));
    }

    static native long createTBodyImpl(long var0);

    @Override
    public HTMLElement createCaption() {
        return HTMLElementImpl.getImpl(HTMLTableElementImpl.createCaptionImpl(this.getPeer()));
    }

    static native long createCaptionImpl(long var0);

    @Override
    public void deleteCaption() {
        HTMLTableElementImpl.deleteCaptionImpl(this.getPeer());
    }

    static native void deleteCaptionImpl(long var0);

    @Override
    public HTMLElement insertRow(int n) throws DOMException {
        return HTMLElementImpl.getImpl(HTMLTableElementImpl.insertRowImpl(this.getPeer(), n));
    }

    static native long insertRowImpl(long var0, int var2);

    @Override
    public void deleteRow(int n) throws DOMException {
        HTMLTableElementImpl.deleteRowImpl(this.getPeer(), n);
    }

    static native void deleteRowImpl(long var0, int var2);
}

