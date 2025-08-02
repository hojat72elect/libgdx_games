package com.nopalsoft.fifteen.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.fifteen.Assets
import com.nopalsoft.fifteen.game.GameScreen
import com.nopalsoft.fifteen.screens.MainMenuScreen
import com.nopalsoft.fifteen.screens.Screens

class MarcoPaused(screen: Screens) : Group() {
    var screen: Screens?

    init {
        this.screen = screen
        setSize(420f, 300f)
        setOrigin(getWidth() / 2f, getHeight() / 2f)
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 260f)
        setScale(.5f)

        val background = Image(Assets.fondoPuntuaciones)
        background.setSize(getWidth(), getHeight())
        addActor(background)

        val lbPaused = Label("Pause", Assets.labelStyleGrande)
        lbPaused.setAlignment(Align.center)
        lbPaused.setFontScale(.85f)
        lbPaused.setPosition(getWidth() / 2f - lbPaused.getWidth() / 2f, 230f)
        addActor(lbPaused)

        val lbResume = Label("Resume", Assets.labelStyleChico)
        lbResume.setWrap(true)
        lbResume.setAlignment(Align.center)
        lbResume.setPosition(getWidth() / 2f - lbResume.getWidth() / 2f, 155f)
        screen.addEfectoPress(lbResume)
        lbResume.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val oGame = screen as GameScreen
                remove()
                oGame.setRunning()
            }
        })

        val lbMainMenu = Label("Main Menu", Assets.labelStyleChico)
        lbMainMenu.setWrap(true)
        lbMainMenu.setAlignment(Align.center)
        lbMainMenu
            .setPosition(getWidth() / 2f - lbMainMenu.getWidth() / 2f, 65f)
        screen.addEfectoPress(lbMainMenu)
        lbMainMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(
                    MainMenuScreen::class.java,
                    screen.game!!
                )
            }
        })

        addAction(
            Actions.sequence(
                Actions.scaleTo(1f, 1f, .2f),
                Actions.run(object : Runnable {
                    override fun run() {
                        addActor(lbMainMenu)
                        addActor(lbResume)
                    }
                })
            )
        )
    }
}
