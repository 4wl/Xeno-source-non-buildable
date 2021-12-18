/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.graphics.BufferData;
import com.sun.webkit.graphics.RenderMediaControls;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCIcon;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCMediaPlayer;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCRenderQueue;
import com.sun.webkit.graphics.WCTransform;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

public final class GraphicsDecoder {
    public static final int FILLRECT_FFFFI = 0;
    public static final int SETFILLCOLOR = 1;
    public static final int SETSTROKESTYLE = 2;
    public static final int SETSTROKECOLOR = 3;
    public static final int SETSTROKEWIDTH = 4;
    public static final int DRAWPOLYGON = 6;
    public static final int DRAWLINE = 7;
    public static final int DRAWIMAGE = 8;
    public static final int DRAWICON = 9;
    public static final int DRAWPATTERN = 10;
    public static final int TRANSLATE = 11;
    public static final int SAVESTATE = 12;
    public static final int RESTORESTATE = 13;
    public static final int CLIP_PATH = 14;
    public static final int SETCLIP_IIII = 15;
    public static final int DRAWRECT = 16;
    public static final int SETCOMPOSITE = 17;
    public static final int STROKEARC = 18;
    public static final int DRAWELLIPSE = 19;
    public static final int DRAWFOCUSRING = 20;
    public static final int SETALPHA = 21;
    public static final int BEGINTRANSPARENCYLAYER = 22;
    public static final int ENDTRANSPARENCYLAYER = 23;
    public static final int STROKE_PATH = 24;
    public static final int FILL_PATH = 25;
    public static final int GETIMAGE = 26;
    public static final int SCALE = 27;
    public static final int SETSHADOW = 28;
    public static final int DRAWSTRING = 29;
    public static final int DRAWSTRING_FAST = 31;
    public static final int DRAWWIDGET = 33;
    public static final int DRAWSCROLLBAR = 34;
    public static final int CLEARRECT_FFFF = 36;
    public static final int STROKERECT_FFFFF = 37;
    public static final int RENDERMEDIAPLAYER = 38;
    public static final int CONCATTRANSFORM_FFFFFF = 39;
    public static final int COPYREGION = 40;
    public static final int DECODERQ = 41;
    public static final int SET_TRANSFORM = 42;
    public static final int ROTATE = 43;
    public static final int RENDERMEDIACONTROL = 44;
    public static final int RENDERMEDIA_TIMETRACK = 45;
    public static final int RENDERMEDIA_VOLUMETRACK = 46;
    public static final int FILLRECT_FFFF = 47;
    public static final int FILL_ROUNDED_RECT = 48;
    public static final int SET_FILL_GRADIENT = 49;
    public static final int SET_STROKE_GRADIENT = 50;
    public static final int SET_LINE_DASH = 51;
    public static final int SET_LINE_CAP = 52;
    public static final int SET_LINE_JOIN = 53;
    public static final int SET_MITER_LIMIT = 54;
    public static final int SET_TEXT_MODE = 55;
    private static final Logger log = Logger.getLogger(GraphicsDecoder.class.getName());

    static void decode(WCGraphicsManager wCGraphicsManager, WCGraphicsContext wCGraphicsContext, BufferData bufferData) {
        if (wCGraphicsContext == null) {
            return;
        }
        ByteBuffer byteBuffer = bufferData.getBuffer();
        byteBuffer.order(ByteOrder.nativeOrder());
        block53: while (byteBuffer.remaining() > 0) {
            int n = byteBuffer.getInt();
            switch (n) {
                case 47: {
                    wCGraphicsContext.fillRect(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), null);
                    continue block53;
                }
                case 0: {
                    wCGraphicsContext.fillRect(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getInt());
                    continue block53;
                }
                case 48: {
                    wCGraphicsContext.fillRoundedRect(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getInt());
                    continue block53;
                }
                case 36: {
                    wCGraphicsContext.clearRect(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
                    continue block53;
                }
                case 37: {
                    wCGraphicsContext.strokeRect(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
                    continue block53;
                }
                case 1: {
                    wCGraphicsContext.setFillColor(byteBuffer.getInt());
                    continue block53;
                }
                case 55: {
                    wCGraphicsContext.setTextMode(GraphicsDecoder.getBoolean(byteBuffer), GraphicsDecoder.getBoolean(byteBuffer), GraphicsDecoder.getBoolean(byteBuffer));
                    continue block53;
                }
                case 2: {
                    wCGraphicsContext.setStrokeStyle(byteBuffer.getInt());
                    continue block53;
                }
                case 3: {
                    wCGraphicsContext.setStrokeColor(byteBuffer.getInt());
                    continue block53;
                }
                case 4: {
                    wCGraphicsContext.setStrokeWidth(byteBuffer.getFloat());
                    continue block53;
                }
                case 49: {
                    wCGraphicsContext.setFillGradient(GraphicsDecoder.getGradient(wCGraphicsContext, byteBuffer));
                    continue block53;
                }
                case 50: {
                    wCGraphicsContext.setStrokeGradient(GraphicsDecoder.getGradient(wCGraphicsContext, byteBuffer));
                    continue block53;
                }
                case 51: {
                    wCGraphicsContext.setLineDash(byteBuffer.getFloat(), GraphicsDecoder.getFloatArray(byteBuffer));
                    continue block53;
                }
                case 52: {
                    wCGraphicsContext.setLineCap(byteBuffer.getInt());
                    continue block53;
                }
                case 53: {
                    wCGraphicsContext.setLineJoin(byteBuffer.getInt());
                    continue block53;
                }
                case 54: {
                    wCGraphicsContext.setMiterLimit(byteBuffer.getFloat());
                    continue block53;
                }
                case 6: {
                    wCGraphicsContext.drawPolygon(GraphicsDecoder.getPath(wCGraphicsManager, byteBuffer), byteBuffer.getInt() == -1);
                    continue block53;
                }
                case 7: {
                    wCGraphicsContext.drawLine(byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 8: {
                    GraphicsDecoder.drawImage(wCGraphicsContext, wCGraphicsManager.getRef(byteBuffer.getInt()), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
                    continue block53;
                }
                case 9: {
                    wCGraphicsContext.drawIcon((WCIcon)wCGraphicsManager.getRef(byteBuffer.getInt()), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 10: {
                    GraphicsDecoder.drawPattern(wCGraphicsContext, wCGraphicsManager.getRef(byteBuffer.getInt()), GraphicsDecoder.getRectangle(byteBuffer), (WCTransform)wCGraphicsManager.getRef(byteBuffer.getInt()), GraphicsDecoder.getPoint(byteBuffer), GraphicsDecoder.getRectangle(byteBuffer));
                    continue block53;
                }
                case 11: {
                    wCGraphicsContext.translate(byteBuffer.getFloat(), byteBuffer.getFloat());
                    continue block53;
                }
                case 27: {
                    wCGraphicsContext.scale(byteBuffer.getFloat(), byteBuffer.getFloat());
                    continue block53;
                }
                case 12: {
                    wCGraphicsContext.saveState();
                    continue block53;
                }
                case 13: {
                    wCGraphicsContext.restoreState();
                    continue block53;
                }
                case 14: {
                    wCGraphicsContext.setClip(GraphicsDecoder.getPath(wCGraphicsManager, byteBuffer), byteBuffer.getInt() > 0);
                    continue block53;
                }
                case 15: {
                    wCGraphicsContext.setClip(byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 16: {
                    wCGraphicsContext.drawRect(byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 17: {
                    wCGraphicsContext.setComposite(byteBuffer.getInt());
                    continue block53;
                }
                case 18: {
                    wCGraphicsContext.strokeArc(byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 19: {
                    wCGraphicsContext.drawEllipse(byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 20: {
                    wCGraphicsContext.drawFocusRing(byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 21: {
                    wCGraphicsContext.setAlpha(byteBuffer.getFloat());
                    continue block53;
                }
                case 22: {
                    wCGraphicsContext.beginTransparencyLayer(byteBuffer.getFloat());
                    continue block53;
                }
                case 23: {
                    wCGraphicsContext.endTransparencyLayer();
                    continue block53;
                }
                case 24: {
                    wCGraphicsContext.strokePath(GraphicsDecoder.getPath(wCGraphicsManager, byteBuffer));
                    continue block53;
                }
                case 25: {
                    wCGraphicsContext.fillPath(GraphicsDecoder.getPath(wCGraphicsManager, byteBuffer));
                    continue block53;
                }
                case 28: {
                    wCGraphicsContext.setShadow(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getInt());
                    continue block53;
                }
                case 29: {
                    wCGraphicsContext.drawString((WCFont)wCGraphicsManager.getRef(byteBuffer.getInt()), bufferData.getString(byteBuffer.getInt()), byteBuffer.getInt() == -1, byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getFloat(), byteBuffer.getFloat());
                    continue block53;
                }
                case 31: {
                    wCGraphicsContext.drawString((WCFont)wCGraphicsManager.getRef(byteBuffer.getInt()), bufferData.getIntArray(byteBuffer.getInt()), bufferData.getFloatArray(byteBuffer.getInt()), byteBuffer.getFloat(), byteBuffer.getFloat());
                    continue block53;
                }
                case 33: {
                    wCGraphicsContext.drawWidget((RenderTheme)wCGraphicsManager.getRef(byteBuffer.getInt()), wCGraphicsManager.getRef(byteBuffer.getInt()), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 34: {
                    wCGraphicsContext.drawScrollbar((ScrollBarTheme)wCGraphicsManager.getRef(byteBuffer.getInt()), wCGraphicsManager.getRef(byteBuffer.getInt()), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 38: {
                    WCMediaPlayer wCMediaPlayer = (WCMediaPlayer)wCGraphicsManager.getRef(byteBuffer.getInt());
                    wCMediaPlayer.render(wCGraphicsContext, byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 39: {
                    wCGraphicsContext.concatTransform(new WCTransform(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat()));
                    continue block53;
                }
                case 42: {
                    wCGraphicsContext.setTransform(new WCTransform(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat()));
                    continue block53;
                }
                case 40: {
                    WCPageBackBuffer wCPageBackBuffer = (WCPageBackBuffer)wCGraphicsManager.getRef(byteBuffer.getInt());
                    wCPageBackBuffer.copyArea(byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 41: {
                    WCRenderQueue wCRenderQueue = (WCRenderQueue)wCGraphicsManager.getRef(byteBuffer.getInt());
                    wCRenderQueue.decode(wCGraphicsContext.getFontSmoothingType());
                    continue block53;
                }
                case 43: {
                    wCGraphicsContext.rotate(byteBuffer.getFloat());
                    continue block53;
                }
                case 44: {
                    RenderMediaControls.paintControl(wCGraphicsContext, byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 45: {
                    int n2 = byteBuffer.getInt();
                    float[] arrf = new float[n2 * 2];
                    byteBuffer.asFloatBuffer().get(arrf);
                    byteBuffer.position(byteBuffer.position() + n2 * 4 * 2);
                    RenderMediaControls.paintTimeSliderTrack(wCGraphicsContext, byteBuffer.getFloat(), byteBuffer.getFloat(), arrf, byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
                case 46: {
                    RenderMediaControls.paintVolumeTrack(wCGraphicsContext, byteBuffer.getFloat(), byteBuffer.getInt() != 0, byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt());
                    continue block53;
                }
            }
            log.fine("ERROR. Unknown primitive found");
        }
    }

    private static void drawPattern(WCGraphicsContext wCGraphicsContext, Object object, WCRectangle wCRectangle, WCTransform wCTransform, WCPoint wCPoint, WCRectangle wCRectangle2) {
        WCImage wCImage = WCImage.getImage(object);
        if (wCImage != null) {
            try {
                wCGraphicsContext.drawPattern(wCImage, wCRectangle, wCTransform, wCPoint, wCRectangle2);
            }
            catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
            }
        }
    }

    private static void drawImage(WCGraphicsContext wCGraphicsContext, Object object, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        WCImage wCImage = WCImage.getImage(object);
        if (wCImage != null) {
            try {
                wCGraphicsContext.drawImage(wCImage, f, f2, f3, f4, f5, f6, f7, f8);
            }
            catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
            }
        }
    }

    private static boolean getBoolean(ByteBuffer byteBuffer) {
        return 0 != byteBuffer.getInt();
    }

    private static float[] getFloatArray(ByteBuffer byteBuffer) {
        float[] arrf = new float[byteBuffer.getInt()];
        for (int i = 0; i < arrf.length; ++i) {
            arrf[i] = byteBuffer.getFloat();
        }
        return arrf;
    }

    private static WCPath getPath(WCGraphicsManager wCGraphicsManager, ByteBuffer byteBuffer) {
        WCPath wCPath = (WCPath)wCGraphicsManager.getRef(byteBuffer.getInt());
        wCPath.setWindingRule(byteBuffer.getInt());
        return wCPath;
    }

    private static WCPoint getPoint(ByteBuffer byteBuffer) {
        return new WCPoint(byteBuffer.getFloat(), byteBuffer.getFloat());
    }

    private static WCRectangle getRectangle(ByteBuffer byteBuffer) {
        return new WCRectangle(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
    }

    private static WCGradient getGradient(WCGraphicsContext wCGraphicsContext, ByteBuffer byteBuffer) {
        WCPoint wCPoint = GraphicsDecoder.getPoint(byteBuffer);
        WCPoint wCPoint2 = GraphicsDecoder.getPoint(byteBuffer);
        WCGradient wCGradient = GraphicsDecoder.getBoolean(byteBuffer) ? wCGraphicsContext.createRadialGradient(wCPoint, byteBuffer.getFloat(), wCPoint2, byteBuffer.getFloat()) : wCGraphicsContext.createLinearGradient(wCPoint, wCPoint2);
        boolean bl = GraphicsDecoder.getBoolean(byteBuffer);
        int n = byteBuffer.getInt();
        if (wCGradient != null) {
            wCGradient.setProportional(bl);
            wCGradient.setSpreadMethod(n);
        }
        int n2 = byteBuffer.getInt();
        for (int i = 0; i < n2; ++i) {
            int n3 = byteBuffer.getInt();
            float f = byteBuffer.getFloat();
            if (wCGradient == null) continue;
            wCGradient.addStop(n3, f);
        }
        return wCGradient;
    }
}

