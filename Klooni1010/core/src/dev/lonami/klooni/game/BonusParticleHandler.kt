package dev.lonami.klooni.game

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import dev.lonami.klooni.Klooni
import com.badlogic.gdx.utils.Array as GdxArray

class BonusParticleHandler(game: Klooni) {
    private val particles = GdxArray<BonusParticle>()
    private val labelStyle = LabelStyle(game.skin.getFont("font_bonus"), Klooni.theme.textColor)

    fun addBonus(pos: Vector2, score: Int) {
        particles.add(BonusParticle(pos, score, labelStyle))
    }

    fun run(batch: Batch) {
        for (particle in particles) {
            particle.run(batch)
        }
    }
}
