package dev.lonami.klooni.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.game.Cell.Companion.draw
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

/**
 * Represents a piece with an arbitrary shape, which can be either rectangles (including squares) or L shaped, with any rotation.
 */
class Piece {
    @JvmField
    val colorIndex: Int

    @JvmField
    val cellCols: Int

    @JvmField
    val cellRows: Int

    @JvmField
    val pos: Vector2
    private val rotation: Int
    private val shape: Array<BooleanArray>

    // Default arbitrary value
    @JvmField
    var cellSize: Float = 10f

    /**
     * Rectangle-shaped constructor
     *
     *
     * If swapSize is true, the rows and columns will be swapped.
     * colorIndex represents a random index that will be used
     * to determine the color of this piece when drawn on the screen.
     */
    private constructor(cols: Int, rows: Int, rotateSizeBy: Int, colorIndex: Int) {
        this.colorIndex = colorIndex

        pos = Vector2()
        rotation = rotateSizeBy % 2
        cellCols = if (rotation == 1) rows else cols
        cellRows = if (rotation == 1) cols else rows

        shape = Array(cellRows) { BooleanArray(cellCols) }
        for (i in 0 until cellRows) {
            for (j in 0 until cellCols) {
                shape[i][j] = true
            }
        }
    }

    // L-shaped constructor
    private constructor(lSize: Int, rotateCount: Int, colorIndex: Int) {
        this.colorIndex = colorIndex

        pos = Vector2()
        cellRows = lSize
        cellCols = cellRows
        shape = Array(lSize) { BooleanArray(lSize) }

        rotation = rotateCount % 4
        when (rotation) {
            0 -> {
                var j = 0
                while (j < lSize) {
                    shape[0][j] = true
                    ++j
                }
                var i = 0
                while (i < lSize) {
                    shape[i][0] = true
                    ++i
                }
            }

            1 -> {
                var j = 0
                while (j < lSize) {
                    shape[0][j] = true
                    ++j
                }
                var i = 0
                while (i < lSize) {
                    shape[i][lSize - 1] = true
                    ++i
                }
            }

            2 -> {
                var j = 0
                while (j < lSize) {
                    shape[lSize - 1][j] = true
                    ++j
                }
                var i = 0
                while (i < lSize) {
                    shape[i][lSize - 1] = true
                    ++i
                }
            }

            3 -> {
                var j = 0
                while (j < lSize) {
                    shape[lSize - 1][j] = true
                    ++j
                }
                var i = 0
                while (i < lSize) {
                    shape[i][0] = true
                    ++i
                }
            }
        }
    }

    fun draw(batch: SpriteBatch) {
        val c = Klooni.theme.getCellColor(colorIndex)
        for (i in 0 until cellRows) for (j in 0 until cellCols) if (shape[i][j]) draw(c, batch, pos.x + j * cellSize, pos.y + i * cellSize, cellSize)
    }

    val rectangle: Rectangle
        // Calculates the rectangle of the piece with screen coordinates
        get() = Rectangle(pos.x, pos.y, cellCols * cellSize, cellRows * cellSize)

    // Determines whether the shape is filled on the given row and column
    fun filled(i: Int, j: Int): Boolean {
        return shape[i][j]
    }

    // Calculates the area occupied by the shape
    fun calculateArea(): Int {
        var area = 0
        for (i in 0 until cellRows) {
            for (j in 0 until cellCols) {
                if (shape[i][j]) {
                    area++
                }
            }
        }
        return area
    }

    // Calculates the gravity center of the piece shape
    fun calculateGravityCenter(): Vector2 {
        var filledCount = 0
        val result = Vector2()
        for (i in 0 until cellRows) {
            for (j in 0 until cellCols) {
                if (shape[i][j]) {
                    filledCount++
                    result.add(
                        pos.x + j * cellSize - cellSize * 0.5f,
                        pos.y + i * cellSize - cellSize * 0.5f
                    )
                }
            }
        }
        return result.scl(1f / filledCount)
    }

    @Throws(IOException::class)
    fun write(out: DataOutputStream) {
        // colorIndex, rotation
        out.writeInt(colorIndex)
        out.writeInt(rotation)
    }

    companion object {
        // Generates a random piece with always the same color for the generated shape
        @JvmStatic
        fun random(): Piece {
            // 9 pieces [0…8]; 4 possible rotations [0…3]
            return fromIndex(MathUtils.random(8), MathUtils.random(4))
        }

        private fun fromIndex(colorIndex: Int, rotateCount: Int): Piece {
            when (colorIndex) {
                0 -> return Piece(1, 1, 0, colorIndex)
                1 -> return Piece(2, 2, 0, colorIndex)
                2 -> return Piece(3, 3, 0, colorIndex)

                3 -> return Piece(1, 2, rotateCount, colorIndex)
                4 -> return Piece(1, 3, rotateCount, colorIndex)
                5 -> return Piece(1, 4, rotateCount, colorIndex)
                6 -> return Piece(1, 5, rotateCount, colorIndex)

                7 -> return Piece(2, rotateCount, colorIndex)
                8 -> return Piece(3, rotateCount, colorIndex)
            }
            throw RuntimeException("Random function is broken.")
        }

        @JvmStatic
        @Throws(IOException::class)
        fun read(`in`: DataInputStream): Piece {
            return fromIndex(`in`.readInt(), `in`.readInt())
        }
    }
}
