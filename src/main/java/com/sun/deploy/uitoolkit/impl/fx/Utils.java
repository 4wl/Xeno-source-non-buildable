/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.applet2.Applet2Context
 *  com.sun.applet2.AppletParameters
 */
package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.applet2.Applet2Context;
import com.sun.applet2.AppletParameters;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Utils {
    static final Set<String> bannedNames = new HashSet<String>();
    static Object unnamedKey = null;

    public static Map<String, String> getNamedParameters(Applet2Context applet2Context) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        Map map = applet2Context.getParameters().rawMap();
        if (map != null) {
            for (Object k : map.keySet()) {
                if (!(k instanceof String) || bannedNames.contains((String)k)) continue;
                hashMap.put((String)k, (String)map.get(k));
            }
        }
        return hashMap;
    }

    public static String[] getUnnamed(Applet2Context applet2Context) {
        AppletParameters appletParameters = applet2Context.getParameters();
        return (String[])appletParameters.get(unnamedKey);
    }

    static {
        try {
            Class<?> class_ = Class.forName("sun.plugin2.util.ParameterNames", true, null);
            for (Field field : class_.getDeclaredFields()) {
                if (field.getType() == String.class) {
                    String string = (String)field.get(null);
                    bannedNames.add(string);
                    continue;
                }
                if (!"ARGUMENTS".equals(field.getName())) continue;
                unnamedKey = field.get(null);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

