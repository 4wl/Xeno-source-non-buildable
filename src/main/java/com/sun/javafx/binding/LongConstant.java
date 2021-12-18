/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableLongValue
 */
package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableLongValue;

public final class LongConstant
implements ObservableLongValue {
    private final long value;

    private LongConstant(long l) {
        this.value = l;
    }

    public static LongConstant valueOf(long l) {
        return new LongConstant(l);
    }

    public long get() {
        return this.value;
    }

    public Long getValue() {
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
        return this.value;
    }

    public float floatValue() {
        return this.value;
    }

    public double doubleValue() {
        return this.value;
    }
}

