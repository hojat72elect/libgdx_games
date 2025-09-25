package com.bitfire.uracer.game.tween

import aurelienribon.tweenengine.Timeline
import aurelienribon.tweenengine.TweenManager
import com.bitfire.uracer.URacer

/**
 * This tweener will update taking the timeMultiplier modulation into account
 */
object GameTweener {
    private val manager = TweenManager()

    @JvmStatic
    fun dispose() {
        clear()
    }

    fun clear() {
        manager.killAll()
    }

    @JvmStatic
    fun start(timeline: Timeline) {
        timeline.start(manager)
    }

    @JvmStatic
    fun stop(target: Any) {
        manager.killTarget(target)
    }

    @JvmStatic
    fun update() {
        manager.update(URacer.Game.getLastDeltaMs() * URacer.timeMultiplier)
    }
}
