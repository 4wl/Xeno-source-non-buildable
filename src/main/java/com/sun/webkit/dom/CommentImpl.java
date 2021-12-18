/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CharacterDataImpl;
import org.w3c.dom.Comment;

public class CommentImpl
extends CharacterDataImpl
implements Comment {
    CommentImpl(long l) {
        super(l);
    }

    static Comment getImpl(long l) {
        return (Comment)CommentImpl.create(l);
    }
}

