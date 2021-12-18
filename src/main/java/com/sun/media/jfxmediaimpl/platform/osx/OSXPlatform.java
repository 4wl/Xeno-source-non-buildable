/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.osx;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.media.jfxmediaimpl.platform.osx.OSXMedia;
import com.sun.media.jfxmediaimpl.platform.osx.OSXMediaPlayer;
import java.security.AccessController;
import java.util.Arrays;

public final class OSXPlatform
extends Platform {
    private static final String[] CONTENT_TYPES = new String[]{"audio/x-aiff", "audio/mp3", "audio/mpeg", "audio/x-m4a", "video/mp4", "video/x-m4v", "application/vnd.apple.mpegurl", "audio/mpegurl"};
    private static final String[] PROTOCOLS = new String[]{"file", "http", "https"};

    public static Platform getPlatformInstance() {
        return OSXPlatformInitializer.globalInstance;
    }

    private OSXPlatform() {
    }

    @Override
    public boolean loadPlatform() {
        if (!HostUtils.isMacOSX()) {
            return false;
        }
        try {
            return OSXPlatform.osxPlatformInit();
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "Unable to load OSX platform.");
            }
            return false;
        }
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
        return new OSXMedia(locator);
    }

    @Override
    public MediaPlayer createMediaPlayer(Locator locator) {
        try {
            return new OSXMediaPlayer(locator);
        }
        catch (Exception exception) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "OSXPlatform caught exception while creating media player: " + exception);
                exception.printStackTrace();
            }
            return null;
        }
    }

    private static native boolean osxPlatformInit();

    private static final class OSXPlatformInitializer {
        private static final OSXPlatform globalInstance;

        private OSXPlatformInitializer() {
        }

        static {
            boolean bl = false;
            try {
                bl = AccessController.doPrivileged(() -> {
                    boolean bl = false;
                    boolean bl2 = false;
                    try {
                        NativeLibLoader.loadLibrary("jfxmedia_avf");
                        bl = true;
                    }
                    catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                        // empty catch block
                    }
                    try {
                        NativeLibLoader.loadLibrary("jfxmedia_qtkit");
                        bl2 = true;
                    }
                    catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                        // empty catch block
                    }
                    return bl || bl2;
                });
            }
            catch (Exception exception) {
                // empty catch block
            }
            globalInstance = bl ? new OSXPlatform() : null;
        }
    }
}

