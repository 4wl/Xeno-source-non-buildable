/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.beans.property.ReadOnlyObjectProperty
 *  javafx.collections.ObservableList
 *  javafx.collections.ObservableMap
 *  javafx.event.Event
 *  javafx.event.EventTarget
 *  javafx.scene.Node
 *  javafx.scene.Scene
 *  javafx.scene.control.CheckMenuItem
 *  javafx.scene.control.Control
 *  javafx.scene.control.Menu
 *  javafx.scene.control.MenuItem
 *  javafx.scene.control.RadioMenuItem
 *  javafx.scene.control.Tab
 *  javafx.scene.control.TabPane
 *  javafx.scene.control.TableColumn
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TreeTableColumn
 */
package com.sun.javafx.scene.control;

import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Control;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeTableColumn;

public class ControlAcceleratorSupport {
    public static void addAcceleratorsIntoScene(ObservableList<MenuItem> observableList, Tab tab) {
        ControlAcceleratorSupport.addAcceleratorsIntoScene(observableList, (Object)tab);
    }

    public static void addAcceleratorsIntoScene(ObservableList<MenuItem> observableList, TableColumnBase<?, ?> tableColumnBase) {
        ControlAcceleratorSupport.addAcceleratorsIntoScene(observableList, tableColumnBase);
    }

    public static void addAcceleratorsIntoScene(final ObservableList<MenuItem> observableList, final Node node) {
        if (observableList == null) {
            return;
        }
        if (node == null) {
            throw new IllegalArgumentException("Anchor cannot be null");
        }
        Scene scene = node.getScene();
        if (scene == null) {
            node.sceneProperty().addListener(new InvalidationListener(){

                public void invalidated(Observable observable) {
                    Scene scene = node.getScene();
                    if (scene != null) {
                        node.sceneProperty().removeListener((InvalidationListener)this);
                        ControlAcceleratorSupport.doAcceleratorInstall((ObservableList<MenuItem>)observableList, scene);
                    }
                }
            });
        } else {
            ControlAcceleratorSupport.doAcceleratorInstall(observableList, scene);
        }
    }

    private static void addAcceleratorsIntoScene(final ObservableList<MenuItem> observableList, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Anchor cannot be null");
        }
        final ReadOnlyObjectProperty<? extends Control> readOnlyObjectProperty = ControlAcceleratorSupport.getControlProperty(object);
        Control control = (Control)readOnlyObjectProperty.get();
        if (control == null) {
            readOnlyObjectProperty.addListener(new InvalidationListener(){

                public void invalidated(Observable observable) {
                    Control control = (Control)readOnlyObjectProperty.get();
                    if (control != null) {
                        readOnlyObjectProperty.removeListener((InvalidationListener)this);
                        ControlAcceleratorSupport.addAcceleratorsIntoScene((ObservableList<MenuItem>)observableList, (Node)control);
                    }
                }
            });
        } else {
            ControlAcceleratorSupport.addAcceleratorsIntoScene(observableList, (Node)control);
        }
    }

    private static void doAcceleratorInstall(ObservableList<MenuItem> observableList, Scene scene) {
        observableList.addListener(change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    ControlAcceleratorSupport.removeAcceleratorsFromScene((List<? extends MenuItem>)change.getRemoved(), scene);
                }
                if (!change.wasAdded()) continue;
                ControlAcceleratorSupport.doAcceleratorInstall(change.getAddedSubList(), scene);
            }
        });
        ControlAcceleratorSupport.doAcceleratorInstall(observableList, scene);
    }

    private static void doAcceleratorInstall(List<? extends MenuItem> list, Scene scene) {
        for (MenuItem menuItem : list) {
            if (menuItem instanceof Menu) {
                ControlAcceleratorSupport.doAcceleratorInstall((ObservableList<MenuItem>)((Menu)menuItem).getItems(), scene);
                continue;
            }
            if (menuItem.getAccelerator() != null) {
                ObservableMap observableMap = scene.getAccelerators();
                Runnable runnable = () -> {
                    Menu menu;
                    if (menuItem.getOnMenuValidation() != null) {
                        Event.fireEvent((EventTarget)menuItem, (Event)new Event(MenuItem.MENU_VALIDATION_EVENT));
                    }
                    if ((menu = menuItem.getParentMenu()) != null && menu.getOnMenuValidation() != null) {
                        Event.fireEvent((EventTarget)menu, (Event)new Event(MenuItem.MENU_VALIDATION_EVENT));
                    }
                    if (!menuItem.isDisable()) {
                        if (menuItem instanceof RadioMenuItem) {
                            ((RadioMenuItem)menuItem).setSelected(!((RadioMenuItem)menuItem).isSelected());
                        } else if (menuItem instanceof CheckMenuItem) {
                            ((CheckMenuItem)menuItem).setSelected(!((CheckMenuItem)menuItem).isSelected());
                        }
                        menuItem.fire();
                    }
                };
                observableMap.put(menuItem.getAccelerator(), runnable);
            }
            menuItem.acceleratorProperty().addListener((observableValue, keyCombination, keyCombination2) -> {
                ObservableMap observableMap = scene.getAccelerators();
                Runnable runnable = (Runnable)observableMap.remove(keyCombination);
                if (keyCombination2 != null) {
                    observableMap.put(keyCombination2, runnable);
                }
            });
        }
    }

    public static void removeAcceleratorsFromScene(List<? extends MenuItem> list, Tab tab) {
        TabPane tabPane = tab.getTabPane();
        if (tabPane == null) {
            return;
        }
        Scene scene = tabPane.getScene();
        ControlAcceleratorSupport.removeAcceleratorsFromScene(list, scene);
    }

    public static void removeAcceleratorsFromScene(List<? extends MenuItem> list, TableColumnBase<?, ?> tableColumnBase) {
        ReadOnlyObjectProperty<? extends Control> readOnlyObjectProperty = ControlAcceleratorSupport.getControlProperty(tableColumnBase);
        if (readOnlyObjectProperty == null) {
            return;
        }
        Control control = (Control)readOnlyObjectProperty.get();
        if (control == null) {
            return;
        }
        Scene scene = control.getScene();
        ControlAcceleratorSupport.removeAcceleratorsFromScene(list, scene);
    }

    public static void removeAcceleratorsFromScene(List<? extends MenuItem> list, Node node) {
        Scene scene = node.getScene();
        ControlAcceleratorSupport.removeAcceleratorsFromScene(list, scene);
    }

    public static void removeAcceleratorsFromScene(List<? extends MenuItem> list, Scene scene) {
        if (scene == null) {
            return;
        }
        for (MenuItem menuItem : list) {
            if (menuItem instanceof Menu) {
                ControlAcceleratorSupport.removeAcceleratorsFromScene((List<? extends MenuItem>)((Menu)menuItem).getItems(), scene);
                continue;
            }
            ObservableMap observableMap = scene.getAccelerators();
            observableMap.remove((Object)menuItem.getAccelerator());
        }
    }

    private static ReadOnlyObjectProperty<? extends Control> getControlProperty(Object object) {
        if (object instanceof TableColumn) {
            return ((TableColumn)object).tableViewProperty();
        }
        if (object instanceof TreeTableColumn) {
            return ((TreeTableColumn)object).treeTableViewProperty();
        }
        if (object instanceof Tab) {
            return ((Tab)object).tabPaneProperty();
        }
        return null;
    }
}

