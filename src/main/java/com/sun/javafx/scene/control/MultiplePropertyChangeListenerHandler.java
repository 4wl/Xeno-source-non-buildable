/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.beans.value.WeakChangeListener
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.util.Callback;

public final class MultiplePropertyChangeListenerHandler {
    private final Callback<String, Void> propertyChangedHandler;
    private Map<ObservableValue<?>, String> propertyReferenceMap = new HashMap();
    private final ChangeListener<Object> propertyChangedListener = new ChangeListener<Object>(){

        public void changed(ObservableValue<?> observableValue, Object object, Object object2) {
            MultiplePropertyChangeListenerHandler.this.propertyChangedHandler.call(MultiplePropertyChangeListenerHandler.this.propertyReferenceMap.get(observableValue));
        }
    };
    private final WeakChangeListener<Object> weakPropertyChangedListener = new WeakChangeListener(this.propertyChangedListener);

    public MultiplePropertyChangeListenerHandler(Callback<String, Void> callback) {
        this.propertyChangedHandler = callback;
    }

    public final void registerChangeListener(ObservableValue<?> observableValue, String string) {
        if (!this.propertyReferenceMap.containsKey(observableValue)) {
            this.propertyReferenceMap.put(observableValue, string);
            observableValue.addListener(this.weakPropertyChangedListener);
        }
    }

    public final void unregisterChangeListener(ObservableValue<?> observableValue) {
        if (this.propertyReferenceMap.containsKey(observableValue)) {
            this.propertyReferenceMap.remove(observableValue);
            observableValue.removeListener(this.weakPropertyChangedListener);
        }
    }

    public void dispose() {
        for (ObservableValue<?> observableValue : this.propertyReferenceMap.keySet()) {
            observableValue.removeListener(this.weakPropertyChangedListener);
        }
        this.propertyReferenceMap.clear();
    }
}

