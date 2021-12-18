/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFormElementImpl;
import com.sun.webkit.dom.NodeListImpl;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLFormElement;

public class HTMLButtonElementImpl
extends HTMLElementImpl
implements HTMLButtonElement {
    HTMLButtonElementImpl(long l) {
        super(l);
    }

    static HTMLButtonElement getImpl(long l) {
        return (HTMLButtonElement)HTMLButtonElementImpl.create(l);
    }

    public boolean getAutofocus() {
        return HTMLButtonElementImpl.getAutofocusImpl(this.getPeer());
    }

    static native boolean getAutofocusImpl(long var0);

    public void setAutofocus(boolean bl) {
        HTMLButtonElementImpl.setAutofocusImpl(this.getPeer(), bl);
    }

    static native void setAutofocusImpl(long var0, boolean var2);

    @Override
    public boolean getDisabled() {
        return HTMLButtonElementImpl.getDisabledImpl(this.getPeer());
    }

    static native boolean getDisabledImpl(long var0);

    @Override
    public void setDisabled(boolean bl) {
        HTMLButtonElementImpl.setDisabledImpl(this.getPeer(), bl);
    }

    static native void setDisabledImpl(long var0, boolean var2);

    @Override
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(HTMLButtonElementImpl.getFormImpl(this.getPeer()));
    }

    static native long getFormImpl(long var0);

    public String getFormAction() {
        return HTMLButtonElementImpl.getFormActionImpl(this.getPeer());
    }

    static native String getFormActionImpl(long var0);

    public void setFormAction(String string) {
        HTMLButtonElementImpl.setFormActionImpl(this.getPeer(), string);
    }

    static native void setFormActionImpl(long var0, String var2);

    public String getFormEnctype() {
        return HTMLButtonElementImpl.getFormEnctypeImpl(this.getPeer());
    }

    static native String getFormEnctypeImpl(long var0);

    public void setFormEnctype(String string) {
        HTMLButtonElementImpl.setFormEnctypeImpl(this.getPeer(), string);
    }

    static native void setFormEnctypeImpl(long var0, String var2);

    public String getFormMethod() {
        return HTMLButtonElementImpl.getFormMethodImpl(this.getPeer());
    }

    static native String getFormMethodImpl(long var0);

    public void setFormMethod(String string) {
        HTMLButtonElementImpl.setFormMethodImpl(this.getPeer(), string);
    }

    static native void setFormMethodImpl(long var0, String var2);

    public boolean getFormNoValidate() {
        return HTMLButtonElementImpl.getFormNoValidateImpl(this.getPeer());
    }

    static native boolean getFormNoValidateImpl(long var0);

    public void setFormNoValidate(boolean bl) {
        HTMLButtonElementImpl.setFormNoValidateImpl(this.getPeer(), bl);
    }

    static native void setFormNoValidateImpl(long var0, boolean var2);

    public String getFormTarget() {
        return HTMLButtonElementImpl.getFormTargetImpl(this.getPeer());
    }

    static native String getFormTargetImpl(long var0);

    public void setFormTarget(String string) {
        HTMLButtonElementImpl.setFormTargetImpl(this.getPeer(), string);
    }

    static native void setFormTargetImpl(long var0, String var2);

    @Override
    public String getName() {
        return HTMLButtonElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLButtonElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    @Override
    public String getType() {
        return HTMLButtonElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    public void setType(String string) {
        HTMLButtonElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);

    @Override
    public String getValue() {
        return HTMLButtonElementImpl.getValueImpl(this.getPeer());
    }

    static native String getValueImpl(long var0);

    @Override
    public void setValue(String string) {
        HTMLButtonElementImpl.setValueImpl(this.getPeer(), string);
    }

    static native void setValueImpl(long var0, String var2);

    public boolean getWillValidate() {
        return HTMLButtonElementImpl.getWillValidateImpl(this.getPeer());
    }

    static native boolean getWillValidateImpl(long var0);

    public String getValidationMessage() {
        return HTMLButtonElementImpl.getValidationMessageImpl(this.getPeer());
    }

    static native String getValidationMessageImpl(long var0);

    public NodeList getLabels() {
        return NodeListImpl.getImpl(HTMLButtonElementImpl.getLabelsImpl(this.getPeer()));
    }

    static native long getLabelsImpl(long var0);

    public boolean checkValidity() {
        return HTMLButtonElementImpl.checkValidityImpl(this.getPeer());
    }

    static native boolean checkValidityImpl(long var0);

    public void setCustomValidity(String string) {
        HTMLButtonElementImpl.setCustomValidityImpl(this.getPeer(), string);
    }

    static native void setCustomValidityImpl(long var0, String var2);
}

