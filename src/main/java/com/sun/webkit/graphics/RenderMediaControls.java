/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCImageDecoder;
import com.sun.webkit.graphics.WCImageFrame;
import java.util.HashMap;
import java.util.Map;

final class RenderMediaControls {
    private static final int PLAY_BUTTON = 1;
    private static final int PAUSE_BUTTON = 2;
    private static final int DISABLED_PLAY_BUTTON = 3;
    private static final int MUTE_BUTTON = 4;
    private static final int UNMUTE_BUTTON = 5;
    private static final int DISABLED_MUTE_BUTTON = 6;
    private static final int TIME_SLIDER_TRACK = 9;
    private static final int TIME_SLIDER_THUMB = 10;
    private static final int VOLUME_CONTAINER = 11;
    private static final int VOLUME_TRACK = 12;
    private static final int VOLUME_THUMB = 13;
    private static final int TimeSliderTrackUnbufferedColor = RenderMediaControls.rgba(236, 135, 125);
    private static final int TimeSliderTrackBufferedColor = RenderMediaControls.rgba(249, 26, 2);
    private static final int TimeSliderTrackThickness = 3;
    private static final int VolumeTrackColor = RenderMediaControls.rgba(208, 208, 208, 128);
    private static final int VolumeTrackThickness = 1;
    private static final int SLIDER_TYPE_TIME = 0;
    private static final int SLIDER_TYPE_VOLUME = 1;
    private static final Map<String, WCImage> controlImages = new HashMap<String, WCImage>();
    private static final boolean log = false;

    private static String getControlName(int n) {
        switch (n) {
            case 1: {
                return "PLAY_BUTTON";
            }
            case 2: {
                return "PAUSE_BUTTON";
            }
            case 3: {
                return "DISABLED_PLAY_BUTTON";
            }
            case 4: {
                return "MUTE_BUTTON";
            }
            case 5: {
                return "UNMUTE_BUTTON";
            }
            case 6: {
                return "DISABLED_MUTE_BUTTON";
            }
            case 9: {
                return "TIME_SLIDER_TRACK";
            }
            case 10: {
                return "TIME_SLIDER_THUMB";
            }
            case 11: {
                return "VOLUME_CONTAINER";
            }
            case 12: {
                return "VOLUME_TRACK";
            }
            case 13: {
                return "VOLUME_THUMB";
            }
        }
        return "{UNKNOWN CONTROL " + n + "}";
    }

    private RenderMediaControls() {
    }

    static void paintControl(WCGraphicsContext wCGraphicsContext, int n, int n2, int n3, int n4, int n5) {
        switch (n) {
            case 1: {
                RenderMediaControls.paintControlImage("mediaPlay", wCGraphicsContext, n2, n3, n4, n5);
                break;
            }
            case 2: {
                RenderMediaControls.paintControlImage("mediaPause", wCGraphicsContext, n2, n3, n4, n5);
                break;
            }
            case 3: {
                RenderMediaControls.paintControlImage("mediaPlayDisabled", wCGraphicsContext, n2, n3, n4, n5);
                break;
            }
            case 4: {
                RenderMediaControls.paintControlImage("mediaMute", wCGraphicsContext, n2, n3, n4, n5);
                break;
            }
            case 5: {
                RenderMediaControls.paintControlImage("mediaUnmute", wCGraphicsContext, n2, n3, n4, n5);
                break;
            }
            case 6: {
                RenderMediaControls.paintControlImage("mediaMuteDisabled", wCGraphicsContext, n2, n3, n4, n5);
                break;
            }
            case 10: {
                RenderMediaControls.paintControlImage("mediaTimeThumb", wCGraphicsContext, n2, n3, n4, n5);
                break;
            }
            case 11: {
                break;
            }
            case 13: {
                RenderMediaControls.paintControlImage("mediaVolumeThumb", wCGraphicsContext, n2, n3, n4, n5);
                break;
            }
        }
    }

    static void paintTimeSliderTrack(WCGraphicsContext wCGraphicsContext, float f, float f2, float[] arrf, int n, int n2, int n3, int n4) {
        n2 += (n4 - 3) / 2;
        n4 = 3;
        int n5 = RenderMediaControls.fwkGetSliderThumbSize(0) >> 16 & 0xFFFF;
        n3 -= n5;
        n += n5 / 2;
        if (!(f < 0.0f)) {
            float f3 = 1.0f / f * (float)n3;
            float f4 = 0.0f;
            for (int i = 0; i < arrf.length; i += 2) {
                wCGraphicsContext.fillRect((float)n + f3 * f4, n2, f3 * (arrf[i] - f4), n4, TimeSliderTrackUnbufferedColor);
                wCGraphicsContext.fillRect((float)n + f3 * arrf[i], n2, f3 * (arrf[i + 1] - arrf[i]), n4, TimeSliderTrackBufferedColor);
                f4 = arrf[i + 1];
            }
            if (f4 < f) {
                wCGraphicsContext.fillRect((float)n + f3 * f4, n2, f3 * (f - f4), n4, TimeSliderTrackUnbufferedColor);
            }
        }
    }

    static void paintVolumeTrack(WCGraphicsContext wCGraphicsContext, float f, boolean bl, int n, int n2, int n3, int n4) {
        n += (n3 + 1 - 1) / 2;
        n3 = 1;
        int n5 = RenderMediaControls.fwkGetSliderThumbSize(0) & 0xFFFF;
        wCGraphicsContext.fillRect(n, n2 += n5 / 2, n3, n4 -= n5, VolumeTrackColor);
    }

    private static int fwkGetSliderThumbSize(int n) {
        WCImage wCImage = null;
        switch (n) {
            case 0: {
                wCImage = RenderMediaControls.getControlImage("mediaTimeThumb");
                break;
            }
            case 1: {
                wCImage = RenderMediaControls.getControlImage("mediaVolumeThumb");
            }
        }
        if (wCImage != null) {
            return wCImage.getWidth() << 16 | wCImage.getHeight();
        }
        return 0;
    }

    private static WCImage getControlImage(String string) {
        WCImage wCImage = controlImages.get(string);
        if (wCImage == null) {
            WCImageDecoder wCImageDecoder = WCGraphicsManager.getGraphicsManager().getImageDecoder();
            wCImageDecoder.loadFromResource(string);
            WCImageFrame wCImageFrame = wCImageDecoder.getFrame(0, null);
            if (wCImageFrame != null) {
                wCImage = wCImageFrame.getFrame();
                controlImages.put(string, wCImage);
            }
        }
        return wCImage;
    }

    private static void paintControlImage(String string, WCGraphicsContext wCGraphicsContext, int n, int n2, int n3, int n4) {
        WCImage wCImage = RenderMediaControls.getControlImage(string);
        if (wCImage != null) {
            n += (n3 - wCImage.getWidth()) / 2;
            n3 = wCImage.getWidth();
            n2 += (n4 - wCImage.getHeight()) / 2;
            n4 = wCImage.getHeight();
            wCGraphicsContext.drawImage(wCImage, n, n2, n3, n4, 0.0f, 0.0f, wCImage.getWidth(), wCImage.getHeight());
        }
    }

    private static int rgba(int n, int n2, int n3, int n4) {
        return (n4 & 0xFF) << 24 | (n & 0xFF) << 16 | (n2 & 0xFF) << 8 | n3 & 0xFF;
    }

    private static int rgba(int n, int n2, int n3) {
        return RenderMediaControls.rgba(n, n2, n3, 255);
    }

    private static void log(String string) {
        System.out.println(string);
        System.out.flush();
    }
}

