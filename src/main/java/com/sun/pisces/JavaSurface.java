/*
 * Decompiled with CFR 0.150.
 */
package com.sun.pisces;

import com.sun.pisces.AbstractSurface;
import java.nio.IntBuffer;

public final class JavaSurface
extends AbstractSurface {
    private IntBuffer dataBuffer;
    private int[] dataInt;

    public JavaSurface(int[] arrn, int n, int n2, int n3) {
        super(n2, n3);
        this.dataInt = arrn;
        this.dataBuffer = IntBuffer.wrap(this.dataInt);
        this.initialize(n, n2, n3);
    }

    public IntBuffer getDataIntBuffer() {
        return this.dataBuffer;
    }

    private native void initialize(int var1, int var2, int var3);
}

