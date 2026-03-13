package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.screens.BaseScreen

class DialogAmazon(currentScreen: BaseScreen) : BaseDialog(currentScreen, WIDTH, HEIGHT, 300f) {
    var labelText: Label
    var buttonAmazonLogin: TextButton

    init {
        setCloseButton(210f)

        labelText = Label(languages?.get("loginToGoogle")?.replace("Google", "Amazon"), LabelStyle(Assets.fontSmall, Color.BLACK))
        labelText.setWidth(getWidth() - 20)
        labelText.setFontScale(.75f)
        labelText.setWrap(true)
        labelText.setPosition(getWidth() / 2f - labelText.getWidth() / 2f, 165f)

        buttonAmazonLogin = TextButton("", TextButtonStyle(Assets.buttonPlayDrawable, null, null, Assets.fontSmall))
        screen.addPressEffect(buttonAmazonLogin)
        buttonAmazonLogin.label.setFontScale(.75f)

        buttonAmazonLogin.addListener(object : ClickListener() {
        })

        addActor(labelText)
        addActor(buttonAmazonLogin)
    }

    override fun show(stage: Stage) {
        super.show(stage)

        val textButton = languages?.get("login")

        buttonAmazonLogin.setText(textButton)
        buttonAmazonLogin.pack()
        buttonAmazonLogin.setPosition(getWidth() / 2f - buttonAmazonLogin.getWidth() / 2f, 35f)
    }

    companion object {
        const val WIDTH: Float = 440f
        const val HEIGHT: Float = 250f
    }
}