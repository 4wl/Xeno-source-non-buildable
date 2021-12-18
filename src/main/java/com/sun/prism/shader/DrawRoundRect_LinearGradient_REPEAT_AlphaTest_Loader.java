/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class DrawRoundRect_LinearGradient_REPEAT_AlphaTest_Loader {
    private DrawRoundRect_LinearGradient_REPEAT_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory shaderFactory, InputStream inputStream) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("colors", 0);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("perspVec", 16);
        hashMap2.put("gradParams", 15);
        hashMap2.put("offset", 14);
        hashMap2.put("iinvarcradii", 1);
        hashMap2.put("fractions", 2);
        hashMap2.put("oinvarcradii", 0);
        return shaderFactory.createShader(inputStream, hashMap, hashMap2, 1, true, true);
    }
}

