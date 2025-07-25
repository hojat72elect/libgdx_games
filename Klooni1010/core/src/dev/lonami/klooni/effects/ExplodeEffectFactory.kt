package dev.lonami.klooni.effects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import dev.lonami.klooni.game.Cell
import dev.lonami.klooni.game.Cell.Companion.draw
import dev.lonami.klooni.interfaces.Effect
import dev.lonami.klooni.interfaces.EffectFactory

class ExplodeEffectFactory : EffectFactory {

    override val name = "explode"
    override val display = "Explode"
    override val price = 200

    override fun create(deadCell: Cell, culprit: Vector2): Effect {
        val effect: Effect = ExplodeEffect()
        effect.setInfo(deadCell, culprit)
        return effect
    }


    private class ExplodeEffect : Effect {
        override var isDone: Boolean = false
        private var color: Color? = null
        private lateinit var shards: Array<Shard?>

        override fun setInfo(deadCell: Cell, culprit: Vector2) {
            color = deadCell.colorCopy

            shards = arrayOfNulls<Shard>(MathUtils.random(4, 6))
            for (i in shards.indices) shards[i] = Shard(deadCell.pos, deadCell.size)
        }

        override fun draw(batch: Batch) {
            this.isDone = true // assume we're death
            val translation = batch.transformMatrix.getTranslation(Vector3())
            var i = shards.size
            while (i-- != 0) {
                shards[i]!!.draw(batch, Gdx.graphics.deltaTime)
                this.isDone = this.isDone and (translation.y + shards[i]!!.pos.y + shards[i]!!.size < 0) // all must be dead
            }
        }

        private inner class Shard(pos: Vector2, size: Float) {
            val pos: Vector2
            val vel: Vector2
            val acc: Vector2
            val size: Float

            init {
                val xRange = Gdx.graphics.width * EXPLOSION_X_RANGE
                val yRange = Gdx.graphics.height * EXPLOSION_Y_RANGE
                vel = Vector2(MathUtils.random(-xRange, +xRange), MathUtils.random(-yRange * 0.2f, +yRange))
                acc = Vector2(0f, Gdx.graphics.height * GRAVITY_PERCENTAGE)

                this.size = size * MathUtils.random(0.40f, 0.60f)
                this.pos = pos.cpy().add(this.size * 0.5f, this.size * 0.5f)
            }

            fun draw(batch: Batch, dt: Float) {
                vel.add(acc.x * dt, acc.y * dt).scl(0.99f)
                pos.add(vel.x * dt, vel.y * dt)
                draw(color, batch, pos.x, pos.y, size)
            }
        }

        companion object {
            private const val EXPLOSION_X_RANGE = 0.25f
            private const val EXPLOSION_Y_RANGE = 0.30f
            private const val GRAVITY_PERCENTAGE = -0.60f
        }
    }
}
