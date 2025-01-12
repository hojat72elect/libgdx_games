package dev.lonami.klooni.interfaces

import com.badlogic.gdx.math.Vector2
import dev.lonami.klooni.game.Cell

/**
 * The factory that creates each one of the effects should comply to this interface.
 * Contains the name, price, and visual display of the Effect. And also dictates how to create the related effect.
 */
interface EffectFactory {
    val name: String

    val display: String

    val price: Int

    fun create(deadCell: Cell, culprit: Vector2): Effect
}
