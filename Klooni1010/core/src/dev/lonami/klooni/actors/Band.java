package dev.lonami.klooni.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import dev.lonami.klooni.Klooni;
import dev.lonami.klooni.Theme;
import dev.lonami.klooni.game.BaseScorer;
import dev.lonami.klooni.game.GameLayout;

// Horizontal band, used to show the score on the pause menu
public class Band extends Actor {

    public final Rectangle scoreBounds;
    public final Rectangle infoBounds;
    private final BaseScorer scorer;
    private final Texture bandTexture;
    private final Label infoLabel;
    private final Label scoreLabel;

    public Band(final Klooni game, final GameLayout layout, final BaseScorer scorer) {
        this.scorer = scorer;
        bandTexture = Theme.getBlankTexture();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.skin.getFont("font");

        scoreLabel = new Label("", labelStyle);
        scoreLabel.setAlignment(Align.center);
        infoLabel = new Label("pause menu", labelStyle);
        infoLabel.setAlignment(Align.center);

        scoreBounds = new Rectangle();
        infoBounds = new Rectangle();
        layout.update(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO This is not the best way to apply the transformation, but, oh well
        float x = getParent().getX();
        float y = getParent().getY();

        // TODO For some strange reason, the texture coordinates and label coordinates are different
        Vector2 pos = localToStageCoordinates(new Vector2(x, y));
        batch.setColor(Klooni.theme.bandColor);
        batch.draw(bandTexture, pos.x, pos.y, getWidth(), getHeight());

        scoreLabel.setBounds(x + scoreBounds.x, y + scoreBounds.y, scoreBounds.width, scoreBounds.height);
        scoreLabel.setText(Integer.toString(scorer.getCurrentScore()));
        scoreLabel.setColor(Klooni.theme.textColor);
        scoreLabel.draw(batch, parentAlpha);

        infoLabel.setBounds(x + infoBounds.x, y + infoBounds.y, infoBounds.width, infoBounds.height);
        infoLabel.setColor(Klooni.theme.textColor);
        infoLabel.draw(batch, parentAlpha);
    }

    // Once game over is set on the menu, it cannot be reverted
    public void setMessage(final String message) {
        if (!message.equals(""))
            infoLabel.setText(message);
    }

}
