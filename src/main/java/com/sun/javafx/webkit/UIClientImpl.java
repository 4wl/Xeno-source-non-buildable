/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.geometry.Rectangle2D
 *  javafx.scene.image.Image
 *  javafx.scene.input.ClipboardContent
 *  javafx.scene.input.DataFormat
 *  javafx.scene.input.Dragboard
 *  javafx.scene.input.TransferMode
 *  javafx.scene.web.PopupFeatures
 *  javafx.scene.web.PromptData
 *  javafx.scene.web.WebEngine
 *  javafx.scene.web.WebEvent
 *  javafx.scene.web.WebView
 *  javafx.stage.FileChooser
 *  javafx.stage.Window
 */
package com.sun.javafx.webkit;

import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.UIClient;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCRectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Map;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.PromptData;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

public final class UIClientImpl
implements UIClient {
    private final Accessor accessor;
    private FileChooser chooser;
    private ClipboardContent content;
    private static final DataFormat DF_DRAG_IMAGE = UIClientImpl.getDataFormat("application/x-java-drag-image");
    private static final DataFormat DF_DRAG_IMAGE_OFFSET = UIClientImpl.getDataFormat("application/x-java-drag-image-offset");

    public UIClientImpl(Accessor accessor) {
        this.accessor = accessor;
    }

    private WebEngine getWebEngine() {
        return this.accessor.getEngine();
    }

    private AccessControlContext getAccessContext() {
        return this.accessor.getPage().getAccessControlContext();
    }

    @Override
    public WebPage createPage(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getCreatePopupHandler() != null) {
            PopupFeatures popupFeatures = new PopupFeatures(bl, bl2, bl3, bl4);
            WebEngine webEngine2 = AccessController.doPrivileged(() -> (WebEngine)webEngine.getCreatePopupHandler().call((Object)popupFeatures), this.getAccessContext());
            return Accessor.getPageFor(webEngine2);
        }
        return null;
    }

    private void dispatchWebEvent(EventHandler eventHandler, WebEvent webEvent) {
        AccessController.doPrivileged(() -> {
            eventHandler.handle((Event)webEvent);
            return null;
        }, this.getAccessContext());
    }

    private void notifyVisibilityChanged(boolean bl) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getOnVisibilityChanged() != null) {
            this.dispatchWebEvent(webEngine.getOnVisibilityChanged(), new WebEvent((Object)webEngine, WebEvent.VISIBILITY_CHANGED, (Object)bl));
        }
    }

    @Override
    public void closePage() {
        this.notifyVisibilityChanged(false);
    }

    @Override
    public void showView() {
        this.notifyVisibilityChanged(true);
    }

    @Override
    public WCRectangle getViewBounds() {
        WebView webView = this.accessor.getView();
        Window window = null;
        if (webView != null && webView.getScene() != null && (window = webView.getScene().getWindow()) != null) {
            return new WCRectangle((float)window.getX(), (float)window.getY(), (float)window.getWidth(), (float)window.getHeight());
        }
        return null;
    }

    @Override
    public void setViewBounds(WCRectangle wCRectangle) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getOnResized() != null) {
            this.dispatchWebEvent(webEngine.getOnResized(), new WebEvent((Object)webEngine, WebEvent.RESIZED, (Object)new Rectangle2D((double)wCRectangle.getX(), (double)wCRectangle.getY(), (double)wCRectangle.getWidth(), (double)wCRectangle.getHeight())));
        }
    }

    @Override
    public void setStatusbarText(String string) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getOnStatusChanged() != null) {
            this.dispatchWebEvent(webEngine.getOnStatusChanged(), new WebEvent((Object)webEngine, WebEvent.STATUS_CHANGED, (Object)string));
        }
    }

    @Override
    public void alert(String string) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getOnAlert() != null) {
            this.dispatchWebEvent(webEngine.getOnAlert(), new WebEvent((Object)webEngine, WebEvent.ALERT, (Object)string));
        }
    }

    @Override
    public boolean confirm(String string) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getConfirmHandler() != null) {
            return AccessController.doPrivileged(() -> (Boolean)webEngine.getConfirmHandler().call((Object)string), this.getAccessContext());
        }
        return false;
    }

    @Override
    public String prompt(String string, String string2) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getPromptHandler() != null) {
            PromptData promptData = new PromptData(string, string2);
            return AccessController.doPrivileged(() -> (String)webEngine.getPromptHandler().call((Object)promptData), this.getAccessContext());
        }
        return "";
    }

    @Override
    public String[] chooseFile(String string, boolean bl) {
        String[] arrstring;
        Object object;
        Window window = null;
        WebView webView = this.accessor.getView();
        if (webView != null && webView.getScene() != null) {
            window = webView.getScene().getWindow();
        }
        if (this.chooser == null) {
            this.chooser = new FileChooser();
        }
        if (string != null) {
            for (object = new File(string); object != null && !((File)object).isDirectory(); object = ((File)object).getParentFile()) {
            }
            this.chooser.setInitialDirectory((File)object);
        }
        if (bl) {
            object = this.chooser.showOpenMultipleDialog(window);
            if (object != null) {
                int n = object.size();
                String[] arrstring2 = new String[n];
                for (int i = 0; i < n; ++i) {
                    arrstring2[i] = ((File)object.get(i)).getAbsolutePath();
                }
                return arrstring2;
            }
            return null;
        }
        object = this.chooser.showOpenDialog(window);
        if (object != null) {
            String[] arrstring3 = new String[1];
            arrstring = arrstring3;
            arrstring3[0] = ((File)object).getAbsolutePath();
        } else {
            arrstring = null;
        }
        return arrstring;
    }

    @Override
    public void print() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static DataFormat getDataFormat(String string) {
        Class<DataFormat> class_ = DataFormat.class;
        synchronized (DataFormat.class) {
            DataFormat dataFormat = DataFormat.lookupMimeType((String)string);
            if (dataFormat == null) {
                dataFormat = new DataFormat(new String[]{string});
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return dataFormat;
        }
    }

    @Override
    public void startDrag(WCImage wCImage, int n, int n2, int n3, int n4, String[] arrstring, Object[] arrobject) {
        this.content = new ClipboardContent();
        for (int i = 0; i < arrstring.length; ++i) {
            if (arrobject[i] == null) continue;
            try {
                this.content.put((Object)UIClientImpl.getDataFormat(arrstring[i]), "text/ie-shortcut-filename".equals(arrstring[i]) ? ByteBuffer.wrap(((String)arrobject[i]).getBytes("UTF-16LE")) : arrobject[i]);
                continue;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                // empty catch block
            }
        }
        if (wCImage != null) {
            Object object;
            ByteBuffer byteBuffer = ByteBuffer.allocate(8);
            byteBuffer.rewind();
            byteBuffer.putInt(n);
            byteBuffer.putInt(n2);
            this.content.put((Object)DF_DRAG_IMAGE_OFFSET, (Object)byteBuffer);
            int n5 = wCImage.getWidth();
            int n6 = wCImage.getHeight();
            ByteBuffer byteBuffer2 = wCImage.getPixelBuffer();
            ByteBuffer byteBuffer3 = ByteBuffer.allocate(8 + n5 * n6 * 4);
            byteBuffer3.putInt(n5);
            byteBuffer3.putInt(n6);
            byteBuffer3.put(byteBuffer2);
            this.content.put((Object)DF_DRAG_IMAGE, (Object)byteBuffer3);
            Object object2 = object = wCImage.getWidth() > 0 && wCImage.getHeight() > 0 ? wCImage.getPlatformImage() : null;
            if (object != null) {
                try {
                    File file = File.createTempFile("jfx", ".png");
                    file.deleteOnExit();
                    ImageIO.write((RenderedImage)UIClientImpl.toBufferedImage(Image.impl_fromPlatformImage((Object)Toolkit.getToolkit().loadPlatformImage(object))), "png", file);
                    this.content.put((Object)DataFormat.FILES, Arrays.asList(file));
                }
                catch (IOException | SecurityException exception) {
                    // empty catch block
                }
            }
        }
    }

    @Override
    public void confirmStartDrag() {
        WebView webView = this.accessor.getView();
        if (webView != null && this.content != null) {
            Dragboard dragboard = webView.startDragAndDrop(TransferMode.ANY);
            dragboard.setContent((Map)this.content);
        }
        this.content = null;
    }

    @Override
    public boolean isDragConfirmed() {
        return this.accessor.getView() != null && this.content != null;
    }

    public static BufferedImage toBufferedImage(Image image) {
        try {
            Class<?> class_ = Class.forName("javafx.embed.swing.SwingFXUtils");
            Method method = class_.getMethod("fromFXImage", Image.class, BufferedImage.class);
            Object object = method.invoke(null, new Object[]{image, null});
            return (BufferedImage)object;
        }
        catch (Exception exception) {
            exception.printStackTrace(System.err);
            return null;
        }
    }
}

