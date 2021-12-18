/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.perf;

import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.perf.PerfLogger;
import java.util.logging.Logger;

public final class WCFontPerfLogger
extends WCFont {
    private static final Logger log = Logger.getLogger(WCFontPerfLogger.class.getName());
    private static final PerfLogger logger = PerfLogger.getLogger(log);
    private final WCFont fnt;

    public WCFontPerfLogger(WCFont wCFont) {
        this.fnt = wCFont;
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
    public Object getPlatformFont() {
        return this.fnt.getPlatformFont();
    }

    @Override
    public WCFont deriveFont(float f) {
        logger.resumeCount("DERIVEFONT");
        WCFont wCFont = this.fnt.deriveFont(f);
        logger.suspendCount("DERIVEFONT");
        return wCFont;
    }

    @Override
    public int getOffsetForPosition(String string, float f) {
        logger.resumeCount("GETOFFSETFORPOSITION");
        int n = this.fnt.getOffsetForPosition(string, f);
        logger.suspendCount("GETOFFSETFORPOSITION");
        return n;
    }

    @Override
    public int[] getGlyphCodes(char[] arrc) {
        logger.resumeCount("GETGLYPHCODES");
        int[] arrn = this.fnt.getGlyphCodes(arrc);
        logger.suspendCount("GETGLYPHCODES");
        return arrn;
    }

    @Override
    public float getXHeight() {
        logger.resumeCount("GETXHEIGHT");
        float f = this.fnt.getXHeight();
        logger.suspendCount("GETXHEIGHT");
        return f;
    }

    @Override
    public double getGlyphWidth(int n) {
        logger.resumeCount("GETGLYPHWIDTH");
        double d = this.fnt.getGlyphWidth(n);
        logger.suspendCount("GETGLYPHWIDTH");
        return d;
    }

    @Override
    public double getStringWidth(String string) {
        logger.resumeCount("GETSTRINGLENGTH");
        double d = this.fnt.getStringWidth(string);
        logger.suspendCount("GETSTRINGLENGTH");
        return d;
    }

    @Override
    public double[] getStringBounds(String string, int n, int n2, boolean bl) {
        logger.resumeCount("GETSTRINGBOUNDS");
        double[] arrd = this.fnt.getStringBounds(string, n, n2, bl);
        logger.suspendCount("GETSTRINGBOUNDS");
        return arrd;
    }

    @Override
    public int hashCode() {
        logger.resumeCount("HASH");
        int n = this.fnt.hashCode();
        logger.suspendCount("HASH");
        return n;
    }

    @Override
    public boolean equals(Object object) {
        logger.resumeCount("COMPARE");
        boolean bl = this.fnt.equals(object);
        logger.suspendCount("COMPARE");
        return bl;
    }

    @Override
    public float getAscent() {
        logger.resumeCount("GETASCENT");
        float f = this.fnt.getAscent();
        logger.suspendCount("GETASCENT");
        return f;
    }

    @Override
    public float getDescent() {
        logger.resumeCount("GETDESCENT");
        float f = this.fnt.getDescent();
        logger.suspendCount("GETDESCENT");
        return f;
    }

    @Override
    public float getLineSpacing() {
        logger.resumeCount("GETLINESPACING");
        float f = this.fnt.getLineSpacing();
        logger.suspendCount("GETLINESPACING");
        return f;
    }

    @Override
    public float getLineGap() {
        logger.resumeCount("GETLINEGAP");
        float f = this.fnt.getLineGap();
        logger.suspendCount("GETLINEGAP");
        return f;
    }

    @Override
    public boolean hasUniformLineMetrics() {
        logger.resumeCount("HASUNIFORMLINEMETRICS");
        boolean bl = this.fnt.hasUniformLineMetrics();
        logger.suspendCount("HASUNIFORMLINEMETRICS");
        return bl;
    }
}

