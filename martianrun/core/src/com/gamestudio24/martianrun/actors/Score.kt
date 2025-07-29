package com.gamestudio24.martianrun.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.AssetsManager.getSmallFont
import com.gamestudio24.martianrun.utils.GameManager
import kotlin.math.floor

class Score(private val bounds: Rectangle) : Actor() {
    private val font: BitmapFont
    private var score: Float
    private var multiplier: Int

    init {
        setWidth(bounds.width)
        setHeight(bounds.height)
        score = 0f
        multiplier = 5
        font = getSmallFont()
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (GameManager.instance.gameState != GameState.RUNNING) {
            return
        }
        score += multiplier * delta
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        if (getScore() == 0) {
            return
        }
        font.drawWrapped(batch, String.format("%d", getScore()), bounds.x, bounds.y, bounds.width, BitmapFont.HAlignment.RIGHT)
    }

    fun getScore(): Int {
        return floor(score.toDouble()).toInt()
    }

    fun setMultiplier(multiplier: Int) {
        this.multiplier = multiplier
    }
}
