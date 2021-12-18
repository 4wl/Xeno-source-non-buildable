/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.beans.value.ObservableValueBase
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.MapChangeListener
 *  javafx.collections.MapChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.collections.ObservableMap
 */
package com.sun.javafx.fxml.expression;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.javafx.fxml.expression.Expression;
import com.sun.javafx.fxml.expression.KeyPath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class ExpressionValue
extends ObservableValueBase<Object> {
    private Object namespace;
    private Expression expression;
    private Class<?> type;
    private ArrayList<KeyPathMonitor> argumentMonitors;
    private int listenerCount = 0;

    public ExpressionValue(Object object, Expression expression, Class<?> class_) {
        if (object == null) {
            throw new NullPointerException();
        }
        if (expression == null) {
            throw new NullPointerException();
        }
        if (class_ == null) {
            throw new NullPointerException();
        }
        this.namespace = object;
        this.expression = expression;
        this.type = class_;
        List<KeyPath> list = expression.getArguments();
        this.argumentMonitors = new ArrayList(list.size());
        for (KeyPath keyPath : list) {
            this.argumentMonitors.add(new KeyPathMonitor(keyPath.iterator()));
        }
    }

    public Object getValue() {
        return BeanAdapter.coerce(this.expression.evaluate(this.namespace), this.type);
    }

    public void addListener(InvalidationListener invalidationListener) {
        if (this.listenerCount == 0) {
            this.monitorArguments();
        }
        super.addListener(invalidationListener);
        ++this.listenerCount;
    }

    public void removeListener(InvalidationListener invalidationListener) {
        super.removeListener(invalidationListener);
        --this.listenerCount;
        if (this.listenerCount == 0) {
            this.unmonitorArguments();
        }
    }

    public void addListener(ChangeListener<? super Object> changeListener) {
        if (this.listenerCount == 0) {
            this.monitorArguments();
        }
        super.addListener(changeListener);
        ++this.listenerCount;
    }

    public void removeListener(ChangeListener<? super Object> changeListener) {
        super.removeListener(changeListener);
        --this.listenerCount;
        if (this.listenerCount == 0) {
            this.unmonitorArguments();
        }
    }

    private void monitorArguments() {
        for (KeyPathMonitor keyPathMonitor : this.argumentMonitors) {
            keyPathMonitor.monitor(this.namespace);
        }
    }

    private void unmonitorArguments() {
        for (KeyPathMonitor keyPathMonitor : this.argumentMonitors) {
            keyPathMonitor.unmonitor();
        }
    }

    private class KeyPathMonitor {
        private String key;
        private KeyPathMonitor next;
        private Object namespace = null;
        private ListChangeListener<Object> listChangeListener = new ListChangeListener<Object>(){

            public void onChanged(ListChangeListener.Change<? extends Object> change) {
                while (change.next()) {
                    int n = Integer.parseInt(KeyPathMonitor.this.key);
                    if (n < change.getFrom() || n >= change.getTo()) continue;
                    ExpressionValue.this.fireValueChangedEvent();
                    KeyPathMonitor.this.remonitor();
                }
            }
        };
        private MapChangeListener<String, Object> mapChangeListener = new MapChangeListener<String, Object>(){

            public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                if (KeyPathMonitor.this.key.equals(change.getKey())) {
                    ExpressionValue.this.fireValueChangedEvent();
                    KeyPathMonitor.this.remonitor();
                }
            }
        };
        private ChangeListener<Object> propertyChangeListener = new ChangeListener<Object>(){

            public void changed(ObservableValue<? extends Object> observableValue, Object object, Object object2) {
                ExpressionValue.this.fireValueChangedEvent();
                KeyPathMonitor.this.remonitor();
            }
        };

        public KeyPathMonitor(Iterator<String> iterator) {
            this.key = iterator.next();
            this.next = iterator.hasNext() ? new KeyPathMonitor(iterator) : null;
        }

        public void monitor(Object object) {
            BeanAdapter beanAdapter;
            if (object instanceof ObservableList) {
                ((ObservableList)object).addListener(this.listChangeListener);
            } else if (object instanceof ObservableMap) {
                ((ObservableMap)object).addListener(this.mapChangeListener);
            } else {
                beanAdapter = new BeanAdapter(object);
                ObservableValue observableValue = beanAdapter.getPropertyModel(this.key);
                if (observableValue != null) {
                    observableValue.addListener(this.propertyChangeListener);
                }
                object = beanAdapter;
            }
            this.namespace = object;
            if (this.next != null && (beanAdapter = Expression.get(object, this.key)) != null) {
                this.next.monitor(beanAdapter);
            }
        }

        public void unmonitor() {
            BeanAdapter beanAdapter;
            ObservableValue observableValue;
            if (this.namespace instanceof ObservableList) {
                ((ObservableList)this.namespace).removeListener(this.listChangeListener);
            } else if (this.namespace instanceof ObservableMap) {
                ((ObservableMap)this.namespace).removeListener(this.mapChangeListener);
            } else if (this.namespace != null && (observableValue = (beanAdapter = (BeanAdapter)this.namespace).getPropertyModel(this.key)) != null) {
                observableValue.removeListener(this.propertyChangeListener);
            }
            this.namespace = null;
            if (this.next != null) {
                this.next.unmonitor();
            }
        }

        public void remonitor() {
            if (this.next != null) {
                this.next.unmonitor();
                Object t = Expression.get(this.namespace, this.key);
                if (t != null) {
                    this.next.monitor(t);
                }
            }
        }
    }
}

