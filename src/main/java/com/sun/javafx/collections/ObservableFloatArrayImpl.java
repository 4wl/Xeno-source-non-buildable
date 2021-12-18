/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableArrayBase
 *  javafx.collections.ObservableFloatArray
 */
package com.sun.javafx.collections;

import java.util.Arrays;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableFloatArray;

public final class ObservableFloatArrayImpl
extends ObservableArrayBase<ObservableFloatArray>
implements ObservableFloatArray {
    private static final float[] INITIAL = new float[0];
    private float[] array = INITIAL;
    private int size = 0;
    private static final int MAX_ARRAY_SIZE = 0x7FFFFFF7;

    public ObservableFloatArrayImpl() {
    }

    public ObservableFloatArrayImpl(float ... arrf) {
        this.setAll(arrf);
    }

    public ObservableFloatArrayImpl(ObservableFloatArray observableFloatArray) {
        this.setAll(observableFloatArray);
    }

    public void clear() {
        this.resize(0);
    }

    public int size() {
        return this.size;
    }

    private void addAllInternal(ObservableFloatArray observableFloatArray, int n, int n2) {
        this.growCapacity(n2);
        observableFloatArray.copyTo(n, this.array, this.size, n2);
        this.size += n2;
        this.fireChange(n2 != 0, this.size - n2, this.size);
    }

    private void addAllInternal(float[] arrf, int n, int n2) {
        this.growCapacity(n2);
        System.arraycopy(arrf, n, this.array, this.size, n2);
        this.size += n2;
        this.fireChange(n2 != 0, this.size - n2, this.size);
    }

    public void addAll(ObservableFloatArray observableFloatArray) {
        this.addAllInternal(observableFloatArray, 0, observableFloatArray.size());
    }

    public void addAll(float ... arrf) {
        this.addAllInternal(arrf, 0, arrf.length);
    }

    public void addAll(ObservableFloatArray observableFloatArray, int n, int n2) {
        this.rangeCheck(observableFloatArray, n, n2);
        this.addAllInternal(observableFloatArray, n, n2);
    }

    public void addAll(float[] arrf, int n, int n2) {
        this.rangeCheck(arrf, n, n2);
        this.addAllInternal(arrf, n, n2);
    }

    private void setAllInternal(ObservableFloatArray observableFloatArray, int n, int n2) {
        boolean bl;
        boolean bl2 = bl = this.size() != n2;
        if (observableFloatArray == this) {
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
            observableFloatArray.copyTo(n, this.array, 0, n2);
            this.size = n2;
            this.fireChange(bl, 0, this.size);
        }
    }

    private void setAllInternal(float[] arrf, int n, int n2) {
        boolean bl = this.size() != n2;
        this.size = 0;
        this.ensureCapacity(n2);
        System.arraycopy(arrf, n, this.array, 0, n2);
        this.size = n2;
        this.fireChange(bl, 0, this.size);
    }

    public void setAll(ObservableFloatArray observableFloatArray) {
        this.setAllInternal(observableFloatArray, 0, observableFloatArray.size());
    }

    public void setAll(ObservableFloatArray observableFloatArray, int n, int n2) {
        this.rangeCheck(observableFloatArray, n, n2);
        this.setAllInternal(observableFloatArray, n, n2);
    }

    public void setAll(float[] arrf, int n, int n2) {
        this.rangeCheck(arrf, n, n2);
        this.setAllInternal(arrf, n, n2);
    }

    public void setAll(float[] arrf) {
        this.setAllInternal(arrf, 0, arrf.length);
    }

    public void set(int n, float[] arrf, int n2, int n3) {
        this.rangeCheck(n + n3);
        System.arraycopy(arrf, n2, this.array, n, n3);
        this.fireChange(false, n, n + n3);
    }

    public void set(int n, ObservableFloatArray observableFloatArray, int n2, int n3) {
        this.rangeCheck(n + n3);
        observableFloatArray.copyTo(n2, this.array, n, n3);
        this.fireChange(false, n, n + n3);
    }

    public float[] toArray(float[] arrf) {
        if (arrf == null || this.size() > arrf.length) {
            arrf = new float[this.size()];
        }
        System.arraycopy(this.array, 0, arrf, 0, this.size());
        return arrf;
    }

    public float get(int n) {
        this.rangeCheck(n + 1);
        return this.array[n];
    }

    public void set(int n, float f) {
        this.rangeCheck(n + 1);
        this.array[n] = f;
        this.fireChange(false, n, n + 1);
    }

    public float[] toArray(int n, float[] arrf, int n2) {
        this.rangeCheck(n + n2);
        if (arrf == null || n2 > arrf.length) {
            arrf = new float[n2];
        }
        System.arraycopy(this.array, n, arrf, 0, n2);
        return arrf;
    }

    public void copyTo(int n, float[] arrf, int n2, int n3) {
        this.rangeCheck(n + n3);
        System.arraycopy(this.array, n, arrf, n2, n3);
    }

    public void copyTo(int n, ObservableFloatArray observableFloatArray, int n2, int n3) {
        this.rangeCheck(n + n3);
        observableFloatArray.set(n2, this.array, n, n3);
    }

    public void resize(int n) {
        if (n < 0) {
            throw new NegativeArraySizeException("Can't resize to negative value: " + n);
        }
        this.ensureCapacity(n);
        int n2 = Math.min(this.size, n);
        boolean bl = this.size != n;
        this.size = n;
        Arrays.fill(this.array, n2, this.size, 0.0f);
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
                n4 = ObservableFloatArrayImpl.hugeCapacity(n2);
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
            float[] arrf = new float[this.size];
            System.arraycopy(this.array, 0, arrf, 0, this.size);
            this.array = arrf;
        }
    }

    private void rangeCheck(int n) {
        if (n > this.size) {
            throw new ArrayIndexOutOfBoundsException(this.size);
        }
    }

    private void rangeCheck(ObservableFloatArray observableFloatArray, int n, int n2) {
        if (observableFloatArray == null) {
            throw new NullPointerException();
        }
        if (n < 0 || n + n2 > observableFloatArray.size()) {
            throw new ArrayIndexOutOfBoundsException(observableFloatArray.size());
        }
        if (n2 < 0) {
            throw new ArrayIndexOutOfBoundsException(-1);
        }
    }

    private void rangeCheck(float[] arrf, int n, int n2) {
        if (arrf == null) {
            throw new NullPointerException();
        }
        if (n < 0 || n + n2 > arrf.length) {
            throw new ArrayIndexOutOfBoundsException(arrf.length);
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

