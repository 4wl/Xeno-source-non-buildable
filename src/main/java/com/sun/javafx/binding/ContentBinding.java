/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.WeakListener
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.MapChangeListener
 *  javafx.collections.MapChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.collections.ObservableMap
 *  javafx.collections.ObservableSet
 *  javafx.collections.SetChangeListener
 *  javafx.collections.SetChangeListener$Change
 */
package com.sun.javafx.binding;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.WeakListener;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public class ContentBinding {
    private static void checkParameters(Object object, Object object2) {
        if (object == null || object2 == null) {
            throw new NullPointerException("Both parameters must be specified.");
        }
        if (object == object2) {
            throw new IllegalArgumentException("Cannot bind object to itself");
        }
    }

    public static <E> Object bind(List<E> list, ObservableList<? extends E> observableList) {
        ContentBinding.checkParameters(list, observableList);
        ListContentBinding<E> listContentBinding = new ListContentBinding<E>(list);
        if (list instanceof ObservableList) {
            ((ObservableList)list).setAll(observableList);
        } else {
            list.clear();
            list.addAll((Collection<? extends E>)observableList);
        }
        observableList.removeListener(listContentBinding);
        observableList.addListener(listContentBinding);
        return listContentBinding;
    }

    public static <E> Object bind(Set<E> set, ObservableSet<? extends E> observableSet) {
        ContentBinding.checkParameters(set, observableSet);
        SetContentBinding<E> setContentBinding = new SetContentBinding<E>(set);
        set.clear();
        set.addAll((Collection<? extends E>)observableSet);
        observableSet.removeListener(setContentBinding);
        observableSet.addListener(setContentBinding);
        return setContentBinding;
    }

    public static <K, V> Object bind(Map<K, V> map, ObservableMap<? extends K, ? extends V> observableMap) {
        ContentBinding.checkParameters(map, observableMap);
        MapContentBinding<K, V> mapContentBinding = new MapContentBinding<K, V>(map);
        map.clear();
        map.putAll((Map<? extends K, ? extends V>)observableMap);
        observableMap.removeListener(mapContentBinding);
        observableMap.addListener(mapContentBinding);
        return mapContentBinding;
    }

    public static void unbind(Object object, Object object2) {
        ContentBinding.checkParameters(object, object2);
        if (object instanceof List && object2 instanceof ObservableList) {
            ((ObservableList)object2).removeListener(new ListContentBinding((List)object));
        } else if (object instanceof Set && object2 instanceof ObservableSet) {
            ((ObservableSet)object2).removeListener(new SetContentBinding((Set)object));
        } else if (object instanceof Map && object2 instanceof ObservableMap) {
            ((ObservableMap)object2).removeListener(new MapContentBinding((Map)object));
        }
    }

    private static class MapContentBinding<K, V>
    implements MapChangeListener<K, V>,
    WeakListener {
        private final WeakReference<Map<K, V>> mapRef;

        public MapContentBinding(Map<K, V> map) {
            this.mapRef = new WeakReference<Map<K, V>>(map);
        }

        public void onChanged(MapChangeListener.Change<? extends K, ? extends V> change) {
            Map map = (Map)this.mapRef.get();
            if (map == null) {
                change.getMap().removeListener((MapChangeListener)this);
            } else if (change.wasRemoved()) {
                map.remove(change.getKey());
            } else {
                map.put(change.getKey(), change.getValueAdded());
            }
        }

        public boolean wasGarbageCollected() {
            return this.mapRef.get() == null;
        }

        public int hashCode() {
            Map map = (Map)this.mapRef.get();
            return map == null ? 0 : map.hashCode();
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            Map map = (Map)this.mapRef.get();
            if (map == null) {
                return false;
            }
            if (object instanceof MapContentBinding) {
                MapContentBinding mapContentBinding = (MapContentBinding)object;
                Map map2 = (Map)mapContentBinding.mapRef.get();
                return map == map2;
            }
            return false;
        }
    }

    private static class SetContentBinding<E>
    implements SetChangeListener<E>,
    WeakListener {
        private final WeakReference<Set<E>> setRef;

        public SetContentBinding(Set<E> set) {
            this.setRef = new WeakReference<Set<E>>(set);
        }

        public void onChanged(SetChangeListener.Change<? extends E> change) {
            Set set = (Set)this.setRef.get();
            if (set == null) {
                change.getSet().removeListener((SetChangeListener)this);
            } else if (change.wasRemoved()) {
                set.remove(change.getElementRemoved());
            } else {
                set.add(change.getElementAdded());
            }
        }

        public boolean wasGarbageCollected() {
            return this.setRef.get() == null;
        }

        public int hashCode() {
            Set set = (Set)this.setRef.get();
            return set == null ? 0 : set.hashCode();
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            Set set = (Set)this.setRef.get();
            if (set == null) {
                return false;
            }
            if (object instanceof SetContentBinding) {
                SetContentBinding setContentBinding = (SetContentBinding)object;
                Set set2 = (Set)setContentBinding.setRef.get();
                return set == set2;
            }
            return false;
        }
    }

    private static class ListContentBinding<E>
    implements ListChangeListener<E>,
    WeakListener {
        private final WeakReference<List<E>> listRef;

        public ListContentBinding(List<E> list) {
            this.listRef = new WeakReference<List<E>>(list);
        }

        public void onChanged(ListChangeListener.Change<? extends E> change) {
            List list = (List)this.listRef.get();
            if (list == null) {
                change.getList().removeListener((ListChangeListener)this);
            } else {
                while (change.next()) {
                    if (change.wasPermutated()) {
                        list.subList(change.getFrom(), change.getTo()).clear();
                        list.addAll(change.getFrom(), change.getList().subList(change.getFrom(), change.getTo()));
                        continue;
                    }
                    if (change.wasRemoved()) {
                        list.subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                    }
                    if (!change.wasAdded()) continue;
                    list.addAll(change.getFrom(), change.getAddedSubList());
                }
            }
        }

        public boolean wasGarbageCollected() {
            return this.listRef.get() == null;
        }

        public int hashCode() {
            List list = (List)this.listRef.get();
            return list == null ? 0 : list.hashCode();
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            List list = (List)this.listRef.get();
            if (list == null) {
                return false;
            }
            if (object instanceof ListContentBinding) {
                ListContentBinding listContentBinding = (ListContentBinding)object;
                List list2 = (List)listContentBinding.listRef.get();
                return list == list2;
            }
            return false;
        }
    }
}

