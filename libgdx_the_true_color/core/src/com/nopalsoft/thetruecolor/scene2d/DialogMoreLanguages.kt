package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.screens.BaseScreen

/**
 * The dialog that will be shown to the user if they click on "more" button in the languages screen.
 */
class DialogMoreLanguages(currentScreen: BaseScreen) : BaseDialog(currentScreen, WIDTH, HEIGHT, 300F) {

    private val labelText = Label(
        Assets.languages.get("translateDescription"),
        Label.LabelStyle(Assets.fontSmall, Color.BLACK)
    )
    private val textButtonTranslate =
        TextButton(Assets.languages.get("translate"), Assets.textButtonStyle)

    init {
        setCloseButton(210F)
        labelText.width = width - 20
        labelText.setFontScale(.75f)
        labelText.wrap = true
        labelText.setPosition(
            width / 2f - labelText.width / 2f,
            height / 2f - labelText.height / 2f + 30
        )

        screen.addPressEffect(textButtonTranslate)
        textButtonTranslate.label.setFontScale(.75f)

        textButtonTranslate.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                textButtonTranslate.isChecked = false
                Gdx.net.openURI("https://webtranslateit.com/en/projects/10553-The-true-color/invitation_request")
                hide()
            }
        })

        textButtonTranslate.pack()
        textButtonTranslate.setPosition(width / 2f - textButtonTranslate.width / 2f, 35f)

        addActor(labelText)
        addActor(textButtonTranslate)
    }

    companion object {
        const val WIDTH = 440F
        const val HEIGHT = 250F
    }
}