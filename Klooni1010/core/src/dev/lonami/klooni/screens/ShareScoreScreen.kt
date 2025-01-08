package dev.lonami.klooni.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import dev.lonami.klooni.Klooni

/**
 * Screen where the user can customize the look and feel of the game.
 * In this screen, the score of the player is displayed and player can share a screenshot of their score as well.
 */
class ShareScoreScreen(val game: Klooni, val lastScreen: Screen, val score: Int, val timeMode: Boolean) : Screen {

    private val infoLabel: Label
    private val spriteBatch = SpriteBatch()

    init {
        val labelStyle = Label.LabelStyle()
        labelStyle.font = game.skin.getFont("font_small")

        infoLabel = Label("Generating image...", labelStyle)
        infoLabel.color = Klooni.theme.textColor
        infoLabel.setAlignment(Align.center)
        infoLabel.layout()
        infoLabel.setPosition(
            (Gdx.graphics.width - infoLabel.width) * 0.5f,
            (Gdx.graphics.height - infoLabel.height) * 0.5f
        )
    }

    private fun goBack() {
        game.transitionTo(lastScreen)
    }


    override fun show() {
        val ok = game.shareChallenge.saveChallengeImage(score, timeMode)
        game.shareChallenge.shareScreenshot(ok)
        goBack()
    }

    override fun render(delta: Float) {
        Klooni.theme.glClearBackground()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        spriteBatch.begin()
        infoLabel.draw(spriteBatch, 1F)
        spriteBatch.end()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}