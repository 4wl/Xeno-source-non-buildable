/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.IDN;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

final class PublicSuffixes {
    private static final Logger logger = Logger.getLogger(PublicSuffixes.class.getName());
    private static final Map<String, Rule> RULES = PublicSuffixes.loadRules("effective_tld_names.dat");

    private PublicSuffixes() {
        throw new AssertionError();
    }

    static boolean isPublicSuffix(String string) {
        String string2;
        if (string.length() == 0) {
            return false;
        }
        Rule rule = RULES.get(string);
        if (rule == Rule.EXCEPTION_RULE) {
            return false;
        }
        if (rule == Rule.SIMPLE_RULE || rule == Rule.WILDCARD_RULE) {
            return true;
        }
        int n = string.indexOf(46) + 1;
        if (n == 0) {
            n = string.length();
        }
        return RULES.get(string2 = string.substring(n)) == Rule.WILDCARD_RULE;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Map<String, Rule> loadRules(String string) {
        logger.log(Level.FINEST, "resourceName: [{0}]", string);
        Map<String, Rule> map = null;
        InputStream inputStream = PublicSuffixes.class.getResourceAsStream(string);
        if (inputStream != null) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                map = PublicSuffixes.loadRules(bufferedReader);
            }
            catch (IOException iOException) {
                logger.log(Level.WARNING, "Unexpected error", iOException);
            }
            finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                }
                catch (IOException iOException) {
                    logger.log(Level.WARNING, "Unexpected error", iOException);
                }
            }
        } else {
            logger.log(Level.WARNING, "Resource not found: [{0}]", string);
        }
        Map<String, Rule> map2 = map = map != null ? Collections.unmodifiableMap(map) : Collections.emptyMap();
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "result: {0}", PublicSuffixes.toLogString(map));
        }
        return map;
    }

    private static Map<String, Rule> loadRules(BufferedReader bufferedReader) throws IOException {
        String string;
        LinkedHashMap<String, Rule> linkedHashMap = new LinkedHashMap<String, Rule>();
        while ((string = bufferedReader.readLine()) != null) {
            Rule rule;
            if ((string = string.split("\\s+", 2)[0]).length() == 0 || string.startsWith("//")) continue;
            if (string.startsWith("!")) {
                string = string.substring(1);
                rule = Rule.EXCEPTION_RULE;
            } else if (string.startsWith("*.")) {
                string = string.substring(2);
                rule = Rule.WILDCARD_RULE;
            } else {
                rule = Rule.SIMPLE_RULE;
            }
            try {
                string = IDN.toASCII(string, 1);
            }
            catch (Exception exception) {
                logger.log(Level.WARNING, String.format("Error parsing rule: [%s]", string), exception);
                continue;
            }
            linkedHashMap.put(string, rule);
        }
        return linkedHashMap;
    }

    private static String toLogString(Map<String, Rule> map) {
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Rule> entry : map.entrySet()) {
            stringBuilder.append(String.format("%n    ", new Object[0]));
            stringBuilder.append(entry.getKey());
            stringBuilder.append(": ");
            stringBuilder.append((Object)entry.getValue());
        }
        return stringBuilder.toString();
    }

    private static enum Rule {
        SIMPLE_RULE,
        WILDCARD_RULE,
        EXCEPTION_RULE;

    }
}

