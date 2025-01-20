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

/**
 * This class handles the transitions between different screens of the game, with a fade-out and fade-in effect.
 *
 * @disposeAfter - Should the previous screen be disposed afterwards? Not desirable.
 * if it was stored somewhere else, for example, to return to it later.
 */
class TransitionScreen(val game: Klooni, private val fromScreen: Screen, private val toScreen: Screen, private val disposeAfter: Boolean) : Screen {

    private val spriteBatch = SpriteBatch()

    // Rendering
    private var frameBuffer: FrameBuffer? = null
    private var bufferTexture: TextureRegion? = null
    private var fadedElapsed = 0f
    private var fadingOut = false

    private var width = 0F
    private var height = 0F

    override fun show() {
        fadedElapsed = 0F
        fadingOut = true
    }

    override fun render(delta: Float) {
        // Black background since we're fading to black
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Render on another buffer so then we can set its opacity. This
        // second buffer also would allow us to do more stuff, since then
        // we can use a texture, which we could move across the screen.
        frameBuffer?.begin()

        val opacity: Float
        if (fadingOut) {
            fromScreen.render(delta)
            opacity = 1F - min(fadedElapsed * FADE_INVERSE_DELAY, 1F)
            if (opacity == 0f) {
                fadedElapsed = 0f
                fadingOut = false
            }
        } else {
            toScreen.render(delta)
            opacity = min(fadedElapsed * FADE_INVERSE_DELAY, 1F)
        }

        frameBuffer?.end()

        // Render the faded texture
        spriteBatch.begin()
        spriteBatch.setColor(1f, 1f, 1f, opacity)
        spriteBatch.draw(bufferTexture, 0f, 0f, width, height)
        spriteBatch.end()
        fadedElapsed += delta


        // We might have finished fading if the opacity is full
        if (opacity == 1f && !fadingOut) {
            game.setScreen(toScreen)
            dispose()
        }
    }

    override fun resize(width: Int, height: Int) {
        this.width = width.toFloat()
        this.height = height.toFloat()
        frameBuffer?.dispose()

        frameBuffer = FrameBuffer(Pixmap.Format.RGB565, width, height, false)
        bufferTexture = TextureRegion(frameBuffer?.colorBufferTexture)
        bufferTexture?.flip(false, true)

    }

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        frameBuffer?.dispose()
        if (disposeAfter) fromScreen.dispose()
    }

    companion object {
        // Time it takes to fade out and in, 0.15s (0.3s total)
        private const val FADE_INVERSE_DELAY = 1 / 0.15F
    }
}