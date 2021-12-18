/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.collections.MapChangeListener
 *  javafx.collections.MapChangeListener$Change
 *  javafx.collections.ObservableMap
 *  javafx.collections.WeakMapChangeListener
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.MapAdapterChange;
import com.sun.javafx.collections.MapListenerHelper;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.WeakMapChangeListener;

public class UnmodifiableObservableMap<K, V>
extends AbstractMap<K, V>
implements ObservableMap<K, V> {
    private MapListenerHelper<K, V> listenerHelper;
    private final ObservableMap<K, V> backingMap;
    private final MapChangeListener<K, V> listener;
    private Set<K> keyset;
    private Collection<V> values;
    private Set<Map.Entry<K, V>> entryset;

    public UnmodifiableObservableMap(ObservableMap<K, V> observableMap) {
        this.backingMap = observableMap;
        this.listener = change -> this.callObservers(new MapAdapterChange(this, change));
        this.backingMap.addListener((MapChangeListener)new WeakMapChangeListener(this.listener));
    }

    private void callObservers(MapChangeListener.Change<? extends K, ? extends V> change) {
        MapListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
    }

    public void addListener(InvalidationListener invalidationListener) {
        this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, invalidationListener);
    }

    public void removeListener(InvalidationListener invalidationListener) {
        this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, invalidationListener);
    }

    public void addListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
        this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, mapChangeListener);
    }

    public void removeListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
        this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, mapChangeListener);
    }

    @Override
    public int size() {
        return this.backingMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object object) {
        return this.backingMap.containsKey(object);
    }

    @Override
    public boolean containsValue(Object object) {
        return this.backingMap.containsValue(object);
    }

    @Override
    public V get(Object object) {
        return (V)this.backingMap.get(object);
    }

    @Override
    public Set<K> keySet() {
        if (this.keyset == null) {
            this.keyset = Collections.unmodifiableSet(this.backingMap.keySet());
        }
        return this.keyset;
    }

    @Override
    public Collection<V> values() {
        if (this.values == null) {
            this.values = Collections.unmodifiableCollection(this.backingMap.values());
        }
        return this.values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entryset == null) {
            this.entryset = Collections.unmodifiableMap(this.backingMap).entrySet();
        }
        return this.entryset;
    }
}

