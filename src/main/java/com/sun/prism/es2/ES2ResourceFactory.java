/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.glass.ui.Screen;
import com.sun.javafx.PlatformUtil;
import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.Mesh;
import com.sun.prism.MeshView;
import com.sun.prism.PhongMaterial;
import com.sun.prism.PixelFormat;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2Mesh;
import com.sun.prism.es2.ES2MeshView;
import com.sun.prism.es2.ES2PhongMaterial;
import com.sun.prism.es2.ES2Pipeline;
import com.sun.prism.es2.ES2RTTexture;
import com.sun.prism.es2.ES2Shader;
import com.sun.prism.es2.ES2SwapChain;
import com.sun.prism.es2.ES2Texture;
import com.sun.prism.es2.ES2VramPool;
import com.sun.prism.es2.GLFactory;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.ps.BaseShaderFactory;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class ES2ResourceFactory
extends BaseShaderFactory {
    private static final Map<Image, Texture> clampTexCache = new WeakHashMap<Image, Texture>();
    private static final Map<Image, Texture> repeatTexCache = new WeakHashMap<Image, Texture>();
    private static final Map<Image, Texture> mipmapTexCache = new WeakHashMap<Image, Texture>();
    private ES2Context context;
    private final int maxTextureSize;

    ES2ResourceFactory(Screen screen) {
        super(clampTexCache, repeatTexCache, mipmapTexCache);
        this.context = new ES2Context(screen, this);
        this.maxTextureSize = this.computeMaxTextureSize();
        if (PrismSettings.verbose) {
            int n;
            int n2;
            int n3;
            System.out.println("Non power of two texture support = " + this.context.getGLContext().canCreateNonPowTwoTextures());
            System.out.println("Maximum number of vertex attributes = " + this.context.getGLContext().getIntParam(124));
            if (PlatformUtil.isEmbedded()) {
                n3 = this.context.getGLContext().getIntParam(129) * 4;
                n2 = this.context.getGLContext().getIntParam(121) * 4;
                n = this.context.getGLContext().getIntParam(126) * 4;
            } else {
                n3 = this.context.getGLContext().getIntParam(128);
                n2 = this.context.getGLContext().getIntParam(120);
                n = this.context.getGLContext().getIntParam(125);
            }
            System.out.println("Maximum number of uniform vertex components = " + n3);
            System.out.println("Maximum number of uniform fragment components = " + n2);
            System.out.println("Maximum number of varying components = " + n);
            System.out.println("Maximum number of texture units usable in a vertex shader = " + this.context.getGLContext().getIntParam(127));
            System.out.println("Maximum number of texture units usable in a fragment shader = " + this.context.getGLContext().getIntParam(122));
        }
    }

    @Override
    public TextureResourcePool getTextureResourcePool() {
        return ES2VramPool.instance;
    }

    @Override
    public Presentable createPresentable(PresentableState presentableState) {
        return new ES2SwapChain(this.context, presentableState);
    }

    @Override
    public boolean isCompatibleTexture(Texture texture) {
        return texture instanceof ES2Texture;
    }

    @Override
    protected boolean canClampToZero() {
        return this.context.getGLContext().canClampToZero();
    }

    @Override
    protected boolean canRepeat() {
        return this.context.getGLContext().canCreateNonPowTwoTextures();
    }

    @Override
    protected boolean canClampToEdge() {
        return this.context.getGLContext().canCreateNonPowTwoTextures();
    }

    @Override
    public Texture createTexture(PixelFormat pixelFormat, Texture.Usage usage, Texture.WrapMode wrapMode, int n, int n2) {
        return this.createTexture(pixelFormat, usage, wrapMode, n, n2, false);
    }

    @Override
    public Texture createTexture(PixelFormat pixelFormat, Texture.Usage usage, Texture.WrapMode wrapMode, int n, int n2, boolean bl) {
        return ES2Texture.create(this.context, pixelFormat, wrapMode, n, n2, bl);
    }

    @Override
    public Texture createTexture(MediaFrame mediaFrame) {
        return ES2Texture.create(this.context, mediaFrame);
    }

    @Override
    public int getRTTWidth(int n, Texture.WrapMode wrapMode) {
        return ES2RTTexture.getCompatibleDimension(this.context, n, wrapMode);
    }

    @Override
    public int getRTTHeight(int n, Texture.WrapMode wrapMode) {
        return ES2RTTexture.getCompatibleDimension(this.context, n, wrapMode);
    }

    @Override
    public RTTexture createRTTexture(int n, int n2, Texture.WrapMode wrapMode) {
        return this.createRTTexture(n, n2, wrapMode, false);
    }

    @Override
    public RTTexture createRTTexture(int n, int n2, Texture.WrapMode wrapMode, boolean bl) {
        return ES2RTTexture.create(this.context, n, n2, wrapMode, bl);
    }

    @Override
    public boolean isFormatSupported(PixelFormat pixelFormat) {
        GLFactory gLFactory = ES2Pipeline.glFactory;
        switch (pixelFormat) {
            case BYTE_RGB: 
            case BYTE_GRAY: 
            case BYTE_ALPHA: 
            case MULTI_YCbCr_420: {
                return true;
            }
            case BYTE_BGRA_PRE: 
            case INT_ARGB_PRE: {
                if (gLFactory.isGL2() || PlatformUtil.isIOS()) {
                    return true;
                }
                return gLFactory.isGLExtensionSupported("GL_EXT_texture_format_BGRA8888");
            }
            case FLOAT_XYZW: {
                return gLFactory.isGL2();
            }
            case BYTE_APPLE_422: {
                return gLFactory.isGLExtensionSupported("GL_APPLE_ycbcr_422");
            }
        }
        return false;
    }

    private int computeMaxTextureSize() {
        int n = this.context.getGLContext().getMaxTextureSize();
        if (PrismSettings.verbose) {
            System.out.println("Maximum supported texture size: " + n);
        }
        if (n > PrismSettings.maxTextureSize) {
            n = PrismSettings.maxTextureSize;
            if (PrismSettings.verbose) {
                System.out.println("Maximum texture size clamped to " + n);
            }
        }
        return n;
    }

    @Override
    public int getMaximumTextureSize() {
        return this.maxTextureSize;
    }

    @Override
    public Shader createShader(InputStream inputStream, Map<String, Integer> map, Map<String, Integer> map2, int n, boolean bl, boolean bl2) {
        Map<String, Integer> map3 = this.getVertexAttributes(bl2, n);
        String string = ES2ResourceFactory.createVertexShaderCode(bl2, n);
        ES2Shader eS2Shader = ES2Shader.createFromSource(this.context, string, inputStream, map, map3, n, bl);
        return eS2Shader;
    }

    private static String createVertexShaderCode(boolean bl, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("void main() {\n");
        boolean bl2 = true;
        if (bl2) {
            stringBuilder.append("attribute vec2 positionAttr;\n");
            stringBuilder3.append("    vec4 tmp = vec4(positionAttr, 0, 1);\n");
            stringBuilder3.append("    gl_Position = mvpMatrix * tmp;\n");
        }
        if (bl) {
            stringBuilder.append("attribute vec4 colorAttr;\n");
            stringBuilder2.append("varying lowp vec4 perVertexColor;\n");
            stringBuilder3.append("    perVertexColor = colorAttr;\n");
        }
        if (n >= 0) {
            stringBuilder.append("attribute vec2 texCoord0Attr;\n");
            stringBuilder2.append("varying vec2 texCoord0;\n");
            stringBuilder3.append("    texCoord0 = texCoord0Attr;\n");
        }
        if (n >= 1) {
            stringBuilder.append("attribute vec2 texCoord1Attr;\n");
            stringBuilder2.append("varying vec2 texCoord1;\n");
            stringBuilder3.append("    texCoord1 = texCoord1Attr;\n");
        }
        stringBuilder3.append("}\n");
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("#ifdef GL_ES\n");
        stringBuilder4.append("#else\n");
        stringBuilder4.append("#define lowp\n");
        stringBuilder4.append("#endif\n");
        stringBuilder4.append("uniform mat4 mvpMatrix;\n");
        stringBuilder4.append((CharSequence)stringBuilder);
        stringBuilder4.append((CharSequence)stringBuilder2);
        stringBuilder4.append((CharSequence)stringBuilder3);
        return stringBuilder4.toString();
    }

    private Map<String, Integer> getVertexAttributes(boolean bl, int n) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        boolean bl2 = true;
        if (bl2) {
            hashMap.put("positionAttr", 0);
        }
        if (bl) {
            hashMap.put("colorAttr", 1);
        }
        if (n >= 0) {
            hashMap.put("texCoord0Attr", 2);
        }
        if (n >= 1) {
            hashMap.put("texCoord1Attr", 3);
        }
        return hashMap;
    }

    @Override
    public Shader createStockShader(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Shader name must be non-null");
        }
        try {
            InputStream inputStream = ES2ResourceFactory.class.getResourceAsStream("glsl/" + string + ".frag");
            Class<?> class_ = Class.forName("com.sun.prism.shader." + string + "_Loader");
            if (PrismSettings.verbose) {
                System.out.println("ES2ResourceFactory: Prism - createStockShader: " + string + ".frag");
            }
            Method method = class_.getMethod("loadShader", ShaderFactory.class, InputStream.class);
            return (Shader)method.invoke(null, this, inputStream);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new InternalError("Error loading stock shader " + string);
        }
    }

    @Override
    public void dispose() {
        this.context.clearContext();
    }

    @Override
    public PhongMaterial createPhongMaterial() {
        return ES2PhongMaterial.create(this.context);
    }

    @Override
    public MeshView createMeshView(Mesh mesh) {
        return ES2MeshView.create(this.context, (ES2Mesh)mesh);
    }

    @Override
    public Mesh createMesh() {
        return ES2Mesh.create(this.context);
    }
}

