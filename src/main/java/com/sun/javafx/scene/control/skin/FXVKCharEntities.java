/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import java.util.HashMap;

class FXVKCharEntities {
    private static final HashMap<String, Character> map = new HashMap();

    FXVKCharEntities() {
    }

    public static String get(String string) {
        Character c = map.get(string);
        if (c == null) {
            if (string.length() != 1) {
                // empty if block
            }
            return string;
        }
        return "" + c;
    }

    private static void put(String string, int n) {
        map.put(string, Character.valueOf((char)n));
    }

    static {
        FXVKCharEntities.put("space", 32);
        FXVKCharEntities.put("quot", 34);
        FXVKCharEntities.put("apos", 39);
        FXVKCharEntities.put("amp", 38);
        FXVKCharEntities.put("lt", 60);
        FXVKCharEntities.put("gt", 62);
        FXVKCharEntities.put("nbsp", 160);
        FXVKCharEntities.put("iexcl", 161);
        FXVKCharEntities.put("cent", 162);
        FXVKCharEntities.put("pound", 163);
        FXVKCharEntities.put("curren", 164);
        FXVKCharEntities.put("yen", 165);
        FXVKCharEntities.put("brvbar", 166);
        FXVKCharEntities.put("sect", 167);
        FXVKCharEntities.put("uml", 168);
        FXVKCharEntities.put("copy", 169);
        FXVKCharEntities.put("ordf", 170);
        FXVKCharEntities.put("laquo", 171);
        FXVKCharEntities.put("not", 172);
        FXVKCharEntities.put("shy", 173);
        FXVKCharEntities.put("reg", 174);
        FXVKCharEntities.put("macr", 175);
        FXVKCharEntities.put("deg", 176);
        FXVKCharEntities.put("plusmn", 177);
        FXVKCharEntities.put("sup2", 178);
        FXVKCharEntities.put("sup3", 179);
        FXVKCharEntities.put("acute", 180);
        FXVKCharEntities.put("micro", 181);
        FXVKCharEntities.put("para", 182);
        FXVKCharEntities.put("middot", 183);
        FXVKCharEntities.put("cedil", 184);
        FXVKCharEntities.put("sup1", 185);
        FXVKCharEntities.put("ordm", 186);
        FXVKCharEntities.put("raquo", 187);
        FXVKCharEntities.put("frac14", 188);
        FXVKCharEntities.put("frac12", 189);
        FXVKCharEntities.put("frac34", 190);
        FXVKCharEntities.put("iquest", 191);
        FXVKCharEntities.put("times", 215);
        FXVKCharEntities.put("divide", 247);
        FXVKCharEntities.put("Agrave", 192);
        FXVKCharEntities.put("Aacute", 193);
        FXVKCharEntities.put("Acirc", 194);
        FXVKCharEntities.put("Atilde", 195);
        FXVKCharEntities.put("Auml", 196);
        FXVKCharEntities.put("Aring", 197);
        FXVKCharEntities.put("AElig", 198);
        FXVKCharEntities.put("Ccedil", 199);
        FXVKCharEntities.put("Egrave", 200);
        FXVKCharEntities.put("Eacute", 201);
        FXVKCharEntities.put("Ecirc", 202);
        FXVKCharEntities.put("Euml", 203);
        FXVKCharEntities.put("Igrave", 204);
        FXVKCharEntities.put("Iacute", 205);
        FXVKCharEntities.put("Icirc", 206);
        FXVKCharEntities.put("Iuml", 207);
        FXVKCharEntities.put("ETH", 208);
        FXVKCharEntities.put("Ntilde", 209);
        FXVKCharEntities.put("Ograve", 210);
        FXVKCharEntities.put("Oacute", 211);
        FXVKCharEntities.put("Ocirc", 212);
        FXVKCharEntities.put("Otilde", 213);
        FXVKCharEntities.put("Ouml", 214);
        FXVKCharEntities.put("Oslash", 216);
        FXVKCharEntities.put("Ugrave", 217);
        FXVKCharEntities.put("Uacute", 218);
        FXVKCharEntities.put("Ucirc", 219);
        FXVKCharEntities.put("Uuml", 220);
        FXVKCharEntities.put("Yacute", 221);
        FXVKCharEntities.put("THORN", 222);
        FXVKCharEntities.put("szlig", 223);
        FXVKCharEntities.put("agrave", 224);
        FXVKCharEntities.put("aacute", 225);
        FXVKCharEntities.put("acirc", 226);
        FXVKCharEntities.put("atilde", 227);
        FXVKCharEntities.put("auml", 228);
        FXVKCharEntities.put("aring", 229);
        FXVKCharEntities.put("aelig", 230);
        FXVKCharEntities.put("ccedil", 231);
        FXVKCharEntities.put("egrave", 232);
        FXVKCharEntities.put("eacute", 233);
        FXVKCharEntities.put("ecirc", 234);
        FXVKCharEntities.put("euml", 235);
        FXVKCharEntities.put("igrave", 236);
        FXVKCharEntities.put("iacute", 237);
        FXVKCharEntities.put("icirc", 238);
        FXVKCharEntities.put("iuml", 239);
        FXVKCharEntities.put("eth", 240);
        FXVKCharEntities.put("ntilde", 241);
        FXVKCharEntities.put("ograve", 242);
        FXVKCharEntities.put("oacute", 243);
        FXVKCharEntities.put("ocirc", 244);
        FXVKCharEntities.put("otilde", 245);
        FXVKCharEntities.put("ouml", 246);
        FXVKCharEntities.put("oslash", 248);
        FXVKCharEntities.put("ugrave", 249);
        FXVKCharEntities.put("uacute", 250);
        FXVKCharEntities.put("ucirc", 251);
        FXVKCharEntities.put("uuml", 252);
        FXVKCharEntities.put("yacute", 253);
        FXVKCharEntities.put("thorn", 254);
        FXVKCharEntities.put("yuml", 255);
        FXVKCharEntities.put("scedil", 351);
        FXVKCharEntities.put("scaron", 353);
        FXVKCharEntities.put("ycirc", 375);
        FXVKCharEntities.put("ymacron", 563);
        FXVKCharEntities.put("pi", 960);
        FXVKCharEntities.put("sigma", 963);
        FXVKCharEntities.put("ygrave", 7923);
        FXVKCharEntities.put("yhook", 7927);
        FXVKCharEntities.put("permil", 8240);
        FXVKCharEntities.put("euro", 8364);
        FXVKCharEntities.put("tm", 8482);
        FXVKCharEntities.put("neq", 8800);
    }
}

