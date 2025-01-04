package dev.lonami.klooni.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import dev.lonami.klooni.Klooni;
import dev.lonami.klooni.Theme;
import dev.lonami.klooni.game.Cell;
import dev.lonami.klooni.game.GameLayout;

// Card-like actor used to display information about a given theme
public class ThemeCard extends ShopCard {


    private final static int[][] colorsUsed = {
            {0, 7, 7},
            {8, 7, 3},
            {8, 8, 3}
    };
    private final Theme theme;
    private final Texture background;
    //region Constructor

    public ThemeCard(final Klooni game, final GameLayout layout, final Theme theme) {
        super(game, layout, theme.getDisplay(), theme.background);
        background = Theme.getBlankTexture();

        this.theme = theme;
        usedItemUpdated();
    }
    //region Public methods

    @Override
    public void draw(Batch batch, float parentAlpha) {
        final float x = getX(), y = getY();

        batch.setColor(theme.background);
        batch.draw(background, x, y, getWidth(), getHeight());

        // Avoid drawing on the borders by adding +1 cell padding
        for (int i = 0; i < colorsUsed.length; ++i) {
            for (int j = 0; j < colorsUsed[i].length; ++j) {
                Cell.draw(theme.cellTexture, theme.getCellColor(colorsUsed[i][j]), batch,
                        x + cellSize * (j + 1), y + cellSize * (i + 1), cellSize
                );
            }
        }

        super.draw(batch, parentAlpha);
    }

    @Override
    public void usedItemUpdated() {
        if (Klooni.theme.getName().equals(theme.getName()))
            priceLabel.setText("currently used");
        else if (Klooni.isThemeBought(theme))
            priceLabel.setText("bought");
        else
            priceLabel.setText("buy for " + theme.getPrice());
    }

    @Override
    public void use() {
        Klooni.updateTheme(theme);
        usedItemUpdated();
    }

    @Override
    public boolean isBought() {
        return Klooni.isThemeBought(theme);
    }

    @Override
    public boolean isUsed() {
        return Klooni.theme.getName().equals(theme.getName());
    }

    @Override
    public float getPrice() {
        return theme.getPrice();
    }

    @Override
    public void performBuy() {
        if (Klooni.buyTheme(theme)) {
            use();
        }
    }

    
}
