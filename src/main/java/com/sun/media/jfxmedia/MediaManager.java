/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMediaManager;
import java.util.List;

public class MediaManager {
    private MediaManager() {
    }

    public static String[] getSupportedContentTypes() {
        return NativeMediaManager.getDefaultInstance().getSupportedContentTypes();
    }

    public static boolean canPlayContentType(String string) {
        if (string == null) {
            throw new IllegalArgumentException("contentType == null!");
        }
        return NativeMediaManager.getDefaultInstance().canPlayContentType(string);
    }

    public static boolean canPlayProtocol(String string) {
        if (string == null) {
            throw new IllegalArgumentException("protocol == null!");
        }
        return NativeMediaManager.getDefaultInstance().canPlayProtocol(string);
    }

    public static MetadataParser getMetadataParser(Locator locator) {
        if (locator == null) {
            throw new IllegalArgumentException("locator == null!");
        }
        NativeMediaManager.getDefaultInstance();
        return NativeMediaManager.getMetadataParser(locator);
    }

    public static Media getMedia(Locator locator) {
        if (locator == null) {
            throw new IllegalArgumentException("locator == null!");
        }
        return NativeMediaManager.getDefaultInstance().getMedia(locator);
    }

    public static MediaPlayer getPlayer(Locator locator) {
        if (locator == null) {
            throw new IllegalArgumentException("locator == null!");
        }
        return NativeMediaManager.getDefaultInstance().getPlayer(locator);
    }

    public static void addMediaErrorListener(MediaErrorListener mediaErrorListener) {
        if (mediaErrorListener == null) {
            throw new IllegalArgumentException("listener == null!");
        }
        NativeMediaManager.getDefaultInstance().addMediaErrorListener(mediaErrorListener);
    }

    public static void removeMediaErrorListener(MediaErrorListener mediaErrorListener) {
        if (mediaErrorListener == null) {
            throw new IllegalArgumentException("listener == null!");
        }
        NativeMediaManager.getDefaultInstance().removeMediaErrorListener(mediaErrorListener);
    }

    public static void registerMediaPlayerForDispose(Object object, MediaPlayer mediaPlayer) {
        NativeMediaManager.registerMediaPlayerForDispose(object, mediaPlayer);
    }

    public static List<MediaPlayer> getAllMediaPlayers() {
        return NativeMediaManager.getDefaultInstance().getAllMediaPlayers();
    }
}

