/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.webkit.prism.WCGraphicsPrismContext;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

final class WCRadialGradient
extends WCGradient<RadialGradient> {
    static final Comparator<Stop> COMPARATOR = (stop, stop2) -> {
        float f;
        float f2 = stop.getOffset();
        if (f2 < (f = stop2.getOffset())) {
            return -1;
        }
        if (f2 > f) {
            return 1;
        }
        return 0;
    };
    private final boolean reverse;
    private final WCPoint p1;
    private final WCPoint p2;
    private final float r1over;
    private final float r1;
    private final float r2;
    private final List<Stop> stops = new ArrayList<Stop>();

    WCRadialGradient(WCPoint wCPoint, float f, WCPoint wCPoint2, float f2) {
        this.reverse = f < f2;
        this.p1 = this.reverse ? wCPoint2 : wCPoint;
        this.p2 = this.reverse ? wCPoint : wCPoint2;
        this.r1 = this.reverse ? f2 : f;
        this.r2 = this.reverse ? f : f2;
        this.r1over = this.r1 > 0.0f ? 1.0f / this.r1 : 0.0f;
    }

    @Override
    protected void addStop(int n, float f) {
        if (this.reverse) {
            f = 1.0f - f;
        }
        f = 1.0f - f + f * this.r2 * this.r1over;
        this.stops.add(new Stop(WCGraphicsPrismContext.createColor(n), f));
    }

    @Override
    public RadialGradient getPlatformGradient() {
        Collections.sort(this.stops, COMPARATOR);
        float f = this.p2.getX() - this.p1.getX();
        float f2 = this.p2.getY() - this.p1.getY();
        return new RadialGradient(this.p1.getX(), this.p1.getY(), (float)(Math.atan2(f2, f) * 180.0 / Math.PI), (float)Math.sqrt(f * f + f2 * f2) * this.r1over, this.r1, BaseTransform.IDENTITY_TRANSFORM, this.isProportional(), this.getSpreadMethod() - 1, this.stops);
    }

    public String toString() {
        return WCRadialGradient.toString(this, this.p1, this.p2, Float.valueOf(this.r1), this.stops);
    }

    static String toString(WCGradient wCGradient, WCPoint wCPoint, WCPoint wCPoint2, Float f, List<Stop> list) {
        StringBuilder stringBuilder = new StringBuilder(wCGradient.getClass().getSimpleName());
        switch (wCGradient.getSpreadMethod()) {
            case 1: {
                stringBuilder.append("[spreadMethod=PAD");
                break;
            }
            case 2: {
                stringBuilder.append("[spreadMethod=REFLECT");
                break;
            }
            case 3: {
                stringBuilder.append("[spreadMethod=REPEAT");
            }
        }
        stringBuilder.append(", proportional=").append(wCGradient.isProportional());
        if (f != null) {
            stringBuilder.append(", radius=").append(f);
        }
        stringBuilder.append(", x1=").append(wCPoint.getX());
        stringBuilder.append(", y1=").append(wCPoint.getY());
        stringBuilder.append(", x2=").append(wCPoint2.getX());
        stringBuilder.append(", y2=").append(wCPoint2.getY());
        stringBuilder.append(", stops=");
        for (int i = 0; i < list.size(); ++i) {
            stringBuilder.append(i == 0 ? "[" : ", ");
            stringBuilder.append(list.get(i).getOffset()).append(":").append(list.get(i).getColor());
        }
        return stringBuilder.append("]]").toString();
    }
}

