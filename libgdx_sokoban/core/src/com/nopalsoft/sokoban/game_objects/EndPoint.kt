package com.nopalsoft.sokoban.game_objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.nopalsoft.sokoban.Assets

class EndPoint(position: Int, color: String) : Tile(position) {
    val numColors = when (color) {
        "brown" -> Box.COLOR_BROWN
        "gray" -> Box.COLOR_GRAY
        "purple" -> Box.COLOR_PURPLE
        "blue" -> Box.COLOR_BLUE
        "black" -> Box.COLOR_BLACK
        "beige" -> Box.COLOR_BEIGE
        "yellow" -> Box.COLOR_YELLOW
        "red" -> Box.COLOR_RED
        else -> {
            Box.COLOR_BROWN
        }
    }
    private var keyFrame: AtlasRegion? = null

    init {
        setTextureColor(numColors)
    }

    private fun setTextureColor(numColor: Int) {
        keyFrame = when (numColor) {
            Box.COLOR_BEIGE -> Assets.endPointBeige
            Box.COLOR_BLACK -> Assets.endPointBlack
            Box.COLOR_BLUE -> Assets.endPointBlue
            Box.COLOR_BROWN -> Assets.endPointBrown
            Box.COLOR_GRAY -> Assets.endPointGray
            Box.COLOR_RED -> Assets.endPointRed
            Box.COLOR_YELLOW -> Assets.endPointYellow
            Box.COLOR_PURPLE -> Assets.endPointPurple
            else -> {
                Assets.endPointBrown
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(keyFrame, x, y, size, size)
    }
}