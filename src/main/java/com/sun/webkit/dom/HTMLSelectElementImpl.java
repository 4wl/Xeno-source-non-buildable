/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLCollectionImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFormElementImpl;
import com.sun.webkit.dom.HTMLOptionsCollectionImpl;
import com.sun.webkit.dom.NodeImpl;
import com.sun.webkit.dom.NodeListImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLSelectElement;

public class HTMLSelectElementImpl
extends HTMLElementImpl
implements HTMLSelectElement {
    HTMLSelectElementImpl(long l) {
        super(l);
    }

    static HTMLSelectElement getImpl(long l) {
        return (HTMLSelectElement)HTMLSelectElementImpl.create(l);
    }

    public boolean getAutofocus() {
        return HTMLSelectElementImpl.getAutofocusImpl(this.getPeer());
    }

    static native boolean getAutofocusImpl(long var0);

    public void setAutofocus(boolean bl) {
        HTMLSelectElementImpl.setAutofocusImpl(this.getPeer(), bl);
    }

    static native void setAutofocusImpl(long var0, boolean var2);

    @Override
    public boolean getDisabled() {
        return HTMLSelectElementImpl.getDisabledImpl(this.getPeer());
    }

    static native boolean getDisabledImpl(long var0);

    @Override
    public void setDisabled(boolean bl) {
        HTMLSelectElementImpl.setDisabledImpl(this.getPeer(), bl);
    }

    static native void setDisabledImpl(long var0, boolean var2);

    @Override
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(HTMLSelectElementImpl.getFormImpl(this.getPeer()));
    }

    static native long getFormImpl(long var0);

    @Override
    public boolean getMultiple() {
        return HTMLSelectElementImpl.getMultipleImpl(this.getPeer());
    }

    static native boolean getMultipleImpl(long var0);

    @Override
    public void setMultiple(boolean bl) {
        HTMLSelectElementImpl.setMultipleImpl(this.getPeer(), bl);
    }

    static native void setMultipleImpl(long var0, boolean var2);

    @Override
    public String getName() {
        return HTMLSelectElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLSelectElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    public boolean getRequired() {
        return HTMLSelectElementImpl.getRequiredImpl(this.getPeer());
    }

    static native boolean getRequiredImpl(long var0);

    public void setRequired(boolean bl) {
        HTMLSelectElementImpl.setRequiredImpl(this.getPeer(), bl);
    }

    static native void setRequiredImpl(long var0, boolean var2);

    @Override
    public int getSize() {
        return HTMLSelectElementImpl.getSizeImpl(this.getPeer());
    }

    static native int getSizeImpl(long var0);

    @Override
    public void setSize(int n) {
        HTMLSelectElementImpl.setSizeImpl(this.getPeer(), n);
    }

    static native void setSizeImpl(long var0, int var2);

    @Override
    public String getType() {
        return HTMLSelectElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public HTMLOptionsCollectionImpl getOptions() {
        return HTMLOptionsCollectionImpl.getImpl(HTMLSelectElementImpl.getOptionsImpl(this.getPeer()));
    }

    static native long getOptionsImpl(long var0);

    @Override
    public int getLength() {
        return HTMLSelectElementImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    public void setLength(int n) throws DOMException {
        HTMLSelectElementImpl.setLengthImpl(this.getPeer(), n);
    }

    static native void setLengthImpl(long var0, int var2);

    public HTMLCollection getSelectedOptions() {
        return HTMLCollectionImpl.getImpl(HTMLSelectElementImpl.getSelectedOptionsImpl(this.getPeer()));
    }

    static native long getSelectedOptionsImpl(long var0);

    @Override
    public int getSelectedIndex() {
        return HTMLSelectElementImpl.getSelectedIndexImpl(this.getPeer());
    }

    static native int getSelectedIndexImpl(long var0);

    @Override
    public void setSelectedIndex(int n) {
        HTMLSelectElementImpl.setSelectedIndexImpl(this.getPeer(), n);
    }

    static native void setSelectedIndexImpl(long var0, int var2);

    @Override
    public String getValue() {
        return HTMLSelectElementImpl.getValueImpl(this.getPeer());
    }

    static native String getValueImpl(long var0);

    @Override
    public void setValue(String string) {
        HTMLSelectElementImpl.setValueImpl(this.getPeer(), string);
    }

    static native void setValueImpl(long var0, String var2);

    public boolean getWillValidate() {
        return HTMLSelectElementImpl.getWillValidateImpl(this.getPeer());
    }

    static native boolean getWillValidateImpl(long var0);

    public String getValidationMessage() {
        return HTMLSelectElementImpl.getValidationMessageImpl(this.getPeer());
    }

    static native String getValidationMessageImpl(long var0);

    public NodeList getLabels() {
        return NodeListImpl.getImpl(HTMLSelectElementImpl.getLabelsImpl(this.getPeer()));
    }

    static native long getLabelsImpl(long var0);

    public Node item(int n) {
        return NodeImpl.getImpl(HTMLSelectElementImpl.itemImpl(this.getPeer(), n));
    }

    static native long itemImpl(long var0, int var2);

    public Node namedItem(String string) {
        return NodeImpl.getImpl(HTMLSelectElementImpl.namedItemImpl(this.getPeer(), string));
    }

    static native long namedItemImpl(long var0, String var2);

    @Override
    public void add(HTMLElement hTMLElement, HTMLElement hTMLElement2) throws DOMException {
        HTMLSelectElementImpl.addImpl(this.getPeer(), HTMLElementImpl.getPeer(hTMLElement), HTMLElementImpl.getPeer(hTMLElement2));
    }

    static native void addImpl(long var0, long var2, long var4);

    @Override
    public void remove(int n) {
        HTMLSelectElementImpl.removeImpl(this.getPeer(), n);
    }

    static native void removeImpl(long var0, int var2);

    public boolean checkValidity() {
        return HTMLSelectElementImpl.checkValidityImpl(this.getPeer());
    }

    static native boolean checkValidityImpl(long var0);

    public void setCustomValidity(String string) {
        HTMLSelectElementImpl.setCustomValidityImpl(this.getPeer(), string);
    }

    static native void setCustomValidityImpl(long var0, String var2);
}

