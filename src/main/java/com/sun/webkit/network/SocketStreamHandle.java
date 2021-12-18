/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.Invoker;
import com.sun.webkit.WebPage;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;

final class SocketStreamHandle {
    private static final Pattern FIRST_LINE_PATTERN = Pattern.compile("^HTTP/1.[01]\\s+(\\d{3})(?:\\s.*)?$");
    private static final Logger logger = Logger.getLogger(SocketStreamHandle.class.getName());
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new CustomThreadFactory());
    private final String host;
    private final int port;
    private final boolean ssl;
    private final WebPage webPage;
    private final long data;
    private volatile Socket socket;
    private volatile State state = State.ACTIVE;
    private volatile boolean connected;

    private SocketStreamHandle(String string, int n, boolean bl, WebPage webPage, long l) {
        this.host = string;
        this.port = n;
        this.ssl = bl;
        this.webPage = webPage;
        this.data = l;
    }

    private static SocketStreamHandle fwkCreate(String string, int n, boolean bl, WebPage webPage, long l) {
        SocketStreamHandle socketStreamHandle = new SocketStreamHandle(string, n, bl, webPage, l);
        logger.log(Level.FINEST, "Starting {0}", socketStreamHandle);
        threadPool.submit(() -> socketStreamHandle.run());
        return socketStreamHandle;
    }

    private void run() {
        if (this.webPage == null) {
            logger.log(Level.FINEST, "{0} is not associated with any web page, aborted", this);
            this.didFail(0, "Web socket is not associated with any web page");
            this.didClose();
            return;
        }
        AccessController.doPrivileged(() -> {
            this.doRun();
            return null;
        }, this.webPage.getAccessControlContext());
    }

    private void doRun() {
        Throwable throwable = null;
        String string = null;
        try {
            byte[] arrby;
            int n;
            logger.log(Level.FINEST, "{0} started", this);
            this.connect();
            this.connected = true;
            logger.log(Level.FINEST, "{0} connected", this);
            this.didOpen();
            InputStream inputStream = this.socket.getInputStream();
            while ((n = inputStream.read(arrby = new byte[8192])) > 0) {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, String.format("%s received len: [%d], data:%s", this, n, SocketStreamHandle.dump(arrby, n)));
                }
                this.didReceiveData(arrby, n);
            }
            logger.log(Level.FINEST, "{0} connection closed by remote host", this);
        }
        catch (UnknownHostException unknownHostException) {
            throwable = unknownHostException;
            string = "Unknown host";
        }
        catch (ConnectException connectException) {
            throwable = connectException;
            string = "Unable to connect";
        }
        catch (NoRouteToHostException noRouteToHostException) {
            throwable = noRouteToHostException;
            string = "No route to host";
        }
        catch (PortUnreachableException portUnreachableException) {
            throwable = portUnreachableException;
            string = "Port unreachable";
        }
        catch (SocketException socketException) {
            if (this.state != State.ACTIVE) {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, String.format("%s exception (most likely caused by local close)", this), socketException);
                }
            } else {
                throwable = socketException;
                string = "Socket error";
            }
        }
        catch (SSLException sSLException) {
            throwable = sSLException;
            string = "SSL error";
        }
        catch (IOException iOException) {
            throwable = iOException;
            string = "I/O error";
        }
        catch (SecurityException securityException) {
            throwable = securityException;
            string = "Security error";
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
        }
        if (throwable != null) {
            if (string == null) {
                string = "Unknown error";
                logger.log(Level.WARNING, String.format("%s unexpected error", this), throwable);
            } else {
                logger.log(Level.FINEST, String.format("%s exception", this), throwable);
            }
            this.didFail(0, string);
        }
        try {
            this.socket.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        this.didClose();
        logger.log(Level.FINEST, "{0} finished", this);
    }

    private void connect() throws IOException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkConnect(this.host, this.port);
        }
        boolean bl = false;
        IOException iOException = null;
        boolean bl2 = false;
        ProxySelector proxySelector = AccessController.doPrivileged(() -> ProxySelector.getDefault());
        if (proxySelector != null) {
            URI uRI;
            try {
                uRI = new URI((this.ssl ? "https" : "http") + "://" + this.host);
            }
            catch (URISyntaxException uRISyntaxException) {
                throw new IOException(uRISyntaxException);
            }
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, String.format("%s selecting proxies for: [%s]", this, uRI));
            }
            List<Proxy> list = proxySelector.select(uRI);
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, String.format("%s selected proxies: %s", this, list));
            }
            for (Proxy proxy : list) {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, String.format("%s trying proxy: [%s]", this, proxy));
                }
                if (proxy.type() == Proxy.Type.DIRECT) {
                    bl2 = true;
                }
                try {
                    this.connect(proxy);
                    bl = true;
                    break;
                }
                catch (IOException iOException2) {
                    logger.log(Level.FINEST, String.format("%s exception", this), iOException2);
                    iOException = iOException2;
                    if (proxy.address() == null) continue;
                    proxySelector.connectFailed(uRI, proxy.address(), iOException2);
                }
            }
        }
        if (!bl && !bl2) {
            logger.log(Level.FINEST, "{0} trying direct connection", this);
            this.connect(Proxy.NO_PROXY);
            bl = true;
        }
        if (!bl) {
            throw iOException;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void connect(Proxy proxy) throws IOException {
        SocketStreamHandle socketStreamHandle = this;
        synchronized (socketStreamHandle) {
            if (this.state != State.ACTIVE) {
                throw new SocketException("Close requested");
            }
            this.socket = new Socket(proxy);
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s connecting to: [%s:%d]", this, this.host, this.port));
        }
        this.socket.connect(new InetSocketAddress(this.host, this.port));
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s connected to: [%s:%d]", this, this.host, this.port));
        }
        if (this.ssl) {
            socketStreamHandle = this;
            synchronized (socketStreamHandle) {
                if (this.state != State.ACTIVE) {
                    throw new SocketException("Close requested");
                }
                logger.log(Level.FINEST, "{0} starting SSL handshake", this);
                this.socket = HttpsURLConnection.getDefaultSSLSocketFactory().createSocket(this.socket, this.host, this.port, true);
            }
            ((SSLSocket)this.socket).startHandshake();
        }
    }

    private int fwkSend(byte[] arrby) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s sending len: [%d], data:%s", this, arrby.length, SocketStreamHandle.dump(arrby, arrby.length)));
        }
        if (this.connected) {
            try {
                this.socket.getOutputStream().write(arrby);
                return arrby.length;
            }
            catch (IOException iOException) {
                logger.log(Level.FINEST, String.format("%s exception", this), iOException);
                this.didFail(0, "I/O error");
                return 0;
            }
        }
        logger.log(Level.FINEST, "{0} not connected", this);
        this.didFail(0, "Not connected");
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void fwkClose() {
        SocketStreamHandle socketStreamHandle = this;
        synchronized (socketStreamHandle) {
            logger.log(Level.FINEST, "{0}", this);
            this.state = State.CLOSE_REQUESTED;
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private void fwkNotifyDisposed() {
        logger.log(Level.FINEST, "{0}", this);
        this.state = State.DISPOSED;
    }

    private void didOpen() {
        Invoker.getInvoker().postOnEventThread(() -> {
            if (this.state == State.ACTIVE) {
                this.notifyDidOpen();
            }
        });
    }

    private void didReceiveData(byte[] arrby, int n) {
        Invoker.getInvoker().postOnEventThread(() -> {
            if (this.state == State.ACTIVE) {
                this.notifyDidReceiveData(arrby, n);
            }
        });
    }

    private void didFail(int n, String string) {
        Invoker.getInvoker().postOnEventThread(() -> {
            if (this.state == State.ACTIVE) {
                this.notifyDidFail(n, string);
            }
        });
    }

    private void didClose() {
        Invoker.getInvoker().postOnEventThread(() -> {
            if (this.state != State.DISPOSED) {
                this.notifyDidClose();
            }
        });
    }

    private void notifyDidOpen() {
        logger.log(Level.FINEST, "{0}", this);
        SocketStreamHandle.twkDidOpen(this.data);
    }

    private void notifyDidReceiveData(byte[] arrby, int n) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s, len: [%d], data:%s", this, n, SocketStreamHandle.dump(arrby, n)));
        }
        SocketStreamHandle.twkDidReceiveData(arrby, n, this.data);
    }

    private void notifyDidFail(int n, String string) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s, errorCode: %d, errorDescription: %s", this, n, string));
        }
        SocketStreamHandle.twkDidFail(n, string, this.data);
    }

    private void notifyDidClose() {
        logger.log(Level.FINEST, "{0}", this);
        SocketStreamHandle.twkDidClose(this.data);
    }

    private static native void twkDidOpen(long var0);

    private static native void twkDidReceiveData(byte[] var0, int var1, long var2);

    private static native void twkDidFail(int var0, String var1, long var2);

    private static native void twkDidClose(long var0);

    private static String dump(byte[] arrby, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        while (n2 < n) {
            StringBuilder stringBuilder2 = new StringBuilder();
            StringBuilder stringBuilder3 = new StringBuilder();
            int n3 = 0;
            while (n3 < 16) {
                if (n2 < n) {
                    int n4 = arrby[n2] & 0xFF;
                    stringBuilder2.append(String.format("%02x ", n4));
                    stringBuilder3.append(n4 >= 32 && n4 <= 126 ? (char)n4 : (char)'.');
                } else {
                    stringBuilder2.append("   ");
                }
                ++n3;
                ++n2;
            }
            stringBuilder.append(String.format("%n  ", new Object[0])).append((CharSequence)stringBuilder2).append(' ').append((CharSequence)stringBuilder3);
        }
        return stringBuilder.toString();
    }

    public String toString() {
        return String.format("SocketStreamHandle{host=%s, port=%d, ssl=%s, data=0x%016X, state=%s, connected=%s}", new Object[]{this.host, this.port, this.ssl, this.data, this.state, this.connected});
    }

    private static final class CustomThreadFactory
    implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger index = new AtomicInteger(1);

        private CustomThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.group, runnable, "SocketStreamHandle-" + this.index.getAndIncrement());
            thread.setDaemon(true);
            if (thread.getPriority() != 5) {
                thread.setPriority(5);
            }
            return thread;
        }
    }

    private static enum State {
        ACTIVE,
        CLOSE_REQUESTED,
        DISPOSED;

    }
}

