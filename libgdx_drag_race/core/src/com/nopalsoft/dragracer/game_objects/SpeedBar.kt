package com.nopalsoft.dragracer.game_objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.dragracer.Assets

class SpeedBar(private val maxLife: Float, x: Float, y: Float, width: Float, height: Float) : Actor() {

    private var actualLife = maxLife

    init {
        setBounds(x, y, width, height)
    }

    fun updateActualLife(actualLife: Float) {
        var actualLife = actualLife
        if (actualLife >= maxLife) {
            actualLife = maxLife
        }
        this.actualLife = actualLife
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(
            Assets.barMarkedRed, x, y, width, height
        )
        if (actualLife > 0) batch.draw(
            Assets.barMarkedGreen, x, y,
            width * (actualLife / maxLife), height
        )
    }
}