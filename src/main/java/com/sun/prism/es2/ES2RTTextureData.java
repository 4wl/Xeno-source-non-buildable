/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2TextureData;
import com.sun.prism.impl.PrismTrace;

class ES2RTTextureData
extends ES2TextureData {
    private int fboID;
    private int dbID;
    private int rbID;

    ES2RTTextureData(ES2Context eS2Context, int n, int n2, int n3, int n4, long l) {
        super(eS2Context, n, l);
        this.fboID = n2;
        PrismTrace.rttCreated((long)n2, n3, n4, l);
    }

    public int getFboID() {
        return this.fboID;
    }

    public int getMSAARenderBufferID() {
        return this.rbID;
    }

    void setMSAARenderBufferID(int n) {
        assert (this.getTexID() == 0);
        this.rbID = n;
    }

    public int getDepthBufferID() {
        return this.dbID;
    }

    void setDepthBufferID(int n) {
        this.dbID = n;
    }

    @Override
    void traceDispose() {
        PrismTrace.rttDisposed(this.fboID);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.fboID != 0) {
            this.context.getGLContext().deleteFBO(this.fboID);
            if (this.dbID != 0) {
                this.context.getGLContext().deleteRenderBuffer(this.dbID);
                this.dbID = 0;
            }
            if (this.rbID != 0) {
                this.context.getGLContext().deleteRenderBuffer(this.rbID);
                this.rbID = 0;
            }
            this.fboID = 0;
        }
    }
}

