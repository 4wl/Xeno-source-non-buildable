/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableSet
 *  javafx.collections.SetChangeListener$Change
 */
package com.sun.javafx.collections;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public class SetAdapterChange<E>
extends SetChangeListener.Change<E> {
    private final SetChangeListener.Change<? extends E> change;

    public SetAdapterChange(ObservableSet<E> observableSet, SetChangeListener.Change<? extends E> change) {
        super(observableSet);
        this.change = change;
    }

    public String toString() {
        return this.change.toString();
    }

    public boolean wasAdded() {
        return this.change.wasAdded();
    }

    public boolean wasRemoved() {
        return this.change.wasRemoved();
    }

    public E getElementAdded() {
        return (E)this.change.getElementAdded();
    }

    public E getElementRemoved() {
        return (E)this.change.getElementRemoved();
    }
}

