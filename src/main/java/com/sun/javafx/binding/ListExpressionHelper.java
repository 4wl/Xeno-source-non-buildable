/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableListValue
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 */
package com.sun.javafx.binding;

import com.sun.javafx.binding.ExpressionHelperBase;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.SourceAdapterChange;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class ListExpressionHelper<E>
extends ExpressionHelperBase {
    protected final ObservableListValue<E> observable;

    public static <E> ListExpressionHelper<E> addListener(ListExpressionHelper<E> listExpressionHelper, ObservableListValue<E> observableListValue, InvalidationListener invalidationListener) {
        if (observableListValue == null || invalidationListener == null) {
            throw new NullPointerException();
        }
        observableListValue.getValue();
        return listExpressionHelper == null ? new SingleInvalidation(observableListValue, invalidationListener) : listExpressionHelper.addListener(invalidationListener);
    }

    public static <E> ListExpressionHelper<E> removeListener(ListExpressionHelper<E> listExpressionHelper, InvalidationListener invalidationListener) {
        if (invalidationListener == null) {
            throw new NullPointerException();
        }
        return listExpressionHelper == null ? null : listExpressionHelper.removeListener(invalidationListener);
    }

    public static <E> ListExpressionHelper<E> addListener(ListExpressionHelper<E> listExpressionHelper, ObservableListValue<E> observableListValue, ChangeListener<? super ObservableList<E>> changeListener) {
        if (observableListValue == null || changeListener == null) {
            throw new NullPointerException();
        }
        return listExpressionHelper == null ? new SingleChange(observableListValue, changeListener) : listExpressionHelper.addListener(changeListener);
    }

    public static <E> ListExpressionHelper<E> removeListener(ListExpressionHelper<E> listExpressionHelper, ChangeListener<? super ObservableList<E>> changeListener) {
        if (changeListener == null) {
            throw new NullPointerException();
        }
        return listExpressionHelper == null ? null : listExpressionHelper.removeListener(changeListener);
    }

    public static <E> ListExpressionHelper<E> addListener(ListExpressionHelper<E> listExpressionHelper, ObservableListValue<E> observableListValue, ListChangeListener<? super E> listChangeListener) {
        if (observableListValue == null || listChangeListener == null) {
            throw new NullPointerException();
        }
        return listExpressionHelper == null ? new SingleListChange(observableListValue, listChangeListener) : listExpressionHelper.addListener(listChangeListener);
    }

    public static <E> ListExpressionHelper<E> removeListener(ListExpressionHelper<E> listExpressionHelper, ListChangeListener<? super E> listChangeListener) {
        if (listChangeListener == null) {
            throw new NullPointerException();
        }
        return listExpressionHelper == null ? null : listExpressionHelper.removeListener(listChangeListener);
    }

    public static <E> void fireValueChangedEvent(ListExpressionHelper<E> listExpressionHelper) {
        if (listExpressionHelper != null) {
            listExpressionHelper.fireValueChangedEvent();
        }
    }

    public static <E> void fireValueChangedEvent(ListExpressionHelper<E> listExpressionHelper, ListChangeListener.Change<? extends E> change) {
        if (listExpressionHelper != null) {
            listExpressionHelper.fireValueChangedEvent(change);
        }
    }

    protected ListExpressionHelper(ObservableListValue<E> observableListValue) {
        this.observable = observableListValue;
    }

    protected abstract ListExpressionHelper<E> addListener(InvalidationListener var1);

    protected abstract ListExpressionHelper<E> removeListener(InvalidationListener var1);

    protected abstract ListExpressionHelper<E> addListener(ChangeListener<? super ObservableList<E>> var1);

    protected abstract ListExpressionHelper<E> removeListener(ChangeListener<? super ObservableList<E>> var1);

    protected abstract ListExpressionHelper<E> addListener(ListChangeListener<? super E> var1);

    protected abstract ListExpressionHelper<E> removeListener(ListChangeListener<? super E> var1);

    protected abstract void fireValueChangedEvent();

    protected abstract void fireValueChangedEvent(ListChangeListener.Change<? extends E> var1);

    private static class Generic<E>
    extends ListExpressionHelper<E> {
        private InvalidationListener[] invalidationListeners;
        private ChangeListener<? super ObservableList<E>>[] changeListeners;
        private ListChangeListener<? super E>[] listChangeListeners;
        private int invalidationSize;
        private int changeSize;
        private int listChangeSize;
        private boolean locked;
        private ObservableList<E> currentValue;

        private Generic(ObservableListValue<E> observableListValue, InvalidationListener invalidationListener, InvalidationListener invalidationListener2) {
            super(observableListValue);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener, invalidationListener2};
            this.invalidationSize = 2;
        }

        private Generic(ObservableListValue<E> observableListValue, ChangeListener<? super ObservableList<E>> changeListener, ChangeListener<? super ObservableList<E>> changeListener2) {
            super(observableListValue);
            this.changeListeners = new ChangeListener[]{changeListener, changeListener2};
            this.changeSize = 2;
            this.currentValue = (ObservableList)observableListValue.getValue();
        }

        private Generic(ObservableListValue<E> observableListValue, ListChangeListener<? super E> listChangeListener, ListChangeListener<? super E> listChangeListener2) {
            super(observableListValue);
            this.listChangeListeners = new ListChangeListener[]{listChangeListener, listChangeListener2};
            this.listChangeSize = 2;
            this.currentValue = (ObservableList)observableListValue.getValue();
        }

        private Generic(ObservableListValue<E> observableListValue, InvalidationListener invalidationListener, ChangeListener<? super ObservableList<E>> changeListener) {
            super(observableListValue);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ChangeListener[]{changeListener};
            this.changeSize = 1;
            this.currentValue = (ObservableList)observableListValue.getValue();
        }

        private Generic(ObservableListValue<E> observableListValue, InvalidationListener invalidationListener, ListChangeListener<? super E> listChangeListener) {
            super(observableListValue);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.listChangeListeners = new ListChangeListener[]{listChangeListener};
            this.listChangeSize = 1;
            this.currentValue = (ObservableList)observableListValue.getValue();
        }

        private Generic(ObservableListValue<E> observableListValue, ChangeListener<? super ObservableList<E>> changeListener, ListChangeListener<? super E> listChangeListener) {
            super(observableListValue);
            this.changeListeners = new ChangeListener[]{changeListener};
            this.changeSize = 1;
            this.listChangeListeners = new ListChangeListener[]{listChangeListener};
            this.listChangeSize = 1;
            this.currentValue = (ObservableList)observableListValue.getValue();
        }

        @Override
        protected ListExpressionHelper<E> addListener(InvalidationListener invalidationListener) {
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
        protected ListExpressionHelper<E> removeListener(InvalidationListener invalidationListener) {
            if (this.invalidationListeners != null) {
                for (int i = 0; i < this.invalidationSize; ++i) {
                    if (!invalidationListener.equals((Object)this.invalidationListeners[i])) continue;
                    if (this.invalidationSize == 1) {
                        if (this.changeSize == 1 && this.listChangeSize == 0) {
                            return new SingleChange(this.observable, this.changeListeners[0]);
                        }
                        if (this.changeSize == 0 && this.listChangeSize == 1) {
                            return new SingleListChange(this.observable, this.listChangeListeners[0]);
                        }
                        this.invalidationListeners = null;
                        this.invalidationSize = 0;
                        break;
                    }
                    if (this.invalidationSize == 2 && this.changeSize == 0 && this.listChangeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[1 - i]);
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
        protected ListExpressionHelper<E> addListener(ChangeListener<? super ObservableList<E>> changeListener) {
            if (this.changeListeners == null) {
                this.changeListeners = new ChangeListener[]{changeListener};
                this.changeSize = 1;
            } else {
                int n = this.changeListeners.length;
                if (this.locked) {
                    int n2 = this.changeSize < n ? n : n * 3 / 2 + 1;
                    this.changeListeners = Arrays.copyOf(this.changeListeners, n2);
                } else if (this.changeSize == n) {
                    this.changeSize = Generic.trim(this.changeSize, this.changeListeners);
                    if (this.changeSize == n) {
                        int n3 = n * 3 / 2 + 1;
                        this.changeListeners = Arrays.copyOf(this.changeListeners, n3);
                    }
                }
                this.changeListeners[this.changeSize++] = changeListener;
            }
            if (this.changeSize == 1) {
                this.currentValue = (ObservableList)this.observable.getValue();
            }
            return this;
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ChangeListener<? super ObservableList<E>> changeListener) {
            if (this.changeListeners != null) {
                for (int i = 0; i < this.changeSize; ++i) {
                    if (!changeListener.equals(this.changeListeners[i])) continue;
                    if (this.changeSize == 1) {
                        if (this.invalidationSize == 1 && this.listChangeSize == 0) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                        }
                        if (this.invalidationSize == 0 && this.listChangeSize == 1) {
                            return new SingleListChange(this.observable, this.listChangeListeners[0]);
                        }
                        this.changeListeners = null;
                        this.changeSize = 0;
                        break;
                    }
                    if (this.changeSize == 2 && this.invalidationSize == 0 && this.listChangeSize == 0) {
                        return new SingleChange(this.observable, this.changeListeners[1 - i]);
                    }
                    int n = this.changeSize - i - 1;
                    ChangeListener<? super ObservableList<E>>[] arrchangeListener = this.changeListeners;
                    if (this.locked) {
                        this.changeListeners = new ChangeListener[this.changeListeners.length];
                        System.arraycopy(arrchangeListener, 0, this.changeListeners, 0, i + 1);
                    }
                    if (n > 0) {
                        System.arraycopy(arrchangeListener, i + 1, this.changeListeners, i, n);
                    }
                    --this.changeSize;
                    if (this.locked) break;
                    this.changeListeners[this.changeSize] = null;
                    break;
                }
            }
            return this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ListChangeListener<? super E> listChangeListener) {
            if (this.listChangeListeners == null) {
                this.listChangeListeners = new ListChangeListener[]{listChangeListener};
                this.listChangeSize = 1;
            } else {
                int n = this.listChangeListeners.length;
                if (this.locked) {
                    int n2 = this.listChangeSize < n ? n : n * 3 / 2 + 1;
                    this.listChangeListeners = Arrays.copyOf(this.listChangeListeners, n2);
                } else if (this.listChangeSize == n) {
                    this.listChangeSize = Generic.trim(this.listChangeSize, this.listChangeListeners);
                    if (this.listChangeSize == n) {
                        int n3 = n * 3 / 2 + 1;
                        this.listChangeListeners = Arrays.copyOf(this.listChangeListeners, n3);
                    }
                }
                this.listChangeListeners[this.listChangeSize++] = listChangeListener;
            }
            if (this.listChangeSize == 1) {
                this.currentValue = (ObservableList)this.observable.getValue();
            }
            return this;
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ListChangeListener<? super E> listChangeListener) {
            if (this.listChangeListeners != null) {
                for (int i = 0; i < this.listChangeSize; ++i) {
                    if (!listChangeListener.equals(this.listChangeListeners[i])) continue;
                    if (this.listChangeSize == 1) {
                        if (this.invalidationSize == 1 && this.changeSize == 0) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                        }
                        if (this.invalidationSize == 0 && this.changeSize == 1) {
                            return new SingleChange(this.observable, this.changeListeners[0]);
                        }
                        this.listChangeListeners = null;
                        this.listChangeSize = 0;
                        break;
                    }
                    if (this.listChangeSize == 2 && this.invalidationSize == 0 && this.changeSize == 0) {
                        return new SingleListChange(this.observable, this.listChangeListeners[1 - i]);
                    }
                    int n = this.listChangeSize - i - 1;
                    ListChangeListener<? super E>[] arrlistChangeListener = this.listChangeListeners;
                    if (this.locked) {
                        this.listChangeListeners = new ListChangeListener[this.listChangeListeners.length];
                        System.arraycopy(arrlistChangeListener, 0, this.listChangeListeners, 0, i + 1);
                    }
                    if (n > 0) {
                        System.arraycopy(arrlistChangeListener, i + 1, this.listChangeListeners, i, n);
                    }
                    --this.listChangeSize;
                    if (this.locked) break;
                    this.listChangeListeners[this.listChangeSize] = null;
                    break;
                }
            }
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            if (this.changeSize == 0 && this.listChangeSize == 0) {
                this.notifyListeners(this.currentValue, null, false);
            } else {
                ObservableList<E> observableList = this.currentValue;
                this.currentValue = (ObservableList)this.observable.getValue();
                if (this.currentValue != observableList) {
                    NonIterableChange.GenericAddRemoveChange genericAddRemoveChange = null;
                    if (this.listChangeSize > 0) {
                        int n = this.currentValue == null ? 0 : this.currentValue.size();
                        ObservableList observableList2 = observableList == null ? FXCollections.emptyObservableList() : FXCollections.unmodifiableObservableList(observableList);
                        genericAddRemoveChange = new NonIterableChange.GenericAddRemoveChange(0, n, observableList2, this.observable);
                    }
                    this.notifyListeners(observableList, genericAddRemoveChange, false);
                } else {
                    this.notifyListeners(this.currentValue, null, true);
                }
            }
        }

        @Override
        protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
            SourceAdapterChange<? extends E> sourceAdapterChange = this.listChangeSize == 0 ? null : new SourceAdapterChange<E>(this.observable, change);
            this.notifyListeners(this.currentValue, sourceAdapterChange, false);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void notifyListeners(ObservableList<E> observableList, ListChangeListener.Change<E> change, boolean bl) {
            InvalidationListener[] arrinvalidationListener = this.invalidationListeners;
            int n = this.invalidationSize;
            ChangeListener<? super ObservableList<E>>[] arrchangeListener = this.changeListeners;
            int n2 = this.changeSize;
            ListChangeListener<? super E>[] arrlistChangeListener = this.listChangeListeners;
            int n3 = this.listChangeSize;
            try {
                int n4;
                this.locked = true;
                for (n4 = 0; n4 < n; ++n4) {
                    arrinvalidationListener[n4].invalidated((Observable)this.observable);
                }
                if (!bl) {
                    for (n4 = 0; n4 < n2; ++n4) {
                        arrchangeListener[n4].changed((ObservableValue)this.observable, observableList, this.currentValue);
                    }
                    if (change != null) {
                        for (n4 = 0; n4 < n3; ++n4) {
                            change.reset();
                            arrlistChangeListener[n4].onChanged(change);
                        }
                    }
                }
            }
            finally {
                this.locked = false;
            }
        }
    }

    private static class SingleListChange<E>
    extends ListExpressionHelper<E> {
        private final ListChangeListener<? super E> listener;
        private ObservableList<E> currentValue;

        private SingleListChange(ObservableListValue<E> observableListValue, ListChangeListener<? super E> listChangeListener) {
            super(observableListValue);
            this.listener = listChangeListener;
            this.currentValue = (ObservableList)observableListValue.getValue();
        }

        @Override
        protected ListExpressionHelper<E> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.observable, invalidationListener, this.listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(InvalidationListener invalidationListener) {
            return this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ChangeListener<? super ObservableList<E>> changeListener) {
            return new Generic(this.observable, changeListener, this.listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ChangeListener<? super ObservableList<E>> changeListener) {
            return this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ListChangeListener<? super E> listChangeListener) {
            return new Generic(this.observable, this.listener, listChangeListener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ListChangeListener<? super E> listChangeListener) {
            return listChangeListener.equals(this.listener) ? null : this;
        }

        @Override
        protected void fireValueChangedEvent() {
            ObservableList<E> observableList = this.currentValue;
            this.currentValue = (ObservableList)this.observable.getValue();
            if (this.currentValue != observableList) {
                int n = this.currentValue == null ? 0 : this.currentValue.size();
                ObservableList observableList2 = observableList == null ? FXCollections.emptyObservableList() : FXCollections.unmodifiableObservableList(observableList);
                NonIterableChange.GenericAddRemoveChange genericAddRemoveChange = new NonIterableChange.GenericAddRemoveChange(0, n, observableList2, this.observable);
                this.listener.onChanged(genericAddRemoveChange);
            }
        }

        @Override
        protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
            this.listener.onChanged(new SourceAdapterChange<E>(this.observable, change));
        }
    }

    private static class SingleChange<E>
    extends ListExpressionHelper<E> {
        private final ChangeListener<? super ObservableList<E>> listener;
        private ObservableList<E> currentValue;

        private SingleChange(ObservableListValue<E> observableListValue, ChangeListener<? super ObservableList<E>> changeListener) {
            super(observableListValue);
            this.listener = changeListener;
            this.currentValue = (ObservableList)observableListValue.getValue();
        }

        @Override
        protected ListExpressionHelper<E> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.observable, invalidationListener, this.listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(InvalidationListener invalidationListener) {
            return this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ChangeListener<? super ObservableList<E>> changeListener) {
            return new Generic(this.observable, this.listener, changeListener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ChangeListener<? super ObservableList<E>> changeListener) {
            return changeListener.equals(this.listener) ? null : this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ListChangeListener<? super E> listChangeListener) {
            return new Generic(this.observable, this.listener, listChangeListener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ListChangeListener<? super E> listChangeListener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            ObservableList<E> observableList = this.currentValue;
            this.currentValue = (ObservableList)this.observable.getValue();
            if (this.currentValue != observableList) {
                this.listener.changed((ObservableValue)this.observable, observableList, this.currentValue);
            }
        }

        @Override
        protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
            this.listener.changed((ObservableValue)this.observable, this.currentValue, this.currentValue);
        }
    }

    private static class SingleInvalidation<E>
    extends ListExpressionHelper<E> {
        private final InvalidationListener listener;

        private SingleInvalidation(ObservableListValue<E> observableListValue, InvalidationListener invalidationListener) {
            super(observableListValue);
            this.listener = invalidationListener;
        }

        @Override
        protected ListExpressionHelper<E> addListener(InvalidationListener invalidationListener) {
            return new Generic(this.observable, this.listener, invalidationListener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(InvalidationListener invalidationListener) {
            return invalidationListener.equals((Object)this.listener) ? null : this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ChangeListener<? super ObservableList<E>> changeListener) {
            return new Generic(this.observable, this.listener, changeListener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ChangeListener<? super ObservableList<E>> changeListener) {
            return this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ListChangeListener<? super E> listChangeListener) {
            return new Generic(this.observable, this.listener, listChangeListener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ListChangeListener<? super E> listChangeListener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            this.listener.invalidated((Observable)this.observable);
        }

        @Override
        protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
            this.listener.invalidated((Observable)this.observable);
        }
    }
}

