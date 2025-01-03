package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.screens.BaseScreen

class DialogGoogle(currentScreen: BaseScreen) : BaseDialog(currentScreen, WIDTH, HEIGHT, 300f) {

    private val labelText =
        Label(languages["loginToGoogle"], LabelStyle(Assets.fontSmall, Color.BLACK))
    private val buttonGoogleLogin =
        TextButton("", TextButtonStyle(Assets.buttonGoogleText, null, null, Assets.fontSmall))

    init {

        setCloseButton(210f)
        labelText.width = width - 20
        labelText.setFontScale(.75f)
        labelText.wrap = true
        labelText.setPosition(width / 2f - labelText.width / 2f, 140f)

        screen.addPressEffect(buttonGoogleLogin)
        buttonGoogleLogin.label.setFontScale(.75f)

        buttonGoogleLogin.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })

        addActor(labelText)
        addActor(buttonGoogleLogin)
    }

    override fun show(stage: Stage) {
        super.show(stage)

        val textButton = languages.get("login")

        buttonGoogleLogin.setText(textButton)
        buttonGoogleLogin.pack()
        buttonGoogleLogin.setPosition(width / 2f - buttonGoogleLogin.width / 2f, 35f)
    }

    companion object {
        const val WIDTH = 440F
        const val HEIGHT = 250F
    }
}