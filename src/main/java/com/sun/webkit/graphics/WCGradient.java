/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

public abstract class WCGradient<G> {
    public static final int PAD = 1;
    public static final int REFLECT = 2;
    public static final int REPEAT = 3;
    private int spreadMethod = 1;
    private boolean proportional;

    void setSpreadMethod(int n) {
        if (n != 2 && n != 3) {
            n = 1;
        }
        this.spreadMethod = n;
    }

    public int getSpreadMethod() {
        return this.spreadMethod;
    }

    void setProportional(boolean bl) {
        this.proportional = bl;
    }

    public boolean isProportional() {
        return this.proportional;
    }

    protected abstract void addStop(int var1, float var2);

    public abstract G getPlatformGradient();
}

