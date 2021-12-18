/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.Node
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.Menu
 *  javafx.scene.control.PopupControl
 *  javafx.scene.control.Skin
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.VBox
 *  javafx.stage.WindowEvent
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TwoLevelFocusPopupBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.EmbeddedTextContextMenuContent;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.Collection;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

public class ContextMenuSkin
implements Skin<ContextMenu> {
    private ContextMenu popupMenu;
    private final Region root;
    private TwoLevelFocusPopupBehavior tlFocus;
    private final EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>(){

        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getEventType() != KeyEvent.KEY_PRESSED) {
                return;
            }
            if (!ContextMenuSkin.this.root.isFocused()) {
                return;
            }
            KeyCode keyCode = keyEvent.getCode();
            switch (keyCode) {
                case ENTER: 
                case SPACE: {
                    ContextMenuSkin.this.popupMenu.hide();
                    return;
                }
            }
        }
    };

    public ContextMenuSkin(final ContextMenu contextMenu) {
        this.popupMenu = contextMenu;
        contextMenu.addEventHandler(Menu.ON_SHOWN, (EventHandler)new EventHandler<Event>(){

            public void handle(Event event) {
                Node node = contextMenu.getSkin().getNode();
                if (node != null) {
                    node.requestFocus();
                    if (node instanceof ContextMenuContent) {
                        VBox vBox = ((ContextMenuContent)node).getItemsContainer();
                        vBox.notifyAccessibleAttributeChanged(AccessibleAttribute.VISIBLE);
                    }
                }
                ContextMenuSkin.this.root.addEventHandler(KeyEvent.KEY_PRESSED, ContextMenuSkin.this.keyListener);
            }
        });
        contextMenu.addEventHandler(Menu.ON_HIDDEN, (EventHandler)new EventHandler<Event>(){

            public void handle(Event event) {
                Node node = contextMenu.getSkin().getNode();
                if (node != null) {
                    node.requestFocus();
                }
                ContextMenuSkin.this.root.removeEventHandler(KeyEvent.KEY_PRESSED, ContextMenuSkin.this.keyListener);
            }
        });
        contextMenu.addEventFilter(WindowEvent.WINDOW_HIDING, (EventHandler)new EventHandler<Event>(){

            public void handle(Event event) {
                Node node = contextMenu.getSkin().getNode();
                if (node instanceof ContextMenuContent) {
                    VBox vBox = ((ContextMenuContent)node).getItemsContainer();
                    vBox.notifyAccessibleAttributeChanged(AccessibleAttribute.VISIBLE);
                }
            }
        });
        this.root = BehaviorSkinBase.IS_TOUCH_SUPPORTED && contextMenu.getStyleClass().contains((Object)"text-input-context-menu") ? new EmbeddedTextContextMenuContent(contextMenu) : new ContextMenuContent(contextMenu);
        this.root.idProperty().bind((ObservableValue)contextMenu.idProperty());
        this.root.styleProperty().bind((ObservableValue)contextMenu.styleProperty());
        this.root.getStyleClass().addAll((Collection)contextMenu.getStyleClass());
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusPopupBehavior((PopupControl)contextMenu);
        }
    }

    public ContextMenu getSkinnable() {
        return this.popupMenu;
    }

    public Node getNode() {
        return this.root;
    }

    public void dispose() {
        this.root.idProperty().unbind();
        this.root.styleProperty().unbind();
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
    }
}

