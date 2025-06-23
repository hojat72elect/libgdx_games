package com.nopalsoft.zombiewars.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiewars.Assets;

public class Bullet {
	public final static int STATE_MUZZLE = 0;
	public final static int STATE_NORMAL = 1;
	public final static int STATE_HIT = 2;
	public final static int STATE_DESTROY = 3;
	public int state;

	public static float VELOCIDAD = 5;

	public final static float DURATION_MUZZLE = Assets.muzzle.animationDuration;
	public final int DAMAGE;

	public Vector2 position;
	public float stateTime;

	public boolean isFacingLeft;
	public boolean isVisible;

	public final Personajes oPerWhoFired;// the one who fired the bullet
	final Body body;

	public Bullet(Body body, Personajes oPerWhoFired) {
		this.body = body;
		position = new Vector2(body.getPosition().x, body.getPosition().y);
		this.oPerWhoFired = oPerWhoFired;
		state = STATE_MUZZLE;

		this.isFacingLeft = oPerWhoFired.isFacingLeft;
		this.DAMAGE = oPerWhoFired.DAMAGE;

		if (isFacingLeft)
			body.setLinearVelocity(-VELOCIDAD, 0);
		else
			body.setLinearVelocity(VELOCIDAD, 0);

		if (oPerWhoFired.tipo == Personajes.TIPO_RANGO)
			isVisible = true;
		else
			isVisible = false;

	}

	public void update(float delta) {
		if (state == STATE_MUZZLE || state == STATE_NORMAL) {
			position.x = body.getPosition().x;
			position.y = body.getPosition().y;

			// Si es invisible no pueede avanzar mucho la bala si no hay nada enfente
			if (!isVisible) {
				if (oPerWhoFired.position.dst(position) > oPerWhoFired.DISTANCE_ATTACK + .025f)
					hit();
			}
		}

		if (state == STATE_HIT)
			body.setLinearVelocity(0, 0);

		if (state == STATE_MUZZLE || state == STATE_HIT) {
			stateTime += delta;
			if (stateTime >= DURATION_MUZZLE) {
				if (state == STATE_MUZZLE)
					state = STATE_NORMAL;
				else if (state == STATE_HIT)
					state = STATE_DESTROY;
				stateTime = 0;
			}
			return;
		}

		stateTime += delta;
	}

	public void hit() {
		state = STATE_HIT;
		stateTime = 0;
	}

}
