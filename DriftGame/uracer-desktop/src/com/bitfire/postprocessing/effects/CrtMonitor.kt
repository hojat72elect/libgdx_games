package com.bitfire.postprocessing.effects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.bitfire.postprocessing.PostProcessor
import com.bitfire.postprocessing.PostProcessorEffect
import com.bitfire.postprocessing.filters.Blur
import com.bitfire.postprocessing.filters.Combine
import com.bitfire.postprocessing.filters.CrtScreen
import com.bitfire.postprocessing.filters.CrtScreen.RgbMode
import com.bitfire.postprocessing.utils.PingPongBuffer

class CrtMonitor(fboWidth: Int, fboHeight: Int, barrelDistortion: Boolean, private val doblur: Boolean, mode: RgbMode?, effectsSupport: Int) : PostProcessorEffect() {

    private var pingPongBuffer: PingPongBuffer? = null
    private var buffer: FrameBuffer? = null
    private val crt = CrtScreen(barrelDistortion, mode, effectsSupport)
    private var blur: Blur? = null
    val combinePass = Combine()

    // the effect is designed to work on the whole screen area, no small/mid size tricks!
    init {
        if (doblur) {
            pingPongBuffer = PostProcessor.newPingPongBuffer(fboWidth, fboHeight, PostProcessor.getFramebufferFormat(), false)
            blur = Blur(fboWidth, fboHeight)
            blur?.passes = 1
            blur?.setAmount(1f)
            blur?.type = Blur.BlurType.Gaussian3x3 // modern machines defocus
        } else {
            buffer = FrameBuffer(PostProcessor.getFramebufferFormat(), fboWidth, fboHeight, false)
        }
    }

    override fun dispose() {
        crt.dispose()
        combinePass.dispose()
        if (doblur) blur?.dispose()
        buffer?.dispose()
        pingPongBuffer?.dispose()
    }

    fun setTime(elapsedSecs: Float) {
        crt.setTime(elapsedSecs)
    }

    fun setColorOffset(offset: Float) {
        crt.setColorOffset(offset)
    }

    fun setChromaticDispersion(redCyan: Float, blueYellow: Float) {
        crt.setChromaticDispersion(redCyan, blueYellow)
    }

    fun setTint(r: Float, g: Float, b: Float) {
        crt.setTint(r, g, b)
    }

    fun setDistortion(distortion: Float) {
        crt.setDistortion(distortion)
    }

    val offset = crt.offset

    var zoom: Float
        get() = crt.zoom
        set(zoom) {
            crt.setZoom(zoom)
        }

    var tint: Color
        get() = crt.tint
        set(tint) {
            crt.setTint(tint)
        }

    val rgbMode = crt.rgbMode

    override fun rebind() {
        crt.rebind()
    }

    override fun render(src: FrameBuffer, dest: FrameBuffer?) {
        // the original scene
        val inputTexture = src.colorBufferTexture
        val blendingWasEnabled = PostProcessor.isStateEnabled(GL20.GL_BLEND)
        Gdx.gl.glDisable(GL20.GL_BLEND)

        val out: Texture

        if (doblur) {
            pingPongBuffer!!.begin()
            run {
                // crt pass
                crt.setInput(inputTexture).setOutput(pingPongBuffer!!.sourceBuffer).render()

                // blur pass
                blur!!.render(pingPongBuffer!!)
            }
            pingPongBuffer!!.end()
            out = pingPongBuffer!!.resultTexture
        } else {
            // crt pass
            crt.setInput(inputTexture).setOutput(buffer).render()
            out = buffer!!.colorBufferTexture
        }

        if (blendingWasEnabled) Gdx.gl.glEnable(GL20.GL_BLEND)
        restoreViewport(dest)

        // do combine pass
        combinePass.setOutput(dest).setInput(inputTexture, out).render()
    }
}
