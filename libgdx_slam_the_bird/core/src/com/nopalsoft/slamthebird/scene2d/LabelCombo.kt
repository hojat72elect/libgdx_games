package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.game.WorldGame

class LabelCombo(x: Float, y: Float, private var actualCombo: Int) : Actor() {
    init {
        this.setPosition(x, y)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(Assets.combo, x, y, 65f, 27f)
        if (actualCombo >= WorldGame.COMBO_TO_START_GETTING_COINS) {
            batch.draw(Assets.coinsRegion, x + 20, y + 35, 23f, 26f)
        }
        drawSmallScoreLeftOrigin(
            batch, this.x + 70, this.y + 2,
            actualCombo
        )
    }

    private fun drawSmallScoreLeftOrigin(
        batch: Batch, x: Float, y: Float,
        comboActual: Int
    ) {
        val score = comboActual.toString()

        val length = score.length
        var charWidth: Float
        var textWidth = 0f
        for (i in 0..<length) {
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

                else -> { // 9
                    keyFrame = Assets.smallNum9!!
                }
            }

            batch.draw(keyFrame, x + textWidth, y, charWidth, 22f)
            textWidth += charWidth
        }
    }
}
