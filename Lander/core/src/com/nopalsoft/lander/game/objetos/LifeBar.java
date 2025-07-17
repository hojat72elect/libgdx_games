package com.nopalsoft.lander.game.objetos;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.lander.Assets;

public class LifeBar extends Actor {

    public float maxlife;
    public float actualLife;

    /**
     * If I use a table, the width and height are overwritten
     */
    public LifeBar(float maxLife) {
        this.maxlife = maxLife;
        this.actualLife = maxLife;
    }

    public void updateActualLife(float actualLife) {
        this.actualLife = actualLife;

        if (actualLife > maxlife)
            maxlife = actualLife;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(Assets.barraMarcadorRojo, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        if (actualLife > 0)
            batch.draw(Assets.barraMarcadorVerde, this.getX(), this.getY(), this.getWidth() * (actualLife / maxlife), this.getHeight());
    }
}
