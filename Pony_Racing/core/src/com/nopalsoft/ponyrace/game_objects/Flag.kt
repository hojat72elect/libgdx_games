package com.nopalsoft.ponyrace.game_objects

import com.nopalsoft.ponyrace.game.TileMapHandler
import java.util.Random

class Flag(world: TileMapHandler, @JvmField var actionType: ActionType?) {

    @JvmField
    var state: State? = State.NORMAL
    var random: Random = world.random

    fun permitirSalto(): Boolean {
        if (state == State.NORMAL && random.nextBoolean()) {
            state = State.ACTIVE
            return true
        }
        return false
    }

    enum class State {
        NORMAL,
        ACTIVE
    }

    enum class ActionType {
        JUMP_LEFT,
        JUMP_RIGHT,
        JUMP
    }
}
