package com.bitfire.uracer.game.rendering

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.bitfire.uracer.utils.ScaleUtils

class GameBatchRenderer(private val gl: GL20) {

    private val batch = SpriteBatch()
    private var begin = false
    private val topLeftOrigin = Matrix4()
    private val identity  = Matrix4()

    init {
        topLeftOrigin.setToOrtho(0f, ScaleUtils.PlayWidth.toFloat(), ScaleUtils.PlayHeight.toFloat(), 0f, 0f, 10f)
    }

    fun dispose() {
        batch.dispose()
    }

    fun begin(proj: Matrix4, viewxform: Matrix4): SpriteBatch? {
        if (!begin) {
            begin = true
            gl.glActiveTexture(GL20.GL_TEXTURE0)
            batch.projectionMatrix = proj
            batch.transformMatrix = viewxform
            batch.begin()
            return batch
        }

        return null
    }

    fun begin(camera: Camera) = begin(camera.projection, camera.view)

    fun beginTopLeft() = begin(topLeftOrigin, identity)

    fun end() {
        if (begin) {
            batch.end()
            begin = false
        }
    }
}
