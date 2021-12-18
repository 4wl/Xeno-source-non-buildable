/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableList
 *  javafx.css.CssMetaData
 *  javafx.css.Styleable
 *  javafx.css.StyleableDoubleProperty
 *  javafx.css.StyleableObjectProperty
 *  javafx.css.StyleableProperty
 *  javafx.geometry.HPos
 *  javafx.geometry.Orientation
 *  javafx.geometry.Pos
 *  javafx.geometry.Side
 *  javafx.geometry.VPos
 *  javafx.scene.AccessibleAction
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.AccessibleRole
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.CustomMenuItem
 *  javafx.scene.control.MenuItem
 *  javafx.scene.control.Separator
 *  javafx.scene.control.SeparatorMenuItem
 *  javafx.scene.control.SkinBase
 *  javafx.scene.control.ToolBar
 *  javafx.scene.input.KeyCode
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.Pane
 *  javafx.scene.layout.StackPane
 *  javafx.scene.layout.VBox
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.ToolBarBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ToolBarSkin
extends BehaviorSkinBase<ToolBar, ToolBarBehavior> {
    private Pane box;
    private ToolBarOverflowMenu overflowMenu;
    private boolean overflow = false;
    private double previousWidth = 0.0;
    private double previousHeight = 0.0;
    private double savedPrefWidth = 0.0;
    private double savedPrefHeight = 0.0;
    private ObservableList<MenuItem> overflowMenuItems = FXCollections.observableArrayList();
    private boolean needsUpdate = false;
    private final ParentTraversalEngine engine;
    private DoubleProperty spacing;
    private ObjectProperty<Pos> boxAlignment;

    public ToolBarSkin(ToolBar toolBar) {
        super(toolBar, new ToolBarBehavior(toolBar));
        this.initialize();
        this.registerChangeListener((ObservableValue<?>)toolBar.orientationProperty(), "ORIENTATION");
        this.engine = new ParentTraversalEngine((Parent)this.getSkinnable(), new Algorithm(){

            private Node selectPrev(int n, TraversalContext traversalContext) {
                for (int i = n; i >= 0; --i) {
                    Node node;
                    Node node2 = (Node)ToolBarSkin.this.box.getChildren().get(i);
                    if (node2.isDisabled() || !node2.impl_isTreeVisible()) continue;
                    if (node2 instanceof Parent && (node = traversalContext.selectLastInParent((Parent)node2)) != null) {
                        return node;
                    }
                    if (!node2.isFocusTraversable()) continue;
                    return node2;
                }
                return null;
            }

            private Node selectNext(int n, TraversalContext traversalContext) {
                int n2 = ToolBarSkin.this.box.getChildren().size();
                for (int i = n; i < n2; ++i) {
                    Node node;
                    Node node2 = (Node)ToolBarSkin.this.box.getChildren().get(i);
                    if (node2.isDisabled() || !node2.impl_isTreeVisible()) continue;
                    if (node2.isFocusTraversable()) {
                        return node2;
                    }
                    if (!(node2 instanceof Parent) || (node = traversalContext.selectFirstInParent((Parent)node2)) == null) continue;
                    return node;
                }
                return null;
            }

            @Override
            public Node select(Node node, Direction direction, TraversalContext traversalContext) {
                Parent parent;
                int n;
                ObservableList observableList = ToolBarSkin.this.box.getChildren();
                if (node == ToolBarSkin.this.overflowMenu) {
                    if (direction.isForward()) {
                        return null;
                    }
                    Node node2 = this.selectPrev(observableList.size() - 1, traversalContext);
                    if (node2 != null) {
                        return node2;
                    }
                }
                if ((n = observableList.indexOf((Object)node)) < 0) {
                    parent = node.getParent();
                    while (!observableList.contains((Object)parent)) {
                        parent = parent.getParent();
                    }
                    Node node3 = traversalContext.selectInSubtree(parent, node, direction);
                    if (node3 != null) {
                        return node3;
                    }
                    n = observableList.indexOf((Object)node);
                    if (direction == Direction.NEXT) {
                        direction = Direction.NEXT_IN_LINE;
                    }
                }
                if (n >= 0) {
                    if (direction.isForward()) {
                        parent = this.selectNext(n + 1, traversalContext);
                        if (parent != null) {
                            return parent;
                        }
                        if (ToolBarSkin.this.overflow) {
                            ToolBarSkin.this.overflowMenu.requestFocus();
                            return ToolBarSkin.this.overflowMenu;
                        }
                    } else {
                        parent = this.selectPrev(n - 1, traversalContext);
                        if (parent != null) {
                            return parent;
                        }
                    }
                }
                return null;
            }

            @Override
            public Node selectFirst(TraversalContext traversalContext) {
                Node node = this.selectNext(0, traversalContext);
                if (node != null) {
                    return node;
                }
                if (ToolBarSkin.this.overflow) {
                    return ToolBarSkin.this.overflowMenu;
                }
                return null;
            }

            @Override
            public Node selectLast(TraversalContext traversalContext) {
                if (ToolBarSkin.this.overflow) {
                    return ToolBarSkin.this.overflowMenu;
                }
                return this.selectPrev(ToolBarSkin.this.box.getChildren().size() - 1, traversalContext);
            }
        });
        ((ToolBar)this.getSkinnable()).setImpl_traversalEngine(this.engine);
        toolBar.focusedProperty().addListener((observableValue, bl, bl2) -> {
            if (bl2.booleanValue()) {
                if (!this.box.getChildren().isEmpty()) {
                    ((Node)this.box.getChildren().get(0)).requestFocus();
                } else {
                    this.overflowMenu.requestFocus();
                }
            }
        });
        toolBar.getItems().addListener(change -> {
            while (change.next()) {
                for (Node node : change.getRemoved()) {
                    this.box.getChildren().remove((Object)node);
                }
                this.box.getChildren().addAll((Collection)change.getAddedSubList());
            }
            this.needsUpdate = true;
            ((ToolBar)this.getSkinnable()).requestLayout();
        });
    }

    public final void setSpacing(double d) {
        this.spacingProperty().set(this.snapSpace(d));
    }

    public final double getSpacing() {
        return this.spacing == null ? 0.0 : this.snapSpace(this.spacing.get());
    }

    public final DoubleProperty spacingProperty() {
        if (this.spacing == null) {
            this.spacing = new StyleableDoubleProperty(){

                protected void invalidated() {
                    double d = this.get();
                    if (((ToolBar)ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                        ((VBox)ToolBarSkin.this.box).setSpacing(d);
                    } else {
                        ((HBox)ToolBarSkin.this.box).setSpacing(d);
                    }
                }

                public Object getBean() {
                    return ToolBarSkin.this;
                }

                public String getName() {
                    return "spacing";
                }

                public CssMetaData<ToolBar, Number> getCssMetaData() {
                    return StyleableProperties.SPACING;
                }
            };
        }
        return this.spacing;
    }

    public final void setBoxAlignment(Pos pos) {
        this.boxAlignmentProperty().set((Object)pos);
    }

    public final Pos getBoxAlignment() {
        return this.boxAlignment == null ? Pos.TOP_LEFT : (Pos)this.boxAlignment.get();
    }

    public final ObjectProperty<Pos> boxAlignmentProperty() {
        if (this.boxAlignment == null) {
            this.boxAlignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT){

                public void invalidated() {
                    Pos pos = (Pos)this.get();
                    if (((ToolBar)ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                        ((VBox)ToolBarSkin.this.box).setAlignment(pos);
                    } else {
                        ((HBox)ToolBarSkin.this.box).setAlignment(pos);
                    }
                }

                public Object getBean() {
                    return ToolBarSkin.this;
                }

                public String getName() {
                    return "boxAlignment";
                }

                public CssMetaData<ToolBar, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }
            };
        }
        return this.boxAlignment;
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ORIENTATION".equals(string)) {
            this.initialize();
        }
    }

    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        return toolBar.getOrientation() == Orientation.VERTICAL ? this.computePrefWidth(-1.0, d2, d3, d4, d5) : this.snapSize(this.overflowMenu.prefWidth(-1.0)) + d5 + d3;
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        return toolBar.getOrientation() == Orientation.VERTICAL ? this.snapSize(this.overflowMenu.prefHeight(-1.0)) + d2 + d4 : this.computePrefHeight(-1.0, d2, d3, d4, d5);
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = 0.0;
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        if (toolBar.getOrientation() == Orientation.HORIZONTAL) {
            for (Node node : toolBar.getItems()) {
                d6 += this.snapSize(node.prefWidth(-1.0)) + this.getSpacing();
            }
            d6 -= this.getSpacing();
        } else {
            for (Node node : toolBar.getItems()) {
                d6 = Math.max(d6, this.snapSize(node.prefWidth(-1.0)));
            }
            if (toolBar.getItems().size() > 0) {
                this.savedPrefWidth = d6;
            } else {
                d6 = this.savedPrefWidth;
            }
        }
        return d5 + d6 + d3;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        double d6 = 0.0;
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        if (toolBar.getOrientation() == Orientation.VERTICAL) {
            for (Node node : toolBar.getItems()) {
                d6 += this.snapSize(node.prefHeight(-1.0)) + this.getSpacing();
            }
            d6 -= this.getSpacing();
        } else {
            for (Node node : toolBar.getItems()) {
                d6 = Math.max(d6, this.snapSize(node.prefHeight(-1.0)));
            }
            if (toolBar.getItems().size() > 0) {
                this.savedPrefHeight = d6;
            } else {
                d6 = this.savedPrefHeight;
            }
        }
        return d2 + d6 + d4;
    }

    protected double computeMaxWidth(double d, double d2, double d3, double d4, double d5) {
        return ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? this.snapSize(((ToolBar)this.getSkinnable()).prefWidth(-1.0)) : Double.MAX_VALUE;
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        return ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? Double.MAX_VALUE : this.snapSize(((ToolBar)this.getSkinnable()).prefHeight(-1.0));
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        if (toolBar.getOrientation() == Orientation.VERTICAL) {
            if (this.snapSize(toolBar.getHeight()) != this.previousHeight || this.needsUpdate) {
                ((VBox)this.box).setSpacing(this.getSpacing());
                ((VBox)this.box).setAlignment(this.getBoxAlignment());
                this.previousHeight = this.snapSize(toolBar.getHeight());
                this.addNodesToToolBar();
            }
        } else if (this.snapSize(toolBar.getWidth()) != this.previousWidth || this.needsUpdate) {
            ((HBox)this.box).setSpacing(this.getSpacing());
            ((HBox)this.box).setAlignment(this.getBoxAlignment());
            this.previousWidth = this.snapSize(toolBar.getWidth());
            this.addNodesToToolBar();
        }
        this.needsUpdate = false;
        double d5 = d3;
        double d6 = d4;
        if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            d6 -= this.overflow ? this.snapSize(this.overflowMenu.prefHeight(-1.0)) : 0.0;
        } else {
            d5 -= this.overflow ? this.snapSize(this.overflowMenu.prefWidth(-1.0)) : 0.0;
        }
        this.box.resize(d5, d6);
        this.positionInArea((Node)this.box, d, d2, d5, d6, 0.0, HPos.CENTER, VPos.CENTER);
        if (this.overflow) {
            double d7 = this.snapSize(this.overflowMenu.prefWidth(-1.0));
            double d8 = this.snapSize(this.overflowMenu.prefHeight(-1.0));
            double d9 = d;
            double d10 = d;
            if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                HPos hPos;
                if (d5 == 0.0) {
                    d5 = this.savedPrefWidth;
                }
                d9 = HPos.LEFT.equals((Object)(hPos = ((VBox)this.box).getAlignment().getHpos())) ? d + Math.abs((d5 - d7) / 2.0) : (HPos.RIGHT.equals((Object)hPos) ? this.snapSize(toolBar.getWidth()) - this.snappedRightInset() - d5 + Math.abs((d5 - d7) / 2.0) : d + Math.abs((this.snapSize(toolBar.getWidth()) - d + this.snappedRightInset() - d7) / 2.0));
                d10 = this.snapSize(toolBar.getHeight()) - d8 - d2;
            } else {
                VPos vPos;
                if (d6 == 0.0) {
                    d6 = this.savedPrefHeight;
                }
                d10 = VPos.TOP.equals((Object)(vPos = ((HBox)this.box).getAlignment().getVpos())) ? d2 + Math.abs((d6 - d8) / 2.0) : (VPos.BOTTOM.equals((Object)vPos) ? this.snapSize(toolBar.getHeight()) - this.snappedBottomInset() - d6 + Math.abs((d6 - d8) / 2.0) : d2 + Math.abs((d6 - d8) / 2.0));
                d9 = this.snapSize(toolBar.getWidth()) - d7 - this.snappedRightInset();
            }
            this.overflowMenu.resize(d7, d8);
            this.positionInArea((Node)this.overflowMenu, d9, d10, d7, d8, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    private void initialize() {
        this.box = ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? new VBox() : new HBox();
        this.box.getStyleClass().add((Object)"container");
        this.box.getChildren().addAll((Collection)((ToolBar)this.getSkinnable()).getItems());
        this.overflowMenu = new ToolBarOverflowMenu(this.overflowMenuItems);
        this.overflowMenu.setVisible(false);
        this.overflowMenu.setManaged(false);
        this.getChildren().clear();
        this.getChildren().add((Object)this.box);
        this.getChildren().add((Object)this.overflowMenu);
        this.previousWidth = 0.0;
        this.previousHeight = 0.0;
        this.savedPrefWidth = 0.0;
        this.savedPrefHeight = 0.0;
        this.needsUpdate = true;
        ((ToolBar)this.getSkinnable()).requestLayout();
    }

    private void addNodesToToolBar() {
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        double d = 0.0;
        d = ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? this.snapSize(toolBar.getHeight()) - this.snappedTopInset() - this.snappedBottomInset() + this.getSpacing() : this.snapSize(toolBar.getWidth()) - this.snappedLeftInset() - this.snappedRightInset() + this.getSpacing();
        double d2 = 0.0;
        boolean bl = false;
        for (Node node : ((ToolBar)this.getSkinnable()).getItems()) {
            d2 = ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? (d2 += this.snapSize(node.prefHeight(-1.0)) + this.getSpacing()) : (d2 += this.snapSize(node.prefWidth(-1.0)) + this.getSpacing());
            if (!(d2 > d)) continue;
            bl = true;
            break;
        }
        if (bl) {
            d = ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? (d -= this.snapSize(this.overflowMenu.prefHeight(-1.0))) : (d -= this.snapSize(this.overflowMenu.prefWidth(-1.0)));
            d -= this.getSpacing();
        }
        d2 = 0.0;
        this.overflowMenuItems.clear();
        this.box.getChildren().clear();
        Iterator iterator = ((ToolBar)this.getSkinnable()).getItems().iterator();
        while (iterator.hasNext()) {
            String string;
            CustomMenuItem customMenuItem;
            Node node;
            node = (Node)iterator.next();
            node.getStyleClass().remove((Object)"menu-item");
            node.getStyleClass().remove((Object)"custom-menu-item");
            d2 = ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? (d2 += this.snapSize(node.prefHeight(-1.0)) + this.getSpacing()) : (d2 += this.snapSize(node.prefWidth(-1.0)) + this.getSpacing());
            if (d2 <= d) {
                this.box.getChildren().add((Object)node);
                continue;
            }
            if (node.isFocused()) {
                if (!this.box.getChildren().isEmpty()) {
                    customMenuItem = this.engine.selectLast();
                    if (customMenuItem != null) {
                        customMenuItem.requestFocus();
                    }
                } else {
                    this.overflowMenu.requestFocus();
                }
            }
            if (node instanceof Separator) {
                this.overflowMenuItems.add((Object)new SeparatorMenuItem());
                continue;
            }
            customMenuItem = new CustomMenuItem(node);
            switch (string = node.getTypeSelector()) {
                case "Button": 
                case "Hyperlink": 
                case "Label": {
                    customMenuItem.setHideOnClick(true);
                    break;
                }
                case "CheckBox": 
                case "ChoiceBox": 
                case "ColorPicker": 
                case "ComboBox": 
                case "DatePicker": 
                case "MenuButton": 
                case "PasswordField": 
                case "RadioButton": 
                case "ScrollBar": 
                case "ScrollPane": 
                case "Slider": 
                case "SplitMenuButton": 
                case "SplitPane": 
                case "TextArea": 
                case "TextField": 
                case "ToggleButton": 
                case "ToolBar": {
                    customMenuItem.setHideOnClick(false);
                }
            }
            this.overflowMenuItems.add((Object)customMenuItem);
        }
        boolean bl2 = this.overflow = this.overflowMenuItems.size() > 0;
        if (!this.overflow && this.overflowMenu.isFocused() && (iterator = this.engine.selectLast()) != null) {
            iterator.requestFocus();
        }
        this.overflowMenu.setVisible(this.overflow);
        this.overflowMenu.setManaged(this.overflow);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return ToolBarSkin.getClassCssMetaData();
    }

    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case OVERFLOW_BUTTON: {
                return this.overflowMenu;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    protected void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        switch (accessibleAction) {
            case SHOW_MENU: {
                this.overflowMenu.fire();
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<ToolBar, Number> SPACING = new CssMetaData<ToolBar, Number>("-fx-spacing", SizeConverter.getInstance(), (Number)0.0){

            public boolean isSettable(ToolBar toolBar) {
                ToolBarSkin toolBarSkin = (ToolBarSkin)toolBar.getSkin();
                return toolBarSkin.spacing == null || !toolBarSkin.spacing.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(ToolBar toolBar) {
                ToolBarSkin toolBarSkin = (ToolBarSkin)toolBar.getSkin();
                return (StyleableProperty)toolBarSkin.spacingProperty();
            }
        };
        private static final CssMetaData<ToolBar, Pos> ALIGNMENT = new CssMetaData<ToolBar, Pos>("-fx-alignment", new EnumConverter<Pos>(Pos.class), Pos.TOP_LEFT){

            public boolean isSettable(ToolBar toolBar) {
                ToolBarSkin toolBarSkin = (ToolBarSkin)toolBar.getSkin();
                return toolBarSkin.boxAlignment == null || !toolBarSkin.boxAlignment.isBound();
            }

            public StyleableProperty<Pos> getStyleableProperty(ToolBar toolBar) {
                ToolBarSkin toolBarSkin = (ToolBarSkin)toolBar.getSkin();
                return (StyleableProperty)toolBarSkin.boxAlignmentProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList<Object> arrayList = new ArrayList<Object>(SkinBase.getClassCssMetaData());
            String string = ALIGNMENT.getProperty();
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                CssMetaData cssMetaData = (CssMetaData)arrayList.get(i);
                if (!string.equals(cssMetaData.getProperty())) continue;
                arrayList.remove((Object)cssMetaData);
            }
            arrayList.add(SPACING);
            arrayList.add(ALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    class ToolBarOverflowMenu
    extends StackPane {
        private StackPane downArrow;
        private ContextMenu popup;
        private ObservableList<MenuItem> menuItems;

        public ToolBarOverflowMenu(ObservableList<MenuItem> observableList) {
            this.getStyleClass().setAll((Object[])new String[]{"tool-bar-overflow-button"});
            this.setAccessibleRole(AccessibleRole.BUTTON);
            this.setAccessibleText(ControlResources.getString("Accessibility.title.ToolBar.OverflowButton"));
            this.setFocusTraversable(true);
            this.menuItems = observableList;
            this.downArrow = new StackPane();
            this.downArrow.getStyleClass().setAll((Object[])new String[]{"arrow"});
            this.downArrow.setOnMousePressed(mouseEvent -> this.fire());
            this.setOnKeyPressed(keyEvent -> {
                if (KeyCode.SPACE.equals((Object)keyEvent.getCode())) {
                    if (!this.popup.isShowing()) {
                        this.popup.getItems().clear();
                        this.popup.getItems().addAll(this.menuItems);
                        this.popup.show((Node)this.downArrow, Side.BOTTOM, 0.0, 0.0);
                    }
                    keyEvent.consume();
                } else if (KeyCode.ESCAPE.equals((Object)keyEvent.getCode())) {
                    if (this.popup.isShowing()) {
                        this.popup.hide();
                    }
                    keyEvent.consume();
                } else if (KeyCode.ENTER.equals((Object)keyEvent.getCode())) {
                    this.fire();
                    keyEvent.consume();
                }
            });
            this.visibleProperty().addListener((observableValue, bl, bl2) -> {
                if (bl2.booleanValue() && ToolBarSkin.this.box.getChildren().isEmpty()) {
                    this.setFocusTraversable(true);
                }
            });
            this.popup = new ContextMenu();
            this.setVisible(false);
            this.setManaged(false);
            this.getChildren().add((Object)this.downArrow);
        }

        private void fire() {
            if (this.popup.isShowing()) {
                this.popup.hide();
            } else {
                this.popup.getItems().clear();
                this.popup.getItems().addAll(this.menuItems);
                this.popup.show((Node)this.downArrow, Side.BOTTOM, 0.0, 0.0);
            }
        }

        protected double computePrefWidth(double d) {
            return this.snappedLeftInset() + this.snappedRightInset();
        }

        protected double computePrefHeight(double d) {
            return this.snappedTopInset() + this.snappedBottomInset();
        }

        protected void layoutChildren() {
            double d = this.snapSize(this.downArrow.prefWidth(-1.0));
            double d2 = this.snapSize(this.downArrow.prefHeight(-1.0));
            double d3 = (this.snapSize(this.getWidth()) - d) / 2.0;
            double d4 = (this.snapSize(this.getHeight()) - d2) / 2.0;
            if (((ToolBar)ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                this.downArrow.setRotate(0.0);
            }
            this.downArrow.resize(d, d2);
            this.positionInArea((Node)this.downArrow, d3, d4, d, d2, 0.0, HPos.CENTER, VPos.CENTER);
        }

        public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
            switch (accessibleAction) {
                case FIRE: {
                    this.fire();
                    break;
                }
                default: {
                    super.executeAccessibleAction(accessibleAction, new Object[0]);
                }
            }
        }
    }
}

