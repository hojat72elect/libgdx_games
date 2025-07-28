package com.nopalsoft.ponyrace.game_objects;

import com.nopalsoft.ponyrace.game.TileMapHandler;

import java.util.Random;

public class Flag {
    public State state;
    public ActionType actionType;
    Random random;

    public Flag(TileMapHandler world, ActionType actionType) {
        random = world.random;
        state = State.NORMAL;
        this.actionType = actionType;
    }

    public boolean permitirSalto() {
        if (state == State.NORMAL && random.nextBoolean()) {
            state = State.ACTIVE;
            return true;
        }
        return false;
    }

    public enum State {
        NORMAL,
        ACTIVE
    }

    public enum ActionType {
        JUMP_LEFT,
        JUMP_RIGHT,
        JUMP
    }
}
