/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import java.net.URL;

public interface PolicyClient {
    public boolean permitNavigateAction(long var1, URL var3);

    public boolean permitRedirectAction(long var1, URL var3);

    public boolean permitAcceptResourceAction(long var1, URL var3);

    public boolean permitSubmitDataAction(long var1, URL var3, String var4);

    public boolean permitResubmitDataAction(long var1, URL var3, String var4);

    public boolean permitEnableScriptsAction(long var1, URL var3);

    public boolean permitNewPageAction(long var1, URL var3);

    public boolean permitClosePageAction(long var1);
}

