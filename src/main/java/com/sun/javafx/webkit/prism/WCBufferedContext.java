/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGRectangle;
import com.sun.javafx.sg.prism.NodeEffectInput;
import com.sun.javafx.webkit.prism.PrismGraphicsManager;
import com.sun.javafx.webkit.prism.PrismImage;
import com.sun.javafx.webkit.prism.WCGraphicsPrismContext;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.DropShadow;
import com.sun.webkit.graphics.WCImage;

final class WCBufferedContext
extends WCGraphicsPrismContext {
    private final PrismImage img;
    private boolean isInitialized;
    private final RectBounds TEMP_BOUNDS = new RectBounds();
    private final NGRectangle TEMP_NGRECT = new NGRectangle();
    private final RoundRectangle2D TEMP_RECT = new RoundRectangle2D();
    private final float[] TEMP_COORDS = new float[6];

    WCBufferedContext(PrismImage prismImage) {
        this.img = prismImage;
    }

    @Override
    public WCGraphicsPrismContext.Type type() {
        return WCGraphicsPrismContext.Type.DEDICATED;
    }

    @Override
    public WCImage getImage() {
        return this.img;
    }

    @Override
    Graphics getGraphics(boolean bl) {
        this.init();
        if (this.baseGraphics == null) {
            this.baseGraphics = this.img.getGraphics();
        }
        return super.getGraphics(bl);
    }

    @Override
    protected boolean shouldCalculateIntersection() {
        return this.baseGraphics == null;
    }

    @Override
    protected boolean shouldRenderRect(float f, float f2, float f3, float f4, DropShadow dropShadow, BasicStroke basicStroke) {
        if (!this.shouldCalculateIntersection()) {
            return true;
        }
        if (dropShadow != null) {
            this.TEMP_RECT.setFrame(f, f2, f3, f4);
            return this.shouldRenderShape(this.TEMP_RECT, dropShadow, basicStroke);
        }
        if (basicStroke != null) {
            float f5 = 0.0f;
            float f6 = 0.0f;
            switch (basicStroke.getType()) {
                case 0: {
                    f6 = basicStroke.getLineWidth();
                    f5 = f6 / 2.0f;
                    break;
                }
                case 2: {
                    f5 = basicStroke.getLineWidth();
                    f6 = f5 * 2.0f;
                    break;
                }
                case 1: {
                    break;
                }
            }
            f -= f5;
            f2 -= f5;
            f3 += f6;
            f4 += f6;
        }
        this.TEMP_BOUNDS.setBounds(f, f2, f + f3, f2 + f4);
        return this.trIntersectsClip(this.TEMP_BOUNDS, this.getTransformNoClone());
    }

    @Override
    protected boolean shouldRenderShape(Shape shape, DropShadow dropShadow, BasicStroke basicStroke) {
        if (!this.shouldCalculateIntersection()) {
            return true;
        }
        BaseTransform baseTransform = dropShadow != null ? BaseTransform.IDENTITY_TRANSFORM : this.getTransformNoClone();
        this.TEMP_COORDS[1] = Float.POSITIVE_INFINITY;
        this.TEMP_COORDS[0] = Float.POSITIVE_INFINITY;
        this.TEMP_COORDS[3] = Float.NEGATIVE_INFINITY;
        this.TEMP_COORDS[2] = Float.NEGATIVE_INFINITY;
        if (basicStroke == null) {
            Shape.accumulate(this.TEMP_COORDS, shape, baseTransform);
        } else {
            basicStroke.accumulateShapeBounds(this.TEMP_COORDS, shape, baseTransform);
        }
        this.TEMP_BOUNDS.setBounds(this.TEMP_COORDS[0], this.TEMP_COORDS[1], this.TEMP_COORDS[2], this.TEMP_COORDS[3]);
        Affine3D affine3D = null;
        if (dropShadow != null) {
            this.TEMP_NGRECT.updateRectangle(this.TEMP_BOUNDS.getMinX(), this.TEMP_BOUNDS.getMinY(), this.TEMP_BOUNDS.getWidth(), this.TEMP_BOUNDS.getHeight(), 0.0f, 0.0f);
            BaseBounds baseBounds = dropShadow.getBounds(BaseTransform.IDENTITY_TRANSFORM, new NodeEffectInput(this.TEMP_NGRECT));
            assert (baseBounds.getBoundsType() == BaseBounds.BoundsType.RECTANGLE);
            this.TEMP_BOUNDS.setBounds((RectBounds)baseBounds);
            affine3D = this.getTransformNoClone();
        }
        return this.trIntersectsClip(this.TEMP_BOUNDS, affine3D);
    }

    private boolean trIntersectsClip(RectBounds rectBounds, BaseTransform baseTransform) {
        Rectangle rectangle;
        if (baseTransform != null && !baseTransform.isIdentity()) {
            baseTransform.transform(rectBounds, rectBounds);
        }
        if ((rectangle = this.getClipRectNoClone()) != null) {
            return rectBounds.intersects(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height);
        }
        if (this.img != null) {
            return rectBounds.intersects(0.0f, 0.0f, (float)this.img.getWidth() * this.img.getPixelScale(), (float)this.img.getHeight() * this.img.getPixelScale());
        }
        return false;
    }

    @Override
    public void saveState() {
        this.init();
        super.saveState();
    }

    private void init() {
        if (!this.isInitialized) {
            BaseTransform baseTransform = PrismGraphicsManager.getPixelScaleTransform();
            this.initBaseTransform(baseTransform);
            this.setClip(0, 0, this.img.getWidth(), this.img.getHeight());
            this.isInitialized = true;
        }
    }
}

