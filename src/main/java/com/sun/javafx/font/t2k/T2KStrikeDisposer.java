/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.t2k;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.t2k.T2KFontFile;
import java.lang.ref.WeakReference;

class T2KStrikeDisposer
implements DisposerRecord {
    FontResource fontResource;
    FontStrikeDesc desc;
    long pScalerContext = 0L;
    boolean disposed = false;

    public T2KStrikeDisposer(FontResource fontResource, FontStrikeDesc fontStrikeDesc, long l) {
        this.fontResource = fontResource;
        this.desc = fontStrikeDesc;
        this.pScalerContext = l;
    }

    @Override
    public synchronized void dispose() {
        if (!this.disposed) {
            Object t;
            WeakReference<FontStrike> weakReference = this.fontResource.getStrikeMap().get(this.desc);
            if (weakReference != null && (t = weakReference.get()) == null) {
                this.fontResource.getStrikeMap().remove(this.desc);
            }
            if (this.pScalerContext != 0L) {
                T2KFontFile.freePointer(this.pScalerContext);
            }
            this.disposed = true;
        }
    }
}

