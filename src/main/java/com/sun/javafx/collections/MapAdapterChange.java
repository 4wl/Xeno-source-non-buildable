/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.MapChangeListener$Change
 *  javafx.collections.ObservableMap
 */
package com.sun.javafx.collections;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

public class MapAdapterChange<K, V>
extends MapChangeListener.Change<K, V> {
    private final MapChangeListener.Change<? extends K, ? extends V> change;

    public MapAdapterChange(ObservableMap<K, V> observableMap, MapChangeListener.Change<? extends K, ? extends V> change) {
        super(observableMap);
        this.change = change;
    }

    public boolean wasAdded() {
        return this.change.wasAdded();
    }

    public boolean wasRemoved() {
        return this.change.wasRemoved();
    }

    public K getKey() {
        return (K)this.change.getKey();
    }

    public V getValueAdded() {
        return (V)this.change.getValueAdded();
    }

    public V getValueRemoved() {
        return (V)this.change.getValueRemoved();
    }

    public String toString() {
        return this.change.toString();
    }
}

