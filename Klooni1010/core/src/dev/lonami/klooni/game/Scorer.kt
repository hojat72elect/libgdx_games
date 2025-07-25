package dev.lonami.klooni.game

import dev.lonami.klooni.Klooni
import dev.lonami.klooni.serializer.BinSerializable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException


/**
 * Used to keep track of the current and maximum
 * score, and to also display it on the screen.
 * The maximum score is NOT saved automatically.
 */
class Scorer(game: Klooni, layout: GameLayout) : BaseScorer(game, layout, Klooni.getMaxScore()), BinSerializable {

    // The board size is required when calculating the score
    private var highScore = Klooni.getMaxScore()

    override fun saveScore() {
        if (isNewRecord) Klooni.setMaxScore(currentScore)
    }

    override val isNewRecord = currentScore > highScore

    override val isGameOver = false

    @Throws(IOException::class)
    override fun write(output: DataOutputStream) {
        output.writeInt(currentScore)
        output.writeInt(highScore)
    }

    @Throws(IOException::class)
    override fun read(input: DataInputStream) {
        currentScore = input.readInt()
        highScore = input.readInt()
    }
}
