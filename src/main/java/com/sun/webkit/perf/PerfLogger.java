/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.perf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PerfLogger {
    private static Thread shutdownHook;
    private static Map<Logger, PerfLogger> loggers;
    private final HashMap<String, ProbeStat> probes = new HashMap();
    private final Logger log;
    private final boolean isEnabled;
    private final Comparator timeComparator = (object, object2) -> {
        long l;
        long l2 = this.probes.get((String)object).totalTime;
        if (l2 > (l = this.probes.get((String)object2).totalTime)) {
            return 1;
        }
        if (l2 < l) {
            return -1;
        }
        return 0;
    };
    private final Comparator countComparator = (object, object2) -> {
        long l;
        long l2 = this.probes.get((String)object).count;
        if (l2 > (l = (long)this.probes.get((String)object2).count)) {
            return 1;
        }
        if (l2 < l) {
            return -1;
        }
        return 0;
    };

    public static synchronized PerfLogger getLogger(Logger logger) {
        PerfLogger perfLogger;
        if (loggers == null) {
            loggers = new HashMap<Logger, PerfLogger>();
        }
        if ((perfLogger = loggers.get(logger)) == null) {
            perfLogger = new PerfLogger(logger);
            loggers.put(logger, perfLogger);
        }
        if (perfLogger.isEnabled() && shutdownHook == null) {
            shutdownHook = new Thread(){

                @Override
                public void run() {
                    for (PerfLogger perfLogger : loggers.values()) {
                        if (!perfLogger.isEnabled()) continue;
                        perfLogger.log(false);
                    }
                }
            };
            Runtime.getRuntime().addShutdownHook(shutdownHook);
        }
        return perfLogger;
    }

    public static synchronized PerfLogger getLogger(String string) {
        return PerfLogger.getLogger(Logger.getLogger("com.sun.webkit.perf." + string));
    }

    private PerfLogger(Logger logger) {
        this.log = logger;
        this.isEnabled = logger.isLoggable(Level.FINE);
        this.startCount("TOTALTIME");
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    private synchronized String fullName(String string) {
        return this.log.getName() + "." + string;
    }

    public synchronized void reset() {
        for (Map.Entry<String, ProbeStat> entry : this.probes.entrySet()) {
            entry.getValue().reset();
        }
        this.startCount("TOTALTIME");
    }

    public static synchronized void resetAll() {
        for (PerfLogger perfLogger : loggers.values()) {
            perfLogger.reset();
        }
    }

    private synchronized ProbeStat registerProbe(String string) {
        String string2 = string.intern();
        if (this.probes.containsKey(string2)) {
            this.log.fine("Warning: \"" + this.fullName(string2) + "\" probe already exists");
        } else {
            this.log.fine("Registering \"" + this.fullName(string2) + "\" probe");
        }
        ProbeStat probeStat = new ProbeStat(string2);
        this.probes.put(string2, probeStat);
        return probeStat;
    }

    public synchronized ProbeStat getProbeStat(String string) {
        String string2 = string.intern();
        ProbeStat probeStat = this.probes.get(string2);
        if (probeStat != null) {
            probeStat.snapshot();
        }
        return probeStat;
    }

    public synchronized void startCount(String string) {
        if (!this.isEnabled()) {
            return;
        }
        String string2 = string.intern();
        ProbeStat probeStat = this.probes.get(string2);
        if (probeStat == null) {
            probeStat = this.registerProbe(string2);
        }
        probeStat.reset();
        probeStat.resume();
    }

    public synchronized void suspendCount(String string) {
        if (!this.isEnabled()) {
            return;
        }
        String string2 = string.intern();
        ProbeStat probeStat = this.probes.get(string2);
        if (probeStat != null) {
            probeStat.suspend();
        } else {
            this.log.fine("Warning: \"" + this.fullName(string2) + "\" probe is not registered");
        }
    }

    public synchronized void resumeCount(String string) {
        if (!this.isEnabled()) {
            return;
        }
        String string2 = string.intern();
        ProbeStat probeStat = this.probes.get(string2);
        if (probeStat == null) {
            probeStat = this.registerProbe(string2);
        }
        probeStat.resume();
    }

    public synchronized void log(StringBuffer stringBuffer) {
        if (!this.isEnabled()) {
            return;
        }
        stringBuffer.append("=========== Performance Statistics =============\n");
        ProbeStat probeStat = this.getProbeStat("TOTALTIME");
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(this.probes.keySet());
        stringBuffer.append("\nTime:\n");
        Collections.sort(arrayList, this.timeComparator);
        for (String string : arrayList) {
            ProbeStat probeStat2 = this.getProbeStat(string);
            stringBuffer.append(String.format("%s: %dms", this.fullName(string), probeStat2.totalTime));
            if (probeStat.totalTime > 0L) {
                stringBuffer.append(String.format(", %.2f%%%n", Float.valueOf(100.0f * (float)probeStat2.totalTime / (float)probeStat.totalTime)));
                continue;
            }
            stringBuffer.append("\n");
        }
        stringBuffer.append("\nInvocations count:\n");
        Collections.sort(arrayList, this.countComparator);
        for (String string : arrayList) {
            stringBuffer.append(String.format("%s: %d%n", this.fullName(string), this.getProbeStat(string).count));
        }
        stringBuffer.append("================================================\n");
    }

    public synchronized void log() {
        this.log(true);
    }

    private synchronized void log(boolean bl) {
        StringBuffer stringBuffer = new StringBuffer();
        this.log(stringBuffer);
        if (bl) {
            this.log.fine(stringBuffer.toString());
        } else {
            System.out.println(stringBuffer.toString());
            System.out.flush();
        }
    }

    public static synchronized void logAll() {
        for (PerfLogger perfLogger : loggers.values()) {
            perfLogger.log();
        }
    }

    public static final class ProbeStat {
        private final String probe;
        private int count;
        private long totalTime;
        private long startTime;
        private boolean isRunning = false;

        private ProbeStat(String string) {
            this.probe = string;
        }

        public String getProbe() {
            return this.probe;
        }

        public int getCount() {
            return this.count;
        }

        public long getTotalTime() {
            return this.totalTime;
        }

        private void reset() {
            this.count = 0;
            this.startTime = 0L;
            this.totalTime = 0L;
        }

        private void suspend() {
            if (this.isRunning) {
                this.totalTime += System.currentTimeMillis() - this.startTime;
                this.isRunning = false;
            }
        }

        private void resume() {
            this.isRunning = true;
            ++this.count;
            this.startTime = System.currentTimeMillis();
        }

        private void snapshot() {
            if (this.isRunning) {
                this.totalTime += System.currentTimeMillis() - this.startTime;
                this.startTime = System.currentTimeMillis();
            }
        }

        public String toString() {
            return super.toString() + "[count=" + this.count + ", time=" + this.totalTime + "]";
        }
    }
}

