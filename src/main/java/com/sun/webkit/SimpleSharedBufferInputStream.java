/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.SharedBuffer;
import java.io.InputStream;

public final class SimpleSharedBufferInputStream
extends InputStream {
    private final SharedBuffer sharedBuffer;
    private long position;

    public SimpleSharedBufferInputStream(SharedBuffer sharedBuffer) {
        if (sharedBuffer == null) {
            throw new NullPointerException("sharedBuffer is null");
        }
        this.sharedBuffer = sharedBuffer;
    }

    @Override
    public int read() {
        byte[] arrby = new byte[1];
        int n = this.sharedBuffer.getSomeData(this.position, arrby, 0, 1);
        if (n != 0) {
            ++this.position;
            return arrby[0] & 0xFF;
        }
        return -1;
    }

    @Override
    public int read(byte[] arrby, int n, int n2) {
        if (arrby == null) {
            throw new NullPointerException("b is null");
        }
        if (n < 0) {
            throw new IndexOutOfBoundsException("off is negative");
        }
        if (n2 < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        if (n2 > arrby.length - n) {
            throw new IndexOutOfBoundsException("len is greater than b.length - off");
        }
        if (n2 == 0) {
            return 0;
        }
        int n3 = this.sharedBuffer.getSomeData(this.position, arrby, n, n2);
        if (n3 != 0) {
            this.position += (long)n3;
            return n3;
        }
        return -1;
    }

    @Override
    public long skip(long l) {
        long l2 = this.sharedBuffer.size() - this.position;
        if (l < l2) {
            l2 = l < 0L ? 0L : l;
        }
        this.position += l2;
        return l2;
    }

    @Override
    public int available() {
        return (int)Math.min(this.sharedBuffer.size() - this.position, Integer.MAX_VALUE);
    }
}

