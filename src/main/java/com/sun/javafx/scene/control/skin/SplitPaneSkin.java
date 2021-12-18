/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableList
 *  javafx.geometry.HPos
 *  javafx.geometry.NodeOrientation
 *  javafx.geometry.Orientation
 *  javafx.geometry.VPos
 *  javafx.scene.Cursor
 *  javafx.scene.Node
 *  javafx.scene.control.SplitPane
 *  javafx.scene.control.SplitPane$Divider
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.StackPane
 *  javafx.scene.shape.Rectangle
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class SplitPaneSkin
extends BehaviorSkinBase<SplitPane, BehaviorBase<SplitPane>> {
    private ObservableList<Content> contentRegions;
    private ObservableList<ContentDivider> contentDividers;
    private boolean horizontal = ((SplitPane)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL;
    private double previousSize = -1.0;
    private int lastDividerUpdate = 0;
    private boolean resize = false;
    private boolean checkDividerPos = true;

    public SplitPaneSkin(SplitPane splitPane) {
        super(splitPane, new BehaviorBase<SplitPane>(splitPane, Collections.emptyList()));
        this.contentRegions = FXCollections.observableArrayList();
        this.contentDividers = FXCollections.observableArrayList();
        int n = 0;
        for (Node node : ((SplitPane)this.getSkinnable()).getItems()) {
            this.addContent(n++, node);
        }
        this.initializeContentListener();
        for (Node node : ((SplitPane)this.getSkinnable()).getDividers()) {
            this.addDivider((SplitPane.Divider)node);
        }
        this.registerChangeListener((ObservableValue<?>)splitPane.orientationProperty(), "ORIENTATION");
        this.registerChangeListener((ObservableValue<?>)splitPane.widthProperty(), "WIDTH");
        this.registerChangeListener((ObservableValue<?>)splitPane.heightProperty(), "HEIGHT");
    }

    private void addContent(int n, Node node) {
        Content content = new Content(node);
        this.contentRegions.add(n, (Object)content);
        this.getChildren().add(n, (Object)content);
    }

    private void removeContent(Node node) {
        for (Content content : this.contentRegions) {
            if (!content.getContent().equals((Object)node)) continue;
            this.getChildren().remove((Object)content);
            this.contentRegions.remove((Object)content);
            break;
        }
    }

    private void initializeContentListener() {
        ((SplitPane)this.getSkinnable()).getItems().addListener(change -> {
            while (change.next()) {
                Iterator iterator2;
                int n;
                if (change.wasPermutated() || change.wasUpdated()) {
                    this.getChildren().clear();
                    this.contentRegions.clear();
                    n = 0;
                    for (Node node : change.getList()) {
                        this.addContent(n++, node);
                    }
                    continue;
                }
                for (Iterator iterator2 : change.getRemoved()) {
                    this.removeContent((Node)iterator2);
                }
                n = change.getFrom();
                iterator2 = change.getAddedSubList().iterator();
                while (iterator2.hasNext()) {
                    Node node;
                    node = (Node)iterator2.next();
                    this.addContent(n++, node);
                }
            }
            this.removeAllDividers();
            for (Iterator iterator2 : ((SplitPane)this.getSkinnable()).getDividers()) {
                this.addDivider((SplitPane.Divider)iterator2);
            }
        });
    }

    private void checkDividerPosition(ContentDivider contentDivider, double d, double d2) {
        double d3;
        double d4;
        double d5;
        double d6 = contentDivider.prefWidth(-1.0);
        Content content = this.getLeft(contentDivider);
        Content content2 = this.getRight(contentDivider);
        double d7 = content == null ? 0.0 : (d5 = this.horizontal ? content.minWidth(-1.0) : content.minHeight(-1.0));
        double d8 = content2 == null ? 0.0 : (d4 = this.horizontal ? content2.minWidth(-1.0) : content2.minHeight(-1.0));
        double d9 = content == null ? 0.0 : (content.getContent() != null ? (this.horizontal ? content.getContent().maxWidth(-1.0) : content.getContent().maxHeight(-1.0)) : (d3 = 0.0));
        double d10 = content2 == null ? 0.0 : (content2.getContent() != null ? (this.horizontal ? content2.getContent().maxWidth(-1.0) : content2.getContent().maxHeight(-1.0)) : 0.0);
        double d11 = 0.0;
        double d12 = this.getSize();
        int n = this.contentDividers.indexOf((Object)contentDivider);
        if (n - 1 >= 0 && (d11 = ((ContentDivider)((Object)this.contentDividers.get(n - 1))).getDividerPos()) == -1.0) {
            d11 = this.getAbsoluteDividerPos((ContentDivider)((Object)this.contentDividers.get(n - 1)));
        }
        if (n + 1 < this.contentDividers.size() && (d12 = ((ContentDivider)((Object)this.contentDividers.get(n + 1))).getDividerPos()) == -1.0) {
            d12 = this.getAbsoluteDividerPos((ContentDivider)((Object)this.contentDividers.get(n + 1)));
        }
        this.checkDividerPos = false;
        if (d > d2) {
            double d13;
            double d14 = d11 == 0.0 ? d3 : d11 + d6 + d3;
            double d15 = Math.min(d14, d13 = d12 - d4 - d6);
            if (d >= d15) {
                this.setAbsoluteDividerPos(contentDivider, d15);
            } else {
                double d16 = d12 - d10 - d6;
                if (d <= d16) {
                    this.setAbsoluteDividerPos(contentDivider, d16);
                } else {
                    this.setAbsoluteDividerPos(contentDivider, d);
                }
            }
        } else {
            double d17 = d12 - d10 - d6;
            double d18 = d11 == 0.0 ? d5 : d11 + d5 + d6;
            double d19 = Math.max(d17, d18);
            if (d <= d19) {
                this.setAbsoluteDividerPos(contentDivider, d19);
            } else {
                double d20 = d11 + d3 + d6;
                if (d >= d20) {
                    this.setAbsoluteDividerPos(contentDivider, d20);
                } else {
                    this.setAbsoluteDividerPos(contentDivider, d);
                }
            }
        }
        this.checkDividerPos = true;
    }

    private void addDivider(SplitPane.Divider divider) {
        ContentDivider contentDivider = new ContentDivider(divider);
        contentDivider.setInitialPos(divider.getPosition());
        contentDivider.setDividerPos(-1.0);
        PosPropertyListener posPropertyListener = new PosPropertyListener(contentDivider);
        contentDivider.setPosPropertyListener(posPropertyListener);
        divider.positionProperty().addListener((ChangeListener)posPropertyListener);
        this.initializeDivderEventHandlers(contentDivider);
        this.contentDividers.add((Object)contentDivider);
        this.getChildren().add((Object)contentDivider);
    }

    private void removeAllDividers() {
        ListIterator listIterator = this.contentDividers.listIterator();
        while (listIterator.hasNext()) {
            ContentDivider contentDivider = (ContentDivider)((Object)listIterator.next());
            this.getChildren().remove((Object)contentDivider);
            contentDivider.getDivider().positionProperty().removeListener(contentDivider.getPosPropertyListener());
            listIterator.remove();
        }
        this.lastDividerUpdate = 0;
    }

    private void initializeDivderEventHandlers(ContentDivider contentDivider) {
        contentDivider.addEventHandler(MouseEvent.ANY, mouseEvent -> mouseEvent.consume());
        contentDivider.setOnMousePressed(mouseEvent -> {
            if (this.horizontal) {
                contentDivider.setInitialPos(contentDivider.getDividerPos());
                contentDivider.setPressPos(mouseEvent.getSceneX());
                contentDivider.setPressPos(((SplitPane)this.getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? ((SplitPane)this.getSkinnable()).getWidth() - mouseEvent.getSceneX() : mouseEvent.getSceneX());
            } else {
                contentDivider.setInitialPos(contentDivider.getDividerPos());
                contentDivider.setPressPos(mouseEvent.getSceneY());
            }
            mouseEvent.consume();
        });
        contentDivider.setOnMouseDragged(mouseEvent -> {
            double d = 0.0;
            d = this.horizontal ? (((SplitPane)this.getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? ((SplitPane)this.getSkinnable()).getWidth() - mouseEvent.getSceneX() : mouseEvent.getSceneX()) : mouseEvent.getSceneY();
            this.setAndCheckAbsoluteDividerPos(contentDivider, Math.ceil(contentDivider.getInitialPos() + (d -= contentDivider.getPressPos())));
            mouseEvent.consume();
        });
    }

    private Content getLeft(ContentDivider contentDivider) {
        int n = this.contentDividers.indexOf((Object)contentDivider);
        if (n != -1) {
            return (Content)((Object)this.contentRegions.get(n));
        }
        return null;
    }

    private Content getRight(ContentDivider contentDivider) {
        int n = this.contentDividers.indexOf((Object)contentDivider);
        if (n != -1) {
            return (Content)((Object)this.contentRegions.get(n + 1));
        }
        return null;
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ORIENTATION".equals(string)) {
            this.horizontal = ((SplitPane)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL;
            this.previousSize = -1.0;
            for (ContentDivider contentDivider : this.contentDividers) {
                contentDivider.setGrabberStyle(this.horizontal);
            }
            ((SplitPane)this.getSkinnable()).requestLayout();
        } else if ("WIDTH".equals(string) || "HEIGHT".equals(string)) {
            ((SplitPane)this.getSkinnable()).requestLayout();
        }
    }

    private void setAbsoluteDividerPos(ContentDivider contentDivider, double d) {
        if (((SplitPane)this.getSkinnable()).getWidth() > 0.0 && ((SplitPane)this.getSkinnable()).getHeight() > 0.0 && contentDivider != null) {
            SplitPane.Divider divider = contentDivider.getDivider();
            contentDivider.setDividerPos(d);
            double d2 = this.getSize();
            if (d2 != 0.0) {
                double d3 = d + contentDivider.prefWidth(-1.0) / 2.0;
                divider.setPosition(d3 / d2);
            } else {
                divider.setPosition(0.0);
            }
        }
    }

    private double getAbsoluteDividerPos(ContentDivider contentDivider) {
        if (((SplitPane)this.getSkinnable()).getWidth() > 0.0 && ((SplitPane)this.getSkinnable()).getHeight() > 0.0 && contentDivider != null) {
            SplitPane.Divider divider = contentDivider.getDivider();
            double d = this.posToDividerPos(contentDivider, divider.getPosition());
            contentDivider.setDividerPos(d);
            return d;
        }
        return 0.0;
    }

    private double posToDividerPos(ContentDivider contentDivider, double d) {
        double d2 = this.getSize() * d;
        d2 = d == 1.0 ? (d2 -= contentDivider.prefWidth(-1.0)) : (d2 -= contentDivider.prefWidth(-1.0) / 2.0);
        return Math.round(d2);
    }

    private double totalMinSize() {
        double d = !this.contentDividers.isEmpty() ? (double)this.contentDividers.size() * ((ContentDivider)((Object)this.contentDividers.get(0))).prefWidth(-1.0) : 0.0;
        double d2 = 0.0;
        for (Content content : this.contentRegions) {
            if (this.horizontal) {
                d2 += content.minWidth(-1.0);
                continue;
            }
            d2 += content.minHeight(-1.0);
        }
        return d2 + d;
    }

    private double getSize() {
        SplitPane splitPane = (SplitPane)this.getSkinnable();
        double d = this.totalMinSize();
        if (this.horizontal) {
            if (splitPane.getWidth() > d) {
                d = splitPane.getWidth() - this.snappedLeftInset() - this.snappedRightInset();
            }
        } else if (splitPane.getHeight() > d) {
            d = splitPane.getHeight() - this.snappedTopInset() - this.snappedBottomInset();
        }
        return d;
    }

    private double distributeTo(List<Content> list, double d) {
        if (list.isEmpty()) {
            return d;
        }
        d = this.snapSize(d);
        int n = (int)d / list.size();
        while (d > 0.0 && !list.isEmpty()) {
            Iterator<Content> iterator = list.iterator();
            while (iterator.hasNext()) {
                double d2;
                Content content = iterator.next();
                double d3 = Math.min(this.horizontal ? content.maxWidth(-1.0) : content.maxHeight(-1.0), Double.MAX_VALUE);
                double d4 = d2 = this.horizontal ? content.minWidth(-1.0) : content.minHeight(-1.0);
                if (content.getArea() >= d3) {
                    content.setAvailable(content.getArea() - d2);
                    iterator.remove();
                    continue;
                }
                if ((double)n >= d3 - content.getArea()) {
                    d -= d3 - content.getArea();
                    content.setArea(d3);
                    content.setAvailable(d3 - d2);
                    iterator.remove();
                } else {
                    content.setArea(content.getArea() + (double)n);
                    content.setAvailable(content.getArea() - d2);
                    d -= (double)n;
                }
                if ((int)d != 0) continue;
                return d;
            }
            if (list.isEmpty()) {
                return d;
            }
            n = (int)d / list.size();
            int n2 = (int)d % list.size();
            if (n != 0 || n2 == 0) continue;
            n = n2;
            n2 = 0;
        }
        return d;
    }

    private double distributeFrom(double d, List<Content> list) {
        if (list.isEmpty()) {
            return d;
        }
        d = this.snapSize(d);
        int n = (int)d / list.size();
        while (d > 0.0 && !list.isEmpty()) {
            Iterator<Content> iterator = list.iterator();
            while (iterator.hasNext()) {
                Content content = iterator.next();
                if ((double)n >= content.getAvailable()) {
                    content.setArea(content.getArea() - content.getAvailable());
                    d -= content.getAvailable();
                    content.setAvailable(0.0);
                    iterator.remove();
                } else {
                    content.setArea(content.getArea() - (double)n);
                    content.setAvailable(content.getAvailable() - (double)n);
                    d -= (double)n;
                }
                if ((int)d != 0) continue;
                return d;
            }
            if (list.isEmpty()) {
                return d;
            }
            n = (int)d / list.size();
            int n2 = (int)d % list.size();
            if (n != 0 || n2 == 0) continue;
            n = n2;
            n2 = 0;
        }
        return d;
    }

    private void setupContentAndDividerForLayout() {
        double d = this.contentDividers.isEmpty() ? 0.0 : ((ContentDivider)((Object)this.contentDividers.get(0))).prefWidth(-1.0);
        double d2 = 0.0;
        double d3 = 0.0;
        for (StackPane stackPane : this.contentRegions) {
            if (this.resize && !stackPane.isResizableWithParent()) {
                stackPane.setArea(stackPane.getResizableWithParentArea());
            }
            stackPane.setX(d2);
            stackPane.setY(d3);
            if (this.horizontal) {
                d2 += stackPane.getArea() + d;
                continue;
            }
            d3 += stackPane.getArea() + d;
        }
        d2 = 0.0;
        d3 = 0.0;
        this.checkDividerPos = false;
        for (int i = 0; i < this.contentDividers.size(); ++i) {
            StackPane stackPane;
            stackPane = (ContentDivider)((Object)this.contentDividers.get(i));
            if (this.horizontal) {
                d2 += this.getLeft((ContentDivider)stackPane).getArea() + (i == 0 ? 0.0 : d);
            } else {
                d3 += this.getLeft((ContentDivider)stackPane).getArea() + (i == 0 ? 0.0 : d);
            }
            stackPane.setX(d2);
            stackPane.setY(d3);
            this.setAbsoluteDividerPos((ContentDivider)stackPane, this.horizontal ? stackPane.getX() : stackPane.getY());
            ((ContentDivider)stackPane).posExplicit = false;
        }
        this.checkDividerPos = true;
    }

    private void layoutDividersAndContent(double d, double d2) {
        double d3 = this.snappedLeftInset();
        double d4 = this.snappedTopInset();
        double d5 = this.contentDividers.isEmpty() ? 0.0 : ((ContentDivider)((Object)this.contentDividers.get(0))).prefWidth(-1.0);
        for (StackPane stackPane : this.contentRegions) {
            if (this.horizontal) {
                stackPane.setClipSize(stackPane.getArea(), d2);
                this.layoutInArea((Node)stackPane, stackPane.getX() + d3, stackPane.getY() + d4, stackPane.getArea(), d2, 0.0, HPos.CENTER, VPos.CENTER);
                continue;
            }
            stackPane.setClipSize(d, stackPane.getArea());
            this.layoutInArea((Node)stackPane, stackPane.getX() + d3, stackPane.getY() + d4, d, stackPane.getArea(), 0.0, HPos.CENTER, VPos.CENTER);
        }
        for (StackPane stackPane : this.contentDividers) {
            if (this.horizontal) {
                stackPane.resize(d5, d2);
                this.positionInArea((Node)stackPane, stackPane.getX() + d3, stackPane.getY() + d4, d5, d2, 0.0, HPos.CENTER, VPos.CENTER);
                continue;
            }
            stackPane.resize(d, d5);
            this.positionInArea((Node)stackPane, stackPane.getX() + d3, stackPane.getY() + d4, d, d5, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected void layoutChildren(double var1_1, double var3_2, double var5_3, double var7_4) {
        block57: {
            block55: {
                block56: {
                    block54: {
                        var9_5 = (SplitPane)this.getSkinnable();
                        var10_6 = var9_5.getWidth();
                        var12_7 = var9_5.getHeight();
                        if (var9_5.isVisible() == false) return;
                        if (this.horizontal) {
                            if (var10_6 == 0.0) {
                                return;
                            }
                        } else if (var12_7 == 0.0) return;
                        if (this.contentRegions.isEmpty()) {
                            return;
                        }
                        v0 = var14_8 = this.contentDividers.isEmpty() != false ? 0.0 : ((ContentDivider)this.contentDividers.get(0)).prefWidth(-1.0);
                        if (this.contentDividers.size() <= 0 || this.previousSize == -1.0 || this.previousSize == (this.horizontal != false ? var10_6 : var12_7)) break block54;
                        var16_9 = new ArrayList<Content>();
                        for (Content var18_13 : this.contentRegions) {
                            if (!var18_13.isResizableWithParent()) continue;
                            var16_9.add(var18_13);
                        }
                        var17_12 = (this.horizontal != false ? var9_5.getWidth() : var9_5.getHeight()) - this.previousSize;
                        var19_16 = var17_12 > 0.0;
                        if ((var17_12 = Math.abs(var17_12)) == 0.0 || var16_9.isEmpty()) break block55;
                        var20_18 = (int)var17_12 / var16_9.size();
                        var21_20 = (int)var17_12 % var16_9.size();
                        var22_23 = 0;
                        if (var20_18 == 0) {
                            var20_18 = var21_20;
                            var22_23 = var21_20;
                            var21_20 = 0;
                        } else {
                            var22_23 = var20_18 * var16_9.size();
                        }
                        break block56;
                    }
                    this.previousSize = this.horizontal != false ? var10_6 : var12_7;
                    break block57;
                }
                while (var22_23 > 0 && !var16_9.isEmpty()) {
                    if (var19_16) {
                        ++this.lastDividerUpdate;
                    } else {
                        --this.lastDividerUpdate;
                        if (this.lastDividerUpdate < 0) {
                            this.lastDividerUpdate = this.contentRegions.size() - 1;
                        }
                    }
                    if (!(var24_29 = (Content)this.contentRegions.get(var23_26 = this.lastDividerUpdate % this.contentRegions.size())).isResizableWithParent() || !var16_9.contains((Object)var24_29)) continue;
                    var25_31 = var24_29.getArea();
                    if (!var19_16) ** GOTO lbl54
                    v1 = var27_34 = this.horizontal != false ? var24_29.maxWidth(-1.0) : var24_29.maxHeight(-1.0);
                    if (var25_31 + (double)var20_18 <= var27_34) {
                        var25_31 += (double)var20_18;
                    } else {
                        var16_9.remove((Object)var24_29);
                        continue;
lbl54:
                        // 1 sources

                        v2 = var27_34 = this.horizontal != false ? var24_29.minWidth(-1.0) : var24_29.minHeight(-1.0);
                        if (var25_31 - (double)var20_18 >= var27_34) {
                            var25_31 -= (double)var20_18;
                        } else {
                            var16_9.remove((Object)var24_29);
                            continue;
                        }
                    }
                    var24_29.setArea(var25_31);
                    if ((var22_23 -= var20_18) == 0 && var21_20 != 0) {
                        var20_18 = var21_20;
                        var22_23 = var21_20;
                        var21_20 = 0;
                        continue;
                    }
                    if (var22_23 != 0) continue;
                }
                for (Content var24_29 : this.contentRegions) {
                    var24_29.setResizableWithParentArea(var24_29.getArea());
                    var24_29.setAvailable(0.0);
                }
                this.resize = true;
            }
            this.previousSize = this.horizontal != false ? var10_6 : var12_7;
        }
        var16_10 = this.totalMinSize();
        if (!(var16_10 > (this.horizontal != false ? var5_3 : var7_4))) {
        } else {
            var18_14 = 0.0;
            var20_18 = 0;
            while (true) {
                if (var20_18 >= this.contentRegions.size()) {
                    this.setupContentAndDividerForLayout();
                    this.layoutDividersAndContent(var5_3, var7_4);
                    this.resize = false;
                    return;
                }
                var21_21 = (Content)this.contentRegions.get(var20_18);
                var22_24 = this.horizontal != false ? var21_21.minWidth(-1.0) : var21_21.minHeight(-1.0);
                var18_14 = var22_24 / var16_10;
                var21_21.setArea(this.snapSpace(var18_14 * (this.horizontal != false ? var5_3 : var7_4)));
                var21_21.setAvailable(0.0);
                ++var20_18;
            }
        }
        for (var18_15 = 0; var18_15 < 10; ++var18_15) {
            var19_17 = null;
            var20_19 = null;
            for (var21_20 = 0; var21_20 < this.contentRegions.size(); ++var21_20) {
                var22_25 = 0.0;
                if (var21_20 < this.contentDividers.size()) {
                    var20_19 = (ContentDivider)this.contentDividers.get(var21_20);
                    if (ContentDivider.access$100(var20_19)) {
                        this.checkDividerPosition(var20_19, this.posToDividerPos(var20_19, ContentDivider.access$200(var20_19).getPosition()), var20_19.getDividerPos());
                    }
                    if (var21_20 == 0) {
                        var22_25 = this.getAbsoluteDividerPos(var20_19);
                    } else {
                        var24_30 = this.getAbsoluteDividerPos(var19_17) + var14_8;
                        if (this.getAbsoluteDividerPos(var20_19) <= this.getAbsoluteDividerPos(var19_17)) {
                            this.setAndCheckAbsoluteDividerPos(var20_19, var24_30);
                        }
                        var22_25 = this.getAbsoluteDividerPos(var20_19) - var24_30;
                    }
                } else if (var21_20 == this.contentDividers.size()) {
                    var22_25 = (this.horizontal != false ? var5_3 : var7_4) - (var19_17 != null ? this.getAbsoluteDividerPos(var19_17) + var14_8 : 0.0);
                }
                if (!this.resize || ContentDivider.access$100(var20_19)) {
                    ((Content)this.contentRegions.get(var21_20)).setArea(var22_25);
                }
                var19_17 = var20_19;
            }
            var21_22 = 0.0;
            var23_28 = 0.0;
            for (Object var26_37 : this.contentRegions) {
                var27_35 = 0.0;
                var29_39 = 0.0;
                if (var26_37 != null) {
                    var27_35 = this.horizontal != false ? var26_37.maxWidth(-1.0) : var26_37.maxHeight(-1.0);
                    v3 = var29_39 = this.horizontal != false ? var26_37.minWidth(-1.0) : var26_37.minHeight(-1.0);
                }
                if (var26_37.getArea() >= var27_35) {
                    var23_28 += var26_37.getArea() - var27_35;
                    var26_37.setArea(var27_35);
                }
                var26_37.setAvailable(var26_37.getArea() - var29_39);
                if (!(var26_37.getAvailable() < 0.0)) continue;
                var21_22 += var26_37.getAvailable();
            }
            var21_22 = Math.abs(var21_22);
            var25_33 = new ArrayList<E>();
            var26_37 = new ArrayList<E>();
            var27_36 = new ArrayList<Object>();
            var28_38 = 0.0;
            for (Content var31_43 : this.contentRegions) {
                if (var31_43 /* !! */ .getAvailable() >= 0.0) {
                    var28_38 += var31_43 /* !! */ .getAvailable();
                    var25_33.add(var31_43 /* !! */ );
                }
                if (this.resize && !var31_43 /* !! */ .isResizableWithParent()) {
                    if (var31_43 /* !! */ .getArea() >= var31_43 /* !! */ .getResizableWithParentArea()) {
                        var23_28 += var31_43 /* !! */ .getArea() - var31_43 /* !! */ .getResizableWithParentArea();
                    } else {
                        var21_22 += var31_43 /* !! */ .getResizableWithParentArea() - var31_43 /* !! */ .getArea();
                    }
                    var31_43 /* !! */ .setAvailable(0.0);
                }
                if (this.resize) {
                    if (var31_43 /* !! */ .isResizableWithParent()) {
                        var26_37.add(var31_43 /* !! */ );
                    }
                } else {
                    var26_37.add(var31_43 /* !! */ );
                }
                if (!(var31_43 /* !! */ .getAvailable() < 0.0)) continue;
                var27_36.add((Object)var31_43 /* !! */ );
            }
            if (var23_28 > 0.0) {
                var23_28 = this.distributeTo((List<Content>)var26_37, var23_28);
                var21_22 = 0.0;
                var27_36.clear();
                var28_38 = 0.0;
                var25_33.clear();
                for (Content var31_43 : this.contentRegions) {
                    if (var31_43 /* !! */ .getAvailable() < 0.0) {
                        var21_22 += var31_43 /* !! */ .getAvailable();
                        var27_36.add((Object)var31_43 /* !! */ );
                        continue;
                    }
                    var28_38 += var31_43 /* !! */ .getAvailable();
                    var25_33.add(var31_43 /* !! */ );
                }
                var21_22 = Math.abs(var21_22);
            }
            if (var28_38 >= var21_22) {
                for (Content var31_43 : var27_36) {
                    var32_44 = this.horizontal != false ? var31_43 /* !! */ .minWidth(-1.0) : var31_43 /* !! */ .minHeight(-1.0);
                    var31_43 /* !! */ .setArea(var32_44);
                    var31_43 /* !! */ .setAvailable(0.0);
                }
                if (var21_22 > 0.0 && !var27_36.isEmpty()) {
                    this.distributeFrom(var21_22, (List<Content>)var25_33);
                }
                if (this.resize) {
                    var30_42 = 0.0;
                    for (Content var33_47 : this.contentRegions) {
                        if (var33_47.isResizableWithParent()) {
                            var30_42 += var33_47.getArea();
                            continue;
                        }
                        var30_42 += var33_47.getResizableWithParentArea();
                    }
                    var30_42 += var14_8 * (double)this.contentDividers.size();
                    v4 = this.horizontal != false ? var5_3 : var7_4;
                    if (var30_42 < v4) {
                        this.distributeTo((List<Content>)var26_37, var23_28 += (this.horizontal != false ? var5_3 : var7_4) - var30_42);
                    } else {
                        this.distributeFrom(var21_22 += var30_42 - (this.horizontal != false ? var5_3 : var7_4), (List<Content>)var26_37);
                    }
                }
            }
            this.setupContentAndDividerForLayout();
            var30_40 = true;
            var31_43 /* !! */  = this.contentRegions.iterator();
            while (var31_43 /* !! */ .hasNext()) {
                var32_46 = (Content)var31_43 /* !! */ .next();
                var33_48 = this.horizontal != false ? var32_46.maxWidth(-1.0) : var32_46.maxHeight(-1.0);
                v5 = var35_49 = this.horizontal != false ? var32_46.minWidth(-1.0) : var32_46.minHeight(-1.0);
                if (!(var32_46.getArea() < var35_49) && !(var32_46.getArea() > var33_48)) continue;
                var30_40 = false;
                break;
            }
            if (var30_40) break;
        }
        this.layoutDividersAndContent(var5_3, var7_4);
        this.resize = false;
    }

    private void setAndCheckAbsoluteDividerPos(ContentDivider contentDivider, double d) {
        double d2 = contentDivider.getDividerPos();
        this.setAbsoluteDividerPos(contentDivider, d);
        this.checkDividerPosition(contentDivider, d, d2);
    }

    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = 0.0;
        double d7 = 0.0;
        for (StackPane stackPane : this.contentRegions) {
            d6 += stackPane.minWidth(-1.0);
            d7 = Math.max(d7, stackPane.minWidth(-1.0));
        }
        for (StackPane stackPane : this.contentDividers) {
            d6 += stackPane.prefWidth(-1.0);
        }
        if (this.horizontal) {
            return d6 + d5 + d3;
        }
        return d7 + d5 + d3;
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        double d6 = 0.0;
        double d7 = 0.0;
        for (StackPane stackPane : this.contentRegions) {
            d6 += stackPane.minHeight(-1.0);
            d7 = Math.max(d7, stackPane.minHeight(-1.0));
        }
        for (StackPane stackPane : this.contentDividers) {
            d6 += stackPane.prefWidth(-1.0);
        }
        if (this.horizontal) {
            return d7 + d2 + d4;
        }
        return d6 + d2 + d4;
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = 0.0;
        double d7 = 0.0;
        for (StackPane stackPane : this.contentRegions) {
            d6 += stackPane.prefWidth(-1.0);
            d7 = Math.max(d7, stackPane.prefWidth(-1.0));
        }
        for (StackPane stackPane : this.contentDividers) {
            d6 += stackPane.prefWidth(-1.0);
        }
        if (this.horizontal) {
            return d6 + d5 + d3;
        }
        return d7 + d5 + d3;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        double d6 = 0.0;
        double d7 = 0.0;
        for (StackPane stackPane : this.contentRegions) {
            d6 += stackPane.prefHeight(-1.0);
            d7 = Math.max(d7, stackPane.prefHeight(-1.0));
        }
        for (StackPane stackPane : this.contentDividers) {
            d6 += stackPane.prefWidth(-1.0);
        }
        if (this.horizontal) {
            return d7 + d2 + d4;
        }
        return d6 + d2 + d4;
    }

    static class Content
    extends StackPane {
        private Node content;
        private Rectangle clipRect = new Rectangle();
        private double x;
        private double y;
        private double area;
        private double resizableWithParentArea;
        private double available;

        public Content(Node node) {
            this.setClip((Node)this.clipRect);
            this.content = node;
            if (node != null) {
                this.getChildren().add((Object)node);
            }
            this.x = 0.0;
            this.y = 0.0;
        }

        public Node getContent() {
            return this.content;
        }

        public double getX() {
            return this.x;
        }

        public void setX(double d) {
            this.x = d;
        }

        public double getY() {
            return this.y;
        }

        public void setY(double d) {
            this.y = d;
        }

        public double getArea() {
            return this.area;
        }

        public void setArea(double d) {
            this.area = d;
        }

        public double getAvailable() {
            return this.available;
        }

        public void setAvailable(double d) {
            this.available = d;
        }

        public boolean isResizableWithParent() {
            return SplitPane.isResizableWithParent((Node)this.content);
        }

        public double getResizableWithParentArea() {
            return this.resizableWithParentArea;
        }

        public void setResizableWithParentArea(double d) {
            this.resizableWithParentArea = !this.isResizableWithParent() ? d : 0.0;
        }

        protected void setClipSize(double d, double d2) {
            this.clipRect.setWidth(d);
            this.clipRect.setHeight(d2);
        }

        protected double computeMaxWidth(double d) {
            return this.snapSize(this.content.maxWidth(d));
        }

        protected double computeMaxHeight(double d) {
            return this.snapSize(this.content.maxHeight(d));
        }
    }

    class ContentDivider
    extends StackPane {
        private double initialPos;
        private double dividerPos;
        private double pressPos;
        private SplitPane.Divider d;
        private StackPane grabber;
        private double x;
        private double y;
        private boolean posExplicit;
        private ChangeListener<Number> listener;

        public ContentDivider(SplitPane.Divider divider) {
            this.getStyleClass().setAll((Object[])new String[]{"split-pane-divider"});
            this.d = divider;
            this.initialPos = 0.0;
            this.dividerPos = 0.0;
            this.pressPos = 0.0;
            this.grabber = new StackPane(){

                protected double computeMinWidth(double d) {
                    return 0.0;
                }

                protected double computeMinHeight(double d) {
                    return 0.0;
                }

                protected double computePrefWidth(double d) {
                    return this.snappedLeftInset() + this.snappedRightInset();
                }

                protected double computePrefHeight(double d) {
                    return this.snappedTopInset() + this.snappedBottomInset();
                }

                protected double computeMaxWidth(double d) {
                    return this.computePrefWidth(-1.0);
                }

                protected double computeMaxHeight(double d) {
                    return this.computePrefHeight(-1.0);
                }
            };
            this.setGrabberStyle(SplitPaneSkin.this.horizontal);
            this.getChildren().add((Object)this.grabber);
        }

        public SplitPane.Divider getDivider() {
            return this.d;
        }

        public final void setGrabberStyle(boolean bl) {
            this.grabber.getStyleClass().clear();
            this.grabber.getStyleClass().setAll((Object[])new String[]{"vertical-grabber"});
            this.setCursor(Cursor.V_RESIZE);
            if (bl) {
                this.grabber.getStyleClass().setAll((Object[])new String[]{"horizontal-grabber"});
                this.setCursor(Cursor.H_RESIZE);
            }
        }

        public double getInitialPos() {
            return this.initialPos;
        }

        public void setInitialPos(double d) {
            this.initialPos = d;
        }

        public double getDividerPos() {
            return this.dividerPos;
        }

        public void setDividerPos(double d) {
            this.dividerPos = d;
        }

        public double getPressPos() {
            return this.pressPos;
        }

        public void setPressPos(double d) {
            this.pressPos = d;
        }

        public double getX() {
            return this.x;
        }

        public void setX(double d) {
            this.x = d;
        }

        public double getY() {
            return this.y;
        }

        public void setY(double d) {
            this.y = d;
        }

        public ChangeListener<Number> getPosPropertyListener() {
            return this.listener;
        }

        public void setPosPropertyListener(ChangeListener<Number> changeListener) {
            this.listener = changeListener;
        }

        protected double computeMinWidth(double d) {
            return this.computePrefWidth(d);
        }

        protected double computeMinHeight(double d) {
            return this.computePrefHeight(d);
        }

        protected double computePrefWidth(double d) {
            return this.snappedLeftInset() + this.snappedRightInset();
        }

        protected double computePrefHeight(double d) {
            return this.snappedTopInset() + this.snappedBottomInset();
        }

        protected double computeMaxWidth(double d) {
            return this.computePrefWidth(d);
        }

        protected double computeMaxHeight(double d) {
            return this.computePrefHeight(d);
        }

        protected void layoutChildren() {
            double d = this.grabber.prefWidth(-1.0);
            double d2 = this.grabber.prefHeight(-1.0);
            double d3 = (this.getWidth() - d) / 2.0;
            double d4 = (this.getHeight() - d2) / 2.0;
            this.grabber.resize(d, d2);
            this.positionInArea((Node)this.grabber, d3, d4, d, d2, 0.0, HPos.CENTER, VPos.CENTER);
        }

        static /* synthetic */ boolean access$100(ContentDivider contentDivider) {
            return contentDivider.posExplicit;
        }

        static /* synthetic */ SplitPane.Divider access$200(ContentDivider contentDivider) {
            return contentDivider.d;
        }
    }

    class PosPropertyListener
    implements ChangeListener<Number> {
        ContentDivider divider;

        public PosPropertyListener(ContentDivider contentDivider) {
            this.divider = contentDivider;
        }

        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            if (SplitPaneSkin.this.checkDividerPos) {
                this.divider.posExplicit = true;
            }
            ((SplitPane)SplitPaneSkin.this.getSkinnable()).requestLayout();
        }
    }
}

