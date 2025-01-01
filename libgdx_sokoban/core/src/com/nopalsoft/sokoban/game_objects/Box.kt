package com.nopalsoft.sokoban.game_objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.nopalsoft.sokoban.Assets
import kotlin.math.abs

class Box(position: Int, color: String) : Tile(position) {

    var isInRightEndPoint = false
    private var numColor = 0
    private var keyFrame: AtlasRegion? = null

    init {
        numColor = when (color) {
            "brown" -> COLOR_BROWN
            "gray" -> COLOR_GRAY
            "purple" -> COLOR_PURPLE
            "blue" -> COLOR_BLUE
            "black" -> COLOR_BLACK
            "beige" -> COLOR_BEIGE
            "yellow" -> COLOR_YELLOW
            "red" -> COLOR_RED
            else -> {
                // I already know this will never happen
                COLOR_BROWN
            }
        }
        setTextureColor(numColor)
    }

    private fun setTextureColor(numColor: Int) {
        keyFrame = when (numColor) {
            COLOR_BEIGE -> Assets.boxBeige
            COLOR_DARK_BEIGE -> Assets.boxDarkBeige
            COLOR_BLACK -> Assets.boxBlack
            COLOR_DARK_BLACK -> Assets.boxDarkBlack
            COLOR_BLUE -> Assets.boxBlue
            COLOR_DARK_BLUE -> Assets.boxDarkBlue
            COLOR_BROWN -> Assets.boxBrown
            COLOR_DARK_BROWN -> Assets.boxDarkBrown
            COLOR_GRAY -> Assets.boxGray
            COLOR_DARK_GRAY -> Assets.boxDarkGray
            COLOR_RED -> Assets.boxRed
            COLOR_DARK_RED -> Assets.boxDarkRed
            COLOR_YELLOW -> Assets.boxYellow
            COLOR_DARK_YELLOW -> Assets.boxDarkYellow
            COLOR_PURPLE -> Assets.boxPurple
            COLOR_DARK_PURPLE -> Assets.boxDarkPurple
            else -> {
                // I already know this will never happen
                Assets.boxBrown
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(keyFrame, x, y, size, size)
    }

    fun setIsInEndPoint(endPoint: EndPoint?) {
        numColor = abs(numColor)
        isInRightEndPoint = false
        if (endPoint != null && endPoint.numColors == numColor) {
            numColor = -numColor
            isInRightEndPoint = true
        }
        setTextureColor(numColor)
    }

    companion object {
        const val COLOR_BEIGE = 1
        const val COLOR_DARK_BEIGE = -1
        const val COLOR_BLACK = 2
        const val COLOR_DARK_BLACK = -2
        const val COLOR_BLUE = 3
        const val COLOR_DARK_BLUE = -3
        const val COLOR_BROWN = 4
        const val COLOR_DARK_BROWN = -4
        const val COLOR_GRAY = 5
        const val COLOR_DARK_GRAY = -5
        const val COLOR_RED = 6
        const val COLOR_DARK_RED = -6
        const val COLOR_YELLOW = 7
        const val COLOR_DARK_YELLOW = -7
        const val COLOR_PURPLE = 8
        const val COLOR_DARK_PURPLE = -8
    }
}