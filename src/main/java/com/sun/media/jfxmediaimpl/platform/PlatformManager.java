/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.media.jfxmediaimpl.platform.gstreamer.GSTPlatform;
import com.sun.media.jfxmediaimpl.platform.ios.IOSPlatform;
import com.sun.media.jfxmediaimpl.platform.java.JavaPlatform;
import com.sun.media.jfxmediaimpl.platform.osx.OSXPlatform;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class PlatformManager {
    private static String enabledPlatforms;
    private final List<Platform> platforms = new ArrayList<Platform>();

    private static void getPlatformSettings() {
        enabledPlatforms = System.getProperty("jfxmedia.platforms", "").toLowerCase();
    }

    private static boolean isPlatformEnabled(String string) {
        if (null == enabledPlatforms || enabledPlatforms.length() == 0) {
            return true;
        }
        return enabledPlatforms.indexOf(string.toLowerCase()) != -1;
    }

    public static PlatformManager getManager() {
        return PlatformManagerInitializer.globalInstance;
    }

    private PlatformManager() {
        Platform platform;
        if (PlatformManager.isPlatformEnabled("JavaPlatform") && null != (platform = JavaPlatform.getPlatformInstance())) {
            this.platforms.add(platform);
        }
        if (!HostUtils.isIOS() && PlatformManager.isPlatformEnabled("GSTPlatform") && null != (platform = GSTPlatform.getPlatformInstance())) {
            this.platforms.add(platform);
        }
        if (HostUtils.isMacOSX() && PlatformManager.isPlatformEnabled("OSXPlatform") && null != (platform = OSXPlatform.getPlatformInstance())) {
            this.platforms.add(platform);
        }
        if (HostUtils.isIOS() && PlatformManager.isPlatformEnabled("IOSPlatform") && null != (platform = IOSPlatform.getPlatformInstance())) {
            this.platforms.add(platform);
        }
        if (Logger.canLog(1)) {
            StringBuilder stringBuilder = new StringBuilder("Enabled JFXMedia platforms: ");
            for (Platform platform2 : this.platforms) {
                stringBuilder.append("\n   - ");
                stringBuilder.append(platform2.getClass().getName());
            }
            Logger.logMsg(1, stringBuilder.toString());
        }
    }

    public synchronized void loadPlatforms() {
        Iterator<Platform> iterator = this.platforms.iterator();
        while (iterator.hasNext()) {
            Platform platform = iterator.next();
            if (platform.loadPlatform()) continue;
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "Failed to load platform: " + platform);
            }
            iterator.remove();
        }
    }

    public List<String> getSupportedContentTypes() {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (!this.platforms.isEmpty()) {
            for (Platform platform : this.platforms) {
                String[] arrstring;
                if (Logger.canLog(1)) {
                    Logger.logMsg(1, "Getting content types from platform: " + platform);
                }
                if ((arrstring = platform.getSupportedContentTypes()) == null) continue;
                for (String string : arrstring) {
                    if (arrayList.contains(string)) continue;
                    arrayList.add(string);
                }
            }
        }
        return arrayList;
    }

    public List<String> getSupportedProtocols() {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (!this.platforms.isEmpty()) {
            for (Platform platform : this.platforms) {
                String[] arrstring = platform.getSupportedProtocols();
                if (arrstring == null) continue;
                for (String string : arrstring) {
                    if (arrayList.contains(string)) continue;
                    arrayList.add(string);
                }
            }
        }
        return arrayList;
    }

    public MetadataParser createMetadataParser(Locator locator) {
        for (Platform platform : this.platforms) {
            MetadataParser metadataParser = platform.createMetadataParser(locator);
            if (metadataParser == null) continue;
            return metadataParser;
        }
        return null;
    }

    public Media createMedia(Locator locator) {
        String string = locator.getContentType();
        String string2 = locator.getProtocol();
        for (Platform platform : this.platforms) {
            Media media;
            if (!platform.canPlayContentType(string) || !platform.canPlayProtocol(string2) || null == (media = platform.createMedia(locator))) continue;
            return media;
        }
        return null;
    }

    public MediaPlayer createMediaPlayer(Locator locator) {
        String string = locator.getContentType();
        String string2 = locator.getProtocol();
        for (Platform platform : this.platforms) {
            MediaPlayer mediaPlayer;
            if (!platform.canPlayContentType(string) || !platform.canPlayProtocol(string2) || null == (mediaPlayer = platform.createMediaPlayer(locator))) continue;
            return mediaPlayer;
        }
        return null;
    }

    static {
        AccessController.doPrivileged(() -> {
            PlatformManager.getPlatformSettings();
            return null;
        });
    }

    private static final class PlatformManagerInitializer {
        private static final PlatformManager globalInstance = new PlatformManager();

        private PlatformManagerInitializer() {
        }
    }
}

