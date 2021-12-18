/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.control;

import com.sun.media.jfxmedia.control.VideoFormat;
import java.nio.ByteBuffer;

public interface VideoDataBuffer {
    public static final int PACKED_FORMAT_PLANE = 0;
    public static final int YCBCR_PLANE_LUMA = 0;
    public static final int YCBCR_PLANE_CR = 1;
    public static final int YCBCR_PLANE_CB = 2;
    public static final int YCBCR_PLANE_ALPHA = 3;

    public ByteBuffer getBufferForPlane(int var1);

    public double getTimestamp();

    public int getWidth();

    public int getHeight();

    public int getEncodedWidth();

    public int getEncodedHeight();

    public VideoFormat getFormat();

    public boolean hasAlpha();

    public int getPlaneCount();

    public int getStrideForPlane(int var1);

    public int[] getPlaneStrides();

    public VideoDataBuffer convertToFormat(VideoFormat var1);

    public void setDirty();

    public void holdFrame();

    public void releaseFrame();
}

