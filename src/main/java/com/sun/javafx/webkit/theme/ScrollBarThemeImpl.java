/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.Observable
 *  javafx.geometry.Orientation
 *  javafx.scene.Node
 *  javafx.scene.control.Control
 *  javafx.scene.control.ScrollBar
 */
package com.sun.javafx.webkit.theme;

import com.sun.javafx.util.Utils;
import com.sun.javafx.webkit.Accessor;
import com.sun.javafx.webkit.theme.RenderThemeImpl;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCSize;
import java.lang.ref.WeakReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;

public final class ScrollBarThemeImpl
extends ScrollBarTheme {
    private static final Logger log = Logger.getLogger(ScrollBarThemeImpl.class.getName());
    private WeakReference<ScrollBar> testSBRef = new WeakReference<Object>(null);
    private boolean thicknessInitialized = false;
    private final Accessor accessor;
    private final RenderThemeImpl.Pool<ScrollBarWidget> pool;

    public ScrollBarThemeImpl(final Accessor accessor) {
        this.accessor = accessor;
        this.pool = new RenderThemeImpl.Pool<ScrollBarWidget>(scrollBarWidget -> accessor.removeChild((Node)scrollBarWidget), ScrollBarWidget.class);
        accessor.addViewListener(new RenderThemeImpl.ViewListener(this.pool, accessor){

            @Override
            public void invalidated(Observable observable) {
                super.invalidated(observable);
                ScrollBarWidget scrollBarWidget = new ScrollBarWidget();
                accessor.addChild((Node)scrollBarWidget);
                ScrollBarThemeImpl.this.testSBRef = new WeakReference<ScrollBarWidget>(scrollBarWidget);
            }
        });
    }

    private static Orientation convertOrientation(int n) {
        return n == 1 ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    private void adjustScrollBar(ScrollBar scrollBar, int n, int n2, int n3) {
        Orientation orientation = ScrollBarThemeImpl.convertOrientation(n3);
        if (orientation != scrollBar.getOrientation()) {
            scrollBar.setOrientation(orientation);
        }
        if (orientation == Orientation.VERTICAL) {
            n = ScrollBarTheme.getThickness();
        } else {
            n2 = ScrollBarTheme.getThickness();
        }
        if ((double)n != scrollBar.getWidth() || (double)n2 != scrollBar.getHeight()) {
            scrollBar.resize((double)n, (double)n2);
        }
    }

    private void adjustScrollBar(ScrollBar scrollBar, int n, int n2, int n3, int n4, int n5, int n6) {
        this.adjustScrollBar(scrollBar, n, n2, n3);
        boolean bl = n6 <= n5;
        scrollBar.setDisable(bl);
        if (bl) {
            return;
        }
        if (n4 < 0) {
            n4 = 0;
        } else if (n4 > n6 - n5) {
            n4 = n6 - n5;
        }
        if (scrollBar.getMax() != (double)n6 || scrollBar.getVisibleAmount() != (double)n5) {
            scrollBar.setValue(0.0);
            scrollBar.setMax((double)n6);
            scrollBar.setVisibleAmount((double)n5);
        }
        if (n6 > n5) {
            float f = (float)n6 / (float)(n6 - n5);
            if (scrollBar.getValue() != (double)((float)n4 * f)) {
                scrollBar.setValue((double)((float)n4 * f));
            }
        }
    }

    @Override
    protected Ref createWidget(long l, int n, int n2, int n3, int n4, int n5, int n6) {
        ScrollBarWidget scrollBarWidget = this.pool.get(l);
        if (scrollBarWidget == null) {
            scrollBarWidget = new ScrollBarWidget();
            this.pool.put(l, scrollBarWidget, this.accessor.getPage().getUpdateContentCycleID());
            this.accessor.addChild((Node)scrollBarWidget);
        }
        this.adjustScrollBar(scrollBarWidget, n, n2, n3, n4, n5, n6);
        return new ScrollBarRef(scrollBarWidget);
    }

    @Override
    public void paint(WCGraphicsContext wCGraphicsContext, Ref ref, int n, int n2, int n3, int n4) {
        ScrollBar scrollBar = (ScrollBar)((ScrollBarRef)ref).asControl();
        if (scrollBar == null) {
            return;
        }
        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, "[{0}, {1} {2}x{3}], {4}", new Object[]{n, n2, scrollBar.getWidth(), scrollBar.getHeight(), scrollBar.getOrientation() == Orientation.VERTICAL ? "VERTICAL" : "HORIZONTAL"});
        }
        wCGraphicsContext.saveState();
        wCGraphicsContext.translate(n, n2);
        Renderer.getRenderer().render((Control)scrollBar, wCGraphicsContext);
        wCGraphicsContext.restoreState();
    }

    @Override
    public WCSize getWidgetSize(Ref ref) {
        ScrollBar scrollBar = (ScrollBar)((ScrollBarRef)ref).asControl();
        if (scrollBar != null) {
            return new WCSize((float)scrollBar.getWidth(), (float)scrollBar.getHeight());
        }
        return new WCSize(0.0f, 0.0f);
    }

    @Override
    protected int hitTest(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        ScrollBar scrollBar;
        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, "[{0}, {1} {2}x{3}], {4}", new Object[]{n7, n8, n, n2, n3 == 1 ? "VERTICAL" : "HORIZONTAL"});
        }
        if ((scrollBar = (ScrollBar)this.testSBRef.get()) == null) {
            return 0;
        }
        Node node = ScrollBarThemeImpl.getThumb(scrollBar);
        Node node2 = ScrollBarThemeImpl.getTrack(scrollBar);
        Node node3 = ScrollBarThemeImpl.getDecButton(scrollBar);
        Node node4 = ScrollBarThemeImpl.getIncButton(scrollBar);
        this.adjustScrollBar(scrollBar, n, n2, n3, n4, n5, n6);
        if (n3 == 1) {
            n13 = n14 = n7;
            n12 = n14;
            n11 = n8 - (int)node3.getLayoutBounds().getHeight();
            n10 = n11 - this.thumbPosition();
            n9 = n11 - (int)node2.getLayoutBounds().getHeight();
        } else {
            n9 = n10 = n8;
            n11 = n10;
            n12 = n7 - (int)node3.getLayoutBounds().getWidth();
            n14 = n12 - this.thumbPosition();
            n13 = n12 - (int)node2.getLayoutBounds().getWidth();
        }
        if (node != null && node.isVisible() && node.contains((double)n14, (double)n10)) {
            log.finer("thumb");
            return 8;
        }
        if (node2 != null && node2.isVisible() && node2.contains((double)n12, (double)n11)) {
            if (n3 == 1 && this.thumbPosition() >= n11 || n3 == 0 && this.thumbPosition() >= n12) {
                log.finer("back track");
                return 4;
            }
            if (n3 == 1 && this.thumbPosition() < n11 || n3 == 0 && this.thumbPosition() < n12) {
                log.finer("forward track");
                return 16;
            }
        } else {
            if (node3 != null && node3.isVisible() && node3.contains((double)n7, (double)n8)) {
                log.finer("back button");
                return 1;
            }
            if (node4 != null && node4.isVisible() && node4.contains((double)n13, (double)n9)) {
                log.finer("forward button");
                return 2;
            }
        }
        log.finer("no part");
        return 0;
    }

    private int thumbPosition() {
        ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
        if (scrollBar == null) {
            return 0;
        }
        Node node = ScrollBarThemeImpl.getThumb(scrollBar);
        if (node == null) {
            return 0;
        }
        double d = scrollBar.getOrientation() == Orientation.VERTICAL ? node.getLayoutBounds().getHeight() : node.getLayoutBounds().getWidth();
        Node node2 = ScrollBarThemeImpl.getTrack(scrollBar);
        double d2 = scrollBar.getOrientation() == Orientation.VERTICAL ? node2.getLayoutBounds().getHeight() : node2.getLayoutBounds().getWidth();
        double d3 = Utils.clamp(scrollBar.getMin(), scrollBar.getValue(), scrollBar.getMax());
        double d4 = scrollBar.getMax() - scrollBar.getMin();
        return (int)Math.round(d4 > 0.0 ? (d2 - d) * (d3 - scrollBar.getMin()) / d4 : 0.0);
    }

    @Override
    protected int getThumbLength(int n, int n2, int n3, int n4, int n5, int n6) {
        ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
        if (scrollBar == null) {
            return 0;
        }
        Node node = ScrollBarThemeImpl.getThumb(scrollBar);
        if (node == null) {
            return 0;
        }
        this.adjustScrollBar(scrollBar, n, n2, n3, n4, n5, n6);
        double d = 0.0;
        d = n3 == 1 ? node.getLayoutBounds().getHeight() : node.getLayoutBounds().getWidth();
        log.log(Level.FINEST, "thumb length: {0}", d);
        return (int)d;
    }

    @Override
    protected int getTrackPosition(int n, int n2, int n3) {
        ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
        if (scrollBar == null) {
            return 0;
        }
        Node node = ScrollBarThemeImpl.getDecButton(scrollBar);
        if (node == null) {
            return 0;
        }
        this.adjustScrollBar(scrollBar, n, n2, n3);
        double d = 0.0;
        d = n3 == 1 ? node.getLayoutBounds().getHeight() : node.getLayoutBounds().getWidth();
        log.log(Level.FINEST, "track position: {0}", d);
        return (int)d;
    }

    @Override
    protected int getTrackLength(int n, int n2, int n3) {
        ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
        if (scrollBar == null) {
            return 0;
        }
        Node node = ScrollBarThemeImpl.getTrack(scrollBar);
        if (node == null) {
            return 0;
        }
        this.adjustScrollBar(scrollBar, n, n2, n3);
        double d = 0.0;
        d = n3 == 1 ? node.getLayoutBounds().getHeight() : node.getLayoutBounds().getWidth();
        log.log(Level.FINEST, "track length: {0}", d);
        return (int)d;
    }

    @Override
    protected int getThumbPosition(int n, int n2, int n3, int n4, int n5, int n6) {
        ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
        if (scrollBar == null) {
            return 0;
        }
        this.adjustScrollBar(scrollBar, n, n2, n3, n4, n5, n6);
        int n7 = this.thumbPosition();
        log.log(Level.FINEST, "thumb position: {0}", n7);
        return n7;
    }

    private void initializeThickness() {
        if (!this.thicknessInitialized) {
            ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
            if (scrollBar == null) {
                return;
            }
            int n = (int)scrollBar.prefWidth(-1.0);
            if (n != 0 && ScrollBarTheme.getThickness() != n) {
                ScrollBarTheme.setThickness(n);
            }
            this.thicknessInitialized = true;
        }
    }

    private static Node getThumb(ScrollBar scrollBar) {
        return ScrollBarThemeImpl.findNode(scrollBar, "thumb");
    }

    private static Node getTrack(ScrollBar scrollBar) {
        return ScrollBarThemeImpl.findNode(scrollBar, "track");
    }

    private static Node getIncButton(ScrollBar scrollBar) {
        return ScrollBarThemeImpl.findNode(scrollBar, "increment-button");
    }

    private static Node getDecButton(ScrollBar scrollBar) {
        return ScrollBarThemeImpl.findNode(scrollBar, "decrement-button");
    }

    private static Node findNode(ScrollBar scrollBar, String string) {
        for (Node node : scrollBar.getChildrenUnmodifiable()) {
            if (!node.getStyleClass().contains((Object)string)) continue;
            return node;
        }
        return null;
    }

    private static final class ScrollBarRef
    extends Ref {
        private final WeakReference<ScrollBarWidget> sbRef;

        private ScrollBarRef(ScrollBarWidget scrollBarWidget) {
            this.sbRef = new WeakReference<ScrollBarWidget>(scrollBarWidget);
        }

        private Control asControl() {
            return (Control)this.sbRef.get();
        }
    }

    private final class ScrollBarWidget
    extends ScrollBar
    implements RenderThemeImpl.Widget {
        private ScrollBarWidget() {
            this.setOrientation(Orientation.VERTICAL);
            this.setMin(0.0);
            this.setManaged(false);
        }

        public void impl_updatePeer() {
            super.impl_updatePeer();
            ScrollBarThemeImpl.this.initializeThickness();
        }

        @Override
        public RenderThemeImpl.WidgetType getType() {
            return RenderThemeImpl.WidgetType.SCROLLBAR;
        }

        protected void layoutChildren() {
            super.layoutChildren();
            ScrollBarThemeImpl.this.initializeThickness();
        }
    }
}

