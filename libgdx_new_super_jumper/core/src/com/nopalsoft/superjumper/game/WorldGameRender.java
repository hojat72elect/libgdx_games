package com.nopalsoft.superjumper.game;

import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.objetos.Bullet;
import com.nopalsoft.superjumper.objetos.Enemigo;
import com.nopalsoft.superjumper.objetos.Item;
import com.nopalsoft.superjumper.objetos.Moneda;
import com.nopalsoft.superjumper.objetos.Nube;
import com.nopalsoft.superjumper.objetos.Personaje;
import com.nopalsoft.superjumper.objetos.PiezaPlataformas;
import com.nopalsoft.superjumper.objetos.Plataformas;
import com.nopalsoft.superjumper.objetos.Rayo;
import com.nopalsoft.superjumper.screens.Screens;

public class WorldGameRender {
	final float WIDTH = Screens.WORLD_WIDTH;
	final float HEIGHT = Screens.WORLD_HEIGHT;

	WorldGame oWorld;
	SpriteBatch batcher;
	OrthographicCamera oCam;
	Box2DDebugRenderer boxRender;

	public WorldGameRender(SpriteBatch batcher, WorldGame oWorld) {
		this.oWorld = oWorld;
		this.batcher = batcher;

		oCam = new OrthographicCamera(WIDTH, HEIGHT);
		oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);

		boxRender = new Box2DDebugRenderer();
	}

	public void unprojectToWorldCoords(Vector3 touchPoint) {
		oCam.unproject(touchPoint);
	}

	public void render(float delta) {
		if (oWorld.state == WorldGame.STATE_RUNNING)
			oCam.position.y = oWorld.oPer.position.y;

		if (oCam.position.y < Screens.WORLD_HEIGHT / 2f) {
			oCam.position.y = Screens.WORLD_HEIGHT / 2f;
		}

		oCam.update();
		batcher.setProjectionMatrix(oCam.combined);

		batcher.begin();

		renderPersonaje();
		renderPlataformas();
		renderPiezasPlataformas();
		renderCoins();
		renderItems();
		renderEnemigo();
		renderNube();
		renderRayo();
		renderBullet();

		batcher.end();

		// boxRender.render(oWorld.oWorldBox, oCam.combined);

	}

	private void renderPersonaje() {
		AtlasRegion keyframe = null;

		Personaje obj = oWorld.oPer;

		if (obj.velocidad.y > 0)
			keyframe = Assets.personajeJump;
		else
			keyframe = Assets.personajeStand;

		if (obj.velocidad.x > 0)
			batcher.draw(keyframe, obj.position.x + Personaje.DRAW_WIDTH / 2f, obj.position.y - Personaje.DRAW_HEIGTH / 2f,
					-Personaje.DRAW_WIDTH / 2f, Personaje.DRAW_HEIGTH / 2f, -Personaje.DRAW_WIDTH, Personaje.DRAW_HEIGTH, 1, 1, obj.angleDeg);

		else
			batcher.draw(keyframe, obj.position.x - Personaje.DRAW_WIDTH / 2f, obj.position.y - Personaje.DRAW_HEIGTH / 2f,
					Personaje.DRAW_WIDTH / 2f, Personaje.DRAW_HEIGTH / 2f, Personaje.DRAW_WIDTH, Personaje.DRAW_HEIGTH, 1, 1, obj.angleDeg);

		if (obj.isJetPack) {
			batcher.draw(Assets.jetpack, obj.position.x - .45f / 2f, obj.position.y - .7f / 2f, .45f, .7f);

			TextureRegion fireFrame = Assets.jetpackFire.getKeyFrame(obj.durationJetPack, true);
			batcher.draw(fireFrame, obj.position.x - .35f / 2f, obj.position.y - .95f, .35f, .6f);

		}
		if (obj.isBubble) {
			batcher.draw(Assets.bubble, obj.position.x - .5f, obj.position.y - .5f, 1, 1);
		}

	}

	private void renderPlataformas() {
		Iterator<Plataformas> i = oWorld.arrPlataformas.iterator();
		while (i.hasNext()) {
			Plataformas obj = i.next();

			AtlasRegion keyframe = null;

			if (obj.tipo == Plataformas.TIPO_ROMPIBLE) {
				switch (obj.color) {
				case Plataformas.COLOR_BEIGE:
					keyframe = Assets.plataformaBeigeBroken;
					break;
				case Plataformas.COLOR_BLUE:
					keyframe = Assets.plataformaBlueBroken;
					break;
				case Plataformas.COLOR_GRAY:
					keyframe = Assets.plataformaGrayBroken;
					break;
				case Plataformas.COLOR_GREEN:
					keyframe = Assets.plataformaGreenBroken;
					break;
				case Plataformas.COLOR_MULTICOLOR:
					keyframe = Assets.plataformaMulticolorBroken;
					break;
				case Plataformas.COLOR_PINK:
					keyframe = Assets.plataformaPinkBroken;
					break;

				}
			}
			else {
				switch (obj.color) {
				case Plataformas.COLOR_BEIGE:
					keyframe = Assets.plataformaBeige;
					break;
				case Plataformas.COLOR_BLUE:
					keyframe = Assets.plataformaBlue;
					break;
				case Plataformas.COLOR_GRAY:
					keyframe = Assets.plataformaGray;
					break;
				case Plataformas.COLOR_GREEN:
					keyframe = Assets.plataformaGreen;
					break;
				case Plataformas.COLOR_MULTICOLOR:
					keyframe = Assets.plataformaMulticolor;
					break;
				case Plataformas.COLOR_PINK:
					keyframe = Assets.plataformaPink;
					break;
				case Plataformas.COLOR_BEIGE_LIGHT:
					keyframe = Assets.plataformaBeigeLight;
					break;
				case Plataformas.COLOR_BLUE_LIGHT:
					keyframe = Assets.plataformaBlueLight;
					break;
				case Plataformas.COLOR_GRAY_LIGHT:
					keyframe = Assets.plataformaGrayLight;
					break;
				case Plataformas.COLOR_GREEN_LIGHT:
					keyframe = Assets.plataformaGreenLight;
					break;
				case Plataformas.COLOR_MULTICOLOR_LIGHT:
					keyframe = Assets.plataformaMulticolorLight;
					break;
				case Plataformas.COLOR_PINK_LIGHT:
					keyframe = Assets.plataformaPinkLight;
					break;
				}

			}
			batcher.draw(keyframe, obj.position.x - Plataformas.DRAW_WIDTH_NORMAL / 2f, obj.position.y - Plataformas.DRAW_HEIGTH_NORMAL / 2f,
					Plataformas.DRAW_WIDTH_NORMAL, Plataformas.DRAW_HEIGTH_NORMAL);
		}
	}

	private void renderPiezasPlataformas() {
		Iterator<PiezaPlataformas> i = oWorld.arrPiezasPlataformas.iterator();
		while (i.hasNext()) {
			PiezaPlataformas obj = i.next();

			AtlasRegion keyframe = null;

			if (obj.tipo == PiezaPlataformas.TIPO_LEFT) {
				switch (obj.color) {
				case Plataformas.COLOR_BEIGE:
					keyframe = Assets.plataformaBeigeLeft;
					break;
				case Plataformas.COLOR_BLUE:
					keyframe = Assets.plataformaBlueLeft;
					break;
				case Plataformas.COLOR_GRAY:
					keyframe = Assets.plataformaGrayLeft;
					break;
				case Plataformas.COLOR_GREEN:
					keyframe = Assets.plataformaGreenLeft;
					break;
				case Plataformas.COLOR_MULTICOLOR:
					keyframe = Assets.plataformaMulticolorLeft;
					break;
				case Plataformas.COLOR_PINK:
					keyframe = Assets.plataformaPinkLeft;
					break;

				}
			}
			else {
				switch (obj.color) {
				case Plataformas.COLOR_BEIGE:
					keyframe = Assets.plataformaBeigeRight;
					break;
				case Plataformas.COLOR_BLUE:
					keyframe = Assets.plataformaBlueRight;
					break;
				case Plataformas.COLOR_GRAY:
					keyframe = Assets.plataformaGrayRight;
					break;
				case Plataformas.COLOR_GREEN:
					keyframe = Assets.plataformaGreenRight;
					break;
				case Plataformas.COLOR_MULTICOLOR:
					keyframe = Assets.plataformaMulticolorRight;
					break;
				case Plataformas.COLOR_PINK:
					keyframe = Assets.plataformaPinkRight;
					break;

				}
			}

			batcher.draw(keyframe, obj.position.x - PiezaPlataformas.DRAW_WIDTH_NORMAL / 2f, obj.position.y - PiezaPlataformas.DRAW_HEIGTH_NORMAL
					/ 2f, PiezaPlataformas.DRAW_WIDTH_NORMAL / 2f, PiezaPlataformas.DRAW_HEIGTH_NORMAL / 2f, PiezaPlataformas.DRAW_WIDTH_NORMAL,
					PiezaPlataformas.DRAW_HEIGTH_NORMAL, 1, 1, obj.angleDeg);

		}
	}

	private void renderCoins() {
		Iterator<Moneda> i = oWorld.arrMonedas.iterator();
		while (i.hasNext()) {
			Moneda obj = i.next();

			batcher.draw(Assets.coin, obj.position.x - Moneda.DRAW_WIDTH / 2f, obj.position.y - Moneda.DRAW_HEIGHT / 2f, Moneda.DRAW_WIDTH,
					Moneda.DRAW_HEIGHT);
		}

	}

	private void renderItems() {
		Iterator<Item> i = oWorld.arrItem.iterator();
		while (i.hasNext()) {
			Item obj = i.next();

			TextureRegion keyframe = null;

			switch (obj.tipo) {
			case Item.TIPO_BUBBLE:
				keyframe = Assets.bubbleSmall;
				break;
			case Item.TIPO_JETPACK:
				keyframe = Assets.jetpackSmall;
				break;
			case Item.TIPO_GUN:
				keyframe = Assets.gun;
				break;

			}

			batcher.draw(keyframe, obj.position.x - Item.DRAW_WIDTH / 2f, obj.position.y - Item.DRAW_HEIGHT / 2f, Item.DRAW_WIDTH, Item.DRAW_HEIGHT);

		}

	}

	private void renderEnemigo() {
		Iterator<Enemigo> i = oWorld.arrEnemigo.iterator();
		while (i.hasNext()) {
			Enemigo obj = i.next();

			TextureRegion keyframe = Assets.enemigo.getKeyFrame(obj.stateTime, true);

			batcher.draw(keyframe, obj.position.x - Enemigo.DRAW_WIDTH / 2f, obj.position.y - Enemigo.DRAW_HEIGHT / 2f, Enemigo.DRAW_WIDTH,
					Enemigo.DRAW_HEIGHT);
		}

	}

	private void renderNube() {
		Iterator<Nube> i = oWorld.arrNubes.iterator();
		while (i.hasNext()) {
			Nube obj = i.next();

			TextureRegion keyframe = null;

			switch (obj.tipo) {
			case Nube.TIPO_ANGRY:
				keyframe = Assets.nubeAngry;
				break;
			case Nube.TIPO_HAPPY:
				keyframe = Assets.nubeHappy;
				break;

			}

			batcher.draw(keyframe, obj.position.x - Nube.DRAW_WIDTH / 2f, obj.position.y - Nube.DRAW_HEIGHT / 2f, Nube.DRAW_WIDTH, Nube.DRAW_HEIGHT);

			if (obj.isBlowing) {
				batcher.draw(Assets.nubeViento, obj.position.x - .35f, obj.position.y - .85f, .6f, .8f);
			}
		}

	}

	private void renderRayo() {
		Iterator<Rayo> i = oWorld.arrRayos.iterator();
		while (i.hasNext()) {
			Rayo obj = i.next();

			TextureRegion keyframe = Assets.rayo.getKeyFrame(obj.stateTime, true);

			batcher.draw(keyframe, obj.position.x - Rayo.DRAW_WIDTH / 2f, obj.position.y - Rayo.DRAW_HEIGHT / 2f, Rayo.DRAW_WIDTH, Rayo.DRAW_HEIGHT);
		}
	}

	private void renderBullet() {
		Iterator<Bullet> i = oWorld.arrBullets.iterator();
		while (i.hasNext()) {
			Bullet obj = i.next();
			batcher.draw(Assets.bullet, obj.position.x - Bullet.SIZE / 2f, obj.position.y - Bullet.SIZE / 2f, Bullet.SIZE, Bullet.SIZE);
		}
	}

}
