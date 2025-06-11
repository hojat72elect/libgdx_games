package com.nopalsoft.superjumper.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.game.GameScreen
import com.nopalsoft.superjumper.game.WorldGame
import com.nopalsoft.superjumper.screens.MainMenuScreen

class BaseWindowPause(currentScreen: GameScreen) : BaseWindow(currentScreen, 350f, 280f, 300f) {
    var buttonMenu: TextButton? = null
    var buttonResume: TextButton? = null
    var worldGame: WorldGame = currentScreen.oWorld

    init {
        val labelShop = Label("Pause", Assets.labelStyleLarge)
        labelShop.setFontScale(1.5f)
        labelShop.setAlignment(Align.center)
        labelShop.setPosition(width / 2f - labelShop.width / 2f, 230f)
        addActor(labelShop)

        initializeButtons()

        val content = Table()

        content.defaults().expandX().uniform().fill()

        content.add(buttonResume)
        content.row().padTop(20f)
        content.add(buttonMenu)

        content.pack()
        content.setPosition(width / 2f - content.width / 2f, 50f)

        addActor(content)
    }

    private fun initializeButtons() {
        buttonMenu = TextButton("Menu", Assets.textButtonStyleLarge)
        buttonMenu!!.pad(15f)

        screen.addPressEffect(buttonMenu!!)
        buttonMenu!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })

        buttonResume = TextButton("Resume", Assets.textButtonStyleLarge)
        buttonResume!!.pad(15f)

        screen.addPressEffect(buttonResume!!)
        buttonResume!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })
    }

    override fun show(stage: Stage) {
        super.show(stage)
    }

    override fun hide() {
        (screen as GameScreen).setRunning()
        super.hide()
    }
}
