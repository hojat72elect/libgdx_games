package com.nGame.utils.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * UI Dialog Helper for handling with gamepad
 *
 * @author Oliver Eichner
 */
public class ControllerDialog extends Dialog {


    public ControllerDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    /**
     * Return helper, to be used by the controller adapter
     *
     * @param object boolean, true for ok and false for cancel
     */
    public void ret(Object object) {
        result(object);
        remove();
    }
}
