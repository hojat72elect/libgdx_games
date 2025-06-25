

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;
import com.watabou.noosa.Game;

import java.nio.IntBuffer;

public class Program {

    private final int handle;

    public Program() {
        handle = Gdx.gl.glCreateProgram();
    }

    public int handle() {
        return handle;
    }

    public void attach(Shader shader) {
        Gdx.gl.glAttachShader(handle, shader.handle());
    }

    public void link() {
        Gdx.gl.glLinkProgram(handle);

        IntBuffer status = BufferUtils.newIntBuffer(1);
        Gdx.gl.glGetProgramiv(handle, GL20.GL_LINK_STATUS, status);
        if (status.get() == GL20.GL_FALSE) {
            Game.reportException(new RuntimeException(Gdx.gl.glGetProgramInfoLog(handle)));
        }
    }

    public Attribute attribute(String name) {
        return new Attribute(Gdx.gl.glGetAttribLocation(handle, name));
    }

    public Uniform uniform(String name) {
        return new Uniform(Gdx.gl.glGetUniformLocation(handle, name));
    }

    public void use() {
        Gdx.gl.glUseProgram(handle);
    }

    public void delete() {
        Gdx.gl.glDeleteProgram(handle);
    }

    public static Program create(Shader... shaders) {
        Program program = new Program();
        for (int i = 0; i < shaders.length; i++) {
            program.attach(shaders[i]);
        }
        program.link();
        return program;
    }
}
