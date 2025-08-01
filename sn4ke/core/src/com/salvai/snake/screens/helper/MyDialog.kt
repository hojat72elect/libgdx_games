package com.salvai.snake.screens.helper

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.salvai.snake.utils.Constants


class MyDialog(title: String, skin: Skin, label: Label?) : Dialog(title, skin) {
    init {
        pad(Constants.DIALOG_BUTTON_PAD.toFloat())
        getButtonTable().defaults().space(Constants.DIALOG_BUTTON_SPACE.toFloat()).size(Constants.DIALOG_BUTTON_SIZE.toFloat())
        if (label != null) {
            label.setFontScale(2f)
            text(label)
        }
    }
}
