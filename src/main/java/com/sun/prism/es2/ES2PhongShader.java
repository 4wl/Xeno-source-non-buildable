/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2Light;
import com.sun.prism.es2.ES2MeshView;
import com.sun.prism.es2.ES2PhongMaterial;
import com.sun.prism.es2.ES2ResourceFactory;
import com.sun.prism.es2.ES2Shader;
import java.util.HashMap;

class ES2PhongShader {
    static ES2Shader[][][][][] shaders = null;
    static String vertexShaderSource;
    static String mainFragShaderSource;
    static final int lightStateCount = 4;
    private static String[] diffuseShaderParts;
    private static String[] specularShaderParts;
    private static String[] selfIllumShaderParts;
    private static String[] normalMapShaderParts;
    private static String[] lightingShaderParts;

    ES2PhongShader() {
    }

    static SpecularState getSpecularState(ES2PhongMaterial eS2PhongMaterial) {
        if (eS2PhongMaterial.maps[ES2PhongMaterial.SPECULAR].getTexture() != null) {
            return eS2PhongMaterial.specularColorSet ? SpecularState.MIX : SpecularState.TEXTURE;
        }
        return eS2PhongMaterial.specularColorSet ? SpecularState.COLOR : SpecularState.NONE;
    }

    /*
     * WARNING - void declaration
     */
    static ES2Shader getShader(ES2MeshView eS2MeshView, ES2Context eS2Context) {
        void var8_11;
        ES2PhongMaterial eS2PhongMaterial = eS2MeshView.getMaterial();
        DiffuseState diffuseState = DiffuseState.DIFFUSECOLOR;
        if (eS2PhongMaterial.maps[ES2PhongMaterial.DIFFUSE].getTexture() != null) {
            diffuseState = DiffuseState.TEXTURE;
        }
        SpecularState specularState = ES2PhongShader.getSpecularState(eS2PhongMaterial);
        BumpMapState bumpMapState = BumpMapState.NONE;
        if (eS2PhongMaterial.maps[ES2PhongMaterial.BUMP].getTexture() != null) {
            bumpMapState = BumpMapState.TEXTURE;
        }
        SelfIllumState selfIllumState = SelfIllumState.NONE;
        if (eS2PhongMaterial.maps[ES2PhongMaterial.SELF_ILLUM].getTexture() != null) {
            selfIllumState = SelfIllumState.TEXTURE;
        }
        int n = 0;
        for (ES2Light eS2Light : eS2MeshView.getPointLights()) {
            if (eS2Light == null || !(eS2Light.w > 0.0f)) continue;
            ++n;
        }
        ES2Shader eS2Shader = shaders[diffuseState.ordinal()][specularState.ordinal()][selfIllumState.ordinal()][bumpMapState.ordinal()][n];
        if (eS2Shader == null) {
            ES2Shader eS2Shader2;
            String string = lightingShaderParts[n].replace("vec4 apply_diffuse();", diffuseShaderParts[diffuseState.ordinal()]);
            string = string.replace("vec4 apply_specular();", specularShaderParts[specularState.ordinal()]);
            string = string.replace("vec3 apply_normal();", normalMapShaderParts[bumpMapState.ordinal()]);
            string = string.replace("vec4 apply_selfIllum();", selfIllumShaderParts[selfIllumState.ordinal()]);
            String[] arrstring = new String[]{string};
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("pos", 0);
            hashMap.put("texCoords", 1);
            hashMap.put("tangent", 2);
            HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
            hashMap2.put("diffuseTexture", 0);
            hashMap2.put("specularMap", 1);
            hashMap2.put("normalMap", 2);
            hashMap2.put("selfIllumTexture", 3);
            ES2PhongShader.shaders[diffuseState.ordinal()][specularState.ordinal()][selfIllumState.ordinal()][bumpMapState.ordinal()][n] = eS2Shader2 = ES2Shader.createFromSource(eS2Context, vertexShaderSource, arrstring, hashMap2, hashMap, 1, false);
        }
        return var8_11;
    }

    static void setShaderParamaters(ES2Shader eS2Shader, ES2MeshView eS2MeshView, ES2Context eS2Context) {
        ES2PhongMaterial eS2PhongMaterial = eS2MeshView.getMaterial();
        eS2Shader.setConstant("diffuseColor", eS2PhongMaterial.diffuseColor.getRed(), eS2PhongMaterial.diffuseColor.getGreen(), eS2PhongMaterial.diffuseColor.getBlue(), eS2PhongMaterial.diffuseColor.getAlpha());
        eS2Shader.setConstant("specularColor", eS2PhongMaterial.specularColor.getRed(), eS2PhongMaterial.specularColor.getGreen(), eS2PhongMaterial.specularColor.getBlue(), eS2PhongMaterial.specularColor.getAlpha());
        eS2Context.updateTexture(0, eS2PhongMaterial.maps[ES2PhongMaterial.DIFFUSE].getTexture());
        eS2Context.updateTexture(1, eS2PhongMaterial.maps[ES2PhongMaterial.SPECULAR].getTexture());
        eS2Context.updateTexture(2, eS2PhongMaterial.maps[ES2PhongMaterial.BUMP].getTexture());
        eS2Context.updateTexture(3, eS2PhongMaterial.maps[ES2PhongMaterial.SELF_ILLUM].getTexture());
        eS2Shader.setConstant("ambientColor", eS2MeshView.getAmbientLightRed(), eS2MeshView.getAmbientLightGreen(), eS2MeshView.getAmbientLightBlue());
        int n = 0;
        for (ES2Light eS2Light : eS2MeshView.getPointLights()) {
            if (eS2Light == null || !(eS2Light.w > 0.0f)) continue;
            eS2Shader.setConstant("lights[" + n + "].pos", eS2Light.x, eS2Light.y, eS2Light.z, eS2Light.w);
            eS2Shader.setConstant("lights[" + n + "].color", eS2Light.r, eS2Light.g, eS2Light.b);
            ++n;
        }
    }

    static {
        diffuseShaderParts = new String[DiffuseState.values().length];
        specularShaderParts = new String[SpecularState.values().length];
        selfIllumShaderParts = new String[SelfIllumState.values().length];
        normalMapShaderParts = new String[BumpMapState.values().length];
        lightingShaderParts = new String[4];
        shaders = new ES2Shader[DiffuseState.values().length][SpecularState.values().length][SelfIllumState.values().length][BumpMapState.values().length][4];
        ES2PhongShader.diffuseShaderParts[DiffuseState.NONE.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/diffuse_none.frag"));
        ES2PhongShader.diffuseShaderParts[DiffuseState.DIFFUSECOLOR.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/diffuse_color.frag"));
        ES2PhongShader.diffuseShaderParts[DiffuseState.TEXTURE.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/diffuse_texture.frag"));
        ES2PhongShader.specularShaderParts[SpecularState.NONE.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/specular_none.frag"));
        ES2PhongShader.specularShaderParts[SpecularState.TEXTURE.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/specular_texture.frag"));
        ES2PhongShader.specularShaderParts[SpecularState.COLOR.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/specular_color.frag"));
        ES2PhongShader.specularShaderParts[SpecularState.MIX.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/specular_mix.frag"));
        ES2PhongShader.selfIllumShaderParts[SelfIllumState.NONE.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/selfIllum_none.frag"));
        ES2PhongShader.selfIllumShaderParts[SelfIllumState.TEXTURE.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/selfIllum_texture.frag"));
        ES2PhongShader.normalMapShaderParts[BumpMapState.NONE.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/normalMap_none.frag"));
        ES2PhongShader.normalMapShaderParts[BumpMapState.TEXTURE.ordinal()] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/normalMap_texture.frag"));
        ES2PhongShader.lightingShaderParts[0] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/main0Lights.frag"));
        ES2PhongShader.lightingShaderParts[1] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/main1Light.frag"));
        ES2PhongShader.lightingShaderParts[2] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/main2Lights.frag"));
        ES2PhongShader.lightingShaderParts[3] = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/main3Lights.frag"));
        vertexShaderSource = ES2Shader.readStreamIntoString(ES2ResourceFactory.class.getResourceAsStream("glsl/main.vert"));
    }

    static enum BumpMapState {
        NONE,
        TEXTURE;

    }

    static enum SelfIllumState {
        NONE,
        TEXTURE;

    }

    static enum SpecularState {
        NONE,
        TEXTURE,
        COLOR,
        MIX;

    }

    static enum DiffuseState {
        NONE,
        DIFFUSECOLOR,
        TEXTURE;

    }
}

