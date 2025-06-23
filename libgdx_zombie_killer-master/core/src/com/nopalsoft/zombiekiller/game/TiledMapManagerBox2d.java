package com.nopalsoft.zombiekiller.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.nopalsoft.zombiekiller.objetos.Crate;
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

public class TiledMapManagerBox2d {

	private WorldGame oWorld;
	private World oWorldBox;
	private float m_units;
	private Logger logger;
	private FixtureDef defaultFixture;

	public TiledMapManagerBox2d(WorldGame oWorld, float unitScale) {
		this.oWorld = oWorld;
		oWorldBox = oWorld.oWorldBox;
		m_units = unitScale;
		logger = new Logger("MapBodyManager", 1);

		defaultFixture = new FixtureDef();
		defaultFixture.density = 1.0f;
		defaultFixture.friction = .5f;
		defaultFixture.restitution = 0.0f;

	}

	public void createObjetosDesdeTiled(Map map) {
		crearFisicos(map, "fisicos");
		crearMalos(map, "malos");

	}

	private void crearFisicos(Map map, String layerName) {
		MapLayer layer = map.getLayers().get(layerName);

		if (layer == null) {
			logger.error("layer " + layerName + " no existe");
			return;
		}

		MapObjects objects = layer.getObjects();
		Iterator<MapObject> objectIt = objects.iterator();

		int skulls = 0;
		while (objectIt.hasNext()) {
			MapObject object = objectIt.next();

			if (object instanceof TextureMapObject) {
				continue;
			}

			MapProperties properties = object.getProperties();
			String tipo = (String) properties.get("type");
			if (tipo == null)
				continue;
			else if (tipo.equals("pisable")) {
				if (object instanceof RectangleMapObject) {
					crearPisable(object);
					continue;
				}
			}
			else if (tipo.equals("ladder")) {
				if (object instanceof RectangleMapObject) {
					createLadder(object, tipo);
					continue;
				}
			}
			else if (tipo.equals("crate")) {
				if (object instanceof RectangleMapObject) {
					createCrate(object);
					continue;
				}
			}
			else if (tipo.equals("saw")) {
				if (object instanceof RectangleMapObject) {
					createSaw(object);
					continue;
				}
			}
			else if (tipo.equals("gem") || tipo.equals("heart") || tipo.equals("star") || tipo.equals("skull") || tipo.equals("meat")
					|| tipo.equals("shield")) {
				if (object instanceof RectangleMapObject) {
					if (tipo.equals("skull"))
						skulls++;
					crearItem(object, tipo);
					continue;
				}
			}

			/**
			 * Normalmente si no ninguno es el piso
			 */
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
			bodyDef.type = BodyType.StaticBody;

			Body body = oWorldBox.createBody(bodyDef);
			body.createFixture(defaultFixture);
			body.setUserData(tipo);

			defaultFixture.shape = null;
			defaultFixture.isSensor = false;
			defaultFixture.filter.groupIndex = 0;
			shape.dispose();

		}
		if (skulls != 3)
			throw new GdxRuntimeException("#### DEBE HABER 3 SKULLS ####");

	}

	private void crearMalos(Map map, String layerName) {
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
			else if (tipo.equals("zombieCuasy") || tipo.equals("zombieFrank") || tipo.equals("zombieMummy") || tipo.equals("zombieKid")
					|| tipo.equals("zombiePan")) {
				if (object instanceof RectangleMapObject) {
					crearZombieMalo(object, tipo);
					oWorld.TOTAL_ZOMBIES_LEVEL++;
					continue;
				}
			}
			else {
				throw new GdxRuntimeException("Error en layer " + layerName + ", objeto:" + tipo);
			}
		}

	}

	private void crearPisable(MapObject object) {
		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units);
		polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f);
		defaultFixture.shape = polygon;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		Body body = oWorldBox.createBody(bodyDef);
		body.createFixture(defaultFixture);

		float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
		float y = (rectangle.y + rectangle.height * 0.5f) * m_units;
		float height = (rectangle.height * m_units * 0.5f);
		float width = (rectangle.width * m_units * 0.5f);
		body.setUserData(new Pisable(x, y, width, height));

	}

	private void createLadder(MapObject object, String tipo) {
		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units);
		polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f);
		defaultFixture.shape = polygon;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		Body body = oWorldBox.createBody(bodyDef);

		defaultFixture.isSensor = true;
		body.createFixture(defaultFixture);
		body.setUserData(tipo);

		defaultFixture.isSensor = false;

	}

	private void createCrate(MapObject object) {
		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

		PolygonShape polygon = new PolygonShape();
		float width = (rectangle.width * m_units);
		float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
		float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

		polygon.setAsBox(width / 2f, width / 2f);

		FixtureDef fixDef = new FixtureDef();
		fixDef.density = 12f;
		fixDef.friction = .7f;
		fixDef.restitution = 0.0f;
		fixDef.shape = polygon;

		Crate obj = new Crate(x, y, width);

		BodyDef bodyDef = new BodyDef();
		bodyDef.position.x = obj.position.x;
		bodyDef.position.y = obj.position.y;
		bodyDef.type = BodyType.DynamicBody;
		Body body = oWorldBox.createBody(bodyDef);

		body.createFixture(fixDef);

		oWorld.arrCrates.add(obj);
		body.setUserData(obj);

	}

	private void createSaw(MapObject object) {
		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

		float width = (rectangle.width * m_units);
		float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
		float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

		CircleShape shape = new CircleShape();
		shape.setRadius(width / 2f);

		defaultFixture.shape = shape;

		Saw obj = new Saw(x, y, width);

		BodyDef bodyDef = new BodyDef();
		bodyDef.position.x = obj.position.x;
		bodyDef.position.y = obj.position.y;
		bodyDef.type = BodyType.KinematicBody;
		Body body = oWorldBox.createBody(bodyDef);

		body.createFixture(defaultFixture);

		oWorld.arrSaws.add(obj);
		body.setUserData(obj);
		body.setAngularVelocity((float) Math.toRadians(360));

		shape.dispose();

	}

	private void crearItem(MapObject object, String tipo) {
		Items obj = null;

		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
		float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
		float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

		if (tipo.equals("gem")) {
			obj = new ItemGem(x, y);
		}
		else if (tipo.equals("heart")) {
			obj = new ItemHearth(x, y);
			Gdx.app.log("Heart", "");
		}
		else if (tipo.equals("skull")) {
			obj = new ItemSkull(x, y);
		}
		else if (tipo.equals("meat")) {
			obj = new ItemMeat(x, y);
		}
		else if (tipo.equals("shield")) {
			obj = new ItemShield(x, y);
		}
		else if (tipo.equals("star")) {
			obj = new ItemStar(x, y);
		}

		BodyDef bd = new BodyDef();
		bd.position.y = obj.position.y;
		bd.position.x = obj.position.x;
		bd.type = BodyType.StaticBody;

		CircleShape shape = new CircleShape();
		shape.setRadius(.15f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;

		Body oBody = oWorldBox.createBody(bd);
		oBody.createFixture(fixture);

		oBody.setUserData(obj);
		oWorld.arrItems.add(obj);
		shape.dispose();

	}

	private void crearZombieMalo(MapObject object, String tipo) {
		Zombie obj = null;

		Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
		float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
		float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

		if (tipo.equals("zombieCuasy")) {
			obj = new Zombie(x, y, Zombie.TIPO_CUASY);
		}
		else if (tipo.equals("zombieFrank")) {
			obj = new Zombie(x, y, Zombie.TIPO_FRANK);
		}
		else if (tipo.equals("zombieKid")) {
			obj = new Zombie(x, y, Zombie.TIPO_KID);
		}
		else if (tipo.equals("zombieMummy")) {
			obj = new Zombie(x, y, Zombie.TIPO_MUMMY);
		}
		else if (tipo.equals("zombiePan")) {
			obj = new Zombie(x, y, Zombie.TIPO_PAN);
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
		oWorld.arrZombies.add(obj);

		shape.dispose();

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
