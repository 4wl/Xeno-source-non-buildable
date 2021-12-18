/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventDispatchChain
 *  javafx.event.EventDispatcher
 */
package com.sun.javafx.event;

import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

public interface EventDispatchTree
extends EventDispatchChain {
    public EventDispatchTree createTree();

    public EventDispatchTree mergeTree(EventDispatchTree var1);

    public EventDispatchTree append(EventDispatcher var1);

    public EventDispatchTree prepend(EventDispatcher var1);
}

