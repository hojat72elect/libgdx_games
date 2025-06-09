package com.nopalsoft.sharkadventure.scene2d

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.game.GameScreen
import com.nopalsoft.sharkadventure.game.GameWorld
import com.nopalsoft.sharkadventure.objects.Shark
import com.nopalsoft.sharkadventure.screens.Screens

class GameUI(gameScreen: GameScreen, gameWorld: GameWorld?) : Group() {
    var gameScreen: GameScreen
    var gameWorld: GameWorld?

    @JvmField
    var accelerationX: Int = 0

    @JvmField
    var didSwimUp: Boolean = false

    @JvmField
    var didFire: Boolean = false

    @JvmField
    var lifeBar: ProgressBarUI

    @JvmField
    var energyBar: ProgressBarUI

    var tableHeader: Table? = null
    var labelScore: Label? = null
    var buttonLeft: Button? = null
    var buttonRight: Button? = null
    var buttonSwimUp: Button? = null
    var buttonFire: Button? = null
    var buttonPause: Button? = null

    init {
        setBounds(0f, 0f, Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())
        this.gameScreen = gameScreen
        this.gameWorld = gameWorld

        init()

        lifeBar = ProgressBarUI(Assets.redBar, Assets.heart, Shark.MAX_LIFE.toFloat(), -ProgressBarUI.WIDTH, 440f)
        energyBar = ProgressBarUI(Assets.energyBar, Assets.blast, Shark.MAX_ENERGY.toFloat(), -ProgressBarUI.WIDTH, 395f)

        addActor(lifeBar)
        addActor(energyBar)
    }

    private fun init() {
        buttonSwimUp = Button(Assets.buttonUp, Assets.buttonUpPressed)
        buttonSwimUp!!.setSize(105f, 105f)
        buttonSwimUp!!.setPosition(692f, -105f)
        buttonSwimUp!!.getColor().a = .35f

        buttonFire = Button(Assets.buttonFire, Assets.buttonFirePressed)
        buttonFire!!.setSize(105f, 105f)
        buttonFire!!.setPosition(579f, -105f)
        buttonFire!!.getColor().a = .35f

        buttonRight = Button(Assets.buttonRight, Assets.buttonRightPressed, Assets.buttonRightPressed)
        buttonRight!!.setSize(120f, 120f)
        buttonRight!!.setPosition(130f, -120f)
        buttonRight!!.getColor().a = .35f

        buttonLeft = Button(Assets.buttonLeft, Assets.buttonLeftPressed, Assets.buttonLeftPressed)
        buttonLeft!!.setSize(120f, 120f)
        buttonLeft!!.setPosition(5f, -120f)
        buttonLeft!!.getColor().a = .35f

        buttonPause = Button(Assets.buttonPause, Assets.buttonPausePressed)
        buttonPause!!.setSize(45f, 45f)
        buttonPause!!.setPosition(845f, 430f)
        buttonPause!!.getColor().a = .5f

        tableHeader = Table()
        tableHeader!!.setSize(Screens.SCREEN_WIDTH.toFloat(), 50f)
        tableHeader!!.setPosition(0f, Screens.SCREEN_HEIGHT - tableHeader!!.getHeight())

        labelScore = Label("0", Assets.lblStyle)
        tableHeader!!.add<Label?>(labelScore).fill()

        addActor(tableHeader)
        buttonRight!!.addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                accelerationX = 1
                buttonRight!!.setChecked(true)
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                accelerationX = 0
                buttonRight!!.setChecked(false)
            }
        })
        buttonLeft!!.addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                accelerationX = -1
                buttonLeft!!.setChecked(true)
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                accelerationX = 0
                buttonLeft!!.setChecked(false)
            }
        })

        buttonSwimUp!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                didSwimUp = true
            }
        })

        buttonFire!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                didFire = true
            }
        })
        buttonPause!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gameScreen.setPaused()
            }
        })

        if (Gdx.app.type == ApplicationType.Android || Gdx.app.type == ApplicationType.iOS) {
            addActor(buttonRight)
            addActor(buttonLeft)
            addActor(buttonSwimUp)
            addActor(buttonFire)
        }

        addActor(buttonPause)
    }

    override fun act(delta: Float) {
        super.act(delta)

        labelScore!!.setText(gameScreen.score.toString() + " m")
    }

    private fun addInActions() {
        buttonSwimUp!!.addAction(Actions.moveTo(692f, 10f, ANIMATION_TIME))
        buttonFire!!.addAction(Actions.moveTo(579f, 10f, ANIMATION_TIME))
        buttonRight!!.addAction(Actions.moveTo(130f, 5f, ANIMATION_TIME))
        buttonLeft!!.addAction(Actions.moveTo(5f, 5f, ANIMATION_TIME))
        buttonPause!!.addAction(Actions.moveTo(750f, 430f, ANIMATION_TIME))
        lifeBar.addAction(Actions.moveTo(20f, 440f, ANIMATION_TIME))
        energyBar.addAction(Actions.moveTo(20f, 395f, ANIMATION_TIME))
    }

    private fun addOutActions() {
        buttonSwimUp!!.addAction(Actions.moveTo(692f, -105f, ANIMATION_TIME))
        buttonFire!!.addAction(Actions.moveTo(579f, -105f, ANIMATION_TIME))
        buttonRight!!.addAction(Actions.moveTo(130f, -120f, ANIMATION_TIME))
        buttonLeft!!.addAction(Actions.moveTo(5f, -120f, ANIMATION_TIME))
        buttonPause!!.addAction(Actions.moveTo(845f, 430f, ANIMATION_TIME))
        lifeBar.addAction(Actions.moveTo(-ProgressBarUI.WIDTH, 440f, ANIMATION_TIME))
        energyBar.addAction(Actions.moveTo(-ProgressBarUI.WIDTH, 395f, ANIMATION_TIME))
    }

    fun show(stage: Stage) {
        addInActions()
        stage.addActor(this)
    }

    fun removeWithAnimations() {
        addOutActions()
        addAction(Actions.sequence(Actions.delay(ANIMATION_TIME), Actions.removeActor()))
    }

    companion object {
        const val ANIMATION_TIME: Float = .35f
    }
}
