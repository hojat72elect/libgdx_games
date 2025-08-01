package com.salvai.snake.utils

import com.badlogic.gdx.graphics.Color

object Colors {
    val SNAKE_BODY: Array<Color?> = arrayOf(
        getColor(38, 222, 129),  //green nephritis
        getColor(249, 168, 37),  // yellow
        getColor(2, 119, 189),  //blue
        getColor(216, 67, 21),  //red
    )
    private val SNAKE_HEAD = arrayOf(
        getColor(32, 191, 107),  //green emerald
        getColor(245, 127, 23),  //yellow
        getColor(1, 87, 155),  //blue
        getColor(191, 54, 12),  //orange
    )


    @JvmStatic
    fun getHeadColor(index: Int): Color {
        return SNAKE_HEAD[index]
    }

    @JvmStatic
    fun getBodyColor(index: Int): Color? {
        return SNAKE_BODY[index]
    }

    @JvmStatic
    fun getBackgroundBlockColor(index: Int): Color? {
        return SNAKE_BODY[(index) % SNAKE_BODY.size]
    }


    //converts value for libgdx color format
    private fun getColor(r: Int, g: Int, b: Int): Color {
        return Color(r / 255f, g / 255f, b / 255f, 1f)
    }

    @JvmStatic
    fun getBackgroundColor(index: Int): Color? {
        return SNAKE_BODY[(index + 1) % SNAKE_BODY.size]
    }
}
