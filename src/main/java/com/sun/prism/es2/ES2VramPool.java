/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.PixelFormat;
import com.sun.prism.es2.ES2TextureData;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;

class ES2VramPool
extends BaseResourcePool<ES2TextureData>
implements TextureResourcePool<ES2TextureData> {
    static ES2VramPool instance = new ES2VramPool();

    private ES2VramPool() {
        super(PrismSettings.targetVram, PrismSettings.maxVram);
    }

    @Override
    public long estimateTextureSize(int n, int n2, PixelFormat pixelFormat) {
        return (long)n * (long)n2 * (long)pixelFormat.getBytesPerPixelUnit();
    }

    @Override
    public long estimateRTTextureSize(int n, int n2, boolean bl) {
        return (long)n * (long)n2 * 4L;
    }

    @Override
    public long size(ES2TextureData eS2TextureData) {
        return eS2TextureData.getSize();
    }

    public String toString() {
        return "ES2 Vram Pool";
    }
}

