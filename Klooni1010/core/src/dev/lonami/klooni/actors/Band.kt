package dev.lonami.klooni.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Theme.Companion.getBlankTexture
import dev.lonami.klooni.game.BaseScorer
import dev.lonami.klooni.game.GameLayout

// Horizontal band, used to show the score on the pause menu
class Band(game: Klooni, layout: GameLayout, private val scorer: BaseScorer) : Actor() {
    @JvmField
    val scoreBounds: Rectangle

    @JvmField
    val infoBounds: Rectangle
    private val bandTexture = getBlankTexture()
    private val infoLabel: Label
    private val scoreLabel: Label

    init {

        val labelStyle = LabelStyle()
        labelStyle.font = game.skin!!.getFont("font")

        scoreLabel = Label("", labelStyle)
        scoreLabel.setAlignment(Align.center)
        infoLabel = Label("pause menu", labelStyle)
        infoLabel.setAlignment(Align.center)

        scoreBounds = Rectangle()
        infoBounds = Rectangle()
        layout.update(this)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        val x = getParent().getX()
        val y = getParent().getY()

        val pos = localToStageCoordinates(Vector2(x, y))
        batch.color = Klooni.theme!!.bandColor
        batch.draw(bandTexture, pos.x, pos.y, getWidth(), getHeight())

        scoreLabel.setBounds(x + scoreBounds.x, y + scoreBounds.y, scoreBounds.width, scoreBounds.height)
        scoreLabel.setText(scorer.getCurrentScore().toString())
        scoreLabel.setColor(Klooni.theme!!.textColor)
        scoreLabel.draw(batch, parentAlpha)

        infoLabel.setBounds(x + infoBounds.x, y + infoBounds.y, infoBounds.width, infoBounds.height)
        infoLabel.setColor(Klooni.theme!!.textColor)
        infoLabel.draw(batch, parentAlpha)
    }

    // Once game over is set on the menu, it cannot be reverted
    fun setMessage(message: String) {
        if (!message.isEmpty()) infoLabel.setText(message)
    }
}
