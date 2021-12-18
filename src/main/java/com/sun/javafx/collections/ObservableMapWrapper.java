/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.collections.MapChangeListener
 *  javafx.collections.MapChangeListener$Change
 *  javafx.collections.ObservableMap
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.MapListenerHelper;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

public class ObservableMapWrapper<K, V>
implements ObservableMap<K, V> {
    private ObservableEntrySet entrySet;
    private ObservableKeySet keySet;
    private ObservableValues values;
    private MapListenerHelper<K, V> listenerHelper;
    private final Map<K, V> backingMap;

    public ObservableMapWrapper(Map<K, V> map) {
        this.backingMap = map;
    }

    protected void callObservers(MapChangeListener.Change<K, V> change) {
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

    public int size() {
        return this.backingMap.size();
    }

    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    public boolean containsKey(Object object) {
        return this.backingMap.containsKey(object);
    }

    public boolean containsValue(Object object) {
        return this.backingMap.containsValue(object);
    }

    public V get(Object object) {
        return this.backingMap.get(object);
    }

    public V put(K k, V v) {
        V v2;
        if (this.backingMap.containsKey(k)) {
            v2 = this.backingMap.put(k, v);
            if (v2 == null && v != null || v2 != null && !v2.equals(v)) {
                this.callObservers(new SimpleChange(k, v2, v, true, true));
            }
        } else {
            v2 = this.backingMap.put(k, v);
            this.callObservers(new SimpleChange(k, v2, v, true, false));
        }
        return v2;
    }

    public V remove(Object object) {
        if (!this.backingMap.containsKey(object)) {
            return null;
        }
        V v = this.backingMap.remove(object);
        this.callObservers(new SimpleChange(object, v, null, false, true));
        return v;
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        Iterator<Map.Entry<K, V>> iterator = this.backingMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = iterator.next();
            K k = entry.getKey();
            V v = entry.getValue();
            iterator.remove();
            this.callObservers(new SimpleChange(k, v, null, false, true));
        }
    }

    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = new ObservableKeySet();
        }
        return this.keySet;
    }

    public Collection<V> values() {
        if (this.values == null) {
            this.values = new ObservableValues();
        }
        return this.values;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new ObservableEntrySet();
        }
        return this.entrySet;
    }

    public String toString() {
        return this.backingMap.toString();
    }

    public boolean equals(Object object) {
        return this.backingMap.equals(object);
    }

    public int hashCode() {
        return this.backingMap.hashCode();
    }

    private class ObservableEntrySet
    implements Set<Map.Entry<K, V>> {
        private ObservableEntrySet() {
        }

        @Override
        public int size() {
            return ObservableMapWrapper.this.backingMap.size();
        }

        @Override
        public boolean isEmpty() {
            return ObservableMapWrapper.this.backingMap.isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return ObservableMapWrapper.this.backingMap.entrySet().contains(object);
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new Iterator<Map.Entry<K, V>>(){
                private Iterator<Map.Entry<K, V>> backingIt;
                private K lastKey;
                private V lastValue;
                {
                    this.backingIt = ObservableMapWrapper.this.backingMap.entrySet().iterator();
                }

                @Override
                public boolean hasNext() {
                    return this.backingIt.hasNext();
                }

                @Override
                public Map.Entry<K, V> next() {
                    Map.Entry entry = this.backingIt.next();
                    this.lastKey = entry.getKey();
                    this.lastValue = entry.getValue();
                    return new ObservableEntry(entry);
                }

                @Override
                public void remove() {
                    this.backingIt.remove();
                    ObservableMapWrapper.this.callObservers(new SimpleChange(this.lastKey, this.lastValue, null, false, true));
                }
            };
        }

        @Override
        public Object[] toArray() {
            Object[] arrobject = ObservableMapWrapper.this.backingMap.entrySet().toArray();
            for (int i = 0; i < arrobject.length; ++i) {
                arrobject[i] = new ObservableEntry((Map.Entry)arrobject[i]);
            }
            return arrobject;
        }

        @Override
        public <T> T[] toArray(T[] arrT) {
            T[] arrT2 = ObservableMapWrapper.this.backingMap.entrySet().toArray(arrT);
            for (int i = 0; i < arrT2.length; ++i) {
                arrT2[i] = new ObservableEntry((Map.Entry)arrT2[i]);
            }
            return arrT2;
        }

        @Override
        public boolean add(Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean remove(Object object) {
            boolean bl = ObservableMapWrapper.this.backingMap.entrySet().remove(object);
            if (bl) {
                Map.Entry entry = (Map.Entry)object;
                ObservableMapWrapper.this.callObservers(new SimpleChange(entry.getKey(), entry.getValue(), null, false, true));
            }
            return bl;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return ObservableMapWrapper.this.backingMap.entrySet().containsAll(collection);
        }

        @Override
        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return this.removeRetain(collection, false);
        }

        private boolean removeRetain(Collection<?> collection, boolean bl) {
            boolean bl2 = false;
            Iterator iterator = ObservableMapWrapper.this.backingMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                if (bl != collection.contains(entry)) continue;
                bl2 = true;
                Object k = entry.getKey();
                Object v = entry.getValue();
                iterator.remove();
                ObservableMapWrapper.this.callObservers(new SimpleChange(k, v, null, false, true));
            }
            return bl2;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return this.removeRetain(collection, true);
        }

        @Override
        public void clear() {
            ObservableMapWrapper.this.clear();
        }

        public String toString() {
            return ObservableMapWrapper.this.backingMap.entrySet().toString();
        }

        @Override
        public boolean equals(Object object) {
            return ObservableMapWrapper.this.backingMap.entrySet().equals(object);
        }

        @Override
        public int hashCode() {
            return ObservableMapWrapper.this.backingMap.entrySet().hashCode();
        }
    }

    private class ObservableEntry
    implements Map.Entry<K, V> {
        private final Map.Entry<K, V> backingEntry;

        public ObservableEntry(Map.Entry<K, V> entry) {
            this.backingEntry = entry;
        }

        @Override
        public K getKey() {
            return this.backingEntry.getKey();
        }

        @Override
        public V getValue() {
            return this.backingEntry.getValue();
        }

        @Override
        public V setValue(V v) {
            Object v2 = this.backingEntry.setValue(v);
            ObservableMapWrapper.this.callObservers(new SimpleChange(this.getKey(), v2, v, true, true));
            return v2;
        }

        @Override
        public final boolean equals(Object object) {
            Object v;
            Object v2;
            Object k;
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry)object;
            Object k2 = this.getKey();
            return (k2 == (k = entry.getKey()) || k2 != null && k2.equals(k)) && ((v2 = this.getValue()) == (v = entry.getValue()) || v2 != null && v2.equals(v));
        }

        @Override
        public final int hashCode() {
            return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
        }

        public final String toString() {
            return this.getKey() + "=" + this.getValue();
        }
    }

    private class ObservableValues
    implements Collection<V> {
        private ObservableValues() {
        }

        @Override
        public int size() {
            return ObservableMapWrapper.this.backingMap.size();
        }

        @Override
        public boolean isEmpty() {
            return ObservableMapWrapper.this.backingMap.isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return ObservableMapWrapper.this.backingMap.values().contains(object);
        }

        @Override
        public Iterator<V> iterator() {
            return new Iterator<V>(){
                private Iterator<Map.Entry<K, V>> entryIt;
                private K lastKey;
                private V lastValue;
                {
                    this.entryIt = ObservableMapWrapper.this.backingMap.entrySet().iterator();
                }

                @Override
                public boolean hasNext() {
                    return this.entryIt.hasNext();
                }

                @Override
                public V next() {
                    Map.Entry entry = this.entryIt.next();
                    this.lastKey = entry.getKey();
                    this.lastValue = entry.getValue();
                    return this.lastValue;
                }

                @Override
                public void remove() {
                    this.entryIt.remove();
                    ObservableMapWrapper.this.callObservers(new SimpleChange(this.lastKey, this.lastValue, null, false, true));
                }
            };
        }

        @Override
        public Object[] toArray() {
            return ObservableMapWrapper.this.backingMap.values().toArray();
        }

        @Override
        public <T> T[] toArray(T[] arrT) {
            return ObservableMapWrapper.this.backingMap.values().toArray(arrT);
        }

        @Override
        public boolean add(V v) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean remove(Object object) {
            Iterator iterator = this.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().equals(object)) continue;
                iterator.remove();
                return true;
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return ObservableMapWrapper.this.backingMap.values().containsAll(collection);
        }

        @Override
        public boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return this.removeRetain(collection, true);
        }

        private boolean removeRetain(Collection<?> collection, boolean bl) {
            boolean bl2 = false;
            Iterator iterator = ObservableMapWrapper.this.backingMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                if (bl != collection.contains(entry.getValue())) continue;
                bl2 = true;
                Object k = entry.getKey();
                Object v = entry.getValue();
                iterator.remove();
                ObservableMapWrapper.this.callObservers(new SimpleChange(k, v, null, false, true));
            }
            return bl2;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return this.removeRetain(collection, false);
        }

        @Override
        public void clear() {
            ObservableMapWrapper.this.clear();
        }

        public String toString() {
            return ObservableMapWrapper.this.backingMap.values().toString();
        }

        @Override
        public boolean equals(Object object) {
            return ObservableMapWrapper.this.backingMap.values().equals(object);
        }

        @Override
        public int hashCode() {
            return ObservableMapWrapper.this.backingMap.values().hashCode();
        }
    }

    private class ObservableKeySet
    implements Set<K> {
        private ObservableKeySet() {
        }

        @Override
        public int size() {
            return ObservableMapWrapper.this.backingMap.size();
        }

        @Override
        public boolean isEmpty() {
            return ObservableMapWrapper.this.backingMap.isEmpty();
        }

        @Override
        public boolean contains(Object object) {
            return ObservableMapWrapper.this.backingMap.keySet().contains(object);
        }

        @Override
        public Iterator<K> iterator() {
            return new Iterator<K>(){
                private Iterator<Map.Entry<K, V>> entryIt;
                private K lastKey;
                private V lastValue;
                {
                    this.entryIt = ObservableMapWrapper.this.backingMap.entrySet().iterator();
                }

                @Override
                public boolean hasNext() {
                    return this.entryIt.hasNext();
                }

                @Override
                public K next() {
                    Map.Entry entry = this.entryIt.next();
                    this.lastKey = entry.getKey();
                    this.lastValue = entry.getValue();
                    return entry.getKey();
                }

                @Override
                public void remove() {
                    this.entryIt.remove();
                    ObservableMapWrapper.this.callObservers(new SimpleChange(this.lastKey, this.lastValue, null, false, true));
                }
            };
        }

        @Override
        public Object[] toArray() {
            return ObservableMapWrapper.this.backingMap.keySet().toArray();
        }

        @Override
        public <T> T[] toArray(T[] arrT) {
            return ObservableMapWrapper.this.backingMap.keySet().toArray(arrT);
        }

        @Override
        public boolean add(K k) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean remove(Object object) {
            return ObservableMapWrapper.this.remove(object) != null;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return ObservableMapWrapper.this.backingMap.keySet().containsAll(collection);
        }

        @Override
        public boolean addAll(Collection<? extends K> collection) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return this.removeRetain(collection, false);
        }

        private boolean removeRetain(Collection<?> collection, boolean bl) {
            boolean bl2 = false;
            Iterator iterator = ObservableMapWrapper.this.backingMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                if (bl != collection.contains(entry.getKey())) continue;
                bl2 = true;
                Object k = entry.getKey();
                Object v = entry.getValue();
                iterator.remove();
                ObservableMapWrapper.this.callObservers(new SimpleChange(k, v, null, false, true));
            }
            return bl2;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return this.removeRetain(collection, true);
        }

        @Override
        public void clear() {
            ObservableMapWrapper.this.clear();
        }

        public String toString() {
            return ObservableMapWrapper.this.backingMap.keySet().toString();
        }

        @Override
        public boolean equals(Object object) {
            return ObservableMapWrapper.this.backingMap.keySet().equals(object);
        }

        @Override
        public int hashCode() {
            return ObservableMapWrapper.this.backingMap.keySet().hashCode();
        }
    }

    private class SimpleChange
    extends MapChangeListener.Change<K, V> {
        private final K key;
        private final V old;
        private final V added;
        private final boolean wasAdded;
        private final boolean wasRemoved;

        public SimpleChange(K k, V v, V v2, boolean bl, boolean bl2) {
            super((ObservableMap)ObservableMapWrapper.this);
            assert (bl || bl2);
            this.key = k;
            this.old = v;
            this.added = v2;
            this.wasAdded = bl;
            this.wasRemoved = bl2;
        }

        public boolean wasAdded() {
            return this.wasAdded;
        }

        public boolean wasRemoved() {
            return this.wasRemoved;
        }

        public K getKey() {
            return this.key;
        }

        public V getValueAdded() {
            return this.added;
        }

        public V getValueRemoved() {
            return this.old;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.wasAdded) {
                if (this.wasRemoved) {
                    stringBuilder.append("replaced ").append(this.old).append("by ").append(this.added);
                } else {
                    stringBuilder.append("added ").append(this.added);
                }
            } else {
                stringBuilder.append("removed ").append(this.old);
            }
            stringBuilder.append(" at key ").append(this.key);
            return stringBuilder.toString();
        }
    }
}

