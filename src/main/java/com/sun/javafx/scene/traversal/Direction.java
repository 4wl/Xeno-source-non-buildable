/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.NodeOrientation
 */
package com.sun.javafx.scene.traversal;

import javafx.geometry.NodeOrientation;

public enum Direction {
    UP(false),
    DOWN(true),
    LEFT(false),
    RIGHT(true),
    NEXT(true),
    NEXT_IN_LINE(true),
    PREVIOUS(false);

    private final boolean forward;

    private Direction(boolean bl) {
        this.forward = bl;
    }

    public boolean isForward() {
        return this.forward;
    }

    public Direction getDirectionForNodeOrientation(NodeOrientation nodeOrientation) {
        if (nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
            switch (this) {
                case LEFT: {
                    return RIGHT;
                }
                case RIGHT: {
                    return LEFT;
                }
            }
        }
        return this;
    }
}

