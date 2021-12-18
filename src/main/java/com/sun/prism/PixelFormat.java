/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

public enum PixelFormat {
    INT_ARGB_PRE(DataType.INT, 1, true, false),
    BYTE_BGRA_PRE(DataType.BYTE, 4, true, false),
    BYTE_RGB(DataType.BYTE, 3, true, true),
    BYTE_GRAY(DataType.BYTE, 1, true, true),
    BYTE_ALPHA(DataType.BYTE, 1, false, false),
    MULTI_YCbCr_420(DataType.BYTE, 1, false, true),
    BYTE_APPLE_422(DataType.BYTE, 2, false, true),
    FLOAT_XYZW(DataType.FLOAT, 4, false, true);

    public static final int YCBCR_PLANE_LUMA = 0;
    public static final int YCBCR_PLANE_CHROMARED = 1;
    public static final int YCBCR_PLANE_CHROMABLUE = 2;
    public static final int YCBCR_PLANE_ALPHA = 3;
    private DataType dataType;
    private int elemsPerPixelUnit;
    private boolean rgb;
    private boolean opaque;

    private PixelFormat(DataType dataType, int n2, boolean bl, boolean bl2) {
        this.dataType = dataType;
        this.elemsPerPixelUnit = n2;
        this.rgb = bl;
        this.opaque = bl2;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public int getBytesPerPixelUnit() {
        return this.elemsPerPixelUnit * this.dataType.getSizeInBytes();
    }

    public int getElemsPerPixelUnit() {
        return this.elemsPerPixelUnit;
    }

    public boolean isRGB() {
        return this.rgb;
    }

    public boolean isOpaque() {
        return this.opaque;
    }

    public static enum DataType {
        BYTE(1),
        INT(4),
        FLOAT(4);

        private int sizeInBytes;

        private DataType(int n2) {
            this.sizeInBytes = n2;
        }

        public int getSizeInBytes() {
            return this.sizeInBytes;
        }
    }
}

