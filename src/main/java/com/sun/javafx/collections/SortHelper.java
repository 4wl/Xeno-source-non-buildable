/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class SortHelper {
    private int[] permutation;
    private int[] reversePermutation;
    private static final int INSERTIONSORT_THRESHOLD = 7;

    public <T extends Comparable<? super T>> int[] sort(List<T> list) {
        Comparable[] arrcomparable = (Comparable[])Array.newInstance(Comparable.class, list.size());
        try {
            arrcomparable = list.toArray(arrcomparable);
        }
        catch (ArrayStoreException arrayStoreException) {
            throw new ClassCastException();
        }
        int[] arrn = this.sort(arrcomparable);
        ListIterator<T> listIterator = list.listIterator();
        for (int i = 0; i < arrcomparable.length; ++i) {
            listIterator.next();
            listIterator.set(arrcomparable[i]);
        }
        return arrn;
    }

    public <T> int[] sort(List<T> list, Comparator<? super T> comparator) {
        Object[] arrobject = list.toArray();
        int[] arrn = this.sort(arrobject, comparator);
        ListIterator<T> listIterator = list.listIterator();
        for (int i = 0; i < arrobject.length; ++i) {
            listIterator.next();
            listIterator.set(arrobject[i]);
        }
        return arrn;
    }

    public <T extends Comparable<? super T>> int[] sort(T[] arrT) {
        return this.sort(arrT, null);
    }

    public <T> int[] sort(T[] arrT, Comparator<? super T> comparator) {
        Object[] arrobject = (Object[])arrT.clone();
        int[] arrn = this.initPermutation(arrT.length);
        if (comparator == null) {
            this.mergeSort(arrobject, arrT, 0, arrT.length, 0);
        } else {
            this.mergeSort(arrobject, arrT, 0, arrT.length, 0, comparator);
        }
        this.reversePermutation = null;
        this.permutation = null;
        return arrn;
    }

    public <T> int[] sort(T[] arrT, int n, int n2, Comparator<? super T> comparator) {
        SortHelper.rangeCheck(arrT.length, n, n2);
        Object[] arrobject = SortHelper.copyOfRange(arrT, n, n2);
        int[] arrn = this.initPermutation(arrT.length);
        if (comparator == null) {
            this.mergeSort(arrobject, arrT, n, n2, -n);
        } else {
            this.mergeSort(arrobject, arrT, n, n2, -n, comparator);
        }
        this.reversePermutation = null;
        this.permutation = null;
        return Arrays.copyOfRange(arrn, n, n2);
    }

    public int[] sort(int[] arrn, int n, int n2) {
        SortHelper.rangeCheck(arrn.length, n, n2);
        int[] arrn2 = SortHelper.copyOfRange(arrn, n, n2);
        int[] arrn3 = this.initPermutation(arrn.length);
        this.mergeSort(arrn2, arrn, n, n2, -n);
        this.reversePermutation = null;
        this.permutation = null;
        return Arrays.copyOfRange(arrn3, n, n2);
    }

    private static void rangeCheck(int n, int n2, int n3) {
        if (n2 > n3) {
            throw new IllegalArgumentException("fromIndex(" + n2 + ") > toIndex(" + n3 + ")");
        }
        if (n2 < 0) {
            throw new ArrayIndexOutOfBoundsException(n2);
        }
        if (n3 > n) {
            throw new ArrayIndexOutOfBoundsException(n3);
        }
    }

    private static int[] copyOfRange(int[] arrn, int n, int n2) {
        int n3 = n2 - n;
        if (n3 < 0) {
            throw new IllegalArgumentException(n + " > " + n2);
        }
        int[] arrn2 = new int[n3];
        System.arraycopy(arrn, n, arrn2, 0, Math.min(arrn.length - n, n3));
        return arrn2;
    }

    private static <T> T[] copyOfRange(T[] arrT, int n, int n2) {
        return SortHelper.copyOfRange(arrT, n, n2, arrT.getClass());
    }

    private static <T, U> T[] copyOfRange(U[] arrU, int n, int n2, Class<? extends T[]> class_) {
        int n3 = n2 - n;
        if (n3 < 0) {
            throw new IllegalArgumentException(n + " > " + n2);
        }
        Object[] arrobject = class_ == Object[].class ? new Object[n3] : (Object[])Array.newInstance(class_.getComponentType(), n3);
        System.arraycopy(arrU, n, arrobject, 0, Math.min(arrU.length - n, n3));
        return arrobject;
    }

    private void mergeSort(int[] arrn, int[] arrn2, int n, int n2, int n3) {
        int n4;
        int n5 = n2 - n;
        if (n5 < 7) {
            for (int i = n; i < n2; ++i) {
                for (int j = i; j > n && Integer.valueOf(arrn2[j - 1]).compareTo(arrn2[j]) > 0; --j) {
                    this.swap(arrn2, j, j - 1);
                }
            }
            return;
        }
        int n6 = n;
        int n7 = n2;
        int n8 = (n += n3) + (n2 += n3) >>> 1;
        this.mergeSort(arrn2, arrn, n, n8, -n3);
        this.mergeSort(arrn2, arrn, n8, n2, -n3);
        if (Integer.valueOf(arrn[n8 - 1]).compareTo(arrn[n8]) <= 0) {
            System.arraycopy(arrn, n, arrn2, n6, n5);
            return;
        }
        int n9 = n;
        int n10 = n8;
        for (n4 = n6; n4 < n7; ++n4) {
            if (n10 >= n2 || n9 < n8 && Integer.valueOf(arrn[n9]).compareTo(arrn[n10]) <= 0) {
                arrn2[n4] = arrn[n9];
                this.permutation[this.reversePermutation[n9++]] = n4;
                continue;
            }
            arrn2[n4] = arrn[n10];
            this.permutation[this.reversePermutation[n10++]] = n4;
        }
        for (n4 = n6; n4 < n7; ++n4) {
            this.reversePermutation[this.permutation[n4]] = n4;
        }
    }

    private void mergeSort(Object[] arrobject, Object[] arrobject2, int n, int n2, int n3) {
        int n4;
        int n5 = n2 - n;
        if (n5 < 7) {
            for (int i = n; i < n2; ++i) {
                for (int j = i; j > n && ((Comparable)arrobject2[j - 1]).compareTo(arrobject2[j]) > 0; --j) {
                    this.swap(arrobject2, j, j - 1);
                }
            }
            return;
        }
        int n6 = n;
        int n7 = n2;
        int n8 = (n += n3) + (n2 += n3) >>> 1;
        this.mergeSort(arrobject2, arrobject, n, n8, -n3);
        this.mergeSort(arrobject2, arrobject, n8, n2, -n3);
        if (((Comparable)arrobject[n8 - 1]).compareTo(arrobject[n8]) <= 0) {
            System.arraycopy(arrobject, n, arrobject2, n6, n5);
            return;
        }
        int n9 = n;
        int n10 = n8;
        for (n4 = n6; n4 < n7; ++n4) {
            if (n10 >= n2 || n9 < n8 && ((Comparable)arrobject[n9]).compareTo(arrobject[n10]) <= 0) {
                arrobject2[n4] = arrobject[n9];
                this.permutation[this.reversePermutation[n9++]] = n4;
                continue;
            }
            arrobject2[n4] = arrobject[n10];
            this.permutation[this.reversePermutation[n10++]] = n4;
        }
        for (n4 = n6; n4 < n7; ++n4) {
            this.reversePermutation[this.permutation[n4]] = n4;
        }
    }

    private void mergeSort(Object[] arrobject, Object[] arrobject2, int n, int n2, int n3, Comparator comparator) {
        int n4;
        int n5 = n2 - n;
        if (n5 < 7) {
            for (int i = n; i < n2; ++i) {
                for (int j = i; j > n && comparator.compare(arrobject2[j - 1], arrobject2[j]) > 0; --j) {
                    this.swap(arrobject2, j, j - 1);
                }
            }
            return;
        }
        int n6 = n;
        int n7 = n2;
        int n8 = (n += n3) + (n2 += n3) >>> 1;
        this.mergeSort(arrobject2, arrobject, n, n8, -n3, comparator);
        this.mergeSort(arrobject2, arrobject, n8, n2, -n3, comparator);
        if (comparator.compare(arrobject[n8 - 1], arrobject[n8]) <= 0) {
            System.arraycopy(arrobject, n, arrobject2, n6, n5);
            return;
        }
        int n9 = n;
        int n10 = n8;
        for (n4 = n6; n4 < n7; ++n4) {
            if (n10 >= n2 || n9 < n8 && comparator.compare(arrobject[n9], arrobject[n10]) <= 0) {
                arrobject2[n4] = arrobject[n9];
                this.permutation[this.reversePermutation[n9++]] = n4;
                continue;
            }
            arrobject2[n4] = arrobject[n10];
            this.permutation[this.reversePermutation[n10++]] = n4;
        }
        for (n4 = n6; n4 < n7; ++n4) {
            this.reversePermutation[this.permutation[n4]] = n4;
        }
    }

    private void swap(int[] arrn, int n, int n2) {
        int n3 = arrn[n];
        arrn[n] = arrn[n2];
        arrn[n2] = n3;
        this.permutation[this.reversePermutation[n]] = n2;
        this.permutation[this.reversePermutation[n2]] = n;
        int n4 = this.reversePermutation[n];
        this.reversePermutation[n] = this.reversePermutation[n2];
        this.reversePermutation[n2] = n4;
    }

    private void swap(Object[] arrobject, int n, int n2) {
        Object object = arrobject[n];
        arrobject[n] = arrobject[n2];
        arrobject[n2] = object;
        this.permutation[this.reversePermutation[n]] = n2;
        this.permutation[this.reversePermutation[n2]] = n;
        int n3 = this.reversePermutation[n];
        this.reversePermutation[n] = this.reversePermutation[n2];
        this.reversePermutation[n2] = n3;
    }

    private int[] initPermutation(int n) {
        this.permutation = new int[n];
        this.reversePermutation = new int[n];
        for (int i = 0; i < n; ++i) {
            this.permutation[i] = this.reversePermutation[i] = i;
        }
        return this.permutation;
    }
}

