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
import javafx.beans.WeakListener;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public class BidirectionalContentBinding {
    private static void checkParameters(Object object, Object object2) {
        if (object == null || object2 == null) {
            throw new NullPointerException("Both parameters must be specified.");
        }
        if (object == object2) {
            throw new IllegalArgumentException("Cannot bind object to itself");
        }
    }

    public static <E> Object bind(ObservableList<E> observableList, ObservableList<E> observableList2) {
        BidirectionalContentBinding.checkParameters(observableList, observableList2);
        ListContentBinding<E> listContentBinding = new ListContentBinding<E>(observableList, observableList2);
        observableList.setAll(observableList2);
        observableList.addListener(listContentBinding);
        observableList2.addListener(listContentBinding);
        return listContentBinding;
    }

    public static <E> Object bind(ObservableSet<E> observableSet, ObservableSet<E> observableSet2) {
        BidirectionalContentBinding.checkParameters(observableSet, observableSet2);
        SetContentBinding<E> setContentBinding = new SetContentBinding<E>(observableSet, observableSet2);
        observableSet.clear();
        observableSet.addAll(observableSet2);
        observableSet.addListener(setContentBinding);
        observableSet2.addListener(setContentBinding);
        return setContentBinding;
    }

    public static <K, V> Object bind(ObservableMap<K, V> observableMap, ObservableMap<K, V> observableMap2) {
        BidirectionalContentBinding.checkParameters(observableMap, observableMap2);
        MapContentBinding<K, V> mapContentBinding = new MapContentBinding<K, V>(observableMap, observableMap2);
        observableMap.clear();
        observableMap.putAll(observableMap2);
        observableMap.addListener(mapContentBinding);
        observableMap2.addListener(mapContentBinding);
        return mapContentBinding;
    }

    public static void unbind(Object object, Object object2) {
        BidirectionalContentBinding.checkParameters(object, object2);
        if (object instanceof ObservableList && object2 instanceof ObservableList) {
            ObservableList observableList = (ObservableList)object;
            ObservableList observableList2 = (ObservableList)object2;
            ListContentBinding listContentBinding = new ListContentBinding(observableList, observableList2);
            observableList.removeListener(listContentBinding);
            observableList2.removeListener(listContentBinding);
        } else if (object instanceof ObservableSet && object2 instanceof ObservableSet) {
            ObservableSet observableSet = (ObservableSet)object;
            ObservableSet observableSet2 = (ObservableSet)object2;
            SetContentBinding setContentBinding = new SetContentBinding(observableSet, observableSet2);
            observableSet.removeListener(setContentBinding);
            observableSet2.removeListener(setContentBinding);
        } else if (object instanceof ObservableMap && object2 instanceof ObservableMap) {
            ObservableMap observableMap = (ObservableMap)object;
            ObservableMap observableMap2 = (ObservableMap)object2;
            MapContentBinding mapContentBinding = new MapContentBinding(observableMap, observableMap2);
            observableMap.removeListener(mapContentBinding);
            observableMap2.removeListener(mapContentBinding);
        }
    }

    private static class MapContentBinding<K, V>
    implements MapChangeListener<K, V>,
    WeakListener {
        private final WeakReference<ObservableMap<K, V>> propertyRef1;
        private final WeakReference<ObservableMap<K, V>> propertyRef2;
        private boolean updating = false;

        public MapContentBinding(ObservableMap<K, V> observableMap, ObservableMap<K, V> observableMap2) {
            this.propertyRef1 = new WeakReference<ObservableMap<K, V>>(observableMap);
            this.propertyRef2 = new WeakReference<ObservableMap<K, V>>(observableMap2);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void onChanged(MapChangeListener.Change<? extends K, ? extends V> change) {
            if (!this.updating) {
                ObservableMap observableMap = (ObservableMap)this.propertyRef1.get();
                ObservableMap observableMap2 = (ObservableMap)this.propertyRef2.get();
                if (observableMap == null || observableMap2 == null) {
                    if (observableMap != null) {
                        observableMap.removeListener((MapChangeListener)this);
                    }
                    if (observableMap2 != null) {
                        observableMap2.removeListener((MapChangeListener)this);
                    }
                } else {
                    try {
                        ObservableMap observableMap3;
                        this.updating = true;
                        ObservableMap observableMap4 = observableMap3 = observableMap == change.getMap() ? observableMap2 : observableMap;
                        if (change.wasRemoved()) {
                            observableMap3.remove(change.getKey());
                        } else {
                            observableMap3.put(change.getKey(), change.getValueAdded());
                        }
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }

        public boolean wasGarbageCollected() {
            return this.propertyRef1.get() == null || this.propertyRef2.get() == null;
        }

        public int hashCode() {
            ObservableMap observableMap = (ObservableMap)this.propertyRef1.get();
            ObservableMap observableMap2 = (ObservableMap)this.propertyRef2.get();
            int n = observableMap == null ? 0 : observableMap.hashCode();
            int n2 = observableMap2 == null ? 0 : observableMap2.hashCode();
            return n * n2;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            Object t = this.propertyRef1.get();
            Object t2 = this.propertyRef2.get();
            if (t == null || t2 == null) {
                return false;
            }
            if (object instanceof MapContentBinding) {
                MapContentBinding mapContentBinding = (MapContentBinding)object;
                Object t3 = mapContentBinding.propertyRef1.get();
                Object t4 = mapContentBinding.propertyRef2.get();
                if (t3 == null || t4 == null) {
                    return false;
                }
                if (t == t3 && t2 == t4) {
                    return true;
                }
                if (t == t4 && t2 == t3) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class SetContentBinding<E>
    implements SetChangeListener<E>,
    WeakListener {
        private final WeakReference<ObservableSet<E>> propertyRef1;
        private final WeakReference<ObservableSet<E>> propertyRef2;
        private boolean updating = false;

        public SetContentBinding(ObservableSet<E> observableSet, ObservableSet<E> observableSet2) {
            this.propertyRef1 = new WeakReference<ObservableSet<E>>(observableSet);
            this.propertyRef2 = new WeakReference<ObservableSet<E>>(observableSet2);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void onChanged(SetChangeListener.Change<? extends E> change) {
            if (!this.updating) {
                ObservableSet observableSet = (ObservableSet)this.propertyRef1.get();
                ObservableSet observableSet2 = (ObservableSet)this.propertyRef2.get();
                if (observableSet == null || observableSet2 == null) {
                    if (observableSet != null) {
                        observableSet.removeListener((SetChangeListener)this);
                    }
                    if (observableSet2 != null) {
                        observableSet2.removeListener((SetChangeListener)this);
                    }
                } else {
                    try {
                        ObservableSet observableSet3;
                        this.updating = true;
                        ObservableSet observableSet4 = observableSet3 = observableSet == change.getSet() ? observableSet2 : observableSet;
                        if (change.wasRemoved()) {
                            observableSet3.remove(change.getElementRemoved());
                        } else {
                            observableSet3.add(change.getElementAdded());
                        }
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }

        public boolean wasGarbageCollected() {
            return this.propertyRef1.get() == null || this.propertyRef2.get() == null;
        }

        public int hashCode() {
            ObservableSet observableSet = (ObservableSet)this.propertyRef1.get();
            ObservableSet observableSet2 = (ObservableSet)this.propertyRef2.get();
            int n = observableSet == null ? 0 : observableSet.hashCode();
            int n2 = observableSet2 == null ? 0 : observableSet2.hashCode();
            return n * n2;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            Object t = this.propertyRef1.get();
            Object t2 = this.propertyRef2.get();
            if (t == null || t2 == null) {
                return false;
            }
            if (object instanceof SetContentBinding) {
                SetContentBinding setContentBinding = (SetContentBinding)object;
                Object t3 = setContentBinding.propertyRef1.get();
                Object t4 = setContentBinding.propertyRef2.get();
                if (t3 == null || t4 == null) {
                    return false;
                }
                if (t == t3 && t2 == t4) {
                    return true;
                }
                if (t == t4 && t2 == t3) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class ListContentBinding<E>
    implements ListChangeListener<E>,
    WeakListener {
        private final WeakReference<ObservableList<E>> propertyRef1;
        private final WeakReference<ObservableList<E>> propertyRef2;
        private boolean updating = false;

        public ListContentBinding(ObservableList<E> observableList, ObservableList<E> observableList2) {
            this.propertyRef1 = new WeakReference<ObservableList<E>>(observableList);
            this.propertyRef2 = new WeakReference<ObservableList<E>>(observableList2);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void onChanged(ListChangeListener.Change<? extends E> change) {
            if (!this.updating) {
                ObservableList observableList = (ObservableList)this.propertyRef1.get();
                ObservableList observableList2 = (ObservableList)this.propertyRef2.get();
                if (observableList == null || observableList2 == null) {
                    if (observableList != null) {
                        observableList.removeListener((ListChangeListener)this);
                    }
                    if (observableList2 != null) {
                        observableList2.removeListener((ListChangeListener)this);
                    }
                } else {
                    try {
                        ObservableList observableList3;
                        this.updating = true;
                        ObservableList observableList4 = observableList3 = observableList == change.getList() ? observableList2 : observableList;
                        while (change.next()) {
                            if (change.wasPermutated()) {
                                observableList3.remove(change.getFrom(), change.getTo());
                                observableList3.addAll(change.getFrom(), (Collection)change.getList().subList(change.getFrom(), change.getTo()));
                                continue;
                            }
                            if (change.wasRemoved()) {
                                observableList3.remove(change.getFrom(), change.getFrom() + change.getRemovedSize());
                            }
                            if (!change.wasAdded()) continue;
                            observableList3.addAll(change.getFrom(), (Collection)change.getAddedSubList());
                        }
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }

        public boolean wasGarbageCollected() {
            return this.propertyRef1.get() == null || this.propertyRef2.get() == null;
        }

        public int hashCode() {
            ObservableList observableList = (ObservableList)this.propertyRef1.get();
            ObservableList observableList2 = (ObservableList)this.propertyRef2.get();
            int n = observableList == null ? 0 : observableList.hashCode();
            int n2 = observableList2 == null ? 0 : observableList2.hashCode();
            return n * n2;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            Object t = this.propertyRef1.get();
            Object t2 = this.propertyRef2.get();
            if (t == null || t2 == null) {
                return false;
            }
            if (object instanceof ListContentBinding) {
                ListContentBinding listContentBinding = (ListContentBinding)object;
                Object t3 = listContentBinding.propertyRef1.get();
                Object t4 = listContentBinding.propertyRef2.get();
                if (t3 == null || t4 == null) {
                    return false;
                }
                if (t == t3 && t2 == t4) {
                    return true;
                }
                if (t == t4 && t2 == t3) {
                    return true;
                }
            }
            return false;
        }
    }
}

