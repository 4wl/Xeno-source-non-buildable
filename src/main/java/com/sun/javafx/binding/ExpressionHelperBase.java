/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.WeakListener
 */
package com.sun.javafx.binding;

import javafx.beans.WeakListener;

public class ExpressionHelperBase {
    protected static int trim(int n, Object[] arrobject) {
        for (int i = 0; i < n; ++i) {
            Object object = arrobject[i];
            if (!(object instanceof WeakListener) || !((WeakListener)object).wasGarbageCollected()) continue;
            int n2 = n - i - 1;
            if (n2 > 0) {
                System.arraycopy(arrobject, i + 1, arrobject, i, n2);
            }
            arrobject[--n] = null;
            --i;
        }
        return n;
    }
}

