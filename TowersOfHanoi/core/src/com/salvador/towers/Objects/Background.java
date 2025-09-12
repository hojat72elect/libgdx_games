package com.salvador.towers.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Background extends Actor {

    private Texture texture;

    public Background(float x, float y) {
        setSize(x, y);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texture != null) {
            batch.draw(texture, getX(), getY());
        }
    }
}
