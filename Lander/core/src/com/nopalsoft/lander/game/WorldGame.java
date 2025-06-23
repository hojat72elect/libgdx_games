package com.nopalsoft.lander.game;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.lander.Assets;
import com.nopalsoft.lander.game.objetos.Bomba;
import com.nopalsoft.lander.game.objetos.Estrella;
import com.nopalsoft.lander.game.objetos.Gas;
import com.nopalsoft.lander.game.objetos.Laser;
import com.nopalsoft.lander.game.objetos.Nave;
import com.nopalsoft.lander.game.objetos.Plataforma;
import com.nopalsoft.lander.screens.Screens;

public class WorldGame {

	final float WIDTH = Screens.WORLD_SCREEN_WIDTH;
	final float HEIGHT = Screens.WORLD_SCREEN_HEIGHT;

	static int STATE_RUNNING = 0;
	static int STATE_PAUSED = 1;
	static int STATE_GAME_OVER = 2;
	static int STATE_NEXT_LEVEL = 3;

	public World oWorldBox;
	public Vector2 graivity;
	float unitScale = 1 / 100f;

	final float TIME_OUT_OF_GAS = 1.5f;
	float timeOutOfGas;

	final float TIME_FOR_NEXT_LEVEL = .75f;
	float timeforNextLevel;

	Nave oNave;
	Array<Plataforma> arrPlataformas;
	Array<Estrella> arrEstrellas;
	Array<Gas> arrGas;
	Array<Laser> arrLaser;
	Array<Bomba> arrBombas;

	Array<Body> arrBodies;

	Random oRan;
	public int state;

	public int estrellasTomadas;

	public WorldGame() {
		graivity = new Vector2(0, -4.9f);
		oWorldBox = new World(graivity, true);
		oWorldBox.setContactListener(new Colisiones(this));

		arrBodies = new Array<Body>();
		arrPlataformas = new Array<Plataforma>();
		arrEstrellas = new Array<Estrella>();
		arrGas = new Array<Gas>();
		arrLaser = new Array<Laser>();
		arrBombas = new Array<Bomba>();
		estrellasTomadas = 0;

		new TiledMapManagerBox2d(this, unitScale).createObjetosDesdeTiled(Assets.map);
		oRan = new Random();
	}

	public void update(float delta, float accelY, float accelX) {
		oWorldBox.step(1 / 60f, 8, 4); // para hacer mas lento el juego 1/300f
		oWorldBox.clearForces();

		oWorldBox.getBodies(arrBodies);

		Iterator<Body> i = arrBodies.iterator();
		while (i.hasNext()) {
			Body body = i.next();

			if (body.getUserData() instanceof Nave) {
				updateNave(body, delta, accelY, accelX);
			}
			else if (body.getUserData() instanceof Gas) {
				Gas obj = (Gas) body.getUserData();
				if (obj.state == Gas.STATE_TOMADA && !oWorldBox.isLocked()) {
					oWorldBox.destroyBody(body);
					arrGas.removeValue(obj, true);
				}
			}
			else if (body.getUserData() instanceof Estrella) {
				Estrella obj = (Estrella) body.getUserData();
				if (obj.state == Estrella.STATE_TOMADA && !oWorldBox.isLocked()) {
					oWorldBox.destroyBody(body);
					arrEstrellas.removeValue(obj, true);
				}
			}
			else if (body.getUserData() instanceof Bomba) {
				Bomba obj = (Bomba) body.getUserData();
				updateBomba(delta, body);
				if (obj.state == Bomba.STATE_EXPLOSION && obj.stateTime >= Bomba.TIME_EXPLOSION && !oWorldBox.isLocked()) {
					oWorldBox.destroyBody(body);
					arrBombas.removeValue(obj, true);
				}
			}
			else if (body.getUserData() instanceof Laser) {
				// Laser obj = (Laser) body.getUserData();
				updateLaser(delta, body);
			}
		}

		if (oNave.gas <= 0 && state == STATE_RUNNING) {
			timeOutOfGas += delta;
			if (timeOutOfGas >= TIME_OUT_OF_GAS)
				state = STATE_GAME_OVER;
		}

		if (oNave.isLanded) {
			timeforNextLevel += delta;
			if (timeforNextLevel >= TIME_FOR_NEXT_LEVEL)
				state = STATE_NEXT_LEVEL;
		}
		else {
			timeforNextLevel = 0;
		}

	}

	private void updateNave(Body body, float delta, float accelY, float accelX) {
		Nave obj = (Nave) body.getUserData();
		if (obj.state == Nave.STATE_EXPLODE && obj.stateTime > Nave.EXPLODE_TIME && !oWorldBox.isLocked()) {
			oWorldBox.destroyBody(body);
			state = STATE_GAME_OVER;
			return;
		}

		obj.update(delta, body, accelX, accelY);

	}

	private void updateBomba(float delta, Body body) {
		Bomba obj = (Bomba) body.getUserData();
		obj.update(delta, body);

		if (obj.state == Bomba.STATE_NORMAL)
			body.setLinearVelocity(obj.velocidad);
		else
			body.setLinearVelocity(0, 0);

	}

	private void updateLaser(float delta, Body body) {
		Laser obj = (Laser) body.getUserData();
		obj.update(delta, body);

		if (obj.isTouchingShip && oNave.state == Nave.STATE_NORMAL) {
			if (obj.state == Laser.STATE_FIRE) {
				oNave.getHurtByLaser(25);
				Vector2 blastDirection = body.getWorldCenter().sub(body.getWorldCenter());
				blastDirection.nor();
				body.applyLinearImpulse(blastDirection.scl(.1f), body.getWorldCenter(), true);
			}
		}

	}

}
