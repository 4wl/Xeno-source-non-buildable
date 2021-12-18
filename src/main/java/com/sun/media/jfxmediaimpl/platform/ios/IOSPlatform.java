/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.ios;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.media.jfxmediaimpl.platform.ios.IOSMedia;
import com.sun.media.jfxmediaimpl.platform.ios.IOSMediaPlayer;
import java.util.Arrays;

public final class IOSPlatform
extends Platform {
    private static final String[] CONTENT_TYPES = new String[]{"video/mp4", "audio/x-m4a", "video/x-m4v", "application/vnd.apple.mpegurl", "audio/mpegurl", "audio/mpeg", "audio/mp3", "audio/x-wav", "video/quicktime", "video/x-quicktime", "audio/x-aiff"};
    private static final String[] PROTOCOLS = new String[]{"http", "https", "ipod-library"};

    public static Platform getPlatformInstance() {
        return IOSPlatformInitializer.globalInstance;
    }

    private IOSPlatform() {
    }

    @Override
    public boolean loadPlatform() {
        if (!HostUtils.isIOS()) {
            return false;
        }
        try {
            IOSPlatform.iosPlatformInit();
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "Unable to load iOS platform.");
            }
            return false;
        }
        return true;
    }

    @Override
    public String[] getSupportedContentTypes() {
        return Arrays.copyOf(CONTENT_TYPES, CONTENT_TYPES.length);
    }

    @Override
    public String[] getSupportedProtocols() {
        return Arrays.copyOf(PROTOCOLS, PROTOCOLS.length);
    }

    @Override
    public Media createMedia(Locator locator) {
        return new IOSMedia(locator);
    }

    @Override
    public MediaPlayer createMediaPlayer(Locator locator) {
        try {
            return new IOSMediaPlayer(locator);
        }
        catch (Exception exception) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "IOSPlatform caught exception while creating media player: " + exception);
            }
            return null;
        }
    }

    private static native void iosPlatformInit();

    private static final class IOSPlatformInitializer {
        private static final IOSPlatform globalInstance = new IOSPlatform();

        private IOSPlatformInitializer() {
        }
    }
}

