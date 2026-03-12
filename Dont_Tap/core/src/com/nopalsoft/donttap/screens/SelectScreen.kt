package com.nopalsoft.donttap.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.donttap.Assets
import com.nopalsoft.donttap.DoNotTapGame
import com.nopalsoft.donttap.dialogs.HelpDialog
import com.nopalsoft.donttap.game.GameScreen

class SelectScreen(game: DoNotTapGame) : Screens(game) {
    var labelSelectGameMode: Label
    var buttonClassic: TextButton
    var buttonEndless: TextButton
    var buttonTime: TextButton
    var buttonHelp: TextButton

    var buttonBack: Button

    init {
        addBackGround()

        labelSelectGameMode = Label("Select game mode", Assets.labelStyleBlack)
        labelSelectGameMode.setWidth(300f)
        labelSelectGameMode.setFontScale(1.3f)
        labelSelectGameMode.setWrap(true)
        labelSelectGameMode.setPosition(
            SCREEN_WIDTH / 2f - labelSelectGameMode.getWidth() / 2f, 650f
        )
        labelSelectGameMode.setAlignment(Align.center)

        val menu = Table()
        menu.setSize(200f, 540f)
        menu.setPosition(SCREEN_WIDTH / 2f - menu.getWidth() / 2f, 60f)
        menu.defaults().center().expand()

        buttonClassic = TextButton("Classic", Assets.textButtonStyleSmall)
        addPressEffect(buttonClassic)
        buttonClassic.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(
                    GameScreen::class.java, game,
                    GameScreen.MODE_CLASSIC
                )
            }
        })

        buttonTime = TextButton("Time trial", Assets.textButtonStyleSmall)
        addPressEffect(buttonTime)
        buttonTime.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(
                    GameScreen::class.java, game,
                    GameScreen.MODE_TIME
                )
            }
        })

        buttonEndless = TextButton("Endless", Assets.textButtonStyleSmall)
        addPressEffect(buttonEndless)
        buttonEndless.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(
                    GameScreen::class.java, game,
                    GameScreen.MODE_ENDLESS
                )
            }
        })

        buttonHelp = TextButton("?", Assets.textButtonStyleSmall)
        addPressEffect(buttonHelp)
        buttonHelp.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                HelpDialog(this@SelectScreen).show(stage)
            }
        })

        buttonBack = Button(ButtonStyle(Assets.buttonBack, null, null))
        buttonBack.setSize(55f, 55f)
        buttonBack.setPosition(5f, 5f)
        addPressEffect(buttonBack)
        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(MainMenuScreen::class.java, game)
                super.clicked(event, x, y)
            }
        })

        menu.add<TextButton?>(buttonClassic)

        menu.row()
        menu.add<TextButton?>(buttonTime)

        menu.row()
        menu.add<TextButton?>(buttonEndless)

        menu.row()
        menu.add<TextButton?>(buttonHelp).width(45f)

        stage.addActor(labelSelectGameMode)
        stage.addActor(menu)
        stage.addActor(buttonBack)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) changeScreenWithFadeOut(MainMenuScreen::class.java, game!!)
        return true
    }

    override fun hide() {
        super.hide()
    }

    override fun draw(delta: Float) {
    }

    override fun update(delta: Float) {
    }
}
