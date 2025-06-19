package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.slamthebird.Assets

class LabelScore(x: Float, y: Float, private var score: Int) : Actor() {
    init {
        this.setPosition(x, y)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        drawLargeNumberCenteredX(batch, this.x, this.y, score)
    }

    private fun drawLargeNumberCenteredX(batcher: Batch, x: Float, y: Float, newScore: Int) {
        val score = newScore.toString()

        val len = score.length
        val charWidth = 42f
        val textWidth = len * charWidth
        for (i in 0..<len) {
            val keyFrame: AtlasRegion

            val character = score[i]

            keyFrame = when (character) {
                '0' -> {
                    Assets.largeNum0
                }

                '1' -> {
                    Assets.largeNum1
                }

                '2' -> {
                    Assets.largeNum2
                }

                '3' -> {
                    Assets.largeNum3
                }

                '4' -> {
                    Assets.largeNum4
                }

                '5' -> {
                    Assets.largeNum5
                }

                '6' -> {
                    Assets.largeNum6
                }

                '7' -> {
                    Assets.largeNum7
                }

                '8' -> {
                    Assets.largeNum8
                }

                else -> {
                    Assets.largeNum9
                }
            }
            batcher.draw(keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f, y, charWidth, 64f)
        }
    }
}
