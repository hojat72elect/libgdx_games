package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.screens.BaseScreen

class DialogMoreLanguages(currentScreen: BaseScreen) : BaseDialog(currentScreen, WIDTH, HEIGHT, 300f) {
    var labelText: Label
    var buttonTranslate: TextButton

    init {
        setCloseButton(210f)

        labelText = Label(Assets.languagesBundle!!.get("translateDescription"), LabelStyle(Assets.fontSmall, Color.BLACK))
        labelText.setWidth(getWidth() - 20)
        labelText.setFontScale(.75f)
        labelText.setWrap(true)
        labelText.setPosition(getWidth() / 2f - labelText.getWidth() / 2f, getHeight() / 2f - labelText.getHeight() / 2f + 30)

        buttonTranslate = TextButton(Assets.languagesBundle!!.get("translate"), Assets.textButtonStyle)
        screen.addPressEffect(buttonTranslate)
        buttonTranslate.label.setFontScale(.75f)

        buttonTranslate.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                buttonTranslate.setChecked(false)
                Gdx.net.openURI("https://webtranslateit.com/en/projects/10553-The-true-color/invitation_request")
                hide()
            }
        })


        buttonTranslate.pack()
        buttonTranslate.setPosition(getWidth() / 2f - buttonTranslate.getWidth() / 2f, 35f)

        addActor(labelText)
        addActor(buttonTranslate)
    }

    companion object {
        const val WIDTH: Float = 440f
        const val HEIGHT: Float = 250f
    }
}
