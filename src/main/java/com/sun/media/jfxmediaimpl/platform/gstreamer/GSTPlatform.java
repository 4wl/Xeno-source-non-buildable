/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.gstreamer;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.media.jfxmediaimpl.platform.gstreamer.GSTMedia;
import com.sun.media.jfxmediaimpl.platform.gstreamer.GSTMediaPlayer;
import java.util.Arrays;

public final class GSTPlatform
extends Platform {
    private static final String[] CONTENT_TYPES = new String[]{"audio/x-aiff", "audio/mp3", "audio/mpeg", "audio/x-wav", "video/x-javafx", "video/x-flv", "video/x-fxm", "video/mp4", "audio/x-m4a", "video/x-m4v", "application/vnd.apple.mpegurl", "audio/mpegurl"};
    private static final String[] PROTOCOLS = new String[]{"file", "http", "https"};
    private static GSTPlatform globalInstance = null;

    @Override
    public boolean loadPlatform() {
        MediaError mediaError;
        try {
            mediaError = MediaError.getFromCode(GSTPlatform.gstInitPlatform());
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            mediaError = MediaError.ERROR_MANAGER_ENGINEINIT_FAIL;
        }
        if (mediaError != MediaError.ERROR_NONE) {
            MediaUtils.nativeError(GSTPlatform.class, mediaError);
        }
        return true;
    }

    public static synchronized Platform getPlatformInstance() {
        if (null == globalInstance) {
            globalInstance = new GSTPlatform();
        }
        return globalInstance;
    }

    private GSTPlatform() {
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
        return new GSTMedia(locator);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public MediaPlayer createMediaPlayer(Locator locator) {
        String string;
        GSTMediaPlayer gSTMediaPlayer;
        try {
            gSTMediaPlayer = new GSTMediaPlayer(locator);
        }
        catch (Exception exception) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "GSTPlatform caught exception while creating media player: " + exception);
            }
            return null;
        }
        if (HostUtils.isMacOSX() && ("video/mp4".equals(string = locator.getContentType()) || "video/x-m4v".equals(string) || locator.getStringLocation().endsWith(".m3u8"))) {
            String string2 = locator.getURI().getScheme();
            long l = string2.equals("http") || string2.equals("https") ? 60000L : 5000L;
            long l2 = 0L;
            Object object = new Object();
            PlayerStateEvent.PlayerState playerState = gSTMediaPlayer.getState();
            while (l2 < l && (playerState == PlayerStateEvent.PlayerState.UNKNOWN || playerState == PlayerStateEvent.PlayerState.STALLED)) {
                try {
                    Object object2 = object;
                    synchronized (object2) {
                        object.wait(50L);
                        l2 += 50L;
                    }
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                playerState = gSTMediaPlayer.getState();
            }
            if (gSTMediaPlayer.getState() != PlayerStateEvent.PlayerState.READY) {
                gSTMediaPlayer.dispose();
                gSTMediaPlayer = null;
            }
        }
        return gSTMediaPlayer;
    }

    private static native int gstInitPlatform();
}

