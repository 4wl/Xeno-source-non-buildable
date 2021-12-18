/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Paint;
import com.sun.webkit.graphics.WCStroke;

final class WCStrokeImpl
extends WCStroke<Paint, BasicStroke> {
    private BasicStroke stroke;

    public WCStrokeImpl() {
    }

    public WCStrokeImpl(float f, int n, int n2, float f2, float[] arrf, float f3) {
        this.setThickness(f);
        this.setLineCap(n);
        this.setLineJoin(n2);
        this.setMiterLimit(f2);
        this.setDashSizes(arrf);
        this.setDashOffset(f3);
    }

    @Override
    protected void invalidate() {
        this.stroke = null;
    }

    @Override
    public BasicStroke getPlatformStroke() {
        int n;
        if (this.stroke == null && (n = this.getStyle()) != 0) {
            float f = this.getThickness();
            float[] arrf = this.getDashSizes();
            if (arrf == null) {
                switch (n) {
                    case 2: {
                        arrf = new float[]{f, f};
                        break;
                    }
                    case 3: {
                        arrf = new float[]{3.0f * f, 3.0f * f};
                    }
                }
            }
            this.stroke = new BasicStroke(f, this.getLineCap(), this.getLineJoin(), this.getMiterLimit(), arrf, this.getDashOffset());
        }
        return this.stroke;
    }

    boolean isApplicable() {
        return this.getPaint() != null && this.getPlatformStroke() != null;
    }

    boolean apply(Graphics graphics) {
        if (this.isApplicable()) {
            Paint paint = (Paint)this.getPaint();
            BasicStroke basicStroke = this.getPlatformStroke();
            graphics.setPaint(paint);
            graphics.setStroke(basicStroke);
            return true;
        }
        return false;
    }
}

