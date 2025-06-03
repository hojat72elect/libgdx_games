package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.game.GameScreen

class TouchPadControlsTable(var gameScreen: GameScreen) : Table() {
    var buttonUp: Button? = null
    var buttonDown: Button? = null
    var buttonLeft: Button? = null
    var buttonRight: Button? = null

    init {
        color.a = .4f
        init()

        val buttonSize = 75
        defaults().size(buttonSize.toFloat())

        add(buttonUp).colspan(2).center()
        row()
        add(buttonLeft).left()
        add(buttonRight).right().padLeft(buttonSize / 1.15f)
        row()
        add(buttonDown).colspan(2).center()
        pack()
    }

    private fun init() {
        buttonUp = Button(Assets.buttonUpDrawable, Assets.buttonUpPressedDrawable)
        buttonUp!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.up()
            }
        })

        buttonDown = Button(Assets.buttonDownDrawable, Assets.buttonDownPressedDrawable)
        buttonDown!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.down()
            }
        })

        buttonLeft = Button(Assets.buttonLeftDrawable, Assets.buttonLeftPressedDrawable)
        buttonLeft!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.left()
            }
        })

        buttonRight = Button(Assets.buttonRightDrawable, Assets.buttonRightPressedDrawable)
        buttonRight!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.right()
            }
        })
    }
}
