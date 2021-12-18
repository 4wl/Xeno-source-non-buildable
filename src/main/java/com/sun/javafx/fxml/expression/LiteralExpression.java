/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml.expression;

import com.sun.javafx.fxml.expression.Expression;
import com.sun.javafx.fxml.expression.KeyPath;
import java.util.List;

public class LiteralExpression<T>
extends Expression<T> {
    private T value;

    public LiteralExpression(T t) {
        this.value = t;
    }

    @Override
    public T evaluate(Object object) {
        return this.value;
    }

    @Override
    public void update(Object object, T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDefined(Object object) {
        return true;
    }

    @Override
    public boolean isLValue() {
        return false;
    }

    @Override
    protected void getArguments(List<KeyPath> list) {
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}

