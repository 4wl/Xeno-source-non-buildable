/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.geometry.Orientation
 *  javafx.geometry.Point2D
 *  javafx.scene.AccessibleAction
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.AccessibleRole
 *  javafx.scene.Node
 *  javafx.scene.control.ScrollBar
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.ScrollEvent
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ScrollBarBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.util.Utils;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class ScrollBarSkin
extends BehaviorSkinBase<ScrollBar, ScrollBarBehavior> {
    public static final int DEFAULT_LENGTH = 100;
    public static final int DEFAULT_WIDTH = 20;
    private StackPane thumb;
    private StackPane trackBackground;
    private StackPane track;
    private EndButton incButton;
    private EndButton decButton;
    private double trackLength;
    private double thumbLength;
    private double preDragThumbPos;
    private Point2D dragStart;
    private double trackPos;
    private static final double DEFAULT_EMBEDDED_SB_BREADTH = 8.0;

    public ScrollBarSkin(ScrollBar scrollBar) {
        super(scrollBar, new ScrollBarBehavior(scrollBar));
        this.initialize();
        ((ScrollBar)this.getSkinnable()).requestLayout();
        this.registerChangeListener((ObservableValue<?>)scrollBar.minProperty(), "MIN");
        this.registerChangeListener((ObservableValue<?>)scrollBar.maxProperty(), "MAX");
        this.registerChangeListener((ObservableValue<?>)scrollBar.valueProperty(), "VALUE");
        this.registerChangeListener((ObservableValue<?>)scrollBar.orientationProperty(), "ORIENTATION");
        this.registerChangeListener((ObservableValue<?>)scrollBar.visibleAmountProperty(), "VISIBLE_AMOUNT");
    }

    private void initialize() {
        this.track = new StackPane();
        this.track.getStyleClass().setAll((Object[])new String[]{"track"});
        this.trackBackground = new StackPane();
        this.trackBackground.getStyleClass().setAll((Object[])new String[]{"track-background"});
        this.thumb = new StackPane(){

            public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
                switch (accessibleAttribute) {
                    case VALUE: {
                        return ((ScrollBar)ScrollBarSkin.this.getSkinnable()).getValue();
                    }
                }
                return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
            }
        };
        this.thumb.getStyleClass().setAll((Object[])new String[]{"thumb"});
        this.thumb.setAccessibleRole(AccessibleRole.THUMB);
        if (!IS_TOUCH_SUPPORTED) {
            this.incButton = new EndButton("increment-button", "increment-arrow"){

                public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
                    switch (accessibleAction) {
                        case FIRE: {
                            ((ScrollBar)ScrollBarSkin.this.getSkinnable()).increment();
                            break;
                        }
                        default: {
                            super.executeAccessibleAction(accessibleAction, arrobject);
                        }
                    }
                }
            };
            this.incButton.setAccessibleRole(AccessibleRole.INCREMENT_BUTTON);
            this.incButton.setOnMousePressed(mouseEvent -> {
                if (!this.thumb.isVisible() || this.trackLength > this.thumbLength) {
                    ((ScrollBarBehavior)this.getBehavior()).incButtonPressed();
                }
                mouseEvent.consume();
            });
            this.incButton.setOnMouseReleased(mouseEvent -> {
                if (!this.thumb.isVisible() || this.trackLength > this.thumbLength) {
                    ((ScrollBarBehavior)this.getBehavior()).incButtonReleased();
                }
                mouseEvent.consume();
            });
            this.decButton = new EndButton("decrement-button", "decrement-arrow"){

                public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
                    switch (accessibleAction) {
                        case FIRE: {
                            ((ScrollBar)ScrollBarSkin.this.getSkinnable()).decrement();
                            break;
                        }
                        default: {
                            super.executeAccessibleAction(accessibleAction, arrobject);
                        }
                    }
                }
            };
            this.decButton.setAccessibleRole(AccessibleRole.DECREMENT_BUTTON);
            this.decButton.setOnMousePressed(mouseEvent -> {
                if (!this.thumb.isVisible() || this.trackLength > this.thumbLength) {
                    ((ScrollBarBehavior)this.getBehavior()).decButtonPressed();
                }
                mouseEvent.consume();
            });
            this.decButton.setOnMouseReleased(mouseEvent -> {
                if (!this.thumb.isVisible() || this.trackLength > this.thumbLength) {
                    ((ScrollBarBehavior)this.getBehavior()).decButtonReleased();
                }
                mouseEvent.consume();
            });
        }
        this.track.setOnMousePressed(mouseEvent -> {
            if (!this.thumb.isPressed() && mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (((ScrollBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                    if (this.trackLength != 0.0) {
                        ((ScrollBarBehavior)this.getBehavior()).trackPress(mouseEvent.getY() / this.trackLength);
                        mouseEvent.consume();
                    }
                } else if (this.trackLength != 0.0) {
                    ((ScrollBarBehavior)this.getBehavior()).trackPress(mouseEvent.getX() / this.trackLength);
                    mouseEvent.consume();
                }
            }
        });
        this.track.setOnMouseReleased(mouseEvent -> {
            ((ScrollBarBehavior)this.getBehavior()).trackRelease();
            mouseEvent.consume();
        });
        this.thumb.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isSynthesized()) {
                mouseEvent.consume();
                return;
            }
            if (((ScrollBar)this.getSkinnable()).getMax() > ((ScrollBar)this.getSkinnable()).getMin()) {
                this.dragStart = this.thumb.localToParent(mouseEvent.getX(), mouseEvent.getY());
                double d = Utils.clamp(((ScrollBar)this.getSkinnable()).getMin(), ((ScrollBar)this.getSkinnable()).getValue(), ((ScrollBar)this.getSkinnable()).getMax());
                this.preDragThumbPos = (d - ((ScrollBar)this.getSkinnable()).getMin()) / (((ScrollBar)this.getSkinnable()).getMax() - ((ScrollBar)this.getSkinnable()).getMin());
                mouseEvent.consume();
            }
        });
        this.thumb.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.isSynthesized()) {
                mouseEvent.consume();
                return;
            }
            if (((ScrollBar)this.getSkinnable()).getMax() > ((ScrollBar)this.getSkinnable()).getMin()) {
                if (this.trackLength > this.thumbLength) {
                    Point2D point2D = this.thumb.localToParent(mouseEvent.getX(), mouseEvent.getY());
                    if (this.dragStart == null) {
                        this.dragStart = this.thumb.localToParent(mouseEvent.getX(), mouseEvent.getY());
                    }
                    double d = ((ScrollBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? point2D.getY() - this.dragStart.getY() : point2D.getX() - this.dragStart.getX();
                    ((ScrollBarBehavior)this.getBehavior()).thumbDragged(this.preDragThumbPos + d / (this.trackLength - this.thumbLength));
                }
                mouseEvent.consume();
            }
        });
        this.thumb.setOnScrollStarted(scrollEvent -> {
            if (scrollEvent.isDirect() && ((ScrollBar)this.getSkinnable()).getMax() > ((ScrollBar)this.getSkinnable()).getMin()) {
                this.dragStart = this.thumb.localToParent(scrollEvent.getX(), scrollEvent.getY());
                double d = Utils.clamp(((ScrollBar)this.getSkinnable()).getMin(), ((ScrollBar)this.getSkinnable()).getValue(), ((ScrollBar)this.getSkinnable()).getMax());
                this.preDragThumbPos = (d - ((ScrollBar)this.getSkinnable()).getMin()) / (((ScrollBar)this.getSkinnable()).getMax() - ((ScrollBar)this.getSkinnable()).getMin());
                scrollEvent.consume();
            }
        });
        this.thumb.setOnScroll(scrollEvent -> {
            if (scrollEvent.isDirect() && ((ScrollBar)this.getSkinnable()).getMax() > ((ScrollBar)this.getSkinnable()).getMin()) {
                if (this.trackLength > this.thumbLength) {
                    Point2D point2D = this.thumb.localToParent(scrollEvent.getX(), scrollEvent.getY());
                    if (this.dragStart == null) {
                        this.dragStart = this.thumb.localToParent(scrollEvent.getX(), scrollEvent.getY());
                    }
                    double d = ((ScrollBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? point2D.getY() - this.dragStart.getY() : point2D.getX() - this.dragStart.getX();
                    ((ScrollBarBehavior)this.getBehavior()).thumbDragged(this.preDragThumbPos + d / (this.trackLength - this.thumbLength));
                }
                scrollEvent.consume();
                return;
            }
        });
        ((ScrollBar)this.getSkinnable()).addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
            if (this.trackLength > this.thumbLength) {
                double d;
                double d2 = scrollEvent.getDeltaX();
                double d3 = scrollEvent.getDeltaY();
                d2 = Math.abs(d2) < Math.abs(d3) ? d3 : d2;
                ScrollBar scrollBar = (ScrollBar)this.getSkinnable();
                double d4 = d = ((ScrollBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? d3 : d2;
                if (scrollEvent.isDirect()) {
                    if (this.trackLength > this.thumbLength) {
                        ((ScrollBarBehavior)this.getBehavior()).thumbDragged((((ScrollBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? scrollEvent.getY() : scrollEvent.getX()) / this.trackLength);
                        scrollEvent.consume();
                    }
                } else if (d > 0.0 && scrollBar.getValue() > scrollBar.getMin()) {
                    scrollBar.decrement();
                    scrollEvent.consume();
                } else if (d < 0.0 && scrollBar.getValue() < scrollBar.getMax()) {
                    scrollBar.increment();
                    scrollEvent.consume();
                }
            }
        });
        this.getChildren().clear();
        if (!IS_TOUCH_SUPPORTED) {
            this.getChildren().addAll((Object[])new Node[]{this.trackBackground, this.incButton, this.decButton, this.track, this.thumb});
        } else {
            this.getChildren().addAll((Object[])new Node[]{this.track, this.thumb});
        }
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ORIENTATION".equals(string)) {
            ((ScrollBar)this.getSkinnable()).requestLayout();
        } else if ("MIN".equals(string) || "MAX".equals(string) || "VISIBLE_AMOUNT".equals(string)) {
            this.positionThumb();
            ((ScrollBar)this.getSkinnable()).requestLayout();
        } else if ("VALUE".equals(string)) {
            this.positionThumb();
        }
    }

    double getBreadth() {
        if (!IS_TOUCH_SUPPORTED) {
            if (((ScrollBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                return Math.max(this.decButton.prefWidth(-1.0), this.incButton.prefWidth(-1.0)) + this.snappedLeftInset() + this.snappedRightInset();
            }
            return Math.max(this.decButton.prefHeight(-1.0), this.incButton.prefHeight(-1.0)) + this.snappedTopInset() + this.snappedBottomInset();
        }
        if (((ScrollBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            return Math.max(8.0, 8.0) + this.snappedLeftInset() + this.snappedRightInset();
        }
        return Math.max(8.0, 8.0) + this.snappedTopInset() + this.snappedBottomInset();
    }

    double minThumbLength() {
        return 1.5 * this.getBreadth();
    }

    double minTrackLength() {
        return 2.0 * this.getBreadth();
    }

    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        if (((ScrollBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            return this.getBreadth();
        }
        if (!IS_TOUCH_SUPPORTED) {
            return this.decButton.minWidth(-1.0) + this.incButton.minWidth(-1.0) + this.minTrackLength() + d5 + d3;
        }
        return this.minTrackLength() + d5 + d3;
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        if (((ScrollBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            if (!IS_TOUCH_SUPPORTED) {
                return this.decButton.minHeight(-1.0) + this.incButton.minHeight(-1.0) + this.minTrackLength() + d2 + d4;
            }
            return this.minTrackLength() + d2 + d4;
        }
        return this.getBreadth();
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        ScrollBar scrollBar = (ScrollBar)this.getSkinnable();
        return scrollBar.getOrientation() == Orientation.VERTICAL ? this.getBreadth() : 100.0 + d5 + d3;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        ScrollBar scrollBar = (ScrollBar)this.getSkinnable();
        return scrollBar.getOrientation() == Orientation.VERTICAL ? 100.0 + d2 + d4 : this.getBreadth();
    }

    protected double computeMaxWidth(double d, double d2, double d3, double d4, double d5) {
        ScrollBar scrollBar = (ScrollBar)this.getSkinnable();
        return scrollBar.getOrientation() == Orientation.VERTICAL ? scrollBar.prefWidth(-1.0) : Double.MAX_VALUE;
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        ScrollBar scrollBar = (ScrollBar)this.getSkinnable();
        return scrollBar.getOrientation() == Orientation.VERTICAL ? Double.MAX_VALUE : scrollBar.prefHeight(-1.0);
    }

    void positionThumb() {
        ScrollBar scrollBar = (ScrollBar)this.getSkinnable();
        double d = Utils.clamp(scrollBar.getMin(), scrollBar.getValue(), scrollBar.getMax());
        double d2 = this.trackPos = scrollBar.getMax() - scrollBar.getMin() > 0.0 ? (this.trackLength - this.thumbLength) * (d - scrollBar.getMin()) / (scrollBar.getMax() - scrollBar.getMin()) : 0.0;
        if (!IS_TOUCH_SUPPORTED) {
            this.trackPos = scrollBar.getOrientation() == Orientation.VERTICAL ? (this.trackPos += this.decButton.prefHeight(-1.0)) : (this.trackPos += this.decButton.prefWidth(-1.0));
        }
        this.thumb.setTranslateX(this.snapPosition(scrollBar.getOrientation() == Orientation.VERTICAL ? this.snappedLeftInset() : this.trackPos + this.snappedLeftInset()));
        this.thumb.setTranslateY(this.snapPosition(scrollBar.getOrientation() == Orientation.VERTICAL ? this.trackPos + this.snappedTopInset() : this.snappedTopInset()));
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        ScrollBar scrollBar = (ScrollBar)this.getSkinnable();
        double d5 = scrollBar.getMax() > scrollBar.getMin() ? scrollBar.getVisibleAmount() / (scrollBar.getMax() - scrollBar.getMin()) : 1.0;
        if (scrollBar.getOrientation() == Orientation.VERTICAL) {
            if (!IS_TOUCH_SUPPORTED) {
                double d6 = this.snapSize(this.decButton.prefHeight(-1.0));
                double d7 = this.snapSize(this.incButton.prefHeight(-1.0));
                this.decButton.resize(d3, d6);
                this.incButton.resize(d3, d7);
                this.trackLength = this.snapSize(d4 - (d6 + d7));
                this.thumbLength = this.snapSize(Utils.clamp(this.minThumbLength(), this.trackLength * d5, this.trackLength));
                this.trackBackground.resizeRelocate(this.snapPosition(d), this.snapPosition(d2), d3, this.trackLength + d6 + d7);
                this.decButton.relocate(this.snapPosition(d), this.snapPosition(d2));
                this.incButton.relocate(this.snapPosition(d), this.snapPosition(d2 + d4 - d7));
                this.track.resizeRelocate(this.snapPosition(d), this.snapPosition(d2 + d6), d3, this.trackLength);
                this.thumb.resize(this.snapSize(d >= 0.0 ? d3 : d3 + d), this.thumbLength);
                this.positionThumb();
            } else {
                this.trackLength = this.snapSize(d4);
                this.thumbLength = this.snapSize(Utils.clamp(this.minThumbLength(), this.trackLength * d5, this.trackLength));
                this.track.resizeRelocate(this.snapPosition(d), this.snapPosition(d2), d3, this.trackLength);
                this.thumb.resize(this.snapSize(d >= 0.0 ? d3 : d3 + d), this.thumbLength);
                this.positionThumb();
            }
        } else {
            if (!IS_TOUCH_SUPPORTED) {
                double d8 = this.snapSize(this.decButton.prefWidth(-1.0));
                double d9 = this.snapSize(this.incButton.prefWidth(-1.0));
                this.decButton.resize(d8, d4);
                this.incButton.resize(d9, d4);
                this.trackLength = this.snapSize(d3 - (d8 + d9));
                this.thumbLength = this.snapSize(Utils.clamp(this.minThumbLength(), this.trackLength * d5, this.trackLength));
                this.trackBackground.resizeRelocate(this.snapPosition(d), this.snapPosition(d2), this.trackLength + d8 + d9, d4);
                this.decButton.relocate(this.snapPosition(d), this.snapPosition(d2));
                this.incButton.relocate(this.snapPosition(d + d3 - d9), this.snapPosition(d2));
                this.track.resizeRelocate(this.snapPosition(d + d8), this.snapPosition(d2), this.trackLength, d4);
                this.thumb.resize(this.thumbLength, this.snapSize(d2 >= 0.0 ? d4 : d4 + d2));
                this.positionThumb();
            } else {
                this.trackLength = this.snapSize(d3);
                this.thumbLength = this.snapSize(Utils.clamp(this.minThumbLength(), this.trackLength * d5, this.trackLength));
                this.track.resizeRelocate(this.snapPosition(d), this.snapPosition(d2), this.trackLength, d4);
                this.thumb.resize(this.thumbLength, this.snapSize(d2 >= 0.0 ? d4 : d4 + d2));
                this.positionThumb();
            }
            scrollBar.resize(this.snapSize(scrollBar.getWidth()), this.snapSize(scrollBar.getHeight()));
        }
        if (scrollBar.getOrientation() == Orientation.VERTICAL && d4 >= this.computeMinHeight(-1.0, (int)d2, this.snappedRightInset(), this.snappedBottomInset(), (int)d) - (d2 + this.snappedBottomInset()) || scrollBar.getOrientation() == Orientation.HORIZONTAL && d3 >= this.computeMinWidth(-1.0, (int)d2, this.snappedRightInset(), this.snappedBottomInset(), (int)d) - (d + this.snappedRightInset())) {
            this.trackBackground.setVisible(true);
            this.track.setVisible(true);
            this.thumb.setVisible(true);
            if (!IS_TOUCH_SUPPORTED) {
                this.incButton.setVisible(true);
                this.decButton.setVisible(true);
            }
        } else {
            this.trackBackground.setVisible(false);
            this.track.setVisible(false);
            this.thumb.setVisible(false);
            if (!IS_TOUCH_SUPPORTED) {
                if (d4 >= this.decButton.computeMinWidth(-1.0)) {
                    this.decButton.setVisible(true);
                } else {
                    this.decButton.setVisible(false);
                }
                if (d4 >= this.incButton.computeMinWidth(-1.0)) {
                    this.incButton.setVisible(true);
                } else {
                    this.incButton.setVisible(false);
                }
            }
        }
    }

    public Node getThumb() {
        return this.thumb;
    }

    public Node getTrack() {
        return this.track;
    }

    public Node getIncButton() {
        return this.incButton;
    }

    public Node getDecButton() {
        return this.decButton;
    }

    private static class EndButton
    extends Region {
        private Region arrow;

        private EndButton(String string, String string2) {
            this.getStyleClass().setAll((Object[])new String[]{string});
            this.arrow = new Region();
            this.arrow.getStyleClass().setAll((Object[])new String[]{string2});
            this.getChildren().setAll((Object[])new Node[]{this.arrow});
            this.requestLayout();
        }

        protected void layoutChildren() {
            double d = this.snappedTopInset();
            double d2 = this.snappedLeftInset();
            double d3 = this.snappedBottomInset();
            double d4 = this.snappedRightInset();
            double d5 = this.snapSize(this.arrow.prefWidth(-1.0));
            double d6 = this.snapSize(this.arrow.prefHeight(-1.0));
            double d7 = this.snapPosition((this.getHeight() - (d + d3 + d6)) / 2.0);
            double d8 = this.snapPosition((this.getWidth() - (d2 + d4 + d5)) / 2.0);
            this.arrow.resizeRelocate(d8 + d2, d7 + d, d5, d6);
        }

        protected double computeMinHeight(double d) {
            return this.prefHeight(-1.0);
        }

        protected double computeMinWidth(double d) {
            return this.prefWidth(-1.0);
        }

        protected double computePrefWidth(double d) {
            double d2 = this.snappedLeftInset();
            double d3 = this.snappedRightInset();
            double d4 = this.snapSize(this.arrow.prefWidth(-1.0));
            return d2 + d4 + d3;
        }

        protected double computePrefHeight(double d) {
            double d2 = this.snappedTopInset();
            double d3 = this.snappedBottomInset();
            double d4 = this.snapSize(this.arrow.prefHeight(-1.0));
            return d2 + d4 + d3;
        }
    }
}

