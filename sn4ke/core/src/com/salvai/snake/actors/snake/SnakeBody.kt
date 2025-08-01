package com.salvai.snake.actors.snake

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.salvai.snake.actors.MovingGameObject
import com.salvai.snake.enums.MovingDirection
import com.salvai.snake.utils.WorldUtils

class SnakeBody(worldPosition: Vector2?, texture: Texture?, direction: MovingDirection, color: Color?, worldUtils: WorldUtils) :
    MovingGameObject(worldPosition, texture, direction, worldUtils, false) {
    init {
        setColor(color)
    }
}
