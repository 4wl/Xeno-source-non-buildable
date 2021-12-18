/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.Observable
 *  javafx.beans.WeakListener
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.FloatProperty
 *  javafx.beans.property.IntegerProperty
 *  javafx.beans.property.LongProperty
 *  javafx.beans.property.Property
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.util.StringConverter
 */
package com.sun.javafx.binding;

import com.sun.javafx.binding.Logging;
import java.lang.ref.WeakReference;
import java.text.Format;
import java.text.ParseException;
import javafx.beans.Observable;
import javafx.beans.WeakListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.StringConverter;

public abstract class BidirectionalBinding<T>
implements ChangeListener<T>,
WeakListener {
    private final int cachedHashCode;

    private static void checkParameters(Object object, Object object2) {
        if (object == null || object2 == null) {
            throw new NullPointerException("Both properties must be specified.");
        }
        if (object == object2) {
            throw new IllegalArgumentException("Cannot bind property to itself");
        }
    }

    public static <T> BidirectionalBinding bind(Property<T> property, Property<T> property2) {
        BidirectionalBinding.checkParameters(property, property2);
        BidirectionalBinding bidirectionalBinding = property instanceof DoubleProperty && property2 instanceof DoubleProperty ? new BidirectionalDoubleBinding((DoubleProperty)property, (DoubleProperty)property2) : (property instanceof FloatProperty && property2 instanceof FloatProperty ? new BidirectionalFloatBinding((FloatProperty)property, (FloatProperty)property2) : (property instanceof IntegerProperty && property2 instanceof IntegerProperty ? new BidirectionalIntegerBinding((IntegerProperty)property, (IntegerProperty)property2) : (property instanceof LongProperty && property2 instanceof LongProperty ? new BidirectionalLongBinding((LongProperty)property, (LongProperty)property2) : (property instanceof BooleanProperty && property2 instanceof BooleanProperty ? new BidirectionalBooleanBinding((BooleanProperty)property, (BooleanProperty)property2) : new TypedGenericBidirectionalBinding(property, property2)))));
        property.setValue(property2.getValue());
        property.addListener((ChangeListener)bidirectionalBinding);
        property2.addListener((ChangeListener)bidirectionalBinding);
        return bidirectionalBinding;
    }

    public static Object bind(Property<String> property, Property<?> property2, Format format) {
        BidirectionalBinding.checkParameters(property, property2);
        if (format == null) {
            throw new NullPointerException("Format cannot be null");
        }
        StringFormatBidirectionalBinding stringFormatBidirectionalBinding = new StringFormatBidirectionalBinding(property, property2, format);
        property.setValue((Object)format.format(property2.getValue()));
        property.addListener((ChangeListener)stringFormatBidirectionalBinding);
        property2.addListener((ChangeListener)stringFormatBidirectionalBinding);
        return stringFormatBidirectionalBinding;
    }

    public static <T> Object bind(Property<String> property, Property<T> property2, StringConverter<T> stringConverter) {
        BidirectionalBinding.checkParameters(property, property2);
        if (stringConverter == null) {
            throw new NullPointerException("Converter cannot be null");
        }
        StringConverterBidirectionalBinding<T> stringConverterBidirectionalBinding = new StringConverterBidirectionalBinding<T>(property, property2, stringConverter);
        property.setValue((Object)stringConverter.toString(property2.getValue()));
        property.addListener(stringConverterBidirectionalBinding);
        property2.addListener(stringConverterBidirectionalBinding);
        return stringConverterBidirectionalBinding;
    }

    public static <T> void unbind(Property<T> property, Property<T> property2) {
        BidirectionalBinding.checkParameters(property, property2);
        UntypedGenericBidirectionalBinding untypedGenericBidirectionalBinding = new UntypedGenericBidirectionalBinding(property, property2);
        property.removeListener((ChangeListener)untypedGenericBidirectionalBinding);
        property2.removeListener((ChangeListener)untypedGenericBidirectionalBinding);
    }

    public static void unbind(Object object, Object object2) {
        BidirectionalBinding.checkParameters(object, object2);
        UntypedGenericBidirectionalBinding untypedGenericBidirectionalBinding = new UntypedGenericBidirectionalBinding(object, object2);
        if (object instanceof ObservableValue) {
            ((ObservableValue)object).removeListener((ChangeListener)untypedGenericBidirectionalBinding);
        }
        if (object2 instanceof Observable) {
            ((ObservableValue)object2).removeListener((ChangeListener)untypedGenericBidirectionalBinding);
        }
    }

    public static BidirectionalBinding bindNumber(Property<Integer> property, IntegerProperty integerProperty) {
        return BidirectionalBinding.bindNumber(property, (Property<Number>)integerProperty);
    }

    public static BidirectionalBinding bindNumber(Property<Long> property, LongProperty longProperty) {
        return BidirectionalBinding.bindNumber(property, (Property<Number>)longProperty);
    }

    public static BidirectionalBinding bindNumber(Property<Float> property, FloatProperty floatProperty) {
        return BidirectionalBinding.bindNumber(property, (Property<Number>)floatProperty);
    }

    public static BidirectionalBinding bindNumber(Property<Double> property, DoubleProperty doubleProperty) {
        return BidirectionalBinding.bindNumber(property, (Property<Number>)doubleProperty);
    }

    public static BidirectionalBinding bindNumber(IntegerProperty integerProperty, Property<Integer> property) {
        return BidirectionalBinding.bindNumberObject((Property<Number>)integerProperty, property);
    }

    public static BidirectionalBinding bindNumber(LongProperty longProperty, Property<Long> property) {
        return BidirectionalBinding.bindNumberObject((Property<Number>)longProperty, property);
    }

    public static BidirectionalBinding bindNumber(FloatProperty floatProperty, Property<Float> property) {
        return BidirectionalBinding.bindNumberObject((Property<Number>)floatProperty, property);
    }

    public static BidirectionalBinding bindNumber(DoubleProperty doubleProperty, Property<Double> property) {
        return BidirectionalBinding.bindNumberObject((Property<Number>)doubleProperty, property);
    }

    private static <T extends Number> BidirectionalBinding bindNumberObject(Property<Number> property, Property<T> property2) {
        BidirectionalBinding.checkParameters(property, property2);
        TypedNumberBidirectionalBinding typedNumberBidirectionalBinding = new TypedNumberBidirectionalBinding(property2, property);
        property.setValue(property2.getValue());
        property.addListener(typedNumberBidirectionalBinding);
        property2.addListener(typedNumberBidirectionalBinding);
        return typedNumberBidirectionalBinding;
    }

    private static <T extends Number> BidirectionalBinding bindNumber(Property<T> property, Property<Number> property2) {
        BidirectionalBinding.checkParameters(property, property2);
        TypedNumberBidirectionalBinding typedNumberBidirectionalBinding = new TypedNumberBidirectionalBinding(property, property2);
        property.setValue((Object)((Number)property2.getValue()));
        property.addListener(typedNumberBidirectionalBinding);
        property2.addListener(typedNumberBidirectionalBinding);
        return typedNumberBidirectionalBinding;
    }

    public static <T extends Number> void unbindNumber(Property<T> property, Property<Number> property2) {
        BidirectionalBinding.checkParameters(property, property2);
        UntypedGenericBidirectionalBinding untypedGenericBidirectionalBinding = new UntypedGenericBidirectionalBinding(property, property2);
        if (property instanceof ObservableValue) {
            property.removeListener((ChangeListener)untypedGenericBidirectionalBinding);
        }
        if (property2 instanceof Observable) {
            property2.removeListener((ChangeListener)untypedGenericBidirectionalBinding);
        }
    }

    private BidirectionalBinding(Object object, Object object2) {
        this.cachedHashCode = object.hashCode() * object2.hashCode();
    }

    protected abstract Object getProperty1();

    protected abstract Object getProperty2();

    public int hashCode() {
        return this.cachedHashCode;
    }

    public boolean wasGarbageCollected() {
        return this.getProperty1() == null || this.getProperty2() == null;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        Object object2 = this.getProperty1();
        Object object3 = this.getProperty2();
        if (object2 == null || object3 == null) {
            return false;
        }
        if (object instanceof BidirectionalBinding) {
            BidirectionalBinding bidirectionalBinding = (BidirectionalBinding)object;
            Object object4 = bidirectionalBinding.getProperty1();
            Object object5 = bidirectionalBinding.getProperty2();
            if (object4 == null || object5 == null) {
                return false;
            }
            if (object2 == object4 && object3 == object5) {
                return true;
            }
            if (object2 == object5 && object3 == object4) {
                return true;
            }
        }
        return false;
    }

    private static class StringConverterBidirectionalBinding<T>
    extends StringConversionBidirectionalBinding<T> {
        private final StringConverter<T> converter;

        public StringConverterBidirectionalBinding(Property<String> property, Property<T> property2, StringConverter<T> stringConverter) {
            super(property, property2);
            this.converter = stringConverter;
        }

        @Override
        protected String toString(T t) {
            return this.converter.toString(t);
        }

        @Override
        protected T fromString(String string) throws ParseException {
            return (T)this.converter.fromString(string);
        }
    }

    private static class StringFormatBidirectionalBinding
    extends StringConversionBidirectionalBinding {
        private final Format format;

        public StringFormatBidirectionalBinding(Property<String> property, Property<?> property2, Format format) {
            super(property, property2);
            this.format = format;
        }

        protected String toString(Object object) {
            return this.format.format(object);
        }

        protected Object fromString(String string) throws ParseException {
            return this.format.parseObject(string);
        }
    }

    public static abstract class StringConversionBidirectionalBinding<T>
    extends BidirectionalBinding<Object> {
        private final WeakReference<Property<String>> stringPropertyRef;
        private final WeakReference<Property<T>> otherPropertyRef;
        private boolean updating;

        public StringConversionBidirectionalBinding(Property<String> property, Property<T> property2) {
            super(property, property2);
            this.stringPropertyRef = new WeakReference<Property<String>>(property);
            this.otherPropertyRef = new WeakReference<Property<Property<T>>>(property2);
        }

        protected abstract String toString(T var1);

        protected abstract T fromString(String var1) throws ParseException;

        @Override
        protected Object getProperty1() {
            return this.stringPropertyRef.get();
        }

        @Override
        protected Object getProperty2() {
            return this.otherPropertyRef.get();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void changed(ObservableValue<? extends Object> observableValue, Object object, Object object2) {
            if (!this.updating) {
                Property property = (Property)this.stringPropertyRef.get();
                Property property2 = (Property)this.otherPropertyRef.get();
                if (property == null || property2 == null) {
                    if (property != null) {
                        property.removeListener((ChangeListener)this);
                    }
                    if (property2 != null) {
                        property2.removeListener((ChangeListener)this);
                    }
                } else {
                    try {
                        this.updating = true;
                        if (property == observableValue) {
                            try {
                                property2.setValue(this.fromString((String)property.getValue()));
                            }
                            catch (Exception exception) {
                                Logging.getLogger().warning("Exception while parsing String in bidirectional binding", exception);
                                property2.setValue(null);
                            }
                        } else {
                            try {
                                property.setValue((Object)this.toString(property2.getValue()));
                            }
                            catch (Exception exception) {
                                Logging.getLogger().warning("Exception while converting Object to String in bidirectional binding", exception);
                                property.setValue((Object)"");
                            }
                        }
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }
    }

    private static class UntypedGenericBidirectionalBinding
    extends BidirectionalBinding<Object> {
        private final Object property1;
        private final Object property2;

        public UntypedGenericBidirectionalBinding(Object object, Object object2) {
            super(object, object2);
            this.property1 = object;
            this.property2 = object2;
        }

        @Override
        protected Object getProperty1() {
            return this.property1;
        }

        @Override
        protected Object getProperty2() {
            return this.property2;
        }

        public void changed(ObservableValue<? extends Object> observableValue, Object object, Object object2) {
            throw new RuntimeException("Should not reach here");
        }
    }

    private static class TypedNumberBidirectionalBinding<T extends Number>
    extends BidirectionalBinding<Number> {
        private final WeakReference<Property<T>> propertyRef1;
        private final WeakReference<Property<Number>> propertyRef2;
        private boolean updating = false;

        private TypedNumberBidirectionalBinding(Property<T> property, Property<Number> property2) {
            super(property, property2);
            this.propertyRef1 = new WeakReference<Property<Property<T>>>(property);
            this.propertyRef2 = new WeakReference<Property<Number>>(property2);
        }

        protected Property<T> getProperty1() {
            return (Property)this.propertyRef1.get();
        }

        protected Property<Number> getProperty2() {
            return (Property)this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            if (!this.updating) {
                Property property = (Property)this.propertyRef1.get();
                Property property2 = (Property)this.propertyRef2.get();
                if (property == null || property2 == null) {
                    if (property != null) {
                        property.removeListener((ChangeListener)this);
                    }
                    if (property2 != null) {
                        property2.removeListener((ChangeListener)this);
                    }
                } else {
                    try {
                        this.updating = true;
                        if (property == observableValue) {
                            property2.setValue((Object)number2);
                        } else {
                            property.setValue((Object)number2);
                        }
                    }
                    catch (RuntimeException runtimeException) {
                        try {
                            if (property == observableValue) {
                                property.setValue((Object)number);
                            } else {
                                property2.setValue((Object)number);
                            }
                        }
                        catch (Exception exception) {
                            exception.addSuppressed(runtimeException);
                            TypedNumberBidirectionalBinding.unbind((Object)property, (Object)property2);
                            throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + (Object)property + " and " + (Object)property2, exception);
                        }
                        throw new RuntimeException("Bidirectional binding failed, setting to the previous value", runtimeException);
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }
    }

    private static class TypedGenericBidirectionalBinding<T>
    extends BidirectionalBinding<T> {
        private final WeakReference<Property<T>> propertyRef1;
        private final WeakReference<Property<T>> propertyRef2;
        private boolean updating = false;

        private TypedGenericBidirectionalBinding(Property<T> property, Property<T> property2) {
            super(property, property2);
            this.propertyRef1 = new WeakReference<Property<Property<T>>>(property);
            this.propertyRef2 = new WeakReference<Property<Property<T>>>(property2);
        }

        protected Property<T> getProperty1() {
            return (Property)this.propertyRef1.get();
        }

        protected Property<T> getProperty2() {
            return (Property)this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends T> observableValue, T t, T t2) {
            if (!this.updating) {
                Property property = (Property)this.propertyRef1.get();
                Property property2 = (Property)this.propertyRef2.get();
                if (property == null || property2 == null) {
                    if (property != null) {
                        property.removeListener((ChangeListener)this);
                    }
                    if (property2 != null) {
                        property2.removeListener((ChangeListener)this);
                    }
                } else {
                    try {
                        this.updating = true;
                        if (property == observableValue) {
                            property2.setValue(t2);
                        } else {
                            property.setValue(t2);
                        }
                    }
                    catch (RuntimeException runtimeException) {
                        try {
                            if (property == observableValue) {
                                property.setValue(t);
                            } else {
                                property2.setValue(t);
                            }
                        }
                        catch (Exception exception) {
                            exception.addSuppressed(runtimeException);
                            TypedGenericBidirectionalBinding.unbind(property, property2);
                            throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + (Object)property + " and " + (Object)property2, exception);
                        }
                        throw new RuntimeException("Bidirectional binding failed, setting to the previous value", runtimeException);
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }
    }

    private static class BidirectionalLongBinding
    extends BidirectionalBinding<Number> {
        private final WeakReference<LongProperty> propertyRef1;
        private final WeakReference<LongProperty> propertyRef2;
        private boolean updating = false;

        private BidirectionalLongBinding(LongProperty longProperty, LongProperty longProperty2) {
            super((Object)longProperty, (Object)longProperty2);
            this.propertyRef1 = new WeakReference<LongProperty>(longProperty);
            this.propertyRef2 = new WeakReference<LongProperty>(longProperty2);
        }

        protected Property<Number> getProperty1() {
            return (Property)this.propertyRef1.get();
        }

        protected Property<Number> getProperty2() {
            return (Property)this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            if (!this.updating) {
                LongProperty longProperty = (LongProperty)this.propertyRef1.get();
                LongProperty longProperty2 = (LongProperty)this.propertyRef2.get();
                if (longProperty == null || longProperty2 == null) {
                    if (longProperty != null) {
                        longProperty.removeListener((ChangeListener)this);
                    }
                    if (longProperty2 != null) {
                        longProperty2.removeListener((ChangeListener)this);
                    }
                } else {
                    try {
                        this.updating = true;
                        if (longProperty == observableValue) {
                            longProperty2.set(number2.longValue());
                        } else {
                            longProperty.set(number2.longValue());
                        }
                    }
                    catch (RuntimeException runtimeException) {
                        try {
                            if (longProperty == observableValue) {
                                longProperty.set(number.longValue());
                            } else {
                                longProperty2.set(number.longValue());
                            }
                        }
                        catch (Exception exception) {
                            exception.addSuppressed(runtimeException);
                            BidirectionalLongBinding.unbind(longProperty, longProperty2);
                            throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + (Object)longProperty + " and " + (Object)longProperty2, exception);
                        }
                        throw new RuntimeException("Bidirectional binding failed, setting to the previous value", runtimeException);
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }
    }

    private static class BidirectionalIntegerBinding
    extends BidirectionalBinding<Number> {
        private final WeakReference<IntegerProperty> propertyRef1;
        private final WeakReference<IntegerProperty> propertyRef2;
        private boolean updating = false;

        private BidirectionalIntegerBinding(IntegerProperty integerProperty, IntegerProperty integerProperty2) {
            super((Object)integerProperty, (Object)integerProperty2);
            this.propertyRef1 = new WeakReference<IntegerProperty>(integerProperty);
            this.propertyRef2 = new WeakReference<IntegerProperty>(integerProperty2);
        }

        protected Property<Number> getProperty1() {
            return (Property)this.propertyRef1.get();
        }

        protected Property<Number> getProperty2() {
            return (Property)this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            if (!this.updating) {
                IntegerProperty integerProperty = (IntegerProperty)this.propertyRef1.get();
                IntegerProperty integerProperty2 = (IntegerProperty)this.propertyRef2.get();
                if (integerProperty == null || integerProperty2 == null) {
                    if (integerProperty != null) {
                        integerProperty.removeListener((ChangeListener)this);
                    }
                    if (integerProperty2 != null) {
                        integerProperty2.removeListener((ChangeListener)this);
                    }
                } else {
                    try {
                        this.updating = true;
                        if (integerProperty == observableValue) {
                            integerProperty2.set(number2.intValue());
                        } else {
                            integerProperty.set(number2.intValue());
                        }
                    }
                    catch (RuntimeException runtimeException) {
                        try {
                            if (integerProperty == observableValue) {
                                integerProperty.set(number.intValue());
                            } else {
                                integerProperty2.set(number.intValue());
                            }
                        }
                        catch (Exception exception) {
                            exception.addSuppressed(runtimeException);
                            BidirectionalIntegerBinding.unbind(integerProperty, integerProperty2);
                            throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + (Object)integerProperty + " and " + (Object)integerProperty2, exception);
                        }
                        throw new RuntimeException("Bidirectional binding failed, setting to the previous value", runtimeException);
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }
    }

    private static class BidirectionalFloatBinding
    extends BidirectionalBinding<Number> {
        private final WeakReference<FloatProperty> propertyRef1;
        private final WeakReference<FloatProperty> propertyRef2;
        private boolean updating = false;

        private BidirectionalFloatBinding(FloatProperty floatProperty, FloatProperty floatProperty2) {
            super((Object)floatProperty, (Object)floatProperty2);
            this.propertyRef1 = new WeakReference<FloatProperty>(floatProperty);
            this.propertyRef2 = new WeakReference<FloatProperty>(floatProperty2);
        }

        protected Property<Number> getProperty1() {
            return (Property)this.propertyRef1.get();
        }

        protected Property<Number> getProperty2() {
            return (Property)this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            if (!this.updating) {
                FloatProperty floatProperty = (FloatProperty)this.propertyRef1.get();
                FloatProperty floatProperty2 = (FloatProperty)this.propertyRef2.get();
                if (floatProperty == null || floatProperty2 == null) {
                    if (floatProperty != null) {
                        floatProperty.removeListener((ChangeListener)this);
                    }
                    if (floatProperty2 != null) {
                        floatProperty2.removeListener((ChangeListener)this);
                    }
                } else {
                    try {
                        this.updating = true;
                        if (floatProperty == observableValue) {
                            floatProperty2.set(number2.floatValue());
                        } else {
                            floatProperty.set(number2.floatValue());
                        }
                    }
                    catch (RuntimeException runtimeException) {
                        try {
                            if (floatProperty == observableValue) {
                                floatProperty.set(number.floatValue());
                            } else {
                                floatProperty2.set(number.floatValue());
                            }
                        }
                        catch (Exception exception) {
                            exception.addSuppressed(runtimeException);
                            BidirectionalFloatBinding.unbind(floatProperty, floatProperty2);
                            throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + (Object)floatProperty + " and " + (Object)floatProperty2, exception);
                        }
                        throw new RuntimeException("Bidirectional binding failed, setting to the previous value", runtimeException);
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }
    }

    private static class BidirectionalDoubleBinding
    extends BidirectionalBinding<Number> {
        private final WeakReference<DoubleProperty> propertyRef1;
        private final WeakReference<DoubleProperty> propertyRef2;
        private boolean updating = false;

        private BidirectionalDoubleBinding(DoubleProperty doubleProperty, DoubleProperty doubleProperty2) {
            super((Object)doubleProperty, (Object)doubleProperty2);
            this.propertyRef1 = new WeakReference<DoubleProperty>(doubleProperty);
            this.propertyRef2 = new WeakReference<DoubleProperty>(doubleProperty2);
        }

        protected Property<Number> getProperty1() {
            return (Property)this.propertyRef1.get();
        }

        protected Property<Number> getProperty2() {
            return (Property)this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            if (!this.updating) {
                DoubleProperty doubleProperty = (DoubleProperty)this.propertyRef1.get();
                DoubleProperty doubleProperty2 = (DoubleProperty)this.propertyRef2.get();
                if (doubleProperty == null || doubleProperty2 == null) {
                    if (doubleProperty != null) {
                        doubleProperty.removeListener((ChangeListener)this);
                    }
                    if (doubleProperty2 != null) {
                        doubleProperty2.removeListener((ChangeListener)this);
                    }
                } else {
                    try {
                        this.updating = true;
                        if (doubleProperty == observableValue) {
                            doubleProperty2.set(number2.doubleValue());
                        } else {
                            doubleProperty.set(number2.doubleValue());
                        }
                    }
                    catch (RuntimeException runtimeException) {
                        try {
                            if (doubleProperty == observableValue) {
                                doubleProperty.set(number.doubleValue());
                            } else {
                                doubleProperty2.set(number.doubleValue());
                            }
                        }
                        catch (Exception exception) {
                            exception.addSuppressed(runtimeException);
                            BidirectionalDoubleBinding.unbind(doubleProperty, doubleProperty2);
                            throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + (Object)doubleProperty + " and " + (Object)doubleProperty2, exception);
                        }
                        throw new RuntimeException("Bidirectional binding failed, setting to the previous value", runtimeException);
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }
    }

    private static class BidirectionalBooleanBinding
    extends BidirectionalBinding<Boolean> {
        private final WeakReference<BooleanProperty> propertyRef1;
        private final WeakReference<BooleanProperty> propertyRef2;
        private boolean updating = false;

        private BidirectionalBooleanBinding(BooleanProperty booleanProperty, BooleanProperty booleanProperty2) {
            super((Object)booleanProperty, (Object)booleanProperty2);
            this.propertyRef1 = new WeakReference<BooleanProperty>(booleanProperty);
            this.propertyRef2 = new WeakReference<BooleanProperty>(booleanProperty2);
        }

        protected Property<Boolean> getProperty1() {
            return (Property)this.propertyRef1.get();
        }

        protected Property<Boolean> getProperty2() {
            return (Property)this.propertyRef2.get();
        }

        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean bl, Boolean bl2) {
            if (!this.updating) {
                BooleanProperty booleanProperty = (BooleanProperty)this.propertyRef1.get();
                BooleanProperty booleanProperty2 = (BooleanProperty)this.propertyRef2.get();
                if (booleanProperty == null || booleanProperty2 == null) {
                    if (booleanProperty != null) {
                        booleanProperty.removeListener((ChangeListener)this);
                    }
                    if (booleanProperty2 != null) {
                        booleanProperty2.removeListener((ChangeListener)this);
                    }
                } else {
                    try {
                        this.updating = true;
                        if (booleanProperty == observableValue) {
                            booleanProperty2.set(bl2.booleanValue());
                        } else {
                            booleanProperty.set(bl2.booleanValue());
                        }
                    }
                    catch (RuntimeException runtimeException) {
                        try {
                            if (booleanProperty == observableValue) {
                                booleanProperty.set(bl.booleanValue());
                            } else {
                                booleanProperty2.set(bl.booleanValue());
                            }
                        }
                        catch (Exception exception) {
                            exception.addSuppressed(runtimeException);
                            BidirectionalBooleanBinding.unbind(booleanProperty, booleanProperty2);
                            throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + (Object)booleanProperty + " and " + (Object)booleanProperty2, exception);
                        }
                        throw new RuntimeException("Bidirectional binding failed, setting to the previous value", runtimeException);
                    }
                    finally {
                        this.updating = false;
                    }
                }
            }
        }
    }
}

