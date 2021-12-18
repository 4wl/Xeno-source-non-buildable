/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableFloatValue
 */
package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableFloatValue;

public final class FloatConstant
implements ObservableFloatValue {
    private final float value;

    private FloatConstant(float f) {
        this.value = f;
    }

    public static FloatConstant valueOf(float f) {
        return new FloatConstant(f);
    }

    public float get() {
        return this.value;
    }

    public Float getValue() {
        return Float.valueOf(this.value);
    }

    public void addListener(InvalidationListener invalidationListener) {
    }

    public void addListener(ChangeListener<? super Number> changeListener) {
    }

    public void removeListener(InvalidationListener invalidationListener) {
    }

    public void removeListener(ChangeListener<? super Number> changeListener) {
    }

    public int intValue() {
        return (int)this.value;
    }

    public long longValue() {
        return (long)this.value;
    }

    public float floatValue() {
        return this.value;
    }

    public double doubleValue() {
        return this.value;
    }
}

