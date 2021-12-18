/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DateParser {
    private static final Logger logger = Logger.getLogger(DateParser.class.getName());
    private static final Pattern DELIMITER_PATTERN = Pattern.compile("[\\x09\\x20-\\x2F\\x3B-\\x40\\x5B-\\x60\\x7B-\\x7E]+");
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})(?:[^\\d].*)*");
    private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})(?:[^\\d].*)*");
    private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})(?:[^\\d].*)*");
    private static final Map<String, Integer> MONTH_MAP;

    private DateParser() {
        throw new AssertionError();
    }

    static long parse(String string) throws ParseException {
        logger.log(Level.FINEST, "date: [{0}]", string);
        Time time = null;
        Integer n = null;
        Integer n2 = null;
        Integer n3 = null;
        String[] arrstring = DELIMITER_PATTERN.split(string, 0);
        for (String string2 : arrstring) {
            Integer n4;
            Integer n5;
            Integer n6;
            Time time2;
            if (string2.length() == 0) continue;
            if (time == null && (time2 = DateParser.parseTime(string2)) != null) {
                time = time2;
                continue;
            }
            if (n == null && (n6 = DateParser.parseDayOfMonth(string2)) != null) {
                n = n6;
                continue;
            }
            if (n2 == null && (n5 = DateParser.parseMonth(string2)) != null) {
                n2 = n5;
                continue;
            }
            if (n3 != null || (n4 = DateParser.parseYear(string2)) == null) continue;
            n3 = n4;
        }
        if (n3 != null) {
            if (n3 >= 70 && n3 <= 99) {
                n3 = n3 + 1900;
            } else if (n3 >= 0 && n3 <= 69) {
                n3 = n3 + 2000;
            }
        }
        if (time == null || n == null || n2 == null || n3 == null || n < 1 || n > 31 || n3 < 1601 || time.hour > 23 || time.minute > 59 || time.second > 59) {
            throw new ParseException("Error parsing date", 0);
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
        calendar.setLenient(false);
        calendar.clear();
        calendar.set(n3, n2, n, time.hour, time.minute, time.second);
        try {
            long l = calendar.getTimeInMillis();
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "result: [{0}]", new Date(l).toString());
            }
            return l;
        }
        catch (Exception exception) {
            ParseException parseException = new ParseException("Error parsing date", 0);
            parseException.initCause(exception);
            throw parseException;
        }
    }

    private static Time parseTime(String string) {
        Matcher matcher = TIME_PATTERN.matcher(string);
        if (matcher.matches()) {
            return new Time(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
        }
        return null;
    }

    private static Integer parseDayOfMonth(String string) {
        Matcher matcher = DAY_OF_MONTH_PATTERN.matcher(string);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }

    private static Integer parseMonth(String string) {
        if (string.length() >= 3) {
            return MONTH_MAP.get(string.substring(0, 3).toLowerCase());
        }
        return null;
    }

    private static Integer parseYear(String string) {
        Matcher matcher = YEAR_PATTERN.matcher(string);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }

    static {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>(12);
        hashMap.put("jan", 0);
        hashMap.put("feb", 1);
        hashMap.put("mar", 2);
        hashMap.put("apr", 3);
        hashMap.put("may", 4);
        hashMap.put("jun", 5);
        hashMap.put("jul", 6);
        hashMap.put("aug", 7);
        hashMap.put("sep", 8);
        hashMap.put("oct", 9);
        hashMap.put("nov", 10);
        hashMap.put("dec", 11);
        MONTH_MAP = Collections.unmodifiableMap(hashMap);
    }

    private static final class Time {
        private final int hour;
        private final int minute;
        private final int second;

        private Time(int n, int n2, int n3) {
            this.hour = n;
            this.minute = n2;
            this.second = n3;
        }
    }
}

