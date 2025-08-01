package com.salvai.snake.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.salvai.snake.utils.WorldUtils

abstract class GameObject(worldPosition: Vector2, texture: Texture, worldUtils: WorldUtils) : Image(texture) {
    @JvmField
    var worldPosition: Vector2?
    var screenPosition: Vector2?

    @JvmField
    var worldUtils: WorldUtils?

    init {
        this.worldUtils = worldUtils
        this.worldPosition = worldPosition
        screenPosition = worldUtils.worldToScreen(worldPosition)
        setBounds(screenPosition!!.x, screenPosition!!.y, worldUtils.blockSize.toFloat(), worldUtils.blockSize.toFloat())
    }
}


