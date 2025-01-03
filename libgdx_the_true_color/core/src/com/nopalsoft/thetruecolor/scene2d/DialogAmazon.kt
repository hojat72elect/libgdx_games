package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.screens.BaseScreen

class DialogAmazon(currentScreen: BaseScreen) : BaseDialog(
    currentScreen,
    WIDTH,
    HEIGHT,
    300F
) {

    private val labelText = Label(
        languages.get("loginToGoogle").replace("Google", "Amazon"),
        Label.LabelStyle(Assets.fontSmall, Color.BLACK)
    )
    private val buttonAmazonLogin =
        TextButton("", TextButton.TextButtonStyle(Assets.buttonPlay, null, null, Assets.fontSmall))


    init {
        setCloseButton(210f)

        labelText.width = width - 20
        labelText.setFontScale(.75f)
        labelText.wrap = true
        labelText.setPosition(width / 2f - labelText.width / 2f, 165f)

        screen.addPressEffect(buttonAmazonLogin)
        buttonAmazonLogin.label.setFontScale(.75f)

        buttonAmazonLogin.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })

        addActor(labelText)
        addActor(buttonAmazonLogin)
    }


    override fun show(stage: Stage) {
        super.show(stage)

        val textButton = languages.get("login")
        buttonAmazonLogin.setText(textButton)
        buttonAmazonLogin.pack()
        buttonAmazonLogin.setPosition(width / 2f - buttonAmazonLogin.width / 2f, 35f)
    }

    companion object {

        private const val WIDTH = 440F
        private const val HEIGHT = 250F
    }
}