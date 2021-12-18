/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

final class ExtendedTime
implements Comparable<ExtendedTime> {
    private final long baseTime;
    private final int subtime;

    ExtendedTime(long l, int n) {
        this.baseTime = l;
        this.subtime = n;
    }

    static ExtendedTime currentTime() {
        return new ExtendedTime(System.currentTimeMillis(), 0);
    }

    long baseTime() {
        return this.baseTime;
    }

    int subtime() {
        return this.subtime;
    }

    ExtendedTime incrementSubtime() {
        return new ExtendedTime(this.baseTime, this.subtime + 1);
    }

    @Override
    public int compareTo(ExtendedTime extendedTime) {
        int n = (int)(this.baseTime - extendedTime.baseTime);
        if (n != 0) {
            return n;
        }
        return this.subtime - extendedTime.subtime;
    }

    public String toString() {
        return "[baseTime=" + this.baseTime + ", subtime=" + this.subtime + "]";
    }
}

