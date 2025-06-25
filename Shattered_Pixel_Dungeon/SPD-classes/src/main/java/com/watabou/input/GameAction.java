

package com.watabou.input;

import java.util.ArrayList;

//This is similar to an enum, but we don't use that because subclasses should be able to add actions
public class GameAction {

    private static final ArrayList<GameAction> ALL_ACTIONS = new ArrayList<>();

    private final int code;
    private final String name;

    protected GameAction(String name) {
        code = ALL_ACTIONS.size();
        this.name = name;

        ALL_ACTIONS.add(this);
    }

    public int code() {
        return code;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GameAction && ((GameAction) o).code == code;
    }

    public static final GameAction NONE = new GameAction("none");
    public static final GameAction BACK = new GameAction("back");

    public static final GameAction LEFT_CLICK = new GameAction("left_click");
    public static final GameAction RIGHT_CLICK = new GameAction("right_click");
    public static final GameAction MIDDLE_CLICK = new GameAction("middle_click");

    public static ArrayList<GameAction> allActions() {
        return new ArrayList<>(ALL_ACTIONS);
    }

    public static int totalActions() {
        return ALL_ACTIONS.size();
    }
}
