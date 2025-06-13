package com.nopalsoft.dragracer.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.dragracer.Assets;

public class SpeedBar extends Actor {

    public float maxLife;
    public float actualLife;

    public SpeedBar(float maxLife, float x, float y, float width, float height) {
        this.setBounds(x, y, width, height);
        this.maxLife = maxLife;
        this.actualLife = maxLife;
    }

    public void updateActualLife(float actualLife) {
        if (actualLife >= maxLife) {
            actualLife = maxLife;
        }
        this.actualLife = actualLife;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(Assets.redMarkerBar, this.getX(), this.getY(),
                this.getWidth(), this.getHeight());
        if (actualLife > 0)
            batch.draw(Assets.greenMarkerBar, this.getX(), this.getY(),
                    this.getWidth() * (actualLife / maxLife), this.getHeight());
    }
}
