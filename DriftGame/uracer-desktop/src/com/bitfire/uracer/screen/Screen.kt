package com.bitfire.uracer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.utils.Disposable

abstract class Screen : Disposable {

    abstract fun init(): Boolean

    open fun enable() {}

    fun disable() {
        Gdx.input.inputProcessor = null
    }

    abstract fun pause()

    abstract fun resume()

    abstract fun tick()

    abstract fun tickCompleted()

    abstract fun render(destinationBuffer: FrameBuffer?)
}
