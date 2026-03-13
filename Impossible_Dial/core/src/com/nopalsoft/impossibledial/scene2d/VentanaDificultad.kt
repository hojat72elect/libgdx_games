package com.nopalsoft.impossibledial.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.impossibledial.Assets
import com.nopalsoft.impossibledial.game.GameScreen
import com.nopalsoft.impossibledial.screens.MainMenuScreen

class VentanaDificultad(currentScreen: MainMenuScreen) : Ventana(currentScreen, WIDTH, HEIGHT, 300f) {
    var btHard: TextButton
    var btEasy: TextButton

    init {
        setCloseButton()


        btEasy = TextButton(Assets.idiomas!!.get("easy"), TextButtonStyle(Assets.btEnabled, null, null, Assets.fontChico))
        btEasy.setSize(200f, 50f)
        screen.addEfectoPress(btEasy)
        btEasy.setPosition(getWidth() / 2f - btEasy.getWidth() / 2f, 125f)
        btEasy.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                currentScreen.changeScreenWithFadeOut(GameScreen::class.java, game!!, GameScreen.DIFICULTAD_EASY)
                hide()
            }
        })

        btHard = TextButton(Assets.idiomas!!.get("hard"), TextButtonStyle(Assets.btEnabled, null, null, Assets.fontChico))
        btHard.setSize(200f, 50f)
        screen.addEfectoPress(btHard)
        btHard.setPosition(getWidth() / 2f - btHard.getWidth() / 2f, 40f)
        btHard.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                currentScreen.changeScreenWithFadeOut(GameScreen::class.java, game!!, GameScreen.DIFICULTAD_HARD)
                hide()
            }
        })

        addActor(btHard)
        addActor(btEasy)
    }

    companion object {
        const val WIDTH: Float = 300f
        const val HEIGHT: Float = 225f
    }
}