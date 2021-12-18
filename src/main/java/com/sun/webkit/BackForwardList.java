/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.WebPage;
import com.sun.webkit.event.WCChangeEvent;
import com.sun.webkit.event.WCChangeListener;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.network.URLs;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public final class BackForwardList {
    private final WebPage page;
    private final List<WCChangeListener> listenerList = new LinkedList<WCChangeListener>();

    BackForwardList(WebPage webPage) {
        this.page = webPage;
        webPage.addLoadListenerClient(new LoadListenerClient(){

            @Override
            public void dispatchLoadEvent(long l, int n, String string, String string2, double d, int n2) {
                Entry entry;
                if (n == 14 && (entry = BackForwardList.this.getCurrentEntry()) != null) {
                    entry.updateLastVisitedDate();
                }
            }

            @Override
            public void dispatchResourceLoadEvent(long l, int n, String string, String string2, double d, int n2) {
            }
        });
    }

    public int size() {
        return BackForwardList.bflSize(this.page.getPage());
    }

    public int getMaximumSize() {
        return BackForwardList.bflGetMaximumSize(this.page.getPage());
    }

    public void setMaximumSize(int n) {
        BackForwardList.bflSetMaximumSize(this.page.getPage(), n);
    }

    public int getCurrentIndex() {
        return BackForwardList.bflGetCurrentIndex(this.page.getPage());
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public void setEnabled(boolean bl) {
        BackForwardList.bflSetEnabled(this.page.getPage(), bl);
    }

    public boolean isEnabled() {
        return BackForwardList.bflIsEnabled(this.page.getPage());
    }

    public Entry get(int n) {
        Entry entry = (Entry)BackForwardList.bflGet(this.page.getPage(), n);
        return entry;
    }

    public Entry getCurrentEntry() {
        return this.get(this.getCurrentIndex());
    }

    public int indexOf(Entry entry) {
        return BackForwardList.bflIndexOf(this.page.getPage(), entry.pitem, false);
    }

    public boolean contains(Entry entry) {
        return this.indexOf(entry) >= 0;
    }

    public Entry[] toArray() {
        int n = this.size();
        Entry[] arrentry = new Entry[n];
        for (int i = 0; i < n; ++i) {
            arrentry[i] = this.get(i);
        }
        return arrentry;
    }

    public void setCurrentIndex(int n) {
        if (BackForwardList.bflSetCurrentIndex(this.page.getPage(), n) < 0) {
            throw new IllegalArgumentException("invalid index: " + n);
        }
    }

    private boolean canGoBack(int n) {
        return n > 0;
    }

    public boolean canGoBack() {
        return this.canGoBack(this.getCurrentIndex());
    }

    public boolean goBack() {
        int n = this.getCurrentIndex();
        if (this.canGoBack(n)) {
            this.setCurrentIndex(n - 1);
            return true;
        }
        return false;
    }

    private boolean canGoForward(int n) {
        return n < this.size() - 1;
    }

    public boolean canGoForward() {
        return this.canGoForward(this.getCurrentIndex());
    }

    public boolean goForward() {
        int n = this.getCurrentIndex();
        if (this.canGoForward(n)) {
            this.setCurrentIndex(n + 1);
            return true;
        }
        return false;
    }

    public void addChangeListener(WCChangeListener wCChangeListener) {
        if (wCChangeListener == null) {
            return;
        }
        if (this.listenerList.isEmpty()) {
            BackForwardList.bflSetHostObject(this.page.getPage(), this);
        }
        this.listenerList.add(wCChangeListener);
    }

    public void removeChangeListener(WCChangeListener wCChangeListener) {
        if (wCChangeListener == null) {
            return;
        }
        this.listenerList.remove(wCChangeListener);
        if (this.listenerList.isEmpty()) {
            BackForwardList.bflSetHostObject(this.page.getPage(), null);
        }
    }

    public WCChangeListener[] getChangeListeners() {
        return this.listenerList.toArray(new WCChangeListener[0]);
    }

    private void notifyChanged() {
        for (WCChangeListener wCChangeListener : this.listenerList) {
            wCChangeListener.stateChanged(new WCChangeEvent(this));
        }
    }

    private static native String bflItemGetURL(long var0);

    private static native String bflItemGetTitle(long var0);

    private static native WCImage bflItemGetIcon(long var0);

    private static native long bflItemGetLastVisitedDate(long var0);

    private static native boolean bflItemIsTargetItem(long var0);

    private static native Entry[] bflItemGetChildren(long var0, long var2);

    private static native String bflItemGetTarget(long var0);

    private static native int bflSize(long var0);

    private static native int bflGetMaximumSize(long var0);

    private static native void bflSetMaximumSize(long var0, int var2);

    private static native int bflGetCurrentIndex(long var0);

    private static native int bflIndexOf(long var0, long var2, boolean var4);

    private static native void bflSetEnabled(long var0, boolean var2);

    private static native boolean bflIsEnabled(long var0);

    private static native Object bflGet(long var0, int var2);

    private static native int bflSetCurrentIndex(long var0, int var2);

    private static native void bflSetHostObject(long var0, Object var2);

    public static final class Entry {
        private long pitem = 0L;
        private long ppage = 0L;
        private Entry[] children;
        private URL url;
        private String title;
        private Date lastVisitedDate;
        private WCImage icon;
        private String target;
        private boolean isTargetItem;
        private final List<WCChangeListener> listenerList = new LinkedList<WCChangeListener>();

        private Entry(long l, long l2) {
            this.pitem = l;
            this.ppage = l2;
            this.getURL();
            this.getTitle();
            this.getLastVisitedDate();
            this.getIcon();
            this.getTarget();
            this.isTargetItem();
            this.getChildren();
        }

        private void notifyItemDestroyed() {
            this.pitem = 0L;
        }

        private void notifyItemChanged() {
            for (WCChangeListener wCChangeListener : this.listenerList) {
                wCChangeListener.stateChanged(new WCChangeEvent(this));
            }
        }

        public URL getURL() {
            try {
                return this.pitem == 0L ? this.url : (this.url = URLs.newURL(BackForwardList.bflItemGetURL(this.pitem)));
            }
            catch (MalformedURLException malformedURLException) {
                this.url = null;
                return null;
            }
        }

        public String getTitle() {
            return this.pitem == 0L ? this.title : (this.title = BackForwardList.bflItemGetTitle(this.pitem));
        }

        public WCImage getIcon() {
            return this.pitem == 0L ? this.icon : (this.icon = BackForwardList.bflItemGetIcon(this.pitem));
        }

        public String getTarget() {
            return this.pitem == 0L ? this.target : (this.target = BackForwardList.bflItemGetTarget(this.pitem));
        }

        public Date getLastVisitedDate() {
            return this.lastVisitedDate == null ? null : (Date)this.lastVisitedDate.clone();
        }

        private void updateLastVisitedDate() {
            this.lastVisitedDate = new Date(System.currentTimeMillis());
            this.notifyItemChanged();
        }

        public boolean isTargetItem() {
            return this.pitem == 0L ? this.isTargetItem : (this.isTargetItem = BackForwardList.bflItemIsTargetItem(this.pitem));
        }

        public Entry[] getChildren() {
            Entry[] arrentry;
            if (this.pitem == 0L) {
                arrentry = this.children;
            } else {
                this.children = BackForwardList.bflItemGetChildren(this.pitem, this.ppage);
                arrentry = this.children;
            }
            return arrentry;
        }

        public String toString() {
            return "url=" + this.getURL() + ",title=" + this.getTitle() + ",date=" + this.getLastVisitedDate();
        }

        public void addChangeListener(WCChangeListener wCChangeListener) {
            if (wCChangeListener == null) {
                return;
            }
            this.listenerList.add(wCChangeListener);
        }

        public void removeChangeListener(WCChangeListener wCChangeListener) {
            if (wCChangeListener == null) {
                return;
            }
            this.listenerList.remove(wCChangeListener);
        }
    }
}

