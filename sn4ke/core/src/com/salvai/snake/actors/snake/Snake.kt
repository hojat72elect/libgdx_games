package com.salvai.snake.actors.snake

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.salvai.snake.actors.Apple
import com.salvai.snake.actors.Block
import com.salvai.snake.enums.MovingDirection
import com.salvai.snake.utils.Colors.getBodyColor
import com.salvai.snake.utils.Colors.getHeadColor
import com.salvai.snake.utils.WorldUtils

class Snake {
    private val headColor: Color
    private val worldUtils: WorldUtils

    @JvmField
    var snakeHead: SnakeHead
    var snakeBodies: Array<SnakeBody>
    var addBody: Boolean
    var texture: Texture?
    var bodyColor: Color?

    constructor(texture: Texture?, styleIndex: Int, worldUtils: WorldUtils) {
        this.texture = texture
        this.worldUtils = worldUtils

        headColor = getHeadColor(styleIndex)
        bodyColor = getBodyColor(styleIndex)

        snakeHead = SnakeHead(texture, headColor, worldUtils)
        snakeBodies = Array<SnakeBody>()
        addBody = false
        setDirection(MovingDirection.NONE)
    }

    constructor(texture: Texture?, startX: Int, startY: Int, styleIndex: Int, worldUtils: WorldUtils) {
        this.texture = texture
        this.worldUtils = worldUtils

        headColor = getHeadColor(styleIndex)
        bodyColor = getBodyColor(styleIndex)

        snakeHead = SnakeHead(texture, startX, startY, headColor, worldUtils)
        snakeBodies = Array<SnakeBody>()
        addBody = false
        setDirection(MovingDirection.NONE)
    }

    fun checkGameOver(blocks: Array<Block>): Boolean {
        for (i in 3..<snakeBodies.size) if (snakeHead.worldPosition == snakeBodies.get(i).worldPosition) return true
        if (snakeHead.worldPosition!!.x < 1 || snakeHead.worldPosition!!.y < 1 || snakeHead.worldPosition!!.x > snakeHead.worldUtils!!.worldWidth - 1 || snakeHead.worldPosition!!.y > snakeHead.worldUtils!!.playableWorldHeigth - 1) return true
        for (block in blocks) if (snakeHead.worldPosition == block.worldPosition) return true
        return false
    }


    fun setDirection(direction: MovingDirection) {
        snakeHead.setDirection(direction)
    }

    fun updateBodyAndTailDirections() {
        for (i in snakeBodies.size - 2 downTo 0) snakeBodies.get(i + 1).setDirectionWhitoutCheck(snakeBodies.get(i).direction)
        if (snakeBodies.size > 0) snakeBodies.first().setDirectionWhitoutCheck(snakeHead.direction)
    }

    fun move(screenBlockSize: Int) {
        snakeHead.move(screenBlockSize)
        for (snakeBody in snakeBodies) snakeBody.move(screenBlockSize)
    }

    fun moveWorldPosition() {
        snakeHead.moveWorldPosition()
        for (snakeBody in snakeBodies) snakeBody.moveWorldPosition()
    }


    fun addBody(): SnakeBody {
        val newBody: SnakeBody
        if (snakeBodies.size > 0) {
            newBody = SnakeBody(Vector2(snakeBodies.peek().worldPosition), texture, snakeBodies.peek().direction, bodyColor, worldUtils)
        } else newBody = SnakeBody(Vector2(snakeHead.worldPosition), texture, snakeHead.direction, bodyColor, worldUtils)
        snakeBodies.add(newBody)
        addBody = false
        return newBody
    }

    fun eats(apple: Apple): Boolean {
        if (snakeHead.worldPosition == apple.worldPosition) {
            addBody = true
            return true
        }
        return false
    }

    fun hide() {
        snakeHead.isVisible = false
        for (snakeBody in snakeBodies) snakeBody.isVisible = false
    }

    fun show() {
        snakeHead.isVisible = true
        for (snakeBody in snakeBodies) snakeBody.isVisible = true
    }
}


