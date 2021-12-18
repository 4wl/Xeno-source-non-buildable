/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.java;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.media.jfxmediaimpl.platform.java.FLVMetadataParser;
import com.sun.media.jfxmediaimpl.platform.java.ID3MetadataParser;

public final class JavaPlatform
extends Platform {
    private static JavaPlatform globalInstance = null;

    public static synchronized Platform getPlatformInstance() {
        if (null == globalInstance) {
            globalInstance = new JavaPlatform();
        }
        return globalInstance;
    }

    JavaPlatform() {
    }

    @Override
    public boolean loadPlatform() {
        return true;
    }

    @Override
    public MetadataParser createMetadataParser(Locator locator) {
        String string = locator.getContentType();
        if (string.equals("video/x-javafx") || string.equals("video/x-flv")) {
            return new FLVMetadataParser(locator);
        }
        if (string.equals("audio/mpeg") || string.equals("audio/mp3")) {
            return new ID3MetadataParser(locator);
        }
        return null;
    }

    @Override
    public Media createMedia(Locator locator) {
        return null;
    }

    @Override
    public MediaPlayer createMediaPlayer(Locator locator) {
        return null;
    }
}

