package com.bitfire.uracer.game.events;

import com.bitfire.uracer.game.player.PlayerCar;

public class GameLogicEvent extends Event<GameLogicEvent.Type, GameLogicEvent.Order> {
    public PlayerCar player;

    public GameLogicEvent() {
        super(Type.class, Order.class);
    }

    /**
     * defines the type of render queue
     */
    public enum Type {
        PlayerAdded,
        PlayerRemoved,
        GameRestart,
        GameReset,
        GameQuit
    }

    /**
     * defines the position in the render queue specified by the Type parameter
     */
    public enum Order {
        MINUS_4, MINUS_3, MINUS_2, MINUS_1, DEFAULT, PLUS_1, PLUS_2, PLUS_3, PLUS_4
    }

    public interface Listener extends Event.Listener<Type, Order> {
        @Override
        void handle(Object source, Type type, Order order);
    }
}
