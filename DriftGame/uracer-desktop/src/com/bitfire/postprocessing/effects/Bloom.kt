package com.bitfire.postprocessing.effects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.bitfire.postprocessing.PostProcessor
import com.bitfire.postprocessing.PostProcessorEffect
import com.bitfire.postprocessing.filters.Blur
import com.bitfire.postprocessing.filters.Combine
import com.bitfire.postprocessing.filters.Threshold

class Bloom(fboWidth: Int, fboHeight: Int) : PostProcessorEffect() {

    private val pingPongBuffer = PostProcessor.newPingPongBuffer(fboWidth, fboHeight, PostProcessor.framebufferFormat, false)
    private val blur = Blur(fboWidth, fboHeight)
    private val threshold = Threshold()
    private val combine = Combine()

    init {
        setSettings(Settings("default", 2, 0.277F, 1F, .85F, 1.1F, .85F))
    }

    override fun dispose() {
        combine.dispose()
        threshold.dispose()
        blur.dispose()
        pingPongBuffer.dispose()
    }

    fun setBaseIntensity(intensity: Float) {
        combine.setSource1Intensity(intensity)
    }

    fun setBloomIntensity(intensity: Float) {
        combine.setSource2Intensity(intensity)
    }

    fun setThreshold(gamma: Float) {
        threshold.setTreshold(gamma)
    }

    fun setBaseSaturation(saturation: Float) {
        combine.setSource1Saturation(saturation)
    }

    fun setBloomSaturation(saturation: Float) {
        combine.setSource2Saturation(saturation)
    }

    fun setBlurType(type: Blur.BlurType) {
        blur.setType(type)
    }

    fun setSettings(settings: Settings) {

        // The threshold filter
        setThreshold(settings.bloomThreshold)

        // The combine filter
        setBaseIntensity(settings.baseIntensity)
        setBaseSaturation(settings.baseSaturation)
        setBloomIntensity(settings.bloomIntensity)
        setBloomSaturation(settings.bloomSaturation)

        // The blur filter
        setBlurPasses(settings.blurPasses)
        setBlurAmount(settings.blurAmount)
        setBlurType(settings.blurType)
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

        // The threshold filter (Only areas with pixels >= threshold are blit to smaller fbo).
        threshold.setInput(texsrc).setOutput(pingPongBuffer.sourceBuffer).render()

        blur.render(pingPongBuffer)
        pingPongBuffer.end()

        if (blendingWasEnabled) Gdx.gl.glEnable(GL20.GL_BLEND)
        restoreViewport(dest)

        // mix original scene and blurred threshold, modulate via set(Base|Bloom)(Saturation|Intensity)
        combine.setOutput(dest).setInput(texsrc, pingPongBuffer.resultTexture).render()
    }

    override fun rebind() {
        blur.rebind()
        threshold.rebind()
        combine.rebind()
        pingPongBuffer.rebind()
    }

    class Settings(
        val name: String, val blurType: Blur.BlurType, val blurPasses: Int, val blurAmount: Float, val bloomThreshold: Float, val baseIntensity: Float, val baseSaturation: Float, val bloomIntensity: Float, val bloomSaturation: Float
    ) {
        constructor(name: String, blurPasses: Int, bloomThreshold: Float, baseIntensity: Float, baseSaturation: Float, bloomIntensity: Float, bloomSaturation: Float) : this(
            name, Blur.BlurType.Gaussian5x5b, blurPasses, 0f, bloomThreshold, baseIntensity, baseSaturation, bloomIntensity, bloomSaturation
        )
    }
}
