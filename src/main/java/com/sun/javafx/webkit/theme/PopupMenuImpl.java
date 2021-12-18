/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.MenuItem
 *  javafx.scene.web.WebView
 */
package com.sun.javafx.webkit.theme;

import com.sun.javafx.webkit.theme.ContextMenuImpl;
import com.sun.webkit.Invoker;
import com.sun.webkit.PopupMenu;
import com.sun.webkit.WebPage;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCPoint;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebView;

public final class PopupMenuImpl
extends PopupMenu {
    private static final Logger log = Logger.getLogger(PopupMenuImpl.class.getName());
    private final ContextMenu popupMenu = new ContextMenu();

    public PopupMenuImpl() {
        this.popupMenu.setOnHidden(windowEvent -> {
            log.finer("onHidden");
            Invoker.getInvoker().postOnEventThread(() -> {
                log.finer("onHidden: notifying");
                this.notifyPopupClosed();
            });
        });
        this.popupMenu.setOnAction(actionEvent -> {
            MenuItem menuItem = (MenuItem)actionEvent.getTarget();
            log.log(Level.FINE, "onAction: item={0}", (Object)menuItem);
            this.notifySelectionCommited(this.popupMenu.getItems().indexOf((Object)menuItem));
        });
    }

    @Override
    protected void show(WebPage webPage, int n, int n2, int n3) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "show at [{0}, {1}], width={2}", new Object[]{n, n2, n3});
        }
        this.popupMenu.setPrefWidth((double)n3);
        this.popupMenu.setPrefHeight(this.popupMenu.getHeight());
        PopupMenuImpl.doShow(this.popupMenu, webPage, n, n2);
    }

    @Override
    protected void hide() {
        log.fine("hiding");
        this.popupMenu.hide();
    }

    @Override
    protected void appendItem(String string, boolean bl, boolean bl2, boolean bl3, int n, int n2, WCFont wCFont) {
        ContextMenuImpl.SeparatorImpl separatorImpl;
        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, "itemText={0}, isLabel={1}, isSeparator={2}, isEnabled={3}, bgColor={4}, fgColor={5}, font={6}", new Object[]{string, bl, bl2, bl3, n, n2, wCFont});
        }
        if (bl2) {
            separatorImpl = new ContextMenuImpl.SeparatorImpl(null);
        } else {
            separatorImpl = new MenuItem(string);
            separatorImpl.setDisable(!bl3);
        }
        this.popupMenu.getItems().add((Object)separatorImpl);
    }

    @Override
    protected void setSelectedItem(int n) {
        log.log(Level.FINEST, "index={0}", n);
    }

    static void doShow(ContextMenu contextMenu, WebPage webPage, int n, int n2) {
        WebPageClient webPageClient = webPage.getPageClient();
        assert (webPageClient != null);
        WCPoint wCPoint = webPageClient.windowToScreen(new WCPoint(n, n2));
        contextMenu.show(((WebView)webPageClient.getContainer()).getScene().getWindow(), (double)wCPoint.getX(), (double)wCPoint.getY());
    }
}

