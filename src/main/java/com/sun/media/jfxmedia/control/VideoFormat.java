/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.control;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum VideoFormat {
    ARGB(1),
    BGRA_PRE(2),
    YCbCr_420p(100),
    YCbCr_422(101);

    private int nativeType;
    private static final Map<Integer, VideoFormat> lookupMap;

    private VideoFormat(int n2) {
        this.nativeType = n2;
    }

    public int getNativeType() {
        return this.nativeType;
    }

    public boolean isRGB() {
        return this == ARGB || this == BGRA_PRE;
    }

    public boolean isEqualTo(int n) {
        return this.nativeType == n;
    }

    public static VideoFormat formatForType(int n) {
        return lookupMap.get(n);
    }

    static {
        lookupMap = new HashMap<Integer, VideoFormat>();
        for (VideoFormat videoFormat : EnumSet.allOf(VideoFormat.class)) {
            lookupMap.put(videoFormat.getNativeType(), videoFormat);
        }
    }

    public static class FormatTypes {
        public static final int FORMAT_TYPE_ARGB = 1;
        public static final int FORMAT_TYPE_BGRA_PRE = 2;
        public static final int FORMAT_TYPE_YCBCR_420P = 100;
        public static final int FORMAT_TYPE_YCBCR_422 = 101;
    }
}

