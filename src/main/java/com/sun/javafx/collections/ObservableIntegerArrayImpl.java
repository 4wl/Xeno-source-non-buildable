/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableArrayBase
 *  javafx.collections.ObservableIntegerArray
 */
package com.sun.javafx.collections;

import java.util.Arrays;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableIntegerArray;

public class ObservableIntegerArrayImpl
extends ObservableArrayBase<ObservableIntegerArray>
implements ObservableIntegerArray {
    private static final int[] INITIAL = new int[0];
    private int[] array = INITIAL;
    private int size = 0;
    private static final int MAX_ARRAY_SIZE = 0x7FFFFFF7;

    public ObservableIntegerArrayImpl() {
    }

    public ObservableIntegerArrayImpl(int ... arrn) {
        this.setAll(arrn);
    }

    public ObservableIntegerArrayImpl(ObservableIntegerArray observableIntegerArray) {
        this.setAll(observableIntegerArray);
    }

    public void clear() {
        this.resize(0);
    }

    public int size() {
        return this.size;
    }

    private void addAllInternal(ObservableIntegerArray observableIntegerArray, int n, int n2) {
        this.growCapacity(n2);
        observableIntegerArray.copyTo(n, this.array, this.size, n2);
        this.size += n2;
        this.fireChange(n2 != 0, this.size - n2, this.size);
    }

    private void addAllInternal(int[] arrn, int n, int n2) {
        this.growCapacity(n2);
        System.arraycopy(arrn, n, this.array, this.size, n2);
        this.size += n2;
        this.fireChange(n2 != 0, this.size - n2, this.size);
    }

    public void addAll(ObservableIntegerArray observableIntegerArray) {
        this.addAllInternal(observableIntegerArray, 0, observableIntegerArray.size());
    }

    public void addAll(int ... arrn) {
        this.addAllInternal(arrn, 0, arrn.length);
    }

    public void addAll(ObservableIntegerArray observableIntegerArray, int n, int n2) {
        this.rangeCheck(observableIntegerArray, n, n2);
        this.addAllInternal(observableIntegerArray, n, n2);
    }

    public void addAll(int[] arrn, int n, int n2) {
        this.rangeCheck(arrn, n, n2);
        this.addAllInternal(arrn, n, n2);
    }

    private void setAllInternal(ObservableIntegerArray observableIntegerArray, int n, int n2) {
        boolean bl;
        boolean bl2 = bl = this.size() != n2;
        if (observableIntegerArray == this) {
            if (n == 0) {
                this.resize(n2);
            } else {
                System.arraycopy(this.array, n, this.array, 0, n2);
                this.size = n2;
                this.fireChange(bl, 0, this.size);
            }
        } else {
            this.size = 0;
            this.ensureCapacity(n2);
            observableIntegerArray.copyTo(n, this.array, 0, n2);
            this.size = n2;
            this.fireChange(bl, 0, this.size);
        }
    }

    private void setAllInternal(int[] arrn, int n, int n2) {
        boolean bl = this.size() != n2;
        this.size = 0;
        this.ensureCapacity(n2);
        System.arraycopy(arrn, n, this.array, 0, n2);
        this.size = n2;
        this.fireChange(bl, 0, this.size);
    }

    public void setAll(ObservableIntegerArray observableIntegerArray) {
        this.setAllInternal(observableIntegerArray, 0, observableIntegerArray.size());
    }

    public void setAll(ObservableIntegerArray observableIntegerArray, int n, int n2) {
        this.rangeCheck(observableIntegerArray, n, n2);
        this.setAllInternal(observableIntegerArray, n, n2);
    }

    public void setAll(int[] arrn, int n, int n2) {
        this.rangeCheck(arrn, n, n2);
        this.setAllInternal(arrn, n, n2);
    }

    public void setAll(int[] arrn) {
        this.setAllInternal(arrn, 0, arrn.length);
    }

    public void set(int n, int[] arrn, int n2, int n3) {
        this.rangeCheck(n + n3);
        System.arraycopy(arrn, n2, this.array, n, n3);
        this.fireChange(false, n, n + n3);
    }

    public void set(int n, ObservableIntegerArray observableIntegerArray, int n2, int n3) {
        this.rangeCheck(n + n3);
        observableIntegerArray.copyTo(n2, this.array, n, n3);
        this.fireChange(false, n, n + n3);
    }

    public int[] toArray(int[] arrn) {
        if (arrn == null || this.size() > arrn.length) {
            arrn = new int[this.size()];
        }
        System.arraycopy(this.array, 0, arrn, 0, this.size());
        return arrn;
    }

    public int get(int n) {
        this.rangeCheck(n + 1);
        return this.array[n];
    }

    public void set(int n, int n2) {
        this.rangeCheck(n + 1);
        this.array[n] = n2;
        this.fireChange(false, n, n + 1);
    }

    public int[] toArray(int n, int[] arrn, int n2) {
        this.rangeCheck(n + n2);
        if (arrn == null || n2 > arrn.length) {
            arrn = new int[n2];
        }
        System.arraycopy(this.array, n, arrn, 0, n2);
        return arrn;
    }

    public void copyTo(int n, int[] arrn, int n2, int n3) {
        this.rangeCheck(n + n3);
        System.arraycopy(this.array, n, arrn, n2, n3);
    }

    public void copyTo(int n, ObservableIntegerArray observableIntegerArray, int n2, int n3) {
        this.rangeCheck(n + n3);
        observableIntegerArray.set(n2, this.array, n, n3);
    }

    public void resize(int n) {
        if (n < 0) {
            throw new NegativeArraySizeException("Can't resize to negative value: " + n);
        }
        this.ensureCapacity(n);
        int n2 = Math.min(this.size, n);
        boolean bl = this.size != n;
        this.size = n;
        Arrays.fill(this.array, n2, this.size, 0);
        this.fireChange(bl, n2, n);
    }

    private void growCapacity(int n) {
        int n2 = this.size + n;
        int n3 = this.array.length;
        if (n2 > this.array.length) {
            int n4 = n3 + (n3 >> 1);
            if (n4 < n2) {
                n4 = n2;
            }
            if (n4 > 0x7FFFFFF7) {
                n4 = ObservableIntegerArrayImpl.hugeCapacity(n2);
            }
            this.ensureCapacity(n4);
        } else if (n > 0 && n2 < 0) {
            throw new OutOfMemoryError();
        }
    }

    public void ensureCapacity(int n) {
        if (this.array.length < n) {
            this.array = Arrays.copyOf(this.array, n);
        }
    }

    private static int hugeCapacity(int n) {
        if (n < 0) {
            throw new OutOfMemoryError();
        }
        return n > 0x7FFFFFF7 ? Integer.MAX_VALUE : 0x7FFFFFF7;
    }

    public void trimToSize() {
        if (this.array.length != this.size) {
            int[] arrn = new int[this.size];
            System.arraycopy(this.array, 0, arrn, 0, this.size);
            this.array = arrn;
        }
    }

    private void rangeCheck(int n) {
        if (n > this.size) {
            throw new ArrayIndexOutOfBoundsException(this.size);
        }
    }

    private void rangeCheck(ObservableIntegerArray observableIntegerArray, int n, int n2) {
        if (observableIntegerArray == null) {
            throw new NullPointerException();
        }
        if (n < 0 || n + n2 > observableIntegerArray.size()) {
            throw new ArrayIndexOutOfBoundsException(observableIntegerArray.size());
        }
        if (n2 < 0) {
            throw new ArrayIndexOutOfBoundsException(-1);
        }
    }

    private void rangeCheck(int[] arrn, int n, int n2) {
        if (arrn == null) {
            throw new NullPointerException();
        }
        if (n < 0 || n + n2 > arrn.length) {
            throw new ArrayIndexOutOfBoundsException(arrn.length);
        }
        if (n2 < 0) {
            throw new ArrayIndexOutOfBoundsException(-1);
        }
    }

    public String toString() {
        if (this.array == null) {
            return "null";
        }
        int n = this.size() - 1;
        if (n == -1) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        int n2 = 0;
        while (true) {
            stringBuilder.append(this.array[n2]);
            if (n2 == n) {
                return stringBuilder.append(']').toString();
            }
            stringBuilder.append(", ");
            ++n2;
        }
    }
}

