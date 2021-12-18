/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.control.Pagination
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.PaginationSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Pagination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class PaginationBehavior
extends BehaviorBase<Pagination> {
    private static final String LEFT = "Left";
    private static final String RIGHT = "Right";
    protected static final List<KeyBinding> PAGINATION_BINDINGS = new ArrayList<KeyBinding>();

    @Override
    protected String matchActionForEvent(KeyEvent keyEvent) {
        String string = super.matchActionForEvent(keyEvent);
        if (string != null) {
            if (keyEvent.getCode() == KeyCode.LEFT) {
                if (((Pagination)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    string = RIGHT;
                }
            } else if (keyEvent.getCode() == KeyCode.RIGHT && ((Pagination)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                string = LEFT;
            }
        }
        return string;
    }

    @Override
    protected void callAction(String string) {
        if (LEFT.equals(string)) {
            PaginationSkin paginationSkin = (PaginationSkin)((Pagination)this.getControl()).getSkin();
            paginationSkin.selectPrevious();
        } else if (RIGHT.equals(string)) {
            PaginationSkin paginationSkin = (PaginationSkin)((Pagination)this.getControl()).getSkin();
            paginationSkin.selectNext();
        } else {
            super.callAction(string);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        Pagination pagination = (Pagination)this.getControl();
        pagination.requestFocus();
    }

    public PaginationBehavior(Pagination pagination) {
        super(pagination, PAGINATION_BINDINGS);
    }

    static {
        PAGINATION_BINDINGS.add(new KeyBinding(KeyCode.LEFT, LEFT));
        PAGINATION_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, RIGHT));
    }
}

