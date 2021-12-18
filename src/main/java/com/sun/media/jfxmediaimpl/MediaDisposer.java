/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.logging.Logger;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

public class MediaDisposer {
    private final ReferenceQueue<Object> purgatory = new ReferenceQueue();
    private final Map<Reference, Disposable> disposers = new HashMap<Reference, Disposable>();
    private static MediaDisposer theDisposinator;

    public static void addResourceDisposer(Object object, Object object2, ResourceDisposer resourceDisposer) {
        MediaDisposer.disposinator().implAddResourceDisposer(object, object2, resourceDisposer);
    }

    public static void removeResourceDisposer(Object object) {
        MediaDisposer.disposinator().implRemoveResourceDisposer(object);
    }

    public static void addDisposable(Object object, Disposable disposable) {
        MediaDisposer.disposinator().implAddDisposable(object, disposable);
    }

    private static synchronized MediaDisposer disposinator() {
        if (null == theDisposinator) {
            theDisposinator = new MediaDisposer();
            Thread thread = new Thread(() -> theDisposinator.disposerLoop(), "Media Resource Disposer");
            thread.setDaemon(true);
            thread.start();
        }
        return theDisposinator;
    }

    private MediaDisposer() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void disposerLoop() {
        while (true) {
            try {
                while (true) {
                    Disposable disposable;
                    Reference<Object> reference = this.purgatory.remove();
                    Map<Reference, Disposable> map = this.disposers;
                    synchronized (map) {
                        disposable = this.disposers.remove(reference);
                    }
                    reference.clear();
                    if (null != disposable) {
                        disposable.dispose();
                    }
                    reference = null;
                    disposable = null;
                }
            }
            catch (InterruptedException interruptedException) {
                if (!Logger.canLog(1)) continue;
                Logger.logMsg(1, MediaDisposer.class.getName(), "disposerLoop", "Disposer loop interrupted, terminating");
                continue;
            }
            break;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void implAddResourceDisposer(Object object, Object object2, ResourceDisposer resourceDisposer) {
        PhantomReference<Object> phantomReference = new PhantomReference<Object>(object, this.purgatory);
        Map<Reference, Disposable> map = this.disposers;
        synchronized (map) {
            this.disposers.put(phantomReference, new ResourceDisposerRecord(object2, resourceDisposer));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void implRemoveResourceDisposer(Object object) {
        Reference reference = null;
        Map<Reference, Disposable> map = this.disposers;
        synchronized (map) {
            for (Map.Entry<Reference, Disposable> entry : this.disposers.entrySet()) {
                Disposable disposable = entry.getValue();
                if (!(disposable instanceof ResourceDisposerRecord)) continue;
                ResourceDisposerRecord resourceDisposerRecord = (ResourceDisposerRecord)disposable;
                if (!resourceDisposerRecord.resource.equals(object)) continue;
                reference = entry.getKey();
                break;
            }
            if (null != reference) {
                this.disposers.remove(reference);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void implAddDisposable(Object object, Disposable disposable) {
        PhantomReference<Object> phantomReference = new PhantomReference<Object>(object, this.purgatory);
        Map<Reference, Disposable> map = this.disposers;
        synchronized (map) {
            this.disposers.put(phantomReference, disposable);
        }
    }

    private static class ResourceDisposerRecord
    implements Disposable {
        Object resource;
        ResourceDisposer disposer;

        public ResourceDisposerRecord(Object object, ResourceDisposer resourceDisposer) {
            this.resource = object;
            this.disposer = resourceDisposer;
        }

        @Override
        public void dispose() {
            this.disposer.disposeResource(this.resource);
        }
    }

    public static interface ResourceDisposer {
        public void disposeResource(Object var1);
    }

    public static interface Disposable {
        public void dispose();
    }
}

