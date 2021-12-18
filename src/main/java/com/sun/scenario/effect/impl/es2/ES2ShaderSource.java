/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.es2;

import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.hw.ShaderSource;
import java.io.InputStream;

public class ES2ShaderSource
implements ShaderSource {
    @Override
    public InputStream loadSource(String string) {
        return ES2ShaderSource.class.getResourceAsStream("glsl/" + string + ".frag");
    }

    @Override
    public Effect.AccelType getAccelType() {
        return Effect.AccelType.OPENGL;
    }
}

