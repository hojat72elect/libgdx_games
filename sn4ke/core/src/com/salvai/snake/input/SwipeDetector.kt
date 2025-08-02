package com.salvai.snake.input

import com.salvai.snake.enums.MovingDirection
import com.salvai.snake.utils.Constants
import kotlin.math.abs

object SwipeDetector {
    @JvmStatic
    fun onSwipe(velocityX: Int, velocityY: Int): MovingDirection? {
        if (abs(velocityX) > abs(velocityY) && abs(velocityX) > Constants.SWIPE_FACTOR) return if (velocityX > 0) MovingDirection.RIGHT
        else MovingDirection.LEFT
        else if (abs(velocityY) > Constants.SWIPE_FACTOR) return if (velocityY > 0) MovingDirection.DOWN
        else MovingDirection.UP
        return null
    }
}