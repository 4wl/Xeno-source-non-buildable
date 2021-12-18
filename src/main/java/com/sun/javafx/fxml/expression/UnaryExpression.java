/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml.expression;

import com.sun.javafx.fxml.expression.Expression;
import com.sun.javafx.fxml.expression.KeyPath;
import java.util.List;
import java.util.function.Function;

public final class UnaryExpression<U, T>
extends Expression<T> {
    private final Expression<U> operand;
    private final Function<U, T> evaluator;

    public UnaryExpression(Expression<U> expression, Function<U, T> function) {
        if (expression == null) {
            throw new NullPointerException();
        }
        this.operand = expression;
        this.evaluator = function;
    }

    @Override
    public T evaluate(Object object) {
        return this.evaluator.apply(this.operand.evaluate(object));
    }

    @Override
    public void update(Object object, T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDefined(Object object) {
        return this.operand.isDefined(object);
    }

    @Override
    public boolean isLValue() {
        return false;
    }

    @Override
    protected void getArguments(List<KeyPath> list) {
        this.operand.getArguments(list);
    }
}

