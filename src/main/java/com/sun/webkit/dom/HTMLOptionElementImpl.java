/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFormElementImpl;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLOptionElement;

public class HTMLOptionElementImpl
extends HTMLElementImpl
implements HTMLOptionElement {
    HTMLOptionElementImpl(long l) {
        super(l);
    }

    static HTMLOptionElement getImpl(long l) {
        return (HTMLOptionElement)HTMLOptionElementImpl.create(l);
    }

    @Override
    public boolean getDisabled() {
        return HTMLOptionElementImpl.getDisabledImpl(this.getPeer());
    }

    static native boolean getDisabledImpl(long var0);

    @Override
    public void setDisabled(boolean bl) {
        HTMLOptionElementImpl.setDisabledImpl(this.getPeer(), bl);
    }

    static native void setDisabledImpl(long var0, boolean var2);

    @Override
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(HTMLOptionElementImpl.getFormImpl(this.getPeer()));
    }

    static native long getFormImpl(long var0);

    @Override
    public String getLabel() {
        return HTMLOptionElementImpl.getLabelImpl(this.getPeer());
    }

    static native String getLabelImpl(long var0);

    @Override
    public void setLabel(String string) {
        HTMLOptionElementImpl.setLabelImpl(this.getPeer(), string);
    }

    static native void setLabelImpl(long var0, String var2);

    @Override
    public boolean getDefaultSelected() {
        return HTMLOptionElementImpl.getDefaultSelectedImpl(this.getPeer());
    }

    static native boolean getDefaultSelectedImpl(long var0);

    @Override
    public void setDefaultSelected(boolean bl) {
        HTMLOptionElementImpl.setDefaultSelectedImpl(this.getPeer(), bl);
    }

    static native void setDefaultSelectedImpl(long var0, boolean var2);

    @Override
    public boolean getSelected() {
        return HTMLOptionElementImpl.getSelectedImpl(this.getPeer());
    }

    static native boolean getSelectedImpl(long var0);

    @Override
    public void setSelected(boolean bl) {
        HTMLOptionElementImpl.setSelectedImpl(this.getPeer(), bl);
    }

    static native void setSelectedImpl(long var0, boolean var2);

    @Override
    public String getValue() {
        return HTMLOptionElementImpl.getValueImpl(this.getPeer());
    }

    static native String getValueImpl(long var0);

    @Override
    public void setValue(String string) {
        HTMLOptionElementImpl.setValueImpl(this.getPeer(), string);
    }

    static native void setValueImpl(long var0, String var2);

    @Override
    public String getText() {
        return HTMLOptionElementImpl.getTextImpl(this.getPeer());
    }

    static native String getTextImpl(long var0);

    @Override
    public int getIndex() {
        return HTMLOptionElementImpl.getIndexImpl(this.getPeer());
    }

    static native int getIndexImpl(long var0);
}

