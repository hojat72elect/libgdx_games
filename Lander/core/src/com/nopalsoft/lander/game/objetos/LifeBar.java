package com.nopalsoft.lander.game.objetos;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.lander.Assets;

public class LifeBar extends Actor {

	public float maxlife;
	public float actualLife;

	public LifeBar(float maxLife, float actualLife, float x, float y, float width, float height) {
		this.setBounds(x, y, width, height);
		this.maxlife = maxLife;
		this.actualLife = actualLife;
	}

	public LifeBar(float maxLife, float x, float y, float width, float height) {
		this.setBounds(x, y, width, height);
		this.maxlife = maxLife;
		this.actualLife = maxLife;
	}

	/**
	 * Si utilizo una tabla, el ancho y la altura se sobreescriben
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
