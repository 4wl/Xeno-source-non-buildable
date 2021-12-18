/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.plugin;

import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.plugin.PluginListener;
import java.io.IOException;

public interface Plugin {
    public static final int EVENT_BEFOREACTIVATE = -4;
    public static final int EVENT_FOCUSCHANGE = -1;

    public void requestFocus();

    public void setNativeContainerBounds(int var1, int var2, int var3, int var4);

    public void activate(Object var1, PluginListener var2);

    public void destroy();

    public void setVisible(boolean var1);

    public void setEnabled(boolean var1);

    public void setBounds(int var1, int var2, int var3, int var4);

    public Object invoke(String var1, String var2, Object[] var3) throws IOException;

    public void paint(WCGraphicsContext var1, int var2, int var3, int var4, int var5);

    public boolean handleMouseEvent(String var1, int var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11, long var12);
}

