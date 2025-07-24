package dev.lonami.klooni.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import dev.lonami.klooni.Klooni

// Screen where the user can customize the look and feel of the game
internal class ShareScoreScreen(
    private val game: Klooni,
    private val lastScreen: Screen,
    private val score: Int,
    private val timeMode: Boolean
) : Screen {
    val labelStyle = LabelStyle(game.skin?.getFont("font_small"), Klooni.theme?.textColor)
    private val infoLabel = Label("Generating image...", labelStyle)
    private val spriteBatch = SpriteBatch()

    init {
        infoLabel.setColor(Klooni.theme?.textColor)
        infoLabel.setAlignment(Align.center)
        infoLabel.layout()
        infoLabel.setPosition(
            (Gdx.graphics.width - infoLabel.getWidth()) * 0.5f,
            (Gdx.graphics.height - infoLabel.getHeight()) * 0.5f
        )
    }

    private fun goBack() {
        game.transitionTo(lastScreen)
    }

    override fun show() {
        val ok = game.shareChallenge!!.saveChallengeImage(score, timeMode)
        game.shareChallenge.shareScreenshot(ok)
        goBack()
    }

    override fun render(delta: Float) {
        Klooni.theme?.glClearBackground()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        spriteBatch.begin()
        infoLabel.draw(spriteBatch, 1f)
        spriteBatch.end()
    }

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}
}
