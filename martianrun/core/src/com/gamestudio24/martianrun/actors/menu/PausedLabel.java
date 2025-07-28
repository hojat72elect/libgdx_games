
package com.gamestudio24.martianrun.actors.menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gamestudio24.martianrun.enums.GameState;
import com.gamestudio24.martianrun.utils.AssetsManager;
import com.gamestudio24.martianrun.utils.Constants;
import com.gamestudio24.martianrun.utils.GameManager;

public class PausedLabel extends Actor {

    private final Rectangle bounds;
    private final BitmapFont font;

    public PausedLabel(Rectangle bounds) {
        this.bounds = bounds;
        setWidth(bounds.width);
        setHeight(bounds.height);
        font = AssetsManager.getSmallFont();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (GameManager.getInstance().getGameState() == GameState.PAUSED) {
            font.drawWrapped(batch, Constants.PAUSED_LABEL, bounds.x, bounds.y, bounds.width,
                    BitmapFont.HAlignment.CENTER);
        }
    }

}
