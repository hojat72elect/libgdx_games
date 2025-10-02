package com.bitfire.uracer.game.logic.gametasks.hud

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.bitfire.uracer.game.logic.gametasks.PlayerClient

abstract class HudElement : PlayerClient(), Disposable {

    fun onTick() {}
    open fun onRestart() {}
    open fun onReset() {}
    abstract fun onRender(batch: SpriteBatch, cameraZoom: Float)
}
