package com.nopalsoft.ninjarunner.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.game.GameWorld
import com.nopalsoft.ninjarunner.screens.Screens

class GameUI(gameScreen: GameScreen?, gameWorld: GameWorld?) : Group() {
    var gameScreen: GameScreen?
    var gameWorld: GameWorld?

    var tableHeader: Table? = null
    var labelScore: Label? = null

    var buttonJump: Button? = null
    var buttonSlide: Button? = null
    var didJump: Boolean = false
    var didSlide: Boolean = false
    var didDash: Boolean = false

    init {
        setBounds(0f, 0f, Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())
        this.gameScreen = gameScreen
        this.gameWorld = gameWorld

        init()
    }

    private fun init() {
        buttonJump = Button(ButtonStyle(null, null, null))
        buttonJump!!.setSize(getWidth() / 2f, getHeight())
        buttonJump!!.setPosition(0f, 0f)
        buttonJump!!.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                didJump = true
                return false
            }
        })

        buttonSlide = Button(ButtonStyle(null, null, null))
        buttonSlide!!.setSize(getWidth() / 2f, getHeight())
        buttonSlide!!.setPosition(getWidth() / 2f + 1, 0f)
        buttonSlide!!.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                didSlide = true
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                didSlide = false
            }
        })

        tableHeader = Table()
        tableHeader!!.setSize(Screens.SCREEN_WIDTH.toFloat(), 50f)
        tableHeader!!.setPosition(0f, Screens.SCREEN_HEIGHT - tableHeader!!.getHeight())

        labelScore = Label("0", Assets.labelStyleSmall)
        tableHeader!!.add<Label?>(labelScore).fill()

        addActor(tableHeader)

        addActor(buttonJump)
        addActor(buttonSlide)
    }

    override fun act(delta: Float) {
        super.act(delta)
    }

    private fun addInActions() {
    }

    fun show(stage: Stage) {
        addInActions()
        stage.addActor(this)
    }

    companion object {
        const val ANIMATION_TIME: Float = .35f
    }
}
