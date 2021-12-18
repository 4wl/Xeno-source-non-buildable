/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.locator.Locator;

public abstract class Platform {
    public static Platform getPlatformInstance() {
        throw new UnsupportedOperationException("Invalid platform class.");
    }

    public boolean loadPlatform() {
        return false;
    }

    public boolean canPlayContentType(String string) {
        String[] arrstring = this.getSupportedContentTypes();
        if (arrstring != null) {
            for (String string2 : arrstring) {
                if (!string2.equalsIgnoreCase(string)) continue;
                return true;
            }
        }
        return false;
    }

    public boolean canPlayProtocol(String string) {
        String[] arrstring = this.getSupportedProtocols();
        if (arrstring != null) {
            for (String string2 : arrstring) {
                if (!string2.equalsIgnoreCase(string)) continue;
                return true;
            }
        }
        return false;
    }

    public String[] getSupportedContentTypes() {
        return new String[0];
    }

    public String[] getSupportedProtocols() {
        return new String[0];
    }

    public MetadataParser createMetadataParser(Locator locator) {
        return null;
    }

    public abstract Media createMedia(Locator var1);

    public abstract MediaPlayer createMediaPlayer(Locator var1);
}

