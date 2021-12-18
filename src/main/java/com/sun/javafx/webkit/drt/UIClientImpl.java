/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.drt;

import com.sun.javafx.webkit.drt.DumpRenderTree;
import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.UIClient;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCRectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class UIClientImpl
implements UIClient {
    private WebPage webPage;
    private final List<UIClient> clients = new ArrayList<UIClient>();
    private WCRectangle bounds = new WCRectangle(0.0f, 0.0f, 800.0f, 600.0f);

    UIClientImpl() {
    }

    void setWebPage(WebPage webPage) {
        this.webPage = webPage;
    }

    @Override
    public WebPage createPage(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        UIClientImpl uIClientImpl = new UIClientImpl();
        final WebPage webPage = new WebPage(null, uIClientImpl, null, null, null, false);
        uIClientImpl.setWebPage(webPage);
        webPage.setBounds(0, 0, 800, 600);
        webPage.addLoadListenerClient(new LoadListenerClient(){

            @Override
            public void dispatchLoadEvent(long l, int n, String string, String string2, double d, int n2) {
                if (n == 14) {
                    DumpRenderTree.drt.dumpUnloadListeners(webPage, l);
                }
            }

            @Override
            public void dispatchResourceLoadEvent(long l, int n, String string, String string2, double d, int n2) {
            }
        });
        webPage.getMainFrame();
        this.clients.add(uIClientImpl);
        return uIClientImpl.webPage;
    }

    @Override
    public void closePage() {
        Iterator<UIClient> iterator = this.clients.iterator();
        while (iterator.hasNext()) {
            iterator.next().closePage();
            iterator.remove();
        }
        if (this.webPage.getMainFrame() != 0L) {
            this.webPage.dispose();
        }
    }

    @Override
    public void showView() {
    }

    @Override
    public WCRectangle getViewBounds() {
        return this.bounds;
    }

    @Override
    public void setViewBounds(WCRectangle wCRectangle) {
        this.bounds = wCRectangle;
    }

    @Override
    public void setStatusbarText(String string) {
    }

    @Override
    public void alert(String string) {
        DumpRenderTree.out.printf("ALERT: %s\n", string);
    }

    @Override
    public boolean confirm(String string) {
        DumpRenderTree.out.printf("CONFIRM: %s\n", string);
        return false;
    }

    @Override
    public String prompt(String string, String string2) {
        DumpRenderTree.out.printf("PROMPT: %s, default text: %s\n", string, string2);
        return string2;
    }

    @Override
    public String[] chooseFile(String string, boolean bl) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public void print() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public void startDrag(WCImage wCImage, int n, int n2, int n3, int n4, String[] arrstring, Object[] arrobject) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public void confirmStartDrag() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDragConfirmed() {
        return false;
    }
}

