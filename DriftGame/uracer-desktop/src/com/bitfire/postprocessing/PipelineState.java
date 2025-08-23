package com.bitfire.postprocessing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;

import java.nio.ByteBuffer;

/**
 * Provides a simple mechanism to query OpenGL pipeline states. Note: state queries are costly and stall the pipeline, especially
 * on mobile devices!
 */
public final class PipelineState implements Disposable {

    private final ByteBuffer byteBuffer;

    PipelineState() {
        byteBuffer = BufferUtils.newByteBuffer(32);
    }

    public boolean isEnabled(int pname) {
        boolean ret;

        if (pname == GL20.GL_BLEND) {
            Gdx.gl20.glGetBooleanv(GL20.GL_BLEND, byteBuffer);
            ret = (byteBuffer.get() == 1);
            byteBuffer.clear();
        } else {
            ret = false;
        }

        return ret;
    }

    @Override
    public void dispose() {
    }
}
