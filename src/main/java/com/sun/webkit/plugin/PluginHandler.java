/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.plugin;

import com.sun.webkit.plugin.Plugin;
import java.net.URL;

interface PluginHandler {
    public String getName();

    public String getFileName();

    public String getDescription();

    public String[] supportedMIMETypes();

    public boolean isSupportedMIMEType(String var1);

    public boolean isSupportedPlatform();

    public Plugin createPlugin(URL var1, String var2, String[] var3, String[] var4);
}

