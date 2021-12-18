/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Point2D
 *  javafx.geometry.Rectangle2D
 *  javafx.scene.Cursor
 *  javafx.scene.Node
 *  javafx.scene.Scene
 *  javafx.scene.control.Tooltip
 *  javafx.scene.web.WebView
 *  javafx.stage.Screen
 *  javafx.stage.Window
 */
package com.sun.javafx.webkit;

import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.util.Utils;
import com.sun.javafx.webkit.Accessor;
import com.sun.javafx.webkit.WebConsoleListener;
import com.sun.webkit.CursorManager;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import java.security.AccessController;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Window;

public final class WebPageClientImpl
implements WebPageClient<WebView> {
    private static final boolean backBufferSupported;
    private static WebConsoleListener consoleListener;
    private final Accessor accessor;
    private Tooltip tooltip;
    private boolean isTooltipRegistered = false;

    static void setConsoleListener(WebConsoleListener webConsoleListener) {
        consoleListener = webConsoleListener;
    }

    public WebPageClientImpl(Accessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public void setFocus(boolean bl) {
        WebView webView = this.accessor.getView();
        if (webView != null && bl) {
            webView.requestFocus();
        }
    }

    @Override
    public void setCursor(long l) {
        WebView webView = this.accessor.getView();
        if (webView != null) {
            Object t = CursorManager.getCursorManager().getCursor(l);
            webView.setCursor(t instanceof Cursor ? (Cursor)t : Cursor.DEFAULT);
        }
    }

    @Override
    public void setTooltip(String string) {
        WebView webView = this.accessor.getView();
        if (string != null) {
            if (this.tooltip == null) {
                this.tooltip = new Tooltip(string);
            } else {
                this.tooltip.setText(string);
            }
            if (!this.isTooltipRegistered) {
                Tooltip.install((Node)webView, (Tooltip)this.tooltip);
                this.isTooltipRegistered = true;
            }
        } else if (this.isTooltipRegistered) {
            Tooltip.uninstall((Node)webView, (Tooltip)this.tooltip);
            this.isTooltipRegistered = false;
        }
    }

    @Override
    public void transferFocus(boolean bl) {
        this.accessor.getView().impl_traverse(bl ? Direction.NEXT : Direction.PREVIOUS);
    }

    @Override
    public WCRectangle getScreenBounds(boolean bl) {
        WebView webView = this.accessor.getView();
        Screen screen = Utils.getScreen((Object)webView);
        if (screen != null) {
            Rectangle2D rectangle2D = bl ? screen.getVisualBounds() : screen.getBounds();
            return new WCRectangle((float)rectangle2D.getMinX(), (float)rectangle2D.getMinY(), (float)rectangle2D.getWidth(), (float)rectangle2D.getHeight());
        }
        return null;
    }

    @Override
    public int getScreenDepth() {
        return 24;
    }

    @Override
    public WebView getContainer() {
        return this.accessor.getView();
    }

    @Override
    public WCPoint screenToWindow(WCPoint wCPoint) {
        WebView webView = this.accessor.getView();
        Scene scene = webView.getScene();
        Window window = null;
        if (scene != null && (window = scene.getWindow()) != null) {
            Point2D point2D = webView.sceneToLocal((double)wCPoint.getX() - window.getX() - scene.getX(), (double)wCPoint.getY() - window.getY() - scene.getY());
            return new WCPoint((float)point2D.getX(), (float)point2D.getY());
        }
        return new WCPoint(0.0f, 0.0f);
    }

    @Override
    public WCPoint windowToScreen(WCPoint wCPoint) {
        WebView webView = this.accessor.getView();
        Scene scene = webView.getScene();
        Window window = null;
        if (scene != null && (window = scene.getWindow()) != null) {
            Point2D point2D = webView.localToScene((double)wCPoint.getX(), (double)wCPoint.getY());
            return new WCPoint((float)(point2D.getX() + scene.getX() + window.getX()), (float)(point2D.getY() + scene.getY() + window.getY()));
        }
        return new WCPoint(0.0f, 0.0f);
    }

    @Override
    public WCPageBackBuffer createBackBuffer() {
        if (this.isBackBufferSupported()) {
            return WCGraphicsManager.getGraphicsManager().createPageBackBuffer();
        }
        return null;
    }

    @Override
    public boolean isBackBufferSupported() {
        return backBufferSupported;
    }

    @Override
    public void addMessageToConsole(String string, int n, String string2) {
        if (consoleListener != null) {
            try {
                consoleListener.messageAdded(this.accessor.getView(), string, n, string2);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void didClearWindowObject(long l, long l2) {
    }

    static {
        consoleListener = null;
        backBufferSupported = Boolean.valueOf(AccessController.doPrivileged(() -> System.getProperty("com.sun.webkit.pagebackbuffer", "true")));
    }
}

