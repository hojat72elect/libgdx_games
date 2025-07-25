package dev.lonami.klooni.effects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import dev.lonami.klooni.game.Cell
import dev.lonami.klooni.game.Cell.Companion.draw
import dev.lonami.klooni.interfaces.Effect
import dev.lonami.klooni.interfaces.EffectFactory

class SpinEffectFactory : EffectFactory {
    override val name = "spin"

    override val display = "Spin"

    override val price = 200

    override fun create(deadCell: Cell, culprit: Vector2): Effect {
        val effect: Effect = SpinEffect()
        effect.setInfo(deadCell, culprit)
        return effect
    }


    private class SpinEffect : Effect {
        private var age = 0f
        private var pos: Vector2? = null
        private var size = 0f
        private var color: Color? = null

        override fun setInfo(deadCell: Cell, culprit: Vector2) {
            age = 0f
            pos = deadCell.pos.cpy()
            size = deadCell.size
            color = deadCell.colorCopy
        }

        override fun draw(batch: Batch) {
            age += Gdx.graphics.deltaTime

            val progress = age * INV_LIFETIME
            val currentSize = Interpolation.pow2In.apply(size, 0f, progress)
            val currentRotation = Interpolation.sine.apply(0f, TOTAL_ROTATION, progress)

            val original = batch.transformMatrix.cpy()
            val rotated = batch.transformMatrix

            val disp =
                (0.5f * (size - currentSize) // the smaller, the more we need to "push" to center
                        + currentSize * 0.5f) // center the cell for rotation

            rotated.translate(pos!!.x + disp, pos!!.y + disp, 0f)
            rotated.rotate(0f, 0f, 1f, currentRotation)
            rotated.translate(currentSize * -0.5f, currentSize * -0.5f, 0f) // revert centering for rotation

            batch.transformMatrix = rotated
            draw(color, batch, 0f, 0f, currentSize)
            batch.transformMatrix = original
        }

        override val isDone: Boolean
            get() = age > LIFETIME

        companion object {
            private const val LIFETIME = 2.0f
            private const val INV_LIFETIME = 1.0f / LIFETIME
            private const val TOTAL_ROTATION = 600f
        }
    }
}
