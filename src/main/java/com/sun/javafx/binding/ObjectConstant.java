/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableObjectValue
 */
package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;

public class ObjectConstant<T>
implements ObservableObjectValue<T> {
    private final T value;

    private ObjectConstant(T t) {
        this.value = t;
    }

    public static <T> ObjectConstant<T> valueOf(T t) {
        return new ObjectConstant<T>(t);
    }

    public T get() {
        return this.value;
    }

    public T getValue() {
        return this.value;
    }

    public void addListener(InvalidationListener invalidationListener) {
    }

    public void addListener(ChangeListener<? super T> changeListener) {
    }

    public void removeListener(InvalidationListener invalidationListener) {
    }

    public void removeListener(ChangeListener<? super T> changeListener) {
    }
}

