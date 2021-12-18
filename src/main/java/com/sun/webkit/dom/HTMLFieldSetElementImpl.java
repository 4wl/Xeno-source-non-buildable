/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLCollectionImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFormElementImpl;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFieldSetElement;
import org.w3c.dom.html.HTMLFormElement;

public class HTMLFieldSetElementImpl
extends HTMLElementImpl
implements HTMLFieldSetElement {
    HTMLFieldSetElementImpl(long l) {
        super(l);
    }

    static HTMLFieldSetElement getImpl(long l) {
        return (HTMLFieldSetElement)HTMLFieldSetElementImpl.create(l);
    }

    public boolean getDisabled() {
        return HTMLFieldSetElementImpl.getDisabledImpl(this.getPeer());
    }

    static native boolean getDisabledImpl(long var0);

    public void setDisabled(boolean bl) {
        HTMLFieldSetElementImpl.setDisabledImpl(this.getPeer(), bl);
    }

    static native void setDisabledImpl(long var0, boolean var2);

    @Override
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(HTMLFieldSetElementImpl.getFormImpl(this.getPeer()));
    }

    static native long getFormImpl(long var0);

    public String getName() {
        return HTMLFieldSetElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    public void setName(String string) {
        HTMLFieldSetElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    public String getType() {
        return HTMLFieldSetElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    public HTMLCollection getElements() {
        return HTMLCollectionImpl.getImpl(HTMLFieldSetElementImpl.getElementsImpl(this.getPeer()));
    }

    static native long getElementsImpl(long var0);

    public boolean getWillValidate() {
        return HTMLFieldSetElementImpl.getWillValidateImpl(this.getPeer());
    }

    static native boolean getWillValidateImpl(long var0);

    public String getValidationMessage() {
        return HTMLFieldSetElementImpl.getValidationMessageImpl(this.getPeer());
    }

    static native String getValidationMessageImpl(long var0);

    public boolean checkValidity() {
        return HTMLFieldSetElementImpl.checkValidityImpl(this.getPeer());
    }

    static native boolean checkValidityImpl(long var0);

    public void setCustomValidity(String string) {
        HTMLFieldSetElementImpl.setCustomValidityImpl(this.getPeer(), string);
    }

    static native void setCustomValidityImpl(long var0, String var2);
}

