/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.logging;

import com.sun.javafx.logging.Logger;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class PrintLogger
extends Logger {
    private static PrintLogger printLogger;
    private static long THRESHOLD;
    private static final int EXIT_ON_PULSE;
    private int pulseCount = 1;
    private static final int INTER_PULSE_DATA = -1;
    private volatile int wrapCount = 0;
    private volatile PulseData fxData;
    private volatile PulseData renderData;
    private long lastPulseStartTime;
    private Thread fxThread;
    private final ThreadLocal<ThreadLocalData> phaseData = new ThreadLocal(){

        public ThreadLocalData initialValue() {
            return new ThreadLocalData();
        }
    };
    private PulseData head = new PulseData();
    private PulseData tail;
    private AtomicInteger active;
    private static final int AVAILABLE = 0;
    private static final int INCOMPLETE = 1;
    private static final int COMPLETE = 2;

    private PrintLogger() {
        this.head.next = this.tail = new PulseData();
        this.active = new AtomicInteger(0);
    }

    public static Logger getInstance() {
        boolean bl;
        if (printLogger == null && (bl = AccessController.doPrivileged(() -> Boolean.getBoolean("javafx.pulseLogger")).booleanValue())) {
            printLogger = new PrintLogger();
        }
        return printLogger;
    }

    private PulseData allocate(int n) {
        PulseData pulseData;
        if (this.head != this.tail && this.head.state == 0) {
            pulseData = this.head;
            this.head = this.head.next;
            pulseData.next = null;
        } else {
            pulseData = new PulseData();
        }
        this.tail.next = pulseData;
        this.tail = pulseData;
        pulseData.init(n);
        return pulseData;
    }

    @Override
    public void pulseStart() {
        if (this.fxThread == null) {
            this.fxThread = Thread.currentThread();
        }
        if (this.fxData != null) {
            this.fxData.state = 2;
            if (this.active.incrementAndGet() == 1) {
                this.fxData.printAndReset();
                this.active.decrementAndGet();
            }
        }
        this.fxData = this.allocate(this.pulseCount++);
        if (this.lastPulseStartTime > 0L) {
            this.fxData.interval = (this.fxData.startTime - this.lastPulseStartTime) / 1000000L;
        }
        this.lastPulseStartTime = this.fxData.startTime;
    }

    @Override
    public void renderStart() {
        this.newPhase(null);
        this.fxData.pushedRender = true;
        this.renderData = this.fxData;
        this.active.incrementAndGet();
    }

    @Override
    public void pulseEnd() {
        if (this.fxData != null && !this.fxData.pushedRender) {
            this.fxData.state = 2;
            if (this.active.incrementAndGet() == 1) {
                this.fxData.printAndReset();
                this.active.decrementAndGet();
            }
        }
        this.fxData = null;
    }

    @Override
    public void renderEnd() {
        this.newPhase(null);
        this.renderData.state = 2;
        while (true) {
            this.renderData.printAndReset();
            if (this.active.decrementAndGet() == 0) break;
            this.renderData = this.renderData.next;
        }
        this.renderData = null;
    }

    @Override
    public void addMessage(String string) {
        PulseData pulseData;
        if (this.fxThread == null || Thread.currentThread() == this.fxThread) {
            if (this.fxData == null) {
                this.fxData = this.allocate(-1);
            }
            pulseData = this.fxData;
        } else {
            pulseData = this.renderData;
        }
        if (pulseData == null) {
            return;
        }
        pulseData.message.append("T").append(Thread.currentThread().getId()).append(" : ").append(string).append("\n");
    }

    @Override
    public void incrementCounter(String string) {
        PulseData pulseData;
        if (this.fxThread == null || Thread.currentThread() == this.fxThread) {
            if (this.fxData == null) {
                this.fxData = this.allocate(-1);
            }
            pulseData = this.fxData;
        } else {
            pulseData = this.renderData;
        }
        if (pulseData == null) {
            return;
        }
        Map<String, Counter> map = pulseData.counters;
        Counter counter = map.get(string);
        if (counter == null) {
            counter = new Counter();
            map.put(string, counter);
        }
        ++counter.value;
    }

    @Override
    public void newPhase(String string) {
        long l = System.nanoTime();
        ThreadLocalData threadLocalData = this.phaseData.get();
        if (threadLocalData.phaseName != null) {
            PulseData pulseData;
            PulseData pulseData2 = pulseData = Thread.currentThread() == this.fxThread ? this.fxData : this.renderData;
            if (pulseData != null) {
                pulseData.message.append("T").append(Thread.currentThread().getId()).append(" (").append((threadLocalData.phaseStart - pulseData.startTime) / 1000000L).append(" +").append((l - threadLocalData.phaseStart) / 1000000L).append("ms): ").append(threadLocalData.phaseName).append("\n");
            }
        }
        threadLocalData.phaseName = string;
        threadLocalData.phaseStart = l;
    }

    static {
        THRESHOLD = AccessController.doPrivileged(() -> Integer.getInteger("javafx.pulseLogger.threshold", 17)).intValue();
        EXIT_ON_PULSE = AccessController.doPrivileged(() -> Integer.getInteger("javafx.pulseLogger.exitOnPulse", 0));
    }

    private final class PulseData {
        PulseData next;
        volatile int state = 0;
        long startTime;
        long interval;
        int pulseCount;
        boolean pushedRender;
        StringBuffer message = new StringBuffer();
        Map<String, Counter> counters = new ConcurrentHashMap<String, Counter>();

        private PulseData() {
        }

        void init(int n) {
            this.state = 1;
            this.pulseCount = n;
            this.startTime = System.nanoTime();
            this.interval = 0L;
            this.pushedRender = false;
        }

        void printAndReset() {
            long l = System.nanoTime();
            long l2 = (l - this.startTime) / 1000000L;
            if (this.state != 2) {
                System.err.println("\nWARNING: logging incomplete state");
            }
            if (l2 <= THRESHOLD) {
                if (this.pulseCount != -1) {
                    System.err.print((PrintLogger.this.wrapCount++ % 10 == 0 ? "\n[" : "[") + this.pulseCount + " " + this.interval + "ms:" + l2 + "ms]");
                }
            } else {
                if (this.pulseCount == -1) {
                    System.err.println("\n\nINTER PULSE LOG DATA");
                } else {
                    System.err.print("\n\nPULSE: " + this.pulseCount + " [" + this.interval + "ms:" + l2 + "ms]");
                    if (!this.pushedRender) {
                        System.err.print(" Required No Rendering");
                    }
                    System.err.println();
                }
                System.err.print(this.message);
                if (!this.counters.isEmpty()) {
                    System.err.println("Counters:");
                    ArrayList<Map.Entry<String, Counter>> arrayList = new ArrayList<Map.Entry<String, Counter>>(this.counters.entrySet());
                    Collections.sort(arrayList, (entry, entry2) -> ((String)entry.getKey()).compareTo((String)entry2.getKey()));
                    for (Map.Entry entry3 : arrayList) {
                        System.err.println("\t" + (String)entry3.getKey() + ": " + ((Counter)entry3.getValue()).value);
                    }
                }
                PrintLogger.this.wrapCount = 0;
            }
            this.message.setLength(0);
            this.counters.clear();
            this.state = 0;
            if (EXIT_ON_PULSE > 0 && this.pulseCount >= EXIT_ON_PULSE) {
                System.err.println("Exiting after pulse #" + this.pulseCount);
                System.exit(0);
            }
        }
    }

    private static class Counter {
        int value;

        private Counter() {
        }
    }

    class ThreadLocalData {
        String phaseName;
        long phaseStart;

        ThreadLocalData() {
        }
    }
}

