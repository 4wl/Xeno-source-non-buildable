/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css.parser;

import com.sun.javafx.css.parser.LexerState;
import com.sun.javafx.css.parser.Recognizer;
import com.sun.javafx.css.parser.Token;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

final class CSSLexer {
    static final int STRING = 10;
    static final int IDENT = 11;
    static final int FUNCTION = 12;
    static final int NUMBER = 13;
    static final int CM = 14;
    static final int EMS = 15;
    static final int EXS = 16;
    static final int IN = 17;
    static final int MM = 18;
    static final int PC = 19;
    static final int PT = 20;
    static final int PX = 21;
    static final int PERCENTAGE = 22;
    static final int DEG = 23;
    static final int GRAD = 24;
    static final int RAD = 25;
    static final int TURN = 26;
    static final int GREATER = 27;
    static final int LBRACE = 28;
    static final int RBRACE = 29;
    static final int SEMI = 30;
    static final int COLON = 31;
    static final int SOLIDUS = 32;
    static final int STAR = 33;
    static final int LPAREN = 34;
    static final int RPAREN = 35;
    static final int COMMA = 36;
    static final int HASH = 37;
    static final int DOT = 38;
    static final int IMPORTANT_SYM = 39;
    static final int WS = 40;
    static final int NL = 41;
    static final int FONT_FACE = 42;
    static final int URL = 43;
    static final int IMPORT = 44;
    static final int SECONDS = 45;
    static final int MS = 46;
    static final int AT_KEYWORD = 47;
    private final Recognizer A = n -> n == 97 || n == 65;
    private final Recognizer B = n -> n == 98 || n == 66;
    private final Recognizer C = n -> n == 99 || n == 67;
    private final Recognizer D = n -> n == 100 || n == 68;
    private final Recognizer E = n -> n == 101 || n == 69;
    private final Recognizer F = n -> n == 102 || n == 70;
    private final Recognizer G = n -> n == 103 || n == 71;
    private final Recognizer H = n -> n == 104 || n == 72;
    private final Recognizer I = n -> n == 105 || n == 73;
    private final Recognizer J = n -> n == 106 || n == 74;
    private final Recognizer K = n -> n == 107 || n == 75;
    private final Recognizer L = n -> n == 108 || n == 76;
    private final Recognizer M = n -> n == 109 || n == 77;
    private final Recognizer N = n -> n == 110 || n == 78;
    private final Recognizer O = n -> n == 111 || n == 79;
    private final Recognizer P = n -> n == 112 || n == 80;
    private final Recognizer Q = n -> n == 113 || n == 81;
    private final Recognizer R = n -> n == 114 || n == 82;
    private final Recognizer S = n -> n == 115 || n == 83;
    private final Recognizer T = n -> n == 116 || n == 84;
    private final Recognizer U = n -> n == 117 || n == 85;
    private final Recognizer V = n -> n == 118 || n == 86;
    private final Recognizer W = n -> n == 119 || n == 87;
    private final Recognizer X = n -> n == 120 || n == 88;
    private final Recognizer Y = n -> n == 121 || n == 89;
    private final Recognizer Z = n -> n == 122 || n == 90;
    private final Recognizer ALPHA = n -> 97 <= n && n <= 122 || 65 <= n && n <= 90;
    private final Recognizer NON_ASCII = n -> 128 <= n && n <= 65535;
    private final Recognizer DOT_CHAR = n -> n == 46;
    private final Recognizer GREATER_CHAR = n -> n == 62;
    private final Recognizer LBRACE_CHAR = n -> n == 123;
    private final Recognizer RBRACE_CHAR = n -> n == 125;
    private final Recognizer SEMI_CHAR = n -> n == 59;
    private final Recognizer COLON_CHAR = n -> n == 58;
    private final Recognizer SOLIDUS_CHAR = n -> n == 47;
    private final Recognizer MINUS_CHAR = n -> n == 45;
    private final Recognizer PLUS_CHAR = n -> n == 43;
    private final Recognizer STAR_CHAR = n -> n == 42;
    private final Recognizer LPAREN_CHAR = n -> n == 40;
    private final Recognizer RPAREN_CHAR = n -> n == 41;
    private final Recognizer COMMA_CHAR = n -> n == 44;
    private final Recognizer UNDERSCORE_CHAR = n -> n == 95;
    private final Recognizer HASH_CHAR = n -> n == 35;
    private final Recognizer WS_CHARS = n -> n == 32 || n == 9 || n == 13 || n == 10 || n == 12;
    private final Recognizer NL_CHARS = n -> n == 13 || n == 10;
    private final Recognizer DIGIT = n -> 48 <= n && n <= 57;
    private final Recognizer HEX_DIGIT = n -> 48 <= n && n <= 57 || 97 <= n && n <= 102 || 65 <= n && n <= 70;
    final LexerState initState = new LexerState("initState", null, new Recognizer[0]){

        @Override
        public boolean accepts(int n) {
            return true;
        }
    };
    final LexerState hashState = new LexerState("hashState", this.HASH_CHAR, new Recognizer[0]);
    final LexerState minusState = new LexerState("minusState", this.MINUS_CHAR, new Recognizer[0]);
    final LexerState plusState = new LexerState("plusState", this.PLUS_CHAR, new Recognizer[0]);
    final LexerState dotState = new LexerState(38, "dotState", this.DOT_CHAR, new Recognizer[0]);
    final LexerState nmStartState = new LexerState(11, "nmStartState", this.UNDERSCORE_CHAR, this.ALPHA);
    final LexerState nmCharState = new LexerState(11, "nmCharState", this.UNDERSCORE_CHAR, this.ALPHA, this.DIGIT, this.MINUS_CHAR);
    final LexerState hashNameCharState = new LexerState(37, "hashNameCharState", this.UNDERSCORE_CHAR, this.ALPHA, this.DIGIT, this.MINUS_CHAR);
    final LexerState lparenState = new LexerState(12, "lparenState", this.LPAREN_CHAR, new Recognizer[0]){

        @Override
        public int getType() {
            if (CSSLexer.this.text.indexOf("url(") == 0) {
                try {
                    return CSSLexer.this.consumeUrl();
                }
                catch (IOException iOException) {
                    return 0;
                }
            }
            return super.getType();
        }
    };
    final LexerState leadingDigitsState = new LexerState(13, "leadingDigitsState", this.DIGIT, new Recognizer[0]);
    final LexerState decimalMarkState = new LexerState("decimalMarkState", this.DOT_CHAR, new Recognizer[0]);
    final LexerState trailingDigitsState = new LexerState(13, "trailingDigitsState", this.DIGIT, new Recognizer[0]);
    final LexerState unitsState = new UnitsState();
    private int pos = 0;
    private int offset = 0;
    private int line = 1;
    private int lastc = -1;
    private int ch;
    private boolean charNotConsumed = false;
    private Reader reader;
    private Token token;
    private final Map<LexerState, LexerState[]> stateMap = this.createStateMap();
    private LexerState currentState;
    private final StringBuilder text = new StringBuilder(64);

    public static CSSLexer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private Map<LexerState, LexerState[]> createStateMap() {
        HashMap<LexerState, LexerState[]> hashMap = new HashMap<LexerState, LexerState[]>();
        hashMap.put(this.initState, new LexerState[]{this.hashState, this.minusState, this.nmStartState, this.plusState, this.minusState, this.leadingDigitsState, this.dotState});
        hashMap.put(this.minusState, new LexerState[]{this.nmStartState, this.leadingDigitsState, this.decimalMarkState});
        hashMap.put(this.hashState, new LexerState[]{this.hashNameCharState});
        hashMap.put(this.hashNameCharState, new LexerState[]{this.hashNameCharState});
        hashMap.put(this.nmStartState, new LexerState[]{this.nmCharState});
        hashMap.put(this.nmCharState, new LexerState[]{this.nmCharState, this.lparenState});
        hashMap.put(this.plusState, new LexerState[]{this.leadingDigitsState, this.decimalMarkState});
        hashMap.put(this.leadingDigitsState, new LexerState[]{this.leadingDigitsState, this.decimalMarkState, this.unitsState});
        hashMap.put(this.dotState, new LexerState[]{this.trailingDigitsState});
        hashMap.put(this.decimalMarkState, new LexerState[]{this.trailingDigitsState});
        hashMap.put(this.trailingDigitsState, new LexerState[]{this.trailingDigitsState, this.unitsState});
        hashMap.put(this.unitsState, new LexerState[]{this.unitsState});
        return hashMap;
    }

    CSSLexer() {
        this.currentState = this.initState;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
        this.lastc = -1;
        this.offset = 0;
        this.pos = 0;
        this.line = 1;
        this.currentState = this.initState;
        this.token = null;
        try {
            this.ch = this.readChar();
        }
        catch (IOException iOException) {
            this.token = Token.EOF_TOKEN;
        }
    }

    private Token scanImportant() throws IOException {
        Recognizer[] arrrecognizer = new Recognizer[]{this.I, this.M, this.P, this.O, this.R, this.T, this.A, this.N, this.T};
        int n = 0;
        this.text.append((char)this.ch);
        this.ch = this.readChar();
        block5: while (true) {
            switch (this.ch) {
                case -1: {
                    this.token = Token.EOF_TOKEN;
                    return this.token;
                }
                case 47: {
                    this.ch = this.readChar();
                    if (this.ch == 42) {
                        this.skipComment();
                        continue block5;
                    }
                    if (this.ch == 47) {
                        this.skipEOL();
                        continue block5;
                    }
                    this.text.append('/').append((char)this.ch);
                    int n2 = this.offset;
                    this.offset = this.pos;
                    return new Token(0, this.text.toString(), this.line, n2);
                }
                case 9: 
                case 10: 
                case 12: 
                case 13: 
                case 32: {
                    this.ch = this.readChar();
                    continue block5;
                }
            }
            break;
        }
        boolean bl = true;
        while (bl && n < arrrecognizer.length) {
            bl = arrrecognizer[n++].recognize(this.ch);
            this.text.append((char)this.ch);
            this.ch = this.readChar();
        }
        if (bl) {
            int n3 = this.offset;
            this.offset = this.pos - 1;
            return new Token(39, "!important", this.line, n3);
        }
        while (this.ch != 59 && this.ch != 125 && this.ch != -1) {
            this.ch = this.readChar();
        }
        if (this.ch != -1) {
            int n4 = this.offset;
            this.offset = this.pos - 1;
            return new Token(1, this.text.toString(), this.line, n4);
        }
        return Token.EOF_TOKEN;
    }

    private int consumeUrl() throws IOException {
        int n;
        this.text.delete(0, this.text.length());
        while (this.WS_CHARS.recognize(this.ch)) {
            this.ch = this.readChar();
        }
        if (this.ch == -1) {
            return -1;
        }
        if (this.ch == 39 || this.ch == 34) {
            n = this.ch;
            this.ch = this.readChar();
            while (this.ch != n && this.ch != -1 && !this.NL_CHARS.recognize(this.ch)) {
                if (this.ch == 92) {
                    this.ch = this.readChar();
                    if (this.NL_CHARS.recognize(this.ch)) {
                        while (this.NL_CHARS.recognize(this.ch)) {
                            this.ch = this.readChar();
                        }
                        continue;
                    }
                    if (this.ch == -1) continue;
                    this.text.append((char)this.ch);
                    this.ch = this.readChar();
                    continue;
                }
                this.text.append((char)this.ch);
                this.ch = this.readChar();
            }
            if (this.ch == n) {
                this.ch = this.readChar();
                while (this.WS_CHARS.recognize(this.ch)) {
                    this.ch = this.readChar();
                }
                if (this.ch == 41) {
                    this.ch = this.readChar();
                    return 43;
                }
                if (this.ch == -1) {
                    return 43;
                }
            }
        } else {
            this.text.append((char)this.ch);
            this.ch = this.readChar();
            block4: while (true) {
                if (this.WS_CHARS.recognize(this.ch)) {
                    this.ch = this.readChar();
                    continue;
                }
                if (this.ch == 41) {
                    this.ch = this.readChar();
                    return 43;
                }
                if (this.ch == -1) {
                    return 43;
                }
                if (this.ch == 92) {
                    this.ch = this.readChar();
                    if (this.NL_CHARS.recognize(this.ch)) {
                        while (true) {
                            if (!this.NL_CHARS.recognize(this.ch)) continue block4;
                            this.ch = this.readChar();
                        }
                    }
                    if (this.ch == -1) continue;
                    this.text.append((char)this.ch);
                    this.ch = this.readChar();
                    continue;
                }
                if (this.ch == 39 || this.ch == 34 || this.ch == 40) break;
                this.text.append((char)this.ch);
                this.ch = this.readChar();
            }
        }
        while (true) {
            n = this.ch;
            if (this.ch == -1) {
                return -1;
            }
            if (this.ch == 41 && n != 92) {
                this.ch = this.readChar();
                return 0;
            }
            n = this.ch;
            this.ch = this.readChar();
        }
    }

    private void skipComment() throws IOException {
        while (this.ch != -1) {
            if (this.ch == 42) {
                this.ch = this.readChar();
                if (this.ch != 47) continue;
                this.offset = this.pos;
                this.ch = this.readChar();
                break;
            }
            this.ch = this.readChar();
        }
    }

    private void skipEOL() throws IOException {
        int n = this.ch;
        while (this.ch != -1) {
            this.ch = this.readChar();
            if (this.ch != 10 && (n != 13 || this.ch == 10)) continue;
            break;
        }
    }

    private int readChar() throws IOException {
        int n = this.reader.read();
        if (this.lastc == 10 || this.lastc == 13 && n != 10) {
            this.pos = 1;
            this.offset = 0;
            ++this.line;
        } else {
            ++this.pos;
        }
        this.lastc = n;
        return n;
    }

    public Token nextToken() {
        Token token = null;
        if (this.token != null) {
            token = this.token;
            if (this.token.getType() != -1) {
                this.token = null;
            }
        } else {
            while ((token = this.getToken()) != null && Token.SKIP_TOKEN.equals(token)) {
            }
        }
        this.text.delete(0, this.text.length());
        this.currentState = this.initState;
        return token;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private Token getToken() {
        try {
            block22: while (true) {
                this.charNotConsumed = false;
                var1_1 = this.currentState != null ? this.stateMap.get(this.currentState) : null;
                var2_3 = var1_1 != null ? var1_1.length : 0;
                var3_4 = null;
                for (var4_5 = 0; var4_5 < var2_3 && var3_4 == null; ++var4_5) {
                    var5_7 = var1_1[var4_5];
                    if (!var5_7.accepts(this.ch)) continue;
                    var3_4 = var5_7;
                }
                if (var3_4 != null) {
                    this.currentState = var3_4;
                    this.text.append((char)this.ch);
                    this.ch = this.readChar();
                    continue;
                }
                v0 = var4_5 = this.currentState != null ? this.currentState.getType() : 0;
                if (var4_5 != 0 || !this.currentState.equals(this.initState)) {
                    var5_7 = this.text.toString();
                    var6_8 = new Token(var4_5, (String)var5_7, this.line, this.offset);
                    this.offset = this.pos - 1;
                    return var6_8;
                }
                switch (this.ch) {
                    case -1: {
                        this.token = Token.EOF_TOKEN;
                        return this.token;
                    }
                    case 34: 
                    case 39: {
                        this.text.append((char)this.ch);
                        var4_5 = this.ch;
                        while ((this.ch = this.readChar()) != -1) {
                            this.text.append((char)this.ch);
                            if (this.ch != var4_5) continue;
                        }
                        if (this.ch != -1) {
                            this.token = new Token(10, this.text.toString(), this.line, this.offset);
                            this.offset = this.pos;
                            break block22;
                        }
                        this.token = new Token(0, this.text.toString(), this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 47: {
                        this.ch = this.readChar();
                        if (this.ch == 42) {
                            this.skipComment();
                            if (this.ch != -1) continue block22;
                            this.token = Token.EOF_TOKEN;
                            return this.token;
                        }
                        if (this.ch == 47) {
                            this.skipEOL();
                            if (this.ch == -1) ** break;
                            continue block22;
                            this.token = Token.EOF_TOKEN;
                            return this.token;
                        }
                        this.token = new Token(32, "/", this.line, this.offset);
                        this.offset = this.pos;
                        this.charNotConsumed = true;
                        break block22;
                    }
                    case 62: {
                        this.token = new Token(27, ">", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 123: {
                        this.token = new Token(28, "{", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 125: {
                        this.token = new Token(29, "}", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 59: {
                        this.token = new Token(30, ";", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 58: {
                        this.token = new Token(31, ":", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 42: {
                        this.token = new Token(33, "*", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 40: {
                        this.token = new Token(34, "(", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 41: {
                        this.token = new Token(35, ")", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 44: {
                        this.token = new Token(36, ",", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 46: {
                        this.token = new Token(38, ".", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 9: 
                    case 12: 
                    case 32: {
                        this.token = new Token(40, Character.toString((char)this.ch), this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    case 13: {
                        this.token = new Token(41, "\\r", this.line, this.offset);
                        this.ch = this.readChar();
                        if (this.ch == 10) {
                            this.token = new Token(41, "\\r\\n", this.line, this.offset);
                            break block22;
                        }
                        var5_7 = this.token;
                        this.token = this.ch == -1 ? Token.EOF_TOKEN : null;
                        return var5_7;
                    }
                    case 10: {
                        this.token = new Token(41, "\\n", this.line, this.offset);
                        break block22;
                    }
                    case 33: {
                        return this.scanImportant();
                    }
                    case 64: {
                        this.token = new Token(47, "@", this.line, this.offset);
                        this.offset = this.pos;
                        break block22;
                    }
                    default: {
                        this.token = new Token(0, Character.toString((char)this.ch), this.line, this.offset);
                        this.offset = this.pos;
                    }
                }
                break;
            }
            if (this.token == null) {
                this.token = new Token(0, null, this.line, this.offset);
                this.offset = this.pos;
            } else if (this.token.getType() == -1) {
                return this.token;
            }
            if (this.ch != -1 && !this.charNotConsumed) {
                this.ch = this.readChar();
            }
            var4_6 = this.token;
            this.token = null;
            return var4_6;
        }
        catch (IOException var1_2) {
            this.token = Token.EOF_TOKEN;
            return this.token;
        }
    }

    private class UnitsState
    extends LexerState {
        private final Recognizer[][] units;
        private int unitsMask;
        private int index;

        UnitsState() {
            super(-1, "UnitsState", null, new Recognizer[0]);
            this.units = new Recognizer[][]{{CSSLexer.this.C, CSSLexer.this.M}, {CSSLexer.this.D, CSSLexer.this.E, CSSLexer.this.G}, {CSSLexer.this.E, CSSLexer.this.M}, {CSSLexer.this.E, CSSLexer.this.X}, {CSSLexer.this.G, CSSLexer.this.R, CSSLexer.this.A, CSSLexer.this.D}, {CSSLexer.this.I, CSSLexer.this.N}, {CSSLexer.this.M, CSSLexer.this.M}, {CSSLexer.this.M, CSSLexer.this.S}, {CSSLexer.this.P, CSSLexer.this.C}, {CSSLexer.this.P, CSSLexer.this.T}, {CSSLexer.this.P, CSSLexer.this.X}, {CSSLexer.this.R, CSSLexer.this.A, CSSLexer.this.D}, {CSSLexer.this.S}, {CSSLexer.this.T, CSSLexer.this.U, CSSLexer.this.R, CSSLexer.this.N}, {n -> n == 37}};
            this.unitsMask = 32767;
            this.index = -1;
        }

        @Override
        public int getType() {
            int n = 0;
            switch (this.unitsMask) {
                case 1: {
                    n = 14;
                    break;
                }
                case 2: {
                    n = 23;
                    break;
                }
                case 4: {
                    n = 15;
                    break;
                }
                case 8: {
                    n = 16;
                    break;
                }
                case 16: {
                    n = 24;
                    break;
                }
                case 32: {
                    n = 17;
                    break;
                }
                case 64: {
                    n = 18;
                    break;
                }
                case 128: {
                    n = 46;
                    break;
                }
                case 256: {
                    n = 19;
                    break;
                }
                case 512: {
                    n = 20;
                    break;
                }
                case 1024: {
                    n = 21;
                    break;
                }
                case 2048: {
                    n = 25;
                    break;
                }
                case 4096: {
                    n = 45;
                    break;
                }
                case 8192: {
                    n = 26;
                    break;
                }
                case 16384: {
                    n = 22;
                    break;
                }
                default: {
                    n = 0;
                }
            }
            this.unitsMask = 32767;
            this.index = -1;
            return n;
        }

        @Override
        public boolean accepts(int n) {
            if (!CSSLexer.this.ALPHA.recognize(n) && n != 37) {
                return false;
            }
            if (this.unitsMask == 0) {
                return true;
            }
            ++this.index;
            for (int i = 0; i < this.units.length; ++i) {
                int n2 = 1 << i;
                if ((this.unitsMask & n2) == 0 || this.index < this.units[i].length && this.units[i][this.index].recognize(n)) continue;
                this.unitsMask &= ~n2;
            }
            return true;
        }
    }

    private static class InstanceHolder {
        static final CSSLexer INSTANCE = new CSSLexer();

        private InstanceHolder() {
        }
    }
}

