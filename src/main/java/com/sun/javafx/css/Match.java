/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.NodeOrientation
 */
package com.sun.javafx.css;

import com.sun.javafx.css.PseudoClassState;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.SimpleSelector;
import javafx.geometry.NodeOrientation;

final class Match
implements Comparable<Match> {
    final Selector selector;
    final PseudoClassState pseudoClasses;
    final int idCount;
    final int styleClassCount;
    final int specificity;

    Match(Selector selector, PseudoClassState pseudoClassState, int n, int n2) {
        SimpleSelector simpleSelector;
        int n3;
        assert (selector != null);
        this.selector = selector;
        this.idCount = n;
        this.styleClassCount = n2;
        this.pseudoClasses = pseudoClassState;
        int n4 = n3 = pseudoClassState != null ? pseudoClassState.size() : 0;
        if (selector instanceof SimpleSelector && (simpleSelector = (SimpleSelector)selector).getNodeOrientation() != NodeOrientation.INHERIT) {
            ++n3;
        }
        this.specificity = n << 8 | n2 << 4 | n3;
    }

    @Override
    public int compareTo(Match match) {
        return this.specificity - match.specificity;
    }
}

