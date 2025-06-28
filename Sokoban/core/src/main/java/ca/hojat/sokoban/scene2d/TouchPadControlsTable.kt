package ca.hojat.sokoban.scene2d

import ca.hojat.sokoban.Assets
import ca.hojat.sokoban.game.GameScreen
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class TouchPadControlsTable(var gameScreen: GameScreen) : Table() {
    private var buttonUp: Button? = null
    private var buttonDown: Button? = null
    private var buttonLeft: Button? = null
    private var buttonRight: Button? = null

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
