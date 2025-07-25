package dev.lonami.klooni.game

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import dev.lonami.klooni.interfaces.Effect
import dev.lonami.klooni.interfaces.EffectFactory
import dev.lonami.klooni.serializer.BinSerializable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import kotlin.math.min

// Represents the on screen board, with all the put cells
// and functions to determine when it is game over given a PieceHolder
class Board : BinSerializable {
    val cellCount: Int
    val pos: Vector2 = Vector2()
    private val effects = Array<Effect?>() // Particle effects once they vanish

    // Used to animate cleared cells vanishing
    private val lastPutPiecePos = Vector2()
    var cellSize: Float = 0f
    private lateinit var cells: kotlin.Array<kotlin.Array<Cell?>?>

    constructor(layout: GameLayout, cellCount: Int) {
        this.cellCount = cellCount

        // Cell size depends on the layout to be updated first
        layout.update(this)
        createCells()
    }

    constructor(area: Rectangle, cellCount: Int) {
        this.cellCount = cellCount

        // Cell size depends on the layout to be updated first
        pos.set(area.x, area.y)
        cellSize = min(area.height, area.width) / cellCount
        createCells()
    }

    private fun createCells() {
        cells = Array(this.cellCount) { arrayOfNulls(this.cellCount) }
        for (i in 0..<this.cellCount) {
            for (j in 0..<this.cellCount) {
                cells[i]!![j] = Cell(j * cellSize, i * cellSize, cellSize)
            }
        }
    }

    // True if the given cell coordinates are inside the bounds of the board
    private fun inBounds(x: Int, y: Int): Boolean {
        return x >= 0 && x < cellCount && y >= 0 && y < cellCount
    }

    // True if the given piece at the given coordinates is not outside the bounds of the board
    private fun inBounds(piece: Piece, x: Int, y: Int): Boolean {
        return inBounds(x, y) && inBounds(x + piece.cellCols - 1, y + piece.cellRows - 1)
    }

    // This only tests for the piece on the given coordinates, not the whole board
    private fun canPutPiece(piece: Piece, x: Int, y: Int): Boolean {
        if (!inBounds(piece, x, y)) return false

        for (i in 0..<piece.cellRows) for (j in 0..<piece.cellCols) if (!cells[y + i]!![x + j]!!.isEmpty && piece.filled(i, j)) return false

        return true
    }

    // Returns true iff the piece was put on the board
    fun putPiece(piece: Piece, x: Int, y: Int): Boolean {
        if (!canPutPiece(piece, x, y)) return false

        lastPutPiecePos.set(piece.calculateGravityCenter())
        for (i in 0..<piece.cellRows) for (j in 0..<piece.cellCols) if (piece.filled(i, j)) cells[y + i]!![x + j]!!.set(piece.colorIndex)

        return true
    }

    fun draw(batch: Batch) {
        batch.transformMatrix = batch.transformMatrix.translate(pos.x, pos.y, 0f)

        for (i in 0..<cellCount) for (j in 0..<cellCount) cells[i]!![j]!!.draw(batch)

        var i = effects.size
        while (i-- != 0) {
            effects.get(i)!!.draw(batch)
            if (effects.get(i)!!.isDone) effects.removeIndex(i)
        }

        batch.transformMatrix = batch.transformMatrix.translate(-pos.x, -pos.y, 0f)
    }

    fun canPutPiece(piece: Piece): Boolean {
        for (i in 0..<cellCount) for (j in 0..<cellCount) if (canPutPiece(piece, j, i)) return true

        return false
    }

    fun putScreenPiece(piece: Piece): Boolean {
        // Convert the on screen coordinates of the piece to the local-board-space coordinates
        // This is done by subtracting the piece coordinates from the board coordinates
        val local = piece.pos.cpy().sub(pos)
        val x = MathUtils.round(local.x / piece.cellSize)
        val y = MathUtils.round(local.y / piece.cellSize)
        return putPiece(piece, x, y)
    }

    fun snapToGrid(piece: Piece, position: Vector2): Vector2 {
        // Snaps the given position (e.g. mouse) to the grid,
        // assuming piece wants to be put at the specified position.
        // If the piece was not on the grid, the original position is returned
        //
        // Logic to determine the x and y is a copy-paste from putScreenPiece
        val local = position.cpy().sub(pos)
        val x = MathUtils.round(local.x / piece.cellSize)
        val y = MathUtils.round(local.y / piece.cellSize)
        return if (canPutPiece(piece, x, y)) Vector2(pos.x + x * piece.cellSize, pos.y + y * piece.cellSize)
        else position
    }

    // This will clear both complete rows and columns, all at once.
    // The reason why we can't check first rows and then columns
    // (or vice versa) is because the following case (* filled, _ empty):
    //
    // 4x4 boardHeight    piece
    // _ _ * *      * *
    // _ * * *      *
    // * * _ _
    // * * _ _
    //
    // If the piece is put on the top left corner, all the cells will be cleared.
    // If we first cleared the columns, then the rows wouldn't have been cleared.
    fun clearComplete(effect: EffectFactory): Int {
        var clearCount = 0
        val clearedRows = BooleanArray(cellCount)
        val clearedCols = BooleanArray(cellCount)

        // Analyze rows and columns that will be cleared
        for (i in 0..<cellCount) {
            clearedRows[i] = true
            for (j in 0..<cellCount) {
                if (cells[i]!![j]!!.isEmpty) {
                    clearedRows[i] = false
                    break
                }
            }
            if (clearedRows[i]) clearCount++
        }
        for (j in 0..<cellCount) {
            clearedCols[j] = true
            for (i in 0..<cellCount) {
                if (cells[i]!![j]!!.isEmpty) {
                    clearedCols[j] = false
                    break
                }
            }
            if (clearedCols[j]) clearCount++
        }
        if (clearCount > 0) {
            // Do clear those rows and columns
            for (i in 0..<cellCount) {
                if (clearedRows[i]) {
                    for (j in 0..<cellCount) {
                        effects.add(effect.create(cells[i]!![j]!!, lastPutPiecePos))
                        cells[i]!![j]!!.set(-1)
                    }
                }
            }

            for (j in 0..<cellCount) {
                if (clearedCols[j]) {
                    for (i in 0..<cellCount) {
                        effects.add(effect.create(cells[i]!![j]!!, lastPutPiecePos))
                        cells[i]!![j]!!.set(-1)
                    }
                }
            }
        }

        return clearCount
    }

    fun clearAll(clearFromX: Int, clearFromY: Int, effect: EffectFactory) {
        val culprit = cells[clearFromY]!![clearFromX]!!.pos

        for (i in 0..<cellCount) {
            for (j in 0..<cellCount) {
                if (!cells[i]!![j]!!.isEmpty) {
                    effects.add(effect.create(cells[i]!![j]!!, culprit))
                    cells[i]!![j]!!.set(-1)
                }
            }
        }
    }

    fun effectsDone(): Boolean {
        return effects.size == 0
    }

    @Throws(IOException::class)
    override fun write(output: DataOutputStream) {
        // Cell count, cells in row-major order
        output.writeInt(cellCount)
        for (i in 0..<cellCount) for (j in 0..<cellCount) cells[i]!![j]!!.write(output)
    }

    @Throws(IOException::class)
    override fun read(input: DataInputStream) {
        // If the saved cell count does not match the current cell count,
        // then an IOException is thrown since the data saved was invalid
        val savedCellCount = input.readInt()
        if (savedCellCount != cellCount) throw IOException("Invalid cellCount saved.")

        for (i in 0..<cellCount) for (j in 0..<cellCount) cells[i]!![j]!!.read(input)
    }
}
