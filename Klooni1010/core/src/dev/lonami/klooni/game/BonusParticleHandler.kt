package dev.lonami.klooni.game

import com.badlogic.gdx.utils.Array as GdxArray
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import dev.lonami.klooni.Klooni

class BonusParticleHandler(game: Klooni) {

    private val particles = GdxArray<BonusParticle>()
    private val labelStyle = LabelStyle()

    init {
        labelStyle.font = game.skin.getFont("font_bonus")
    }

    fun addBonus(pos: Vector2, score: Int) {
        particles.add(BonusParticle(pos, score, labelStyle))
    }

    fun run(batch: Batch) {
        val iterator = particles.iterator()
        while (iterator.hasNext()) {
            val particle = iterator.next()
            particle.run(batch)
            if (particle.done()) iterator.remove()
        }
    }
}