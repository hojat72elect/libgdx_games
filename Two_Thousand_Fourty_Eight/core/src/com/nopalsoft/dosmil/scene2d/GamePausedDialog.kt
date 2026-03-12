package com.nopalsoft.dosmil.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.dosmil.Assets
import com.nopalsoft.dosmil.game.GameScreen
import com.nopalsoft.dosmil.screens.MainMenuScreen
import com.nopalsoft.dosmil.screens.Screens

class GamePausedDialog(var screen: Screens) : Group() {
    init {
        setSize(420f, 300f)
        setOrigin(width / 2f, height / 2f)
        setPosition(Screens.SCREEN_WIDTH / 2f - width / 2f, 260f)
        setScale(.5f)

        val background = Image(Assets.scoresBackgroundAtlasRegion)
        background.setSize(width, height)
        addActor(background)

        val labelPaused = Label(Assets.languagesBundle!!["pause"], Assets.labelStyleLarge)
        labelPaused.setAlignment(Align.center)
        labelPaused.setFontScale(.85f)
        labelPaused.setPosition(width / 2f - labelPaused.width / 2f, 230f)
        addActor(labelPaused)

        val labelResume = Label(Assets.languagesBundle!!["resume"], Assets.labelStyleSmall)
        labelResume.wrap = true
        labelResume.setAlignment(Align.center)
        labelResume.setPosition(width / 2f - labelResume.width / 2f, 155f)
        screen.addPressEffect(labelResume)
        labelResume.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                val gameScreen = screen as GameScreen
                remove()
                gameScreen.setRunning()
            }
        })

        val labelMainMenu = Label(Assets.languagesBundle!!["menu"], Assets.labelStyleSmall)
        labelMainMenu.wrap = true
        labelMainMenu.setAlignment(Align.center)
        labelMainMenu.setPosition(width / 2f - labelMainMenu.width / 2f, 65f)
        screen.addPressEffect(labelMainMenu)
        labelMainMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, screen.game)
            }
        })

        addAction(
            Actions.sequence(
                Actions.scaleTo(1f, 1f, .2f),
                Actions.run {
                    addActor(labelMainMenu)
                    addActor(labelResume)
                })
        )
    }
}
