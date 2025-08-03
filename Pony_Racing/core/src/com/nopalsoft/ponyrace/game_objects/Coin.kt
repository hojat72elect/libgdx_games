package com.nopalsoft.ponyrace.game_objects

import com.esotericsoftware.spine.Skeleton
import com.nopalsoft.ponyrace.game.TileMapHandler

class Coin(x: Float, y: Float, tileMapHandler: TileMapHandler) : BaseGameObject(x, y) {
    @JvmField
    var lastStatetime: Float

    @JvmField
    var stateTime: Float

    @JvmField
    var state: State?

    @JvmField
    var coinSkeleton: Skeleton?

    init {
        stateTime = tileMapHandler.random.nextFloat() * 5f
        lastStatetime = stateTime
        state = State.IDLE
        coinSkeleton = Skeleton(tileMapHandler.game.assetsHandler!!.skeletonMonedaData)
        TIEMPO_TOMADA = tileMapHandler.game.assetsHandler!!.monedaTomadaAnim!!.duration
    }

    fun update(delta: Float) {
        lastStatetime = stateTime
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
        var TIEMPO_TOMADA: Float = 0F
    }
}
