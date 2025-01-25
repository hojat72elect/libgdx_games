package dev.lonami.klooni.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.game.Piece.Companion.random
import dev.lonami.klooni.serializer.BinSerializable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import kotlin.math.min

/**
 * A holder of pieces that can be drawn on screen.
 * Pieces can be picked up from it and dropped on a board.
 * Throughout the game, this PieceHolder will be drawn below the game board and user chooses pieces from it.
 */
class PieceHolder(
    layout: GameLayout, // Every piece holder belongs to a specific board
    private val board: Board,
    // Count of pieces to be shown
    private val count: Int, // The size the cells will adopt once picked
    private val pickedCellSize: Float
) : BinSerializable {
    val area: Rectangle
    private val pieces = arrayOfNulls<Piece>(count)
    private val pieceDropSound: Sound = Gdx.audio.newSound(Gdx.files.internal("sound/piece_drop.mp3"))
    private val invalidPieceDropSound: Sound = Gdx.audio.newSound(Gdx.files.internal("sound/invalid_drop.mp3"))
    private val takePiecesSound: Sound = Gdx.audio.newSound(Gdx.files.internal("sound/take_pieces.mp3"))

    // Needed after a piece is dropped, so it can go back
    private val originalPositions = arrayOfNulls<Rectangle>(count)

    var enabled: Boolean = true

    // Currently held piece index (picked by the user)
    private var heldPiece: Int

    init {
        heldPiece = -1

        area = Rectangle()
        layout.update(this)

        // takeMore depends on the layout to be ready to do so, how would pieces handle a layout update?
        takeMore()
    }

    /**
     * Determines whether all the pieces have been put (and the "hand" is finished).
     */
    private fun handFinished(): Boolean {
        for (i in 0 until count) if (pieces[i] != null) return false

        return true
    }

    /**
     * Takes a new set of pieces. Should be called when there are no more piece left.
     */
    private fun takeMore() {
        for (i in 0 until count) pieces[i] = random()
        updatePiecesStartLocation()

        if (Klooni.soundsEnabled()) {
            // Random pitch so it's not always the same sound
            takePiecesSound.play(1f, MathUtils.random(0.8f, 1.2f), 0f)
        }
    }

    private fun updatePiecesStartLocation() {
        val perPieceWidth = area.width / count
        var piece: Piece?
        for (i in 0 until count) {
            piece = pieces[i]
            if (piece == null) continue

            // Set the absolute position on screen and the cells' cellSize Also clamp the cell size to be the picked size as maximum, or it would be too big in some cases.
            piece.pos[area.x + i * perPieceWidth] = area.y
            piece.cellSize = min(
                min(
                    (perPieceWidth / piece.cellCols).toDouble(),
                    (area.height / piece.cellRows).toDouble()
                ), pickedCellSize.toDouble()
            ).toFloat()

            // Center the piece on the X and Y axes. For this we see how much up we can go, this is, (area.height - piece.height) / 2
            val rectangle = piece.rectangle
            piece.pos.y += (area.height - rectangle.height) * 0.5f
            piece.pos.x += (perPieceWidth - rectangle.width) * 0.5f

            originalPositions[i] = Rectangle(
                piece.pos.x, piece.pos.y,
                piece.cellSize, piece.cellSize
            )

            // Now that we have the original positions, reset the size so it animates and grows
            piece.cellSize = 0f
        }
    }

    /**
     * Picks the piece below the finger/mouse, returning true if any was picked.
     */
    fun pickPiece(): Boolean {
        val mouse = Vector2(
            Gdx.input.x.toFloat(),
            (Gdx.graphics.height - Gdx.input.y).toFloat()
        ) // Y axis is inverted

        val perPieceWidth = area.width / count
        for (i in 0 until count) {
            if (pieces[i] != null) {
                val maxPieceArea = Rectangle(
                    area.x + i * perPieceWidth, area.y, perPieceWidth, area.height
                )

                if (maxPieceArea.contains(mouse)) {
                    heldPiece = i
                    return true
                }
            }
        }

        heldPiece = -1
        return false
    }

    val availablePieces: Array<Piece?>
        get() {
            val result = Array<Piece?>(count)
            for (i in 0 until count) if (pieces[i] != null) result.add(pieces[i])

            return result
        }

    /**
     * If no piece is currently being held, the area will be 0.
     */
    private fun calculateHeldPieceArea(): Int {
        return if (heldPiece > -1) pieces[heldPiece]!!.calculateArea() else 0
    }

    private fun calculateHeldPieceCenter(): Vector2? {
        return if (heldPiece > -1) pieces[heldPiece]!!.calculateGravityCenter() else null
    }

    /**
     * Tries to drop the piece on the given board. As a result, it
     * returns one of the following: NO_DROP, NORMAL_DROP, ON_BOARD_DROP.
     */
    fun dropPiece(): DropResult {
        val result: DropResult

        if (heldPiece > -1) {
            val put = enabled && board.putScreenPiece(pieces[heldPiece])
            if (put) {
                if (Klooni.soundsEnabled()) {
                    // The larger the piece size, the smaller the pitch
                    // Considering 10 cells to be the largest, 1.1 highest pitch, 0.7 lowest
                    val pitch = 1.104f - pieces[heldPiece]!!.calculateArea() * 0.04f
                    pieceDropSound.play(1f, pitch, 0f)
                }

                result = DropResult(calculateHeldPieceArea(), calculateHeldPieceCenter())
                pieces[heldPiece] = null
            } else {
                if (Klooni.soundsEnabled()) invalidPieceDropSound.play()

                result = DropResult(true)
            }

            heldPiece = -1
            if (handFinished()) takeMore()
        } else result = DropResult(false)

        return result
    }

    /**
     * Updates the state of the piece holder (and the held piece)
     */
    fun update() {
        var piece: Piece?
        if (heldPiece > -1) {
            piece = pieces[heldPiece]

            val mouse = getVector2(piece!!)
            if (Klooni.shouldSnapToGrid()) mouse.set(board.snapToGrid(piece, mouse))

            piece.pos.lerp(mouse, DRAG_SPEED)
            piece.cellSize = Interpolation.linear.apply(piece.cellSize, pickedCellSize, DRAG_SPEED)
        }

        // Return the pieces to their original position (This seems somewhat expensive, can't it be done any better?)
        var original: Rectangle?
        for (i in 0 until count) {
            if (i == heldPiece) continue

            piece = pieces[i]
            if (piece == null) continue

            original = originalPositions[i]
            piece.pos.lerp(Vector2(original!!.x, original.y), 0.3f)
            piece.cellSize = Interpolation.linear.apply(piece.cellSize, original.width, 0.3f)
        }
    }

    private fun getVector2(piece: Piece): Vector2 {
        val mouse = Vector2(
            Gdx.input.x.toFloat(),
            (Gdx.graphics.height - Gdx.input.y).toFloat()
        ) // Y axis is inverted

        if (Klooni.onDesktop) { // This is a bad assumption to make. There are desktops with touch input and non-desktops with mouse input.
            // Center the piece to the mouse
            mouse.sub(piece.rectangle.width * 0.5f, piece.rectangle.height * 0.5f)
        } else {
            /*
                       * Center the new piece position horizontally and push it up by it's a cell (arbitrary) vertically,
                       * thus avoiding to cover it with the finger (issue on Android devices).
                       */

            mouse.sub(piece.rectangle.width * 0.5f, -pickedCellSize)
        }
        return mouse
    }

    fun draw(batch: SpriteBatch) {
        for (i in 0 until count) {
            if (pieces[i] != null) {
                pieces[i]!!.draw(batch)
            }
        }
    }

    @Throws(IOException::class)
    override fun write(outputStream: DataOutputStream) {
        // Piece count, false if piece == null, true + piece if piece != null
        outputStream.writeInt(count)
        for (i in 0 until count) {
            if (pieces[i] == null) {
                outputStream.writeBoolean(false)
            } else {
                outputStream.writeBoolean(true)
                pieces[i]!!.write(outputStream)
            }
        }
    }

    @Throws(IOException::class)
    override fun read(inputStream: DataInputStream) {
        // If the saved piece count does not match the current piece count, then an IOException is thrown since the data saved was invalid
        val savedPieceCount = inputStream.readInt()
        if (savedPieceCount != count) throw IOException("Invalid piece count saved.")

        for (i in 0 until count) pieces[i] = if (inputStream.readBoolean()) Piece.read(inputStream) else null
        updatePiecesStartLocation()
    }

    class DropResult {
        val dropped: Boolean
        val onBoard: Boolean

        val area: Int
        val pieceCenter: Vector2?

        internal constructor(dropped: Boolean) {
            this.dropped = dropped
            onBoard = false
            area = 0
            pieceCenter = null
        }

        internal constructor(area: Int, pieceCenter: Vector2?) {
            onBoard = true
            dropped = onBoard
            this.area = area
            this.pieceCenter = pieceCenter
        }
    }

    companion object {
        // Interpolation value ((pos -> new) / frame)
        private const val DRAG_SPEED = 0.5f
    }
}
