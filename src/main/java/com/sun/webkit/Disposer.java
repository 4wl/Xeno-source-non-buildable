/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.DisposerRecord;
import com.sun.webkit.Invoker;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public final class Disposer
implements Runnable {
    private static final ReferenceQueue queue = new ReferenceQueue();
    private static final Disposer disposerInstance = new Disposer();
    private static final Set<WeakDisposerRecord> records = new HashSet<WeakDisposerRecord>();

    public static void addRecord(Object object, DisposerRecord disposerRecord) {
        disposerInstance.add(object, disposerRecord);
    }

    private synchronized void add(Object object, DisposerRecord disposerRecord) {
        records.add(new WeakDisposerRecord(object, disposerRecord));
    }

    @Override
    public void run() {
        while (true) {
            try {
                while (true) {
                    WeakDisposerRecord weakDisposerRecord = (WeakDisposerRecord)queue.remove();
                    weakDisposerRecord.clear();
                    DisposerRunnable.getInstance().enqueue(weakDisposerRecord);
                }
            }
            catch (Exception exception) {
                System.out.println("Exception while removing reference: " + exception);
                exception.printStackTrace();
                continue;
            }
            break;
        }
    }

    static {
        AccessController.doPrivileged(() -> {
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            Object object = threadGroup;
            while (object != null) {
                threadGroup = object;
                object = threadGroup.getParent();
            }
            object = new Thread(threadGroup, disposerInstance, "Disposer");
            ((Thread)object).setDaemon(true);
            ((Thread)object).setPriority(10);
            ((Thread)object).start();
            return null;
        });
    }

    public static class WeakDisposerRecord
    extends WeakReference
    implements DisposerRecord {
        private final DisposerRecord record;

        protected WeakDisposerRecord(Object object) {
            super(object, queue);
            this.record = null;
        }

        private WeakDisposerRecord(Object object, DisposerRecord disposerRecord) {
            super(object, queue);
            this.record = disposerRecord;
        }

        @Override
        public void dispose() {
            this.record.dispose();
        }
    }

    private static final class DisposerRunnable
    implements Runnable {
        private static final DisposerRunnable theInstance = new DisposerRunnable();
        private boolean isRunning = false;
        private final Object disposerLock = new Object();
        private final LinkedBlockingQueue<WeakDisposerRecord> disposerQueue = new LinkedBlockingQueue();

        private DisposerRunnable() {
        }

        private static DisposerRunnable getInstance() {
            return theInstance;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void enqueueAll(Collection<WeakDisposerRecord> collection) {
            Object object = this.disposerLock;
            synchronized (object) {
                this.disposerQueue.addAll(collection);
                if (!this.isRunning) {
                    Invoker.getInvoker().invokeOnEventThread(this);
                    this.isRunning = true;
                }
            }
        }

        private void enqueue(WeakDisposerRecord weakDisposerRecord) {
            this.enqueueAll(Arrays.asList(weakDisposerRecord));
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            while (true) {
                WeakDisposerRecord weakDisposerRecord;
                Object object = this.disposerLock;
                synchronized (object) {
                    weakDisposerRecord = this.disposerQueue.poll();
                    if (weakDisposerRecord == null) {
                        this.isRunning = false;
                        break;
                    }
                }
                if (!records.contains(weakDisposerRecord)) continue;
                records.remove(weakDisposerRecord);
                weakDisposerRecord.dispose();
            }
        }
    }
}

