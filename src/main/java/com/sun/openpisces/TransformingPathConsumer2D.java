/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.transform.BaseTransform;

public abstract class TransformingPathConsumer2D
implements PathConsumer2D {
    protected PathConsumer2D out;

    public TransformingPathConsumer2D(PathConsumer2D pathConsumer2D) {
        this.out = pathConsumer2D;
    }

    public void setConsumer(PathConsumer2D pathConsumer2D) {
        this.out = pathConsumer2D;
    }

    static final class DeltaTransformFilter
    extends TransformingPathConsumer2D {
        private float Mxx;
        private float Mxy;
        private float Myx;
        private float Myy;

        DeltaTransformFilter(PathConsumer2D pathConsumer2D, float f, float f2, float f3, float f4) {
            super(pathConsumer2D);
            this.set(f, f2, f3, f4);
        }

        public void set(float f, float f2, float f3, float f4) {
            this.Mxx = f;
            this.Mxy = f2;
            this.Myx = f3;
            this.Myy = f4;
        }

        @Override
        public void moveTo(float f, float f2) {
            this.out.moveTo(f * this.Mxx + f2 * this.Mxy, f * this.Myx + f2 * this.Myy);
        }

        @Override
        public void lineTo(float f, float f2) {
            this.out.lineTo(f * this.Mxx + f2 * this.Mxy, f * this.Myx + f2 * this.Myy);
        }

        @Override
        public void quadTo(float f, float f2, float f3, float f4) {
            this.out.quadTo(f * this.Mxx + f2 * this.Mxy, f * this.Myx + f2 * this.Myy, f3 * this.Mxx + f4 * this.Mxy, f3 * this.Myx + f4 * this.Myy);
        }

        @Override
        public void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
            this.out.curveTo(f * this.Mxx + f2 * this.Mxy, f * this.Myx + f2 * this.Myy, f3 * this.Mxx + f4 * this.Mxy, f3 * this.Myx + f4 * this.Myy, f5 * this.Mxx + f6 * this.Mxy, f5 * this.Myx + f6 * this.Myy);
        }

        @Override
        public void closePath() {
            this.out.closePath();
        }

        @Override
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    static final class DeltaScaleFilter
    extends TransformingPathConsumer2D {
        private float sx;
        private float sy;

        public DeltaScaleFilter(PathConsumer2D pathConsumer2D, float f, float f2) {
            super(pathConsumer2D);
            this.set(f, f2);
        }

        public void set(float f, float f2) {
            this.sx = f;
            this.sy = f2;
        }

        @Override
        public void moveTo(float f, float f2) {
            this.out.moveTo(f * this.sx, f2 * this.sy);
        }

        @Override
        public void lineTo(float f, float f2) {
            this.out.lineTo(f * this.sx, f2 * this.sy);
        }

        @Override
        public void quadTo(float f, float f2, float f3, float f4) {
            this.out.quadTo(f * this.sx, f2 * this.sy, f3 * this.sx, f4 * this.sy);
        }

        @Override
        public void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
            this.out.curveTo(f * this.sx, f2 * this.sy, f3 * this.sx, f4 * this.sy, f5 * this.sx, f6 * this.sy);
        }

        @Override
        public void closePath() {
            this.out.closePath();
        }

        @Override
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    static final class TransformFilter
    extends TransformingPathConsumer2D {
        private float Mxx;
        private float Mxy;
        private float Mxt;
        private float Myx;
        private float Myy;
        private float Myt;

        TransformFilter(PathConsumer2D pathConsumer2D, float f, float f2, float f3, float f4, float f5, float f6) {
            super(pathConsumer2D);
            this.set(f, f2, f3, f4, f5, f6);
        }

        public void set(float f, float f2, float f3, float f4, float f5, float f6) {
            this.Mxx = f;
            this.Mxy = f2;
            this.Mxt = f3;
            this.Myx = f4;
            this.Myy = f5;
            this.Myt = f6;
        }

        @Override
        public void moveTo(float f, float f2) {
            this.out.moveTo(f * this.Mxx + f2 * this.Mxy + this.Mxt, f * this.Myx + f2 * this.Myy + this.Myt);
        }

        @Override
        public void lineTo(float f, float f2) {
            this.out.lineTo(f * this.Mxx + f2 * this.Mxy + this.Mxt, f * this.Myx + f2 * this.Myy + this.Myt);
        }

        @Override
        public void quadTo(float f, float f2, float f3, float f4) {
            this.out.quadTo(f * this.Mxx + f2 * this.Mxy + this.Mxt, f * this.Myx + f2 * this.Myy + this.Myt, f3 * this.Mxx + f4 * this.Mxy + this.Mxt, f3 * this.Myx + f4 * this.Myy + this.Myt);
        }

        @Override
        public void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
            this.out.curveTo(f * this.Mxx + f2 * this.Mxy + this.Mxt, f * this.Myx + f2 * this.Myy + this.Myt, f3 * this.Mxx + f4 * this.Mxy + this.Mxt, f3 * this.Myx + f4 * this.Myy + this.Myt, f5 * this.Mxx + f6 * this.Mxy + this.Mxt, f5 * this.Myx + f6 * this.Myy + this.Myt);
        }

        @Override
        public void closePath() {
            this.out.closePath();
        }

        @Override
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    static final class ScaleTranslateFilter
    extends TransformingPathConsumer2D {
        private float sx;
        private float sy;
        private float tx;
        private float ty;

        ScaleTranslateFilter(PathConsumer2D pathConsumer2D, float f, float f2, float f3, float f4) {
            super(pathConsumer2D);
            this.set(f, f2, f3, f4);
        }

        public void set(float f, float f2, float f3, float f4) {
            this.sx = f;
            this.sy = f2;
            this.tx = f3;
            this.ty = f4;
        }

        @Override
        public void moveTo(float f, float f2) {
            this.out.moveTo(f * this.sx + this.tx, f2 * this.sy + this.ty);
        }

        @Override
        public void lineTo(float f, float f2) {
            this.out.lineTo(f * this.sx + this.tx, f2 * this.sy + this.ty);
        }

        @Override
        public void quadTo(float f, float f2, float f3, float f4) {
            this.out.quadTo(f * this.sx + this.tx, f2 * this.sy + this.ty, f3 * this.sx + this.tx, f4 * this.sy + this.ty);
        }

        @Override
        public void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
            this.out.curveTo(f * this.sx + this.tx, f2 * this.sy + this.ty, f3 * this.sx + this.tx, f4 * this.sy + this.ty, f5 * this.sx + this.tx, f6 * this.sy + this.ty);
        }

        @Override
        public void closePath() {
            this.out.closePath();
        }

        @Override
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    static final class TranslateFilter
    extends TransformingPathConsumer2D {
        private float tx;
        private float ty;

        TranslateFilter(PathConsumer2D pathConsumer2D, float f, float f2) {
            super(pathConsumer2D);
            this.set(f, f2);
        }

        public void set(float f, float f2) {
            this.tx = f;
            this.ty = f2;
        }

        @Override
        public void moveTo(float f, float f2) {
            this.out.moveTo(f + this.tx, f2 + this.ty);
        }

        @Override
        public void lineTo(float f, float f2) {
            this.out.lineTo(f + this.tx, f2 + this.ty);
        }

        @Override
        public void quadTo(float f, float f2, float f3, float f4) {
            this.out.quadTo(f + this.tx, f2 + this.ty, f3 + this.tx, f4 + this.ty);
        }

        @Override
        public void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
            this.out.curveTo(f + this.tx, f2 + this.ty, f3 + this.tx, f4 + this.ty, f5 + this.tx, f6 + this.ty);
        }

        @Override
        public void closePath() {
            this.out.closePath();
        }

        @Override
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    public static final class FilterSet {
        private TranslateFilter translater;
        private DeltaScaleFilter deltascaler;
        private ScaleTranslateFilter scaletranslater;
        private DeltaTransformFilter deltatransformer;
        private TransformFilter transformer;

        public PathConsumer2D getConsumer(PathConsumer2D pathConsumer2D, BaseTransform baseTransform) {
            if (baseTransform == null) {
                return pathConsumer2D;
            }
            float f = (float)baseTransform.getMxx();
            float f2 = (float)baseTransform.getMxy();
            float f3 = (float)baseTransform.getMxt();
            float f4 = (float)baseTransform.getMyx();
            float f5 = (float)baseTransform.getMyy();
            float f6 = (float)baseTransform.getMyt();
            if (f2 == 0.0f && f4 == 0.0f) {
                if (f == 1.0f && f5 == 1.0f) {
                    if (f3 == 0.0f && f6 == 0.0f) {
                        return pathConsumer2D;
                    }
                    if (this.translater == null) {
                        this.translater = new TranslateFilter(pathConsumer2D, f3, f6);
                    } else {
                        this.translater.set(f3, f6);
                    }
                    return this.translater;
                }
                if (f3 == 0.0f && f6 == 0.0f) {
                    if (this.deltascaler == null) {
                        this.deltascaler = new DeltaScaleFilter(pathConsumer2D, f, f5);
                    } else {
                        this.deltascaler.set(f, f5);
                    }
                    return this.deltascaler;
                }
                if (this.scaletranslater == null) {
                    this.scaletranslater = new ScaleTranslateFilter(pathConsumer2D, f, f5, f3, f6);
                } else {
                    this.scaletranslater.set(f, f5, f3, f6);
                }
                return this.scaletranslater;
            }
            if (f3 == 0.0f && f6 == 0.0f) {
                if (this.deltatransformer == null) {
                    this.deltatransformer = new DeltaTransformFilter(pathConsumer2D, f, f2, f4, f5);
                } else {
                    this.deltatransformer.set(f, f2, f4, f5);
                }
                return this.deltatransformer;
            }
            if (this.transformer == null) {
                this.transformer = new TransformFilter(pathConsumer2D, f, f2, f3, f4, f5, f6);
            } else {
                this.transformer.set(f, f2, f3, f4, f5, f6);
            }
            return this.transformer;
        }
    }
}

