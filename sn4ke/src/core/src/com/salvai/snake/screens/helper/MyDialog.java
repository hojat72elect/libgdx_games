package com.salvai.snake.screens.helper;


import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.salvai.snake.utils.Constants;

public class MyDialog extends Dialog {
    public MyDialog(String title, Skin skin, Label label) {
        super(title, skin);
        pad(Constants.DIALOG_BUTTON_PAD);
        getButtonTable().defaults().space(Constants.DIALOG_BUTTON_SPACE).size(Constants.DIALOG_BUTTON_SIZE);
        if (label != null) {
            label.setFontScale(2);
            text(label);
        }
    }


}
