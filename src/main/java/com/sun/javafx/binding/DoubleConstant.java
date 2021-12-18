/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableDoubleValue
 */
package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;

public final class DoubleConstant
implements ObservableDoubleValue {
    private final double value;

    private DoubleConstant(double d) {
        this.value = d;
    }

    public static DoubleConstant valueOf(double d) {
        return new DoubleConstant(d);
    }

    public double get() {
        return this.value;
    }

    public Double getValue() {
        return this.value;
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
        return (float)this.value;
    }

    public double doubleValue() {
        return this.value;
    }
}

