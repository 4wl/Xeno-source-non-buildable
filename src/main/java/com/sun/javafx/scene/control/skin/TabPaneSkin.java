/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation$Status
 *  javafx.animation.Interpolator
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 *  javafx.animation.Timeline
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.beans.WeakInvalidationListener
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.SimpleDoubleProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.beans.value.WritableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.collections.WeakListChangeListener
 *  javafx.css.CssMetaData
 *  javafx.css.PseudoClass
 *  javafx.css.Styleable
 *  javafx.css.StyleableObjectProperty
 *  javafx.css.StyleableProperty
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.geometry.HPos
 *  javafx.geometry.Pos
 *  javafx.geometry.Side
 *  javafx.geometry.VPos
 *  javafx.scene.AccessibleAction
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.AccessibleRole
 *  javafx.scene.Node
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.Label
 *  javafx.scene.control.MenuItem
 *  javafx.scene.control.RadioMenuItem
 *  javafx.scene.control.SkinBase
 *  javafx.scene.control.Tab
 *  javafx.scene.control.TabPane
 *  javafx.scene.control.TabPane$TabClosingPolicy
 *  javafx.scene.control.ToggleGroup
 *  javafx.scene.control.Tooltip
 *  javafx.scene.effect.DropShadow
 *  javafx.scene.image.ImageView
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.input.ScrollEvent
 *  javafx.scene.input.SwipeEvent
 *  javafx.scene.layout.Pane
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 *  javafx.scene.shape.Rectangle
 *  javafx.scene.transform.Rotate
 *  javafx.util.Callback
 *  javafx.util.Duration
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalEngine;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Callback;
import javafx.util.Duration;

public class TabPaneSkin
extends BehaviorSkinBase<TabPane, TabPaneBehavior> {
    private ObjectProperty<TabAnimation> openTabAnimation = new StyleableObjectProperty<TabAnimation>(TabAnimation.GROW){

        public CssMetaData<TabPane, TabAnimation> getCssMetaData() {
            return StyleableProperties.OPEN_TAB_ANIMATION;
        }

        public Object getBean() {
            return TabPaneSkin.this;
        }

        public String getName() {
            return "openTabAnimation";
        }
    };
    private ObjectProperty<TabAnimation> closeTabAnimation = new StyleableObjectProperty<TabAnimation>(TabAnimation.GROW){

        public CssMetaData<TabPane, TabAnimation> getCssMetaData() {
            return StyleableProperties.CLOSE_TAB_ANIMATION;
        }

        public Object getBean() {
            return TabPaneSkin.this;
        }

        public String getName() {
            return "closeTabAnimation";
        }
    };
    private static final double ANIMATION_SPEED = 150.0;
    private static final int SPACER = 10;
    private TabHeaderArea tabHeaderArea;
    private ObservableList<TabContentRegion> tabContentRegions;
    private Rectangle clipRect;
    private Rectangle tabHeaderAreaClipRect;
    private Tab selectedTab;
    private boolean isSelectingTab;
    private double maxw = 0.0;
    private double maxh = 0.0;
    static int CLOSE_BTN_SIZE = 16;
    private static final PseudoClass SELECTED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"selected");
    private static final PseudoClass TOP_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"top");
    private static final PseudoClass BOTTOM_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"bottom");
    private static final PseudoClass LEFT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"left");
    private static final PseudoClass RIGHT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"right");
    private static final PseudoClass DISABLED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"disabled");

    private static int getRotation(Side side) {
        switch (side) {
            case TOP: {
                return 0;
            }
            case BOTTOM: {
                return 180;
            }
            case LEFT: {
                return -90;
            }
            case RIGHT: {
                return 90;
            }
        }
        return 0;
    }

    private static Node clone(Node node) {
        if (node == null) {
            return null;
        }
        if (node instanceof ImageView) {
            ImageView imageView = (ImageView)node;
            ImageView imageView2 = new ImageView();
            imageView2.setImage(imageView.getImage());
            return imageView2;
        }
        if (node instanceof Label) {
            Label label = (Label)node;
            Label label2 = new Label(label.getText(), label.getGraphic());
            return label2;
        }
        return null;
    }

    public TabPaneSkin(TabPane tabPane) {
        super(tabPane, new TabPaneBehavior(tabPane));
        this.clipRect = new Rectangle(tabPane.getWidth(), tabPane.getHeight());
        ((TabPane)this.getSkinnable()).setClip((Node)this.clipRect);
        this.tabContentRegions = FXCollections.observableArrayList();
        for (Tab tab : ((TabPane)this.getSkinnable()).getTabs()) {
            this.addTabContent(tab);
        }
        this.tabHeaderAreaClipRect = new Rectangle();
        this.tabHeaderArea = new TabHeaderArea();
        this.tabHeaderArea.setClip((Node)this.tabHeaderAreaClipRect);
        this.getChildren().add((Object)this.tabHeaderArea);
        if (((TabPane)this.getSkinnable()).getTabs().size() == 0) {
            this.tabHeaderArea.setVisible(false);
        }
        this.initializeTabListener();
        this.registerChangeListener((ObservableValue<?>)tabPane.getSelectionModel().selectedItemProperty(), "SELECTED_TAB");
        this.registerChangeListener((ObservableValue<?>)tabPane.sideProperty(), "SIDE");
        this.registerChangeListener((ObservableValue<?>)tabPane.widthProperty(), "WIDTH");
        this.registerChangeListener((ObservableValue<?>)tabPane.heightProperty(), "HEIGHT");
        this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
        if (this.selectedTab == null && ((TabPane)this.getSkinnable()).getSelectionModel().getSelectedIndex() != -1) {
            ((TabPane)this.getSkinnable()).getSelectionModel().select(((TabPane)this.getSkinnable()).getSelectionModel().getSelectedIndex());
            this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
        }
        if (this.selectedTab == null) {
            ((TabPane)this.getSkinnable()).getSelectionModel().selectFirst();
        }
        this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
        this.isSelectingTab = false;
        this.initializeSwipeHandlers();
    }

    public StackPane getSelectedTabContentRegion() {
        for (TabContentRegion tabContentRegion : this.tabContentRegions) {
            if (!tabContentRegion.getTab().equals((Object)this.selectedTab)) continue;
            return tabContentRegion;
        }
        return null;
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("SELECTED_TAB".equals(string)) {
            this.isSelectingTab = true;
            this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
            ((TabPane)this.getSkinnable()).requestLayout();
        } else if ("SIDE".equals(string)) {
            this.updateTabPosition();
        } else if ("WIDTH".equals(string)) {
            this.clipRect.setWidth(((TabPane)this.getSkinnable()).getWidth());
        } else if ("HEIGHT".equals(string)) {
            this.clipRect.setHeight(((TabPane)this.getSkinnable()).getHeight());
        }
    }

    private void removeTabs(List<? extends Tab> list) {
        for (Tab tab : list) {
            MenuItem menuItem;
            Iterator iterator;
            this.stopCurrentAnimation(tab);
            TabHeaderSkin tabHeaderSkin = this.tabHeaderArea.getTabHeaderSkin(tab);
            if (tabHeaderSkin == null) continue;
            tabHeaderSkin.isClosing = true;
            tabHeaderSkin.removeListeners(tab);
            this.removeTabContent(tab);
            ContextMenu contextMenu = this.tabHeaderArea.controlButtons.popup;
            TabMenuItem tabMenuItem = null;
            if (contextMenu != null) {
                iterator = contextMenu.getItems().iterator();
                while (iterator.hasNext() && tab != (tabMenuItem = (TabMenuItem)(menuItem = (MenuItem)iterator.next())).getTab()) {
                    tabMenuItem = null;
                }
            }
            if (tabMenuItem != null) {
                tabMenuItem.dispose();
                contextMenu.getItems().remove((Object)tabMenuItem);
            }
            iterator = actionEvent -> {
                tabHeaderSkin.animationState = TabAnimationState.NONE;
                this.tabHeaderArea.removeTab(tab);
                this.tabHeaderArea.requestLayout();
                if (((TabPane)this.getSkinnable()).getTabs().isEmpty()) {
                    this.tabHeaderArea.setVisible(false);
                }
            };
            if (this.closeTabAnimation.get() == TabAnimation.GROW) {
                tabHeaderSkin.animationState = TabAnimationState.HIDING;
                menuItem = tabHeaderSkin.currentAnimation = this.createTimeline(tabHeaderSkin, Duration.millis((double)150.0), 0.0, (EventHandler<ActionEvent>)iterator);
                menuItem.play();
                continue;
            }
            iterator.handle(null);
        }
    }

    private void stopCurrentAnimation(Tab tab) {
        Timeline timeline;
        TabHeaderSkin tabHeaderSkin = this.tabHeaderArea.getTabHeaderSkin(tab);
        if (tabHeaderSkin != null && (timeline = tabHeaderSkin.currentAnimation) != null && timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.getOnFinished().handle(null);
            timeline.stop();
            tabHeaderSkin.currentAnimation = null;
        }
    }

    private void addTabs(List<? extends Tab> list, int n) {
        int n2 = 0;
        ArrayList arrayList = new ArrayList(this.tabHeaderArea.headersRegion.getChildren());
        for (Node node : arrayList) {
            TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
            if (tabHeaderSkin.animationState != TabAnimationState.HIDING) continue;
            this.stopCurrentAnimation(tabHeaderSkin.tab);
        }
        for (Tab tab : list) {
            this.stopCurrentAnimation(tab);
            if (!this.tabHeaderArea.isVisible()) {
                this.tabHeaderArea.setVisible(true);
            }
            int n3 = n + n2++;
            this.tabHeaderArea.addTab(tab, n3);
            this.addTabContent(tab);
            TabHeaderSkin tabHeaderSkin = this.tabHeaderArea.getTabHeaderSkin(tab);
            if (tabHeaderSkin == null) continue;
            if (this.openTabAnimation.get() == TabAnimation.GROW) {
                tabHeaderSkin.animationState = TabAnimationState.SHOWING;
                tabHeaderSkin.animationTransition.setValue((Number)0.0);
                tabHeaderSkin.setVisible(true);
                tabHeaderSkin.currentAnimation = this.createTimeline(tabHeaderSkin, Duration.millis((double)150.0), 1.0, (EventHandler<ActionEvent>)((EventHandler)actionEvent -> {
                    tabHeaderSkin.animationState = TabAnimationState.NONE;
                    tabHeaderSkin.setVisible(true);
                    tabHeaderSkin.inner.requestLayout();
                }));
                tabHeaderSkin.currentAnimation.play();
                continue;
            }
            tabHeaderSkin.setVisible(true);
            tabHeaderSkin.inner.requestLayout();
        }
    }

    private void initializeTabListener() {
        ((TabPane)this.getSkinnable()).getTabs().addListener(change -> {
            Object object;
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int n = -1;
            while (change.next()) {
                if (change.wasPermutated()) {
                    TabPane tabPane = (TabPane)this.getSkinnable();
                    Object object2 = tabPane.getTabs();
                    int n2 = change.getTo() - change.getFrom();
                    object = (Tab)tabPane.getSelectionModel().getSelectedItem();
                    ArrayList arrayList3 = new ArrayList(n2);
                    ((TabPane)this.getSkinnable()).getSelectionModel().clearSelection();
                    TabAnimation tabAnimation = (TabAnimation)((Object)((Object)this.openTabAnimation.get()));
                    TabAnimation tabAnimation2 = (TabAnimation)((Object)((Object)this.closeTabAnimation.get()));
                    this.openTabAnimation.set((Object)TabAnimation.NONE);
                    this.closeTabAnimation.set((Object)TabAnimation.NONE);
                    for (int i = change.getFrom(); i < change.getTo(); ++i) {
                        arrayList3.add(object2.get(i));
                    }
                    this.removeTabs(arrayList3);
                    this.addTabs(arrayList3, change.getFrom());
                    this.openTabAnimation.set((Object)tabAnimation);
                    this.closeTabAnimation.set((Object)tabAnimation2);
                    ((TabPane)this.getSkinnable()).getSelectionModel().select(object);
                }
                if (change.wasRemoved()) {
                    arrayList.addAll(change.getRemoved());
                }
                if (!change.wasAdded()) continue;
                arrayList2.addAll(change.getAddedSubList());
                n = change.getFrom();
            }
            arrayList.removeAll(arrayList2);
            this.removeTabs(arrayList);
            if (!arrayList2.isEmpty()) {
                for (Object object2 : this.tabContentRegions) {
                    Tab tab = object2.getTab();
                    object = this.tabHeaderArea.getTabHeaderSkin(tab);
                    if (((TabHeaderSkin)((Object)((Object)object))).isClosing || !arrayList2.contains((Object)object2.getTab())) continue;
                    arrayList2.remove((Object)object2.getTab());
                }
                this.addTabs(arrayList2, n == -1 ? this.tabContentRegions.size() : n);
            }
            ((TabPane)this.getSkinnable()).requestLayout();
        });
    }

    private void addTabContent(Tab tab) {
        TabContentRegion tabContentRegion = new TabContentRegion(tab);
        tabContentRegion.setClip((Node)new Rectangle());
        this.tabContentRegions.add((Object)tabContentRegion);
        this.getChildren().add(0, (Object)tabContentRegion);
    }

    private void removeTabContent(Tab tab) {
        for (TabContentRegion tabContentRegion : this.tabContentRegions) {
            if (!tabContentRegion.getTab().equals((Object)tab)) continue;
            tabContentRegion.removeListeners(tab);
            this.getChildren().remove((Object)tabContentRegion);
            this.tabContentRegions.remove((Object)tabContentRegion);
            break;
        }
    }

    private void updateTabPosition() {
        this.tabHeaderArea.setScrollOffset(0.0);
        ((TabPane)this.getSkinnable()).applyCss();
        ((TabPane)this.getSkinnable()).requestLayout();
    }

    private Timeline createTimeline(TabHeaderSkin tabHeaderSkin, Duration duration, double d, EventHandler<ActionEvent> eventHandler) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        KeyValue keyValue = new KeyValue((WritableValue)tabHeaderSkin.animationTransition, (Object)d, Interpolator.LINEAR);
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().add((Object)new KeyFrame(duration, new KeyValue[]{keyValue}));
        timeline.setOnFinished(eventHandler);
        return timeline;
    }

    private boolean isHorizontal() {
        Side side = ((TabPane)this.getSkinnable()).getSide();
        return Side.TOP.equals((Object)side) || Side.BOTTOM.equals((Object)side);
    }

    private void initializeSwipeHandlers() {
        if (IS_TOUCH_SUPPORTED) {
            ((TabPane)this.getSkinnable()).addEventHandler(SwipeEvent.SWIPE_LEFT, swipeEvent -> ((TabPaneBehavior)this.getBehavior()).selectNextTab());
            ((TabPane)this.getSkinnable()).addEventHandler(SwipeEvent.SWIPE_RIGHT, swipeEvent -> ((TabPaneBehavior)this.getBehavior()).selectPreviousTab());
        }
    }

    private boolean isFloatingStyleClass() {
        return ((TabPane)this.getSkinnable()).getStyleClass().contains((Object)"floating");
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        for (TabContentRegion tabContentRegion : this.tabContentRegions) {
            this.maxw = Math.max(this.maxw, this.snapSize(tabContentRegion.prefWidth(-1.0)));
        }
        boolean bl = this.isHorizontal();
        double d6 = this.snapSize(bl ? this.tabHeaderArea.prefWidth(-1.0) : this.tabHeaderArea.prefHeight(-1.0));
        double d7 = bl ? Math.max(this.maxw, d6) : this.maxw + d6;
        return this.snapSize(d7) + d3 + d5;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        for (TabContentRegion tabContentRegion : this.tabContentRegions) {
            this.maxh = Math.max(this.maxh, this.snapSize(tabContentRegion.prefHeight(-1.0)));
        }
        boolean bl = this.isHorizontal();
        double d6 = this.snapSize(bl ? this.tabHeaderArea.prefHeight(-1.0) : this.tabHeaderArea.prefWidth(-1.0));
        double d7 = bl ? this.maxh + this.snapSize(d6) : Math.max(this.maxh, d6);
        return this.snapSize(d7) + d2 + d4;
    }

    public double computeBaselineOffset(double d, double d2, double d3, double d4) {
        Side side = ((TabPane)this.getSkinnable()).getSide();
        if (side == Side.TOP) {
            return this.tabHeaderArea.getBaselineOffset() + d;
        }
        return 0.0;
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        double d5;
        TabPane tabPane = (TabPane)this.getSkinnable();
        Side side = tabPane.getSide();
        double d6 = this.snapSize(this.tabHeaderArea.prefHeight(-1.0));
        double d7 = side.equals((Object)Side.RIGHT) ? d + d3 - d6 : d;
        double d8 = d5 = side.equals((Object)Side.BOTTOM) ? d2 + d4 - d6 : d2;
        if (side == Side.TOP) {
            this.tabHeaderArea.resize(d3, d6);
            this.tabHeaderArea.relocate(d7, d5);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add((Object)new Rotate((double)TabPaneSkin.getRotation(Side.TOP)));
        } else if (side == Side.BOTTOM) {
            this.tabHeaderArea.resize(d3, d6);
            this.tabHeaderArea.relocate(d3, d5 - d6);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add((Object)new Rotate((double)TabPaneSkin.getRotation(Side.BOTTOM), 0.0, d6));
        } else if (side == Side.LEFT) {
            this.tabHeaderArea.resize(d4, d6);
            this.tabHeaderArea.relocate(d7 + d6, d4 - d6);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add((Object)new Rotate((double)TabPaneSkin.getRotation(Side.LEFT), 0.0, d6));
        } else if (side == Side.RIGHT) {
            this.tabHeaderArea.resize(d4, d6);
            this.tabHeaderArea.relocate(d7, d2 - d6);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add((Object)new Rotate((double)TabPaneSkin.getRotation(Side.RIGHT), 0.0, d6));
        }
        this.tabHeaderAreaClipRect.setX(0.0);
        this.tabHeaderAreaClipRect.setY(0.0);
        if (this.isHorizontal()) {
            this.tabHeaderAreaClipRect.setWidth(d3);
        } else {
            this.tabHeaderAreaClipRect.setWidth(d4);
        }
        this.tabHeaderAreaClipRect.setHeight(d6);
        double d9 = 0.0;
        double d10 = 0.0;
        if (side == Side.TOP) {
            d9 = d;
            d10 = d2 + d6;
            if (this.isFloatingStyleClass()) {
                d10 -= 1.0;
            }
        } else if (side == Side.BOTTOM) {
            d9 = d;
            d10 = d2;
            if (this.isFloatingStyleClass()) {
                d10 = 1.0;
            }
        } else if (side == Side.LEFT) {
            d9 = d + d6;
            d10 = d2;
            if (this.isFloatingStyleClass()) {
                d9 -= 1.0;
            }
        } else if (side == Side.RIGHT) {
            d9 = d;
            d10 = d2;
            if (this.isFloatingStyleClass()) {
                d9 = 1.0;
            }
        }
        double d11 = d3 - (this.isHorizontal() ? 0.0 : d6);
        double d12 = d4 - (this.isHorizontal() ? d6 : 0.0);
        int n = this.tabContentRegions.size();
        for (int i = 0; i < n; ++i) {
            TabContentRegion tabContentRegion = (TabContentRegion)((Object)this.tabContentRegions.get(i));
            tabContentRegion.setAlignment(Pos.TOP_LEFT);
            if (tabContentRegion.getClip() != null) {
                ((Rectangle)tabContentRegion.getClip()).setWidth(d11);
                ((Rectangle)tabContentRegion.getClip()).setHeight(d12);
            }
            tabContentRegion.resize(d11, d12);
            tabContentRegion.relocate(d9, d10);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return TabPaneSkin.getClassCssMetaData();
    }

    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                return this.tabHeaderArea.getTabHeaderSkin(this.selectedTab);
            }
            case ITEM_COUNT: {
                return this.tabHeaderArea.headersRegion.getChildren().size();
            }
            case ITEM_AT_INDEX: {
                Integer n = (Integer)arrobject[0];
                if (n == null) {
                    return null;
                }
                return this.tabHeaderArea.headersRegion.getChildren().get(n.intValue());
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    class TabMenuItem
    extends RadioMenuItem {
        Tab tab;
        private InvalidationListener disableListener;
        private WeakInvalidationListener weakDisableListener;

        public TabMenuItem(Tab tab) {
            super(tab.getText(), TabPaneSkin.clone(tab.getGraphic()));
            this.disableListener = new InvalidationListener(){

                public void invalidated(Observable observable) {
                    TabMenuItem.this.setDisable(TabMenuItem.this.tab.isDisable());
                }
            };
            this.weakDisableListener = new WeakInvalidationListener(this.disableListener);
            this.tab = tab;
            this.setDisable(tab.isDisable());
            tab.disableProperty().addListener((InvalidationListener)this.weakDisableListener);
            this.textProperty().bind((ObservableValue)tab.textProperty());
        }

        public Tab getTab() {
            return this.tab;
        }

        public void dispose() {
            this.tab.disableProperty().removeListener((InvalidationListener)this.weakDisableListener);
        }
    }

    class TabControlButtons
    extends StackPane {
        private StackPane inner;
        private StackPane downArrow;
        private Pane downArrowBtn;
        private boolean showControlButtons;
        private ContextMenu popup;
        private boolean showTabsMenu = false;

        public TabControlButtons() {
            this.getStyleClass().setAll((Object[])new String[]{"control-buttons-tab"});
            TabPane tabPane = (TabPane)TabPaneSkin.this.getSkinnable();
            this.downArrowBtn = new Pane();
            this.downArrowBtn.getStyleClass().setAll((Object[])new String[]{"tab-down-button"});
            this.downArrowBtn.setVisible(this.isShowTabsMenu());
            this.downArrow = new StackPane();
            this.downArrow.setManaged(false);
            this.downArrow.getStyleClass().setAll((Object[])new String[]{"arrow"});
            this.downArrow.setRotate(tabPane.getSide().equals((Object)Side.BOTTOM) ? 180.0 : 0.0);
            this.downArrowBtn.getChildren().add((Object)this.downArrow);
            this.downArrowBtn.setOnMouseClicked(mouseEvent -> this.showPopupMenu());
            this.setupPopupMenu();
            this.inner = new StackPane(){

                protected double computePrefWidth(double d) {
                    double d2 = !TabControlButtons.this.isShowTabsMenu() ? 0.0 : this.snapSize(TabControlButtons.this.downArrow.prefWidth(this.getHeight())) + this.snapSize(TabControlButtons.this.downArrowBtn.prefWidth(this.getHeight()));
                    double d3 = 0.0;
                    if (TabControlButtons.this.isShowTabsMenu()) {
                        d3 += d2;
                    }
                    if (d3 > 0.0) {
                        d3 += this.snappedLeftInset() + this.snappedRightInset();
                    }
                    return d3;
                }

                protected double computePrefHeight(double d) {
                    double d2 = 0.0;
                    if (TabControlButtons.this.isShowTabsMenu()) {
                        d2 = Math.max(d2, this.snapSize(TabControlButtons.this.downArrowBtn.prefHeight(d)));
                    }
                    if (d2 > 0.0) {
                        d2 += this.snappedTopInset() + this.snappedBottomInset();
                    }
                    return d2;
                }

                protected void layoutChildren() {
                    if (TabControlButtons.this.isShowTabsMenu()) {
                        double d = 0.0;
                        double d2 = this.snappedTopInset();
                        double d3 = this.snapSize(this.getWidth()) - d + this.snappedLeftInset();
                        double d4 = this.snapSize(this.getHeight()) - d2 + this.snappedBottomInset();
                        this.positionArrow(TabControlButtons.this.downArrowBtn, TabControlButtons.this.downArrow, d, d2, d3, d4);
                    }
                }

                private void positionArrow(Pane pane, StackPane stackPane, double d, double d2, double d3, double d4) {
                    pane.resize(d3, d4);
                    this.positionInArea((Node)pane, d, d2, d3, d4, 0.0, HPos.CENTER, VPos.CENTER);
                    double d5 = this.snapSize(stackPane.prefWidth(-1.0));
                    double d6 = this.snapSize(stackPane.prefHeight(-1.0));
                    stackPane.resize(d5, d6);
                    this.positionInArea((Node)stackPane, pane.snappedLeftInset(), pane.snappedTopInset(), d3 - pane.snappedLeftInset() - pane.snappedRightInset(), d4 - pane.snappedTopInset() - pane.snappedBottomInset(), 0.0, HPos.CENTER, VPos.CENTER);
                }
            };
            this.inner.getStyleClass().add((Object)"container");
            this.inner.getChildren().add((Object)this.downArrowBtn);
            this.getChildren().add((Object)this.inner);
            tabPane.sideProperty().addListener(observable -> {
                Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
                this.downArrow.setRotate(side.equals((Object)Side.BOTTOM) ? 180.0 : 0.0);
            });
            tabPane.getTabs().addListener(change -> this.setupPopupMenu());
            this.showControlButtons = false;
            if (this.isShowTabsMenu()) {
                this.showControlButtons = true;
                this.requestLayout();
            }
            this.getProperties().put(ContextMenu.class, (Object)this.popup);
        }

        private void showTabsMenu(boolean bl) {
            boolean bl2 = this.isShowTabsMenu();
            this.showTabsMenu = bl;
            if (this.showTabsMenu && !bl2) {
                this.downArrowBtn.setVisible(true);
                this.showControlButtons = true;
                this.inner.requestLayout();
                TabPaneSkin.this.tabHeaderArea.requestLayout();
            } else if (!this.showTabsMenu && bl2) {
                this.hideControlButtons();
            }
        }

        private boolean isShowTabsMenu() {
            return this.showTabsMenu;
        }

        protected double computePrefWidth(double d) {
            double d2 = this.snapSize(this.inner.prefWidth(d));
            if (d2 > 0.0) {
                d2 += this.snappedLeftInset() + this.snappedRightInset();
            }
            return d2;
        }

        protected double computePrefHeight(double d) {
            return Math.max(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMinHeight(), this.snapSize(this.inner.prefHeight(d))) + this.snappedTopInset() + this.snappedBottomInset();
        }

        protected void layoutChildren() {
            double d = this.snappedLeftInset();
            double d2 = this.snappedTopInset();
            double d3 = this.snapSize(this.getWidth()) - d + this.snappedRightInset();
            double d4 = this.snapSize(this.getHeight()) - d2 + this.snappedBottomInset();
            if (this.showControlButtons) {
                this.showControlButtons();
                this.showControlButtons = false;
            }
            this.inner.resize(d3, d4);
            this.positionInArea((Node)this.inner, d, d2, d3, d4, 0.0, HPos.CENTER, VPos.BOTTOM);
        }

        private void showControlButtons() {
            this.setVisible(true);
            if (this.popup == null) {
                this.setupPopupMenu();
            }
        }

        private void hideControlButtons() {
            if (this.isShowTabsMenu()) {
                this.showControlButtons = true;
            } else {
                this.setVisible(false);
                this.popup.getItems().clear();
                this.popup = null;
            }
            this.requestLayout();
        }

        private void setupPopupMenu() {
            if (this.popup == null) {
                this.popup = new ContextMenu();
            }
            this.popup.getItems().clear();
            ToggleGroup toggleGroup = new ToggleGroup();
            ObservableList observableList = FXCollections.observableArrayList();
            for (Tab tab : ((TabPane)TabPaneSkin.this.getSkinnable()).getTabs()) {
                TabMenuItem tabMenuItem = new TabMenuItem(tab);
                tabMenuItem.setToggleGroup(toggleGroup);
                tabMenuItem.setOnAction(actionEvent -> ((TabPane)TabPaneSkin.this.getSkinnable()).getSelectionModel().select((Object)tab));
                observableList.add((Object)tabMenuItem);
            }
            this.popup.getItems().addAll((Collection)observableList);
        }

        private void showPopupMenu() {
            for (MenuItem menuItem : this.popup.getItems()) {
                TabMenuItem tabMenuItem = (TabMenuItem)menuItem;
                if (!TabPaneSkin.this.selectedTab.equals((Object)tabMenuItem.getTab())) continue;
                tabMenuItem.setSelected(true);
                break;
            }
            this.popup.show((Node)this.downArrowBtn, Side.BOTTOM, 0.0, 0.0);
        }
    }

    class TabContentRegion
    extends StackPane {
        private TraversalEngine engine;
        private Direction direction = Direction.NEXT;
        private Tab tab;
        private InvalidationListener tabContentListener = observable -> this.updateContent();
        private InvalidationListener tabSelectedListener = new InvalidationListener(){

            public void invalidated(Observable observable) {
                TabContentRegion.this.setVisible(TabContentRegion.this.tab.isSelected());
            }
        };
        private WeakInvalidationListener weakTabContentListener = new WeakInvalidationListener(this.tabContentListener);
        private WeakInvalidationListener weakTabSelectedListener = new WeakInvalidationListener(this.tabSelectedListener);

        public Tab getTab() {
            return this.tab;
        }

        public TabContentRegion(Tab tab) {
            this.getStyleClass().setAll((Object[])new String[]{"tab-content-area"});
            this.setManaged(false);
            this.tab = tab;
            this.updateContent();
            this.setVisible(tab.isSelected());
            tab.selectedProperty().addListener((InvalidationListener)this.weakTabSelectedListener);
            tab.contentProperty().addListener((InvalidationListener)this.weakTabContentListener);
        }

        private void updateContent() {
            Node node = this.getTab().getContent();
            if (node == null) {
                this.getChildren().clear();
            } else {
                this.getChildren().setAll((Object[])new Node[]{node});
            }
        }

        private void removeListeners(Tab tab) {
            tab.selectedProperty().removeListener((InvalidationListener)this.weakTabSelectedListener);
            tab.contentProperty().removeListener((InvalidationListener)this.weakTabContentListener);
        }
    }

    class TabHeaderSkin
    extends StackPane {
        private final Tab tab;
        private Label label;
        private StackPane closeBtn;
        private StackPane inner;
        private Tooltip oldTooltip;
        private Tooltip tooltip;
        private Rectangle clip;
        private boolean isClosing = false;
        private MultiplePropertyChangeListenerHandler listener = new MultiplePropertyChangeListenerHandler((Callback<String, Void>)((Callback)string -> {
            this.handlePropertyChanged((String)string);
            return null;
        }));
        private final ListChangeListener<String> styleClassListener = new ListChangeListener<String>(){

            public void onChanged(ListChangeListener.Change<? extends String> change) {
                TabHeaderSkin.this.getStyleClass().setAll((Collection)TabHeaderSkin.this.tab.getStyleClass());
            }
        };
        private final WeakListChangeListener<String> weakStyleClassListener = new WeakListChangeListener(this.styleClassListener);
        private final DoubleProperty animationTransition = new SimpleDoubleProperty((Object)this, "animationTransition", 1.0){

            protected void invalidated() {
                TabHeaderSkin.this.requestLayout();
            }
        };
        private TabAnimationState animationState = TabAnimationState.NONE;
        private Timeline currentAnimation;

        public Tab getTab() {
            return this.tab;
        }

        public TabHeaderSkin(Tab tab) {
            this.getStyleClass().setAll((Collection)tab.getStyleClass());
            this.setId(tab.getId());
            this.setStyle(tab.getStyle());
            this.setAccessibleRole(AccessibleRole.TAB_ITEM);
            this.tab = tab;
            this.clip = new Rectangle();
            this.setClip((Node)this.clip);
            this.label = new Label(tab.getText(), tab.getGraphic());
            this.label.getStyleClass().setAll((Object[])new String[]{"tab-label"});
            this.closeBtn = new StackPane(){

                protected double computePrefWidth(double d) {
                    return CLOSE_BTN_SIZE;
                }

                protected double computePrefHeight(double d) {
                    return CLOSE_BTN_SIZE;
                }

                public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
                    switch (accessibleAction) {
                        case FIRE: {
                            Tab tab = TabHeaderSkin.this.getTab();
                            TabPaneBehavior tabPaneBehavior = (TabPaneBehavior)TabPaneSkin.this.getBehavior();
                            if (!tabPaneBehavior.canCloseTab(tab)) break;
                            tabPaneBehavior.closeTab(tab);
                            this.setOnMousePressed(null);
                        }
                    }
                    super.executeAccessibleAction(accessibleAction, arrobject);
                }
            };
            this.closeBtn.setAccessibleRole(AccessibleRole.BUTTON);
            this.closeBtn.setAccessibleText(ControlResources.getString("Accessibility.title.TabPane.CloseButton"));
            this.closeBtn.getStyleClass().setAll((Object[])new String[]{"tab-close-button"});
            this.closeBtn.setOnMousePressed((EventHandler)new EventHandler<MouseEvent>(){

                public void handle(MouseEvent mouseEvent) {
                    Tab tab = TabHeaderSkin.this.getTab();
                    TabPaneBehavior tabPaneBehavior = (TabPaneBehavior)TabPaneSkin.this.getBehavior();
                    if (tabPaneBehavior.canCloseTab(tab)) {
                        tabPaneBehavior.closeTab(tab);
                        TabHeaderSkin.this.setOnMousePressed(null);
                    }
                }
            });
            this.updateGraphicRotation();
            final Region region = new Region();
            region.setMouseTransparent(true);
            region.getStyleClass().add((Object)"focus-indicator");
            this.inner = new StackPane(){

                protected void layoutChildren() {
                    TabPane tabPane = (TabPane)TabPaneSkin.this.getSkinnable();
                    double d = this.snappedTopInset();
                    double d2 = this.snappedRightInset();
                    double d3 = this.snappedBottomInset();
                    double d4 = this.snappedLeftInset();
                    double d5 = this.getWidth() - (d4 + d2);
                    double d6 = this.getHeight() - (d + d3);
                    double d7 = this.snapSize(TabHeaderSkin.this.label.prefWidth(-1.0));
                    double d8 = this.snapSize(TabHeaderSkin.this.label.prefHeight(-1.0));
                    double d9 = TabHeaderSkin.this.showCloseButton() ? this.snapSize(TabHeaderSkin.this.closeBtn.prefWidth(-1.0)) : 0.0;
                    double d10 = TabHeaderSkin.this.showCloseButton() ? this.snapSize(TabHeaderSkin.this.closeBtn.prefHeight(-1.0)) : 0.0;
                    double d11 = this.snapSize(tabPane.getTabMinWidth());
                    double d12 = this.snapSize(tabPane.getTabMaxWidth());
                    double d13 = this.snapSize(tabPane.getTabMaxHeight());
                    double d14 = d7;
                    double d15 = d7;
                    double d16 = d8;
                    double d17 = d14 + d9;
                    double d18 = Math.max(d16, d10);
                    if (d17 > d12 && d12 != Double.MAX_VALUE) {
                        d14 = d12 - d9;
                        d15 = d12 - d9;
                    } else if (d17 < d11) {
                        d14 = d11 - d9;
                    }
                    if (d18 > d13 && d13 != Double.MAX_VALUE) {
                        d16 = d13;
                    }
                    if (TabHeaderSkin.this.animationState != TabAnimationState.NONE) {
                        d14 *= TabHeaderSkin.this.animationTransition.get();
                        TabHeaderSkin.this.closeBtn.setVisible(false);
                    } else {
                        TabHeaderSkin.this.closeBtn.setVisible(TabHeaderSkin.this.showCloseButton());
                    }
                    TabHeaderSkin.this.label.resize(d15, d16);
                    double d19 = d4;
                    double d20 = (d12 < Double.MAX_VALUE ? Math.min(d5, d12) : d5) - d2 - d9;
                    this.positionInArea((Node)TabHeaderSkin.this.label, d19, d, d14, d6, 0.0, HPos.CENTER, VPos.CENTER);
                    if (TabHeaderSkin.this.closeBtn.isVisible()) {
                        TabHeaderSkin.this.closeBtn.resize(d9, d10);
                        this.positionInArea((Node)TabHeaderSkin.this.closeBtn, d20, d, d9, d6, 0.0, HPos.CENTER, VPos.CENTER);
                    }
                    int n = Utils.isMac() ? 2 : 3;
                    int n2 = Utils.isMac() ? 2 : 1;
                    region.resizeRelocate(d4 - (double)n2, d + (double)n, d5 + (double)(2 * n2), d6 - (double)(2 * n));
                }
            };
            this.inner.getStyleClass().add((Object)"tab-container");
            this.inner.setRotate(((TabPane)TabPaneSkin.this.getSkinnable()).getSide().equals((Object)Side.BOTTOM) ? 180.0 : 0.0);
            this.inner.getChildren().addAll((Object[])new Node[]{this.label, this.closeBtn, region});
            this.getChildren().addAll((Object[])new Node[]{this.inner});
            this.tooltip = tab.getTooltip();
            if (this.tooltip != null) {
                Tooltip.install((Node)this, (Tooltip)this.tooltip);
                this.oldTooltip = this.tooltip;
            }
            this.listener.registerChangeListener((ObservableValue<?>)tab.closableProperty(), "CLOSABLE");
            this.listener.registerChangeListener((ObservableValue<?>)tab.selectedProperty(), "SELECTED");
            this.listener.registerChangeListener((ObservableValue<?>)tab.textProperty(), "TEXT");
            this.listener.registerChangeListener((ObservableValue<?>)tab.graphicProperty(), "GRAPHIC");
            this.listener.registerChangeListener((ObservableValue<?>)tab.contextMenuProperty(), "CONTEXT_MENU");
            this.listener.registerChangeListener((ObservableValue<?>)tab.tooltipProperty(), "TOOLTIP");
            this.listener.registerChangeListener((ObservableValue<?>)tab.disableProperty(), "DISABLE");
            this.listener.registerChangeListener((ObservableValue<?>)tab.styleProperty(), "STYLE");
            tab.getStyleClass().addListener(this.weakStyleClassListener);
            this.listener.registerChangeListener((ObservableValue<?>)((TabPane)TabPaneSkin.this.getSkinnable()).tabClosingPolicyProperty(), "TAB_CLOSING_POLICY");
            this.listener.registerChangeListener((ObservableValue<?>)((TabPane)TabPaneSkin.this.getSkinnable()).sideProperty(), "SIDE");
            this.listener.registerChangeListener((ObservableValue<?>)((TabPane)TabPaneSkin.this.getSkinnable()).rotateGraphicProperty(), "ROTATE_GRAPHIC");
            this.listener.registerChangeListener((ObservableValue<?>)((TabPane)TabPaneSkin.this.getSkinnable()).tabMinWidthProperty(), "TAB_MIN_WIDTH");
            this.listener.registerChangeListener((ObservableValue<?>)((TabPane)TabPaneSkin.this.getSkinnable()).tabMaxWidthProperty(), "TAB_MAX_WIDTH");
            this.listener.registerChangeListener((ObservableValue<?>)((TabPane)TabPaneSkin.this.getSkinnable()).tabMinHeightProperty(), "TAB_MIN_HEIGHT");
            this.listener.registerChangeListener((ObservableValue<?>)((TabPane)TabPaneSkin.this.getSkinnable()).tabMaxHeightProperty(), "TAB_MAX_HEIGHT");
            this.getProperties().put(Tab.class, (Object)tab);
            this.getProperties().put(ContextMenu.class, (Object)tab.getContextMenu());
            this.setOnContextMenuRequested(contextMenuEvent -> {
                if (this.getTab().getContextMenu() != null) {
                    this.getTab().getContextMenu().show((Node)this.inner, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
                    contextMenuEvent.consume();
                }
            });
            this.setOnMousePressed((EventHandler)new EventHandler<MouseEvent>(){

                public void handle(MouseEvent mouseEvent) {
                    if (TabHeaderSkin.this.getTab().isDisable()) {
                        return;
                    }
                    if (mouseEvent.getButton().equals((Object)MouseButton.MIDDLE)) {
                        if (TabHeaderSkin.this.showCloseButton()) {
                            Tab tab = TabHeaderSkin.this.getTab();
                            TabPaneBehavior tabPaneBehavior = (TabPaneBehavior)TabPaneSkin.this.getBehavior();
                            if (tabPaneBehavior.canCloseTab(tab)) {
                                TabHeaderSkin.this.removeListeners(tab);
                                tabPaneBehavior.closeTab(tab);
                            }
                        }
                    } else if (mouseEvent.getButton().equals((Object)MouseButton.PRIMARY)) {
                        ((TabPaneBehavior)TabPaneSkin.this.getBehavior()).selectTab(TabHeaderSkin.this.getTab());
                    }
                }
            });
            this.pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, tab.isSelected());
            this.pseudoClassStateChanged(DISABLED_PSEUDOCLASS_STATE, tab.isDisable());
            Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
            this.pseudoClassStateChanged(TOP_PSEUDOCLASS_STATE, side == Side.TOP);
            this.pseudoClassStateChanged(RIGHT_PSEUDOCLASS_STATE, side == Side.RIGHT);
            this.pseudoClassStateChanged(BOTTOM_PSEUDOCLASS_STATE, side == Side.BOTTOM);
            this.pseudoClassStateChanged(LEFT_PSEUDOCLASS_STATE, side == Side.LEFT);
        }

        private void handlePropertyChanged(String string) {
            if ("CLOSABLE".equals(string)) {
                this.inner.requestLayout();
                this.requestLayout();
            } else if ("SELECTED".equals(string)) {
                this.pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, this.tab.isSelected());
                this.inner.requestLayout();
                this.requestLayout();
            } else if ("TEXT".equals(string)) {
                this.label.setText(this.getTab().getText());
            } else if ("GRAPHIC".equals(string)) {
                this.label.setGraphic(this.getTab().getGraphic());
            } else if (!"CONTEXT_MENU".equals(string)) {
                if ("TOOLTIP".equals(string)) {
                    if (this.oldTooltip != null) {
                        Tooltip.uninstall((Node)this, (Tooltip)this.oldTooltip);
                    }
                    this.tooltip = this.tab.getTooltip();
                    if (this.tooltip != null) {
                        Tooltip.install((Node)this, (Tooltip)this.tooltip);
                        this.oldTooltip = this.tooltip;
                    }
                } else if ("DISABLE".equals(string)) {
                    this.pseudoClassStateChanged(DISABLED_PSEUDOCLASS_STATE, this.tab.isDisable());
                    this.inner.requestLayout();
                    this.requestLayout();
                } else if ("STYLE".equals(string)) {
                    this.setStyle(this.tab.getStyle());
                } else if ("TAB_CLOSING_POLICY".equals(string)) {
                    this.inner.requestLayout();
                    this.requestLayout();
                } else if ("SIDE".equals(string)) {
                    Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
                    this.pseudoClassStateChanged(TOP_PSEUDOCLASS_STATE, side == Side.TOP);
                    this.pseudoClassStateChanged(RIGHT_PSEUDOCLASS_STATE, side == Side.RIGHT);
                    this.pseudoClassStateChanged(BOTTOM_PSEUDOCLASS_STATE, side == Side.BOTTOM);
                    this.pseudoClassStateChanged(LEFT_PSEUDOCLASS_STATE, side == Side.LEFT);
                    this.inner.setRotate(side == Side.BOTTOM ? 180.0 : 0.0);
                    if (((TabPane)TabPaneSkin.this.getSkinnable()).isRotateGraphic()) {
                        this.updateGraphicRotation();
                    }
                } else if ("ROTATE_GRAPHIC".equals(string)) {
                    this.updateGraphicRotation();
                } else if ("TAB_MIN_WIDTH".equals(string)) {
                    this.requestLayout();
                    ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
                } else if ("TAB_MAX_WIDTH".equals(string)) {
                    this.requestLayout();
                    ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
                } else if ("TAB_MIN_HEIGHT".equals(string)) {
                    this.requestLayout();
                    ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
                } else if ("TAB_MAX_HEIGHT".equals(string)) {
                    this.requestLayout();
                    ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
                }
            }
        }

        private void updateGraphicRotation() {
            if (this.label.getGraphic() != null) {
                this.label.getGraphic().setRotate(((TabPane)TabPaneSkin.this.getSkinnable()).isRotateGraphic() ? 0.0 : (double)(((TabPane)TabPaneSkin.this.getSkinnable()).getSide().equals((Object)Side.RIGHT) ? -90.0f : (((TabPane)TabPaneSkin.this.getSkinnable()).getSide().equals((Object)Side.LEFT) ? 90.0f : 0.0f)));
            }
        }

        private boolean showCloseButton() {
            return this.tab.isClosable() && (((TabPane)TabPaneSkin.this.getSkinnable()).getTabClosingPolicy().equals((Object)TabPane.TabClosingPolicy.ALL_TABS) || ((TabPane)TabPaneSkin.this.getSkinnable()).getTabClosingPolicy().equals((Object)TabPane.TabClosingPolicy.SELECTED_TAB) && this.tab.isSelected());
        }

        private void removeListeners(Tab tab) {
            this.listener.dispose();
            this.inner.getChildren().clear();
            this.getChildren().clear();
        }

        protected double computePrefWidth(double d) {
            double d2 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMinWidth());
            double d3 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMaxWidth());
            double d4 = this.snappedRightInset();
            double d5 = this.snappedLeftInset();
            double d6 = this.snapSize(this.label.prefWidth(-1.0));
            if (this.showCloseButton()) {
                d6 += this.snapSize(this.closeBtn.prefWidth(-1.0));
            }
            if (d6 > d3) {
                d6 = d3;
            } else if (d6 < d2) {
                d6 = d2;
            }
            return d6 += d4 + d5;
        }

        protected double computePrefHeight(double d) {
            double d2 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMinHeight());
            double d3 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMaxHeight());
            double d4 = this.snappedTopInset();
            double d5 = this.snappedBottomInset();
            double d6 = this.snapSize(this.label.prefHeight(d));
            if (d6 > d3) {
                d6 = d3;
            } else if (d6 < d2) {
                d6 = d2;
            }
            return d6 += d4 + d5;
        }

        protected void layoutChildren() {
            double d = (this.snapSize(this.getWidth()) - this.snappedRightInset() - this.snappedLeftInset()) * this.animationTransition.getValue();
            this.inner.resize(d, this.snapSize(this.getHeight()) - this.snappedTopInset() - this.snappedBottomInset());
            this.inner.relocate(this.snappedLeftInset(), this.snappedTopInset());
        }

        protected void setWidth(double d) {
            super.setWidth(d);
            this.clip.setWidth(d);
        }

        protected void setHeight(double d) {
            super.setHeight(d);
            this.clip.setHeight(d);
        }

        public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
            switch (accessibleAttribute) {
                case TEXT: {
                    return this.getTab().getText();
                }
                case SELECTED: {
                    return TabPaneSkin.this.selectedTab == this.getTab();
                }
            }
            return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
        }

        public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
            switch (accessibleAction) {
                case REQUEST_FOCUS: {
                    ((TabPane)TabPaneSkin.this.getSkinnable()).getSelectionModel().select((Object)this.getTab());
                    break;
                }
                default: {
                    super.executeAccessibleAction(accessibleAction, arrobject);
                }
            }
        }
    }

    class TabHeaderArea
    extends StackPane {
        private Rectangle headerClip;
        private StackPane headersRegion;
        private StackPane headerBackground;
        private TabControlButtons controlButtons;
        private boolean measureClosingTabs = false;
        private double scrollOffset;
        private List<TabHeaderSkin> removeTab = new ArrayList<TabHeaderSkin>();

        public TabHeaderArea() {
            this.getStyleClass().setAll((Object[])new String[]{"tab-header-area"});
            this.setManaged(false);
            TabPane tabPane = (TabPane)TabPaneSkin.this.getSkinnable();
            this.headerClip = new Rectangle();
            this.headersRegion = new StackPane(){

                protected double computePrefWidth(double d) {
                    double d2 = 0.0;
                    for (Node node : this.getChildren()) {
                        TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                        if (!tabHeaderSkin.isVisible() || !TabHeaderArea.this.measureClosingTabs && tabHeaderSkin.isClosing) continue;
                        d2 += tabHeaderSkin.prefWidth(d);
                    }
                    return this.snapSize(d2) + this.snappedLeftInset() + this.snappedRightInset();
                }

                protected double computePrefHeight(double d) {
                    double d2 = 0.0;
                    for (Node node : this.getChildren()) {
                        TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                        d2 = Math.max(d2, tabHeaderSkin.prefHeight(d));
                    }
                    return this.snapSize(d2) + this.snappedTopInset() + this.snappedBottomInset();
                }

                protected void layoutChildren() {
                    if (TabHeaderArea.this.tabsFit()) {
                        TabHeaderArea.this.setScrollOffset(0.0);
                    } else if (!TabHeaderArea.this.removeTab.isEmpty()) {
                        double d = 0.0;
                        double d2 = TabPaneSkin.this.tabHeaderArea.getWidth() - this.snapSize(TabHeaderArea.this.controlButtons.prefWidth(-1.0)) - TabHeaderArea.this.firstTabIndent() - 10.0;
                        Iterator iterator = this.getChildren().iterator();
                        while (iterator.hasNext()) {
                            TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)((Object)iterator.next());
                            double d3 = this.snapSize(tabHeaderSkin.prefWidth(-1.0));
                            if (TabHeaderArea.this.removeTab.contains((Object)tabHeaderSkin)) {
                                if (d < d2) {
                                    TabPaneSkin.this.isSelectingTab = true;
                                }
                                iterator.remove();
                                TabHeaderArea.this.removeTab.remove((Object)tabHeaderSkin);
                                if (TabHeaderArea.this.removeTab.isEmpty()) break;
                            }
                            d += d3;
                        }
                    }
                    if (TabPaneSkin.this.isSelectingTab) {
                        TabHeaderArea.this.ensureSelectedTabIsVisible();
                        TabPaneSkin.this.isSelectingTab = false;
                    } else {
                        TabHeaderArea.this.validateScrollOffset();
                    }
                    Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
                    double d = this.snapSize(this.prefHeight(-1.0));
                    double d4 = side.equals((Object)Side.LEFT) || side.equals((Object)Side.BOTTOM) ? this.snapSize(this.getWidth()) - TabHeaderArea.this.getScrollOffset() : TabHeaderArea.this.getScrollOffset();
                    TabHeaderArea.this.updateHeaderClip();
                    for (Node node : this.getChildren()) {
                        double d5;
                        TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                        double d6 = this.snapSize(tabHeaderSkin.prefWidth(-1.0) * tabHeaderSkin.animationTransition.get());
                        double d7 = this.snapSize(tabHeaderSkin.prefHeight(-1.0));
                        tabHeaderSkin.resize(d6, d7);
                        double d8 = d5 = side.equals((Object)Side.BOTTOM) ? 0.0 : d - d7 - this.snappedBottomInset();
                        if (side.equals((Object)Side.LEFT) || side.equals((Object)Side.BOTTOM)) {
                            tabHeaderSkin.relocate(d4 -= d6, d5);
                            continue;
                        }
                        tabHeaderSkin.relocate(d4, d5);
                        d4 += d6;
                    }
                }
            };
            this.headersRegion.getStyleClass().setAll((Object[])new String[]{"headers-region"});
            this.headersRegion.setClip((Node)this.headerClip);
            this.headerBackground = new StackPane();
            this.headerBackground.getStyleClass().setAll((Object[])new String[]{"tab-header-background"});
            int n = 0;
            for (Tab tab : tabPane.getTabs()) {
                this.addTab(tab, n++);
            }
            this.controlButtons = new TabControlButtons();
            this.controlButtons.setVisible(false);
            if (this.controlButtons.isVisible()) {
                this.controlButtons.setVisible(true);
            }
            this.getChildren().addAll((Object[])new Node[]{this.headerBackground, this.headersRegion, this.controlButtons});
            this.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
                Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
                side = side == null ? Side.TOP : side;
                switch (side) {
                    default: {
                        this.setScrollOffset(this.scrollOffset - scrollEvent.getDeltaY());
                        break;
                    }
                    case LEFT: 
                    case RIGHT: {
                        this.setScrollOffset(this.scrollOffset + scrollEvent.getDeltaY());
                    }
                }
            });
        }

        private void updateHeaderClip() {
            Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
            double d = 0.0;
            double d2 = 0.0;
            double d3 = 0.0;
            double d4 = 0.0;
            double d5 = 0.0;
            double d6 = 0.0;
            double d7 = this.firstTabIndent();
            double d8 = this.snapSize(this.controlButtons.prefWidth(-1.0));
            this.measureClosingTabs = true;
            double d9 = this.snapSize(this.headersRegion.prefWidth(-1.0));
            this.measureClosingTabs = false;
            double d10 = this.snapSize(this.headersRegion.prefHeight(-1.0));
            if (d8 > 0.0) {
                d8 += 10.0;
            }
            if (this.headersRegion.getEffect() instanceof DropShadow) {
                DropShadow dropShadow = (DropShadow)this.headersRegion.getEffect();
                d6 = dropShadow.getRadius();
            }
            d5 = this.snapSize(this.getWidth()) - d8 - d7;
            if (side.equals((Object)Side.LEFT) || side.equals((Object)Side.BOTTOM)) {
                if (d9 < d5) {
                    d3 = d9 + d6;
                } else {
                    d = d9 - d5;
                    d3 = d5 + d6;
                }
                d4 = d10;
            } else {
                d = -d6;
                d3 = (d9 < d5 ? d9 : d5) + d6;
                d4 = d10;
            }
            this.headerClip.setX(d);
            this.headerClip.setY(d2);
            this.headerClip.setWidth(d3);
            this.headerClip.setHeight(d4);
        }

        private void addTab(Tab tab, int n) {
            TabHeaderSkin tabHeaderSkin = new TabHeaderSkin(tab);
            this.headersRegion.getChildren().add(n, (Object)tabHeaderSkin);
        }

        private void removeTab(Tab tab) {
            TabHeaderSkin tabHeaderSkin = this.getTabHeaderSkin(tab);
            if (tabHeaderSkin != null) {
                if (this.tabsFit()) {
                    this.headersRegion.getChildren().remove((Object)tabHeaderSkin);
                } else {
                    this.removeTab.add(tabHeaderSkin);
                    tabHeaderSkin.removeListeners(tab);
                }
            }
        }

        private TabHeaderSkin getTabHeaderSkin(Tab tab) {
            for (Node node : this.headersRegion.getChildren()) {
                TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                if (!tabHeaderSkin.getTab().equals((Object)tab)) continue;
                return tabHeaderSkin;
            }
            return null;
        }

        private boolean tabsFit() {
            double d;
            double d2 = this.snapSize(this.headersRegion.prefWidth(-1.0));
            double d3 = d2 + (d = this.snapSize(this.controlButtons.prefWidth(-1.0))) + this.firstTabIndent() + 10.0;
            return d3 < this.getWidth();
        }

        private void ensureSelectedTabIsVisible() {
            double d = this.snapSize(TabPaneSkin.this.isHorizontal() ? ((TabPane)TabPaneSkin.this.getSkinnable()).getWidth() : ((TabPane)TabPaneSkin.this.getSkinnable()).getHeight());
            double d2 = this.snapSize(this.controlButtons.getWidth());
            double d3 = d - d2 - this.firstTabIndent() - 10.0;
            double d4 = 0.0;
            double d5 = 0.0;
            double d6 = 0.0;
            for (Node node : this.headersRegion.getChildren()) {
                TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                double d7 = this.snapSize(tabHeaderSkin.prefWidth(-1.0));
                if (TabPaneSkin.this.selectedTab != null && TabPaneSkin.this.selectedTab.equals((Object)tabHeaderSkin.getTab())) {
                    d5 = d4;
                    d6 = d7;
                }
                d4 += d7;
            }
            double d8 = this.getScrollOffset();
            double d9 = d5;
            double d10 = d5 + d6;
            double d11 = d3;
            if (d9 < -d8) {
                this.setScrollOffset(-d9);
            } else if (d10 > d11 - d8) {
                this.setScrollOffset(d11 - d10);
            }
        }

        public double getScrollOffset() {
            return this.scrollOffset;
        }

        private void validateScrollOffset() {
            this.setScrollOffset(this.getScrollOffset());
        }

        private void setScrollOffset(double d) {
            double d2 = this.snapSize(TabPaneSkin.this.isHorizontal() ? ((TabPane)TabPaneSkin.this.getSkinnable()).getWidth() : ((TabPane)TabPaneSkin.this.getSkinnable()).getHeight());
            double d3 = this.snapSize(this.controlButtons.getWidth());
            double d4 = d2 - d3 - this.firstTabIndent() - 10.0;
            double d5 = 0.0;
            for (Node node : this.headersRegion.getChildren()) {
                TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                double d6 = this.snapSize(tabHeaderSkin.prefWidth(-1.0));
                d5 += d6;
            }
            double d7 = d4 - d > d5 && d < 0.0 ? d4 - d5 : (d > 0.0 ? 0.0 : d);
            if (d7 != this.scrollOffset) {
                this.scrollOffset = d7;
                this.headersRegion.requestLayout();
            }
        }

        private double firstTabIndent() {
            switch (((TabPane)TabPaneSkin.this.getSkinnable()).getSide()) {
                case TOP: 
                case BOTTOM: {
                    return this.snappedLeftInset();
                }
                case LEFT: 
                case RIGHT: {
                    return this.snappedTopInset();
                }
            }
            return 0.0;
        }

        protected double computePrefWidth(double d) {
            double d2 = TabPaneSkin.this.isHorizontal() ? this.snappedLeftInset() + this.snappedRightInset() : this.snappedTopInset() + this.snappedBottomInset();
            return this.snapSize(this.headersRegion.prefWidth(d)) + this.controlButtons.prefWidth(d) + this.firstTabIndent() + 10.0 + d2;
        }

        protected double computePrefHeight(double d) {
            double d2 = TabPaneSkin.this.isHorizontal() ? this.snappedTopInset() + this.snappedBottomInset() : this.snappedLeftInset() + this.snappedRightInset();
            return this.snapSize(this.headersRegion.prefHeight(-1.0)) + d2;
        }

        public double getBaselineOffset() {
            if (((TabPane)TabPaneSkin.this.getSkinnable()).getSide() == Side.TOP) {
                return this.headersRegion.getBaselineOffset() + this.snappedTopInset();
            }
            return 0.0;
        }

        protected void layoutChildren() {
            double d = this.snappedLeftInset();
            double d2 = this.snappedRightInset();
            double d3 = this.snappedTopInset();
            double d4 = this.snappedBottomInset();
            double d5 = this.snapSize(this.getWidth()) - (TabPaneSkin.this.isHorizontal() ? d + d2 : d3 + d4);
            double d6 = this.snapSize(this.getHeight()) - (TabPaneSkin.this.isHorizontal() ? d3 + d4 : d + d2);
            double d7 = this.snapSize(this.prefHeight(-1.0));
            double d8 = this.snapSize(this.headersRegion.prefWidth(-1.0));
            double d9 = this.snapSize(this.headersRegion.prefHeight(-1.0));
            this.controlButtons.showTabsMenu(!this.tabsFit());
            this.updateHeaderClip();
            this.headersRegion.requestLayout();
            double d10 = this.snapSize(this.controlButtons.prefWidth(-1.0));
            double d11 = this.controlButtons.prefHeight(d10);
            this.controlButtons.resize(d10, d11);
            this.headersRegion.resize(d8, d9);
            if (TabPaneSkin.this.isFloatingStyleClass()) {
                this.headerBackground.setVisible(false);
            } else {
                this.headerBackground.resize(this.snapSize(this.getWidth()), this.snapSize(this.getHeight()));
                this.headerBackground.setVisible(true);
            }
            double d12 = 0.0;
            double d13 = 0.0;
            double d14 = 0.0;
            double d15 = 0.0;
            Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
            if (side.equals((Object)Side.TOP)) {
                d12 = d;
                d13 = d7 - d9 - d4;
                d14 = d5 - d10 + d;
                d15 = this.snapSize(this.getHeight()) - d11 - d4;
            } else if (side.equals((Object)Side.RIGHT)) {
                d12 = d3;
                d13 = d7 - d9 - d;
                d14 = d5 - d10 + d3;
                d15 = this.snapSize(this.getHeight()) - d11 - d;
            } else if (side.equals((Object)Side.BOTTOM)) {
                d12 = this.snapSize(this.getWidth()) - d8 - d;
                d13 = d7 - d9 - d3;
                d14 = d2;
                d15 = this.snapSize(this.getHeight()) - d11 - d3;
            } else if (side.equals((Object)Side.LEFT)) {
                d12 = this.snapSize(this.getWidth()) - d8 - d3;
                d13 = d7 - d9 - d2;
                d14 = d;
                d15 = this.snapSize(this.getHeight()) - d11 - d2;
            }
            if (this.headerBackground.isVisible()) {
                this.positionInArea((Node)this.headerBackground, 0.0, 0.0, this.snapSize(this.getWidth()), this.snapSize(this.getHeight()), 0.0, HPos.CENTER, VPos.CENTER);
            }
            this.positionInArea((Node)this.headersRegion, d12, d13, d5, d6, 0.0, HPos.LEFT, VPos.CENTER);
            this.positionInArea((Node)this.controlButtons, d14, d15, d10, d11, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        private static final CssMetaData<TabPane, TabAnimation> OPEN_TAB_ANIMATION;
        private static final CssMetaData<TabPane, TabAnimation> CLOSE_TAB_ANIMATION;

        private StyleableProperties() {
        }

        static {
            OPEN_TAB_ANIMATION = new CssMetaData<TabPane, TabAnimation>("-fx-open-tab-animation", new EnumConverter<TabAnimation>(TabAnimation.class), TabAnimation.GROW){

                public boolean isSettable(TabPane tabPane) {
                    return true;
                }

                public StyleableProperty<TabAnimation> getStyleableProperty(TabPane tabPane) {
                    TabPaneSkin tabPaneSkin = (TabPaneSkin)tabPane.getSkin();
                    return (StyleableProperty)tabPaneSkin.openTabAnimation;
                }
            };
            CLOSE_TAB_ANIMATION = new CssMetaData<TabPane, TabAnimation>("-fx-close-tab-animation", new EnumConverter<TabAnimation>(TabAnimation.class), TabAnimation.GROW){

                public boolean isSettable(TabPane tabPane) {
                    return true;
                }

                public StyleableProperty<TabAnimation> getStyleableProperty(TabPane tabPane) {
                    TabPaneSkin tabPaneSkin = (TabPaneSkin)tabPane.getSkin();
                    return (StyleableProperty)tabPaneSkin.closeTabAnimation;
                }
            };
            ArrayList<CssMetaData<TabPane, TabAnimation>> arrayList = new ArrayList<CssMetaData<TabPane, TabAnimation>>(SkinBase.getClassCssMetaData());
            arrayList.add(OPEN_TAB_ANIMATION);
            arrayList.add(CLOSE_TAB_ANIMATION);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    private static enum TabAnimationState {
        SHOWING,
        HIDING,
        NONE;

    }

    private static enum TabAnimation {
        NONE,
        GROW;

    }
}

