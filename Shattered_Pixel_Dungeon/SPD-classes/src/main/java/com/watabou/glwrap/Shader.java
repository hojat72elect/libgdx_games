

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.IntBuffer;

public class Shader {

    public static final int VERTEX = GL20.GL_VERTEX_SHADER;
    public static final int FRAGMENT = GL20.GL_FRAGMENT_SHADER;

    private final int handle;

    public Shader(int type) {
        handle = Gdx.gl.glCreateShader(type);
    }

    public int handle() {
        return handle;
    }

    public void source(String src) {
        Gdx.gl.glShaderSource(handle, src);
    }

    public void compile() {
        Gdx.gl.glCompileShader(handle);

        IntBuffer status = BufferUtils.newIntBuffer(1);
        Gdx.gl.glGetShaderiv(handle, GL20.GL_COMPILE_STATUS, status);
        if (status.get() == GL20.GL_FALSE) {
            throw new Error(Gdx.gl.glGetShaderInfoLog(handle));
        }
    }

    public void delete() {
        Gdx.gl.glDeleteShader(handle);
    }

    public static Shader createCompiled(int type, String src) {
        Shader shader = new Shader(type);
        shader.source(src);
        shader.compile();
        return shader;
    }
}
