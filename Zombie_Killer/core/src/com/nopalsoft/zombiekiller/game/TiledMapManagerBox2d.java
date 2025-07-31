package com.nopalsoft.zombiekiller.game;

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
import com.nopalsoft.zombiekiller.game_objects.Crate;
import com.nopalsoft.zombiekiller.game_objects.ItemGem;
import com.nopalsoft.zombiekiller.game_objects.ItemHeart;
import com.nopalsoft.zombiekiller.game_objects.ItemMeat;
import com.nopalsoft.zombiekiller.game_objects.ItemShield;
import com.nopalsoft.zombiekiller.game_objects.ItemSkull;
import com.nopalsoft.zombiekiller.game_objects.ItemStar;
import com.nopalsoft.zombiekiller.game_objects.Items;
import com.nopalsoft.zombiekiller.game_objects.Platform;
import com.nopalsoft.zombiekiller.game_objects.Saw;
import com.nopalsoft.zombiekiller.game_objects.Zombie;

import java.util.Iterator;

public class TiledMapManagerBox2d {

    private final WorldGame worldGame;
    private final World world;
    private final float m_units;
    private final Logger logger;
    private final FixtureDef defaultFixtureDefinition;

    public TiledMapManagerBox2d(WorldGame worldGame, float unitScale) {
        this.worldGame = worldGame;
        world = worldGame.world;
        m_units = unitScale;
        logger = new Logger("MapBodyManager", 1);

        defaultFixtureDefinition = new FixtureDef();
        defaultFixtureDefinition.density = 1.0f;
        defaultFixtureDefinition.friction = .5f;
        defaultFixtureDefinition.restitution = 0.0f;
    }

    public void createObjectsFromTiled(Map map) {
        createPhysics(map);
        createEnemies(map);
    }

    private void createPhysics(Map map) {
        MapLayer layer = map.getLayers().get("fisicos");

        if (layer == null) {
            logger.error("layer " + "fisicos" + " no existe");
            return;
        }

        MapObjects objects = layer.getObjects();
        Iterator<MapObject> objectIterator = objects.iterator();

        int skulls = 0;
        while (objectIterator.hasNext()) {
            MapObject object = objectIterator.next();

            if (object instanceof TextureMapObject) {
                continue;
            }

            MapProperties properties = object.getProperties();
            String type = (String) properties.get("type");
            if (type == null)
                continue;
            else if (type.equals("pisable")) {
                if (object instanceof RectangleMapObject) {
                    createPlatform(object);
                    continue;
                }
            } else if (type.equals("ladder")) {
                if (object instanceof RectangleMapObject) {
                    createLadder(object, type);
                    continue;
                }
            } else if (type.equals("crate")) {
                if (object instanceof RectangleMapObject) {
                    createCrate(object);
                    continue;
                }
            } else if (type.equals("saw")) {
                if (object instanceof RectangleMapObject) {
                    createSaw(object);
                    continue;
                }
            } else if (type.equals("gem") || type.equals("heart") || type.equals("star") || type.equals("skull") || type.equals("meat")
                    || type.equals("shield")) {
                if (object instanceof RectangleMapObject) {
                    if (type.equals("skull"))
                        skulls++;
                    createItem(object, type);
                    continue;
                }
            }

            /*
             * Normally if not none is the floor.
             */
            Shape shape;
            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject) object);
            } else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject) object);
            } else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject) object);
            } else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject) object);
            } else {
                logger.error("non suported shape " + object);
                continue;
            }

            defaultFixtureDefinition.shape = shape;

            BodyDef bodyDefinition = new BodyDef();
            bodyDefinition.type = BodyType.StaticBody;

            Body body = world.createBody(bodyDefinition);
            body.createFixture(defaultFixtureDefinition);
            body.setUserData(type);

            defaultFixtureDefinition.shape = null;
            defaultFixtureDefinition.isSensor = false;
            defaultFixtureDefinition.filter.groupIndex = 0;
            shape.dispose();
        }
        if (skulls != 3)
            throw new GdxRuntimeException("#### DEBE HABER 3 SKULLS ####");
    }

    private void createEnemies(Map map) {
        MapLayer layer = map.getLayers().get("malos");

        if (layer == null) {
            logger.error("layer " + "malos" + " no existe");
            return;
        }

        MapObjects objects = layer.getObjects();

        for (MapObject object : objects) {
            if (object instanceof TextureMapObject) {
                continue;
            }

            MapProperties properties = object.getProperties();
            String type = (String) properties.get("type");
            if (type.equals("zombieCuasy") || type.equals("zombieFrank") || type.equals("zombieMummy") || type.equals("zombieKid")
                    || type.equals("zombiePan")) {
                if (object instanceof RectangleMapObject) {
                    createZombie(object, type);
                    worldGame.TOTAL_ZOMBIES_LEVEL++;
                }
            } else {
                throw new GdxRuntimeException("Error en layer " + "malos" + ", objeto:" + type);
            }
        }
    }

    private void createPlatform(MapObject object) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units);
        polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f);
        defaultFixtureDefinition.shape = polygon;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        body.createFixture(defaultFixtureDefinition);

        float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
        float y = (rectangle.y + rectangle.height * 0.5f) * m_units;
        float height = (rectangle.height * m_units * 0.5f);
        float width = (rectangle.width * m_units * 0.5f);
        body.setUserData(new Platform(x, y, width, height));
    }

    private void createLadder(MapObject object, String tipo) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * m_units, (rectangle.y + rectangle.height * 0.5f) * m_units);
        polygon.setAsBox(rectangle.getWidth() * 0.5f * m_units, rectangle.height * 0.5f * m_units, size, 0.0f);
        defaultFixtureDefinition.shape = polygon;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        defaultFixtureDefinition.isSensor = true;
        body.createFixture(defaultFixtureDefinition);
        body.setUserData(tipo);

        defaultFixtureDefinition.isSensor = false;
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

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = obj.position.x;
        bodyDefinition.position.y = obj.position.y;
        bodyDefinition.type = BodyType.DynamicBody;
        Body body = world.createBody(bodyDefinition);

        body.createFixture(fixDef);

        worldGame.crates.add(obj);
        body.setUserData(obj);
    }

    private void createSaw(MapObject object) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

        float width = (rectangle.width * m_units);
        float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
        float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2f);

        defaultFixtureDefinition.shape = shape;

        Saw obj = new Saw(x, y, width);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.x = obj.position.x;
        bodyDef.position.y = obj.position.y;
        bodyDef.type = BodyType.KinematicBody;
        Body body = world.createBody(bodyDef);

        body.createFixture(defaultFixtureDefinition);

        worldGame.saws.add(obj);
        body.setUserData(obj);
        body.setAngularVelocity((float) Math.toRadians(360));

        shape.dispose();
    }

    private void createItem(MapObject object, String type) {
        Items obj = null;

        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
        float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

        switch (type) {
            case "gem":
                obj = new ItemGem(x, y);
                break;
            case "heart":
                obj = new ItemHeart(x, y);
                Gdx.app.log("Heart", "");
                break;
            case "skull":
                obj = new ItemSkull(x, y);
                break;
            case "meat":
                obj = new ItemMeat(x, y);
                break;
            case "shield":
                obj = new ItemShield(x, y);
                break;
            case "star":
                obj = new ItemStar(x, y);
                break;
        }

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.y = obj.position.y;
        bodyDefinition.position.x = obj.position.x;
        bodyDefinition.type = BodyType.StaticBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(.15f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        Body body = world.createBody(bodyDefinition);
        body.createFixture(fixtureDefinition);

        body.setUserData(obj);
        worldGame.items.add(obj);
        shape.dispose();
    }

    private void createZombie(MapObject object, String type) {
        Zombie obj = null;

        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
        float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

        switch (type) {
            case "zombieCuasy":
                obj = new Zombie(x, y, Zombie.TIPO_CUASY);
                break;
            case "zombieFrank":
                obj = new Zombie(x, y, Zombie.TIPO_FRANK);
                break;
            case "zombieKid":
                obj = new Zombie(x, y, Zombie.TIPO_KID);
                break;
            case "zombieMummy":
                obj = new Zombie(x, y, Zombie.TIPO_MUMMY);
                break;
            case "zombiePan":
                obj = new Zombie(x, y, Zombie.TIPO_PAN);
                break;
        }

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = obj.position.x;
        bodyDefinition.position.y = obj.position.y;
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = world.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.17f, .32f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 8;
        fixtureDefinition.friction = 0;
        fixtureDefinition.filter.groupIndex = -1;
        body.createFixture(fixtureDefinition);

        body.setFixedRotation(true);
        body.setUserData(obj);
        worldGame.zombies.add(obj);

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
