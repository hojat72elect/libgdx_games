package com.nopalsoft.zombiekiller.game;

import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.zombiekiller.AnimationSprite;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.objetos.Bullet;
import com.nopalsoft.zombiekiller.objetos.Crate;
import com.nopalsoft.zombiekiller.objetos.Hero;
import com.nopalsoft.zombiekiller.objetos.ItemGem;
import com.nopalsoft.zombiekiller.objetos.ItemHearth;
import com.nopalsoft.zombiekiller.objetos.ItemMeat;
import com.nopalsoft.zombiekiller.objetos.ItemShield;
import com.nopalsoft.zombiekiller.objetos.ItemSkull;
import com.nopalsoft.zombiekiller.objetos.ItemStar;
import com.nopalsoft.zombiekiller.objetos.Items;
import com.nopalsoft.zombiekiller.objetos.Saw;
import com.nopalsoft.zombiekiller.objetos.Zombie;
import com.nopalsoft.zombiekiller.screens.Screens;

public class WorldGameRenderer2 {

	final float WIDTH = Screens.WORLD_WIDTH;
	final float HEIGHT = Screens.WORLD_HEIGHT;

	SpriteBatch batcher;
	WorldGame oWorld;
	ParallaxCamera oCam;
	OrthogonalTiledMapRenderer tiledRender;

	Box2DDebugRenderer renderBox;

	float xMin, xMax, yMin, yMax;

	TiledMapTileLayer map1;
	TiledMapTileLayer map2;
	TiledMapTileLayer map3;
	TiledMapTileLayer map4;

	TiledMapTileLayer mapInFront;// Enfrente del mono

	boolean showMoon;

	public WorldGameRenderer2(SpriteBatch batcher, WorldGame oWorld) {

		this.oCam = new ParallaxCamera(WIDTH, HEIGHT);
		this.oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
		this.batcher = batcher;
		this.oWorld = oWorld;
		this.renderBox = new Box2DDebugRenderer();
		tiledRender = new OrthogonalTiledMapRenderer(Assets.map, oWorld.unitScale);

		/**
		 * Entre mas chico el numero se renderean primero.
		 */
		map1 = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("1");
		map2 = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("2");
		map3 = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("3");
		map4 = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("4");
		mapInFront = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("inFront");

		xMin = 4.0f;// Inicia en 4 porque la camara esta centrada no en el origen
		xMax = oWorld.unitScale * oWorld.tiledWidth * 32 - 4;// Menos 4 porque la camara esta centrada en el origen
		yMin = 2.4f;
		yMax = oWorld.unitScale * oWorld.tiledHeight * 32 - 1f;// Aqui no le voy a restar el -2.4 solamente -1f para que tenga un poco mas de libertad al ir hacia arriba.

		showMoon = MathUtils.randomBoolean();
	}

	public void render(float delta) {
		oCam.position.x = oWorld.oHero.position.x;
		oCam.position.y = oWorld.oHero.position.y;

		// Actualizo la camara para que no se salga de los bounds
		if (oCam.position.x < xMin)
			oCam.position.x = xMin;
		else if (oCam.position.x > xMax)
			oCam.position.x = xMax;

		if (oCam.position.y < yMin)
			oCam.position.y = yMin;
		else if (oCam.position.y > yMax)
			oCam.position.y = yMax;

		oCam.update();
		batcher.setProjectionMatrix(oCam.combined);
		batcher.begin();
		batcher.disableBlending();
		drawBackGround(delta);
		batcher.end();

		batcher.setProjectionMatrix(oCam.calculateParallaxMatrix(0.5f, 1));
		batcher.begin();
		drawParallaxBackground(delta);
		batcher.end();

		if (showMoon) {
			batcher.setProjectionMatrix(oCam.calculateParallaxMatrix(0.25f, .8f));
			batcher.begin();
			batcher.enableBlending();
			drawMoon();
			batcher.end();
		}

		drawTiled();

		batcher.setProjectionMatrix(oCam.combined);
		batcher.begin();
		batcher.enableBlending();
		drawItems();
		drawCrates();
		drawSaw();
		drawZombie();
		drawBullets();
		drawPlayer();

		batcher.end();

		drawTiledInfront();

		// renderBox.render(oWorld.oWorldBox, oCam.combined);
	}

	private void drawBackGround(float delta) {
		batcher.draw(Assets.backBackground, oCam.position.x - 4f, oCam.position.y - 2.4f, 8.0f, 4.8f);
	}

	private void drawParallaxBackground(float delta) {
		for (int i = 0; i < 2; i += 1) {
			batcher.draw(Assets.background, (-xMin / 2f) + (i * 16), 0, 8.0f, 4.8f);
			batcher.draw(Assets.background, (-xMin / 2f) + ((i + 1) * 16), 0, -8.0f, 4.8f);
		}

		// batcher.draw(Assets.background, (-xMin / 2f), 0, 8.0f, 4.8f);
		// batcher.draw(Assets.background, (-xMin / 2f) + 16, 0, -8.0f, 4.8f);
	}

	private void drawMoon() {
		batcher.draw(Assets.moon, 4, 2.3f, 3.5f, 2.55f);

	}

	private void drawTiledInfront() {

		tiledRender.setView(oCam);

		tiledRender.getBatch().begin();
		if (mapInFront != null)
			tiledRender.renderTileLayer(mapInFront);
		tiledRender.getBatch().end();

	}

	private void drawTiled() {
		tiledRender.setView(oCam);
		tiledRender.getBatch().begin();
		if (map1 != null)
			tiledRender.renderTileLayer(map1);
		if (map2 != null)
			tiledRender.renderTileLayer(map2);
		if (map3 != null)
			tiledRender.renderTileLayer(map3);
		if (map4 != null)
			tiledRender.renderTileLayer(map4);

		// tiledRender.render();
		tiledRender.getBatch().end();

	}

	private void drawCrates() {

		Iterator<Crate> i = oWorld.arrCrates.iterator();
		while (i.hasNext()) {
			Crate obj = i.next();
			float halfSize = obj.SIZE / 2f;
			batcher.draw(Assets.crate, obj.position.x - halfSize, obj.position.y - halfSize, halfSize, halfSize, obj.SIZE, obj.SIZE, 1, 1,
					obj.angleDeg);

		}

	}

	private void drawSaw() {

		Iterator<Saw> i = oWorld.arrSaws.iterator();
		while (i.hasNext()) {
			Saw obj = i.next();

			float halfSize = (obj.SIZE + .2f) / 2f;
			batcher.draw(Assets.saw, obj.position.x - halfSize, obj.position.y - halfSize, halfSize, halfSize, obj.SIZE + .2f, obj.SIZE + .2f, 1, 1,
					obj.angleDeg);

		}

	}

	private void drawItems() {
		TextureRegion keyframe = null;

		Iterator<Items> i = oWorld.arrItems.iterator();
		while (i.hasNext()) {
			Items obj = i.next();

			if (obj instanceof ItemGem) {
				keyframe = Assets.itemGem;
			}
			else if (obj instanceof ItemHearth) {
				keyframe = Assets.itemHeart;
			}
			else if (obj instanceof ItemMeat) {
				keyframe = Assets.itemMeat;
			}
			else if (obj instanceof ItemSkull) {
				keyframe = Assets.itemSkull;
			}
			else if (obj instanceof ItemShield) {
				keyframe = Assets.itemShield;
			}
			else if (obj instanceof ItemStar) {
				keyframe = Assets.itemStar;
			}

			batcher.draw(keyframe, obj.position.x - obj.DRAW_WIDTH / 2f, obj.position.y - obj.DRAW_HEIGHT / 2f, obj.DRAW_WIDTH, obj.DRAW_HEIGHT);

		}

	}

	private void drawZombie() {

		Iterator<Zombie> i = oWorld.arrZombies.iterator();
		while (i.hasNext()) {

			Zombie obj = i.next();
			AnimationSprite animWalk = null;
			AnimationSprite animIdle = null;
			AnimationSprite animRise = null;
			AnimationSprite animDie = null;
			Sprite zombieHurt = null;

			float ajusteY = 0;

			switch (obj.tipo) {
			case Zombie.TIPO_CUASY:
				animWalk = Assets.zombieCuasyWalk;
				animIdle = Assets.zombieCuasyIdle;
				animRise = Assets.zombieCuasyRise;
				animDie = Assets.zombieCuasyDie;
				zombieHurt = Assets.zombieCuasyHurt;
				ajusteY = -.035f;
				break;

			case Zombie.TIPO_FRANK:
				animWalk = Assets.zombieFrankWalk;
				animIdle = Assets.zombieFrankIdle;
				animRise = Assets.zombieFrankRise;
				animDie = Assets.zombieFrankDie;
				zombieHurt = Assets.zombieFrankHurt;
				ajusteY = -.033f;
				break;
			case Zombie.TIPO_KID:
				animWalk = Assets.zombieKidWalk;
				animIdle = Assets.zombieKidIdle;
				animRise = Assets.zombieKidRise;
				animDie = Assets.zombieKidDie;
				zombieHurt = Assets.zombieKidHurt;
				break;
			case Zombie.TIPO_MUMMY:
				animWalk = Assets.zombieMummyWalk;
				animIdle = Assets.zombieMummyIdle;
				animRise = Assets.zombieMummyRise;
				animDie = Assets.zombieMummyDie;
				zombieHurt = Assets.zombieMummyHurt;
				ajusteY = -.035f;
				break;
			case Zombie.TIPO_PAN:
				animWalk = Assets.zombiePanWalk;
				animIdle = Assets.zombiePanIdle;
				animRise = Assets.zombiePanRise;
				animDie = Assets.zombiePanDie;
				zombieHurt = Assets.zombiePanHurt;
				ajusteY = -.038f;
				break;
			}

			Sprite spriteFrame;

			if (obj.state == Zombie.STATE_NORMAL) {
				if (obj.isWalking)
					spriteFrame = animWalk.getKeyFrame(obj.stateTime, true);
				else {
					spriteFrame = animIdle.getKeyFrame(obj.stateTime, true);

				}
			}
			else if (obj.state == Zombie.STATE_RISE) {
				spriteFrame = animRise.getKeyFrame(obj.stateTime, false);
			}
			else if (obj.state == Zombie.STATE_DEAD) {
				spriteFrame = animDie.getKeyFrame(obj.stateTime, false);
			}
			else if (obj.state == Zombie.STATE_HURT) {
				spriteFrame = zombieHurt;
			}
			else
				spriteFrame = null;

			if (obj.isFacingLeft) {
				spriteFrame.setPosition(obj.position.x + .29f, obj.position.y - .34f + ajusteY);
				spriteFrame.setSize(-.8f, .8f);
				spriteFrame.draw(batcher);
			}
			else {
				spriteFrame.setPosition(obj.position.x - .29f, obj.position.y - .34f + ajusteY);
				spriteFrame.setSize(.8f, .8f);
				spriteFrame.draw(batcher);
			}

			// Barra de vidas
			if (obj.vidas > 0 && (obj.state == Zombie.STATE_NORMAL || obj.state == Zombie.STATE_HURT))
				batcher.draw(Assets.redBar, obj.position.x - .33f, obj.position.y + .36f, .65f * ((float) obj.vidas / obj.MAX_LIFE), .075f);
		}

	}

	private void drawBullets() {
		Iterator<Bullet> i = oWorld.arrBullets.iterator();
		while (i.hasNext()) {
			Bullet obj = i.next();

			AnimationSprite animBullet = null;

			switch (obj.tipo) {
			case Bullet.LEVEL_0:
				animBullet = Assets.bullet1;
				break;
			case Bullet.LEVEL_1:
				animBullet = Assets.bullet2;
				break;
			case Bullet.LEVEL_2:
				animBullet = Assets.bullet3;
				break;
			case Bullet.LEVEL_3:
				animBullet = Assets.bullet4;
				break;
			case Bullet.LEVEL_4_AND_UP:
				animBullet = Assets.bullet5;
				break;
			}

			if (obj.state == Bullet.STATE_DESTROY)
				continue;

			// BALA
			{
				Sprite spriteFrame = animBullet.getKeyFrame(obj.stateTime, false);

				if (obj.isFacingLeft) {
					spriteFrame.setPosition(obj.position.x + .1f, obj.position.y - .1f);
					spriteFrame.setSize(-.2f, .2f);
					spriteFrame.draw(batcher);
				}
				else {
					spriteFrame.setPosition(obj.position.x - .1f, obj.position.y - .1f);
					spriteFrame.setSize(.2f, .2f);
					spriteFrame.draw(batcher);
				}

			}

			// MUZZLE FIRE
			if (obj.state == Bullet.STATE_MUZZLE) {
				Sprite spriteFrame = Assets.muzzle.getKeyFrame(obj.stateTime, false);
				if (obj.isFacingLeft) {
					spriteFrame.setPosition(oWorld.oHero.position.x + .1f - .42f, oWorld.oHero.position.y - .1f - .14f);
					spriteFrame.setSize(-.2f, .2f);
				}
				else {
					spriteFrame.setPosition(oWorld.oHero.position.x - .1f + .42f, oWorld.oHero.position.y - .1f - .14f);
					spriteFrame.setSize(.2f, .2f);
				}
				spriteFrame.draw(batcher);
			}

			// MUZZLE HIT
			if (obj.state == Bullet.STATE_HIT) {
				Sprite spriteFrame = Assets.muzzle.getKeyFrame(obj.stateTime, false);
				if (obj.isFacingLeft) { // Aqui es lo mismo que muzzle fire pero alreves
					spriteFrame.setPosition(obj.position.x - .1f, obj.position.y - .1f);
					spriteFrame.setSize(.2f, .2f);
				}
				else {
					spriteFrame.setPosition(obj.position.x + .1f, obj.position.y - .1f);
					spriteFrame.setSize(-.2f, .2f);
				}
				spriteFrame.draw(batcher);
			}

		}

	}

	private void drawPlayer() {

		Hero obj = oWorld.oHero;

		AnimationSprite heroClimb = null;
		AnimationSprite heroDie = null;
		Sprite heroHurt = null;
		Sprite heroIdle = null;
		AnimationSprite heroShoot = null;
		AnimationSprite heroWalk = null;

		switch (obj.tipo) {
		case Hero.TIPO_FORCE:
			heroClimb = Assets.heroForceClimb;
			heroDie = Assets.heroForceDie;
			heroHurt = Assets.heroForceHurt;
			heroIdle = Assets.heroForceIdle;
			heroShoot = Assets.heroForceShoot;
			heroWalk = Assets.heroForceWalk;
			break;

		case Hero.TIPO_RAMBO:
			heroClimb = Assets.heroRamboClimb;
			heroDie = Assets.heroRamboDie;
			heroHurt = Assets.heroRamboHurt;
			heroIdle = Assets.heroRamboIdle;
			heroShoot = Assets.heroRamboShoot;
			heroWalk = Assets.heroRamboWalk;
			break;
		case Hero.TIPO_SOLDIER:
			heroClimb = Assets.heroSoldierClimb;
			heroDie = Assets.heroSoldierDie;
			heroHurt = Assets.heroSoldierHurt;
			heroIdle = Assets.heroSoldierIdle;
			heroShoot = Assets.heroSoldierShoot;
			heroWalk = Assets.heroSoldierWalk;
			break;
		case Hero.TIPO_SWAT:
			heroClimb = Assets.heroSwatClimb;
			heroDie = Assets.heroSwatDie;
			heroHurt = Assets.heroSwatHurt;
			heroIdle = Assets.heroSwatIdle;
			heroShoot = Assets.heroSwatShoot;
			heroWalk = Assets.heroSwatWalk;
			break;
		case Hero.TIPO_VADER:
			heroClimb = Assets.heroVaderClimb;
			heroDie = Assets.heroVaderDie;
			heroHurt = Assets.heroVaderHurt;
			heroIdle = Assets.heroVaderIdle;
			heroShoot = Assets.heroVaderShoot;
			heroWalk = Assets.heroVaderWalk;
			break;
		}

		Sprite spriteFrame;

		if (obj.state == Hero.STATE_NORMAL) {
			if (obj.isClimbing) {
				spriteFrame = heroClimb.getKeyFrame(obj.stateTime, true);
			}
			else if (obj.isWalking)
				spriteFrame = heroWalk.getKeyFrame(obj.stateTime, true);
			else if (obj.isFiring) {
				spriteFrame = heroShoot.getKeyFrame(obj.stateTime, true);
			}
			else {
				spriteFrame = heroIdle;
			}
		}
		else if (obj.state == Hero.STATE_DEAD) {
			spriteFrame = heroDie.getKeyFrame(obj.stateTime, false);
		}
		else if (obj.state == Hero.STATE_HURT) {
			spriteFrame = heroHurt;
		}
		else
			spriteFrame = null;

		// Si esta escalando lo dibujo siempre del mismo lado
		if (obj.isClimbing) {
			spriteFrame.setPosition(obj.position.x + .35f, obj.position.y - .34f);
			spriteFrame.setSize(-.7f, .77f);
			spriteFrame.draw(batcher);
		}
		else if (obj.isFacingLeft) {
			spriteFrame.setPosition(obj.position.x + .29f, obj.position.y - .34f);
			spriteFrame.setSize(-.7f, .7f);
			spriteFrame.draw(batcher);
		}
		else {
			spriteFrame.setPosition(obj.position.x - .29f, obj.position.y - .34f);
			spriteFrame.setSize(.7f, .7f);
			spriteFrame.draw(batcher);
		}
	}

	class ParallaxCamera extends OrthographicCamera {
		Matrix4 parallaxView = new Matrix4();
		Matrix4 parallaxCombined = new Matrix4();
		Vector3 tmp = new Vector3();
		Vector3 tmp2 = new Vector3();

		public ParallaxCamera(float viewportWidth, float viewportHeight) {
			super(viewportWidth, viewportHeight);
		}

		public Matrix4 calculateParallaxMatrix(float parallaxX, float parallaxY) {
			update();
			tmp.set(position);
			tmp.x *= parallaxX;
			tmp.y *= parallaxY;

			parallaxView.setToLookAt(tmp, tmp2.set(tmp).add(direction), up);
			parallaxCombined.set(projection);
			Matrix4.mul(parallaxCombined.val, parallaxView.val);
			return parallaxCombined;
		}
	}

}
