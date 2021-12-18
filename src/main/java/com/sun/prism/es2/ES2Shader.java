/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.GLContext;
import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;
import com.sun.prism.ps.Shader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ES2Shader
extends BaseGraphicsResource
implements Shader {
    private int programID;
    private final ES2Context context;
    private final Map<String, Uniform> uniforms = new HashMap<String, Uniform>();
    private final int maxTexCoordIndex;
    private final boolean isPixcoordUsed;
    private boolean valid;
    private float[] currentMatrix;

    private ES2Shader(ES2Context eS2Context, int n, int n2, int[] arrn, Map<String, Integer> map, int n3, boolean bl) throws RuntimeException {
        super(new ES2ShaderDisposerRecord(eS2Context, n2, arrn, n));
        this.context = eS2Context;
        this.programID = n;
        this.maxTexCoordIndex = n3;
        this.isPixcoordUsed = bl;
        boolean bl2 = this.valid = n != 0;
        if (this.valid && map != null) {
            int n4 = eS2Context.getShaderProgram();
            eS2Context.setShaderProgram(n);
            for (String string : map.keySet()) {
                this.setConstant(string, map.get(string));
            }
            eS2Context.setShaderProgram(n4);
        }
    }

    static ES2Shader createFromSource(ES2Context eS2Context, String string, String[] arrstring, Map<String, Integer> map, Map<String, Integer> map2, int n, boolean bl) {
        GLContext gLContext = eS2Context.getGLContext();
        if (!gLContext.isShaderCompilerSupported()) {
            throw new RuntimeException("Shader compiler not available on this device");
        }
        if (string == null || arrstring == null || arrstring.length == 0) {
            throw new RuntimeException("Both vertexShaderSource and fragmentShaderSource must be specified");
        }
        int n2 = gLContext.compileShader(string, true);
        if (n2 == 0) {
            throw new RuntimeException("Error creating vertex shader");
        }
        int[] arrn = new int[arrstring.length];
        for (int i = 0; i < arrstring.length; ++i) {
            arrn[i] = gLContext.compileShader(arrstring[i], false);
            if (arrn[i] != 0) continue;
            gLContext.deleteShader(n2);
            throw new RuntimeException("Error creating fragment shader");
        }
        String[] arrstring2 = new String[map2.size()];
        int[] arrn2 = new int[arrstring2.length];
        int n3 = 0;
        Iterator<String> iterator = map2.keySet().iterator();
        while (iterator.hasNext()) {
            String string2;
            arrstring2[n3] = string2 = iterator.next();
            arrn2[n3] = map2.get(string2);
            ++n3;
        }
        int n4 = gLContext.createProgram(n2, arrn, arrstring2, arrn2);
        if (n4 == 0) {
            throw new RuntimeException("Error creating shader program");
        }
        return new ES2Shader(eS2Context, n4, n2, arrn, map, n, bl);
    }

    static ES2Shader createFromSource(ES2Context eS2Context, String string, InputStream inputStream, Map<String, Integer> map, Map<String, Integer> map2, int n, boolean bl) {
        String[] arrstring = new String[]{ES2Shader.readStreamIntoString(inputStream)};
        return ES2Shader.createFromSource(eS2Context, string, arrstring, map, map2, n, bl);
    }

    static String readStreamIntoString(InputStream inputStream) {
        StringBuffer stringBuffer = new StringBuffer(1024);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            char[] arrc = new char[1024];
            int n = 0;
            while ((n = bufferedReader.read(arrc)) > -1) {
                stringBuffer.append(String.valueOf(arrc, 0, n));
            }
        }
        catch (IOException iOException) {
            throw new RuntimeException("Error reading shader stream");
        }
        finally {
            try {
                bufferedReader.close();
            }
            catch (IOException iOException) {
                throw new RuntimeException("Error closing reader");
            }
        }
        return stringBuffer.toString();
    }

    public int getProgramObject() {
        return this.programID;
    }

    public int getMaxTexCoordIndex() {
        return this.maxTexCoordIndex;
    }

    public boolean isPixcoordUsed() {
        return this.isPixcoordUsed;
    }

    private Uniform getUniform(String string) {
        Uniform uniform = this.uniforms.get(string);
        if (uniform == null) {
            int n = this.context.getGLContext().getUniformLocation(this.programID, string);
            uniform = new Uniform();
            uniform.location = n;
            this.uniforms.put(string, uniform);
        }
        return uniform;
    }

    @Override
    public void enable() throws RuntimeException {
        this.context.updateShaderProgram(this.programID);
    }

    @Override
    public void disable() throws RuntimeException {
        this.context.updateShaderProgram(0);
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public void setConstant(String string, int n) throws RuntimeException {
        int[] arrn;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new int[1];
        }
        if ((arrn = (int[])uniform.values)[0] != n) {
            arrn[0] = n;
            this.context.getGLContext().uniform1i(uniform.location, n);
        }
    }

    @Override
    public void setConstant(String string, int n, int n2) throws RuntimeException {
        int[] arrn;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new int[2];
        }
        if ((arrn = (int[])uniform.values)[0] != n || arrn[1] != n2) {
            arrn[0] = n;
            arrn[1] = n2;
            this.context.getGLContext().uniform2i(uniform.location, n, n2);
        }
    }

    @Override
    public void setConstant(String string, int n, int n2, int n3) throws RuntimeException {
        int[] arrn;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new int[3];
        }
        if ((arrn = (int[])uniform.values)[0] != n || arrn[1] != n2 || arrn[2] != n3) {
            arrn[0] = n;
            arrn[1] = n2;
            arrn[2] = n3;
            this.context.getGLContext().uniform3i(uniform.location, n, n2, n3);
        }
    }

    @Override
    public void setConstant(String string, int n, int n2, int n3, int n4) throws RuntimeException {
        int[] arrn;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new int[4];
        }
        if ((arrn = (int[])uniform.values)[0] != n || arrn[1] != n2 || arrn[2] != n3 || arrn[3] != n4) {
            arrn[0] = n;
            arrn[1] = n2;
            arrn[2] = n3;
            arrn[3] = n4;
            this.context.getGLContext().uniform4i(uniform.location, n, n2, n3, n4);
        }
    }

    @Override
    public void setConstant(String string, float f) throws RuntimeException {
        float[] arrf;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new float[1];
        }
        if ((arrf = (float[])uniform.values)[0] != f) {
            arrf[0] = f;
            this.context.getGLContext().uniform1f(uniform.location, f);
        }
    }

    @Override
    public void setConstant(String string, float f, float f2) throws RuntimeException {
        float[] arrf;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new float[2];
        }
        if ((arrf = (float[])uniform.values)[0] != f || arrf[1] != f2) {
            arrf[0] = f;
            arrf[1] = f2;
            this.context.getGLContext().uniform2f(uniform.location, f, f2);
        }
    }

    @Override
    public void setConstant(String string, float f, float f2, float f3) throws RuntimeException {
        float[] arrf;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new float[3];
        }
        if ((arrf = (float[])uniform.values)[0] != f || arrf[1] != f2 || arrf[2] != f3) {
            arrf[0] = f;
            arrf[1] = f2;
            arrf[2] = f3;
            this.context.getGLContext().uniform3f(uniform.location, f, f2, f3);
        }
    }

    @Override
    public void setConstant(String string, float f, float f2, float f3, float f4) throws RuntimeException {
        float[] arrf;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new float[4];
        }
        if ((arrf = (float[])uniform.values)[0] != f || arrf[1] != f2 || arrf[2] != f3 || arrf[3] != f4) {
            arrf[0] = f;
            arrf[1] = f2;
            arrf[2] = f3;
            arrf[3] = f4;
            this.context.getGLContext().uniform4f(uniform.location, f, f2, f3, f4);
        }
    }

    @Override
    public void setConstants(String string, IntBuffer intBuffer, int n, int n2) throws RuntimeException {
        int n3 = this.getUniform(string).location;
        if (n3 == -1) {
            return;
        }
        this.context.getGLContext().uniform4iv(n3, n2, intBuffer);
    }

    @Override
    public void setConstants(String string, FloatBuffer floatBuffer, int n, int n2) throws RuntimeException {
        int n3 = this.getUniform(string).location;
        if (n3 == -1) {
            return;
        }
        this.context.getGLContext().uniform4fv(n3, n2, floatBuffer);
    }

    public void setMatrix(String string, float[] arrf) throws RuntimeException {
        int n = this.getUniform(string).location;
        if (n == -1) {
            return;
        }
        if (this.currentMatrix == null) {
            this.currentMatrix = new float[16];
        }
        if (!Arrays.equals(this.currentMatrix, arrf)) {
            this.context.getGLContext().uniformMatrix4fv(n, false, arrf);
            System.arraycopy(arrf, 0, this.currentMatrix, 0, arrf.length);
        }
    }

    @Override
    public void dispose() throws RuntimeException {
        if (this.programID != 0) {
            this.disposerRecord.dispose();
            this.programID = 0;
        }
        this.valid = false;
    }

    private static class ES2ShaderDisposerRecord
    implements Disposer.Record {
        private final ES2Context context;
        private int vertexShaderID;
        private int[] fragmentShaderID;
        private int programID;

        private ES2ShaderDisposerRecord(ES2Context eS2Context, int n, int[] arrn, int n2) {
            this.context = eS2Context;
            this.vertexShaderID = n;
            this.fragmentShaderID = arrn;
            this.programID = n2;
        }

        @Override
        public void dispose() {
            if (this.programID != 0) {
                this.context.getGLContext().disposeShaders(this.programID, this.vertexShaderID, this.fragmentShaderID);
                this.vertexShaderID = 0;
                this.programID = 0;
                this.fragmentShaderID = null;
            }
        }
    }

    private static class Uniform {
        private int location;
        private Object values;

        private Uniform() {
        }
    }
}

