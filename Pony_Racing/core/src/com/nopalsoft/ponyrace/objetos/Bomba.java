package com.nopalsoft.ponyrace.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.Skeleton;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game.WorldTiled;

public class Bomba extends GameObject {
	public enum State {
		normal,
		explode;

	}

	public final float TIEMPO_HURT;
	public static final float TIEMPO_NORMAL = 1.5f;
	public static final float TIEMPO_EXPLOSION = .3f;
	public float lastStatetime;
	public float stateTime;

	public float angulo;

	public State state;

	public Skeleton skelBomb;

	public Bomba(float x, float y, WorldTiled oWorld) {
		super(x, y, 0);
		stateTime = 0;
		lastStatetime = stateTime;
		state = State.normal;
		skelBomb = new Skeleton(oWorld.game.oAssets.skeletonBombData);
		skelBomb.setToSetupPose();

		switch (Settings.nivelBomb) {
			default:
			case 0:
				TIEMPO_HURT = 2;
				break;
			case 1:
				TIEMPO_HURT = 2.5f;
				break;
			case 2:
				TIEMPO_HURT = 2.7f;
				break;
			case 3:
				TIEMPO_HURT = 3f;
				break;
			case 4:
				TIEMPO_HURT = 3.25f;
				break;
			case 5:
				TIEMPO_HURT = 3.5f;
				break;
		}

	}

	public void update(float delta, Body obj) {
		lastStatetime = stateTime;
		stateTime += delta;

		position.x = obj.getPosition().x;
		position.y = obj.getPosition().y;
		angulo = MathUtils.radiansToDegrees * obj.getAngle();

		if (state == State.normal && stateTime >= TIEMPO_NORMAL) {
			state = State.explode;
			stateTime = 0;
		}

	}

	public void explode(Body obj) {
		if (state == State.normal) {
			state = State.explode;
			stateTime = 0;
			obj.setLinearVelocity(0, 0);
		}
	}

}
