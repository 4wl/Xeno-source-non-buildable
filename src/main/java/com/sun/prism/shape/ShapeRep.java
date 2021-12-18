/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;

public interface ShapeRep {
    public boolean is3DCapable();

    public void invalidate(InvalidationType var1);

    public void fill(Graphics var1, Shape var2, BaseBounds var3);

    public void draw(Graphics var1, Shape var2, BaseBounds var3);

    public void dispose();

    public static enum InvalidationType {
        LOCATION,
        LOCATION_AND_GEOMETRY;

    }
}

