package dev.lonami.klooni.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Theme
import dev.lonami.klooni.game.BaseScorer
import dev.lonami.klooni.game.GameLayout

/**
 * Horizontal band, used to show the score on the pause menu.
 */
class Band(game: Klooni, layout: GameLayout, private val scorer: BaseScorer) : Actor() {

    private val bandTexture = Theme.getBlankTexture()
    private val scoreLabel: Label
    private val infoLabel: Label

    @JvmField
    val scoreBounds = Rectangle()

    @JvmField
    val infoBounds = Rectangle()

    init {
        val labelStyle = Label.LabelStyle()
        labelStyle.font = game.skin.getFont("font")

        scoreLabel = Label("", labelStyle)
        scoreLabel.setAlignment(Align.center)

        infoLabel = Label("pause menu", labelStyle)
        infoLabel.setAlignment(Align.center)

        layout.update(this)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        // ?? This is not the best way to apply the transformation, but, oh well
        val x = parent.x
        val y = parent.y

        // ?? For some strange reason, the texture coordinates and label coordinates are different
        val pos = localToStageCoordinates(Vector2(x, y))
        batch.color = Klooni.theme.bandColor
        batch.draw(bandTexture, pos.x, pos.y, width, height)

        scoreLabel.setBounds(x + scoreBounds.x, y + scoreBounds.y, scoreBounds.width, scoreBounds.height)
        scoreLabel.setText(scorer.currentScore)
        scoreLabel.color = Klooni.theme.textColor
        scoreLabel.draw(batch, parentAlpha)

        infoLabel.setBounds(x + infoBounds.x, y + infoBounds.y, infoBounds.width, infoBounds.height)
        infoLabel.color = Klooni.theme.textColor
        infoLabel.draw(batch, parentAlpha)
    }

    // Once game over is set on the menu, it cannot be reverted
    fun setMessage(message: String) {
        if (message.isNotEmpty()) infoLabel.setText(message)
    }
}