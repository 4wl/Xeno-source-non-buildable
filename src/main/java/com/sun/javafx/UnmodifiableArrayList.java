/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx;

import java.util.AbstractList;
import java.util.RandomAccess;

public class UnmodifiableArrayList<T>
extends AbstractList<T>
implements RandomAccess {
    private T[] elements;
    private final int size;

    public UnmodifiableArrayList(T[] arrT, int n) {
        assert (arrT != null ? n <= arrT.length : n == 0);
        this.size = n;
        this.elements = arrT;
    }

    @Override
    public T get(int n) {
        return this.elements[n];
    }

    @Override
    public int size() {
        return this.size;
    }
}

