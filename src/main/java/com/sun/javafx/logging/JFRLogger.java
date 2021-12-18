/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.oracle.jrockit.jfr.EventToken
 *  com.oracle.jrockit.jfr.FlightRecorder
 *  com.oracle.jrockit.jfr.Producer
 */
package com.sun.javafx.logging;

import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.FlightRecorder;
import com.oracle.jrockit.jfr.Producer;
import com.sun.javafx.logging.JFRInputEvent;
import com.sun.javafx.logging.JFRPulseEvent;
import com.sun.javafx.logging.Logger;

class JFRLogger
extends Logger {
    private static final String PRODUCER_URI = "http://www.oracle.com/technetwork/java/javafx/index.html";
    private static JFRLogger jfrLogger;
    private final Producer producer = new Producer("JavaFX producer", "JavaFX producer.", "http://www.oracle.com/technetwork/java/javafx/index.html");
    private final EventToken pulseEventToken = this.producer.addEvent(JFRPulseEvent.class);
    private final EventToken inputEventToken = this.producer.addEvent(JFRInputEvent.class);
    private final ThreadLocal<JFRPulseEvent> curPhaseEvent;
    private final ThreadLocal<JFRInputEvent> curInputEvent;
    private int pulseNumber;
    private int fxPulseNumber;
    private int renderPulseNumber;
    private Thread fxThread;

    private JFRLogger() throws Exception {
        this.producer.register();
        this.curPhaseEvent = new ThreadLocal(){

            public JFRPulseEvent initialValue() {
                return new JFRPulseEvent(JFRLogger.this.pulseEventToken);
            }
        };
        this.curInputEvent = new ThreadLocal(){

            public JFRInputEvent initialValue() {
                return new JFRInputEvent(JFRLogger.this.inputEventToken);
            }
        };
    }

    public static JFRLogger getInstance() {
        if (jfrLogger == null) {
            try {
                Class<?> class_ = Class.forName("com.oracle.jrockit.jfr.FlightRecorder");
                if (class_ != null && FlightRecorder.isActive()) {
                    jfrLogger = new JFRLogger();
                }
            }
            catch (Exception exception) {
                jfrLogger = null;
            }
        }
        return jfrLogger;
    }

    @Override
    public void pulseStart() {
        ++this.pulseNumber;
        this.fxPulseNumber = this.pulseNumber;
        if (this.fxThread == null) {
            this.fxThread = Thread.currentThread();
        }
        this.newPhase("Pulse start");
    }

    @Override
    public void pulseEnd() {
        this.newPhase(null);
        this.fxPulseNumber = 0;
    }

    @Override
    public void renderStart() {
        this.renderPulseNumber = this.fxPulseNumber;
    }

    @Override
    public void renderEnd() {
        this.newPhase(null);
        this.renderPulseNumber = 0;
    }

    @Override
    public void newPhase(String string) {
        if (this.pulseEventToken == null) {
            return;
        }
        JFRPulseEvent jFRPulseEvent = this.curPhaseEvent.get();
        if (!this.pulseEventToken.isEnabled()) {
            jFRPulseEvent.setPhase(null);
            return;
        }
        if (jFRPulseEvent.getPhase() != null) {
            jFRPulseEvent.end();
            jFRPulseEvent.commit();
        }
        if (string == null) {
            jFRPulseEvent.setPhase(null);
            return;
        }
        jFRPulseEvent.reset();
        jFRPulseEvent.begin();
        jFRPulseEvent.setPhase(string);
        jFRPulseEvent.setPulseNumber(Thread.currentThread() == this.fxThread ? this.fxPulseNumber : this.renderPulseNumber);
    }

    @Override
    public void newInput(String string) {
        if (this.inputEventToken == null) {
            return;
        }
        JFRInputEvent jFRInputEvent = this.curInputEvent.get();
        if (!this.inputEventToken.isEnabled()) {
            jFRInputEvent.setInput(null);
            return;
        }
        if (jFRInputEvent.getInput() != null) {
            jFRInputEvent.end();
            jFRInputEvent.commit();
        }
        if (string == null) {
            jFRInputEvent.setInput(null);
            return;
        }
        jFRInputEvent.reset();
        jFRInputEvent.begin();
        jFRInputEvent.setInput(string);
    }
}

