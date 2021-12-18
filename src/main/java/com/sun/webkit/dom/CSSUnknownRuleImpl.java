/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import org.w3c.dom.css.CSSUnknownRule;

public class CSSUnknownRuleImpl
extends CSSRuleImpl
implements CSSUnknownRule {
    CSSUnknownRuleImpl(long l) {
        super(l);
    }

    static CSSUnknownRule getImpl(long l) {
        return (CSSUnknownRule)CSSUnknownRuleImpl.create(l);
    }
}

