/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableIntegerValue
 */
package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;

public final class IntegerConstant
implements ObservableIntegerValue {
    private final int value;

    private IntegerConstant(int n) {
        this.value = n;
    }

    public static IntegerConstant valueOf(int n) {
        return new IntegerConstant(n);
    }

    public int get() {
        return this.value;
    }

    public Integer getValue() {
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
        return this.value;
    }

    public long longValue() {
        return this.value;
    }

    public float floatValue() {
        return this.value;
    }

    public double doubleValue() {
        return this.value;
    }
}

