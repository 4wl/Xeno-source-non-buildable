/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.transform.BaseTransform;

public class PrismFontUtils {
    private PrismFontUtils() {
    }

    static Metrics getFontMetrics(PGFont pGFont) {
        FontStrike fontStrike = pGFont.getStrike(BaseTransform.IDENTITY_TRANSFORM, 0);
        return fontStrike.getMetrics();
    }

    static double computeStringWidth(PGFont pGFont, String string) {
        if (string == null || string.equals("")) {
            return 0.0;
        }
        FontStrike fontStrike = pGFont.getStrike(BaseTransform.IDENTITY_TRANSFORM, 0);
        double d = 0.0;
        for (int i = 0; i < string.length(); ++i) {
            d += (double)fontStrike.getCharAdvance(string.charAt(i));
        }
        return d;
    }
}

