package com.bitfire.postprocessing

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture.TextureWrap
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.bitfire.postprocessing.utils.PingPongBuffer
import com.bitfire.utils.ItemsManager

/**
 * Provides a way to capture the rendered scene to an off-screen buffer and to apply a chain of effects on it before rendering it to screen.
 * Effects can be added or removed via [addEffect].
 */
class PostProcessor(fboWidth: Int, fboHeight: Int, var useDepth: Boolean = false, useAlphaChannel: Boolean, use32Bits: Boolean, u: TextureWrap, v: TextureWrap) : Disposable {

    private val composite: PingPongBuffer
    private val effectsManager = ItemsManager<PostProcessorEffect>()
    private val clearColor: Color = Color.CLEAR

    // This is a per-frame updated list of enabled effects
    private val enabledEffects = Array<PostProcessorEffect>(5)
    private var compositeWrapU: TextureWrap? = null
    private var compositeWrapV: TextureWrap? = null
    private var clearBits = GL20.GL_COLOR_BUFFER_BIT
    private var clearDepth = 1F

    /**
     * Whether or not the post-processor is enabled
     */
    @JvmField
    var enabled = true
    private var capturing = false
    private var hasCaptured = false
    private var listener: PostProcessorListener? = null

    /**
     * Construct a new PostProcessor with the given parameters and viewport, defaulting to *TextureWrap.ClampToEdge* as
     * texture wrap mode
     */
    constructor(viewport: Rectangle, useDepth: Boolean, useAlphaChannel: Boolean, use32Bits: Boolean) : this(
        viewport.width.toInt(), viewport.height.toInt(), useDepth, useAlphaChannel, use32Bits, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge
    ) {
        setViewport(viewport)
    }

    /**
     * Construct a new PostProcessor with the given parameters and the specified texture wrap mode
     */
    init {
        framebufferFormat = if (use32Bits) {
            if (useAlphaChannel) {
                Pixmap.Format.RGBA8888
            } else {
                Pixmap.Format.RGB888
            }
        } else {
            if (useAlphaChannel) {
                Pixmap.Format.RGBA4444
            } else {
                Pixmap.Format.RGB565
            }
        }

        composite = newPingPongBuffer(fboWidth, fboHeight, framebufferFormat, useDepth)
        setBufferTextureWrap(u, v)
        pipelineState = PipelineState()

        if (useDepth)
            clearBits = clearBits or GL20.GL_DEPTH_BUFFER_BIT

        setViewport(null)
    }

    /**
     * Sets the viewport to be restored, if null is specified then the viewport will NOT be restored at all.
     *
     * The predefined effects will restore the viewport settings at the final blitting stage (render to screen) by invoking the
     * restoreViewport static method.
     */
    fun setViewport(viewport: Rectangle?) {
        hasViewport = (viewport != null)
        if (hasViewport) Companion.viewport.set(viewport)
    }

    /**
     * Frees owned resources.
     */
    override fun dispose() {
        effectsManager.dispose()

        // cleanup managed buffers, if any
        for (i in 0..<buffers.size) buffers.get(i)!!.dispose()

        buffers.clear()
        enabledEffects.clear()
        pipelineState?.dispose()
    }

    /**
     * Returns the number of the currently enabled effects
     */
    fun getEnabledEffectsCount() = enabledEffects.size

    /**
     * Adds the specified effect to the effect chain and transfer ownership to the PostProcessor, it will manage cleaning it up for
     * you. The order of the inserted effects IS important, since effects will be applied in a FIFO fashion, the first added is the
     * first being applied.
     */
    fun addEffect(effect: PostProcessorEffect) {
        effectsManager.add(effect)
    }

    /**
     * Sets the color that will be used to clear the buffer.
     */
    fun setClearColor(r: Float, g: Float, b: Float, a: Float) {
        clearColor.set(r, g, b, a)
    }

    /**
     * Sets the clear bit for when glClear is invoked.
     */
    fun setClearBits(bits: Int) {
        clearBits = bits
    }

    /**
     * Sets the depth value with which to clear the depth buffer when needed.
     */
    fun setClearDepth(depth: Float) {
        clearDepth = depth
    }

    fun setBufferTextureWrap(u: TextureWrap, v: TextureWrap) {
        compositeWrapU = u
        compositeWrapV = v
        composite.texture1.setWrap(compositeWrapU, compositeWrapV)
        composite.texture2.setWrap(compositeWrapU, compositeWrapV)
    }

    /**
     * Starts capturing the scene, clears the buffer with the clear color specified by
     * [.setClearColor].
     *
     * @return true or false, whether or not capturing has been initiated. Capturing will fail in case there are no enabled effects
     * in the chain or this instance is not enabled or capturing is already started.
     */
    fun capture(): Boolean {
        hasCaptured = false

        if (enabled && !capturing) {
            if (buildEnabledEffectsList() == 0) return false

            capturing = true
            composite.begin()
            composite.capture()

            if (useDepth) Gdx.gl.glClearDepthf(clearDepth)

            Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a)
            Gdx.gl.glClear(clearBits)
            return true
        }

        return false
    }

    /**
     * Starts capturing the scene as [.capture], but **without** clearing the screen.
     */
    fun captureNoClear() {
        hasCaptured = false

        if (enabled && !capturing) {
            if (buildEnabledEffectsList() == 0) return

            capturing = true
            composite.begin()
            composite.capture()
        }
    }

    /**
     * Stops capturing the scene and returns the result, or null if nothing was captured.
     */
    fun captureEnd(): FrameBuffer? {
        if (enabled && capturing) {
            capturing = false
            hasCaptured = true
            composite.end()
            return composite.resultBuffer
        }

        return null
    }

    /**
     * Regenerates and/or rebinds owned resources when needed, eg. when the OpenGL context is lost.
     */
    fun rebind() {
        composite.texture1.setWrap(compositeWrapU, compositeWrapV)
        composite.texture2.setWrap(compositeWrapU, compositeWrapV)

        for (i in 0..<buffers.size) buffers.get(i)?.rebind()
        for (e in effectsManager) e.rebind()
    }

    /**
     * Stops capturing the scene and apply the effect chain, if there is one. If the specified output framebuffer is NULL, then the
     * rendering will be performed to screen.
     */
    /**
     * Convenience method to render to screen.
     */
    @JvmOverloads
    fun render(dest: FrameBuffer? = null) {
        captureEnd()

        if (!hasCaptured) return
        val items = enabledEffects
        val count = items.size
        if (count > 0) {
            Gdx.gl.glDisable(GL20.GL_CULL_FACE)
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST)

            // render effects chain, [0,n-1]
            if (count > 1) {
                for (i in 0..<count - 1) {
                    val e = items.get(i)

                    composite.capture()
                    e.render(composite.sourceBuffer, composite.resultBuffer)
                }

                // complete
                composite.end()
            }

            if (dest == null) listener?.beforeRenderToScreen()

            // render with null dest (to screen)
            items.get(count - 1).render(composite.resultBuffer, dest)

            // ensure default texture unit #0 is active
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)
        } else
            Gdx.app.log("PostProcessor", "No post-processor effects enabled, aborting render")
    }

    private fun buildEnabledEffectsList(): Int {
        enabledEffects.clear()
        for (effect in effectsManager) {
            if (effect.isEnabled) enabledEffects.add(effect)
        }

        return enabledEffects.size
    }

    companion object {
        private val buffers = Array<PingPongBuffer?>(5)
        private val viewport = Rectangle()

        /**
         * Enable pipeline state queries: beware the pipeline can stall!
         */
        @JvmField
        var EnableQueryStates = false
        private var pipelineState: PipelineState? = null

        /**
         * Returns the internal framebuffer format, computed from the parameters specified during construction. NOTE: the returned
         * Format will be valid after construction and NOT early!
         */
        var framebufferFormat: Pixmap.Format? = null
            private set
        private var hasViewport = false

        /**
         * Creates and returns a managed PingPongBuffer buffer, just create and forget. If rebind() is called on context loss, managed
         * PingPongBuffers will be rebound for you.
         *
         * This is a drop-in replacement for the same-signature PingPongBuffer's constructor.
         */
        fun newPingPongBuffer(width: Int, height: Int, frameBufferFormat: Pixmap.Format?, hasDepth: Boolean): PingPongBuffer {
            val buffer = PingPongBuffer(width, height, frameBufferFormat, hasDepth)
            buffers.add(buffer)
            return buffer
        }

        /**
         * Provides a way to query the pipeline for the most used states
         */
        fun isStateEnabled(pname: Int): Boolean {
            if (EnableQueryStates)
                return pipelineState!!.isEnabled(pname)

            return false
        }

        /**
         * Restores the previously set viewport if one was specified earlier and the destination buffer is the screen
         */
        fun restoreViewport(dest: FrameBuffer?) {
            if (hasViewport && dest == null)
                Gdx.gl.glViewport(viewport.x.toInt(), viewport.y.toInt(), viewport.width.toInt(), viewport.height.toInt())
        }
    }
}
