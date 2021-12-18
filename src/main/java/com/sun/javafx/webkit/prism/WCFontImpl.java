/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.text.TextRun;
import com.sun.javafx.webkit.prism.TextUtilities;
import com.sun.prism.GraphicsPipeline;
import com.sun.webkit.graphics.WCFont;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

final class WCFontImpl
extends WCFont {
    private static final Logger log = Logger.getLogger(WCFontImpl.class.getName());
    private static final HashMap<String, String> FONT_MAP = new HashMap();
    private final PGFont font;
    private FontStrike strike;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static WCFont getFont(String string, boolean bl, boolean bl2, float f) {
        FontFactory fontFactory = GraphicsPipeline.getPipeline().getFontFactory();
        Object object = FONT_MAP;
        synchronized (object) {
            if (FONT_MAP.isEmpty()) {
                FONT_MAP.put("serif", "Serif");
                FONT_MAP.put("dialog", "SansSerif");
                FONT_MAP.put("helvetica", "SansSerif");
                FONT_MAP.put("sansserif", "SansSerif");
                FONT_MAP.put("sans-serif", "SansSerif");
                FONT_MAP.put("monospace", "Monospaced");
                FONT_MAP.put("monospaced", "Monospaced");
                for (String string2 : fontFactory.getFontFamilyNames()) {
                    FONT_MAP.put(string2.toLowerCase(), string2);
                }
            }
        }
        object = FONT_MAP.get(string.toLowerCase());
        if (log.isLoggable(Level.FINE)) {
            String[] arrstring = new StringBuilder("WCFontImpl.get(");
            arrstring.append(string).append(", ").append(f);
            if (bl) {
                arrstring.append(", bold");
            }
            if (bl2) {
                arrstring.append(", italic");
            }
            log.fine(arrstring.append(") = ").append((String)object).toString());
        }
        return object != null ? new WCFontImpl(fontFactory.createFont((String)object, bl, bl2, f)) : null;
    }

    WCFontImpl(PGFont pGFont) {
        this.font = pGFont;
    }

    @Override
    public WCFont deriveFont(float f) {
        FontFactory fontFactory = GraphicsPipeline.getPipeline().getFontFactory();
        return new WCFontImpl(fontFactory.deriveFont(this.font, this.font.getFontResource().isBold(), this.font.getFontResource().isItalic(), f));
    }

    @Override
    public int getOffsetForPosition(String string, float f) {
        TextLayout textLayout = TextUtilities.createLayout(string, this.font);
        GlyphList[] arrglyphList = textLayout.getRuns();
        TextRun textRun = (TextRun)arrglyphList[0];
        int n = textRun.getOffsetAtX(f, null);
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("str='%s' (length=%d), x=%.2f => %d", string, string.length(), Float.valueOf(f), n));
        }
        return n;
    }

    private FontStrike getFontStrike() {
        if (this.strike == null) {
            this.strike = this.font.getStrike(BaseTransform.IDENTITY_TRANSFORM, 1);
        }
        return this.strike;
    }

    @Override
    public double getGlyphWidth(int n) {
        return this.getFontStrike().getFontResource().getAdvance(n, this.font.getSize());
    }

    @Override
    public float getXHeight() {
        return this.getFontStrike().getMetrics().getXHeight();
    }

    @Override
    public int[] getGlyphCodes(char[] arrc) {
        int[] arrn = new int[arrc.length];
        CharToGlyphMapper charToGlyphMapper = this.getFontStrike().getFontResource().getGlyphMapper();
        charToGlyphMapper.charsToGlyphs(arrc.length, arrc, arrn);
        return arrn;
    }

    @Override
    public double getStringWidth(String string) {
        double d = TextUtilities.getLayoutWidth(string, this.font);
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("str='%s' (length=%d) => %.1f", string, string.length(), d));
        }
        return d;
    }

    @Override
    public double[] getStringBounds(String string, int n, int n2, boolean bl) {
        float f = TextUtilities.getLayoutWidth(string.substring(0, n), this.font);
        BaseBounds baseBounds = TextUtilities.getLayoutBounds(string.substring(0, n2), this.font);
        double[] arrd = new double[]{f, 0.0, baseBounds.getWidth() - f, baseBounds.getHeight()};
        if (bl) {
            float f2 = TextUtilities.getLayoutWidth(string, this.font);
            arrd[0] = f2 - baseBounds.getWidth();
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("str='%s' (length=%d) [%d, %d], rtl=%b => [%.1f, %.1f + %.1f x %.1f]", string, string.length(), n, n2, bl, arrd[0], arrd[1], arrd[2], arrd[3]));
        }
        return arrd;
    }

    @Override
    public float getAscent() {
        float f = -this.getFontStrike().getMetrics().getAscent();
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "getAscent({0}, {1}) = {2}", new Object[]{this.font.getName(), Float.valueOf(this.font.getSize()), Float.valueOf(f)});
        }
        return f;
    }

    @Override
    public float getDescent() {
        float f = this.getFontStrike().getMetrics().getDescent();
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "getDescent({0}, {1}) = {2}", new Object[]{this.font.getName(), Float.valueOf(this.font.getSize()), Float.valueOf(f)});
        }
        return f;
    }

    @Override
    public float getLineSpacing() {
        float f = this.getFontStrike().getMetrics().getLineHeight();
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "getLineSpacing({0}, {1}) = {2}", new Object[]{this.font.getName(), Float.valueOf(this.font.getSize()), Float.valueOf(f)});
        }
        return f;
    }

    @Override
    public float getLineGap() {
        float f = this.getFontStrike().getMetrics().getLineGap();
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "getLineGap({0}, {1}) = {2}", new Object[]{this.font.getName(), Float.valueOf(this.font.getSize()), Float.valueOf(f)});
        }
        return f;
    }

    @Override
    public boolean hasUniformLineMetrics() {
        return false;
    }

    @Override
    public Object getPlatformFont() {
        return this.font;
    }
}

