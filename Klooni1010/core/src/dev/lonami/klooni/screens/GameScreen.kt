package dev.lonami.klooni.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.Klooni.Companion.addMoneyFromScore
import dev.lonami.klooni.Klooni.Companion.soundsEnabled
import dev.lonami.klooni.game.BaseScorer
import dev.lonami.klooni.game.Board
import dev.lonami.klooni.game.BonusParticleHandler
import dev.lonami.klooni.game.GameLayout
import dev.lonami.klooni.game.PieceHolder
import dev.lonami.klooni.game.Scorer
import dev.lonami.klooni.game.TimeScorer
import dev.lonami.klooni.serializer.BinSerializable
import dev.lonami.klooni.serializer.BinSerializer.deserialize
import dev.lonami.klooni.serializer.BinSerializer.serialize
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

// Main game screen. Here the board, piece holder and score are shown
internal class GameScreen @JvmOverloads constructor(game: Klooni, gameMode: Int, loadSave: Boolean = true) : Screen, InputProcessor, BinSerializable {
    private val game: Klooni
    private val scorer: BaseScorer
    private val bonusParticleHandler: BonusParticleHandler
    private val board: Board
    private val holder: PieceHolder
    private val batch: SpriteBatch

    private val gameOverSound: Sound
    private val pauseMenu: PauseMenuStage

    private val gameMode: Int
    private var gameOverDone = false

    // The last score that was saved when adding the money.
    // We use this so we don't add the same old score to the money twice,
    // but rather subtract it from the current score and then update it
    // with the current score to get the "increase" of money score.
    private var savedMoneyScore = 0

    // Load any previously saved file by default
    init {
        batch = SpriteBatch()
        this.game = game
        this.gameMode = gameMode

        val layout = GameLayout()
        when (gameMode) {
            GAME_MODE_SCORE -> scorer = Scorer(game, layout)
            GAME_MODE_TIME -> scorer = TimeScorer(game, layout)
            else -> throw RuntimeException("Unknown game mode given: " + gameMode)
        }

        board = Board(layout, BOARD_SIZE)
        holder = PieceHolder(layout, board, HOLDER_PIECE_COUNT, board.cellSize)
        pauseMenu = PauseMenuStage(layout, game, scorer, gameMode)
        bonusParticleHandler = BonusParticleHandler(game)

        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sound/game_over.mp3"))

        if (gameMode == GAME_MODE_SCORE) {
            if (loadSave) {
                // The user might have a previous game. If this is the case, load it
                if (!tryLoad()) {
                    System.err.println("failed to load previous games")
                }
            } else {
                // Ensure that there is no old save, we don't want to load it, thus delete it
                deleteSave()
            }
        }
    }

    private val isGameOver: Boolean
        // If no piece can be put, then it is considered to be game over
        get() {
            for (piece in holder.getAvailablePieces()) if (board.canPutPiece(piece)) return false

            return true
        }

    private fun doGameOver(gameOverReason: String) {
        if (!gameOverDone) {
            gameOverDone = true

            saveMoney()
            holder.enabled = false
            pauseMenu.showGameOver(gameOverReason, scorer is TimeScorer)
            if (soundsEnabled()) gameOverSound.play()

            // The user should not be able to return to the game if its game over
            if (gameMode == GAME_MODE_SCORE) deleteSave()
        }
    }

    override fun show() {
        if (pauseMenu.isShown)  // Will happen if we go to the customize menu
            Gdx.input.inputProcessor = pauseMenu
        else Gdx.input.inputProcessor = this
    }

    // Save the state, the user might leave the game in any of the following 2 methods
    private fun showPauseMenu() {
        saveMoney()
        pauseMenu.show()
        save()
    }

    override fun pause() {
        save()
    }

    override fun render(delta: Float) {
        Klooni.theme!!.glClearBackground()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (scorer.isGameOver() && !pauseMenu.isShown) {
            doGameOver(scorer.gameOverReason())
        }

        batch.begin()

        scorer.draw(batch)
        board.draw(batch)
        holder.update()
        holder.draw(batch)
        bonusParticleHandler.run(batch)

        batch.end()

        if (pauseMenu.isShown || pauseMenu.isHiding) {
            pauseMenu.act(delta)
            pauseMenu.draw()
        }
    }

    override fun dispose() {
        pauseMenu.dispose()
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Input.Keys.P || keycode == Input.Keys.BACK)  // Pause
            showPauseMenu()

        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return holder.pickPiece()
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val result = holder.dropPiece()
        if (!result.dropped) return false

        if (result.onBoard) {
            scorer.addPieceScore(result.area)
            val bonus = scorer.addBoardScore(board.clearComplete(game.effect), board.cellCount)
            if (bonus > 0) {
                bonusParticleHandler.addBonus(result.pieceCenter, bonus)
                if (soundsEnabled()) {
                    game.playEffectSound()
                }
            }

            // After the piece was put, check if it's game over
            if (this.isGameOver) {
                doGameOver("no moves left")
            }
        }
        return true
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun resume() {
    }

    override fun hide() { /* Hide can only be called if the menu was shown. Place logic there. */
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    private fun saveMoney() {
        // Calculate new money since the previous saving
        val nowScore = scorer.getCurrentScore()
        val newMoneyScore = nowScore - savedMoneyScore
        savedMoneyScore = nowScore
        addMoneyFromScore(newMoneyScore)
    }

    private fun save() {
        // Only save if the game is not over and the game mode is not the time mode. It
        // makes no sense to save the time game mode since it's supposed to be something quick.
        // Don't save either if the score is 0, which means the player did nothing.
        if (gameOverDone || gameMode != GAME_MODE_SCORE || scorer.getCurrentScore() == 0) return

        val handle = Gdx.files.local(SAVE_DAT_FILENAME)
        try {
            serialize(this, handle.write(false))
        } catch (e: IOException) {
            // Should never happen but what else could be done if the game wasn't saved?
            e.printStackTrace()
        }
    }

    private fun tryLoad(): Boolean {
        val handle = Gdx.files.local(SAVE_DAT_FILENAME)
        if (handle.exists()) {
            try {
                deserialize(this, handle.read())
                // No cheating! We need to load the previous money
                // or it would seem like we earned it on this game
                savedMoneyScore = scorer.getCurrentScore()

                // After it's been loaded, delete the save file
                deleteSave()
                return true
            } catch (ignored: IOException) {
            }
        }
        return false
    }

    @Throws(IOException::class)
    override fun write(output: DataOutputStream) {
        // gameMode, board, holder, scorer
        output.writeInt(gameMode)
        board.write(output)
        holder.write(output)
        scorer.write(output)
    }

    @Throws(IOException::class)
    override fun read(input: DataInputStream) {
        val savedGameMode = input.readInt()
        if (savedGameMode != gameMode) throw IOException("A different game mode was saved. Cannot load the save data.")

        board.read(input)
        holder.read(input)
        scorer.read(input)
    }

    companion object {
        const val GAME_MODE_SCORE: Int = 0
        const val GAME_MODE_TIME: Int = 1
        private const val BOARD_SIZE = 10
        private const val HOLDER_PIECE_COUNT = 3
        private const val SAVE_DAT_FILENAME = ".klooni.sav"
        private fun deleteSave() {
            val handle = Gdx.files.local(SAVE_DAT_FILENAME)
            if (handle.exists()) handle.delete()
        }

        fun hasSavedData(): Boolean {
            return Gdx.files.local(SAVE_DAT_FILENAME).exists()
        }
    }
}
