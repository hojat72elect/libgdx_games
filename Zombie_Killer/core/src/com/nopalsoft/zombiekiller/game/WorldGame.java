package com.nopalsoft.zombiekiller.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.Settings;
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
import com.nopalsoft.zombiekiller.objetos.Pisable;
import com.nopalsoft.zombiekiller.objetos.Saw;
import com.nopalsoft.zombiekiller.objetos.Zombie;
import com.nopalsoft.zombiekiller.screens.Screens;

public class WorldGame {
	final float WIDTH = Screens.WORLD_WIDTH;
	final float HEIGHT = Screens.WORLD_HEIGHT;

	static final int STATE_RUNNING = 0;
	static final int STATE_GAMEOVER = 1;
	static final int STATE_NEXT_LEVEL = 2;
	public int state;

	float TIME_TO_FIRE_AGAIN = .3f;
	float timeToFireAgain;

	/**
	 * Por el momento hice una prueba con 110 tiles de width y todo funciona bien, no se pasa el background
	 */

	/**
	 * Mis tiles son de 32px, asi que la unidad seria 1/32 con una camara ortograpicha de 10x15 para ver 10 tiles de ancho y 15 de alto. El probema es que mi camara es de 8x4.8f por eso tengo que
	 * cambiar la escala, con 1/32 solo veria 8 tiles a lo ancho y de altura 4.8 por como esta configurada la camara.
	 * 
	 * con 1/96 veo 24 tiles a lo ancho
	 */
	float unitScale = 1 / 76f;
	public int tiledWidth;
	public int tiledHeight;

	public World oWorldBox;
	public int gems;
	public int skulls;

	public int TOTAL_ZOMBIES_LEVEL;
	public int totalZombiesKilled;

	public int bonus;// Si se matan todos los zombies, las gemas recolectadas x2

	Hero oHero;

	Array<Zombie> arrZombies;
	Array<Items> arrItems;
	Array<Crate> arrCrates;
	Array<Bullet> arrBullets;
	Array<Saw> arrSaws;
	Array<Body> arrBodies;

	public WorldGame() {
		oWorldBox = new World(new Vector2(0, -9.8f), true);
		oWorldBox.setContactListener(new Colisiones());

		arrItems = new Array<Items>();
		arrZombies = new Array<Zombie>();
		arrBullets = new Array<Bullet>();
		arrCrates = new Array<Crate>();
		arrSaws = new Array<Saw>();
		arrBodies = new Array<Body>();

		new TiledMapManagerBox2d(this, unitScale).createObjetosDesdeTiled(Assets.map);
		tiledWidth = ((TiledMapTileLayer) Assets.map.getLayers().get("1")).getWidth();
		tiledHeight = ((TiledMapTileLayer) Assets.map.getLayers().get("1")).getHeight();

		if (tiledWidth * tiledHeight > 2500) {
			Gdx.app.log("Advertencia de rendimiento", "Hay mas de 2500 tiles " + tiledWidth + " x " + tiledHeight + " = "
					+ (tiledWidth * tiledHeight));
		}

		Gdx.app.log("Tile Width", tiledWidth + "");
		Gdx.app.log("Tile Height", tiledHeight + "");

		crearHeroPrueba();

		state = STATE_RUNNING;

	}

	private void crearHeroPrueba() {
		oHero = new Hero(1.35f, 1.6f, Settings.skinSeleccionada);

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

	private void createBullet(boolean isFiring, float delta) {
		// No puede disparar si esta escalando
		if (oHero.isClimbing || oHero.state == Hero.STATE_HURT || oHero.state == Hero.STATE_DEAD)
			return;

		if (isFiring) {
			timeToFireAgain += delta;
			if (timeToFireAgain >= TIME_TO_FIRE_AGAIN) {
				timeToFireAgain -= TIME_TO_FIRE_AGAIN;
				createBullet();
				Assets.playSound(Assets.shoot1, .75f);

			}
		}
		else {
			timeToFireAgain = TIME_TO_FIRE_AGAIN;
		}

	}

	private void createBullet() {
		boolean isFacingLeft = oHero.isFacingLeft;
		Bullet obj;

		if (isFacingLeft) {
			obj = new Bullet(oHero.position.x - .42f, oHero.position.y - .14f, oHero.isFacingLeft);
		}
		else {
			obj = new Bullet(oHero.position.x + .42f, oHero.position.y - .14f, oHero.isFacingLeft);
		}

		if (!oHero.isWalking)
			oHero.fire();// Pone el estado en fire y aparece la animacion

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

		if (isFacingLeft)
			oBody.setLinearVelocity(-Bullet.VELOCIDAD, 0);
		else
			oBody.setLinearVelocity(Bullet.VELOCIDAD, 0);

	}

	private void createItemFromZombie(float x, float y) {
		Items obj = null;
		int tipo = MathUtils.random(4);

		switch (tipo) {
		case 0:
			obj = new ItemGem(x, y);
			break;
		case 1:
			obj = new ItemHearth(x, y);
			break;
		case 2:
			obj = new ItemShield(x, y);
			break;
		default:
			obj = new ItemMeat(x, y);
			break;

		}

		BodyDef bd = new BodyDef();
		bd.position.y = obj.position.y;
		bd.position.x = obj.position.x;
		bd.type = BodyType.DynamicBody;

		CircleShape shape = new CircleShape();
		shape.setRadius(.15f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.restitution = .3f;
		fixture.density = 1;
		fixture.friction = 1f;
		fixture.filter.groupIndex = -1;

		Body oBody = oWorldBox.createBody(bd);
		oBody.createFixture(fixture);

		oBody.setUserData(obj);
		arrItems.add(obj);
		shape.dispose();

	}

	public void update(float delta, boolean didJump, boolean isFiring, float accelX, float accelY) {
		oWorldBox.step(delta, 8, 4);

		eliminarObjetos();

		createBullet(isFiring, delta);

		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();

			if (body.getUserData() instanceof Hero) {
				updateHeroPlayer(delta, body, didJump, accelX, accelY);
			}
			else if (body.getUserData() instanceof Zombie) {
				updateZombieMalo(delta, body);
			}
			else if (body.getUserData() instanceof Bullet) {
				updateBullet(delta, body);
			}
			else if (body.getUserData() instanceof Crate) {
				updateCrate(delta, body);
			}
			else if (body.getUserData() instanceof Saw) {
				updateSaw(delta, body);
			}
			else if (body.getUserData() instanceof Items) {
				updateItems(delta, body);
			}
		}

		if (oHero.state == Hero.STATE_DEAD && oHero.stateTime >= Hero.DURATION_DEAD)
			state = STATE_GAMEOVER;

	}

	private void updateZombieMalo(float delta, Body body) {
		Zombie obj = (Zombie) body.getUserData();

		if (obj.position.x > oHero.position.x - 2 && obj.position.x < oHero.position.x + 2 && obj.position.y < oHero.position.y + .5f
				&& obj.position.y > oHero.position.y - .5f && !obj.canUpdate) {
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

		obj.update(delta, body, 0, oHero);

		if (obj.position.y < -.5f) {
			obj.die();
		}
	}

	private void updateHeroPlayer(float delta, Body body, boolean didJump, float accelX, float accelY) {
		oHero.update(delta, body, didJump, accelX, accelY);

		if (oHero.position.y < -.5f) {
			oHero.die();
		}

	}

	private void updateBullet(float delta, Body body) {
		Bullet obj = (Bullet) body.getUserData();
		obj.update(delta, body);

		if (obj.position.x > oHero.position.x + 4 || obj.position.x < oHero.position.x - 4)
			obj.state = Bullet.STATE_DESTROY;

	}

	private void updateCrate(float delta, Body body) {
		Crate obj = (Crate) body.getUserData();
		obj.update(delta, body);

	}

	private void updateSaw(float delta, Body body) {
		Saw obj = (Saw) body.getUserData();
		obj.update(delta, body);

	}

	private void updateItems(float delta, Body body) {
		Items obj = (Items) body.getUserData();
		obj.update(delta, body);

	}

	private void eliminarObjetos() {
		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();

			if (!oWorldBox.isLocked()) {

				if (body.getUserData() instanceof Items) {
					Items obj = (Items) body.getUserData();
					if (obj.state == Items.STATE_TAKEN) {
						arrItems.removeValue(obj, true);
						oWorldBox.destroyBody(body);
						continue;
					}
				}
				else if (body.getUserData() instanceof Zombie) {
					Zombie obj = (Zombie) body.getUserData();
					if (obj.state == Zombie.STATE_DEAD && obj.stateTime >= Zombie.DURATION_DEAD) {
						float x = obj.position.x;
						float y = obj.position.y;
						arrZombies.removeValue(obj, true);
						oWorldBox.destroyBody(body);
						totalZombiesKilled++;
						Settings.gemsTotal += 3;
						gems += 3;

						if (MathUtils.random(50) <= ((Settings.LEVEL_CHANCE_DROP + 1) * 2))
							createItemFromZombie(x, y);
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

			if (a.getBody().getUserData() instanceof Bullet)
				beginContactBulletOtraCosa(a, b);
			else if (b.getBody().getUserData() instanceof Bullet)
				beginContactBulletOtraCosa(b, a);

		}

		private void beginContactHeroOtraCosa(Fixture fixHero, Fixture otraCosa) {
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa.equals("ladder")) {
				oHero.isOnStairs = true;
			}
			else if (oOtraCosa.equals("suelo") || oOtraCosa instanceof Pisable) {
				if (fixHero.getUserData().equals("pies"))
					oHero.canJump = true;

			}
			else if (oOtraCosa instanceof Crate) {
				// Crate obj = (Crate) oOtraCosa;

				if (fixHero.getUserData().equals("pies")) {
					oHero.canJump = true;
					oHero.bodyCrate = otraCosa.getBody();
				}

			}
			else if (oOtraCosa.equals("spikes")) {
				oHero.die();
			}
			else if (oOtraCosa instanceof Saw) {
				oHero.die();
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
			else if (oOtraCosa instanceof ItemSkull) {
				Items obj = (Items) oOtraCosa;
				if (oHero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
					obj.taken();
					skulls++;

					Assets.playSound(Assets.skull, .3f);
				}
			}
			else if (oOtraCosa instanceof ItemMeat) {
				Items obj = (Items) oOtraCosa;
				if (oHero.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
					obj.taken();

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
			else if (oOtraCosa instanceof ItemStar) {
				Items obj = (Items) oOtraCosa;
				if (oHero.state != Hero.STATE_DEAD && state == STATE_RUNNING) {
					obj.taken();
					state = STATE_NEXT_LEVEL;
					if (totalZombiesKilled == TOTAL_ZOMBIES_LEVEL) {
						bonus = (int) (gems * 2.5f);
						Settings.gemsTotal += bonus;
					}
				}
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

					float impulseX = obj.isFacingLeft ? -obj.FORCE_IMPACT : obj.FORCE_IMPACT;
					float impulseY = 2.5f;

					fixHero.getBody().setLinearVelocity(impulseX, impulseY);
				}
				obj.isTouchingPlayer = true;

			}

		}

		private void beginContactBulletOtraCosa(Fixture fixBullet, Fixture otraCosa) {
			Object oOtraCosa = otraCosa.getBody().getUserData();
			Bullet oBullet = (Bullet) fixBullet.getBody().getUserData();

			if (oOtraCosa instanceof Zombie) {
				if (oBullet.state == Bullet.STATE_NORMAL || oBullet.state == Bullet.STATE_MUZZLE) {
					Zombie obj = (Zombie) oOtraCosa;
					if (obj.state != Zombie.STATE_RISE && obj.state != Zombie.STATE_DEAD) {
						// if (obj.state != Zombie.STATE_DEAD)
						// Assets.zombieHit.play();

						obj.getHurt(oBullet.DAMAGE);
						oBullet.hit();

						float impulse = 0;
						switch (obj.tipo) {
						case Zombie.TIPO_KID:
						case Zombie.TIPO_MUMMY:
						case Zombie.TIPO_CUASY:
							impulse = oBullet.isFacingLeft ? -oBullet.FORCE_IMPACT : oBullet.FORCE_IMPACT;
							break;
						}
						otraCosa.getBody().setLinearVelocity(impulse, .5f);

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
			if (oOtraCosa.equals("ladder")) {
				oHero.isOnStairs = false;
			}
			else if (oOtraCosa instanceof Zombie) {
				Zombie obj = (Zombie) oOtraCosa;
				obj.isTouchingPlayer = false;
			}
			else if (oOtraCosa instanceof Crate) {
				if (fixHero.getUserData().equals("pies")) {
					oHero.bodyCrate = null;
				}

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

			if (a.getBody().getUserData() instanceof Zombie)
				preSolveZombie(a, b, contact);
			else if (b.getBody().getUserData() instanceof Zombie)
				preSolveZombie(b, a, contact);
		}

		private void preSolveHero(Fixture fixHero, Fixture otraCosa, Contact contact) {
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa instanceof Pisable) {
				Pisable obj = (Pisable) oOtraCosa;

				if (oHero.isClimbing) {
					contact.setEnabled(false);
					return;
				}
				// Si el pony su centro - la mitad de su altura y el piso su centro mas la mitad de su altura
				// Si ponyY es menor significa q esta por abajo.

				float ponyY = fixHero.getBody().getPosition().y - .30f;
				float pisY = obj.position.y + obj.height / 2f;

				if (ponyY < pisY)
					contact.setEnabled(false);

			}
			else if (oOtraCosa instanceof Zombie) {
				contact.setEnabled(false);
			}

		}

		// Para que el zombie no se atore con las plataformas si va caminando
		private void preSolveZombie(Fixture fixZombie, Fixture otraCosa, Contact contact) {
			Object oOtraCosa = otraCosa.getBody().getUserData();
			Zombie oZombie = (Zombie) fixZombie.getBody().getUserData();
			if (oOtraCosa instanceof Pisable) {
				Pisable obj = (Pisable) oOtraCosa;

				float ponyY = fixZombie.getBody().getPosition().y - .30f;
				float pisY = obj.position.y + obj.height / 2f;

				if (ponyY < pisY)
					contact.setEnabled(false);

			}
			else if (oOtraCosa instanceof Crate) {
				if (oZombie.state == Zombie.STATE_RISE)
					contact.setEnabled(false);
			}
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {

		}

	}

}
