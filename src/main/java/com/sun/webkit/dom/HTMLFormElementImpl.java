/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLCollectionImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFormElement;

public class HTMLFormElementImpl
extends HTMLElementImpl
implements HTMLFormElement {
    HTMLFormElementImpl(long l) {
        super(l);
    }

    static HTMLFormElement getImpl(long l) {
        return (HTMLFormElement)HTMLFormElementImpl.create(l);
    }

    @Override
    public String getAcceptCharset() {
        return HTMLFormElementImpl.getAcceptCharsetImpl(this.getPeer());
    }

    static native String getAcceptCharsetImpl(long var0);

    @Override
    public void setAcceptCharset(String string) {
        HTMLFormElementImpl.setAcceptCharsetImpl(this.getPeer(), string);
    }

    static native void setAcceptCharsetImpl(long var0, String var2);

    @Override
    public String getAction() {
        return HTMLFormElementImpl.getActionImpl(this.getPeer());
    }

    static native String getActionImpl(long var0);

    @Override
    public void setAction(String string) {
        HTMLFormElementImpl.setActionImpl(this.getPeer(), string);
    }

    static native void setActionImpl(long var0, String var2);

    public String getAutocomplete() {
        return HTMLFormElementImpl.getAutocompleteImpl(this.getPeer());
    }

    static native String getAutocompleteImpl(long var0);

    public void setAutocomplete(String string) {
        HTMLFormElementImpl.setAutocompleteImpl(this.getPeer(), string);
    }

    static native void setAutocompleteImpl(long var0, String var2);

    @Override
    public String getEnctype() {
        return HTMLFormElementImpl.getEnctypeImpl(this.getPeer());
    }

    static native String getEnctypeImpl(long var0);

    @Override
    public void setEnctype(String string) {
        HTMLFormElementImpl.setEnctypeImpl(this.getPeer(), string);
    }

    static native void setEnctypeImpl(long var0, String var2);

    public String getEncoding() {
        return HTMLFormElementImpl.getEncodingImpl(this.getPeer());
    }

    static native String getEncodingImpl(long var0);

    public void setEncoding(String string) {
        HTMLFormElementImpl.setEncodingImpl(this.getPeer(), string);
    }

    static native void setEncodingImpl(long var0, String var2);

    @Override
    public String getMethod() {
        return HTMLFormElementImpl.getMethodImpl(this.getPeer());
    }

    static native String getMethodImpl(long var0);

    @Override
    public void setMethod(String string) {
        HTMLFormElementImpl.setMethodImpl(this.getPeer(), string);
    }

    static native void setMethodImpl(long var0, String var2);

    @Override
    public String getName() {
        return HTMLFormElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLFormElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    public boolean getNoValidate() {
        return HTMLFormElementImpl.getNoValidateImpl(this.getPeer());
    }

    static native boolean getNoValidateImpl(long var0);

    public void setNoValidate(boolean bl) {
        HTMLFormElementImpl.setNoValidateImpl(this.getPeer(), bl);
    }

    static native void setNoValidateImpl(long var0, boolean var2);

    @Override
    public String getTarget() {
        return HTMLFormElementImpl.getTargetImpl(this.getPeer());
    }

    static native String getTargetImpl(long var0);

    @Override
    public void setTarget(String string) {
        HTMLFormElementImpl.setTargetImpl(this.getPeer(), string);
    }

    static native void setTargetImpl(long var0, String var2);

    @Override
    public HTMLCollection getElements() {
        return HTMLCollectionImpl.getImpl(HTMLFormElementImpl.getElementsImpl(this.getPeer()));
    }

    static native long getElementsImpl(long var0);

    @Override
    public int getLength() {
        return HTMLFormElementImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public void submit() {
        HTMLFormElementImpl.submitImpl(this.getPeer());
    }

    static native void submitImpl(long var0);

    @Override
    public void reset() {
        HTMLFormElementImpl.resetImpl(this.getPeer());
    }

    static native void resetImpl(long var0);

    public boolean checkValidity() {
        return HTMLFormElementImpl.checkValidityImpl(this.getPeer());
    }

    static native boolean checkValidityImpl(long var0);
}

