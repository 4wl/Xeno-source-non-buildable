/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml.expression;

import com.sun.javafx.fxml.expression.Expression;
import com.sun.javafx.fxml.expression.KeyPath;
import java.util.List;

public class VariableExpression
extends Expression<Object> {
    private KeyPath keyPath;

    public VariableExpression(KeyPath keyPath) {
        if (keyPath == null) {
            throw new NullPointerException();
        }
        this.keyPath = keyPath;
    }

    public KeyPath getKeyPath() {
        return this.keyPath;
    }

    @Override
    public Object evaluate(Object object) {
        return VariableExpression.get(object, this.keyPath);
    }

    @Override
    public void update(Object object, Object object2) {
        VariableExpression.set(object, this.keyPath, object2);
    }

    @Override
    public boolean isDefined(Object object) {
        return VariableExpression.isDefined(object, this.keyPath);
    }

    @Override
    public boolean isLValue() {
        return true;
    }

    @Override
    protected void getArguments(List<KeyPath> list) {
        list.add(this.keyPath);
    }

    public String toString() {
        return this.keyPath.toString();
    }
}

