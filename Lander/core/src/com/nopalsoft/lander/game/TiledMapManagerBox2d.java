package com.nopalsoft.lander.game;

import java.util.Iterator;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;
import com.nopalsoft.lander.game.objetos.Bomba;
import com.nopalsoft.lander.game.objetos.Estrella;
import com.nopalsoft.lander.game.objetos.Gas;
import com.nopalsoft.lander.game.objetos.Laser;
import com.nopalsoft.lander.game.objetos.Nave;
import com.nopalsoft.lander.game.objetos.Plataforma;

public class TiledMapManagerBox2d {

	private WorldGame oWorld;
	private World oWorldBox;
	private float m_units;
	private Logger logger;
	private FixtureDef defaultFixture;

	public TiledMapManagerBox2d(WorldGame oWorld, float unitsPerPixel) {
		this.oWorld = oWorld;
		oWorldBox = oWorld.oWorldBox;
		m_units = unitsPerPixel;
		logger = new Logger("MapBodyManager", 1);

		defaultFixture = new FixtureDef();
		defaultFixture.density = 1.0f;
		defaultFixture.friction = .5f;
		defaultFixture.restitution = 0.0f;

	}

	public void createObjetosDesdeTiled(Map map) {
		crearFisicos(map, "fisicos");
		crearInteaccion(map, "interaccion");

	}

	private void crearInteaccion(Map map, String layerName) {
		MapLayer layer = map.getLayers().get(layerName);

		if (layer == null) {
			logger.error("layer " + layerName + " no existe");
			return;
		}

		MapObjects objects = layer.getObjects();
		Iterator<MapObject> objectIt = objects.iterator();
		while (objectIt.hasNext()) {
			MapObject object = objectIt.next();

			if (object instanceof TextureMapObject) {
				continue;
			}

			MapProperties properties = object.getProperties();
			String tipo = (String) properties.get("type");
			if (tipo == null)
				continue;
			else if (tipo.equals("estrella")) {
				crearEstrella(object);
				continue;
			}
			else if (tipo.equals("gas")) {
				crearGas(object);
				continue;
			}
			else if (tipo.equals("laser")) {
				crearLaser(object);
				continue;
			}
			else if (tipo.equals("bomba")) {
				crearBomba(object);
				continue;
			}

		}

	}

	private void crearFisicos(Map map, String layerName) {
		MapLayer layer = map.getLayers().get(layerName);

		if (layer == null) {
			logger.error("layer " + layerName + " no existe");
			return;
		}

		MapObjects objects = layer.getObjects();
		Iterator<MapObject> objectIt = objects.iterator();

		while (objectIt.hasNext()) {
			MapObject object = objectIt.next();

			if (object instanceof TextureMapObject) {
				continue;
			}

			MapProperties properties = object.getProperties();
			String tipo = (String) properties.get("type");
			if (tipo == null)
				continue;
			else if (tipo.equals("inicio") || tipo.equals("fin")) {
				creatPlataformas(object, tipo);
			}

			Shape shape;
			if (object instanceof RectangleMapObject) {
				shape = getRectangle((RectangleMapObject) object);
			}
			else if (object instanceof PolygonMapObject) {
				shape = getPolygon((PolygonMapObject) object);
			}
			else if (object instanceof PolylineMapObject) {
				shape = getPolyline((PolylineMapObject) object);
			}
			else if (object instanceof CircleMapObject) {
				shape = getCircle((CircleMapObject) object);
			}
			else {
				logger.error("non suported shape " + object);
				continue;
			}

			defaultFixture.shape = shape;

			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyDef.BodyType.StaticBody;

			Body body = oWorldBox.createBody(bodyDef);
			body.createFixture(defaultFixture);

			body.setUserData(tipo);

			defaultFixture.shape = null;
			defaultFixture.isSensor = false;
			defaultFixture.filter.groupIndex = 0;
			shape.dispose();

		}

	}

	private void creatPlataformas(MapObject object, String tipo) {
		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units);
		polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f);
		defaultFixture.shape = polygon;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		Body body = oWorldBox.createBody(bodyDef);
		body.createFixture(defaultFixture);

		float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
		float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

		float height = (rectangle.height * m_units * 0.5f);
		float width = (rectangle.width * m_units * 0.5f);

		Plataforma plataforma = new Plataforma(x, y, width, height);

		body.setUserData(plataforma);
		oWorld.arrPlataformas.add(plataforma);

		if (tipo.equals("inicio"))
			crearNave(plataforma);
		else if (tipo.equals("fin"))
			plataforma.isFinal = true;

	}

	private void crearNave(Plataforma plataforma) {
		Nave oNave = new Nave(plataforma.position.x, plataforma.position.y + plataforma.size.y / 2);

		BodyDef bd = new BodyDef();
		bd.position.x = oNave.position.x;
		bd.position.y = oNave.position.y;
		bd.type = BodyType.DynamicBody;
		Body oBody = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Nave.WIDTH / 2f, Nave.HEIGHT / 2f);// Lo puse mejor con vertices pa que no tuviera esquinas picudas y tratar de minimizar

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = Nave.DENSIDAD_INICIAL;
		fixture.restitution = 0;
		fixture.friction = .5f;
		oBody.createFixture(fixture);

		oBody.setUserData(oNave);
		oBody.setBullet(true);

		shape.dispose();

		oWorld.oNave = oNave;

	}

	private void crearEstrella(MapObject object) {
		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
		float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
		float y = (rectangle.y + rectangle.height * 0.5f) * m_units;
		float height = (rectangle.height * m_units * 0.5f);
		float width = (rectangle.width * m_units * 0.5f);

		Estrella obj = new Estrella(x, y, width, height);
		BodyDef bd = new BodyDef();
		bd.position.y = obj.position.y;
		bd.position.x = obj.position.x;
		bd.type = BodyType.StaticBody;

		PolygonShape pies = new PolygonShape();
		pies.setAsBox(.1f, .1f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = pies;
		fixture.density = .5f;
		fixture.restitution = 0f;
		fixture.friction = 0f;
		fixture.isSensor = true;
		// fixture.filter.groupIndex = CONTACT_CORREDORES;

		Body oBody = oWorldBox.createBody(bd);
		oBody.createFixture(fixture);

		oBody.setUserData(obj);

		oWorld.arrEstrellas.add(obj);
		pies.dispose();

	}

	private void crearGas(MapObject object) {
		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
		float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
		float y = (rectangle.y + rectangle.height * 0.5f) * m_units;
		float height = (rectangle.height * m_units * 0.5f);
		float width = (rectangle.width * m_units * 0.5f);

		Gas obj = new Gas(x, y, width, height);
		BodyDef bd = new BodyDef();
		bd.position.y = obj.position.y;
		bd.position.x = obj.position.x;
		bd.type = BodyType.StaticBody;

		PolygonShape pies = new PolygonShape();
		pies.setAsBox(.1f, .1f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = pies;
		fixture.density = .5f;
		fixture.restitution = 0f;
		fixture.friction = 0f;
		fixture.isSensor = true;
		// fixture.filter.groupIndex = CONTACT_CORREDORES;

		Body oBody = oWorldBox.createBody(bd);
		oBody.createFixture(fixture);

		oBody.setUserData(obj);

		oWorld.arrGas.add(obj);
		pies.dispose();

	}

	private void crearLaser(MapObject object) {
		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
		float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
		float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

		String direccion = object.getProperties().get("direccion").toString();
		float timeON, timeOFF, timeOffActual;
		// try {
		timeON = Float.parseFloat(object.getProperties().get("tiempoPrendido").toString());
		timeOFF = Float.parseFloat(object.getProperties().get("tiempoApagado").toString());
		timeOffActual = Float.parseFloat(object.getProperties().get("tiempoApagadoActual").toString());
		// }
		// catch (Exception xx) {
		// timeON = 1;
		// timeOFF = 5;
		// timeOffActual = 3;
		// }

		Laser obj = new Laser(x, y, rectangle.getWidth() * m_units, rectangle.height * m_units, timeON, timeOFF, timeOffActual, direccion);
		BodyDef bd = new BodyDef();
		bd.position.y = obj.position.y;
		bd.position.x = obj.position.x;
		bd.type = BodyType.StaticBody;

		PolygonShape pies = new PolygonShape();
		pies.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = pies;
		fixture.density = .5f;
		fixture.restitution = 0f;
		fixture.friction = 0f;
		fixture.isSensor = true;
		// fixture.filter.groupIndex = CONTACT_CORREDORES;

		Body oBody = oWorldBox.createBody(bd);
		oBody.createFixture(fixture);

		oBody.setUserData(obj);

		oWorld.arrLaser.add(obj);
		pies.dispose();

	}

	private void crearBomba(MapObject object) {
		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
		float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
		float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

		Bomba obj = new Bomba(x, y, object.getProperties().get("direccion").toString());

		BodyDef bd = new BodyDef();
		bd.position.y = obj.position.y;
		bd.position.x = obj.position.x;
		bd.type = BodyType.DynamicBody;

		CircleShape pies = new CircleShape();
		pies.setRadius(.2f);
		// pies.setAsBox(.05f, .05f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = pies;
		fixture.density = 15f;
		fixture.restitution = 0f;
		fixture.friction = 0f;
		// fixture.filter.groupIndex = CONTACT_CORREDORES;

		Body oBody = oWorldBox.createBody(bd);
		oBody.setGravityScale(0);
		oBody.createFixture(fixture);

		oBody.setUserData(obj);
		oWorld.arrBombas.add(obj);

		pies.dispose();

	}

	private Shape getRectangle(RectangleMapObject rectangleObject) {
		Rectangle rectangle = rectangleObject.getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units);
		polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f);
		return polygon;
	}

	private Shape getCircle(CircleMapObject circleObject) {
		Circle circle = circleObject.getCircle();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(circle.radius * m_units);
		circleShape.setPosition(new Vector2(circle.x * m_units, circle.y * m_units));
		return circleShape;

	}

	private Shape getPolygon(PolygonMapObject polygonObject) {
		PolygonShape polygon = new PolygonShape();
		float[] vertices = polygonObject.getPolygon().getVertices();
		float[] worldVertices = new float[vertices.length];
		float yLost = polygonObject.getPolygon().getY() * m_units;
		float xLost = polygonObject.getPolygon().getX() * m_units;

		for (int i = 0; i < vertices.length; ++i) {
			if (i % 2 == 0)
				worldVertices[i] = vertices[i] * m_units + xLost;
			else
				worldVertices[i] = vertices[i] * m_units + yLost;
		}
		polygon.set(worldVertices);

		return polygon;
	}

	private Shape getPolyline(PolylineMapObject polylineObject) {
		float[] vertices = polylineObject.getPolyline().getVertices();

		Vector2[] worldVertices = new Vector2[vertices.length / 2];
		float yLost = polylineObject.getPolyline().getY() * m_units;
		float xLost = polylineObject.getPolyline().getX() * m_units;
		for (int i = 0; i < vertices.length / 2; ++i) {
			worldVertices[i] = new Vector2();
			worldVertices[i].x = vertices[i * 2] * m_units + xLost;
			worldVertices[i].y = vertices[i * 2 + 1] * m_units + yLost;
		}
		ChainShape chain = new ChainShape();
		chain.createChain(worldVertices);
		return chain;
	}

}
