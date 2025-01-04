package dev.lonami.klooni.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import dev.lonami.klooni.Klooni;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import dev.lonami.klooni.serializer.BinSerializable;

// Represents a single cell, with a position, size and color.
// Instances will use the cell texture provided by the currently used skin.
public class Cell implements BinSerializable {


    public final Vector2 pos;
    public final float size;
    // Negative index indicates that the cell is empty
    private int colorIndex;
    //region Constructor

    Cell(float x, float y, float cellSize) {
        pos = new Vector2(x, y);
        size = cellSize;

        colorIndex = -1;
    }
    //region Package local methods

    // Default texture (don't call overloaded version to avoid overhead)
    public static void draw(final Color color, final Batch batch,
                            final float x, final float y, final float size
    ) {
        batch.setColor(color);
        batch.draw(Klooni.theme.cellTexture, x, y, size, size);
    }

    // Custom texture
    public static void draw(final Texture texture, final Color color, final Batch batch,
                            final float x, final float y, final float size
    ) {
        batch.setColor(color);
        batch.draw(texture, x, y, size, size);
    }

    // Sets the cell to be non-empty and of the specified color index
    public void set(int ci) {
        colorIndex = ci;
    }

    public void draw(Batch batch) {
        // Always query the color to the theme, because it might have changed
        draw(Klooni.theme.getCellColor(colorIndex), batch, pos.x, pos.y, size);
    }
    //region Static methods

    public Color getColorCopy() {
        return Klooni.theme.getCellColor(colorIndex).cpy();
    }

    boolean isEmpty() {
        return colorIndex < 0;
    }
    //region Serialization

    @Override
    public void write(DataOutputStream outputStream) throws IOException {
        // Only the color index is saved
        outputStream.writeInt(colorIndex);
    }

    @Override
    public void read(DataInputStream inputStream) throws IOException {
        colorIndex = inputStream.readInt();
    }
}
