

package com.watabou.input;

import com.badlogic.gdx.Input;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Cursor;
import com.watabou.utils.PointF;
import com.watabou.utils.Signal;

import java.util.ArrayList;
import java.util.HashMap;

public class PointerEvent {

    public enum Type {
        DOWN,
        UP,
        CANCEL,
        HOVER
    }

    //buttons
    public static final int NONE = -1;
    public static final int LEFT = Input.Buttons.LEFT;
    public static final int RIGHT = Input.Buttons.RIGHT;
    public static final int MIDDLE = Input.Buttons.MIDDLE;
    public static final int BACK = Input.Buttons.BACK; //currently unused
    public static final int FORWARD = Input.Buttons.FORWARD;//currently unused

    public PointF start;
    public PointF current;
    public int id;
    public Type type;
    public int button;
    public boolean handled; //for hover events, to ensure hover always ends even with overlapping elements

    public PointerEvent(int x, int y, int id, Type type) {
        this(x, y, id, type, NONE);
    }

    public PointerEvent(int x, int y, int id, Type type, int button) {
        if (Cursor.isCursorCaptured()) {
            x = Game.width / 2;
            y = Game.width / 2;
        }
        start = current = new PointF(x, y);
        this.id = id;
        this.type = type;
        handled = false;
        this.button = button;
    }

    public void update(PointerEvent other) {
        this.current = other.current;
    }

    public void update(int x, int y) {
        current.set(x, y);
    }

    public PointerEvent up() {
        if (type == Type.DOWN) type = Type.UP;
        return this;
    }

    public PointerEvent cancel() {
        if (type == Type.DOWN) type = Type.CANCEL;
        return this;
    }

    public PointerEvent handle() {
        handled = true;
        return this;
    }

    // **********************
    // *** Static members ***
    // **********************

    private static final Signal<PointerEvent> pointerSignal = new Signal<>(true);

    public static void addPointerListener(Signal.Listener<PointerEvent> listener) {
        pointerSignal.add(listener);
    }

    public static void removePointerListener(Signal.Listener<PointerEvent> listener) {
        pointerSignal.remove(listener);
    }

    public static void clearListeners() {
        pointerSignal.removeAll();
    }

    // Accumulated pointer events
    private static final ArrayList<PointerEvent> pointerEvents = new ArrayList<>();
    private static final HashMap<Integer, PointerEvent> activePointers = new HashMap<>();

    private static final PointF lastHoverPos = new PointF();

    public static PointF currentHoverPos() {
        if (lastHoverPos.x == 0 && lastHoverPos.y == 0) {
            lastHoverPos.x = Game.width / 2;
            lastHoverPos.y = Game.height / 2;
        }
        return lastHoverPos.clone();
    }

    public static void setHoverPos(PointF pos) {
        lastHoverPos.set(pos);
    }

    public static synchronized void addPointerEvent(PointerEvent event) {
        pointerEvents.add(event);
    }

    public static synchronized void addIfExisting(PointerEvent event) {
        if (activePointers.containsKey(event.id)) {
            pointerEvents.add(event);
        }
    }

    public static boolean clearKeyboardThisPress = true;

    public static synchronized void processPointerEvents() {
        if (pointerEvents.isEmpty()) {
            return;
        }

        //handle any hover events separately first as we may need to add drag events
        boolean hovered = false;
        for (PointerEvent p : pointerEvents) {
            if (p.type == Type.HOVER) {
                lastHoverPos.set(p.current);
                pointerSignal.dispatch(p);
                hovered = true;
            }
        }

        for (PointerEvent p : pointerEvents) {
            if (p.type == Type.HOVER) {
                continue;
            }
            clearKeyboardThisPress = true;
            if (activePointers.containsKey(p.id)) {
                PointerEvent existing = activePointers.get(p.id);
                existing.current = p.current;
                if (existing.type == p.type) {
                    pointerSignal.dispatch(null);
                } else if (p.type == Type.DOWN) {
                    pointerSignal.dispatch(existing);
                } else if (p.type == Type.UP) {
                    activePointers.remove(existing.id);
                    pointerSignal.dispatch(existing.up());
                } else if (p.type == Type.CANCEL) {
                    activePointers.remove(existing.id);
                    pointerSignal.dispatch(existing.cancel());
                }
            } else {
                if (p.type == Type.DOWN) {
                    activePointers.put(p.id, p);
                }
                pointerSignal.dispatch(p);
            }
            if (clearKeyboardThisPress) {
                //most press events should clear the keyboard
                Game.platform.setOnscreenKeyboardVisible(false);
            }
        }
        pointerEvents.clear();

        //add drag events for any emulated presses
        if (hovered && activePointers.containsKey(ControllerHandler.CONTROLLER_POINTER_ID)) {
            Game.inputHandler.emulateDrag(ControllerHandler.CONTROLLER_POINTER_ID);
        }
    }
}
