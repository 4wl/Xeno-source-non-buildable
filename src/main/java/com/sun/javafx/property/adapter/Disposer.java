/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.property.adapter;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Disposer
implements Runnable {
    private static final ReferenceQueue queue = new ReferenceQueue();
    private static final Map<Object, Runnable> records = new ConcurrentHashMap<Object, Runnable>();
    private static Disposer disposerInstance = new Disposer();

    public static void addRecord(Object object, Runnable runnable) {
        PhantomReference<Object> phantomReference = new PhantomReference<Object>(object, queue);
        records.put(phantomReference, runnable);
    }

    @Override
    public void run() {
        while (true) {
            try {
                while (true) {
                    Reference reference = queue.remove();
                    reference.clear();
                    Runnable runnable = records.remove(reference);
                    runnable.run();
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
        AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                Object object = threadGroup;
                while (object != null) {
                    threadGroup = object;
                    object = threadGroup.getParent();
                }
                object = new Thread(threadGroup, disposerInstance, "Property Disposer");
                ((Thread)object).setContextClassLoader(null);
                ((Thread)object).setDaemon(true);
                ((Thread)object).setPriority(10);
                ((Thread)object).start();
                return null;
            }
        });
    }
}

