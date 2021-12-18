/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ListChangeListener$Change
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.ObservableListWrapper;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;

public abstract class TrackableObservableList<T>
extends ObservableListWrapper<T> {
    public TrackableObservableList(List<T> list) {
        super(list);
    }

    public TrackableObservableList() {
        super(new ArrayList());
        this.addListener(change -> this.onChanged(change));
    }

    protected abstract void onChanged(ListChangeListener.Change<T> var1);
}

