/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.network.ByteBufferAllocator;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

final class ByteBufferPool {
    private final Queue<ByteBuffer> byteBuffers = new ConcurrentLinkedQueue<ByteBuffer>();
    private final int bufferSize;

    private ByteBufferPool(int n) {
        this.bufferSize = n;
    }

    static ByteBufferPool newInstance(int n) {
        return new ByteBufferPool(n);
    }

    ByteBufferAllocator newAllocator(int n) {
        return new ByteBufferAllocatorImpl(n);
    }

    private final class ByteBufferAllocatorImpl
    implements ByteBufferAllocator {
        private final Semaphore semaphore;

        private ByteBufferAllocatorImpl(int n) {
            this.semaphore = new Semaphore(n);
        }

        @Override
        public ByteBuffer allocate() throws InterruptedException {
            this.semaphore.acquire();
            ByteBuffer byteBuffer = (ByteBuffer)ByteBufferPool.this.byteBuffers.poll();
            if (byteBuffer == null) {
                byteBuffer = ByteBuffer.allocateDirect(ByteBufferPool.this.bufferSize);
            }
            return byteBuffer;
        }

        @Override
        public void release(ByteBuffer byteBuffer) {
            ByteBufferPool.this.byteBuffers.add(byteBuffer);
            this.semaphore.release();
        }
    }
}

