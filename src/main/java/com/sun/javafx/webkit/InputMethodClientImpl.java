/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Point2D
 *  javafx.scene.input.InputMethodEvent
 *  javafx.scene.input.InputMethodHighlight
 *  javafx.scene.input.InputMethodTextRun
 *  javafx.scene.web.WebView
 */
package com.sun.javafx.webkit;

import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.webkit.InputMethodClient;
import com.sun.webkit.WebPage;
import com.sun.webkit.event.WCInputMethodEvent;
import com.sun.webkit.graphics.WCPoint;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.web.WebView;

public final class InputMethodClientImpl
implements InputMethodClient,
ExtendedInputMethodRequests {
    private final WeakReference<WebView> wvRef;
    private final WebPage webPage;
    private boolean state;

    public InputMethodClientImpl(WebView webView, WebPage webPage) {
        this.wvRef = new WeakReference<WebView>(webView);
        this.webPage = webPage;
        if (webPage != null) {
            webPage.setInputMethodClient(this);
        }
    }

    @Override
    public void activateInputMethods(boolean bl) {
        WebView webView = (WebView)this.wvRef.get();
        if (webView != null && webView.getScene() != null) {
            webView.getScene().impl_enableInputMethodEvents(bl);
        }
        this.state = bl;
    }

    public boolean getInputMethodState() {
        return this.state;
    }

    public static WCInputMethodEvent convertToWCInputMethodEvent(InputMethodEvent inputMethodEvent) {
        Object object2;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        for (Object object2 : inputMethodEvent.getComposed()) {
            String string = object2.getText();
            InputMethodHighlight inputMethodHighlight = object2.getHighlight();
            arrayList.add(n);
            arrayList.add(n + string.length());
            arrayList.add(inputMethodHighlight == InputMethodHighlight.SELECTED_CONVERTED || inputMethodHighlight == InputMethodHighlight.SELECTED_RAW ? 1 : 0);
            n += string.length();
            stringBuilder.append(string);
        }
        int n2 = arrayList.size();
        if (n2 == 0) {
            arrayList.add(0);
            arrayList.add(n);
            arrayList.add(0);
            n2 = arrayList.size();
        }
        object2 = new int[n2];
        for (int i = 0; i < n2; ++i) {
            object2[i] = (InputMethodTextRun)((Integer)arrayList.get(i));
        }
        return new WCInputMethodEvent(inputMethodEvent.getCommitted(), stringBuilder.toString(), (int[])object2, inputMethodEvent.getCaretPosition());
    }

    public Point2D getTextLocation(int n) {
        int[] arrn = this.webPage.getClientTextLocation(n);
        WCPoint wCPoint = this.webPage.getPageClient().windowToScreen(new WCPoint(arrn[0], arrn[1] + arrn[3]));
        return new Point2D((double)wCPoint.getIntX(), (double)wCPoint.getIntY());
    }

    public int getLocationOffset(int n, int n2) {
        WCPoint wCPoint = this.webPage.getPageClient().windowToScreen(new WCPoint(0.0f, 0.0f));
        return this.webPage.getClientLocationOffset(n - wCPoint.getIntX(), n2 - wCPoint.getIntY());
    }

    public void cancelLatestCommittedText() {
    }

    public String getSelectedText() {
        return this.webPage.getClientSelectedText();
    }

    @Override
    public int getInsertPositionOffset() {
        return this.webPage.getClientInsertPositionOffset();
    }

    @Override
    public String getCommittedText(int n, int n2) {
        try {
            return this.webPage.getClientCommittedText().substring(n, n2);
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            throw new IllegalArgumentException(stringIndexOutOfBoundsException);
        }
    }

    @Override
    public int getCommittedTextLength() {
        return this.webPage.getClientCommittedTextLength();
    }
}

