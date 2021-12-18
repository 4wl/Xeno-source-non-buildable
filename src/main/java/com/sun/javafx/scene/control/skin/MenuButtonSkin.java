/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.scene.Node
 *  javafx.scene.control.MenuButton
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.MenuButtonBehavior;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.MenuButtonSkinBase;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;

public class MenuButtonSkin
extends MenuButtonSkinBase<MenuButton, MenuButtonBehavior> {
    static final String AUTOHIDE = "autoHide";

    public MenuButtonSkin(MenuButton menuButton) {
        super(menuButton, new MenuButtonBehavior(menuButton));
        this.popup.setOnAutoHide((EventHandler)new EventHandler<Event>(){

            public void handle(Event event) {
                MenuButton menuButton = (MenuButton)MenuButtonSkin.this.getSkinnable();
                if (!menuButton.getProperties().containsKey((Object)MenuButtonSkin.AUTOHIDE)) {
                    menuButton.getProperties().put((Object)MenuButtonSkin.AUTOHIDE, (Object)Boolean.TRUE);
                }
            }
        });
        this.popup.setOnShown(windowEvent -> {
            ContextMenuContent contextMenuContent = (ContextMenuContent)this.popup.getSkin().getNode();
            if (contextMenuContent != null) {
                contextMenuContent.requestFocus();
            }
        });
        if (menuButton.getOnAction() == null) {
            menuButton.setOnAction(actionEvent -> menuButton.show());
        }
        this.label.setLabelFor((Node)menuButton);
    }
}

