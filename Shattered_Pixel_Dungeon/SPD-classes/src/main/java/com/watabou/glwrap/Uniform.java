

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;

public class Uniform {

    private final int location;

    public Uniform(int location) {
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

    public void value1f(float value) {
        Gdx.gl.glUniform1f(location, value);
    }

    public void value2f(float v1, float v2) {
        Gdx.gl.glUniform2f(location, v1, v2);
    }

    public void value4f(float v1, float v2, float v3, float v4) {
        Gdx.gl.glUniform4f(location, v1, v2, v3, v4);
    }

    public void valueM3(float[] value) {
        Gdx.gl.glUniformMatrix3fv(location, 1, false, value, 0);
    }

    public void valueM4(float[] value) {
        Gdx.gl.glUniformMatrix4fv(location, 1, false, value, 0);
    }
}