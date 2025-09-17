package com.bitfire.postprocessing

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.Disposable

/**
 * Provides a simple mechanism to query OpenGL pipeline states. Note: state queries are costly and stall the pipeline, especially
 * on mobile devices!
 */
class PipelineState() : Disposable {

    private val byteBuffer = BufferUtils.newByteBuffer(32)

    fun isEnabled(pname: Int): Boolean {
        val ret: Boolean

        if (pname == GL20.GL_BLEND) {
            Gdx.gl20.glGetBooleanv(GL20.GL_BLEND, byteBuffer)
            ret = (byteBuffer.get().toInt() == 1)
            byteBuffer.clear()
        } else {
            ret = false
        }

        return ret
    }

    override fun dispose() {}
}
