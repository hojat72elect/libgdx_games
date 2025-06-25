

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Framebuffer {

    public static final int COLOR = GL20.GL_COLOR_ATTACHMENT0;
    public static final int DEPTH = GL20.GL_DEPTH_ATTACHMENT;
    public static final int STENCIL = GL20.GL_STENCIL_ATTACHMENT;

    public static final Framebuffer system = new Framebuffer(0);

    private int id;

    public Framebuffer() {
        id = Gdx.gl.glGenBuffer();
    }

    private Framebuffer(int n) {

    }

    public void bind() {
        Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, id);
    }

    public void delete() {
        Gdx.gl.glDeleteBuffer(id);
    }

    public void attach(int point, Texture tex) {
        bind();
        Gdx.gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, point, GL20.GL_TEXTURE_2D, tex.id, 0);
    }

    public void attach(int point, Renderbuffer buffer) {
        bind();
        Gdx.gl.glFramebufferRenderbuffer(GL20.GL_RENDERBUFFER, point, GL20.GL_TEXTURE_2D, buffer.id());
    }

    public boolean status() {
        bind();
        return Gdx.gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER) == GL20.GL_FRAMEBUFFER_COMPLETE;
    }
}
