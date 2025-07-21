package com.nopalsoft.clumsy.game.arcade;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.nopalsoft.clumsy.Assets;
import com.nopalsoft.clumsy.objects.Asteroid0;
import com.nopalsoft.clumsy.objects.Asteroid1;
import com.nopalsoft.clumsy.objects.Asteroid2;
import com.nopalsoft.clumsy.objects.Asteroid3;
import com.nopalsoft.clumsy.objects.Asteroid4;
import com.nopalsoft.clumsy.objects.Asteroid5;
import com.nopalsoft.clumsy.objects.Asteroid6;
import com.nopalsoft.clumsy.objects.ScoreKeeper;
import com.nopalsoft.clumsy.objects.Tail;
import com.nopalsoft.clumsy.objects.Ufo;
import com.nopalsoft.clumsy.screens.Screens;

import java.util.Iterator;
import java.util.Random;

public class WorldGameArcade {

    static final int STATE_RUNNING = 0;
    static final int STATE_GAMEOVER = 1;
    final float WIDTH = Screens.WORLD_SCREEN_WIDTH;
    final float HEIGHT = Screens.WORLD_SCREEN_HEIGHT;
    final float TIME_TO_SPAWN_METEOR = .17f;// Time between pipes, change this to increase or decrase gap between pipes.
    final float TIME_TO_SPAWN_ARCOIRIS = .005f;
    private final Pool<Tail> arcoirisPool = new Pool<Tail>() {
        @Override
        protected Tail newObject() {
            return new Tail();
        }
    };
    private final Pool<Asteroid1> meteoro1Pool = new Pool<Asteroid1>() {
        @Override
        protected Asteroid1 newObject() {
            return new Asteroid1();
        }
    };
    private final Pool<Asteroid2> meteoro2Pool = new Pool<Asteroid2>() {
        @Override
        protected Asteroid2 newObject() {
            return new Asteroid2();
        }
    };
    private final Pool<Asteroid3> meteoro3Pool = new Pool<Asteroid3>() {
        @Override
        protected Asteroid3 newObject() {
            return new Asteroid3();
        }
    };
    private final Pool<Asteroid4> meteoro4Pool = new Pool<Asteroid4>() {
        @Override
        protected Asteroid4 newObject() {
            return new Asteroid4();
        }
    };
    private final Pool<Asteroid5> meteoro5Pool = new Pool<Asteroid5>() {
        @Override
        protected Asteroid5 newObject() {
            return new Asteroid5();
        }
    };
    private final Pool<Asteroid6> meteoro6Pool = new Pool<Asteroid6>() {
        @Override
        protected Asteroid6 newObject() {
            return new Asteroid6();
        }
    };
    public World oWorldBox;
    public float score;
    public int state;
    float timeToSpawnMeteor;
    float timeToSpawnArcoiris;
    Ufo oUfo;
    Array<Asteroid0> arrMeteoros;
    Array<Body> arrBodies;
    Array<Tail> arrTail;
    Random oRan;

    public WorldGameArcade() {
        oWorldBox = new World(new Vector2(0, -12.8f), true);
        oWorldBox.setContactListener(new ColisionesArcade());

        arrMeteoros = new Array<>();
        arrBodies = new Array<>();
        arrTail = new Array<>();

        createGato();
        createPiso();
        crearTecho();

        state = STATE_RUNNING;

        oRan = new Random();
    }

    private void crearTecho() {
        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = HEIGHT;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, WIDTH, 0);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 0f;
        fixture.restitution = 0;
        fixture.friction = 0;

        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);

        shape.dispose();
    }

    private void createPiso() {

        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = 1.4f;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, WIDTH, 0);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 0f;
        fixture.restitution = 0;
        fixture.friction = 0;

        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);

        shape.dispose();
    }

    private void createGato() {
        oUfo = new Ufo(WIDTH / 3.2f, HEIGHT / 2f);

        BodyDef bd = new BodyDef();
        bd.position.x = oUfo.position.x;
        bd.position.y = oUfo.position.y;
        bd.type = BodyType.DynamicBody;

        Body oBody = oWorldBox.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(.19f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 8;
        fixture.restitution = 0;
        fixture.friction = 0;
        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setUserData(oUfo);
        oBody.setBullet(true);

        shape.dispose();
    }

    private void agregarMetoro() {
        Asteroid0 obj;

        switch (oRan.nextInt(6)) {

            case 0:
                obj = meteoro1Pool.obtain();
                break;

            case 1:
                obj = meteoro2Pool.obtain();
                break;
            case 2:
                obj = meteoro3Pool.obtain();
                break;
            case 3:
                obj = meteoro4Pool.obtain();
                break;
            case 4:
                obj = meteoro5Pool.obtain();
                break;
            case 5:
            default:
                obj = meteoro6Pool.obtain();
                break;
        }

        obj.init(this, 5, oRan.nextInt(9));
        arrMeteoros.add(obj);
    }

    public void update(float delta, boolean jump) {
        oWorldBox.step(delta, 8, 4); // para hacer mas lento el juego 1/300f

        cleanupGameObjects();

        timeToSpawnMeteor += delta;

        if (timeToSpawnMeteor >= TIME_TO_SPAWN_METEOR) {
            timeToSpawnMeteor -= TIME_TO_SPAWN_METEOR;
            agregarMetoro();
        }

        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (body.getUserData() instanceof Ufo) {
                updateUfo(body, delta, jump);
            } else if (body.getUserData() instanceof Asteroid0) {
                updateMetoro(body, delta);
            }
        }

        updateRainbowTail(delta);

        if (oUfo.state == Ufo.STATE_NORMAL) {
            score += delta * 5;
        }
    }

    private void updateRainbowTail(float delta) {
        timeToSpawnArcoiris += delta;

        if (timeToSpawnArcoiris >= TIME_TO_SPAWN_ARCOIRIS) {
            timeToSpawnArcoiris -= TIME_TO_SPAWN_ARCOIRIS;
            Tail tail = arcoirisPool.obtain();
            tail.init(oUfo.position.x, oUfo.position.y);
            arrTail.add(tail);
        }

        Iterator<Tail> i = arrTail.iterator();
        while (i.hasNext()) {
            Tail obj = i.next();
            obj.update(delta);

            if (obj.position.x < -3) {
                i.remove();
                arcoirisPool.free(obj);
            }
        }
    }

    private void cleanupGameObjects() {
        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (!oWorldBox.isLocked()) {

                if (body.getUserData() instanceof Ufo) {
                    Ufo obj = (Ufo) body.getUserData();
                    if (obj.state == Ufo.STATE_DEAD && obj.stateTime >= Ufo.DEATH_DURATION) {
                        oWorldBox.destroyBody(body);
                        state = STATE_GAMEOVER;
                    }
                } else if (body.getUserData() instanceof Asteroid0) {
                    Asteroid0 obj = (Asteroid0) body.getUserData();
                    if (obj.state == Asteroid0.STATE_DESTROY) {
                        oWorldBox.destroyBody(body);
                    }
                }
            }
        }
    }

    private void updateUfo(Body body, float delta, boolean jump) {
        Ufo obj = (Ufo) body.getUserData();

        obj.update(delta, body);

        if (jump && obj.state == Ufo.STATE_NORMAL) {
            body.setLinearVelocity(0, Ufo.JUMP_SPEED);
            Assets.playSound(Assets.wing);
        } else
            body.setLinearVelocity(0, body.getLinearVelocity().y);
    }

    private void updateMetoro(Body body, float delta) {
        if (oUfo.state == Ufo.STATE_NORMAL) {
            Asteroid0 obj = (Asteroid0) body.getUserData();
            if (obj != null) {

                obj.update(delta, body);
                if (obj.position.x <= -5)
                    obj.state = Asteroid0.STATE_DESTROY;
            }
        } else
            body.setLinearVelocity(0, 0);
    }

    static class ColisionesArcade implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Ufo)
                handlePlayerCollisions(a, b);
            else if (b.getBody().getUserData() instanceof Ufo)
                handlePlayerCollisions(b, a);
        }

        private void handlePlayerCollisions(Fixture playerFixture, Fixture otherFixture) {
            Ufo oUfo = (Ufo) playerFixture.getBody().getUserData();
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject instanceof ScoreKeeper) {
                ScoreKeeper obj = (ScoreKeeper) otherObject;
                if (obj.state == ScoreKeeper.STATE_NORMAL) {
                    obj.state = ScoreKeeper.STATE_DESTROY;
                    Assets.playSound(Assets.point);
                }
            } else {
                if (oUfo.state == Ufo.STATE_NORMAL) {
                    oUfo.getHurt();
                    Assets.playSound(Assets.hit);
                }
            }
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }
    }
}
