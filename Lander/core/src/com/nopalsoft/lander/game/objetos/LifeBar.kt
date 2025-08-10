package com.nopalsoft.lander.game.objetos

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.lander.Assets

/**
 * If I use a table, the width and height are overwritten
 */
class LifeBar(var maxlife: Float) : Actor() {
    var actualLife = maxlife

    fun updateActualLife(actualLife: Float) {
        this.actualLife = actualLife

        if (actualLife > maxlife) maxlife = actualLife
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(Assets.barraMarcadorRojo, this.x, this.y, this.width, this.height)
        if (actualLife > 0) batch.draw(Assets.barraMarcadorVerde, this.x, this.y, this.width * (actualLife / maxlife), this.height)
    }
}
