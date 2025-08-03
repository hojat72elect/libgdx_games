package com.nopalsoft.ponyrace.game_objects;

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
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game.TileMapHandler;

import java.util.LinkedHashMap;

public class TiledMapManagerBox2d {

    public final static short CONTACT_CORREDORES = -1;

    private final TileMapHandler oWorld;
    private final World oWorldBox;
    private final float m_units;
    private final Logger logger;
    private final FixtureDef defaultFixture;
    private final LinkedHashMap<Integer, String> nombrePonys;
    int contadorPonisCreados = 0;

    public TiledMapManagerBox2d(TileMapHandler oWorld, float unitsPerPixel) {
        this.oWorld = oWorld;
        oWorldBox = oWorld.oWorldBox;
        m_units = unitsPerPixel;
        logger = new Logger("MapBodyManager", 1);
        nombrePonys = oWorld.game.assetsHandler.nombrePonys;

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
        for (MapObject object : objects) {
            if (object instanceof TextureMapObject) {
                continue;
            }

            MapProperties properties = object.getProperties();
            String tipo = (String) properties.get("type");

            if (tipo != null) {
                switch (tipo) {
                    case "moneda":
                        crearMoneda(object);
                        break;
                    case "dulce":
                        crearDulce(object);
                        break;
                    case "chile":
                        crearChile(object);
                        break;
                    case "globo":
                        crearGlobo(object);
                        break;
                }
            }
        }
    }

    public void crearFisicos(Map map, String layerName) {
        MapLayer layer = map.getLayers().get(layerName);

        if (layer == null) {
            logger.error("layer " + layerName + " no existe");
            return;
        }

        MapObjects objects = layer.getObjects();

        for (MapObject object : objects) {
            if (object instanceof TextureMapObject) {
                continue;
            }

            MapProperties properties = object.getProperties();
            String tipo = (String) properties.get("type");
            if (tipo == null)
                continue;
            else if (tipo.equals("pony")) {
                crearPony(object, tipo);
                continue;
            } else if (tipo.equals("malos")) {
                crearPony(object, tipo);
                continue;
            } else if (tipo.equals("plumas")) {
                crearPlumas(object);
                continue;
            } else if (tipo.equals("fogata")) {
                crearFogata(object);
                continue;
            } else if (tipo.equals("pisable")) {
                if (object instanceof RectangleMapObject) {

                    crearPisable(object);
                    continue;
                }
            } else if (tipo.equals("fin")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
                float y = (rectangle.y + rectangle.height * 0.5f) * m_units;
                oWorld.finJuego = new Vector2(x, y);
                continue;
            } else if (tipo.equals("gemaChica")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
                float y = (rectangle.y + rectangle.height * 0.5f) * m_units;
                oWorld.arrBloodStone.add(new BloodStone(x, y, BloodStone.Type.SMALL, oWorld.random));
                continue;
            } else if (tipo.equals("gemaMediana")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
                float y = (rectangle.y + rectangle.height * 0.5f) * m_units;
                oWorld.arrBloodStone.add(new BloodStone(x, y, BloodStone.Type.MEDIUM, oWorld.random));
                continue;
            } else if (tipo.equals("gemaGrande")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
                float y = (rectangle.y + rectangle.height * 0.5f) * m_units;
                oWorld.arrBloodStone.add(new BloodStone(x, y, BloodStone.Type.LARGE, oWorld.random));
                continue;
            }

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
            bodyDef.type = BodyType.StaticBody;

            if (tipo.equals("bandera1") || tipo.equals("caer") || tipo.equals("saltoDerecha") || tipo.equals("bandera") || tipo.equals("regresoHoyo")
                    || tipo.equals("caminarIzquierda") || tipo.equals("caminarDerecha") || tipo.equals("saltoIzquierda") || tipo.equals("salto")) {
                defaultFixture.isSensor = true;
            }

            Body body = oWorldBox.createBody(bodyDef);
            body.createFixture(defaultFixture);

            if (tipo.equals("bandera1")) {
                if (properties.get("jump").equals("left"))
                    body.setUserData(new Flag(oWorld,
                            Flag.ActionType.JUMP_LEFT));
                else if (properties.get("jump").equals("right"))
                    body.setUserData(new Flag(oWorld,
                            Flag.ActionType.JUMP_RIGHT));
                else if (properties.get("jump").equals("stand"))
                    body.setUserData(new Flag(oWorld, Flag.ActionType.JUMP));
            } else
                body.setUserData(tipo);

            defaultFixture.shape = null;
            defaultFixture.isSensor = false;
            defaultFixture.filter.groupIndex = 0;
            shape.dispose();
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
        body.setUserData(new Platform(x, y, width, height));
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

    private void crearPony(MapObject object, String tipo) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
        float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

        Pony oPony;

        String nombreSkin;

        if (tipo.equals("pony")) {
            nombreSkin = Settings.selectedSkin;
            oPony = new PonyPlayer(x, y, nombreSkin, oWorld);
        } else {// Ponis malos

            if (Settings.selectedSkin.equals(nombrePonys.get(contadorPonisCreados)))
                contadorPonisCreados++;
            nombreSkin = nombrePonys.get(contadorPonisCreados);

            oPony = new OpponentPony(x, y, nombreSkin, oWorld);
        }
        contadorPonisCreados++; // Se comenta esta linea si se quieren poner muchos ponys. PAra debugear

        BodyDef bd = new BodyDef();
        bd.position.y = oPony.position.y;
        bd.position.x = oPony.position.x;
        bd.type = BodyType.DynamicBody;
        Body oBody = oWorldBox.createBody(bd);

        PolygonShape pies = new PolygonShape();
        // pies.setAsBox(.2f, .23f);//Lo puse mejor con vertices pa que no tuviera esquinas picudas y tratar de minimizar
        // que los ponis se caian al vacio

        float[] vert = {-.2f, -.2f, -.18f, -.23f, .18f, -.23f, .2f, -.2f, .2f, .23f, -.2f, .23f};
        pies.set(vert);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = pies;
        fixture.density = .5f;
        fixture.restitution = 0f;
        fixture.friction = 0f;
        fixture.filter.groupIndex = CONTACT_CORREDORES;
        Fixture cuerpo = oBody.createFixture(fixture);
        cuerpo.setUserData("cuerpo");
        oBody.createFixture(fixture);

        // Sensor pa cuando tenga el chile pueda danar a los enemigos
        // pies.setAsBox(.2f, .23f, new Vector2(.1f, .2f), 0);
        fixture.shape = pies;
        fixture.density = .1f;
        fixture.restitution = 0;
        fixture.friction = 0f;
        fixture.isSensor = true;
        fixture.filter.groupIndex = 0;
        cuerpo = oBody.createFixture(fixture);
        cuerpo.setUserData("cuerpoSensor");
        oBody.createFixture(fixture);

        CircleShape sensorBottomIzq = new CircleShape();
        sensorBottomIzq.setRadius(.03f);
        sensorBottomIzq.setPosition(new Vector2(-.14f, -.23f));
        fixture.shape = sensorBottomIzq;
        fixture.density = 03f;
        fixture.restitution = 0f;
        fixture.friction = 0;
        fixture.isSensor = true;
        fixture.filter.groupIndex = CONTACT_CORREDORES;
        Fixture sensorBottomIzqFixture = oBody.createFixture(fixture);
        sensorBottomIzqFixture.setUserData("sensorBottomIzq");
        oBody.createFixture(fixture);

        sensorBottomIzq = new CircleShape();
        sensorBottomIzq.setRadius(.03f);
        sensorBottomIzq.setPosition(new Vector2(.14f, -.23f));
        fixture.shape = sensorBottomIzq;
        fixture.density = 03f;
        fixture.restitution = 0f;
        fixture.friction = 0;
        fixture.isSensor = true;
        fixture.filter.groupIndex = CONTACT_CORREDORES;
        sensorBottomIzqFixture = oBody.createFixture(fixture);
        sensorBottomIzqFixture.setUserData("sensorBottomDer");
        oBody.createFixture(fixture);

        // oBody.setFixedRotation(true);
        oBody.setUserData(oPony);
        oBody.setBullet(true);

        if (tipo.equals("pony")) {
            oWorld.oPony = (PonyPlayer) oPony;
        } else {// ponis malos
            oWorld.arrPonysMalos.add((OpponentPony) oPony);
        }

        pies.dispose();
        sensorBottomIzq.dispose();
    }

    private void crearFogata(MapObject object) {
        PolygonShape polygon = new PolygonShape();
        PolygonMapObject polygonObject = (PolygonMapObject) object;
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

        Polygon as = new Polygon(vertices);
        Bonfire oBonfire;
        oBonfire = new Bonfire(as.getBoundingRectangle().width / 2f * m_units + as.getBoundingRectangle().x * m_units + xLost,
                as.getBoundingRectangle().height / 2f * m_units + as.getBoundingRectangle().y * m_units + yLost - .15f, oWorld.random);

        polygon.set(worldVertices);

        FixtureDef fixture = new FixtureDef();
        fixture.density = 1.0f;
        fixture.friction = 1f;
        fixture.restitution = 0.0f;
        fixture.shape = polygon;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;

        Body body = oWorldBox.createBody(bodyDef);
        body.createFixture(fixture);
        body.setUserData(oBonfire);

        oWorld.arrFogatas.add(oBonfire);
        polygon.dispose();
    }

    private void crearPlumas(MapObject object) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
        float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

        Wing obj = new Wing(x, y, oWorld.random);
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
        fixture.filter.groupIndex = CONTACT_CORREDORES;

        Body oBody = oWorldBox.createBody(bd);
        oBody.createFixture(fixture);

        oBody.setUserData(obj);
        oWorld.arrPlumas.add(obj);
        pies.dispose();
    }

    private void crearMoneda(MapObject object) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
        float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

        Coin obj = new Coin(x, y, oWorld);
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
        oWorld.arrMonedas.add(obj);
        pies.dispose();
    }

    private void crearChile(MapObject object) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
        float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

        Chili obj = new Chili(x, y, oWorld);
        BodyDef bd = new BodyDef();
        bd.position.y = obj.position.y;
        bd.position.x = obj.position.x;
        bd.type = BodyType.StaticBody;

        PolygonShape pies = new PolygonShape();
        pies.setAsBox(.25f, .15f);

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
        oWorld.arrChiles.add(obj);
        pies.dispose();
    }

    private void crearGlobo(MapObject object) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
        float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

        Balloons obj = new Balloons(x, y, oWorld);
        BodyDef bd = new BodyDef();
        bd.position.y = obj.position.y;
        bd.position.x = obj.position.x;
        bd.type = BodyType.StaticBody;

        PolygonShape pies = new PolygonShape();
        pies.setAsBox(.15f, .4f);

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
        oWorld.arrGlobos.add(obj);
        pies.dispose();
    }

    private void crearDulce(MapObject object) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        float x = (rectangle.x + rectangle.width * 0.5f) * m_units;
        float y = (rectangle.y + rectangle.height * 0.5f) * m_units;

        Candy obj = new Candy(x, y, oWorld);
        BodyDef bd = new BodyDef();
        bd.position.y = obj.position.y;
        bd.position.x = obj.position.x;
        bd.type = BodyType.StaticBody;

        PolygonShape pies = new PolygonShape();
        pies.setAsBox(.15f, .25f);

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
        oWorld.arrDulces.add(obj);
        pies.dispose();
    }
}
