/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation
 *  javafx.animation.Animation$Status
 *  javafx.util.Duration
 */
package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.shared.AnimationAccessor;
import com.sun.scenario.animation.shared.ClipEnvelope;
import javafx.animation.Animation;
import javafx.util.Duration;

public class SingleLoopClipEnvelope
extends ClipEnvelope {
    private int cycleCount;

    @Override
    public void setRate(double d) {
        Animation.Status status = this.animation.getStatus();
        if (status != Animation.Status.STOPPED) {
            if (status == Animation.Status.RUNNING) {
                this.setCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12 ? d : -d);
            }
            this.deltaTicks = this.ticks - Math.round((double)(this.ticks - this.deltaTicks) * d / this.rate);
            this.abortCurrentPulse();
        }
        this.rate = d;
    }

    @Override
    public void setAutoReverse(boolean bl) {
    }

    @Override
    protected double calculateCurrentRate() {
        return this.rate;
    }

    protected SingleLoopClipEnvelope(Animation animation) {
        super(animation);
        if (animation != null) {
            this.cycleCount = animation.getCycleCount();
        }
    }

    @Override
    public boolean wasSynched() {
        return super.wasSynched() && this.cycleCount != 0;
    }

    @Override
    public ClipEnvelope setCycleDuration(Duration duration) {
        if (this.cycleCount != 1 && !duration.isIndefinite()) {
            return SingleLoopClipEnvelope.create(this.animation);
        }
        this.updateCycleTicks(duration);
        return this;
    }

    @Override
    public ClipEnvelope setCycleCount(int n) {
        if (n != 1 && this.cycleTicks != Long.MAX_VALUE) {
            return SingleLoopClipEnvelope.create(this.animation);
        }
        this.cycleCount = n;
        return this;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void timePulse(long l) {
        if (this.cycleTicks == 0L) {
            return;
        }
        this.aborted = false;
        this.inTimePulse = true;
        try {
            boolean bl;
            this.ticks = ClipEnvelope.checkBounds(this.deltaTicks + Math.round((double)l * this.currentRate), this.cycleTicks);
            AnimationAccessor.getDefault().playTo(this.animation, this.ticks, this.cycleTicks);
            boolean bl2 = this.currentRate > 0.0 ? this.ticks == this.cycleTicks : (bl = this.ticks == 0L);
            if (bl && !this.aborted) {
                AnimationAccessor.getDefault().finished(this.animation);
            }
        }
        finally {
            this.inTimePulse = false;
        }
    }

    @Override
    public void jumpTo(long l) {
        if (this.cycleTicks == 0L) {
            return;
        }
        long l2 = ClipEnvelope.checkBounds(l, this.cycleTicks);
        this.deltaTicks += l2 - this.ticks;
        this.ticks = l2;
        AnimationAccessor.getDefault().jumpTo(this.animation, l2, this.cycleTicks, false);
        this.abortCurrentPulse();
    }
}

