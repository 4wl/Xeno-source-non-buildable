/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGDefaultCamera;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.Material;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.es2.ES2Graphics;
import com.sun.prism.es2.ES2Mesh;
import com.sun.prism.es2.ES2MeshView;
import com.sun.prism.es2.ES2PhongMaterial;
import com.sun.prism.es2.ES2PhongShader;
import com.sun.prism.es2.ES2Pipeline;
import com.sun.prism.es2.ES2RTTexture;
import com.sun.prism.es2.ES2RenderTarget;
import com.sun.prism.es2.ES2Shader;
import com.sun.prism.es2.ES2SwapChain;
import com.sun.prism.es2.ES2Texture;
import com.sun.prism.es2.GLContext;
import com.sun.prism.es2.GLDrawable;
import com.sun.prism.es2.GLFactory;
import com.sun.prism.es2.GLPixelFormat;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ps.BaseShaderContext;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;

class ES2Context
extends BaseShaderContext {
    private static GeneralTransform3D scratchTx = new GeneralTransform3D();
    private static final GeneralTransform3D flipTx = new GeneralTransform3D();
    private static final Affine3D scratchAffine3DTx = new Affine3D();
    private static float[] rawMatrix = new float[16];
    private GeneralTransform3D projViewTx = new GeneralTransform3D();
    private GeneralTransform3D worldTx = new GeneralTransform3D();
    private Vec3d cameraPos = new Vec3d();
    private RenderTarget currentTarget;
    private final GLContext glContext;
    private final GLDrawable dummyGLDrawable;
    private final GLPixelFormat pixelFormat;
    private BaseShaderContext.State state;
    private int quadIndices;
    private GLDrawable currentDrawable = null;
    private int indexBuffer = 0;
    private int shaderProgram;
    public static final int NUM_QUADS = PrismSettings.superShader ? 4096 : 256;

    ES2Context(Screen screen, ShaderFactory shaderFactory) {
        super(screen, shaderFactory, NUM_QUADS);
        GLFactory gLFactory = ES2Pipeline.glFactory;
        this.pixelFormat = gLFactory.createGLPixelFormat(screen.getNativeScreen(), ES2Pipeline.pixelFormatAttributes);
        this.dummyGLDrawable = gLFactory.createDummyGLDrawable(this.pixelFormat);
        this.glContext = gLFactory.createGLContext(this.dummyGLDrawable, this.pixelFormat, gLFactory.getShareContext(), PrismSettings.isVsyncEnabled);
        this.makeCurrent(this.dummyGLDrawable);
        this.glContext.enableVertexAttributes();
        this.quadIndices = this.genQuadsIndexBuffer(NUM_QUADS);
        this.setIndexBuffer(this.quadIndices);
        this.state = new BaseShaderContext.State();
    }

    static short[] getQuadIndices16bit(int n) {
        short[] arrs = new short[n * 6];
        for (int i = 0; i != n; ++i) {
            int n2 = i * 4;
            int n3 = i * 6;
            arrs[n3 + 0] = (short)(n2 + 0);
            arrs[n3 + 1] = (short)(n2 + 1);
            arrs[n3 + 2] = (short)(n2 + 2);
            arrs[n3 + 3] = (short)(n2 + 2);
            arrs[n3 + 4] = (short)(n2 + 1);
            arrs[n3 + 5] = (short)(n2 + 3);
        }
        return arrs;
    }

    int genQuadsIndexBuffer(int n) {
        if (n * 6 > 65536) {
            throw new IllegalArgumentException("vertex indices overflow");
        }
        return this.glContext.createIndexBuffer16(ES2Context.getQuadIndices16bit(n));
    }

    final void clearContext() {
        if (this.currentDrawable != null) {
            this.currentDrawable.swapBuffers(this.glContext);
        }
    }

    final void setIndexBuffer(int n) {
        if (this.indexBuffer != n) {
            this.indexBuffer = n;
            this.glContext.setIndexBuffer(this.indexBuffer);
        }
    }

    GLContext getGLContext() {
        return this.glContext;
    }

    GLPixelFormat getPixelFormat() {
        return this.pixelFormat;
    }

    ES2Shader getPhongShader(ES2MeshView eS2MeshView) {
        return ES2PhongShader.getShader(eS2MeshView, this);
    }

    void makeCurrent(GLDrawable gLDrawable) {
        if (gLDrawable == null) {
            gLDrawable = this.dummyGLDrawable;
        }
        if (gLDrawable != this.currentDrawable) {
            this.glContext.makeCurrent(gLDrawable);
            this.glContext.bindFBO(0);
            this.currentDrawable = gLDrawable;
        }
    }

    void forceRenderTarget(ES2Graphics eS2Graphics) {
        this.updateRenderTarget(eS2Graphics.getRenderTarget(), eS2Graphics.getCameraNoClone(), eS2Graphics.isDepthTest() && eS2Graphics.isDepthBuffer());
    }

    int getShaderProgram() {
        return this.shaderProgram;
    }

    void setShaderProgram(int n) {
        this.shaderProgram = n;
        this.glContext.setShaderProgram(n);
    }

    void updateShaderProgram(int n) {
        if (n != this.shaderProgram) {
            this.setShaderProgram(n);
        }
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void releaseRenderTarget() {
        this.currentTarget = null;
        super.releaseRenderTarget();
    }

    @Override
    protected BaseShaderContext.State updateRenderTarget(RenderTarget renderTarget, NGCamera nGCamera, boolean bl) {
        int n = ((ES2RenderTarget)((Object)renderTarget)).getFboID();
        this.glContext.bindFBO(n);
        boolean bl2 = false;
        if (renderTarget instanceof ES2RTTexture) {
            ES2RTTexture eS2RTTexture = (ES2RTTexture)renderTarget;
            bl2 = eS2RTTexture.isMSAA();
            if (bl) {
                eS2RTTexture.attachDepthBuffer(this);
            }
        }
        int n2 = renderTarget.getContentX();
        int n3 = renderTarget.getContentY();
        int n4 = renderTarget.getContentWidth();
        int n5 = renderTarget.getContentHeight();
        this.glContext.updateViewportAndDepthTest(n2, n3, n4, n5, bl);
        this.glContext.updateMSAAState(bl2);
        if (nGCamera instanceof NGDefaultCamera) {
            ((NGDefaultCamera)nGCamera).validate(n4, n5);
            scratchTx = nGCamera.getProjViewTx(scratchTx);
        } else {
            scratchTx = nGCamera.getProjViewTx(scratchTx);
            double d = nGCamera.getViewWidth();
            double d2 = nGCamera.getViewHeight();
            if ((double)n4 != d || (double)n5 != d2) {
                scratchTx.scale(d / (double)n4, d2 / (double)n5, 1.0);
            }
        }
        if (renderTarget instanceof ES2RTTexture) {
            this.projViewTx.set(flipTx);
            this.projViewTx.mul(scratchTx);
        } else {
            this.projViewTx.set(scratchTx);
        }
        this.cameraPos = nGCamera.getPositionInWorld(this.cameraPos);
        this.currentTarget = renderTarget;
        return this.state;
    }

    @Override
    protected void updateTexture(int n, Texture texture) {
        this.glContext.updateActiveTextureUnit(n);
        if (texture == null) {
            this.glContext.updateBoundTexture(0);
        } else {
            ES2Texture eS2Texture = (ES2Texture)texture;
            this.glContext.updateBoundTexture(eS2Texture.getNativeSourceHandle());
            eS2Texture.updateWrapState();
            eS2Texture.updateFilterState();
        }
    }

    @Override
    protected void updateShaderTransform(Shader shader, BaseTransform baseTransform) {
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        scratchTx.set(this.projViewTx);
        this.updateRawMatrix(scratchTx.mul(baseTransform));
        ES2Shader eS2Shader = (ES2Shader)shader;
        eS2Shader.setMatrix("mvpMatrix", rawMatrix);
        if (eS2Shader.isPixcoordUsed()) {
            float f;
            float f2;
            float f3 = this.currentTarget.getContentX();
            float f4 = this.currentTarget.getContentY();
            if (this.currentTarget instanceof ES2SwapChain) {
                f2 = this.currentTarget.getPhysicalHeight();
                f = 1.0f;
            } else {
                f2 = 0.0f;
                f = -1.0f;
            }
            shader.setConstant("jsl_pixCoordOffset", f3, f4, f2, f);
        }
    }

    @Override
    protected void updateWorldTransform(BaseTransform baseTransform) {
        this.worldTx.setIdentity();
        if (baseTransform != null && !baseTransform.isIdentity()) {
            this.worldTx.mul(baseTransform);
        }
    }

    @Override
    protected void updateClipRect(Rectangle rectangle) {
        if (rectangle == null || rectangle.isEmpty()) {
            this.glContext.scissorTest(false, 0, 0, 0, 0);
        } else {
            int n = rectangle.width;
            int n2 = rectangle.height;
            int n3 = this.currentTarget.getContentX();
            int n4 = this.currentTarget.getContentY();
            if (this.currentTarget instanceof ES2RTTexture) {
                n3 += rectangle.x;
                n4 += rectangle.y;
            } else {
                int n5 = this.currentTarget.getPhysicalHeight();
                n3 += rectangle.x;
                n4 += n5 - (rectangle.y + n2);
            }
            this.glContext.scissorTest(true, n3, n4, n, n2);
        }
    }

    @Override
    protected void updateCompositeMode(CompositeMode compositeMode) {
        switch (compositeMode) {
            case CLEAR: {
                this.glContext.blendFunc(0, 0);
                break;
            }
            case SRC: {
                this.glContext.blendFunc(1, 0);
                break;
            }
            case SRC_OVER: {
                this.glContext.blendFunc(1, 7);
                break;
            }
            case DST_OUT: {
                this.glContext.blendFunc(0, 7);
                break;
            }
            case ADD: {
                this.glContext.blendFunc(1, 1);
                break;
            }
            default: {
                throw new InternalError("Unrecognized composite mode: " + (Object)((Object)compositeMode));
            }
        }
    }

    @Override
    public void setDeviceParametersFor2D() {
        this.indexBuffer = 0;
        this.shaderProgram = 0;
        this.glContext.setDeviceParametersFor2D();
        this.glContext.enableVertexAttributes();
        this.setIndexBuffer(this.quadIndices);
    }

    @Override
    public void setDeviceParametersFor3D() {
        this.glContext.disableVertexAttributes();
        this.glContext.setDeviceParametersFor3D();
    }

    long createES2Mesh() {
        return this.glContext.createES2Mesh();
    }

    void releaseES2Mesh(long l) {
        this.glContext.releaseES2Mesh(l);
    }

    boolean buildNativeGeometry(long l, float[] arrf, int n, short[] arrs, int n2) {
        return this.glContext.buildNativeGeometry(l, arrf, n, arrs, n2);
    }

    boolean buildNativeGeometry(long l, float[] arrf, int n, int[] arrn, int n2) {
        return this.glContext.buildNativeGeometry(l, arrf, n, arrn, n2);
    }

    long createES2PhongMaterial() {
        return this.glContext.createES2PhongMaterial();
    }

    void releaseES2PhongMaterial(long l) {
        this.glContext.releaseES2PhongMaterial(l);
    }

    void setSolidColor(long l, float f, float f2, float f3, float f4) {
        this.glContext.setSolidColor(l, f, f2, f3, f4);
    }

    void setMap(long l, int n, int n2) {
        this.glContext.setMap(l, n, n2);
    }

    long createES2MeshView(ES2Mesh eS2Mesh) {
        return this.glContext.createES2MeshView(eS2Mesh.getNativeHandle());
    }

    void releaseES2MeshView(long l) {
        this.glContext.releaseES2MeshView(l);
    }

    void setCullingMode(long l, int n) {
        this.glContext.setCullingMode(l, n);
    }

    void setMaterial(long l, Material material) {
        ES2PhongMaterial eS2PhongMaterial = (ES2PhongMaterial)material;
        this.glContext.setMaterial(l, eS2PhongMaterial.getNativeHandle());
    }

    void setWireframe(long l, boolean bl) {
        this.glContext.setWireframe(l, bl);
    }

    void setAmbientLight(long l, float f, float f2, float f3) {
        this.glContext.setAmbientLight(l, f, f2, f3);
    }

    void setPointLight(long l, int n, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.glContext.setPointLight(l, n, f, f2, f3, f4, f5, f6, f7);
    }

    @Override
    public void blit(RTTexture rTTexture, RTTexture rTTexture2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int n9 = rTTexture2 == null ? 0 : ((ES2RTTexture)rTTexture2).getFboID();
        int n10 = ((ES2RTTexture)rTTexture).getFboID();
        this.glContext.blitFBO(n10, n9, n, n2, n3, n4, n5, n6, n7, n8);
    }

    void renderMeshView(long l, Graphics graphics, ES2MeshView eS2MeshView) {
        ES2Shader eS2Shader = this.getPhongShader(eS2MeshView);
        this.setShaderProgram(eS2Shader.getProgramObject());
        float f = graphics.getPixelScaleFactor();
        if ((double)f != 1.0) {
            scratchTx = scratchTx.set(this.projViewTx);
            scratchTx.scale(f, f, 1.0);
            this.updateRawMatrix(scratchTx);
        } else {
            this.updateRawMatrix(this.projViewTx);
        }
        eS2Shader.setMatrix("viewProjectionMatrix", rawMatrix);
        eS2Shader.setConstant("camPos", (float)this.cameraPos.x, (float)this.cameraPos.y, (float)this.cameraPos.z);
        BaseTransform baseTransform = graphics.getTransformNoClone();
        if ((double)f != 1.0) {
            float f2 = 1.0f / f;
            scratchAffine3DTx.setToIdentity();
            scratchAffine3DTx.scale(f2, f2);
            scratchAffine3DTx.concatenate(baseTransform);
            this.updateWorldTransform(scratchAffine3DTx);
        } else {
            this.updateWorldTransform(baseTransform);
        }
        this.updateRawMatrix(this.worldTx);
        eS2Shader.setMatrix("worldMatrix", rawMatrix);
        ES2PhongShader.setShaderParamaters(eS2Shader, eS2MeshView, this);
        this.glContext.renderMeshView(l);
    }

    @Override
    protected void renderQuads(float[] arrf, byte[] arrby, int n) {
        this.glContext.drawIndexedQuads(arrf, arrby, n);
    }

    void printRawMatrix(String string) {
        System.err.println(string + " = ");
        for (int i = 0; i < 4; ++i) {
            System.err.println(rawMatrix[i] + ", " + rawMatrix[i + 4] + ", " + rawMatrix[i + 8] + ", " + rawMatrix[i + 12]);
        }
    }

    private void updateRawMatrix(GeneralTransform3D generalTransform3D) {
        ES2Context.rawMatrix[0] = (float)generalTransform3D.get(0);
        ES2Context.rawMatrix[1] = (float)generalTransform3D.get(4);
        ES2Context.rawMatrix[2] = (float)generalTransform3D.get(8);
        ES2Context.rawMatrix[3] = (float)generalTransform3D.get(12);
        ES2Context.rawMatrix[4] = (float)generalTransform3D.get(1);
        ES2Context.rawMatrix[5] = (float)generalTransform3D.get(5);
        ES2Context.rawMatrix[6] = (float)generalTransform3D.get(9);
        ES2Context.rawMatrix[7] = (float)generalTransform3D.get(13);
        ES2Context.rawMatrix[8] = (float)generalTransform3D.get(2);
        ES2Context.rawMatrix[9] = (float)generalTransform3D.get(6);
        ES2Context.rawMatrix[10] = (float)generalTransform3D.get(10);
        ES2Context.rawMatrix[11] = (float)generalTransform3D.get(14);
        ES2Context.rawMatrix[12] = (float)generalTransform3D.get(3);
        ES2Context.rawMatrix[13] = (float)generalTransform3D.get(7);
        ES2Context.rawMatrix[14] = (float)generalTransform3D.get(11);
        ES2Context.rawMatrix[15] = (float)generalTransform3D.get(15);
    }

    static {
        BaseTransform baseTransform = Affine2D.getScaleInstance(1.0, -1.0);
        flipTx.setIdentity();
        flipTx.mul(baseTransform);
    }
}

