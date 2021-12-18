/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.platform.PlatformManager;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

public class NativeMediaManager {
    private static boolean isNativeLayerInitialized = false;
    private final List<WeakReference<MediaErrorListener>> errorListeners = new ArrayList<WeakReference<MediaErrorListener>>();
    private static final NativeMediaPlayerDisposer playerDisposer = new NativeMediaPlayerDisposer();
    private static final Map<MediaPlayer, Boolean> allMediaPlayers = new WeakHashMap<MediaPlayer, Boolean>();
    private final List<String> supportedContentTypes = new ArrayList<String>();
    private final List<String> supportedProtocols = new ArrayList<String>();

    public static NativeMediaManager getDefaultInstance() {
        return NativeMediaManagerInitializer.globalInstance;
    }

    protected NativeMediaManager() {
        try {
            AccessController.doPrivileged(() -> {
                if (HostUtils.isWindows() || HostUtils.isMacOSX()) {
                    NativeLibLoader.loadLibrary("glib-lite");
                }
                if (!HostUtils.isLinux() && !HostUtils.isIOS()) {
                    NativeLibLoader.loadLibrary("gstreamer-lite");
                }
                NativeLibLoader.loadLibrary("jfxmedia");
                return null;
            });
        }
        catch (PrivilegedActionException privilegedActionException) {
            MediaUtils.error(null, MediaError.ERROR_MANAGER_ENGINEINIT_FAIL.code(), "Unable to load one or more dependent libraries.", privilegedActionException);
        }
        if (!Logger.initNative()) {
            MediaUtils.error(null, MediaError.ERROR_MANAGER_LOGGER_INIT.code(), "Unable to init logger", null);
        }
    }

    static synchronized void initNativeLayer() {
        if (!isNativeLayerInitialized) {
            PlatformManager.getManager().loadPlatforms();
            isNativeLayerInitialized = true;
        }
    }

    private synchronized void loadContentTypes() {
        if (!this.supportedContentTypes.isEmpty()) {
            return;
        }
        List<String> list = PlatformManager.getManager().getSupportedContentTypes();
        if (null != list && !list.isEmpty()) {
            this.supportedContentTypes.addAll(list);
        }
        if (Logger.canLog(1)) {
            StringBuilder stringBuilder = new StringBuilder("JFXMedia supported content types:\n");
            for (String string : this.supportedContentTypes) {
                stringBuilder.append("    ");
                stringBuilder.append(string);
                stringBuilder.append("\n");
            }
            Logger.logMsg(1, stringBuilder.toString());
        }
    }

    private synchronized void loadProtocols() {
        if (!this.supportedProtocols.isEmpty()) {
            return;
        }
        List<String> list = PlatformManager.getManager().getSupportedProtocols();
        if (null != list && !list.isEmpty()) {
            this.supportedProtocols.addAll(list);
        }
        if (Logger.canLog(1)) {
            StringBuilder stringBuilder = new StringBuilder("JFXMedia supported protocols:\n");
            for (String string : this.supportedProtocols) {
                stringBuilder.append("    ");
                stringBuilder.append(string);
                stringBuilder.append("\n");
            }
            Logger.logMsg(1, stringBuilder.toString());
        }
    }

    public boolean canPlayContentType(String string) {
        if (string == null) {
            throw new IllegalArgumentException("contentType == null!");
        }
        if (this.supportedContentTypes.isEmpty()) {
            this.loadContentTypes();
        }
        for (String string2 : this.supportedContentTypes) {
            if (!string.equalsIgnoreCase(string2)) continue;
            return true;
        }
        return false;
    }

    public String[] getSupportedContentTypes() {
        if (this.supportedContentTypes.isEmpty()) {
            this.loadContentTypes();
        }
        return this.supportedContentTypes.toArray(new String[1]);
    }

    public boolean canPlayProtocol(String string) {
        if (string == null) {
            throw new IllegalArgumentException("protocol == null!");
        }
        if (this.supportedProtocols.isEmpty()) {
            this.loadProtocols();
        }
        for (String string2 : this.supportedProtocols) {
            if (!string.equalsIgnoreCase(string2)) continue;
            return true;
        }
        return false;
    }

    public static MetadataParser getMetadataParser(Locator locator) {
        return PlatformManager.getManager().createMetadataParser(locator);
    }

    public MediaPlayer getPlayer(Locator locator) {
        NativeMediaManager.initNativeLayer();
        MediaPlayer mediaPlayer = PlatformManager.getManager().createMediaPlayer(locator);
        if (null == mediaPlayer) {
            throw new MediaException("Could not create player!");
        }
        allMediaPlayers.put(mediaPlayer, Boolean.TRUE);
        return mediaPlayer;
    }

    public Media getMedia(Locator locator) {
        NativeMediaManager.initNativeLayer();
        return PlatformManager.getManager().createMedia(locator);
    }

    public void addMediaErrorListener(MediaErrorListener mediaErrorListener) {
        if (mediaErrorListener != null) {
            ListIterator<WeakReference<MediaErrorListener>> listIterator = this.errorListeners.listIterator();
            while (listIterator.hasNext()) {
                MediaErrorListener mediaErrorListener2 = (MediaErrorListener)listIterator.next().get();
                if (mediaErrorListener2 != null) continue;
                listIterator.remove();
            }
            this.errorListeners.add(new WeakReference<MediaErrorListener>(mediaErrorListener));
        }
    }

    public void removeMediaErrorListener(MediaErrorListener mediaErrorListener) {
        if (mediaErrorListener != null) {
            ListIterator<WeakReference<MediaErrorListener>> listIterator = this.errorListeners.listIterator();
            while (listIterator.hasNext()) {
                MediaErrorListener mediaErrorListener2 = (MediaErrorListener)listIterator.next().get();
                if (mediaErrorListener2 != null && mediaErrorListener2 != mediaErrorListener) continue;
                listIterator.remove();
            }
        }
    }

    public static void registerMediaPlayerForDispose(Object object, MediaPlayer mediaPlayer) {
        MediaDisposer.addResourceDisposer(object, mediaPlayer, playerDisposer);
    }

    public List<MediaPlayer> getAllMediaPlayers() {
        ArrayList<MediaPlayer> arrayList = null;
        if (!allMediaPlayers.isEmpty()) {
            arrayList = new ArrayList<MediaPlayer>(allMediaPlayers.keySet());
        }
        return arrayList;
    }

    List<WeakReference<MediaErrorListener>> getMediaErrorListeners() {
        return this.errorListeners;
    }

    private static class NativeMediaPlayerDisposer
    implements MediaDisposer.ResourceDisposer {
        private NativeMediaPlayerDisposer() {
        }

        @Override
        public void disposeResource(Object object) {
            MediaPlayer mediaPlayer = (MediaPlayer)object;
            if (mediaPlayer != null) {
                mediaPlayer.dispose();
            }
        }
    }

    private static class NativeMediaManagerInitializer {
        private static final NativeMediaManager globalInstance = new NativeMediaManager();

        private NativeMediaManagerInitializer() {
        }
    }
}

