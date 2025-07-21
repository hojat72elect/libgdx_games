package com.nopalsoft.donttap.dialogs

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.donttap.Assets
import com.nopalsoft.donttap.game.GameScreen
import com.nopalsoft.donttap.screens.MainMenuScreen
import com.nopalsoft.donttap.screens.Screens

class GamePausedDialog(var screen: GameScreen) : Group() {
    var dim: Image? = null

    init {
        setSize(420f, 300f)
        setOrigin(getWidth() / 2f, getHeight() / 2f)
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 260f)

        val background = Image(Assets.scoresBackgroundDrawable)
        background.setSize(getWidth(), getHeight())
        addActor(background)
        getColor().a = 0f

        val lbPaused = Label("Pause", Assets.labelStyleBlack)
        lbPaused.setAlignment(Align.center)
        lbPaused.setFontScale(1.15f)
        lbPaused.setPosition(getWidth() / 2f - lbPaused.getWidth() / 2f, 250f)

        val txtMode = when (screen.mode) {
            GameScreen.MODE_CLASSIC -> "Classic"
            GameScreen.MODE_TIME, GameScreen.MODE_ENDLESS -> "Endless"
            else -> "Endless"
        }

        val labelMode = Label(txtMode, Assets.labelStyleBlack)
        labelMode.setAlignment(Align.center)
        labelMode.setPosition(getWidth() / 2f - labelMode.getWidth() / 2f, 210f)

        val btResume = TextButton(
            "Resume",
            Assets.textButtonStyleSmall
        )
        btResume.setPosition(getWidth() / 2f - btResume.getWidth() / 2f, 120f)
        screen.addPressEffect(btResume)
        btResume.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                screen.setRunning()
                hide()
            }
        })

        val buttonMainMenu = TextButton(
            "Main menu",
            Assets.textButtonStyleSmall
        )
        buttonMainMenu
            .setPosition(getWidth() / 2f - buttonMainMenu.getWidth() / 2f, 20f)
        screen.addPressEffect(buttonMainMenu)
        buttonMainMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(
                    MainMenuScreen::class.java,
                    screen.game!!
                )
            }
        })

        addActor(lbPaused)
        addActor(labelMode)
        addAction(
            Actions.sequence(
                Actions.alpha(1f, fadeDuration),
                Actions.run {
                    addActor(btResume)
                    addActor(buttonMainMenu)
                }
            )
        )
    }

    fun show(stage: Stage) {
        dim = Image(Assets.blackPixel)
        dim!!.setFillParent(true)
        dim!!.getColor().a = 0f
        dim!!.addAction(Actions.alpha(.7f, fadeDuration - .5f))

        stage.addActor(dim)

        stage.addActor(this)
    }

    private fun hide() {
        dim!!.remove()
        remove()
    }

    companion object {
        var fadeDuration: Float = 0.25f
    }
}
