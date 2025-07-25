package dev.lonami.klooni.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.SkinLoader.loadPng
import dev.lonami.klooni.serializer.BinSerializable

abstract class BaseScorer internal constructor(game: Klooni, layout: GameLayout, highScore: Int) : BinSerializable {
    @JvmField
    val currentScoreLabel: Label

    @JvmField
    val highScoreLabel: Label

    @JvmField
    val cupTexture: Texture

    @JvmField
    val cupArea: Rectangle
    private val cupColor: Color
    var currentScore: Int = 0

    // To interpolate between shown score -> real score
    private var shownScore = 0f

    // The board size is required when calculating the score
    init {
        cupTexture = loadPng("cup.png")
        cupColor = Klooni.theme!!.currentScore!!.cpy()
        cupArea = Rectangle()

        val labelStyle = LabelStyle()
        labelStyle.font = game.skin!!.getFont("font")

        currentScoreLabel = Label("0", labelStyle)
        currentScoreLabel.setAlignment(Align.right)

        highScoreLabel = Label(highScore.toString(), labelStyle)

        layout.update(this)
    }

    // The original game seems to work as follows:
    // If < 1 were cleared, score = 0
    // If = 1  was cleared, score = cells cleared
    // If > 1 were cleared, score = cells cleared + score(cleared - 1)
    fun calculateClearScore(stripsCleared: Int, boardSize: Int): Int {
        if (stripsCleared < 1) return 0
        if (stripsCleared == 1) return boardSize
        else return boardSize * stripsCleared + calculateClearScore(stripsCleared - 1, boardSize)
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

    abstract val isGameOver: Boolean

    protected abstract val isNewRecord: Boolean

    open fun gameOverReason(): String {
        return ""
    }

    abstract fun saveScore()

    open fun draw(batch: SpriteBatch) {
        // If we beat a new record, the cup color will linear interpolate to the high score color
        cupColor.lerp(if (this.isNewRecord) Klooni.theme!!.highScore else Klooni.theme!!.currentScore, 0.05f)
        batch.setColor(cupColor)
        batch.draw(cupTexture, cupArea.x, cupArea.y, cupArea.width, cupArea.height)

        val roundShown = MathUtils.round(shownScore)
        if (roundShown != currentScore) {
            shownScore = Interpolation.linear.apply(shownScore, currentScore.toFloat(), 0.1f)
            currentScoreLabel.setText(MathUtils.round(shownScore).toString())
        }

        currentScoreLabel.setColor(Klooni.theme!!.currentScore)
        currentScoreLabel.draw(batch, 1f)

        highScoreLabel.setColor(Klooni.theme!!.highScore)
        highScoreLabel.draw(batch, 1f)
    }
}
