package com.nopalsoft.ponyrace.game_objects

import com.esotericsoftware.spine.Skeleton
import com.nopalsoft.ponyrace.game.TileMapHandler

class Balloons(x: Float, y: Float, tileMapHandler: TileMapHandler) : BaseGameObject(x, y) {
    @JvmField
    var lastStateTime: Float

    @JvmField
    var stateTime: Float

    @JvmField
    var state: State?

    @JvmField
    var balloonsSkeleton: Skeleton?

    init {
        stateTime = tileMapHandler.random.nextFloat() * 5f
        lastStateTime = stateTime
        state = State.IDLE
        balloonsSkeleton = Skeleton(tileMapHandler.game.assetsHandler.globoSkeletonData)
        TAKEN_ANIMATION_DURATION = tileMapHandler.game.assetsHandler.globoTomadaAnim.duration
    }

    fun update(delta: Float) {
        lastStateTime = stateTime
        stateTime += delta
    }

    fun hitPony() {
        state = State.TAKEN
        stateTime = 0f
    }

    enum class State {
        IDLE,
        TAKEN
    }

    companion object {
        @JvmField
        var TAKEN_ANIMATION_DURATION: Float = 0F
    }
}
