/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import com.sun.openpisces.AlphaConsumer;
import com.sun.openpisces.Curve;
import com.sun.openpisces.Helpers;
import java.util.Arrays;

public final class Renderer
implements PathConsumer2D {
    private static final int YMAX = 0;
    private static final int CURX = 1;
    private static final int OR = 2;
    private static final int SLOPE = 3;
    private static final int NEXT = 4;
    private static final int SIZEOF_EDGE = 5;
    private int sampleRowMin;
    private int sampleRowMax;
    private float edgeMinX;
    private float edgeMaxX;
    private float[] edges;
    private int[] edgeBuckets;
    private int numEdges;
    private static final float DEC_BND = 1.0f;
    private static final float INC_BND = 0.4f;
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    private final int SUBPIXEL_LG_POSITIONS_X;
    private final int SUBPIXEL_LG_POSITIONS_Y;
    private final int SUBPIXEL_POSITIONS_X;
    private final int SUBPIXEL_POSITIONS_Y;
    private final int SUBPIXEL_MASK_X;
    private final int SUBPIXEL_MASK_Y;
    final int MAX_AA_ALPHA;
    private int boundsMinX;
    private int boundsMinY;
    private int boundsMaxX;
    private int boundsMaxY;
    private int windingRule;
    private float x0;
    private float y0;
    private float pix_sx0;
    private float pix_sy0;
    private Curve c = new Curve();
    private int[] savedAlpha;
    private ScanlineIterator savedIterator;

    private void addEdgeToBucket(int n, int n2) {
        this.edges[n + 4] = this.edgeBuckets[n2 * 2];
        this.edgeBuckets[n2 * 2] = n + 1;
        int n3 = n2 * 2 + 1;
        this.edgeBuckets[n3] = this.edgeBuckets[n3] + 2;
    }

    private void quadBreakIntoLinesAndAdd(float f, float f2, Curve curve, float f3, float f4) {
        int n = 16;
        int n2 = n * n;
        float f5 = Math.max(curve.dbx / (float)n2, curve.dby / (float)n2);
        while (f5 > 32.0f) {
            f5 /= 4.0f;
            n <<= 1;
        }
        n2 = n * n;
        float f6 = curve.dbx / (float)n2;
        float f7 = curve.dby / (float)n2;
        float f8 = curve.bx / (float)n2 + curve.cx / (float)n;
        float f9 = curve.by / (float)n2 + curve.cy / (float)n;
        while (n-- > 1) {
            float f10 = f + f8;
            f8 += f6;
            float f11 = f2 + f9;
            f9 += f7;
            this.addLine(f, f2, f10, f11);
            f = f10;
            f2 = f11;
        }
        this.addLine(f, f2, f3, f4);
    }

    private void curveBreakIntoLinesAndAdd(float f, float f2, Curve curve, float f3, float f4) {
        int n = 8;
        float f5 = 2.0f * curve.dax / 512.0f;
        float f6 = 2.0f * curve.day / 512.0f;
        float f7 = f5 + curve.dbx / 64.0f;
        float f8 = f6 + curve.dby / 64.0f;
        float f9 = curve.ax / 512.0f + curve.bx / 64.0f + curve.cx / 8.0f;
        float f10 = curve.ay / 512.0f + curve.by / 64.0f + curve.cy / 8.0f;
        float f11 = f;
        float f12 = f2;
        while (n > 0) {
            while (Math.abs(f7) > 1.0f || Math.abs(f8) > 1.0f) {
                f7 = f7 / 4.0f - (f5 /= 8.0f);
                f8 = f8 / 4.0f - (f6 /= 8.0f);
                f9 = (f9 - f7) / 2.0f;
                f10 = (f10 - f8) / 2.0f;
                n <<= 1;
            }
            while (n % 2 == 0 && Math.abs(f9) <= 0.4f && Math.abs(f10) <= 0.4f) {
                f9 = 2.0f * f9 + f7;
                f10 = 2.0f * f10 + f8;
                f7 = 4.0f * (f7 + f5);
                f8 = 4.0f * (f8 + f6);
                f5 = 8.0f * f5;
                f6 = 8.0f * f6;
                n >>= 1;
            }
            if (--n > 0) {
                f11 += f9;
                f9 += f7;
                f7 += f5;
                f12 += f10;
                f10 += f8;
                f8 += f6;
            } else {
                f11 = f3;
                f12 = f4;
            }
            this.addLine(f, f2, f11, f12);
            f = f11;
            f2 = f12;
        }
    }

    private void addLine(float f, float f2, float f3, float f4) {
        float f5;
        int n;
        int n2;
        float f6 = 1.0f;
        if (f4 < f2) {
            f6 = f4;
            f4 = f2;
            f2 = f6;
            f6 = f3;
            f3 = f;
            f = f6;
            f6 = 0.0f;
        }
        if ((n2 = Math.max((int)Math.ceil(f2 - 0.5f), this.boundsMinY)) >= (n = Math.min((int)Math.ceil(f4 - 0.5f), this.boundsMaxY))) {
            return;
        }
        if (n2 < this.sampleRowMin) {
            this.sampleRowMin = n2;
        }
        if (n > this.sampleRowMax) {
            this.sampleRowMax = n;
        }
        if ((f5 = (f3 - f) / (f4 - f2)) > 0.0f) {
            if (f < this.edgeMinX) {
                this.edgeMinX = f;
            }
            if (f3 > this.edgeMaxX) {
                this.edgeMaxX = f3;
            }
        } else {
            if (f3 < this.edgeMinX) {
                this.edgeMinX = f3;
            }
            if (f > this.edgeMaxX) {
                this.edgeMaxX = f;
            }
        }
        int n3 = this.numEdges * 5;
        this.edges = Helpers.widenArray(this.edges, n3, 5);
        ++this.numEdges;
        this.edges[n3 + 2] = f6;
        this.edges[n3 + 1] = f + ((float)n2 + 0.5f - f2) * f5;
        this.edges[n3 + 3] = f5;
        this.edges[n3 + 0] = n;
        int n4 = n2 - this.boundsMinY;
        this.addEdgeToBucket(n3, n4);
        int n5 = (n - this.boundsMinY) * 2 + 1;
        this.edgeBuckets[n5] = this.edgeBuckets[n5] | 1;
    }

    public Renderer(int n, int n2) {
        this.SUBPIXEL_LG_POSITIONS_X = n;
        this.SUBPIXEL_LG_POSITIONS_Y = n2;
        this.SUBPIXEL_POSITIONS_X = 1 << this.SUBPIXEL_LG_POSITIONS_X;
        this.SUBPIXEL_POSITIONS_Y = 1 << this.SUBPIXEL_LG_POSITIONS_Y;
        this.SUBPIXEL_MASK_X = this.SUBPIXEL_POSITIONS_X - 1;
        this.SUBPIXEL_MASK_Y = this.SUBPIXEL_POSITIONS_Y - 1;
        this.MAX_AA_ALPHA = this.SUBPIXEL_POSITIONS_X * this.SUBPIXEL_POSITIONS_Y;
    }

    public Renderer(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        this(n, n2);
        this.reset(n3, n4, n5, n6, n7);
    }

    public void reset(int n, int n2, int n3, int n4, int n5) {
        this.windingRule = n5;
        this.boundsMinX = n * this.SUBPIXEL_POSITIONS_X;
        this.boundsMinY = n2 * this.SUBPIXEL_POSITIONS_Y;
        this.boundsMaxX = (n + n3) * this.SUBPIXEL_POSITIONS_X;
        this.boundsMaxY = (n2 + n4) * this.SUBPIXEL_POSITIONS_Y;
        this.edgeMinX = Float.POSITIVE_INFINITY;
        this.edgeMaxX = Float.NEGATIVE_INFINITY;
        this.sampleRowMax = this.boundsMinY;
        this.sampleRowMin = this.boundsMaxY;
        int n6 = this.boundsMaxY - this.boundsMinY;
        if (this.edgeBuckets == null || this.edgeBuckets.length < n6 * 2 + 2) {
            this.edgeBuckets = new int[n6 * 2 + 2];
        } else {
            Arrays.fill(this.edgeBuckets, 0, n6 * 2, 0);
        }
        if (this.edges == null) {
            this.edges = new float[160];
        }
        this.numEdges = 0;
        this.y0 = 0.0f;
        this.x0 = 0.0f;
        this.pix_sy0 = 0.0f;
        this.pix_sx0 = 0.0f;
    }

    private float tosubpixx(float f) {
        return f * (float)this.SUBPIXEL_POSITIONS_X;
    }

    private float tosubpixy(float f) {
        return f * (float)this.SUBPIXEL_POSITIONS_Y;
    }

    @Override
    public void moveTo(float f, float f2) {
        this.closePath();
        this.pix_sx0 = f;
        this.pix_sy0 = f2;
        this.y0 = this.tosubpixy(f2);
        this.x0 = this.tosubpixx(f);
    }

    @Override
    public void lineTo(float f, float f2) {
        float f3 = this.tosubpixx(f);
        float f4 = this.tosubpixy(f2);
        this.addLine(this.x0, this.y0, f3, f4);
        this.x0 = f3;
        this.y0 = f4;
    }

    @Override
    public void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = this.tosubpixx(f5);
        float f8 = this.tosubpixy(f6);
        this.c.set(this.x0, this.y0, this.tosubpixx(f), this.tosubpixy(f2), this.tosubpixx(f3), this.tosubpixy(f4), f7, f8);
        this.curveBreakIntoLinesAndAdd(this.x0, this.y0, this.c, f7, f8);
        this.x0 = f7;
        this.y0 = f8;
    }

    @Override
    public void quadTo(float f, float f2, float f3, float f4) {
        float f5 = this.tosubpixx(f3);
        float f6 = this.tosubpixy(f4);
        this.c.set(this.x0, this.y0, this.tosubpixx(f), this.tosubpixy(f2), f5, f6);
        this.quadBreakIntoLinesAndAdd(this.x0, this.y0, this.c, f5, f6);
        this.x0 = f5;
        this.y0 = f6;
    }

    @Override
    public void closePath() {
        this.lineTo(this.pix_sx0, this.pix_sy0);
    }

    @Override
    public void pathDone() {
        this.closePath();
    }

    public void produceAlphas(AlphaConsumer alphaConsumer) {
        alphaConsumer.setMaxAlpha(this.MAX_AA_ALPHA);
        int n = this.windingRule == 0 ? 1 : -1;
        int n2 = alphaConsumer.getWidth();
        int[] arrn = this.savedAlpha;
        if (arrn == null || arrn.length < n2 + 2) {
            this.savedAlpha = arrn = new int[n2 + 2];
        } else {
            Arrays.fill(arrn, 0, n2 + 2, 0);
        }
        int n3 = alphaConsumer.getOriginX() << this.SUBPIXEL_LG_POSITIONS_X;
        int n4 = n3 + (n2 << this.SUBPIXEL_LG_POSITIONS_X);
        int n5 = n4 >> this.SUBPIXEL_LG_POSITIONS_X;
        int n6 = n3 >> this.SUBPIXEL_LG_POSITIONS_Y;
        int n7 = this.boundsMinY;
        ScanlineIterator scanlineIterator = this.savedIterator;
        if (scanlineIterator == null) {
            this.savedIterator = scanlineIterator = new ScanlineIterator();
        } else {
            scanlineIterator.reset();
        }
        while (scanlineIterator.hasNext()) {
            int n8;
            int n9;
            int n10;
            int n11;
            int n12 = scanlineIterator.next();
            int[] arrn2 = scanlineIterator.crossings;
            n7 = scanlineIterator.curY();
            if (n12 > 0) {
                n11 = arrn2[0] >> 1;
                n10 = arrn2[n12 - 1] >> 1;
                n9 = Math.max(n11, n3);
                n8 = Math.min(n10, n4);
                n6 = Math.min(n6, n9 >> this.SUBPIXEL_LG_POSITIONS_X);
                n5 = Math.max(n5, n8 >> this.SUBPIXEL_LG_POSITIONS_X);
            }
            n11 = 0;
            n10 = n3;
            for (n9 = 0; n9 < n12; ++n9) {
                int n13;
                int n14;
                n8 = arrn2[n9];
                int n15 = n8 >> 1;
                int n16 = ((n8 & 1) << 1) - 1;
                if ((n11 & n) != 0 && (n14 = Math.max(n10, n3)) < (n13 = Math.min(n15, n4))) {
                    int n17 = (n14 -= n3) >> this.SUBPIXEL_LG_POSITIONS_X;
                    int n18 = (n13 -= n3) - 1 >> this.SUBPIXEL_LG_POSITIONS_X;
                    if (n17 == n18) {
                        int n19 = n17;
                        arrn[n19] = arrn[n19] + (n13 - n14);
                        int n20 = n17 + 1;
                        arrn[n20] = arrn[n20] - (n13 - n14);
                    } else {
                        int n21 = n13 >> this.SUBPIXEL_LG_POSITIONS_X;
                        int n22 = n17;
                        arrn[n22] = arrn[n22] + (this.SUBPIXEL_POSITIONS_X - (n14 & this.SUBPIXEL_MASK_X));
                        int n23 = n17 + 1;
                        arrn[n23] = arrn[n23] + (n14 & this.SUBPIXEL_MASK_X);
                        int n24 = n21;
                        arrn[n24] = arrn[n24] - (this.SUBPIXEL_POSITIONS_X - (n13 & this.SUBPIXEL_MASK_X));
                        int n25 = n21 + 1;
                        arrn[n25] = arrn[n25] - (n13 & this.SUBPIXEL_MASK_X);
                    }
                }
                n11 += n16;
                n10 = n15;
            }
            if ((n7 & this.SUBPIXEL_MASK_Y) != this.SUBPIXEL_MASK_Y) continue;
            alphaConsumer.setAndClearRelativeAlphas(arrn, n7 >> this.SUBPIXEL_LG_POSITIONS_Y, n6, n5);
            n5 = n4 >> this.SUBPIXEL_LG_POSITIONS_X;
            n6 = n3 >> this.SUBPIXEL_LG_POSITIONS_Y;
        }
        if ((n7 & this.SUBPIXEL_MASK_Y) < this.SUBPIXEL_MASK_Y) {
            alphaConsumer.setAndClearRelativeAlphas(arrn, n7 >> this.SUBPIXEL_LG_POSITIONS_Y, n6, n5);
        }
    }

    public int getSubpixMinX() {
        int n = (int)Math.ceil(this.edgeMinX - 0.5f);
        if (n < this.boundsMinX) {
            n = this.boundsMinX;
        }
        return n;
    }

    public int getSubpixMaxX() {
        int n = (int)Math.ceil(this.edgeMaxX - 0.5f);
        if (n > this.boundsMaxX) {
            n = this.boundsMaxX;
        }
        return n;
    }

    public int getSubpixMinY() {
        return this.sampleRowMin;
    }

    public int getSubpixMaxY() {
        return this.sampleRowMax;
    }

    public int getOutpixMinX() {
        return this.getSubpixMinX() >> this.SUBPIXEL_LG_POSITIONS_X;
    }

    public int getOutpixMaxX() {
        return this.getSubpixMaxX() + this.SUBPIXEL_MASK_X >> this.SUBPIXEL_LG_POSITIONS_X;
    }

    public int getOutpixMinY() {
        return this.sampleRowMin >> this.SUBPIXEL_LG_POSITIONS_Y;
    }

    public int getOutpixMaxY() {
        return this.sampleRowMax + this.SUBPIXEL_MASK_Y >> this.SUBPIXEL_LG_POSITIONS_Y;
    }

    private final class ScanlineIterator {
        private int[] crossings = new int[10];
        private int[] edgePtrs = new int[10];
        private int edgeCount;
        private int nextY;
        private static final int INIT_CROSSINGS_SIZE = 10;

        private ScanlineIterator() {
            this.reset();
        }

        public void reset() {
            this.nextY = Renderer.this.sampleRowMin;
            this.edgeCount = 0;
        }

        private int next() {
            int n;
            int n2;
            int n3;
            int n4 = this.nextY++;
            int n5 = n4 - Renderer.this.boundsMinY;
            int n6 = this.edgeCount;
            int[] arrn = this.edgePtrs;
            float[] arrf = Renderer.this.edges;
            int n7 = Renderer.this.edgeBuckets[n5 * 2 + 1];
            if ((n7 & 1) != 0) {
                n3 = 0;
                for (n2 = 0; n2 < n6; ++n2) {
                    n = arrn[n2];
                    if (!(arrf[n + 0] > (float)n4)) continue;
                    arrn[n3++] = n;
                }
                n6 = n3;
            }
            arrn = Helpers.widenArray(arrn, n6, n7 >> 1);
            n3 = Renderer.this.edgeBuckets[n5 * 2];
            while (n3 != 0) {
                arrn[n6++] = --n3;
                n3 = (int)arrf[n3 + 4];
            }
            this.edgePtrs = arrn;
            this.edgeCount = n6;
            int[] arrn2 = this.crossings;
            if (arrn2.length < n6) {
                this.crossings = arrn2 = new int[arrn.length];
            }
            for (n2 = 0; n2 < n6; ++n2) {
                int n8;
                n = arrn[n2];
                float f = arrf[n + 1];
                int n9 = (int)Math.ceil(f - 0.5f) << 1;
                arrf[n + 1] = f + arrf[n + 3];
                if (arrf[n + 2] > 0.0f) {
                    n9 |= 1;
                }
                int n10 = n2;
                while (--n10 >= 0 && (n8 = arrn2[n10]) > n9) {
                    arrn2[n10 + 1] = n8;
                    arrn[n10 + 1] = arrn[n10];
                }
                arrn2[n10 + 1] = n9;
                arrn[n10 + 1] = n;
            }
            return n6;
        }

        private boolean hasNext() {
            return this.nextY < Renderer.this.sampleRowMax;
        }

        private int curY() {
            return this.nextY - 1;
        }
    }
}

