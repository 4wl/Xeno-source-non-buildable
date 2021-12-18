/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.WebPage;
import com.sun.webkit.network.ByteBufferPool;
import com.sun.webkit.network.FormDataElement;
import com.sun.webkit.network.URLLoader;
import com.sun.webkit.network.URLs;
import com.sun.webkit.network.Util;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.Permission;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

final class NetworkContext {
    private static final Logger logger = Logger.getLogger(NetworkContext.class.getName());
    private static final int THREAD_POOL_SIZE = 20;
    private static final long THREAD_POOL_KEEP_ALIVE_TIME = 10000L;
    private static final int DEFAULT_HTTP_MAX_CONNECTIONS = 5;
    private static final int BYTE_BUFFER_SIZE = 40960;
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 20, 10000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new URLLoaderThreadFactory());
    private static final ByteBufferPool byteBufferPool;

    private NetworkContext() {
        throw new AssertionError();
    }

    private static boolean canHandleURL(String string) {
        URL uRL = null;
        try {
            uRL = URLs.newURL(string);
        }
        catch (MalformedURLException malformedURLException) {
            // empty catch block
        }
        return uRL != null;
    }

    private static URLLoader fwkLoad(WebPage webPage, boolean bl, String string, String string2, String string3, FormDataElement[] arrformDataElement, long l) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("webPage: [%s], asynchronous: [%s], url: [%s], method: [%s], formDataElements: %s, data: [0x%016X], headers:%n%s", webPage, bl, string, string2, arrformDataElement != null ? Arrays.asList(arrformDataElement) : "[null]", l, Util.formatHeaders(string3)));
        }
        URLLoader uRLLoader = new URLLoader(webPage, byteBufferPool, bl, string, string2, string3, arrformDataElement, l);
        if (bl) {
            threadPool.submit(uRLLoader);
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "active count: [{0}], pool size: [{1}], max pool size: [{2}], task count: [{3}], completed task count: [{4}]", new Object[]{threadPool.getActiveCount(), threadPool.getPoolSize(), threadPool.getMaximumPoolSize(), threadPool.getTaskCount(), threadPool.getCompletedTaskCount()});
            }
            return uRLLoader;
        }
        uRLLoader.run();
        return null;
    }

    private static int fwkGetMaximumHTTPConnectionCountPerHost() {
        int n = AccessController.doPrivileged(() -> Integer.getInteger("http.maxConnections", -1));
        return n >= 0 ? n : 5;
    }

    static {
        threadPool.allowCoreThreadTimeOut(true);
        byteBufferPool = ByteBufferPool.newInstance(40960);
    }

    private static final class URLLoaderThreadFactory
    implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger index = new AtomicInteger(1);
        private static final Permission modifyThreadGroupPerm = new RuntimePermission("modifyThreadGroup");
        private static final Permission modifyThreadPerm = new RuntimePermission("modifyThread");

        private URLLoaderThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable runnable) {
            return AccessController.doPrivileged(() -> {
                Thread thread = new Thread(this.group, runnable, "URL-Loader-" + this.index.getAndIncrement());
                thread.setDaemon(true);
                if (thread.getPriority() != 5) {
                    thread.setPriority(5);
                }
                return thread;
            }, null, modifyThreadGroupPerm, modifyThreadPerm);
        }
    }
}

