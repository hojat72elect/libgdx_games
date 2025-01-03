package com.nopalsoft.zombiekiller.objetos;

import com.badlogic.gdx.math.Vector2;

public class Pisable {

	public final Vector2 position;

	public final float width, height;

	public Pisable(float x, float y, float width, float height) {
		position = new Vector2(x, y);
		this.width = width;
		this.height = height;
	}

}
