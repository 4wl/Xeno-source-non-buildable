/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.KeyCombination
 *  javafx.scene.input.KeyCombination$Modifier
 *  javafx.scene.input.KeyEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class TextBinding {
    private String MNEMONIC_SYMBOL = "_";
    private String text = null;
    private String mnemonic = null;
    private KeyCombination mnemonicKeyCombination = null;
    private int mnemonicIndex = -1;
    private String extendedMnemonicText = null;

    public String getText() {
        return this.text;
    }

    public String getMnemonic() {
        return this.mnemonic;
    }

    public KeyCombination getMnemonicKeyCombination() {
        if (this.mnemonic != null && this.mnemonicKeyCombination == null) {
            this.mnemonicKeyCombination = new MnemonicKeyCombination(this.mnemonic);
        }
        return this.mnemonicKeyCombination;
    }

    public int getMnemonicIndex() {
        return this.mnemonicIndex;
    }

    public String getExtendedMnemonicText() {
        return this.extendedMnemonicText;
    }

    public TextBinding(String string) {
        this.parseAndSplit(string);
    }

    private void parseAndSplit(String string) {
        if (string == null || string.length() == 0) {
            this.text = string;
            return;
        }
        StringBuffer stringBuffer = new StringBuffer(string);
        int n = stringBuffer.indexOf(this.MNEMONIC_SYMBOL);
        while (n >= 0 && n < stringBuffer.length() - 1) {
            if (this.MNEMONIC_SYMBOL.equals(stringBuffer.substring(n + 1, n + 2))) {
                stringBuffer.delete(n, n + 1);
            } else {
                if (stringBuffer.charAt(n + 1) != '(' || n == stringBuffer.length() - 2) {
                    this.mnemonic = stringBuffer.substring(n + 1, n + 2);
                    if (this.mnemonic != null) {
                        this.mnemonicIndex = n;
                    }
                    stringBuffer.delete(n, n + 1);
                    break;
                }
                int n2 = stringBuffer.indexOf(")", n + 3);
                if (n2 == -1) {
                    this.mnemonic = stringBuffer.substring(n + 1, n + 2);
                    if (this.mnemonic != null) {
                        this.mnemonicIndex = n;
                    }
                    stringBuffer.delete(n, n + 1);
                    break;
                }
                if (n2 == n + 3) {
                    this.mnemonic = stringBuffer.substring(n + 2, n + 3);
                    this.extendedMnemonicText = stringBuffer.substring(n + 1, n + 4);
                    stringBuffer.delete(n, n2 + 3);
                    break;
                }
            }
            n = stringBuffer.indexOf(this.MNEMONIC_SYMBOL, n + 1);
        }
        this.text = stringBuffer.toString();
    }

    public static class MnemonicKeyCombination
    extends KeyCombination {
        private String character = "";

        public MnemonicKeyCombination(String string) {
            super(new KeyCombination.Modifier[]{PlatformUtil.isMac() ? KeyCombination.META_DOWN : KeyCombination.ALT_DOWN});
            this.character = string;
        }

        public final String getCharacter() {
            return this.character;
        }

        public boolean match(KeyEvent keyEvent) {
            String string = keyEvent.getText();
            return string != null && !string.isEmpty() && string.equalsIgnoreCase(this.getCharacter()) && super.match(keyEvent);
        }

        public String getName() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(super.getName());
            if (stringBuilder.length() > 0) {
                stringBuilder.append("+");
            }
            return stringBuilder.append('\'').append(this.character.replace("'", "\\'")).append('\'').toString();
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof MnemonicKeyCombination)) {
                return false;
            }
            return this.character.equals(((MnemonicKeyCombination)((Object)object)).getCharacter()) && super.equals(object);
        }

        public int hashCode() {
            return 23 * super.hashCode() + this.character.hashCode();
        }
    }
}

