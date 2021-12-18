/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.t2k;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.t2k.T2KFontFile;
import com.sun.javafx.font.t2k.T2KGlyph;
import com.sun.javafx.font.t2k.T2KStrikeDisposer;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import java.util.concurrent.ConcurrentHashMap;

class T2KFontStrike
extends PrismFontStrike<T2KFontFile> {
    private long pScalerContext = 0L;
    ConcurrentHashMap<Integer, Point2D> glyphPointMap;
    Affine2D invTx = null;
    boolean gdiLCDGlyphs = false;
    int gdiSize;

    T2KFontStrike(T2KFontFile t2KFontFile, float f, BaseTransform baseTransform, int n, FontStrikeDesc fontStrikeDesc) {
        super(t2KFontFile, f, baseTransform, n, fontStrikeDesc);
        int n2 = 0;
        boolean bl = false;
        float f2 = 1.0f;
        float f3 = 0.0f;
        double[] arrd = new double[4];
        if (baseTransform.isTranslateOrIdentity()) {
            arrd[0] = arrd[3] = (double)f;
        } else {
            BaseTransform baseTransform2 = this.getTransform();
            this.invTx = new Affine2D(baseTransform2);
            try {
                this.invTx.invert();
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                this.invTx = null;
            }
            arrd[0] = baseTransform2.getMxx() * (double)f;
            arrd[1] = baseTransform2.getMyx() * (double)f;
            arrd[2] = baseTransform2.getMxy() * (double)f;
            arrd[3] = baseTransform2.getMyy() * (double)f;
        }
        float f4 = PrismFontFactory.getFontSizeLimit();
        if (Math.abs(arrd[0]) > (double)f4 || Math.abs(arrd[1]) > (double)f4 || Math.abs(arrd[2]) > (double)f4 || Math.abs(arrd[3]) > (double)f4) {
            this.drawShapes = true;
        } else if (PrismFontFactory.isWindows && this.getAAMode() == 1 && t2KFontFile.isInstalledFont() && arrd[0] > 0.0 && arrd[0] == arrd[3] && arrd[1] == 0.0 && arrd[2] == 0.0) {
            this.gdiLCDGlyphs = true;
            this.gdiSize = (int)(arrd[0] + 0.5);
        }
        int n3 = 2;
        if (this.getAAMode() == 1) {
            n3 = 4;
            bl = true;
        }
        this.pScalerContext = t2KFontFile.createScalerContext(arrd, n3, n2, bl, f2, f3);
    }

    @Override
    protected DisposerRecord createDisposer(FontStrikeDesc fontStrikeDesc) {
        T2KFontFile t2KFontFile = (T2KFontFile)this.getFontResource();
        return new T2KStrikeDisposer(t2KFontFile, fontStrikeDesc, this.pScalerContext);
    }

    long getScalerContext() {
        return this.pScalerContext;
    }

    private native long getLCDGlyphFromWindows(String var1, boolean var2, boolean var3, int var4, int var5, boolean var6);

    private long getGlyphFromWindows(int n) {
        FontResource fontResource = this.getFontResource();
        String string = fontResource.getFamilyName();
        boolean bl = fontResource.isBold();
        boolean bl2 = fontResource.isItalic();
        return this.getLCDGlyphFromWindows(string, bl, bl2, this.gdiSize, n, true);
    }

    public Point2D getGlyphMetrics(int n) {
        T2KFontFile t2KFontFile = (T2KFontFile)this.getFontResource();
        float[] arrf = t2KFontFile.getGlyphMetrics(this.pScalerContext, n);
        return new Point2D(arrf[0], arrf[1]);
    }

    @Override
    protected Glyph createGlyph(int n) {
        T2KGlyph t2KGlyph;
        T2KFontFile t2KFontFile = (T2KFontFile)this.getFontResource();
        if (this.drawAsShapes()) {
            float f = t2KFontFile.getAdvance(n, this.getSize());
            t2KGlyph = new T2KGlyph(this, n, f);
        } else {
            long l = 0L;
            long l2 = 0L;
            if (this.gdiLCDGlyphs) {
                l = l2 = this.getGlyphFromWindows(n);
            }
            if (l == 0L) {
                l = t2KFontFile.getGlyphImage(this.pScalerContext, n);
            }
            t2KGlyph = new T2KGlyph(this, n, l);
            if (l2 != 0L) {
                float f;
                float f2 = f = t2KFontFile.getAdvance(n, this.getSize());
                float f3 = 0.0f;
                if (this.invTx != null) {
                    Point2D point2D = new Point2D(f, 0.0f);
                    this.getTransform().transform(point2D, point2D);
                    f2 = point2D.x;
                    f3 = point2D.y;
                }
                t2KGlyph.setAdvance(f, f2, f3);
            }
        }
        return t2KGlyph;
    }

    @Override
    protected Path2D createGlyphOutline(int n) {
        T2KFontFile t2KFontFile = (T2KFontFile)this.getFontResource();
        Path2D path2D = t2KFontFile.getGlyphOutline(this.pScalerContext, n);
        if (this.invTx != null) {
            path2D.transform(this.invTx);
        }
        return path2D;
    }

    RectBounds getGlyphBounds(int n) {
        T2KFontFile t2KFontFile = (T2KFontFile)this.getFontResource();
        return t2KFontFile.getGlyphBounds(this.pScalerContext, n);
    }

    float getGlyphUserAdvance(float f, float f2) {
        if (this.invTx != null) {
            Point2D point2D = new Point2D(f, f2);
            this.invTx.transform(point2D, point2D);
            return point2D.x;
        }
        return f;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    Point2D getGlyphPoint(int n, int n2) {
        Object object;
        Point2D point2D = null;
        Integer n3 = n << 16 | n2;
        if (this.glyphPointMap == null) {
            object = this;
            synchronized (object) {
                if (this.glyphPointMap == null) {
                    this.glyphPointMap = new ConcurrentHashMap();
                }
            }
        } else {
            point2D = this.glyphPointMap.get(n3);
        }
        if (point2D == null && (point2D = ((T2KFontFile)(object = (T2KFontFile)this.getFontResource())).getGlyphPoint(this.pScalerContext, n, n2)) != null) {
            this.adjustPoint(point2D);
            this.glyphPointMap.put(n3, point2D);
        }
        return point2D;
    }

    protected void adjustPoint(Point2D point2D) {
        if (this.invTx != null) {
            this.invTx.deltaTransform(point2D, point2D);
        }
    }
}

