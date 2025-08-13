package com.nopalsoft.zombiewars.game;

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
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;

import java.util.Iterator;

public class TiledMapManagerBox2d {

    private final World oWorldBox;
    private final float unitScale;
    private final Logger logger;
    private final FixtureDef defaultFixture;

    public TiledMapManagerBox2d(WorldGame oWorld, float unitScale) {
        oWorldBox = oWorld.oWorldBox;
        this.unitScale = unitScale;
        logger = new Logger("MapBodyManager", 1);

        defaultFixture = new FixtureDef();
        defaultFixture.density = 1.0f;
        defaultFixture.friction = .5f;
        defaultFixture.restitution = 0.0f;
    }

    public void createObjetosDesdeTiled(Map map) {
        crearFisicos(map, "fisicos");
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

            /**
             * Normalmente si no ninguno es el piso
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

    private Shape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * unitScale, (rectangle.y + rectangle.height * 0.5f) * unitScale);
        polygon.setAsBox(rectangle.getWidth() * 0.5f * unitScale, rectangle.height * 0.5f * unitScale, size, 0.0f);
        return polygon;
    }

    private Shape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius * unitScale);
        circleShape.setPosition(new Vector2(circle.x * unitScale, circle.y * unitScale));
        return circleShape;
    }

    private Shape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getVertices();
        float[] worldVertices = new float[vertices.length];
        float yLost = polygonObject.getPolygon().getY() * unitScale;
        float xLost = polygonObject.getPolygon().getX() * unitScale;

        for (int i = 0; i < vertices.length; ++i) {
            if (i % 2 == 0)
                worldVertices[i] = vertices[i] * unitScale + xLost;
            else
                worldVertices[i] = vertices[i] * unitScale + yLost;
        }
        polygon.set(worldVertices);

        return polygon;
    }

    private Shape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getVertices();

        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        float yLost = polylineObject.getPolyline().getY() * unitScale;
        float xLost = polylineObject.getPolyline().getX() * unitScale;
        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] * unitScale + xLost;
            worldVertices[i].y = vertices[i * 2 + 1] * unitScale + yLost;
        }
        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }
}
