package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.objetos.*;


public class ObjectManagerBox2d {

	WorldGame oWorld;
	World oWorldBox;

	public ObjectManagerBox2d(WorldGame oWorld) {
		this.oWorld = oWorld;
		oWorldBox = oWorld.oWorldBox;
	}

	public void crearHeroStand(float x, float y, int tipo) {
		oWorld.oPersonaje = new Personaje(x, y, tipo);

		BodyDef bd = new BodyDef();
		bd.position.x = x;
		bd.position.y = y;
		bd.type = BodyType.DynamicBody;

		Body oBody = oWorldBox.createBody(bd);

		recreateFixturePersonajeStand(oBody);

		oBody.setFixedRotation(true);
		oBody.setUserData(oWorld.oPersonaje);
		oBody.setBullet(true);
		oBody.setLinearVelocity(Personaje.VELOCIDAD_RUN, 0);

	}

	private void destroyAllFixturesFromBody(Body oBody) {
		for (Fixture fix : oBody.getFixtureList()) {
			oBody.destroyFixture(fix);
		}
		oBody.getFixtureList().clear();
	}

	public void recreateFixturePersonajeStand(Body oBody) {
		destroyAllFixturesFromBody(oBody);// Primero le quito todas las que tenga

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Personaje.WIDTH / 2f, Personaje.HEIGHT / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 10;
		fixture.friction = 0;
		Fixture cuerpo = oBody.createFixture(fixture);
		cuerpo.setUserData("cuerpo");

		PolygonShape sensorPiesShape = new PolygonShape();
		sensorPiesShape.setAsBox(Personaje.WIDTH / 2.2f, .025f, new Vector2(0, -.51f), 0);
		fixture.shape = sensorPiesShape;
		fixture.density = 0;
		fixture.restitution = 0f;
		fixture.friction = 0;
		fixture.isSensor = true;
		Fixture sensorPies = oBody.createFixture(fixture);
		sensorPies.setUserData("pies");

		shape.dispose();
		sensorPiesShape.dispose();

	}

	public void recreateFixturePersonajeSlide(Body oBody) {
		destroyAllFixturesFromBody(oBody);// Primero le quito todas las que tenga

		PolygonShape shape = new PolygonShape();
		// Para cuando se crea el cubo como es mas chico que quede en la posicion correcta
		shape.setAsBox(Personaje.WIDTH / 2f, Personaje.HEIGHT_SLIDE / 2f, new Vector2(0, -.25f), 0);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 10;
		fixture.friction = 0;
		Fixture cuerpo = oBody.createFixture(fixture);
		cuerpo.setUserData("cuerpo");

		PolygonShape sensorPiesShape = new PolygonShape();
		sensorPiesShape.setAsBox(Personaje.WIDTH / 2.2f, .025f, new Vector2(0, -.51f), 0);
		fixture.shape = sensorPiesShape;
		fixture.density = 0;
		fixture.restitution = 0f;
		fixture.friction = 0;
		fixture.isSensor = true;
		Fixture sensorPies = oBody.createFixture(fixture);
		sensorPies.setUserData("pies");

		shape.dispose();
		sensorPiesShape.dispose();

	}

	public void crearMascota(float x, float y) {
		oWorld.oMascota = new Mascota(x, y, Settings.skinMascotaSeleccionada);

		BodyDef bd = new BodyDef();
		bd.position.set(x, y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		CircleShape shape = new CircleShape();
		shape.setRadius(Mascota.RADIUS);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(oWorld.oMascota);

		shape.dispose();

	}

	public float crearItem(Class<? extends Item> itemClass, float x, float y) {
		Item obj = Pools.obtain(itemClass);
		x += obj.WIDTH / 2f;

		obj.init(x, y);

		BodyDef bd = new BodyDef();
		bd.position.set(obj.position.x, obj.position.y);
		bd.type = BodyType.KinematicBody;

		Body body = oWorldBox.createBody(bd);

		CircleShape shape = new CircleShape();
		shape.setRadius(obj.WIDTH / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(obj);
		oWorld.arrItems.add(obj);

		shape.dispose();

		return x + obj.WIDTH / 2f;

	}

	/**
	 * Regresa la posicion de la orilla derecha de la caja en X
	 */
	public float crearCaja4(float x, float y) {
		ObstaculoCajas4 obj = Pools.obtain(ObstaculoCajas4.class);

		x += ObstaculoCajas4.DRAW_WIDTH / 2f;

		obj.init(x, y);

		BodyDef bd = new BodyDef();
		bd.position.set(x, y);
		bd.type = BodyType.StaticBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(.35f, .19f, new Vector2(0, -.19f), 0);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;
		body.createFixture(fixutre);

		shape.setAsBox(.18f, .19f, new Vector2(0, .19f), 0);
		fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;
		body.createFixture(fixutre);

		body.setUserData(obj);
		oWorld.arrObstaculos.add(obj);

		shape.dispose();

		return x + ObstaculoCajas4.DRAW_WIDTH / 2f;

	}

	public float crearCaja7(float x, float y) {
		ObstaculoCajas7 obj = Pools.obtain(ObstaculoCajas7.class);

		x += ObstaculoCajas7.DRAW_WIDTH / 2f;

		obj.init(x, y);

		BodyDef bd = new BodyDef();
		bd.position.set(x, y);
		bd.type = BodyType.StaticBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(.35f, .38f, new Vector2(0, -.19f), 0);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;
		body.createFixture(fixutre);

		shape.setAsBox(.18f, .19f, new Vector2(0, .38f), 0);
		fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;
		body.createFixture(fixutre);

		body.setUserData(obj);
		oWorld.arrObstaculos.add(obj);

		shape.dispose();

		return x + ObstaculoCajas7.DRAW_WIDTH / 2f;

	}

	/**
	 * 
	 * 
	 * @param x
	 *            poiscion de la izq inferior
	 * @param y
	 *            posicion de la izq inferior
	 * @param numPlats
	 *            numero de plataformas pegadas
	 * @return
	 */
	public float crearPlataforma(float x, float y, int numPlats) {

		float yCenter = Plataforma.HEIGHT / 2f + y;

		float xInicio = x;
		Plataforma oPlat = null;
		for (int i = 0; i < numPlats; i++) {
			oPlat = Pools.obtain(Plataforma.class);
			x += Plataforma.WIDTH / 2f;
			oPlat.init(x, yCenter);
			oWorld.arrPlataformas.add(oPlat);
			// Le resto el -.01 para que quede un pixel a la izquiera y no aparesca la linea cuando dos plataformas estan pegadas
			x += Plataforma.WIDTH / 2f - .01f;
		}

		xInicio += Plataforma.WIDTH / 2f * numPlats - (.005f * numPlats);

		// AQUI TENGO QUE AJUSTAR LA POSICION EN X DE LA PLATAFORMA para que no overlapee a las anteriores

		BodyDef bd = new BodyDef();
		bd.position.set(xInicio, yCenter);
		bd.type = BodyType.StaticBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Plataforma.WIDTH / 2f * numPlats - (.005f * numPlats), Plataforma.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.friction = 0;

		body.createFixture(fixutre);
		body.setUserData(oPlat);

		shape.dispose();

		return xInicio + Plataforma.WIDTH * numPlats / 2f;

	}

	public float crearPared(float x, float y) {
		Pared oPard = Pools.obtain(Pared.class);

		x += Pared.WIDTH / 2f;
		oPard.init(x, y);

		BodyDef bd = new BodyDef();
		bd.position.set(oPard.position.x, oPard.position.y);
		bd.type = BodyType.StaticBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Pared.WIDTH / 2f, Pared.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(oPard);
		oWorld.arrPared.add(oPard);

		shape.dispose();

		return x + Pared.WIDTH / 2f;

	}

	public void crearMissil(float x, float y) {
		Missil obj = Pools.obtain(Missil.class);
		obj.init(x, y);

		BodyDef bd = new BodyDef();
		bd.position.set(obj.position.x, obj.position.y);
		bd.type = BodyType.KinematicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Missil.WIDTH / 2f, Missil.HEIGHT / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(obj);
		body.setLinearVelocity(Missil.VELOCIDAD_X, 0);
		oWorld.arrMissiles.add(obj);

		shape.dispose();

	}

}
