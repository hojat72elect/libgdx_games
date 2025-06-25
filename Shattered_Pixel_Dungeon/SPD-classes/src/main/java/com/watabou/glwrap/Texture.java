

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Texture {

    public static final int NEAREST = GL20.GL_NEAREST;
    public static final int LINEAR = GL20.GL_LINEAR;

    public static final int REPEAT = GL20.GL_REPEAT;
    public static final int MIRROR = GL20.GL_MIRRORED_REPEAT;
    public static final int CLAMP = GL20.GL_CLAMP_TO_EDGE;

    public int id = -1;
    private static int bound_id = 0; //id of the currently bound texture

    public boolean premultiplied = false;

    protected void generate() {
        id = Gdx.gl.glGenTexture();
    }

    public static void activate(int index) {
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0 + index);
    }

    public void bind() {
        if (id == -1) {
            generate();
        }
        if (id != bound_id) {
            Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, id);
            bound_id = id;
        }
    }

    public static void clear() {
        bound_id = 0;
    }

    public void filter(int minMode, int maxMode) {
        bind();
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, minMode);
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, maxMode);
    }

    public void wrap(int s, int t) {
        bind();
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, s);
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, t);
    }

    public void delete() {
        if (bound_id == id) bound_id = 0;
        Gdx.gl.glDeleteTexture(id);
    }

    public void bitmap(Pixmap pixmap) {
        bind();

        Gdx.gl.glTexImage2D(
                GL20.GL_TEXTURE_2D,
                0,
                pixmap.getGLInternalFormat(),
                pixmap.getWidth(),
                pixmap.getHeight(),
                0,
                pixmap.getGLFormat(),
                pixmap.getGLType(),
                pixmap.getPixels()
        );

        premultiplied = true;
    }

    public void pixels(int w, int h, int[] pixels) {

        bind();

        IntBuffer imageBuffer = ByteBuffer.
                allocateDirect(w * h * 4).
                order(ByteOrder.nativeOrder()).
                asIntBuffer();
        imageBuffer.put(pixels);
        ((Buffer) imageBuffer).position(0);

        Gdx.gl.glTexImage2D(
                GL20.GL_TEXTURE_2D,
                0,
                GL20.GL_RGBA,
                w,
                h,
                0,
                GL20.GL_RGBA,
                GL20.GL_UNSIGNED_BYTE,
                imageBuffer);
    }

    public void pixels(int w, int h, byte[] pixels) {

        bind();

        ByteBuffer imageBuffer = ByteBuffer.
                allocateDirect(w * h).
                order(ByteOrder.nativeOrder());
        imageBuffer.put(pixels);
        ((Buffer) imageBuffer).position(0);

        Gdx.gl.glPixelStorei(GL20.GL_UNPACK_ALIGNMENT, 1);

        Gdx.gl.glTexImage2D(
                GL20.GL_TEXTURE_2D,
                0,
                GL20.GL_ALPHA,
                w,
                h,
                0,
                GL20.GL_ALPHA,
                GL20.GL_UNSIGNED_BYTE,
                imageBuffer);
    }

    public static Texture create(Pixmap pix) {
        Texture tex = new Texture();
        tex.bitmap(pix);

        return tex;
    }

    public static Texture create(int width, int height, int[] pixels) {
        Texture tex = new Texture();
        tex.pixels(width, height, pixels);

        return tex;
    }

    public static Texture create(int width, int height, byte[] pixels) {
        Texture tex = new Texture();
        tex.pixels(width, height, pixels);

        return tex;
    }
}
