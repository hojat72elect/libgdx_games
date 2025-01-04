package dev.lonami.klooni.interfaces

import com.badlogic.gdx.math.Vector2
import dev.lonami.klooni.game.Cell

/**
 * This interface should be implemented for each effect.
 * It tells the name and the price of the effect and will create it, when needed.
 */
interface IEffectFactory {
    val name: String

    val display: String

    val price: Int

    fun create(deadCell: Cell, culprit: Vector2): IEffect
}
