package dev.lonami.klooni.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import dev.lonami.klooni.Klooni
import dev.lonami.klooni.serializer.BinSerializable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

// Represents a single cell, with a position, size and color.
// Instances will use the cell texture provided by the currently used skin.
class Cell internal constructor(x: Float, y: Float, cellSize: Float) : BinSerializable {
    @JvmField
    val pos: Vector2

    @JvmField
    val size: Float

    // Negative index indicates that the cell is empty
    private var colorIndex: Int

    init {
        pos = Vector2(x, y)
        size = cellSize

        colorIndex = -1
    }

    // Sets the cell to be non-empty and of the specified color index
    fun set(ci: Int) {
        colorIndex = ci
    }

    fun draw(batch: Batch) {
        // Always query the color to the theme, because it might have changed
        draw(Klooni.theme!!.getCellColor(colorIndex), batch, pos.x, pos.y, size)
    }

    val colorCopy: Color?
        get() = Klooni.theme!!.getCellColor(colorIndex)!!.cpy()

    val isEmpty: Boolean
        get() = colorIndex < 0

    @Throws(IOException::class)
    override fun write(output: DataOutputStream) {
        // Only the color index is saved
        output.writeInt(colorIndex)
    }

    @Throws(IOException::class)
    override fun read(input: DataInputStream) {
        colorIndex = input.readInt()
    }

    companion object {
        // Default texture (don't call overloaded version to avoid overhead)
        @JvmStatic
        fun draw(
            color: Color?, batch: Batch,
            x: Float, y: Float, size: Float
        ) {
            batch.color = color
            batch.draw(Klooni.theme!!.cellTexture, x, y, size, size)
        }

        // Custom texture
        fun draw(
            texture: Texture?, color: Color?, batch: Batch,
            x: Float, y: Float, size: Float
        ) {
            batch.color = color
            batch.draw(texture, x, y, size, size)
        }
    }
}
