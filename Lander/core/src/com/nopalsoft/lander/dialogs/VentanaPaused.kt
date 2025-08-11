package com.nopalsoft.lander.dialogs

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.lander.Assets
import com.nopalsoft.lander.MainLander
import com.nopalsoft.lander.game.GameScreen
import com.nopalsoft.lander.game.WorldGame
import com.nopalsoft.lander.screens.LevelScreen
import com.nopalsoft.lander.screens.Screens

/**
 * I used the Window class because I had to put the little mark on it.
 */
class VentanaPaused(var game: MainLander, var oWorld: WorldGame, val levelActual: Int) : Window("", Assets.styleDialogGameOver) {

    var lblLevelActual = Label("Level " + (levelActual + 1), Assets.styleLabelMediana)
    var estrellas = arrayOfNulls<Image>(3)
    var btMenu = ImageButton(Assets.styleImageButtonPause)
    var btResume = ImageButton(Assets.styleImageButtonPause)
    var btTryAgain = ImageButton(Assets.styleImageButtonPause)
    var ignoreTouchDown = object : InputListener() {
        override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            event.cancel()
            return false
        }
    }

    init {
        this.setMovable(false)
        val paused = Label("Paused", Assets.styleLabelMediana)
        val starTable = Table()
        starTable.defaults().pad(15f)
        if (oWorld.estrellasTomadas >= 0) {
            for (star in 0..2) {
                // They are all gray the first time
                estrellas[star] = Image(Assets.starOff)
                starTable.add<Image?>(estrellas[star]).width(50f).height(50f)
            }
        }

        btMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide(LevelScreen::class.java)
            }
        })

        btResume.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide(null)
                resumeGame()
            }
        })
        btTryAgain.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide(GameScreen::class.java)
            }
        })

        this.row().padTop(30f)
        this.add(paused).colspan(3)
        this.row().padTop(30f)
        this.add(lblLevelActual).colspan(3)
        this.row().padTop(30f)
        this.add(starTable).colspan(3)
        this.row().padTop(30f).expandX()
        this.add(btMenu)
        this.add(btResume)
        this.add(btTryAgain)
        this.row().expandX()
        this.add(Label("Menu", Assets.styleLabelMediana))
        this.add(Label("Resume", Assets.styleLabelMediana))
        this.add(Label("Try Again", Assets.styleLabelMediana))
    }

    fun show(stage: Stage) {
        /*
                * The grey stars are replaced by the taken ones =)
                */

        for (i in 0..<oWorld.estrellasTomadas) {
            estrellas[i]!!.setDrawable(TextureRegionDrawable(Assets.star))
        }

        this.pack()

        setSize(Screens.SCREEN_WIDTH.toFloat(), 0f)

        val sizeAction = Actions.action(SizeToAction::class.java)
        sizeAction.setSize(Screens.SCREEN_WIDTH.toFloat(), 500f) // FINAL HEIGHT
        sizeAction.duration = .25f

        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, Screens.SCREEN_HEIGHT / 2f - 500 / 2f) // 500 FINAL HEIGHT
        addAction(sizeAction)

        stage.addActor(this)
        if (fadeDuration > 0) {
            getColor().a = 0f
            addAction(Actions.fadeIn(fadeDuration, Interpolation.fade))
        }
    }

    fun resumeGame() {
        hide(null)
        if (game.getScreen() is GameScreen) GameScreen.state = GameScreen.STATE_RUNNING
    }

    fun hide(newScreen: Class<*>?) {
        if (fadeDuration > 0) {
            addCaptureListener(ignoreTouchDown)

            val sizeAction = Actions.action(SizeToAction::class.java)
            sizeAction.setSize(Screens.SCREEN_WIDTH.toFloat(), 0f) //FINAL HEIGHT
            sizeAction.duration = .25f

            val run = Actions.run {
                if (newScreen == LevelScreen::class.java) {
                    game.setScreen(LevelScreen(game))
                } else if (newScreen == GameScreen::class.java) {
                    game.setScreen(GameScreen(game, levelActual))
                }
            }

            addAction(Actions.sequence(Actions.parallel(Actions.fadeOut(fadeDuration, Interpolation.fade), sizeAction), Actions.removeListener(ignoreTouchDown, true), run, Actions.removeActor()))
        } else remove()
    }

    companion object {
        var fadeDuration: Float = 0.4f
    }
}
