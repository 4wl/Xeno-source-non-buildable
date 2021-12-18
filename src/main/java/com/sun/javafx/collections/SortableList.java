/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import java.util.Comparator;
import java.util.List;

public interface SortableList<E>
extends List<E> {
    public void sort();

    @Override
    public void sort(Comparator<? super E> var1);
}

