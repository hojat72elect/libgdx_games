package com.nopalsoft.sharkadventure.game;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.sharkadventure.Achievements;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.Settings;
import com.nopalsoft.sharkadventure.objetos.Barril;
import com.nopalsoft.sharkadventure.objetos.Blast;
import com.nopalsoft.sharkadventure.objetos.Chain;
import com.nopalsoft.sharkadventure.objetos.Items;
import com.nopalsoft.sharkadventure.objetos.Mina;
import com.nopalsoft.sharkadventure.objetos.Submarino;
import com.nopalsoft.sharkadventure.objetos.Tiburon;
import com.nopalsoft.sharkadventure.objetos.Torpedo;
import com.nopalsoft.sharkadventure.screens.Screens;

public class WorldGame {
	static final int STATE_RUNNING = 0;
	static final int STATE_GAMEOVER = 1;
	public int state;

	float TIME_TO_GAMEOVER = 2f;
	float timeToGameOver;

	static final float TIME_TO_SPWAN_BARRIL = 5;
	float timeToSwanBarril;

	static final float TIME_TO_SPWAN_MINA = 5;
	float timeToSpwanMina;

	static final float TIME_TO_SPWAN_MINA_CHAIN = 7;
	float timeToSpwanMinaChain;

	static final float TIME_TO_SPWAN_SUBMARINO = 15;
	float timeToSpwanSubmarino;

	static final float TIME_TO_SPWAN_ITEMS = 10;
	float timeToSpawnItems;

	World oWorldBox;
	Tiburon oTiburon;

	Array<Barril> arrBarriles;
	Array<Body> arrBodies;
	Array<Mina> arrMinas;
	Array<Chain> arrChains;
	Array<Blast> arrBlasts;
	Array<Torpedo> arrTorpedos;
	Array<Submarino> arrSubmarinos;
	Array<Items> arrItems;

	double puntuacion;

	int submarinosDestruidos;

	public WorldGame() {
		oWorldBox = new World(new Vector2(0, -4f), true);
		oWorldBox.setContactListener(new Colisiones());

		state = STATE_RUNNING;
		timeToGameOver = 0;
		puntuacion = 0;
		submarinosDestruidos = 0;

		arrBodies = new Array<Body>();
		arrBarriles = new Array<Barril>();
		arrMinas = new Array<Mina>();
		arrChains = new Array<Chain>();
		arrBlasts = new Array<Blast>();
		arrTorpedos = new Array<Torpedo>();
		arrSubmarinos = new Array<Submarino>();
		arrItems = new Array<Items>();
		oTiburon = new Tiburon(3.5f, 2f);

		timeToSwanBarril = 0;
		crearMinaChain();
		crearParedes();
		crearPersonaje(false);

	}

	private void crearParedes() {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;

		Body body = oWorldBox.createBody(bd);

		EdgeShape shape = new EdgeShape();

		// Abajo
		shape.set(0, 0, Screens.WORLD_WIDTH, 0);
		body.createFixture(shape, 0);

		// Derecha
		shape.set(Screens.WORLD_WIDTH + .5f, 0, Screens.WORLD_WIDTH + .5f, Screens.WORLD_HEIGHT);
		body.createFixture(shape, 0);

		// Arriba
		shape.set(0, Screens.WORLD_HEIGHT + .5f, Screens.WORLD_WIDTH, Screens.WORLD_HEIGHT + .5f);
		body.createFixture(shape, 0);

		// Izq
		shape.set(-.5f, 0, -.5f, Screens.WORLD_HEIGHT);
		body.createFixture(shape, 0);

		for (Fixture fix : body.getFixtureList()) {
			fix.setFriction(0);
			Filter filterData = new Filter();
			filterData.groupIndex = -1;
			fix.setFilterData(filterData);
		}

		body.setUserData("piso");

		shape.dispose();

	}

	private void crearPersonaje(boolean isFacingLeft) {

		BodyDef bd = new BodyDef();
		bd.position.set(oTiburon.position.x, oTiburon.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);
		PolygonShape shape = new PolygonShape();

		if (isFacingLeft) {
			shape.set(new float[] { .05f, .34f, -.12f, .18f, .13f, .19f, .18f, .37f });
			body.createFixture(shape, 0);

			shape.set(new float[] { -.12f, .18f, -.40f, .09f, -.40f, -.18f, -.25f, -.37f, .29f, -.39f, .36f, -.19f, .27f, -.03f, .13f, .19f });
			body.createFixture(shape, 0);

			shape.set(new float[] { .59f, .12f, .43f, -.06f, .36f, -.19f, .52f, -.33f });
			body.createFixture(shape, 0);

			shape.set(new float[] { -.40f, -.18f, -.40f, .09f, -.58f, -.05f, -.59f, -.12f });
			body.createFixture(shape, 0);

			shape.set(new float[] { .36f, -.19f, .29f, -.39f, .33f, -.34f });
			body.createFixture(shape, 0);

			shape.set(new float[] { .36f, -.19f, .43f, -.06f, .27f, -.03f });
			body.createFixture(shape, 0);
		}
		else {
			shape.set(new float[] { -.13f, .19f, .12f, .18f, -.05f, .34f, -.18f, .37f });
			body.createFixture(shape, 0);

			shape.set(new float[] { -.27f, -.03f, -.36f, -.19f, -.29f, -.39f, .25f, -.37f, .40f, -.18f, .40f, .09f, .12f, .18f, -.13f, .19f });
			body.createFixture(shape, 0);

			shape.set(new float[] { -.36f, -.19f, -.43f, -.06f, -.59f, .12f, -.52f, -.33f });
			body.createFixture(shape, 0);

			shape.set(new float[] { .58f, -.05f, .40f, .09f, .40f, -.18f, .59f, -.12f });
			body.createFixture(shape, 0);

			shape.set(new float[] { -.29f, -.39f, -.36f, -.19f, -.33f, -.34f });
			body.createFixture(shape, 0);

			shape.set(new float[] { -.43f, -.06f, -.36f, -.19f, -.27f, -.03f });
			body.createFixture(shape, 0);
		}

		body.setUserData(oTiburon);
		body.setFixedRotation(true);
		body.setGravityScale(.45f);

		shape.dispose();
	}

	private void crearBarril(float x, float y) {
		Barril obj = Pools.obtain(Barril.class);
		obj.init(x, y);

		BodyDef bd = new BodyDef();
		bd.position.set(obj.position.x, obj.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Barril.WIDTH / 2f, Barril.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(obj);
		body.setFixedRotation(true);
		body.setGravityScale(.15f);
		body.setAngularVelocity(MathUtils.degRad * MathUtils.random(-50, 50));

		arrBarriles.add(obj);
		shape.dispose();
	}

	private void crearItem() {
		Items obj = Pools.obtain(Items.class);
		obj.init(Screens.WORLD_WIDTH + 1, MathUtils.random(Screens.WORLD_HEIGHT));

		BodyDef bd = new BodyDef();
		bd.position.set(obj.position.x, obj.position.y);
		bd.type = BodyType.KinematicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Items.WIDTH / 2f, Items.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(obj);
		body.setFixedRotation(true);
		body.setLinearVelocity(Items.VELOCIDAD_X, 0);

		arrItems.add(obj);
		shape.dispose();
	}

	private void crearBlast() {
		float velX = Blast.VELOCIDAD_X;
		float x = oTiburon.position.x + .3f;

		if (oTiburon.isFacingLeft) {
			velX = -Blast.VELOCIDAD_X;
			x = oTiburon.position.x - .3f;
		}
		Blast obj = Pools.obtain(Blast.class);

		obj.init(x, oTiburon.position.y - .15f);

		BodyDef bd = new BodyDef();
		bd.position.set(obj.position.x, obj.position.y);
		bd.type = BodyType.KinematicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Blast.WIDTH / 2f, Blast.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.setBullet(true);
		body.createFixture(fixutre);
		body.setUserData(obj);
		body.setFixedRotation(true);
		body.setLinearVelocity(velX, 0);

		arrBlasts.add(obj);
		shape.dispose();
	}

	private void crearTorpedo(float x, float y, boolean goLeft) {
		float velX = Torpedo.VELOCIDAD_X;
		if (goLeft) {
			velX = -Torpedo.VELOCIDAD_X;
		}
		Torpedo obj = Pools.obtain(Torpedo.class);
		obj.init(x, y, goLeft);

		BodyDef bd = new BodyDef();
		bd.position.set(obj.position.x, obj.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Blast.WIDTH / 2f, Blast.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(obj);
		body.setGravityScale(0);
		body.setFixedRotation(true);
		body.setLinearVelocity(velX, 0);

		arrTorpedos.add(obj);
		shape.dispose();
	}

	private void crearMina(float x, float y) {
		Mina obj = Pools.obtain(Mina.class);
		obj.init(x, y);

		BodyDef bd = new BodyDef();
		bd.position.set(obj.position.x, obj.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		CircleShape shape = new CircleShape();
		shape.setRadius(Mina.WIDTH / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(obj);
		body.setFixedRotation(true);
		body.setGravityScale(0);
		body.setLinearVelocity(Mina.VELOCIDAD_X, 0);

		arrMinas.add(obj);
		shape.dispose();
	}

	private void crearSubmarino() {
		Submarino obj = Pools.obtain(Submarino.class);
		float x, y, xTarget, yTarget;
		switch (MathUtils.random(1, 4)) {
		case 1:
			x = -1;
			y = -1;
			xTarget = Screens.WORLD_WIDTH + 6;
			yTarget = Screens.WORLD_HEIGHT + 6;
			break;
		case 2:
			x = -1;
			y = Screens.WORLD_HEIGHT + 1;
			xTarget = Screens.WORLD_WIDTH + 6;
			yTarget = -6;
			break;
		case 3:
			x = Screens.WORLD_WIDTH + 1;
			y = Screens.WORLD_HEIGHT + 1;
			xTarget = -6;
			yTarget = -6;
			break;
		case 4:
		default:
			x = Screens.WORLD_WIDTH + 1;
			y = -1;
			xTarget = -6;
			yTarget = Screens.WORLD_HEIGHT + 6;
			break;
		}

		obj.init(x, y, xTarget, yTarget);

		BodyDef bd = new BodyDef();
		bd.position.set(obj.position.x, obj.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Submarino.WIDTH / 2f, Submarino.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(obj);
		body.setFixedRotation(true);
		body.setGravityScale(0);
		arrSubmarinos.add(obj);
		shape.dispose();
	}

	private void crearMinaChain() {
		float x = 10;
		Mina obj = Pools.obtain(Mina.class);
		obj.init(x, 1);
		obj.tipo = Mina.TIPO_GRIS;

		BodyDef bd = new BodyDef();
		bd.position.set(obj.position.x, obj.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		CircleShape shape = new CircleShape();
		shape.setRadius(Mina.WIDTH / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;
		fixutre.density = 1;

		body.createFixture(fixutre);
		body.setUserData(obj);
		body.setFixedRotation(true);
		body.setGravityScale(-3.5f);
		// body.setLinearVelocity(Mina.VELOCIDAD_X, 0);

		arrMinas.add(obj);
		shape.dispose();

		PolygonShape chainShape = new PolygonShape();
		chainShape.setAsBox(Chain.WIDTH / 2f, Chain.HEIGHT / 2f);

		fixutre.isSensor = false;
		fixutre.shape = chainShape;
		fixutre.filter.groupIndex = -1;

		int numEslabones = MathUtils.random(5, 15);
		Body link = null;
		for (int i = 0; i < numEslabones; i++) {
			Chain objChain = Pools.obtain(Chain.class);
			objChain.init(x, Chain.HEIGHT * i);
			bd.position.set(objChain.position.x, objChain.position.y);
			if (i == 0) {
				objChain.init(x, -.12f);// Hace que el cuerpo kinematico aparezca un poco mas abajo para no estar chocando con el
				bd.position.set(objChain.position.x, objChain.position.y);
				bd.type = BodyType.KinematicBody;
				link = oWorldBox.createBody(bd);
				link.createFixture(fixutre);
				link.setLinearVelocity(Chain.VELOCIDAD_X, 0);

			}
			else {
				bd.type = BodyType.DynamicBody;
				Body newLink = oWorldBox.createBody(bd);
				newLink.createFixture(fixutre);
				crearRevoltureJoint(link, newLink, 0, Chain.HEIGHT / 2f, 0, -Chain.HEIGHT / 2f);
				link = newLink;
			}
			arrChains.add(objChain);
			link.setUserData(objChain);
		}

		crearRevoltureJoint(link, body, 0, Chain.HEIGHT / 2f, 0, -Mina.HEIGHT / 2f);

	}

	private void crearRevoltureJoint(Body bodyA, Body bodyB, float anchorAX, float anchorAY, float anchorBX, float anchorBY) {
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.localAnchorA.set(anchorAX, anchorAY);
		jointDef.localAnchorB.set(anchorBX, anchorBY);
		jointDef.bodyA = bodyA;
		jointDef.bodyB = bodyB;
		oWorldBox.createJoint(jointDef);
	}

	public void update(float delta, float accelX, boolean didSwimUp, boolean didFire) {
		oWorldBox.step(delta, 8, 4);

		eliminarObjetos();

		timeToSwanBarril += delta;
		if (timeToSwanBarril >= TIME_TO_SPWAN_BARRIL) {
			timeToSwanBarril -= TIME_TO_SPWAN_BARRIL;

			if (MathUtils.randomBoolean()) {
				for (int i = 0; i < 6; i++) {
					crearBarril(MathUtils.random(Screens.WORLD_WIDTH), MathUtils.random(5.5f, 8.5f));
				}
			}
		}

		timeToSpwanMina += delta;
		if (timeToSpwanMina >= TIME_TO_SPWAN_MINA) {
			timeToSpwanMina -= TIME_TO_SPWAN_MINA;
			for (int i = 0; i < 5; i++) {
				if (MathUtils.randomBoolean())
					crearMina(MathUtils.random(9, 10f), MathUtils.random(Screens.WORLD_HEIGHT));
			}
		}

		if (arrMinas.size == 0)
			crearMina(9, MathUtils.random(Screens.WORLD_HEIGHT));

		timeToSpwanMinaChain += delta;
		if (timeToSpwanMinaChain >= TIME_TO_SPWAN_MINA_CHAIN) {
			timeToSpwanMinaChain -= TIME_TO_SPWAN_MINA_CHAIN;
			if (MathUtils.randomBoolean(.75f))
				crearMinaChain();
		}

		timeToSpwanSubmarino += delta;
		if (timeToSpwanSubmarino >= TIME_TO_SPWAN_SUBMARINO) {
			timeToSpwanSubmarino -= TIME_TO_SPWAN_SUBMARINO;
			if (MathUtils.randomBoolean(.65f)) {
				crearSubmarino();
				if (Settings.isSoundOn)
					Assets.sSonar.play();
			}
		}

		timeToSpawnItems += delta;
		if (timeToSpawnItems >= TIME_TO_SPWAN_ITEMS) {
			timeToSpawnItems -= TIME_TO_SPWAN_ITEMS;
			if (MathUtils.randomBoolean()) {
				crearItem();
			}
		}

		oWorldBox.getBodies(arrBodies);

		Iterator<Body> i = arrBodies.iterator();
		while (i.hasNext()) {
			Body body = i.next();

			if (body.getUserData() instanceof Tiburon) {
				updatePersonaje(body, delta, accelX, didSwimUp, didFire);
			}
			else if (body.getUserData() instanceof Barril) {
				updateBarriles(body, delta);
			}
			else if (body.getUserData() instanceof Mina) {
				updateMina(body, delta);
			}
			else if (body.getUserData() instanceof Chain) {
				updateChain(body, delta);
			}
			else if (body.getUserData() instanceof Blast) {
				updateBlast(body, delta);
			}
			else if (body.getUserData() instanceof Torpedo) {
				updateTorpedo(body, delta);
			}
			else if (body.getUserData() instanceof Submarino) {
				updateSubmarino(body, delta);
			}
			else if (body.getUserData() instanceof Items) {
				updateItems(body, delta);
			}

		}

		if (oTiburon.state == Tiburon.STATE_DEAD) {
			timeToGameOver += delta;
			if (timeToGameOver >= TIME_TO_GAMEOVER) {
				state = STATE_GAMEOVER;
			}
		}
		else {
			puntuacion += delta * 15;
		}

		Achievements.distance((long) puntuacion, oTiburon.didGetHurtOnce);

	}

	private void eliminarObjetos() {
		Iterator<Body> i = arrBodies.iterator();
		while (i.hasNext()) {
			Body body = i.next();

			if (!oWorldBox.isLocked()) {
				if (body.getUserData() instanceof Barril) {
					Barril obj = (Barril) body.getUserData();
					if (obj.state == Barril.STATE_REMOVE) {
						arrBarriles.removeValue(obj, true);
						Pools.free(obj);
						oWorldBox.destroyBody(body);
					}
				}
				else if (body.getUserData() instanceof Mina) {
					Mina obj = (Mina) body.getUserData();
					if (obj.state == Mina.STATE_REMOVE) {
						arrMinas.removeValue(obj, true);
						Pools.free(obj);
						oWorldBox.destroyBody(body);
					}
				}
				else if (body.getUserData() instanceof Chain) {
					Chain obj = (Chain) body.getUserData();
					if (obj.state == Chain.STATE_REMOVE) {
						arrChains.removeValue(obj, true);
						Pools.free(obj);
						oWorldBox.destroyBody(body);

					}
				}
				else if (body.getUserData() instanceof Blast) {
					Blast obj = (Blast) body.getUserData();
					if (obj.state == Blast.STATE_REMOVE) {
						arrBlasts.removeValue(obj, true);
						Pools.free(obj);
						oWorldBox.destroyBody(body);
					}
				}
				else if (body.getUserData() instanceof Torpedo) {
					Torpedo obj = (Torpedo) body.getUserData();
					if (obj.state == Torpedo.STATE_REMOVE) {
						arrTorpedos.removeValue(obj, true);
						Pools.free(obj);
						oWorldBox.destroyBody(body);
					}
				}
				else if (body.getUserData() instanceof Submarino) {
					Submarino obj = (Submarino) body.getUserData();
					if (obj.state == Submarino.STATE_REMOVE) {
						arrSubmarinos.removeValue(obj, true);
						Pools.free(obj);
						oWorldBox.destroyBody(body);
					}
				}
				else if (body.getUserData() instanceof Items) {
					Items obj = (Items) body.getUserData();
					if (obj.state == Items.STATE_REMOVE) {
						arrItems.removeValue(obj, true);
						Pools.free(obj);
						oWorldBox.destroyBody(body);
					}
				}

			}

		}
	}

	private void updatePersonaje(Body body, float delta, float accelX, boolean didSwimUp, boolean didFire) {
		// Si cambia de posicion tengo que hacer otra vez el cuerpo
		if (oTiburon.didFlipX) {
			oTiburon.didFlipX = false;
			oWorldBox.destroyBody(body);
			crearPersonaje(oTiburon.isFacingLeft);
		}

		if (didFire && oTiburon.state == Tiburon.STATE_NORMAL) {
			if (oTiburon.energy > 0) {
				crearBlast();
				if (Settings.isSoundOn) {
					Assets.sBlast.play();
				}
			}
			oTiburon.fire();

		}

		oTiburon.update(body, delta, accelX, didSwimUp);

	}

	private void updateBarriles(Body body, float delta) {
		Barril obj = (Barril) body.getUserData();
		obj.update(body, delta);

	}

	private void updateMina(Body body, float delta) {
		Mina obj = (Mina) body.getUserData();
		obj.update(body, delta);

	}

	private void updateChain(Body body, float delta) {
		Chain obj = (Chain) body.getUserData();
		obj.update(body, delta);

	}

	private void updateBlast(Body body, float delta) {
		Blast obj = (Blast) body.getUserData();
		obj.update(body, delta);

	}

	private void updateTorpedo(Body body, float delta) {
		Torpedo obj = (Torpedo) body.getUserData();
		obj.update(body, delta);
	}

	private void updateSubmarino(Body body, float delta) {
		Submarino obj = (Submarino) body.getUserData();
		obj.update(body, delta);

		if (obj.didFire) {
			obj.didFire = false;

			if (obj.velocity.x > 0)
				crearTorpedo(obj.position.x, obj.position.y, false);
			else
				crearTorpedo(obj.position.x, obj.position.y, true);
		}

	}

	private void updateItems(Body body, float delta) {
		Items obj = (Items) body.getUserData();
		obj.update(body, delta);
	}

	class Colisiones implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Tiburon) {
				beginContactTiburon(a, b);
			}
			else if (b.getBody().getUserData() instanceof Tiburon) {
				beginContactTiburon(b, a);
			}
			else if (a.getBody().getUserData() instanceof Blast) {
				beginContactBlast(a, b);
			}
			else if (b.getBody().getUserData() instanceof Blast) {
				beginContactBlast(b, a);
			}
			else {
				beginContactOtrasCosas(a, b);
			}

		}

		private void beginContactBlast(Fixture fixBlast, Fixture fixOtraCosa) {
			Object otraCosa = fixOtraCosa.getBody().getUserData();
			Blast oBlast = (Blast) fixBlast.getBody().getUserData();

			if (otraCosa instanceof Barril) {
				Barril obj = (Barril) otraCosa;
				if (obj.state == Barril.STATE_NORMAL) {
					obj.hit();
					oBlast.hit();

				}

			}
			else if (otraCosa instanceof Mina) {
				Mina obj = (Mina) otraCosa;
				if (obj.state == Mina.STATE_NORMAL) {
					obj.hit();
					oBlast.hit();

				}
			}
			else if (otraCosa instanceof Chain) {
				Chain obj = (Chain) otraCosa;
				if (obj.state == Chain.STATE_NORMAL) {
					obj.hit();
					oBlast.hit();
				}
			}
			else if (otraCosa instanceof Submarino) {
				Submarino obj = (Submarino) otraCosa;
				if (obj.state == Submarino.STATE_NORMAL) {
					obj.hit();
					oBlast.hit();

					if (obj.state == Submarino.STATE_EXPLODE) {
						submarinosDestruidos++;
						Achievements.unlockKilledSubmarines(submarinosDestruidos);
					}

				}
			}

		}

		private void beginContactTiburon(Fixture fixTiburon, Fixture fixOtraCosa) {
			Object otraCosa = fixOtraCosa.getBody().getUserData();

			if (otraCosa instanceof Barril) {
				Barril obj = (Barril) otraCosa;
				if (obj.state == Barril.STATE_NORMAL) {
					obj.hit();
					oTiburon.hit();
				}
			}
			else if (otraCosa instanceof Mina) {
				Mina obj = (Mina) otraCosa;
				if (obj.state == Mina.STATE_NORMAL) {
					obj.hit();
					oTiburon.hit();
				}
			}
			else if (otraCosa instanceof Torpedo) {
				Torpedo obj = (Torpedo) otraCosa;
				if (obj.state == Torpedo.STATE_NORMAL) {
					obj.hit();
					oTiburon.hit();
					oTiburon.hit();
					oTiburon.hit();
				}
			}
			else if (otraCosa instanceof Items) {
				Items obj = (Items) otraCosa;
				if (obj.state == Items.STATE_NORMAL) {
					if (obj.tipo == Items.TIPO_CARNE) {
						oTiburon.energy += 15;
					}
					else {
						oTiburon.life += 1;
					}
					obj.hit();
				}
			}

		}

		public void beginContactOtrasCosas(Fixture fixA, Fixture fixB) {
			Object objA = fixA.getBody().getUserData();
			Object objB = fixB.getBody().getUserData();

			if (objA instanceof Barril && objB instanceof Mina) {
				((Barril) objA).hit();
				((Mina) objB).hit();
			}
			else if (objA instanceof Mina && objB instanceof Barril) {
				((Barril) objB).hit();
				((Mina) objA).hit();
			}

		}

		@Override
		public void endContact(Contact contact) {

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {

		}

	}

}
