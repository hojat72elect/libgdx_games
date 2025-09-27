package com.bitfire.postprocessing.effects

import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2
import com.bitfire.postprocessing.PostProcessorEffect
import com.bitfire.postprocessing.filters.RadialBlur
import com.bitfire.postprocessing.filters.Zoom

/**
 * Implements a zooming effect: either a radial blur filter or a zoom filter is used.
 */
class Zoomer(viewportWidth: Int, viewportHeight: Int, quality: RadialBlur.Quality) : PostProcessorEffect() {

    private var doRadial = false
    private var radialBlur: RadialBlur? = null
    private var zoom: Zoom? = null
    private var oneOnW = 0F
    private var oneOnH = 0F

    /**
     * Creating a Zoomer specifying the radial blur quality will enable radial blur
     */
    init {
        setup(viewportWidth, viewportHeight, RadialBlur(quality))
    }

    private fun setup(viewportWidth: Int, viewportHeight: Int, radialBlurFilter: RadialBlur?) {
        radialBlur = radialBlurFilter
        if (radialBlur != null) {
            doRadial = true
            zoom = null
        } else {
            doRadial = false
            zoom = Zoom()
        }

        oneOnW = 1F / viewportWidth
        oneOnH = 1F / viewportHeight
    }

    /**
     * Specify the zoom origin, in screen coordinates.
     */
    fun setOrigin(o: Vector2) {
        setOrigin(o.x, o.y)
    }

    /**
     * Specify the zoom origin, in screen coordinates.
     */
    fun setOrigin(x: Float, y: Float) {
        if (doRadial) {
            radialBlur!!.setOrigin(x * oneOnW, 1f - y * oneOnH)
        } else {
            zoom!!.setOrigin(x * oneOnW, 1f - y * oneOnH)
        }
    }

    fun setZoom(zoom: Float) {
        if (doRadial)
            radialBlur!!.setZoom(1F / zoom)
        else
            this.zoom!!.setZoom(1F / zoom)
    }

    fun setBlurStrength(strength: Float) {
        if (doRadial) radialBlur!!.setStrength(strength)
    }

    override fun dispose() {
        if (radialBlur != null) {
            radialBlur!!.dispose()
            radialBlur = null
        }

        if (zoom != null) {
            zoom!!.dispose()
            zoom = null
        }
    }

    override fun rebind() {
        radialBlur!!.rebind()
    }

    override fun render(src: FrameBuffer, dest: FrameBuffer?) {
        restoreViewport(dest)
        if (doRadial)
            radialBlur!!.setInput(src).setOutput(dest).render()
        else
            zoom!!.setInput(src).setOutput(dest).render()
    }
}
