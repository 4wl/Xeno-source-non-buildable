/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.Dragboard
 */
package com.sun.javafx.scene.input;

import javafx.scene.input.Dragboard;

public class DragboardHelper {
    private static DragboardAccessor dragboardAccessor;

    private DragboardHelper() {
    }

    public static void setDataAccessRestriction(Dragboard dragboard, boolean bl) {
        dragboardAccessor.setDataAccessRestriction(dragboard, bl);
    }

    public static void setDragboardAccessor(DragboardAccessor dragboardAccessor) {
        if (DragboardHelper.dragboardAccessor != null) {
            throw new IllegalStateException();
        }
        DragboardHelper.dragboardAccessor = dragboardAccessor;
    }

    private static void forceInit(Class<?> class_) {
        try {
            Class.forName(class_.getName(), true, class_.getClassLoader());
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new AssertionError((Object)classNotFoundException);
        }
    }

    static {
        DragboardHelper.forceInit(Dragboard.class);
    }

    public static interface DragboardAccessor {
        public void setDataAccessRestriction(Dragboard var1, boolean var2);
    }
}

