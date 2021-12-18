/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.Graphics;
import com.sun.prism.Material;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2Light;
import com.sun.prism.es2.ES2Mesh;
import com.sun.prism.es2.ES2PhongMaterial;
import com.sun.prism.impl.BaseMeshView;
import com.sun.prism.impl.Disposer;

class ES2MeshView
extends BaseMeshView {
    static int count = 0;
    private final ES2Context context;
    private final long nativeHandle;
    private float ambientLightRed = 0.0f;
    private float ambientLightBlue = 0.0f;
    private float ambientLightGreen = 0.0f;
    private ES2Light[] lights = new ES2Light[3];
    private final ES2Mesh mesh;
    private ES2PhongMaterial material;

    private ES2MeshView(ES2Context eS2Context, long l, ES2Mesh eS2Mesh, Disposer.Record record) {
        super(record);
        this.context = eS2Context;
        this.mesh = eS2Mesh;
        this.nativeHandle = l;
        ++count;
    }

    static ES2MeshView create(ES2Context eS2Context, ES2Mesh eS2Mesh) {
        long l = eS2Context.createES2MeshView(eS2Mesh);
        return new ES2MeshView(eS2Context, l, eS2Mesh, new ES2MeshViewDisposerRecord(eS2Context, l));
    }

    @Override
    public void setCullingMode(int n) {
        this.context.setCullingMode(this.nativeHandle, n);
    }

    @Override
    public void setMaterial(Material material) {
        this.context.setMaterial(this.nativeHandle, material);
        this.material = (ES2PhongMaterial)material;
    }

    @Override
    public void setWireframe(boolean bl) {
        this.context.setWireframe(this.nativeHandle, bl);
    }

    @Override
    public void setAmbientLight(float f, float f2, float f3) {
        this.ambientLightRed = f;
        this.ambientLightGreen = f2;
        this.ambientLightBlue = f3;
        this.context.setAmbientLight(this.nativeHandle, f, f2, f3);
    }

    float getAmbientLightRed() {
        return this.ambientLightRed;
    }

    float getAmbientLightGreen() {
        return this.ambientLightGreen;
    }

    float getAmbientLightBlue() {
        return this.ambientLightBlue;
    }

    @Override
    public void setPointLight(int n, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (n >= 0 && n <= 2) {
            this.lights[n] = new ES2Light(f, f2, f3, f4, f5, f6, f7);
            this.context.setPointLight(this.nativeHandle, n, f, f2, f3, f4, f5, f6, f7);
        }
    }

    ES2Light[] getPointLights() {
        return this.lights;
    }

    @Override
    public void render(Graphics graphics) {
        this.material.lockTextureMaps();
        this.context.renderMeshView(this.nativeHandle, graphics, this);
        this.material.unlockTextureMaps();
    }

    ES2PhongMaterial getMaterial() {
        return this.material;
    }

    @Override
    public void dispose() {
        this.material = null;
        this.lights = null;
        this.disposerRecord.dispose();
        --count;
    }

    public int getCount() {
        return count;
    }

    static class ES2MeshViewDisposerRecord
    implements Disposer.Record {
        private final ES2Context context;
        private long nativeHandle;

        ES2MeshViewDisposerRecord(ES2Context eS2Context, long l) {
            this.context = eS2Context;
            this.nativeHandle = l;
        }

        void traceDispose() {
        }

        @Override
        public void dispose() {
            if (this.nativeHandle != 0L) {
                this.traceDispose();
                this.context.releaseES2MeshView(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}

