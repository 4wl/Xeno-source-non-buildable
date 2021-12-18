/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.t2k;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.CompositeStrike;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.t2k.T2KFontFile;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.text.ScriptMapper;
import com.sun.javafx.text.TextRun;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.Bidi;

class ICUGlyphLayout
extends GlyphLayout {
    private float[] _mat = new float[4];
    private long textPtr;
    private static final int CANONICAL_MASK = 448;

    private static native void initIDs();

    private static native long createTextPtr(char[] var0);

    private static native void freeTextPtr(long var0);

    private static native void nativeLayout(T2KFontFile var0, FontStrike var1, float var2, float[] var3, int var4, long var5, int var7, int var8, int var9, int var10, int var11, int var12, int var13, TextRun var14, int var15, int var16, long var17);

    @Override
    public int breakRuns(PrismTextLayout prismTextLayout, char[] arrc, int n) {
        int n2;
        int n3;
        int n4 = arrc.length;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = true;
        boolean bl6 = true;
        if ((n & 2) != 0) {
            bl5 = (n & 0x10) != 0;
            bl6 = (n & 8) != 0;
        }
        TextRun textRun = null;
        Bidi bidi = null;
        byte by = 0;
        int n9 = n4;
        int n10 = 0;
        int n11 = 0;
        TextSpan textSpan = null;
        int n12 = n4;
        PGFont pGFont = null;
        TextSpan[] arrtextSpan = prismTextLayout.getTextSpans();
        if (arrtextSpan != null) {
            if (arrtextSpan.length > 0) {
                textSpan = arrtextSpan[n11];
                n12 = textSpan.getText().length();
                pGFont = (PGFont)textSpan.getFont();
                if (pGFont == null) {
                    n |= 0x20;
                }
            }
        } else {
            pGFont = prismTextLayout.getFont();
        }
        CharToGlyphMapper charToGlyphMapper = null;
        if (pGFont != null) {
            FontResource fontResource = pGFont.getFontResource();
            n3 = pGFont.getFeatures();
            boolean bl7 = bl4 = (n3 & (n2 = fontResource.getFeatures())) != 0;
            if (bl5 && fontResource instanceof CompositeFontResource) {
                charToGlyphMapper = fontResource.getGlyphMapper();
            }
        }
        if (bl6 && n4 > 0) {
            int n13 = prismTextLayout.getDirection();
            bidi = new Bidi(arrc, 0, null, 0, n4, n13);
            by = (byte)bidi.getLevelAt(bidi.getRunStart(n10));
            n9 = bidi.getRunLimit(n10);
            if ((by & 1) != 0) {
                n |= 8;
            }
        }
        int n14 = 0;
        n3 = 0;
        while (n3 < n4) {
            int n15 = n2 = arrc[n3];
            boolean bl8 = n2 == 9 || n2 == 10 || n2 == 13;
            boolean bl9 = n3 >= n12;
            boolean bl10 = n3 >= n9;
            boolean bl11 = false;
            boolean bl12 = false;
            boolean bl13 = false;
            if (bl5 && !bl8 && !bl9 && !bl10) {
                if (Character.isHighSurrogate((char)n2) && n3 + 1 < n12 && Character.isLowSurrogate(arrc[n3 + 1])) {
                    n15 = Character.toCodePoint((char)n2, arrc[++n3]);
                }
                if (bl) {
                    int n16;
                    if (charToGlyphMapper != null && !Character.isWhitespace(n15) && n7 != (n8 = (n16 = charToGlyphMapper.charToGlyph(n15)) != 0 ? n16 >>> 24 : -1)) {
                        bl11 = true;
                    }
                    n6 = ScriptMapper.getScript(n15);
                    if (n5 > 1 && n6 > 1 && n6 != n5) {
                        bl12 = true;
                    }
                    if (!bl3 && ((n16 = 1 << Character.getType(n15)) & 0x1C0) != 0) {
                        bl3 = true;
                    }
                } else {
                    boolean bl14 = bl2 = bl4 || ScriptMapper.isComplexCharCode(n15);
                    if (bl2) {
                        bl13 = true;
                    }
                }
            }
            if (bl8 || bl9 || bl10 || bl11 || bl12 || bl13) {
                int n17;
                if (n3 != n14) {
                    textRun = new TextRun(n14, n3 - n14, by, bl, n5, textSpan, n7, bl3);
                    prismTextLayout.addTextRun(textRun);
                    if (bl) {
                        n |= 0x10;
                    }
                    bl3 = false;
                    n14 = n3;
                }
                if (bl8) {
                    if (n2 == 13 && ++n3 < n12 && arrc[n3] == '\n') {
                        ++n3;
                    }
                    textRun = new TextRun(n14, n3 - n14, by, false, 0, textSpan, 0, false);
                    if (n2 == 9) {
                        textRun.setTab();
                        n |= 4;
                    } else {
                        textRun.setLinebreak();
                    }
                    prismTextLayout.addTextRun(textRun);
                    n14 = n3;
                    if (n3 == n4) break;
                    bl9 = n3 >= n12;
                    bl10 = n3 >= n9;
                    bl3 = false;
                }
                if (bl9) {
                    textSpan = arrtextSpan[++n11];
                    n12 += textSpan.getText().length();
                    pGFont = (PGFont)textSpan.getFont();
                    charToGlyphMapper = null;
                    if (pGFont == null) {
                        n |= 0x20;
                    } else {
                        int n18;
                        FontResource fontResource = pGFont.getFontResource();
                        n17 = pGFont.getFeatures();
                        boolean bl15 = bl4 = (n17 & (n18 = fontResource.getFeatures())) != 0;
                        if (bl5 && fontResource instanceof CompositeFontResource) {
                            charToGlyphMapper = fontResource.getGlyphMapper();
                        }
                    }
                }
                if (bl10) {
                    by = (byte)bidi.getLevelAt(bidi.getRunStart(++n10));
                    n9 = bidi.getRunLimit(n10);
                    if ((by & 1) != 0) {
                        n |= 8;
                    }
                }
                if (bl5) {
                    if (bl8 || bl9 || bl10) {
                        n2 = arrc[n3];
                        if (Character.isHighSurrogate((char)n2) && n3 + 1 < n12 && Character.isLowSurrogate(arrc[n3 + 1])) {
                            n15 = Character.toCodePoint((char)n2, arrc[++n3]);
                        }
                        bl13 = true;
                        boolean bl16 = bl2 = bl4 || ScriptMapper.isComplexCharCode(n15);
                    }
                    if (bl12) {
                        boolean bl17 = bl2 = bl4 || ScriptMapper.isComplexCharCode(n15);
                        if (bl2) {
                            n5 = n6;
                        } else {
                            bl13 = true;
                        }
                    }
                    if (bl11) {
                        n7 = n8;
                    }
                    if (bl13) {
                        if (bl2) {
                            bl = true;
                            int n19 = 1 << Character.getType(n15);
                            bl3 = (n19 & 0x1C0) != 0;
                            n5 = ScriptMapper.getScript(n15);
                            n7 = 0;
                            if (charToGlyphMapper != null && !Character.isWhitespace(n15)) {
                                n17 = charToGlyphMapper.charToGlyph(n15);
                                n7 = n17 != 0 ? n17 >>> 24 : -1;
                            }
                        } else {
                            bl = false;
                            bl3 = false;
                            n5 = 0;
                            n7 = 0;
                        }
                    }
                }
            }
            if (bl8) continue;
            ++n3;
        }
        if (n14 < n4) {
            prismTextLayout.addTextRun(new TextRun(n14, n4 - n14, by, bl, n5, textSpan, n7, bl3));
            if (bl) {
                n |= 0x10;
            }
        } else if (textRun == null || textRun.isLinebreak()) {
            textRun = new TextRun(n14, 0, 0, false, 0, textSpan, 0, false);
            prismTextLayout.addTextRun(textRun);
        }
        if (bidi != null && !bidi.baseIsLeftToRight()) {
            n |= 0x100;
        }
        return n |= 2;
    }

    @Override
    public void layout(TextRun textRun, PGFont pGFont, FontStrike fontStrike, char[] arrc) {
        int n = textRun.getLength();
        T2KFontFile t2KFontFile = null;
        if (fontStrike instanceof CompositeStrike) {
            int n2 = textRun.getSlot();
            if (n2 != -1) {
                CompositeStrike compositeStrike = (CompositeStrike)fontStrike;
                t2KFontFile = (T2KFontFile)compositeStrike.getStrikeSlot(n2).getFontResource();
            }
        } else if (fontStrike.getFontResource() instanceof T2KFontFile) {
            t2KFontFile = (T2KFontFile)fontStrike.getFontResource();
        }
        if (t2KFontFile != null) {
            FontStrike fontStrike2;
            if (this.textPtr == 0L) {
                this.textPtr = ICUGlyphLayout.createTextPtr(arrc);
            }
            BaseTransform baseTransform = fontStrike.getTransform();
            float f = fontStrike.getSize();
            this._mat[0] = (float)baseTransform.getMxx() * f;
            this._mat[1] = (float)baseTransform.getMxy() * f;
            this._mat[2] = (float)baseTransform.getMyx() * f;
            this._mat[3] = (float)baseTransform.getMyy() * f;
            int n3 = -1;
            boolean bl = textRun.isLeftToRight();
            int n4 = bl ? 1 : 2;
            int n5 = pGFont.getFeatures();
            int n6 = t2KFontFile.getFeatures();
            int n7 = n5 & n6;
            int n8 = textRun.getScript();
            if (n7 != 0 && n8 == 0) {
                n8 = 25;
            }
            if (textRun.isCanonical()) {
                n7 |= 0x40000000;
            }
            if ((fontStrike2 = t2KFontFile.getStrike(f, baseTransform)).getAAMode() == 1) {
                n4 |= 0x10;
            }
            if (textRun.isNoLinkBefore()) {
                n4 |= 4;
            }
            if (textRun.isNoLinkAfter()) {
                n4 |= 8;
            }
            ICUGlyphLayout.nativeLayout(t2KFontFile, fontStrike2, f, this._mat, textRun.getSlot() << 24, this.textPtr, textRun.getStart(), n, arrc.length, n8, n3, n4, n7, textRun, t2KFontFile.getUnitsPerEm(), t2KFontFile.getNumHMetrics(), t2KFontFile.getLayoutTableCache());
        } else {
            float f = 0.0f;
            float f2 = 0.0f;
            int[] arrn = new int[n];
            float[] arrf = new float[n * 2 + 2];
            Glyph glyph = fontStrike.getGlyph(0);
            float f3 = glyph.getPixelXAdvance();
            float f4 = glyph.getPixelYAdvance();
            for (int i = 0; i < n; ++i) {
                arrf[i * 2] = f;
                arrf[i * 2 + 1] = f2;
                f += f3;
                f2 += f4;
            }
            arrf[n * 2] = f;
            arrf[n * 2 + 1] = f2;
            textRun.shape(n, arrn, arrf, null);
        }
    }

    @Override
    public void dispose() {
        if (this.textPtr != 0L) {
            ICUGlyphLayout.freeTextPtr(this.textPtr);
            this.textPtr = 0L;
        }
        super.dispose();
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>(){

            @Override
            public Void run() {
                NativeLibLoader.loadLibrary("javafx_font_t2k");
                return null;
            }
        });
        ICUGlyphLayout.initIDs();
    }
}

