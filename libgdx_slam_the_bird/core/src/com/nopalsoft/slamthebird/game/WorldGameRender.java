package com.nopalsoft.slamthebird.game;

import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.objetos.Boost;
import com.nopalsoft.slamthebird.objetos.Enemigo;
import com.nopalsoft.slamthebird.objetos.Moneda;
import com.nopalsoft.slamthebird.objetos.Plataforma;
import com.nopalsoft.slamthebird.objetos.Robot;
import com.nopalsoft.slamthebird.screens.Screens;

public class WorldGameRender {

	final float WIDTH = Screens.WORLD_SCREEN_WIDTH;
	final float HEIGHT = Screens.WORLD_SCREEN_HEIGHT;

	SpriteBatch batcher;
	WorldGame oWorld;

	OrthographicCamera oCam;

	Box2DDebugRenderer renderBox;

	public WorldGameRender(SpriteBatch batcher, WorldGame oWorld) {

		this.oCam = new OrthographicCamera(WIDTH, HEIGHT);
		this.oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);

		this.batcher = batcher;
		this.oWorld = oWorld;
		this.renderBox = new Box2DDebugRenderer();
	}

	public void render(float delta) {

		oCam.position.y = oWorld.oRobo.position.y;

		if (oCam.position.y < HEIGHT / 2f)
			oCam.position.y = HEIGHT / 2f;
		else if (oCam.position.y > HEIGHT / 2f + 3)
			oCam.position.y = HEIGHT / 2f + 3;

		oCam.update();
		batcher.setProjectionMatrix(oCam.combined);

		batcher.begin();

		batcher.enableBlending();

		renderFondo();
		renderPlataformas();
		renderBoost();
		renderMonedas();
		renderEnemigos();
		renderPersonaje();

		batcher.end();
//		renderBox.render(oWorld.oWorldBox, oCam.combined);
	}

	private void renderFondo() {
		batcher.draw(Assets.fondo, 0, 0, WIDTH, HEIGHT + 3);
	}

	private void renderPlataformas() {
		Iterator<Plataforma> i = oWorld.arrPlataformas.iterator();

		while (i.hasNext()) {
			Plataforma obj = i.next();
			TextureRegion keyFrame = Assets.plataforma;

			if (obj.state == Plataforma.STATE_BROKEN) {
				if (obj.stateTime < Assets.plataformBreakable.getAnimationDuration())
					keyFrame = Assets.plataformBreakable.getKeyFrame(
							obj.stateTime, false);
				else
					continue;
			}

			if (obj.state == Plataforma.STATE_BREAKABLE)
				keyFrame = Assets.plataformBreakable.getKeyFrame(0);

			if (obj.state == Plataforma.STATE_CHANGING)
				batcher.draw(keyFrame, obj.position.x - .5f,
						obj.position.y - .1f, .5f, .15f, 1f, .3f,
						obj.changinScale, obj.changinScale, 0);
			else
				batcher.draw(keyFrame, obj.position.x - .5f,
						obj.position.y - .15f, 1f, .3f);

			if (obj.state == Plataforma.STATE_FIRE)
				batcher.draw(Assets.animPlataformFire.getKeyFrame(
						obj.stateTime, true), obj.position.x - .5f,
						obj.position.y + .1f, 1f, .3f);
		}

	}

	private void renderBoost() {
		Iterator<Boost> i = oWorld.arrBoost.iterator();

		while (i.hasNext()) {
			Boost obj = i.next();
			TextureRegion keyFrame;
			switch (obj.tipo) {
			case Boost.TIPO_COIN_RAIN:
				keyFrame = Assets.boostCoinRain;
				break;
			case Boost.TIPO_ICE:
				keyFrame = Assets.boostIce;
				break;

			case Boost.TIPO_SUPERJUMP:
				keyFrame = Assets.boostSuperSalto;
				break;
			case Boost.TIPO_INVENCIBLE:

			default:
				keyFrame = Assets.boostInvencible;
				break;
			}

			batcher.draw(keyFrame, obj.position.x - .175f,
					obj.position.y - .15f, .35f, .3f);
		}

	}

	private void renderMonedas() {
		Iterator<Moneda> i = oWorld.arrMonedas.iterator();

		while (i.hasNext()) {
			Moneda obj = i.next();
			batcher.draw(Assets.animMoneda.getKeyFrame(obj.stateTime, true),
					obj.position.x - .15f, obj.position.y - .15f, .3f, .34f);
		}

	}

	public void renderEnemigos() {
		Iterator<Enemigo> i = oWorld.arrEnemigos.iterator();
		while (i.hasNext()) {
			Enemigo obj = i.next();

			if (obj.state == Enemigo.STATE_JUST_APPEAR) {
				batcher.draw(Assets.flapSpawn, obj.position.x - .25f,
						obj.position.y - .25f, .25f, .25f, .5f, .5f,
						obj.appearScale, obj.appearScale, 0);
				continue;
			}

			TextureRegion keyFrame;
			if (obj.state == Enemigo.STATE_FLYING) {
				if (obj.vidas >= 3)
					keyFrame = Assets.animflapAlasRojo.getKeyFrame(
							obj.stateTime, true);
				else
					keyFrame = Assets.animflapAlasAzul.getKeyFrame(
							obj.stateTime, true);
			}
			else if (obj.state == Enemigo.STATE_EVOLVING) {
				keyFrame = Assets.animEvolving.getKeyFrame(obj.stateTime, true);
			}
			else {
				keyFrame = Assets.flapAzul;
			}

			if (obj.velocidad.x > 0)
				batcher.draw(keyFrame, obj.position.x - .285f,
						obj.position.y - .21f, .57f, .42f);
			else
				batcher.draw(keyFrame, obj.position.x + .285f,
						obj.position.y - .21f, -.57f, .42f);

		}
	}

	private void renderPersonaje() {

		Robot obj = oWorld.oRobo;
		TextureRegion keyFrame;

		if (obj.slam && obj.state == Robot.STATE_FALLING) {
			keyFrame = Assets.animPersonajeSlam.getKeyFrame(obj.stateTime);
			batcher.draw(Assets.slam.getKeyFrame(obj.stateTime, true),
					obj.position.x - .4f, obj.position.y - .55f, .8f, .5f);
		}
		else if (obj.state == Robot.STATE_FALLING
				|| obj.state == Robot.STATE_JUMPING) {
			keyFrame = Assets.animPersonajeJump
					.getKeyFrame(obj.stateTime, true);
		}
		else
			keyFrame = Assets.animPersonajeHit.getKeyFrame(obj.stateTime, true);

		// c

		if (obj.velocidad.x > .1f)
			batcher.draw(keyFrame, obj.position.x - .3f, obj.position.y - .3f,
					.3f, .3f, .6f, .6f, 1, 1, obj.angleGrad);
		else if (obj.velocidad.x < -.1f)
			batcher.draw(keyFrame, obj.position.x + .3f, obj.position.y - .3f,
					-.3f, .3f, -.6f, .6f, 1, 1, obj.angleGrad);
		else
			batcher.draw(Assets.personaje, obj.position.x - .3f,
					obj.position.y - .3f, .3f, .3f, .6f, .6f, 1, 1,
					obj.angleGrad);

		// TODO el personaje cuando se muere no tiene velocidad por lo que no aparece el keyframe sino que agarra assetspersonaje

		// Esto renderear los boost arriba de la cabeza del personaje
		renderBoostActivo(obj);

	}

	private void renderBoostActivo(Robot obj) {
		if (obj.isInvencible || obj.isSuperJump) {
			float timeToAlert = 2.5f;// Tiempo para que empieze a parpaderar el boost
			TextureRegion boostKeyFrame;
			if (obj.isInvencible) {
				if (obj.DURATION_INVENCIBLE - obj.durationInvencible <= timeToAlert) {
					boostKeyFrame = Assets.animBoostEndInvencible.getKeyFrame(
							obj.stateTime, true);// anim
				}
				else
					boostKeyFrame = Assets.boostInvencible;
			}
			else {// jump
				if (obj.DURATION_SUPER_JUMP - obj.durationSuperJump <= timeToAlert) {
					boostKeyFrame = Assets.animBoostEndSuperSalto.getKeyFrame(
							obj.stateTime, true);// anim
				}
				else
					boostKeyFrame = Assets.boostSuperSalto;
			}
			// batcher.draw(boostKeyFrame, obj.position.x - .0875f, obj.position.y + .3f, .175f, .15f);
			batcher.draw(boostKeyFrame, obj.position.x - .175f,
					obj.position.y + .3f, .35f, .3f);
		}
	}

}
