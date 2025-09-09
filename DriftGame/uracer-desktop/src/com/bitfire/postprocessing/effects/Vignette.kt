package com.bitfire.postprocessing.effects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.bitfire.postprocessing.PostProcessorEffect
import com.bitfire.postprocessing.filters.Vignetting

/**
 * Vignette is a very common post-processing technique you can apply to an image for
 * darkening or de-saturating its edges; so the viewer's focus will be drawn to the center.
 */
class Vignette(viewportWidth: Int, viewportHeight: Int, controlSaturation: Boolean) : PostProcessorEffect() {

    private val vignetting = Vignetting(controlSaturation)
    private val oneOnW = 1F / viewportWidth
    private val oneOnH = 1F / viewportHeight

    override fun dispose() {
        vignetting.dispose()
    }

    fun setCoords(x: Float, y: Float) {
        vignetting.setCoords(x, y)
    }

    fun setLutTexture(texture: Texture) {
        vignetting.setLut(texture)
    }

    fun setLutIndexVal(index: Int, value: Int) {
        vignetting.setLutIndexVal(index, value)
    }

    fun setLutIndexOffset(value: Float) {
        vignetting.setLutIndexOffset(value)
    }

    /**
     * Specify the center, in screen coordinates.
     */
    fun setCenter(x: Float, y: Float) {
        vignetting.setCenter(x * oneOnW, 1F - (y * oneOnH))
    }

    fun setIntensity(intensity: Float) {
        vignetting.intensity = intensity
    }

    fun setLutIntensity(value: Float) {
        vignetting.setLutIntensity(value)
    }

    override fun rebind() {
        vignetting.rebind()
    }

    override fun render(src: FrameBuffer, dest: FrameBuffer) {
        restoreViewport(dest)
        vignetting.setInput(src).setOutput(dest).render()
    }
}
