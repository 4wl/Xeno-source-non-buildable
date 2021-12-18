/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import java.nio.ByteBuffer;

interface ByteBufferAllocator {
    public ByteBuffer allocate() throws InterruptedException;

    public void release(ByteBuffer var1);
}

