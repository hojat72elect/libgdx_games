package dev.lonami.klooni.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.game.BaseScorer
import dev.lonami.klooni.game.Board
import dev.lonami.klooni.game.BonusParticleHandler
import dev.lonami.klooni.game.GameLayout
import dev.lonami.klooni.game.PieceHolder
import dev.lonami.klooni.game.Scorer
import dev.lonami.klooni.game.TimeScorer
import dev.lonami.klooni.serializer.BinSerializable
import dev.lonami.klooni.serializer.BinSerializer
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

/**
 * Main game screen. In this screen the board, piece holder, and score are shown.
 *
 * @param gameMode - Perhaps make an abstract base class for the game screen and game modes by implementing different "isGameOver" etc. logic instead of using an integer?
 * @param loadSave - By default, any previously saved file will be loaded.
 */
class GameScreen(
    val game: Klooni,
    private val gameMode: Int,
    loadSave: Boolean = true
) : Screen, InputProcessor, BinSerializable {

    private var scorer: BaseScorer
    private val bonusParticleHandler = BonusParticleHandler(game)
    private val layout = GameLayout()
    private val board = Board(layout, BOARD_SIZE)
    private val holder = PieceHolder(layout, board, HOLDER_PIECE_COUNT, board.cellSize)
    private val batch = SpriteBatch()
    private val gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sound/game_over.mp3"))
    private var pauseMenu:PauseMenuStage
    private var gameOverDone = false

    /**
     * The last score that was saved when adding the money.
     * We use this so we don't add the same old score to the money twice,
     * but rather subtract it from the current score and then update it
     * with the current score to get the "increase" of money score.
     */
    private var savedMoneyScore = 0

    init {
        scorer = when (gameMode) {
            GAME_MODE_SCORE -> Scorer(game, layout)
            GAME_MODE_TIME -> TimeScorer(game, layout)
            else -> throw RuntimeException("Unknown game mode given: $gameMode")
        }

        if (gameMode == GAME_MODE_SCORE) {
            if (loadSave) {
                // The user might have a previous game. If this is the case, load it
                if (tryLoad().not()) {
                    System.err.println("failed to load previous games")
                }
            } else {
                // Ensure that there is no old save, we don't want to load it, thus delete it
                deleteSave()
            }
        }

        pauseMenu = PauseMenuStage(layout, game, scorer, gameMode)
    }

    /**
     * If no piece can be put, then it is considered to be game over.
     */
    private fun isGameOver(): Boolean {
        for (piece in holder.availablePieces) {
            if (board.canPutPiece(piece)) return false
        }
        return true
    }

    private fun doGameOver(gameOverReason: String) {
        if (gameOverDone.not()) {
            gameOverDone = true

            saveMoney()
            holder.enabled = false
            pauseMenu.showGameOver(gameOverReason, scorer is TimeScorer)
            if (Klooni.soundsEnabled()) gameOverSound.play()

            // The user should not be able to return to the game if its game over
            if (gameMode == GAME_MODE_SCORE) deleteSave()
        }
    }

    /**
     * Save the state, the user might leave the game in any of the following 2 methods.
     */
    private fun showPauseMenu() {
        saveMoney()
        pauseMenu.show()
        save()
    }

    private fun saveMoney() {
        // Calculate new money since the previous saving
        val nowScore = scorer.currentScore
        val newMoneyScore = nowScore - savedMoneyScore
        savedMoneyScore = nowScore
        Klooni.addMoneyFromScore(newMoneyScore)
    }

    /**
     * Only save if the game is not over and the game mode is not the time mode. It
     * makes no sense to save the time game mode since it's supposed to be something quick.
     * Don't save either if the score is 0, which means the player did nothing.
     */
    private fun save() {
        if (gameOverDone || gameMode != GAME_MODE_SCORE || scorer.currentScore == 0) return

        val handle = Gdx.files.local(SAVE_DAT_FILENAME)
        try {
            BinSerializer.serialize(this, handle.write(false))
        } catch (e: IOException) {
            // This should never happen but what else could be done if the game wasn't saved?
            e.printStackTrace()
        }
    }

    private fun tryLoad(): Boolean {
        val handle = Gdx.files.local(SAVE_DAT_FILENAME)
        if (handle.exists()) {
            try {
                BinSerializer.deserialize(this, handle.read())

                // No cheating! We need to load the previous money or it would seem like we earned it on this game.
                savedMoneyScore = scorer.currentScore

                // After it's been loaded, delete the save file
                deleteSave()
                return true
            } catch (ignored: IOException) {
            }
        }
        return false
    }

    override fun show() {
        if (pauseMenu.isShown())  // Will happen if we go to the customize menu
            Gdx.input.inputProcessor = pauseMenu
        else Gdx.input.inputProcessor = this
    }

    override fun render(delta: Float) {

        Klooni.theme.glClearBackground()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (scorer.isGameOver() && pauseMenu.isShown().not()) {
            // A bit hardcoded (timeOver = scorer instanceof TimeScorer). Perhaps have a better mode to pass the required texture to overlay
            doGameOver(scorer.gameOverReason())
        }

        batch.begin()

        scorer.draw(batch)
        board.draw(batch)
        holder.update()
        holder.draw(batch)
        bonusParticleHandler.run(batch)

        batch.end()

        if (pauseMenu.isShown() || pauseMenu.isHiding()) {
            pauseMenu.act(delta)
            pauseMenu.draw()
        }


    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {
        save()
    }

    override fun resume() {}

    override fun hide() {
        // Hide can only be called if the menu was shown. Place your logic there.
    }

    override fun dispose() {
        pauseMenu.dispose()
    }

    override fun keyDown(keycode: Int) = false

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Input.Keys.P || keycode == Input.Keys.BACK)
            showPauseMenu()

        return false
    }

    override fun keyTyped(character: Char) = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = holder.pickPiece()

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        val result = holder.dropPiece()
        if (result.dropped.not()) return false

        if (result.onBoard) {
            scorer.addPieceScore(result.area)
            val bonus = scorer.addBoardScore(board.clearComplete(game.effect), board.cellCount)
            if (bonus > 0) {
                bonusParticleHandler.addBonus(result.pieceCenter!!, bonus)
                if (Klooni.soundsEnabled()) {
                    game.playEffectSound()
                }
            }

            // After the piece was put, check if it's game over
            if (isGameOver()) {
                doGameOver("no moves left")
            }
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false
    override fun mouseMoved(screenX: Int, screenY: Int) = false
    override fun scrolled(amount: Int) = false

    override fun write(outputStream: DataOutputStream) {
        // gameMode, board, holder, scorer
        outputStream.writeInt(gameMode)
        board.write(outputStream)
        holder.write(outputStream)
        scorer.write(outputStream)
    }

    override fun read(inputStream: DataInputStream) {

        val savedGameMode = inputStream.readInt()
        if (savedGameMode != gameMode) throw IOException("A different game mode was saved. Cannot load the save data.")

        board.read(inputStream)
        holder.read(inputStream)
        scorer.read(inputStream)

    }

    companion object {
        const val GAME_MODE_SCORE = 0
        const val GAME_MODE_TIME = 1
        private const val BOARD_SIZE = 10
        private const val HOLDER_PIECE_COUNT = 3
        private const val SAVE_DAT_FILENAME = ".klooni.sav"

        private fun deleteSave() {
            val handle = Gdx.files.local(SAVE_DAT_FILENAME)
            if (handle.exists()) handle.delete()
        }

        fun hasSavedData() = Gdx.files.local(SAVE_DAT_FILENAME).exists()

    }
}
