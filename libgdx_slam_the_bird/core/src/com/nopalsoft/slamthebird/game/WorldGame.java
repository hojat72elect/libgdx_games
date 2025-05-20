package com.nopalsoft.slamthebird.game;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.nopalsoft.slamthebird.Achievements;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.objetos.Boost;
import com.nopalsoft.slamthebird.objetos.Enemigo;
import com.nopalsoft.slamthebird.objetos.Moneda;
import com.nopalsoft.slamthebird.objetos.Plataforma;
import com.nopalsoft.slamthebird.objetos.Robot;
import com.nopalsoft.slamthebird.screens.Screens;

public class WorldGame {
	final float WIDTH = Screens.WORLD_SCREEN_WIDTH;
	final float HEIGHT = Screens.WORLD_SCREEN_HEIGHT;

	public static final float COMBO_TO_START_GETTING_COINS = 3;

	final public static int STATE_RUNNING = 0;
	final public static int STATE_GAME_OVER = 1;
	int state;

	final public float TIME_TO_SPAWN_ENEMY = 7;
	public float timeToSpawnEnemy;

	public float TIME_TO_SPAWN_BOOST = 15f;
	public float timeToSpawnBoost;

	final public float TIME_TO_CHANGE_STATE_PLATFORM = 16f; // Este tiempo debe ser mayor que DURATION_ACTIVE en la clase plataformas.
	public float timeToChangeStatePlatform;

	final public float TIME_TO_SPAWN_COIN = .75f;
	public float timeToSpawnCoin;

	public World oWorldBox;

	Robot oRobo;
	Array<Plataforma> arrPlataformas;
	Array<Enemigo> arrEnemigos;
	Array<Body> arrBodies;
	Array<Boost> arrBoost;
	Array<Moneda> arrMonedas;

	Random oRan;

	int scoreSlamed;
	int monedasTomadas;

	int combo;
	boolean isCoinRain;

	private final Pool<Boost> boostPool = new Pool<Boost>() {
		@Override
		protected Boost newObject() {
			return new Boost();
		}
	};

	private final Pool<Moneda> monedaPool = new Pool<Moneda>() {
		@Override
		protected Moneda newObject() {
			return new Moneda();
		}
	};

	public WorldGame() {
		oWorldBox = new World(new Vector2(0, -9.8f), true);
		oWorldBox.setContactListener(new Colisiones());

		state = STATE_RUNNING;
		arrBodies = new Array<Body>();
		arrEnemigos = new Array<Enemigo>();
		arrPlataformas = new Array<Plataforma>();
		arrBoost = new Array<Boost>();
		arrMonedas = new Array<Moneda>();

		oRan = new Random();

		timeToSpawnEnemy = 5;
		isCoinRain = false;

		monedasTomadas = scoreSlamed = 0;

		float posPiso = .6f;
		crearParedes(posPiso);// .05
		crearRobot(WIDTH / 2f, posPiso + .251f);

		crearPlataformas(0 + Plataforma.WIDTH / 2f, 1.8f + posPiso);// Izq Abajo
		crearPlataformas(WIDTH - Plataforma.WIDTH / 2f + .1f, 1.8f + posPiso);// Derecha abajo

		crearPlataformas(0 + Plataforma.WIDTH / 2f, 1.8f * 2f + posPiso);// Izq Arriba
		crearPlataformas(WIDTH - Plataforma.WIDTH / 2f + .1f,
				1.8f * 2f + posPiso);// Derecha Arribadd

		// Boost stuff
		TIME_TO_SPAWN_BOOST -= Settings.NIVEL_BOOST_BOOST_TIME;
	}

	private void crearParedes(float posPisoY) {
		BodyDef bd = new BodyDef();
		bd.position.x = 0;
		bd.position.y = 0;
		bd.type = BodyType.StaticBody;
		Body oBody = oWorldBox.createBody(bd);

		ChainShape shape = new ChainShape();
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(0.005f, 50);
		vertices[1] = new Vector2(0.005f, 0);
		vertices[2] = new Vector2(WIDTH, 0);
		vertices[3] = new Vector2(WIDTH, 50);
		shape.createChain(vertices);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.restitution = 0;
		fixture.friction = 0;

		oBody.createFixture(fixture);
		oBody.setUserData("pared");
		shape.dispose();

		// Piso
		EdgeShape shapePiso = new EdgeShape();
		shapePiso.set(0, 0, WIDTH, 0);
		bd.position.y = posPisoY;
		Body oBodyPiso = oWorldBox.createBody(bd);

		fixture.shape = shapePiso;
		oBodyPiso.createFixture(fixture);
		oBodyPiso.setUserData("piso");

		shapePiso.dispose();

	}

	private void crearRobot(float x, float y) {
		oRobo = new Robot(x, y);
		BodyDef bd = new BodyDef();
		bd.position.x = x;
		bd.position.y = y;
		bd.type = BodyType.DynamicBody;

		Body oBody = oWorldBox.createBody(bd);

		CircleShape shape = new CircleShape();
		shape.setRadius(Robot.RADIUS);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 5;
		fixture.restitution = 0;
		fixture.friction = 0;
		oBody.createFixture(fixture);

		oBody.setFixedRotation(true);
		oBody.setUserData(oRobo);
		oBody.setBullet(true);

		shape.dispose();
	}

	private void crearEnemigos() {
		float x = oRan.nextFloat() * (WIDTH - 1) + .5f;// Para que no apareza
		float y = oRan.nextFloat() * 4f + .6f;

		Enemigo obj = new Enemigo(x, y);
		arrEnemigos.add(obj);
		BodyDef bd = new BodyDef();
		bd.position.x = x;
		bd.position.y = y;
		bd.type = BodyType.DynamicBody;

		Body oBody = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Enemigo.WIDTH / 2f, Enemigo.HEIGHT / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 5;
		fixture.restitution = 0;
		fixture.friction = 0;
		fixture.filter.groupIndex = -1;
		oBody.createFixture(fixture);

		oBody.setFixedRotation(true);
		oBody.setGravityScale(0);
		oBody.setUserData(obj);

		shape.dispose();
	}

	private void crearPlataformas(float x, float y) {

		BodyDef bd = new BodyDef();
		bd.position.x = x;
		bd.position.y = y;
		bd.type = BodyType.StaticBody;
		Body oBody = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();

		shape.setAsBox(Plataforma.WIDTH / 2f, Plataforma.HEIGHT / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.restitution = 0;
		fixture.friction = 0;
		oBody.createFixture(fixture);

		Plataforma obj = new Plataforma(bd.position.x, bd.position.y);
		oBody.setUserData(obj);
		shape.dispose();

		arrPlataformas.add(obj);
	}

	private void crearBoost() {
		Boost obj = boostPool.obtain();

		int plat = oRan.nextInt(4);// arriba de que plataforma
		int tipo = oRan.nextInt(4);// ice, invencible,moneda,etc
		obj.init(this, arrPlataformas.get(plat).position.x,
				arrPlataformas.get(plat).position.y + .3f, tipo);

		arrBoost.add(obj);

	}

	private void crearMonedas() {

		for (int i = 0; i < 6; i++) {
			float x = 0;
			float y = 8.4f + (i * .5f);
			float velocidad = Moneda.VELOCIDAD_MOVE;
			if (i % 2f != 0) {
				velocidad *= -1;
				x = WIDTH;
			}

			Body body = Moneda.crearMoneda(oWorldBox, x, y, velocidad);
			Moneda obj = monedaPool.obtain();
			obj.init(body.getPosition().x, body.getPosition().y);
			arrMonedas.add(obj);
			body.setUserData(obj);
		}

	}

	public void updateReady(float delta, float acelX) {
		oWorldBox.step(delta, 8, 4);
		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();
		while (i.hasNext()) {
			Body body = i.next();

			if (body.getUserData() instanceof Robot) {
				oRobo.updateReady(delta, body, acelX);
				break;
			}
		}
	}

	public void update(float delta, float acelX, boolean slam) {
		oWorldBox.step(delta, 8, 4);

		eliminarObjetos();

		timeToSpawnEnemy += delta;
		timeToSpawnBoost += delta;
		timeToChangeStatePlatform += delta;
		timeToSpawnCoin += delta;

		if (timeToSpawnEnemy >= TIME_TO_SPAWN_ENEMY) {
			timeToSpawnEnemy -= TIME_TO_SPAWN_ENEMY;
			timeToSpawnEnemy += (scoreSlamed * .025f); // Hace que aparezcan mas rapido los malos
			if (arrEnemigos.size < 7 + (scoreSlamed * .15f)) {
				if (scoreSlamed <= 15) {
					crearEnemigos();
				}
				else if (scoreSlamed <= 50) {
					crearEnemigos();
					crearEnemigos();
				}
				else {
					crearEnemigos();
					crearEnemigos();
					crearEnemigos();
				}
			}

		}

		if (timeToSpawnBoost >= TIME_TO_SPAWN_BOOST) {
			timeToSpawnBoost -= TIME_TO_SPAWN_BOOST;
			if (oRan.nextBoolean())
				crearBoost();
		}

		if (timeToSpawnCoin >= TIME_TO_SPAWN_COIN) {
			timeToSpawnCoin -= TIME_TO_SPAWN_COIN;
			crearMonedas();
		}

		if (timeToChangeStatePlatform >= TIME_TO_CHANGE_STATE_PLATFORM) {
			timeToChangeStatePlatform -= TIME_TO_CHANGE_STATE_PLATFORM;
			if (oRan.nextBoolean()) {
				int plat = oRan.nextInt(4);
				int state = oRan.nextInt(2);
				Plataforma obj = arrPlataformas.get(plat);
				if (state == 0) {
					obj.setBreakable();
				}
				else if (state == 1) {
					obj.setFire();
				}
			}
		}

		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();

			if (body.getUserData() instanceof Robot) {
				updateRobot(delta, body, acelX, slam);
			}
			else if (body.getUserData() instanceof Enemigo) {
				updateEnemigo(delta, body);
			}
			else if (body.getUserData() instanceof Boost) {
				updateBoost(delta, body);
			}
			else if (body.getUserData() instanceof Plataforma) {
				updatePlataforma(delta, body);
			}
			else if (body.getUserData() instanceof Moneda) {
				updateMoneda(delta, body);
			}
		}

		isCoinRain = false;
	}

	private void eliminarObjetos() {
		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();

			if (!oWorldBox.isLocked()) {
				if (body.getUserData() instanceof Robot) {
					Robot obj = (Robot) body.getUserData();
					if (obj.state == Robot.STATE_DEAD
							&& obj.stateTime >= Robot.DURATION_DEAD_ANIMATION) {
						oWorldBox.destroyBody(body);
						state = STATE_GAME_OVER;
						continue;
					}
				}
				else if (body.getUserData() instanceof Enemigo) {
					Enemigo obj = (Enemigo) body.getUserData();
					if (obj.state == Enemigo.STATE_DEAD) {
						oWorldBox.destroyBody(body);
						arrEnemigos.removeValue(obj, true);
						scoreSlamed++;

						/*
						 * Si no hay enemigos el menos creo uno esto lo pongo aqui para que no afecte el tiempo en el que aparece el primer malo
						 */
						if (arrEnemigos.size == 0)
							crearEnemigos();
						continue;
					}
				}
				else if (body.getUserData() instanceof Boost) {
					Boost obj = (Boost) body.getUserData();
					if (obj.state == Boost.STATE_TAKEN) {
						oWorldBox.destroyBody(body);
						arrBoost.removeValue(obj, true);
						boostPool.free(obj);
						continue;
					}
				}
				else if (body.getUserData() instanceof Moneda) {
					Moneda obj = (Moneda) body.getUserData();
					if (obj.state == Moneda.STATE_TAKEN) {
						oWorldBox.destroyBody(body);
						arrMonedas.removeValue(obj, true);
						monedaPool.free(obj);
						continue;
					}
				}
			}

		}
	}

	private void updateRobot(float delta, Body body, float acelX, boolean slam) {
		Robot obj = (Robot) body.getUserData();
		obj.update(delta, body, acelX, slam);

		if (obj.position.y > 12) {
			Achievements.unlockSuperJump();
			Gdx.app.log("ACHIIIII", "Asdsadasd");
		}

	}

	private void updateEnemigo(float delta, Body body) {
		Enemigo obj = (Enemigo) body.getUserData();
		obj.update(delta, body, oRan);

	}

	private void updateBoost(float delta, Body body) {
		Boost obj = (Boost) body.getUserData();
		obj.update(delta, body);

	}

	private void updatePlataforma(float delta, Body body) {
		Plataforma obj = (Plataforma) body.getUserData();
		obj.update(delta);

	}

	private void updateMoneda(float delta, Body body) {
		Moneda obj = (Moneda) body.getUserData();
		obj.update(delta, body);

		if (obj.position.x < -3 || obj.position.x > WIDTH + 3) {
			obj.state = Moneda.STATE_TAKEN;
		}

		if (isCoinRain) {
			body.setGravityScale(1);
			body.setLinearVelocity(body.getLinearVelocity().x * .25f, 0);
		}
	}

	class Colisiones implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Robot)
				beginContactRobotOtraCosa(a, b);
			else if (b.getBody().getUserData() instanceof Robot)
				beginContactRobotOtraCosa(b, a);

			if (a.getBody().getUserData() instanceof Enemigo)
				beginContactEnemigoOtraCosa(a, b);
			else if (b.getBody().getUserData() instanceof Enemigo)
				beginContactEnemigoOtraCosa(b, a);

		}

		/**
		 * Begin contacto ROBOT con OTRA-COSA
		 * 
		 * @param robot
		 * @param otraCosa
		 */
		private void beginContactRobotOtraCosa(Fixture robot, Fixture otraCosa) {
			Robot oRobo = (Robot) robot.getBody().getUserData();
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa.equals("piso")) {
				oRobo.jump();

				if (!oRobo.isInvencible)// Si es invencible no le quito el combo
					combo = 0;
			}
			else if (oOtraCosa instanceof Plataforma) {
				Plataforma obj = (Plataforma) oOtraCosa;
				if (obj.state == Plataforma.STATE_FIRE && !oRobo.isInvencible) {
					oRobo.hit();
					return;
				}
				else if (obj.state == Plataforma.STATE_BREAKABLE) {
					obj.setBroken();
				}
				else if (obj.state == Plataforma.STATE_BROKEN) {
					return;
				}
				if (!oRobo.isInvencible && oRobo.state == Robot.STATE_FALLING)// Si es invencible no le quito el combo
					combo = 0;
				oRobo.jump();
			}
			else if (oOtraCosa instanceof Boost) {
				Boost obj = (Boost) oOtraCosa;
				obj.hit();
				Assets.playSound(Assets.soundBoost);

				if (obj.tipo == Boost.TIPO_SUPERJUMP) {
					oRobo.isSuperJump = true;
				}
				else if (obj.tipo == Boost.TIPO_INVENCIBLE) {
					oRobo.isInvencible = true;
				}
				else if (obj.tipo == Boost.TIPO_COIN_RAIN) {
					isCoinRain = true;
				}
				else if (obj.tipo == Boost.TIPO_ICE) {
					Iterator<Enemigo> i = arrEnemigos.iterator();
					while (i.hasNext()) {
						i.next().setFrozen();
					}
				}
			}
			else if (oOtraCosa instanceof Moneda) {
				Moneda obj = (Moneda) oOtraCosa;
				if (obj.state == Moneda.STATE_NORMAL) {
					obj.state = Moneda.STATE_TAKEN;
					monedasTomadas++;
					Settings.monedasActuales++;
					Assets.playSound(Assets.soundCoin);
				}

			}
			else if (oOtraCosa instanceof Enemigo) {
				Enemigo obj = (Enemigo) oOtraCosa;

				// Puedo tocar de la mitad del enemigo para arriba
				float posRobot = oRobo.position.y - Robot.RADIUS;
				float pisY = obj.position.y;

				if (obj.state != Enemigo.STATE_JUST_APPEAR) {
					if (oRobo.isInvencible) {
						obj.die();
						combo++;
					}
					else if (posRobot > pisY) {
						obj.hit();
						oRobo.jump();
						combo++;
					}
					else if (oRobo.state != Robot.STATE_DEAD) {
						oRobo.hit();
						combo = 0;
					}
					if (combo >= COMBO_TO_START_GETTING_COINS) {
						monedasTomadas += combo;
						Settings.monedasActuales += combo;
					}

					Achievements.unlockCombos(combo, oRobo.isInvencible);
				}
			}
		}

		private void beginContactEnemigoOtraCosa(Fixture enemy, Fixture otraCosa) {
			Enemigo oEnemy = (Enemigo) enemy.getBody().getUserData();
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa.equals("pared")) {

				enemy.getBody().setLinearVelocity(
						enemy.getBody().getLinearVelocity().x * -1,
						enemy.getBody().getLinearVelocity().y);
			}
			else if (oOtraCosa.equals("piso")) {
				if (oEnemy.state == Enemigo.STATE_FLYING) {
					enemy.getBody().setLinearVelocity(
							enemy.getBody().getLinearVelocity().x,
							enemy.getBody().getLinearVelocity().y * -1);
				}
			}
		}

		@Override
		public void endContact(Contact contact) {

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {

			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Robot)
				preSolveRobot(a, b, contact);
			else if (b.getBody().getUserData() instanceof Robot)
				preSolveRobot(b, a, contact);

			if (a.getBody().getUserData() instanceof Enemigo)
				preSolveEnemigo(a, b, contact);
			else if (b.getBody().getUserData() instanceof Enemigo)
				preSolveEnemigo(b, a, contact);

			if (a.getBody().getUserData() instanceof Moneda)
				preSolveMoneda(a, b, contact);
			else if (b.getBody().getUserData() instanceof Moneda)
				preSolveMoneda(b, a, contact);

		}

		private void preSolveRobot(Fixture robot, Fixture otraCosa,
				Contact contact) {
			Object oOtraCosa = otraCosa.getBody().getUserData();
			Robot oRobo = (Robot) robot.getBody().getUserData();

			// Plataforma oneSide
			if (oOtraCosa instanceof Plataforma) {
				Plataforma obj = (Plataforma) oOtraCosa;
				float posRobot = oRobo.position.y - Robot.RADIUS + .05f;
				float pisY = obj.position.y + (Plataforma.HEIGHT / 2f);

				if (posRobot < pisY || obj.state == Plataforma.STATE_BROKEN)
					contact.setEnabled(false);

			}
			// Enemigo no se puede tocar cuando aparece
			else if (oOtraCosa instanceof Enemigo) {
				Enemigo obj = (Enemigo) oOtraCosa;
				if (obj.state == Enemigo.STATE_JUST_APPEAR
						|| oRobo.isInvencible)
					contact.setEnabled(false);
			}
			else if (oOtraCosa instanceof Moneda) {
				contact.setEnabled(false);
			}
		}

		private void preSolveEnemigo(Fixture enemigo, Fixture otraCosa,
				Contact contact) {
			Object oOtraCosa = otraCosa.getBody().getUserData();
			Enemigo oEnem = (Enemigo) enemigo.getBody().getUserData();

			// Enemigo no puede tocar las plataformas si esta volando
			if (oOtraCosa instanceof Plataforma) {
				if (oEnem.state == Enemigo.STATE_FLYING)
					contact.setEnabled(false);

			}
		}

		private void preSolveMoneda(Fixture obj, Fixture otraCosa,
				Contact contact) {
			Object oOtraCosa = otraCosa.getBody().getUserData();
			// Moneda oMoneda = (Moneda) obj.getBody().getUserData();//lo comente porque no se usa "No me gustan los warnings"

			if (oOtraCosa.equals("pared")) {
				contact.setEnabled(false);
			}
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub

		}
	}

}
