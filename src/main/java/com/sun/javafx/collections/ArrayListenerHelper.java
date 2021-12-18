/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.collections.ArrayChangeListener
 *  javafx.collections.ObservableArray
 */
package com.sun.javafx.collections;

import com.sun.javafx.binding.ExpressionHelperBase;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ArrayChangeListener;
import javafx.collections.ObservableArray;

public abstract class ArrayListenerHelper<T extends ObservableArray<T>>
extends ExpressionHelperBase {
    protected final T observable;

    public static <T extends ObservableArray<T>> ArrayListenerHelper addListener(ArrayListenerHelper arrayListenerHelper, T t, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return arrayListenerHelper == null ? new SingleInvalidation(t, invalidationListener, null) : arrayListenerHelper.addListener(invalidationListener);
    }

    public static ArrayListenerHelper removeListener(ArrayListenerHelper arrayListenerHelper, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return arrayListenerHelper == null ? null : arrayListenerHelper.removeListener(invalidationListener);
    }

    public static <T extends ObservableArray<T>> ArrayListenerHelper addListener(ArrayListenerHelper arrayListenerHelper, T t, ArrayChangeListener arrayChangeListener) {
        if (arrayChangeListener == null) {
            throw new NullPointerException();
        }
        return arrayListenerHelper == null ? new SingleChange(t, arrayChangeListener, null) : arrayListenerHelper.addListener(arrayChangeListener);
    }

    public static ArrayListenerHelper removeListener(ArrayListenerHelper arrayListenerHelper, ArrayChangeListener arrayChangeListener) {
        if (arrayChangeListener == null) {
            throw new NullPointerException();
        }
        return arrayListenerHelper == null ? null : arrayListenerHelper.removeListener(arrayChangeListener);
    }

    public static void fireValueChangedEvent(ArrayListenerHelper arrayListenerHelper, boolean bl, int n, int n2) {
        if (arrayListenerHelper != null && (n < n2 || bl)) {
            arrayListenerHelper.fireValueChangedEvent(bl, n, n2);
        }
    }

    public static boolean hasListeners(ArrayListenerHelper arrayListenerHelper) {
        return arrayListenerHelper != null;
    }

    public ArrayListenerHelper(T t) {
        this.observable = t;
    }

    protected abstract ArrayListenerHelper addListener(InvalidationListener var1);

    protected abstract ArrayListenerHelper removeListener(InvalidationListener var1);

    protected abstract ArrayListenerHelper addListener(ArrayChangeListener<T> var1);

    protected abstract ArrayListenerHelper removeListener(ArrayChangeListener<T> var1);

    protected abstract void fireValueChangedEvent(boolean var1, int var2, int var3);

    private static class Generic<T extends ObservableArray<T>>
    extends ArrayListenerHelper<T> {
        private InvalidationListener[] invalidationListeners;
        private ArrayChangeListener[] changeListeners;
        private int invalidationSize;
        private int changeSize;
        private boolean locked;

        private Generic(T t, InvalidationListener invalidationListener, InvalidationListener invalidationListener2) {
            super(t);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener, invalidationListener2};
            this.invalidationSize = 2;
        }

        private Generic(T t, ArrayChangeListener arrayChangeListener, ArrayChangeListener arrayChangeListener2) {
            super(t);
            this.changeListeners = new ArrayChangeListener[]{arrayChangeListener, arrayChangeListener2};
            this.changeSize = 2;
        }

        private Generic(T t, InvalidationListener invalidationListener, ArrayChangeListener arrayChangeListener) {
            super(t);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ArrayChangeListener[]{arrayChangeListener};
            this.changeSize = 1;
        }

        @Override
        protected Generic addListener(InvalidationListener invalidationListener) {
            if (this.invalidationListeners == null) {
                this.invalidationListeners = new InvalidationListener[]{invalidationListener};
                this.invalidationSize = 1;
            } else {
                int n = this.invalidationListeners.length;
                if (this.locked) {
                    int n2 = this.invalidationSize < n ? n : n * 3 / 2 + 1;
                    this.invalidationListeners = Arrays.copyOf(this.invalidationListeners, n2);
                } else if (this.invalidationSize == n) {
                    this.invalidationSize = Generic.trim(this.invalidationSize, (Object[])this.invalidationListeners);
                    if (this.invalidationSize == n) {
                        int n3 = n * 3 / 2 + 1;
                        this.invalidationListeners = Arrays.copyOf(this.invalidationListeners, n3);
                    }
                }
                this.invalidationListeners[this.invalidationSize++] = invalidationListener;
            }
            return this;
        }

        @Override
        protected ArrayListenerHelper removeListener(InvalidationListener invalidationListener) {
            if (this.invalidationListeners != null) {
                for (int i = 0; i < this.invalidationSize; ++i) {
                    if (!invalidationListener.equals((Object)this.invalidationListeners[i])) continue;
                    if (this.invalidationSize == 1) {
                        if (this.changeSize == 1) {
                            return new SingleChange(this.observable, this.changeListeners[0], null);
                        }
                        this.invalidationListeners = null;
                        this.invalidationSize = 0;
                        break;
                    }
                    if (this.invalidationSize == 2 && this.changeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[1 - i], null);
                    }
                    int n = this.invalidationSize - i - 1;
                    InvalidationListener[] arrinvalidationListener = this.invalidationListeners;
                    if (this.locked) {
                        this.invalidationListeners = new InvalidationListener[this.invalidationListeners.length];
                        System.arraycopy(arrinvalidationListener, 0, this.invalidationListeners, 0, i + 1);
                    }
                    if (n > 0) {
                        System.arraycopy(arrinvalidationListener, i + 1, this.invalidationListeners, i, n);
                    }
                    --this.invalidationSize;
                    if (this.locked) break;
                    this.invalidationListeners[this.invalidationSize] = null;
                    break;
                }
            }
            return this;
        }

        @Override
        protected ArrayListenerHelper addListener(ArrayChangeListener<T> arrayChangeListener) {
            if (this.changeListeners == null) {
                this.changeListeners = new ArrayChangeListener[]{arrayChangeListener};
                this.changeSize = 1;
            } else {
                int n = this.changeListeners.length;
                if (this.locked) {
                    int n2 = this.changeSize < n ? n : n * 3 / 2 + 1;
                    this.changeListeners = Arrays.copyOf(this.changeListeners, n2);
                } else if (this.changeSize == n) {
                    this.changeSize = Generic.trim(this.changeSize, (Object[])this.changeListeners);
                    if (this.changeSize == n) {
                        int n3 = n * 3 / 2 + 1;
                        this.changeListeners = Arrays.copyOf(this.changeListeners, n3);
                    }
                }
                this.changeListeners[this.changeSize++] = arrayChangeListener;
            }
            return this;
        }

        @Override
        protected ArrayListenerHelper removeListener(ArrayChangeListener<T> arrayChangeListener) {
            if (this.changeListeners != null) {
                for (int i = 0; i < this.changeSize; ++i) {
                    if (!arrayChangeListener.equals((Object)this.changeListeners[i])) continue;
                    if (this.changeSize == 1) {
                        if (this.invalidationSize == 1) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0], null);
                        }
                        this.changeListeners = null;
                        this.changeSize = 0;
                        break;
                    }
                    if (this.changeSize == 2 && this.invalidationSize == 0) {
                        return new SingleChange(this.observable, this.changeListeners[1 - i], null);
                    }
                    int n = this.changeSize - i - 1;
                    ArrayChangeListener[] arrarrayChangeListener = this.changeListeners;
                    if (this.locked) {
                        this.changeListeners = new ArrayChangeListener[this.changeListeners.length];
                        System.arraycopy(arrarrayChangeListener, 0, this.changeListeners, 0, i + 1);
                    }
                    if (n > 0) {
                        System.arraycopy(arrarrayChangeListener, i + 1, this.changeListeners, i, n);
                    }
                    --this.changeSize;
                    if (this.locked) break;
                    this.changeListeners[this.changeSize] = null;
                    break;
                }
            }
            return this;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        protected void fireValueChangedEvent(boolean bl, int n, int n2) {
            InvalidationListener[] arrinvalidationListener = this.invalidationListeners;
            int n3 = this.invalidationSize;
            ArrayChangeListener[] arrarrayChangeListener = this.changeListeners;
            int n4 = this.changeSize;
            try {
                int n5;
                this.locked = true;
                for (n5 = 0; n5 < n3; ++n5) {
                    try {
                        arrinvalidationListener[n5].invalidated((Observable)this.observable);
                        continue;
                    }
                    catch (Exception exception) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
                    }
                }
                for (n5 = 0; n5 < n4; ++n5) {
                    try {
                        arrarrayChangeListener[n5].onChanged(this.observable, bl, n, n2);
                        continue;
                    }
                    catch (Exception exception) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
                    }
                }
            }
            finally {
                this.locked = false;
            }
        }

        /* synthetic */ Generic(ObservableArray observableArray, InvalidationListener invalidationListener, InvalidationListener invalidationListener2, 1 var4_4) {
            this(observableArray, invalidationListener, invalidationListener2);
        }

        /* synthetic */ Generic(ObservableArray observableArray, InvalidationListener invalidationListener, ArrayChangeListener arrayChangeListener, 1 var4_4) {
            this(observableArray, invalidationListener, arrayChangeListener);
        }

        /* synthetic */ Generic(ObservableArray observableArray, ArrayChangeListener arrayChangeListener, ArrayChangeListener arrayChangeListener2, 1 var4_4) {
            this(observableArray, arrayChangeListener, arrayChangeListener2);
        }
    }

    private static class SingleChange<T extends ObservableArray<T>>
    extends ArrayListenerHelper<T> {
        private final ArrayChangeListener listener;

        private SingleChange(T t, ArrayChangeListener arrayChangeListener) {
            super(t);
            this.listener = arrayChangeListener;
        }

        @Override
        protected ArrayListenerHelper addListener(InvalidationListener invalidationListener) {
            return new Generic(this.observable, invalidationListener, this.listener, null);
        }

        @Override
        protected ArrayListenerHelper removeListener(InvalidationListener invalidationListener) {
            return this;
        }

        @Override
        protected ArrayListenerHelper addListener(ArrayChangeListener arrayChangeListener) {
            return new Generic(this.observable, this.listener, arrayChangeListener, null);
        }

        @Override
        protected ArrayListenerHelper removeListener(ArrayChangeListener arrayChangeListener) {
            return arrayChangeListener.equals((Object)this.listener) ? null : this;
        }

        @Override
        protected void fireValueChangedEvent(boolean bl, int n, int n2) {
            try {
                this.listener.onChanged(this.observable, bl, n, n2);
            }
            catch (Exception exception) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
            }
        }

        /* synthetic */ SingleChange(ObservableArray observableArray, ArrayChangeListener arrayChangeListener, 1 var3_3) {
            this(observableArray, arrayChangeListener);
        }
    }

    private static class SingleInvalidation<T extends ObservableArray<T>>
    extends ArrayListenerHelper<T> {
        private final InvalidationListener listener;

        private SingleInvalidation(T t, InvalidationListener invalidationListener) {
            super(t);
            this.listener = invalidationListener;
        }

        @Override
        protected ArrayListenerHelper addListener(InvalidationListener invalidationListener) {
            return new Generic(this.observable, this.listener, invalidationListener, null);
        }

        @Override
        protected ArrayListenerHelper removeListener(InvalidationListener invalidationListener) {
            return invalidationListener.equals((Object)this.listener) ? null : this;
        }

        @Override
        protected ArrayListenerHelper addListener(ArrayChangeListener arrayChangeListener) {
            return new Generic(this.observable, this.listener, arrayChangeListener, null);
        }

        @Override
        protected ArrayListenerHelper removeListener(ArrayChangeListener arrayChangeListener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent(boolean bl, int n, int n2) {
            try {
                this.listener.invalidated((Observable)this.observable);
            }
            catch (Exception exception) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
            }
        }

        /* synthetic */ SingleInvalidation(ObservableArray observableArray, InvalidationListener invalidationListener, 1 var3_3) {
            this(observableArray, invalidationListener);
        }
    }
}

