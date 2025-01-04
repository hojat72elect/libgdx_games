package dev.lonami.klooni.interfaces

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import dev.lonami.klooni.game.Cell

interface IEffect {
    fun setInfo(deadCell: Cell, culprit: Vector2)

    fun draw(batch: Batch?)

    val isDone: Boolean
}
