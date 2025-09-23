package com.bitfire.uracer.game.logic.types;

import com.bitfire.uracer.game.GameEvents;
import com.bitfire.uracer.game.actors.GhostCar;
import com.bitfire.uracer.game.events.*;
import com.bitfire.uracer.game.events.GameRendererEvent.Order;
import com.bitfire.uracer.game.events.GameRendererEvent.Type;
import com.bitfire.uracer.game.player.PlayerCar;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class EventHandlers {
    private final CommonLogic logic;
    private final PlayerLapCompletionMonitorEvent.Listener playerLapCompletionMonitorListener = new PlayerLapCompletionMonitorEvent.Listener() {
        @Override
        public void handle(Object source, PlayerLapCompletionMonitorEvent.Type type, PlayerLapCompletionMonitorEvent.Order order) {
            switch (type) {
                case onWarmUpStarted:
                    logic.warmUpStarted();
                    break;
                case onWarmUpCompleted:
                    logic.warmUpCompleted();
                    break;
                case onLapStarted:
                    logic.playerLapStarted();
                    break;
                case onLapCompleted:
                    logic.playerLapCompleted();
                    break;
            }
        }
    };

    private final GhostLapCompletionMonitorEvent.Listener ghostLapCompletionMonitorListener = new GhostLapCompletionMonitorEvent.Listener() {
        @Override
        public void handle(@NotNull Object source, @NotNull GhostLapCompletionMonitorEvent.Type type, @NotNull GhostLapCompletionMonitorEvent.Order order) {
            if (Objects.requireNonNull(type) == GhostLapCompletionMonitorEvent.Type.OnLapCompleted) {
                GhostCar ghost = (GhostCar) source;
                logic.ghostLapCompleted(ghost);
            }
        }
    };
    private final WrongWayMonitorEvent.Listener wrongWayMonitorListener = new WrongWayMonitorEvent.Listener() {
        @Override
        public void handle(Object source, WrongWayMonitorEvent.Type type, WrongWayMonitorEvent.Order order) {
            switch (type) {
                case onWrongWayBegins:
                    logic.wrongWayBegins();
                    break;
                case onWrongWayEnds:
                    logic.wrongWayEnds();
                    break;
            }
        }
    };
    private final GameRendererEvent.Listener rendererListener = new GameRendererEvent.Listener() {
        @Override
        public void handle(@NotNull Object source, @NotNull Type type, @NotNull Order order) {
            if (Objects.requireNonNull(type) == Type.BeforeRender) {
                logic.beforeRender();
            }
        }
    };
    private final PlayerDriftStateEvent.Listener driftStateListener = new PlayerDriftStateEvent.Listener() {
        @Override
        public void handle(@NotNull Object source, PlayerDriftStateEvent.Type type, @NotNull PlayerDriftStateEvent.Order order) {
            PlayerCar player = (PlayerCar) source;

            switch (type) {
                case OnBeginDrift:
                    logic.driftBegins(player);
                    break;
                case OnEndDrift:
                    logic.driftEnds(player);
                    break;
            }
        }
    };
    private final CarEvent.Listener playerCarListener = new CarEvent.Listener() {
        @Override
        public void handle(@NotNull Object source, CarEvent.Type type, @NotNull CarEvent.Order order) {
            CarEvent.Data eventData = GameEvents.playerCar.data;

            switch (type) {
                case OnCollision:
                    logic.collision(eventData);
                    break;
                case OnOutOfTrack:
                    logic.outOfTrack();
                    break;
                case OnBackInTrack:
                    logic.backInTrack();
                    break;
                case OnPhysicsForcesReady:
                    logic.physicsForcesReady(eventData);
                    break;
            }
        }
    };
    private final GhostCarEvent.Listener ghostListener = new GhostCarEvent.Listener() {
        @Override
        public void handle(@NotNull Object source, GhostCarEvent.Type type, @NotNull GhostCarEvent.Order order) {
            switch (type) {
                case OnGhostFadingOut:
                    logic.ghostFadingOut((GhostCar) source);
                    break;
                case ReplayStarted:
                    logic.ghostReplayStarted((GhostCar) source);
                    break;
                case ReplayEnded:
                    logic.ghostReplayEnded((GhostCar) source);
                    break;
            }
        }
    };

    public EventHandlers(CommonLogic logic) {
        this.logic = logic;
    }

    public void registerPlayerEvents() {
        GameEvents.driftState.addListener(driftStateListener, PlayerDriftStateEvent.Type.OnBeginDrift);
        GameEvents.driftState.addListener(driftStateListener, PlayerDriftStateEvent.Type.OnEndDrift);
        GameEvents.playerCar.addListener(playerCarListener, CarEvent.Type.OnCollision);
        GameEvents.playerCar.addListener(playerCarListener, CarEvent.Type.OnPhysicsForcesReady);
        GameEvents.playerCar.addListener(playerCarListener, CarEvent.Type.OnOutOfTrack);
        GameEvents.playerCar.addListener(playerCarListener, CarEvent.Type.OnBackInTrack);
    }

    public void unregisterPlayerEvents() {
        GameEvents.driftState.removeListener(driftStateListener, PlayerDriftStateEvent.Type.OnBeginDrift);
        GameEvents.driftState.removeListener(driftStateListener, PlayerDriftStateEvent.Type.OnEndDrift);
        GameEvents.playerCar.removeListener(playerCarListener, CarEvent.Type.OnCollision);
        GameEvents.playerCar.removeListener(playerCarListener, CarEvent.Type.OnPhysicsForcesReady);
        GameEvents.playerCar.removeListener(playerCarListener, CarEvent.Type.OnOutOfTrack);
        GameEvents.playerCar.removeListener(playerCarListener, CarEvent.Type.OnBackInTrack);
    }

    public void registerPlayerMonitorEvents() {
        GameEvents.lapCompletion.addListener(playerLapCompletionMonitorListener, PlayerLapCompletionMonitorEvent.Type.onWarmUpStarted, PlayerLapCompletionMonitorEvent.Order.MINUS_4);
        GameEvents.lapCompletion.addListener(playerLapCompletionMonitorListener, PlayerLapCompletionMonitorEvent.Type.onWarmUpCompleted, PlayerLapCompletionMonitorEvent.Order.MINUS_4);
        GameEvents.lapCompletion.addListener(playerLapCompletionMonitorListener, PlayerLapCompletionMonitorEvent.Type.onLapStarted, PlayerLapCompletionMonitorEvent.Order.MINUS_4);
        GameEvents.lapCompletion.addListener(playerLapCompletionMonitorListener, PlayerLapCompletionMonitorEvent.Type.onLapCompleted, PlayerLapCompletionMonitorEvent.Order.MINUS_4);
        GameEvents.wrongWay.addListener(wrongWayMonitorListener, WrongWayMonitorEvent.Type.onWrongWayBegins, WrongWayMonitorEvent.Order.MINUS_4);
        GameEvents.wrongWay.addListener(wrongWayMonitorListener, WrongWayMonitorEvent.Type.onWrongWayEnds, WrongWayMonitorEvent.Order.MINUS_4);
    }

    public void unregisterPlayerMonitorEvents() {
        GameEvents.lapCompletion.removeListener(playerLapCompletionMonitorListener, PlayerLapCompletionMonitorEvent.Type.onWarmUpStarted, PlayerLapCompletionMonitorEvent.Order.MINUS_4);
        GameEvents.lapCompletion.removeListener(playerLapCompletionMonitorListener, PlayerLapCompletionMonitorEvent.Type.onWarmUpCompleted, PlayerLapCompletionMonitorEvent.Order.MINUS_4);
        GameEvents.lapCompletion.removeListener(playerLapCompletionMonitorListener, PlayerLapCompletionMonitorEvent.Type.onLapStarted, PlayerLapCompletionMonitorEvent.Order.MINUS_4);
        GameEvents.lapCompletion.removeListener(playerLapCompletionMonitorListener, PlayerLapCompletionMonitorEvent.Type.onLapCompleted, PlayerLapCompletionMonitorEvent.Order.MINUS_4);
        GameEvents.wrongWay.removeListener(wrongWayMonitorListener, WrongWayMonitorEvent.Type.onWrongWayBegins, WrongWayMonitorEvent.Order.MINUS_4);
        GameEvents.wrongWay.removeListener(wrongWayMonitorListener, WrongWayMonitorEvent.Type.onWrongWayEnds, WrongWayMonitorEvent.Order.MINUS_4);
    }

    public void registerGhostEvents() {
        GameEvents.ghostCars.addListener(ghostListener, GhostCarEvent.Type.OnGhostFadingOut);
        GameEvents.ghostCars.addListener(ghostListener, GhostCarEvent.Type.ReplayStarted);
        GameEvents.ghostCars.addListener(ghostListener, GhostCarEvent.Type.ReplayEnded);
        GameEvents.ghostLapCompletion.addListener(ghostLapCompletionMonitorListener, GhostLapCompletionMonitorEvent.Type.OnLapCompleted, GhostLapCompletionMonitorEvent.Order.MINUS_4);
    }

    public void unregisterGhostEvents() {
        GameEvents.ghostCars.removeListener(ghostListener, GhostCarEvent.Type.OnGhostFadingOut);
        GameEvents.ghostCars.removeListener(ghostListener, GhostCarEvent.Type.ReplayStarted);
        GameEvents.ghostCars.removeListener(ghostListener, GhostCarEvent.Type.ReplayEnded);
        GameEvents.ghostLapCompletion.removeListener(ghostLapCompletionMonitorListener, GhostLapCompletionMonitorEvent.Type.OnLapCompleted, GhostLapCompletionMonitorEvent.Order.MINUS_4);
    }

    public void registerRenderEvents() {
        GameEvents.gameRenderer.addListener(rendererListener, GameRendererEvent.Type.BeforeRender, GameRendererEvent.Order.MINUS_4);
    }

    public void unregisterRenderEvents() {
        GameEvents.gameRenderer.removeListener(rendererListener, GameRendererEvent.Type.BeforeRender, GameRendererEvent.Order.MINUS_4);
    }
}
