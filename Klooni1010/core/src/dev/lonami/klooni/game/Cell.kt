package dev.lonami.klooni.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.serializer.BinSerializable
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Represents a single cell of the game board, with a position, size and color.
 * Instances will use the cell texture provided by the currently used skin.
 */
class Cell(val x: Float, val y: Float, @JvmField val cellSize: Float) : BinSerializable {

    // the position of each cell on board
    @JvmField
    val position = Vector2(x, y)

    // Negative index indicates that the cell is empty
    private var _colorIndex = -1

    // Sets the cell to be non-empty and of the specified color index
    fun set(colorIndex: Int) {
        _colorIndex = colorIndex
    }

    fun draw(batch: Batch) {
        // Always query the color to the theme, because it might have changed
        draw(Klooni.theme.getCellColor(_colorIndex), batch, position.x, position.y, cellSize)
    }

    fun getColorCopy(): Color = Klooni.theme.getCellColor(_colorIndex).cpy()

    fun isEmpty() = _colorIndex < 0

    override fun write(outputStream: DataOutputStream) {
        // Only the color index is saved
        outputStream.writeInt(_colorIndex)
    }

    override fun read(inputStream: DataInputStream) {
        _colorIndex = inputStream.readInt()
    }

    companion object {

        // Default texture (don't call overloaded version to avoid overhead)
        @JvmStatic
        fun draw(color: Color, batch: Batch, x: Float, y: Float, size: Float) {
            batch.color = color
            batch.draw(Klooni.theme.cellTexture, x, y, size, size)
        }

        @JvmStatic
        fun draw(
            texture: Texture, color: Color, batch: Batch,
            x: Float, y: Float, size: Float
        ) {
            batch.color = color
            batch.draw(texture, x, y, size, size)
        }
    }
}