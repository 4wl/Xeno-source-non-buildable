/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.Pixels;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

final class GtkPixels
extends Pixels {
    public GtkPixels(int n, int n2, ByteBuffer byteBuffer) {
        super(n, n2, byteBuffer);
    }

    public GtkPixels(int n, int n2, IntBuffer intBuffer) {
        super(n, n2, intBuffer);
    }

    public GtkPixels(int n, int n2, IntBuffer intBuffer, float f) {
        super(n, n2, intBuffer, f);
    }

    @Override
    protected void _fillDirectByteBuffer(ByteBuffer byteBuffer) {
        if (this.bytes != null) {
            this.bytes.rewind();
            if (this.bytes.isDirect()) {
                this._copyPixels(byteBuffer, this.bytes, this.getWidth() * this.getHeight());
            } else {
                byteBuffer.put(this.bytes);
            }
            this.bytes.rewind();
        } else {
            this.ints.rewind();
            if (this.ints.isDirect()) {
                this._copyPixels(byteBuffer, this.ints, this.getWidth() * this.getHeight());
            } else {
                for (int i = 0; i < this.ints.capacity(); ++i) {
                    int n = this.ints.get();
                    byteBuffer.put((byte)(n & 0xFF));
                    byteBuffer.put((byte)(n >> 8 & 0xFF));
                    byteBuffer.put((byte)(n >> 16 & 0xFF));
                    byteBuffer.put((byte)(n >> 24 & 0xFF));
                }
            }
            this.ints.rewind();
        }
    }

    protected native void _copyPixels(Buffer var1, Buffer var2, int var3);

    @Override
    protected native void _attachInt(long var1, int var3, int var4, IntBuffer var5, int[] var6, int var7);

    @Override
    protected native void _attachByte(long var1, int var3, int var4, ByteBuffer var5, byte[] var6, int var7);
}

