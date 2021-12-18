/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.text;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

final class TextCodec {
    private final Charset charset;
    private static final Map<String, String> reMap = new HashMap<String, String>();

    private TextCodec(String string) {
        this.charset = Charset.forName(string);
    }

    private byte[] encode(char[] arrc) {
        ByteBuffer byteBuffer = this.charset.encode(CharBuffer.wrap(arrc));
        byte[] arrby = new byte[byteBuffer.remaining()];
        byteBuffer.get(arrby);
        return arrby;
    }

    private String decode(byte[] arrby) {
        CharBuffer charBuffer = this.charset.decode(ByteBuffer.wrap(arrby));
        char[] arrc = new char[charBuffer.remaining()];
        charBuffer.get(arrc);
        return new String(arrc);
    }

    private static String[] getEncodings() {
        ArrayList<String> arrayList = new ArrayList<String>();
        SortedMap<String, Charset> sortedMap = Charset.availableCharsets();
        for (Map.Entry entry : sortedMap.entrySet()) {
            String string = (String)entry.getKey();
            arrayList.add(string);
            arrayList.add(string);
            Charset charset = (Charset)entry.getValue();
            for (String string2 : charset.aliases()) {
                if (string2.equals("8859_1")) continue;
                arrayList.add(string2);
                String string3 = reMap.get(string2);
                arrayList.add(string3 == null ? string : string3);
            }
        }
        return arrayList.toArray(new String[0]);
    }

    static {
        reMap.put("ISO-10646-UCS-2", "UTF-16");
    }
}

