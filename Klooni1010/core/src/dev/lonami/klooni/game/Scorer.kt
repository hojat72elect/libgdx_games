package dev.lonami.klooni.game

import dev.lonami.klooni.Klooni
import dev.lonami.klooni.serializer.BinSerializable
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Used to keep track of the current and maximum score of the player, and to also display it on the screen.
 * The maximum score is NOT saved automatically.
 *
 * Please pay attention that the board size is required when calculating the score.
 */
class Scorer(game: Klooni, layout: GameLayout) : BaseScorer(game, layout, Klooni.getMaxScore()), BinSerializable {

    private var highScore = Klooni.getMaxScore();

    override fun saveScore() {
        if (isNewRecord()) {
            Klooni.setMaxScore(currentScore)
        }
    }

    override fun isNewRecord() = currentScore > highScore

    override fun isGameOver() = false

    override fun write(outputStream: DataOutputStream) {
        outputStream.writeInt(currentScore)
        outputStream.writeInt(highScore)
    }

    override fun read(inputStream: DataInputStream) {
        currentScore = inputStream.readInt()
        highScore = inputStream.readInt()
    }
}