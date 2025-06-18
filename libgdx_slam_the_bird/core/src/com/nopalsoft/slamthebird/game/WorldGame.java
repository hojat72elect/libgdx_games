package com.nopalsoft.slamthebird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
import com.badlogic.gdx.utils.Pool;
import com.nopalsoft.slamthebird.Achievements;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.game_objects.Coin;
import com.nopalsoft.slamthebird.game_objects.Enemy;
import com.nopalsoft.slamthebird.game_objects.Platform;
import com.nopalsoft.slamthebird.game_objects.Player;
import com.nopalsoft.slamthebird.game_objects.PowerUp;
import com.nopalsoft.slamthebird.screens.BaseScreen;

import java.util.Random;

public class WorldGame {
    final float WIDTH = BaseScreen.WORLD_SCREEN_WIDTH;

    public static final float COMBO_TO_START_GETTING_COINS = 3;

    final public static int STATE_RUNNING = 0;
    final public static int STATE_GAME_OVER = 1;
    int state;

    final public float TIME_TO_SPAWN_ENEMY = 7;
    public float timeToSpawnEnemy;

    public float TIME_TO_SPAWN_BOOST = 15f;
    public float timeToSpawnBoost;

    final public float TIME_TO_CHANGE_STATE_PLATFORM = 16f; // Este tiempo debe ser mayor que DURATION_ACTIVE en la clase plataformas.
    public float timeToChangeStatePlatform;

    final public float TIME_TO_SPAWN_COIN = .75f;
    public float timeToSpawnCoin;

    public World world;

    Player player;
    Array<Platform> arrayPlatforms;
    Array<Enemy> arrayEnemies;
    Array<Body> arrayBodies;
    Array<PowerUp> arrayPowerUps;
    Array<Coin> arrayCoins;

    Random random;

    int scoreForSlammingEnemies;
    int coinsCollected;

    int combo;
    boolean isCoinRain;

    private final Pool<PowerUp> boostPool = new Pool<>() {
        @Override
        protected PowerUp newObject() {
            return new PowerUp();
        }
    };

    private final Pool<Coin> monedaPool = new Pool<>() {
        @Override
        protected Coin newObject() {
            return new Coin();
        }
    };

    public WorldGame() {
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(new CollisionHandler());

        state = STATE_RUNNING;
        arrayBodies = new Array<>();
        arrayEnemies = new Array<>();
        arrayPlatforms = new Array<>();
        arrayPowerUps = new Array<>();
        arrayCoins = new Array<>();

        random = new Random();

        timeToSpawnEnemy = 5;
        isCoinRain = false;

        coinsCollected = scoreForSlammingEnemies = 0;

        float posPiso = .6f;
        createWalls(posPiso);// .05
        createPlayer(posPiso + .251f);

        createPlatforms(0 + Platform.WIDTH / 2f, 1.8f + posPiso);// Left Down
        createPlatforms(WIDTH - Platform.WIDTH / 2f + .1f, 1.8f + posPiso);// Down right

        createPlatforms(0 + Platform.WIDTH / 2f, 1.8f * 2f + posPiso);// Left Top
        createPlatforms(WIDTH - Platform.WIDTH / 2f + .1f,
                1.8f * 2f + posPiso);// Top Right

        // PowerUp stuff
        TIME_TO_SPAWN_BOOST -= Settings.BOOST_DURATION;
    }

    private void createWalls(float floorYPosition) {
        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = 0;
        bodyDefinition.position.y = 0;
        bodyDefinition.type = BodyType.StaticBody;
        Body body = world.createBody(bodyDefinition);

        ChainShape shape = new ChainShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(0.005f, 50);
        vertices[1] = new Vector2(0.005f, 0);
        vertices[2] = new Vector2(WIDTH, 0);
        vertices[3] = new Vector2(WIDTH, 50);
        shape.createChain(vertices);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.restitution = 0;
        fixtureDefinition.friction = 0;

        body.createFixture(fixtureDefinition);
        body.setUserData("pared");
        shape.dispose();

        EdgeShape groundShape = new EdgeShape();
        groundShape.set(0, 0, WIDTH, 0);
        bodyDefinition.position.y = floorYPosition;
        Body groundBody = world.createBody(bodyDefinition);

        fixtureDefinition.shape = groundShape;
        groundBody.createFixture(fixtureDefinition);
        groundBody.setUserData("piso");

        groundShape.dispose();
    }

    private void createPlayer(float y) {
        player = new Player((float) 2.4, y);
        BodyDef bd = new BodyDef();
        bd.position.x = (float) 2.4;
        bd.position.y = y;
        bd.type = BodyType.DynamicBody;

        Body body = world.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(Player.RADIUS);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 5;
        fixtureDefinition.restitution = 0;
        fixtureDefinition.friction = 0;
        body.createFixture(fixtureDefinition);

        body.setFixedRotation(true);
        body.setUserData(player);
        body.setBullet(true);

        shape.dispose();
    }

    private void createEnemies() {
        float x = random.nextFloat() * (WIDTH - 1) + .5f;// Para que no apareza
        float y = random.nextFloat() * 4f + .6f;

        Enemy obj = new Enemy(x, y);
        arrayEnemies.add(obj);
        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = x;
        bodyDefinition.position.y = y;
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = world.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 5;
        fixture.restitution = 0;
        fixture.friction = 0;
        fixture.filter.groupIndex = -1;
        body.createFixture(fixture);

        body.setFixedRotation(true);
        body.setGravityScale(0);
        body.setUserData(obj);

        shape.dispose();
    }

    private void createPlatforms(float x, float y) {

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = x;
        bodyDefinition.position.y = y;
        bodyDefinition.type = BodyType.StaticBody;
        Body body = world.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(Platform.WIDTH / 2f, Platform.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.restitution = 0;
        fixtureDefinition.friction = 0;
        body.createFixture(fixtureDefinition);

        Platform platform = new Platform(bodyDefinition.position.x, bodyDefinition.position.y);
        body.setUserData(platform);
        shape.dispose();

        arrayPlatforms.add(platform);
    }

    private void createPowerUps() {
        PowerUp obj = boostPool.obtain();

        int platformIndex = random.nextInt(4);// above which platform
        int powerUpType = random.nextInt(4);// ice, invincible, coin, etc.
        obj.init(this, arrayPlatforms.get(platformIndex).position.x,
                arrayPlatforms.get(platformIndex).position.y + .3f, powerUpType);

        arrayPowerUps.add(obj);
    }

    private void createCoins() {

        for (int i = 0; i < 6; i++) {
            float x = 0;
            float y = 8.4f + (i * .5f);
            float velocity = Coin.MOVE_SPEED;
            if (i % 2f != 0) {
                velocity *= -1;
                x = WIDTH;
            }

            Body body = Coin.createCoinBody(world, x, y, velocity);
            Coin obj = monedaPool.obtain();
            obj.init(body.getPosition().x, body.getPosition().y);
            arrayCoins.add(obj);
            body.setUserData(obj);
        }
    }

    public void updateReady(float delta, float acelX) {
        world.step(delta, 8, 4);
        world.getBodies(arrayBodies);
        for (Body body : arrayBodies) {
            if (body.getUserData() instanceof Player) {
                player.updateReady(body, acelX);
                break;
            }
        }
    }

    public void update(float delta, float accelerationX, boolean slam) {
        world.step(delta, 8, 4);

        removeGameObjects();

        timeToSpawnEnemy += delta;
        timeToSpawnBoost += delta;
        timeToChangeStatePlatform += delta;
        timeToSpawnCoin += delta;

        if (timeToSpawnEnemy >= TIME_TO_SPAWN_ENEMY) {
            timeToSpawnEnemy -= TIME_TO_SPAWN_ENEMY;
            timeToSpawnEnemy += (scoreForSlammingEnemies * .025f); // Hace que aparezcan mas rapido los malos
            if (arrayEnemies.size < 7 + (scoreForSlammingEnemies * .15f)) {
                if (scoreForSlammingEnemies <= 15) {
                    createEnemies();
                } else if (scoreForSlammingEnemies <= 50) {
                    createEnemies();
                    createEnemies();
                } else {
                    createEnemies();
                    createEnemies();
                    createEnemies();
                }
            }
        }

        if (timeToSpawnBoost >= TIME_TO_SPAWN_BOOST) {
            timeToSpawnBoost -= TIME_TO_SPAWN_BOOST;
            if (random.nextBoolean())
                createPowerUps();
        }

        if (timeToSpawnCoin >= TIME_TO_SPAWN_COIN) {
            timeToSpawnCoin -= TIME_TO_SPAWN_COIN;
            createCoins();
        }

        if (timeToChangeStatePlatform >= TIME_TO_CHANGE_STATE_PLATFORM) {
            timeToChangeStatePlatform -= TIME_TO_CHANGE_STATE_PLATFORM;
            if (random.nextBoolean()) {
                int plat = random.nextInt(4);
                int state = random.nextInt(2);
                Platform obj = arrayPlatforms.get(plat);
                if (state == 0) {
                    obj.setBreakable();
                } else {
                    obj.setFire();
                }
            }
        }

        world.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (body.getUserData() instanceof Player) {
                updatePlayer(delta, body, accelerationX, slam);
            } else if (body.getUserData() instanceof Enemy) {
                updateEnemy(delta, body);
            } else if (body.getUserData() instanceof PowerUp) {
                updatePowerUp(delta, body);
            } else if (body.getUserData() instanceof Platform) {
                updatePlatform(delta, body);
            } else if (body.getUserData() instanceof Coin) {
                updateCoin(delta, body);
            }
        }

        isCoinRain = false;
    }

    private void removeGameObjects() {
        world.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (!world.isLocked()) {
                if (body.getUserData() instanceof Player obj) {
                    if (obj.state == Player.STATE_DEAD
                            && obj.stateTime >= Player.DEAD_ANIMATION_DURATION) {
                        world.destroyBody(body);
                        state = STATE_GAME_OVER;
                    }
                } else if (body.getUserData() instanceof Enemy obj) {
                    if (obj.state == Enemy.STATE_DEAD) {
                        world.destroyBody(body);
                        arrayEnemies.removeValue(obj, true);
                        scoreForSlammingEnemies++;

                        /*
                         * If there are no enemies at least I create one, I put this here so that it does not affect the time in which the first bad guy appears.
                         */
                        if (arrayEnemies.size == 0)
                            createEnemies();
                    }
                } else if (body.getUserData() instanceof PowerUp obj) {
                    if (obj.state == PowerUp.STATE_TAKEN) {
                        world.destroyBody(body);
                        arrayPowerUps.removeValue(obj, true);
                        boostPool.free(obj);
                    }
                } else if (body.getUserData() instanceof Coin obj) {
                    if (obj.state == Coin.STATE_TAKEN) {
                        world.destroyBody(body);
                        arrayCoins.removeValue(obj, true);
                        monedaPool.free(obj);
                    }
                }
            }
        }
    }

    private void updatePlayer(float delta, Body body, float acelX, boolean slam) {
        Player obj = (Player) body.getUserData();
        obj.update(delta, body, acelX, slam);

        if (obj.position.y > 12) {
            Achievements.unlockSuperJump();
            Gdx.app.log("ACHIIIII", "Asdsadasd");
        }
    }

    private void updateEnemy(float delta, Body body) {
        Enemy obj = (Enemy) body.getUserData();
        obj.update(delta, body, random);
    }

    private void updatePowerUp(float delta, Body body) {
        PowerUp obj = (PowerUp) body.getUserData();
        obj.update(delta, body);
    }

    private void updatePlatform(float delta, Body body) {
        Platform obj = (Platform) body.getUserData();
        obj.update(delta);
    }

    private void updateCoin(float delta, Body body) {
        Coin obj = (Coin) body.getUserData();
        obj.update(delta, body);

        if (obj.position.x < -3 || obj.position.x > WIDTH + 3) {
            obj.state = Coin.STATE_TAKEN;
        }

        if (isCoinRain) {
            body.setGravityScale(1);
            body.setLinearVelocity(body.getLinearVelocity().x * .25f, 0);
        }
    }

    class CollisionHandler implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Player)
                handlePlayerCollisions(a, b);
            else if (b.getBody().getUserData() instanceof Player)
                handlePlayerCollisions(b, a);

            if (a.getBody().getUserData() instanceof Enemy)
                handleEnemyCollisions(a, b);
            else if (b.getBody().getUserData() instanceof Enemy)
                handleEnemyCollisions(b, a);
        }

        /**
         * Begin contact PLAYER with OTHER-THING
         */
        private void handlePlayerCollisions(Fixture playerFixture, Fixture otherFixture) {
            Player player = (Player) playerFixture.getBody().getUserData();
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject.equals("piso")) {
                player.jump();

                if (!player.isInvincible)// If he is invincible I don't take away the combo.
                    combo = 0;
            } else if (otherObject instanceof Platform obj) {
                if (obj.state == Platform.STATE_FIRE && !player.isInvincible) {
                    player.hit();
                    return;
                } else if (obj.state == Platform.STATE_BREAKABLE) {
                    obj.setBroken();
                } else if (obj.state == Platform.STATE_BROKEN) {
                    return;
                }
                if (!player.isInvincible && player.state == Player.STATE_FALLING)// If he is invincible I don't take away the combo.
                    combo = 0;
                player.jump();
            } else if (otherObject instanceof PowerUp obj) {
                obj.hit();
                Assets.playSound(Assets.soundBoost);

                if (obj.type == PowerUp.TYPE_SUPER_JUMP) {
                    player.isSuperJump = true;
                } else if (obj.type == PowerUp.TYPE_INVINCIBLE) {
                    player.isInvincible = true;
                } else if (obj.type == PowerUp.TYPE_COIN_RAIN) {
                    isCoinRain = true;
                } else if (obj.type == PowerUp.TYPE_FREEZE) {
                    for (Enemy enemy : arrayEnemies) {
                        enemy.setFrozen();
                    }
                }
            } else if (otherObject instanceof Coin obj) {
                if (obj.state == Coin.STATE_NORMAL) {
                    obj.state = Coin.STATE_TAKEN;
                    coinsCollected++;
                    Settings.currentCoins++;
                    Assets.playSound(Assets.soundCoin);
                }
            } else if (otherObject instanceof Enemy obj) {

                // I can touch from the middle of the enemy up
                float posRobot = player.position.y - Player.RADIUS;
                float pisY = obj.position.y;

                if (obj.state != Enemy.STATE_JUST_APPEARED) {
                    if (player.isInvincible) {
                        obj.die();
                        combo++;
                    } else if (posRobot > pisY) {
                        obj.hit();
                        player.jump();
                        combo++;
                    } else if (player.state != Player.STATE_DEAD) {
                        player.hit();
                        combo = 0;
                    }
                    if (combo >= COMBO_TO_START_GETTING_COINS) {
                        coinsCollected += combo;
                        Settings.currentCoins += combo;
                    }

                    Achievements.unlockCombos();
                }
            }
        }

        private void handleEnemyCollisions(Fixture enemyFixture, Fixture otherFixture) {
            Enemy oEnemy = (Enemy) enemyFixture.getBody().getUserData();
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject.equals("pared")) {

                enemyFixture.getBody().setLinearVelocity(
                        enemyFixture.getBody().getLinearVelocity().x * -1,
                        enemyFixture.getBody().getLinearVelocity().y);
            } else if (otherObject.equals("piso")) {
                if (oEnemy.state == Enemy.STATE_FLYING) {
                    enemyFixture.getBody().setLinearVelocity(
                            enemyFixture.getBody().getLinearVelocity().x,
                            enemyFixture.getBody().getLinearVelocity().y * -1);
                }
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

            if (a.getBody().getUserData() instanceof Enemy)
                preSolveEnemy(a, b, contact);
            else if (b.getBody().getUserData() instanceof Enemy)
                preSolveEnemy(b, a, contact);

            if (a.getBody().getUserData() instanceof Coin)
                preSolveCoins(b, contact);
            else if (b.getBody().getUserData() instanceof Coin)
                preSolveCoins(a, contact);
        }

        private void preSolvePlayer(Fixture playerFixture, Fixture otherFixture,
                                    Contact contact) {
            Object otherObject = otherFixture.getBody().getUserData();
            Player player = (Player) playerFixture.getBody().getUserData();

            // Platform oneSide
            if (otherObject instanceof Platform obj) {
                float playerPosition = player.position.y - Player.RADIUS + .05f;
                float platformPosition = obj.position.y + (Platform.HEIGHT / 2f);

                if (playerPosition < platformPosition || obj.state == Platform.STATE_BROKEN)
                    contact.setEnabled(false);
            }
            // Enemy cannot be touched when it appears
            else if (otherObject instanceof Enemy obj) {
                if (obj.state == Enemy.STATE_JUST_APPEARED
                        || player.isInvincible)
                    contact.setEnabled(false);
            } else if (otherObject instanceof Coin) {
                contact.setEnabled(false);
            }
        }

        private void preSolveEnemy(Fixture enemyFixture, Fixture otherFixture,
                                   Contact contact) {
            Object otherObject = otherFixture.getBody().getUserData();
            Enemy enemy = (Enemy) enemyFixture.getBody().getUserData();

            // Enemy cannot touch platforms if he is flying
            if (otherObject instanceof Platform) {
                if (enemy.state == Enemy.STATE_FLYING)
                    contact.setEnabled(false);
            }
        }

        private void preSolveCoins(Fixture otherFixture,
                                   Contact contact) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject.equals("pared")) {
                contact.setEnabled(false);
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }
    }
}
