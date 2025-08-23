package com.bitfire.uracer.game.events;


public final class PlayerDriftStateEvent extends
        Event<PlayerDriftStateEvent.Type, PlayerDriftStateEvent.Order> {
    public PlayerDriftStateEvent() {
        super(Type.class, Order.class);
    }

    public enum Type {
        onBeginDrift, onEndDrift
    }

    public enum Order {
        Default
    }

    public interface Listener extends Event.Listener<Type, Order> {
        @Override
        void handle(Object source, Type type, Order order);
    }
}
