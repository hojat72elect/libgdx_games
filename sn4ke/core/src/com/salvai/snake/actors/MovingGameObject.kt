package com.salvai.snake.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.salvai.snake.enums.MovingDirection
import com.salvai.snake.utils.Constants
import com.salvai.snake.utils.WorldUtils

abstract class MovingGameObject(
    worldPosition: Vector2?, texture: Texture?, @JvmField var direction: MovingDirection, worldUtils: WorldUtils, // to wait before first move
    private var move: Boolean
) : GameObject(worldPosition!!, texture!!, worldUtils) {
    fun setDirection(direction: MovingDirection) {
        when (direction) {
            MovingDirection.UP -> if (this.direction != MovingDirection.DOWN) this.direction = direction
            MovingDirection.DOWN -> if (this.direction != MovingDirection.UP) this.direction = direction
            MovingDirection.LEFT -> if (this.direction != MovingDirection.RIGHT) this.direction = direction
            MovingDirection.RIGHT -> if (this.direction != MovingDirection.LEFT) this.direction = direction
            else -> this.direction = direction
        }
    }

    fun setDirectionWhitoutCheck(direction: MovingDirection) {
        this.direction = direction
    }

    fun move(screenBlockSize: Int) {
        if (move) when (direction) {
            MovingDirection.UP -> moveUp(screenBlockSize)
            MovingDirection.DOWN -> moveDown(screenBlockSize)
            MovingDirection.LEFT -> moveLeft(screenBlockSize)
            MovingDirection.RIGHT -> moveRight(screenBlockSize)
            else -> {}
        }
        else move = true
        setScreenPosition()
    }

    private fun moveUp(screenBlockSize: Int) {
        addAction(Actions.moveBy(0f, screenBlockSize.toFloat(), Constants.SNAKE_DURATION, Constants.SNAKE_INTERPOLATION))
    }

    private fun moveDown(screenBlockSize: Int) {
        addAction(Actions.moveBy(0f, -screenBlockSize.toFloat(), Constants.SNAKE_DURATION, Constants.SNAKE_INTERPOLATION))
    }

    private fun moveLeft(screenBlockSize: Int) {
        addAction(Actions.moveBy(-screenBlockSize.toFloat(), 0f, Constants.SNAKE_DURATION, Constants.SNAKE_INTERPOLATION))
    }

    private fun moveRight(screenBlockSize: Int) {
        addAction(Actions.moveBy(screenBlockSize.toFloat(), 0f, Constants.SNAKE_DURATION, Constants.SNAKE_INTERPOLATION))
    }

    fun moveWorldPosition() {
        if (move) when (direction) {
            MovingDirection.UP -> moveUpWorldPosition()
            MovingDirection.DOWN -> moveDownWorldPosition()
            MovingDirection.LEFT -> moveLeftWorldPosition()
            MovingDirection.RIGHT -> moveRightWorldPosition()
            else -> {}
        }
        setScreenPosition()
    }

    private fun moveUpWorldPosition() {
        worldPosition.y += Constants.WORLD_BLOCK_SIZE.toFloat()
    }

    private fun moveDownWorldPosition() {
        worldPosition.y -= Constants.WORLD_BLOCK_SIZE.toFloat()
    }

    private fun moveLeftWorldPosition() {
        worldPosition.x -= Constants.WORLD_BLOCK_SIZE.toFloat()
    }

    private fun moveRightWorldPosition() {
        worldPosition.x += Constants.WORLD_BLOCK_SIZE.toFloat()
    }

    fun setScreenPosition() {
        screenPosition = worldUtils.worldToScreen(worldPosition)
    }
}
