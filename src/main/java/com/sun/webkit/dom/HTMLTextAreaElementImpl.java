/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFormElementImpl;
import com.sun.webkit.dom.NodeListImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLTextAreaElement;

public class HTMLTextAreaElementImpl
extends HTMLElementImpl
implements HTMLTextAreaElement {
    HTMLTextAreaElementImpl(long l) {
        super(l);
    }

    static HTMLTextAreaElement getImpl(long l) {
        return (HTMLTextAreaElement)HTMLTextAreaElementImpl.create(l);
    }

    public boolean getAutofocus() {
        return HTMLTextAreaElementImpl.getAutofocusImpl(this.getPeer());
    }

    static native boolean getAutofocusImpl(long var0);

    public void setAutofocus(boolean bl) {
        HTMLTextAreaElementImpl.setAutofocusImpl(this.getPeer(), bl);
    }

    static native void setAutofocusImpl(long var0, boolean var2);

    @Override
    public int getCols() {
        return HTMLTextAreaElementImpl.getColsImpl(this.getPeer());
    }

    static native int getColsImpl(long var0);

    @Override
    public void setCols(int n) {
        HTMLTextAreaElementImpl.setColsImpl(this.getPeer(), n);
    }

    static native void setColsImpl(long var0, int var2);

    public String getDirName() {
        return HTMLTextAreaElementImpl.getDirNameImpl(this.getPeer());
    }

    static native String getDirNameImpl(long var0);

    public void setDirName(String string) {
        HTMLTextAreaElementImpl.setDirNameImpl(this.getPeer(), string);
    }

    static native void setDirNameImpl(long var0, String var2);

    @Override
    public boolean getDisabled() {
        return HTMLTextAreaElementImpl.getDisabledImpl(this.getPeer());
    }

    static native boolean getDisabledImpl(long var0);

    @Override
    public void setDisabled(boolean bl) {
        HTMLTextAreaElementImpl.setDisabledImpl(this.getPeer(), bl);
    }

    static native void setDisabledImpl(long var0, boolean var2);

    @Override
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(HTMLTextAreaElementImpl.getFormImpl(this.getPeer()));
    }

    static native long getFormImpl(long var0);

    public int getMaxLength() {
        return HTMLTextAreaElementImpl.getMaxLengthImpl(this.getPeer());
    }

    static native int getMaxLengthImpl(long var0);

    public void setMaxLength(int n) throws DOMException {
        HTMLTextAreaElementImpl.setMaxLengthImpl(this.getPeer(), n);
    }

    static native void setMaxLengthImpl(long var0, int var2);

    @Override
    public String getName() {
        return HTMLTextAreaElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLTextAreaElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    public String getPlaceholder() {
        return HTMLTextAreaElementImpl.getPlaceholderImpl(this.getPeer());
    }

    static native String getPlaceholderImpl(long var0);

    public void setPlaceholder(String string) {
        HTMLTextAreaElementImpl.setPlaceholderImpl(this.getPeer(), string);
    }

    static native void setPlaceholderImpl(long var0, String var2);

    @Override
    public boolean getReadOnly() {
        return HTMLTextAreaElementImpl.getReadOnlyImpl(this.getPeer());
    }

    static native boolean getReadOnlyImpl(long var0);

    @Override
    public void setReadOnly(boolean bl) {
        HTMLTextAreaElementImpl.setReadOnlyImpl(this.getPeer(), bl);
    }

    static native void setReadOnlyImpl(long var0, boolean var2);

    public boolean getRequired() {
        return HTMLTextAreaElementImpl.getRequiredImpl(this.getPeer());
    }

    static native boolean getRequiredImpl(long var0);

    public void setRequired(boolean bl) {
        HTMLTextAreaElementImpl.setRequiredImpl(this.getPeer(), bl);
    }

    static native void setRequiredImpl(long var0, boolean var2);

    @Override
    public int getRows() {
        return HTMLTextAreaElementImpl.getRowsImpl(this.getPeer());
    }

    static native int getRowsImpl(long var0);

    @Override
    public void setRows(int n) {
        HTMLTextAreaElementImpl.setRowsImpl(this.getPeer(), n);
    }

    static native void setRowsImpl(long var0, int var2);

    public String getWrap() {
        return HTMLTextAreaElementImpl.getWrapImpl(this.getPeer());
    }

    static native String getWrapImpl(long var0);

    public void setWrap(String string) {
        HTMLTextAreaElementImpl.setWrapImpl(this.getPeer(), string);
    }

    static native void setWrapImpl(long var0, String var2);

    @Override
    public String getType() {
        return HTMLTextAreaElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public String getDefaultValue() {
        return HTMLTextAreaElementImpl.getDefaultValueImpl(this.getPeer());
    }

    static native String getDefaultValueImpl(long var0);

    @Override
    public void setDefaultValue(String string) {
        HTMLTextAreaElementImpl.setDefaultValueImpl(this.getPeer(), string);
    }

    static native void setDefaultValueImpl(long var0, String var2);

    @Override
    public String getValue() {
        return HTMLTextAreaElementImpl.getValueImpl(this.getPeer());
    }

    static native String getValueImpl(long var0);

    @Override
    public void setValue(String string) {
        HTMLTextAreaElementImpl.setValueImpl(this.getPeer(), string);
    }

    static native void setValueImpl(long var0, String var2);

    public int getTextLength() {
        return HTMLTextAreaElementImpl.getTextLengthImpl(this.getPeer());
    }

    static native int getTextLengthImpl(long var0);

    public boolean getWillValidate() {
        return HTMLTextAreaElementImpl.getWillValidateImpl(this.getPeer());
    }

    static native boolean getWillValidateImpl(long var0);

    public String getValidationMessage() {
        return HTMLTextAreaElementImpl.getValidationMessageImpl(this.getPeer());
    }

    static native String getValidationMessageImpl(long var0);

    public NodeList getLabels() {
        return NodeListImpl.getImpl(HTMLTextAreaElementImpl.getLabelsImpl(this.getPeer()));
    }

    static native long getLabelsImpl(long var0);

    public int getSelectionStart() {
        return HTMLTextAreaElementImpl.getSelectionStartImpl(this.getPeer());
    }

    static native int getSelectionStartImpl(long var0);

    public void setSelectionStart(int n) {
        HTMLTextAreaElementImpl.setSelectionStartImpl(this.getPeer(), n);
    }

    static native void setSelectionStartImpl(long var0, int var2);

    public int getSelectionEnd() {
        return HTMLTextAreaElementImpl.getSelectionEndImpl(this.getPeer());
    }

    static native int getSelectionEndImpl(long var0);

    public void setSelectionEnd(int n) {
        HTMLTextAreaElementImpl.setSelectionEndImpl(this.getPeer(), n);
    }

    static native void setSelectionEndImpl(long var0, int var2);

    public String getSelectionDirection() {
        return HTMLTextAreaElementImpl.getSelectionDirectionImpl(this.getPeer());
    }

    static native String getSelectionDirectionImpl(long var0);

    public void setSelectionDirection(String string) {
        HTMLTextAreaElementImpl.setSelectionDirectionImpl(this.getPeer(), string);
    }

    static native void setSelectionDirectionImpl(long var0, String var2);

    public boolean checkValidity() {
        return HTMLTextAreaElementImpl.checkValidityImpl(this.getPeer());
    }

    static native boolean checkValidityImpl(long var0);

    public void setCustomValidity(String string) {
        HTMLTextAreaElementImpl.setCustomValidityImpl(this.getPeer(), string);
    }

    static native void setCustomValidityImpl(long var0, String var2);

    @Override
    public void select() {
        HTMLTextAreaElementImpl.selectImpl(this.getPeer());
    }

    static native void selectImpl(long var0);

    public void setRangeText(String string) throws DOMException {
        HTMLTextAreaElementImpl.setRangeTextImpl(this.getPeer(), string);
    }

    static native void setRangeTextImpl(long var0, String var2);

    public void setRangeTextEx(String string, int n, int n2, String string2) throws DOMException {
        HTMLTextAreaElementImpl.setRangeTextExImpl(this.getPeer(), string, n, n2, string2);
    }

    static native void setRangeTextExImpl(long var0, String var2, int var3, int var4, String var5);

    public void setSelectionRange(int n, int n2, String string) {
        HTMLTextAreaElementImpl.setSelectionRangeImpl(this.getPeer(), n, n2, string);
    }

    static native void setSelectionRangeImpl(long var0, int var2, int var3, String var4);
}

