/*
 * Decompiled with CFR 0.150.
 */
package org.json;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XMLParserConfiguration;
import org.json.XMLTokener;

public class XML {
    public static final Character AMP = Character.valueOf('&');
    public static final Character APOS = Character.valueOf('\'');
    public static final Character BANG = Character.valueOf('!');
    public static final Character EQ = Character.valueOf('=');
    public static final Character GT = Character.valueOf('>');
    public static final Character LT = Character.valueOf('<');
    public static final Character QUEST = Character.valueOf('?');
    public static final Character QUOT = Character.valueOf('\"');
    public static final Character SLASH = Character.valueOf('/');
    public static final String NULL_ATTR = "xsi:nil";

    private static Iterable<Integer> codePointIterator(final String string) {
        return new Iterable<Integer>(){

            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>(){
                    private int nextIndex = 0;
                    private int length;
                    {
                        this.length = string.length();
                    }

                    @Override
                    public boolean hasNext() {
                        return this.nextIndex < this.length;
                    }

                    @Override
                    public Integer next() {
                        int result = string.codePointAt(this.nextIndex);
                        this.nextIndex += Character.charCount(result);
                        return result;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static String escape(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        block7: for (int cp : XML.codePointIterator(string)) {
            switch (cp) {
                case 38: {
                    sb.append("&amp;");
                    continue block7;
                }
                case 60: {
                    sb.append("&lt;");
                    continue block7;
                }
                case 62: {
                    sb.append("&gt;");
                    continue block7;
                }
                case 34: {
                    sb.append("&quot;");
                    continue block7;
                }
                case 39: {
                    sb.append("&apos;");
                    continue block7;
                }
            }
            if (XML.mustEscape(cp)) {
                sb.append("&#x");
                sb.append(Integer.toHexString(cp));
                sb.append(';');
                continue;
            }
            sb.appendCodePoint(cp);
        }
        return sb.toString();
    }

    private static boolean mustEscape(int cp) {
        return Character.isISOControl(cp) && cp != 9 && cp != 10 && cp != 13 || (cp < 32 || cp > 55295) && (cp < 57344 || cp > 65533) && (cp < 65536 || cp > 0x10FFFF);
    }

    public static String unescape(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        int length = string.length();
        for (int i = 0; i < length; ++i) {
            char c = string.charAt(i);
            if (c == '&') {
                int semic = string.indexOf(59, i);
                if (semic > i) {
                    String entity = string.substring(i + 1, semic);
                    sb.append(XMLTokener.unescapeEntity(entity));
                    i += entity.length() + 1;
                    continue;
                }
                sb.append(c);
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static void noSpace(String string) throws JSONException {
        int length = string.length();
        if (length == 0) {
            throw new JSONException("Empty string.");
        }
        for (int i = 0; i < length; ++i) {
            if (!Character.isWhitespace(string.charAt(i))) continue;
            throw new JSONException("'" + string + "' contains a space character.");
        }
    }

    private static boolean parse(XMLTokener x, JSONObject context, String name, XMLParserConfiguration config) throws JSONException {
        String string;
        JSONObject jsonObject = null;
        Object token = x.nextToken();
        if (token == BANG) {
            char c = x.next();
            if (c == '-') {
                if (x.next() == '-') {
                    x.skipPast("-->");
                    return false;
                }
                x.back();
            } else if (c == '[') {
                token = x.nextToken();
                if ("CDATA".equals(token) && x.next() == '[') {
                    String string2 = x.nextCDATA();
                    if (string2.length() > 0) {
                        context.accumulate(config.cDataTagName, string2);
                    }
                    return false;
                }
                throw x.syntaxError("Expected 'CDATA['");
            }
            int i = 1;
            do {
                if ((token = x.nextMeta()) == null) {
                    throw x.syntaxError("Missing '>' after '<!'.");
                }
                if (token == LT) {
                    ++i;
                    continue;
                }
                if (token != GT) continue;
                --i;
            } while (i > 0);
            return false;
        }
        if (token == QUEST) {
            x.skipPast("?>");
            return false;
        }
        if (token == SLASH) {
            token = x.nextToken();
            if (name == null) {
                throw x.syntaxError("Mismatched close tag " + token);
            }
            if (!token.equals(name)) {
                throw x.syntaxError("Mismatched " + name + " and " + token);
            }
            if (x.nextToken() != GT) {
                throw x.syntaxError("Misshaped close tag");
            }
            return true;
        }
        if (token instanceof Character) {
            throw x.syntaxError("Misshaped tag");
        }
        String tagName = (String)token;
        token = null;
        jsonObject = new JSONObject();
        boolean nilAttributeFound = false;
        while (true) {
            if (token == null) {
                token = x.nextToken();
            }
            if (!(token instanceof String)) break;
            string = (String)token;
            token = x.nextToken();
            if (token == EQ) {
                token = x.nextToken();
                if (!(token instanceof String)) {
                    throw x.syntaxError("Missing value");
                }
                if (config.convertNilAttributeToNull && NULL_ATTR.equals(string) && Boolean.parseBoolean((String)token)) {
                    nilAttributeFound = true;
                } else if (!nilAttributeFound) {
                    jsonObject.accumulate(string, config.keepStrings ? (String)token : XML.stringToValue((String)token));
                }
                token = null;
                continue;
            }
            jsonObject.accumulate(string, "");
        }
        if (token == SLASH) {
            if (x.nextToken() != GT) {
                throw x.syntaxError("Misshaped tag");
            }
            if (nilAttributeFound) {
                context.accumulate(tagName, JSONObject.NULL);
            } else if (jsonObject.length() > 0) {
                context.accumulate(tagName, jsonObject);
            } else {
                context.accumulate(tagName, "");
            }
            return false;
        }
        if (token == GT) {
            while (true) {
                if ((token = x.nextContent()) == null) {
                    if (tagName != null) {
                        throw x.syntaxError("Unclosed tag " + tagName);
                    }
                    return false;
                }
                if (token instanceof String) {
                    string = (String)token;
                    if (string.length() <= 0) continue;
                    jsonObject.accumulate(config.cDataTagName, config.keepStrings ? string : XML.stringToValue(string));
                    continue;
                }
                if (token == LT && XML.parse(x, jsonObject, tagName, config)) break;
            }
            if (jsonObject.length() == 0) {
                context.accumulate(tagName, "");
            } else if (jsonObject.length() == 1 && jsonObject.opt(config.cDataTagName) != null) {
                context.accumulate(tagName, jsonObject.opt(config.cDataTagName));
            } else {
                context.accumulate(tagName, jsonObject);
            }
            return false;
        }
        throw x.syntaxError("Misshaped tag");
    }

    public static Object stringToValue(String string) {
        if (string.equals("")) {
            return string;
        }
        if (string.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (string.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("null")) {
            return JSONObject.NULL;
        }
        char initial = string.charAt(0);
        if (initial >= '0' && initial <= '9' || initial == '-') {
            try {
                if (string.indexOf(46) > -1 || string.indexOf(101) > -1 || string.indexOf(69) > -1 || "-0".equals(string)) {
                    Double d = Double.valueOf(string);
                    if (!d.isInfinite() && !d.isNaN()) {
                        return d;
                    }
                } else {
                    Long myLong = Long.valueOf(string);
                    if (string.equals(myLong.toString())) {
                        if (myLong == (long)myLong.intValue()) {
                            return myLong.intValue();
                        }
                        return myLong;
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return string;
    }

    public static JSONObject toJSONObject(String string) throws JSONException {
        return XML.toJSONObject(string, XMLParserConfiguration.ORIGINAL);
    }

    public static JSONObject toJSONObject(Reader reader) throws JSONException {
        return XML.toJSONObject(reader, XMLParserConfiguration.ORIGINAL);
    }

    public static JSONObject toJSONObject(Reader reader, boolean keepStrings) throws JSONException {
        if (keepStrings) {
            return XML.toJSONObject(reader, XMLParserConfiguration.KEEP_STRINGS);
        }
        return XML.toJSONObject(reader, XMLParserConfiguration.ORIGINAL);
    }

    public static JSONObject toJSONObject(Reader reader, XMLParserConfiguration config) throws JSONException {
        JSONObject jo = new JSONObject();
        XMLTokener x = new XMLTokener(reader);
        while (x.more()) {
            x.skipPast("<");
            if (!x.more()) continue;
            XML.parse(x, jo, null, config);
        }
        return jo;
    }

    public static JSONObject toJSONObject(String string, boolean keepStrings) throws JSONException {
        return XML.toJSONObject((Reader)new StringReader(string), keepStrings);
    }

    public static JSONObject toJSONObject(String string, XMLParserConfiguration config) throws JSONException {
        return XML.toJSONObject((Reader)new StringReader(string), config);
    }

    public static String toString(Object object) throws JSONException {
        return XML.toString(object, null, XMLParserConfiguration.ORIGINAL);
    }

    public static String toString(Object object, String tagName) {
        return XML.toString(object, tagName, XMLParserConfiguration.ORIGINAL);
    }

    public static String toString(Object object, String tagName, XMLParserConfiguration config) throws JSONException {
        String string;
        StringBuilder sb = new StringBuilder();
        if (object instanceof JSONObject) {
            if (tagName != null) {
                sb.append('<');
                sb.append(tagName);
                sb.append('>');
            }
            JSONObject jo = (JSONObject)object;
            for (String key : jo.keySet()) {
                Object val;
                int i;
                int jaLength;
                JSONArray ja;
                Object value = jo.opt(key);
                if (value == null) {
                    value = "";
                } else if (value.getClass().isArray()) {
                    value = new JSONArray(value);
                }
                if (key.equals(config.cDataTagName)) {
                    if (value instanceof JSONArray) {
                        ja = (JSONArray)value;
                        jaLength = ja.length();
                        for (i = 0; i < jaLength; ++i) {
                            if (i > 0) {
                                sb.append('\n');
                            }
                            val = ja.opt(i);
                            sb.append(XML.escape(val.toString()));
                        }
                        continue;
                    }
                    sb.append(XML.escape(value.toString()));
                    continue;
                }
                if (value instanceof JSONArray) {
                    ja = (JSONArray)value;
                    jaLength = ja.length();
                    for (i = 0; i < jaLength; ++i) {
                        val = ja.opt(i);
                        if (val instanceof JSONArray) {
                            sb.append('<');
                            sb.append(key);
                            sb.append('>');
                            sb.append(XML.toString(val, null, config));
                            sb.append("</");
                            sb.append(key);
                            sb.append('>');
                            continue;
                        }
                        sb.append(XML.toString(val, key, config));
                    }
                    continue;
                }
                if ("".equals(value)) {
                    sb.append('<');
                    sb.append(key);
                    sb.append("/>");
                    continue;
                }
                sb.append(XML.toString(value, key, config));
            }
            if (tagName != null) {
                sb.append("</");
                sb.append(tagName);
                sb.append('>');
            }
            return sb.toString();
        }
        if (object != null && (object instanceof JSONArray || object.getClass().isArray())) {
            JSONArray ja = object.getClass().isArray() ? new JSONArray(object) : (JSONArray)object;
            int jaLength = ja.length();
            for (int i = 0; i < jaLength; ++i) {
                Object val = ja.opt(i);
                sb.append(XML.toString(val, tagName == null ? "array" : tagName, config));
            }
            return sb.toString();
        }
        String string2 = string = object == null ? "null" : XML.escape(object.toString());
        return tagName == null ? "\"" + string + "\"" : (string.length() == 0 ? "<" + tagName + "/>" : "<" + tagName + ">" + string + "</" + tagName + ">");
    }
}

