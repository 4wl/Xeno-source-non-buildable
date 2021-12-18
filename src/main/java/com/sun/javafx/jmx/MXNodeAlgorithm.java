/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.Parent
 */
package com.sun.javafx.jmx;

import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import javafx.scene.Node;
import javafx.scene.Parent;

public interface MXNodeAlgorithm {
    public Object processLeafNode(Node var1, MXNodeAlgorithmContext var2);

    public Object processContainerNode(Parent var1, MXNodeAlgorithmContext var2);
}

