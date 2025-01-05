package com.nGame.utils.ng.input;

/**
 * Created by oli on 06.03.14.
 */
public class GamePadMapping {

    private int[] buttonMap;

    private int[] axisMap;


    public GamePadMapping(int buttons, int axis) {
        buttonMap = new int[buttons];
        axisMap = new int[axis];
    }

    public void mapButtonToKey(int buttonId, int keycode) {
        if (buttonId >= 0 && buttonId < buttonMap.length) {
            buttonMap[buttonId] = keycode;
        }
    }

    public void mapAxisToMouse(int axisId, int mouseAxisId) {
        if (axisId >= 0 && axisId < axisMap.length) {
            axisMap[axisId] = mouseAxisId;
        }
    }

    public int getKeycode(int buttonId) {
        return buttonMap[buttonId];
    }

    public int getMouseAxis(int axisId) {
        return axisMap[axisId];
    }
}
