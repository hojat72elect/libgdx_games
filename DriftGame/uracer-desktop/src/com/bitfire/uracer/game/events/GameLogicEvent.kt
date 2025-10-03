package com.bitfire.uracer.game.events

import com.bitfire.uracer.game.player.PlayerCar

class GameLogicEvent : Event<GameLogicEvent.Type, GameLogicEvent.Order>(Type::class.java, Order::class.java) {

    @JvmField
    var player: PlayerCar? = null

    /**
     * defines the type of render queue.
     */
    enum class Type {
        PlayerAdded,
        PlayerRemoved,
        GameRestart,
        GameReset,
        GameQuit
    }

    /**
     * defines the position in the render queue specified by the Type parameter.
     */
    enum class Order {
        MINUS_4, MINUS_3, MINUS_2, MINUS_1, DEFAULT, PLUS_1, PLUS_2, PLUS_3, PLUS_4
    }

   fun interface Listener : Event.Listener<Type, Order> {
        override fun handle(source: Any, type: Type, order: Order)
    }
}
