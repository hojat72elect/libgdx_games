package dev.lonami.klooni.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import dev.lonami.klooni.Klooni

class BonusParticle(pos: Vector2, score: Int, style: LabelStyle) {

    private val label = Label("+$score", style)
    private var lifetime = 0F

    init {
        label.setBounds(pos.x, pos.y, 0F, 0F)
    }

    fun run(batch: Batch) {
        // Update
        lifetime += SPEED * Gdx.graphics.deltaTime
        if (lifetime > 1F) lifetime = 1F

        // Render
        label.color = Klooni.theme.bonus
        label.setFontScale(Interpolation.elasticOut.apply(0f, 1f, lifetime))
        val opacity = Interpolation.linear.apply(1f, 0f, lifetime)
        label.draw(batch, opacity)
    }

    fun done() = lifetime >= 1f

    companion object {
        private const val SPEED = 1F
    }
}