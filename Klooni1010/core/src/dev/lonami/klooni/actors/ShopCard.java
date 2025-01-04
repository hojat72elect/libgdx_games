package dev.lonami.klooni.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import dev.lonami.klooni.Klooni;
import dev.lonami.klooni.Theme;
import dev.lonami.klooni.game.GameLayout;

public abstract class ShopCard extends Actor {

    public final Rectangle nameBounds;
    public final Rectangle priceBounds;
    final Klooni game;
    final Label priceLabel;
    private final Label nameLabel;
    public float cellSize;

    ShopCard(final Klooni game, final GameLayout layout,
             final String itemName, final Color backgroundColor
    ) {
        this.game = game;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.skin.getFont("font_small");

        priceLabel = new Label("", labelStyle);
        nameLabel = new Label(itemName, labelStyle);

        Color labelColor = Theme.shouldUseWhite(backgroundColor) ? Color.WHITE : Color.BLACK;
        priceLabel.setColor(labelColor);
        nameLabel.setColor(labelColor);

        priceBounds = new Rectangle();
        nameBounds = new Rectangle();

        layout.update(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        final float x = getX(), y = getY();
        nameLabel.setBounds(x + nameBounds.x, y + nameBounds.y, nameBounds.width, nameBounds.height);
        nameLabel.draw(batch, parentAlpha);

        priceLabel.setBounds(x + priceBounds.x, y + priceBounds.y, priceBounds.width, priceBounds.height);
        priceLabel.draw(batch, parentAlpha);
    }

    // Showcases the current effect (the shop will be showcasing them, one by one)
    // This method should be called on the same card as long as it returns true.
    // It should return false once it's done so that the next card can be showcased.
    public boolean showcase(Batch batch, float yDisplacement) {
        return false;
    }

    public abstract void usedItemUpdated();

    public abstract void use();

    public abstract boolean isBought();

    public abstract boolean isUsed();

    public abstract float getPrice();

    public abstract void performBuy();
}
