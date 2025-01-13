package dev.lonami.klooni.effects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import dev.lonami.klooni.SkinLoader
import dev.lonami.klooni.game.Cell
import dev.lonami.klooni.interfaces.Effect
import dev.lonami.klooni.interfaces.EffectFactory
import kotlin.math.max
import kotlin.math.min

class WaterDropEffectFactory : EffectFactory {

    private var dropTexture: Texture? = null

    override val name = "waterdrop"
    override val display = "Waterdrop"
    override val price = 20

    override fun create(deadCell: Cell, culprit: Vector2): Effect {

        if (dropTexture == null) dropTexture = SkinLoader.loadPng("cells/drop.png")
        val effect: Effect = WaterDropEffect()
        effect.setInfo(deadCell, culprit)
        return effect
    }

    private inner class WaterDropEffect : Effect {
        private val fallAcceleration = FALL_ACCELERATION + MathUtils.random(-FALL_VARIATION, FALL_VARIATION)
        private var pos: Vector2? = null
        override var isDone: Boolean = false
        private var cellColor: Color? = null
        private var dropColor: Color? = null
        private var cellSize = 0f
        private var fallSpeed = 0f

        override fun setInfo(deadCell: Cell, culprit: Vector2) {
            pos = deadCell.position.cpy()
            cellSize = deadCell.cellSize
            cellColor = deadCell.getColorCopy()
            dropColor = Color(cellColor!!.r, cellColor!!.g, cellColor!!.b, 0.0f)
        }

        override fun draw(batch: Batch) {
            val dt = Gdx.graphics.deltaTime
            fallSpeed += fallAcceleration * dt
            pos!!.y -= fallSpeed * dt

            cellColor!![cellColor!!.r, cellColor!!.g, cellColor!!.b] = max((cellColor!!.a - COLOR_SPEED * dt).toDouble(), 0.0).toFloat()
            dropColor!![cellColor!!.r, cellColor!!.g, cellColor!!.b] = min((dropColor!!.a + COLOR_SPEED * dt).toDouble(), 1.0).toFloat()

            Cell.draw(cellColor!!, batch, pos!!.x, pos!!.y, cellSize)
            Cell.draw(dropTexture!!, dropColor!!, batch, pos!!.x, pos!!.y, cellSize)

            val translation = batch.transformMatrix.getTranslation(Vector3())
            isDone = translation.y + pos!!.y + dropTexture!!.height < 0
        }

    }

    companion object {
        private const val FALL_ACCELERATION = 500.0F
        private const val FALL_VARIATION = 50.0F
        private const val COLOR_SPEED = 7.5F
    }
}
