/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.ShapeRasterizer;
import java.nio.ByteBuffer;
import java.security.AccessController;

public class NativePiscesRasterizer
implements ShapeRasterizer {
    private static MaskData emptyData = MaskData.create(new byte[1], 0, 0, 1, 1);
    private static final byte SEG_MOVETO = 0;
    private static final byte SEG_LINETO = 1;
    private static final byte SEG_QUADTO = 2;
    private static final byte SEG_CUBICTO = 3;
    private static final byte SEG_CLOSE = 4;
    private byte[] cachedMask;
    private ByteBuffer cachedBuffer;
    private MaskData cachedData;
    private int[] bounds = new int[4];
    private boolean lastAntialiasedShape;
    private boolean firstTimeAASetting = true;

    static native void init(int var0, int var1);

    static native void produceFillAlphas(float[] var0, byte[] var1, int var2, boolean var3, double var4, double var6, double var8, double var10, double var12, double var14, int[] var16, byte[] var17);

    static native void produceStrokeAlphas(float[] var0, byte[] var1, int var2, float var3, int var4, int var5, float var6, float[] var7, float var8, double var9, double var11, double var13, double var15, double var17, double var19, int[] var21, byte[] var22);

    @Override
    public MaskData getMaskData(Shape shape, BasicStroke basicStroke, RectBounds rectBounds, BaseTransform baseTransform, boolean bl, boolean bl2) {
        double d;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        Path2D path2D;
        if (this.firstTimeAASetting || this.lastAntialiasedShape != bl2) {
            int n = bl2 ? 3 : 0;
            NativePiscesRasterizer.init(n, n);
            this.firstTimeAASetting = false;
            this.lastAntialiasedShape = bl2;
        }
        if (basicStroke != null && basicStroke.getType() != 0) {
            shape = basicStroke.createStrokedShape(shape);
            basicStroke = null;
        }
        if (rectBounds == null) {
            if (basicStroke != null) {
                shape = basicStroke.createStrokedShape(shape);
                basicStroke = null;
            }
            rectBounds = new RectBounds();
            rectBounds = (RectBounds)baseTransform.transform(shape.getBounds(), rectBounds);
        }
        this.bounds[0] = (int)Math.floor(rectBounds.getMinX());
        this.bounds[1] = (int)Math.floor(rectBounds.getMinY());
        this.bounds[2] = (int)Math.ceil(rectBounds.getMaxX());
        this.bounds[3] = (int)Math.ceil(rectBounds.getMaxY());
        if (this.bounds[2] <= this.bounds[0] || this.bounds[3] <= this.bounds[1]) {
            return emptyData;
        }
        Path2D path2D2 = path2D = shape instanceof Path2D ? (Path2D)shape : new Path2D(shape);
        if (baseTransform == null || baseTransform.isIdentity()) {
            d6 = 1.0;
            d5 = 1.0;
            d4 = 0.0;
            d3 = 0.0;
            d2 = 0.0;
            d = 0.0;
        } else {
            d5 = baseTransform.getMxx();
            d3 = baseTransform.getMxy();
            d = baseTransform.getMxt();
            d4 = baseTransform.getMyx();
            d6 = baseTransform.getMyy();
            d2 = baseTransform.getMyt();
        }
        int n = this.bounds[0];
        int n2 = this.bounds[1];
        int n3 = this.bounds[2] - n;
        int n4 = this.bounds[3] - n2;
        if (n3 <= 0 || n4 <= 0) {
            return emptyData;
        }
        if (this.cachedMask == null || n3 * n4 > this.cachedMask.length) {
            this.cachedMask = null;
            this.cachedBuffer = null;
            this.cachedData = new MaskData();
            int n5 = n3 * n4 + 4095 & 0xFFFFF000;
            this.cachedMask = new byte[n5];
            this.cachedBuffer = ByteBuffer.wrap(this.cachedMask);
        }
        if (basicStroke != null) {
            NativePiscesRasterizer.produceStrokeAlphas(path2D.getFloatCoordsNoClone(), path2D.getCommandsNoClone(), path2D.getNumCommands(), basicStroke.getLineWidth(), basicStroke.getEndCap(), basicStroke.getLineJoin(), basicStroke.getMiterLimit(), basicStroke.getDashArray(), basicStroke.getDashPhase(), d5, d3, d, d4, d6, d2, this.bounds, this.cachedMask);
        } else {
            NativePiscesRasterizer.produceFillAlphas(path2D.getFloatCoordsNoClone(), path2D.getCommandsNoClone(), path2D.getNumCommands(), path2D.getWindingRule() == 1, d5, d3, d, d4, d6, d2, this.bounds, this.cachedMask);
        }
        n = this.bounds[0];
        n2 = this.bounds[1];
        n3 = this.bounds[2] - n;
        n4 = this.bounds[3] - n2;
        if (n3 <= 0 || n4 <= 0) {
            return emptyData;
        }
        this.cachedData.update(this.cachedBuffer, n, n2, n3, n4);
        return this.cachedData;
    }

    static {
        AccessController.doPrivileged(() -> {
            String string = "prism_common";
            if (PrismSettings.verbose) {
                System.out.println("Loading Prism common native library ...");
            }
            NativeLibLoader.loadLibrary(string);
            if (PrismSettings.verbose) {
                System.out.println("\tsucceeded.");
            }
            return null;
        });
    }
}

