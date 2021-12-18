/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.osx;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.media.jfxmediaimpl.platform.osx.OSXPlatform;

final class OSXMedia
extends NativeMedia {
    OSXMedia(Locator locator) {
        super(locator);
    }

    @Override
    public Platform getPlatform() {
        return OSXPlatform.getPlatformInstance();
    }

    @Override
    public void dispose() {
    }
}

