/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.LinearConvolveCoreEffect;

public abstract class AbstractShadow
extends LinearConvolveCoreEffect {
    public AbstractShadow(Effect effect) {
        super(effect);
    }

    public abstract ShadowMode getMode();

    public abstract AbstractShadow implFor(ShadowMode var1);

    public abstract float getGaussianRadius();

    public abstract void setGaussianRadius(float var1);

    public abstract float getGaussianWidth();

    public abstract void setGaussianWidth(float var1);

    public abstract float getGaussianHeight();

    public abstract void setGaussianHeight(float var1);

    public abstract float getSpread();

    public abstract void setSpread(float var1);

    public abstract Color4f getColor();

    public abstract void setColor(Color4f var1);

    public abstract Effect getInput();

    public abstract void setInput(Effect var1);

    public static enum ShadowMode {
        ONE_PASS_BOX,
        TWO_PASS_BOX,
        THREE_PASS_BOX,
        GAUSSIAN;

    }
}

