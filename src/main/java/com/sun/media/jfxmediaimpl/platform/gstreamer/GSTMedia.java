/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.gstreamer;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.media.jfxmediaimpl.platform.gstreamer.GSTPlatform;

final class GSTMedia
extends NativeMedia {
    private final Object markerMutex = new Object();
    protected long refNativeMedia;

    GSTMedia(Locator locator) {
        super(locator);
        this.init();
    }

    @Override
    public Platform getPlatform() {
        return GSTPlatform.getPlatformInstance();
    }

    private void init() {
        long[] arrl = new long[1];
        Locator locator = this.getLocator();
        MediaError mediaError = MediaError.getFromCode(this.gstInitNativeMedia(locator, locator.getContentType(), locator.getContentLength(), arrl));
        if (mediaError != MediaError.ERROR_NONE && mediaError != MediaError.ERROR_PLATFORM_UNSUPPORTED) {
            MediaUtils.nativeError(this, mediaError);
        }
        this.refNativeMedia = arrl[0];
    }

    long getNativeMediaRef() {
        return this.refNativeMedia;
    }

    @Override
    public synchronized void dispose() {
        if (0L != this.refNativeMedia) {
            this.gstDispose(this.refNativeMedia);
            this.refNativeMedia = 0L;
        }
    }

    private native int gstInitNativeMedia(Locator var1, String var2, long var3, long[] var5);

    private native void gstDispose(long var1);
}

