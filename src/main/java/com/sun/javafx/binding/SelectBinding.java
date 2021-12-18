/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.beans.WeakInvalidationListener
 *  javafx.beans.binding.Binding
 *  javafx.beans.binding.BooleanBinding
 *  javafx.beans.binding.DoubleBinding
 *  javafx.beans.binding.FloatBinding
 *  javafx.beans.binding.IntegerBinding
 *  javafx.beans.binding.LongBinding
 *  javafx.beans.binding.ObjectBinding
 *  javafx.beans.binding.StringBinding
 *  javafx.beans.value.ObservableBooleanValue
 *  javafx.beans.value.ObservableNumberValue
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableList
 */
package com.sun.javafx.binding;

import com.sun.javafx.binding.Logging;
import com.sun.javafx.property.JavaBeanAccessHelper;
import com.sun.javafx.property.PropertyReference;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.util.logging.PlatformLogger;

public class SelectBinding {
    private SelectBinding() {
    }

    private static class SelectBindingHelper
    implements InvalidationListener {
        private final Binding<?> binding;
        private final String[] propertyNames;
        private final ObservableValue<?>[] properties;
        private final PropertyReference<?>[] propRefs;
        private final WeakInvalidationListener observer;
        private ObservableList<ObservableValue<?>> dependencies;

        private SelectBindingHelper(Binding<?> binding, ObservableValue<?> observableValue, String ... arrstring) {
            if (observableValue == null) {
                throw new NullPointerException("Must specify the root");
            }
            if (arrstring == null) {
                arrstring = new String[]{};
            }
            this.binding = binding;
            int n = arrstring.length;
            for (int i = 0; i < n; ++i) {
                if (arrstring[i] != null) continue;
                throw new NullPointerException("all steps must be specified");
            }
            this.observer = new WeakInvalidationListener((InvalidationListener)this);
            this.propertyNames = new String[n];
            System.arraycopy(arrstring, 0, this.propertyNames, 0, n);
            this.propRefs = new PropertyReference[n];
            this.properties = new ObservableValue[n + 1];
            this.properties[0] = observableValue;
            this.properties[0].addListener((InvalidationListener)this.observer);
        }

        private static ObservableValue<?> checkAndCreateFirstStep(Object object, String[] arrstring) {
            if (object == null || arrstring == null || arrstring[0] == null) {
                throw new NullPointerException("Must specify the root and the first property");
            }
            try {
                return JavaBeanAccessHelper.createReadOnlyJavaBeanProperty(object, arrstring[0]);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                throw new IllegalArgumentException("The first property '" + arrstring[0] + "' doesn't exist");
            }
        }

        private SelectBindingHelper(Binding<?> binding, Object object, String ... arrstring) {
            this(binding, SelectBindingHelper.checkAndCreateFirstStep(object, arrstring), Arrays.copyOfRange(arrstring, 1, arrstring.length));
        }

        public void invalidated(Observable observable) {
            this.binding.invalidate();
        }

        public ObservableValue<?> getObservableValue() {
            int n = this.properties.length;
            for (int i = 0; i < n - 1; ++i) {
                Object object = this.properties[i].getValue();
                try {
                    if (this.propRefs[i] == null || !object.getClass().equals(this.propRefs[i].getContainingClass())) {
                        this.propRefs[i] = new PropertyReference(object.getClass(), this.propertyNames[i]);
                    }
                    this.properties[i + 1] = this.propRefs[i].hasProperty() ? this.propRefs[i].getProperty(object) : JavaBeanAccessHelper.createReadOnlyJavaBeanProperty(object, this.propRefs[i].getName());
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    Logging.getLogger().warning("Exception while evaluating select-binding " + this.stepsToString(), noSuchMethodException);
                    this.updateDependencies();
                    return null;
                }
                catch (RuntimeException runtimeException) {
                    PlatformLogger platformLogger = Logging.getLogger();
                    if (platformLogger.isLoggable(PlatformLogger.Level.WARNING)) {
                        Logging.getLogger().warning("Exception while evaluating select-binding " + this.stepsToString());
                        if (runtimeException instanceof IllegalStateException) {
                            platformLogger.warning("Property '" + this.propertyNames[i] + "' does not exist in " + object.getClass(), runtimeException);
                        } else if (runtimeException instanceof NullPointerException) {
                            platformLogger.fine("Property '" + this.propertyNames[i] + "' in " + this.properties[i] + " is null", runtimeException);
                        } else {
                            Logging.getLogger().warning("", runtimeException);
                        }
                    }
                    this.updateDependencies();
                    return null;
                }
                this.properties[i + 1].addListener((InvalidationListener)this.observer);
            }
            this.updateDependencies();
            ObservableValue<?> observableValue = this.properties[n - 1];
            if (observableValue == null) {
                Logging.getLogger().fine("Property '" + this.propertyNames[n - 1] + "' in " + this.properties[n - 1] + " is null", new NullPointerException());
            }
            return observableValue;
        }

        private String stepsToString() {
            return Arrays.toString(this.propertyNames);
        }

        private void unregisterListener() {
            int n = this.properties.length;
            for (int i = 1; i < n && this.properties[i] != null; ++i) {
                this.properties[i].removeListener((InvalidationListener)this.observer);
                this.properties[i] = null;
            }
            this.updateDependencies();
        }

        private void updateDependencies() {
            if (this.dependencies != null) {
                this.dependencies.clear();
                int n = this.properties.length;
                for (int i = 0; i < n && this.properties[i] != null; ++i) {
                    this.dependencies.add(this.properties[i]);
                }
            }
        }

        public ObservableList<ObservableValue<?>> getDependencies() {
            if (this.dependencies == null) {
                this.dependencies = FXCollections.observableArrayList();
                this.updateDependencies();
            }
            return FXCollections.unmodifiableObservableList(this.dependencies);
        }
    }

    public static class AsString
    extends StringBinding {
        private static final String DEFAULT_VALUE = null;
        private final SelectBindingHelper helper;

        public AsString(ObservableValue<?> observableValue, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, (ObservableValue)observableValue, arrstring);
        }

        public AsString(Object object, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, object, arrstring);
        }

        public void dispose() {
            this.helper.unregisterListener();
        }

        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        protected String computeValue() {
            ObservableValue<?> observableValue = this.helper.getObservableValue();
            if (observableValue == null) {
                return DEFAULT_VALUE;
            }
            try {
                return observableValue.getValue().toString();
            }
            catch (RuntimeException runtimeException) {
                Logging.getLogger().warning("Exception while evaluating select-binding", runtimeException);
                return DEFAULT_VALUE;
            }
        }

        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    public static class AsLong
    extends LongBinding {
        private static final long DEFAULT_VALUE = 0L;
        private final SelectBindingHelper helper;

        public AsLong(ObservableValue<?> observableValue, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, (ObservableValue)observableValue, arrstring);
        }

        public AsLong(Object object, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, object, arrstring);
        }

        public void dispose() {
            this.helper.unregisterListener();
        }

        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        protected long computeValue() {
            ObservableValue<?> observableValue = this.helper.getObservableValue();
            if (observableValue == null) {
                return 0L;
            }
            if (observableValue instanceof ObservableNumberValue) {
                return ((ObservableNumberValue)observableValue).longValue();
            }
            try {
                return ((Number)observableValue.getValue()).longValue();
            }
            catch (NullPointerException nullPointerException) {
                Logging.getLogger().fine("Value of select binding is null, returning default value", nullPointerException);
            }
            catch (ClassCastException classCastException) {
                Logging.getLogger().warning("Exception while evaluating select-binding", classCastException);
            }
            return 0L;
        }

        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    public static class AsInteger
    extends IntegerBinding {
        private static final int DEFAULT_VALUE = 0;
        private final SelectBindingHelper helper;

        public AsInteger(ObservableValue<?> observableValue, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, (ObservableValue)observableValue, arrstring);
        }

        public AsInteger(Object object, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, object, arrstring);
        }

        public void dispose() {
            this.helper.unregisterListener();
        }

        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        protected int computeValue() {
            ObservableValue<?> observableValue = this.helper.getObservableValue();
            if (observableValue == null) {
                return 0;
            }
            if (observableValue instanceof ObservableNumberValue) {
                return ((ObservableNumberValue)observableValue).intValue();
            }
            try {
                return ((Number)observableValue.getValue()).intValue();
            }
            catch (NullPointerException nullPointerException) {
                Logging.getLogger().fine("Value of select binding is null, returning default value", nullPointerException);
            }
            catch (ClassCastException classCastException) {
                Logging.getLogger().warning("Exception while evaluating select-binding", classCastException);
            }
            return 0;
        }

        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    public static class AsFloat
    extends FloatBinding {
        private static final float DEFAULT_VALUE = 0.0f;
        private final SelectBindingHelper helper;

        public AsFloat(ObservableValue<?> observableValue, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, (ObservableValue)observableValue, arrstring);
        }

        public AsFloat(Object object, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, object, arrstring);
        }

        public void dispose() {
            this.helper.unregisterListener();
        }

        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        protected float computeValue() {
            ObservableValue<?> observableValue = this.helper.getObservableValue();
            if (observableValue == null) {
                return 0.0f;
            }
            if (observableValue instanceof ObservableNumberValue) {
                return ((ObservableNumberValue)observableValue).floatValue();
            }
            try {
                return ((Number)observableValue.getValue()).floatValue();
            }
            catch (NullPointerException nullPointerException) {
                Logging.getLogger().fine("Value of select binding is null, returning default value", nullPointerException);
            }
            catch (ClassCastException classCastException) {
                Logging.getLogger().warning("Exception while evaluating select-binding", classCastException);
            }
            return 0.0f;
        }

        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    public static class AsDouble
    extends DoubleBinding {
        private static final double DEFAULT_VALUE = 0.0;
        private final SelectBindingHelper helper;

        public AsDouble(ObservableValue<?> observableValue, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, (ObservableValue)observableValue, arrstring);
        }

        public AsDouble(Object object, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, object, arrstring);
        }

        public void dispose() {
            this.helper.unregisterListener();
        }

        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        protected double computeValue() {
            ObservableValue<?> observableValue = this.helper.getObservableValue();
            if (observableValue == null) {
                return 0.0;
            }
            if (observableValue instanceof ObservableNumberValue) {
                return ((ObservableNumberValue)observableValue).doubleValue();
            }
            try {
                return ((Number)observableValue.getValue()).doubleValue();
            }
            catch (NullPointerException nullPointerException) {
                Logging.getLogger().fine("Value of select binding is null, returning default value", nullPointerException);
            }
            catch (ClassCastException classCastException) {
                Logging.getLogger().warning("Exception while evaluating select-binding", classCastException);
            }
            return 0.0;
        }

        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    public static class AsBoolean
    extends BooleanBinding {
        private static final boolean DEFAULT_VALUE = false;
        private final SelectBindingHelper helper;

        public AsBoolean(ObservableValue<?> observableValue, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, (ObservableValue)observableValue, arrstring);
        }

        public AsBoolean(Object object, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, object, arrstring);
        }

        public void dispose() {
            this.helper.unregisterListener();
        }

        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        protected boolean computeValue() {
            ObservableValue<?> observableValue = this.helper.getObservableValue();
            if (observableValue == null) {
                return false;
            }
            if (observableValue instanceof ObservableBooleanValue) {
                return ((ObservableBooleanValue)observableValue).get();
            }
            try {
                return (Boolean)observableValue.getValue();
            }
            catch (NullPointerException nullPointerException) {
                Logging.getLogger().fine("Value of select binding is null, returning default value", nullPointerException);
            }
            catch (ClassCastException classCastException) {
                Logging.getLogger().warning("Value of select-binding has wrong type, returning default value.", classCastException);
            }
            return false;
        }

        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    public static class AsObject<T>
    extends ObjectBinding<T> {
        private final SelectBindingHelper helper;

        public AsObject(ObservableValue<?> observableValue, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, (ObservableValue)observableValue, arrstring);
        }

        public AsObject(Object object, String ... arrstring) {
            this.helper = new SelectBindingHelper((Binding)this, object, arrstring);
        }

        public void dispose() {
            this.helper.unregisterListener();
        }

        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        protected T computeValue() {
            ObservableValue<?> observableValue = this.helper.getObservableValue();
            if (observableValue == null) {
                return null;
            }
            try {
                return (T)observableValue.getValue();
            }
            catch (ClassCastException classCastException) {
                Logging.getLogger().warning("Value of select-binding has wrong type, returning null.", classCastException);
                return null;
            }
        }

        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }
}

