/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.control.VideoFormat;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicInteger;

final class NativeVideoBuffer
implements VideoDataBuffer {
    private long nativePeer;
    private final AtomicInteger holdCount = new AtomicInteger(1);
    private NativeVideoBuffer cachedBGRARep;
    private static final boolean DEBUG_DISPOSED_BUFFERS = false;
    private static final VideoBufferDisposer disposer = new VideoBufferDisposer();

    private static native void nativeDisposeBuffer(long var0);

    private native double nativeGetTimestamp(long var1);

    private native ByteBuffer nativeGetBufferForPlane(long var1, int var3);

    private native int nativeGetWidth(long var1);

    private native int nativeGetHeight(long var1);

    private native int nativeGetEncodedWidth(long var1);

    private native int nativeGetEncodedHeight(long var1);

    private native int nativeGetFormat(long var1);

    private native boolean nativeHasAlpha(long var1);

    private native int nativeGetPlaneCount(long var1);

    private native int[] nativeGetPlaneStrides(long var1);

    private native long nativeConvertToFormat(long var1, int var3);

    private native void nativeSetDirty(long var1);

    public static NativeVideoBuffer createVideoBuffer(long l) {
        NativeVideoBuffer nativeVideoBuffer = new NativeVideoBuffer(l);
        MediaDisposer.addResourceDisposer(nativeVideoBuffer, l, disposer);
        return nativeVideoBuffer;
    }

    private NativeVideoBuffer(long l) {
        this.nativePeer = l;
    }

    @Override
    public void holdFrame() {
        if (0L != this.nativePeer) {
            this.holdCount.incrementAndGet();
        }
    }

    @Override
    public void releaseFrame() {
        if (0L != this.nativePeer && this.holdCount.decrementAndGet() <= 0) {
            if (null != this.cachedBGRARep) {
                this.cachedBGRARep.releaseFrame();
                this.cachedBGRARep = null;
            }
            MediaDisposer.removeResourceDisposer(this.nativePeer);
            NativeVideoBuffer.nativeDisposeBuffer(this.nativePeer);
            this.nativePeer = 0L;
        }
    }

    @Override
    public double getTimestamp() {
        if (0L != this.nativePeer) {
            return this.nativeGetTimestamp(this.nativePeer);
        }
        return 0.0;
    }

    @Override
    public ByteBuffer getBufferForPlane(int n) {
        if (0L != this.nativePeer) {
            ByteBuffer byteBuffer = this.nativeGetBufferForPlane(this.nativePeer, n);
            byteBuffer.order(ByteOrder.nativeOrder());
            return byteBuffer;
        }
        return null;
    }

    @Override
    public int getWidth() {
        if (0L != this.nativePeer) {
            return this.nativeGetWidth(this.nativePeer);
        }
        return 0;
    }

    @Override
    public int getHeight() {
        if (0L != this.nativePeer) {
            return this.nativeGetHeight(this.nativePeer);
        }
        return 0;
    }

    @Override
    public int getEncodedWidth() {
        if (0L != this.nativePeer) {
            return this.nativeGetEncodedWidth(this.nativePeer);
        }
        return 0;
    }

    @Override
    public int getEncodedHeight() {
        if (0L != this.nativePeer) {
            return this.nativeGetEncodedHeight(this.nativePeer);
        }
        return 0;
    }

    @Override
    public VideoFormat getFormat() {
        if (0L != this.nativePeer) {
            int n = this.nativeGetFormat(this.nativePeer);
            return VideoFormat.formatForType(n);
        }
        return null;
    }

    @Override
    public boolean hasAlpha() {
        if (0L != this.nativePeer) {
            return this.nativeHasAlpha(this.nativePeer);
        }
        return false;
    }

    @Override
    public int getPlaneCount() {
        if (0L != this.nativePeer) {
            return this.nativeGetPlaneCount(this.nativePeer);
        }
        return 0;
    }

    @Override
    public int getStrideForPlane(int n) {
        if (0L != this.nativePeer) {
            int[] arrn = this.nativeGetPlaneStrides(this.nativePeer);
            return arrn[n];
        }
        return 0;
    }

    @Override
    public int[] getPlaneStrides() {
        if (0L != this.nativePeer) {
            return this.nativeGetPlaneStrides(this.nativePeer);
        }
        return null;
    }

    @Override
    public VideoDataBuffer convertToFormat(VideoFormat videoFormat) {
        if (0L != this.nativePeer) {
            if (videoFormat == VideoFormat.BGRA_PRE && null != this.cachedBGRARep) {
                this.cachedBGRARep.holdFrame();
                return this.cachedBGRARep;
            }
            long l = this.nativeConvertToFormat(this.nativePeer, videoFormat.getNativeType());
            if (0L != l) {
                NativeVideoBuffer nativeVideoBuffer = NativeVideoBuffer.createVideoBuffer(l);
                if (videoFormat == VideoFormat.BGRA_PRE) {
                    nativeVideoBuffer.holdFrame();
                    this.cachedBGRARep = nativeVideoBuffer;
                }
                return nativeVideoBuffer;
            }
            throw new UnsupportedOperationException("Conversion from " + (Object)((Object)this.getFormat()) + " to " + (Object)((Object)videoFormat) + " is not supported.");
        }
        return null;
    }

    @Override
    public void setDirty() {
        if (0L != this.nativePeer) {
            this.nativeSetDirty(this.nativePeer);
        }
    }

    public String toString() {
        return "[NativeVideoBuffer peer=" + Long.toHexString(this.nativePeer) + ", format=" + (Object)((Object)this.getFormat()) + ", size=(" + this.getWidth() + "," + this.getHeight() + "), timestamp=" + this.getTimestamp() + "]";
    }

    private static class VideoBufferDisposer
    implements MediaDisposer.ResourceDisposer {
        private VideoBufferDisposer() {
        }

        @Override
        public void disposeResource(Object object) {
            if (object instanceof Long) {
                NativeVideoBuffer.nativeDisposeBuffer((Long)object);
            }
        }
    }
}

