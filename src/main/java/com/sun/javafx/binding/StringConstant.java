/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.binding.StringExpression
 *  javafx.beans.value.ChangeListener
 */
package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ChangeListener;

public class StringConstant
extends StringExpression {
    private final String value;

    private StringConstant(String string) {
        this.value = string;
    }

    public static StringConstant valueOf(String string) {
        return new StringConstant(string);
    }

    public String get() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    public void addListener(InvalidationListener invalidationListener) {
    }

    public void addListener(ChangeListener<? super String> changeListener) {
    }

    public void removeListener(InvalidationListener invalidationListener) {
    }

    public void removeListener(ChangeListener<? super String> changeListener) {
    }
}

