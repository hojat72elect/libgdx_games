package com.bitfire.uracer.game.logic.types;

import com.bitfire.uracer.game.GameEvents;
import com.bitfire.uracer.game.actors.GhostCar;
import com.bitfire.uracer.game.events.CarEvent;
import com.bitfire.uracer.game.events.GameRendererEvent;
import com.bitfire.uracer.game.events.GameRendererEvent.Order;
import com.bitfire.uracer.game.events.GameRendererEvent.Type;
import com.bitfire.uracer.game.events.GhostCarEvent;
import com.bitfire.uracer.game.events.GhostLapCompletionMonitorEvent;
import com.bitfire.uracer.game.events.PlayerDriftStateEvent;
import com.bitfire.uracer.game.events.PlayerLapCompletionMonitorEvent;
import com.bitfire.uracer.game.events.WrongWayMonitorEvent;
import com.bitfire.uracer.game.player.PlayerCar;

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

    // PlayerLapCompletionMonitorEvent events order redux
    //
    // 1. warmup started
    // 2. warmup completed + 3. lap started
    // 4. lap completed + 5. lap started
    private final GhostLapCompletionMonitorEvent.Listener ghostLapCompletionMonitorListener = new GhostLapCompletionMonitorEvent.Listener() {
        @Override
        public void handle(Object source, GhostLapCompletionMonitorEvent.Type type, GhostLapCompletionMonitorEvent.Order order) {
            switch (type) {
                case onLapCompleted:
                    GhostCar ghost = (GhostCar) source;
                    logic.ghostLapCompleted(ghost);
                    break;
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
        @SuppressWarnings("incomplete-switch")
        @Override
        public void handle(Object source, Type type, Order order) {
            switch (type) {
                case BeforeRender:
                    logic.beforeRender();
                    break;
            }
        }
    };
    private final PlayerDriftStateEvent.Listener driftStateListener = new PlayerDriftStateEvent.Listener() {
        @Override
        public void handle(Object source, PlayerDriftStateEvent.Type type, PlayerDriftStateEvent.Order order) {
            PlayerCar player = (PlayerCar) source;

            switch (type) {
                case onBeginDrift:
                    logic.driftBegins(player);
                    break;
                case onEndDrift:
                    logic.driftEnds(player);
                    break;
            }
        }
    };
    private final CarEvent.Listener playerCarListener = new CarEvent.Listener() {
        @Override
        public void handle(Object source, CarEvent.Type type, CarEvent.Order order) {
            CarEvent.Data eventData = GameEvents.playerCar.data;

            switch (type) {
                case onCollision:
                    logic.collision(eventData);
                    break;
                case onOutOfTrack:
                    logic.outOfTrack();
                    break;
                case onBackInTrack:
                    logic.backInTrack();
                    break;
                case onPhysicsForcesReady:
                    logic.physicsForcesReady(eventData);
                    break;
            }
        }
    };
    private final GhostCarEvent.Listener ghostListener = new GhostCarEvent.Listener() {
        @Override
        public void handle(Object source, GhostCarEvent.Type type, GhostCarEvent.Order order) {
            switch (type) {
                case onGhostFadingOut:
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

    //@off
    public void registerPlayerEvents() {
        GameEvents.driftState.addListener(driftStateListener, PlayerDriftStateEvent.Type.onBeginDrift);
        GameEvents.driftState.addListener(driftStateListener, PlayerDriftStateEvent.Type.onEndDrift);

        GameEvents.playerCar.addListener(playerCarListener, CarEvent.Type.onCollision);
        GameEvents.playerCar.addListener(playerCarListener, CarEvent.Type.onPhysicsForcesReady);
        GameEvents.playerCar.addListener(playerCarListener, CarEvent.Type.onOutOfTrack);
        GameEvents.playerCar.addListener(playerCarListener, CarEvent.Type.onBackInTrack);
    }

    public void unregisterPlayerEvents() {
        GameEvents.driftState.removeListener(driftStateListener, PlayerDriftStateEvent.Type.onBeginDrift);
        GameEvents.driftState.removeListener(driftStateListener, PlayerDriftStateEvent.Type.onEndDrift);

        GameEvents.playerCar.removeListener(playerCarListener, CarEvent.Type.onCollision);
        GameEvents.playerCar.removeListener(playerCarListener, CarEvent.Type.onPhysicsForcesReady);
        GameEvents.playerCar.removeListener(playerCarListener, CarEvent.Type.onOutOfTrack);
        GameEvents.playerCar.removeListener(playerCarListener, CarEvent.Type.onBackInTrack);
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
        GameEvents.ghostCars.addListener(ghostListener, GhostCarEvent.Type.onGhostFadingOut);
        GameEvents.ghostCars.addListener(ghostListener, GhostCarEvent.Type.ReplayStarted);
        GameEvents.ghostCars.addListener(ghostListener, GhostCarEvent.Type.ReplayEnded);
        GameEvents.ghostLapCompletion.addListener(ghostLapCompletionMonitorListener, GhostLapCompletionMonitorEvent.Type.onLapCompleted, GhostLapCompletionMonitorEvent.Order.MINUS_4);
    }

    public void unregisterGhostEvents() {
        GameEvents.ghostCars.removeListener(ghostListener, GhostCarEvent.Type.onGhostFadingOut);
        GameEvents.ghostCars.removeListener(ghostListener, GhostCarEvent.Type.ReplayStarted);
        GameEvents.ghostCars.removeListener(ghostListener, GhostCarEvent.Type.ReplayEnded);
        GameEvents.ghostLapCompletion.removeListener(ghostLapCompletionMonitorListener, GhostLapCompletionMonitorEvent.Type.onLapCompleted, GhostLapCompletionMonitorEvent.Order.MINUS_4);
    }

    public void registerRenderEvents() {
        GameEvents.gameRenderer.addListener(rendererListener, GameRendererEvent.Type.BeforeRender, GameRendererEvent.Order.MINUS_4);
    }

    public void unregisterRenderEvents() {
        GameEvents.gameRenderer.removeListener(rendererListener, GameRendererEvent.Type.BeforeRender, GameRendererEvent.Order.MINUS_4);
    }
    //@on
}
