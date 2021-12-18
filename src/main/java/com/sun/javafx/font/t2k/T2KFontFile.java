/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.t2k;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.t2k.T2KFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import java.security.AccessController;
import java.security.PrivilegedAction;

class T2KFontFile
extends PrismFontFile {
    private static final int TRUETYPE_FONT = 1;
    private long pScaler = 0L;
    private boolean scalerInited = false;
    private ScalerDisposer scalerDisposer;

    public T2KFontFile(String string, String string2, int n, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws Exception {
        super(string, string2, n, bl, bl2, bl3, bl4);
    }

    private synchronized void initScaler() {
        if (this.pScaler == 0L && !this.scalerInited) {
            int n = 1;
            String string = this.getFileName();
            byte[] arrby = string.getBytes();
            this.pScaler = this.initNativeScaler(n, this.getFontIndex(), false, string, arrby, this.getFileSize(), null);
            this.scalerInited = true;
            if (!this.isRegistered()) {
                this.scalerDisposer = new ScalerDisposer(this.pScaler);
                Disposer.addRecord(this, this.scalerDisposer);
            }
        }
    }

    private void invalidateScaler() {
        this.pScaler = 0L;
        if (this.scalerDisposer != null) {
            this.scalerDisposer.pScaler = 0L;
        }
    }

    @Override
    protected synchronized void disposeOnShutdown() {
        if (this.pScaler != 0L) {
            T2KFontFile.disposeNativeScaler(this.pScaler);
            if (PrismFontFactory.debugFonts) {
                System.err.println("pScaler freed: " + this.pScaler);
            }
            this.pScaler = 0L;
        }
        super.disposeOnShutdown();
    }

    Point2D getGlyphPoint(long l, int n, int n2) {
        this.initScaler();
        if (this.pScaler != 0L) {
            return this.getGlyphPointNative(l, this.pScaler, n, n2);
        }
        return null;
    }

    native Point2D getGlyphPointNative(long var1, long var3, int var5, int var6);

    static synchronized native void freePointer(long var0);

    private static native void initNativeIDs();

    private synchronized native long initNativeScaler(int var1, int var2, boolean var3, String var4, byte[] var5, int var6, int[] var7);

    public long getLayoutTableCache() {
        return T2KFontFile.getLayoutTableCacheNative(this.pScaler);
    }

    private static native long getLayoutTableCacheNative(long var0);

    private static native void disposeNativeScaler(long var0);

    synchronized long createScalerContext(double[] arrd, int n, int n2, boolean bl, float f, float f2) {
        this.initScaler();
        return this.createScalerContext(this.pScaler, arrd, n, n2, bl, f, f2);
    }

    private synchronized native long createScalerContext(long var1, double[] var3, int var4, int var5, boolean var6, float var7, float var8);

    synchronized Path2D getGlyphOutline(long l, int n) {
        return this.getGlyphOutline(this.pScaler, l, n);
    }

    private native Path2D getGlyphOutline(long var1, long var3, int var5);

    synchronized RectBounds getGlyphBounds(long l, int n) {
        return this.getGlyphBounds(this.pScaler, l, n);
    }

    private native RectBounds getGlyphBounds(long var1, long var3, int var5);

    synchronized float[] getGlyphMetrics(long l, int n) {
        return this.getGlyphMetrics(this.pScaler, l, n);
    }

    private native float[] getGlyphMetrics(long var1, long var3, int var5);

    synchronized long getGlyphImage(long l, int n) {
        return this.getGlyphImage(this.pScaler, l, n);
    }

    private native long getGlyphImage(long var1, long var3, int var5);

    @Override
    protected PrismFontStrike createStrike(float f, BaseTransform baseTransform, int n, FontStrikeDesc fontStrikeDesc) {
        return new T2KFontStrike(this, f, baseTransform, n, fontStrikeDesc);
    }

    private synchronized native int[] getGlyphBoundingBoxNative(long var1, int var3);

    private int[] getGlyphBoundingBoxCFF(int n, float f) {
        if (f < 1.0f || f > 48.0f) {
            f = 12.0f;
        }
        T2KFontStrike t2KFontStrike = (T2KFontStrike)this.getStrike(f, BaseTransform.IDENTITY_TRANSFORM);
        long l = t2KFontStrike.getScalerContext();
        RectBounds rectBounds = this.getGlyphBounds(this.pScaler, l, n);
        int[] arrn = new int[4];
        float f2 = (float)this.getUnitsPerEm() / f;
        arrn[0] = Math.round(rectBounds.getMinX() * f2);
        arrn[1] = Math.round(rectBounds.getMinY() * f2);
        arrn[2] = Math.round(rectBounds.getMaxX() * f2);
        arrn[3] = Math.round(rectBounds.getMaxY() * f2);
        return arrn;
    }

    @Override
    protected int[] createGlyphBoundingBox(int n) {
        if (this.pScaler == 0L) {
            this.initScaler();
        }
        if (this.isCFF()) {
            return this.getGlyphBoundingBoxCFF(n, 12.0f);
        }
        return this.getGlyphBoundingBoxNative(this.pScaler, n);
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>(){

            @Override
            public Void run() {
                NativeLibLoader.loadLibrary("javafx_font_t2k");
                return null;
            }
        });
        T2KFontFile.initNativeIDs();
    }

    static class ScalerDisposer
    implements DisposerRecord {
        long pScaler;

        public ScalerDisposer(long l) {
            this.pScaler = l;
        }

        @Override
        public synchronized void dispose() {
            if (this.pScaler != 0L) {
                T2KFontFile.disposeNativeScaler(this.pScaler);
                if (PrismFontFactory.debugFonts) {
                    System.err.println("ScalerDisposer=" + this.pScaler);
                }
                this.pScaler = 0L;
            }
        }
    }
}

