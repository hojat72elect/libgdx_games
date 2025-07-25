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

class EvaporateEffectFactory : EffectFactory {
    override val name: String
        get() = "evaporate"

    override val display: String
        get() = "Evaporate"

    override val price: Int
        get() = 200

    override fun create(deadCell: Cell, culprit: Vector2): Effect {
        val effect: Effect = EvaporateEffect()
        effect.setInfo(deadCell, culprit)
        return effect
    }


    private inner class EvaporateEffect : Effect {
        private var pos: Vector2? = null
        private var originalX = 0f
        private var size = 0f
        private var vanishColor: Color? = null
        private var vanishSize = 0f
        private var vanishElapsed: Float
        private var driftMagnitude = 0f
        private var randomOffset = 0f

        init {
            vanishElapsed = Float.Companion.POSITIVE_INFINITY
        }

        override fun setInfo(deadCell: Cell, culprit: Vector2) {
            pos = deadCell.pos.cpy()
            originalX = pos!!.x
            size = deadCell.size

            vanishSize = deadCell.size
            vanishColor = deadCell.colorCopy
            driftMagnitude = Gdx.graphics.width * 0.05f
            vanishElapsed = 0f
            randomOffset = MathUtils.random(MathUtils.PI2)
        }

        override fun draw(batch: Batch) {
            vanishElapsed += Gdx.graphics.deltaTime

            // Update the size as we fade away
            val progress = vanishElapsed * INV_LIFETIME
            vanishSize = Interpolation.fade.apply(size, 0f, progress)

            // Fade away depending on the time
            vanishColor!!.set(vanishColor!!.r, vanishColor!!.g, vanishColor!!.b, 1.0f - progress)

            // Ghostly fade upwards, by doing a lerp from our current position to the wavy one
            pos!!.x = MathUtils.lerp(
                pos!!.x,
                originalX + MathUtils.sin(randomOffset + vanishElapsed * 3f) * driftMagnitude,
                0.3f
            )
            pos!!.y += UP_SPEED * Gdx.graphics.deltaTime

            draw(vanishColor, batch, pos!!.x, pos!!.y, vanishSize)
        }

        override val isDone: Boolean
            get() = vanishElapsed > LIFETIME


    }

    companion object {
        private const val UP_SPEED = 100.0f
        private const val LIFETIME = 3.0f
        private const val INV_LIFETIME = 1.0f / 3.0f
    }
}
