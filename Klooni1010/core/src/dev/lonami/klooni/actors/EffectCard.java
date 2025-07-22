package dev.lonami.klooni.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

import dev.lonami.klooni.Klooni;
import dev.lonami.klooni.Theme;
import dev.lonami.klooni.game.Board;
import dev.lonami.klooni.game.GameLayout;
import dev.lonami.klooni.game.Piece;
import dev.lonami.klooni.interfaces.IEffectFactory;

// Card-like actor used to display information about a given theme
public class EffectCard extends ShopCard {

    //region Members

    private final IEffectFactory effect;
    private final Board board;
    private final Texture background;
    // We want to create an effect from the beginning
    private boolean needCreateEffect = true;

    //endregion

    //region Constructor

    public EffectCard(final Klooni game, final GameLayout layout, final IEffectFactory effect) {
        super(game, layout, effect.getDisplay(), Klooni.theme.background);
        background = Theme.getBlankTexture();
        this.effect = effect;

        // Let the board have room for 3 cells, so cellSize * 3
        board = new Board(new Rectangle(0, 0, cellSize * 3, cellSize * 3), 3);

        setRandomPiece();
        usedItemUpdated();
    }

    private void setRandomPiece() {
        while (true) {
            final Piece piece = Piece.random();
            if (piece.cellCols > 3 || piece.cellRows > 3)
                continue;

            // Try to center it (max size is 3, so center is the second grid bit unless max size)
            int x = piece.cellCols == 3 ? 0 : 1;
            int y = piece.cellRows == 3 ? 0 : 1;
            if (board.putPiece(piece, x, y))
                break; // Should not fail, but if it does, don't break
        }
    }

    //endregion

    //region Public methods

    @Override
    public void draw(Batch batch, float parentAlpha) {
        final float x = getX(), y = getY();

        batch.setColor(Klooni.theme.background);
        batch.draw(background, x, y, getWidth(), getHeight());

        // Avoid drawing on the borders by adding +1 cell padding
        board.pos.set(x + cellSize * 1, y + cellSize * 1);

        // Draw only if effects are done, i.e. not showcasing
        if (board.effectsDone())
            board.draw(batch);

        super.draw(batch, parentAlpha);
    }

    @Override
    public boolean showcase(Batch batch, float yDisplacement) {
        board.pos.y += yDisplacement;

        // If no effect is running
        if (board.effectsDone()) {
            // And we want to create a new one
            if (needCreateEffect) {
                // Clear at cells[1][1], the center one
                board.clearAll(1, 1, effect);
                needCreateEffect = false;
            } else {
                // Otherwise, the previous effect finished, so return false because we're done
                // We also want to draw the next time so set the flag to true
                setRandomPiece();
                needCreateEffect = true;
                return false;
            }
        }

        board.draw(batch);
        return true;
    }

    @Override
    public void usedItemUpdated() {
        if (game.effect.getName().equals(effect.getName()))
            priceLabel.setText("currently used");
        else if (Klooni.isEffectBought(effect))
            priceLabel.setText("bought");
        else
            priceLabel.setText("buy for " + effect.getPrice());
    }

    @Override
    public void use() {
        game.updateEffect(effect);
        usedItemUpdated();
    }

    @Override
    public boolean isBought() {
        return Klooni.isEffectBought(effect);
    }

    @Override
    public boolean isUsed() {
        return game.effect.getName().equals(effect.getName());
    }

    @Override
    public float getPrice() {
        return effect.getPrice();
    }

    @Override
    public void performBuy() {
        if (Klooni.buyEffect(effect)) {
            use();
        }
    }

    //endregion
}
