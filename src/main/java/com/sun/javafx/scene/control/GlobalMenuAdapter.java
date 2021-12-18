/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.Property
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.event.Event
 *  javafx.event.EventTarget
 *  javafx.scene.control.CheckMenuItem
 *  javafx.scene.control.CustomMenuItem
 *  javafx.scene.control.Menu
 *  javafx.scene.control.MenuItem
 *  javafx.scene.control.RadioMenuItem
 *  javafx.scene.control.SeparatorMenuItem
 */
package com.sun.javafx.scene.control;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.menu.CheckMenuItemBase;
import com.sun.javafx.menu.CustomMenuItemBase;
import com.sun.javafx.menu.MenuBase;
import com.sun.javafx.menu.MenuItemBase;
import com.sun.javafx.menu.RadioMenuItemBase;
import com.sun.javafx.menu.SeparatorMenuItemBase;
import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class GlobalMenuAdapter
extends Menu
implements MenuBase {
    private Menu menu;
    private final ObservableList<MenuItemBase> items = new TrackableObservableList<MenuItemBase>(){

        @Override
        protected void onChanged(ListChangeListener.Change<MenuItemBase> change) {
        }
    };

    public static MenuBase adapt(Menu menu) {
        return new GlobalMenuAdapter(menu);
    }

    private GlobalMenuAdapter(Menu menu) {
        super(menu.getText());
        this.menu = menu;
        GlobalMenuAdapter.bindMenuItemProperties((MenuItem)this, (MenuItem)menu);
        menu.showingProperty().addListener(observable -> {
            if (menu.isShowing() && !this.isShowing()) {
                this.show();
            } else if (!menu.isShowing() && this.isShowing()) {
                this.hide();
            }
        });
        this.showingProperty().addListener(observable -> {
            if (this.isShowing() && !menu.isShowing()) {
                menu.show();
            } else if (!this.isShowing() && menu.isShowing()) {
                menu.hide();
            }
        });
        menu.getItems().addListener((ListChangeListener)new ListChangeListener<MenuItem>(){

            public void onChanged(ListChangeListener.Change<? extends MenuItem> change) {
                while (change.next()) {
                    int n;
                    int n2 = change.getFrom();
                    int n3 = change.getTo();
                    List list = change.getRemoved();
                    for (n = n2 + list.size() - 1; n >= n2; --n) {
                        GlobalMenuAdapter.this.items.remove(n);
                        GlobalMenuAdapter.this.getItems().remove(n);
                    }
                    for (n = n2; n < n3; ++n) {
                        MenuItem menuItem = (MenuItem)change.getList().get(n);
                        GlobalMenuAdapter.this.insertItem(menuItem, n);
                    }
                }
            }
        });
        for (MenuItem menuItem : menu.getItems()) {
            this.insertItem(menuItem, this.items.size());
        }
    }

    private void insertItem(MenuItem menuItem, int n) {
        MenuItemBase menuItemBase = menuItem instanceof Menu ? new GlobalMenuAdapter((Menu)menuItem) : (menuItem instanceof CheckMenuItem ? new CheckMenuItemAdapter((CheckMenuItem)menuItem) : (menuItem instanceof RadioMenuItem ? new RadioMenuItemAdapter((RadioMenuItem)menuItem) : (menuItem instanceof SeparatorMenuItem ? new SeparatorMenuItemAdapter((SeparatorMenuItem)menuItem) : (menuItem instanceof CustomMenuItem ? new CustomMenuItemAdapter((CustomMenuItem)menuItem) : new MenuItemAdapter(menuItem)))));
        this.items.add(n, (Object)menuItemBase);
        this.getItems().add(n, (Object)((MenuItem)menuItemBase));
    }

    @Override
    public final ObservableList<MenuItemBase> getItemsBase() {
        return this.items;
    }

    private static void bindMenuItemProperties(MenuItem menuItem, MenuItem menuItem2) {
        menuItem.idProperty().bind((ObservableValue)menuItem2.idProperty());
        menuItem.textProperty().bind((ObservableValue)menuItem2.textProperty());
        menuItem.graphicProperty().bind((ObservableValue)menuItem2.graphicProperty());
        menuItem.disableProperty().bind((ObservableValue)menuItem2.disableProperty());
        menuItem.visibleProperty().bind((ObservableValue)menuItem2.visibleProperty());
        menuItem.acceleratorProperty().bind((ObservableValue)menuItem2.acceleratorProperty());
        menuItem.mnemonicParsingProperty().bind((ObservableValue)menuItem2.mnemonicParsingProperty());
        menuItem.setOnAction(actionEvent -> menuItem2.fire());
    }

    @Override
    public void fireValidation() {
        Menu menu;
        if (this.menu.getOnMenuValidation() != null) {
            Event.fireEvent((EventTarget)this.menu, (Event)new Event(MENU_VALIDATION_EVENT));
        }
        if ((menu = this.menu.getParentMenu()) != null && menu.getOnMenuValidation() != null) {
            Event.fireEvent((EventTarget)menu, (Event)new Event(MenuItem.MENU_VALIDATION_EVENT));
        }
    }

    private static class CustomMenuItemAdapter
    extends CustomMenuItem
    implements CustomMenuItemBase {
        private CustomMenuItem menuItem;

        private CustomMenuItemAdapter(CustomMenuItem customMenuItem) {
            this.menuItem = customMenuItem;
            GlobalMenuAdapter.bindMenuItemProperties((MenuItem)this, (MenuItem)customMenuItem);
        }

        @Override
        public void fireValidation() {
            Menu menu;
            if (this.getOnMenuValidation() != null) {
                Event.fireEvent((EventTarget)this.menuItem, (Event)new Event(MENU_VALIDATION_EVENT));
            }
            if ((menu = this.menuItem.getParentMenu()).getOnMenuValidation() != null) {
                Event.fireEvent((EventTarget)menu, (Event)new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    private static class SeparatorMenuItemAdapter
    extends SeparatorMenuItem
    implements SeparatorMenuItemBase {
        private SeparatorMenuItem menuItem;

        private SeparatorMenuItemAdapter(SeparatorMenuItem separatorMenuItem) {
            this.menuItem = separatorMenuItem;
            GlobalMenuAdapter.bindMenuItemProperties((MenuItem)this, (MenuItem)separatorMenuItem);
        }

        @Override
        public void fireValidation() {
            Menu menu;
            if (this.getOnMenuValidation() != null) {
                Event.fireEvent((EventTarget)this.menuItem, (Event)new Event(MENU_VALIDATION_EVENT));
            }
            if ((menu = this.menuItem.getParentMenu()).getOnMenuValidation() != null) {
                Event.fireEvent((EventTarget)menu, (Event)new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    private static class RadioMenuItemAdapter
    extends RadioMenuItem
    implements RadioMenuItemBase {
        private RadioMenuItem menuItem;

        private RadioMenuItemAdapter(RadioMenuItem radioMenuItem) {
            super(radioMenuItem.getText());
            this.menuItem = radioMenuItem;
            GlobalMenuAdapter.bindMenuItemProperties((MenuItem)this, (MenuItem)radioMenuItem);
            this.selectedProperty().bindBidirectional((Property)radioMenuItem.selectedProperty());
        }

        @Override
        public void fireValidation() {
            Menu menu;
            if (this.getOnMenuValidation() != null) {
                Event.fireEvent((EventTarget)this.menuItem, (Event)new Event(MENU_VALIDATION_EVENT));
            }
            if ((menu = this.menuItem.getParentMenu()).getOnMenuValidation() != null) {
                Event.fireEvent((EventTarget)menu, (Event)new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    private static class CheckMenuItemAdapter
    extends CheckMenuItem
    implements CheckMenuItemBase {
        private CheckMenuItem menuItem;

        private CheckMenuItemAdapter(CheckMenuItem checkMenuItem) {
            super(checkMenuItem.getText());
            this.menuItem = checkMenuItem;
            GlobalMenuAdapter.bindMenuItemProperties((MenuItem)this, (MenuItem)checkMenuItem);
            this.selectedProperty().bindBidirectional((Property)checkMenuItem.selectedProperty());
        }

        @Override
        public void fireValidation() {
            Menu menu;
            if (this.getOnMenuValidation() != null) {
                Event.fireEvent((EventTarget)this.menuItem, (Event)new Event(MENU_VALIDATION_EVENT));
            }
            if ((menu = this.menuItem.getParentMenu()).getOnMenuValidation() != null) {
                Event.fireEvent((EventTarget)menu, (Event)new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    private static class MenuItemAdapter
    extends MenuItem
    implements MenuItemBase {
        private MenuItem menuItem;

        private MenuItemAdapter(MenuItem menuItem) {
            super(menuItem.getText());
            this.menuItem = menuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, menuItem);
        }

        @Override
        public void fireValidation() {
            Menu menu;
            if (this.menuItem.getOnMenuValidation() != null) {
                Event.fireEvent((EventTarget)this.menuItem, (Event)new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
            if ((menu = this.menuItem.getParentMenu()).getOnMenuValidation() != null) {
                Event.fireEvent((EventTarget)menu, (Event)new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }
}

