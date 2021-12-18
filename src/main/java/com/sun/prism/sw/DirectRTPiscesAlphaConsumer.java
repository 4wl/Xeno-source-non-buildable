/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.openpisces.AlphaConsumer;
import com.sun.openpisces.Renderer;
import com.sun.pisces.PiscesRenderer;

final class DirectRTPiscesAlphaConsumer
implements AlphaConsumer {
    private byte[] alpha_map;
    private int outpix_xmin;
    private int outpix_ymin;
    private int w;
    private int h;
    private int rowNum;
    private PiscesRenderer pr;

    DirectRTPiscesAlphaConsumer() {
    }

    void initConsumer(Renderer renderer, PiscesRenderer piscesRenderer) {
        this.outpix_xmin = renderer.getOutpixMinX();
        this.outpix_ymin = renderer.getOutpixMinY();
        this.w = renderer.getOutpixMaxX() - this.outpix_xmin;
        if (this.w < 0) {
            this.w = 0;
        }
        this.h = renderer.getOutpixMaxY() - this.outpix_ymin;
        if (this.h < 0) {
            this.h = 0;
        }
        this.rowNum = 0;
        this.pr = piscesRenderer;
    }

    @Override
    public int getOriginX() {
        return this.outpix_xmin;
    }

    @Override
    public int getOriginY() {
        return this.outpix_ymin;
    }

    @Override
    public int getWidth() {
        return this.w;
    }

    @Override
    public int getHeight() {
        return this.h;
    }

    @Override
    public void setMaxAlpha(int n) {
        if (this.alpha_map == null || this.alpha_map.length != n + 1) {
            this.alpha_map = new byte[n + 1];
            for (int i = 0; i <= n; ++i) {
                this.alpha_map[i] = (byte)((i * 255 + n / 2) / n);
            }
        }
    }

    @Override
    public void setAndClearRelativeAlphas(int[] arrn, int n, int n2, int n3) {
        this.pr.emitAndClearAlphaRow(this.alpha_map, arrn, n, n2, n3, this.rowNum);
        ++this.rowNum;
    }
}

