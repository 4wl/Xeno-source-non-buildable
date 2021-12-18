/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.TextImpl;
import org.w3c.dom.CDATASection;

public class CDATASectionImpl
extends TextImpl
implements CDATASection {
    CDATASectionImpl(long l) {
        super(l);
    }

    static CDATASection getImpl(long l) {
        return (CDATASection)CDATASectionImpl.create(l);
    }
}

