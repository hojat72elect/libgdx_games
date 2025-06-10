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
import com.nopalsoft.superjumper.objects.LightningBolt;
import com.nopalsoft.superjumper.objects.Platform;
import com.nopalsoft.superjumper.objects.PlatformPiece;
import com.nopalsoft.superjumper.objects.Player;
import com.nopalsoft.superjumper.objects.PowerUpItem;
import com.nopalsoft.superjumper.screens.Screens;

public class WorldGame {

    final public static int STATE_RUNNING = 0;
    final public static int STATE_GAMEOVER = 1;
    int state;

    private final Array<Body> arrayBodies;
    public World worldBox;
    public int maxDistance;
    float TIME_UNTIL_NEXT_CLOUD = 15;
    float timeUntilNextCloud;
    Player player;
    Array<Platform> arrayPlatforms;
    Array<PlatformPiece> arrayPlatformPieces;
    Array<Coin> arrayCoins;
    Array<Enemy> arrayEnemies;
    Array<PowerUpItem> arrayItems;
    Array<Cloud> arrayClouds;
    Array<LightningBolt> arrayLightningBolts;

    public int coins;
    Array<Bullet> arrayBullets;
    float gameWorldCreatedUntilY;

    public WorldGame() {
        worldBox = new World(new Vector2(0, -9.8f), true);
        worldBox.setContactListener(new CollisionHandler());

        arrayBodies = new Array<>();
        arrayPlatforms = new Array<>();
        arrayPlatformPieces = new Array<>();
        arrayCoins = new Array<>();
        arrayEnemies = new Array<>();
        arrayItems = new Array<>();
        arrayClouds = new Array<>();
        arrayLightningBolts = new Array<>();
        arrayBullets = new Array<>();

        timeUntilNextCloud = 0;

        state = STATE_RUNNING;

        createFloor();
        createPlayer();

        gameWorldCreatedUntilY = player.position.y;
        createNextSection();
    }

    private void createNextSection() {
        float y = gameWorldCreatedUntilY + 2;

        for (int i = 0; gameWorldCreatedUntilY < (y + 10); i++) {
            gameWorldCreatedUntilY = y + (i * 2);

            createPlatform(gameWorldCreatedUntilY);
            createPlatform(gameWorldCreatedUntilY);

            if (MathUtils.random(100) < 5)
                Coin.createCoin(worldBox, arrayCoins, gameWorldCreatedUntilY);

            if (MathUtils.random(20) < 5)
                Coin.createOneCoin(worldBox, arrayCoins, gameWorldCreatedUntilY + .5f);

            if (MathUtils.random(20) < 5)
                createEnemy(gameWorldCreatedUntilY + .5f);

            if (timeUntilNextCloud >= TIME_UNTIL_NEXT_CLOUD) {
                createCloud(gameWorldCreatedUntilY + .7f);
                timeUntilNextCloud = 0;
            }

            if (MathUtils.random(50) < 5)
                createItem(gameWorldCreatedUntilY + .5f);
        }
    }

    /**
     * The floor only appears once, at the beginning of the game.
     */
    private void createFloor() {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.StaticBody;

        Body body = worldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, Screens.WORLD_WIDTH, 0);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;

        body.createFixture(fixtureDefinition);
        body.setUserData("piso");

        shape.dispose();
    }

    private void createPlayer() {
        player = new Player(2.4f, .5f);

        BodyDef bd = new BodyDef();
        bd.position.set(player.position.x, player.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = worldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 10;
        fixtureDefinition.friction = 0;
        fixtureDefinition.restitution = 0;

        body.createFixture(fixtureDefinition);
        body.setUserData(player);
        body.setFixedRotation(true);

        shape.dispose();
    }

    private void createPlatform(float y) {

        Platform platform = Pools.obtain(Platform.class);
        platform.initialize(MathUtils.random(Screens.WORLD_WIDTH), y, MathUtils.random(1));

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(platform.position.x, platform.position.y);
        bodyDefinition.type = BodyType.KinematicBody;

        Body body = worldBox.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Platform.WIDTH_NORMAL / 2f, Platform.HEIGHT_NORMAL / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;

        body.createFixture(fixtureDefinition);
        body.setUserData(platform);
        arrayPlatforms.add(platform);

        shape.dispose();
    }

    /**
     * The breakable platform is 2 frames.
     */
    private void createPlatformPieces(Platform oPlat) {
        createPlatformPieces(oPlat, PlatformPiece.TYPE_LEFT);
        createPlatformPieces(oPlat, PlatformPiece.TYPE_RIGHT);
    }

    private void createPlatformPieces(Platform platform, int type) {
        PlatformPiece platformPiece;
        float x;
        float angularVelocity = 100;

        if (type == PlatformPiece.TYPE_LEFT) {
            x = platform.position.x - PlatformPiece.WIDTH_NORMAL / 2f;
            angularVelocity *= -1;
        } else {
            x = platform.position.x + PlatformPiece.WIDTH_NORMAL / 2f;
        }

        platformPiece = Pools.obtain(PlatformPiece.class);
        platformPiece.initialize(x, platform.position.y, type, platform.color);

        BodyDef bd = new BodyDef();
        bd.position.set(platformPiece.position.x, platformPiece.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = worldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PlatformPiece.WIDTH_NORMAL / 2f, PlatformPiece.HEIGHT_NORMAL / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);
        body.setUserData(platformPiece);
        body.setAngularVelocity(MathUtils.degRad * angularVelocity);
        arrayPlatformPieces.add(platformPiece);

        shape.dispose();
    }

    private void createEnemy(float y) {
        Enemy oEn = Pools.obtain(Enemy.class);
        oEn.initialize(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef bd = new BodyDef();
        bd.position.set(oEn.position.x, oEn.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = worldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f);

        FixtureDef enemyFixtureDefinition = new FixtureDef();
        enemyFixtureDefinition.shape = shape;
        enemyFixtureDefinition.isSensor = true;

        body.createFixture(enemyFixtureDefinition);
        body.setUserData(oEn);
        body.setGravityScale(0);

        float speed = MathUtils.random(1f, Enemy.SPEED_X);

        if (MathUtils.randomBoolean())
            body.setLinearVelocity(speed, 0);
        else
            body.setLinearVelocity(-speed, 0);
        arrayEnemies.add(oEn);

        shape.dispose();
    }

    private void createItem(float y) {
        PowerUpItem oPowerUpItem = Pools.obtain(PowerUpItem.class);
        oPowerUpItem.init(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef bd = new BodyDef();
        bd.position.set(oPowerUpItem.position.x, oPowerUpItem.position.y);
        bd.type = BodyType.StaticBody;
        Body oBody = worldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PowerUpItem.WIDTH / 2f, PowerUpItem.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;
        oBody.createFixture(fixture);
        oBody.setUserData(oPowerUpItem);
        shape.dispose();
        arrayItems.add(oPowerUpItem);
    }

    private void createCloud(float y) {
        Cloud cloud = Pools.obtain(Cloud.class);
        cloud.init(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef cloudBodyDefinition = new BodyDef();
        cloudBodyDefinition.position.set(cloud.position.x, cloud.position.y);
        cloudBodyDefinition.type = BodyType.DynamicBody;

        Body cloudBody = worldBox.createBody(cloudBodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Cloud.WIDTH / 2f, Cloud.HEIGHT / 2f);

        FixtureDef cloudFixtureDefinition = new FixtureDef();
        cloudFixtureDefinition.shape = shape;
        cloudFixtureDefinition.isSensor = true;

        cloudBody.createFixture(cloudFixtureDefinition);
        cloudBody.setUserData(cloud);
        cloudBody.setGravityScale(0);

        float speed = MathUtils.random(1f, Cloud.SPEED_X);

        if (MathUtils.randomBoolean())
            cloudBody.setLinearVelocity(speed, 0);
        else
            cloudBody.setLinearVelocity(-speed, 0);
        arrayClouds.add(cloud);

        shape.dispose();
    }

    private void createLightningBolt(float x, float y) {
        LightningBolt oLightningBolt = Pools.obtain(LightningBolt.class);
        oLightningBolt.init(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(oLightningBolt.position.x, oLightningBolt.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = worldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(LightningBolt.WIDTH / 2f, LightningBolt.HEIGHT / 2f);

        FixtureDef lightningBoltFixtureDefinition = new FixtureDef();
        lightningBoltFixtureDefinition.shape = shape;
        lightningBoltFixtureDefinition.isSensor = true;

        body.createFixture(lightningBoltFixtureDefinition);
        body.setUserData(oLightningBolt);

        body.setLinearVelocity(0, LightningBolt.SPEED_Y);
        arrayLightningBolts.add(oLightningBolt);

        shape.dispose();
    }

    private void createBullet(float origenX, float origenY, float destinationX, float destinationY) {
        Bullet bullet = Pools.obtain(Bullet.class);
        bullet.init(origenX, origenY);

        BodyDef bulletBodyDefinition = new BodyDef();
        bulletBodyDefinition.position.set(bullet.position.x, bullet.position.y);
        bulletBodyDefinition.type = BodyType.KinematicBody;

        Body bulletBody = worldBox.createBody(bulletBodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Bullet.SIZE / 2f, Bullet.SIZE / 2f);

        FixtureDef bulletFixtureDefinition = new FixtureDef();
        bulletFixtureDefinition.shape = shape;
        bulletFixtureDefinition.isSensor = true;

        bulletBody.createFixture(bulletFixtureDefinition);
        bulletBody.setUserData(bullet);
        bulletBody.setBullet(true);

        Vector2 destination = new Vector2(destinationX, destinationY);
        destination.sub(bullet.position).nor().scl(Bullet.SPEED);

        bulletBody.setLinearVelocity(destination.x, destination.y);

        arrayBullets.add(bullet);

        shape.dispose();
    }

    public void update(float delta, float accelerationX, boolean fire, Vector3 touchPositionWorldCoordinates) {
        worldBox.step(delta, 8, 4);

        removeObjects();

        /*
         * I check if it is necessary to generate the next part of the world
         */
        if (player.position.y + 10 > gameWorldCreatedUntilY) {
            createNextSection();
        }

        timeUntilNextCloud += delta;// I update the time to create a cloud.

        worldBox.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (body.getUserData() instanceof Player) {
                updatePlayer(body, delta, accelerationX, fire, touchPositionWorldCoordinates);
            } else if (body.getUserData() instanceof Platform) {
                updatePlatform(body, delta);
            } else if (body.getUserData() instanceof PlatformPiece) {
                updatePlatformPiece(body, delta);
            } else if (body.getUserData() instanceof Coin) {
                updateCoin(body, delta);
            } else if (body.getUserData() instanceof Enemy) {
                updateEnemy(body, delta);
            } else if (body.getUserData() instanceof PowerUpItem) {
                updateItem(body, delta);
            } else if (body.getUserData() instanceof Cloud) {
                updateCloud(body, delta);
            } else if (body.getUserData() instanceof LightningBolt) {
                updateLightningBolt(body, delta);
            } else if (body.getUserData() instanceof Bullet) {
                updateBullet(body, delta);
            }
        }

        if (maxDistance < (player.position.y * 10)) {
            maxDistance = (int) (player.position.y * 10);
        }

        // If the character is 5.5f below the maximum height, he dies (It is multiplied by 10 because the distance is multiplied by 10).
        if (player.state == Player.STATE_NORMAL && maxDistance - (5.5f * 10) > (player.position.y * 10)) {
            player.die();
        }
        if (player.state == Player.STATE_DEAD && maxDistance - (25 * 10) > (player.position.y * 10)) {
            state = STATE_GAMEOVER;
        }
    }

    private void removeObjects() {
        worldBox.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (!worldBox.isLocked()) {

                if (body.getUserData() instanceof Platform) {
                    Platform oPlat = (Platform) body.getUserData();
                    if (oPlat.state == Platform.STATE_DESTROY) {
                        arrayPlatforms.removeValue(oPlat, true);
                        worldBox.destroyBody(body);
                        if (oPlat.type == Platform.TYPE_BREAKABLE)
                            createPlatformPieces(oPlat);
                        Pools.free(oPlat);
                    }
                } else if (body.getUserData() instanceof Coin) {
                    Coin oMon = (Coin) body.getUserData();
                    if (oMon.state == Coin.STATE_TAKEN) {
                        arrayCoins.removeValue(oMon, true);
                        worldBox.destroyBody(body);
                        Pools.free(oMon);
                    }
                } else if (body.getUserData() instanceof PlatformPiece) {
                    PlatformPiece platformPiece = (PlatformPiece) body.getUserData();
                    if (platformPiece.state == PlatformPiece.STATE_DESTROY) {
                        arrayPlatformPieces.removeValue(platformPiece, true);
                        worldBox.destroyBody(body);
                        Pools.free(platformPiece);
                    }
                } else if (body.getUserData() instanceof Enemy) {
                    Enemy oEnemy = (Enemy) body.getUserData();
                    if (oEnemy.state == Enemy.STATE_DEAD) {
                        arrayEnemies.removeValue(oEnemy, true);
                        worldBox.destroyBody(body);
                        Pools.free(oEnemy);
                    }
                } else if (body.getUserData() instanceof PowerUpItem) {
                    PowerUpItem oPowerUpItem = (PowerUpItem) body.getUserData();
                    if (oPowerUpItem.state == PowerUpItem.STATE_TAKEN) {
                        arrayItems.removeValue(oPowerUpItem, true);
                        worldBox.destroyBody(body);
                        Pools.free(oPowerUpItem);
                    }
                } else if (body.getUserData() instanceof Cloud) {
                    Cloud oCloud = (Cloud) body.getUserData();
                    if (oCloud.state == Cloud.STATE_DEAD) {
                        arrayClouds.removeValue(oCloud, true);
                        worldBox.destroyBody(body);
                        Pools.free(oCloud);
                    }
                } else if (body.getUserData() instanceof LightningBolt) {
                    LightningBolt oLightningBolt = (LightningBolt) body.getUserData();
                    if (oLightningBolt.state == LightningBolt.STATE_DESTROY) {
                        arrayLightningBolts.removeValue(oLightningBolt, true);
                        worldBox.destroyBody(body);
                        Pools.free(oLightningBolt);
                    }
                } else if (body.getUserData() instanceof Bullet) {
                    Bullet oBullet = (Bullet) body.getUserData();
                    if (oBullet.state == Bullet.STATE_DESTROY) {
                        arrayBullets.removeValue(oBullet, true);
                        worldBox.destroyBody(body);
                        Pools.free(oBullet);
                    }
                } else if (body.getUserData().equals("piso")) {
                    if (player.position.y - 5.5f > body.getPosition().y || player.state == Player.STATE_DEAD) {
                        worldBox.destroyBody(body);
                    }
                }
            }
        }
    }

    private void updatePlayer(Body body, float delta, float accelerationX, boolean fire, Vector3 touchPositionWorldCoordinates) {
        player.update(body, delta, accelerationX);

        if (Settings.numBullets > 0 && fire) {
            createBullet(player.position.x, player.position.y, touchPositionWorldCoordinates.x, touchPositionWorldCoordinates.y);
            Settings.numBullets--;
        }
    }

    private void updatePlatform(Body body, float delta) {
        Platform obj = (Platform) body.getUserData();
        obj.update(delta);
        if (player.position.y - 5.5f > obj.position.y) {
            obj.setDestroy();
        }
    }

    private void updatePlatformPiece(Body body, float delta) {
        PlatformPiece obj = (PlatformPiece) body.getUserData();
        obj.update(delta, body);
        if (player.position.y - 5.5f > obj.position.y) {
            obj.setDestroy();
        }
    }

    private void updateCoin(Body body, float delta) {
        Coin obj = (Coin) body.getUserData();
        obj.update(delta);
        if (player.position.y - 5.5f > obj.position.y) {
            obj.take();
        }
    }

    private void updateEnemy(Body body, float delta) {
        Enemy obj = (Enemy) body.getUserData();
        obj.update(body, delta);
        if (player.position.y - 5.5f > obj.position.y) {
            obj.hit();
        }
    }

    private void updateItem(Body body, float delta) {
        PowerUpItem obj = (PowerUpItem) body.getUserData();
        obj.update(delta);
        if (player.position.y - 5.5f > obj.position.y) {
            obj.take();
        }
    }

    private void updateCloud(Body body, float delta) {
        Cloud obj = (Cloud) body.getUserData();
        obj.update(body, delta);

        if (obj.isLightning) {
            createLightningBolt(obj.position.x, obj.position.y - .65f);
            obj.fireLighting();
        }

        if (player.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    private void updateLightningBolt(Body body, float delta) {
        LightningBolt obj = (LightningBolt) body.getUserData();
        obj.update(body, delta);

        if (player.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    private void updateBullet(Body body, float delta) {
        Bullet obj = (Bullet) body.getUserData();
        obj.update(body, delta);

        if (player.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    class CollisionHandler implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Player)
                checkPlayerCollisions(b);
            else if (b.getBody().getUserData() instanceof Player)
                checkPlayerCollisions(a);

            if (a.getBody().getUserData() instanceof Bullet)
                checkBulletCollisions(a, b);
            else if (b.getBody().getUserData() instanceof Bullet)
                checkBulletCollisions(b, a);
        }

        private void checkPlayerCollisions(Fixture otherFixture) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject.equals("piso")) {
                player.jump();

                if (player.state == Player.STATE_DEAD) {
                    state = STATE_GAMEOVER;
                }
            } else if (otherObject instanceof Platform) {
                Platform obj = (Platform) otherObject;

                if (player.speed.y <= 0) {
                    player.jump();
                    if (obj.type == Platform.TYPE_BREAKABLE) {
                        obj.setDestroy();
                    }
                }
            } else if (otherObject instanceof Coin) {
                Coin obj = (Coin) otherObject;
                obj.take();
                coins++;
                player.jump();
            } else if (otherObject instanceof Enemy) {
                player.hit();
            } else if (otherObject instanceof LightningBolt) {
                player.hit();
            } else if (otherObject instanceof PowerUpItem) {
                PowerUpItem obj = (PowerUpItem) otherObject;
                obj.take();

                switch (obj.type) {
                    case PowerUpItem.TYPE_BUBBLE:
                        player.setBubble();
                        break;
                    case PowerUpItem.TYPE_JETPACK:
                        player.setJetPack();
                        break;
                    case PowerUpItem.TYPE_GUN:
                        Settings.numBullets += 10;
                        break;
                }
            }
        }

        private void checkBulletCollisions(Fixture bulletFixture, Fixture otherFixture) {
            Object otherObject = otherFixture.getBody().getUserData();
            Bullet bullet = (Bullet) bulletFixture.getBody().getUserData();

            if (otherObject instanceof Enemy) {
                Enemy obj = (Enemy) otherObject;
                obj.hit();
                bullet.destroy();
            } else if (otherObject instanceof Cloud) {
                Cloud obj = (Cloud) otherObject;
                obj.hit();
                bullet.destroy();
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
                preSolvePlayer(a, b, contact);
            else if (b.getBody().getUserData() instanceof Player)
                preSolvePlayer(b, a, contact);
        }

        private void preSolvePlayer(Fixture playerFixture, Fixture otherFixture, Contact contact) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject instanceof Platform) {
                // If you go up, cross the platform.

                Platform obj = (Platform) otherObject;

                float ponyY = playerFixture.getBody().getPosition().y - .30f;
                float pisY = obj.position.y + Platform.HEIGHT_NORMAL / 2f;

                if (ponyY < pisY)
                    contact.setEnabled(false);

                if (obj.type == Platform.TYPE_NORMAL && player.state == Player.STATE_DEAD) {
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
