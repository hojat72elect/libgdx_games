package com.nopalsoft.ponyrace.game_objects

import com.esotericsoftware.spine.Skeleton
import com.nopalsoft.ponyrace.game.TileMapHandler

class Candy(x: Float, y: Float, oWorld: TileMapHandler) : BaseGameObject(x, y) {
    @JvmField
    var lastStateTime: Float

    @JvmField
    var stateTime: Float

    @JvmField
    var state: State?

    @JvmField
    var skeleton: Skeleton?

    init {
        stateTime = oWorld.random.nextFloat() * 5f
        lastStateTime = stateTime
        state = State.NORMAL
        skeleton = Skeleton(oWorld.game.assetsHandler!!.dulceSkeletonData)
        PICK_UP_DURATION = oWorld.game.assetsHandler!!.dulceTomadaAnim!!.duration
    }

    fun update(delta: Float) {
        lastStateTime = stateTime
        stateTime += delta
    }

    fun hitPony() {
        state = State.ACTIVE
        stateTime = 0f
    }

    enum class State {
        NORMAL,
        ACTIVE
    }

    companion object {
        @JvmField
        var PICK_UP_DURATION: Float = 0F // if you don't pick them up before this time ends, they will be deleted
    }
}
