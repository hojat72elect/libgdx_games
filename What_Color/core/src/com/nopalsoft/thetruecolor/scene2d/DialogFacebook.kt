package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.screens.BaseScreen

class DialogFacebook(currentScreen: BaseScreen) : BaseDialog(currentScreen, WIDTH, HEIGHT, 300f) {
    var labelText: Label
    var buttonFacebookLogin: TextButton

    init {
        setCloseButton(210f)

        labelText = Label(languages?.get("loginToFacebook"), LabelStyle(Assets.fontSmall, Color.BLACK))
        labelText.setWidth(getWidth() - 20)
        labelText.setFontScale(.75f)
        labelText.setWrap(true)
        labelText.setPosition(getWidth() / 2f - labelText.getWidth() / 2f, 140f)

        buttonFacebookLogin = TextButton("", TextButtonStyle(Assets.buttonFacebookTextDrawable, null, null, Assets.fontSmall))
        screen.addPressEffect(buttonFacebookLogin)
        buttonFacebookLogin.label.setFontScale(.75f)

        addActor(labelText)
        addActor(buttonFacebookLogin)
    }

    override fun show(stage: Stage) {
        super.show(stage)

        val textButton = languages?.get("login")

        buttonFacebookLogin.setText(textButton)
        buttonFacebookLogin.pack()
        buttonFacebookLogin.setPosition(getWidth() / 2f - buttonFacebookLogin.getWidth() / 2f, 35f)
    }

    companion object {
        const val WIDTH: Float = 440f
        const val HEIGHT: Float = 250f
    }
}