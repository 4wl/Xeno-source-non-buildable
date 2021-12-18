/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.openpisces.AlphaConsumer;
import com.sun.openpisces.Renderer;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.OpenPiscesPrismUtils;
import com.sun.prism.impl.shape.ShapeRasterizer;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class OpenPiscesRasterizer
implements ShapeRasterizer {
    private static MaskData emptyData = MaskData.create(new byte[1], 0, 0, 1, 1);
    private static Consumer savedConsumer;

    @Override
    public MaskData getMaskData(Shape shape, BasicStroke basicStroke, RectBounds rectBounds, BaseTransform baseTransform, boolean bl, boolean bl2) {
        Rectangle rectangle;
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
        if ((rectangle = new Rectangle(rectBounds)).isEmpty()) {
            return emptyData;
        }
        Renderer renderer = null;
        if (shape instanceof Path2D) {
            renderer = OpenPiscesPrismUtils.setupRenderer((Path2D)shape, basicStroke, baseTransform, rectangle, bl2);
        }
        if (renderer == null) {
            renderer = OpenPiscesPrismUtils.setupRenderer(shape, basicStroke, baseTransform, rectangle, bl2);
        }
        int n = renderer.getOutpixMinX();
        int n2 = renderer.getOutpixMinY();
        int n3 = renderer.getOutpixMaxX();
        int n4 = renderer.getOutpixMaxY();
        int n5 = n3 - n;
        int n6 = n4 - n2;
        if (n5 <= 0 || n6 <= 0) {
            return emptyData;
        }
        Consumer consumer = savedConsumer;
        if (consumer == null || n5 * n6 > consumer.getAlphaLength()) {
            int n7 = n5 * n6 + 4095 & 0xFFFFF000;
            savedConsumer = consumer = new Consumer(n7);
            if (PrismSettings.verbose) {
                System.out.println("new alphas");
            }
        }
        consumer.setBoundsNoClone(n, n2, n5, n6);
        renderer.produceAlphas(consumer);
        return consumer.getMaskData();
    }

    private static class Consumer
    implements AlphaConsumer {
        static byte[] savedAlphaMap;
        int x;
        int y;
        int width;
        int height;
        byte[] alphas;
        byte[] alphaMap;
        ByteBuffer alphabuffer;
        MaskData maskdata = new MaskData();

        public Consumer(int n) {
            this.alphas = new byte[n];
            this.alphabuffer = ByteBuffer.wrap(this.alphas);
        }

        public void setBoundsNoClone(int n, int n2, int n3, int n4) {
            this.x = n;
            this.y = n2;
            this.width = n3;
            this.height = n4;
            this.maskdata.update(this.alphabuffer, n, n2, n3, n4);
        }

        @Override
        public int getOriginX() {
            return this.x;
        }

        @Override
        public int getOriginY() {
            return this.y;
        }

        @Override
        public int getWidth() {
            return this.width;
        }

        @Override
        public int getHeight() {
            return this.height;
        }

        public byte[] getAlphasNoClone() {
            return this.alphas;
        }

        public int getAlphaLength() {
            return this.alphas.length;
        }

        public MaskData getMaskData() {
            return this.maskdata;
        }

        @Override
        public void setMaxAlpha(int n) {
            byte[] arrby = savedAlphaMap;
            if (arrby == null || arrby.length != n + 1) {
                arrby = new byte[n + 1];
                for (int i = 0; i <= n; ++i) {
                    arrby[i] = (byte)((i * 255 + n / 2) / n);
                }
                savedAlphaMap = arrby;
            }
            this.alphaMap = arrby;
        }

        @Override
        public void setAndClearRelativeAlphas(int[] arrn, int n, int n2, int n3) {
            int n4 = this.width;
            int n5 = (n - this.y) * n4;
            byte[] arrby = this.alphas;
            byte[] arrby2 = this.alphaMap;
            int n6 = 0;
            for (int i = 0; i < n4; ++i) {
                arrn[i] = 0;
                arrby[n5 + i] = arrby2[n6 += arrn[i]];
            }
        }

        public void setAndClearRelativeAlphas2(int[] arrn, int n, int n2, int n3) {
            if (n3 >= n2) {
                int n4;
                byte[] arrby = this.alphas;
                byte[] arrby2 = this.alphaMap;
                int n5 = n2 - this.x;
                int n6 = n3 - this.x;
                int n7 = this.width;
                int n8 = (n - this.y) * n7;
                for (n4 = 0; n4 < n5; ++n4) {
                    arrby[n8 + n4] = 0;
                }
                int n9 = 0;
                while (n4 <= n6) {
                    byte by;
                    arrn[n4] = 0;
                    arrby[n8 + n4] = by = arrby2[n9 += arrn[n4]];
                    ++n4;
                }
                arrn[n4] = 0;
                while (n4 < n7) {
                    arrby[n8 + n4] = 0;
                    ++n4;
                }
            } else {
                Arrays.fill(arrn, 0);
            }
        }
    }
}

