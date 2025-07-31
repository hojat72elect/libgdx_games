package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.screens.Screens

class DialogWarning(currentScreen: Screens, text: String?) : Dialog(currentScreen, 350f, 200f, 150f, Assets.backgroundSmallWindow) {
    init {
        setCloseButton(305f, 155f, 45f)

        val labelShop = Label(text, Assets.labelStyleChico)
        labelShop.setFontScale(1f)
        labelShop.setWrap(true)
        labelShop.setAlignment(Align.center)
        labelShop.setWidth(getWidth() - 20)

        labelShop.setPosition(getWidth() / 2f - labelShop.getWidth() / 2f, 90f)
        addActor(labelShop)
    }
}
