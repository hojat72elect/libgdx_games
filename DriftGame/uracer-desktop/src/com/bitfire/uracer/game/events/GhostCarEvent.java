package com.bitfire.uracer.game.events;

public final class GhostCarEvent extends Event<GhostCarEvent.Type, GhostCarEvent.Order> {
    public GhostCarEvent() {
        super(Type.class, Order.class);
    }

    public enum Type {
        onGhostFadingOut, ReplayStarted, ReplayEnded
    }

    public enum Order {
        Default
    }

    public interface Listener extends Event.Listener<Type, Order> {
        @Override
        void handle(Object source, Type type, Order order);
    }
}
