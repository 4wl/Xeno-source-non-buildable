/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.graphics.Ref;
import java.util.Arrays;

public final class WCTransform
extends Ref {
    private final double[] m = new double[6];

    public WCTransform(double d, double d2, double d3, double d4, double d5, double d6) {
        this.m[0] = d;
        this.m[1] = d2;
        this.m[2] = d3;
        this.m[3] = d4;
        this.m[4] = d5;
        this.m[5] = d6;
    }

    public double[] getMatrix() {
        return Arrays.copyOf(this.m, this.m.length);
    }
}

