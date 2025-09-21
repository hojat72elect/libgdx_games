package com.bitfire.uracer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.FrameBuffer

/**
 * Implements useful Screen objects related utilities.
 */
object ScreenUtils {

    /**
     * Render the specified screen to the specified buffer.
     */
    @JvmOverloads
    @JvmStatic
    fun copyScreen(screen: Screen?, buffer: FrameBuffer, clearColor: Color = Color.BLACK, clearDepth: Float = 1F, useDepth: Boolean = false) {

        if (screen != null) {
            clear(buffer, clearColor, clearDepth, useDepth)

            // ensures default active texture is active
            Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0)

            screen.render(buffer)
        }
    }

    /**
     * Clear the specified buffer.
     */
    @JvmOverloads
    @JvmStatic
    fun clear(buffer: FrameBuffer, clearColor: Color, clearDepth: Float = 1F, useDepth: Boolean = false) {
        Gdx.gl20.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a)

        buffer.begin()

        if (useDepth) {
            Gdx.gl20.glClearDepthf(clearDepth)
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        } else {
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)
        }

        buffer.end()
    }
}
