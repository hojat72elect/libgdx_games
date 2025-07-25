package dev.lonami.klooni.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.TimeUtils
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Klooni.Companion.getMaxTimeScore
import dev.lonami.klooni.Klooni.Companion.setMaxTimeScore
import dev.lonami.klooni.serializer.BinSerializable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import kotlin.math.max

class TimeScorer(game: Klooni, layout: GameLayout) : BaseScorer(game, layout, getMaxTimeScore()), BinSerializable {
    private val timeLeftLabel: Label
    private var startTime: Long
    private var highScore: Int

    // Indicates where we would die in time. Score adds to this, so we take
    // longer to die. To get the "score" we simply calculate `deadTime - startTime`
    private var deadTime: Long

    // We need to know when the game was paused to "stop" counting
    private var pauseTime: Long = 0
    private var pausedTimeLeft: Int

    // The board size is required when calculating the score
    init {
        highScore = getMaxTimeScore()

        val labelStyle = LabelStyle()
        labelStyle.font = game.skin!!.getFont("font")
        timeLeftLabel = Label("", labelStyle)
        timeLeftLabel.setAlignment(Align.center)
        layout.updateTimeLeftLabel(timeLeftLabel)

        startTime = TimeUtils.nanoTime()
        deadTime = startTime + START_TIME

        pausedTimeLeft = -1
    }

    private fun nanosToSeconds(nano: Long): Int {
        return MathUtils.ceil((nano * NANOS_TO_SECONDS).toFloat())
    }

    private fun scoreToNanos(score: Int): Long {
        return (score * SCORE_TO_NANOS).toLong()
    }

    private val timeLeft: Int
        get() = max(nanosToSeconds(deadTime - TimeUtils.nanoTime()), 0)

    override fun addBoardScore(stripsCleared: Int, boardSize: Int): Int {
        // Only clearing strips adds extra time
        val extraTime = scoreToNanos(calculateClearScore(stripsCleared, boardSize))
        deadTime += extraTime
        super.addBoardScore(stripsCleared, boardSize)

        return nanosToSeconds(extraTime)
    }

    override val isGameOver = TimeUtils.nanoTime() > deadTime

    override fun gameOverReason(): String {
        return "time is up"
    }

    override fun saveScore() {
        if (isNewRecord) {
            setMaxTimeScore(currentScore)
        }
    }

    override val isNewRecord = currentScore > highScore

    override fun pause() {
        pauseTime = TimeUtils.nanoTime()
        pausedTimeLeft = this.timeLeft
    }

    override fun resume() {
        if (pauseTime != 0L) {
            val difference = TimeUtils.nanoTime() - pauseTime
            startTime += difference
            deadTime += difference

            pauseTime = 0L
            pausedTimeLeft = -1
        }
    }

    override fun draw(batch: SpriteBatch) {
        super.draw(batch)

        val timeLeft = if (pausedTimeLeft < 0) this.timeLeft else pausedTimeLeft
        timeLeftLabel.setText(timeLeft.toString())
        timeLeftLabel.setColor(Klooni.theme!!.currentScore)
        timeLeftLabel.draw(batch, 1f)
    }

    @Throws(IOException::class)
    override fun write(output: DataOutputStream) {
        // current/dead offset ("how long until we die"), highScore
        output.writeLong(TimeUtils.nanoTime() - startTime)
        output.writeInt(highScore)
    }

    @Throws(IOException::class)
    override fun read(input: DataInputStream) {
        // We need to use the offset, since the start time
        // is different and we couldn't save absolute values
        val deadOffset = input.readLong()
        deadTime = startTime + deadOffset
        highScore = input.readInt()
    }

    companion object {
        private const val START_TIME = 30_000_000_000L

        // 2 seconds every 10 points: (2/10)*10^9 to get the nanoseconds
        private const val SCORE_TO_NANOS = 0.2e+09
        private const val NANOS_TO_SECONDS = 1e-09
    }
}
