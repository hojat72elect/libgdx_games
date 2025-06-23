package com.nopalsoft.zombiewars.objetos;

import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.zombiewars.Assets;

public class HeroForce extends Personajes {

	public HeroForce(Body body) {
		super(body);
		DURATION_ATTACK = Assets.heroForceShoot.animationDuration;
		DURATION_DEAD = Assets.heroForceDie.animationDuration + .2f;
		VELOCIDAD_WALK = 1;
		DAMAGE = 1;
		DISTANCE_ATTACK = 2;
		TIME_TO_ATTACK_AGAIN = 2;
		vidas = 5;
		isFacingLeft = false;
		tipo = TIPO_RANGO;

	}

}
