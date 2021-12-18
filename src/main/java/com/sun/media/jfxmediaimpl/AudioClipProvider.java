/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.AudioClip;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.NativeAudioClip;
import com.sun.media.jfxmediaimpl.NativeMediaAudioClip;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AudioClipProvider {
    private static AudioClipProvider primaDonna;
    private boolean useNative = false;

    public static synchronized AudioClipProvider getProvider() {
        if (null == primaDonna) {
            primaDonna = new AudioClipProvider();
        }
        return primaDonna;
    }

    private AudioClipProvider() {
        try {
            this.useNative = NativeAudioClip.init();
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            Logger.logMsg(1, "JavaFX AudioClip native methods not linked, using NativeMedia implementation");
        }
        catch (Exception exception) {
            Logger.logMsg(4, "Exception while loading native AudioClip library: " + exception);
        }
    }

    public AudioClip load(URI uRI) throws URISyntaxException, FileNotFoundException, IOException {
        if (this.useNative) {
            return NativeAudioClip.load(uRI);
        }
        return NativeMediaAudioClip.load(uRI);
    }

    public AudioClip create(byte[] arrby, int n, int n2, int n3, int n4, int n5) throws IllegalArgumentException {
        if (this.useNative) {
            return NativeAudioClip.create(arrby, n, n2, n3, n4, n5);
        }
        return NativeMediaAudioClip.create(arrby, n, n2, n3, n4, n5);
    }

    public void stopAllClips() {
        if (this.useNative) {
            NativeAudioClip.stopAllClips();
        }
        NativeMediaAudioClip.stopAllClips();
    }
}

