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

/**
 * Represents a single cell of the game board, with a position, size and color.
 * Instances will use the cell texture provided by the currently used skin.
 */
public class Cell implements BinSerializable {

    public final Vector2 position;
    public final float size;
    private int _colorIndex; // Negative index indicates that the cell is empty

    Cell(float x, float y, float cellSize) {
        position = new Vector2(x, y);
        size = cellSize;
        _colorIndex = -1;
    }

    // Default texture (don't call overloaded version to avoid overhead)
    public static void draw(final Color color, final Batch batch,
                            final float x, final float y, final float size
    ) {
        batch.setColor(color);
        batch.draw(Klooni.theme.cellTexture, x, y, size, size);
    }

    public static void draw(final Texture texture, final Color color, final Batch batch,
                            final float x, final float y, final float size
    ) {
        batch.setColor(color);
        batch.draw(texture, x, y, size, size);
    }

    // Sets the cell to be non-empty and of the specified color index
    public void set(int colorIndex) {
        _colorIndex = colorIndex;
    }

    public void draw(Batch batch) {
        // Always query the color to the theme, because it might have changed
        draw(Klooni.theme.getCellColor(_colorIndex), batch, position.x, position.y, size);
    }

    public Color getColorCopy() {
        return Klooni.theme.getCellColor(_colorIndex).cpy();
    }

    boolean isEmpty() {
        return _colorIndex < 0;
    }

    @Override
    public void write(DataOutputStream outputStream) throws IOException {
        // Only the color index is saved
        outputStream.writeInt(_colorIndex);
    }

    @Override
    public void read(DataInputStream inputStream) throws IOException {
        _colorIndex = inputStream.readInt();
    }
}
