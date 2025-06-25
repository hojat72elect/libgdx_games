

package com.watabou.input;

import com.watabou.noosa.Game;
import com.watabou.utils.Signal;

import java.util.ArrayList;

public class KeyEvent {

    public int code;
    public boolean pressed;

    public KeyEvent(int code, boolean pressed) {
        this.code = code;
        this.pressed = pressed;
    }

    // **********************
    // *** Static members ***
    // **********************

    private static final Signal<KeyEvent> keySignal = new Signal<>(true);

    public static void addKeyListener(Signal.Listener<KeyEvent> listener) {
        keySignal.add(listener);
    }

    public static void removeKeyListener(Signal.Listener<KeyEvent> listener) {
        keySignal.remove(listener);
    }

    public static void clearListeners() {
        keySignal.removeAll();
    }

    //Accumulated key events
    private static final ArrayList<KeyEvent> keyEvents = new ArrayList<>();

    public static synchronized void addKeyEvent(KeyEvent event) {
        keyEvents.add(event);
    }

    public static synchronized void processKeyEvents() {
        if (keyEvents.isEmpty()) {
            return;
        }

        for (KeyEvent k : keyEvents) {
            if (KeyBindings.getActionForKey(k) == GameAction.LEFT_CLICK) {
                Game.inputHandler.emulateTouch(ControllerHandler.CONTROLLER_POINTER_ID, PointerEvent.LEFT, k.pressed);
                if (KeyBindings.bindingKey) keySignal.dispatch(k);
            } else if (KeyBindings.getActionForKey(k) == GameAction.RIGHT_CLICK) {
                Game.inputHandler.emulateTouch(ControllerHandler.CONTROLLER_POINTER_ID, PointerEvent.RIGHT, k.pressed);
                if (KeyBindings.bindingKey) keySignal.dispatch(k);
            } else if (KeyBindings.getActionForKey(k) == GameAction.MIDDLE_CLICK) {
                Game.inputHandler.emulateTouch(ControllerHandler.CONTROLLER_POINTER_ID, PointerEvent.MIDDLE, k.pressed);
                if (KeyBindings.bindingKey) keySignal.dispatch(k);
            } else {
                keySignal.dispatch(k);
            }
        }
        keyEvents.clear();
    }

    public static boolean isKeyboardKey(int keyCode) {
        return keyCode == 0 || !ControllerHandler.icControllerKey(keyCode);
    }
}
