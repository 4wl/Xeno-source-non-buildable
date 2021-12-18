/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

public final class SharedBuffer {
    private long nativePointer;

    SharedBuffer() {
        this.nativePointer = SharedBuffer.twkCreate();
    }

    private SharedBuffer(long l) {
        if (l == 0L) {
            throw new IllegalArgumentException("nativePointer is 0");
        }
        this.nativePointer = l;
    }

    private static SharedBuffer fwkCreate(long l) {
        return new SharedBuffer(l);
    }

    long size() {
        if (this.nativePointer == 0L) {
            throw new IllegalStateException("nativePointer is 0");
        }
        return SharedBuffer.twkSize(this.nativePointer);
    }

    int getSomeData(long l, byte[] arrby, int n, int n2) {
        if (this.nativePointer == 0L) {
            throw new IllegalStateException("nativePointer is 0");
        }
        if (l < 0L) {
            throw new IndexOutOfBoundsException("position is negative");
        }
        if (l > this.size()) {
            throw new IndexOutOfBoundsException("position is greater than size");
        }
        if (arrby == null) {
            throw new NullPointerException("buffer is null");
        }
        if (n < 0) {
            throw new IndexOutOfBoundsException("offset is negative");
        }
        if (n2 < 0) {
            throw new IndexOutOfBoundsException("length is negative");
        }
        if (n2 > arrby.length - n) {
            throw new IndexOutOfBoundsException("length is greater than buffer.length - offset");
        }
        return SharedBuffer.twkGetSomeData(this.nativePointer, l, arrby, n, n2);
    }

    void append(byte[] arrby, int n, int n2) {
        if (this.nativePointer == 0L) {
            throw new IllegalStateException("nativePointer is 0");
        }
        if (arrby == null) {
            throw new NullPointerException("buffer is null");
        }
        if (n < 0) {
            throw new IndexOutOfBoundsException("offset is negative");
        }
        if (n2 < 0) {
            throw new IndexOutOfBoundsException("length is negative");
        }
        if (n2 > arrby.length - n) {
            throw new IndexOutOfBoundsException("length is greater than buffer.length - offset");
        }
        SharedBuffer.twkAppend(this.nativePointer, arrby, n, n2);
    }

    void dispose() {
        if (this.nativePointer == 0L) {
            throw new IllegalStateException("nativePointer is 0");
        }
        SharedBuffer.twkDispose(this.nativePointer);
        this.nativePointer = 0L;
    }

    private static native long twkCreate();

    private static native long twkSize(long var0);

    private static native int twkGetSomeData(long var0, long var2, byte[] var4, int var5, int var6);

    private static native void twkAppend(long var0, byte[] var2, int var3, int var4);

    private static native void twkDispose(long var0);
}

