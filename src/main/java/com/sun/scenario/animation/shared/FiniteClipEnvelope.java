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

public class FiniteClipEnvelope
extends ClipEnvelope {
    private boolean autoReverse;
    private int cycleCount;
    private long totalTicks;
    private long pos;

    protected FiniteClipEnvelope(Animation animation) {
        super(animation);
        if (animation != null) {
            this.autoReverse = animation.isAutoReverse();
            this.cycleCount = animation.getCycleCount();
        }
        this.updateTotalTicks();
    }

    @Override
    public void setAutoReverse(boolean bl) {
        this.autoReverse = bl;
    }

    @Override
    protected double calculateCurrentRate() {
        return !this.autoReverse ? this.rate : (this.ticks % (2L * this.cycleTicks) < this.cycleTicks == this.rate > 0.0 ? this.rate : -this.rate);
    }

    @Override
    public ClipEnvelope setCycleDuration(Duration duration) {
        if (duration.isIndefinite()) {
            return FiniteClipEnvelope.create(this.animation);
        }
        this.updateCycleTicks(duration);
        this.updateTotalTicks();
        return this;
    }

    @Override
    public ClipEnvelope setCycleCount(int n) {
        if (n == 1 || n == -1) {
            return FiniteClipEnvelope.create(this.animation);
        }
        this.cycleCount = n;
        this.updateTotalTicks();
        return this;
    }

    @Override
    public void setRate(double d) {
        boolean bl = d * this.rate < 0.0;
        long l = bl ? this.totalTicks - this.ticks : this.ticks;
        Animation.Status status = this.animation.getStatus();
        if (status != Animation.Status.STOPPED) {
            if (status == Animation.Status.RUNNING) {
                this.setCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12 ? d : -d);
            }
            this.deltaTicks = l - Math.round((double)(this.ticks - this.deltaTicks) * Math.abs(d / this.rate));
            this.abortCurrentPulse();
        }
        this.ticks = l;
        this.rate = d;
    }

    private void updateTotalTicks() {
        this.totalTicks = (long)this.cycleCount * this.cycleTicks;
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
            long l2;
            long l3 = this.ticks;
            this.ticks = ClipEnvelope.checkBounds(this.deltaTicks + Math.round((double)l * Math.abs(this.rate)), this.totalTicks);
            boolean bl = this.ticks >= this.totalTicks;
            long l4 = this.ticks - l3;
            if (l4 == 0L) {
                return;
            }
            long l5 = l2 = this.currentRate > 0.0 ? this.cycleTicks - this.pos : this.pos;
            while (l4 >= l2) {
                if (l2 > 0L) {
                    this.pos = this.currentRate > 0.0 ? this.cycleTicks : 0L;
                    l4 -= l2;
                    AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
                    if (this.aborted) {
                        return;
                    }
                }
                if (!bl || l4 > 0L) {
                    if (this.autoReverse) {
                        this.setCurrentRate(-this.currentRate);
                    } else {
                        this.pos = this.currentRate > 0.0 ? 0L : this.cycleTicks;
                        AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
                    }
                }
                l2 = this.cycleTicks;
            }
            if (l4 > 0L && !bl) {
                this.pos += this.currentRate > 0.0 ? l4 : -l4;
                AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
            }
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
        long l2 = this.ticks;
        if (this.rate < 0.0) {
            l = this.totalTicks - l;
        }
        this.ticks = ClipEnvelope.checkBounds(l, this.totalTicks);
        long l3 = this.ticks - l2;
        if (l3 != 0L) {
            this.deltaTicks += l3;
            if (this.autoReverse) {
                boolean bl = this.ticks % (2L * this.cycleTicks) < this.cycleTicks;
                if (bl == this.rate > 0.0) {
                    this.pos = this.ticks % this.cycleTicks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        this.setCurrentRate(Math.abs(this.rate));
                    }
                } else {
                    this.pos = this.cycleTicks - this.ticks % this.cycleTicks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        this.setCurrentRate(-Math.abs(this.rate));
                    }
                }
            } else {
                this.pos = this.ticks % this.cycleTicks;
                if (this.rate < 0.0) {
                    this.pos = this.cycleTicks - this.pos;
                }
                if (this.pos == 0L && this.ticks > 0L) {
                    this.pos = this.cycleTicks;
                }
            }
            AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
            this.abortCurrentPulse();
        }
    }
}

