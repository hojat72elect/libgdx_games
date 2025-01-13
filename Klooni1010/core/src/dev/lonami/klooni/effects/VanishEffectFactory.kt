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
    override fun create(deadCell: Cell, culprit: Vector2): Effect = VanishEffect().apply {
        setInfo(deadCell, culprit)
    }

    private class VanishEffect : Effect {
        private lateinit var cell: Cell
        private var vanishColor: Color? = null
        private var vanishSize = 0F
        private var vanishElapsed = Float.POSITIVE_INFINITY
        private var vanishLifetime = 0F

        override fun setInfo(deadCell: Cell, culprit: Vector2) {
            cell = deadCell

            vanishSize = cell.cellSize
            vanishColor = cell.getColorCopy()
            vanishLifetime = 1f

            // The vanish distance is this measure (distance² + size³ * 20% size)
            // because it seems good enough. The more the distance, the more the
            // delay, but we decrease the delay depending on the cell size too or
            // it would be way too high
            val center = Vector2(cell.position.x + cell.cellSize * 0.5f, cell.position.y + 0.5f)
            val vanishDist = Vector2.dst2(
                culprit.x, culprit.y, center.x, center.y
            ) / (cell.cellSize.pow(4F) * 0.2F)

            // Negative time = delay, + 0.4*lifetime because elastic interpolation has that delay
            vanishElapsed = vanishLifetime * 0.4f - vanishDist
        }

        override fun draw(batch: Batch) {
            vanishElapsed += Gdx.graphics.deltaTime

            // vanishElapsed might be < 0 (delay), so clamp to 0
            val progress = min(1F, (max(vanishElapsed, 0F) / vanishLifetime))

            // If one were to plot the elasticIn function, they would see that the slope increases
            // a lot towards the end- a linear interpolation between the last size + the desired
            // size at 20% seems to look a lot better.
            vanishSize = MathUtils.lerp(
                vanishSize,
                Interpolation.elasticIn.apply(cell.cellSize, 0f, progress),
                0.2f
            )

            val centerOffset = cell.cellSize * 0.5f - vanishSize * 0.5f
            draw(vanishColor!!, batch, cell.position.x + centerOffset, cell.position.y + centerOffset, vanishSize)
        }

        override val isDone: Boolean
            get() = vanishSize < MINIMUM_SIZE

        companion object {
            private const val MINIMUM_SIZE = 0.3f
        }
    }
}
