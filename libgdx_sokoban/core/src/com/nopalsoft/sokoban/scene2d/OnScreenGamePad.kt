package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.game.GameScreen

/**
 * The game-pad on screen of the game.  It allows mobile phone users to control the player.
 */
class OnScreenGamePad(val screen: GameScreen) : Table() {

    private val buttonUp = Button(Assets.btUp, Assets.btUpPress)
    private val buttonDown = Button(Assets.btDown, Assets.btDownPress)
    private val buttonLeft = Button(Assets.buttonIzq, Assets.btIzqPress)
    private val buttonRight = Button(Assets.buttonDer, Assets.buttonDerPress)

    init {
        color.a = 0.4F
        buttonUp.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.up()
            }
        })
        buttonDown.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.down()
            }
        })
        buttonLeft.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.left()
            }
        })
        buttonRight.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.right()
            }
        })

        val buttonSize = 75F
        defaults().size(buttonSize)

        add(buttonUp).colspan(2).center()
        row()
        add(buttonLeft).left()
        add(buttonRight).right().padLeft(buttonSize / 1.15F)
        row()
        add(buttonDown).colspan(2).center()
        pack()
    }
}