/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.ObjectPropertyBase
 *  javafx.beans.property.SimpleObjectProperty
 *  javafx.event.EventHandler
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.Node
 *  javafx.scene.control.Control
 *  javafx.scene.control.Skin
 *  javafx.scene.input.KeyEvent
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.FXVKSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyEvent;

public class FXVK
extends Control {
    private final ObjectProperty<EventHandler<KeyEvent>> onAction = new SimpleObjectProperty((Object)this, "onAction");
    static final String[] VK_TYPE_NAMES = new String[]{"text", "numeric", "url", "email"};
    public static final String VK_TYPE_PROP_KEY = "vkType";
    String[] chars;
    private ObjectProperty<Node> attachedNode;
    static FXVK vk;
    private static final String DEFAULT_STYLE_CLASS = "fxvk";

    public final void setOnAction(EventHandler<KeyEvent> eventHandler) {
        this.onAction.set(eventHandler);
    }

    public final EventHandler<KeyEvent> getOnAction() {
        return (EventHandler)this.onAction.get();
    }

    public final ObjectProperty<EventHandler<KeyEvent>> onActionProperty() {
        return this.onAction;
    }

    public FXVK() {
        this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.getStyleClass().add((Object)DEFAULT_STYLE_CLASS);
    }

    final ObjectProperty<Node> attachedNodeProperty() {
        if (this.attachedNode == null) {
            this.attachedNode = new ObjectPropertyBase<Node>(){

                public Object getBean() {
                    return FXVK.this;
                }

                public String getName() {
                    return "attachedNode";
                }
            };
        }
        return this.attachedNode;
    }

    final void setAttachedNode(Node node) {
        this.attachedNodeProperty().setValue((Object)node);
    }

    final Node getAttachedNode() {
        return this.attachedNode == null ? null : (Node)this.attachedNode.getValue();
    }

    public static void init(Node node) {
        if (vk != null) {
            return;
        }
        vk = new FXVK();
        FXVKSkin fXVKSkin = new FXVKSkin(vk);
        vk.setSkin((Skin)fXVKSkin);
        fXVKSkin.prerender(node);
    }

    public static void attach(Node node) {
        if (vk == null) {
            vk = new FXVK();
            vk.setSkin((Skin)new FXVKSkin(vk));
        }
        vk.setAttachedNode(node);
    }

    public static void detach() {
        if (vk != null) {
            vk.setAttachedNode(null);
        }
    }

    protected Skin<?> createDefaultSkin() {
        return new FXVKSkin(this);
    }

    public static enum Type {
        TEXT,
        NUMERIC,
        EMAIL;

    }
}

