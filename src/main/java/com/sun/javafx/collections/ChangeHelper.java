/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import java.util.Arrays;
import java.util.List;

public class ChangeHelper {
    public static String addRemoveChangeToString(int n, int n2, List<?> list, List<?> list2) {
        StringBuilder stringBuilder = new StringBuilder();
        if (list2.isEmpty()) {
            stringBuilder.append(list.subList(n, n2));
            stringBuilder.append(" added at ").append(n);
        } else {
            stringBuilder.append(list2);
            if (n == n2) {
                stringBuilder.append(" removed at ").append(n);
            } else {
                stringBuilder.append(" replaced by ");
                stringBuilder.append(list.subList(n, n2));
                stringBuilder.append(" at ").append(n);
            }
        }
        return stringBuilder.toString();
    }

    public static String permChangeToString(int[] arrn) {
        return "permutated by " + Arrays.toString(arrn);
    }

    public static String updateChangeToString(int n, int n2) {
        return "updated at range [" + n + ", " + n2 + ")";
    }
}

