/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.webkit.prism.WCGraphicsPrismContext;
import com.sun.javafx.webkit.prism.WCRadialGradient;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Stop;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class WCLinearGradient
extends WCGradient<LinearGradient> {
    private final WCPoint p1;
    private final WCPoint p2;
    private final List<Stop> stops = new ArrayList<Stop>();

    WCLinearGradient(WCPoint wCPoint, WCPoint wCPoint2) {
        this.p1 = wCPoint;
        this.p2 = wCPoint2;
    }

    @Override
    protected void addStop(int n, float f) {
        this.stops.add(new Stop(WCGraphicsPrismContext.createColor(n), f));
    }

    @Override
    public LinearGradient getPlatformGradient() {
        Collections.sort(this.stops, WCRadialGradient.COMPARATOR);
        return new LinearGradient(this.p1.getX(), this.p1.getY(), this.p2.getX(), this.p2.getY(), BaseTransform.IDENTITY_TRANSFORM, this.isProportional(), this.getSpreadMethod() - 1, this.stops);
    }

    public String toString() {
        return WCRadialGradient.toString(this, this.p1, this.p2, null, this.stops);
    }
}

