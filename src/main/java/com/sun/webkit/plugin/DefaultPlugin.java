/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.plugin;

import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.plugin.Plugin;
import com.sun.webkit.plugin.PluginListener;
import java.io.IOError;
import java.net.URL;
import java.util.logging.Logger;

final class DefaultPlugin
implements Plugin {
    private static final Logger log = Logger.getLogger("com.sun.browser.plugin.DefaultPlugin");
    private int x = 0;
    private int y = 0;
    private int w = 0;
    private int h = 0;

    private void init(String string) {
    }

    DefaultPlugin(URL uRL, String string, String[] arrstring, String[] arrstring2) {
        this.init("Default Plugin for: " + (null == uRL ? "(null)" : uRL.toExternalForm()));
    }

    @Override
    public void paint(WCGraphicsContext wCGraphicsContext, int n, int n2, int n3, int n4) {
        wCGraphicsContext.fillRect(this.x, this.y, this.w, this.h, 0x11AAFFFF);
    }

    @Override
    public void activate(Object object, PluginListener pluginListener) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void setVisible(boolean bl) {
    }

    @Override
    public void setEnabled(boolean bl) {
    }

    @Override
    public void setBounds(int n, int n2, int n3, int n4) {
        this.x = n;
        this.y = n2;
        this.w = n3;
        this.h = n4;
    }

    @Override
    public Object invoke(String string, String string2, Object[] arrobject) throws IOError {
        return null;
    }

    @Override
    public boolean handleMouseEvent(String string, int n, int n2, int n3, int n4, int n5, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, long l) {
        return false;
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public void setNativeContainerBounds(int n, int n2, int n3, int n4) {
    }
}

