/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.locator;

import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class LocatorCache {
    private final Map<URI, WeakReference<CacheReference>> uriCache = new HashMap<URI, WeakReference<CacheReference>>();
    private final CacheDisposer cacheDisposer = new CacheDisposer();

    public static LocatorCache locatorCache() {
        return CacheInitializer.globalInstance;
    }

    private LocatorCache() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public CacheReference registerURICache(URI uRI, ByteBuffer object, String string) {
        Object object2;
        if (Logger.canLog(1)) {
            Logger.logMsg(1, "New cache entry: URI " + uRI + ", buffer " + object + ", MIME type " + string);
        }
        if (!((ByteBuffer)object).isDirect()) {
            ((ByteBuffer)object).rewind();
            object2 = ByteBuffer.allocateDirect(((Buffer)object).capacity());
            ((ByteBuffer)object2).put((ByteBuffer)object);
            object = object2;
        }
        object2 = new CacheReference((ByteBuffer)object, string);
        Map<URI, WeakReference<CacheReference>> map = this.uriCache;
        synchronized (map) {
            this.uriCache.put(uRI, new WeakReference<Object>(object2));
        }
        MediaDisposer.addResourceDisposer(object2, uRI, this.cacheDisposer);
        return object2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public CacheReference fetchURICache(URI uRI) {
        Map<URI, WeakReference<CacheReference>> map = this.uriCache;
        synchronized (map) {
            WeakReference<CacheReference> weakReference = this.uriCache.get(uRI);
            if (null == weakReference) {
                return null;
            }
            CacheReference cacheReference = (CacheReference)weakReference.get();
            if (null != cacheReference) {
                if (Logger.canLog(1)) {
                    Logger.logMsg(1, "Fetched cache entry: URI " + uRI + ", buffer " + cacheReference.getBuffer() + ", MIME type " + cacheReference.getMIMEType());
                }
                return cacheReference;
            }
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isCached(URI uRI) {
        Map<URI, WeakReference<CacheReference>> map = this.uriCache;
        synchronized (map) {
            return this.uriCache.containsKey(uRI);
        }
    }

    private class CacheDisposer
    implements MediaDisposer.ResourceDisposer {
        private CacheDisposer() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void disposeResource(Object object) {
            if (object instanceof URI) {
                Map map = LocatorCache.this.uriCache;
                synchronized (map) {
                    LocatorCache.this.uriCache.remove((URI)object);
                }
            }
        }
    }

    public static class CacheReference {
        private final ByteBuffer buffer;
        private String mimeType;

        public CacheReference(ByteBuffer byteBuffer, String string) {
            this.buffer = byteBuffer;
            this.mimeType = string;
        }

        public ByteBuffer getBuffer() {
            return this.buffer;
        }

        public String getMIMEType() {
            return this.mimeType;
        }
    }

    private static class CacheInitializer {
        private static final LocatorCache globalInstance = new LocatorCache();

        private CacheInitializer() {
        }
    }
}

