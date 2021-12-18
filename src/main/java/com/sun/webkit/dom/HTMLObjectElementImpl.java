/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.DocumentImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFormElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLObjectElement;

public class HTMLObjectElementImpl
extends HTMLElementImpl
implements HTMLObjectElement {
    HTMLObjectElementImpl(long l) {
        super(l);
    }

    static HTMLObjectElement getImpl(long l) {
        return (HTMLObjectElement)HTMLObjectElementImpl.create(l);
    }

    @Override
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(HTMLObjectElementImpl.getFormImpl(this.getPeer()));
    }

    static native long getFormImpl(long var0);

    @Override
    public String getCode() {
        return HTMLObjectElementImpl.getCodeImpl(this.getPeer());
    }

    static native String getCodeImpl(long var0);

    @Override
    public void setCode(String string) {
        HTMLObjectElementImpl.setCodeImpl(this.getPeer(), string);
    }

    static native void setCodeImpl(long var0, String var2);

    @Override
    public String getAlign() {
        return HTMLObjectElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLObjectElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public String getArchive() {
        return HTMLObjectElementImpl.getArchiveImpl(this.getPeer());
    }

    static native String getArchiveImpl(long var0);

    @Override
    public void setArchive(String string) {
        HTMLObjectElementImpl.setArchiveImpl(this.getPeer(), string);
    }

    static native void setArchiveImpl(long var0, String var2);

    @Override
    public String getBorder() {
        return HTMLObjectElementImpl.getBorderImpl(this.getPeer());
    }

    static native String getBorderImpl(long var0);

    @Override
    public void setBorder(String string) {
        HTMLObjectElementImpl.setBorderImpl(this.getPeer(), string);
    }

    static native void setBorderImpl(long var0, String var2);

    @Override
    public String getCodeBase() {
        return HTMLObjectElementImpl.getCodeBaseImpl(this.getPeer());
    }

    static native String getCodeBaseImpl(long var0);

    @Override
    public void setCodeBase(String string) {
        HTMLObjectElementImpl.setCodeBaseImpl(this.getPeer(), string);
    }

    static native void setCodeBaseImpl(long var0, String var2);

    @Override
    public String getCodeType() {
        return HTMLObjectElementImpl.getCodeTypeImpl(this.getPeer());
    }

    static native String getCodeTypeImpl(long var0);

    @Override
    public void setCodeType(String string) {
        HTMLObjectElementImpl.setCodeTypeImpl(this.getPeer(), string);
    }

    static native void setCodeTypeImpl(long var0, String var2);

    @Override
    public String getData() {
        return HTMLObjectElementImpl.getDataImpl(this.getPeer());
    }

    static native String getDataImpl(long var0);

    @Override
    public void setData(String string) {
        HTMLObjectElementImpl.setDataImpl(this.getPeer(), string);
    }

    static native void setDataImpl(long var0, String var2);

    @Override
    public boolean getDeclare() {
        return HTMLObjectElementImpl.getDeclareImpl(this.getPeer());
    }

    static native boolean getDeclareImpl(long var0);

    @Override
    public void setDeclare(boolean bl) {
        HTMLObjectElementImpl.setDeclareImpl(this.getPeer(), bl);
    }

    static native void setDeclareImpl(long var0, boolean var2);

    @Override
    public String getHeight() {
        return HTMLObjectElementImpl.getHeightImpl(this.getPeer());
    }

    static native String getHeightImpl(long var0);

    @Override
    public void setHeight(String string) {
        HTMLObjectElementImpl.setHeightImpl(this.getPeer(), string);
    }

    static native void setHeightImpl(long var0, String var2);

    @Override
    public String getHspace() {
        return HTMLObjectElementImpl.getHspaceImpl(this.getPeer()) + "";
    }

    static native int getHspaceImpl(long var0);

    @Override
    public void setHspace(String string) {
        HTMLObjectElementImpl.setHspaceImpl(this.getPeer(), Integer.parseInt(string));
    }

    static native void setHspaceImpl(long var0, int var2);

    @Override
    public String getName() {
        return HTMLObjectElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLObjectElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    @Override
    public String getStandby() {
        return HTMLObjectElementImpl.getStandbyImpl(this.getPeer());
    }

    static native String getStandbyImpl(long var0);

    @Override
    public void setStandby(String string) {
        HTMLObjectElementImpl.setStandbyImpl(this.getPeer(), string);
    }

    static native void setStandbyImpl(long var0, String var2);

    @Override
    public String getType() {
        return HTMLObjectElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public void setType(String string) {
        HTMLObjectElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);

    @Override
    public String getUseMap() {
        return HTMLObjectElementImpl.getUseMapImpl(this.getPeer());
    }

    static native String getUseMapImpl(long var0);

    @Override
    public void setUseMap(String string) {
        HTMLObjectElementImpl.setUseMapImpl(this.getPeer(), string);
    }

    static native void setUseMapImpl(long var0, String var2);

    @Override
    public String getVspace() {
        return HTMLObjectElementImpl.getVspaceImpl(this.getPeer()) + "";
    }

    static native int getVspaceImpl(long var0);

    @Override
    public void setVspace(String string) {
        HTMLObjectElementImpl.setVspaceImpl(this.getPeer(), Integer.parseInt(string));
    }

    static native void setVspaceImpl(long var0, int var2);

    @Override
    public String getWidth() {
        return HTMLObjectElementImpl.getWidthImpl(this.getPeer());
    }

    static native String getWidthImpl(long var0);

    @Override
    public void setWidth(String string) {
        HTMLObjectElementImpl.setWidthImpl(this.getPeer(), string);
    }

    static native void setWidthImpl(long var0, String var2);

    public boolean getWillValidate() {
        return HTMLObjectElementImpl.getWillValidateImpl(this.getPeer());
    }

    static native boolean getWillValidateImpl(long var0);

    public String getValidationMessage() {
        return HTMLObjectElementImpl.getValidationMessageImpl(this.getPeer());
    }

    static native String getValidationMessageImpl(long var0);

    @Override
    public Document getContentDocument() {
        return DocumentImpl.getImpl(HTMLObjectElementImpl.getContentDocumentImpl(this.getPeer()));
    }

    static native long getContentDocumentImpl(long var0);

    public boolean checkValidity() {
        return HTMLObjectElementImpl.checkValidityImpl(this.getPeer());
    }

    static native boolean checkValidityImpl(long var0);

    public void setCustomValidity(String string) {
        HTMLObjectElementImpl.setCustomValidityImpl(this.getPeer(), string);
    }

    static native void setCustomValidityImpl(long var0, String var2);
}

