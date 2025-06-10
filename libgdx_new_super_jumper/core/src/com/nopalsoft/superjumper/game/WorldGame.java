package com.nopalsoft.superjumper.game;

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
import com.nopalsoft.superjumper.objects.Bullet;
import com.nopalsoft.superjumper.objects.Cloud;
import com.nopalsoft.superjumper.objects.Coin;
import com.nopalsoft.superjumper.objects.Enemy;
import com.nopalsoft.superjumper.objects.Item;
import com.nopalsoft.superjumper.objects.LightningBolt;
import com.nopalsoft.superjumper.objects.Platform;
import com.nopalsoft.superjumper.objects.PlatformPiece;
import com.nopalsoft.superjumper.objects.Player;
import com.nopalsoft.superjumper.screens.Screens;

public class WorldGame {

    final public static int STATE_RUNNING = 0;
    final public static int STATE_GAMEOVER = 1;
    int state;

    float TIME_TO_CREATE_NUBE = 15;
    float timeToCreateNube;

    public World oWorldBox;

    Player oPer;
    private final Array<Body> arrBodies;
    Array<Platform> arrPlataformas;
    Array<PlatformPiece> arrPiezasPlataformas;
    Array<Coin> arrMonedas;
    Array<Enemy> arrEnemigo;
    Array<Item> arrItem;
    Array<Cloud> arrNubes;
    Array<LightningBolt> arrRayos;
    Array<Bullet> arrBullets;

    public int coins;
    public int distanciaMax;
    float mundoCreadoHastaY;

    public WorldGame() {
        oWorldBox = new World(new Vector2(0, -9.8f), true);
        oWorldBox.setContactListener(new Colisiones());

        arrBodies = new Array<>();
        arrPlataformas = new Array<>();
        arrPiezasPlataformas = new Array<>();
        arrMonedas = new Array<>();
        arrEnemigo = new Array<>();
        arrItem = new Array<>();
        arrNubes = new Array<>();
        arrRayos = new Array<>();
        arrBullets = new Array<>();

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
                Coin.createCoin(oWorldBox, arrMonedas, mundoCreadoHastaY);

            if (MathUtils.random(20) < 5)
                Coin.createUnaMoneda(oWorldBox, arrMonedas, mundoCreadoHastaY + .5f);

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
        oPer = new Player(2.4f, .5f);

        BodyDef bd = new BodyDef();
        bd.position.set(oPer.position.x, oPer.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT / 2f);

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

        Platform oPlat = Pools.obtain(Platform.class);
        oPlat.initialize(MathUtils.random(Screens.WORLD_WIDTH), y, MathUtils.random(1));

        BodyDef bd = new BodyDef();
        bd.position.set(oPlat.position.x, oPlat.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Platform.WIDTH_NORMAL / 2f, Platform.HEIGHT_NORMAL / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;

        body.createFixture(fixutre);
        body.setUserData(oPlat);
        arrPlataformas.add(oPlat);

        shape.dispose();
    }

    /**
     * La plataforma rompible son 2 cuadros
     */
    private void crearPiezasPlataforma(Platform oPlat) {
        crearPiezasPlataforma(oPlat, PlatformPiece.TYPE_LEFT);
        crearPiezasPlataforma(oPlat, PlatformPiece.TYPE_RIGHT);
    }

    private void crearPiezasPlataforma(Platform oPla, int tipo) {
        PlatformPiece oPieza;
        float x;
        float angularVelocity = 100;

        if (tipo == PlatformPiece.TYPE_LEFT) {
            x = oPla.position.x - PlatformPiece.WIDTH_NORMAL / 2f;
            angularVelocity *= -1;
        } else {
            x = oPla.position.x + PlatformPiece.WIDTH_NORMAL / 2f;
        }

        oPieza = Pools.obtain(PlatformPiece.class);
        oPieza.initialize(x, oPla.position.y, tipo, oPla.color);

        BodyDef bd = new BodyDef();
        bd.position.set(oPieza.position.x, oPieza.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PlatformPiece.WIDTH_NORMAL / 2f, PlatformPiece.HEIGHT_NORMAL / 2f);

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
        Enemy oEn = Pools.obtain(Enemy.class);
        oEn.initialize(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef bd = new BodyDef();
        bd.position.set(oEn.position.x, oEn.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(oEn);
        body.setGravityScale(0);

        float velocidad = MathUtils.random(1f, Enemy.SPEED_X);

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
        Cloud oCloud = Pools.obtain(Cloud.class);
        oCloud.init(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef bd = new BodyDef();
        bd.position.set(oCloud.position.x, oCloud.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Cloud.WIDTH / 2f, Cloud.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(oCloud);
        body.setGravityScale(0);

        float velocidad = MathUtils.random(1f, Cloud.SPEED_X);

        if (MathUtils.randomBoolean())
            body.setLinearVelocity(velocidad, 0);
        else
            body.setLinearVelocity(-velocidad, 0);
        arrNubes.add(oCloud);

        shape.dispose();
    }

    private void crearRayo(float x, float y) {
        LightningBolt oLightningBolt = Pools.obtain(LightningBolt.class);
        oLightningBolt.init(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(oLightningBolt.position.x, oLightningBolt.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(LightningBolt.WIDTH / 2f, LightningBolt.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(oLightningBolt);

        body.setLinearVelocity(0, LightningBolt.SPEED_Y);
        arrRayos.add(oLightningBolt);

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

        /*
         * REviso si es necesario generar la siquiete parte del mundo
         */
        if (oPer.position.y + 10 > mundoCreadoHastaY) {
            crearSiguienteParte();
        }

        timeToCreateNube += delta;// Actualizo el tiempo para crear una nube

        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (body.getUserData() instanceof Player) {
                updatePersonaje(body, delta, acelX, fire, touchPositionWorldCoords);
            } else if (body.getUserData() instanceof Platform) {
                updatePlataforma(body, delta);
            } else if (body.getUserData() instanceof PlatformPiece) {
                updatePiezaPlataforma(body, delta);
            } else if (body.getUserData() instanceof Coin) {
                updateMoneda(body, delta);
            } else if (body.getUserData() instanceof Enemy) {
                updateEnemigo(body, delta);
            } else if (body.getUserData() instanceof Item) {
                updateItem(body, delta);
            } else if (body.getUserData() instanceof Cloud) {
                updateNube(body, delta);
            } else if (body.getUserData() instanceof LightningBolt) {
                updateRayo(body, delta);
            } else if (body.getUserData() instanceof Bullet) {
                updateBullet(body, delta);
            }
        }

        if (distanciaMax < (oPer.position.y * 10)) {
            distanciaMax = (int) (oPer.position.y * 10);
        }

        // Si el personaje esta 5.5f mas abajo de la altura maxima se muere (Se multiplica por 10 porque la distancia se multiplica por 10 )
        if (oPer.state == Player.STATE_NORMAL && distanciaMax - (5.5f * 10) > (oPer.position.y * 10)) {
            oPer.die();
        }
        if (oPer.state == Player.STATE_DEAD && distanciaMax - (25 * 10) > (oPer.position.y * 10)) {
            state = STATE_GAMEOVER;
        }
    }

    private void eliminarObjetos() {
        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (!oWorldBox.isLocked()) {

                if (body.getUserData() instanceof Platform) {
                    Platform oPlat = (Platform) body.getUserData();
                    if (oPlat.state == Platform.STATE_DESTROY) {
                        arrPlataformas.removeValue(oPlat, true);
                        oWorldBox.destroyBody(body);
                        if (oPlat.type == Platform.TYPE_BREAKABLE)
                            crearPiezasPlataforma(oPlat);
                        Pools.free(oPlat);
                    }
                } else if (body.getUserData() instanceof Coin) {
                    Coin oMon = (Coin) body.getUserData();
                    if (oMon.state == Coin.STATE_TAKEN) {
                        arrMonedas.removeValue(oMon, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(oMon);
                    }
                } else if (body.getUserData() instanceof PlatformPiece) {
                    PlatformPiece oPiez = (PlatformPiece) body.getUserData();
                    if (oPiez.state == PlatformPiece.STATE_DESTROY) {
                        arrPiezasPlataformas.removeValue(oPiez, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(oPiez);
                    }
                } else if (body.getUserData() instanceof Enemy) {
                    Enemy oEnemy = (Enemy) body.getUserData();
                    if (oEnemy.state == Enemy.STATE_DEAD) {
                        arrEnemigo.removeValue(oEnemy, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(oEnemy);
                    }
                } else if (body.getUserData() instanceof Item) {
                    Item oItem = (Item) body.getUserData();
                    if (oItem.state == Item.STATE_TAKEN) {
                        arrItem.removeValue(oItem, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(oItem);
                    }
                } else if (body.getUserData() instanceof Cloud) {
                    Cloud oCloud = (Cloud) body.getUserData();
                    if (oCloud.state == Cloud.STATE_DEAD) {
                        arrNubes.removeValue(oCloud, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(oCloud);
                    }
                } else if (body.getUserData() instanceof LightningBolt) {
                    LightningBolt oLightningBolt = (LightningBolt) body.getUserData();
                    if (oLightningBolt.state == LightningBolt.STATE_DESTROY) {
                        arrRayos.removeValue(oLightningBolt, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(oLightningBolt);
                    }
                } else if (body.getUserData() instanceof Bullet) {
                    Bullet oBullet = (Bullet) body.getUserData();
                    if (oBullet.state == Bullet.STATE_DESTROY) {
                        arrBullets.removeValue(oBullet, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(oBullet);
                    }
                } else if (body.getUserData().equals("piso")) {
                    if (oPer.position.y - 5.5f > body.getPosition().y || oPer.state == Player.STATE_DEAD) {
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
        Platform obj = (Platform) body.getUserData();
        obj.update(delta);
        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.setDestroy();
        }
    }

    private void updatePiezaPlataforma(Body body, float delta) {
        PlatformPiece obj = (PlatformPiece) body.getUserData();
        obj.update(delta, body);
        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.setDestroy();
        }
    }

    private void updateMoneda(Body body, float delta) {
        Coin obj = (Coin) body.getUserData();
        obj.update(delta);
        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.take();
        }
    }

    private void updateEnemigo(Body body, float delta) {
        Enemy obj = (Enemy) body.getUserData();
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
        Cloud obj = (Cloud) body.getUserData();
        obj.update(body, delta);

        if (obj.isLightning) {
            crearRayo(obj.position.x, obj.position.y - .65f);
            obj.fireLighting();
        }

        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    private void updateRayo(Body body, float delta) {
        LightningBolt obj = (LightningBolt) body.getUserData();
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

            if (a.getBody().getUserData() instanceof Player)
                beginContactPersonaje(b);
            else if (b.getBody().getUserData() instanceof Player)
                beginContactPersonaje(a);

            if (a.getBody().getUserData() instanceof Bullet)
                beginContactBullet(a, b);
            else if (b.getBody().getUserData() instanceof Bullet)
                beginContactBullet(b, a);
        }

        private void beginContactPersonaje(Fixture fixOtraCosa) {
            Object otraCosa = fixOtraCosa.getBody().getUserData();

            if (otraCosa.equals("piso")) {
                oPer.jump();

                if (oPer.state == Player.STATE_DEAD) {
                    state = STATE_GAMEOVER;
                }
            } else if (otraCosa instanceof Platform) {
                Platform obj = (Platform) otraCosa;

                if (oPer.speed.y <= 0) {
                    oPer.jump();
                    if (obj.type == Platform.TYPE_BREAKABLE) {
                        obj.setDestroy();
                    }
                }
            } else if (otraCosa instanceof Coin) {
                Coin obj = (Coin) otraCosa;
                obj.take();
                coins++;
                oPer.jump();
            } else if (otraCosa instanceof Enemy) {
                oPer.hit();
            } else if (otraCosa instanceof LightningBolt) {
                oPer.hit();
            } else if (otraCosa instanceof Item) {
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

            if (otraCosa instanceof Enemy) {
                Enemy obj = (Enemy) otraCosa;
                obj.hit();
                oBullet.destroy();
            } else if (otraCosa instanceof Cloud) {
                Cloud obj = (Cloud) otraCosa;
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

            if (a.getBody().getUserData() instanceof Player)
                preSolveHero(a, b, contact);
            else if (b.getBody().getUserData() instanceof Player)
                preSolveHero(b, a, contact);
        }

        private void preSolveHero(Fixture fixPersonaje, Fixture otraCosa, Contact contact) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Platform) {
                // Si va para arriba atraviesa la plataforma

                Platform obj = (Platform) oOtraCosa;

                float ponyY = fixPersonaje.getBody().getPosition().y - .30f;
                float pisY = obj.position.y + Platform.HEIGHT_NORMAL / 2f;

                if (ponyY < pisY)
                    contact.setEnabled(false);

                if (obj.type == Platform.TYPE_NORMAL && oPer.state == Player.STATE_DEAD) {
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
