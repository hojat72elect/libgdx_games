package com.bitfire.uracer.game.logic.gametasks.trackeffects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.bitfire.uracer.game.logic.gametasks.PlayerClient

abstract class TrackEffect(@JvmField val type: TrackEffectType) : PlayerClient(), Disposable {

    @JvmField
    var isPaused = false
    abstract fun tick()
    abstract fun reset()

    /**
     * The used GameRenderer instance is being passed for utilities, such as querying visibility.
     */
    abstract fun render(batch: SpriteBatch)

    abstract val particleCount: Int
    abstract val maxParticleCount: Int

    fun gamePause() {
        isPaused = true
    }

    fun gameResume() {
        isPaused = false
    }
}
