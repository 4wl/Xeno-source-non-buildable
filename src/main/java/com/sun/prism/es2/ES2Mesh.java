/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.ES2Context;
import com.sun.prism.impl.BaseMesh;
import com.sun.prism.impl.Disposer;

class ES2Mesh
extends BaseMesh {
    static int count = 0;
    private final ES2Context context;
    private final long nativeHandle;

    private ES2Mesh(ES2Context eS2Context, long l, Disposer.Record record) {
        super(record);
        this.context = eS2Context;
        this.nativeHandle = l;
        ++count;
    }

    static ES2Mesh create(ES2Context eS2Context) {
        long l = eS2Context.createES2Mesh();
        return new ES2Mesh(eS2Context, l, new ES2MeshDisposerRecord(eS2Context, l));
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    @Override
    public void dispose() {
        this.disposerRecord.dispose();
        --count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean buildNativeGeometry(float[] arrf, int n, int[] arrn, int n2) {
        return this.context.buildNativeGeometry(this.nativeHandle, arrf, n, arrn, n2);
    }

    @Override
    public boolean buildNativeGeometry(float[] arrf, int n, short[] arrs, int n2) {
        return this.context.buildNativeGeometry(this.nativeHandle, arrf, n, arrs, n2);
    }

    static class ES2MeshDisposerRecord
    implements Disposer.Record {
        private final ES2Context context;
        private long nativeHandle;

        ES2MeshDisposerRecord(ES2Context eS2Context, long l) {
            this.context = eS2Context;
            this.nativeHandle = l;
        }

        void traceDispose() {
        }

        @Override
        public void dispose() {
            if (this.nativeHandle != 0L) {
                this.traceDispose();
                this.context.releaseES2Mesh(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}

