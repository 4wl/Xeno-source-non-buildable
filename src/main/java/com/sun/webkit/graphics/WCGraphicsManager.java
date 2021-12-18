/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.SharedBuffer;
import com.sun.webkit.SimpleSharedBufferInputStream;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCFontCustomPlatformData;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCImageDecoder;
import com.sun.webkit.graphics.WCImageFrame;
import com.sun.webkit.graphics.WCMediaPlayer;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCRenderQueue;
import com.sun.webkit.graphics.WCTransform;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WCGraphicsManager {
    private static final Logger logger = Logger.getLogger(WCGraphicsManager.class.getName());
    private final AtomicInteger idCount = new AtomicInteger(0);
    private final HashMap<Integer, Ref> refMap = new HashMap();
    private static ResourceBundle imageProperties = null;
    private static WCGraphicsManager manager = null;

    public static void setGraphicsManager(WCGraphicsManager wCGraphicsManager) {
        manager = wCGraphicsManager;
    }

    public static WCGraphicsManager getGraphicsManager() {
        return manager;
    }

    public abstract float getDevicePixelScale();

    protected abstract WCImageDecoder getImageDecoder();

    public abstract WCGraphicsContext createGraphicsContext(Object var1);

    public abstract WCRenderQueue createRenderQueue(WCRectangle var1, boolean var2);

    protected abstract WCRenderQueue createBufferedContextRQ(WCImage var1);

    public abstract WCPageBackBuffer createPageBackBuffer();

    protected abstract WCFont getWCFont(String var1, boolean var2, boolean var3, float var4);

    private WCFontCustomPlatformData fwkCreateFontCustomPlatformData(SharedBuffer sharedBuffer) {
        try {
            return this.createFontCustomPlatformData(new SimpleSharedBufferInputStream(sharedBuffer));
        }
        catch (IOException iOException) {
            logger.log(Level.FINEST, "Error creating font custom platform data", iOException);
            return null;
        }
    }

    protected abstract WCFontCustomPlatformData createFontCustomPlatformData(InputStream var1) throws IOException;

    protected abstract WCPath createWCPath();

    protected abstract WCPath createWCPath(WCPath var1);

    protected abstract WCImage createWCImage(int var1, int var2);

    protected abstract WCImage createRTImage(int var1, int var2);

    public abstract WCImage getIconImage(String var1);

    public abstract Object toPlatformImage(WCImage var1);

    protected abstract WCImageFrame createFrame(int var1, int var2, ByteBuffer var3);

    public static String getResourceName(String string) {
        if (imageProperties == null) {
            imageProperties = ResourceBundle.getBundle("com.sun.webkit.graphics.Images");
        }
        try {
            return imageProperties.getString(string);
        }
        catch (MissingResourceException missingResourceException) {
            return string;
        }
    }

    private void fwkLoadFromResource(String string, long l) {
        InputStream inputStream = this.getClass().getResourceAsStream(WCGraphicsManager.getResourceName(string));
        if (inputStream == null) {
            return;
        }
        byte[] arrby = new byte[1024];
        try {
            int n;
            while ((n = inputStream.read(arrby)) > -1) {
                WCGraphicsManager.append(l, arrby, n);
            }
            inputStream.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    protected abstract WCTransform createTransform(double var1, double var3, double var5, double var7, double var9, double var11);

    protected String[] getSupportedMediaTypes() {
        return new String[0];
    }

    private WCMediaPlayer fwkCreateMediaPlayer(long l) {
        WCMediaPlayer wCMediaPlayer = this.createMediaPlayer();
        wCMediaPlayer.setNativePointer(l);
        return wCMediaPlayer;
    }

    protected abstract WCMediaPlayer createMediaPlayer();

    int createID() {
        return this.idCount.incrementAndGet();
    }

    synchronized void ref(Ref ref) {
        this.refMap.put(ref.getID(), ref);
    }

    synchronized Ref deref(Ref ref) {
        return this.refMap.remove(ref.getID());
    }

    synchronized Ref getRef(int n) {
        return this.refMap.get(n);
    }

    private static native void append(long var0, byte[] var2, int var3);
}

