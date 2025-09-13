package com.salvador.bricks.game_objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.salvador.bricks.game_objects.Constants.BUTTON_EXIT
import com.salvador.bricks.game_objects.Constants.BUTTON_INFO
import com.salvador.bricks.game_objects.Constants.BUTTON_PLAY
import com.salvador.bricks.game_objects.Constants.BUTTON_RESET

class MenuButton(type: Int, var positionX: Float, var positionY: Float, var w: Float, var h: Float) : Actor() {

    @JvmField
    var touch = false
    private var menuButton = when (type) {
        BUTTON_PLAY -> ResourceManager.getTexture("button.png")
        BUTTON_EXIT -> ResourceManager.getTexture("btn_exit.png")
        BUTTON_INFO -> ResourceManager.getTexture("btn_info.png")
        BUTTON_RESET -> ResourceManager.getTexture("btn_reset.png")
        else -> {
            null
        }
    }

    init {
        setBounds(positionX, positionY, w, h)

        addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                touch = true
                return true
            }

        })
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(menuButton, positionX, positionY, w, h)
    }
}
