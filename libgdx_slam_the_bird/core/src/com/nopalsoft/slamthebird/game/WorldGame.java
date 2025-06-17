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
import com.nopalsoft.slamthebird.objetos.Boost;
import com.nopalsoft.slamthebird.objetos.Coin;
import com.nopalsoft.slamthebird.objetos.Enemy;
import com.nopalsoft.slamthebird.objetos.Platform;
import com.nopalsoft.slamthebird.objetos.Robot;
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

    public World oWorldBox;

    Robot oRobo;
    Array<Platform> arrPlataformas;
    Array<Enemy> arrEnemigos;
    Array<Body> arrBodies;
    Array<Boost> arrBoost;
    Array<Coin> arrMonedas;

    Random oRan;

    int scoreSlamed;
    int monedasTomadas;

    int combo;
    boolean isCoinRain;

    private final Pool<Boost> boostPool = new Pool<>() {
        @Override
        protected Boost newObject() {
            return new Boost();
        }
    };

    private final Pool<Coin> monedaPool = new Pool<>() {
        @Override
        protected Coin newObject() {
            return new Coin();
        }
    };

    public WorldGame() {
        oWorldBox = new World(new Vector2(0, -9.8f), true);
        oWorldBox.setContactListener(new Colisiones());

        state = STATE_RUNNING;
        arrBodies = new Array<>();
        arrEnemigos = new Array<>();
        arrPlataformas = new Array<>();
        arrBoost = new Array<>();
        arrMonedas = new Array<>();

        oRan = new Random();

        timeToSpawnEnemy = 5;
        isCoinRain = false;

        monedasTomadas = scoreSlamed = 0;

        float posPiso = .6f;
        crearParedes(posPiso);// .05
        crearRobot(posPiso + .251f);

        crearPlataformas(0 + Platform.WIDTH / 2f, 1.8f + posPiso);// Izq Abajo
        crearPlataformas(WIDTH - Platform.WIDTH / 2f + .1f, 1.8f + posPiso);// Derecha abajo

        crearPlataformas(0 + Platform.WIDTH / 2f, 1.8f * 2f + posPiso);// Izq Arriba
        crearPlataformas(WIDTH - Platform.WIDTH / 2f + .1f,
                1.8f * 2f + posPiso);// Derecha Arribadd

        // Boost stuff
        TIME_TO_SPAWN_BOOST -= Settings.BOOST_DURATION;
    }

    private void crearParedes(float posPisoY) {
        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = 0;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        ChainShape shape = new ChainShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(0.005f, 50);
        vertices[1] = new Vector2(0.005f, 0);
        vertices[2] = new Vector2(WIDTH, 0);
        vertices[3] = new Vector2(WIDTH, 50);
        shape.createChain(vertices);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.restitution = 0;
        fixture.friction = 0;

        oBody.createFixture(fixture);
        oBody.setUserData("pared");
        shape.dispose();

        // Piso
        EdgeShape shapePiso = new EdgeShape();
        shapePiso.set(0, 0, WIDTH, 0);
        bd.position.y = posPisoY;
        Body oBodyPiso = oWorldBox.createBody(bd);

        fixture.shape = shapePiso;
        oBodyPiso.createFixture(fixture);
        oBodyPiso.setUserData("piso");

        shapePiso.dispose();
    }

    private void crearRobot(float y) {
        oRobo = new Robot((float) 2.4, y);
        BodyDef bd = new BodyDef();
        bd.position.x = (float) 2.4;
        bd.position.y = y;
        bd.type = BodyType.DynamicBody;

        Body oBody = oWorldBox.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(Robot.RADIUS);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 5;
        fixture.restitution = 0;
        fixture.friction = 0;
        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setUserData(oRobo);
        oBody.setBullet(true);

        shape.dispose();
    }

    private void crearEnemigos() {
        float x = oRan.nextFloat() * (WIDTH - 1) + .5f;// Para que no apareza
        float y = oRan.nextFloat() * 4f + .6f;

        Enemy obj = new Enemy(x, y);
        arrEnemigos.add(obj);
        BodyDef bd = new BodyDef();
        bd.position.x = x;
        bd.position.y = y;
        bd.type = BodyType.DynamicBody;

        Body oBody = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 5;
        fixture.restitution = 0;
        fixture.friction = 0;
        fixture.filter.groupIndex = -1;
        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setGravityScale(0);
        oBody.setUserData(obj);

        shape.dispose();
    }

    private void crearPlataformas(float x, float y) {

        BodyDef bd = new BodyDef();
        bd.position.x = x;
        bd.position.y = y;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(Platform.WIDTH / 2f, Platform.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.restitution = 0;
        fixture.friction = 0;
        oBody.createFixture(fixture);

        Platform obj = new Platform(bd.position.x, bd.position.y);
        oBody.setUserData(obj);
        shape.dispose();

        arrPlataformas.add(obj);
    }

    private void crearBoost() {
        Boost obj = boostPool.obtain();

        int plat = oRan.nextInt(4);// arriba de que plataforma
        int tipo = oRan.nextInt(4);// ice, invencible,moneda,etc
        obj.init(this, arrPlataformas.get(plat).position.x,
                arrPlataformas.get(plat).position.y + .3f, tipo);

        arrBoost.add(obj);
    }

    private void crearMonedas() {

        for (int i = 0; i < 6; i++) {
            float x = 0;
            float y = 8.4f + (i * .5f);
            float velocidad = Coin.MOVE_SPEED;
            if (i % 2f != 0) {
                velocidad *= -1;
                x = WIDTH;
            }

            Body body = Coin.createCoinBody(oWorldBox, x, y, velocidad);
            Coin obj = monedaPool.obtain();
            obj.init(body.getPosition().x, body.getPosition().y);
            arrMonedas.add(obj);
            body.setUserData(obj);
        }
    }

    public void updateReady(float delta, float acelX) {
        oWorldBox.step(delta, 8, 4);
        oWorldBox.getBodies(arrBodies);
        for (Body body : arrBodies) {
            if (body.getUserData() instanceof Robot) {
                oRobo.updateReady(body, acelX);
                break;
            }
        }
    }

    public void update(float delta, float acelX, boolean slam) {
        oWorldBox.step(delta, 8, 4);

        eliminarObjetos();

        timeToSpawnEnemy += delta;
        timeToSpawnBoost += delta;
        timeToChangeStatePlatform += delta;
        timeToSpawnCoin += delta;

        if (timeToSpawnEnemy >= TIME_TO_SPAWN_ENEMY) {
            timeToSpawnEnemy -= TIME_TO_SPAWN_ENEMY;
            timeToSpawnEnemy += (scoreSlamed * .025f); // Hace que aparezcan mas rapido los malos
            if (arrEnemigos.size < 7 + (scoreSlamed * .15f)) {
                if (scoreSlamed <= 15) {
                    crearEnemigos();
                } else if (scoreSlamed <= 50) {
                    crearEnemigos();
                    crearEnemigos();
                } else {
                    crearEnemigos();
                    crearEnemigos();
                    crearEnemigos();
                }
            }
        }

        if (timeToSpawnBoost >= TIME_TO_SPAWN_BOOST) {
            timeToSpawnBoost -= TIME_TO_SPAWN_BOOST;
            if (oRan.nextBoolean())
                crearBoost();
        }

        if (timeToSpawnCoin >= TIME_TO_SPAWN_COIN) {
            timeToSpawnCoin -= TIME_TO_SPAWN_COIN;
            crearMonedas();
        }

        if (timeToChangeStatePlatform >= TIME_TO_CHANGE_STATE_PLATFORM) {
            timeToChangeStatePlatform -= TIME_TO_CHANGE_STATE_PLATFORM;
            if (oRan.nextBoolean()) {
                int plat = oRan.nextInt(4);
                int state = oRan.nextInt(2);
                Platform obj = arrPlataformas.get(plat);
                if (state == 0) {
                    obj.setBreakable();
                } else {
                    obj.setFire();
                }
            }
        }

        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (body.getUserData() instanceof Robot) {
                updateRobot(delta, body, acelX, slam);
            } else if (body.getUserData() instanceof Enemy) {
                updateEnemigo(delta, body);
            } else if (body.getUserData() instanceof Boost) {
                updateBoost(delta, body);
            } else if (body.getUserData() instanceof Platform) {
                updatePlataforma(delta, body);
            } else if (body.getUserData() instanceof Coin) {
                updateMoneda(delta, body);
            }
        }

        isCoinRain = false;
    }

    private void eliminarObjetos() {
        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (!oWorldBox.isLocked()) {
                if (body.getUserData() instanceof Robot obj) {
                    if (obj.state == Robot.STATE_DEAD
                            && obj.stateTime >= Robot.DEAD_ANIMATION_DURATION) {
                        oWorldBox.destroyBody(body);
                        state = STATE_GAME_OVER;
                    }
                } else if (body.getUserData() instanceof Enemy obj) {
                    if (obj.state == Enemy.STATE_DEAD) {
                        oWorldBox.destroyBody(body);
                        arrEnemigos.removeValue(obj, true);
                        scoreSlamed++;

                        /*
                         * Si no hay enemigos el menos creo uno esto lo pongo aqui para que no afecte el tiempo en el que aparece el primer malo
                         */
                        if (arrEnemigos.size == 0)
                            crearEnemigos();
                    }
                } else if (body.getUserData() instanceof Boost obj) {
                    if (obj.state == Boost.STATE_TAKEN) {
                        oWorldBox.destroyBody(body);
                        arrBoost.removeValue(obj, true);
                        boostPool.free(obj);
                    }
                } else if (body.getUserData() instanceof Coin obj) {
                    if (obj.state == Coin.STATE_TAKEN) {
                        oWorldBox.destroyBody(body);
                        arrMonedas.removeValue(obj, true);
                        monedaPool.free(obj);
                    }
                }
            }
        }
    }

    private void updateRobot(float delta, Body body, float acelX, boolean slam) {
        Robot obj = (Robot) body.getUserData();
        obj.update(delta, body, acelX, slam);

        if (obj.position.y > 12) {
            Achievements.unlockSuperJump();
            Gdx.app.log("ACHIIIII", "Asdsadasd");
        }
    }

    private void updateEnemigo(float delta, Body body) {
        Enemy obj = (Enemy) body.getUserData();
        obj.update(delta, body, oRan);
    }

    private void updateBoost(float delta, Body body) {
        Boost obj = (Boost) body.getUserData();
        obj.update(delta, body);
    }

    private void updatePlataforma(float delta, Body body) {
        Platform obj = (Platform) body.getUserData();
        obj.update(delta);
    }

    private void updateMoneda(float delta, Body body) {
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

    class Colisiones implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Robot)
                beginContactRobotOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Robot)
                beginContactRobotOtraCosa(b, a);

            if (a.getBody().getUserData() instanceof Enemy)
                beginContactEnemigoOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Enemy)
                beginContactEnemigoOtraCosa(b, a);
        }

        /**
         * Begin contacto ROBOT con OTRA-COSA
         */
        private void beginContactRobotOtraCosa(Fixture robot, Fixture otraCosa) {
            Robot oRobo = (Robot) robot.getBody().getUserData();
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa.equals("piso")) {
                oRobo.jump();

                if (!oRobo.isInvincible)// Si es invencible no le quito el combo
                    combo = 0;
            } else if (oOtraCosa instanceof Platform obj) {
                if (obj.state == Platform.STATE_FIRE && !oRobo.isInvincible) {
                    oRobo.hit();
                    return;
                } else if (obj.state == Platform.STATE_BREAKABLE) {
                    obj.setBroken();
                } else if (obj.state == Platform.STATE_BROKEN) {
                    return;
                }
                if (!oRobo.isInvincible && oRobo.state == Robot.STATE_FALLING)// Si es invencible no le quito el combo
                    combo = 0;
                oRobo.jump();
            } else if (oOtraCosa instanceof Boost obj) {
                obj.hit();
                Assets.playSound(Assets.soundBoost);

                if (obj.type == Boost.TYPE_SUPER_JUMP) {
                    oRobo.isSuperJump = true;
                } else if (obj.type == Boost.TYPE_INVINCIBLE) {
                    oRobo.isInvincible = true;
                } else if (obj.type == Boost.TYPE_COIN_RAIN) {
                    isCoinRain = true;
                } else if (obj.type == Boost.TYPE_FREEZE) {
                    for (Enemy arrEnemy : arrEnemigos) {
                        arrEnemy.setFrozen();
                    }
                }
            } else if (oOtraCosa instanceof Coin obj) {
                if (obj.state == Coin.STATE_NORMAL) {
                    obj.state = Coin.STATE_TAKEN;
                    monedasTomadas++;
                    Settings.currentCoins++;
                    Assets.playSound(Assets.soundCoin);
                }
            } else if (oOtraCosa instanceof Enemy obj) {

                // Puedo tocar de la mitad del enemigo para arriba
                float posRobot = oRobo.position.y - Robot.RADIUS;
                float pisY = obj.position.y;

                if (obj.state != Enemy.STATE_JUST_APPEARED) {
                    if (oRobo.isInvincible) {
                        obj.die();
                        combo++;
                    } else if (posRobot > pisY) {
                        obj.hit();
                        oRobo.jump();
                        combo++;
                    } else if (oRobo.state != Robot.STATE_DEAD) {
                        oRobo.hit();
                        combo = 0;
                    }
                    if (combo >= COMBO_TO_START_GETTING_COINS) {
                        monedasTomadas += combo;
                        Settings.currentCoins += combo;
                    }

                    Achievements.unlockCombos();
                }
            }
        }

        private void beginContactEnemigoOtraCosa(Fixture enemy, Fixture otraCosa) {
            Enemy oEnemy = (Enemy) enemy.getBody().getUserData();
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa.equals("pared")) {

                enemy.getBody().setLinearVelocity(
                        enemy.getBody().getLinearVelocity().x * -1,
                        enemy.getBody().getLinearVelocity().y);
            } else if (oOtraCosa.equals("piso")) {
                if (oEnemy.state == Enemy.STATE_FLYING) {
                    enemy.getBody().setLinearVelocity(
                            enemy.getBody().getLinearVelocity().x,
                            enemy.getBody().getLinearVelocity().y * -1);
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

            if (a.getBody().getUserData() instanceof Robot)
                preSolveRobot(a, b, contact);
            else if (b.getBody().getUserData() instanceof Robot)
                preSolveRobot(b, a, contact);

            if (a.getBody().getUserData() instanceof Enemy)
                preSolveEnemigo(a, b, contact);
            else if (b.getBody().getUserData() instanceof Enemy)
                preSolveEnemigo(b, a, contact);

            if (a.getBody().getUserData() instanceof Coin)
                preSolveMoneda(b, contact);
            else if (b.getBody().getUserData() instanceof Coin)
                preSolveMoneda(a, contact);
        }

        private void preSolveRobot(Fixture robot, Fixture otraCosa,
                                   Contact contact) {
            Object oOtraCosa = otraCosa.getBody().getUserData();
            Robot oRobo = (Robot) robot.getBody().getUserData();

            // Platform oneSide
            if (oOtraCosa instanceof Platform obj) {
                float posRobot = oRobo.position.y - Robot.RADIUS + .05f;
                float pisY = obj.position.y + (Platform.HEIGHT / 2f);

                if (posRobot < pisY || obj.state == Platform.STATE_BROKEN)
                    contact.setEnabled(false);
            }
            // Enemy no se puede tocar cuando aparece
            else if (oOtraCosa instanceof Enemy obj) {
                if (obj.state == Enemy.STATE_JUST_APPEARED
                        || oRobo.isInvincible)
                    contact.setEnabled(false);
            } else if (oOtraCosa instanceof Coin) {
                contact.setEnabled(false);
            }
        }

        private void preSolveEnemigo(Fixture enemigo, Fixture otraCosa,
                                     Contact contact) {
            Object oOtraCosa = otraCosa.getBody().getUserData();
            Enemy oEnem = (Enemy) enemigo.getBody().getUserData();

            // Enemy no puede tocar las plataformas si esta volando
            if (oOtraCosa instanceof Platform) {
                if (oEnem.state == Enemy.STATE_FLYING)
                    contact.setEnabled(false);
            }
        }

        private void preSolveMoneda(Fixture otraCosa,
                                    Contact contact) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa.equals("pared")) {
                contact.setEnabled(false);
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            // TODO Auto-generated method stub

        }
    }
}
