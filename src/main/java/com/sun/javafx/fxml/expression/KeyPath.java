/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml.expression;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.AbstractList;
import java.util.ArrayList;

public class KeyPath
extends AbstractList<String> {
    private ArrayList<String> elements;

    public KeyPath(ArrayList<String> arrayList) {
        if (arrayList == null) {
            throw new NullPointerException();
        }
        this.elements = arrayList;
    }

    @Override
    public String get(int n) {
        return this.elements.get(n);
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int n = this.elements.size();
        for (int i = 0; i < n; ++i) {
            String string = this.elements.get(i);
            if (Character.isDigit(string.charAt(0))) {
                stringBuilder.append("[");
                stringBuilder.append(string);
                stringBuilder.append("]");
                continue;
            }
            if (i > 0) {
                stringBuilder.append(".");
            }
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    public static KeyPath parse(String string) {
        KeyPath keyPath;
        try (PushbackReader pushbackReader = new PushbackReader(new StringReader(string));){
            keyPath = KeyPath.parse(pushbackReader);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        return keyPath;
    }

    protected static KeyPath parse(PushbackReader pushbackReader) throws IOException {
        ArrayList<String> arrayList = new ArrayList<String>();
        int n = pushbackReader.read();
        while (n != -1 && (Character.isJavaIdentifierStart(n) || n == 91)) {
            boolean bl;
            StringBuilder stringBuilder = new StringBuilder();
            boolean bl2 = bl = n == 91;
            if (bl) {
                char c;
                boolean bl3;
                n = pushbackReader.read();
                boolean bl4 = bl3 = n == 34 || n == 39;
                if (bl3) {
                    c = (char)n;
                    n = pushbackReader.read();
                } else {
                    c = '\u0000';
                }
                while (n != -1 && bl) {
                    if (Character.isISOControl(n)) {
                        throw new IllegalArgumentException("Illegal identifier character.");
                    }
                    if (!bl3 && !Character.isDigit(n)) {
                        throw new IllegalArgumentException("Illegal character in index value.");
                    }
                    stringBuilder.append((char)n);
                    n = pushbackReader.read();
                    if (bl3) {
                        boolean bl5 = bl3 = n != c;
                        if (!bl3) {
                            n = pushbackReader.read();
                        }
                    }
                    bl = n != 93;
                }
                if (bl3) {
                    throw new IllegalArgumentException("Unterminated quoted identifier.");
                }
                if (bl) {
                    throw new IllegalArgumentException("Unterminated bracketed identifier.");
                }
                n = pushbackReader.read();
            } else {
                while (n != -1 && n != 46 && n != 91 && Character.isJavaIdentifierPart(n)) {
                    stringBuilder.append((char)n);
                    n = pushbackReader.read();
                }
            }
            if (n == 46 && (n = pushbackReader.read()) == -1) {
                throw new IllegalArgumentException("Illegal terminator character.");
            }
            if (stringBuilder.length() == 0) {
                throw new IllegalArgumentException("Missing identifier.");
            }
            arrayList.add(stringBuilder.toString());
        }
        if (arrayList.size() == 0) {
            throw new IllegalArgumentException("Invalid path.");
        }
        if (n != -1) {
            pushbackReader.unread(n);
        }
        return new KeyPath(arrayList);
    }
}

