/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventType
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.Node
 *  javafx.scene.control.DateCell
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.traversal.Direction;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class DateCellBehavior
extends BehaviorBase<DateCell> {
    protected static final List<KeyBinding> DATE_CELL_BINDINGS = new ArrayList<KeyBinding>();

    public DateCellBehavior(DateCell dateCell) {
        super(dateCell, DATE_CELL_BINDINGS);
    }

    @Override
    public void callAction(String string) {
        DateCell dateCell = (DateCell)this.getControl();
        DatePickerContent datePickerContent = this.findDatePickerContent((Node)dateCell);
        if (datePickerContent != null) {
            switch (string) {
                case "SelectDate": {
                    datePickerContent.selectDayCell(dateCell);
                    break;
                }
                default: {
                    super.callAction(string);
                }
            }
            return;
        }
        super.callAction(string);
    }

    @Override
    public void traverse(Node node, Direction direction) {
        boolean bl = node.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
        switch (direction) {
            case UP: 
            case DOWN: 
            case LEFT: 
            case RIGHT: {
                DatePickerContent datePickerContent;
                if (!(node instanceof DateCell) || (datePickerContent = this.findDatePickerContent(node)) == null) break;
                DateCell dateCell = (DateCell)node;
                switch (direction) {
                    case UP: {
                        datePickerContent.goToDayCell(dateCell, -1, ChronoUnit.WEEKS, true);
                        break;
                    }
                    case DOWN: {
                        datePickerContent.goToDayCell(dateCell, 1, ChronoUnit.WEEKS, true);
                        break;
                    }
                    case LEFT: {
                        datePickerContent.goToDayCell(dateCell, bl ? 1 : -1, ChronoUnit.DAYS, true);
                        break;
                    }
                    case RIGHT: {
                        datePickerContent.goToDayCell(dateCell, bl ? -1 : 1, ChronoUnit.DAYS, true);
                    }
                }
                return;
            }
        }
        super.traverse(node, direction);
    }

    protected DatePickerContent findDatePickerContent(Node node) {
        Node node2 = node;
        while ((node2 = node2.getParent()) != null && !(node2 instanceof DatePickerContent)) {
        }
        return (DatePickerContent)node2;
    }

    static {
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.ENTER, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "SelectDate"));
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.SPACE, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "SelectDate"));
    }
}

