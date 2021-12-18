/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation$Status
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 *  javafx.animation.Timeline
 *  javafx.beans.InvalidationListener
 *  javafx.beans.WeakInvalidationListener
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.ReadOnlyBooleanProperty
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.css.CssMetaData
 *  javafx.css.PseudoClass
 *  javafx.css.Styleable
 *  javafx.event.ActionEvent
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.geometry.Bounds
 *  javafx.geometry.HPos
 *  javafx.geometry.NodeOrientation
 *  javafx.geometry.Orientation
 *  javafx.geometry.Side
 *  javafx.geometry.VPos
 *  javafx.scene.AccessibleAction
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.AccessibleRole
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.control.CheckMenuItem
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.CustomMenuItem
 *  javafx.scene.control.Label
 *  javafx.scene.control.Menu
 *  javafx.scene.control.MenuItem
 *  javafx.scene.control.RadioMenuItem
 *  javafx.scene.control.SeparatorMenuItem
 *  javafx.scene.control.Skin
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.input.ScrollEvent
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 *  javafx.scene.layout.VBox
 *  javafx.scene.shape.Rectangle
 *  javafx.util.Callback
 *  javafx.util.Duration
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusPopupBehavior;
import com.sun.javafx.scene.control.skin.MenuBarSkin;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;

public class ContextMenuContent
extends Region {
    private ContextMenu contextMenu;
    private double maxGraphicWidth = 0.0;
    private double maxRightWidth = 0.0;
    private double maxLabelWidth = 0.0;
    private double maxRowHeight = 0.0;
    private double maxLeftWidth = 0.0;
    private double oldWidth = 0.0;
    private Rectangle clipRect;
    MenuBox itemsContainer;
    private ArrowMenuItem upArrow;
    private ArrowMenuItem downArrow;
    private int currentFocusedIndex = -1;
    private boolean itemsDirty = true;
    private InvalidationListener popupShowingListener = observable -> this.updateItems();
    private WeakInvalidationListener weakPopupShowingListener = new WeakInvalidationListener(this.popupShowingListener);
    private boolean isFirstShow = true;
    private double ty;
    private ChangeListener<Boolean> menuShowingListener = (observableValue, bl, bl2) -> {
        ReadOnlyBooleanProperty readOnlyBooleanProperty = (ReadOnlyBooleanProperty)observableValue;
        Menu menu = (Menu)readOnlyBooleanProperty.getBean();
        if (bl.booleanValue() && !bl2.booleanValue()) {
            this.hideSubmenu();
        } else if (!bl.booleanValue() && bl2.booleanValue()) {
            this.showSubmenu(menu);
        }
    };
    private ListChangeListener<MenuItem> contextMenuItemsListener = change -> {
        while (change.next()) {
            this.updateMenuShowingListeners(change.getRemoved(), false);
            this.updateMenuShowingListeners(change.getAddedSubList(), true);
        }
        this.itemsDirty = true;
        this.updateItems();
    };
    private ChangeListener<Boolean> menuItemVisibleListener = (observableValue, bl, bl2) -> this.requestLayout();
    private Menu openSubmenu;
    private ContextMenu submenu;
    Region selectedBackground;
    private static final PseudoClass SELECTED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"selected");
    private static final PseudoClass DISABLED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"disabled");
    private static final PseudoClass CHECKED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"checked");

    public ContextMenuContent(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
        this.clipRect = new Rectangle();
        this.clipRect.setSmooth(false);
        this.itemsContainer = new MenuBox();
        this.itemsContainer.setClip((Node)this.clipRect);
        this.upArrow = new ArrowMenuItem(this);
        this.upArrow.setUp(true);
        this.upArrow.setFocusTraversable(false);
        this.downArrow = new ArrowMenuItem(this);
        this.downArrow.setUp(false);
        this.downArrow.setFocusTraversable(false);
        this.getChildren().add((Object)this.itemsContainer);
        this.getChildren().add((Object)this.upArrow);
        this.getChildren().add((Object)this.downArrow);
        this.initialize();
        this.setUpBinds();
        this.updateItems();
        contextMenu.showingProperty().addListener((InvalidationListener)this.weakPopupShowingListener);
        if (Utils.isTwoLevelFocus()) {
            new TwoLevelFocusPopupBehavior((Node)this);
        }
    }

    public VBox getItemsContainer() {
        return this.itemsContainer;
    }

    int getCurrentFocusIndex() {
        return this.currentFocusedIndex;
    }

    void setCurrentFocusedIndex(int n) {
        if (n < this.itemsContainer.getChildren().size()) {
            this.currentFocusedIndex = n;
        }
    }

    private void updateItems() {
        if (this.itemsDirty) {
            this.updateVisualItems();
            this.itemsDirty = false;
        }
    }

    private void computeVisualMetrics() {
        Object object;
        this.maxRightWidth = 0.0;
        this.maxLabelWidth = 0.0;
        this.maxRowHeight = 0.0;
        this.maxGraphicWidth = 0.0;
        this.maxLeftWidth = 0.0;
        for (int i = 0; i < this.itemsContainer.getChildren().size(); ++i) {
            Node node = (Node)this.itemsContainer.getChildren().get(i);
            if (!(node instanceof MenuItemContainer) || !(object = (MenuItemContainer)((Object)this.itemsContainer.getChildren().get(i))).isVisible()) continue;
            double d = -1.0;
            Node node2 = ((MenuItemContainer)((Object)object)).left;
            if (node2 != null) {
                d = node2.getContentBias() == Orientation.VERTICAL ? this.snapSize(node2.prefHeight(-1.0)) : -1.0;
                this.maxLeftWidth = Math.max(this.maxLeftWidth, this.snapSize(node2.prefWidth(d)));
                this.maxRowHeight = Math.max(this.maxRowHeight, node2.prefHeight(-1.0));
            }
            if ((node2 = ((MenuItemContainer)((Object)object)).graphic) != null) {
                d = node2.getContentBias() == Orientation.VERTICAL ? this.snapSize(node2.prefHeight(-1.0)) : -1.0;
                this.maxGraphicWidth = Math.max(this.maxGraphicWidth, this.snapSize(node2.prefWidth(d)));
                this.maxRowHeight = Math.max(this.maxRowHeight, node2.prefHeight(-1.0));
            }
            if ((node2 = ((MenuItemContainer)((Object)object)).label) != null) {
                d = node2.getContentBias() == Orientation.VERTICAL ? this.snapSize(node2.prefHeight(-1.0)) : -1.0;
                this.maxLabelWidth = Math.max(this.maxLabelWidth, this.snapSize(node2.prefWidth(d)));
                this.maxRowHeight = Math.max(this.maxRowHeight, node2.prefHeight(-1.0));
            }
            if ((node2 = ((MenuItemContainer)((Object)object)).right) == null) continue;
            d = node2.getContentBias() == Orientation.VERTICAL ? this.snapSize(node2.prefHeight(-1.0)) : -1.0;
            this.maxRightWidth = Math.max(this.maxRightWidth, this.snapSize(node2.prefWidth(d)));
            this.maxRowHeight = Math.max(this.maxRowHeight, node2.prefHeight(-1.0));
        }
        double d = this.maxRightWidth + this.maxLabelWidth + this.maxGraphicWidth + this.maxLeftWidth;
        object = this.contextMenu.getOwnerWindow();
        if (object instanceof ContextMenu && this.contextMenu.getX() < object.getX() && this.oldWidth != d) {
            this.contextMenu.setX(this.contextMenu.getX() + this.oldWidth - d);
        }
        this.oldWidth = d;
    }

    private void updateVisualItems() {
        ObservableList observableList = this.itemsContainer.getChildren();
        this.disposeVisualItems();
        for (int i = 0; i < this.getItems().size(); ++i) {
            Object object;
            MenuItem menuItem = (MenuItem)this.getItems().get(i);
            if (menuItem instanceof CustomMenuItem && ((CustomMenuItem)menuItem).getContent() == null) continue;
            if (menuItem instanceof SeparatorMenuItem) {
                object = ((CustomMenuItem)menuItem).getContent();
                object.visibleProperty().bind((ObservableValue)menuItem.visibleProperty());
                observableList.add(object);
                object.getProperties().put(MenuItem.class, (Object)menuItem);
                continue;
            }
            object = new MenuItemContainer(menuItem);
            object.visibleProperty().bind((ObservableValue)menuItem.visibleProperty());
            observableList.add(object);
        }
        if (this.getItems().size() > 0) {
            MenuItem menuItem = (MenuItem)this.getItems().get(0);
            this.getProperties().put(Menu.class, (Object)menuItem.getParentMenu());
        }
        this.impl_reapplyCSS();
    }

    private void disposeVisualItems() {
        ObservableList observableList = this.itemsContainer.getChildren();
        int n = observableList.size();
        for (int i = 0; i < n; ++i) {
            Node node = (Node)observableList.get(i);
            if (!(node instanceof MenuItemContainer)) continue;
            MenuItemContainer menuItemContainer = (MenuItemContainer)node;
            menuItemContainer.visibleProperty().unbind();
            menuItemContainer.dispose();
        }
        observableList.clear();
    }

    public void dispose() {
        this.disposeBinds();
        this.disposeVisualItems();
        this.disposeContextMenu(this.submenu);
        this.submenu = null;
        this.openSubmenu = null;
        this.selectedBackground = null;
        if (this.contextMenu != null) {
            this.contextMenu.getItems().clear();
            this.contextMenu = null;
        }
    }

    public void disposeContextMenu(ContextMenu contextMenu) {
        if (contextMenu == null) {
            return;
        }
        Skin skin = contextMenu.getSkin();
        if (skin == null) {
            return;
        }
        ContextMenuContent contextMenuContent = (ContextMenuContent)skin.getNode();
        if (contextMenuContent == null) {
            return;
        }
        contextMenuContent.dispose();
    }

    protected void layoutChildren() {
        double d;
        if (this.itemsContainer.getChildren().size() == 0) {
            return;
        }
        double d2 = this.snappedLeftInset();
        double d3 = this.snappedTopInset();
        double d4 = this.getWidth() - d2 - this.snappedRightInset();
        double d5 = this.getHeight() - d3 - this.snappedBottomInset();
        double d6 = this.snapSize(this.getContentHeight());
        this.itemsContainer.resize(d4, d6);
        this.itemsContainer.relocate(d2, d3);
        if (this.isFirstShow && this.ty == 0.0) {
            this.upArrow.setVisible(false);
            this.isFirstShow = false;
        } else {
            this.upArrow.setVisible(this.ty < d3 && this.ty < 0.0);
        }
        this.downArrow.setVisible(this.ty + d6 > d3 + d5);
        this.clipRect.setX(0.0);
        this.clipRect.setY(0.0);
        this.clipRect.setWidth(d4);
        this.clipRect.setHeight(d5);
        if (this.upArrow.isVisible()) {
            d = this.snapSize(this.upArrow.prefHeight(-1.0));
            this.clipRect.setHeight(this.snapSize(this.clipRect.getHeight() - d));
            this.clipRect.setY(this.snapSize(this.clipRect.getY()) + d);
            this.upArrow.resize(this.snapSize(this.upArrow.prefWidth(-1.0)), d);
            this.positionInArea((Node)this.upArrow, d2, d3, d4, d, 0.0, HPos.CENTER, VPos.CENTER);
        }
        if (this.downArrow.isVisible()) {
            d = this.snapSize(this.downArrow.prefHeight(-1.0));
            this.clipRect.setHeight(this.snapSize(this.clipRect.getHeight()) - d);
            this.downArrow.resize(this.snapSize(this.downArrow.prefWidth(-1.0)), d);
            this.positionInArea((Node)this.downArrow, d2, d3 + d5 - d, d4, d, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    protected double computePrefWidth(double d) {
        this.computeVisualMetrics();
        double d2 = 0.0;
        if (this.itemsContainer.getChildren().size() == 0) {
            return 0.0;
        }
        for (Node node : this.itemsContainer.getChildren()) {
            if (!node.isVisible()) continue;
            d2 = Math.max(d2, this.snapSize(node.prefWidth(-1.0)));
        }
        return this.snappedLeftInset() + this.snapSize(d2) + this.snappedRightInset();
    }

    protected double computePrefHeight(double d) {
        if (this.itemsContainer.getChildren().size() == 0) {
            return 0.0;
        }
        double d2 = this.getScreenHeight();
        double d3 = this.getContentHeight();
        double d4 = this.snappedTopInset() + this.snapSize(d3) + this.snappedBottomInset();
        double d5 = d2 <= 0.0 ? d4 : Math.min(d4, d2);
        return d5;
    }

    protected double computeMinHeight(double d) {
        return 0.0;
    }

    protected double computeMaxHeight(double d) {
        return this.getScreenHeight();
    }

    private double getScreenHeight() {
        if (this.contextMenu == null || this.contextMenu.getOwnerWindow() == null || this.contextMenu.getOwnerWindow().getScene() == null) {
            return -1.0;
        }
        return this.snapSize(com.sun.javafx.util.Utils.getScreen((Object)this.contextMenu.getOwnerWindow().getScene().getRoot()).getVisualBounds().getHeight());
    }

    private double getContentHeight() {
        double d = 0.0;
        for (Node node : this.itemsContainer.getChildren()) {
            if (!node.isVisible()) continue;
            d += this.snapSize(node.prefHeight(-1.0));
        }
        return d;
    }

    private void ensureFocusedMenuItemIsVisible(Node node) {
        if (node == null) {
            return;
        }
        Bounds bounds = node.getBoundsInParent();
        Bounds bounds2 = this.clipRect.getBoundsInParent();
        if (bounds.getMaxY() >= bounds2.getMaxY()) {
            this.scroll(-bounds.getMaxY() + bounds2.getMaxY());
        } else if (bounds.getMinY() <= bounds2.getMinY()) {
            this.scroll(-bounds.getMinY() + bounds2.getMinY());
        }
    }

    protected ObservableList<MenuItem> getItems() {
        return this.contextMenu.getItems();
    }

    private int findFocusedIndex() {
        for (int i = 0; i < this.itemsContainer.getChildren().size(); ++i) {
            Node node = (Node)this.itemsContainer.getChildren().get(i);
            if (!node.isFocused()) continue;
            return i;
        }
        return -1;
    }

    private void initialize() {
        this.contextMenu.focusedProperty().addListener((observableValue, bl, bl2) -> {
            if (bl2.booleanValue()) {
                this.currentFocusedIndex = -1;
                this.requestFocus();
            }
        });
        this.contextMenu.addEventHandler(Menu.ON_SHOWN, event -> {
            for (Node node : this.itemsContainer.getChildren()) {
                MenuItem menuItem;
                if (!(node instanceof MenuItemContainer) || !"choice-box-menu-item".equals((menuItem = ((MenuItemContainer)node).item).getId()) || !((RadioMenuItem)menuItem).isSelected()) continue;
                node.requestFocus();
                break;
            }
        });
        this.setOnKeyPressed((EventHandler)new EventHandler<KeyEvent>(){

            public void handle(KeyEvent keyEvent) {
                Node node;
                switch (keyEvent.getCode()) {
                    case LEFT: {
                        if (ContextMenuContent.this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                            ContextMenuContent.this.processRightKey(keyEvent);
                            break;
                        }
                        ContextMenuContent.this.processLeftKey(keyEvent);
                        break;
                    }
                    case RIGHT: {
                        if (ContextMenuContent.this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                            ContextMenuContent.this.processLeftKey(keyEvent);
                            break;
                        }
                        ContextMenuContent.this.processRightKey(keyEvent);
                        break;
                    }
                    case CANCEL: {
                        keyEvent.consume();
                        break;
                    }
                    case ESCAPE: {
                        node = ContextMenuContent.this.contextMenu.getOwnerNode();
                        if (node instanceof MenuBarSkin.MenuBarButton) break;
                        ContextMenuContent.this.contextMenu.hide();
                        keyEvent.consume();
                        break;
                    }
                    case DOWN: {
                        ContextMenuContent.this.moveToNextSibling();
                        keyEvent.consume();
                        break;
                    }
                    case UP: {
                        ContextMenuContent.this.moveToPreviousSibling();
                        keyEvent.consume();
                        break;
                    }
                    case SPACE: 
                    case ENTER: {
                        ContextMenuContent.this.selectMenuItem();
                        keyEvent.consume();
                        break;
                    }
                }
                if (!keyEvent.isConsumed()) {
                    MenuBarSkin menuBarSkin;
                    node = ContextMenuContent.this.contextMenu.getOwnerNode();
                    if (node instanceof MenuItemContainer) {
                        Parent parent;
                        for (parent = node.getParent(); parent != null && !(parent instanceof ContextMenuContent); parent = parent.getParent()) {
                        }
                        if (parent instanceof ContextMenuContent) {
                            parent.getOnKeyPressed().handle((Event)keyEvent);
                        }
                    } else if (node instanceof MenuBarSkin.MenuBarButton && (menuBarSkin = ((MenuBarSkin.MenuBarButton)node).getMenuBarSkin()) != null && menuBarSkin.getKeyEventHandler() != null) {
                        menuBarSkin.getKeyEventHandler().handle((Event)keyEvent);
                    }
                }
            }
        });
        this.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
            double d = scrollEvent.getTextDeltaY();
            double d2 = scrollEvent.getDeltaY();
            if (this.downArrow.isVisible() && (d < 0.0 || d2 < 0.0) || this.upArrow.isVisible() && (d > 0.0 || d2 > 0.0)) {
                switch (scrollEvent.getTextDeltaYUnits()) {
                    case LINES: {
                        int n = this.findFocusedIndex();
                        if (n == -1) {
                            n = 0;
                        }
                        double d3 = ((Node)this.itemsContainer.getChildren().get(n)).prefHeight(-1.0);
                        this.scroll(d * d3);
                        break;
                    }
                    case PAGES: {
                        this.scroll(d * this.itemsContainer.getHeight());
                        break;
                    }
                    case NONE: {
                        this.scroll(d2);
                    }
                }
                scrollEvent.consume();
            }
        });
    }

    private void processLeftKey(KeyEvent keyEvent) {
        Menu menu;
        MenuItem menuItem;
        Node node;
        if (this.currentFocusedIndex != -1 && (node = (Node)this.itemsContainer.getChildren().get(this.currentFocusedIndex)) instanceof MenuItemContainer && (menuItem = ((MenuItemContainer)node).item) instanceof Menu && (menu = (Menu)menuItem) == this.openSubmenu && this.submenu != null && this.submenu.isShowing()) {
            this.hideSubmenu();
            keyEvent.consume();
        }
    }

    private void processRightKey(KeyEvent keyEvent) {
        MenuItem menuItem;
        Node node;
        if (this.currentFocusedIndex != -1 && (node = (Node)this.itemsContainer.getChildren().get(this.currentFocusedIndex)) instanceof MenuItemContainer && (menuItem = ((MenuItemContainer)node).item) instanceof Menu) {
            Menu menu = (Menu)menuItem;
            if (menu.isDisable()) {
                return;
            }
            this.selectedBackground = (MenuItemContainer)node;
            if (this.openSubmenu == menu && this.submenu != null && this.submenu.isShowing()) {
                return;
            }
            this.showMenu(menu);
            keyEvent.consume();
        }
    }

    private void showMenu(Menu menu) {
        menu.show();
        ContextMenuContent contextMenuContent = (ContextMenuContent)this.submenu.getSkin().getNode();
        if (contextMenuContent != null) {
            if (contextMenuContent.itemsContainer.getChildren().size() > 0) {
                ((Node)contextMenuContent.itemsContainer.getChildren().get(0)).requestFocus();
                contextMenuContent.currentFocusedIndex = 0;
            } else {
                contextMenuContent.requestFocus();
            }
        }
    }

    private void selectMenuItem() {
        Node node;
        if (this.currentFocusedIndex != -1 && (node = (Node)this.itemsContainer.getChildren().get(this.currentFocusedIndex)) instanceof MenuItemContainer) {
            MenuItem menuItem = ((MenuItemContainer)node).item;
            if (menuItem instanceof Menu) {
                Menu menu = (Menu)menuItem;
                if (this.openSubmenu != null) {
                    this.hideSubmenu();
                }
                if (menu.isDisable()) {
                    return;
                }
                this.selectedBackground = (MenuItemContainer)node;
                menu.show();
            } else {
                ((MenuItemContainer)node).doSelect();
            }
        }
    }

    private int findNext(int n) {
        Node node;
        int n2;
        for (n2 = n; n2 < this.itemsContainer.getChildren().size(); ++n2) {
            node = (Node)this.itemsContainer.getChildren().get(n2);
            if (!(node instanceof MenuItemContainer)) continue;
            return n2;
        }
        for (n2 = 0; n2 < n; ++n2) {
            node = (Node)this.itemsContainer.getChildren().get(n2);
            if (!(node instanceof MenuItemContainer)) continue;
            return n2;
        }
        return -1;
    }

    private void moveToNextSibling() {
        if (this.currentFocusedIndex != -1) {
            this.currentFocusedIndex = this.findNext(this.currentFocusedIndex + 1);
        } else if (this.currentFocusedIndex == -1 || this.currentFocusedIndex == this.itemsContainer.getChildren().size() - 1) {
            this.currentFocusedIndex = this.findNext(0);
        }
        if (this.currentFocusedIndex != -1) {
            Node node = (Node)this.itemsContainer.getChildren().get(this.currentFocusedIndex);
            this.selectedBackground = (MenuItemContainer)node;
            node.requestFocus();
            this.ensureFocusedMenuItemIsVisible(node);
        }
    }

    private int findPrevious(int n) {
        Node node;
        int n2;
        for (n2 = n; n2 >= 0; --n2) {
            node = (Node)this.itemsContainer.getChildren().get(n2);
            if (!(node instanceof MenuItemContainer)) continue;
            return n2;
        }
        for (n2 = this.itemsContainer.getChildren().size() - 1; n2 > n; --n2) {
            node = (Node)this.itemsContainer.getChildren().get(n2);
            if (!(node instanceof MenuItemContainer)) continue;
            return n2;
        }
        return -1;
    }

    private void moveToPreviousSibling() {
        if (this.currentFocusedIndex != -1) {
            this.currentFocusedIndex = this.findPrevious(this.currentFocusedIndex - 1);
        } else if (this.currentFocusedIndex == -1 || this.currentFocusedIndex == 0) {
            this.currentFocusedIndex = this.findPrevious(this.itemsContainer.getChildren().size() - 1);
        }
        if (this.currentFocusedIndex != -1) {
            Node node = (Node)this.itemsContainer.getChildren().get(this.currentFocusedIndex);
            this.selectedBackground = (MenuItemContainer)node;
            node.requestFocus();
            this.ensureFocusedMenuItemIsVisible(node);
        }
    }

    double getMenuYOffset(int n) {
        double d = 0.0;
        if (this.itemsContainer.getChildren().size() > n) {
            d = this.snappedTopInset();
            Node node = (Node)this.itemsContainer.getChildren().get(n);
            d += node.getLayoutY() + node.prefHeight(-1.0);
        }
        return d;
    }

    private void setUpBinds() {
        this.updateMenuShowingListeners((List<? extends MenuItem>)this.contextMenu.getItems(), true);
        this.contextMenu.getItems().addListener(this.contextMenuItemsListener);
    }

    private void disposeBinds() {
        this.updateMenuShowingListeners((List<? extends MenuItem>)this.contextMenu.getItems(), false);
        this.contextMenu.getItems().removeListener(this.contextMenuItemsListener);
    }

    private void updateMenuShowingListeners(List<? extends MenuItem> list, boolean bl) {
        for (MenuItem menuItem : list) {
            if (menuItem instanceof Menu) {
                Menu menu = (Menu)menuItem;
                if (bl) {
                    menu.showingProperty().addListener(this.menuShowingListener);
                } else {
                    menu.showingProperty().removeListener(this.menuShowingListener);
                }
            }
            if (bl) {
                menuItem.visibleProperty().addListener(this.menuItemVisibleListener);
                continue;
            }
            menuItem.visibleProperty().removeListener(this.menuItemVisibleListener);
        }
    }

    ContextMenu getSubMenu() {
        return this.submenu;
    }

    Menu getOpenSubMenu() {
        return this.openSubmenu;
    }

    private void createSubmenu() {
        if (this.submenu == null) {
            this.submenu = new ContextMenu();
            this.submenu.showingProperty().addListener((ChangeListener)new ChangeListener<Boolean>(){

                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean bl, Boolean bl2) {
                    if (!ContextMenuContent.this.submenu.isShowing()) {
                        for (Node node : ContextMenuContent.this.itemsContainer.getChildren()) {
                            Menu menu;
                            if (!(node instanceof MenuItemContainer) || !(((MenuItemContainer)node).item instanceof Menu) || !(menu = (Menu)((MenuItemContainer)node).item).isShowing()) continue;
                            menu.hide();
                        }
                    }
                }
            });
        }
    }

    private void showSubmenu(Menu menu) {
        this.openSubmenu = menu;
        this.createSubmenu();
        this.submenu.getItems().setAll((Collection)menu.getItems());
        this.submenu.show((Node)this.selectedBackground, Side.RIGHT, 0.0, 0.0);
    }

    private void hideSubmenu() {
        if (this.submenu == null) {
            return;
        }
        this.submenu.hide();
        this.openSubmenu = null;
        this.disposeContextMenu(this.submenu);
        this.submenu = null;
    }

    private void hideAllMenus(MenuItem menuItem) {
        Menu menu;
        if (this.contextMenu != null) {
            this.contextMenu.hide();
        }
        while ((menu = menuItem.getParentMenu()) != null) {
            menu.hide();
            menuItem = menu;
        }
        if (menuItem.getParentPopup() != null) {
            menuItem.getParentPopup().hide();
        }
    }

    void scroll(double d) {
        double d2 = this.ty + d;
        if (this.ty == d2) {
            return;
        }
        if (d2 > 0.0) {
            d2 = 0.0;
        }
        if (d < 0.0 && this.getHeight() - d2 > this.itemsContainer.getHeight() - this.downArrow.getHeight()) {
            d2 = this.getHeight() - this.itemsContainer.getHeight() - this.downArrow.getHeight();
        }
        this.ty = d2;
        this.itemsContainer.requestLayout();
    }

    public Styleable getStyleableParent() {
        return this.contextMenu;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return ContextMenuContent.getClassCssMetaData();
    }

    protected Label getLabelAt(int n) {
        return ((MenuItemContainer)((Object)this.itemsContainer.getChildren().get(n))).getLabel();
    }

    private class MenuLabel
    extends Label {
        public MenuLabel(MenuItem menuItem, MenuItemContainer menuItemContainer) {
            super(menuItem.getText());
            this.setMnemonicParsing(menuItem.isMnemonicParsing());
            this.setLabelFor((Node)menuItemContainer);
        }
    }

    public class MenuItemContainer
    extends Region {
        private final MenuItem item;
        private Node left;
        private Node graphic;
        private Node label;
        private Node right;
        private final MultiplePropertyChangeListenerHandler listener = new MultiplePropertyChangeListenerHandler((Callback<String, Void>)((Callback)string -> {
            this.handlePropertyChanged((String)string);
            return null;
        }));
        private EventHandler<MouseEvent> mouseEnteredEventHandler;
        private EventHandler<MouseEvent> mouseReleasedEventHandler;
        private EventHandler<ActionEvent> actionEventHandler;
        private EventHandler<MouseEvent> customMenuItemMouseClickedHandler;

        protected Label getLabel() {
            return (Label)this.label;
        }

        public MenuItem getItem() {
            return this.item;
        }

        public MenuItemContainer(MenuItem menuItem) {
            if (menuItem == null) {
                throw new NullPointerException("MenuItem can not be null");
            }
            this.getStyleClass().addAll((Collection)menuItem.getStyleClass());
            this.setId(menuItem.getId());
            this.setFocusTraversable(!(menuItem instanceof CustomMenuItem));
            this.item = menuItem;
            this.createChildren();
            if (menuItem instanceof Menu) {
                ReadOnlyBooleanProperty readOnlyBooleanProperty = ((Menu)menuItem).showingProperty();
                this.listener.registerChangeListener((ObservableValue<?>)readOnlyBooleanProperty, "MENU_SHOWING");
                this.pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, readOnlyBooleanProperty.get());
                this.setAccessibleRole(AccessibleRole.MENU);
            } else if (menuItem instanceof RadioMenuItem) {
                BooleanProperty booleanProperty = ((RadioMenuItem)menuItem).selectedProperty();
                this.listener.registerChangeListener((ObservableValue<?>)booleanProperty, "RADIO_ITEM_SELECTED");
                this.pseudoClassStateChanged(CHECKED_PSEUDOCLASS_STATE, booleanProperty.get());
                this.setAccessibleRole(AccessibleRole.RADIO_MENU_ITEM);
            } else if (menuItem instanceof CheckMenuItem) {
                BooleanProperty booleanProperty = ((CheckMenuItem)menuItem).selectedProperty();
                this.listener.registerChangeListener((ObservableValue<?>)booleanProperty, "CHECK_ITEM_SELECTED");
                this.pseudoClassStateChanged(CHECKED_PSEUDOCLASS_STATE, booleanProperty.get());
                this.setAccessibleRole(AccessibleRole.CHECK_MENU_ITEM);
            } else {
                this.setAccessibleRole(AccessibleRole.MENU_ITEM);
            }
            this.pseudoClassStateChanged(DISABLED_PSEUDOCLASS_STATE, menuItem.disableProperty().get());
            this.listener.registerChangeListener((ObservableValue<?>)menuItem.disableProperty(), "DISABLE");
            this.getProperties().put(MenuItem.class, (Object)menuItem);
            this.listener.registerChangeListener((ObservableValue<?>)menuItem.graphicProperty(), "GRAPHIC");
            this.actionEventHandler = actionEvent -> {
                if (menuItem instanceof Menu) {
                    Menu menu = (Menu)menuItem;
                    if (ContextMenuContent.this.openSubmenu == menu && ContextMenuContent.this.submenu.isShowing()) {
                        return;
                    }
                    if (ContextMenuContent.this.openSubmenu != null) {
                        ContextMenuContent.this.hideSubmenu();
                    }
                    ContextMenuContent.this.selectedBackground = this;
                    ContextMenuContent.this.showMenu(menu);
                } else {
                    this.doSelect();
                }
            };
            this.addEventHandler(ActionEvent.ACTION, this.actionEventHandler);
        }

        public void dispose() {
            Node node;
            if (this.item instanceof CustomMenuItem && (node = ((CustomMenuItem)this.item).getContent()) != null) {
                node.removeEventHandler(MouseEvent.MOUSE_CLICKED, this.customMenuItemMouseClickedHandler);
            }
            this.listener.dispose();
            this.removeEventHandler(ActionEvent.ACTION, this.actionEventHandler);
            if (this.label != null) {
                ((Label)this.label).textProperty().unbind();
            }
            this.left = null;
            this.graphic = null;
            this.label = null;
            this.right = null;
        }

        private void handlePropertyChanged(String string) {
            if ("MENU_SHOWING".equals(string)) {
                Menu menu = (Menu)this.item;
                this.pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, menu.isShowing());
            } else if ("RADIO_ITEM_SELECTED".equals(string)) {
                RadioMenuItem radioMenuItem = (RadioMenuItem)this.item;
                this.pseudoClassStateChanged(CHECKED_PSEUDOCLASS_STATE, radioMenuItem.isSelected());
            } else if ("CHECK_ITEM_SELECTED".equals(string)) {
                CheckMenuItem checkMenuItem = (CheckMenuItem)this.item;
                this.pseudoClassStateChanged(CHECKED_PSEUDOCLASS_STATE, checkMenuItem.isSelected());
            } else if ("DISABLE".equals(string)) {
                this.pseudoClassStateChanged(DISABLED_PSEUDOCLASS_STATE, this.item.isDisable());
            } else if ("GRAPHIC".equals(string)) {
                this.createChildren();
                ContextMenuContent.this.computeVisualMetrics();
            } else if ("ACCELERATOR".equals(string)) {
                this.updateAccelerator();
            } else if ("FOCUSED".equals(string) && this.isFocused()) {
                ContextMenuContent.this.currentFocusedIndex = ContextMenuContent.this.itemsContainer.getChildren().indexOf((Object)this);
            }
        }

        private void createChildren() {
            this.getChildren().clear();
            if (this.item instanceof CustomMenuItem) {
                this.createNodeMenuItemChildren((CustomMenuItem)this.item);
                if (this.mouseEnteredEventHandler == null) {
                    this.mouseEnteredEventHandler = mouseEvent -> this.requestFocus();
                } else {
                    this.removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
                }
                this.addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
            } else {
                StackPane stackPane;
                StackPane stackPane2;
                Node node = this.getLeftGraphic(this.item);
                if (node != null) {
                    stackPane2 = new StackPane();
                    stackPane2.getStyleClass().add((Object)"left-container");
                    stackPane2.getChildren().add((Object)node);
                    this.left = stackPane2;
                    this.getChildren().add((Object)this.left);
                    this.left.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                }
                if (this.item.getGraphic() != null) {
                    stackPane2 = this.item.getGraphic();
                    stackPane = new StackPane();
                    stackPane.getStyleClass().add((Object)"graphic-container");
                    stackPane.getChildren().add((Object)stackPane2);
                    this.graphic = stackPane;
                    this.getChildren().add((Object)this.graphic);
                }
                this.label = new MenuLabel(this.item, this);
                this.label.setStyle(this.item.getStyle());
                ((Label)this.label).textProperty().bind((ObservableValue)this.item.textProperty());
                this.label.setMouseTransparent(true);
                this.getChildren().add((Object)this.label);
                this.listener.unregisterChangeListener((ObservableValue<?>)this.focusedProperty());
                this.listener.registerChangeListener((ObservableValue<?>)this.focusedProperty(), "FOCUSED");
                if (this.item instanceof Menu) {
                    stackPane2 = new Region();
                    stackPane2.setMouseTransparent(true);
                    stackPane2.getStyleClass().add((Object)"arrow");
                    stackPane = new StackPane();
                    stackPane.setMaxWidth(Math.max(stackPane2.prefWidth(-1.0), 10.0));
                    stackPane.setMouseTransparent(true);
                    stackPane.getStyleClass().add((Object)"right-container");
                    stackPane.getChildren().add((Object)stackPane2);
                    this.right = stackPane;
                    this.getChildren().add((Object)stackPane);
                    if (this.mouseEnteredEventHandler == null) {
                        this.mouseEnteredEventHandler = mouseEvent -> {
                            Menu menu;
                            if (ContextMenuContent.this.openSubmenu != null && this.item != ContextMenuContent.this.openSubmenu) {
                                ContextMenuContent.this.hideSubmenu();
                            }
                            if ((menu = (Menu)this.item).isDisable()) {
                                return;
                            }
                            ContextMenuContent.this.selectedBackground = this;
                            menu.show();
                            this.requestFocus();
                        };
                    } else {
                        this.removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
                    }
                    if (this.mouseReleasedEventHandler == null) {
                        this.mouseReleasedEventHandler = mouseEvent -> this.item.fire();
                    } else {
                        this.removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
                    }
                    this.addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
                    this.addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
                } else {
                    this.listener.unregisterChangeListener((ObservableValue<?>)this.item.acceleratorProperty());
                    this.updateAccelerator();
                    if (this.mouseEnteredEventHandler == null) {
                        this.mouseEnteredEventHandler = mouseEvent -> {
                            if (ContextMenuContent.this.openSubmenu != null) {
                                ContextMenuContent.this.openSubmenu.hide();
                            }
                            this.requestFocus();
                        };
                    } else {
                        this.removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
                    }
                    if (this.mouseReleasedEventHandler == null) {
                        this.mouseReleasedEventHandler = mouseEvent -> this.doSelect();
                    } else {
                        this.removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
                    }
                    this.addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
                    this.addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
                    this.listener.registerChangeListener((ObservableValue<?>)this.item.acceleratorProperty(), "ACCELERATOR");
                }
            }
        }

        private void updateAccelerator() {
            if (this.item.getAccelerator() != null) {
                if (this.right != null) {
                    this.getChildren().remove((Object)this.right);
                }
                String string = this.item.getAccelerator().getDisplayText();
                this.right = new Label(string);
                this.right.setStyle(this.item.getStyle());
                this.right.getStyleClass().add((Object)"accelerator-text");
                this.getChildren().add((Object)this.right);
            } else {
                this.getChildren().remove((Object)this.right);
            }
        }

        void doSelect() {
            CheckMenuItem checkMenuItem;
            if (this.item.isDisable()) {
                return;
            }
            if (this.item instanceof CheckMenuItem) {
                checkMenuItem.setSelected(!(checkMenuItem = (CheckMenuItem)this.item).isSelected());
            } else if (this.item instanceof RadioMenuItem) {
                checkMenuItem.setSelected((checkMenuItem = (RadioMenuItem)this.item).getToggleGroup() != null ? true : !checkMenuItem.isSelected());
            }
            this.item.fire();
            if (this.item instanceof CustomMenuItem) {
                checkMenuItem = (CustomMenuItem)this.item;
                if (checkMenuItem.isHideOnClick()) {
                    ContextMenuContent.this.hideAllMenus(this.item);
                }
            } else {
                ContextMenuContent.this.hideAllMenus(this.item);
            }
        }

        private void createNodeMenuItemChildren(CustomMenuItem customMenuItem) {
            Node node = customMenuItem.getContent();
            this.getChildren().add((Object)node);
            this.customMenuItemMouseClickedHandler = mouseEvent -> {
                if (customMenuItem == null || customMenuItem.isDisable()) {
                    return;
                }
                customMenuItem.fire();
                if (customMenuItem.isHideOnClick()) {
                    ContextMenuContent.this.hideAllMenus((MenuItem)customMenuItem);
                }
            };
            node.addEventHandler(MouseEvent.MOUSE_CLICKED, this.customMenuItemMouseClickedHandler);
        }

        protected void layoutChildren() {
            double d;
            double d2 = this.prefHeight(-1.0);
            if (this.left != null) {
                d = this.snappedLeftInset();
                this.left.resize(this.left.prefWidth(-1.0), this.left.prefHeight(-1.0));
                this.positionInArea(this.left, d, 0.0, ContextMenuContent.this.maxLeftWidth, d2, 0.0, HPos.LEFT, VPos.CENTER);
            }
            if (this.graphic != null) {
                d = this.snappedLeftInset() + ContextMenuContent.this.maxLeftWidth;
                this.graphic.resize(this.graphic.prefWidth(-1.0), this.graphic.prefHeight(-1.0));
                this.positionInArea(this.graphic, d, 0.0, ContextMenuContent.this.maxGraphicWidth, d2, 0.0, HPos.LEFT, VPos.CENTER);
            }
            if (this.label != null) {
                d = this.snappedLeftInset() + ContextMenuContent.this.maxLeftWidth + ContextMenuContent.this.maxGraphicWidth;
                this.label.resize(this.label.prefWidth(-1.0), this.label.prefHeight(-1.0));
                this.positionInArea(this.label, d, 0.0, ContextMenuContent.this.maxLabelWidth, d2, 0.0, HPos.LEFT, VPos.CENTER);
            }
            if (this.right != null) {
                d = this.snappedLeftInset() + ContextMenuContent.this.maxLeftWidth + ContextMenuContent.this.maxGraphicWidth + ContextMenuContent.this.maxLabelWidth;
                this.right.resize(this.right.prefWidth(-1.0), this.right.prefHeight(-1.0));
                this.positionInArea(this.right, d, 0.0, ContextMenuContent.this.maxRightWidth, d2, 0.0, HPos.RIGHT, VPos.CENTER);
            }
            if (this.item instanceof CustomMenuItem) {
                Node node = ((CustomMenuItem)this.item).getContent();
                if (this.item instanceof SeparatorMenuItem) {
                    double d3 = this.prefWidth(-1.0) - (this.snappedLeftInset() + ContextMenuContent.this.maxGraphicWidth + this.snappedRightInset());
                    node.resize(d3, node.prefHeight(-1.0));
                    this.positionInArea(node, this.snappedLeftInset() + ContextMenuContent.this.maxGraphicWidth, 0.0, this.prefWidth(-1.0), d2, 0.0, HPos.LEFT, VPos.CENTER);
                } else {
                    node.resize(node.prefWidth(-1.0), node.prefHeight(-1.0));
                    this.positionInArea(node, this.snappedLeftInset(), 0.0, this.getWidth(), d2, 0.0, HPos.LEFT, VPos.CENTER);
                }
            }
        }

        protected double computePrefHeight(double d) {
            double d2 = 0.0;
            if (this.item instanceof CustomMenuItem || this.item instanceof SeparatorMenuItem) {
                d2 = this.getChildren().isEmpty() ? 0.0 : ((Node)this.getChildren().get(0)).prefHeight(-1.0);
            } else {
                d2 = Math.max(d2, this.left != null ? this.left.prefHeight(-1.0) : 0.0);
                d2 = Math.max(d2, this.graphic != null ? this.graphic.prefHeight(-1.0) : 0.0);
                d2 = Math.max(d2, this.label != null ? this.label.prefHeight(-1.0) : 0.0);
                d2 = Math.max(d2, this.right != null ? this.right.prefHeight(-1.0) : 0.0);
            }
            return this.snappedTopInset() + d2 + this.snappedBottomInset();
        }

        protected double computePrefWidth(double d) {
            double d2 = 0.0;
            if (this.item instanceof CustomMenuItem && !(this.item instanceof SeparatorMenuItem)) {
                d2 = this.snappedLeftInset() + ((CustomMenuItem)this.item).getContent().prefWidth(-1.0) + this.snappedRightInset();
            }
            return Math.max(d2, this.snappedLeftInset() + ContextMenuContent.this.maxLeftWidth + ContextMenuContent.this.maxGraphicWidth + ContextMenuContent.this.maxLabelWidth + ContextMenuContent.this.maxRightWidth + this.snappedRightInset());
        }

        private Node getLeftGraphic(MenuItem menuItem) {
            if (menuItem instanceof RadioMenuItem) {
                Region region = new Region();
                region.getStyleClass().add((Object)"radio");
                return region;
            }
            if (menuItem instanceof CheckMenuItem) {
                StackPane stackPane = new StackPane();
                stackPane.getStyleClass().add((Object)"check");
                return stackPane;
            }
            return null;
        }

        public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
            switch (accessibleAttribute) {
                case SELECTED: {
                    if (this.item instanceof CheckMenuItem) {
                        return ((CheckMenuItem)this.item).isSelected();
                    }
                    if (this.item instanceof RadioMenuItem) {
                        return ((RadioMenuItem)this.item).isSelected();
                    }
                    return false;
                }
                case ACCELERATOR: {
                    return this.item.getAccelerator();
                }
                case TEXT: {
                    String string;
                    String string2;
                    Object object;
                    String string3 = "";
                    if (this.graphic != null && (object = (String)this.graphic.queryAccessibleAttribute(AccessibleAttribute.TEXT, new Object[0])) != null) {
                        string3 = string3 + (String)object;
                    }
                    if ((object = this.getLabel()) != null && (string2 = (String)object.queryAccessibleAttribute(AccessibleAttribute.TEXT, new Object[0])) != null) {
                        string3 = string3 + string2;
                    }
                    if (this.item instanceof CustomMenuItem && (string2 = ((CustomMenuItem)this.item).getContent()) != null && (string = (String)string2.queryAccessibleAttribute(AccessibleAttribute.TEXT, new Object[0])) != null) {
                        string3 = string3 + string;
                    }
                    return string3;
                }
                case MNEMONIC: {
                    String string;
                    Label label = this.getLabel();
                    if (label != null && (string = (String)label.queryAccessibleAttribute(AccessibleAttribute.MNEMONIC, new Object[0])) != null) {
                        return string;
                    }
                    return null;
                }
                case DISABLED: {
                    return this.item.isDisable();
                }
                case SUBMENU: {
                    ContextMenuContent.this.createSubmenu();
                    if (ContextMenuContent.this.submenu.getSkin() == null) {
                        ContextMenuContent.this.submenu.impl_styleableGetNode().impl_processCSS(true);
                    }
                    ContextMenuContent contextMenuContent = (ContextMenuContent)ContextMenuContent.this.submenu.getSkin().getNode();
                    return contextMenuContent.itemsContainer;
                }
            }
            return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
        }

        public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
            switch (accessibleAction) {
                case SHOW_MENU: {
                    if (!(this.item instanceof Menu)) break;
                    Menu menu = (Menu)this.item;
                    if (menu.isShowing()) {
                        menu.hide();
                        break;
                    }
                    menu.show();
                    break;
                }
                case FIRE: {
                    this.doSelect();
                    break;
                }
                default: {
                    super.executeAccessibleAction(accessibleAction, new Object[0]);
                }
            }
        }
    }

    class ArrowMenuItem
    extends StackPane {
        private StackPane upDownArrow;
        private ContextMenuContent popupMenuContent;
        private boolean up = false;
        private Timeline scrollTimeline;

        public final boolean isUp() {
            return this.up;
        }

        public void setUp(boolean bl) {
            this.up = bl;
            this.upDownArrow.getStyleClass().setAll((Object[])new String[]{this.isUp() ? "menu-up-arrow" : "menu-down-arrow"});
        }

        public ArrowMenuItem(ContextMenuContent contextMenuContent2) {
            this.getStyleClass().setAll((Object[])new String[]{"scroll-arrow"});
            this.upDownArrow = new StackPane();
            this.popupMenuContent = contextMenuContent2;
            this.upDownArrow.setMouseTransparent(true);
            this.upDownArrow.getStyleClass().setAll((Object[])new String[]{this.isUp() ? "menu-up-arrow" : "menu-down-arrow"});
            this.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
                if (this.scrollTimeline != null && this.scrollTimeline.getStatus() != Animation.Status.STOPPED) {
                    return;
                }
                this.startTimeline();
            });
            this.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> this.stopTimeline());
            this.setVisible(false);
            this.setManaged(false);
            this.getChildren().add((Object)this.upDownArrow);
        }

        protected double computePrefWidth(double d) {
            return ContextMenuContent.this.itemsContainer.getWidth();
        }

        protected double computePrefHeight(double d) {
            return this.snappedTopInset() + this.upDownArrow.prefHeight(-1.0) + this.snappedBottomInset();
        }

        protected void layoutChildren() {
            double d = this.snapSize(this.upDownArrow.prefWidth(-1.0));
            double d2 = this.snapSize(this.upDownArrow.prefHeight(-1.0));
            this.upDownArrow.resize(d, d2);
            this.positionInArea((Node)this.upDownArrow, 0.0, 0.0, this.getWidth(), this.getHeight(), 0.0, HPos.CENTER, VPos.CENTER);
        }

        private void adjust() {
            if (this.up) {
                this.popupMenuContent.scroll(12.0);
            } else {
                this.popupMenuContent.scroll(-12.0);
            }
        }

        private void startTimeline() {
            this.scrollTimeline = new Timeline();
            this.scrollTimeline.setCycleCount(-1);
            KeyFrame keyFrame = new KeyFrame(Duration.millis((double)60.0), actionEvent -> this.adjust(), new KeyValue[0]);
            this.scrollTimeline.getKeyFrames().clear();
            this.scrollTimeline.getKeyFrames().add((Object)keyFrame);
            this.scrollTimeline.play();
        }

        private void stopTimeline() {
            this.scrollTimeline.stop();
            this.scrollTimeline = null;
        }
    }

    class MenuBox
    extends VBox {
        MenuBox() {
            this.setAccessibleRole(AccessibleRole.CONTEXT_MENU);
        }

        protected void layoutChildren() {
            double d = ContextMenuContent.this.ty;
            for (Node node : this.getChildren()) {
                if (!node.isVisible()) continue;
                double d2 = this.snapSize(node.prefHeight(-1.0));
                node.resize(this.snapSize(this.getWidth()), d2);
                node.relocate(this.snappedLeftInset(), d);
                d += d2;
            }
        }

        public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
            switch (accessibleAttribute) {
                case VISIBLE: {
                    return ContextMenuContent.this.contextMenu.isShowing();
                }
                case PARENT_MENU: {
                    return ContextMenuContent.this.contextMenu.getOwnerNode();
                }
            }
            return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
        }
    }

    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList<CssMetaData> arrayList = new ArrayList<CssMetaData>(Region.getClassCssMetaData());
            List list = Node.getClassCssMetaData();
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                CssMetaData cssMetaData = (CssMetaData)list.get(i);
                if (!"effect".equals(cssMetaData.getProperty())) continue;
                arrayList.add(cssMetaData);
                break;
            }
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

