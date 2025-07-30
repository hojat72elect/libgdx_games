package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.screens.BaseScreen

class DialogGoogle(currentScreen: BaseScreen) : BaseDialog(currentScreen, WIDTH, HEIGHT, 300f) {
    var labelText: Label
    var buttonGoogleLogin: TextButton

    init {
        setCloseButton(210f)

        labelText = Label(languages?.get("loginToGoogle"), LabelStyle(Assets.fontSmall, Color.BLACK))
        labelText.setWidth(getWidth() - 20)
        labelText.setFontScale(.75f)
        labelText.setWrap(true)
        labelText.setPosition(getWidth() / 2f - labelText.getWidth() / 2f, 140f)

        buttonGoogleLogin = TextButton("", TextButtonStyle(Assets.buttonGoogleTextDrawable, null, null, Assets.fontSmall))
        screen.addPressEffect(buttonGoogleLogin)
        buttonGoogleLogin.label.setFontScale(.75f)

        addActor(labelText)
        addActor(buttonGoogleLogin)
    }

    override fun show(stage: Stage) {
        super.show(stage)

        val textButton = languages?.get("login")

        buttonGoogleLogin.setText(textButton)
        buttonGoogleLogin.pack()
        buttonGoogleLogin.setPosition(getWidth() / 2f - buttonGoogleLogin.getWidth() / 2f, 35f)
    }

    companion object {
        const val WIDTH: Float = 440f
        const val HEIGHT: Float = 250f
    }
}