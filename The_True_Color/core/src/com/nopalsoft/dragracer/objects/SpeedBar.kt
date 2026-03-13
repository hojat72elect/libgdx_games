package com.nopalsoft.dragracer.objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.dragracer.Assets

class SpeedBar(maxLife: Float, x: Float, y: Float, width: Float, height: Float) : Actor() {
    private var maxLife: Float
    private var actualLife: Float

    init {
        this.setBounds(x, y, width, height)
        this.maxLife = maxLife
        this.actualLife = maxLife
    }

    fun updateLifeBar(newLifeLevel: Float) {
        var actualLife = newLifeLevel
        if (actualLife >= maxLife) {
            actualLife = maxLife
        }
        this.actualLife = actualLife
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(
            Assets.redMarkerBar!!, this.x, this.y,
            this.width, this.height
        )
        if (actualLife > 0) batch.draw(
            Assets.greenMarkerBar!!, this.x, this.y,
            this.width * (actualLife / maxLife), this.height
        )
    }
}
