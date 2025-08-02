package com.salvai.snake.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.salvai.snake.utils.WorldUtils

abstract class GameObject(var worldPosition: Vector2, texture: Texture, var worldUtils: WorldUtils) : Image(texture) {

    var screenPosition = worldUtils.worldToScreen(worldPosition)

    init {
        setBounds(screenPosition.x, screenPosition.y, worldUtils.blockSize.toFloat(), worldUtils.blockSize.toFloat())
    }
}


