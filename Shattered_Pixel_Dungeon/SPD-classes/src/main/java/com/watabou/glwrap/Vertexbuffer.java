

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Vertexbuffer {

    private final int id;
    private FloatBuffer vertices;
    private int updateStart, updateEnd;

    private static final ArrayList<Vertexbuffer> buffers = new ArrayList<>();

    public Vertexbuffer(FloatBuffer vertices) {
        synchronized (buffers) {
            id = Gdx.gl.glGenBuffer();

            this.vertices = vertices;
            buffers.add(this);

            updateStart = 0;
            updateEnd = vertices.limit();
        }
    }

    //For flagging the buffer for a full update without changing anything
    public void updateVertices() {
        updateVertices(vertices);
    }

    //For flagging an update with a full set of new data
    public void updateVertices(FloatBuffer vertices) {
        updateVertices(vertices, 0, vertices.limit());
    }

    //For flagging an update with a subset of data changed
    public void updateVertices(FloatBuffer vertices, int start, int end) {
        this.vertices = vertices;

        if (updateStart == -1)
            updateStart = start;
        else
            updateStart = Math.min(start, updateStart);

        if (updateEnd == -1)
            updateEnd = end;
        else
            updateEnd = Math.max(end, updateEnd);
    }

    public void updateGLData() {
        if (updateStart == -1) return;

        ((Buffer) vertices).position(updateStart);
        bind();

        if (updateStart == 0 && updateEnd == vertices.limit()) {
            Gdx.gl.glBufferData(GL20.GL_ARRAY_BUFFER, vertices.limit() * 4, vertices, GL20.GL_DYNAMIC_DRAW);
        } else {
            Gdx.gl.glBufferSubData(GL20.GL_ARRAY_BUFFER, updateStart * 4, (updateEnd - updateStart) * 4, vertices);
        }

        release();
        updateStart = updateEnd = -1;
    }

    public void bind() {
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, id);
    }

    public void release() {
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }

    public void delete() {
        synchronized (buffers) {
            Gdx.gl.glDeleteBuffer(id);
            buffers.remove(this);
        }
    }

    public static void clear() {
        synchronized (buffers) {
            for (Vertexbuffer buf : buffers.toArray(new Vertexbuffer[0])) {
                buf.delete();
            }
        }
    }

    public static void reload() {
        synchronized (buffers) {
            for (Vertexbuffer buf : buffers) {
                buf.updateVertices();
                buf.updateGLData();
            }
        }
    }
}
