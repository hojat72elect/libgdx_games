package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.game.GameScreen
import com.nopalsoft.slamthebird.screens.BaseScreen

class DialogPause(currentScreen: BaseScreen) : Dialog(currentScreen) {
    var gameScreen: GameScreen

    init {
        setSize(350f, 260f)
        y = 300f
        setBackGround()

        gameScreen = currentScreen as GameScreen

        val labelTitle = Label("Paused", Assets.smallLabelStyle)
        labelTitle.setPosition(width / 2f - labelTitle.width / 2f, 210f)

        val buttonResume = TextButton("Resume", Assets.styleTextButtonPurchased)
        screen.addPressEffect(buttonResume)
        buttonResume.setSize(150f, 50f)
        buttonResume.setPosition(width / 2f - buttonResume.width / 2f, 130f)
        buttonResume.label.wrap = true
        buttonResume.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                gameScreen.setRunningFromPaused()
            }
        })

        val buttonMainMenu = TextButton(
            "Menu",
            Assets.styleTextButtonPurchased
        )
        screen.addPressEffect(buttonMainMenu)
        buttonMainMenu.setSize(150f, 50f)
        buttonMainMenu.setPosition(width / 2f - buttonResume.width / 2f, 40f)
        buttonMainMenu.label.wrap = true
        buttonMainMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })

        addActor(buttonResume)
        addActor(buttonMainMenu)
        addActor(labelTitle)
    }
}
