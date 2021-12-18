/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.ES2TextureData;
import com.sun.prism.es2.ES2VramPool;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.DisposerManagedResource;

class ES2TextureResource<T extends ES2TextureData>
extends DisposerManagedResource<T> {
    ES2TextureResource(T t) {
        super(t, ES2VramPool.instance, (Disposer.Record)t);
    }

    @Override
    public void free() {
        if (this.resource != null) {
            ((ES2TextureData)this.resource).dispose();
        }
    }
}

