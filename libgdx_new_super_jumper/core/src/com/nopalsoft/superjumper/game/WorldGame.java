package com.nopalsoft.superjumper.game;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
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
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.superjumper.Settings;
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

public class WorldGame {
	final float WIDTH = Screens.WORLD_WIDTH;
	final float HEIGHT = Screens.WORLD_HEIGHT;

	final public static int STATE_RUNNING = 0;
	final public static int STATE_GAMEOVER = 1;
	int state;

	float TIME_TO_CREATE_NUBE = 15;
	float timeToCreateNube;

	public World oWorldBox;

	Personaje oPer;
	private Array<Body> arrBodies;
	Array<Plataformas> arrPlataformas;
	Array<PiezaPlataformas> arrPiezasPlataformas;
	Array<Moneda> arrMonedas;
	Array<Enemigo> arrEnemigo;
	Array<Item> arrItem;
	Array<Nube> arrNubes;
	Array<Rayo> arrRayos;
	Array<Bullet> arrBullets;

	public int coins;
	public int distanciaMax;
	float mundoCreadoHastaY;

	public WorldGame() {
		oWorldBox = new World(new Vector2(0, -9.8f), true);
		oWorldBox.setContactListener(new Colisiones());

		arrBodies = new Array<Body>();
		arrPlataformas = new Array<Plataformas>();
		arrPiezasPlataformas = new Array<PiezaPlataformas>();
		arrMonedas = new Array<Moneda>();
		arrEnemigo = new Array<Enemigo>();
		arrItem = new Array<Item>();
		arrNubes = new Array<Nube>();
		arrRayos = new Array<Rayo>();
		arrBullets = new Array<Bullet>();

		timeToCreateNube = 0;

		state = STATE_RUNNING;

		crearPiso();
		crearPersonaje();

		mundoCreadoHastaY = oPer.position.y;
		crearSiguienteParte();

	}

	private void crearSiguienteParte() {
		float y = mundoCreadoHastaY + 2;

		for (int i = 0; mundoCreadoHastaY < (y + 10); i++) {
			mundoCreadoHastaY = y + (i * 2);

			crearPlataforma(mundoCreadoHastaY);
			crearPlataforma(mundoCreadoHastaY);

			if (MathUtils.random(100) < 5)
				Moneda.createMoneda(oWorldBox, arrMonedas, mundoCreadoHastaY);

			if (MathUtils.random(20) < 5)
				Moneda.createUnaMoneda(oWorldBox, arrMonedas, mundoCreadoHastaY + .5f);

			if (MathUtils.random(20) < 5)
				crearEnemigo(mundoCreadoHastaY + .5f);

			if (timeToCreateNube >= TIME_TO_CREATE_NUBE) {
				crearNubes(mundoCreadoHastaY + .7f);
				timeToCreateNube = 0;
			}

			if (MathUtils.random(50) < 5)
				createItem(mundoCreadoHastaY + .5f);
		}

	}

	/**
	 * El piso solo aparece 1 vez, al principio del juego
	 */
	private void crearPiso() {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;

		Body body = oWorldBox.createBody(bd);

		EdgeShape shape = new EdgeShape();
		shape.set(0, 0, Screens.WORLD_WIDTH, 0);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;

		body.createFixture(fixutre);
		body.setUserData("piso");

		shape.dispose();

	}

	private void crearPersonaje() {
		oPer = new Personaje(2.4f, .5f);

		BodyDef bd = new BodyDef();
		bd.position.set(oPer.position.x, oPer.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Personaje.WIDTH / 2f, Personaje.HEIGTH / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.density = 10;
		fixutre.friction = 0;
		fixutre.restitution = 0;

		body.createFixture(fixutre);
		body.setUserData(oPer);
		body.setFixedRotation(true);

		shape.dispose();
	}

	private void crearPlataforma(float y) {

		Plataformas oPlat = Pools.obtain(Plataformas.class);
		oPlat.init(MathUtils.random(Screens.WORLD_WIDTH), y, MathUtils.random(1));

		BodyDef bd = new BodyDef();
		bd.position.set(oPlat.position.x, oPlat.position.y);
		bd.type = BodyType.KinematicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Plataformas.WIDTH_NORMAL / 2f, Plataformas.HEIGTH_NORMAL / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;

		body.createFixture(fixutre);
		body.setUserData(oPlat);
		arrPlataformas.add(oPlat);

		shape.dispose();

	}

	/**
	 * La plataforma rompible son 2 cuadros
	 * 
	 * @param i
	 */
	private void crearPiezasPlataforma(Plataformas oPlat) {
		crearPiezasPlataforma(oPlat, PiezaPlataformas.TIPO_LEFT);
		crearPiezasPlataforma(oPlat, PiezaPlataformas.TIPO_RIGHT);

	}

	private void crearPiezasPlataforma(Plataformas oPla, int tipo) {
		PiezaPlataformas oPieza;
		float x;
		float angularVelocity = 100;

		if (tipo == PiezaPlataformas.TIPO_LEFT) {
			x = oPla.position.x - PiezaPlataformas.WIDTH_NORMAL / 2f;
			angularVelocity *= -1;
		}
		else {
			x = oPla.position.x + PiezaPlataformas.WIDTH_NORMAL / 2f;
		}

		oPieza = Pools.obtain(PiezaPlataformas.class);
		oPieza.init(x, oPla.position.y, tipo, oPla.color);

		BodyDef bd = new BodyDef();
		bd.position.set(oPieza.position.x, oPieza.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(PiezaPlataformas.WIDTH_NORMAL / 2f, PiezaPlataformas.HEIGTH_NORMAL / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(oPieza);
		body.setAngularVelocity(MathUtils.degRad * angularVelocity);
		arrPiezasPlataformas.add(oPieza);

		shape.dispose();
	}

	private void crearEnemigo(float y) {
		Enemigo oEn = Pools.obtain(Enemigo.class);
		oEn.init(MathUtils.random(Screens.WORLD_WIDTH), y);

		BodyDef bd = new BodyDef();
		bd.position.set(oEn.position.x, oEn.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Enemigo.WIDTH / 2f, Enemigo.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(oEn);
		body.setGravityScale(0);

		float velocidad = MathUtils.random(1f, Enemigo.VELOCIDAD_X);

		if (MathUtils.randomBoolean())
			body.setLinearVelocity(velocidad, 0);
		else
			body.setLinearVelocity(-velocidad, 0);
		arrEnemigo.add(oEn);

		shape.dispose();
	}

	private void createItem(float y) {
		Item oItem = Pools.obtain(Item.class);
		oItem.init(MathUtils.random(Screens.WORLD_WIDTH), y);

		BodyDef bd = new BodyDef();
		bd.position.set(oItem.position.x, oItem.position.y);
		bd.type = BodyType.StaticBody;
		Body oBody = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Item.WIDTH / 2f, Item.HEIGHT / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		oBody.createFixture(fixture);
		oBody.setUserData(oItem);
		shape.dispose();
		arrItem.add(oItem);
	}

	private void crearNubes(float y) {
		Nube oNube = Pools.obtain(Nube.class);
		oNube.init(MathUtils.random(Screens.WORLD_WIDTH), y);

		BodyDef bd = new BodyDef();
		bd.position.set(oNube.position.x, oNube.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Nube.WIDTH / 2f, Nube.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(oNube);
		body.setGravityScale(0);

		float velocidad = MathUtils.random(1f, Nube.VELOCIDAD_X);

		if (MathUtils.randomBoolean())
			body.setLinearVelocity(velocidad, 0);
		else
			body.setLinearVelocity(-velocidad, 0);
		arrNubes.add(oNube);

		shape.dispose();
	}

	private void crearRayo(float x, float y) {
		Rayo oRayo = Pools.obtain(Rayo.class);
		oRayo.init(x, y);

		BodyDef bd = new BodyDef();
		bd.position.set(oRayo.position.x, oRayo.position.y);
		bd.type = BodyType.KinematicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Rayo.WIDTH / 2f, Rayo.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(oRayo);

		body.setLinearVelocity(0, Rayo.VELOCIDAD_Y);
		arrRayos.add(oRayo);

		shape.dispose();
	}

	private void crearBullet(float origenX, float origenY, float destinoX, float destinoY) {
		Bullet oBullet = Pools.obtain(Bullet.class);
		oBullet.init(origenX, origenY);

		BodyDef bd = new BodyDef();
		bd.position.set(oBullet.position.x, oBullet.position.y);
		bd.type = BodyType.KinematicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Bullet.SIZE / 2f, Bullet.SIZE / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(oBullet);
		body.setBullet(true);

		Vector2 destino = new Vector2(destinoX, destinoY);
		destino.sub(oBullet.position).nor().scl(Bullet.VELOCIDAD_XY);

		body.setLinearVelocity(destino.x, destino.y);

		arrBullets.add(oBullet);

		shape.dispose();
	}

	public void update(float delta, float acelX, boolean fire, Vector3 touchPositionWorldCoords) {
		oWorldBox.step(delta, 8, 4);

		eliminarObjetos();

		/**
		 * REviso si es necesario generar la siquiete parte del mundo
		 */
		if (oPer.position.y + 10 > mundoCreadoHastaY) {
			crearSiguienteParte();
		}

		timeToCreateNube += delta;// Actualizo el tiempo para crear una nube

		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();
			if (body.getUserData() instanceof Personaje) {
				updatePersonaje(body, delta, acelX, fire, touchPositionWorldCoords);
			}
			else if (body.getUserData() instanceof Plataformas) {
				updatePlataforma(body, delta);
			}
			else if (body.getUserData() instanceof PiezaPlataformas) {
				updatePiezaPlataforma(body, delta);
			}
			else if (body.getUserData() instanceof Moneda) {
				updateMoneda(body, delta);
			}
			else if (body.getUserData() instanceof Enemigo) {
				updateEnemigo(body, delta);
			}
			else if (body.getUserData() instanceof Item) {
				updateItem(body, delta);
			}
			else if (body.getUserData() instanceof Nube) {
				updateNube(body, delta);
			}
			else if (body.getUserData() instanceof Rayo) {
				updateRayo(body, delta);
			}
			else if (body.getUserData() instanceof Bullet) {
				updateBullet(body, delta);
			}

		}

		if (distanciaMax < (oPer.position.y * 10)) {
			distanciaMax = (int) (oPer.position.y * 10);
		}

		// Si el personaje esta 5.5f mas abajo de la altura maxima se muere (Se multiplica por 10 porque la distancia se multiplica por 10 )
		if (oPer.state == Personaje.STATE_NORMAL && distanciaMax - (5.5f * 10) > (oPer.position.y * 10)) {
			oPer.die();
		}
		if (oPer.state == Personaje.STATE_DEAD && distanciaMax - (25 * 10) > (oPer.position.y * 10)) {
			state = STATE_GAMEOVER;
		}

	}

	private void eliminarObjetos() {
		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();

			if (!oWorldBox.isLocked()) {

				if (body.getUserData() instanceof Plataformas) {
					Plataformas oPlat = (Plataformas) body.getUserData();
					if (oPlat.state == Plataformas.STATE_DESTROY) {
						arrPlataformas.removeValue(oPlat, true);
						oWorldBox.destroyBody(body);
						if (oPlat.tipo == Plataformas.TIPO_ROMPIBLE)
							crearPiezasPlataforma(oPlat);
						Pools.free(oPlat);
					}
				}
				else if (body.getUserData() instanceof Moneda) {
					Moneda oMon = (Moneda) body.getUserData();
					if (oMon.state == Moneda.STATE_TAKEN) {
						arrMonedas.removeValue(oMon, true);
						oWorldBox.destroyBody(body);
						Pools.free(oMon);
					}
				}
				else if (body.getUserData() instanceof PiezaPlataformas) {
					PiezaPlataformas oPiez = (PiezaPlataformas) body.getUserData();
					if (oPiez.state == PiezaPlataformas.STATE_DESTROY) {
						arrPiezasPlataformas.removeValue(oPiez, true);
						oWorldBox.destroyBody(body);
						Pools.free(oPiez);
					}
				}
				else if (body.getUserData() instanceof Enemigo) {
					Enemigo oEnemy = (Enemigo) body.getUserData();
					if (oEnemy.state == Enemigo.STATE_DEAD) {
						arrEnemigo.removeValue(oEnemy, true);
						oWorldBox.destroyBody(body);
						Pools.free(oEnemy);
					}
				}
				else if (body.getUserData() instanceof Item) {
					Item oItem = (Item) body.getUserData();
					if (oItem.state == Item.STATE_TAKEN) {
						arrItem.removeValue(oItem, true);
						oWorldBox.destroyBody(body);
						Pools.free(oItem);
					}
				}
				else if (body.getUserData() instanceof Nube) {
					Nube oNube = (Nube) body.getUserData();
					if (oNube.state == Nube.STATE_DEAD) {
						arrNubes.removeValue(oNube, true);
						oWorldBox.destroyBody(body);
						Pools.free(oNube);
					}
				}
				else if (body.getUserData() instanceof Rayo) {
					Rayo oRayo = (Rayo) body.getUserData();
					if (oRayo.state == Rayo.STATE_DESTROY) {
						arrRayos.removeValue(oRayo, true);
						oWorldBox.destroyBody(body);
						Pools.free(oRayo);
					}
				}
				else if (body.getUserData() instanceof Bullet) {
					Bullet oBullet = (Bullet) body.getUserData();
					if (oBullet.state == Bullet.STATE_DESTROY) {
						arrBullets.removeValue(oBullet, true);
						oWorldBox.destroyBody(body);
						Pools.free(oBullet);
					}
				}
				else if (body.getUserData().equals("piso")) {
					if (oPer.position.y - 5.5f > body.getPosition().y || oPer.state == Personaje.STATE_DEAD) {
						oWorldBox.destroyBody(body);
					}
				}
			}
		}
	}

	private void updatePersonaje(Body body, float delta, float acelX, boolean fire, Vector3 touchPositionWorldCoords) {
		oPer.update(body, delta, acelX);

		if (Settings.numBullets > 0 && fire) {
			crearBullet(oPer.position.x, oPer.position.y, touchPositionWorldCoords.x, touchPositionWorldCoords.y);
			Settings.numBullets--;

		}

	}

	private void updatePlataforma(Body body, float delta) {
		Plataformas obj = (Plataformas) body.getUserData();
		obj.update(delta);
		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.setDestroy();
		}
	}

	private void updatePiezaPlataforma(Body body, float delta) {
		PiezaPlataformas obj = (PiezaPlataformas) body.getUserData();
		obj.update(delta, body);
		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.setDestroy();
		}

	}

	private void updateMoneda(Body body, float delta) {
		Moneda obj = (Moneda) body.getUserData();
		obj.update(delta);
		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.take();
		}

	}

	private void updateEnemigo(Body body, float delta) {
		Enemigo obj = (Enemigo) body.getUserData();
		obj.update(body, delta);
		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.hit();
		}

	}

	private void updateItem(Body body, float delta) {
		Item obj = (Item) body.getUserData();
		obj.update(delta);
		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.take();
		}
	}

	private void updateNube(Body body, float delta) {
		Nube obj = (Nube) body.getUserData();
		obj.update(body, delta);

		if (obj.isLighthing) {
			crearRayo(obj.position.x, obj.position.y - .65f);
			obj.fireLighting();
		}

		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.destroy();
		}
	}

	private void updateRayo(Body body, float delta) {
		Rayo obj = (Rayo) body.getUserData();
		obj.update(body, delta);

		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.destroy();
		}
	}

	private void updateBullet(Body body, float delta) {
		Bullet obj = (Bullet) body.getUserData();
		obj.update(body, delta);

		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.destroy();
		}
	}

	class Colisiones implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Personaje)
				beginContactPersonaje(a, b);
			else if (b.getBody().getUserData() instanceof Personaje)
				beginContactPersonaje(b, a);

			if (a.getBody().getUserData() instanceof Bullet)
				beginContactBullet(a, b);
			else if (b.getBody().getUserData() instanceof Bullet)
				beginContactBullet(b, a);

		}

		private void beginContactPersonaje(Fixture fixPersonaje, Fixture fixOtraCosa) {
			Object otraCosa = fixOtraCosa.getBody().getUserData();

			if (otraCosa.equals("piso")) {
				oPer.jump();

				if (oPer.state == Personaje.STATE_DEAD) {
					state = STATE_GAMEOVER;
				}
			}
			else if (otraCosa instanceof Plataformas) {
				Plataformas obj = (Plataformas) otraCosa;

				if (oPer.velocidad.y <= 0) {
					oPer.jump();
					if (obj.tipo == Plataformas.TIPO_ROMPIBLE) {
						obj.setDestroy();
					}
				}

			}
			else if (otraCosa instanceof Moneda) {
				Moneda obj = (Moneda) otraCosa;
				obj.take();
				coins++;
				oPer.jump();
			}
			else if (otraCosa instanceof Enemigo) {
				oPer.hit();
			}
			else if (otraCosa instanceof Rayo) {
				oPer.hit();
			}
			else if (otraCosa instanceof Item) {
				Item obj = (Item) otraCosa;
				obj.take();

				switch (obj.tipo) {
				case Item.TIPO_BUBBLE:
					oPer.setBubble();
					break;
				case Item.TIPO_JETPACK:
					oPer.setJetPack();
					break;
				case Item.TIPO_GUN:
					Settings.numBullets += 10;
					break;

				}

			}

		}

		private void beginContactBullet(Fixture fixBullet, Fixture fixOtraCosa) {
			Object otraCosa = fixOtraCosa.getBody().getUserData();
			Bullet oBullet = (Bullet) fixBullet.getBody().getUserData();

			if (otraCosa instanceof Enemigo) {
				Enemigo obj = (Enemigo) otraCosa;
				obj.hit();
				oBullet.destroy();

			}

			else if (otraCosa instanceof Nube) {
				Nube obj = (Nube) otraCosa;
				obj.hit();
				oBullet.destroy();

			}
		}

		@Override
		public void endContact(Contact contact) {

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Personaje)
				preSolveHero(a, b, contact);
			else if (b.getBody().getUserData() instanceof Personaje)
				preSolveHero(b, a, contact);

		}

		private void preSolveHero(Fixture fixPersonaje, Fixture otraCosa, Contact contact) {
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa instanceof Plataformas) {
				// Si va para arriba atraviesa la plataforma

				Plataformas obj = (Plataformas) oOtraCosa;

				float ponyY = fixPersonaje.getBody().getPosition().y - .30f;
				float pisY = obj.position.y + Plataformas.HEIGTH_NORMAL / 2f;

				if (ponyY < pisY)
					contact.setEnabled(false);

				if (obj.tipo == Plataformas.TIPO_NORMAL && oPer.state == Personaje.STATE_DEAD) {
					contact.setEnabled(false);
				}

			}

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub

		}

	}

}
