package com.salvai.snake.input

import com.salvai.snake.enums.MovingDirection
import com.salvai.snake.utils.Constants
import kotlin.math.abs

object SwipeDetector {
    @JvmStatic
    fun onSwipe(velocityX: Int, velocityY: Int): MovingDirection? {
        if (abs(velocityX) > abs(velocityY) && abs(velocityX) > Constants.SWIPE_FACTOR) if (velocityX > 0) return MovingDirection.RIGHT
        else return MovingDirection.LEFT
        else if (abs(velocityY) > Constants.SWIPE_FACTOR) if (velocityY > 0) return MovingDirection.DOWN
        else return MovingDirection.UP
        return null
    }
}