/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.Invoker;
import com.sun.webkit.WebPage;
import com.sun.webkit.network.ByteBufferAllocator;
import com.sun.webkit.network.ByteBufferPool;
import com.sun.webkit.network.DirectoryURLConnection;
import com.sun.webkit.network.FormDataElement;
import com.sun.webkit.network.URLs;
import com.sun.webkit.network.Util;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessControlException;
import java.security.AccessController;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.net.ssl.SSLHandshakeException;

final class URLLoader
implements Runnable {
    private static final Logger logger = Logger.getLogger(URLLoader.class.getName());
    private static final int MAX_REDIRECTS = 10;
    private static final int MAX_BUF_COUNT = 3;
    private static final String GET = "GET";
    private static final String HEAD = "HEAD";
    private static final String DELETE = "DELETE";
    private final WebPage webPage;
    private final ByteBufferPool byteBufferPool;
    private final boolean asynchronous;
    private String url;
    private String method;
    private final String headers;
    private FormDataElement[] formDataElements;
    private final long data;
    private volatile boolean canceled = false;

    URLLoader(WebPage webPage, ByteBufferPool byteBufferPool, boolean bl, String string, String string2, String string3, FormDataElement[] arrformDataElement, long l) {
        this.webPage = webPage;
        this.byteBufferPool = byteBufferPool;
        this.asynchronous = bl;
        this.url = string;
        this.method = string2;
        this.headers = string3;
        this.formDataElements = arrformDataElement;
        this.data = l;
    }

    private void fwkCancel() {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("data: [0x%016X]", this.data));
        }
        this.canceled = true;
    }

    @Override
    public void run() {
        AccessController.doPrivileged(() -> {
            this.doRun();
            return null;
        }, this.webPage.getAccessControlContext());
    }

    private void doRun() {
        Throwable throwable = null;
        int n = 0;
        try {
            int n2 = 0;
            boolean bl = true;
            while (true) {
                int n3;
                String string = this.url;
                if (this.url.startsWith("file:") && (n3 = this.url.indexOf(63)) != -1) {
                    string = this.url.substring(0, n3);
                }
                URL uRL = URLs.newURL(string);
                URLLoader.workaround7177996(uRL);
                URLConnection uRLConnection = uRL.openConnection();
                this.prepareConnection(uRLConnection);
                Redirect redirect = null;
                try {
                    this.sendRequest(uRLConnection, bl);
                    redirect = this.receiveResponse(uRLConnection);
                }
                catch (HttpRetryException httpRetryException) {
                    if (bl) {
                        bl = false;
                        continue;
                    }
                    throw httpRetryException;
                }
                finally {
                    URLLoader.close(uRLConnection);
                    continue;
                }
                if (redirect != null) {
                    if (n2++ >= 10) {
                        throw new TooManyRedirectsException();
                    }
                    boolean bl2 = !redirect.preserveRequest && !this.method.equals(GET) && !this.method.equals(HEAD);
                    String string2 = bl2 ? GET : this.method;
                    this.willSendRequest(redirect.url, string2, uRLConnection);
                    if (!this.canceled) {
                        this.url = redirect.url;
                        this.method = string2;
                        this.formDataElements = bl2 ? null : this.formDataElements;
                        continue;
                    }
                }
                break;
            }
        }
        catch (MalformedURLException malformedURLException) {
            throwable = malformedURLException;
            n = 2;
        }
        catch (AccessControlException accessControlException) {
            throwable = accessControlException;
            n = 8;
        }
        catch (UnknownHostException unknownHostException) {
            throwable = unknownHostException;
            n = 1;
        }
        catch (NoRouteToHostException noRouteToHostException) {
            throwable = noRouteToHostException;
            n = 6;
        }
        catch (ConnectException connectException) {
            throwable = connectException;
            n = 4;
        }
        catch (SocketException socketException) {
            throwable = socketException;
            n = 5;
        }
        catch (SSLHandshakeException sSLHandshakeException) {
            throwable = sSLHandshakeException;
            n = 3;
        }
        catch (SocketTimeoutException socketTimeoutException) {
            throwable = socketTimeoutException;
            n = 7;
        }
        catch (InvalidResponseException invalidResponseException) {
            throwable = invalidResponseException;
            n = 9;
        }
        catch (TooManyRedirectsException tooManyRedirectsException) {
            throwable = tooManyRedirectsException;
            n = 10;
        }
        catch (FileNotFoundException fileNotFoundException) {
            throwable = fileNotFoundException;
            n = 11;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            n = 99;
        }
        if (throwable != null) {
            if (n == 99) {
                logger.log(Level.WARNING, "Unexpected error", throwable);
            } else {
                logger.log(Level.FINEST, "Load error", throwable);
            }
            this.didFail(n, throwable.getMessage());
        }
    }

    private static void workaround7177996(URL uRL) throws FileNotFoundException {
        if (!uRL.getProtocol().equals("file")) {
            return;
        }
        String string = uRL.getHost();
        if (string == null || string.equals("") || string.equals("~") || string.equalsIgnoreCase("localhost")) {
            return;
        }
        if (System.getProperty("os.name").startsWith("Windows")) {
            String string2 = null;
            try {
                string2 = URLDecoder.decode(uRL.getPath(), "UTF-8");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                // empty catch block
            }
            string2 = string2.replace('/', '\\');
            string2 = string2.replace('|', ':');
            File file = new File("\\\\" + string + string2);
            if (!file.exists()) {
                throw new FileNotFoundException("File not found: " + uRL);
            }
        } else {
            throw new FileNotFoundException("File not found: " + uRL);
        }
    }

    private void prepareConnection(URLConnection uRLConnection) throws IOException {
        uRLConnection.setConnectTimeout(30000);
        uRLConnection.setReadTimeout(3600000);
        uRLConnection.setUseCaches(false);
        Locale locale = Locale.getDefault();
        String string = "";
        if (!locale.equals(Locale.US) && !locale.equals(Locale.ENGLISH)) {
            string = locale.getCountry().isEmpty() ? locale.getLanguage() + "," : locale.getLanguage() + "-" + locale.getCountry() + ",";
        }
        uRLConnection.setRequestProperty("Accept-Language", string.toLowerCase() + "en-us;q=0.8,en;q=0.7");
        uRLConnection.setRequestProperty("Accept-Encoding", "gzip");
        uRLConnection.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        if (this.headers != null && this.headers.length() > 0) {
            for (String string2 : this.headers.split("\n")) {
                int n = string2.indexOf(58);
                if (n <= 0) continue;
                uRLConnection.addRequestProperty(string2.substring(0, n), string2.substring(n + 2));
            }
        }
        if (uRLConnection instanceof HttpURLConnection) {
            String[] arrstring = (String[])uRLConnection;
            arrstring.setRequestMethod(this.method);
            arrstring.setInstanceFollowRedirects(false);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void sendRequest(URLConnection uRLConnection, boolean bl) throws IOException {
        OutputStream outputStream = null;
        try {
            boolean bl2;
            long l = 0L;
            int n = this.formDataElements != null && uRLConnection instanceof HttpURLConnection && !this.method.equals(DELETE) ? 1 : 0;
            boolean bl3 = bl2 = this.method.equals(GET) || this.method.equals(HEAD);
            if (n != 0) {
                uRLConnection.setDoOutput(true);
                for (FormDataElement formDataElement : this.formDataElements) {
                    formDataElement.open();
                    l += formDataElement.getSize();
                }
                if (bl) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)uRLConnection;
                    if (l <= Integer.MAX_VALUE) {
                        httpURLConnection.setFixedLengthStreamingMode((int)l);
                    } else {
                        httpURLConnection.setChunkedStreamingMode(0);
                    }
                }
            } else if (!bl2 && uRLConnection instanceof HttpURLConnection) {
                uRLConnection.setRequestProperty("Content-Length", "0");
            }
            int n2 = bl2 ? 3 : 1;
            uRLConnection.setConnectTimeout(uRLConnection.getConnectTimeout() / n2);
            int n3 = 0;
            while (!this.canceled) {
                try {
                    uRLConnection.connect();
                    break;
                }
                catch (SocketTimeoutException socketTimeoutException) {
                    if (++n3 < n2) continue;
                    throw socketTimeoutException;
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    throw new MalformedURLException(this.url);
                }
            }
            if (n != 0) {
                outputStream = uRLConnection.getOutputStream();
                byte[] arrby = new byte[4096];
                long l2 = 0L;
                for (FormDataElement formDataElement : this.formDataElements) {
                    int n4;
                    InputStream inputStream = formDataElement.getInputStream();
                    while ((n4 = inputStream.read(arrby)) > 0) {
                        outputStream.write(arrby, 0, n4);
                        this.didSendData(l2 += (long)n4, l);
                    }
                    formDataElement.close();
                }
                outputStream.flush();
                outputStream.close();
                outputStream = null;
            }
        }
        finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                }
                catch (IOException iOException) {}
            }
            if (this.formDataElements != null && uRLConnection instanceof HttpURLConnection) {
                for (FormDataElement formDataElement : this.formDataElements) {
                    try {
                        formDataElement.close();
                    }
                    catch (IOException iOException) {}
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Redirect receiveResponse(URLConnection uRLConnection) throws IOException, InterruptedException {
        Object object;
        Object object2;
        Object object3;
        block35: {
            int n;
            if (this.canceled) {
                return null;
            }
            InputStream inputStream = null;
            if (uRLConnection instanceof HttpURLConnection) {
                object3 = (HttpURLConnection)uRLConnection;
                n = ((HttpURLConnection)object3).getResponseCode();
                if (n == -1) {
                    throw new InvalidResponseException();
                }
                if (this.canceled) {
                    return null;
                }
                switch (n) {
                    case 301: 
                    case 302: 
                    case 303: 
                    case 307: {
                        URL uRL;
                        object2 = ((URLConnection)object3).getHeaderField("Location");
                        if (object2 == null) break;
                        try {
                            uRL = URLs.newURL((String)object2);
                        }
                        catch (MalformedURLException malformedURLException) {
                            uRL = URLs.newURL(uRLConnection.getURL(), (String)object2);
                        }
                        return new Redirect(uRL.toExternalForm(), n == 307);
                    }
                    case 304: {
                        this.didReceiveResponse(uRLConnection);
                        this.didFinishLoading();
                        return null;
                    }
                }
                if (n >= 400 && !this.method.equals(HEAD)) {
                    inputStream = ((HttpURLConnection)object3).getErrorStream();
                }
            }
            if (this.url.startsWith("ftp:") || this.url.startsWith("ftps:")) {
                boolean bl = false;
                n = 0;
                object2 = uRLConnection.getURL().getPath();
                if (object2 == null || ((String)object2).isEmpty() || ((String)object2).endsWith("/") || ((String)object2).contains(";type=d")) {
                    bl = true;
                } else {
                    object = uRLConnection.getContentType();
                    if ("text/plain".equalsIgnoreCase((String)object) || "text/html".equalsIgnoreCase((String)object)) {
                        bl = true;
                        n = 1;
                    }
                }
                if (bl) {
                    uRLConnection = new DirectoryURLConnection(uRLConnection, n != 0);
                }
            }
            if (this.url.startsWith("file:") && "text/plain".equals(uRLConnection.getContentType()) && uRLConnection.getHeaderField("content-length") == null) {
                uRLConnection = new DirectoryURLConnection(uRLConnection);
            }
            this.didReceiveResponse(uRLConnection);
            if (this.method.equals(HEAD)) {
                this.didFinishLoading();
                return null;
            }
            object3 = null;
            try {
                object3 = inputStream == null ? uRLConnection.getInputStream() : inputStream;
            }
            catch (IOException iOException) {
                if (!logger.isLoggable(Level.FINE)) break block35;
                logger.log(Level.FINE, String.format("Exception caught: [%s], %s", iOException.getClass().getSimpleName(), iOException.getMessage()));
            }
        }
        String string = uRLConnection.getContentEncoding();
        if ("gzip".equalsIgnoreCase(string)) {
            object3 = new GZIPInputStream((InputStream)object3);
        } else if ("deflate".equalsIgnoreCase(string)) {
            object3 = new InflaterInputStream((InputStream)object3);
        }
        object2 = this.byteBufferPool.newAllocator(3);
        object = null;
        try {
            if (object3 != null) {
                byte[] arrby = new byte[8192];
                while (!this.canceled) {
                    int n;
                    int n2;
                    try {
                        n2 = ((InputStream)object3).read(arrby);
                    }
                    catch (EOFException eOFException) {
                        n2 = -1;
                    }
                    if (n2 == -1) break;
                    if (object == null) {
                        object = object2.allocate();
                    }
                    if (n2 < (n = ((Buffer)object).remaining())) {
                        ((ByteBuffer)object).put(arrby, 0, n2);
                        continue;
                    }
                    ((ByteBuffer)object).put(arrby, 0, n);
                    ((ByteBuffer)object).flip();
                    this.didReceiveData((ByteBuffer)object, (ByteBufferAllocator)object2);
                    object = null;
                    int n3 = n2 - n;
                    if (n3 <= 0) continue;
                    object = object2.allocate();
                    ((ByteBuffer)object).put(arrby, n, n3);
                }
            }
            if (!this.canceled) {
                if (object != null && ((Buffer)object).position() > 0) {
                    ((ByteBuffer)object).flip();
                    this.didReceiveData((ByteBuffer)object, (ByteBufferAllocator)object2);
                    object = null;
                }
                this.didFinishLoading();
            }
        }
        finally {
            if (object != null) {
                ((ByteBuffer)object).clear();
                object2.release((ByteBuffer)object);
            }
        }
        return null;
    }

    private static void close(URLConnection uRLConnection) {
        InputStream inputStream;
        if (uRLConnection instanceof HttpURLConnection && (inputStream = ((HttpURLConnection)uRLConnection).getErrorStream()) != null) {
            try {
                inputStream.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        try {
            uRLConnection.getInputStream().close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private void didSendData(long l, long l2) {
        this.callBack(() -> {
            if (!this.canceled) {
                this.notifyDidSendData(l, l2);
            }
        });
    }

    private void notifyDidSendData(long l, long l2) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("totalBytesSent: [%d], totalBytesToBeSent: [%d], data: [0x%016X]", l, l2, this.data));
        }
        URLLoader.twkDidSendData(l, l2, this.data);
    }

    private void willSendRequest(String string, String string2, URLConnection uRLConnection) throws InterruptedException {
        String string3 = URLLoader.adjustUrlForWebKit(string);
        int n = URLLoader.extractStatus(uRLConnection);
        String string4 = uRLConnection.getContentType();
        String string5 = URLLoader.extractContentEncoding(uRLConnection);
        long l = URLLoader.extractContentLength(uRLConnection);
        String string6 = URLLoader.extractHeaders(uRLConnection);
        String string7 = URLLoader.adjustUrlForWebKit(this.url);
        CountDownLatch countDownLatch = this.asynchronous ? new CountDownLatch(1) : null;
        this.callBack(() -> {
            try {
                boolean bl;
                if (!this.canceled && !(bl = this.notifyWillSendRequest(string3, string2, n, string4, string5, l, string6, string7))) {
                    this.fwkCancel();
                }
            }
            finally {
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }
        });
        if (countDownLatch != null) {
            countDownLatch.await();
        }
    }

    private boolean notifyWillSendRequest(String string, String string2, int n, String string3, String string4, long l, String string5, String string6) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("newUrl: [%s], newMethod: [%s], status: [%d], contentType: [%s], contentEncoding: [%s], contentLength: [%d], url: [%s], data: [0x%016X], headers:%n%s", string, string2, n, string3, string4, l, string6, this.data, Util.formatHeaders(string5)));
        }
        boolean bl = URLLoader.twkWillSendRequest(string, string2, n, string3, string4, l, string5, string6, this.data);
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("result: [%s]", bl));
        }
        return bl;
    }

    private void didReceiveResponse(URLConnection uRLConnection) {
        int n = URLLoader.extractStatus(uRLConnection);
        String string = uRLConnection.getContentType();
        String string2 = URLLoader.extractContentEncoding(uRLConnection);
        long l = URLLoader.extractContentLength(uRLConnection);
        String string3 = URLLoader.extractHeaders(uRLConnection);
        String string4 = URLLoader.adjustUrlForWebKit(this.url);
        this.callBack(() -> {
            if (!this.canceled) {
                this.notifyDidReceiveResponse(n, string, string2, l, string3, string4);
            }
        });
    }

    private void notifyDidReceiveResponse(int n, String string, String string2, long l, String string3, String string4) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("status: [%d], contentType: [%s], contentEncoding: [%s], contentLength: [%d], url: [%s], data: [0x%016X], headers:%n%s", n, string, string2, l, string4, this.data, Util.formatHeaders(string3)));
        }
        URLLoader.twkDidReceiveResponse(n, string, string2, l, string3, string4, this.data);
    }

    private void didReceiveData(ByteBuffer byteBuffer, ByteBufferAllocator byteBufferAllocator) {
        this.callBack(() -> {
            if (!this.canceled) {
                this.notifyDidReceiveData(byteBuffer, byteBuffer.position(), byteBuffer.remaining());
            }
            byteBuffer.clear();
            byteBufferAllocator.release(byteBuffer);
        });
    }

    private void notifyDidReceiveData(ByteBuffer byteBuffer, int n, int n2) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("byteBuffer: [%s], position: [%s], remaining: [%s], data: [0x%016X]", byteBuffer, n, n2, this.data));
        }
        URLLoader.twkDidReceiveData(byteBuffer, n, n2, this.data);
    }

    private void didFinishLoading() {
        this.callBack(() -> {
            if (!this.canceled) {
                this.notifyDidFinishLoading();
            }
        });
    }

    private void notifyDidFinishLoading() {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("data: [0x%016X]", this.data));
        }
        URLLoader.twkDidFinishLoading(this.data);
    }

    private void didFail(int n, String string) {
        String string2 = URLLoader.adjustUrlForWebKit(this.url);
        this.callBack(() -> {
            if (!this.canceled) {
                this.notifyDidFail(n, string2, string);
            }
        });
    }

    private void notifyDidFail(int n, String string, String string2) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("errorCode: [%d], url: [%s], message: [%s], data: [0x%016X]", n, string, string2, this.data));
        }
        URLLoader.twkDidFail(n, string, string2, this.data);
    }

    private void callBack(Runnable runnable) {
        if (this.asynchronous) {
            Invoker.getInvoker().invokeOnEventThread(runnable);
        } else {
            runnable.run();
        }
    }

    private static native void twkDidSendData(long var0, long var2, long var4);

    private static native boolean twkWillSendRequest(String var0, String var1, int var2, String var3, String var4, long var5, String var7, String var8, long var9);

    private static native void twkDidReceiveResponse(int var0, String var1, String var2, long var3, String var5, String var6, long var7);

    private static native void twkDidReceiveData(ByteBuffer var0, int var1, int var2, long var3);

    private static native void twkDidFinishLoading(long var0);

    private static native void twkDidFail(int var0, String var1, String var2, long var3);

    private static int extractStatus(URLConnection uRLConnection) {
        int n = 0;
        if (uRLConnection instanceof HttpURLConnection) {
            try {
                n = ((HttpURLConnection)uRLConnection).getResponseCode();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return n;
    }

    private static String extractContentEncoding(URLConnection uRLConnection) {
        String string = uRLConnection.getContentEncoding();
        if ("gzip".equalsIgnoreCase(string) || "deflate".equalsIgnoreCase(string)) {
            int n;
            string = null;
            String string2 = uRLConnection.getContentType();
            if (string2 != null && (n = string2.indexOf("charset=")) >= 0 && (n = (string = string2.substring(n + 8)).indexOf(";")) > 0) {
                string = string.substring(0, n);
            }
        }
        return string;
    }

    private static long extractContentLength(URLConnection uRLConnection) {
        try {
            return Long.parseLong(uRLConnection.getHeaderField("content-length"));
        }
        catch (Exception exception) {
            return -1L;
        }
    }

    private static String extractHeaders(URLConnection uRLConnection) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, List<String>> map = uRLConnection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String string = entry.getKey();
            List<String> list = entry.getValue();
            for (String string2 : list) {
                stringBuilder.append(string != null ? string : "");
                stringBuilder.append(':').append(string2).append('\n');
            }
        }
        return stringBuilder.toString();
    }

    private static String adjustUrlForWebKit(String string) {
        try {
            string = Util.adjustUrlForWebKit(string);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return string;
    }

    private static final class TooManyRedirectsException
    extends IOException {
        private TooManyRedirectsException() {
            super("Too many redirects");
        }
    }

    private static final class InvalidResponseException
    extends IOException {
        private InvalidResponseException() {
            super("Invalid server response");
        }
    }

    private static final class Redirect {
        private final String url;
        private final boolean preserveRequest;

        private Redirect(String string, boolean bl) {
            this.url = string;
            this.preserveRequest = bl;
        }
    }
}

