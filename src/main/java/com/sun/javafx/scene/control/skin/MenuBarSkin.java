/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.beans.InvalidationListener
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.Property
 *  javafx.beans.property.ReadOnlyProperty
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.beans.value.WeakChangeListener
 *  javafx.collections.ListChangeListener
 *  javafx.collections.MapChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.css.CssMetaData
 *  javafx.css.Styleable
 *  javafx.css.StyleableDoubleProperty
 *  javafx.css.StyleableObjectProperty
 *  javafx.css.StyleableProperty
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.event.WeakEventHandler
 *  javafx.geometry.Bounds
 *  javafx.geometry.NodeOrientation
 *  javafx.geometry.Pos
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.AccessibleRole
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.control.CustomMenuItem
 *  javafx.scene.control.Menu
 *  javafx.scene.control.MenuBar
 *  javafx.scene.control.MenuButton
 *  javafx.scene.control.MenuItem
 *  javafx.scene.control.SeparatorMenuItem
 *  javafx.scene.control.SkinBase
 *  javafx.scene.input.KeyCombination
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.HBox
 *  javafx.stage.Stage
 *  javafx.stage.Window
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.menu.MenuBase;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.control.GlobalMenuAdapter;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalEngine;
import com.sun.javafx.scene.traversal.TraverseListener;
import com.sun.javafx.stage.StageHelper;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MenuBarSkin
extends BehaviorSkinBase<MenuBar, BehaviorBase<MenuBar>>
implements TraverseListener {
    private final HBox container;
    private Menu openMenu;
    private MenuBarButton openMenuButton;
    private int focusedMenuIndex = -1;
    private static WeakHashMap<Stage, Reference<MenuBarSkin>> systemMenuMap;
    private static List<MenuBase> wrappedDefaultMenus;
    private static Stage currentMenuBarStage;
    private List<MenuBase> wrappedMenus;
    private WeakEventHandler<KeyEvent> weakSceneKeyEventHandler;
    private WeakEventHandler<MouseEvent> weakSceneMouseEventHandler;
    private WeakChangeListener<Boolean> weakWindowFocusListener;
    private WeakChangeListener<Window> weakWindowSceneListener;
    private EventHandler<KeyEvent> keyEventHandler;
    private EventHandler<MouseEvent> mouseEventHandler;
    private ChangeListener<Boolean> menuBarFocusedPropertyListener;
    private ChangeListener<Scene> sceneChangeListener;
    Runnable firstMenuRunnable = new Runnable(){

        @Override
        public void run() {
            if (MenuBarSkin.this.container.getChildren().size() > 0 && MenuBarSkin.this.container.getChildren().get(0) instanceof MenuButton) {
                if (MenuBarSkin.this.focusedMenuIndex != 0) {
                    MenuBarSkin.this.unSelectMenus();
                    MenuBarSkin.this.menuModeStart(0);
                    MenuBarSkin.this.openMenuButton = (MenuBarButton)((Object)MenuBarSkin.this.container.getChildren().get(0));
                    MenuBarSkin.this.openMenu = (Menu)((MenuBar)MenuBarSkin.this.getSkinnable()).getMenus().get(0);
                    MenuBarSkin.this.openMenuButton.setHover();
                } else {
                    MenuBarSkin.this.unSelectMenus();
                }
            }
        }
    };
    private boolean pendingDismiss = false;
    private EventHandler<ActionEvent> menuActionEventHandler = actionEvent -> {
        CustomMenuItem customMenuItem;
        if (actionEvent.getSource() instanceof CustomMenuItem && !(customMenuItem = (CustomMenuItem)actionEvent.getSource()).isHideOnClick()) {
            return;
        }
        this.unSelectMenus();
    };
    private ListChangeListener<MenuItem> menuItemListener = change -> {
        while (change.next()) {
            for (MenuItem menuItem : change.getAddedSubList()) {
                menuItem.addEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
            }
            for (MenuItem menuItem : change.getRemoved()) {
                menuItem.removeEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
            }
        }
    };
    private DoubleProperty spacing;
    private ObjectProperty<Pos> containerAlignment;
    private static final CssMetaData<MenuBar, Number> SPACING;
    private static final CssMetaData<MenuBar, Pos> ALIGNMENT;
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    public static void setDefaultSystemMenuBar(MenuBar menuBar) {
        if (Toolkit.getToolkit().getSystemMenu().isSupported()) {
            wrappedDefaultMenus.clear();
            for (Menu menu : menuBar.getMenus()) {
                wrappedDefaultMenus.add(GlobalMenuAdapter.adapt(menu));
            }
            menuBar.getMenus().addListener(change -> {
                wrappedDefaultMenus.clear();
                for (Menu menu : menuBar.getMenus()) {
                    wrappedDefaultMenus.add(GlobalMenuAdapter.adapt(menu));
                }
            });
        }
    }

    private static MenuBarSkin getMenuBarSkin(Stage stage) {
        if (systemMenuMap == null) {
            return null;
        }
        Reference<MenuBarSkin> reference = systemMenuMap.get((Object)stage);
        return reference == null ? null : reference.get();
    }

    private static void setSystemMenu(Stage stage) {
        List<MenuBase> list;
        if (stage != null && stage.isFocused()) {
            while (stage != null && stage.getOwner() instanceof Stage && ((list = MenuBarSkin.getMenuBarSkin(stage)) == null || ((MenuBarSkin)list).wrappedMenus == null)) {
                stage = (Stage)stage.getOwner();
            }
        } else {
            stage = null;
        }
        if (stage != currentMenuBarStage) {
            MenuBarSkin menuBarSkin;
            list = null;
            if (stage != null && (menuBarSkin = MenuBarSkin.getMenuBarSkin(stage)) != null) {
                list = menuBarSkin.wrappedMenus;
            }
            if (list == null) {
                list = wrappedDefaultMenus;
            }
            Toolkit.getToolkit().getSystemMenu().setMenus(list);
            currentMenuBarStage = stage;
        }
    }

    private static void initSystemMenuBar() {
        systemMenuMap = new WeakHashMap();
        InvalidationListener invalidationListener = observable -> MenuBarSkin.setSystemMenu((Stage)((ReadOnlyProperty)observable).getBean());
        ObservableList<Stage> observableList = StageHelper.getStages();
        for (Stage stage : observableList) {
            stage.focusedProperty().addListener(invalidationListener);
        }
        observableList.addListener(change -> {
            while (change.next()) {
                for (Stage stage : change.getRemoved()) {
                    stage.focusedProperty().removeListener(invalidationListener);
                }
                for (Stage stage : change.getAddedSubList()) {
                    stage.focusedProperty().addListener(invalidationListener);
                    MenuBarSkin.setSystemMenu(stage);
                }
            }
        });
    }

    EventHandler<KeyEvent> getKeyEventHandler() {
        return this.keyEventHandler;
    }

    public MenuBarSkin(MenuBar menuBar) {
        super(menuBar, new BehaviorBase<MenuBar>(menuBar, Collections.emptyList()));
        Object object2;
        this.container = new HBox();
        this.container.getStyleClass().add((Object)"container");
        this.getChildren().add((Object)this.container);
        this.keyEventHandler = keyEvent -> {
            if (this.openMenu != null) {
                switch (keyEvent.getCode()) {
                    case LEFT: {
                        boolean bl;
                        boolean bl2 = bl = menuBar.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
                        if (menuBar.getScene().getWindow().isFocused()) {
                            if (this.openMenu == null) {
                                return;
                            }
                            if (!this.openMenu.isShowing()) {
                                if (bl) {
                                    this.selectNextMenu();
                                } else {
                                    this.selectPrevMenu();
                                }
                                keyEvent.consume();
                                return;
                            }
                            if (bl) {
                                this.showNextMenu();
                            } else {
                                this.showPrevMenu();
                            }
                        }
                        keyEvent.consume();
                        break;
                    }
                    case RIGHT: {
                        boolean bl;
                        boolean bl3 = bl = menuBar.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
                        if (menuBar.getScene().getWindow().isFocused()) {
                            if (this.openMenu == null) {
                                return;
                            }
                            if (!this.openMenu.isShowing()) {
                                if (bl) {
                                    this.selectPrevMenu();
                                } else {
                                    this.selectNextMenu();
                                }
                                keyEvent.consume();
                                return;
                            }
                            if (bl) {
                                this.showPrevMenu();
                            } else {
                                this.showNextMenu();
                            }
                        }
                        keyEvent.consume();
                        break;
                    }
                    case DOWN: {
                        if (!menuBar.getScene().getWindow().isFocused() || this.focusedMenuIndex == -1 || this.openMenu == null) break;
                        this.openMenu = (Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex);
                        if (!this.isMenuEmpty((Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex))) {
                            this.openMenu.show();
                        }
                        keyEvent.consume();
                        break;
                    }
                    case ESCAPE: {
                        this.unSelectMenus();
                        keyEvent.consume();
                        break;
                    }
                }
            }
        };
        this.menuBarFocusedPropertyListener = (observableValue, bl, bl2) -> {
            if (bl2.booleanValue()) {
                this.unSelectMenus();
                this.menuModeStart(0);
                this.openMenuButton = (MenuBarButton)((Object)((Object)this.container.getChildren().get(0)));
                this.openMenu = (Menu)((MenuBar)this.getSkinnable()).getMenus().get(0);
                this.openMenuButton.setHover();
            } else {
                this.unSelectMenus();
            }
        };
        this.weakSceneKeyEventHandler = new WeakEventHandler(this.keyEventHandler);
        Utils.executeOnceWhenPropertyIsNonNull(menuBar.sceneProperty(), scene -> scene.addEventFilter(KeyEvent.KEY_PRESSED, this.weakSceneKeyEventHandler));
        this.mouseEventHandler = mouseEvent -> {
            if (!this.container.localToScreen(this.container.getLayoutBounds()).contains(mouseEvent.getScreenX(), mouseEvent.getScreenY())) {
                this.unSelectMenus();
            }
        };
        this.weakSceneMouseEventHandler = new WeakEventHandler(this.mouseEventHandler);
        Utils.executeOnceWhenPropertyIsNonNull(menuBar.sceneProperty(), scene -> scene.addEventFilter(MouseEvent.MOUSE_CLICKED, this.weakSceneMouseEventHandler));
        this.weakWindowFocusListener = new WeakChangeListener((observableValue, bl, bl2) -> {
            if (!bl2.booleanValue()) {
                this.unSelectMenus();
            }
        });
        Utils.executeOnceWhenPropertyIsNonNull(menuBar.sceneProperty(), scene -> {
            if (scene.getWindow() != null) {
                scene.getWindow().focusedProperty().addListener(this.weakWindowFocusListener);
            } else {
                ChangeListener changeListener = (observableValue, window, window2) -> {
                    if (window != null) {
                        window.focusedProperty().removeListener(this.weakWindowFocusListener);
                    }
                    if (window2 != null) {
                        window2.focusedProperty().addListener(this.weakWindowFocusListener);
                    }
                };
                this.weakWindowSceneListener = new WeakChangeListener(changeListener);
                scene.windowProperty().addListener(this.weakWindowSceneListener);
            }
        });
        this.rebuildUI();
        menuBar.getMenus().addListener(change -> this.rebuildUI());
        for (Object object2 : ((MenuBar)this.getSkinnable()).getMenus()) {
            object2.visibleProperty().addListener((observableValue, bl, bl2) -> this.rebuildUI());
        }
        if (Toolkit.getToolkit().getSystemMenu().isSupported()) {
            menuBar.useSystemMenuBarProperty().addListener(observable -> this.rebuildUI());
        }
        Iterator iterator = com.sun.javafx.util.Utils.isMac() ? KeyCombination.keyCombination((String)"ctrl+F10") : KeyCombination.keyCombination((String)"F10");
        Utils.executeOnceWhenPropertyIsNonNull(menuBar.sceneProperty(), arg_0 -> this.lambda$new$392((KeyCombination)iterator, arg_0));
        object2 = new ParentTraversalEngine((Parent)this.getSkinnable());
        ((TraversalEngine)object2).addTraverseListener(this);
        ((MenuBar)this.getSkinnable()).setImpl_traversalEngine((ParentTraversalEngine)object2);
        menuBar.sceneProperty().addListener((arg_0, arg_1, arg_2) -> this.lambda$new$393((KeyCombination)iterator, arg_0, arg_1, arg_2));
    }

    MenuButton getNodeForMenu(int n) {
        if (n < this.container.getChildren().size()) {
            return (MenuBarButton)((Object)this.container.getChildren().get(n));
        }
        return null;
    }

    int getFocusedMenuIndex() {
        return this.focusedMenuIndex;
    }

    private boolean menusContainCustomMenuItem() {
        for (Menu menu : ((MenuBar)this.getSkinnable()).getMenus()) {
            if (!this.menuContainsCustomMenuItem(menu)) continue;
            System.err.println("Warning: MenuBar ignored property useSystemMenuBar because menus contain CustomMenuItem");
            return true;
        }
        return false;
    }

    private boolean menuContainsCustomMenuItem(Menu menu) {
        for (MenuItem menuItem : menu.getItems()) {
            if (menuItem instanceof CustomMenuItem && !(menuItem instanceof SeparatorMenuItem)) {
                return true;
            }
            if (!(menuItem instanceof Menu) || !this.menuContainsCustomMenuItem((Menu)menuItem)) continue;
            return true;
        }
        return false;
    }

    private int getMenuBarButtonIndex(MenuBarButton menuBarButton) {
        for (int i = 0; i < this.container.getChildren().size(); ++i) {
            MenuBarButton menuBarButton2 = (MenuBarButton)((Object)this.container.getChildren().get(i));
            if (menuBarButton != menuBarButton2) continue;
            return i;
        }
        return -1;
    }

    private void updateActionListeners(Menu menu, boolean bl) {
        if (bl) {
            menu.getItems().addListener(this.menuItemListener);
        } else {
            menu.getItems().removeListener(this.menuItemListener);
        }
        for (MenuItem menuItem : menu.getItems()) {
            if (menuItem instanceof Menu) {
                this.updateActionListeners((Menu)menuItem, bl);
                continue;
            }
            if (bl) {
                menuItem.addEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
                continue;
            }
            menuItem.removeEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
        }
    }

    private void rebuildUI() {
        MenuBarButton menuBarButton;
        ((MenuBar)this.getSkinnable()).focusedProperty().removeListener(this.menuBarFocusedPropertyListener);
        for (Object object : ((MenuBar)this.getSkinnable()).getMenus()) {
            this.updateActionListeners((Menu)object, false);
        }
        for (Object object : this.container.getChildren()) {
            menuBarButton = (MenuBarButton)((Object)object);
            menuBarButton.hide();
            menuBarButton.menu.showingProperty().removeListener(menuBarButton.menuListener);
            menuBarButton.disableProperty().unbind();
            menuBarButton.textProperty().unbind();
            menuBarButton.graphicProperty().unbind();
            menuBarButton.styleProperty().unbind();
            menuBarButton.dispose();
            menuBarButton.setSkin(null);
            menuBarButton = null;
        }
        this.container.getChildren().clear();
        if (Toolkit.getToolkit().getSystemMenu().isSupported()) {
            Object object;
            Iterator iterator = ((MenuBar)this.getSkinnable()).getScene();
            if (iterator != null) {
                if (this.sceneChangeListener == null) {
                    this.sceneChangeListener = (observableValue, scene, scene2) -> {
                        Stage stage;
                        Object object;
                        if (scene != null && scene.getWindow() instanceof Stage && (object = MenuBarSkin.getMenuBarSkin(stage = (Stage)scene.getWindow())) == this) {
                            ((MenuBarSkin)object).wrappedMenus = null;
                            systemMenuMap.remove((Object)stage);
                            if (currentMenuBarStage == stage) {
                                currentMenuBarStage = null;
                                MenuBarSkin.setSystemMenu(stage);
                            }
                        }
                        if (scene2 != null && ((MenuBar)this.getSkinnable()).isUseSystemMenuBar() && !this.menusContainCustomMenuItem() && scene2.getWindow() instanceof Stage) {
                            stage = (Stage)scene2.getWindow();
                            if (systemMenuMap == null) {
                                MenuBarSkin.initSystemMenuBar();
                            }
                            if (this.wrappedMenus == null) {
                                this.wrappedMenus = new ArrayList<MenuBase>();
                                systemMenuMap.put(stage, new WeakReference<MenuBarSkin>(this));
                            } else {
                                this.wrappedMenus.clear();
                            }
                            for (Menu menu : ((MenuBar)this.getSkinnable()).getMenus()) {
                                this.wrappedMenus.add(GlobalMenuAdapter.adapt(menu));
                            }
                            currentMenuBarStage = null;
                            MenuBarSkin.setSystemMenu(stage);
                            ((MenuBar)this.getSkinnable()).requestLayout();
                            Platform.runLater(() -> ((MenuBar)this.getSkinnable()).requestLayout());
                        }
                    };
                    ((MenuBar)this.getSkinnable()).sceneProperty().addListener(this.sceneChangeListener);
                }
                this.sceneChangeListener.changed((ObservableValue)((MenuBar)this.getSkinnable()).sceneProperty(), (Object)iterator, (Object)iterator);
                if (currentMenuBarStage != null ? MenuBarSkin.getMenuBarSkin(currentMenuBarStage) == this : ((MenuBar)this.getSkinnable()).isUseSystemMenuBar()) {
                    return;
                }
            } else if (currentMenuBarStage != null && (object = MenuBarSkin.getMenuBarSkin(currentMenuBarStage)) == this) {
                MenuBarSkin.setSystemMenu(null);
            }
        }
        ((MenuBar)this.getSkinnable()).focusedProperty().addListener(this.menuBarFocusedPropertyListener);
        for (Object object : ((MenuBar)this.getSkinnable()).getMenus()) {
            if (!object.isVisible()) continue;
            menuBarButton = new MenuBarButton(this, (Menu)object);
            menuBarButton.setFocusTraversable(false);
            menuBarButton.getStyleClass().add((Object)"menu");
            menuBarButton.setStyle(object.getStyle());
            menuBarButton.getItems().setAll((Collection)object.getItems());
            this.container.getChildren().add((Object)menuBarButton);
            menuBarButton.menuListener = (arg_0, arg_1, arg_2) -> this.lambda$rebuildUI$398((Menu)object, menuBarButton, arg_0, arg_1, arg_2);
            menuBarButton.menu = object;
            object.showingProperty().addListener(menuBarButton.menuListener);
            menuBarButton.disableProperty().bindBidirectional((Property)object.disableProperty());
            menuBarButton.textProperty().bind((ObservableValue)object.textProperty());
            menuBarButton.graphicProperty().bind((ObservableValue)object.graphicProperty());
            menuBarButton.styleProperty().bind((ObservableValue)object.styleProperty());
            menuBarButton.getProperties().addListener(arg_0 -> MenuBarSkin.lambda$rebuildUI$399(menuBarButton, (Menu)object, arg_0));
            menuBarButton.showingProperty().addListener((arg_0, arg_1, arg_2) -> this.lambda$rebuildUI$400(menuBarButton, (Menu)object, arg_0, arg_1, arg_2));
            menuBarButton.setOnMousePressed(arg_0 -> this.lambda$rebuildUI$401(menuBarButton, (Menu)object, arg_0));
            menuBarButton.setOnMouseReleased(mouseEvent -> {
                if (menuBarButton.getScene().getWindow().isFocused() && this.pendingDismiss) {
                    this.resetOpenMenu();
                }
                this.pendingDismiss = false;
            });
            menuBarButton.setOnMouseEntered(arg_0 -> this.lambda$rebuildUI$403(menuBarButton, (Menu)object, arg_0));
            this.updateActionListeners((Menu)object, true);
        }
        ((MenuBar)this.getSkinnable()).requestLayout();
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
                    MenuBarSkin.this.container.setSpacing(d);
                }

                public Object getBean() {
                    return MenuBarSkin.this;
                }

                public String getName() {
                    return "spacing";
                }

                public CssMetaData<MenuBar, Number> getCssMetaData() {
                    return SPACING;
                }
            };
        }
        return this.spacing;
    }

    public final void setContainerAlignment(Pos pos) {
        this.containerAlignmentProperty().set((Object)pos);
    }

    public final Pos getContainerAlignment() {
        return this.containerAlignment == null ? Pos.TOP_LEFT : (Pos)this.containerAlignment.get();
    }

    public final ObjectProperty<Pos> containerAlignmentProperty() {
        if (this.containerAlignment == null) {
            this.containerAlignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT){

                public void invalidated() {
                    Pos pos = (Pos)this.get();
                    MenuBarSkin.this.container.setAlignment(pos);
                }

                public Object getBean() {
                    return MenuBarSkin.this;
                }

                public String getName() {
                    return "containerAlignment";
                }

                public CssMetaData<MenuBar, Pos> getCssMetaData() {
                    return ALIGNMENT;
                }
            };
        }
        return this.containerAlignment;
    }

    @Override
    public void dispose() {
        this.cleanUpSystemMenu();
        super.dispose();
    }

    private void cleanUpSystemMenu() {
        if (this.sceneChangeListener != null && this.getSkinnable() != null) {
            ((MenuBar)this.getSkinnable()).sceneProperty().removeListener(this.sceneChangeListener);
            this.sceneChangeListener = null;
        }
        if (currentMenuBarStage != null && MenuBarSkin.getMenuBarSkin(currentMenuBarStage) == this) {
            MenuBarSkin.setSystemMenu(null);
        }
        if (systemMenuMap != null) {
            Iterator<Map.Entry<Stage, Reference<MenuBarSkin>>> iterator = systemMenuMap.entrySet().iterator();
            while (iterator.hasNext()) {
                MenuBarSkin menuBarSkin;
                Map.Entry<Stage, Reference<MenuBarSkin>> entry = iterator.next();
                Reference<MenuBarSkin> reference = entry.getValue();
                MenuBarSkin menuBarSkin2 = menuBarSkin = reference != null ? reference.get() : null;
                if (menuBarSkin != null && menuBarSkin != this) continue;
                iterator.remove();
            }
        }
    }

    private boolean isMenuEmpty(Menu menu) {
        boolean bl = true;
        if (menu != null) {
            for (MenuItem menuItem : menu.getItems()) {
                if (menuItem == null || !menuItem.isVisible()) continue;
                bl = false;
            }
        }
        return bl;
    }

    private void resetOpenMenu() {
        if (this.openMenu != null) {
            this.openMenu.hide();
            this.openMenu = null;
            this.openMenuButton = (MenuBarButton)((Object)this.container.getChildren().get(this.focusedMenuIndex));
            this.openMenuButton.clearHover();
            this.openMenuButton = null;
            this.menuModeEnd();
        }
    }

    private void unSelectMenus() {
        this.clearMenuButtonHover();
        if (this.focusedMenuIndex == -1) {
            return;
        }
        if (this.openMenu != null) {
            this.openMenu.hide();
            this.openMenu = null;
        }
        if (this.openMenuButton != null) {
            this.openMenuButton.clearHover();
            this.openMenuButton = null;
        }
        this.menuModeEnd();
    }

    private void menuModeStart(int n) {
        if (this.focusedMenuIndex == -1) {
            SceneHelper.getSceneAccessor().setTransientFocusContainer(((MenuBar)this.getSkinnable()).getScene(), (Node)this.getSkinnable());
        }
        this.focusedMenuIndex = n;
    }

    private void menuModeEnd() {
        if (this.focusedMenuIndex != -1) {
            SceneHelper.getSceneAccessor().setTransientFocusContainer(((MenuBar)this.getSkinnable()).getScene(), null);
            ((MenuBar)this.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_NODE);
        }
        this.focusedMenuIndex = -1;
    }

    private void selectNextMenu() {
        Menu menu = this.findNextSibling();
        if (menu != null && this.focusedMenuIndex != -1) {
            this.openMenuButton = (MenuBarButton)((Object)this.container.getChildren().get(this.focusedMenuIndex));
            this.openMenuButton.setHover();
            this.openMenu = menu;
        }
    }

    private void selectPrevMenu() {
        Menu menu = this.findPreviousSibling();
        if (menu != null && this.focusedMenuIndex != -1) {
            this.openMenuButton = (MenuBarButton)((Object)this.container.getChildren().get(this.focusedMenuIndex));
            this.openMenuButton.setHover();
            this.openMenu = menu;
        }
    }

    private void showNextMenu() {
        Menu menu = this.findNextSibling();
        if (this.openMenu != null) {
            this.openMenu.hide();
        }
        this.openMenu = menu;
        if (!this.isMenuEmpty(menu)) {
            this.openMenu.show();
        }
    }

    private void showPrevMenu() {
        Menu menu = this.findPreviousSibling();
        if (this.openMenu != null) {
            this.openMenu.hide();
        }
        this.openMenu = menu;
        if (!this.isMenuEmpty(menu)) {
            this.openMenu.show();
        }
    }

    private Menu findPreviousSibling() {
        if (this.focusedMenuIndex == -1) {
            return null;
        }
        this.focusedMenuIndex = this.focusedMenuIndex == 0 ? this.container.getChildren().size() - 1 : --this.focusedMenuIndex;
        if (((Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex)).isDisable()) {
            return this.findPreviousSibling();
        }
        this.clearMenuButtonHover();
        return (Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex);
    }

    private Menu findNextSibling() {
        if (this.focusedMenuIndex == -1) {
            return null;
        }
        this.focusedMenuIndex = this.focusedMenuIndex == this.container.getChildren().size() - 1 ? 0 : ++this.focusedMenuIndex;
        if (((Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex)).isDisable()) {
            return this.findNextSibling();
        }
        this.clearMenuButtonHover();
        return (Menu)((MenuBar)this.getSkinnable()).getMenus().get(this.focusedMenuIndex);
    }

    private void updateFocusedIndex() {
        int n = 0;
        for (Node node : this.container.getChildren()) {
            if (node.isHover()) {
                this.focusedMenuIndex = n;
                return;
            }
            ++n;
        }
        this.menuModeEnd();
    }

    private void clearMenuButtonHover() {
        for (Node node : this.container.getChildren()) {
            if (!node.isHover()) continue;
            ((MenuBarButton)node).clearHover();
            return;
        }
    }

    @Override
    public void onTraverse(Node node, Bounds bounds) {
        if (this.openMenu != null) {
            this.openMenu.hide();
        }
        this.focusedMenuIndex = 0;
    }

    protected double snappedTopInset() {
        return this.container.getChildren().isEmpty() ? 0.0 : super.snappedTopInset();
    }

    protected double snappedBottomInset() {
        return this.container.getChildren().isEmpty() ? 0.0 : super.snappedBottomInset();
    }

    protected double snappedLeftInset() {
        return this.container.getChildren().isEmpty() ? 0.0 : super.snappedLeftInset();
    }

    protected double snappedRightInset() {
        return this.container.getChildren().isEmpty() ? 0.0 : super.snappedRightInset();
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        this.container.resizeRelocate(d, d2, d3, d4);
    }

    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        return this.container.minWidth(d) + this.snappedLeftInset() + this.snappedRightInset();
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        return this.container.prefWidth(d) + this.snappedLeftInset() + this.snappedRightInset();
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        return this.container.minHeight(d) + this.snappedTopInset() + this.snappedBottomInset();
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        return this.container.prefHeight(d) + this.snappedTopInset() + this.snappedBottomInset();
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        return ((MenuBar)this.getSkinnable()).prefHeight(-1.0);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return MenuBarSkin.getClassCssMetaData();
    }

    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_NODE: {
                return this.openMenuButton;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    private /* synthetic */ void lambda$rebuildUI$403(MenuBarButton menuBarButton, Menu menu, MouseEvent mouseEvent) {
        if (menuBarButton.getScene() != null && menuBarButton.getScene().getWindow() != null && menuBarButton.getScene().getWindow().isFocused()) {
            if (this.openMenuButton != null && this.openMenuButton != menuBarButton) {
                this.openMenuButton.clearHover();
                this.openMenuButton = null;
                this.openMenuButton = menuBarButton;
            }
            this.updateFocusedIndex();
            if (this.openMenu != null && this.openMenu != menu) {
                this.openMenu.hide();
                this.openMenu = menu;
                this.updateFocusedIndex();
                if (!this.isMenuEmpty(menu)) {
                    this.openMenu.show();
                }
            }
        }
    }

    private /* synthetic */ void lambda$rebuildUI$401(MenuBarButton menuBarButton, Menu menu, MouseEvent mouseEvent) {
        this.pendingDismiss = menuBarButton.isShowing();
        if (menuBarButton.getScene().getWindow().isFocused()) {
            this.openMenu = menu;
            if (!this.isMenuEmpty(menu)) {
                this.openMenu.show();
            }
            this.menuModeStart(this.getMenuBarButtonIndex(menuBarButton));
        }
    }

    private /* synthetic */ void lambda$rebuildUI$400(MenuBarButton menuBarButton, Menu menu, ObservableValue observableValue, Boolean bl, Boolean bl2) {
        if (bl2.booleanValue()) {
            if (this.openMenuButton != null && this.openMenuButton != menuBarButton) {
                this.openMenuButton.hide();
            }
            this.openMenuButton = menuBarButton;
            this.openMenu = menu;
            if (!menu.isShowing()) {
                menu.show();
            }
        }
    }

    private static /* synthetic */ void lambda$rebuildUI$399(MenuBarButton menuBarButton, Menu menu, MapChangeListener.Change change) {
        if (change.wasAdded() && "autoHide".equals(change.getKey())) {
            menuBarButton.getProperties().remove((Object)"autoHide");
            menu.hide();
        }
    }

    private /* synthetic */ void lambda$rebuildUI$398(Menu menu, MenuBarButton menuBarButton, ObservableValue observableValue, Boolean bl, Boolean bl2) {
        if (menu.isShowing()) {
            menuBarButton.show();
            this.menuModeStart(this.container.getChildren().indexOf((Object)menuBarButton));
        } else {
            menuBarButton.hide();
        }
    }

    private /* synthetic */ void lambda$new$393(KeyCombination keyCombination, ObservableValue observableValue, Scene scene, Scene scene2) {
        if (this.weakSceneKeyEventHandler != null && scene != null) {
            scene.removeEventFilter(KeyEvent.KEY_PRESSED, this.weakSceneKeyEventHandler);
        }
        if (this.weakSceneMouseEventHandler != null && scene != null) {
            scene.removeEventFilter(MouseEvent.MOUSE_CLICKED, this.weakSceneMouseEventHandler);
        }
        if (scene != null) {
            scene.getAccelerators().remove((Object)keyCombination);
        }
        if (scene2 != null) {
            scene2.getAccelerators().put((Object)keyCombination, (Object)this.firstMenuRunnable);
        }
    }

    private /* synthetic */ void lambda$new$392(KeyCombination keyCombination, Scene scene) {
        scene.getAccelerators().put((Object)keyCombination, (Object)this.firstMenuRunnable);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.isAltDown() && !keyEvent.isConsumed()) {
                this.firstMenuRunnable.run();
            }
        });
    }

    static {
        wrappedDefaultMenus = new ArrayList<MenuBase>();
        SPACING = new CssMetaData<MenuBar, Number>("-fx-spacing", SizeConverter.getInstance(), (Number)0.0){

            public boolean isSettable(MenuBar menuBar) {
                MenuBarSkin menuBarSkin = (MenuBarSkin)menuBar.getSkin();
                return menuBarSkin.spacing == null || !menuBarSkin.spacing.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(MenuBar menuBar) {
                MenuBarSkin menuBarSkin = (MenuBarSkin)menuBar.getSkin();
                return (StyleableProperty)menuBarSkin.spacingProperty();
            }
        };
        ALIGNMENT = new CssMetaData<MenuBar, Pos>("-fx-alignment", new EnumConverter<Pos>(Pos.class), Pos.TOP_LEFT){

            public boolean isSettable(MenuBar menuBar) {
                MenuBarSkin menuBarSkin = (MenuBarSkin)menuBar.getSkin();
                return menuBarSkin.containerAlignment == null || !menuBarSkin.containerAlignment.isBound();
            }

            public StyleableProperty<Pos> getStyleableProperty(MenuBar menuBar) {
                MenuBarSkin menuBarSkin = (MenuBarSkin)menuBar.getSkin();
                return (StyleableProperty)menuBarSkin.containerAlignmentProperty();
            }
        };
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

    static class MenuBarButton
    extends MenuButton {
        private ChangeListener<Boolean> menuListener;
        private MenuBarSkin menuBarSkin;
        private Menu menu;
        private final ListChangeListener<MenuItem> itemsListener;
        private final ListChangeListener<String> styleClassListener;

        public MenuBarButton(MenuBarSkin menuBarSkin, Menu menu) {
            super(menu.getText(), menu.getGraphic());
            this.menuBarSkin = menuBarSkin;
            this.setAccessibleRole(AccessibleRole.MENU);
            this.itemsListener = change -> {
                while (change.next()) {
                    this.getItems().removeAll((Collection)change.getRemoved());
                    this.getItems().addAll(change.getFrom(), (Collection)change.getAddedSubList());
                }
            };
            menu.getItems().addListener(this.itemsListener);
            this.styleClassListener = change -> {
                while (change.next()) {
                    for (int i = change.getFrom(); i < change.getTo(); ++i) {
                        this.getStyleClass().add(menu.getStyleClass().get(i));
                    }
                    for (String string : change.getRemoved()) {
                        this.getStyleClass().remove((Object)string);
                    }
                }
            };
            menu.getStyleClass().addListener(this.styleClassListener);
            this.idProperty().bind((ObservableValue)menu.idProperty());
        }

        public MenuBarSkin getMenuBarSkin() {
            return this.menuBarSkin;
        }

        private void clearHover() {
            this.setHover(false);
        }

        private void setHover() {
            this.setHover(true);
            ((MenuBar)this.menuBarSkin.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_NODE);
        }

        void dispose() {
            this.menu.getItems().removeListener(this.itemsListener);
            this.menu.getStyleClass().removeListener(this.styleClassListener);
            this.idProperty().unbind();
        }

        public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
            switch (accessibleAttribute) {
                case FOCUS_ITEM: {
                    return this;
                }
            }
            return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
        }
    }
}

