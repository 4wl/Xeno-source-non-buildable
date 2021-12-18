/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.ios;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.media.jfxmediaimpl.platform.ios.IOSPlatform;

final class IOSMedia
extends NativeMedia {
    private long refNativeMedia;

    IOSMedia(Locator locator) {
        super(locator);
        this.init();
    }

    @Override
    public Platform getPlatform() {
        return IOSPlatform.getPlatformInstance();
    }

    private void init() {
        long[] arrl = new long[1];
        Locator locator = this.getLocator();
        MediaError mediaError = MediaError.getFromCode(this.iosInitNativeMedia(locator, locator.getContentType(), locator.getContentLength(), arrl));
        if (mediaError != MediaError.ERROR_NONE) {
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
            this.iosDispose(this.refNativeMedia);
            this.refNativeMedia = 0L;
        }
    }

    private native int iosInitNativeMedia(Locator var1, String var2, long var3, long[] var5);

    private native void iosDispose(long var1);
}

