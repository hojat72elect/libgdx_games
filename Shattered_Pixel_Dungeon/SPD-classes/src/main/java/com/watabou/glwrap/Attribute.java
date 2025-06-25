

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

public class Attribute {

    private final int location;

    public Attribute(int location) {
        this.location = location;
    }

    public int location() {
        return location;
    }

    public void enable() {
        Gdx.gl.glEnableVertexAttribArray(location);
    }

    public void disable() {
        Gdx.gl.glDisableVertexAttribArray(location);
    }

    public void vertexPointer(int size, int stride, FloatBuffer ptr) {
        Gdx.gl.glVertexAttribPointer(location, size, GL20.GL_FLOAT, false, stride * 4, ptr);
    }

    public void vertexBuffer(int size, int stride, int offset) {
        Gdx.gl.glVertexAttribPointer(location, size, GL20.GL_FLOAT, false, stride * 4, offset * 4);
    }
}
