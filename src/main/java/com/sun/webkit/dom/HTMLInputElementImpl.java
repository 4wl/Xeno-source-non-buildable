/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFormElementImpl;
import com.sun.webkit.dom.NodeListImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

public class HTMLInputElementImpl
extends HTMLElementImpl
implements HTMLInputElement {
    HTMLInputElementImpl(long l) {
        super(l);
    }

    static HTMLInputElement getImpl(long l) {
        return (HTMLInputElement)HTMLInputElementImpl.create(l);
    }

    @Override
    public String getAccept() {
        return HTMLInputElementImpl.getAcceptImpl(this.getPeer());
    }

    static native String getAcceptImpl(long var0);

    @Override
    public void setAccept(String string) {
        HTMLInputElementImpl.setAcceptImpl(this.getPeer(), string);
    }

    static native void setAcceptImpl(long var0, String var2);

    @Override
    public String getAlt() {
        return HTMLInputElementImpl.getAltImpl(this.getPeer());
    }

    static native String getAltImpl(long var0);

    @Override
    public void setAlt(String string) {
        HTMLInputElementImpl.setAltImpl(this.getPeer(), string);
    }

    static native void setAltImpl(long var0, String var2);

    public String getAutocomplete() {
        return HTMLInputElementImpl.getAutocompleteImpl(this.getPeer());
    }

    static native String getAutocompleteImpl(long var0);

    public void setAutocomplete(String string) {
        HTMLInputElementImpl.setAutocompleteImpl(this.getPeer(), string);
    }

    static native void setAutocompleteImpl(long var0, String var2);

    public boolean getAutofocus() {
        return HTMLInputElementImpl.getAutofocusImpl(this.getPeer());
    }

    static native boolean getAutofocusImpl(long var0);

    public void setAutofocus(boolean bl) {
        HTMLInputElementImpl.setAutofocusImpl(this.getPeer(), bl);
    }

    static native void setAutofocusImpl(long var0, boolean var2);

    @Override
    public boolean getDefaultChecked() {
        return HTMLInputElementImpl.getDefaultCheckedImpl(this.getPeer());
    }

    static native boolean getDefaultCheckedImpl(long var0);

    @Override
    public void setDefaultChecked(boolean bl) {
        HTMLInputElementImpl.setDefaultCheckedImpl(this.getPeer(), bl);
    }

    static native void setDefaultCheckedImpl(long var0, boolean var2);

    @Override
    public boolean getChecked() {
        return HTMLInputElementImpl.getCheckedImpl(this.getPeer());
    }

    static native boolean getCheckedImpl(long var0);

    @Override
    public void setChecked(boolean bl) {
        HTMLInputElementImpl.setCheckedImpl(this.getPeer(), bl);
    }

    static native void setCheckedImpl(long var0, boolean var2);

    public String getDirName() {
        return HTMLInputElementImpl.getDirNameImpl(this.getPeer());
    }

    static native String getDirNameImpl(long var0);

    public void setDirName(String string) {
        HTMLInputElementImpl.setDirNameImpl(this.getPeer(), string);
    }

    static native void setDirNameImpl(long var0, String var2);

    @Override
    public boolean getDisabled() {
        return HTMLInputElementImpl.getDisabledImpl(this.getPeer());
    }

    static native boolean getDisabledImpl(long var0);

    @Override
    public void setDisabled(boolean bl) {
        HTMLInputElementImpl.setDisabledImpl(this.getPeer(), bl);
    }

    static native void setDisabledImpl(long var0, boolean var2);

    @Override
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(HTMLInputElementImpl.getFormImpl(this.getPeer()));
    }

    static native long getFormImpl(long var0);

    public String getFormAction() {
        return HTMLInputElementImpl.getFormActionImpl(this.getPeer());
    }

    static native String getFormActionImpl(long var0);

    public void setFormAction(String string) {
        HTMLInputElementImpl.setFormActionImpl(this.getPeer(), string);
    }

    static native void setFormActionImpl(long var0, String var2);

    public String getFormEnctype() {
        return HTMLInputElementImpl.getFormEnctypeImpl(this.getPeer());
    }

    static native String getFormEnctypeImpl(long var0);

    public void setFormEnctype(String string) {
        HTMLInputElementImpl.setFormEnctypeImpl(this.getPeer(), string);
    }

    static native void setFormEnctypeImpl(long var0, String var2);

    public String getFormMethod() {
        return HTMLInputElementImpl.getFormMethodImpl(this.getPeer());
    }

    static native String getFormMethodImpl(long var0);

    public void setFormMethod(String string) {
        HTMLInputElementImpl.setFormMethodImpl(this.getPeer(), string);
    }

    static native void setFormMethodImpl(long var0, String var2);

    public boolean getFormNoValidate() {
        return HTMLInputElementImpl.getFormNoValidateImpl(this.getPeer());
    }

    static native boolean getFormNoValidateImpl(long var0);

    public void setFormNoValidate(boolean bl) {
        HTMLInputElementImpl.setFormNoValidateImpl(this.getPeer(), bl);
    }

    static native void setFormNoValidateImpl(long var0, boolean var2);

    public String getFormTarget() {
        return HTMLInputElementImpl.getFormTargetImpl(this.getPeer());
    }

    static native String getFormTargetImpl(long var0);

    public void setFormTarget(String string) {
        HTMLInputElementImpl.setFormTargetImpl(this.getPeer(), string);
    }

    static native void setFormTargetImpl(long var0, String var2);

    public int getHeight() {
        return HTMLInputElementImpl.getHeightImpl(this.getPeer());
    }

    static native int getHeightImpl(long var0);

    public void setHeight(int n) {
        HTMLInputElementImpl.setHeightImpl(this.getPeer(), n);
    }

    static native void setHeightImpl(long var0, int var2);

    public boolean getIndeterminate() {
        return HTMLInputElementImpl.getIndeterminateImpl(this.getPeer());
    }

    static native boolean getIndeterminateImpl(long var0);

    public void setIndeterminate(boolean bl) {
        HTMLInputElementImpl.setIndeterminateImpl(this.getPeer(), bl);
    }

    static native void setIndeterminateImpl(long var0, boolean var2);

    public HTMLElement getList() {
        return HTMLElementImpl.getImpl(HTMLInputElementImpl.getListImpl(this.getPeer()));
    }

    static native long getListImpl(long var0);

    public String getMax() {
        return HTMLInputElementImpl.getMaxImpl(this.getPeer());
    }

    static native String getMaxImpl(long var0);

    public void setMax(String string) {
        HTMLInputElementImpl.setMaxImpl(this.getPeer(), string);
    }

    static native void setMaxImpl(long var0, String var2);

    @Override
    public int getMaxLength() {
        return HTMLInputElementImpl.getMaxLengthImpl(this.getPeer());
    }

    static native int getMaxLengthImpl(long var0);

    @Override
    public void setMaxLength(int n) throws DOMException {
        HTMLInputElementImpl.setMaxLengthImpl(this.getPeer(), n);
    }

    static native void setMaxLengthImpl(long var0, int var2);

    public String getMin() {
        return HTMLInputElementImpl.getMinImpl(this.getPeer());
    }

    static native String getMinImpl(long var0);

    public void setMin(String string) {
        HTMLInputElementImpl.setMinImpl(this.getPeer(), string);
    }

    static native void setMinImpl(long var0, String var2);

    public boolean getMultiple() {
        return HTMLInputElementImpl.getMultipleImpl(this.getPeer());
    }

    static native boolean getMultipleImpl(long var0);

    public void setMultiple(boolean bl) {
        HTMLInputElementImpl.setMultipleImpl(this.getPeer(), bl);
    }

    static native void setMultipleImpl(long var0, boolean var2);

    @Override
    public String getName() {
        return HTMLInputElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLInputElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    public String getPattern() {
        return HTMLInputElementImpl.getPatternImpl(this.getPeer());
    }

    static native String getPatternImpl(long var0);

    public void setPattern(String string) {
        HTMLInputElementImpl.setPatternImpl(this.getPeer(), string);
    }

    static native void setPatternImpl(long var0, String var2);

    public String getPlaceholder() {
        return HTMLInputElementImpl.getPlaceholderImpl(this.getPeer());
    }

    static native String getPlaceholderImpl(long var0);

    public void setPlaceholder(String string) {
        HTMLInputElementImpl.setPlaceholderImpl(this.getPeer(), string);
    }

    static native void setPlaceholderImpl(long var0, String var2);

    @Override
    public boolean getReadOnly() {
        return HTMLInputElementImpl.getReadOnlyImpl(this.getPeer());
    }

    static native boolean getReadOnlyImpl(long var0);

    @Override
    public void setReadOnly(boolean bl) {
        HTMLInputElementImpl.setReadOnlyImpl(this.getPeer(), bl);
    }

    static native void setReadOnlyImpl(long var0, boolean var2);

    public boolean getRequired() {
        return HTMLInputElementImpl.getRequiredImpl(this.getPeer());
    }

    static native boolean getRequiredImpl(long var0);

    public void setRequired(boolean bl) {
        HTMLInputElementImpl.setRequiredImpl(this.getPeer(), bl);
    }

    static native void setRequiredImpl(long var0, boolean var2);

    @Override
    public String getSize() {
        return HTMLInputElementImpl.getSizeImpl(this.getPeer()) + "";
    }

    static native int getSizeImpl(long var0);

    @Override
    public void setSize(String string) throws DOMException {
        HTMLInputElementImpl.setSizeImpl(this.getPeer(), Integer.parseInt(string));
    }

    static native void setSizeImpl(long var0, int var2);

    @Override
    public String getSrc() {
        return HTMLInputElementImpl.getSrcImpl(this.getPeer());
    }

    static native String getSrcImpl(long var0);

    @Override
    public void setSrc(String string) {
        HTMLInputElementImpl.setSrcImpl(this.getPeer(), string);
    }

    static native void setSrcImpl(long var0, String var2);

    public String getStep() {
        return HTMLInputElementImpl.getStepImpl(this.getPeer());
    }

    static native String getStepImpl(long var0);

    public void setStep(String string) {
        HTMLInputElementImpl.setStepImpl(this.getPeer(), string);
    }

    static native void setStepImpl(long var0, String var2);

    @Override
    public String getType() {
        return HTMLInputElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    public void setType(String string) {
        HTMLInputElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);

    @Override
    public String getDefaultValue() {
        return HTMLInputElementImpl.getDefaultValueImpl(this.getPeer());
    }

    static native String getDefaultValueImpl(long var0);

    @Override
    public void setDefaultValue(String string) {
        HTMLInputElementImpl.setDefaultValueImpl(this.getPeer(), string);
    }

    static native void setDefaultValueImpl(long var0, String var2);

    @Override
    public String getValue() {
        return HTMLInputElementImpl.getValueImpl(this.getPeer());
    }

    static native String getValueImpl(long var0);

    @Override
    public void setValue(String string) {
        HTMLInputElementImpl.setValueImpl(this.getPeer(), string);
    }

    static native void setValueImpl(long var0, String var2);

    public long getValueAsDate() {
        return HTMLInputElementImpl.getValueAsDateImpl(this.getPeer());
    }

    static native long getValueAsDateImpl(long var0);

    public void setValueAsDate(long l) throws DOMException {
        HTMLInputElementImpl.setValueAsDateImpl(this.getPeer(), l);
    }

    static native void setValueAsDateImpl(long var0, long var2);

    public double getValueAsNumber() {
        return HTMLInputElementImpl.getValueAsNumberImpl(this.getPeer());
    }

    static native double getValueAsNumberImpl(long var0);

    public void setValueAsNumber(double d) throws DOMException {
        HTMLInputElementImpl.setValueAsNumberImpl(this.getPeer(), d);
    }

    static native void setValueAsNumberImpl(long var0, double var2);

    public int getWidth() {
        return HTMLInputElementImpl.getWidthImpl(this.getPeer());
    }

    static native int getWidthImpl(long var0);

    public void setWidth(int n) {
        HTMLInputElementImpl.setWidthImpl(this.getPeer(), n);
    }

    static native void setWidthImpl(long var0, int var2);

    public boolean getWillValidate() {
        return HTMLInputElementImpl.getWillValidateImpl(this.getPeer());
    }

    static native boolean getWillValidateImpl(long var0);

    public String getValidationMessage() {
        return HTMLInputElementImpl.getValidationMessageImpl(this.getPeer());
    }

    static native String getValidationMessageImpl(long var0);

    public NodeList getLabels() {
        return NodeListImpl.getImpl(HTMLInputElementImpl.getLabelsImpl(this.getPeer()));
    }

    static native long getLabelsImpl(long var0);

    @Override
    public String getAlign() {
        return HTMLInputElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLInputElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public String getUseMap() {
        return HTMLInputElementImpl.getUseMapImpl(this.getPeer());
    }

    static native String getUseMapImpl(long var0);

    @Override
    public void setUseMap(String string) {
        HTMLInputElementImpl.setUseMapImpl(this.getPeer(), string);
    }

    static native void setUseMapImpl(long var0, String var2);

    public boolean getIncremental() {
        return HTMLInputElementImpl.getIncrementalImpl(this.getPeer());
    }

    static native boolean getIncrementalImpl(long var0);

    public void setIncremental(boolean bl) {
        HTMLInputElementImpl.setIncrementalImpl(this.getPeer(), bl);
    }

    static native void setIncrementalImpl(long var0, boolean var2);

    public void stepUp(int n) throws DOMException {
        HTMLInputElementImpl.stepUpImpl(this.getPeer(), n);
    }

    static native void stepUpImpl(long var0, int var2);

    public void stepDown(int n) throws DOMException {
        HTMLInputElementImpl.stepDownImpl(this.getPeer(), n);
    }

    static native void stepDownImpl(long var0, int var2);

    public boolean checkValidity() {
        return HTMLInputElementImpl.checkValidityImpl(this.getPeer());
    }

    static native boolean checkValidityImpl(long var0);

    public void setCustomValidity(String string) {
        HTMLInputElementImpl.setCustomValidityImpl(this.getPeer(), string);
    }

    static native void setCustomValidityImpl(long var0, String var2);

    @Override
    public void select() {
        HTMLInputElementImpl.selectImpl(this.getPeer());
    }

    static native void selectImpl(long var0);

    public void setRangeText(String string) throws DOMException {
        HTMLInputElementImpl.setRangeTextImpl(this.getPeer(), string);
    }

    static native void setRangeTextImpl(long var0, String var2);

    public void setRangeTextEx(String string, int n, int n2, String string2) throws DOMException {
        HTMLInputElementImpl.setRangeTextExImpl(this.getPeer(), string, n, n2, string2);
    }

    static native void setRangeTextExImpl(long var0, String var2, int var3, int var4, String var5);

    public void setValueForUser(String string) {
        HTMLInputElementImpl.setValueForUserImpl(this.getPeer(), string);
    }

    static native void setValueForUserImpl(long var0, String var2);
}

