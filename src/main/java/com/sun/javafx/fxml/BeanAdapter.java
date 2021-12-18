/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 */
package com.sun.javafx.fxml;

import com.sun.javafx.fxml.PropertyNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.value.ObservableValue;
import sun.reflect.misc.FieldUtil;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

public class BeanAdapter
extends AbstractMap<String, Object> {
    private final Object bean;
    private static final HashMap<Class<?>, MethodCache> globalMethodCache = new HashMap();
    private final MethodCache localCache;
    public static final String GET_PREFIX = "get";
    public static final String IS_PREFIX = "is";
    public static final String SET_PREFIX = "set";
    public static final String PROPERTY_SUFFIX = "Property";
    public static final String VALUE_OF_METHOD_NAME = "valueOf";

    public BeanAdapter(Object object) {
        this.bean = object;
        this.localCache = BeanAdapter.getClassMethodCache(object.getClass());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static MethodCache getClassMethodCache(final Class<?> class_) {
        if (class_ == Object.class) {
            return null;
        }
        HashMap<Class<?>, MethodCache> hashMap = globalMethodCache;
        synchronized (hashMap) {
            Object object;
            MethodCache methodCache = globalMethodCache.get(class_);
            if (methodCache != null) {
                return methodCache;
            }
            HashMap<String, ArrayList<Method>> hashMap2 = new HashMap<String, ArrayList<Method>>();
            ReflectUtil.checkPackageAccess(class_);
            if (Modifier.isPublic(class_.getModifiers())) {
                object = AccessController.doPrivileged(new PrivilegedAction<Method[]>(){

                    @Override
                    public Method[] run() {
                        return class_.getDeclaredMethods();
                    }
                });
                for (int i = 0; i < ((Method[])object).length; ++i) {
                    Method method = object[i];
                    int n = method.getModifiers();
                    if (!Modifier.isPublic(n) || Modifier.isStatic(n)) continue;
                    String string = method.getName();
                    ArrayList<Method> arrayList = (ArrayList<Method>)hashMap2.get(string);
                    if (arrayList == null) {
                        arrayList = new ArrayList<Method>();
                        hashMap2.put(string, arrayList);
                    }
                    arrayList.add(method);
                }
            }
            object = new MethodCache(hashMap2, BeanAdapter.getClassMethodCache(class_.getSuperclass()));
            globalMethodCache.put(class_, (MethodCache)object);
            return object;
        }
    }

    public Object getBean() {
        return this.bean;
    }

    private Method getGetterMethod(String string) {
        Method method = this.localCache.getMethod(BeanAdapter.getMethodName(GET_PREFIX, string), new Class[0]);
        if (method == null) {
            method = this.localCache.getMethod(BeanAdapter.getMethodName(IS_PREFIX, string), new Class[0]);
        }
        return method;
    }

    private Method getSetterMethod(String string) {
        Class<?> class_ = this.getType(string);
        if (class_ == null) {
            throw new UnsupportedOperationException("Cannot determine type for property.");
        }
        return this.localCache.getMethod(BeanAdapter.getMethodName(SET_PREFIX, string), new Class[]{class_});
    }

    private static String getMethodName(String string, String string2) {
        return string + Character.toUpperCase(string2.charAt(0)) + string2.substring(1);
    }

    @Override
    public Object get(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return this.get(object.toString());
    }

    private Object get(String string) {
        Object object;
        Method method;
        Method method2 = method = string.endsWith(PROPERTY_SUFFIX) ? this.localCache.getMethod(string, new Class[0]) : this.getGetterMethod(string);
        if (method != null) {
            try {
                object = MethodUtil.invoke(method, this.bean, null);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException);
            }
        } else {
            object = null;
        }
        return object;
    }

    @Override
    public Object put(String string, Object object) {
        if (string == null) {
            throw new NullPointerException();
        }
        Method method = this.getSetterMethod(string);
        if (method == null) {
            throw new PropertyNotFoundException("Property \"" + string + "\" does not exist" + " or is read-only.");
        }
        try {
            MethodUtil.invoke(method, this.bean, new Object[]{BeanAdapter.coerce(object, this.getType(string))});
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new RuntimeException(invocationTargetException);
        }
        return null;
    }

    @Override
    public boolean containsKey(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return this.getType(object.toString()) != null;
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public boolean isReadOnly(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
        return this.getSetterMethod(string) == null;
    }

    public <T> ObservableValue<T> getPropertyModel(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
        return (ObservableValue)this.get(string + PROPERTY_SUFFIX);
    }

    public Class<?> getType(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
        Method method = this.getGetterMethod(string);
        return method == null ? null : method.getReturnType();
    }

    public Type getGenericType(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
        Method method = this.getGetterMethod(string);
        return method == null ? null : method.getGenericReturnType();
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = false;
        if (object instanceof BeanAdapter) {
            BeanAdapter beanAdapter = (BeanAdapter)object;
            bl = this.bean == beanAdapter.bean;
        }
        return bl;
    }

    @Override
    public int hashCode() {
        return this.bean == null ? -1 : this.bean.hashCode();
    }

    public static <T> T coerce(Object object, Class<? extends T> class_) {
        if (class_ == null) {
            throw new NullPointerException();
        }
        Object object2 = null;
        if (object == null) {
            object2 = null;
        } else if (class_.isAssignableFrom(object.getClass())) {
            object2 = object;
        } else if (class_ == Boolean.class || class_ == Boolean.TYPE) {
            object2 = Boolean.valueOf(object.toString());
        } else if (class_ == Character.class || class_ == Character.TYPE) {
            object2 = Character.valueOf(object.toString().charAt(0));
        } else if (class_ == Byte.class || class_ == Byte.TYPE) {
            object2 = object instanceof Number ? Byte.valueOf(((Number)object).byteValue()) : Byte.valueOf(object.toString());
        } else if (class_ == Short.class || class_ == Short.TYPE) {
            object2 = object instanceof Number ? Short.valueOf(((Number)object).shortValue()) : Short.valueOf(object.toString());
        } else if (class_ == Integer.class || class_ == Integer.TYPE) {
            object2 = object instanceof Number ? Integer.valueOf(((Number)object).intValue()) : Integer.valueOf(object.toString());
        } else if (class_ == Long.class || class_ == Long.TYPE) {
            object2 = object instanceof Number ? Long.valueOf(((Number)object).longValue()) : Long.valueOf(object.toString());
        } else if (class_ == BigInteger.class) {
            object2 = object instanceof Number ? BigInteger.valueOf(((Number)object).longValue()) : new BigInteger(object.toString());
        } else if (class_ == Float.class || class_ == Float.TYPE) {
            object2 = object instanceof Number ? Float.valueOf(((Number)object).floatValue()) : Float.valueOf(object.toString());
        } else if (class_ == Double.class || class_ == Double.TYPE) {
            object2 = object instanceof Number ? Double.valueOf(((Number)object).doubleValue()) : Double.valueOf(object.toString());
        } else if (class_ == Number.class) {
            String string = object.toString();
            object2 = string.contains(".") ? (Number)Double.valueOf(string) : (Number)Long.valueOf(string);
        } else if (class_ == BigDecimal.class) {
            object2 = object instanceof Number ? BigDecimal.valueOf(((Number)object).doubleValue()) : new BigDecimal(object.toString());
        } else {
            if (class_ == Class.class) {
                try {
                    String string = object.toString();
                    ReflectUtil.checkPackageAccess(string);
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    object2 = Class.forName(string, false, classLoader);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    throw new IllegalArgumentException(classNotFoundException);
                }
            }
            Class<?> class_2 = object.getClass();
            Method method = null;
            while (method == null && class_2 != null) {
                try {
                    ReflectUtil.checkPackageAccess(class_);
                    method = class_.getDeclaredMethod(VALUE_OF_METHOD_NAME, class_2);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
                if (method != null) continue;
                class_2 = class_2.getSuperclass();
            }
            if (method == null) {
                throw new IllegalArgumentException("Unable to coerce " + object + " to " + class_ + ".");
            }
            if (class_.isEnum() && object instanceof String && Character.isLowerCase(((String)object).charAt(0))) {
                object = BeanAdapter.toAllCaps((String)object);
            }
            try {
                object2 = MethodUtil.invoke(method, null, new Object[]{object});
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException);
            }
            catch (SecurityException securityException) {
                throw new RuntimeException(securityException);
            }
        }
        return (T)object2;
    }

    public static <T> T get(Object object, Class<?> class_, String string) {
        Object object2 = null;
        Class<?> class_2 = object.getClass();
        Method method = BeanAdapter.getStaticGetterMethod(class_, string, class_2);
        if (method != null) {
            try {
                object2 = MethodUtil.invoke(method, null, new Object[]{object});
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
        }
        return (T)object2;
    }

    public static void put(Object object, Class<?> class_, String string, Object object2) {
        Class<?> class_2;
        Class<?> class_3 = object.getClass();
        Method method = null;
        if (object2 != null) {
            method = BeanAdapter.getStaticSetterMethod(class_, string, object2.getClass(), class_3);
        }
        if (method == null && (class_2 = BeanAdapter.getType(class_, string, class_3)) != null) {
            method = BeanAdapter.getStaticSetterMethod(class_, string, class_2, class_3);
            object2 = BeanAdapter.coerce(object2, class_2);
        }
        if (method == null) {
            throw new PropertyNotFoundException("Static property \"" + string + "\" does not exist" + " or is read-only.");
        }
        try {
            MethodUtil.invoke(method, null, new Object[]{object, object2});
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new RuntimeException(invocationTargetException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException);
        }
    }

    public static boolean isDefined(Class<?> class_, String string, Class<?> class_2) {
        return BeanAdapter.getStaticGetterMethod(class_, string, class_2) != null;
    }

    public static Class<?> getType(Class<?> class_, String string, Class<?> class_2) {
        Method method = BeanAdapter.getStaticGetterMethod(class_, string, class_2);
        return method == null ? null : method.getReturnType();
    }

    public static Type getGenericType(Class<?> class_, String string, Class<?> class_2) {
        Method method = BeanAdapter.getStaticGetterMethod(class_, string, class_2);
        return method == null ? null : method.getGenericReturnType();
    }

    public static Class<?> getListItemType(Type type2) {
        Type type3 = BeanAdapter.getGenericListItemType(type2);
        if (type3 instanceof ParameterizedType) {
            type3 = ((ParameterizedType)type3).getRawType();
        }
        return (Class)type3;
    }

    public static Class<?> getMapValueType(Type type2) {
        Type type3 = BeanAdapter.getGenericMapValueType(type2);
        if (type3 instanceof ParameterizedType) {
            type3 = ((ParameterizedType)type3).getRawType();
        }
        return (Class)type3;
    }

    public static Type getGenericListItemType(Type type2) {
        Object object = null;
        Type type3 = type2;
        while (type3 != null) {
            Object object2;
            Type type4;
            if (type3 instanceof ParameterizedType) {
                type4 = (ParameterizedType)type3;
                object2 = (Class)type4.getRawType();
                if (!List.class.isAssignableFrom((Class<?>)object2)) break;
                object = type4.getActualTypeArguments()[0];
                break;
            }
            type4 = (Class)type3;
            object2 = ((Class)type4).getGenericInterfaces();
            for (int i = 0; i < ((Type[])object2).length; ++i) {
                ParameterizedType parameterizedType;
                Class class_;
                Type type5 = object2[i];
                if (!(type5 instanceof ParameterizedType) || !List.class.isAssignableFrom(class_ = (Class)(parameterizedType = (ParameterizedType)type5).getRawType())) continue;
                object = parameterizedType.getActualTypeArguments()[0];
                break;
            }
            if (object != null) break;
            type3 = ((Class)type4).getGenericSuperclass();
        }
        if (object != null && object instanceof TypeVariable) {
            object = Object.class;
        }
        return object;
    }

    public static Type getGenericMapValueType(Type type2) {
        Object object = null;
        Type type3 = type2;
        while (type3 != null) {
            Object object2;
            Type type4;
            if (type3 instanceof ParameterizedType) {
                type4 = (ParameterizedType)type3;
                object2 = (Class)type4.getRawType();
                if (!Map.class.isAssignableFrom((Class<?>)object2)) break;
                object = type4.getActualTypeArguments()[1];
                break;
            }
            type4 = (Class)type3;
            object2 = ((Class)type4).getGenericInterfaces();
            for (int i = 0; i < ((Type[])object2).length; ++i) {
                ParameterizedType parameterizedType;
                Class class_;
                Type type5 = object2[i];
                if (!(type5 instanceof ParameterizedType) || !Map.class.isAssignableFrom(class_ = (Class)(parameterizedType = (ParameterizedType)type5).getRawType())) continue;
                object = parameterizedType.getActualTypeArguments()[1];
                break;
            }
            if (object != null) break;
            type3 = ((Class)type4).getGenericSuperclass();
        }
        if (object != null && object instanceof TypeVariable) {
            object = Object.class;
        }
        return object;
    }

    public static Object getConstantValue(Class<?> class_, String string) {
        Object object;
        Field field;
        if (class_ == null) {
            throw new IllegalArgumentException();
        }
        if (string == null) {
            throw new IllegalArgumentException();
        }
        try {
            field = FieldUtil.getField(class_, string);
        }
        catch (NoSuchFieldException noSuchFieldException) {
            throw new IllegalArgumentException(noSuchFieldException);
        }
        int n = field.getModifiers();
        if ((n & 8) == 0 || (n & 0x10) == 0) {
            throw new IllegalArgumentException("Field is not a constant.");
        }
        try {
            object = field.get(null);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new IllegalArgumentException(illegalAccessException);
        }
        return object;
    }

    private static Method getStaticGetterMethod(Class<?> class_, String string, Class<?> class_2) {
        if (class_ == null) {
            throw new NullPointerException();
        }
        if (string == null) {
            throw new NullPointerException();
        }
        Method method = null;
        if (class_2 != null) {
            string = Character.toUpperCase(string.charAt(0)) + string.substring(1);
            String string2 = GET_PREFIX + string;
            String string3 = IS_PREFIX + string;
            try {
                method = MethodUtil.getMethod(class_, string2, new Class[]{class_2});
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            if (method == null) {
                try {
                    method = MethodUtil.getMethod(class_, string3, new Class[]{class_2});
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
            }
            if (method == null) {
                Class<?>[] arrclass = class_2.getInterfaces();
                for (int i = 0; i < arrclass.length; ++i) {
                    try {
                        method = MethodUtil.getMethod(class_, string2, new Class[]{arrclass[i]});
                    }
                    catch (NoSuchMethodException noSuchMethodException) {
                        // empty catch block
                    }
                    if (method == null) {
                        try {
                            method = MethodUtil.getMethod(class_, string3, new Class[]{arrclass[i]});
                        }
                        catch (NoSuchMethodException noSuchMethodException) {
                            // empty catch block
                        }
                    }
                    if (method != null) break;
                }
            }
            if (method == null) {
                method = BeanAdapter.getStaticGetterMethod(class_, string, class_2.getSuperclass());
            }
        }
        return method;
    }

    private static Method getStaticSetterMethod(Class<?> class_, String string, Class<?> class_2, Class<?> class_3) {
        if (class_ == null) {
            throw new NullPointerException();
        }
        if (string == null) {
            throw new NullPointerException();
        }
        if (class_2 == null) {
            throw new NullPointerException();
        }
        Method method = null;
        if (class_3 != null) {
            string = Character.toUpperCase(string.charAt(0)) + string.substring(1);
            String string2 = SET_PREFIX + string;
            try {
                method = MethodUtil.getMethod(class_, string2, new Class[]{class_3, class_2});
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            if (method == null) {
                Class<?>[] arrclass = class_3.getInterfaces();
                for (int i = 0; i < arrclass.length; ++i) {
                    try {
                        method = MethodUtil.getMethod(class_, string2, new Class[]{arrclass[i], class_2});
                    }
                    catch (NoSuchMethodException noSuchMethodException) {
                        // empty catch block
                    }
                    if (method != null) break;
                }
            }
            if (method == null) {
                method = BeanAdapter.getStaticSetterMethod(class_, string, class_2, class_3.getSuperclass());
            }
        }
        return method;
    }

    private static String toAllCaps(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n = string.length();
        for (int i = 0; i < n; ++i) {
            char c = string.charAt(i);
            if (Character.isUpperCase(c)) {
                stringBuilder.append('_');
            }
            stringBuilder.append(Character.toUpperCase(c));
        }
        return stringBuilder.toString();
    }

    private static class MethodCache {
        private final Map<String, List<Method>> methods;
        private final MethodCache nextClassCache;

        private MethodCache(Map<String, List<Method>> map, MethodCache methodCache) {
            this.methods = map;
            this.nextClassCache = methodCache;
        }

        private Method getMethod(String string, Class<?> ... arrclass) {
            List<Method> list = this.methods.get(string);
            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    Method method = list.get(i);
                    if (!method.getName().equals(string) || !Arrays.equals(method.getParameterTypes(), arrclass)) continue;
                    return method;
                }
            }
            return this.nextClassCache != null ? this.nextClassCache.getMethod(string, arrclass) : null;
        }
    }
}

