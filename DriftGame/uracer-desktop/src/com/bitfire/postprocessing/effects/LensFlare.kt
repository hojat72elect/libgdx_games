package com.bitfire.postprocessing.effects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.bitfire.postprocessing.PostProcessor
import com.bitfire.postprocessing.PostProcessorEffect
import com.bitfire.postprocessing.filters.Bias
import com.bitfire.postprocessing.filters.Blur
import com.bitfire.postprocessing.filters.Combine
import com.bitfire.postprocessing.filters.Lens

/**
 * Pseudo lens flare implementation. This is a post-processing effect entirely, no need for light positions or anything. It
 * includes ghost generation, halos, chromatic distortion and blur.
 */
class LensFlare(fboWidth: Int, fboHeight: Int) : PostProcessorEffect() {

    private val pingPongBuffer = PostProcessor.newPingPongBuffer(fboWidth, fboHeight, PostProcessor.framebufferFormat, false)
    private val lens = Lens(fboWidth, fboHeight)
    private val blur = Blur(fboWidth, fboHeight)
    private val bias = Bias()
    private val combine = Combine()

    init {
        setSettings(Settings("default", 2, -0.9F, 1F, 1F, 0.7F, 1F, 8, 0.5F))
    }

    override fun dispose() {
        combine.dispose()
        bias.dispose()
        blur.dispose()
        pingPongBuffer.dispose()
    }

    fun setBaseIntensity(intensity: Float) {
        combine.setSource1Intensity(intensity)
    }

    fun setFlareIntensity(intensity: Float) {
        combine.setSource2Intensity(intensity)
    }

    fun setHaloWidth(haloWidth: Float) {
        lens.setHaloWidth(haloWidth)
    }

    fun setLensColorTexture(tex: Texture) {
        lens.setLensColorTexture(tex)
    }

    fun setBias(b: Float) {
        bias.setBias(b)
    }

    fun setBaseSaturation(saturation: Float) {
        combine.setSource1Saturation(saturation)
    }

    fun setFlareSaturation(saturation: Float) {
        combine.setSource2Saturation(saturation)
    }

    fun setGhosts(ghosts: Int) {
        lens.setGhosts(ghosts)
    }

    fun setBlurType(type: Blur.BlurType) {
        blur.setType(type)
    }

    fun setSettings(settings: Settings) {

        // setup threshold filter
        setBias(settings.flareBias)

        // setup combine filter
        setBaseIntensity(settings.baseIntensity)
        setBaseSaturation(settings.baseSaturation)
        setFlareIntensity(settings.flareIntensity)
        setFlareSaturation(settings.flareSaturation)

        // setup blur filter
        setBlurPasses(settings.blurPasses)
        setBlurAmount(settings.blurAmount)
        setBlurType(settings.blurType)

        setGhosts(settings.ghosts)
    }

    fun setBlurPasses(passes: Int) {
        blur.passes = passes
    }

    fun setBlurAmount(amount: Float) {
        blur.setAmount(amount)
    }

    override fun render(src: FrameBuffer, dest: FrameBuffer?) {
        val texsrc = src.colorBufferTexture
        val blendingWasEnabled = PostProcessor.isStateEnabled(GL20.GL_BLEND)
        Gdx.gl.glDisable(GL20.GL_BLEND)

        pingPongBuffer.begin()

        // apply bias
        bias.setInput(texsrc).setOutput(pingPongBuffer.sourceBuffer).render()
        lens.setInput(pingPongBuffer.sourceBuffer).setOutput(pingPongBuffer.resultBuffer).render()
        pingPongBuffer.set(pingPongBuffer.resultBuffer, pingPongBuffer.sourceBuffer)

        // blur pass
        blur.render(pingPongBuffer)

        pingPongBuffer.end()

        if (blendingWasEnabled) Gdx.gl.glEnable(GL20.GL_BLEND)
        restoreViewport(dest)

        // mix original scene and blurred threshold, modulate via
        combine.setOutput(dest).setInput(texsrc, pingPongBuffer.resultTexture).render()
    }

    override fun rebind() {
        blur.rebind()
        bias.rebind()
        combine.rebind()
        pingPongBuffer.rebind()
    }

    class Settings(val name: String, val blurType: Blur.BlurType, val blurPasses: Int, val blurAmount: Float, val flareBias: Float, val baseIntensity: Float, val baseSaturation: Float, val flareIntensity: Float, val flareSaturation: Float, val ghosts: Int, val haloWidth: Float) {
        // simple blur
        constructor(name: String, blurPasses: Int, flareBias: Float, baseIntensity: Float, baseSaturation: Float, flareIntensity: Float, flareSaturation: Float, ghosts: Int, haloWidth: Float) : this(
            name, Blur.BlurType.Gaussian5x5b, blurPasses, 0F, flareBias, baseIntensity, baseSaturation, flareIntensity, flareSaturation, ghosts, haloWidth
        )
    }
}
