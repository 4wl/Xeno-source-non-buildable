/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.locator;

import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.locator.ConnectionHolder;
import com.sun.media.jfxmedia.locator.LocatorCache;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.MediaUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

public class Locator {
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final int MAX_CONNECTION_ATTEMPTS = 5;
    private static final long CONNECTION_RETRY_INTERVAL = 1000L;
    protected String contentType = "application/octet-stream";
    protected long contentLength = -1L;
    protected URI uri;
    private Map<String, Object> connectionProperties;
    private final Object propertyLock = new Object();
    private String uriString = null;
    private String scheme = null;
    private String protocol = null;
    private LocatorCache.CacheReference cacheEntry = null;
    private boolean canBlock = false;
    private CountDownLatch readySignal = new CountDownLatch(1);
    private boolean isIpod;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private LocatorConnection getConnection(URI uRI, String string) throws MalformedURLException, IOException {
        LocatorConnection locatorConnection = new LocatorConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRI.toURL().openConnection();
        httpURLConnection.setRequestMethod(string);
        Object object = this.propertyLock;
        synchronized (object) {
            if (this.connectionProperties != null) {
                for (String string2 : this.connectionProperties.keySet()) {
                    Object object2 = this.connectionProperties.get(string2);
                    if (!(object2 instanceof String)) continue;
                    httpURLConnection.setRequestProperty(string2, (String)object2);
                }
            }
        }
        locatorConnection.responseCode = httpURLConnection.getResponseCode();
        if (httpURLConnection.getResponseCode() == 200) {
            locatorConnection.connection = httpURLConnection;
        } else {
            Locator.closeConnection(httpURLConnection);
            locatorConnection.connection = null;
        }
        return locatorConnection;
    }

    private static long getContentLengthLong(URLConnection uRLConnection) {
        Method method = AccessController.doPrivileged(() -> {
            try {
                return uRLConnection.getClass().getMethod("getContentLengthLong", new Class[0]);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                return null;
            }
        });
        try {
            if (method != null) {
                return (Long)method.invoke(uRLConnection, new Object[0]);
            }
            return uRLConnection.getContentLength();
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            return -1L;
        }
    }

    public Locator(URI uRI) throws URISyntaxException {
        if (uRI == null) {
            throw new NullPointerException("uri == null!");
        }
        this.uriString = uRI.toASCIIString();
        this.scheme = uRI.getScheme();
        if (this.scheme == null) {
            throw new IllegalArgumentException("uri.getScheme() == null! uri == '" + uRI + "'");
        }
        this.scheme = this.scheme.toLowerCase();
        if (this.scheme.equals("jar")) {
            URI uRI2 = new URI(this.uriString.substring(4));
            this.protocol = uRI2.getScheme();
            if (this.protocol == null) {
                throw new IllegalArgumentException("uri.getScheme() == null! subURI == '" + uRI2 + "'");
            }
            this.protocol = this.protocol.toLowerCase();
        } else {
            this.protocol = this.scheme;
        }
        if (HostUtils.isIOS() && this.protocol.equals("ipod-library")) {
            this.isIpod = true;
        }
        if (!this.isIpod && !MediaManager.canPlayProtocol(this.protocol)) {
            throw new UnsupportedOperationException("Unsupported protocol \"" + this.protocol + "\"");
        }
        if (this.protocol.equals("http") || this.protocol.equals("https")) {
            this.canBlock = true;
        }
        this.uri = uRI;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private InputStream getInputStream(URI uRI) throws MalformedURLException, IOException {
        URL uRL = uRI.toURL();
        URLConnection uRLConnection = uRL.openConnection();
        Object object = this.propertyLock;
        synchronized (object) {
            if (this.connectionProperties != null) {
                for (String string : this.connectionProperties.keySet()) {
                    Object object2 = this.connectionProperties.get(string);
                    if (!(object2 instanceof String)) continue;
                    uRLConnection.setRequestProperty(string, (String)object2);
                }
            }
        }
        object = uRL.openStream();
        this.contentLength = Locator.getContentLengthLong(uRLConnection);
        return object;
    }

    public void cacheMedia() {
        LocatorCache.CacheReference cacheReference = LocatorCache.locatorCache().fetchURICache(this.uri);
        if (null == cacheReference) {
            InputStream inputStream;
            try {
                inputStream = this.getInputStream(this.uri);
            }
            catch (Throwable throwable) {
                return;
            }
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int)this.contentLength);
            byte[] arrby = new byte[8192];
            int n = 0;
            while ((long)n < this.contentLength) {
                int n2;
                try {
                    n2 = inputStream.read(arrby);
                }
                catch (IOException iOException) {
                    try {
                        inputStream.close();
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                    if (Logger.canLog(1)) {
                        Logger.logMsg(1, "IOException trying to preload media: " + iOException);
                    }
                    return;
                }
                if (n2 == -1) break;
                byteBuffer.put(arrby, 0, n2);
            }
            try {
                inputStream.close();
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            this.cacheEntry = LocatorCache.locatorCache().registerURICache(this.uri, byteBuffer, this.contentType);
            this.canBlock = false;
        }
    }

    public boolean canBlock() {
        return this.canBlock;
    }

    public void init() throws URISyntaxException, IOException, FileNotFoundException {
        try {
            int n;
            int n2 = this.uriString.indexOf("/");
            if (n2 != -1 && this.uriString.charAt(n2 + 1) != '/') {
                if (this.protocol.equals("file")) {
                    this.uriString = this.uriString.replaceFirst("/", "///");
                } else if (this.protocol.equals("http") || this.protocol.equals("https")) {
                    this.uriString = this.uriString.replaceFirst("/", "//");
                }
            }
            if (System.getProperty("os.name").toLowerCase().indexOf("win") == -1 && this.protocol.equals("file") && (n = this.uriString.indexOf("/~/")) != -1) {
                this.uriString = this.uriString.substring(0, n) + System.getProperty("user.home") + this.uriString.substring(n + 2);
            }
            this.uri = new URI(this.uriString);
            this.cacheEntry = LocatorCache.locatorCache().fetchURICache(this.uri);
            if (null != this.cacheEntry) {
                this.contentType = this.cacheEntry.getMIMEType();
                this.contentLength = this.cacheEntry.getBuffer().capacity();
                if (Logger.canLog(1)) {
                    Logger.logMsg(1, "Locator init cache hit:\n    uri " + this.uri + "\n    type " + this.contentType + "\n    length " + this.contentLength);
                }
                return;
            }
            n = 0;
            boolean bl = false;
            boolean bl2 = true;
            if (!this.isIpod) {
                for (int i = 0; i < 5; ++i) {
                    block38: {
                        try {
                            Object object;
                            if (this.scheme.equals("http") || this.scheme.equals("https")) {
                                object = this.getConnection(this.uri, "HEAD");
                                if (object == null || ((LocatorConnection)object).connection == null) {
                                    object = this.getConnection(this.uri, "GET");
                                }
                                if (object != null && ((LocatorConnection)object).connection != null) {
                                    n = 1;
                                    this.contentType = ((LocatorConnection)object).connection.getContentType();
                                    this.contentLength = Locator.getContentLengthLong(((LocatorConnection)object).connection);
                                    Locator.closeConnection(((LocatorConnection)object).connection);
                                    ((LocatorConnection)object).connection = null;
                                } else if (object != null && ((LocatorConnection)object).responseCode == 404) {
                                    bl = true;
                                }
                            } else if (this.scheme.equals("file") || this.scheme.equals("jar")) {
                                object = this.getInputStream(this.uri);
                                ((InputStream)object).close();
                                n = 1;
                                this.contentType = MediaUtils.filenameToContentType(this.uriString);
                            }
                            if (n == 0) break block38;
                            if ("audio/x-wav".equals(this.contentType)) {
                                this.contentType = this.getContentTypeFromFileSignature(this.uri);
                                if (!MediaManager.canPlayContentType(this.contentType)) {
                                    bl2 = false;
                                }
                            } else if (this.contentType == null || !MediaManager.canPlayContentType(this.contentType)) {
                                this.contentType = MediaUtils.filenameToContentType(this.uriString);
                                if (DEFAULT_CONTENT_TYPE.equals(this.contentType)) {
                                    this.contentType = this.getContentTypeFromFileSignature(this.uri);
                                }
                                if (!MediaManager.canPlayContentType(this.contentType)) {
                                    bl2 = false;
                                }
                            }
                            break;
                        }
                        catch (IOException iOException) {
                            if (i + 1 < 5) break block38;
                            throw iOException;
                        }
                    }
                    try {
                        Thread.sleep(1000L);
                        continue;
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                }
            } else {
                this.contentType = MediaUtils.filenameToContentType(this.uriString);
            }
            if (!this.isIpod && n == 0) {
                if (bl) {
                    throw new FileNotFoundException("media is unavailable (" + this.uri.toString() + ")");
                }
                throw new IOException("could not connect to media (" + this.uri.toString() + ")");
            }
            if (!bl2) {
                throw new MediaException("media type not supported (" + this.uri.toString() + ")");
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            throw fileNotFoundException;
        }
        catch (IOException iOException) {
            throw iOException;
        }
        catch (MediaException mediaException) {
            throw mediaException;
        }
        finally {
            this.readySignal.countDown();
        }
    }

    public String getContentType() {
        try {
            this.readySignal.await();
        }
        catch (Exception exception) {
            // empty catch block
        }
        return this.contentType;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public long getContentLength() {
        try {
            this.readySignal.await();
        }
        catch (Exception exception) {
            // empty catch block
        }
        return this.contentLength;
    }

    public void waitForReadySignal() {
        try {
            this.readySignal.await();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public URI getURI() {
        return this.uri;
    }

    public String toString() {
        if (LocatorCache.locatorCache().isCached(this.uri)) {
            return "{LocatorURI uri: " + this.uri.toString() + " (media cached)}";
        }
        return "{LocatorURI uri: " + this.uri.toString() + "}";
    }

    public String getStringLocation() {
        return this.uri.toString();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setConnectionProperty(String string, Object object) {
        Object object2 = this.propertyLock;
        synchronized (object2) {
            if (this.connectionProperties == null) {
                this.connectionProperties = new TreeMap<String, Object>();
            }
            this.connectionProperties.put(string, object);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ConnectionHolder createConnectionHolder() throws IOException {
        ConnectionHolder connectionHolder;
        if (null != this.cacheEntry) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "Locator.createConnectionHolder: media cached, creating memory connection holder");
            }
            return ConnectionHolder.createMemoryConnectionHolder(this.cacheEntry.getBuffer());
        }
        if ("file".equals(this.scheme)) {
            connectionHolder = ConnectionHolder.createFileConnectionHolder(this.uri);
        } else if (this.uri.toString().endsWith(".m3u8") || this.uri.toString().endsWith(".m3u")) {
            connectionHolder = ConnectionHolder.createHLSConnectionHolder(this.uri);
        } else {
            Object object = this.propertyLock;
            synchronized (object) {
                connectionHolder = ConnectionHolder.createURIConnectionHolder(this.uri, this.connectionProperties);
            }
        }
        return connectionHolder;
    }

    private String getContentTypeFromFileSignature(URI uRI) throws MalformedURLException, IOException {
        InputStream inputStream = this.getInputStream(uRI);
        byte[] arrby = new byte[22];
        int n = inputStream.read(arrby);
        inputStream.close();
        return MediaUtils.fileSignatureToContentType(arrby, n);
    }

    static void closeConnection(URLConnection uRLConnection) {
        if (uRLConnection instanceof HttpURLConnection) {
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRLConnection;
            try {
                if (httpURLConnection.getResponseCode() < 400 && httpURLConnection.getInputStream() != null) {
                    httpURLConnection.getInputStream().close();
                }
            }
            catch (IOException iOException) {
                try {
                    if (httpURLConnection.getErrorStream() != null) {
                        httpURLConnection.getErrorStream().close();
                    }
                }
                catch (IOException iOException2) {
                    // empty catch block
                }
            }
        }
    }

    private static class LocatorConnection {
        public HttpURLConnection connection = null;
        public int responseCode = 200;

        private LocatorConnection() {
        }
    }
}

