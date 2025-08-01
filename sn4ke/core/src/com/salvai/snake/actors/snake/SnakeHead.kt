package com.salvai.snake.actors.snake

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.salvai.snake.actors.MovingGameObject
import com.salvai.snake.enums.MovingDirection
import com.salvai.snake.utils.WorldUtils

class SnakeHead : MovingGameObject {
    internal constructor(baseTexture: Texture?, color: Color?, worldUtils: WorldUtils) : super(
        Vector2(worldUtils.worldWidthCenter.toFloat(), worldUtils.worldHeightCenter.toFloat()),
        baseTexture,
        MovingDirection.NONE,
        worldUtils,
        true
    ) //using baseTexture as normal one
    {
        setColor(color)
    }

    internal constructor(baseTexture: Texture?, startX: Int, startY: Int, color: Color?, worldUtils: WorldUtils) : super(
        Vector2(startX.toFloat(), startY.toFloat()),
        baseTexture,
        MovingDirection.NONE,
        worldUtils,
        true
    ) //using baseTexture as normal one
    {
        setColor(color)
    }
}
