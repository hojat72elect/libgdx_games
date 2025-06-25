

package com.watabou.input;

import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.LinkedHashMap;

//FIXME at lot of the logic here, in WndKeyBindings, and SPDAction is fairly messy
// should see about doing some refactoring to clean this up
public class KeyBindings {

    //for keyboard keys
    private static LinkedHashMap<Integer, GameAction> bindings = new LinkedHashMap<>();

    //for controller buttons
    private static LinkedHashMap<Integer, GameAction> controllerBindings = new LinkedHashMap<>();

    public static LinkedHashMap<Integer, GameAction> getAllBindings() {
        return new LinkedHashMap<>(bindings);
    }

    public static void setAllBindings(LinkedHashMap<Integer, GameAction> newBindings) {
        bindings = new LinkedHashMap<>(newBindings);
    }

    public static LinkedHashMap<Integer, GameAction> getAllControllerBindings() {
        return new LinkedHashMap<>(controllerBindings);
    }

    public static void setAllControllerBindings(LinkedHashMap<Integer, GameAction> newBindings) {
        controllerBindings = new LinkedHashMap<>(newBindings);
    }

    //these are special keybinding that are not user-configurable
    private static final LinkedHashMap<Integer, GameAction> hardBindings = new LinkedHashMap<>();

    public static void addHardBinding(int keyCode, GameAction action) {
        hardBindings.put(keyCode, action);
    }

    public static boolean bindingKey = false;

    public static boolean isKeyBound(int keyCode) {
        if (keyCode < 0 || (keyCode > 255 && keyCode < 1000)) {
            return false;
        }
        return bindingKey
                || bindings.containsKey(keyCode)
                || controllerBindings.containsKey(keyCode)
                || hardBindings.containsKey(keyCode);
    }

    public static GameAction getActionForKey(KeyEvent event) {
        if (bindings.containsKey(event.code)) {
            return bindings.get(event.code);
        } else if (controllerBindings.containsKey(event.code)) {
            return controllerBindings.get(event.code);
        } else if (hardBindings.containsKey(event.code)) {
            return hardBindings.get(event.code);
        }
        return GameAction.NONE;
    }

    public static int getFirstKeyForAction(GameAction action, boolean preferController) {
        ArrayList<Integer> keys = getKeyboardKeysForAction(action);
        ArrayList<Integer> buttons = getControllerKeysForAction(action);
        if (preferController) {
            if (!buttons.isEmpty()) return buttons.get(0);
        } else {
            if (!keys.isEmpty()) return keys.get(0);
        }
        return 0;
    }

    public static ArrayList<Integer> getKeyboardKeysForAction(GameAction action) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i : bindings.keySet()) {
            if (bindings.get(i) == action) {
                result.add(i);
            }
        }
        return result;
    }

    public static ArrayList<Integer> getControllerKeysForAction(GameAction action) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i : controllerBindings.keySet()) {
            if (controllerBindings.get(i) == action) {
                result.add(i);
            }
        }
        return result;
    }

    public static String getKeyName(int keyCode) {
        if (ControllerHandler.customButtonName(keyCode) != null) {
            return ControllerHandler.customButtonName(keyCode);
        }

        //custom codes for mouse buttons
        if (keyCode == 1003) {
            return "Mouse 4";
        } else if (keyCode == 1004) {
            return "Mouse 5";
        }

        if (keyCode == Input.Keys.UNKNOWN) {
            return "None";
        } else if (keyCode == Input.Keys.PLUS) {
            return "+";
        } else if (keyCode == Input.Keys.BACKSPACE) {
            return "Backspc";
        } else if (keyCode == Input.Keys.FORWARD_DEL) {
            return "Delete";
        } else {
            return Input.Keys.toString(keyCode);
        }
    }
}
