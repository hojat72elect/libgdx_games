package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.slamthebird.Assets

class LabelCoins(x: Float, y: Float, private var numCoins: Int) : Actor() {
    init {
        this.setPosition(x, y)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        drawScoreRightAligned(batch, this.x, this.y, numCoins)
    }

    private fun drawScoreRightAligned(batch: Batch, x: Float, y: Float, numCoins: Int) {
        val score = numCoins.toString()

        val length = score.length
        var charWidth: Float
        var textWidth = 0f
        for (i in length - 1 downTo 0) {
            val keyFrame: AtlasRegion

            charWidth = 22f
            val character = score[i]

            when (character) {
                '0' -> {
                    keyFrame = Assets.smallNum0!!
                }

                '1' -> {
                    keyFrame = Assets.smallNum1!!
                    charWidth = 11f
                }

                '2' -> {
                    keyFrame = Assets.smallNum2!!
                }

                '3' -> {
                    keyFrame = Assets.smallNum3!!
                }

                '4' -> {
                    keyFrame = Assets.smallNum4!!
                }

                '5' -> {
                    keyFrame = Assets.smallNum5!!
                }

                '6' -> {
                    keyFrame = Assets.smallNum6!!
                }

                '7' -> {
                    keyFrame = Assets.smallNum7!!
                }

                '8' -> {
                    keyFrame = Assets.smallNum8!!
                }

                else -> {
                    keyFrame = Assets.smallNum9!!
                }
            }
            textWidth += charWidth
            batch.draw(keyFrame, x - textWidth, y, charWidth, 32f)
        }
    }
}
