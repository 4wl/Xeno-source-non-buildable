/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control;

import java.util.LinkedList;
import java.util.List;

public class SizeLimitedList<E> {
    private final int maxSize;
    private final List<E> backingList;

    public SizeLimitedList(int n) {
        this.maxSize = n;
        this.backingList = new LinkedList();
    }

    public E get(int n) {
        return this.backingList.get(n);
    }

    public void add(E e) {
        this.backingList.add(0, e);
        if (this.backingList.size() > this.maxSize) {
            this.backingList.remove(this.maxSize);
        }
    }

    public int size() {
        return this.backingList.size();
    }

    public boolean contains(E e) {
        return this.backingList.contains(e);
    }
}

