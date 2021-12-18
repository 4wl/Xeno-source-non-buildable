/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.KeyCode
 */
package com.sun.javafx.webkit;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.KeyCode;

public final class KeyCodeMap {
    private static final Map<KeyCode, Entry> MAP;

    private static void put(Map<KeyCode, Entry> map, KeyCode keyCode, int n, String string) {
        map.put(keyCode, new Entry(n, string));
    }

    private static void put(Map<KeyCode, Entry> map, KeyCode keyCode, int n) {
        KeyCodeMap.put(map, keyCode, n, null);
    }

    public static Entry lookup(KeyCode keyCode) {
        Entry entry = MAP.get((Object)keyCode);
        if (entry == null || entry.getKeyIdentifier() == null) {
            int n = entry != null ? entry.getWindowsVirtualKeyCode() : 0;
            String string = String.format("U+%04X", n);
            entry = new Entry(n, string);
        }
        return entry;
    }

    static {
        HashMap<KeyCode, Entry> hashMap = new HashMap<KeyCode, Entry>();
        KeyCodeMap.put(hashMap, KeyCode.ENTER, 13, "Enter");
        KeyCodeMap.put(hashMap, KeyCode.BACK_SPACE, 8);
        KeyCodeMap.put(hashMap, KeyCode.TAB, 9);
        KeyCodeMap.put(hashMap, KeyCode.CANCEL, 3);
        KeyCodeMap.put(hashMap, KeyCode.CLEAR, 12, "Clear");
        KeyCodeMap.put(hashMap, KeyCode.SHIFT, 16, "Shift");
        KeyCodeMap.put(hashMap, KeyCode.CONTROL, 17, "Control");
        KeyCodeMap.put(hashMap, KeyCode.ALT, 18, "Alt");
        KeyCodeMap.put(hashMap, KeyCode.PAUSE, 19, "Pause");
        KeyCodeMap.put(hashMap, KeyCode.CAPS, 20, "CapsLock");
        KeyCodeMap.put(hashMap, KeyCode.ESCAPE, 27);
        KeyCodeMap.put(hashMap, KeyCode.SPACE, 32);
        KeyCodeMap.put(hashMap, KeyCode.PAGE_UP, 33, "PageUp");
        KeyCodeMap.put(hashMap, KeyCode.PAGE_DOWN, 34, "PageDown");
        KeyCodeMap.put(hashMap, KeyCode.END, 35, "End");
        KeyCodeMap.put(hashMap, KeyCode.HOME, 36, "Home");
        KeyCodeMap.put(hashMap, KeyCode.LEFT, 37, "Left");
        KeyCodeMap.put(hashMap, KeyCode.UP, 38, "Up");
        KeyCodeMap.put(hashMap, KeyCode.RIGHT, 39, "Right");
        KeyCodeMap.put(hashMap, KeyCode.DOWN, 40, "Down");
        KeyCodeMap.put(hashMap, KeyCode.COMMA, 188);
        KeyCodeMap.put(hashMap, KeyCode.MINUS, 189);
        KeyCodeMap.put(hashMap, KeyCode.PERIOD, 190);
        KeyCodeMap.put(hashMap, KeyCode.SLASH, 191);
        KeyCodeMap.put(hashMap, KeyCode.DIGIT0, 48);
        KeyCodeMap.put(hashMap, KeyCode.DIGIT1, 49);
        KeyCodeMap.put(hashMap, KeyCode.DIGIT2, 50);
        KeyCodeMap.put(hashMap, KeyCode.DIGIT3, 51);
        KeyCodeMap.put(hashMap, KeyCode.DIGIT4, 52);
        KeyCodeMap.put(hashMap, KeyCode.DIGIT5, 53);
        KeyCodeMap.put(hashMap, KeyCode.DIGIT6, 54);
        KeyCodeMap.put(hashMap, KeyCode.DIGIT7, 55);
        KeyCodeMap.put(hashMap, KeyCode.DIGIT8, 56);
        KeyCodeMap.put(hashMap, KeyCode.DIGIT9, 57);
        KeyCodeMap.put(hashMap, KeyCode.SEMICOLON, 186);
        KeyCodeMap.put(hashMap, KeyCode.EQUALS, 187);
        KeyCodeMap.put(hashMap, KeyCode.A, 65);
        KeyCodeMap.put(hashMap, KeyCode.B, 66);
        KeyCodeMap.put(hashMap, KeyCode.C, 67);
        KeyCodeMap.put(hashMap, KeyCode.D, 68);
        KeyCodeMap.put(hashMap, KeyCode.E, 69);
        KeyCodeMap.put(hashMap, KeyCode.F, 70);
        KeyCodeMap.put(hashMap, KeyCode.G, 71);
        KeyCodeMap.put(hashMap, KeyCode.H, 72);
        KeyCodeMap.put(hashMap, KeyCode.I, 73);
        KeyCodeMap.put(hashMap, KeyCode.J, 74);
        KeyCodeMap.put(hashMap, KeyCode.K, 75);
        KeyCodeMap.put(hashMap, KeyCode.L, 76);
        KeyCodeMap.put(hashMap, KeyCode.M, 77);
        KeyCodeMap.put(hashMap, KeyCode.N, 78);
        KeyCodeMap.put(hashMap, KeyCode.O, 79);
        KeyCodeMap.put(hashMap, KeyCode.P, 80);
        KeyCodeMap.put(hashMap, KeyCode.Q, 81);
        KeyCodeMap.put(hashMap, KeyCode.R, 82);
        KeyCodeMap.put(hashMap, KeyCode.S, 83);
        KeyCodeMap.put(hashMap, KeyCode.T, 84);
        KeyCodeMap.put(hashMap, KeyCode.U, 85);
        KeyCodeMap.put(hashMap, KeyCode.V, 86);
        KeyCodeMap.put(hashMap, KeyCode.W, 87);
        KeyCodeMap.put(hashMap, KeyCode.X, 88);
        KeyCodeMap.put(hashMap, KeyCode.Y, 89);
        KeyCodeMap.put(hashMap, KeyCode.Z, 90);
        KeyCodeMap.put(hashMap, KeyCode.OPEN_BRACKET, 219);
        KeyCodeMap.put(hashMap, KeyCode.BACK_SLASH, 220);
        KeyCodeMap.put(hashMap, KeyCode.CLOSE_BRACKET, 221);
        KeyCodeMap.put(hashMap, KeyCode.NUMPAD0, 96);
        KeyCodeMap.put(hashMap, KeyCode.NUMPAD1, 97);
        KeyCodeMap.put(hashMap, KeyCode.NUMPAD2, 98);
        KeyCodeMap.put(hashMap, KeyCode.NUMPAD3, 99);
        KeyCodeMap.put(hashMap, KeyCode.NUMPAD4, 100);
        KeyCodeMap.put(hashMap, KeyCode.NUMPAD5, 101);
        KeyCodeMap.put(hashMap, KeyCode.NUMPAD6, 102);
        KeyCodeMap.put(hashMap, KeyCode.NUMPAD7, 103);
        KeyCodeMap.put(hashMap, KeyCode.NUMPAD8, 104);
        KeyCodeMap.put(hashMap, KeyCode.NUMPAD9, 105);
        KeyCodeMap.put(hashMap, KeyCode.MULTIPLY, 106);
        KeyCodeMap.put(hashMap, KeyCode.ADD, 107);
        KeyCodeMap.put(hashMap, KeyCode.SEPARATOR, 108);
        KeyCodeMap.put(hashMap, KeyCode.SUBTRACT, 109);
        KeyCodeMap.put(hashMap, KeyCode.DECIMAL, 110);
        KeyCodeMap.put(hashMap, KeyCode.DIVIDE, 111);
        KeyCodeMap.put(hashMap, KeyCode.DELETE, 46, "U+007F");
        KeyCodeMap.put(hashMap, KeyCode.NUM_LOCK, 144);
        KeyCodeMap.put(hashMap, KeyCode.SCROLL_LOCK, 145, "Scroll");
        KeyCodeMap.put(hashMap, KeyCode.F1, 112, "F1");
        KeyCodeMap.put(hashMap, KeyCode.F2, 113, "F2");
        KeyCodeMap.put(hashMap, KeyCode.F3, 114, "F3");
        KeyCodeMap.put(hashMap, KeyCode.F4, 115, "F4");
        KeyCodeMap.put(hashMap, KeyCode.F5, 116, "F5");
        KeyCodeMap.put(hashMap, KeyCode.F6, 117, "F6");
        KeyCodeMap.put(hashMap, KeyCode.F7, 118, "F7");
        KeyCodeMap.put(hashMap, KeyCode.F8, 119, "F8");
        KeyCodeMap.put(hashMap, KeyCode.F9, 120, "F9");
        KeyCodeMap.put(hashMap, KeyCode.F10, 121, "F10");
        KeyCodeMap.put(hashMap, KeyCode.F11, 122, "F11");
        KeyCodeMap.put(hashMap, KeyCode.F12, 123, "F12");
        KeyCodeMap.put(hashMap, KeyCode.F13, 124, "F13");
        KeyCodeMap.put(hashMap, KeyCode.F14, 125, "F14");
        KeyCodeMap.put(hashMap, KeyCode.F15, 126, "F15");
        KeyCodeMap.put(hashMap, KeyCode.F16, 127, "F16");
        KeyCodeMap.put(hashMap, KeyCode.F17, 128, "F17");
        KeyCodeMap.put(hashMap, KeyCode.F18, 129, "F18");
        KeyCodeMap.put(hashMap, KeyCode.F19, 130, "F19");
        KeyCodeMap.put(hashMap, KeyCode.F20, 131, "F20");
        KeyCodeMap.put(hashMap, KeyCode.F21, 132, "F21");
        KeyCodeMap.put(hashMap, KeyCode.F22, 133, "F22");
        KeyCodeMap.put(hashMap, KeyCode.F23, 134, "F23");
        KeyCodeMap.put(hashMap, KeyCode.F24, 135, "F24");
        KeyCodeMap.put(hashMap, KeyCode.PRINTSCREEN, 44, "PrintScreen");
        KeyCodeMap.put(hashMap, KeyCode.INSERT, 45, "Insert");
        KeyCodeMap.put(hashMap, KeyCode.HELP, 47, "Help");
        KeyCodeMap.put(hashMap, KeyCode.META, 0, "Meta");
        KeyCodeMap.put(hashMap, KeyCode.BACK_QUOTE, 192);
        KeyCodeMap.put(hashMap, KeyCode.QUOTE, 222);
        KeyCodeMap.put(hashMap, KeyCode.KP_UP, 38, "Up");
        KeyCodeMap.put(hashMap, KeyCode.KP_DOWN, 40, "Down");
        KeyCodeMap.put(hashMap, KeyCode.KP_LEFT, 37, "Left");
        KeyCodeMap.put(hashMap, KeyCode.KP_RIGHT, 39, "Right");
        KeyCodeMap.put(hashMap, KeyCode.AMPERSAND, 55);
        KeyCodeMap.put(hashMap, KeyCode.ASTERISK, 56);
        KeyCodeMap.put(hashMap, KeyCode.QUOTEDBL, 222);
        KeyCodeMap.put(hashMap, KeyCode.LESS, 188);
        KeyCodeMap.put(hashMap, KeyCode.GREATER, 190);
        KeyCodeMap.put(hashMap, KeyCode.BRACELEFT, 219);
        KeyCodeMap.put(hashMap, KeyCode.BRACERIGHT, 221);
        KeyCodeMap.put(hashMap, KeyCode.AT, 50);
        KeyCodeMap.put(hashMap, KeyCode.COLON, 186);
        KeyCodeMap.put(hashMap, KeyCode.CIRCUMFLEX, 54);
        KeyCodeMap.put(hashMap, KeyCode.DOLLAR, 52);
        KeyCodeMap.put(hashMap, KeyCode.EXCLAMATION_MARK, 49);
        KeyCodeMap.put(hashMap, KeyCode.LEFT_PARENTHESIS, 57);
        KeyCodeMap.put(hashMap, KeyCode.NUMBER_SIGN, 51);
        KeyCodeMap.put(hashMap, KeyCode.PLUS, 187);
        KeyCodeMap.put(hashMap, KeyCode.RIGHT_PARENTHESIS, 48);
        KeyCodeMap.put(hashMap, KeyCode.UNDERSCORE, 189);
        KeyCodeMap.put(hashMap, KeyCode.WINDOWS, 91, "Win");
        KeyCodeMap.put(hashMap, KeyCode.CONTEXT_MENU, 93);
        KeyCodeMap.put(hashMap, KeyCode.FINAL, 24);
        KeyCodeMap.put(hashMap, KeyCode.CONVERT, 28);
        KeyCodeMap.put(hashMap, KeyCode.NONCONVERT, 29);
        KeyCodeMap.put(hashMap, KeyCode.ACCEPT, 30);
        KeyCodeMap.put(hashMap, KeyCode.MODECHANGE, 31);
        KeyCodeMap.put(hashMap, KeyCode.KANA, 21);
        KeyCodeMap.put(hashMap, KeyCode.KANJI, 25);
        KeyCodeMap.put(hashMap, KeyCode.ALT_GRAPH, 165);
        KeyCodeMap.put(hashMap, KeyCode.PLAY, 250);
        KeyCodeMap.put(hashMap, KeyCode.TRACK_PREV, 177);
        KeyCodeMap.put(hashMap, KeyCode.TRACK_NEXT, 176);
        KeyCodeMap.put(hashMap, KeyCode.VOLUME_UP, 175);
        KeyCodeMap.put(hashMap, KeyCode.VOLUME_DOWN, 174);
        KeyCodeMap.put(hashMap, KeyCode.MUTE, 173);
        MAP = Collections.unmodifiableMap(hashMap);
    }

    public static final class Entry {
        private final int windowsVirtualKeyCode;
        private final String keyIdentifier;

        private Entry(int n, String string) {
            this.windowsVirtualKeyCode = n;
            this.keyIdentifier = string;
        }

        public int getWindowsVirtualKeyCode() {
            return this.windowsVirtualKeyCode;
        }

        public String getKeyIdentifier() {
            return this.keyIdentifier;
        }
    }
}

