package dev.lonami.klooni.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.lonami.klooni.Klooni;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// Represents a piece with an arbitrary shape, which
// can be either rectangles (squares too) or L shaped
// with any rotation.
public class Piece {


    public final int colorIndex;
    public final int cellCols, cellRows;
    final Vector2 pos;
    private final int rotation;
    private final boolean[][] shape;

    // Default arbitrary value
    float cellSize = 10f;
    //region Constructors

    // Rectangle-shaped constructor
    //
    // If swapSize is true, the rows and columns will be swapped.
    // colorIndex represents a random index that will be used
    // to determine the color of this piece when drawn on the screen.
    private Piece(int cols, int rows, int rotateSizeBy, int colorIndex) {
        this.colorIndex = colorIndex;

        pos = new Vector2();
        rotation = rotateSizeBy % 2;
        cellCols = rotation == 1 ? rows : cols;
        cellRows = rotation == 1 ? cols : rows;

        shape = new boolean[cellRows][cellCols];
        for (int i = 0; i < cellRows; ++i) {
            for (int j = 0; j < cellCols; ++j) {
                shape[i][j] = true;
            }
        }
    }

    // L-shaped constructor
    private Piece(int lSize, int rotateCount, int colorIndex) {
        this.colorIndex = colorIndex;

        pos = new Vector2();
        cellCols = cellRows = lSize;
        shape = new boolean[lSize][lSize];

        rotation = rotateCount % 4;
        switch (rotation) {
            case 0: // ┌
                for (int j = 0; j < lSize; ++j)
                    shape[0][j] = true;
                for (int i = 0; i < lSize; ++i)
                    shape[i][0] = true;
                break;
            case 1: // ┐
                for (int j = 0; j < lSize; ++j)
                    shape[0][j] = true;
                for (int i = 0; i < lSize; ++i)
                    shape[i][lSize - 1] = true;
                break;
            case 2: // ┘
                for (int j = 0; j < lSize; ++j)
                    shape[lSize - 1][j] = true;
                for (int i = 0; i < lSize; ++i)
                    shape[i][lSize - 1] = true;
                break;
            case 3: // └
                for (int j = 0; j < lSize; ++j)
                    shape[lSize - 1][j] = true;
                for (int i = 0; i < lSize; ++i)
                    shape[i][0] = true;
                break;
        }
    }
    //region Static methods

    // Generates a random piece with always the same color for the generated shape
    public static Piece random() {
        // 9 pieces [0…8]; 4 possible rotations [0…3]
        return fromIndex(MathUtils.random(8), MathUtils.random(4));
    }

    private static Piece fromIndex(int colorIndex, int rotateCount) {
        switch (colorIndex) {
            // Squares
            case 0:
                return new Piece(1, 1, 0, colorIndex);
            case 1:
                return new Piece(2, 2, 0, colorIndex);
            case 2:
                return new Piece(3, 3, 0, colorIndex);

            // Lines
            case 3:
                return new Piece(1, 2, rotateCount, colorIndex);
            case 4:
                return new Piece(1, 3, rotateCount, colorIndex);
            case 5:
                return new Piece(1, 4, rotateCount, colorIndex);
            case 6:
                return new Piece(1, 5, rotateCount, colorIndex);

            // L's
            case 7:
                return new Piece(2, rotateCount, colorIndex);
            case 8:
                return new Piece(3, rotateCount, colorIndex);
        }
        throw new RuntimeException("Random function is broken.");
    }
    //region Package local methods

    static Piece read(DataInputStream in) throws IOException {
        return fromIndex(in.readInt(), in.readInt());
    }

    void draw(SpriteBatch batch) {
        final Color c = Klooni.theme.getCellColor(colorIndex);
        for (int i = 0; i < cellRows; ++i)
            for (int j = 0; j < cellCols; ++j)
                if (shape[i][j])
                    Cell.draw(c, batch, pos.x + j * cellSize, pos.y + i * cellSize, cellSize);
    }

    // Calculates the rectangle of the piece with screen coordinates
    Rectangle getRectangle() {
        return new Rectangle(pos.x, pos.y, cellCols * cellSize, cellRows * cellSize);
    }

    // Determines whether the shape is filled on the given row and column
    boolean filled(int i, int j) {
        return shape[i][j];
    }

    // Calculates the area occupied by the shape
    int calculateArea() {
        int area = 0;
        for (int i = 0; i < cellRows; ++i) {
            for (int j = 0; j < cellCols; ++j) {
                if (shape[i][j]) {
                    area++;
                }
            }
        }
        return area;
    }
    //region Serialization

    // Calculates the gravity center of the piece shape
    Vector2 calculateGravityCenter() {
        int filledCount = 0;
        Vector2 result = new Vector2();
        for (int i = 0; i < cellRows; ++i) {
            for (int j = 0; j < cellCols; ++j) {
                if (shape[i][j]) {
                    filledCount++;
                    result.add(
                            pos.x + j * cellSize - cellSize * 0.5f,
                            pos.y + i * cellSize - cellSize * 0.5f
                    );
                }
            }
        }
        return result.scl(1f / filledCount);
    }

    void write(DataOutputStream out) throws IOException {
        // colorIndex, rotation
        out.writeInt(colorIndex);
        out.writeInt(rotation);
    }

    
}
