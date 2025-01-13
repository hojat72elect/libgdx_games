package dev.lonami.klooni.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.SkinLoader
import dev.lonami.klooni.serializer.BinSerializable

/**
 * this abstract class is teh basic foundation for calculating, displaying, and saving player's score.
 * But the logic of game-over condition and record tracking is left to be implemented by subclasses.
 */
abstract class BaseScorer(game: Klooni, layout: GameLayout, highScore: Int) : BinSerializable {

    @JvmField
    val cupTexture: Texture? = SkinLoader.loadPng("cup.png")
    private val cupColor = Klooni.theme.currentScore.cpy()

    @JvmField
    val cupArea = Rectangle()
    var currentScore = 0

    // To interpolate between shown score -> real score
    private var shownScore = 0F

    @JvmField
    val currentScoreLabel: Label
    @JvmField
    val highScoreLabel: Label

    init {
        val labelStyle = LabelStyle()
        labelStyle.font = game.skin.getFont("font")

        currentScoreLabel = Label("0", labelStyle)
        currentScoreLabel.setAlignment(Align.right)

        highScoreLabel = Label(highScore.toString(), labelStyle)

        layout.update(this)
    }

    /**
     * The board size is required when calculating the score.
     * The score of the player throughout the game is calculated like this:
     * If < 1 were cleared, score = 0
     * If = 1  was cleared, score = cells cleared
     * If > 1 were cleared, score = cells cleared + score(cleared - 1)
     */
    fun calculateClearScore(stripsCleared: Int, boardSize: Int): Int {
        if (stripsCleared < 1) return 0
        return if (stripsCleared == 1) boardSize
        else boardSize * stripsCleared + calculateClearScore(stripsCleared - 1, boardSize)
    }

    // Adds the score a given piece would give
    fun addPieceScore(areaPut: Int) {
        currentScore += areaPut
    }

    // Adds the score given by the board, this is, the count of cleared strips
    open fun addBoardScore(stripsCleared: Int, boardSize: Int): Int {
        val score = calculateClearScore(stripsCleared, boardSize)
        currentScore += score
        return score
    }

    open fun pause() {
    }

    open fun resume() {
    }

    abstract fun isGameOver(): Boolean
    protected abstract fun isNewRecord(): Boolean
    abstract fun saveScore()

    open fun gameOverReason() = ""

    open fun draw(batch: SpriteBatch) {
        // If we beat a new record, the cup color will linear interpolate to the high score color
        cupColor.lerp(if (isNewRecord()) Klooni.theme.highScore else Klooni.theme.currentScore, 0.05F)
        batch.color = cupColor
        batch.draw(cupTexture, cupArea.x, cupArea.y, cupArea.width, cupArea.height)

        val roundShown = MathUtils.round(shownScore)
        if (roundShown != currentScore) {
            shownScore = Interpolation.linear.apply(shownScore, currentScore.toFloat(), 0.1F)
            currentScoreLabel.setText(MathUtils.round(shownScore))
        }

        currentScoreLabel.color = Klooni.theme.currentScore
        currentScoreLabel.draw(batch, 1F)

        highScoreLabel.color = Klooni.theme.highScore
        highScoreLabel.draw(batch, 1F)
    }
}