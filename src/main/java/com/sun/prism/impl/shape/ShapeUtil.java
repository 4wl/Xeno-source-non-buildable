/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.NativePiscesRasterizer;
import com.sun.prism.impl.shape.OpenPiscesRasterizer;
import com.sun.prism.impl.shape.ShapeRasterizer;

public class ShapeUtil {
    private static final ShapeRasterizer shapeRasterizer = PrismSettings.doNativePisces ? new NativePiscesRasterizer() : new OpenPiscesRasterizer();

    public static MaskData rasterizeShape(Shape shape, BasicStroke basicStroke, RectBounds rectBounds, BaseTransform baseTransform, boolean bl, boolean bl2) {
        return shapeRasterizer.getMaskData(shape, basicStroke, rectBounds, baseTransform, bl, bl2);
    }

    private ShapeUtil() {
    }
}

