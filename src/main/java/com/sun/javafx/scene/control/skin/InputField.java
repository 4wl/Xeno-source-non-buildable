/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.IntegerProperty
 *  javafx.beans.property.IntegerPropertyBase
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.ObjectPropertyBase
 *  javafx.beans.property.SimpleBooleanProperty
 *  javafx.beans.property.StringProperty
 *  javafx.beans.property.StringPropertyBase
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.scene.control.Control
 */
package com.sun.javafx.scene.control.skin;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;

abstract class InputField
extends Control {
    public static final int DEFAULT_PREF_COLUMN_COUNT = 12;
    private BooleanProperty editable = new SimpleBooleanProperty((Object)this, "editable", true);
    private StringProperty promptText = new StringPropertyBase(""){

        protected void invalidated() {
            String string = this.get();
            if (string != null && string.contains("\n")) {
                string = string.replace("\n", "");
                this.set(string);
            }
        }

        public Object getBean() {
            return InputField.this;
        }

        public String getName() {
            return "promptText";
        }
    };
    private IntegerProperty prefColumnCount = new IntegerPropertyBase(12){
        private int oldValue;
        {
            this.oldValue = this.get();
        }

        protected void invalidated() {
            int n = this.get();
            if (n < 0) {
                if (this.isBound()) {
                    this.unbind();
                }
                this.set(this.oldValue);
                throw new IllegalArgumentException("value cannot be negative.");
            }
            this.oldValue = n;
        }

        public Object getBean() {
            return InputField.this;
        }

        public String getName() {
            return "prefColumnCount";
        }
    };
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>(){

        protected void invalidated() {
            InputField.this.setEventHandler(ActionEvent.ACTION, (EventHandler)this.get());
        }

        public Object getBean() {
            return InputField.this;
        }

        public String getName() {
            return "onAction";
        }
    };

    public final boolean isEditable() {
        return this.editable.getValue();
    }

    public final void setEditable(boolean bl) {
        this.editable.setValue(Boolean.valueOf(bl));
    }

    public final BooleanProperty editableProperty() {
        return this.editable;
    }

    public final StringProperty promptTextProperty() {
        return this.promptText;
    }

    public final String getPromptText() {
        return (String)this.promptText.get();
    }

    public final void setPromptText(String string) {
        this.promptText.set((Object)string);
    }

    public final IntegerProperty prefColumnCountProperty() {
        return this.prefColumnCount;
    }

    public final int getPrefColumnCount() {
        return this.prefColumnCount.getValue();
    }

    public final void setPrefColumnCount(int n) {
        this.prefColumnCount.setValue((Number)n);
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return this.onAction;
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return (EventHandler)this.onActionProperty().get();
    }

    public final void setOnAction(EventHandler<ActionEvent> eventHandler) {
        this.onActionProperty().set(eventHandler);
    }

    public InputField() {
        this.getStyleClass().setAll((Object[])new String[]{"input-field"});
    }
}

