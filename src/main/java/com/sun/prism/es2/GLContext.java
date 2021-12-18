/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.javafx.PlatformUtil;
import com.sun.prism.MeshView;
import com.sun.prism.PhongMaterial;
import com.sun.prism.Texture;
import com.sun.prism.es2.BufferFactory;
import com.sun.prism.es2.ES2Pipeline;
import com.sun.prism.es2.GLDrawable;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

abstract class GLContext {
    static final int GL_ZERO = 0;
    static final int GL_ONE = 1;
    static final int GL_SRC_COLOR = 2;
    static final int GL_ONE_MINUS_SRC_COLOR = 3;
    static final int GL_DST_COLOR = 4;
    static final int GL_ONE_MINUS_DST_COLOR = 5;
    static final int GL_SRC_ALPHA = 6;
    static final int GL_ONE_MINUS_SRC_ALPHA = 7;
    static final int GL_DST_ALPHA = 8;
    static final int GL_ONE_MINUS_DST_ALPHA = 9;
    static final int GL_CONSTANT_COLOR = 10;
    static final int GL_ONE_MINUS_CONSTANT_COLOR = 11;
    static final int GL_CONSTANT_ALPHA = 12;
    static final int GL_ONE_MINUS_CONSTANT_ALPHA = 13;
    static final int GL_SRC_ALPHA_SATURATE = 14;
    static final int GL_FLOAT = 20;
    static final int GL_UNSIGNED_BYTE = 21;
    static final int GL_UNSIGNED_INT_8_8_8_8_REV = 22;
    static final int GL_UNSIGNED_INT_8_8_8_8 = 23;
    static final int GL_UNSIGNED_SHORT_8_8_APPLE = 24;
    static final int GL_RGBA = 40;
    static final int GL_BGRA = 41;
    static final int GL_RGB = 42;
    static final int GL_LUMINANCE = 43;
    static final int GL_ALPHA = 44;
    static final int GL_RGBA32F = 45;
    static final int GL_YCBCR_422_APPLE = 46;
    static final int GL_TEXTURE_2D = 50;
    static final int GL_TEXTURE_BINDING_2D = 51;
    static final int GL_NEAREST = 52;
    static final int GL_LINEAR = 53;
    static final int GL_NEAREST_MIPMAP_NEAREST = 54;
    static final int GL_LINEAR_MIPMAP_LINEAR = 55;
    static final int GL_UNPACK_ALIGNMENT = 60;
    static final int GL_UNPACK_ROW_LENGTH = 61;
    static final int GL_UNPACK_SKIP_PIXELS = 62;
    static final int GL_UNPACK_SKIP_ROWS = 63;
    static final int WRAPMODE_REPEAT = 100;
    static final int WRAPMODE_CLAMP_TO_EDGE = 101;
    static final int WRAPMODE_CLAMP_TO_BORDER = 102;
    static final int GL_BACK = 110;
    static final int GL_FRONT = 111;
    static final int GL_NONE = 112;
    static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 120;
    static final int GL_MAX_FRAGMENT_UNIFORM_VECTORS = 121;
    static final int GL_MAX_TEXTURE_IMAGE_UNITS = 122;
    static final int GL_MAX_TEXTURE_SIZE = 123;
    static final int GL_MAX_VERTEX_ATTRIBS = 124;
    static final int GL_MAX_VARYING_COMPONENTS = 125;
    static final int GL_MAX_VARYING_VECTORS = 126;
    static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 127;
    static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS = 128;
    static final int GL_MAX_VERTEX_UNIFORM_VECTORS = 129;
    static final int MAPTYPE_DIFFUSE = PhongMaterial.MapType.DIFFUSE.ordinal();
    static final int MAPTYPE_SPECULAR = PhongMaterial.MapType.SPECULAR.ordinal();
    static final int MAPTYPE_BUMP = PhongMaterial.MapType.BUMP.ordinal();
    static final int MAPTYPE_SELFILLUM = PhongMaterial.MapType.SELF_ILLUM.ordinal();
    static final int NUM_MATRIX_ELEMENTS = 16;
    long nativeCtxInfo;
    private int maxTextureSize = -1;
    private Boolean nonPowTwoExtAvailable;
    private Boolean clampToZeroAvailable;
    private int activeTexUnit;
    private int[] boundTextures = new int[4];
    private int viewportX;
    private int viewportY;
    private int viewportWidth;
    private int viewportHeight;
    private boolean depthTest = false;
    private boolean msaa = false;
    private int maxSampleSize = -1;
    private static final int FBO_ID_UNSET = -1;
    private static final int FBO_ID_NOCACHE = -2;
    private int nativeFBOID = PlatformUtil.isMac() || PlatformUtil.isIOS() ? -2 : -1;

    GLContext() {
    }

    private static native void nActiveTexture(long var0, int var2);

    private static native void nBindFBO(long var0, int var2);

    private static native void nBindTexture(long var0, int var2);

    private static native void nBlendFunc(int var0, int var1);

    private static native void nClearBuffers(long var0, float var2, float var3, float var4, float var5, boolean var6, boolean var7, boolean var8);

    private static native int nCompileShader(long var0, String var2, boolean var3);

    private static native int nCreateDepthBuffer(long var0, int var2, int var3, int var4);

    private static native int nCreateRenderBuffer(long var0, int var2, int var3, int var4);

    private static native int nCreateFBO(long var0, int var2);

    private static native int nCreateProgram(long var0, int var2, int[] var3, int var4, String[] var5, int[] var6);

    private static native int nCreateTexture(long var0, int var2, int var3);

    private static native void nDeleteRenderBuffer(long var0, int var2);

    private static native void nDeleteFBO(long var0, int var2);

    private static native void nDeleteShader(long var0, int var2);

    private static native void nDeleteTexture(long var0, int var2);

    private static native void nDisposeShaders(long var0, int var2, int var3, int[] var4);

    private static native void nFinish();

    private static native int nGenAndBindTexture();

    private static native int nGetFBO();

    private static native int nGetIntParam(int var0);

    private static native int nGetMaxSampleSize();

    private static native int nGetUniformLocation(long var0, int var2, String var3);

    private static native void nPixelStorei(int var0, int var1);

    private static native boolean nReadPixelsByte(long var0, int var2, Buffer var3, byte[] var4, int var5, int var6, int var7, int var8);

    private static native boolean nReadPixelsInt(long var0, int var2, Buffer var3, int[] var4, int var5, int var6, int var7, int var8);

    private static native void nScissorTest(long var0, boolean var2, int var3, int var4, int var5, int var6);

    private static native void nSetDepthTest(long var0, boolean var2);

    private static native void nSetMSAA(long var0, boolean var2);

    private static native void nTexParamsMinMax(int var0, int var1);

    private static native boolean nTexImage2D0(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, Object var8, int var9, boolean var10);

    private static native boolean nTexImage2D1(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, Object var8, int var9, boolean var10);

    private static native void nTexSubImage2D0(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, Object var8, int var9);

    private static native void nTexSubImage2D1(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, Object var8, int var9);

    private static native void nUpdateViewport(long var0, int var2, int var3, int var4, int var5);

    private static native void nUniform1f(long var0, int var2, float var3);

    private static native void nUniform2f(long var0, int var2, float var3, float var4);

    private static native void nUniform3f(long var0, int var2, float var3, float var4, float var5);

    private static native void nUniform4f(long var0, int var2, float var3, float var4, float var5, float var6);

    private static native void nUniform4fv0(long var0, int var2, int var3, Object var4, int var5);

    private static native void nUniform4fv1(long var0, int var2, int var3, Object var4, int var5);

    private static native void nUniform1i(long var0, int var2, int var3);

    private static native void nUniform2i(long var0, int var2, int var3, int var4);

    private static native void nUniform3i(long var0, int var2, int var3, int var4, int var5);

    private static native void nUniform4i(long var0, int var2, int var3, int var4, int var5, int var6);

    private static native void nUniform4iv0(long var0, int var2, int var3, Object var4, int var5);

    private static native void nUniform4iv1(long var0, int var2, int var3, Object var4, int var5);

    private static native void nUniformMatrix4fv(long var0, int var2, boolean var3, float[] var4);

    private static native void nUpdateFilterState(long var0, int var2, boolean var3);

    private static native void nUpdateWrapState(long var0, int var2, int var3);

    private static native void nUseProgram(long var0, int var2);

    private static native void nEnableVertexAttributes(long var0);

    private static native void nDisableVertexAttributes(long var0);

    private static native void nDrawIndexedQuads(long var0, int var2, float[] var3, byte[] var4);

    private static native int nCreateIndexBuffer16(long var0, short[] var2, int var3);

    private static native void nSetIndexBuffer(long var0, int var2);

    private static native void nSetDeviceParametersFor2D(long var0);

    private static native void nSetDeviceParametersFor3D(long var0);

    private static native long nCreateES2Mesh(long var0);

    private static native void nReleaseES2Mesh(long var0, long var2);

    private static native boolean nBuildNativeGeometryShort(long var0, long var2, float[] var4, int var5, short[] var6, int var7);

    private static native boolean nBuildNativeGeometryInt(long var0, long var2, float[] var4, int var5, int[] var6, int var7);

    private static native long nCreateES2PhongMaterial(long var0);

    private static native void nReleaseES2PhongMaterial(long var0, long var2);

    private static native void nSetSolidColor(long var0, long var2, float var4, float var5, float var6, float var7);

    private static native void nSetMap(long var0, long var2, int var4, int var5);

    private static native long nCreateES2MeshView(long var0, long var2);

    private static native void nReleaseES2MeshView(long var0, long var2);

    private static native void nSetCullingMode(long var0, long var2, int var4);

    private static native void nSetMaterial(long var0, long var2, long var4);

    private static native void nSetWireframe(long var0, long var2, boolean var4);

    private static native void nSetAmbientLight(long var0, long var2, float var4, float var5, float var6);

    private static native void nSetPointLight(long var0, long var2, int var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11);

    private static native void nRenderMeshView(long var0, long var2);

    private static native void nBlit(long var0, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11);

    void activeTexture(int n) {
        GLContext.nActiveTexture(this.nativeCtxInfo, n);
    }

    void bindFBO(int n) {
        switch (this.nativeFBOID) {
            case -1: {
                this.nativeFBOID = n;
                GLContext.nBindFBO(this.nativeCtxInfo, n);
                break;
            }
            case -2: {
                GLContext.nBindFBO(this.nativeCtxInfo, n);
                break;
            }
            default: {
                if (this.nativeFBOID == n) break;
                GLContext.nBindFBO(this.nativeCtxInfo, n);
                this.nativeFBOID = n;
            }
        }
    }

    void bindTexture(int n) {
        GLContext.nBindTexture(this.nativeCtxInfo, n);
    }

    void blendFunc(int n, int n2) {
        GLContext.nBlendFunc(n, n2);
    }

    boolean canCreateNonPowTwoTextures() {
        if (this.nonPowTwoExtAvailable == null) {
            this.nonPowTwoExtAvailable = PrismSettings.forcePow2 ? Boolean.FALSE.booleanValue() : ES2Pipeline.glFactory.isNPOTSupported();
        }
        return this.nonPowTwoExtAvailable;
    }

    boolean canClampToZero() {
        if (this.clampToZeroAvailable == null) {
            this.clampToZeroAvailable = PrismSettings.noClampToZero ? Boolean.FALSE.booleanValue() : ES2Pipeline.glFactory.isGL2();
        }
        return this.clampToZeroAvailable;
    }

    void clearBuffers(Color color, boolean bl, boolean bl2, boolean bl3) {
        float f = color.getRedPremult();
        float f2 = color.getGreenPremult();
        float f3 = color.getBluePremult();
        float f4 = color.getAlpha();
        GLContext.nClearBuffers(this.nativeCtxInfo, f, f2, f3, f4, bl, bl2, bl3);
    }

    int compileShader(String string, boolean bl) {
        return GLContext.nCompileShader(this.nativeCtxInfo, string, bl);
    }

    int createDepthBuffer(int n, int n2, int n3) {
        return GLContext.nCreateDepthBuffer(this.nativeCtxInfo, n, n2, n3);
    }

    int createRenderBuffer(int n, int n2, int n3) {
        return GLContext.nCreateRenderBuffer(this.nativeCtxInfo, n, n2, n3);
    }

    int createFBO(int n) {
        if (this.nativeFBOID != -2) {
            this.nativeFBOID = -1;
        }
        return GLContext.nCreateFBO(this.nativeCtxInfo, n);
    }

    int createProgram(int n, int[] arrn, String[] arrstring, int[] arrn2) {
        if (arrn == null) {
            System.err.println("Error: fragmentShaderIDArr is null");
            return 0;
        }
        boolean bl = true;
        for (int i = 0; i < arrn.length; ++i) {
            if (arrn[i] != 0) continue;
            bl = false;
            break;
        }
        if (n == 0 || arrn.length == 0 || !bl) {
            System.err.println("Both vertexShader and fragmentShader(s) must be specified");
        }
        if (arrstring.length != arrn2.length) {
            System.err.println("attrs.length must be equal to index.length");
        }
        return GLContext.nCreateProgram(this.nativeCtxInfo, n, arrn, arrstring.length, arrstring, arrn2);
    }

    int createTexture(int n, int n2) {
        return GLContext.nCreateTexture(this.nativeCtxInfo, n, n2);
    }

    void deleteRenderBuffer(int n) {
        GLContext.nDeleteRenderBuffer(this.nativeCtxInfo, n);
    }

    void deleteFBO(int n) {
        GLContext.nDeleteFBO(this.nativeCtxInfo, n);
    }

    void deleteShader(int n) {
        GLContext.nDeleteShader(this.nativeCtxInfo, n);
    }

    void blitFBO(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10) {
        GLContext.nBlit(this.nativeCtxInfo, n, n2, n3, n4, n5, n6, n7, n8, n9, n10);
    }

    void deleteTexture(int n) {
        GLContext.nDeleteTexture(this.nativeCtxInfo, n);
    }

    void disposeShaders(int n, int n2, int[] arrn) {
        GLContext.nDisposeShaders(this.nativeCtxInfo, n, n2, arrn);
    }

    void finish() {
        GLContext.nFinish();
    }

    int genAndBindTexture() {
        int n;
        this.boundTextures[this.activeTexUnit] = n = GLContext.nGenAndBindTexture();
        return n;
    }

    int getBoundFBO() {
        switch (this.nativeFBOID) {
            case -1: {
                this.nativeFBOID = GLContext.nGetFBO();
                return this.nativeFBOID;
            }
            case -2: {
                return GLContext.nGetFBO();
            }
        }
        return this.nativeFBOID;
    }

    long getNativeCtxInfo() {
        return this.nativeCtxInfo;
    }

    abstract long getNativeHandle();

    int getActiveTextureUnit() {
        return this.activeTexUnit;
    }

    void setActiveTextureUnit(int n) {
        this.activeTexture(n);
        this.activeTexUnit = n;
    }

    void updateActiveTextureUnit(int n) {
        if (n != this.getActiveTextureUnit()) {
            this.setActiveTextureUnit(n);
        }
    }

    int getBoundTexture() {
        return this.boundTextures[this.activeTexUnit];
    }

    int getBoundTexture(int n) {
        return this.boundTextures[n];
    }

    int getNumBoundTexture() {
        return this.boundTextures.length;
    }

    void setBoundTexture(int n) {
        this.bindTexture(n);
        this.boundTextures[this.activeTexUnit] = n;
    }

    void updateBoundTexture(int n) {
        if (n != this.getBoundTexture()) {
            this.setBoundTexture(n);
        }
    }

    int getIntParam(int n) {
        return GLContext.nGetIntParam(n);
    }

    int getSampleSize() {
        int n = this.getMaxSampleSize();
        return n < 2 ? 0 : (n < 4 ? 2 : 4);
    }

    int getMaxSampleSize() {
        if (this.maxSampleSize > -1) {
            return this.maxSampleSize;
        }
        this.maxSampleSize = ES2Pipeline.msaa ? GLContext.nGetMaxSampleSize() : 0;
        return this.maxSampleSize;
    }

    int getMaxTextureSize() {
        if (this.maxTextureSize > -1) {
            return this.maxTextureSize;
        }
        this.maxTextureSize = this.getIntParam(123);
        return this.maxTextureSize;
    }

    int getUniformLocation(int n, String string) {
        return GLContext.nGetUniformLocation(this.nativeCtxInfo, n, string);
    }

    boolean isShaderCompilerSupported() {
        return true;
    }

    abstract void makeCurrent(GLDrawable var1);

    void pixelStorei(int n, int n2) {
        GLContext.nPixelStorei(n, n2);
    }

    boolean readPixels(Buffer buffer, int n, int n2, int n3, int n4) {
        boolean bl = false;
        if (buffer instanceof ByteBuffer) {
            ByteBuffer byteBuffer = (ByteBuffer)buffer;
            byte[] arrby = byteBuffer.hasArray() ? byteBuffer.array() : null;
            int n5 = byteBuffer.capacity();
            bl = GLContext.nReadPixelsByte(this.nativeCtxInfo, n5, buffer, arrby, n, n2, n3, n4);
        } else if (buffer instanceof IntBuffer) {
            IntBuffer intBuffer = (IntBuffer)buffer;
            int[] arrn = intBuffer.hasArray() ? intBuffer.array() : null;
            int n6 = intBuffer.capacity() * 4;
            bl = GLContext.nReadPixelsInt(this.nativeCtxInfo, n6, buffer, arrn, n, n2, n3, n4);
        } else {
            throw new IllegalArgumentException("readPixel: pixel's buffer type is not supported: " + buffer);
        }
        return bl;
    }

    void scissorTest(boolean bl, int n, int n2, int n3, int n4) {
        GLContext.nScissorTest(this.nativeCtxInfo, bl, n, n2, n3, n4);
    }

    void setShaderProgram(int n) {
        GLContext.nUseProgram(this.nativeCtxInfo, n);
    }

    void texParamsMinMax(int n, boolean bl) {
        int n2 = n;
        int n3 = n;
        if (bl) {
            n2 = n2 == 53 ? 55 : 54;
        }
        GLContext.nTexParamsMinMax(n2, n3);
    }

    boolean texImage2D(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, Buffer buffer, boolean bl) {
        boolean bl2 = BufferFactory.isDirect(buffer);
        boolean bl3 = bl2 ? GLContext.nTexImage2D0(n, n2, n3, n4, n5, n6, n7, n8, buffer, BufferFactory.getDirectBufferByteOffset(buffer), bl) : GLContext.nTexImage2D1(n, n2, n3, n4, n5, n6, n7, n8, BufferFactory.getArray(buffer), BufferFactory.getIndirectBufferByteOffset(buffer), bl);
        return bl3;
    }

    void texSubImage2D(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, Buffer buffer) {
        boolean bl = BufferFactory.isDirect(buffer);
        if (bl) {
            GLContext.nTexSubImage2D0(n, n2, n3, n4, n5, n6, n7, n8, buffer, BufferFactory.getDirectBufferByteOffset(buffer));
        } else {
            GLContext.nTexSubImage2D1(n, n2, n3, n4, n5, n6, n7, n8, BufferFactory.getArray(buffer), BufferFactory.getIndirectBufferByteOffset(buffer));
        }
    }

    void updateViewportAndDepthTest(int n, int n2, int n3, int n4, boolean bl) {
        if (this.viewportX != n || this.viewportY != n2 || this.viewportWidth != n3 || this.viewportHeight != n4) {
            this.viewportX = n;
            this.viewportY = n2;
            this.viewportWidth = n3;
            this.viewportHeight = n4;
            GLContext.nUpdateViewport(this.nativeCtxInfo, n, n2, n3, n4);
        }
        if (this.depthTest != bl) {
            GLContext.nSetDepthTest(this.nativeCtxInfo, bl);
            this.depthTest = bl;
        }
    }

    void updateMSAAState(boolean bl) {
        if (this.msaa != bl) {
            GLContext.nSetMSAA(this.nativeCtxInfo, bl);
            this.msaa = bl;
        }
    }

    void updateFilterState(int n, boolean bl) {
        GLContext.nUpdateFilterState(this.nativeCtxInfo, n, bl);
    }

    void updateWrapState(int n, Texture.WrapMode wrapMode) {
        int n2;
        switch (wrapMode) {
            case REPEAT_SIMULATED: 
            case REPEAT: {
                n2 = 100;
                break;
            }
            case CLAMP_TO_ZERO_SIMULATED: 
            case CLAMP_TO_EDGE_SIMULATED: 
            case CLAMP_TO_EDGE: {
                n2 = 101;
                break;
            }
            case CLAMP_TO_ZERO: {
                n2 = 102;
                break;
            }
            case CLAMP_NOT_NEEDED: {
                return;
            }
            default: {
                throw new InternalError("Unrecognized wrap mode: " + (Object)((Object)wrapMode));
            }
        }
        GLContext.nUpdateWrapState(this.nativeCtxInfo, n, n2);
    }

    void uniform1f(int n, float f) {
        GLContext.nUniform1f(this.nativeCtxInfo, n, f);
    }

    void uniform2f(int n, float f, float f2) {
        GLContext.nUniform2f(this.nativeCtxInfo, n, f, f2);
    }

    void uniform3f(int n, float f, float f2, float f3) {
        GLContext.nUniform3f(this.nativeCtxInfo, n, f, f2, f3);
    }

    void uniform4f(int n, float f, float f2, float f3, float f4) {
        GLContext.nUniform4f(this.nativeCtxInfo, n, f, f2, f3, f4);
    }

    void uniform4fv(int n, int n2, FloatBuffer floatBuffer) {
        boolean bl = BufferFactory.isDirect(floatBuffer);
        if (bl) {
            GLContext.nUniform4fv0(this.nativeCtxInfo, n, n2, floatBuffer, BufferFactory.getDirectBufferByteOffset(floatBuffer));
        } else {
            GLContext.nUniform4fv1(this.nativeCtxInfo, n, n2, BufferFactory.getArray(floatBuffer), BufferFactory.getIndirectBufferByteOffset(floatBuffer));
        }
    }

    void uniform1i(int n, int n2) {
        GLContext.nUniform1i(this.nativeCtxInfo, n, n2);
    }

    void uniform2i(int n, int n2, int n3) {
        GLContext.nUniform2i(this.nativeCtxInfo, n, n2, n3);
    }

    void uniform3i(int n, int n2, int n3, int n4) {
        GLContext.nUniform3i(this.nativeCtxInfo, n, n2, n3, n4);
    }

    void uniform4i(int n, int n2, int n3, int n4, int n5) {
        GLContext.nUniform4i(this.nativeCtxInfo, n, n2, n3, n4, n5);
    }

    void uniform4iv(int n, int n2, IntBuffer intBuffer) {
        boolean bl = BufferFactory.isDirect(intBuffer);
        if (bl) {
            GLContext.nUniform4iv0(this.nativeCtxInfo, n, n2, intBuffer, BufferFactory.getDirectBufferByteOffset(intBuffer));
        } else {
            GLContext.nUniform4iv1(this.nativeCtxInfo, n, n2, BufferFactory.getArray(intBuffer), BufferFactory.getIndirectBufferByteOffset(intBuffer));
        }
    }

    void uniformMatrix4fv(int n, boolean bl, float[] arrf) {
        GLContext.nUniformMatrix4fv(this.nativeCtxInfo, n, bl, arrf);
    }

    void enableVertexAttributes() {
        GLContext.nEnableVertexAttributes(this.nativeCtxInfo);
    }

    void disableVertexAttributes() {
        GLContext.nDisableVertexAttributes(this.nativeCtxInfo);
    }

    void drawIndexedQuads(float[] arrf, byte[] arrby, int n) {
        GLContext.nDrawIndexedQuads(this.nativeCtxInfo, n, arrf, arrby);
    }

    int createIndexBuffer16(short[] arrs) {
        return GLContext.nCreateIndexBuffer16(this.nativeCtxInfo, arrs, arrs.length);
    }

    void setIndexBuffer(int n) {
        GLContext.nSetIndexBuffer(this.nativeCtxInfo, n);
    }

    void setDeviceParametersFor2D() {
        GLContext.nSetDeviceParametersFor2D(this.nativeCtxInfo);
    }

    void setDeviceParametersFor3D() {
        GLContext.nSetDeviceParametersFor3D(this.nativeCtxInfo);
    }

    long createES2Mesh() {
        return GLContext.nCreateES2Mesh(this.nativeCtxInfo);
    }

    void releaseES2Mesh(long l) {
        GLContext.nReleaseES2Mesh(this.nativeCtxInfo, l);
    }

    boolean buildNativeGeometry(long l, float[] arrf, int n, short[] arrs, int n2) {
        return GLContext.nBuildNativeGeometryShort(this.nativeCtxInfo, l, arrf, n, arrs, n2);
    }

    boolean buildNativeGeometry(long l, float[] arrf, int n, int[] arrn, int n2) {
        return GLContext.nBuildNativeGeometryInt(this.nativeCtxInfo, l, arrf, n, arrn, n2);
    }

    long createES2PhongMaterial() {
        return GLContext.nCreateES2PhongMaterial(this.nativeCtxInfo);
    }

    void releaseES2PhongMaterial(long l) {
        GLContext.nReleaseES2PhongMaterial(this.nativeCtxInfo, l);
    }

    void setSolidColor(long l, float f, float f2, float f3, float f4) {
        GLContext.nSetSolidColor(this.nativeCtxInfo, l, f, f2, f3, f4);
    }

    void setMap(long l, int n, int n2) {
        GLContext.nSetMap(this.nativeCtxInfo, l, n, n2);
    }

    long createES2MeshView(long l) {
        return GLContext.nCreateES2MeshView(this.nativeCtxInfo, l);
    }

    void releaseES2MeshView(long l) {
        GLContext.nReleaseES2MeshView(this.nativeCtxInfo, l);
    }

    void setCullingMode(long l, int n) {
        int n2;
        if (n == MeshView.CULL_NONE) {
            n2 = 112;
        } else if (n == MeshView.CULL_BACK) {
            n2 = 110;
        } else if (n == MeshView.CULL_FRONT) {
            n2 = 111;
        } else {
            throw new IllegalArgumentException("illegal value for CullMode: " + n);
        }
        GLContext.nSetCullingMode(this.nativeCtxInfo, l, n2);
    }

    void setMaterial(long l, long l2) {
        GLContext.nSetMaterial(this.nativeCtxInfo, l, l2);
    }

    void setWireframe(long l, boolean bl) {
        GLContext.nSetWireframe(this.nativeCtxInfo, l, bl);
    }

    void setAmbientLight(long l, float f, float f2, float f3) {
        GLContext.nSetAmbientLight(this.nativeCtxInfo, l, f, f2, f3);
    }

    void setPointLight(long l, int n, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        GLContext.nSetPointLight(this.nativeCtxInfo, l, n, f, f2, f3, f4, f5, f6, f7);
    }

    void renderMeshView(long l) {
        GLContext.nRenderMeshView(this.nativeCtxInfo, l);
    }
}

