package dev.lonami.klooni.interfaces

import com.badlogic.gdx.math.Vector2
import dev.lonami.klooni.game.Cell

/**
 * IEffectFactory interface has to be implemented for each effect.
 *
 *
 * It tells the name and the price of the effect and will create it, when needed.
 *
 * @see Effect
 */
interface EffectFactory {

    val name: String

    val display: String

    val price: Int

    fun create(deadCell: Cell, culprit: Vector2): Effect
}
