package com.nopalsoft.ponyrace.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.nopalsoft.ponyrace.Assets;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.objetos.*;
import com.nopalsoft.ponyrace.screens.Screens;

import java.util.Iterator;

public class WorldTiledRenderer {

	final float WIDTH = Screens.WORLD_SCREEN_WIDTH / 10f;
	final float HEIGHT = Screens.WORLD_SCREEN_HEIGHT / 10f;

	WorldTiled oWorld;
	public OrthographicCamera OrthoCam;
	SpriteBatch batch;
	public OrthogonalTiledMapRenderer tiledRender;
	Box2DDebugRenderer renderBox;
	SkeletonRenderer skelrender;

	public WorldTiledRenderer(SpriteBatch batch, WorldTiled oWorld) {
		this.OrthoCam = new OrthographicCamera(WIDTH, HEIGHT);
		this.OrthoCam.position.set(4, 2.4f, 0);

		this.batch = batch;
		this.oWorld = oWorld;
		tiledRender = new OrthogonalTiledMapRenderer(oWorld.game.oAssets.tiledMap, oWorld.m_units);
		renderBox = new Box2DDebugRenderer();
		skelrender = new SkeletonRenderer();

	}

	float fondoLastTime, fondoStateTime;

	public void render(float delta, float screenlastStatetime, float screenStateTime) {
		OrthoCam.position.x = oWorld.oPony.position.x;
		OrthoCam.position.y = oWorld.oPony.position.y;

		// Gdx.app.log("POSITION Y", OrthoCam.position.y + "");
		// Gdx.app.log("POSITION X", OrthoCam.position.x + "");

		float xMin, xMax, yMin, yMax;

		xMin = 4.0f;
		xMax = oWorld.tamanoMapaX - 4f; // Menos 4 porque es la mitad de la camara
		yMin = 2.4f;
		yMax = oWorld.tamanoMapaY - 2.4f;

		// Actualizo la camara para que no se salga de los bounds
		if (OrthoCam.position.x < xMin)
			OrthoCam.position.x = xMin;
		else if (OrthoCam.position.x > xMax)
			OrthoCam.position.x = xMax;

		if (OrthoCam.position.y < yMin)
			OrthoCam.position.y = yMin;
		else if (OrthoCam.position.y > yMax)
			OrthoCam.position.y = yMax;

		OrthoCam.update();
		batch.setProjectionMatrix(OrthoCam.combined);

		if (Settings.isBackGroundEnabled) {
			if (OrthoCam.position.x == oWorld.oPony.position.x) {
				if (oWorld.oPony.aceleration.x > 0) {
					fondoLastTime = fondoStateTime;
					fondoStateTime += delta;
				}
				else if (oWorld.oPony.aceleration.x < 0 && fondoLastTime > 0) {
					fondoLastTime = fondoStateTime;
					fondoStateTime -= delta;
				}
			}
			batch.begin();
			oWorld.game.oAssets.fondoAnim.apply(oWorld.game.oAssets.fondoSkeleton, fondoLastTime, fondoStateTime, true, null);
			oWorld.game.oAssets.fondoSkeleton.setX(OrthoCam.position.x);
			oWorld.game.oAssets.fondoSkeleton.setY(1.55f);
			oWorld.game.oAssets.fondoSkeleton.updateWorldTransform();
			oWorld.game.oAssets.fondoSkeleton.update(delta);
			skelrender.draw(batch, oWorld.game.oAssets.fondoSkeleton);
			batch.end();

		}
		renderBackGround(delta);

		batch.begin();
		batch.enableBlending();

		renderFuegos(delta);
		renderBloodStone(delta);
		renderPonyMalo(delta);
		renderPluma(delta);
		renderPony(delta);
		renderBombas(delta);
		renderWoods(delta);
		renderMonedas(delta);
		renderChiles(delta);
		renderGlobos(delta);
		renderDulces(delta);

		batch.end();

		if (Assets.drawDebugLines)
			renderBox.render(oWorld.oWorldBox, OrthoCam.combined);

	}

	private void renderBloodStone(float delta) {
		Iterator<BloodStone> ite = oWorld.arrBloodStone.iterator();
		while (ite.hasNext()) {
			BloodStone obj = ite.next();
			if (!OrthoCam.frustum.sphereInFrustum(obj.position, 3))
				continue;

			if (obj.tipo == BloodStone.Tipo.chica) {
				oWorld.game.oAssets.bloodStoneAnim.apply(oWorld.game.oAssets.bloodStoneSkeleton, obj.lastStatetime, obj.stateTime, true, null);
				oWorld.game.oAssets.bloodStoneSkeleton.setX(obj.position.x);
				oWorld.game.oAssets.bloodStoneSkeleton.setY(obj.position.y - .12f);
				oWorld.game.oAssets.bloodStoneSkeleton.updateWorldTransform();
				oWorld.game.oAssets.bloodStoneSkeleton.update(delta);
				skelrender.draw(batch, oWorld.game.oAssets.bloodStoneSkeleton);
			}
			else if (obj.tipo == BloodStone.Tipo.mediana) {
				oWorld.game.oAssets.bloodStone2Anim.apply(oWorld.game.oAssets.bloodStone2Skeleton, obj.lastStatetime, obj.stateTime, true, null);
				oWorld.game.oAssets.bloodStone2Skeleton.setX(obj.position.x);
				oWorld.game.oAssets.bloodStone2Skeleton.setY(obj.position.y - .12f);
				oWorld.game.oAssets.bloodStone2Skeleton.updateWorldTransform();
				oWorld.game.oAssets.bloodStone2Skeleton.update(delta);
				skelrender.draw(batch, oWorld.game.oAssets.bloodStone2Skeleton);
			}
		}

	}

	private void renderBackGround(float delta) {

		tiledRender.setView(OrthoCam);
		tiledRender.render();

	}

	private void renderPony(float delta) {

		oWorld.oPony.animState.update(delta);

		oWorld.oPony.animState.apply(oWorld.oPony.ponySkel);

		if (oWorld.oPony.state == Pony.STATE_WALK_RIGHT) {
			oWorld.oPony.ponySkel.setFlipX(true);
			oWorld.oPony.ponySkel.getRootBone().setRotation((float) Math.toDegrees(-oWorld.oPony.angleRad));
		}
		else if (oWorld.oPony.state == Pony.STATE_WALK_LEFT) {
			oWorld.oPony.ponySkel.setFlipX(false);
			oWorld.oPony.ponySkel.getRootBone().setRotation((float) Math.toDegrees(oWorld.oPony.angleRad));
		}

		oWorld.oPony.ponySkel.setX(oWorld.oPony.position.x);
		oWorld.oPony.ponySkel.setY(oWorld.oPony.position.y - .26f);

		oWorld.oPony.ponySkel.updateWorldTransform();
		skelrender.draw(batch, oWorld.oPony.ponySkel);
	}

	private void renderPonyMalo(float delta) {
		Iterator<PonyMalo> ite = oWorld.arrPonysMalos.iterator();
		while (ite.hasNext()) {
			Pony obj = ite.next();

			if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1f))
				continue;

			obj.animState.update(delta);
			obj.animState.apply(obj.ponySkel);

			if (obj.aceleration.x >= 0)
				obj.ponySkel.setFlipX(true);
			else if (obj.aceleration.x < 0)
				obj.ponySkel.setFlipX(false);

			obj.ponySkel.setX(obj.position.x);
			obj.ponySkel.setY(obj.position.y - .26f);

			obj.ponySkel.updateWorldTransform();
			skelrender.draw(batch, obj.ponySkel);
		}

	}

	private void renderBombas(float delta) {

		Iterator<Bomba> ite = oWorld.arrBombas.iterator();
		while (ite.hasNext()) {
			Bomba obj = ite.next();
			if (!OrthoCam.frustum.sphereInFrustum(obj.position, .5f))
				continue;

			if (obj.state == Bomba.State.normal) {
				oWorld.game.oAssets.bombAnim.apply(obj.skelBomb, obj.lastStatetime, obj.stateTime, true, null);
			}
			else {
				oWorld.game.oAssets.bombExAnim.apply(obj.skelBomb, obj.lastStatetime, obj.stateTime, true, null);
			}
			obj.skelBomb.setX(obj.position.x + .025f);
			obj.skelBomb.setY(obj.position.y - .15f);
			obj.skelBomb.updateWorldTransform();
			obj.skelBomb.update(delta);

			skelrender.draw(batch, obj.skelBomb);
		}

	}

	private void renderWoods(float delta) {

		Iterator<Wood> ite = oWorld.arrWoods.iterator();
		while (ite.hasNext()) {
			Wood obj = ite.next();
			if (!OrthoCam.frustum.sphereInFrustum(obj.position, .5f))
				continue;

			TextureRegion keyFrame;

			if (obj.tipo == Wood.Tipo.platano)
				keyFrame = oWorld.game.oAssets.platano;
			else
				keyFrame = oWorld.game.oAssets.tachuelas;

			batch.draw(keyFrame, obj.position.x - .25f, obj.position.y - .1f, .5f, .25f);
		}

	}

	private void renderMonedas(float delta) {
		Iterator<Moneda> ite = oWorld.arrMonedas.iterator();
		while (ite.hasNext()) {
			Moneda obj = ite.next();
			if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
				continue;

			Animation anim;
			if (obj.state == Moneda.State.normal)
				anim = oWorld.game.oAssets.monedaAnim;
			else
				anim = oWorld.game.oAssets.monedaTomadaAnim;

			anim.apply(obj.monedaSkeleton, obj.lastStatetime, obj.stateTime, true, null);
			obj.monedaSkeleton.setX(obj.position.x);
			obj.monedaSkeleton.setY(obj.position.y);
			obj.monedaSkeleton.updateWorldTransform();
			obj.monedaSkeleton.update(delta);
			skelrender.draw(batch, obj.monedaSkeleton);
		}

	}

	private void renderChiles(float delta) {
		Iterator<Chile> ite = oWorld.arrChiles.iterator();
		while (ite.hasNext()) {
			Chile obj = ite.next();
			if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
				continue;

			Animation anim;
			if (obj.state == Chile.State.normal)
				anim = oWorld.game.oAssets.chileAnim;
			else
				anim = oWorld.game.oAssets.chileTomadaAnim;

			anim.apply(obj.objSkeleton, obj.lastStatetime, obj.stateTime, true, null);
			obj.objSkeleton.setX(obj.position.x);
			obj.objSkeleton.setY(obj.position.y - .05f);
			obj.objSkeleton.updateWorldTransform();
			obj.objSkeleton.update(delta);
			skelrender.draw(batch, obj.objSkeleton);
		}
	}

	private void renderGlobos(float delta) {
		Iterator<Globo> ite = oWorld.arrGlobos.iterator();
		while (ite.hasNext()) {
			Globo obj = ite.next();
			if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
				continue;

			Animation anim;
			if (obj.state == Globo.State.normal)
				anim = oWorld.game.oAssets.globoAnim;
			else
				anim = oWorld.game.oAssets.globoTomadaAnim;

			anim.apply(obj.objSkeleton, obj.lastStatetime, obj.stateTime, true, null);
			obj.objSkeleton.setX(obj.position.x);
			obj.objSkeleton.setY(obj.position.y - .36f);
			obj.objSkeleton.updateWorldTransform();
			obj.objSkeleton.update(delta);
			skelrender.draw(batch, obj.objSkeleton);
		}

	}

	private void renderDulces(float delta) {
		Iterator<Dulce> ite = oWorld.arrDulces.iterator();
		while (ite.hasNext()) {
			Dulce obj = ite.next();
			if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
				continue;

			Animation anim;
			if (obj.state == Dulce.State.normal)
				anim = oWorld.game.oAssets.dulceAnim;
			else
				anim = oWorld.game.oAssets.dulceTomadaAnim;

			anim.apply(obj.objSkeleton, obj.lastStatetime, obj.stateTime, true, null);
			obj.objSkeleton.setX(obj.position.x);
			obj.objSkeleton.setY(obj.position.y - .4f);
			obj.objSkeleton.updateWorldTransform();
			obj.objSkeleton.update(delta);
			skelrender.draw(batch, obj.objSkeleton);
		}

	}

	private void renderPluma(float delta) {
		Iterator<Pluma> ite = oWorld.arrPlumas.iterator();
		while (ite.hasNext()) {
			Pluma obj = ite.next();
			if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
				continue;
			oWorld.game.oAssets.plumaAnim.apply(oWorld.game.oAssets.plumaSkeleton, obj.lastStatetime, obj.stateTime, true, null);
			oWorld.game.oAssets.plumaSkeleton.setX(obj.position.x);
			oWorld.game.oAssets.plumaSkeleton.setY(obj.position.y - .2f);
			oWorld.game.oAssets.plumaSkeleton.updateWorldTransform();
			oWorld.game.oAssets.plumaSkeleton.update(delta);
			skelrender.draw(batch, oWorld.game.oAssets.plumaSkeleton);
		}

	}

	private void renderFuegos(float delta) {

		Iterator<Fogata> ite = oWorld.arrFogatas.iterator();
		while (ite.hasNext()) {
			Fogata obj = ite.next();
			if (!OrthoCam.frustum.sphereInFrustum(obj.position, 1))
				continue;
			oWorld.game.oAssets.fogataAnim.apply(oWorld.game.oAssets.fogataSkeleton, obj.lastStatetime, obj.stateTime, true, null);
			oWorld.game.oAssets.fogataSkeleton.setX(obj.position.x);
			oWorld.game.oAssets.fogataSkeleton.setY(obj.position.y);
			oWorld.game.oAssets.fogataSkeleton.updateWorldTransform();
			oWorld.game.oAssets.fogataSkeleton.update(delta);
			skelrender.draw(batch, oWorld.game.oAssets.fogataSkeleton);
		}

	}

}
