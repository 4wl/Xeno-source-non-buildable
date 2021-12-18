/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 *  javafx.animation.Timeline
 *  javafx.beans.InvalidationListener
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.BooleanPropertyBase
 *  javafx.beans.value.ChangeListener
 *  javafx.collections.ObservableList
 *  javafx.event.EventDispatcher
 *  javafx.event.EventHandler
 *  javafx.geometry.Orientation
 *  javafx.scene.AccessibleRole
 *  javafx.scene.Group
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.control.Cell
 *  javafx.scene.control.IndexedCell
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.input.ScrollEvent
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 *  javafx.scene.shape.Rectangle
 *  javafx.util.Callback
 *  javafx.util.Duration
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.Logging;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.control.skin.VirtualScrollBar;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.scene.control.IndexedCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;
import sun.util.logging.PlatformLogger;

public class VirtualFlow<T extends IndexedCell>
extends Region {
    private static final int MIN_SCROLLING_LINES_PER_PAGE = 8;
    private boolean touchDetected = false;
    private boolean mouseDown = false;
    private BooleanProperty vertical;
    private boolean pannable = true;
    private int cellCount;
    private double position;
    private double fixedCellSize = 0.0;
    private boolean fixedCellSizeEnabled = false;
    private Callback<VirtualFlow, T> createCell;
    private double maxPrefBreadth;
    private double viewportBreadth;
    private double viewportLength;
    double lastWidth = -1.0;
    double lastHeight = -1.0;
    int lastCellCount = 0;
    boolean lastVertical;
    double lastPosition;
    double lastCellBreadth = -1.0;
    double lastCellLength = -1.0;
    final ArrayLinkedList<T> cells = new ArrayLinkedList();
    final ArrayLinkedList<T> pile = new ArrayLinkedList();
    T accumCell;
    Group accumCellParent;
    final Group sheet;
    final ObservableList<Node> sheetChildren;
    private VirtualScrollBar hbar = new VirtualScrollBar(this);
    private VirtualScrollBar vbar = new VirtualScrollBar(this);
    ClippedContainer clipView;
    StackPane corner;
    private double lastX;
    private double lastY;
    private boolean isPanning = false;
    private final List<T> privateCells = new ArrayList<T>();
    private static final String NEW_CELL = "newcell";
    private boolean needsReconfigureCells = false;
    private boolean needsRecreateCells = false;
    private boolean needsRebuildCells = false;
    private boolean needsCellsLayout = false;
    private boolean sizeChanged = false;
    private final BitSet dirtyCells = new BitSet();
    private static final double GOLDEN_RATIO_MULTIPLIER = 0.618033987;
    Timeline sbTouchTimeline;
    KeyFrame sbTouchKF1;
    KeyFrame sbTouchKF2;
    private boolean needBreadthBar;
    private boolean needLengthBar;
    private boolean tempVisibility = false;

    public final void setVertical(boolean bl) {
        this.verticalProperty().set(bl);
    }

    public final boolean isVertical() {
        return this.vertical == null ? true : this.vertical.get();
    }

    public final BooleanProperty verticalProperty() {
        if (this.vertical == null) {
            this.vertical = new BooleanPropertyBase(true){

                protected void invalidated() {
                    VirtualFlow.this.pile.clear();
                    VirtualFlow.this.sheetChildren.clear();
                    VirtualFlow.this.cells.clear();
                    VirtualFlow.this.lastHeight = -1.0;
                    VirtualFlow.this.lastWidth = -1.0;
                    VirtualFlow.this.setMaxPrefBreadth(-1.0);
                    VirtualFlow.this.setViewportBreadth(0.0);
                    VirtualFlow.this.setViewportLength(0.0);
                    VirtualFlow.this.lastPosition = 0.0;
                    VirtualFlow.this.hbar.setValue(0.0);
                    VirtualFlow.this.vbar.setValue(0.0);
                    VirtualFlow.this.setPosition(0.0);
                    VirtualFlow.this.setNeedsLayout(true);
                    VirtualFlow.this.requestLayout();
                }

                public Object getBean() {
                    return VirtualFlow.this;
                }

                public String getName() {
                    return "vertical";
                }
            };
        }
        return this.vertical;
    }

    public boolean isPannable() {
        return this.pannable;
    }

    public void setPannable(boolean bl) {
        this.pannable = bl;
    }

    public int getCellCount() {
        return this.cellCount;
    }

    public void setCellCount(int n) {
        VirtualScrollBar virtualScrollBar;
        boolean bl;
        int n2 = this.cellCount;
        this.cellCount = n;
        boolean bl2 = bl = n2 != this.cellCount;
        if (bl) {
            virtualScrollBar = this.isVertical() ? this.vbar : this.hbar;
            virtualScrollBar.setMax(n);
        }
        if (bl) {
            this.layoutChildren();
            this.sheetChildren.clear();
            virtualScrollBar = this.getParent();
            if (virtualScrollBar != null) {
                virtualScrollBar.requestLayout();
            }
        }
    }

    public double getPosition() {
        return this.position;
    }

    public void setPosition(double d) {
        boolean bl = this.position != d;
        this.position = com.sun.javafx.util.Utils.clamp(0.0, d, 1.0);
        if (bl) {
            this.requestLayout();
        }
    }

    public void setFixedCellSize(double d) {
        this.fixedCellSize = d;
        this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        this.needsCellsLayout = true;
        this.layoutChildren();
    }

    public Callback<VirtualFlow, T> getCreateCell() {
        return this.createCell;
    }

    public void setCreateCell(Callback<VirtualFlow, T> callback) {
        this.createCell = callback;
        if (this.createCell != null) {
            this.accumCell = null;
            this.setNeedsLayout(true);
            this.recreateCells();
            if (this.getParent() != null) {
                this.getParent().requestLayout();
            }
        }
    }

    protected final void setMaxPrefBreadth(double d) {
        this.maxPrefBreadth = d;
    }

    protected final double getMaxPrefBreadth() {
        return this.maxPrefBreadth;
    }

    protected final void setViewportBreadth(double d) {
        this.viewportBreadth = d;
    }

    protected final double getViewportBreadth() {
        return this.viewportBreadth;
    }

    void setViewportLength(double d) {
        this.viewportLength = d;
    }

    protected double getViewportLength() {
        return this.viewportLength;
    }

    protected List<T> getCells() {
        return this.cells;
    }

    protected final VirtualScrollBar getHbar() {
        return this.hbar;
    }

    protected final VirtualScrollBar getVbar() {
        return this.vbar;
    }

    public VirtualFlow() {
        this.getStyleClass().add((Object)"virtual-flow");
        this.setId("virtual-flow");
        this.sheet = new Group();
        this.sheet.getStyleClass().add((Object)"sheet");
        this.sheet.setAutoSizeChildren(false);
        this.sheetChildren = this.sheet.getChildren();
        this.clipView = new ClippedContainer(this);
        this.clipView.setNode((Node)this.sheet);
        this.getChildren().add((Object)this.clipView);
        this.accumCellParent = new Group();
        this.accumCellParent.setVisible(false);
        this.getChildren().add((Object)this.accumCellParent);
        EventDispatcher eventDispatcher = (event, eventDispatchChain) -> event;
        EventDispatcher eventDispatcher2 = this.hbar.getEventDispatcher();
        this.hbar.setEventDispatcher((event, eventDispatchChain) -> {
            if (event.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent)event).isDirect()) {
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher);
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher2);
                return eventDispatchChain.dispatchEvent(event);
            }
            return eventDispatcher2.dispatchEvent(event, eventDispatchChain);
        });
        EventDispatcher eventDispatcher3 = this.vbar.getEventDispatcher();
        this.vbar.setEventDispatcher((event, eventDispatchChain) -> {
            if (event.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent)event).isDirect()) {
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher);
                eventDispatchChain = eventDispatchChain.prepend(eventDispatcher3);
                return eventDispatchChain.dispatchEvent(event);
            }
            return eventDispatcher3.dispatchEvent(event, eventDispatchChain);
        });
        this.setOnScroll((EventHandler)new EventHandler<ScrollEvent>(){

            public void handle(ScrollEvent scrollEvent) {
                VirtualScrollBar virtualScrollBar;
                double d;
                if (BehaviorSkinBase.IS_TOUCH_SUPPORTED && !VirtualFlow.this.touchDetected && !VirtualFlow.this.mouseDown) {
                    VirtualFlow.this.startSBReleasedAnimation();
                }
                double d2 = 0.0;
                if (VirtualFlow.this.isVertical()) {
                    switch (scrollEvent.getTextDeltaYUnits()) {
                        case PAGES: {
                            d2 = scrollEvent.getTextDeltaY() * VirtualFlow.this.lastHeight;
                            break;
                        }
                        case LINES: {
                            if (VirtualFlow.this.fixedCellSizeEnabled) {
                                d = VirtualFlow.this.fixedCellSize;
                            } else {
                                IndexedCell indexedCell = (IndexedCell)VirtualFlow.this.cells.getLast();
                                d = (VirtualFlow.this.getCellPosition(indexedCell) + VirtualFlow.this.getCellLength(indexedCell) - VirtualFlow.this.getCellPosition((IndexedCell)VirtualFlow.this.cells.getFirst())) / (double)VirtualFlow.this.cells.size();
                            }
                            if (VirtualFlow.this.lastHeight / d < 8.0) {
                                d = VirtualFlow.this.lastHeight / 8.0;
                            }
                            d2 = scrollEvent.getTextDeltaY() * d;
                            break;
                        }
                        case NONE: {
                            d2 = scrollEvent.getDeltaY();
                        }
                    }
                } else {
                    switch (scrollEvent.getTextDeltaXUnits()) {
                        case CHARACTERS: 
                        case NONE: {
                            d = scrollEvent.getDeltaX();
                            double d3 = scrollEvent.getDeltaY();
                            double d4 = d2 = Math.abs(d) > Math.abs(d3) ? d : d3;
                        }
                    }
                }
                if (d2 != 0.0 && (d = VirtualFlow.this.adjustPixels(-d2)) != 0.0) {
                    scrollEvent.consume();
                }
                VirtualScrollBar virtualScrollBar2 = virtualScrollBar = VirtualFlow.this.isVertical() ? VirtualFlow.this.hbar : VirtualFlow.this.vbar;
                if (VirtualFlow.this.needBreadthBar) {
                    double d5;
                    double d6 = d5 = VirtualFlow.this.isVertical() ? scrollEvent.getDeltaX() : scrollEvent.getDeltaY();
                    if (d5 != 0.0) {
                        double d7 = virtualScrollBar.getValue() - d5;
                        if (d7 < virtualScrollBar.getMin()) {
                            virtualScrollBar.setValue(virtualScrollBar.getMin());
                        } else if (d7 > virtualScrollBar.getMax()) {
                            virtualScrollBar.setValue(virtualScrollBar.getMax());
                        } else {
                            virtualScrollBar.setValue(d7);
                        }
                        scrollEvent.consume();
                    }
                }
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, (EventHandler)new EventHandler<MouseEvent>(){

            public void handle(MouseEvent mouseEvent) {
                VirtualFlow.this.mouseDown = true;
                if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                    VirtualFlow.this.scrollBarOn();
                }
                if (VirtualFlow.this.isFocusTraversable()) {
                    boolean bl = true;
                    Node node = VirtualFlow.this.getScene().getFocusOwner();
                    if (node != null) {
                        for (Parent parent = node.getParent(); parent != null; parent = parent.getParent()) {
                            if (!parent.equals((Object)VirtualFlow.this)) continue;
                            bl = false;
                            break;
                        }
                    }
                    if (bl) {
                        VirtualFlow.this.requestFocus();
                    }
                }
                VirtualFlow.this.lastX = mouseEvent.getX();
                VirtualFlow.this.lastY = mouseEvent.getY();
                VirtualFlow.this.isPanning = !VirtualFlow.this.vbar.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY()) && !VirtualFlow.this.hbar.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY());
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            this.mouseDown = false;
            if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                this.startSBReleasedAnimation();
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            VirtualScrollBar virtualScrollBar;
            if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                this.scrollBarOn();
            }
            if (!this.isPanning || !this.isPannable()) {
                return;
            }
            double d = this.lastX - mouseEvent.getX();
            double d2 = this.lastY - mouseEvent.getY();
            double d3 = this.isVertical() ? d2 : d;
            double d4 = this.adjustPixels(d3);
            if (d4 != 0.0) {
                if (this.isVertical()) {
                    this.lastY = mouseEvent.getY();
                } else {
                    this.lastX = mouseEvent.getX();
                }
            }
            double d5 = this.isVertical() ? d : d2;
            VirtualScrollBar virtualScrollBar2 = virtualScrollBar = this.isVertical() ? this.hbar : this.vbar;
            if (virtualScrollBar.isVisible()) {
                double d6 = virtualScrollBar.getValue() + d5;
                if (d6 < virtualScrollBar.getMin()) {
                    virtualScrollBar.setValue(virtualScrollBar.getMin());
                } else if (d6 > virtualScrollBar.getMax()) {
                    virtualScrollBar.setValue(virtualScrollBar.getMax());
                } else {
                    virtualScrollBar.setValue(d6);
                    if (this.isVertical()) {
                        this.lastX = mouseEvent.getX();
                    } else {
                        this.lastY = mouseEvent.getY();
                    }
                }
            }
        });
        this.vbar.setOrientation(Orientation.VERTICAL);
        this.vbar.addEventHandler(MouseEvent.ANY, mouseEvent -> mouseEvent.consume());
        this.getChildren().add((Object)this.vbar);
        this.hbar.setOrientation(Orientation.HORIZONTAL);
        this.hbar.addEventHandler(MouseEvent.ANY, mouseEvent -> mouseEvent.consume());
        this.getChildren().add((Object)this.hbar);
        this.corner = new StackPane();
        this.corner.getStyleClass().setAll((Object[])new String[]{"corner"});
        this.getChildren().add((Object)this.corner);
        InvalidationListener invalidationListener = observable -> this.updateHbar();
        this.verticalProperty().addListener(invalidationListener);
        this.hbar.valueProperty().addListener(invalidationListener);
        this.hbar.visibleProperty().addListener(invalidationListener);
        ChangeListener changeListener = (observableValue, number, number2) -> this.clipView.setClipY(this.isVertical() ? 0.0 : this.vbar.getValue());
        this.vbar.valueProperty().addListener(changeListener);
        super.heightProperty().addListener((observableValue, number, number2) -> {
            if (number.doubleValue() == 0.0 && number2.doubleValue() > 0.0) {
                this.recreateCells();
            }
        });
        this.setOnTouchPressed(touchEvent -> {
            this.touchDetected = true;
            this.scrollBarOn();
        });
        this.setOnTouchReleased(touchEvent -> {
            this.touchDetected = false;
            this.startSBReleasedAnimation();
        });
        this.setImpl_traversalEngine(new ParentTraversalEngine((Parent)this, new Algorithm(){

            Node selectNextAfterIndex(int n, TraversalContext traversalContext) {
                Object t;
                while ((t = VirtualFlow.this.getVisibleCell(++n)) != null) {
                    if (t.isFocusTraversable()) {
                        return t;
                    }
                    Node node = traversalContext.selectFirstInParent((Parent)t);
                    if (node == null) continue;
                    return node;
                }
                return null;
            }

            Node selectPreviousBeforeIndex(int n, TraversalContext traversalContext) {
                Object t;
                while ((t = VirtualFlow.this.getVisibleCell(--n)) != null) {
                    Node node = traversalContext.selectLastInParent((Parent)t);
                    if (node != null) {
                        return node;
                    }
                    if (!t.isFocusTraversable()) continue;
                    return t;
                }
                return null;
            }

            @Override
            public Node select(Node node, Direction direction, TraversalContext traversalContext) {
                Object object;
                if (VirtualFlow.this.cells.isEmpty()) {
                    return null;
                }
                if (VirtualFlow.this.cells.contains((Object)node)) {
                    object = (IndexedCell)node;
                } else {
                    object = this.findOwnerCell(node);
                    Node node2 = traversalContext.selectInSubtree((Parent)object, node, direction);
                    if (node2 != null) {
                        return node2;
                    }
                    if (direction == Direction.NEXT) {
                        direction = Direction.NEXT_IN_LINE;
                    }
                }
                int n = object.getIndex();
                switch (direction) {
                    case PREVIOUS: {
                        return this.selectPreviousBeforeIndex(n, traversalContext);
                    }
                    case NEXT: {
                        Node node3 = traversalContext.selectFirstInParent((Parent)object);
                        if (node3 != null) {
                            return node3;
                        }
                    }
                    case NEXT_IN_LINE: {
                        return this.selectNextAfterIndex(n, traversalContext);
                    }
                }
                return null;
            }

            private T findOwnerCell(Node node) {
                Parent parent = node.getParent();
                while (!VirtualFlow.this.cells.contains((Object)parent)) {
                    parent = parent.getParent();
                }
                return (Object)((IndexedCell)parent);
            }

            @Override
            public Node selectFirst(TraversalContext traversalContext) {
                IndexedCell indexedCell = (IndexedCell)VirtualFlow.this.cells.getFirst();
                if (indexedCell == null) {
                    return null;
                }
                if (indexedCell.isFocusTraversable()) {
                    return indexedCell;
                }
                Node node = traversalContext.selectFirstInParent((Parent)indexedCell);
                if (node != null) {
                    return node;
                }
                return this.selectNextAfterIndex(indexedCell.getIndex(), traversalContext);
            }

            @Override
            public Node selectLast(TraversalContext traversalContext) {
                IndexedCell indexedCell = (IndexedCell)VirtualFlow.this.cells.getLast();
                if (indexedCell == null) {
                    return null;
                }
                Node node = traversalContext.selectLastInParent((Parent)indexedCell);
                if (node != null) {
                    return node;
                }
                if (indexedCell.isFocusTraversable()) {
                    return indexedCell;
                }
                return this.selectPreviousBeforeIndex(indexedCell.getIndex(), traversalContext);
            }
        }));
    }

    void updateHbar() {
        if (!this.isVisible() || this.getScene() == null) {
            return;
        }
        if (this.isVertical()) {
            if (this.hbar.isVisible()) {
                this.clipView.setClipX(this.hbar.getValue());
            } else {
                this.clipView.setClipX(0.0);
                this.hbar.setValue(0.0);
            }
        }
    }

    public void requestLayout() {
        this.setNeedsLayout(true);
    }

    protected void layoutChildren() {
        double d;
        int n;
        boolean bl;
        double d2;
        int n2;
        int n3;
        if (this.needsRecreateCells) {
            this.lastWidth = -1.0;
            this.lastHeight = -1.0;
            this.releaseCell(this.accumCell);
            this.sheet.getChildren().clear();
            n3 = this.cells.size();
            for (n2 = 0; n2 < n3; ++n2) {
                ((IndexedCell)this.cells.get(n2)).updateIndex(-1);
            }
            this.cells.clear();
            this.pile.clear();
            this.releaseAllPrivateCells();
        } else if (this.needsRebuildCells) {
            this.lastWidth = -1.0;
            this.lastHeight = -1.0;
            this.releaseCell(this.accumCell);
            for (n2 = 0; n2 < this.cells.size(); ++n2) {
                ((IndexedCell)this.cells.get(n2)).updateIndex(-1);
            }
            this.addAllToPile();
            this.releaseAllPrivateCells();
        } else if (this.needsReconfigureCells) {
            this.setMaxPrefBreadth(-1.0);
            this.lastWidth = -1.0;
            this.lastHeight = -1.0;
        }
        if (!this.dirtyCells.isEmpty()) {
            n3 = this.cells.size();
            while ((n2 = this.dirtyCells.nextSetBit(0)) != -1 && n2 < n3) {
                IndexedCell indexedCell = (IndexedCell)this.cells.get(n2);
                if (indexedCell != null) {
                    indexedCell.requestLayout();
                }
                this.dirtyCells.clear(n2);
            }
            this.setMaxPrefBreadth(-1.0);
            this.lastWidth = -1.0;
            this.lastHeight = -1.0;
        }
        n2 = this.sizeChanged;
        n3 = this.needsRebuildCells || this.needsRecreateCells || this.sizeChanged ? 1 : 0;
        this.needsRecreateCells = false;
        this.needsReconfigureCells = false;
        this.needsRebuildCells = false;
        this.sizeChanged = false;
        if (this.needsCellsLayout) {
            int n4 = this.cells.size();
            for (int i = 0; i < n4; ++i) {
                Cell cell = (Cell)this.cells.get(i);
                if (cell == null) continue;
                cell.requestLayout();
            }
            this.needsCellsLayout = false;
            return;
        }
        double d3 = this.getWidth();
        double d4 = this.getHeight();
        boolean bl2 = this.isVertical();
        double d5 = this.getPosition();
        if (d3 <= 0.0 || d4 <= 0.0) {
            this.addAllToPile();
            this.lastWidth = d3;
            this.lastHeight = d4;
            this.hbar.setVisible(false);
            this.vbar.setVisible(false);
            this.corner.setVisible(false);
            return;
        }
        boolean bl3 = false;
        boolean bl4 = false;
        if (BehaviorSkinBase.IS_TOUCH_SUPPORTED && (this.tempVisibility && (!this.hbar.isVisible() || !this.vbar.isVisible()) || !this.tempVisibility && (this.hbar.isVisible() || this.vbar.isVisible()))) {
            bl4 = true;
        }
        if (!bl3) {
            Cell cell;
            for (int i = 0; i < this.cells.size() && !(bl3 = (cell = (Cell)this.cells.get(i)).isNeedsLayout()); ++i) {
            }
        }
        T t = this.getFirstVisibleCell();
        if (!bl3 && !bl4) {
            boolean bl5 = false;
            if (t != null) {
                double d6 = this.getCellBreadth((Cell)t);
                d2 = this.getCellLength(t);
                bl5 = d6 != this.lastCellBreadth || d2 != this.lastCellLength;
                this.lastCellBreadth = d6;
                this.lastCellLength = d2;
            }
            if (d3 == this.lastWidth && d4 == this.lastHeight && this.cellCount == this.lastCellCount && bl2 == this.lastVertical && d5 == this.lastPosition && !bl5) {
                return;
            }
        }
        boolean bl6 = false;
        boolean bl7 = bl = bl3 || bl2 != this.lastVertical || this.cells.isEmpty() || this.getMaxPrefBreadth() == -1.0 || d5 != this.lastPosition || this.cellCount != this.lastCellCount || n2 != 0 || bl2 && d4 < this.lastHeight || !bl2 && d3 < this.lastWidth;
        if (!bl) {
            double d7 = this.getMaxPrefBreadth();
            boolean bl8 = false;
            for (n = 0; n < this.cells.size(); ++n) {
                d = this.getCellBreadth((Cell)this.cells.get(n));
                if (d7 == d) {
                    bl8 = true;
                    continue;
                }
                if (!(d > d7)) continue;
                bl = true;
                break;
            }
            if (!bl8) {
                bl = true;
            }
        }
        if (!bl && (bl2 && d4 > this.lastHeight || !bl2 && d3 > this.lastWidth)) {
            bl6 = true;
        }
        this.initViewport();
        int n5 = this.computeCurrentIndex();
        if (this.lastCellCount != this.cellCount) {
            if (d5 != 0.0 && d5 != 1.0) {
                if (n5 >= this.cellCount) {
                    this.setPosition(1.0);
                } else if (t != null) {
                    d2 = this.getCellPosition(t);
                    n = this.getCellIndex(t);
                    this.adjustPositionToIndex(n);
                    d = -this.computeOffsetForCell(n);
                    this.adjustByPixelAmount(d - d2);
                }
            }
            n5 = this.computeCurrentIndex();
        }
        if (bl) {
            this.setMaxPrefBreadth(-1.0);
            this.addAllToPile();
            d2 = -this.computeViewportOffset(this.getPosition());
            this.addLeadingCells(n5, d2);
            this.addTrailingCells(true);
        } else if (bl6) {
            this.addTrailingCells(true);
        }
        this.computeBarVisiblity();
        this.updateScrollBarsAndCells(n3 != 0);
        this.lastWidth = this.getWidth();
        this.lastHeight = this.getHeight();
        this.lastCellCount = this.getCellCount();
        this.lastVertical = this.isVertical();
        this.lastPosition = this.getPosition();
        this.cleanPile();
    }

    protected void addLeadingCells(int n, double d) {
        double d2 = d;
        int n2 = n;
        boolean bl = true;
        IndexedCell indexedCell = null;
        if (n2 == this.cellCount && d2 == this.getViewportLength()) {
            --n2;
            bl = false;
        }
        while (n2 >= 0 && (d2 > 0.0 || bl)) {
            indexedCell = (IndexedCell)this.getAvailableCell(n2);
            this.setCellIndex((T)indexedCell, n2);
            this.resizeCellSize((T)indexedCell);
            this.cells.addFirst(indexedCell);
            if (bl) {
                bl = false;
            } else {
                d2 -= this.getCellLength((T)indexedCell);
            }
            this.positionCell((T)indexedCell, d2);
            this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth((Cell)indexedCell)));
            indexedCell.setVisible(true);
            --n2;
        }
        if (this.cells.size() > 0) {
            indexedCell = (IndexedCell)this.cells.getFirst();
            int n3 = this.getCellIndex((T)indexedCell);
            double d3 = this.getCellPosition((T)indexedCell);
            if (n3 == 0 && d3 > 0.0) {
                this.setPosition(0.0);
                d2 = 0.0;
                for (int i = 0; i < this.cells.size(); ++i) {
                    indexedCell = (IndexedCell)this.cells.get(i);
                    this.positionCell((T)indexedCell, d2);
                    d2 += this.getCellLength((T)indexedCell);
                }
            }
        } else {
            this.vbar.setValue(0.0);
            this.hbar.setValue(0.0);
        }
    }

    protected boolean addTrailingCells(boolean bl) {
        Object object;
        if (this.cells.isEmpty()) {
            return false;
        }
        IndexedCell indexedCell = (IndexedCell)this.cells.getLast();
        double d = this.getCellPosition((T)indexedCell) + this.getCellLength((T)indexedCell);
        int n = this.getCellIndex((T)indexedCell) + 1;
        boolean bl2 = n <= this.cellCount;
        double d2 = this.getViewportLength();
        if (d < 0.0 && !bl) {
            return false;
        }
        double d3 = d2 - d;
        while (d < d2) {
            if (n >= this.cellCount) {
                if (d < d2) {
                    bl2 = false;
                }
                if (!bl) {
                    return bl2;
                }
                if ((double)n > d3) {
                    object = Logging.getControlsLogger();
                    if (((PlatformLogger)object).isLoggable(PlatformLogger.Level.INFO)) {
                        if (indexedCell != null) {
                            ((PlatformLogger)object).info("index exceeds maxCellCount. Check size calculations for " + indexedCell.getClass());
                        } else {
                            ((PlatformLogger)object).info("index exceeds maxCellCount");
                        }
                    }
                    return bl2;
                }
            }
            object = this.getAvailableCell(n);
            this.setCellIndex(object, n);
            this.resizeCellSize(object);
            this.cells.addLast(object);
            this.positionCell(object, d);
            this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth((Cell)object)));
            d += this.getCellLength(object);
            object.setVisible(true);
            ++n;
        }
        object = (IndexedCell)this.cells.getFirst();
        n = this.getCellIndex(object);
        T t = this.getLastVisibleCell();
        double d4 = this.getCellPosition(object);
        double d5 = this.getCellPosition(t) + this.getCellLength(t);
        if ((n != 0 || n == 0 && d4 < 0.0) && bl && t != null && this.getCellIndex(t) == this.cellCount - 1 && d5 < d2) {
            double d6;
            double d7 = d2 - d5;
            for (double d8 = d5; d8 < d2 && n != 0 && -d4 < d7; d8 += d6) {
                T t2 = this.getAvailableCell(--n);
                this.setCellIndex(t2, n);
                this.resizeCellSize(t2);
                this.cells.addFirst(t2);
                d6 = this.getCellLength(t2);
                this.positionCell(t2, d4 -= d6);
                this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth((Cell)t2)));
                t2.setVisible(true);
            }
            object = (IndexedCell)this.cells.getFirst();
            d4 = this.getCellPosition(object);
            double d9 = d2 - d5;
            if (this.getCellIndex(object) == 0 && d9 > -d4) {
                d9 = -d4;
            }
            for (int i = 0; i < this.cells.size(); ++i) {
                IndexedCell indexedCell2 = (IndexedCell)this.cells.get(i);
                this.positionCell((T)indexedCell2, this.getCellPosition((T)indexedCell2) + d9);
            }
            d4 = this.getCellPosition(object);
            if (this.getCellIndex(object) == 0 && d4 == 0.0) {
                this.setPosition(0.0);
            } else if (this.getPosition() != 1.0) {
                this.setPosition(1.0);
            }
        }
        return bl2;
    }

    private boolean computeBarVisiblity() {
        if (this.cells.isEmpty()) {
            this.needLengthBar = false;
            this.needBreadthBar = false;
            return true;
        }
        boolean bl = this.isVertical();
        boolean bl2 = false;
        VirtualScrollBar virtualScrollBar = bl ? this.hbar : this.vbar;
        VirtualScrollBar virtualScrollBar2 = bl ? this.vbar : this.hbar;
        double d = this.getViewportBreadth();
        int n = this.cells.size();
        for (int i = 0; i < 2; ++i) {
            boolean bl3;
            boolean bl4;
            boolean bl5 = bl4 = this.getPosition() > 0.0 || this.cellCount > n || this.cellCount == n && this.getCellPosition((T)((IndexedCell)this.cells.getLast())) + this.getCellLength((T)((IndexedCell)this.cells.getLast())) > this.getViewportLength() || this.cellCount == n - 1 && bl2 && this.needBreadthBar;
            if (bl4 ^ this.needLengthBar) {
                this.needLengthBar = bl4;
                bl2 = true;
            }
            boolean bl6 = bl3 = this.maxPrefBreadth > d;
            if (!(bl3 ^ this.needBreadthBar)) continue;
            this.needBreadthBar = bl3;
            bl2 = true;
        }
        if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
            this.updateViewportDimensions();
            virtualScrollBar.setVisible(this.needBreadthBar);
            virtualScrollBar2.setVisible(this.needLengthBar);
        } else {
            virtualScrollBar.setVisible(this.needBreadthBar && this.tempVisibility);
            virtualScrollBar2.setVisible(this.needLengthBar && this.tempVisibility);
        }
        return bl2;
    }

    private void updateViewportDimensions() {
        boolean bl = this.isVertical();
        double d = this.snapSize(bl ? this.hbar.prefHeight(-1.0) : this.vbar.prefWidth(-1.0));
        double d2 = this.snapSize(bl ? this.vbar.prefWidth(-1.0) : this.hbar.prefHeight(-1.0));
        this.setViewportBreadth((bl ? this.getWidth() : this.getHeight()) - (this.needLengthBar ? d2 : 0.0));
        this.setViewportLength((bl ? this.getHeight() : this.getWidth()) - (this.needBreadthBar ? d : 0.0));
    }

    private void initViewport() {
        boolean bl = this.isVertical();
        this.updateViewportDimensions();
        VirtualScrollBar virtualScrollBar = bl ? this.hbar : this.vbar;
        VirtualScrollBar virtualScrollBar2 = bl ? this.vbar : this.hbar;
        virtualScrollBar.setVirtual(false);
        virtualScrollBar2.setVirtual(true);
    }

    protected void setWidth(double d) {
        if (d != this.lastWidth) {
            super.setWidth(d);
            this.sizeChanged = true;
            this.setNeedsLayout(true);
            this.requestLayout();
        }
    }

    protected void setHeight(double d) {
        if (d != this.lastHeight) {
            super.setHeight(d);
            this.sizeChanged = true;
            this.setNeedsLayout(true);
            this.requestLayout();
        }
    }

    private void updateScrollBarsAndCells(boolean bl) {
        double d;
        double d2;
        boolean bl2 = this.isVertical();
        VirtualScrollBar virtualScrollBar = bl2 ? this.hbar : this.vbar;
        VirtualScrollBar virtualScrollBar2 = bl2 ? this.vbar : this.hbar;
        this.fitCells();
        if (!this.cells.isEmpty()) {
            IndexedCell indexedCell;
            int n;
            d2 = -this.computeViewportOffset(this.getPosition());
            int n2 = this.computeCurrentIndex() - ((IndexedCell)this.cells.getFirst()).getIndex();
            int n3 = this.cells.size();
            d = d2;
            for (n = n2 - 1; n >= 0 && n < n3; --n) {
                indexedCell = (IndexedCell)this.cells.get(n);
                this.positionCell((T)indexedCell, d -= this.getCellLength((T)indexedCell));
            }
            d = d2;
            for (n = n2; n >= 0 && n < n3; ++n) {
                indexedCell = (IndexedCell)this.cells.get(n);
                this.positionCell((T)indexedCell, d);
                d += this.getCellLength((T)indexedCell);
            }
        }
        this.corner.setVisible(virtualScrollBar.isVisible() && virtualScrollBar2.isVisible());
        d2 = 0.0;
        double d3 = (bl2 ? this.getHeight() : this.getWidth()) - (virtualScrollBar.isVisible() ? virtualScrollBar.prefHeight(-1.0) : 0.0);
        d = this.getViewportBreadth();
        double d4 = this.getViewportLength();
        if (virtualScrollBar.isVisible()) {
            double d5;
            if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                if (bl2) {
                    this.hbar.resizeRelocate(0.0, d4, d, this.hbar.prefHeight(d));
                } else {
                    this.vbar.resizeRelocate(d4, 0.0, this.vbar.prefWidth(d), d);
                }
            } else if (bl2) {
                this.hbar.resizeRelocate(0.0, d4 - this.hbar.getHeight(), d, this.hbar.prefHeight(d));
            } else {
                this.vbar.resizeRelocate(d4 - this.vbar.getWidth(), 0.0, this.vbar.prefWidth(d), d);
            }
            if (this.getMaxPrefBreadth() != -1.0 && (d5 = Math.max(1.0, this.getMaxPrefBreadth() - d)) != virtualScrollBar.getMax()) {
                boolean bl3;
                virtualScrollBar.setMax(d5);
                double d6 = virtualScrollBar.getValue();
                boolean bl4 = bl3 = d6 != 0.0 && d5 == d6;
                if (bl3 || d6 > d5) {
                    virtualScrollBar.setValue(d5);
                }
                virtualScrollBar.setVisibleAmount(d / this.getMaxPrefBreadth() * d5);
            }
        }
        if (bl && (virtualScrollBar2.isVisible() || BehaviorSkinBase.IS_TOUCH_SUPPORTED)) {
            int n = 0;
            int n4 = this.cells.size();
            for (int i = 0; i < n4; ++i) {
                IndexedCell indexedCell = (IndexedCell)this.cells.get(i);
                if (indexedCell == null || indexedCell.isEmpty()) continue;
                if ((d2 += bl2 ? indexedCell.getHeight() : indexedCell.getWidth()) > d3) break;
                ++n;
            }
            virtualScrollBar2.setMax(1.0);
            if (n == 0 && this.cellCount == 1) {
                virtualScrollBar2.setVisibleAmount(d3 / d2);
            } else {
                virtualScrollBar2.setVisibleAmount((float)n / (float)this.cellCount);
            }
        }
        if (virtualScrollBar2.isVisible()) {
            if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                if (bl2) {
                    this.vbar.resizeRelocate(d, 0.0, this.vbar.prefWidth(d4), d4);
                } else {
                    this.hbar.resizeRelocate(0.0, d, d4, this.hbar.prefHeight(-1.0));
                }
            } else if (bl2) {
                this.vbar.resizeRelocate(d - this.vbar.getWidth(), 0.0, this.vbar.prefWidth(d4), d4);
            } else {
                this.hbar.resizeRelocate(0.0, d - this.hbar.getHeight(), d4, this.hbar.prefHeight(-1.0));
            }
        }
        if (this.corner.isVisible()) {
            if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                this.corner.resize(this.vbar.getWidth(), this.hbar.getHeight());
                this.corner.relocate(this.hbar.getLayoutX() + this.hbar.getWidth(), this.vbar.getLayoutY() + this.vbar.getHeight());
            } else {
                this.corner.resize(this.vbar.getWidth(), this.hbar.getHeight());
                this.corner.relocate(this.hbar.getLayoutX() + (this.hbar.getWidth() - this.vbar.getWidth()), this.vbar.getLayoutY() + (this.vbar.getHeight() - this.hbar.getHeight()));
                this.hbar.resize(this.hbar.getWidth() - this.vbar.getWidth(), this.hbar.getHeight());
                this.vbar.resize(this.vbar.getWidth(), this.vbar.getHeight() - this.hbar.getHeight());
            }
        }
        this.clipView.resize(this.snapSize(bl2 ? d : d4), this.snapSize(bl2 ? d4 : d));
        if (this.getPosition() != virtualScrollBar2.getValue()) {
            virtualScrollBar2.setValue(this.getPosition());
        }
    }

    private void fitCells() {
        double d = Math.max(this.getMaxPrefBreadth(), this.getViewportBreadth());
        boolean bl = this.isVertical();
        for (int i = 0; i < this.cells.size(); ++i) {
            Cell cell = (Cell)this.cells.get(i);
            if (bl) {
                cell.resize(d, cell.prefHeight(d));
                continue;
            }
            cell.resize(cell.prefWidth(d), d);
        }
    }

    private void cull() {
        double d = this.getViewportLength();
        for (int i = this.cells.size() - 1; i >= 0; --i) {
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i);
            double d2 = this.getCellLength((T)indexedCell);
            double d3 = this.getCellPosition((T)indexedCell);
            double d4 = d3 + d2;
            if (!(d3 >= d) && !(d4 < 0.0)) continue;
            this.addToPile((T)((IndexedCell)this.cells.remove(i)));
        }
    }

    protected int getCellIndex(T t) {
        return t.getIndex();
    }

    public T getCell(int n) {
        Callback<VirtualFlow, T> callback;
        T t;
        if (!this.cells.isEmpty() && (t = this.getVisibleCell(n)) != null) {
            return t;
        }
        for (int i = 0; i < this.pile.size(); ++i) {
            IndexedCell indexedCell = (IndexedCell)this.pile.get(i);
            if (this.getCellIndex((T)indexedCell) != n) continue;
            return (T)indexedCell;
        }
        if (this.pile.size() > 0) {
            return (T)((IndexedCell)this.pile.get(0));
        }
        if (this.accumCell == null && (callback = this.getCreateCell()) != null) {
            this.accumCell = (IndexedCell)callback.call((Object)this);
            this.accumCell.getProperties().put((Object)NEW_CELL, null);
            this.accumCellParent.getChildren().setAll((Object[])new Node[]{this.accumCell});
            this.accumCell.setAccessibleRole(AccessibleRole.NODE);
            this.accumCell.getChildrenUnmodifiable().addListener(observable -> {
                for (Node node : this.accumCell.getChildrenUnmodifiable()) {
                    node.setAccessibleRole(AccessibleRole.NODE);
                }
            });
        }
        this.setCellIndex(this.accumCell, n);
        this.resizeCellSize(this.accumCell);
        return this.accumCell;
    }

    private void releaseCell(T t) {
        if (this.accumCell != null && t == this.accumCell) {
            this.accumCell.updateIndex(-1);
        }
    }

    T getPrivateCell(int n) {
        Callback<VirtualFlow, T> callback;
        IndexedCell indexedCell = null;
        if (!this.cells.isEmpty() && (indexedCell = (IndexedCell)this.getVisibleCell(n)) != null) {
            indexedCell.layout();
            return (T)indexedCell;
        }
        if (indexedCell == null) {
            for (int i = 0; i < this.sheetChildren.size(); ++i) {
                IndexedCell indexedCell2 = (IndexedCell)this.sheetChildren.get(i);
                if (this.getCellIndex((T)indexedCell2) != n) continue;
                return (T)indexedCell2;
            }
        }
        if (indexedCell == null && (callback = this.getCreateCell()) != null) {
            indexedCell = (IndexedCell)callback.call((Object)this);
        }
        if (indexedCell != null) {
            this.setCellIndex((T)indexedCell, n);
            this.resizeCellSize((T)indexedCell);
            indexedCell.setVisible(false);
            this.sheetChildren.add((Object)indexedCell);
            this.privateCells.add((T)indexedCell);
        }
        return (T)indexedCell;
    }

    private void releaseAllPrivateCells() {
        this.sheetChildren.removeAll(this.privateCells);
    }

    protected double getCellLength(int n) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        T t = this.getCell(n);
        double d = this.getCellLength(t);
        this.releaseCell(t);
        return d;
    }

    protected double getCellBreadth(int n) {
        T t = this.getCell(n);
        double d = this.getCellBreadth((Cell)t);
        this.releaseCell(t);
        return d;
    }

    protected double getCellLength(T t) {
        if (t == null) {
            return 0.0;
        }
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return this.isVertical() ? t.getLayoutBounds().getHeight() : t.getLayoutBounds().getWidth();
    }

    protected double getCellBreadth(Cell cell) {
        return this.isVertical() ? cell.prefWidth(-1.0) : cell.prefHeight(-1.0);
    }

    protected double getCellPosition(T t) {
        if (t == null) {
            return 0.0;
        }
        return this.isVertical() ? t.getLayoutY() : t.getLayoutX();
    }

    protected void positionCell(T t, double d) {
        if (this.isVertical()) {
            t.setLayoutX(0.0);
            t.setLayoutY(this.snapSize(d));
        } else {
            t.setLayoutX(this.snapSize(d));
            t.setLayoutY(0.0);
        }
    }

    protected void resizeCellSize(T t) {
        if (t == null) {
            return;
        }
        if (this.isVertical()) {
            double d = Math.max(this.getMaxPrefBreadth(), this.getViewportBreadth());
            t.resize(d, this.fixedCellSizeEnabled ? this.fixedCellSize : Utils.boundedSize(t.prefHeight(d), t.minHeight(d), t.maxHeight(d)));
        } else {
            double d = Math.max(this.getMaxPrefBreadth(), this.getViewportBreadth());
            t.resize(this.fixedCellSizeEnabled ? this.fixedCellSize : Utils.boundedSize(t.prefWidth(d), t.minWidth(d), t.maxWidth(d)), d);
        }
    }

    protected void setCellIndex(T t, int n) {
        assert (t != null);
        t.updateIndex(n);
        if (t.isNeedsLayout() && t.getScene() != null || t.getProperties().containsKey((Object)NEW_CELL)) {
            t.applyCss();
            t.getProperties().remove((Object)NEW_CELL);
        }
    }

    protected T getAvailableCell(int n) {
        int n2;
        IndexedCell indexedCell = null;
        int n3 = this.pile.size();
        for (n2 = 0; n2 < n3; ++n2) {
            IndexedCell indexedCell2 = (IndexedCell)this.pile.get(n2);
            assert (indexedCell2 != null);
            if (this.getCellIndex((T)indexedCell2) == n) {
                indexedCell = indexedCell2;
                this.pile.remove(n2);
                break;
            }
            indexedCell = null;
        }
        if (indexedCell == null) {
            if (this.pile.size() > 0) {
                n2 = (n & 1) == 0 ? 1 : 0;
                int n4 = this.pile.size();
                for (n3 = 0; n3 < n4; ++n3) {
                    IndexedCell indexedCell3 = (IndexedCell)this.pile.get(n3);
                    int n5 = this.getCellIndex((T)indexedCell3);
                    if ((n5 & 1) == 0 && n2 != 0) {
                        indexedCell = indexedCell3;
                        this.pile.remove(n3);
                        break;
                    }
                    if ((n5 & 1) != 1 || n2 != 0) continue;
                    indexedCell = indexedCell3;
                    this.pile.remove(n3);
                    break;
                }
                if (indexedCell == null) {
                    indexedCell = (IndexedCell)this.pile.removeFirst();
                }
            } else {
                indexedCell = (IndexedCell)this.getCreateCell().call((Object)this);
                indexedCell.getProperties().put((Object)NEW_CELL, null);
            }
        }
        if (indexedCell.getParent() == null) {
            this.sheetChildren.add(indexedCell);
        }
        return (T)indexedCell;
    }

    protected void addAllToPile() {
        int n = this.cells.size();
        for (int i = 0; i < n; ++i) {
            this.addToPile((T)((IndexedCell)this.cells.removeFirst()));
        }
    }

    private void addToPile(T t) {
        assert (t != null);
        this.pile.addLast(t);
    }

    private void cleanPile() {
        boolean bl = false;
        int n = this.pile.size();
        for (int i = 0; i < n; ++i) {
            IndexedCell indexedCell = (IndexedCell)this.pile.get(i);
            bl = bl || this.doesCellContainFocus((Cell<?>)indexedCell);
            indexedCell.setVisible(false);
        }
        if (bl) {
            this.requestFocus();
        }
    }

    private boolean doesCellContainFocus(Cell<?> cell) {
        Node node;
        Scene scene = cell.getScene();
        Node node2 = node = scene == null ? null : scene.getFocusOwner();
        if (node != null) {
            if (cell.equals((Object)node)) {
                return true;
            }
            for (Parent parent = node.getParent(); parent != null && !(parent instanceof VirtualFlow); parent = parent.getParent()) {
                if (!cell.equals((Object)parent)) continue;
                return true;
            }
        }
        return false;
    }

    public T getVisibleCell(int n) {
        IndexedCell indexedCell;
        if (this.cells.isEmpty()) {
            return null;
        }
        IndexedCell indexedCell2 = (IndexedCell)this.cells.getLast();
        int n2 = this.getCellIndex((T)indexedCell2);
        if (n == n2) {
            return (T)indexedCell2;
        }
        IndexedCell indexedCell3 = (IndexedCell)this.cells.getFirst();
        int n3 = this.getCellIndex((T)indexedCell3);
        if (n == n3) {
            return (T)indexedCell3;
        }
        if (n > n3 && n < n2 && this.getCellIndex((T)(indexedCell = (IndexedCell)this.cells.get(n - n3))) == n) {
            return (T)indexedCell;
        }
        return null;
    }

    public T getLastVisibleCell() {
        if (this.cells.isEmpty() || this.getViewportLength() <= 0.0) {
            return null;
        }
        for (int i = this.cells.size() - 1; i >= 0; --i) {
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i);
            if (indexedCell.isEmpty()) continue;
            return (T)indexedCell;
        }
        return null;
    }

    public T getFirstVisibleCell() {
        if (this.cells.isEmpty() || this.getViewportLength() <= 0.0) {
            return null;
        }
        IndexedCell indexedCell = (IndexedCell)this.cells.getFirst();
        return (T)(indexedCell.isEmpty() ? null : indexedCell);
    }

    public T getLastVisibleCellWithinViewPort() {
        if (this.cells.isEmpty() || this.getViewportLength() <= 0.0) {
            return null;
        }
        double d = this.getViewportLength();
        for (int i = this.cells.size() - 1; i >= 0; --i) {
            double d2;
            double d3;
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i);
            if (indexedCell.isEmpty() || !((d3 = (d2 = this.getCellPosition((T)indexedCell)) + this.getCellLength((T)indexedCell)) <= d + 2.0)) continue;
            return (T)indexedCell;
        }
        return null;
    }

    public T getFirstVisibleCellWithinViewPort() {
        if (this.cells.isEmpty() || this.getViewportLength() <= 0.0) {
            return null;
        }
        for (int i = 0; i < this.cells.size(); ++i) {
            double d;
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i);
            if (indexedCell.isEmpty() || !((d = this.getCellPosition((T)indexedCell)) >= 0.0)) continue;
            return (T)indexedCell;
        }
        return null;
    }

    public void showAsFirst(T t) {
        if (t != null) {
            this.adjustPixels(this.getCellPosition(t));
        }
    }

    public void showAsLast(T t) {
        if (t != null) {
            this.adjustPixels(this.getCellPosition(t) + this.getCellLength(t) - this.getViewportLength());
        }
    }

    public void show(T t) {
        if (t != null) {
            double d = this.getCellPosition(t);
            double d2 = this.getCellLength(t);
            double d3 = d + d2;
            double d4 = this.getViewportLength();
            if (d < 0.0) {
                this.adjustPixels(d);
            } else if (d3 > d4) {
                this.adjustPixels(d3 - d4);
            }
        }
    }

    public void show(int n) {
        T t = this.getVisibleCell(n);
        if (t != null) {
            this.show(t);
        } else {
            T t2 = this.getVisibleCell(n - 1);
            if (t2 != null) {
                t = this.getAvailableCell(n);
                this.setCellIndex(t, n);
                this.resizeCellSize(t);
                this.cells.addLast(t);
                this.positionCell(t, this.getCellPosition(t2) + this.getCellLength(t2));
                this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth((Cell)t)));
                t.setVisible(true);
                this.show(t);
                return;
            }
            T t3 = this.getVisibleCell(n + 1);
            if (t3 != null) {
                t = this.getAvailableCell(n);
                this.setCellIndex(t, n);
                this.resizeCellSize(t);
                this.cells.addFirst(t);
                this.positionCell(t, this.getCellPosition(t3) - this.getCellLength(t));
                this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth((Cell)t)));
                t.setVisible(true);
                this.show(t);
                return;
            }
            this.adjustPositionToIndex(n);
            this.addAllToPile();
            this.requestLayout();
        }
    }

    public void scrollTo(int n) {
        boolean bl = false;
        if (n >= this.cellCount - 1) {
            this.setPosition(1.0);
            bl = true;
        } else if (n < 0) {
            this.setPosition(0.0);
            bl = true;
        }
        if (!bl) {
            this.adjustPositionToIndex(n);
            double d = -this.computeOffsetForCell(n);
            this.adjustByPixelAmount(d);
        }
        this.requestLayout();
    }

    public void scrollToOffset(int n) {
        this.adjustPixels((double)n * this.getCellLength(0));
    }

    public double adjustPixels(double d) {
        if (d == 0.0) {
            return 0.0;
        }
        boolean bl = this.isVertical();
        if (bl && (!this.tempVisibility ? !this.vbar.isVisible() : !this.needLengthBar) || !bl && (this.tempVisibility ? !this.needLengthBar : !this.hbar.isVisible())) {
            return 0.0;
        }
        double d2 = this.getPosition();
        if (d2 == 0.0 && d < 0.0) {
            return 0.0;
        }
        if (d2 == 1.0 && d > 0.0) {
            return 0.0;
        }
        this.adjustByPixelAmount(d);
        if (d2 == this.getPosition()) {
            return 0.0;
        }
        if (this.cells.size() > 0) {
            double d3;
            int n;
            for (int i = 0; i < this.cells.size(); ++i) {
                IndexedCell indexedCell = (IndexedCell)this.cells.get(i);
                assert (indexedCell != null);
                this.positionCell((T)indexedCell, this.getCellPosition((T)indexedCell) - d);
            }
            IndexedCell indexedCell = (IndexedCell)this.cells.getFirst();
            double d4 = indexedCell == null ? 0.0 : this.getCellPosition((T)indexedCell);
            for (n = 0; n < this.cells.size(); ++n) {
                IndexedCell indexedCell2 = (IndexedCell)this.cells.get(n);
                assert (indexedCell2 != null);
                double d5 = this.getCellPosition((T)indexedCell2);
                if (d5 != d4) {
                    this.positionCell((T)indexedCell2, d4);
                }
                d4 += this.getCellLength((T)indexedCell2);
            }
            this.cull();
            indexedCell = (IndexedCell)this.cells.getFirst();
            if (indexedCell != null) {
                n = this.getCellIndex((T)indexedCell);
                d3 = this.getCellLength(n - 1);
                this.addLeadingCells(n - 1, this.getCellPosition((T)indexedCell) - d3);
            } else {
                n = this.computeCurrentIndex();
                d3 = -this.computeViewportOffset(this.getPosition());
                this.addLeadingCells(n, d3);
            }
            if (!this.addTrailingCells(false)) {
                double d6;
                T t = this.getLastVisibleCell();
                d3 = this.getCellLength(t);
                double d7 = this.getCellPosition(t) + d3;
                if (d7 < (d6 = this.getViewportLength())) {
                    int n2;
                    double d8 = d6 - d7;
                    for (n2 = 0; n2 < this.cells.size(); ++n2) {
                        IndexedCell indexedCell3 = (IndexedCell)this.cells.get(n2);
                        this.positionCell((T)indexedCell3, this.getCellPosition((T)indexedCell3) + d8);
                    }
                    this.setPosition(1.0);
                    indexedCell = (IndexedCell)this.cells.getFirst();
                    n2 = this.getCellIndex((T)indexedCell);
                    double d9 = this.getCellLength(n2 - 1);
                    this.addLeadingCells(n2 - 1, this.getCellPosition((T)indexedCell) - d9);
                }
            }
        }
        this.cull();
        this.updateScrollBarsAndCells(false);
        this.lastPosition = this.getPosition();
        return d;
    }

    public void reconfigureCells() {
        this.needsReconfigureCells = true;
        this.requestLayout();
    }

    public void recreateCells() {
        this.needsRecreateCells = true;
        this.requestLayout();
    }

    public void rebuildCells() {
        this.needsRebuildCells = true;
        this.requestLayout();
    }

    public void requestCellLayout() {
        this.needsCellsLayout = true;
        this.requestLayout();
    }

    public void setCellDirty(int n) {
        this.dirtyCells.set(n);
        this.requestLayout();
    }

    private double getPrefBreadth(double d) {
        double d2 = this.getMaxCellWidth(10);
        if (d > -1.0) {
            double d3 = this.getPrefLength();
            d2 = Math.max(d2, d3 * 0.618033987);
        }
        return d2;
    }

    private double getPrefLength() {
        double d = 0.0;
        int n = Math.min(10, this.cellCount);
        for (int i = 0; i < n; ++i) {
            d += this.getCellLength(i);
        }
        return d;
    }

    protected double computePrefWidth(double d) {
        double d2 = this.isVertical() ? this.getPrefBreadth(d) : this.getPrefLength();
        return d2 + this.vbar.prefWidth(-1.0);
    }

    protected double computePrefHeight(double d) {
        double d2 = this.isVertical() ? this.getPrefLength() : this.getPrefBreadth(d);
        return d2 + this.hbar.prefHeight(-1.0);
    }

    double getMaxCellWidth(int n) {
        double d = 0.0;
        int n2 = Math.max(1, n == -1 ? this.cellCount : n);
        for (int i = 0; i < n2; ++i) {
            d = Math.max(d, this.getCellBreadth(i));
        }
        return d;
    }

    private double computeViewportOffset(double d) {
        double d2 = com.sun.javafx.util.Utils.clamp(0.0, d, 1.0);
        double d3 = d2 * (double)this.getCellCount();
        int n = (int)d3;
        double d4 = d3 - (double)n;
        double d5 = this.getCellLength(n);
        double d6 = d5 * d4;
        double d7 = this.getViewportLength() * d2;
        return d6 - d7;
    }

    private void adjustPositionToIndex(int n) {
        int n2 = this.getCellCount();
        if (n2 <= 0) {
            this.setPosition(0.0);
        } else {
            this.setPosition((double)n / (double)n2);
        }
    }

    private void adjustByPixelAmount(double d) {
        double d2;
        if (d == 0.0) {
            return;
        }
        boolean bl = d > 0.0;
        int n = this.getCellCount();
        double d3 = this.getPosition() * (double)n;
        int n2 = (int)d3;
        if (bl && n2 == n) {
            return;
        }
        double d4 = this.getCellLength(n2);
        double d5 = d3 - (double)n2;
        double d6 = d4 * d5;
        double d7 = 1.0 / (double)n;
        double d8 = this.computeOffsetForCell(n2);
        double d9 = d4 + this.computeOffsetForCell(n2 + 1);
        double d10 = d9 - d8;
        double d11 = d7 * (double)n2;
        for (d2 = bl ? d + d6 - this.getViewportLength() * this.getPosition() - d8 : -d + d9 - (d6 - this.getViewportLength() * this.getPosition()); d2 > d10 && (bl && n2 < n - 1 || !bl && n2 > 0); d2 -= d10) {
            n2 = bl ? ++n2 : --n2;
            d4 = this.getCellLength(n2);
            d8 = this.computeOffsetForCell(n2);
            d9 = d4 + this.computeOffsetForCell(n2 + 1);
            d10 = d9 - d8;
            d11 = d7 * (double)n2;
        }
        if (d2 > d10) {
            this.setPosition(bl ? 1.0 : 0.0);
        } else if (bl) {
            double d12 = d7 / Math.abs(d9 - d8);
            this.setPosition(d11 + d12 * d2);
        } else {
            double d13 = d7 / Math.abs(d9 - d8);
            this.setPosition(d11 + d7 - d13 * d2);
        }
    }

    private int computeCurrentIndex() {
        return (int)(this.getPosition() * (double)this.getCellCount());
    }

    private double computeOffsetForCell(int n) {
        double d = this.getCellCount();
        double d2 = com.sun.javafx.util.Utils.clamp(0.0, (double)n, d) / d;
        return -(this.getViewportLength() * d2);
    }

    protected void startSBReleasedAnimation() {
        if (this.sbTouchTimeline == null) {
            this.sbTouchTimeline = new Timeline();
            this.sbTouchKF1 = new KeyFrame(Duration.millis((double)0.0), actionEvent -> {
                this.tempVisibility = true;
                this.requestLayout();
            }, new KeyValue[0]);
            this.sbTouchKF2 = new KeyFrame(Duration.millis((double)1000.0), actionEvent -> {
                if (!this.touchDetected && !this.mouseDown) {
                    this.tempVisibility = false;
                    this.requestLayout();
                }
            }, new KeyValue[0]);
            this.sbTouchTimeline.getKeyFrames().addAll((Object[])new KeyFrame[]{this.sbTouchKF1, this.sbTouchKF2});
        }
        this.sbTouchTimeline.playFromStart();
    }

    protected void scrollBarOn() {
        this.tempVisibility = true;
        this.requestLayout();
    }

    public static class ArrayLinkedList<T>
    extends AbstractList<T> {
        private final ArrayList<T> array = new ArrayList(50);
        private int firstIndex = -1;
        private int lastIndex = -1;

        public ArrayLinkedList() {
            for (int i = 0; i < 50; ++i) {
                this.array.add(null);
            }
        }

        public T getFirst() {
            return this.firstIndex == -1 ? null : (T)this.array.get(this.firstIndex);
        }

        public T getLast() {
            return this.lastIndex == -1 ? null : (T)this.array.get(this.lastIndex);
        }

        public void addFirst(T t) {
            if (this.firstIndex == -1) {
                this.firstIndex = this.lastIndex = this.array.size() / 2;
                this.array.set(this.firstIndex, t);
            } else if (this.firstIndex == 0) {
                this.array.add(0, t);
                ++this.lastIndex;
            } else {
                this.array.set(--this.firstIndex, t);
            }
        }

        public void addLast(T t) {
            if (this.firstIndex == -1) {
                this.firstIndex = this.lastIndex = this.array.size() / 2;
                this.array.set(this.lastIndex, t);
            } else if (this.lastIndex == this.array.size() - 1) {
                this.array.add(++this.lastIndex, t);
            } else {
                this.array.set(++this.lastIndex, t);
            }
        }

        @Override
        public int size() {
            return this.firstIndex == -1 ? 0 : this.lastIndex - this.firstIndex + 1;
        }

        @Override
        public boolean isEmpty() {
            return this.firstIndex == -1;
        }

        @Override
        public T get(int n) {
            if (n > this.lastIndex - this.firstIndex || n < 0) {
                return null;
            }
            return this.array.get(this.firstIndex + n);
        }

        @Override
        public void clear() {
            for (int i = 0; i < this.array.size(); ++i) {
                this.array.set(i, null);
            }
            this.lastIndex = -1;
            this.firstIndex = -1;
        }

        public T removeFirst() {
            if (this.isEmpty()) {
                return null;
            }
            return this.remove(0);
        }

        public T removeLast() {
            if (this.isEmpty()) {
                return null;
            }
            return this.remove(this.lastIndex - this.firstIndex);
        }

        @Override
        public T remove(int n) {
            if (n > this.lastIndex - this.firstIndex || n < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (n == 0) {
                T t = this.array.get(this.firstIndex);
                this.array.set(this.firstIndex, null);
                if (this.firstIndex == this.lastIndex) {
                    this.lastIndex = -1;
                    this.firstIndex = -1;
                } else {
                    ++this.firstIndex;
                }
                return t;
            }
            if (n == this.lastIndex - this.firstIndex) {
                T t = this.array.get(this.lastIndex);
                this.array.set(this.lastIndex--, null);
                return t;
            }
            T t = this.array.get(this.firstIndex + n);
            this.array.set(this.firstIndex + n, null);
            for (int i = this.firstIndex + n + 1; i <= this.lastIndex; ++i) {
                this.array.set(i - 1, this.array.get(i));
            }
            this.array.set(this.lastIndex--, null);
            return t;
        }
    }

    static class ClippedContainer
    extends Region {
        private Node node;
        private final Rectangle clipRect;

        public Node getNode() {
            return this.node;
        }

        public void setNode(Node node) {
            this.node = node;
            this.getChildren().clear();
            this.getChildren().add((Object)this.node);
        }

        public void setClipX(double d) {
            this.setLayoutX(-d);
            this.clipRect.setLayoutX(d);
        }

        public void setClipY(double d) {
            this.setLayoutY(-d);
            this.clipRect.setLayoutY(d);
        }

        public ClippedContainer(VirtualFlow<?> virtualFlow) {
            if (virtualFlow == null) {
                throw new IllegalArgumentException("VirtualFlow can not be null");
            }
            this.getStyleClass().add((Object)"clipped-container");
            this.clipRect = new Rectangle();
            this.clipRect.setSmooth(false);
            this.setClip((Node)this.clipRect);
            super.widthProperty().addListener(observable -> this.clipRect.setWidth(this.getWidth()));
            super.heightProperty().addListener(observable -> this.clipRect.setHeight(this.getHeight()));
        }
    }
}

