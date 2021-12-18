/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml.expression;

import com.sun.javafx.fxml.expression.Expression;
import com.sun.javafx.fxml.expression.KeyPath;
import java.util.List;
import java.util.function.BiFunction;

public final class BinaryExpression<U, T>
extends Expression<T> {
    private final BiFunction<U, U, T> evaluator;
    private final Expression<U> left;
    private final Expression<U> right;

    public BinaryExpression(Expression<U> expression, Expression<U> expression2, BiFunction<U, U, T> biFunction) {
        if (expression == null) {
            throw new NullPointerException();
        }
        if (expression2 == null) {
            throw new NullPointerException();
        }
        this.left = expression;
        this.right = expression2;
        this.evaluator = biFunction;
    }

    @Override
    public T evaluate(Object object) {
        return this.evaluator.apply(this.left.evaluate(object), this.right.evaluate(object));
    }

    @Override
    public void update(Object object, Object object2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDefined(Object object) {
        return this.left.isDefined(object) && this.right.isDefined(object);
    }

    @Override
    public boolean isLValue() {
        return false;
    }

    @Override
    protected void getArguments(List<KeyPath> list) {
        this.left.getArguments(list);
        this.right.getArguments(list);
    }
}

