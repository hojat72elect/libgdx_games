package com.salvador.towers.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RectBase extends Actor {

    private final float x;
    private final float y;
    private final float w;
    private final float h;

    private final Texture txeRect;

    public RectBase(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        txeRect = new Texture(Gdx.files.internal("rect.png"));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(txeRect, x, y, w, h);
    }
}
