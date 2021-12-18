/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.web.WebEngine
 */
package com.sun.javafx.webkit.drt;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.webkit.drt.EventSender;
import com.sun.javafx.webkit.drt.UIClientImpl;
import com.sun.webkit.BackForwardList;
import com.sun.webkit.Invoker;
import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.PageCache;
import com.sun.webkit.ThemeClient;
import com.sun.webkit.WebPage;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCSize;
import com.sun.webkit.network.URLs;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javafx.scene.web.WebEngine;

public final class DumpRenderTree {
    private static final Logger log = Logger.getLogger("DumpRenderTree");
    private static final long PID = new Date().getTime() & 0xFFFFL;
    private static final String fileSep = System.getProperty("file.separator");
    private static boolean forceDumpAsText = false;
    static final PrintWriter out;
    static volatile DumpRenderTree drt;
    private final WebPage webPage;
    private final UIClientImpl uiClient;
    private final EventSender eventSender;
    private final CountDownLatch latch;
    private final String testPath;
    private String pixelsHash = "";
    private boolean loaded;
    private boolean waiting;
    private boolean complete;
    private static final String TEST_DIR_NAME = "LayoutTests";
    private static final int TEST_DIR_LEN;
    private static final String CUR_ITEM_STR = "curr->";
    private static final int CUR_ITEM_STR_LEN;
    private static final String INDENT = "    ";
    private BackForwardList bfl;

    private DumpRenderTree(String string, CountDownLatch countDownLatch) {
        int n = string.indexOf("'");
        if (n > 0 && n < string.length() - 1) {
            this.pixelsHash = string.substring(n + 1);
            string = string.substring(0, n);
        }
        this.testPath = string;
        this.latch = countDownLatch;
        drt = this;
        this.uiClient = new UIClientImpl();
        this.webPage = new WebPage(new WebPageClientImpl(), this.uiClient, null, null, new ThemeClientImplStub(), false);
        this.uiClient.setWebPage(this.webPage);
        this.eventSender = new EventSender(this.webPage);
        this.webPage.setBounds(0, 0, 800, 600);
        this.webPage.setUsePageCache(true);
        this.webPage.setDeveloperExtrasEnabled(true);
        this.webPage.addLoadListenerClient(new DRTLoadListener());
        DumpRenderTree.init(this.testPath, this.pixelsHash);
    }

    private void run() {
        String string = this.testPath;
        DumpRenderTree.mlog("{runTest: " + string);
        long l = this.webPage.getMainFrame();
        try {
            new URL(string);
        }
        catch (MalformedURLException malformedURLException) {
            string = "file:///" + string;
        }
        this.webPage.open(l, string);
        DumpRenderTree.mlog("}runTest");
    }

    private static boolean isDebug() {
        return log.isLoggable(Level.FINE);
    }

    private static void mlog(String string) {
        if (DumpRenderTree.isDebug()) {
            log.fine("PID:" + Long.toHexString(PID) + " TID:" + Thread.currentThread().getId() + "(" + Thread.currentThread().getName() + ") " + string);
        }
    }

    private static void initPlatform() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        PlatformImpl.startup(() -> {
            new WebEngine();
            System.loadLibrary("DumpRenderTreeJava");
            PageCache.setCapacity(1);
            countDownLatch.countDown();
        });
        countDownLatch.await();
    }

    private static void runTest(String string) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Invoker.getInvoker().invokeOnEventThread(() -> new DumpRenderTree(string, countDownLatch).run());
        countDownLatch.await();
        Invoker.getInvoker().invokeOnEventThread(() -> {
            DumpRenderTree.mlog("dispose");
            DumpRenderTree.drt.uiClient.closePage();
            DumpRenderTree.dispose();
        });
    }

    private static void waitUntilDone() {
        DumpRenderTree.mlog("waitUntilDone");
        drt.setWaiting(true);
    }

    private static void notifyDone() {
        DumpRenderTree.mlog("notifyDone");
        drt.setWaiting(false);
    }

    private synchronized void setLoaded(boolean bl) {
        this.loaded = bl;
        this.done();
    }

    private synchronized void setWaiting(boolean bl) {
        this.waiting = bl;
        this.done();
    }

    private synchronized void dump(long l) {
        boolean bl = DumpRenderTree.dumpAsText() || forceDumpAsText;
        DumpRenderTree.mlog("dumpAsText = " + bl);
        if (bl) {
            List<Long> list;
            String string = this.webPage.getInnerText(l);
            if (l == this.webPage.getMainFrame()) {
                if (string != null) {
                    out.print(string + '\n');
                }
            } else {
                out.printf("\n--------\nFrame: '%s'\n--------\n%s\n", this.webPage.getName(l), string);
            }
            if (DumpRenderTree.dumpChildFramesAsText() && (list = this.webPage.getChildFrames(l)) != null) {
                for (long l2 : list) {
                    this.dump(l2);
                }
            }
            if (DumpRenderTree.dumpBackForwardList() && l == this.webPage.getMainFrame()) {
                drt.dumpBfl();
            }
        } else {
            String string = this.webPage.getRenderTree(l);
            out.print(string);
        }
    }

    private synchronized void done() {
        if (this.waiting || !this.loaded || this.complete) {
            return;
        }
        DumpRenderTree.mlog("dump");
        this.dump(this.webPage.getMainFrame());
        DumpRenderTree.mlog("done");
        out.print("#EOF\n");
        out.print("#EOF\n");
        out.flush();
        System.err.print("#EOF\n");
        System.err.flush();
        this.complete = true;
        this.latch.countDown();
    }

    private static native void init(String var0, String var1);

    private static native void didClearWindowObject(long var0, long var2, EventSender var4);

    private static native void dispose();

    private static native boolean dumpAsText();

    private static native boolean dumpChildFramesAsText();

    private static native boolean dumpBackForwardList();

    public static void main(String[] arrstring) throws Exception {
        if (DumpRenderTree.isDebug()) {
            log.setLevel(Level.FINEST);
            FileHandler fileHandler = new FileHandler("drt.log", true);
            fileHandler.setFormatter(new Formatter(){

                @Override
                public String format(LogRecord logRecord) {
                    return this.formatMessage(logRecord) + "\n";
                }
            });
            log.addHandler(fileHandler);
        }
        DumpRenderTree.mlog("{main");
        DumpRenderTree.initPlatform();
        for (String string : arrstring) {
            if ("--dump-as-text".equals(string)) {
                forceDumpAsText = true;
                continue;
            }
            if ("-".equals(string)) {
                String string2;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                while ((string2 = bufferedReader.readLine()) != null) {
                    DumpRenderTree.runTest(string2);
                }
                bufferedReader.close();
                continue;
            }
            DumpRenderTree.runTest(string);
        }
        PlatformImpl.exit();
        DumpRenderTree.mlog("}main");
        System.exit(0);
    }

    private static int getWorkerThreadCount() {
        return WebPage.getWorkerThreadCount();
    }

    private static String resolveURL(String string) {
        String string2 = new File(DumpRenderTree.drt.testPath).getParentFile().getPath();
        File file = new File(string2, string);
        String string3 = "file:///" + file.toString().replace(fileSep, "/");
        DumpRenderTree.mlog("resolveURL: " + string3);
        return string3;
    }

    private static void loadURL(String string) {
        DumpRenderTree.drt.webPage.open(DumpRenderTree.drt.webPage.getMainFrame(), string);
    }

    private static void goBackForward(int n) {
        if (n > 0) {
            DumpRenderTree.drt.webPage.goForward();
        } else {
            DumpRenderTree.drt.webPage.goBack();
        }
    }

    private static int getBackForwardItemCount() {
        return drt.getBackForwardList().size();
    }

    private BackForwardList getBackForwardList() {
        if (this.bfl == null) {
            this.bfl = this.webPage.createBackForwardList();
        }
        return this.bfl;
    }

    private void dumpBfl() {
        out.print("\n============== Back Forward List ==============\n");
        this.getBackForwardList();
        BackForwardList.Entry entry = this.bfl.getCurrentEntry();
        for (BackForwardList.Entry entry2 : this.bfl.toArray()) {
            this.dumpBflItem(entry2, 2, entry2 == entry);
        }
        out.print("===============================================\n");
    }

    private void dumpBflItem(BackForwardList.Entry entry, int n, boolean bl) {
        String string;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = n; i > 0; --i) {
            stringBuilder.append("    ");
        }
        if (bl) {
            stringBuilder.replace(0, CUR_ITEM_STR_LEN, "curr->");
        }
        if ((string = entry.getURL().toString()).contains("file:/")) {
            BackForwardList.Entry[] arrentry = string.substring(string.indexOf("LayoutTests") + TEST_DIR_LEN + 1);
            stringBuilder.append("(file test):" + (String)arrentry);
        } else {
            stringBuilder.append(string);
        }
        if (entry.getTarget() != null) {
            stringBuilder.append(" (in frame \"" + entry.getTarget() + "\")");
        }
        if (entry.isTargetItem()) {
            stringBuilder.append("  **nav target**\n");
        } else {
            stringBuilder.append("\n");
        }
        out.print(stringBuilder);
        if (entry.getChildren() != null) {
            for (BackForwardList.Entry entry2 : entry.getChildren()) {
                this.dumpBflItem(entry2, n + 1, false);
            }
        }
    }

    void dumpUnloadListeners(WebPage webPage, long l) {
        String string;
        if (this.waiting && DumpRenderTree.dumpAsText() && (string = DumpRenderTree.getUnloadListenersDescription(webPage, l)) != null) {
            out.print(string + '\n');
        }
    }

    private static String getUnloadListenersDescription(WebPage webPage, long l) {
        int n = webPage.getUnloadEventListenersCount(l);
        if (n > 0) {
            return DumpRenderTree.getFrameDescription(webPage, l) + " - has " + n + " onunload handler(s)";
        }
        return null;
    }

    private static String getFrameDescription(WebPage webPage, long l) {
        String string = webPage.getName(l);
        if (l == webPage.getMainFrame()) {
            return string == null ? "main frame" : "main frame " + string;
        }
        return string == null ? "frame (anonymous)" : "frame " + string;
    }

    private static native boolean didFinishLoad();

    static {
        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter((OutputStream)System.out, "UTF-8")), true);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException(unsupportedEncodingException);
        }
        TEST_DIR_LEN = "LayoutTests".length();
        CUR_ITEM_STR_LEN = "curr->".length();
    }

    private final class WebPageClientImpl
    implements WebPageClient<Void> {
        private WebPageClientImpl() {
        }

        @Override
        public void setCursor(long l) {
        }

        @Override
        public void setFocus(boolean bl) {
        }

        @Override
        public void transferFocus(boolean bl) {
        }

        @Override
        public void setTooltip(String string) {
        }

        @Override
        public WCRectangle getScreenBounds(boolean bl) {
            return null;
        }

        @Override
        public int getScreenDepth() {
            return 24;
        }

        @Override
        public Void getContainer() {
            return null;
        }

        @Override
        public WCPoint screenToWindow(WCPoint wCPoint) {
            return wCPoint;
        }

        @Override
        public WCPoint windowToScreen(WCPoint wCPoint) {
            return wCPoint;
        }

        @Override
        public WCPageBackBuffer createBackBuffer() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isBackBufferSupported() {
            return false;
        }

        @Override
        public void addMessageToConsole(String string, int n, String string2) {
            int n2;
            if (!string.isEmpty() && (n2 = string.indexOf("file://")) != -1) {
                String string3 = string.substring(0, n2);
                String string4 = string.substring(n2);
                try {
                    string4 = new File(URLs.newURL(string4).getPath()).getName();
                }
                catch (MalformedURLException malformedURLException) {
                    // empty catch block
                }
                string = string3 + string4;
            }
            if (n == 0) {
                out.printf("CONSOLE MESSAGE: %s\n", string);
            } else {
                out.printf("CONSOLE MESSAGE: line %d: %s\n", n, string);
            }
        }

        @Override
        public void didClearWindowObject(long l, long l2) {
            DumpRenderTree.mlog("didClearWindowObject");
            DumpRenderTree.didClearWindowObject(l, l2, DumpRenderTree.this.eventSender);
        }
    }

    private final class DRTLoadListener
    implements LoadListenerClient {
        private DRTLoadListener() {
        }

        @Override
        public void dispatchLoadEvent(long l, int n, String string, String string2, double d, int n2) {
            DumpRenderTree.mlog("dispatchLoadEvent: ENTER");
            if (l == DumpRenderTree.this.webPage.getMainFrame()) {
                DumpRenderTree.mlog("dispatchLoadEvent: STATE = " + n);
                switch (n) {
                    case 0: {
                        DumpRenderTree.mlog("PAGE_STARTED");
                        DumpRenderTree.this.setLoaded(false);
                        break;
                    }
                    case 1: {
                        DumpRenderTree.mlog("PAGE_FINISHED");
                        if (!DumpRenderTree.didFinishLoad()) break;
                        DumpRenderTree.this.setLoaded(true);
                        break;
                    }
                    case 14: {
                        DumpRenderTree.this.dumpUnloadListeners(DumpRenderTree.this.webPage, l);
                        break;
                    }
                    case 5: {
                        DumpRenderTree.mlog("LOAD_FAILED");
                        DumpRenderTree.this.setLoaded(true);
                    }
                }
            }
            DumpRenderTree.mlog("dispatchLoadEvent: EXIT");
        }

        @Override
        public void dispatchResourceLoadEvent(long l, int n, String string, String string2, double d, int n2) {
        }
    }

    class ScrollBarThemeStub
    extends ScrollBarTheme {
        ScrollBarThemeStub() {
        }

        @Override
        protected Ref createWidget(long l, int n, int n2, int n3, int n4, int n5, int n6) {
            return null;
        }

        @Override
        public void paint(WCGraphicsContext wCGraphicsContext, Ref ref, int n, int n2, int n3, int n4) {
        }

        @Override
        protected int hitTest(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
            return 0;
        }

        @Override
        protected int getThumbPosition(int n, int n2, int n3, int n4, int n5, int n6) {
            return 0;
        }

        @Override
        protected int getThumbLength(int n, int n2, int n3, int n4, int n5, int n6) {
            return 0;
        }

        @Override
        protected int getTrackPosition(int n, int n2, int n3) {
            return 0;
        }

        @Override
        protected int getTrackLength(int n, int n2, int n3) {
            return 0;
        }

        @Override
        public WCSize getWidgetSize(Ref ref) {
            return new WCSize(0.0f, 0.0f);
        }
    }

    class RenderThemeStub
    extends RenderTheme {
        RenderThemeStub() {
        }

        @Override
        protected Ref createWidget(long l, int n, int n2, int n3, int n4, int n5, ByteBuffer byteBuffer) {
            return null;
        }

        @Override
        public void drawWidget(WCGraphicsContext wCGraphicsContext, Ref ref, int n, int n2) {
        }

        @Override
        protected int getRadioButtonSize() {
            return 0;
        }

        @Override
        protected int getSelectionColor(int n) {
            return 0;
        }

        @Override
        public WCSize getWidgetSize(Ref ref) {
            return new WCSize(0.0f, 0.0f);
        }
    }

    class ThemeClientImplStub
    extends ThemeClient {
        ThemeClientImplStub() {
        }

        @Override
        protected RenderTheme createRenderTheme() {
            return new RenderThemeStub();
        }

        @Override
        protected ScrollBarTheme createScrollBarTheme() {
            return new ScrollBarThemeStub();
        }
    }
}

