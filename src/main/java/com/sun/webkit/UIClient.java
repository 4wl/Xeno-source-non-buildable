/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCRectangle;

public interface UIClient {
    public WebPage createPage(boolean var1, boolean var2, boolean var3, boolean var4);

    public void closePage();

    public void showView();

    public WCRectangle getViewBounds();

    public void setViewBounds(WCRectangle var1);

    public void setStatusbarText(String var1);

    public void alert(String var1);

    public boolean confirm(String var1);

    public String prompt(String var1, String var2);

    public String[] chooseFile(String var1, boolean var2);

    public void print();

    public void startDrag(WCImage var1, int var2, int var3, int var4, int var5, String[] var6, Object[] var7);

    public void confirmStartDrag();

    public boolean isDragConfirmed();
}

