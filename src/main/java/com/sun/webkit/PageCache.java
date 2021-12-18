/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

public final class PageCache {
    private PageCache() {
        throw new AssertionError();
    }

    public static int getCapacity() {
        return PageCache.twkGetCapacity();
    }

    public static void setCapacity(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("capacity is negative:" + n);
        }
        PageCache.twkSetCapacity(n);
    }

    private static native int twkGetCapacity();

    private static native void twkSetCapacity(int var0);
}

