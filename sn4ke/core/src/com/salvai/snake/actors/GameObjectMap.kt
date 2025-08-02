package com.salvai.snake.actors

import com.badlogic.gdx.math.Vector2
import com.salvai.snake.actors.snake.Snake
import com.salvai.snake.utils.Constants
import com.salvai.snake.utils.WorldUtils
import com.badlogic.gdx.utils.Array as GdxArray

class GameObjectMap(blocks: GdxArray<Block>, worldUtils: WorldUtils) {
    //because world starts from 0 to screenwidth/height
    private val map = Array<IntArray?>(worldUtils.worldWidth - Constants.PLAY_HEIGHT_FACTOR_X) {
        IntArray(worldUtils.playableWorldHeigth - 1)
    }
    private val freePositions = GdxArray<Vector2?>()

    init {
        for (block in blocks) map[block.worldPosition.x.toInt() - 1]!![block.worldPosition.y.toInt() - 1] = 1
    }

    fun getFreePositions(snake: Snake, apple: Apple?): Vector2? {
        map[snake.snakeHead.worldPosition.x.toInt() - 1]!![snake.snakeHead.worldPosition.y.toInt() - 1] = 2

        for (snakeBody in snake.snakeBodies) map[snakeBody.worldPosition.x.toInt() - 1]!![snakeBody.worldPosition.y.toInt() - 1] = 2

        if (apple != null) map[apple.worldPosition.x.toInt() - 1]!![apple.worldPosition.y.toInt() - 1] = 2
        return this.freePosition
    }

    private val freePosition: Vector2?
        get() {
            freePositions.clear()
            for (i in map.indices) for (j in map[i]!!.indices) if (map[i]!![j] == 0) freePositions.add(Vector2((i + 1).toFloat(), (j + 1).toFloat())) //+1 because of border
            else if (map[i]!![j] == 2) map[i]!![j] = 0
            return freePositions.random()
        }
}
