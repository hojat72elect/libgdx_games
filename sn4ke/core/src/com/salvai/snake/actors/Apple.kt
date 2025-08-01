package com.salvai.snake.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.salvai.snake.utils.Colors.getBodyColor
import com.salvai.snake.utils.Constants
import com.salvai.snake.utils.WorldUtils

class Apple(worldPosition: Vector2?, texture: Texture?, worldUtils: WorldUtils, index: Int) : GameObject(worldPosition!!, texture!!, worldUtils) {
    init {
        setOrigin(getWidth() * 0.5f, getHeight() * 0.5f)
        setColor(getBodyColor(index))
        addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.scaleBy(Constants.SCALE, Constants.SCALE, Constants.DURATION),
                    Actions.delay(0.2f),
                    Actions.scaleBy(-Constants.SCALE, -Constants.SCALE, Constants.DURATION)
                )
            )
        )
    }

    fun reset(worldPosition: Vector2?) {
        this.worldPosition = worldPosition
        screenPosition = worldUtils!!.worldToScreen(worldPosition!!)
        addAction(Actions.sequence(Actions.fadeOut(Constants.DURATION * 0.5f), Actions.run(object : Runnable {
            override fun run() {
                setPosition(screenPosition!!.x, screenPosition!!.y)
            }
        }), Actions.delay(0.2f), Actions.fadeIn(Constants.DURATION)))
    }
}
