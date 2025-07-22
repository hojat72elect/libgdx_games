package dev.lonami.klooni.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import dev.lonami.klooni.Klooni;

class BonusParticle {

    private final static float SPEED = 1f;
    private final Label label;
    private float lifetime;

    BonusParticle(final Vector2 pos, final int score, final Label.LabelStyle style) {
        label = new Label("+" + score, style);
        label.setBounds(pos.x, pos.y, 0, 0);
    }

    void run(final Batch batch) {
        // Update
        lifetime += SPEED * Gdx.graphics.getDeltaTime();
        if (lifetime > 1f)
            lifetime = 1f;

        // Render
        label.setColor(Klooni.theme.bonus);
        label.setFontScale(Interpolation.elasticOut.apply(0f, 1f, lifetime));
        float opacity = Interpolation.linear.apply(1f, 0f, lifetime);
        label.draw(batch, opacity);
    }

    boolean done() {
        return lifetime >= 1f;
    }
}
