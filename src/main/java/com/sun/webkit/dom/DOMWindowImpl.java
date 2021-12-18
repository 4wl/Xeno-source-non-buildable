/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.dom.CSSStyleDeclarationImpl;
import com.sun.webkit.dom.DOMSelectionImpl;
import com.sun.webkit.dom.DocumentImpl;
import com.sun.webkit.dom.ElementImpl;
import com.sun.webkit.dom.EventImpl;
import com.sun.webkit.dom.EventListenerImpl;
import com.sun.webkit.dom.JSObject;
import java.lang.ref.Reference;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

public class DOMWindowImpl
extends JSObject
implements AbstractView,
EventTarget {
    private static SelfDisposer[] hashTable = new SelfDisposer[64];
    private static int hashCount;

    private static int hashPeer(long l) {
        return (int)(l ^ 0xFFFFFFFFFFFFFFFFL ^ l >> 7) & hashTable.length - 1;
    }

    private static AbstractView getCachedImpl(long l) {
        SelfDisposer selfDisposer;
        if (l == 0L) {
            return null;
        }
        int n = DOMWindowImpl.hashPeer(l);
        SelfDisposer selfDisposer2 = hashTable[n];
        Object object = null;
        Object object2 = selfDisposer2;
        while (object2 != null) {
            selfDisposer = ((SelfDisposer)object2).next;
            if (((SelfDisposer)object2).peer == l) {
                DOMWindowImpl dOMWindowImpl = (DOMWindowImpl)((Reference)object2).get();
                if (dOMWindowImpl != null) {
                    DOMWindowImpl.dispose(l);
                    return dOMWindowImpl;
                }
                if (object != null) {
                    ((SelfDisposer)object).next = selfDisposer;
                    break;
                }
                DOMWindowImpl.hashTable[n] = selfDisposer;
                break;
            }
            object = object2;
            object2 = selfDisposer;
        }
        object2 = (DOMWindowImpl)DOMWindowImpl.createInterface(l);
        selfDisposer = new SelfDisposer(object2, l);
        selfDisposer.next = selfDisposer2;
        DOMWindowImpl.hashTable[n] = selfDisposer;
        if (3 * hashCount >= 2 * hashTable.length) {
            DOMWindowImpl.rehash();
        }
        ++hashCount;
        return object2;
    }

    private static void rehash() {
        SelfDisposer[] arrselfDisposer = hashTable;
        int n = arrselfDisposer.length;
        SelfDisposer[] arrselfDisposer2 = new SelfDisposer[2 * n];
        hashTable = arrselfDisposer2;
        int n2 = n;
        while (--n2 >= 0) {
            SelfDisposer selfDisposer = arrselfDisposer[n2];
            while (selfDisposer != null) {
                SelfDisposer selfDisposer2 = selfDisposer.next;
                int n3 = DOMWindowImpl.hashPeer(selfDisposer.peer);
                selfDisposer.next = arrselfDisposer2[n3];
                arrselfDisposer2[n3] = selfDisposer;
                selfDisposer = selfDisposer2;
            }
        }
    }

    DOMWindowImpl(long l) {
        super(l, 2);
    }

    static AbstractView createInterface(long l) {
        if (l == 0L) {
            return null;
        }
        return new DOMWindowImpl(l);
    }

    static AbstractView create(long l) {
        return DOMWindowImpl.getCachedImpl(l);
    }

    static long getPeer(AbstractView abstractView) {
        return abstractView == null ? 0L : ((DOMWindowImpl)abstractView).getPeer();
    }

    private static native void dispose(long var0);

    static AbstractView getImpl(long l) {
        return DOMWindowImpl.create(l);
    }

    public Element getFrameElement() {
        return ElementImpl.getImpl(DOMWindowImpl.getFrameElementImpl(this.getPeer()));
    }

    static native long getFrameElementImpl(long var0);

    public boolean getOffscreenBuffering() {
        return DOMWindowImpl.getOffscreenBufferingImpl(this.getPeer());
    }

    static native boolean getOffscreenBufferingImpl(long var0);

    public int getOuterHeight() {
        return DOMWindowImpl.getOuterHeightImpl(this.getPeer());
    }

    static native int getOuterHeightImpl(long var0);

    public int getOuterWidth() {
        return DOMWindowImpl.getOuterWidthImpl(this.getPeer());
    }

    static native int getOuterWidthImpl(long var0);

    public int getInnerHeight() {
        return DOMWindowImpl.getInnerHeightImpl(this.getPeer());
    }

    static native int getInnerHeightImpl(long var0);

    public int getInnerWidth() {
        return DOMWindowImpl.getInnerWidthImpl(this.getPeer());
    }

    static native int getInnerWidthImpl(long var0);

    public int getScreenX() {
        return DOMWindowImpl.getScreenXImpl(this.getPeer());
    }

    static native int getScreenXImpl(long var0);

    public int getScreenY() {
        return DOMWindowImpl.getScreenYImpl(this.getPeer());
    }

    static native int getScreenYImpl(long var0);

    public int getScreenLeft() {
        return DOMWindowImpl.getScreenLeftImpl(this.getPeer());
    }

    static native int getScreenLeftImpl(long var0);

    public int getScreenTop() {
        return DOMWindowImpl.getScreenTopImpl(this.getPeer());
    }

    static native int getScreenTopImpl(long var0);

    public int getScrollX() {
        return DOMWindowImpl.getScrollXImpl(this.getPeer());
    }

    static native int getScrollXImpl(long var0);

    public int getScrollY() {
        return DOMWindowImpl.getScrollYImpl(this.getPeer());
    }

    static native int getScrollYImpl(long var0);

    public int getPageXOffset() {
        return DOMWindowImpl.getPageXOffsetImpl(this.getPeer());
    }

    static native int getPageXOffsetImpl(long var0);

    public int getPageYOffset() {
        return DOMWindowImpl.getPageYOffsetImpl(this.getPeer());
    }

    static native int getPageYOffsetImpl(long var0);

    public boolean getClosed() {
        return DOMWindowImpl.getClosedImpl(this.getPeer());
    }

    static native boolean getClosedImpl(long var0);

    public int getLength() {
        return DOMWindowImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    public String getName() {
        return DOMWindowImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    public void setName(String string) {
        DOMWindowImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    public String getStatus() {
        return DOMWindowImpl.getStatusImpl(this.getPeer());
    }

    static native String getStatusImpl(long var0);

    public void setStatus(String string) {
        DOMWindowImpl.setStatusImpl(this.getPeer(), string);
    }

    static native void setStatusImpl(long var0, String var2);

    public String getDefaultStatus() {
        return DOMWindowImpl.getDefaultStatusImpl(this.getPeer());
    }

    static native String getDefaultStatusImpl(long var0);

    public void setDefaultStatus(String string) {
        DOMWindowImpl.setDefaultStatusImpl(this.getPeer(), string);
    }

    static native void setDefaultStatusImpl(long var0, String var2);

    public AbstractView getSelf() {
        return DOMWindowImpl.getImpl(DOMWindowImpl.getSelfImpl(this.getPeer()));
    }

    static native long getSelfImpl(long var0);

    public AbstractView getWindow() {
        return DOMWindowImpl.getImpl(DOMWindowImpl.getWindowImpl(this.getPeer()));
    }

    static native long getWindowImpl(long var0);

    public AbstractView getFrames() {
        return DOMWindowImpl.getImpl(DOMWindowImpl.getFramesImpl(this.getPeer()));
    }

    static native long getFramesImpl(long var0);

    public AbstractView getOpener() {
        return DOMWindowImpl.getImpl(DOMWindowImpl.getOpenerImpl(this.getPeer()));
    }

    static native long getOpenerImpl(long var0);

    public AbstractView getParent() {
        return DOMWindowImpl.getImpl(DOMWindowImpl.getParentImpl(this.getPeer()));
    }

    static native long getParentImpl(long var0);

    public AbstractView getTop() {
        return DOMWindowImpl.getImpl(DOMWindowImpl.getTopImpl(this.getPeer()));
    }

    static native long getTopImpl(long var0);

    public Document getDocumentEx() {
        return DocumentImpl.getImpl(DOMWindowImpl.getDocumentExImpl(this.getPeer()));
    }

    static native long getDocumentExImpl(long var0);

    public double getDevicePixelRatio() {
        return DOMWindowImpl.getDevicePixelRatioImpl(this.getPeer());
    }

    static native double getDevicePixelRatioImpl(long var0);

    public EventListener getOnabort() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnabortImpl(this.getPeer()));
    }

    static native long getOnabortImpl(long var0);

    public void setOnabort(EventListener eventListener) {
        DOMWindowImpl.setOnabortImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnabortImpl(long var0, long var2);

    public EventListener getOnbeforeunload() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnbeforeunloadImpl(this.getPeer()));
    }

    static native long getOnbeforeunloadImpl(long var0);

    public void setOnbeforeunload(EventListener eventListener) {
        DOMWindowImpl.setOnbeforeunloadImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnbeforeunloadImpl(long var0, long var2);

    public EventListener getOnblur() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnblurImpl(this.getPeer()));
    }

    static native long getOnblurImpl(long var0);

    public void setOnblur(EventListener eventListener) {
        DOMWindowImpl.setOnblurImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnblurImpl(long var0, long var2);

    public EventListener getOncanplay() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOncanplayImpl(this.getPeer()));
    }

    static native long getOncanplayImpl(long var0);

    public void setOncanplay(EventListener eventListener) {
        DOMWindowImpl.setOncanplayImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOncanplayImpl(long var0, long var2);

    public EventListener getOncanplaythrough() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOncanplaythroughImpl(this.getPeer()));
    }

    static native long getOncanplaythroughImpl(long var0);

    public void setOncanplaythrough(EventListener eventListener) {
        DOMWindowImpl.setOncanplaythroughImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOncanplaythroughImpl(long var0, long var2);

    public EventListener getOnchange() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnchangeImpl(this.getPeer()));
    }

    static native long getOnchangeImpl(long var0);

    public void setOnchange(EventListener eventListener) {
        DOMWindowImpl.setOnchangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnchangeImpl(long var0, long var2);

    public EventListener getOnclick() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnclickImpl(this.getPeer()));
    }

    static native long getOnclickImpl(long var0);

    public void setOnclick(EventListener eventListener) {
        DOMWindowImpl.setOnclickImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnclickImpl(long var0, long var2);

    public EventListener getOncontextmenu() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOncontextmenuImpl(this.getPeer()));
    }

    static native long getOncontextmenuImpl(long var0);

    public void setOncontextmenu(EventListener eventListener) {
        DOMWindowImpl.setOncontextmenuImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOncontextmenuImpl(long var0, long var2);

    public EventListener getOndblclick() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOndblclickImpl(this.getPeer()));
    }

    static native long getOndblclickImpl(long var0);

    public void setOndblclick(EventListener eventListener) {
        DOMWindowImpl.setOndblclickImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndblclickImpl(long var0, long var2);

    public EventListener getOndrag() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOndragImpl(this.getPeer()));
    }

    static native long getOndragImpl(long var0);

    public void setOndrag(EventListener eventListener) {
        DOMWindowImpl.setOndragImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragImpl(long var0, long var2);

    public EventListener getOndragend() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOndragendImpl(this.getPeer()));
    }

    static native long getOndragendImpl(long var0);

    public void setOndragend(EventListener eventListener) {
        DOMWindowImpl.setOndragendImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragendImpl(long var0, long var2);

    public EventListener getOndragenter() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOndragenterImpl(this.getPeer()));
    }

    static native long getOndragenterImpl(long var0);

    public void setOndragenter(EventListener eventListener) {
        DOMWindowImpl.setOndragenterImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragenterImpl(long var0, long var2);

    public EventListener getOndragleave() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOndragleaveImpl(this.getPeer()));
    }

    static native long getOndragleaveImpl(long var0);

    public void setOndragleave(EventListener eventListener) {
        DOMWindowImpl.setOndragleaveImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragleaveImpl(long var0, long var2);

    public EventListener getOndragover() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOndragoverImpl(this.getPeer()));
    }

    static native long getOndragoverImpl(long var0);

    public void setOndragover(EventListener eventListener) {
        DOMWindowImpl.setOndragoverImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragoverImpl(long var0, long var2);

    public EventListener getOndragstart() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOndragstartImpl(this.getPeer()));
    }

    static native long getOndragstartImpl(long var0);

    public void setOndragstart(EventListener eventListener) {
        DOMWindowImpl.setOndragstartImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragstartImpl(long var0, long var2);

    public EventListener getOndrop() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOndropImpl(this.getPeer()));
    }

    static native long getOndropImpl(long var0);

    public void setOndrop(EventListener eventListener) {
        DOMWindowImpl.setOndropImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndropImpl(long var0, long var2);

    public EventListener getOndurationchange() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOndurationchangeImpl(this.getPeer()));
    }

    static native long getOndurationchangeImpl(long var0);

    public void setOndurationchange(EventListener eventListener) {
        DOMWindowImpl.setOndurationchangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndurationchangeImpl(long var0, long var2);

    public EventListener getOnemptied() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnemptiedImpl(this.getPeer()));
    }

    static native long getOnemptiedImpl(long var0);

    public void setOnemptied(EventListener eventListener) {
        DOMWindowImpl.setOnemptiedImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnemptiedImpl(long var0, long var2);

    public EventListener getOnended() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnendedImpl(this.getPeer()));
    }

    static native long getOnendedImpl(long var0);

    public void setOnended(EventListener eventListener) {
        DOMWindowImpl.setOnendedImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnendedImpl(long var0, long var2);

    public EventListener getOnerror() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnerrorImpl(this.getPeer()));
    }

    static native long getOnerrorImpl(long var0);

    public void setOnerror(EventListener eventListener) {
        DOMWindowImpl.setOnerrorImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnerrorImpl(long var0, long var2);

    public EventListener getOnfocus() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnfocusImpl(this.getPeer()));
    }

    static native long getOnfocusImpl(long var0);

    public void setOnfocus(EventListener eventListener) {
        DOMWindowImpl.setOnfocusImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnfocusImpl(long var0, long var2);

    public EventListener getOnhashchange() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnhashchangeImpl(this.getPeer()));
    }

    static native long getOnhashchangeImpl(long var0);

    public void setOnhashchange(EventListener eventListener) {
        DOMWindowImpl.setOnhashchangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnhashchangeImpl(long var0, long var2);

    public EventListener getOninput() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOninputImpl(this.getPeer()));
    }

    static native long getOninputImpl(long var0);

    public void setOninput(EventListener eventListener) {
        DOMWindowImpl.setOninputImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOninputImpl(long var0, long var2);

    public EventListener getOninvalid() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOninvalidImpl(this.getPeer()));
    }

    static native long getOninvalidImpl(long var0);

    public void setOninvalid(EventListener eventListener) {
        DOMWindowImpl.setOninvalidImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOninvalidImpl(long var0, long var2);

    public EventListener getOnkeydown() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnkeydownImpl(this.getPeer()));
    }

    static native long getOnkeydownImpl(long var0);

    public void setOnkeydown(EventListener eventListener) {
        DOMWindowImpl.setOnkeydownImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnkeydownImpl(long var0, long var2);

    public EventListener getOnkeypress() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnkeypressImpl(this.getPeer()));
    }

    static native long getOnkeypressImpl(long var0);

    public void setOnkeypress(EventListener eventListener) {
        DOMWindowImpl.setOnkeypressImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnkeypressImpl(long var0, long var2);

    public EventListener getOnkeyup() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnkeyupImpl(this.getPeer()));
    }

    static native long getOnkeyupImpl(long var0);

    public void setOnkeyup(EventListener eventListener) {
        DOMWindowImpl.setOnkeyupImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnkeyupImpl(long var0, long var2);

    public EventListener getOnload() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnloadImpl(this.getPeer()));
    }

    static native long getOnloadImpl(long var0);

    public void setOnload(EventListener eventListener) {
        DOMWindowImpl.setOnloadImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnloadImpl(long var0, long var2);

    public EventListener getOnloadeddata() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnloadeddataImpl(this.getPeer()));
    }

    static native long getOnloadeddataImpl(long var0);

    public void setOnloadeddata(EventListener eventListener) {
        DOMWindowImpl.setOnloadeddataImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnloadeddataImpl(long var0, long var2);

    public EventListener getOnloadedmetadata() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnloadedmetadataImpl(this.getPeer()));
    }

    static native long getOnloadedmetadataImpl(long var0);

    public void setOnloadedmetadata(EventListener eventListener) {
        DOMWindowImpl.setOnloadedmetadataImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnloadedmetadataImpl(long var0, long var2);

    public EventListener getOnloadstart() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnloadstartImpl(this.getPeer()));
    }

    static native long getOnloadstartImpl(long var0);

    public void setOnloadstart(EventListener eventListener) {
        DOMWindowImpl.setOnloadstartImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnloadstartImpl(long var0, long var2);

    public EventListener getOnmessage() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnmessageImpl(this.getPeer()));
    }

    static native long getOnmessageImpl(long var0);

    public void setOnmessage(EventListener eventListener) {
        DOMWindowImpl.setOnmessageImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmessageImpl(long var0, long var2);

    public EventListener getOnmousedown() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnmousedownImpl(this.getPeer()));
    }

    static native long getOnmousedownImpl(long var0);

    public void setOnmousedown(EventListener eventListener) {
        DOMWindowImpl.setOnmousedownImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmousedownImpl(long var0, long var2);

    public EventListener getOnmouseenter() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnmouseenterImpl(this.getPeer()));
    }

    static native long getOnmouseenterImpl(long var0);

    public void setOnmouseenter(EventListener eventListener) {
        DOMWindowImpl.setOnmouseenterImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmouseenterImpl(long var0, long var2);

    public EventListener getOnmouseleave() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnmouseleaveImpl(this.getPeer()));
    }

    static native long getOnmouseleaveImpl(long var0);

    public void setOnmouseleave(EventListener eventListener) {
        DOMWindowImpl.setOnmouseleaveImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmouseleaveImpl(long var0, long var2);

    public EventListener getOnmousemove() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnmousemoveImpl(this.getPeer()));
    }

    static native long getOnmousemoveImpl(long var0);

    public void setOnmousemove(EventListener eventListener) {
        DOMWindowImpl.setOnmousemoveImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmousemoveImpl(long var0, long var2);

    public EventListener getOnmouseout() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnmouseoutImpl(this.getPeer()));
    }

    static native long getOnmouseoutImpl(long var0);

    public void setOnmouseout(EventListener eventListener) {
        DOMWindowImpl.setOnmouseoutImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmouseoutImpl(long var0, long var2);

    public EventListener getOnmouseover() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnmouseoverImpl(this.getPeer()));
    }

    static native long getOnmouseoverImpl(long var0);

    public void setOnmouseover(EventListener eventListener) {
        DOMWindowImpl.setOnmouseoverImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmouseoverImpl(long var0, long var2);

    public EventListener getOnmouseup() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnmouseupImpl(this.getPeer()));
    }

    static native long getOnmouseupImpl(long var0);

    public void setOnmouseup(EventListener eventListener) {
        DOMWindowImpl.setOnmouseupImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmouseupImpl(long var0, long var2);

    public EventListener getOnmousewheel() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnmousewheelImpl(this.getPeer()));
    }

    static native long getOnmousewheelImpl(long var0);

    public void setOnmousewheel(EventListener eventListener) {
        DOMWindowImpl.setOnmousewheelImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmousewheelImpl(long var0, long var2);

    public EventListener getOnoffline() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnofflineImpl(this.getPeer()));
    }

    static native long getOnofflineImpl(long var0);

    public void setOnoffline(EventListener eventListener) {
        DOMWindowImpl.setOnofflineImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnofflineImpl(long var0, long var2);

    public EventListener getOnonline() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnonlineImpl(this.getPeer()));
    }

    static native long getOnonlineImpl(long var0);

    public void setOnonline(EventListener eventListener) {
        DOMWindowImpl.setOnonlineImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnonlineImpl(long var0, long var2);

    public EventListener getOnpagehide() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnpagehideImpl(this.getPeer()));
    }

    static native long getOnpagehideImpl(long var0);

    public void setOnpagehide(EventListener eventListener) {
        DOMWindowImpl.setOnpagehideImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnpagehideImpl(long var0, long var2);

    public EventListener getOnpageshow() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnpageshowImpl(this.getPeer()));
    }

    static native long getOnpageshowImpl(long var0);

    public void setOnpageshow(EventListener eventListener) {
        DOMWindowImpl.setOnpageshowImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnpageshowImpl(long var0, long var2);

    public EventListener getOnpause() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnpauseImpl(this.getPeer()));
    }

    static native long getOnpauseImpl(long var0);

    public void setOnpause(EventListener eventListener) {
        DOMWindowImpl.setOnpauseImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnpauseImpl(long var0, long var2);

    public EventListener getOnplay() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnplayImpl(this.getPeer()));
    }

    static native long getOnplayImpl(long var0);

    public void setOnplay(EventListener eventListener) {
        DOMWindowImpl.setOnplayImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnplayImpl(long var0, long var2);

    public EventListener getOnplaying() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnplayingImpl(this.getPeer()));
    }

    static native long getOnplayingImpl(long var0);

    public void setOnplaying(EventListener eventListener) {
        DOMWindowImpl.setOnplayingImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnplayingImpl(long var0, long var2);

    public EventListener getOnpopstate() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnpopstateImpl(this.getPeer()));
    }

    static native long getOnpopstateImpl(long var0);

    public void setOnpopstate(EventListener eventListener) {
        DOMWindowImpl.setOnpopstateImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnpopstateImpl(long var0, long var2);

    public EventListener getOnprogress() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnprogressImpl(this.getPeer()));
    }

    static native long getOnprogressImpl(long var0);

    public void setOnprogress(EventListener eventListener) {
        DOMWindowImpl.setOnprogressImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnprogressImpl(long var0, long var2);

    public EventListener getOnratechange() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnratechangeImpl(this.getPeer()));
    }

    static native long getOnratechangeImpl(long var0);

    public void setOnratechange(EventListener eventListener) {
        DOMWindowImpl.setOnratechangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnratechangeImpl(long var0, long var2);

    public EventListener getOnresize() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnresizeImpl(this.getPeer()));
    }

    static native long getOnresizeImpl(long var0);

    public void setOnresize(EventListener eventListener) {
        DOMWindowImpl.setOnresizeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnresizeImpl(long var0, long var2);

    public EventListener getOnscroll() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnscrollImpl(this.getPeer()));
    }

    static native long getOnscrollImpl(long var0);

    public void setOnscroll(EventListener eventListener) {
        DOMWindowImpl.setOnscrollImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnscrollImpl(long var0, long var2);

    public EventListener getOnseeked() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnseekedImpl(this.getPeer()));
    }

    static native long getOnseekedImpl(long var0);

    public void setOnseeked(EventListener eventListener) {
        DOMWindowImpl.setOnseekedImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnseekedImpl(long var0, long var2);

    public EventListener getOnseeking() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnseekingImpl(this.getPeer()));
    }

    static native long getOnseekingImpl(long var0);

    public void setOnseeking(EventListener eventListener) {
        DOMWindowImpl.setOnseekingImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnseekingImpl(long var0, long var2);

    public EventListener getOnselect() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnselectImpl(this.getPeer()));
    }

    static native long getOnselectImpl(long var0);

    public void setOnselect(EventListener eventListener) {
        DOMWindowImpl.setOnselectImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnselectImpl(long var0, long var2);

    public EventListener getOnstalled() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnstalledImpl(this.getPeer()));
    }

    static native long getOnstalledImpl(long var0);

    public void setOnstalled(EventListener eventListener) {
        DOMWindowImpl.setOnstalledImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnstalledImpl(long var0, long var2);

    public EventListener getOnstorage() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnstorageImpl(this.getPeer()));
    }

    static native long getOnstorageImpl(long var0);

    public void setOnstorage(EventListener eventListener) {
        DOMWindowImpl.setOnstorageImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnstorageImpl(long var0, long var2);

    public EventListener getOnsubmit() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnsubmitImpl(this.getPeer()));
    }

    static native long getOnsubmitImpl(long var0);

    public void setOnsubmit(EventListener eventListener) {
        DOMWindowImpl.setOnsubmitImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnsubmitImpl(long var0, long var2);

    public EventListener getOnsuspend() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnsuspendImpl(this.getPeer()));
    }

    static native long getOnsuspendImpl(long var0);

    public void setOnsuspend(EventListener eventListener) {
        DOMWindowImpl.setOnsuspendImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnsuspendImpl(long var0, long var2);

    public EventListener getOntimeupdate() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOntimeupdateImpl(this.getPeer()));
    }

    static native long getOntimeupdateImpl(long var0);

    public void setOntimeupdate(EventListener eventListener) {
        DOMWindowImpl.setOntimeupdateImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOntimeupdateImpl(long var0, long var2);

    public EventListener getOnunload() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnunloadImpl(this.getPeer()));
    }

    static native long getOnunloadImpl(long var0);

    public void setOnunload(EventListener eventListener) {
        DOMWindowImpl.setOnunloadImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnunloadImpl(long var0, long var2);

    public EventListener getOnvolumechange() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnvolumechangeImpl(this.getPeer()));
    }

    static native long getOnvolumechangeImpl(long var0);

    public void setOnvolumechange(EventListener eventListener) {
        DOMWindowImpl.setOnvolumechangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnvolumechangeImpl(long var0, long var2);

    public EventListener getOnwaiting() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnwaitingImpl(this.getPeer()));
    }

    static native long getOnwaitingImpl(long var0);

    public void setOnwaiting(EventListener eventListener) {
        DOMWindowImpl.setOnwaitingImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnwaitingImpl(long var0, long var2);

    public EventListener getOnwheel() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnwheelImpl(this.getPeer()));
    }

    static native long getOnwheelImpl(long var0);

    public void setOnwheel(EventListener eventListener) {
        DOMWindowImpl.setOnwheelImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnwheelImpl(long var0, long var2);

    public EventListener getOnreset() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnresetImpl(this.getPeer()));
    }

    static native long getOnresetImpl(long var0);

    public void setOnreset(EventListener eventListener) {
        DOMWindowImpl.setOnresetImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnresetImpl(long var0, long var2);

    public EventListener getOnsearch() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnsearchImpl(this.getPeer()));
    }

    static native long getOnsearchImpl(long var0);

    public void setOnsearch(EventListener eventListener) {
        DOMWindowImpl.setOnsearchImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnsearchImpl(long var0, long var2);

    public EventListener getOnwebkitanimationend() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnwebkitanimationendImpl(this.getPeer()));
    }

    static native long getOnwebkitanimationendImpl(long var0);

    public void setOnwebkitanimationend(EventListener eventListener) {
        DOMWindowImpl.setOnwebkitanimationendImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnwebkitanimationendImpl(long var0, long var2);

    public EventListener getOnwebkitanimationiteration() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnwebkitanimationiterationImpl(this.getPeer()));
    }

    static native long getOnwebkitanimationiterationImpl(long var0);

    public void setOnwebkitanimationiteration(EventListener eventListener) {
        DOMWindowImpl.setOnwebkitanimationiterationImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnwebkitanimationiterationImpl(long var0, long var2);

    public EventListener getOnwebkitanimationstart() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnwebkitanimationstartImpl(this.getPeer()));
    }

    static native long getOnwebkitanimationstartImpl(long var0);

    public void setOnwebkitanimationstart(EventListener eventListener) {
        DOMWindowImpl.setOnwebkitanimationstartImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnwebkitanimationstartImpl(long var0, long var2);

    public EventListener getOnwebkittransitionend() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOnwebkittransitionendImpl(this.getPeer()));
    }

    static native long getOnwebkittransitionendImpl(long var0);

    public void setOnwebkittransitionend(EventListener eventListener) {
        DOMWindowImpl.setOnwebkittransitionendImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnwebkittransitionendImpl(long var0, long var2);

    public EventListener getOntransitionend() {
        return EventListenerImpl.getImpl(DOMWindowImpl.getOntransitionendImpl(this.getPeer()));
    }

    static native long getOntransitionendImpl(long var0);

    public void setOntransitionend(EventListener eventListener) {
        DOMWindowImpl.setOntransitionendImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOntransitionendImpl(long var0, long var2);

    public DOMSelectionImpl getSelection() {
        return DOMSelectionImpl.getImpl(DOMWindowImpl.getSelectionImpl(this.getPeer()));
    }

    static native long getSelectionImpl(long var0);

    public void focus() {
        DOMWindowImpl.focusImpl(this.getPeer());
    }

    static native void focusImpl(long var0);

    public void blur() {
        DOMWindowImpl.blurImpl(this.getPeer());
    }

    static native void blurImpl(long var0);

    public void close() {
        DOMWindowImpl.closeImpl(this.getPeer());
    }

    static native void closeImpl(long var0);

    public void print() {
        DOMWindowImpl.printImpl(this.getPeer());
    }

    static native void printImpl(long var0);

    public void stop() {
        DOMWindowImpl.stopImpl(this.getPeer());
    }

    static native void stopImpl(long var0);

    public void alert(String string) {
        DOMWindowImpl.alertImpl(this.getPeer(), string);
    }

    static native void alertImpl(long var0, String var2);

    public boolean confirm(String string) {
        return DOMWindowImpl.confirmImpl(this.getPeer(), string);
    }

    static native boolean confirmImpl(long var0, String var2);

    public String prompt(String string, String string2) {
        return DOMWindowImpl.promptImpl(this.getPeer(), string, string2);
    }

    static native String promptImpl(long var0, String var2, String var3);

    public boolean find(String string, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6) {
        return DOMWindowImpl.findImpl(this.getPeer(), string, bl, bl2, bl3, bl4, bl5, bl6);
    }

    static native boolean findImpl(long var0, String var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8);

    public void scrollBy(int n, int n2) {
        DOMWindowImpl.scrollByImpl(this.getPeer(), n, n2);
    }

    static native void scrollByImpl(long var0, int var2, int var3);

    public void scrollTo(int n, int n2) {
        DOMWindowImpl.scrollToImpl(this.getPeer(), n, n2);
    }

    static native void scrollToImpl(long var0, int var2, int var3);

    public void scroll(int n, int n2) {
        DOMWindowImpl.scrollImpl(this.getPeer(), n, n2);
    }

    static native void scrollImpl(long var0, int var2, int var3);

    public void moveBy(float f, float f2) {
        DOMWindowImpl.moveByImpl(this.getPeer(), f, f2);
    }

    static native void moveByImpl(long var0, float var2, float var3);

    public void moveTo(float f, float f2) {
        DOMWindowImpl.moveToImpl(this.getPeer(), f, f2);
    }

    static native void moveToImpl(long var0, float var2, float var3);

    public void resizeBy(float f, float f2) {
        DOMWindowImpl.resizeByImpl(this.getPeer(), f, f2);
    }

    static native void resizeByImpl(long var0, float var2, float var3);

    public void resizeTo(float f, float f2) {
        DOMWindowImpl.resizeToImpl(this.getPeer(), f, f2);
    }

    static native void resizeToImpl(long var0, float var2, float var3);

    public CSSStyleDeclaration getComputedStyle(Element element, String string) {
        return CSSStyleDeclarationImpl.getImpl(DOMWindowImpl.getComputedStyleImpl(this.getPeer(), ElementImpl.getPeer(element), string));
    }

    static native long getComputedStyleImpl(long var0, long var2, String var4);

    @Override
    public void addEventListener(String string, EventListener eventListener, boolean bl) {
        DOMWindowImpl.addEventListenerImpl(this.getPeer(), string, EventListenerImpl.getPeer(eventListener), bl);
    }

    static native void addEventListenerImpl(long var0, String var2, long var3, boolean var5);

    @Override
    public void removeEventListener(String string, EventListener eventListener, boolean bl) {
        DOMWindowImpl.removeEventListenerImpl(this.getPeer(), string, EventListenerImpl.getPeer(eventListener), bl);
    }

    static native void removeEventListenerImpl(long var0, String var2, long var3, boolean var5);

    @Override
    public boolean dispatchEvent(Event event) throws DOMException {
        return DOMWindowImpl.dispatchEventImpl(this.getPeer(), EventImpl.getPeer(event));
    }

    static native boolean dispatchEventImpl(long var0, long var2);

    public void captureEvents() {
        DOMWindowImpl.captureEventsImpl(this.getPeer());
    }

    static native void captureEventsImpl(long var0);

    public void releaseEvents() {
        DOMWindowImpl.releaseEventsImpl(this.getPeer());
    }

    static native void releaseEventsImpl(long var0);

    public String atob(String string) throws DOMException {
        return DOMWindowImpl.atobImpl(this.getPeer(), string);
    }

    static native String atobImpl(long var0, String var2);

    public String btoa(String string) throws DOMException {
        return DOMWindowImpl.btoaImpl(this.getPeer(), string);
    }

    static native String btoaImpl(long var0, String var2);

    public void clearTimeout(int n) {
        DOMWindowImpl.clearTimeoutImpl(this.getPeer(), n);
    }

    static native void clearTimeoutImpl(long var0, int var2);

    public void clearInterval(int n) {
        DOMWindowImpl.clearIntervalImpl(this.getPeer(), n);
    }

    static native void clearIntervalImpl(long var0, int var2);

    @Override
    public DocumentView getDocument() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static final class SelfDisposer
    extends Disposer.WeakDisposerRecord {
        private final long peer;
        SelfDisposer next;

        SelfDisposer(Object object, long l) {
            super(object);
            this.peer = l;
        }

        @Override
        public void dispose() {
            int n = DOMWindowImpl.hashPeer(this.peer);
            SelfDisposer selfDisposer = hashTable[n];
            SelfDisposer selfDisposer2 = null;
            SelfDisposer selfDisposer3 = selfDisposer;
            while (selfDisposer3 != null) {
                SelfDisposer selfDisposer4 = selfDisposer3.next;
                if (selfDisposer3.peer == this.peer) {
                    selfDisposer3.clear();
                    if (selfDisposer2 != null) {
                        selfDisposer2.next = selfDisposer4;
                    } else {
                        hashTable[n] = selfDisposer4;
                    }
                    hashCount--;
                    break;
                }
                selfDisposer2 = selfDisposer3;
                selfDisposer3 = selfDisposer4;
            }
            DOMWindowImpl.dispose(this.peer);
        }
    }
}

