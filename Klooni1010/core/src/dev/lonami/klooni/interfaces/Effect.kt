package dev.lonami.klooni.interfaces

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import dev.lonami.klooni.game.Cell

/**
 * Defines each one of the visual effects for dead cells.
 * denotes how to draw it, how to register its information, and if it's finished.
 */
interface Effect {
    fun setInfo(deadCell: Cell, culprit: Vector2)

    fun draw(batch: Batch)

    val isDone: Boolean
}
