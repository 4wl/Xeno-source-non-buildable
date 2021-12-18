/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.oracle.jrockit.jfr.ContentType
 *  com.oracle.jrockit.jfr.EventDefinition
 *  com.oracle.jrockit.jfr.EventToken
 *  com.oracle.jrockit.jfr.TimedEvent
 *  com.oracle.jrockit.jfr.ValueDefinition
 */
package com.sun.javafx.logging;

import com.oracle.jrockit.jfr.ContentType;
import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.TimedEvent;
import com.oracle.jrockit.jfr.ValueDefinition;

@EventDefinition(path="javafx/input", name="JavaFX Input", description="JavaFX input event", stacktrace=false, thread=true)
public class JFRInputEvent
extends TimedEvent {
    @ValueDefinition(name="inputType", description="Input event type", contentType=ContentType.None)
    private String input;

    public JFRInputEvent(EventToken eventToken) {
        super(eventToken);
    }

    public String getInput() {
        return this.input;
    }

    public void setInput(String string) {
        this.input = string;
    }
}

