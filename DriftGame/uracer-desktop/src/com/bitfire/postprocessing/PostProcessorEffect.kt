package com.bitfire.postprocessing

import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.utils.Disposable

/**
 * This interface defines the base class for the concrete implementation of post-processor effects. An effect is considered
 * enabled by default.
 */
abstract class PostProcessorEffect : Disposable {
    /**
     * Whether this effect is enabled and should be processed, or not.
     */
    var isEnabled = true

    /**
     * Concrete objects shall be responsible to recreate or rebind their own resources whenever they're needed, usually when the OpenGL
     * context is lost. E.g., framebuffer textures should be updated and shader parameters should be reuploaded/rebound.
     */
    abstract fun rebind()

    /**
     * Concrete objects shall implement their own rendering, given the source and destination buffers.
     */
    abstract fun render(src: FrameBuffer, dest: FrameBuffer?)

    /**
     * This is a convenience method to pass the call to the PostProcessor object while still being a non-publicly accessible method.
     */
    protected fun restoreViewport(dest: FrameBuffer?) {
        PostProcessor.restoreViewport(dest)
    }
}
