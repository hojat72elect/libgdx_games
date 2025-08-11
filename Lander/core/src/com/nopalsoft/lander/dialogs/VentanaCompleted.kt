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
class VentanaCompleted(var game: MainLander, var oWorld: WorldGame, levelActual: Int) : Window("", Assets.styleDialogGameOver) {
    var lblLevelActual: Label?
    var estrellas: Array<Image?>

    var btMenu: ImageButton?
    var btTryAgain: ImageButton?
    var btNextLevel: ImageButton?
    var ignoreTouchDown: InputListener = object : InputListener() {
        override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            event.cancel()
            return false
        }
    }

    init {
        this.setMovable(false)

        val paused = Label("Level Completed", Assets.styleLabelMediana)
        lblLevelActual = Label("Level " + (levelActual + 1), Assets.styleLabelMediana)

        estrellas = arrayOfNulls<Image>(3)
        val starTable = Table()
        starTable.defaults().pad(15f)
        if (oWorld.estrellasTomadas >= 0) {
            for (star in 0..2) {
                // Todas son grises la primera vez
                estrellas[star] = Image(Assets.starOff)
                starTable.add<Image?>(estrellas[star]).width(50f).height(50f)
            }
        }

        btMenu = ImageButton(Assets.styleImageButtonPause)
        btMenu!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide(LevelScreen::class.java, 0)
            }
        })

        btTryAgain = ImageButton(Assets.styleImageButtonPause)
        btTryAgain!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide(GameScreen::class.java, levelActual)
            }
        })

        btNextLevel = ImageButton(Assets.styleImageButtonPause)
        btNextLevel!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide(GameScreen::class.java, levelActual + 1)
            }
        })

        this.row().padTop(30f)
        this.add<Label?>(paused).colspan(3)

        this.row().padTop(30f)
        this.add<Label?>(lblLevelActual).colspan(3)

        this.row().padTop(30f)
        this.add<Table?>(starTable).colspan(3)

        this.row().padTop(30f).expandX()
        this.add<ImageButton?>(btMenu)
        this.add<ImageButton?>(btTryAgain)
        this.add<ImageButton?>(btNextLevel)

        this.row().expandX()
        this.add<Label?>(Label("Menu", Assets.styleLabelMediana))
        this.add<Label?>(Label("Try Again", Assets.styleLabelMediana))
        this.add<Label?>(Label("Next Level", Assets.styleLabelMediana))
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

    fun hide(newScreen: Class<*>?, level: Int) {
        if (fadeDuration > 0) {
            addCaptureListener(ignoreTouchDown)

            val sizeAction = Actions.action(SizeToAction::class.java)
            sizeAction.setSize(Screens.SCREEN_WIDTH.toFloat(), 0f) // FINAL HEIGHT
            sizeAction.duration = .25f

            val run = Actions.run {
                if (newScreen == LevelScreen::class.java) {
                    game.setScreen(LevelScreen(game))
                } else if (newScreen == GameScreen::class.java) {
                    game.setScreen(GameScreen(game, level))
                }
            }

            addAction(Actions.sequence(Actions.parallel(Actions.fadeOut(fadeDuration, Interpolation.fade), sizeAction), Actions.removeListener(ignoreTouchDown, true), run, Actions.removeActor()))
        } else remove()
    }

    companion object {
        var fadeDuration: Float = 0.4f
    }
}
