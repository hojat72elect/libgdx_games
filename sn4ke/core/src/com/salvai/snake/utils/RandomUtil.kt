package com.salvai.snake.utils

import com.badlogic.gdx.graphics.Color
import java.util.Random

object RandomUtil {
    var random: Random = Random()

    //BACKGROUND
    @JvmStatic
    fun getRandomBackgroundStarYCoordinate(height: Int): Int {
        return random.nextInt(height)
    }

    @JvmStatic
    val randomCloudSizeFactor: Int
        get() = random.nextInt(3) + 1

    @JvmStatic
    fun getRandomBackgroundStarXCoordinate(width: Int): Int {
        return random.nextInt(width)
    }

    @JvmStatic
    val randomAlpha: Float
        get() = random.nextFloat() * 0.5f

    @JvmStatic
    val randomColor: Color
        get() = Color(Colors.SNAKE_BODY[random.nextInt(Colors.SNAKE_BODY.size)])

    @JvmStatic
    val randomStarSpeed: Int
        get() = random.nextInt(Constants.MAX_BACKGROUND_SPEED - Constants.MIN_BACKGROUND_SPEED) + Constants.MIN_BACKGROUND_SPEED
}
