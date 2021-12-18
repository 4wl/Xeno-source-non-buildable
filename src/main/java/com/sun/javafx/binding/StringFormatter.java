/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.Observable
 *  javafx.beans.binding.StringBinding
 *  javafx.beans.binding.StringExpression
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableList
 */
package com.sun.javafx.binding;

import com.sun.javafx.binding.StringConstant;
import java.util.ArrayList;
import java.util.Locale;
import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class StringFormatter
extends StringBinding {
    private static Object extractValue(Object object) {
        return object instanceof ObservableValue ? ((ObservableValue)object).getValue() : object;
    }

    private static Object[] extractValues(Object[] arrobject) {
        int n = arrobject.length;
        Object[] arrobject2 = new Object[n];
        for (int i = 0; i < n; ++i) {
            arrobject2[i] = StringFormatter.extractValue(arrobject[i]);
        }
        return arrobject2;
    }

    private static ObservableValue<?>[] extractDependencies(Object ... arrobject) {
        ArrayList<ObservableValue> arrayList = new ArrayList<ObservableValue>();
        for (Object object : arrobject) {
            if (!(object instanceof ObservableValue)) continue;
            arrayList.add((ObservableValue)object);
        }
        return arrayList.toArray((T[])new ObservableValue[arrayList.size()]);
    }

    public static StringExpression convert(final ObservableValue<?> observableValue) {
        if (observableValue == null) {
            throw new NullPointerException("ObservableValue must be specified");
        }
        if (observableValue instanceof StringExpression) {
            return (StringExpression)observableValue;
        }
        return new StringBinding(){
            {
                super.bind(new Observable[]{observableValue});
            }

            public void dispose() {
                super.unbind(new Observable[]{observableValue});
            }

            protected String computeValue() {
                Object object = observableValue.getValue();
                return object == null ? "null" : object.toString();
            }

            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.singletonObservableList((Object)observableValue);
            }
        };
    }

    public static StringExpression concat(final Object ... arrobject) {
        if (arrobject == null || arrobject.length == 0) {
            return StringConstant.valueOf("");
        }
        if (arrobject.length == 1) {
            Object object = arrobject[0];
            return object instanceof ObservableValue ? StringFormatter.convert((ObservableValue)object) : StringConstant.valueOf(object.toString());
        }
        if (StringFormatter.extractDependencies(arrobject).length == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object object : arrobject) {
                stringBuilder.append(object);
            }
            return StringConstant.valueOf(stringBuilder.toString());
        }
        return new StringFormatter(){
            {
                super.bind((Observable[])StringFormatter.extractDependencies(arrobject));
            }

            public void dispose() {
                super.unbind((Observable[])StringFormatter.extractDependencies(arrobject));
            }

            protected String computeValue() {
                StringBuilder stringBuilder = new StringBuilder();
                for (Object object : arrobject) {
                    stringBuilder.append(StringFormatter.extractValue(object));
                }
                return stringBuilder.toString();
            }

            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.unmodifiableObservableList((ObservableList)FXCollections.observableArrayList((Object[])StringFormatter.extractDependencies(arrobject)));
            }
        };
    }

    public static StringExpression format(final Locale locale, final String string, final Object ... arrobject) {
        if (string == null) {
            throw new NullPointerException("Format cannot be null.");
        }
        if (StringFormatter.extractDependencies(arrobject).length == 0) {
            return StringConstant.valueOf(String.format(locale, string, arrobject));
        }
        StringFormatter stringFormatter = new StringFormatter(){
            {
                super.bind((Observable[])StringFormatter.extractDependencies(arrobject));
            }

            public void dispose() {
                super.unbind((Observable[])StringFormatter.extractDependencies(arrobject));
            }

            protected String computeValue() {
                Object[] arrobject2 = StringFormatter.extractValues(arrobject);
                return String.format(locale, string, arrobject2);
            }

            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.unmodifiableObservableList((ObservableList)FXCollections.observableArrayList((Object[])StringFormatter.extractDependencies(arrobject)));
            }
        };
        stringFormatter.get();
        return stringFormatter;
    }

    public static StringExpression format(final String string, final Object ... arrobject) {
        if (string == null) {
            throw new NullPointerException("Format cannot be null.");
        }
        if (StringFormatter.extractDependencies(arrobject).length == 0) {
            return StringConstant.valueOf(String.format(string, arrobject));
        }
        StringFormatter stringFormatter = new StringFormatter(){
            {
                super.bind((Observable[])StringFormatter.extractDependencies(arrobject));
            }

            public void dispose() {
                super.unbind((Observable[])StringFormatter.extractDependencies(arrobject));
            }

            protected String computeValue() {
                Object[] arrobject2 = StringFormatter.extractValues(arrobject);
                return String.format(string, arrobject2);
            }

            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.unmodifiableObservableList((ObservableList)FXCollections.observableArrayList((Object[])StringFormatter.extractDependencies(arrobject)));
            }
        };
        stringFormatter.get();
        return stringFormatter;
    }
}

