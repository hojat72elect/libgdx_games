package com.bitfire.uracer.game.debug

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.bitfire.uracer.game.debug.DebugHelper.RenderFlags
import com.bitfire.uracer.game.logic.gametasks.PlayerClient

abstract class DebugRenderable(val flag: RenderFlags) : PlayerClient(), Disposable {
    abstract fun tick()

    fun reset() {}

    open fun render() {}

    open fun renderBatch(batch: SpriteBatch) {}
}
