/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.EventListenerImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLBodyElement;

public class HTMLBodyElementImpl
extends HTMLElementImpl
implements HTMLBodyElement {
    HTMLBodyElementImpl(long l) {
        super(l);
    }

    static HTMLBodyElement getImpl(long l) {
        return (HTMLBodyElement)HTMLBodyElementImpl.create(l);
    }

    @Override
    public String getALink() {
        return HTMLBodyElementImpl.getALinkImpl(this.getPeer());
    }

    static native String getALinkImpl(long var0);

    @Override
    public void setALink(String string) {
        HTMLBodyElementImpl.setALinkImpl(this.getPeer(), string);
    }

    static native void setALinkImpl(long var0, String var2);

    @Override
    public String getBackground() {
        return HTMLBodyElementImpl.getBackgroundImpl(this.getPeer());
    }

    static native String getBackgroundImpl(long var0);

    @Override
    public void setBackground(String string) {
        HTMLBodyElementImpl.setBackgroundImpl(this.getPeer(), string);
    }

    static native void setBackgroundImpl(long var0, String var2);

    @Override
    public String getBgColor() {
        return HTMLBodyElementImpl.getBgColorImpl(this.getPeer());
    }

    static native String getBgColorImpl(long var0);

    @Override
    public void setBgColor(String string) {
        HTMLBodyElementImpl.setBgColorImpl(this.getPeer(), string);
    }

    static native void setBgColorImpl(long var0, String var2);

    @Override
    public String getLink() {
        return HTMLBodyElementImpl.getLinkImpl(this.getPeer());
    }

    static native String getLinkImpl(long var0);

    @Override
    public void setLink(String string) {
        HTMLBodyElementImpl.setLinkImpl(this.getPeer(), string);
    }

    static native void setLinkImpl(long var0, String var2);

    @Override
    public String getText() {
        return HTMLBodyElementImpl.getTextImpl(this.getPeer());
    }

    static native String getTextImpl(long var0);

    @Override
    public void setText(String string) {
        HTMLBodyElementImpl.setTextImpl(this.getPeer(), string);
    }

    static native void setTextImpl(long var0, String var2);

    @Override
    public String getVLink() {
        return HTMLBodyElementImpl.getVLinkImpl(this.getPeer());
    }

    static native String getVLinkImpl(long var0);

    @Override
    public void setVLink(String string) {
        HTMLBodyElementImpl.setVLinkImpl(this.getPeer(), string);
    }

    static native void setVLinkImpl(long var0, String var2);

    public EventListener getOnbeforeunload() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnbeforeunloadImpl(this.getPeer()));
    }

    static native long getOnbeforeunloadImpl(long var0);

    public void setOnbeforeunload(EventListener eventListener) {
        HTMLBodyElementImpl.setOnbeforeunloadImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnbeforeunloadImpl(long var0, long var2);

    public EventListener getOnhashchange() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnhashchangeImpl(this.getPeer()));
    }

    static native long getOnhashchangeImpl(long var0);

    public void setOnhashchange(EventListener eventListener) {
        HTMLBodyElementImpl.setOnhashchangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnhashchangeImpl(long var0, long var2);

    public EventListener getOnmessage() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnmessageImpl(this.getPeer()));
    }

    static native long getOnmessageImpl(long var0);

    public void setOnmessage(EventListener eventListener) {
        HTMLBodyElementImpl.setOnmessageImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmessageImpl(long var0, long var2);

    public EventListener getOnoffline() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnofflineImpl(this.getPeer()));
    }

    static native long getOnofflineImpl(long var0);

    public void setOnoffline(EventListener eventListener) {
        HTMLBodyElementImpl.setOnofflineImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnofflineImpl(long var0, long var2);

    public EventListener getOnonline() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnonlineImpl(this.getPeer()));
    }

    static native long getOnonlineImpl(long var0);

    public void setOnonline(EventListener eventListener) {
        HTMLBodyElementImpl.setOnonlineImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnonlineImpl(long var0, long var2);

    public EventListener getOnpopstate() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnpopstateImpl(this.getPeer()));
    }

    static native long getOnpopstateImpl(long var0);

    public void setOnpopstate(EventListener eventListener) {
        HTMLBodyElementImpl.setOnpopstateImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnpopstateImpl(long var0, long var2);

    public EventListener getOnresize() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnresizeImpl(this.getPeer()));
    }

    static native long getOnresizeImpl(long var0);

    public void setOnresize(EventListener eventListener) {
        HTMLBodyElementImpl.setOnresizeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnresizeImpl(long var0, long var2);

    public EventListener getOnstorage() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnstorageImpl(this.getPeer()));
    }

    static native long getOnstorageImpl(long var0);

    public void setOnstorage(EventListener eventListener) {
        HTMLBodyElementImpl.setOnstorageImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnstorageImpl(long var0, long var2);

    public EventListener getOnunload() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnunloadImpl(this.getPeer()));
    }

    static native long getOnunloadImpl(long var0);

    public void setOnunload(EventListener eventListener) {
        HTMLBodyElementImpl.setOnunloadImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnunloadImpl(long var0, long var2);

    @Override
    public EventListener getOnblur() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnblurImpl(this.getPeer()));
    }

    static native long getOnblurImpl(long var0);

    @Override
    public void setOnblur(EventListener eventListener) {
        HTMLBodyElementImpl.setOnblurImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnblurImpl(long var0, long var2);

    @Override
    public EventListener getOnerror() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnerrorImpl(this.getPeer()));
    }

    static native long getOnerrorImpl(long var0);

    @Override
    public void setOnerror(EventListener eventListener) {
        HTMLBodyElementImpl.setOnerrorImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnerrorImpl(long var0, long var2);

    @Override
    public EventListener getOnfocus() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnfocusImpl(this.getPeer()));
    }

    static native long getOnfocusImpl(long var0);

    @Override
    public void setOnfocus(EventListener eventListener) {
        HTMLBodyElementImpl.setOnfocusImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnfocusImpl(long var0, long var2);

    @Override
    public EventListener getOnload() {
        return EventListenerImpl.getImpl(HTMLBodyElementImpl.getOnloadImpl(this.getPeer()));
    }

    static native long getOnloadImpl(long var0);

    @Override
    public void setOnload(EventListener eventListener) {
        HTMLBodyElementImpl.setOnloadImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnloadImpl(long var0, long var2);
}

