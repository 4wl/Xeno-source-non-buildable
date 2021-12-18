/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.utils.other;

public class Timer {
    public long lastMS = 0L;

    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (System.currentTimeMillis() - this.lastMS > time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public boolean delay(float milliSec) {
        return (float)(this.getTime() - this.lastMS) >= milliSec;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long getDifference() {
        return this.getTime() - this.lastMS;
    }

    public void setDifference(long difference) {
        this.lastMS = this.getTime() - difference;
    }
}

