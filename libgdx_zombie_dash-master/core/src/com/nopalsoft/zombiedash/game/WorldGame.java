package com.nopalsoft.zombiedash.game;

import java.util.Iterator;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.zombiedash.Achievements;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.Settings;
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

public class WorldGame {
	final float WIDTH = Screens.WORLD_WIDTH;
	final float HEIGHT = Screens.WORLD_HEIGHT;

	static final int STATE_RUNNING = 0;
	static final int STATE_GAMEOVER = 1;
	public int state;

	public final static float velocidadX = -2;

	float TIME_TO_SPAWN_PISO;
	float timeToSpawnPiso;

	float TIME_TO_SPAWN_ZOMBIE = 2.0f;
	float timeToSpawnZombie;

	float TIME_TO_SPAWN_SPIKE = 5f;
	float timeToSpawnSpike;

	float TIME_TO_SPAWN_SAW = 12;
	float timeToSpawnSaw;

	float TIME_TO_SPAWN_ITEM = 3.0f;// Items son corazones o escudos
	float timeToSpawnItem;

	float TIME_TO_SPAWN_GEMS = 1.8f;
	float timeToSpawnGems;

	float TIME_TO_INCREASE_SPAWN = 5;
	float timeToIncreaseSpawn;

	public World oWorldBox;
	public int gems;
	public float distance;
	public int zombiesKilled;

	public Hero oHero;

	Array<Body> arrBodies;
	Array<Pisable> arrPisables;
	Array<Zombie> arrZombies;
	Array<Bullet> arrBullets;
	Array<Items> arrItems;
	Array<Spike> arrSpikes;
	Array<Saw> arrSaws;

	public WorldGame() {
		oWorldBox = new World(new Vector2(0, -9.8f), true);
		oWorldBox.setContactListener(new Colisiones());

		arrBodies = new Array<Body>();
		arrPisables = new Array<Pisable>();
		arrZombies = new Array<Zombie>();
		arrBullets = new Array<Bullet>();
		arrItems = new Array<Items>();
		arrSpikes = new Array<Spike>();
		arrSaws = new Array<Saw>();

		timeToSpawnPiso = -.15f;

		crearPiso(3.5f);
		crearHeroPrueba();
	}

	void crearPiso() {
		crearPiso(13);
	}

	private void crearPiso(float x) {

		float y = MathUtils.random(1f);
		Piso obj = new Piso(x, y);

		BodyDef bd = new BodyDef();
		bd.position.x = x;
		bd.position.y = obj.position.y;
		bd.type = BodyType.KinematicBody;

		Body oBody = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(4, 1.13f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 8;
		fixture.friction = 0;
		oBody.createFixture(fixture);
		oBody.setUserData(obj);
		arrPisables.add(obj);
		oBody.setLinearVelocity(velocidadX, 0);

		shape.dispose();
	}

	private void createItem() {
		Items obj = null;
		int tipo = MathUtils.random(2);

		float y = MathUtils.random(2.15f, 3f);
		float x = 9;

		switch (tipo) {
		case 0:
			obj = new ItemShield(x, y);
			break;
		case 1:
			obj = new ItemHearth(x, y);
			break;
		case 2:
			obj = new ItemWeapon(x, y);
			break;
		}

		BodyDef bd = new BodyDef();
		bd.position.y = obj.position.y;
		bd.position.x = obj.position.x;
		bd.type = BodyType.KinematicBody;

		CircleShape shape = new CircleShape();
		shape.setRadius(.15f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.restitution = .3f;
		fixture.density = 1;
		fixture.friction = 1f;
		fixture.isSensor = true;

		Body oBody = oWorldBox.createBody(bd);
		oBody.createFixture(fixture);

		oBody.setUserData(obj);
		arrItems.add(obj);
		oBody.setLinearVelocity(velocidadX, 0);

		shape.dispose();

	}

	private void createGem(float x, float y) {
		Items obj = new ItemGem(x, y);

		BodyDef bd = new BodyDef();
		bd.position.y = obj.position.y;
		bd.position.x = obj.position.x;
		bd.type = BodyType.KinematicBody;

		CircleShape shape = new CircleShape();
		shape.setRadius(.15f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.restitution = .3f;
		fixture.density = 1;
		fixture.friction = 1f;
		fixture.isSensor = true;

		Body oBody = oWorldBox.createBody(bd);
		oBody.createFixture(fixture);

		oBody.setUserData(obj);
		arrItems.add(obj);
		oBody.setLinearVelocity(velocidadX, 0);

		shape.dispose();

	}

	private void createFiguraGema() {
		float yHeight = MathUtils.random(2.1f, 2.85f);

		int tipo = MathUtils.random(3);

		switch (tipo) {
		case 0:
			// 5x2
			for (int col = 0; col < 5; col++) {
				float x = 9 + col * .37f;
				for (int ren = 0; ren < 2; ren++) {
					float y = yHeight + ren * .35f;
					createGem(x, y);
				}
			}
			break;
		case 1:
			// 10x1
			for (int col = 0; col < 10; col++) {
				float x = 9 + col * .37f;
				createGem(x, yHeight);
			}
			timeToSpawnGems -= 3;// Tengo que ajustar el tiempo un poco
			break;
		default:
			break;
		}

	}

	private void createSpike() {
		float x = 9.4f, y = 5;

		Spike obj = new Spike(x, y);

		BodyDef bd = new BodyDef();
		bd.position.x = obj.position.x;
		bd.position.y = obj.position.y;
		bd.type = BodyType.DynamicBody;

		Body oBody = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(.45f, .07f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 8;
		fixture.friction = 0;
		fixture.filter.groupIndex = -1;
		oBody.createFixture(fixture).setUserData("spikeBody");

		// Sensor Izq
		CircleShape sensorPiesShape = new CircleShape();
		sensorPiesShape.setRadius(.05f);
		sensorPiesShape.setPosition(new Vector2(-.44f, -.10f));

		fixture.shape = sensorPiesShape;
		fixture.density = 0;
		fixture.restitution = 0f;
		fixture.friction = 0;
		fixture.isSensor = true;
		Fixture sensorPies = oBody.createFixture(fixture);
		sensorPies.setUserData("spikeLeft");

		sensorPiesShape.setPosition(new Vector2(.44f, -.11f));
		sensorPies = oBody.createFixture(fixture);
		sensorPies.setUserData("spikeRight");

		oBody.setFixedRotation(true);
		oBody.setUserData(obj);
		arrSpikes.add(obj);

		shape.dispose();

	}

	private void crearHeroPrueba() {
		oHero = new Hero(1.35f, 2.5f, Settings.skinSeleccionada);

		BodyDef bd = new BodyDef();
		bd.position.x = oHero.position.x;
		bd.position.y = oHero.position.y;
		bd.type = BodyType.DynamicBody;

		Body oBody = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(.17f, .32f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 8;
		fixture.friction = 0;
		Fixture cuerpo = oBody.createFixture(fixture);
		cuerpo.setUserData("cuerpo");

		PolygonShape sensorPiesShape = new PolygonShape();
		sensorPiesShape.setAsBox(.11f, .025f, new Vector2(0, -.32f), 0);
		fixture.shape = sensorPiesShape;
		fixture.density = 0;
		fixture.restitution = 0f;
		fixture.friction = 0;
		fixture.isSensor = true;
		Fixture sensorPies = oBody.createFixture(fixture);
		sensorPies.setUserData("pies");

		oBody.setFixedRotation(true);
		oBody.setUserData(oHero);
		oBody.setBullet(true);

		shape.dispose();
	}

	private void crearZombieMalo() {
		Zombie obj = null;

		int tipo = MathUtils.random(4);
		float x = 7.5f, y = 4;

		switch (tipo) {
		case 0:
			obj = new Zombie(x, y, Zombie.TIPO_CUASY);
			break;
		case 1:
			obj = new Zombie(x, y, Zombie.TIPO_FRANK);
			break;
		case 2:
			obj = new Zombie(x, y, Zombie.TIPO_KID);
			break;
		case 3:
			obj = new Zombie(x, y, Zombie.TIPO_MUMMY);
			break;
		case 4:
			obj = new Zombie(x, y, Zombie.TIPO_PAN);
			break;

		}

		BodyDef bd = new BodyDef();
		bd.position.x = obj.position.x;
		bd.position.y = obj.position.y;
		bd.type = BodyType.DynamicBody;

		Body oBody = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(.17f, .32f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 8;
		fixture.friction = 0;
		fixture.filter.groupIndex = -1;
		oBody.createFixture(fixture);

		oBody.setFixedRotation(true);
		oBody.setUserData(obj);
		arrZombies.add(obj);

		shape.dispose();

	}

	private void createBullet() {
		Bullet obj = new Bullet(oHero.position.x + .42f, oHero.position.y - .14f);

		BodyDef bd = new BodyDef();
		bd.position.x = obj.position.x;
		bd.position.y = obj.position.y;
		bd.type = BodyType.DynamicBody;

		Body oBody = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(.1f, .1f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1;
		fixture.isSensor = true;
		oBody.createFixture(fixture);

		oBody.setFixedRotation(true);
		oBody.setUserData(obj);
		oBody.setBullet(true);
		oBody.setGravityScale(0);
		arrBullets.add(obj);

		oBody.setLinearVelocity(Bullet.VELOCIDAD, 0);

	}

	private void createSaw() {
		float x = 9.4f, y = oHero.position.y;

		Saw obj = new Saw(x, y);

		BodyDef bd = new BodyDef();
		bd.position.x = obj.position.x;
		bd.position.y = obj.position.y;
		bd.type = BodyType.KinematicBody;

		Body oBody = oWorldBox.createBody(bd);

		CircleShape shape = new CircleShape();
		shape.setRadius(Saw.SIZE / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 8;
		fixture.friction = 0;
		fixture.isSensor = true;
		oBody.createFixture(fixture).setUserData("sawBody");

		oBody.setFixedRotation(true);
		oBody.setUserData(obj);
		arrSaws.add(obj);
		oBody.setAngularVelocity((float) Math.toRadians(360));

		shape.dispose();

	}

	public void update(float delta, boolean didJump, boolean isJumpPressed, boolean isFiring) {
		oWorldBox.step(delta, 8, 4);

		eliminarObjetos();

		// Spwan Objects

		timeToIncreaseSpawn += delta;
		if (timeToIncreaseSpawn >= TIME_TO_INCREASE_SPAWN) {
			timeToIncreaseSpawn -= TIME_TO_INCREASE_SPAWN;

			TIME_TO_SPAWN_ZOMBIE -= .025f;
			if (TIME_TO_SPAWN_ZOMBIE < .5f)
				TIME_TO_SPAWN_ZOMBIE = .5f;

			TIME_TO_SPAWN_SPIKE -= .05f;
			if (TIME_TO_SPAWN_SPIKE < 2.5f)
				TIME_TO_SPAWN_SPIKE = 2.5f;

		}

		spawnStuff(delta);

		if (isFiring && oHero.state == Hero.STATE_NORMAL) {
			if (Settings.numBullets > 0) {
				createBullet();
				Settings.numBullets--;
				Assets.playSound(Assets.shoot1, .75f);
			}
			else {
				Assets.playSound(Assets.noBullet, .85f);
			}
		}

		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();

			if (body.getUserData() instanceof Hero) {
				updateHeroPlayer(delta, body, didJump, isJumpPressed);
			}
			else if (body.getUserData() instanceof Pisable) {
				updatePisable(delta, body);
			}
			else if (body.getUserData() instanceof Zombie) {
				updateZombie(delta, body);
			}
			else if (body.getUserData() instanceof Bullet) {
				updateBullet(delta, body);
			}
			else if (body.getUserData() instanceof Spike) {
				updateSpike(delta, body);
			}
			// else if (body.getUserData() instanceof Crate) {
			// updateCrate(delta, body);
			// }
			else if (body.getUserData() instanceof Saw) {
				updateSaw(delta, body);
			}
			else if (body.getUserData() instanceof Items) {
				updateItems(delta, body);
			}
		}

		if (oHero.state == Hero.STATE_DEAD && oHero.stateTime >= Hero.DURATION_DEAD)
			state = STATE_GAMEOVER;

		distance += 5 * delta;

	}

	private void spawnStuff(float delta) {

		timeToSpawnPiso += delta;
		if (timeToSpawnPiso >= TIME_TO_SPAWN_PISO) {
			timeToSpawnPiso -= TIME_TO_SPAWN_PISO;
			TIME_TO_SPAWN_PISO = MathUtils.random(0f, 1.1f) + 4.1f;

			crearPiso();
		}

		timeToSpawnZombie += delta;
		if (timeToSpawnZombie >= TIME_TO_SPAWN_ZOMBIE) {
			timeToSpawnZombie -= TIME_TO_SPAWN_ZOMBIE;
			if (MathUtils.randomBoolean())
				crearZombieMalo();
		}

		timeToSpawnItem += delta;
		if (timeToSpawnItem >= TIME_TO_SPAWN_ITEM) {
			timeToSpawnItem -= TIME_TO_SPAWN_ITEM;
			if (MathUtils.randomBoolean(.4f))
				createItem();
		}

		timeToSpawnGems += delta;
		if (timeToSpawnGems >= TIME_TO_SPAWN_GEMS) {
			timeToSpawnGems -= TIME_TO_SPAWN_GEMS;
			createFiguraGema();
		}

		timeToSpawnSpike += delta;
		if (timeToSpawnSpike >= TIME_TO_SPAWN_SPIKE) {
			timeToSpawnSpike -= TIME_TO_SPAWN_SPIKE;
			if (MathUtils.randomBoolean(.5f))
				createSpike();
		}

		timeToSpawnSaw += delta;
		if (timeToSpawnSaw >= TIME_TO_SPAWN_SAW) {
			timeToSpawnSaw -= TIME_TO_SPAWN_SAW;
			TIME_TO_SPAWN_SAW = MathUtils.random(1.3f, 20f);
			createSaw();
		}
	}

	private void updateHeroPlayer(float delta, Body body, boolean didJump, boolean isJumpPressed) {
		oHero.update(delta, body, didJump, isJumpPressed);

		if (oHero.position.y < -.5f || oHero.position.x < 0) {
			oHero.die();
		}

	}

	private void updatePisable(float delta, Body body) {
		Pisable obj = (Pisable) body.getUserData();
		obj.update(delta, body);

		if (obj.position.x < -(obj.DRAW_WIDTH / 2f)) {
			obj.state = Pisable.STATE_DESTROY;
		}

	}

	private void updateZombie(float delta, Body body) {
		Zombie obj = (Zombie) body.getUserData();

		obj.update(delta, body);

		if (obj.position.y < -.75f) {
			obj.getHurt(999999);
		}

	}

	private void updateBullet(float delta, Body body) {
		Bullet obj = (Bullet) body.getUserData();
		obj.update(delta, body);

		if (obj.position.x > WIDTH + 1)
			obj.state = Bullet.STATE_DESTROY;

	}

	private void updateSpike(float delta, Body body) {
		Spike obj = (Spike) body.getUserData();
		obj.update(delta, body);

		if (obj.position.x < -.75) {
			obj.state = Spike.STATE_DESTROY;
		}

	}

	private void updateSaw(float delta, Body body) {
		Saw obj = (Saw) body.getUserData();
		obj.update(delta, body, oHero);

		if (obj.position.x < -1) {
			obj.state = Saw.STATE_DESTROY;
		}

	}

	private void updateItems(float delta, Body body) {
		Items obj = (Items) body.getUserData();
		obj.update(delta, body);

		if (obj.position.x < -.75) {
			obj.state = Items.STATE_TAKEN;
		}

	}

	private void eliminarObjetos() {
		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();

			if (!oWorldBox.isLocked()) {

				if (body.getUserData() instanceof Zombie) {
					Zombie obj = (Zombie) body.getUserData();
					if (obj.state == Zombie.STATE_DEAD && obj.stateTime >= Zombie.DURATION_DEAD || oHero.state == Hero.STATE_REVIVE) {
						arrZombies.removeValue(obj, true);
						oWorldBox.destroyBody(body);
						continue;
					}
				}
				else if (body.getUserData() instanceof Bullet) {
					Bullet obj = (Bullet) body.getUserData();
					if (obj.state == Bullet.STATE_DESTROY) {
						arrBullets.removeValue(obj, true);
						oWorldBox.destroyBody(body);
						continue;
					}
				}
				else if (body.getUserData() instanceof Items) {
					Items obj = (Items) body.getUserData();
					if (obj.state == Items.STATE_TAKEN) {
						arrItems.removeValue(obj, true);
						oWorldBox.destroyBody(body);
						continue;
					}
				}
				else if (body.getUserData() instanceof Pisable) {
					Pisable obj = (Pisable) body.getUserData();
					if (obj.state == Pisable.STATE_DESTROY) {
						arrPisables.removeValue(obj, true);
						oWorldBox.destroyBody(body);
						continue;
					}
				}
				else if (body.getUserData() instanceof Spike) {
					Spike obj = (Spike) body.getUserData();
					if (obj.state == Spike.STATE_DESTROY || oHero.state == Hero.STATE_REVIVE) {
						arrSpikes.removeValue(obj, true);
						oWorldBox.destroyBody(body);
						continue;
					}
				}
				else if (body.getUserData() instanceof Saw) {
					Saw obj = (Saw) body.getUserData();
					if (obj.state == Saw.STATE_DESTROY || oHero.state == Hero.STATE_REVIVE) {
						arrSaws.removeValue(obj, true);
						oWorldBox.destroyBody(body);
						continue;
					}
				}
			}

		}

	}

	class Colisiones implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Hero)
				beginContactHeroOtraCosa(a, b);
			else if (b.getBody().getUserData() instanceof Hero)
				beginContactHeroOtraCosa(b, a);

			if (a.getBody().getUserData() instanceof Pisable)
				beginContactPisableOtraCosa(a, b);
			else if (b.getBody().getUserData() instanceof Pisable)
				beginContactPisableOtraCosa(b, a);

			if (a.getBody().getUserData() instanceof Bullet)
				beginContactBulletOtraCosa(a, b);
			else if (b.getBody().getUserData() instanceof Bullet)
				beginContactBulletOtraCosa(b, a);

		}

		private void beginContactHeroOtraCosa(Fixture fixHero, Fixture otraCosa) {
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa instanceof Pisable) {
				if (fixHero.getUserData().equals("pies"))
					oHero.touchFloor();
			}
			else if (oOtraCosa instanceof ItemGem) {
				Items obj = (Items) oOtraCosa;
				if (oHero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
					obj.taken();
					Settings.gemsTotal++;
					gems++;
					Assets.playSound(Assets.gem, .075f);
				}

			}
			else if (oOtraCosa instanceof ItemHearth) {
				Items obj = (Items) oOtraCosa;
				if (oHero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
					obj.taken();
					oHero.getHearth();

					Assets.playSound(Assets.hearth, 1);
				}
			}
			else if (oOtraCosa instanceof ItemShield) {
				Items obj = (Items) oOtraCosa;
				if (oHero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
					obj.taken();
					oHero.getShield();
					Assets.playSound(Assets.shield, 1);
				}
			}
			else if (oOtraCosa instanceof ItemWeapon) {
				Items obj = (Items) oOtraCosa;
				if (oHero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
					obj.taken();
					Settings.numBullets += 25;
					Assets.playSound(Assets.shield, 1);
				}
			}
			else if (oOtraCosa instanceof Spike) {
				oHero.getHurt();
			}
			else if (oOtraCosa instanceof Saw) {
				oHero.getHurt();
			}
			else if (oOtraCosa instanceof Zombie) {
				Zombie obj = (Zombie) oOtraCosa;
				if (obj.state == Zombie.STATE_NORMAL || obj.state == Zombie.STATE_HURT) {
					oHero.getHurt();
					Sound sound;
					switch (oHero.tipo) {
					case Hero.TIPO_FORCE:
					case Hero.TIPO_RAMBO:
						sound = Assets.hurt1;
						break;
					case Hero.TIPO_SWAT:
						sound = Assets.hurt2;
						break;
					default:
						sound = Assets.hurt3;
						break;
					}
					Assets.playSound(sound, 1);

				}

			}

		}

		private void beginContactPisableOtraCosa(Fixture fixPisable, Fixture fixOtraCosa) {
			// Pisable objPisable = (Pisable) fixPisable.getBody().getUserData();
			Object objOtraCosa = fixOtraCosa.getBody().getUserData();

			if (objOtraCosa instanceof Zombie) {
				Zombie obj = (Zombie) objOtraCosa;
				obj.canUpdate = true;
				Sound sound = null;
				switch (obj.tipo) {

				case Zombie.TIPO_CUASY:
					sound = Assets.zombieCuasy;
					break;
				case Zombie.TIPO_FRANK:
					sound = Assets.zombieFrank;
					break;
				case Zombie.TIPO_KID:
					sound = Assets.zombieKid;
					break;
				case Zombie.TIPO_MUMMY:
					sound = Assets.zombieMummy;
					break;
				case Zombie.TIPO_PAN:
					sound = Assets.zombiePan;
					break;
				}
				Assets.playSound(sound, 1);
			}
			else if (objOtraCosa instanceof Spike) {
				Spike obj = (Spike) objOtraCosa;
				if (fixOtraCosa.getUserData().equals("spikeLeft")) {
					obj.didTouchLeft = true;
				}
				else if (fixOtraCosa.getUserData().equals("spikeRight")) {
					obj.didTouchRight = true;
				}
			}
			else if (objOtraCosa instanceof Items) {
				Items obj = (Items) objOtraCosa;
				obj.state = Items.STATE_TAKEN;

			}

		}

		private void beginContactBulletOtraCosa(Fixture fixBullet, Fixture otraCosa) {
			Object oOtraCosa = otraCosa.getBody().getUserData();
			Bullet oBullet = (Bullet) fixBullet.getBody().getUserData();

			if (oOtraCosa instanceof Zombie) {
				if (oBullet.state == Bullet.STATE_NORMAL || oBullet.state == Bullet.STATE_MUZZLE) {
					Zombie obj = (Zombie) oOtraCosa;
					if (obj.state != Zombie.STATE_RISE && obj.state != Zombie.STATE_DEAD) {

						obj.getHurt(oBullet.DAMAGE);
						oBullet.hit();
						fixBullet.getBody().setLinearVelocity(otraCosa.getBody().getLinearVelocity().x, 0);

						if (obj.state == Zombie.STATE_DEAD) {
							zombiesKilled++;
							Achievements.unlockKilledZombies(zombiesKilled);
						}

					}
				}
			}

		}

		@Override
		public void endContact(Contact contact) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a == null || b == null)
				return;

			if (a.getBody().getUserData() instanceof Hero)
				endContactHeroOtraCosa(a, b);
			else if (b.getBody().getUserData() instanceof Hero)
				endContactHeroOtraCosa(b, a);

		}

		private void endContactHeroOtraCosa(Fixture fixHero, Fixture otraCosa) {
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa instanceof Pisable) {
				if (fixHero.getUserData().equals("pies"))
					oHero.endTouchFloor();

			}
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Hero)
				preSolveHero(a, b, contact);
			else if (b.getBody().getUserData() instanceof Hero)
				preSolveHero(b, a, contact);

		}

		private void preSolveHero(Fixture fixHero, Fixture otraCosa, Contact contact) {
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa instanceof Pisable) {
				// Pisable obj = (Pisable) oOtraCosa;
				//
				// float ponyY = fixHero.getBody().getPosition().y - .30f;
				// float pisY = obj.position.y + obj.height / 2f;
				//
				// if (ponyY < pisY)
				// contact.setEnabled(false);

			}
			else if (oOtraCosa instanceof Zombie) {
				contact.setEnabled(false);
			}
			else if (oOtraCosa instanceof Spike) {
				contact.setEnabled(false);
			}

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub

		}
	}

}
