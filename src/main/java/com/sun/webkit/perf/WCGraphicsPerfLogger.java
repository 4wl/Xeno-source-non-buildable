/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.perf;

import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCIcon;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCTransform;
import com.sun.webkit.perf.PerfLogger;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public final class WCGraphicsPerfLogger
extends WCGraphicsContext {
    private static final Logger log = Logger.getLogger(WCGraphicsPerfLogger.class.getName());
    private static final PerfLogger logger = PerfLogger.getLogger(log);
    private final WCGraphicsContext gc;

    public WCGraphicsPerfLogger(WCGraphicsContext wCGraphicsContext) {
        this.gc = wCGraphicsContext;
    }

    public static synchronized boolean isEnabled() {
        return logger.isEnabled();
    }

    public static void log() {
        logger.log();
    }

    public static void reset() {
        logger.reset();
    }

    @Override
    public Object getPlatformGraphics() {
        return this.gc.getPlatformGraphics();
    }

    @Override
    public void drawString(WCFont wCFont, int[] arrn, float[] arrf, float f, float f2) {
        logger.resumeCount("DRAWSTRING_GV");
        this.gc.drawString(wCFont, arrn, arrf, f, f2);
        logger.suspendCount("DRAWSTRING_GV");
    }

    @Override
    public void strokeRect(float f, float f2, float f3, float f4, float f5) {
        logger.resumeCount("STROKERECT_FFFFF");
        this.gc.strokeRect(f, f2, f3, f4, f5);
        logger.suspendCount("STROKERECT_FFFFF");
    }

    @Override
    public void fillRect(float f, float f2, float f3, float f4, Integer n) {
        logger.resumeCount("FILLRECT_FFFFI");
        this.gc.fillRect(f, f2, f3, f4, n);
        logger.suspendCount("FILLRECT_FFFFI");
    }

    @Override
    public void fillRoundedRect(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, int n) {
        logger.resumeCount("FILL_ROUNDED_RECT");
        this.gc.fillRoundedRect(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, n);
        logger.suspendCount("FILL_ROUNDED_RECT");
    }

    @Override
    public void clearRect(float f, float f2, float f3, float f4) {
        logger.resumeCount("CLEARRECT");
        this.gc.clearRect(f, f2, f3, f4);
        logger.suspendCount("CLEARRECT");
    }

    @Override
    public void setFillColor(int n) {
        logger.resumeCount("SETFILLCOLOR");
        this.gc.setFillColor(n);
        logger.suspendCount("SETFILLCOLOR");
    }

    @Override
    public void setFillGradient(WCGradient wCGradient) {
        logger.resumeCount("SET_FILL_GRADIENT");
        this.gc.setFillGradient(wCGradient);
        logger.suspendCount("SET_FILL_GRADIENT");
    }

    @Override
    public void setTextMode(boolean bl, boolean bl2, boolean bl3) {
        logger.resumeCount("SET_TEXT_MODE");
        this.gc.setTextMode(bl, bl2, bl3);
        logger.suspendCount("SET_TEXT_MODE");
    }

    @Override
    public void setFontSmoothingType(int n) {
        logger.resumeCount("SET_FONT_SMOOTHING_TYPE");
        this.gc.setFontSmoothingType(n);
        logger.suspendCount("SET_FONT_SMOOTHING_TYPE");
    }

    @Override
    public int getFontSmoothingType() {
        logger.resumeCount("GET_FONT_SMOOTHING_TYPE");
        int n = this.gc.getFontSmoothingType();
        logger.suspendCount("GET_FONT_SMOOTHING_TYPE");
        return n;
    }

    @Override
    public void setStrokeStyle(int n) {
        logger.resumeCount("SETSTROKESTYLE");
        this.gc.setStrokeStyle(n);
        logger.suspendCount("SETSTROKESTYLE");
    }

    @Override
    public void setStrokeColor(int n) {
        logger.resumeCount("SETSTROKECOLOR");
        this.gc.setStrokeColor(n);
        logger.suspendCount("SETSTROKECOLOR");
    }

    @Override
    public void setStrokeWidth(float f) {
        logger.resumeCount("SETSTROKEWIDTH");
        this.gc.setStrokeWidth(f);
        logger.suspendCount("SETSTROKEWIDTH");
    }

    @Override
    public void setStrokeGradient(WCGradient wCGradient) {
        logger.resumeCount("SET_STROKE_GRADIENT");
        this.gc.setStrokeGradient(wCGradient);
        logger.suspendCount("SET_STROKE_GRADIENT");
    }

    @Override
    public void setLineDash(float f, float ... arrf) {
        logger.resumeCount("SET_LINE_DASH");
        this.gc.setLineDash(f, arrf);
        logger.suspendCount("SET_LINE_DASH");
    }

    @Override
    public void setLineCap(int n) {
        logger.resumeCount("SET_LINE_CAP");
        this.gc.setLineCap(n);
        logger.suspendCount("SET_LINE_CAP");
    }

    @Override
    public void setLineJoin(int n) {
        logger.resumeCount("SET_LINE_JOIN");
        this.gc.setLineJoin(n);
        logger.suspendCount("SET_LINE_JOIN");
    }

    @Override
    public void setMiterLimit(float f) {
        logger.resumeCount("SET_MITER_LIMIT");
        this.gc.setMiterLimit(f);
        logger.suspendCount("SET_MITER_LIMIT");
    }

    @Override
    public void setShadow(float f, float f2, float f3, int n) {
        logger.resumeCount("SETSHADOW");
        this.gc.setShadow(f, f2, f3, n);
        logger.suspendCount("SETSHADOW");
    }

    @Override
    public void drawPolygon(WCPath wCPath, boolean bl) {
        logger.resumeCount("DRAWPOLYGON");
        this.gc.drawPolygon(wCPath, bl);
        logger.suspendCount("DRAWPOLYGON");
    }

    @Override
    public void drawLine(int n, int n2, int n3, int n4) {
        logger.resumeCount("DRAWLINE");
        this.gc.drawLine(n, n2, n3, n4);
        logger.suspendCount("DRAWLINE");
    }

    @Override
    public void drawImage(WCImage wCImage, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        logger.resumeCount("DRAWIMAGE");
        this.gc.drawImage(wCImage, f, f2, f3, f4, f5, f6, f7, f8);
        logger.suspendCount("DRAWIMAGE");
    }

    @Override
    public void drawIcon(WCIcon wCIcon, int n, int n2) {
        logger.resumeCount("DRAWICON");
        this.gc.drawIcon(wCIcon, n, n2);
        logger.suspendCount("DRAWICON");
    }

    @Override
    public void drawPattern(WCImage wCImage, WCRectangle wCRectangle, WCTransform wCTransform, WCPoint wCPoint, WCRectangle wCRectangle2) {
        logger.resumeCount("DRAWPATTERN");
        this.gc.drawPattern(wCImage, wCRectangle, wCTransform, wCPoint, wCRectangle2);
        logger.suspendCount("DRAWPATTERN");
    }

    @Override
    public void translate(float f, float f2) {
        logger.resumeCount("TRANSLATE");
        this.gc.translate(f, f2);
        logger.suspendCount("TRANSLATE");
    }

    @Override
    public void scale(float f, float f2) {
        logger.resumeCount("SCALE");
        this.gc.scale(f, f2);
        logger.suspendCount("SCALE");
    }

    @Override
    public void rotate(float f) {
        logger.resumeCount("ROTATE");
        this.gc.rotate(f);
        logger.suspendCount("ROTATE");
    }

    @Override
    public void saveState() {
        logger.resumeCount("SAVESTATE");
        this.gc.saveState();
        logger.suspendCount("SAVESTATE");
    }

    @Override
    public void restoreState() {
        logger.resumeCount("RESTORESTATE");
        this.gc.restoreState();
        logger.suspendCount("RESTORESTATE");
    }

    @Override
    public void setClip(WCPath wCPath, boolean bl) {
        logger.resumeCount("CLIP_PATH");
        this.gc.setClip(wCPath, bl);
        logger.suspendCount("CLIP_PATH");
    }

    @Override
    public void setClip(WCRectangle wCRectangle) {
        logger.resumeCount("SETCLIP_R");
        this.gc.setClip(wCRectangle);
        logger.suspendCount("SETCLIP_R");
    }

    @Override
    public void setClip(int n, int n2, int n3, int n4) {
        logger.resumeCount("SETCLIP_IIII");
        this.gc.setClip(n, n2, n3, n4);
        logger.suspendCount("SETCLIP_IIII");
    }

    @Override
    public WCRectangle getClip() {
        logger.resumeCount("SETCLIP_IIII");
        WCRectangle wCRectangle = this.gc.getClip();
        logger.suspendCount("SETCLIP_IIII");
        return wCRectangle;
    }

    @Override
    public void drawRect(int n, int n2, int n3, int n4) {
        logger.resumeCount("DRAWRECT");
        this.gc.drawRect(n, n2, n3, n4);
        logger.suspendCount("DRAWRECT");
    }

    @Override
    public void setComposite(int n) {
        logger.resumeCount("SETCOMPOSITE");
        this.gc.setComposite(n);
        logger.suspendCount("SETCOMPOSITE");
    }

    @Override
    public void strokeArc(int n, int n2, int n3, int n4, int n5, int n6) {
        logger.resumeCount("STROKEARC");
        this.gc.strokeArc(n, n2, n3, n4, n5, n6);
        logger.suspendCount("STROKEARC");
    }

    @Override
    public void drawEllipse(int n, int n2, int n3, int n4) {
        logger.resumeCount("DRAWELLIPSE");
        this.gc.drawEllipse(n, n2, n3, n4);
        logger.suspendCount("DRAWELLIPSE");
    }

    @Override
    public void drawFocusRing(int n, int n2, int n3, int n4, int n5) {
        logger.resumeCount("DRAWFOCUSRING");
        this.gc.drawFocusRing(n, n2, n3, n4, n5);
        logger.suspendCount("DRAWFOCUSRING");
    }

    @Override
    public void setAlpha(float f) {
        logger.resumeCount("SETALPHA");
        this.gc.setAlpha(f);
        logger.suspendCount("SETALPHA");
    }

    @Override
    public float getAlpha() {
        logger.resumeCount("GETALPHA");
        float f = this.gc.getAlpha();
        logger.suspendCount("GETALPHA");
        return f;
    }

    @Override
    public void beginTransparencyLayer(float f) {
        logger.resumeCount("BEGINTRANSPARENCYLAYER");
        this.gc.beginTransparencyLayer(f);
        logger.suspendCount("BEGINTRANSPARENCYLAYER");
    }

    @Override
    public void endTransparencyLayer() {
        logger.resumeCount("ENDTRANSPARENCYLAYER");
        this.gc.endTransparencyLayer();
        logger.suspendCount("ENDTRANSPARENCYLAYER");
    }

    @Override
    public void drawString(WCFont wCFont, String string, boolean bl, int n, int n2, float f, float f2) {
        logger.resumeCount("DRAWSTRING");
        this.gc.drawString(wCFont, string, bl, n, n2, f, f2);
        logger.suspendCount("DRAWSTRING");
    }

    @Override
    public void strokePath(WCPath wCPath) {
        logger.resumeCount("STROKE_PATH");
        this.gc.strokePath(wCPath);
        logger.suspendCount("STROKE_PATH");
    }

    @Override
    public void fillPath(WCPath wCPath) {
        logger.resumeCount("FILL_PATH");
        this.gc.fillPath(wCPath);
        logger.suspendCount("FILL_PATH");
    }

    @Override
    public WCImage getImage() {
        logger.resumeCount("GETIMAGE");
        WCImage wCImage = this.gc.getImage();
        logger.suspendCount("GETIMAGE");
        return wCImage;
    }

    @Override
    public void drawWidget(RenderTheme renderTheme, Ref ref, int n, int n2) {
        logger.resumeCount("DRAWWIDGET");
        this.gc.drawWidget(renderTheme, ref, n, n2);
        logger.suspendCount("DRAWWIDGET");
    }

    @Override
    public void drawScrollbar(ScrollBarTheme scrollBarTheme, Ref ref, int n, int n2, int n3, int n4) {
        logger.resumeCount("DRAWSCROLLBAR");
        this.gc.drawScrollbar(scrollBarTheme, ref, n, n2, n3, n4);
        logger.suspendCount("DRAWSCROLLBAR");
    }

    @Override
    public void dispose() {
        logger.resumeCount("DISPOSE");
        this.gc.dispose();
        logger.suspendCount("DISPOSE");
    }

    @Override
    public void flush() {
        logger.resumeCount("FLUSH");
        this.gc.flush();
        logger.suspendCount("FLUSH");
    }

    @Override
    public void setTransform(WCTransform wCTransform) {
        logger.resumeCount("SETTRANSFORM");
        this.gc.setTransform(wCTransform);
        logger.suspendCount("SETTRANSFORM");
    }

    @Override
    public WCTransform getTransform() {
        logger.resumeCount("GETTRANSFORM");
        WCTransform wCTransform = this.gc.getTransform();
        logger.suspendCount("GETTRANSFORM");
        return wCTransform;
    }

    @Override
    public void concatTransform(WCTransform wCTransform) {
        logger.resumeCount("CONCATTRANSFORM");
        this.gc.concatTransform(wCTransform);
        logger.suspendCount("CONCATTRANSFORM");
    }

    @Override
    public void drawBitmapImage(ByteBuffer byteBuffer, int n, int n2, int n3, int n4) {
        logger.resumeCount("DRAWBITMAPIMAGE");
        this.gc.drawBitmapImage(byteBuffer, n, n2, n3, n4);
        logger.suspendCount("DRAWBITMAPIMAGE");
    }

    @Override
    public WCGradient createLinearGradient(WCPoint wCPoint, WCPoint wCPoint2) {
        logger.resumeCount("CREATE_LINEAR_GRADIENT");
        WCGradient wCGradient = this.gc.createLinearGradient(wCPoint, wCPoint2);
        logger.suspendCount("CREATE_LINEAR_GRADIENT");
        return wCGradient;
    }

    @Override
    public WCGradient createRadialGradient(WCPoint wCPoint, float f, WCPoint wCPoint2, float f2) {
        logger.resumeCount("CREATE_RADIAL_GRADIENT");
        WCGradient wCGradient = this.gc.createRadialGradient(wCPoint, f, wCPoint2, f2);
        logger.suspendCount("CREATE_RADIAL_GRADIENT");
        return wCGradient;
    }
}

