package com.bitfire.uracer.screen

import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.utils.Disposable
import com.bitfire.uracer.screen.ScreenFactory.ScreenId

abstract class ScreenTransition(private val screenFactory: ScreenFactory) : Disposable {

    protected fun createScreen(screenId: ScreenId) = screenFactory.createScreen(screenId)

    fun pause() {}

    open fun resume() {}

    /**
     * Called before the transition is started.
     */
    abstract fun frameBuffersReady(current: Screen, from: FrameBuffer, next: ScreenId, to: FrameBuffer)

    /**
     * Called when the transition is finished.
     */
    abstract fun nextScreen(): Screen

    abstract fun setDuration(durationMs: Long)

    abstract val isComplete: Boolean

    abstract fun reset()

    abstract override fun dispose()

    abstract fun update()

    abstract fun render()
}
