/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.scene.Node
 *  javafx.scene.web.WebEngine
 *  javafx.scene.web.WebView
 */
package com.sun.javafx.webkit;

import com.sun.webkit.WebPage;
import javafx.beans.InvalidationListener;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public abstract class Accessor {
    private static PageAccessor pageAccessor;

    public static void setPageAccessor(PageAccessor pageAccessor) {
        Accessor.pageAccessor = pageAccessor;
    }

    public static WebPage getPageFor(WebEngine webEngine) {
        return pageAccessor.getPage(webEngine);
    }

    public abstract WebEngine getEngine();

    public abstract WebView getView();

    public abstract WebPage getPage();

    public abstract void addChild(Node var1);

    public abstract void removeChild(Node var1);

    public abstract void addViewListener(InvalidationListener var1);

    public static interface PageAccessor {
        public WebPage getPage(WebEngine var1);
    }
}

