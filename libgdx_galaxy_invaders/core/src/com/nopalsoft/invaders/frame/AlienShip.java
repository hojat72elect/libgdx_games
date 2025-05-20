package com.nopalsoft.invaders.frame;

public class AlienShip extends DynamicGameObject {

	public static final float RADIUS = 1.5f;

	public static final float DRAW_WIDTH = 3.5f;
	public static final float DRAW_HEIGHT = 3.5f;

	public static final int MOVE_SIDES = 0;
	public static final int MOVE_DOWN = 2;
	public static final int EXPLOTING = 3;
	public static final float SPEED = 4f;
	public static final float SPEED_DOWN = -3.5f;

	public static final float MOVE_RANGO_SIDES = 6.7f;
	public static final float MOVE_RANGO_DOWN = 1.2f;
	public static final float TIEMPO_EXPLODE = 0.05f * 19;

	public final int PUNTUACION_SIMPLE = 10;

	public int vidasLeft;
	public int puntuacion;
	public float stateTime;
	public int state;
	float movedDistance;
	float aumentoVelocidad;

	public AlienShip(int vida, float aumentoVelocidad, float x, float y) {
		super(x, y, RADIUS);
		stateTime = 0;
		state = MOVE_SIDES;
		velocity.set(SPEED, SPEED_DOWN);
		movedDistance = 0;
		puntuacion = PUNTUACION_SIMPLE;
		vidasLeft = vida;
		this.aumentoVelocidad = 1 + aumentoVelocidad;
	}

	public void update(float deltaTime) {
		if (state != EXPLOTING) {
			switch (state) {
				case MOVE_SIDES:
					position.x += velocity.x * deltaTime * aumentoVelocidad;
					movedDistance += Math.abs(velocity.x * deltaTime) * aumentoVelocidad;
					if (movedDistance > MOVE_RANGO_SIDES) {
						state = MOVE_DOWN;
						velocity.x *= -1;
						movedDistance = 0;
					}
					break;
				case MOVE_DOWN:
					position.y += velocity.y * deltaTime * aumentoVelocidad;
					movedDistance += Math.abs(velocity.x * deltaTime) * aumentoVelocidad;
					if (movedDistance > MOVE_RANGO_DOWN) {
						state = MOVE_SIDES;
						movedDistance = 0;
					}
					break;
			}
		}

		boundsCircle.x = position.x;
		boundsCircle.y = position.y;
		stateTime += deltaTime;
	}

	public void beingHit(int poderBala) {
		vidasLeft--;
		if (vidasLeft <= 0) {
			state = EXPLOTING;
			velocity.add(0, 0);
			stateTime = 0;
		}
	}

	/**
	 * Llamar este metodo es poder de bala 1
	 */
	public void beingHit() {
		beingHit(1);
	}
}
