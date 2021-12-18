/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.Toolkit;
import com.sun.prism.impl.PrismSettings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

abstract class PerformanceTrackerHelper {
    private static final PerformanceTrackerHelper instance = PerformanceTrackerHelper.createInstance();

    public static PerformanceTrackerHelper getInstance() {
        return instance;
    }

    private PerformanceTrackerHelper() {
    }

    private static PerformanceTrackerHelper createInstance() {
        PerformanceTrackerHelper performanceTrackerHelper = AccessController.doPrivileged(new PrivilegedAction<PerformanceTrackerHelper>(){

            @Override
            public PerformanceTrackerHelper run() {
                try {
                    if (PrismSettings.perfLog != null) {
                        final PerformanceTrackerDefaultImpl performanceTrackerDefaultImpl = new PerformanceTrackerDefaultImpl();
                        if (PrismSettings.perfLogExitFlush) {
                            Runtime.getRuntime().addShutdownHook(new Thread(){

                                @Override
                                public void run() {
                                    performanceTrackerDefaultImpl.outputLog();
                                }
                            });
                        }
                        return performanceTrackerDefaultImpl;
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                return null;
            }
        });
        if (performanceTrackerHelper == null) {
            performanceTrackerHelper = new PerformanceTrackerDummyImpl();
        }
        return performanceTrackerHelper;
    }

    public abstract void logEvent(String var1);

    public abstract void outputLog();

    public abstract boolean isPerfLoggingEnabled();

    public final long nanoTime() {
        return Toolkit.getToolkit().getMasterTimer().nanos();
    }

    private static final class PerformanceTrackerDummyImpl
    extends PerformanceTrackerHelper {
        private PerformanceTrackerDummyImpl() {
        }

        @Override
        public void logEvent(String string) {
        }

        @Override
        public void outputLog() {
        }

        @Override
        public boolean isPerfLoggingEnabled() {
            return false;
        }
    }

    private static final class PerformanceTrackerDefaultImpl
    extends PerformanceTrackerHelper {
        private long firstTime;
        private long lastTime;
        private final Method logEventMethod;
        private final Method outputLogMethod;
        private final Method getStartTimeMethod;
        private final Method setStartTimeMethod;

        public PerformanceTrackerDefaultImpl() throws ClassNotFoundException, NoSuchMethodException {
            Class<?> class_ = Class.forName("sun.misc.PerformanceLogger", true, null);
            this.logEventMethod = class_.getMethod("setTime", String.class);
            this.outputLogMethod = class_.getMethod("outputLog", new Class[0]);
            this.getStartTimeMethod = class_.getMethod("getStartTime", new Class[0]);
            this.setStartTimeMethod = class_.getMethod("setStartTime", String.class, Long.TYPE);
        }

        @Override
        public void logEvent(String string) {
            long l = System.currentTimeMillis();
            if (this.firstTime == 0L) {
                this.firstTime = l;
            }
            try {
                this.logEventMethod.invoke(null, "JavaFX> " + string + " (" + (l - this.firstTime) + "ms total, " + (l - this.lastTime) + "ms)");
            }
            catch (IllegalAccessException illegalAccessException) {
            }
            catch (IllegalArgumentException illegalArgumentException) {
            }
            catch (InvocationTargetException invocationTargetException) {
                // empty catch block
            }
            this.lastTime = l;
        }

        @Override
        public void outputLog() {
            this.logLaunchTime();
            try {
                this.outputLogMethod.invoke(null, new Object[0]);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }

        @Override
        public boolean isPerfLoggingEnabled() {
            return true;
        }

        private void logLaunchTime() {
            try {
                String string;
                if ((Long)this.getStartTimeMethod.invoke(null, new Object[0]) <= 0L && (string = AccessController.doPrivileged(() -> System.getProperty("launchTime"))) != null && !string.equals("")) {
                    long l = Long.parseLong(string);
                    this.setStartTimeMethod.invoke(null, "LaunchTime", l);
                }
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}

