/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.logging;

import com.sun.javafx.logging.Logger;
import com.sun.javafx.logging.PrintLogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PulseLogger {
    public static final boolean PULSE_LOGGING_ENABLED;
    private static final Logger[] loggers;

    public static void pulseStart() {
        for (Logger logger : loggers) {
            logger.pulseStart();
        }
    }

    public static void pulseEnd() {
        for (Logger logger : loggers) {
            logger.pulseEnd();
        }
    }

    public static void renderStart() {
        for (Logger logger : loggers) {
            logger.renderStart();
        }
    }

    public static void renderEnd() {
        for (Logger logger : loggers) {
            logger.renderEnd();
        }
    }

    public static void addMessage(String string) {
        for (Logger logger : loggers) {
            logger.addMessage(string);
        }
    }

    public static void incrementCounter(String string) {
        for (Logger logger : loggers) {
            logger.incrementCounter(string);
        }
    }

    public static void newPhase(String string) {
        for (Logger logger : loggers) {
            logger.newPhase(string);
        }
    }

    public static void newInput(String string) {
        for (Logger logger : loggers) {
            logger.newInput(string);
        }
    }

    static {
        ArrayList<Logger> arrayList = new ArrayList<Logger>();
        Logger logger = PrintLogger.getInstance();
        if (logger != null) {
            arrayList.add(logger);
        }
        try {
            Method method;
            Class<?> class_ = Class.forName("com.sun.javafx.logging.JFRLogger");
            if (class_ != null && (logger = (Logger)(method = class_.getDeclaredMethod("getInstance", new Class[0])).invoke(null, new Object[0])) != null) {
                arrayList.add(logger);
            }
        }
        catch (ClassNotFoundException | IllegalAccessException | NoClassDefFoundError | NoSuchMethodException | InvocationTargetException throwable) {
            // empty catch block
        }
        loggers = arrayList.toArray(new Logger[arrayList.size()]);
        PULSE_LOGGING_ENABLED = loggers.length > 0;
    }
}

