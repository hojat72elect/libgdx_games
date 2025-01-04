package dev.lonami.klooni.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.lonami.klooni.SkinLoader;
import dev.lonami.klooni.game.Cell;
import dev.lonami.klooni.interfaces.IEffect;
import dev.lonami.klooni.interfaces.IEffectFactory;


public class WaterdropEffectFactory implements IEffectFactory {
    private Texture dropTexture;


    private void init() {
        if (dropTexture == null)
            dropTexture = SkinLoader.loadPng("cells/drop.png");
    }

    @Override
    public String getName() {
        return "waterdrop";
    }

    @Override
    public String getDisplay() {
        return "Waterdrop";
    }

    @Override
    public int getPrice() {
        return 200;
    }

    @Override
    public IEffect create(Cell deadCell, Vector2 culprit) {
        init();
        IEffect effect = new WaterdropEffect();
        effect.setInfo(deadCell, culprit);
        return effect;
    }


    private class WaterdropEffect implements IEffect {
        private static final float FALL_ACCELERATION = 500.0f;
        private static final float FALL_VARIATION = 50.0f;
        private static final float COLOR_SPEED = 7.5f;
        private final float fallAcceleration;
        private Vector2 pos;
        private boolean dead;
        private Color cellColor;
        private Color dropColor;
        private float cellSize;
        private float fallSpeed;

        WaterdropEffect() {
            fallAcceleration = FALL_ACCELERATION + MathUtils.random(-FALL_VARIATION, FALL_VARIATION);
        }

        @Override
        public void setInfo(Cell deadCell, Vector2 culprit) {
            pos = deadCell.pos.cpy();
            cellSize = deadCell.size;
            cellColor = deadCell.getColorCopy();
            dropColor = new Color(cellColor.r, cellColor.g, cellColor.b, 0.0f);
        }

        @Override
        public void draw(Batch batch) {
            final float dt = Gdx.graphics.getDeltaTime();
            fallSpeed += fallAcceleration * dt;
            pos.y -= fallSpeed * dt;

            cellColor.set(
                    cellColor.r, cellColor.g, cellColor.b,
                    Math.max(cellColor.a - COLOR_SPEED * dt, 0.0f)
            );
            dropColor.set(
                    cellColor.r, cellColor.g, cellColor.b,
                    Math.min(dropColor.a + COLOR_SPEED * dt, 1.0f)
            );

            Cell.draw(cellColor, batch, pos.x, pos.y, cellSize);
            Cell.draw(dropTexture, dropColor, batch, pos.x, pos.y, cellSize);

            final Vector3 translation = batch.getTransformMatrix().getTranslation(new Vector3());
            dead = translation.y + pos.y + dropTexture.getHeight() < 0;
        }

        @Override
        public boolean isDone() {
            return dead;
        }
    }
}
