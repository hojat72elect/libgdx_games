package dev.lonami.klooni.effects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import dev.lonami.klooni.game.Cell
import dev.lonami.klooni.game.Cell.Companion.draw
import dev.lonami.klooni.interfaces.Effect
import dev.lonami.klooni.interfaces.EffectFactory
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class VanishEffectFactory : EffectFactory {
    override val name = "vanish"
    override val display = "Vanish"
    override val price = 0

    override fun create(deadCell: Cell, culprit: Vector2): Effect {
        val effect: Effect = VanishEffect()
        effect.setInfo(deadCell, culprit)
        return effect
    }

    private class VanishEffect : Effect {
        private var cell: Cell? = null
        private var vanishColor: Color? = null
        private var vanishSize = 0f
        private var vanishElapsed = Float.Companion.POSITIVE_INFINITY
        private var vanishLifetime = 0f

        override fun setInfo(deadCell: Cell, culprit: Vector2) {
            cell = deadCell

            vanishSize = cell!!.size
            vanishColor = cell!!.colorCopy
            vanishLifetime = 1f

            // The vanish distance is this measure (distance² + size³ * 20% size)
            // because it seems good enough. The more the distance, the more the
            // delay, but we decrease the delay depending on the cell size too or
            // it would be way too high
            val center = Vector2(cell!!.pos.x + cell!!.size * 0.5f, cell!!.pos.y + 0.5f)
            val vanishDist = Vector2.dst2(
                culprit.x, culprit.y, center.x, center.y
            ) / (cell!!.size.toDouble().pow(4.0).toFloat() * 0.2f)

            // Negative time = delay, + 0.4*lifetime because elastic interpolation has that delay
            vanishElapsed = vanishLifetime * 0.4f - vanishDist
        }

        override fun draw(batch: Batch) {
            vanishElapsed += Gdx.graphics.deltaTime

            // vanishElapsed might be < 0 (delay), so clamp to 0
            val progress = min(
                1f,
                max(vanishElapsed, 0f) / vanishLifetime
            )

            // If one were to plot the elasticIn function, they would see that the slope increases
            // a lot towards the end- a linear interpolation between the last size + the desired
            // size at 20% seems to look a lot better.
            vanishSize = MathUtils.lerp(
                vanishSize,
                Interpolation.elasticIn.apply(cell!!.size, 0f, progress),
                0.2f
            )

            val centerOffset = cell!!.size * 0.5f - vanishSize * 0.5f
            draw(vanishColor, batch, cell!!.pos.x + centerOffset, cell!!.pos.y + centerOffset, vanishSize)
        }

        override val isDone: Boolean
            get() = vanishSize < MINIMUM_SIZE

        companion object {
            private const val MINIMUM_SIZE = 0.3f
        }
    }
}
