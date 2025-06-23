package com.nopalsoft.zombiedash.game;

import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.zombiedash.AnimationSprite;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.objects.Bullet;
import com.nopalsoft.zombiedash.objects.Hero;
import com.nopalsoft.zombiedash.objects.ItemGem;
import com.nopalsoft.zombiedash.objects.ItemHearth;
import com.nopalsoft.zombiedash.objects.ItemShield;
import com.nopalsoft.zombiedash.objects.ItemWeapon;
import com.nopalsoft.zombiedash.objects.Items;
import com.nopalsoft.zombiedash.objects.Pisable;
import com.nopalsoft.zombiedash.objects.Piso;
import com.nopalsoft.zombiedash.objects.Saw;
import com.nopalsoft.zombiedash.objects.Spike;
import com.nopalsoft.zombiedash.objects.Zombie;
import com.nopalsoft.zombiedash.screens.Screens;

public class WorldGameRenderer2 {

	final float WIDTH = Screens.WORLD_WIDTH;
	final float HEIGHT = Screens.WORLD_HEIGHT;

	SpriteBatch batcher;
	WorldGame oWorld;
	OrthographicCamera oCam;

	Box2DDebugRenderer renderBox;

	public WorldGameRenderer2(SpriteBatch batcher, WorldGame oWorld) {

		this.oCam = new OrthographicCamera(WIDTH, HEIGHT);
		this.oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
		this.batcher = batcher;
		this.oWorld = oWorld;
		this.renderBox = new Box2DDebugRenderer();

	}

	public void render(float delta) {

		oCam.update();
		batcher.setProjectionMatrix(oCam.combined);
		batcher.begin();
		batcher.enableBlending();

		drawPisables();
		drawItems();
		drawCrates();
		drawZombie();
		drawSpikes();
		drawSaw();
		drawBullets();
		drawPlayer();

		batcher.end();

		// renderBox.render(oWorld.oWorldBox, oCam.combined);
	}

	private void drawPisables() {
		AtlasRegion keyframe = null;

		Iterator<Pisable> i = oWorld.arrPisables.iterator();
		while (i.hasNext()) {
			Pisable obj = i.next();

			if (obj instanceof Piso) {
				keyframe = Assets.pisoVerde;

			}
			// else if (obj instanceof ItemHearth) {
			// keyframe = Assets.itemHeart;
			// }
			// else if (obj instanceof ItemMeat) {
			// keyframe = Assets.itemMeat;
			// }
			// else if (obj instanceof ItemSkull) {
			// keyframe = Assets.itemSkull;
			// }
			// else if (obj instanceof ItemShield) {
			// keyframe = Assets.itemShield;
			// }
			// else if (obj instanceof ItemStar) {
			// keyframe = Assets.itemStar;
			// }

			batcher.draw(keyframe, obj.position.x - obj.DRAW_WIDTH / 2f, obj.position.y - obj.DRAW_HEIGHT / 2f, obj.DRAW_WIDTH, obj.DRAW_HEIGHT);

		}

	}

	private void drawCrates() {

		// Iterator<Crate> i = oWorld.arrCrates.iterator();
		// while (i.hasNext()) {
		// Crate obj = i.next();
		// float halfSize = obj.SIZE / 2f;
		// batcher.draw(Assets.crate, obj.position.x - halfSize, obj.position.y - halfSize, halfSize, halfSize, obj.SIZE, obj.SIZE, 1, 1,
		// obj.angleDeg);
		//
		// }

	}

	private void drawSaw() {

		Iterator<Saw> i = oWorld.arrSaws.iterator();
		while (i.hasNext()) {
			Saw obj = i.next();

			if (obj.state == Saw.STATE_NORMAL) {
				float halfSize = (Saw.SIZE + .2f) / 2f;
				batcher.draw(Assets.saw, obj.position.x - halfSize, obj.position.y - halfSize, halfSize, halfSize, Saw.SIZE + .2f, Saw.SIZE + .2f, 1,
						1, obj.angleDeg);
			}
			else if (obj.state == Saw.STATE_DIALOG) {
				batcher.draw(Assets.sawDialog, 7.35f, obj.position.y - .3f, .7f, .6f);

			}
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
			else if (obj instanceof ItemShield) {
				keyframe = Assets.itemShield;
			}
			else if (obj instanceof ItemWeapon) {
				keyframe = Assets.weaponSmall;
			}

			batcher.draw(keyframe, obj.position.x - obj.DRAW_WIDTH / 2f, obj.position.y - obj.DRAW_HEIGHT / 2f, obj.DRAW_WIDTH, obj.DRAW_HEIGHT);

		}

	}

	private void drawZombie() {

		Iterator<Zombie> i = oWorld.arrZombies.iterator();
		while (i.hasNext()) {

			Zombie obj = i.next();
			if (!obj.canUpdate)
				continue;
			AnimationSprite animWalk = null;
			AnimationSprite animRise = null;
			AnimationSprite animDie = null;
			Sprite zombieHurt = null;

			float ajusteY = 0;

			switch (obj.tipo) {
			case Zombie.TIPO_CUASY:
				animWalk = Assets.zombieCuasyWalk;
				animRise = Assets.zombieCuasyRise;
				animDie = Assets.zombieCuasyDie;
				zombieHurt = Assets.zombieCuasyHurt;
				ajusteY = -.035f;
				break;

			case Zombie.TIPO_FRANK:
				animWalk = Assets.zombieFrankWalk;
				animRise = Assets.zombieFrankRise;
				animDie = Assets.zombieFrankDie;
				zombieHurt = Assets.zombieFrankHurt;
				ajusteY = -.033f;
				break;
			case Zombie.TIPO_KID:
				animWalk = Assets.zombieKidWalk;
				animRise = Assets.zombieKidRise;
				animDie = Assets.zombieKidDie;
				zombieHurt = Assets.zombieKidHurt;
				break;
			case Zombie.TIPO_MUMMY:
				animWalk = Assets.zombieMummyWalk;
				animRise = Assets.zombieMummyRise;
				animDie = Assets.zombieMummyDie;
				zombieHurt = Assets.zombieMummyHurt;
				ajusteY = -.035f;
				break;
			case Zombie.TIPO_PAN:
				animWalk = Assets.zombiePanWalk;
				animRise = Assets.zombiePanRise;
				animDie = Assets.zombiePanDie;
				zombieHurt = Assets.zombiePanHurt;
				ajusteY = -.038f;
				break;
			}

			Sprite spriteFrame;

			if (obj.state == Zombie.STATE_NORMAL) {

				spriteFrame = animWalk.getKeyFrame(obj.stateTime, true);
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

			// // Barra de vidas
			if (obj.vidas > 0 && (obj.state == Zombie.STATE_NORMAL || obj.state == Zombie.STATE_HURT))
				batcher.draw(Assets.redBar, obj.position.x - .33f, obj.position.y + .36f, .65f * ((float) obj.vidas / obj.MAX_LIFE), .075f);
		}

	}

	private void drawSpikes() {
		TextureRegion keyframe = Assets.spike;

		Iterator<Spike> i = oWorld.arrSpikes.iterator();
		while (i.hasNext()) {
			Spike obj = i.next();

			batcher.draw(keyframe, obj.position.x - obj.DRAW_WIDTH / 2f, obj.position.y - obj.DRAW_HEIGHT / 2f, obj.DRAW_WIDTH, obj.DRAW_HEIGHT);

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

		AnimationSprite heroRun = null;
		AnimationSprite heroDie = null;
		AnimationSprite heroJump = null;
		Sprite heroHurt = null;

		switch (obj.tipo) {
		case Hero.TIPO_FORCE:
			heroRun = Assets.heroForceRun;
			heroDie = Assets.heroForceDie;
			heroHurt = Assets.heroForceHurt;
			heroJump = Assets.heroForceJump;
			break;
		case Hero.TIPO_RAMBO:
			heroRun = Assets.heroRamboRun;
			heroDie = Assets.heroRamboDie;
			heroHurt = Assets.heroRamboHurt;
			heroJump = Assets.heroRamboJump;
			break;
		case Hero.TIPO_SOLDIER:
			heroRun = Assets.heroSoldierRun;
			heroDie = Assets.heroSoldierDie;
			heroHurt = Assets.heroSoldierHurt;
			heroJump = Assets.heroSoldierJump;
			break;
		case Hero.TIPO_SWAT:
			heroRun = Assets.heroSwatRun;
			heroDie = Assets.heroSwatDie;
			heroHurt = Assets.heroSwatHurt;
			heroJump = Assets.heroSwatJump;
			break;
		case Hero.TIPO_VADER:
			heroRun = Assets.heroVaderRun;
			heroDie = Assets.heroVaderDie;
			heroHurt = Assets.heroVaderHurt;
			heroJump = Assets.heroVaderJump;
			break;
		}

		Sprite spriteFrame;

		if (obj.state == Hero.STATE_NORMAL) {
			if (obj.isJumping) {
				spriteFrame = heroJump.getKeyFrame(obj.stateTime, false);
			}
			else
				spriteFrame = heroRun.getKeyFrame(obj.stateTime, true);

		}
		else if (obj.state == Hero.STATE_DEAD) {
			spriteFrame = heroDie.getKeyFrame(obj.stateTime, false);

		}
		else if (obj.state == Hero.STATE_HURT) {
			spriteFrame = heroHurt;

		}
		else
			spriteFrame = null;

		if (spriteFrame == null)
			return;

		spriteFrame.setPosition(obj.position.x - .29f, obj.position.y - .34f);
		spriteFrame.setSize(.7f, .7f);
		spriteFrame.draw(batcher);
	}
}
