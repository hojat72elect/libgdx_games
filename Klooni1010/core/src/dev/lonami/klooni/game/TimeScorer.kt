package dev.lonami.klooni.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.TimeUtils
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.serializer.BinSerializable
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.math.max

class TimeScorer(game:Klooni, layout:GameLayout):BaseScorer(game, layout, Klooni.getMaxTimeScore()), BinSerializable {

    private var timeLeftLabel: Label
    private var startTime = TimeUtils.nanoTime()
    private var highScore = Klooni.getMaxTimeScore()

    // Indicates where we would die in time. Score adds to this, so we take longer to die. To get the "score" we simply calculate `deadTime - startTime`
    private var deadTime = startTime + START_TIME

    // We need to know when the game was paused to "stop" counting
    private var pauseTime = 0L
    private var pausedTimeLeft = -1


    init {
        val labelStyle = Label.LabelStyle()
        labelStyle.font = game.skin.getFont("font")
        timeLeftLabel = Label("", labelStyle)
        timeLeftLabel.setAlignment(Align.center)
        layout.updateTimeLeftLabel(timeLeftLabel)
    }

    private fun nanosToSeconds(nano: Long) = MathUtils.ceil((nano * NANOS_TO_SECONDS).toFloat())
    private fun scoreToNanos(score: Int) = (score * SCORE_TO_NANOS).toLong()
    private fun getTimeLeft() = max(nanosToSeconds(deadTime - TimeUtils.nanoTime()), 0)

    override fun addBoardScore(stripsCleared: Int, boardSize: Int): Int {

        // Only clearing strips adds extra time
        val extraTime = scoreToNanos(calculateClearScore(stripsCleared, boardSize))
        deadTime += extraTime
        super.addBoardScore(stripsCleared, boardSize)

        return nanosToSeconds(extraTime)
    }
    override fun isGameOver() = TimeUtils.nanoTime() > deadTime
    override fun gameOverReason() = "time is up"

    override fun saveScore() {
        if (isNewRecord()) Klooni.setMaxTimeScore(currentScore)
    }
    override fun isNewRecord() = currentScore > highScore

    override fun pause() {
        pauseTime = TimeUtils.nanoTime()
        pausedTimeLeft = getTimeLeft()
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

        val timeLeft = if (pausedTimeLeft < 0) getTimeLeft() else pausedTimeLeft
        timeLeftLabel.setText(timeLeft.toString())
        timeLeftLabel.color = Klooni.theme.currentScore
        timeLeftLabel.draw(batch, 1f)
    }

    override fun write(outputStream: DataOutputStream) {
        // current/dead offset ("how long until we die"), highScore
        outputStream.writeLong(TimeUtils.nanoTime() - startTime)
        outputStream.writeInt(highScore)
    }

    override fun read(inputStream: DataInputStream) {
        // We need to use the offset, since the start time is different and we couldn't save absolute values
        val deadOffset = inputStream.readLong()
        deadTime = startTime + deadOffset
        highScore = inputStream.readInt()
    }

    companion object{
        private const val START_TIME = 30 * 1_000_000_000L

        // 2 seconds every 10 points: (2/10)*10^9 to get the nanoseconds
        private const val SCORE_TO_NANOS = 0.2e+09
        private const val NANOS_TO_SECONDS = 1e-09
    }
}