/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringStore {
    private final Map<String, Integer> stringMap = new HashMap<String, Integer>();
    public final List<String> strings = new ArrayList<String>();

    public int addString(String string) {
        Integer n = this.stringMap.get(string);
        if (n == null) {
            n = this.strings.size();
            this.strings.add(string);
            this.stringMap.put(string, n);
        }
        return n;
    }

    public void writeBinary(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.strings.size());
        if (this.stringMap.containsKey(null)) {
            Integer n = this.stringMap.get(null);
            dataOutputStream.writeShort(n);
        } else {
            dataOutputStream.writeShort(-1);
        }
        for (int i = 0; i < this.strings.size(); ++i) {
            String string = this.strings.get(i);
            if (string == null) continue;
            dataOutputStream.writeUTF(string);
        }
    }

    static String[] readBinary(DataInputStream dataInputStream) throws IOException {
        int n = dataInputStream.readShort();
        short s = dataInputStream.readShort();
        Object[] arrobject = new String[n];
        Arrays.fill(arrobject, null);
        for (int i = 0; i < n; ++i) {
            if (i == s) continue;
            arrobject[i] = dataInputStream.readUTF();
        }
        return arrobject;
    }
}

