package dev.lonami.klooni.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import dev.lonami.klooni.Klooni
import kotlin.math.min

class TransitionScreen(
    private val game: Klooni, // From, to, and game to change the screen after the transition finishes
    private val fromScreen: Screen, private val toScreen: Screen, // Should the previous screen be disposed afterwards? Not desirable
    // if it was stored somewhere else, for example, to return to it later
    private val disposeAfter: Boolean
) : Screen {
    private val spriteBatch = SpriteBatch()

    // Rendering
    private var frameBuffer: FrameBuffer? = null
    private var bufferTexture: TextureRegion? = null
    private var fadedElapsed = 0f
    private var fadingOut = false
    private var width = 0
    private var height = 0

    override fun show() {
        fadedElapsed = 0f
        fadingOut = true
    }

    override fun render(delta: Float) {
        // Black background since we're fading to black
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Render on another buffer so then we can set its opacity. This
        // second buffer also would allow us to do more stuff, since then
        // we can use a texture, which we could move across the screen.
        frameBuffer!!.begin()

        val opacity: Float
        if (fadingOut) {
            fromScreen.render(delta)
            opacity = 1 - min(fadedElapsed * FADE_INVERSE_DELAY, 1f)
            if (opacity == 0f) {
                fadedElapsed = 0f
                fadingOut = false
            }
        } else {
            toScreen.render(delta)
            opacity = min(fadedElapsed * FADE_INVERSE_DELAY, 1f)
        }

        frameBuffer!!.end()

        // Render the faded texture
        spriteBatch.begin()
        spriteBatch.setColor(1f, 1f, 1f, opacity)
        spriteBatch.draw(bufferTexture, 0f, 0f, width.toFloat(), height.toFloat())
        spriteBatch.end()
        fadedElapsed += delta

        // We might have finished fading if the opacity is full
        if (opacity == 1f && !fadingOut) {
            game.setScreen(toScreen)
            dispose()
        }
    }

    override fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
        if (frameBuffer != null) frameBuffer!!.dispose()

        frameBuffer = FrameBuffer(Pixmap.Format.RGB565, width, height, false)
        bufferTexture = TextureRegion(frameBuffer!!.colorBufferTexture)
        bufferTexture!!.flip(false, true)
    }

    override fun dispose() {
        frameBuffer!!.dispose()
        if (disposeAfter) fromScreen.dispose()
    }

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    companion object {
        // Time it takes to fade out and in, 0.15s (0.3s total)
        private const val FADE_INVERSE_DELAY = 1f / 0.15f
    }
}
