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
import com.nopalsoft.clumsy.objects.Contador;
import com.nopalsoft.clumsy.objects.Meteoro;
import com.nopalsoft.clumsy.objects.Meteoro1;
import com.nopalsoft.clumsy.objects.Meteoro2;
import com.nopalsoft.clumsy.objects.Meteoro3;
import com.nopalsoft.clumsy.objects.Meteoro4;
import com.nopalsoft.clumsy.objects.Meteoro5;
import com.nopalsoft.clumsy.objects.Meteoro6;
import com.nopalsoft.clumsy.objects.Tail;
import com.nopalsoft.clumsy.objects.Ufo;
import com.nopalsoft.clumsy.screens.Screens;

import java.util.Iterator;
import java.util.Random;

public class WorldGameArcade {

    final float WIDTH = Screens.WORLD_SCREEN_WIDTH;
    final float HEIGHT = Screens.WORLD_SCREEN_HEIGHT;

    static final int STATE_RUNNING = 0;
    static final int STATE_GAMEOVER = 1;

    final float TIME_TO_SPAWN_METEOR = .17f;// Time between pipes, change this to increase or decrase gap between pipes.
    float timeToSpawnMeteor;

    final float TIME_TO_SPAWN_ARCOIRIS = .005f;
    float timeToSpawnArcoiris;

    public World oWorldBox;

    public float score;

    Ufo oUfo;
    Array<Meteoro> arrMeteoros;
    Array<Body> arrBodies;
    Array<Tail> arrTail;

    private final Pool<Tail> arcoirisPool = new Pool<Tail>() {
        @Override
        protected Tail newObject() {
            return new Tail();
        }
    };

    private final Pool<Meteoro1> meteoro1Pool = new Pool<Meteoro1>() {
        @Override
        protected Meteoro1 newObject() {
            return new Meteoro1();
        }
    };

    private final Pool<Meteoro2> meteoro2Pool = new Pool<Meteoro2>() {
        @Override
        protected Meteoro2 newObject() {
            return new Meteoro2();
        }
    };

    private final Pool<Meteoro3> meteoro3Pool = new Pool<Meteoro3>() {
        @Override
        protected Meteoro3 newObject() {
            return new Meteoro3();
        }
    };

    private final Pool<Meteoro4> meteoro4Pool = new Pool<Meteoro4>() {
        @Override
        protected Meteoro4 newObject() {
            return new Meteoro4();
        }
    };

    private final Pool<Meteoro5> meteoro5Pool = new Pool<Meteoro5>() {
        @Override
        protected Meteoro5 newObject() {
            return new Meteoro5();
        }
    };

    private final Pool<Meteoro6> meteoro6Pool = new Pool<Meteoro6>() {
        @Override
        protected Meteoro6 newObject() {
            return new Meteoro6();
        }
    };

    Random oRan;

    public int state;

    public WorldGameArcade() {
        oWorldBox = new World(new Vector2(0, -12.8f), true);
        oWorldBox.setContactListener(new ColisionesArcade());

        arrMeteoros = new Array<Meteoro>();
        arrBodies = new Array<Body>();
        arrTail = new Array<Tail>();

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
        Meteoro obj;

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
            default:
            case 5:
                obj = meteoro6Pool.obtain();
                break;
        }

        obj.init(this, 5, oRan.nextInt(9));
        arrMeteoros.add(obj);
    }

    public void update(float delta, boolean jump) {
        oWorldBox.step(delta, 8, 4); // para hacer mas lento el juego 1/300f

        eliminarObjetos();

        timeToSpawnMeteor += delta;

        if (timeToSpawnMeteor >= TIME_TO_SPAWN_METEOR) {
            timeToSpawnMeteor -= TIME_TO_SPAWN_METEOR;
            agregarMetoro();
        }

        oWorldBox.getBodies(arrBodies);
        Iterator<Body> i = arrBodies.iterator();

        while (i.hasNext()) {
            Body body = i.next();

            if (body.getUserData() instanceof Ufo) {
                updateGato(body, delta, jump);
            } else if (body.getUserData() instanceof Meteoro) {
                updateMetoro(body, delta);
            }
        }

        updateArcoiris(delta);

        if (oUfo.state == Ufo.STATE_NORMAL) {
            score += delta * 5;
        }
    }

    private void updateArcoiris(float delta) {
        timeToSpawnArcoiris += delta;

        if (timeToSpawnArcoiris >= TIME_TO_SPAWN_ARCOIRIS) {
            timeToSpawnArcoiris -= TIME_TO_SPAWN_ARCOIRIS;
            Tail oArco = arcoirisPool.obtain();
            oArco.init(oUfo.position.x, oUfo.position.y);
            arrTail.add(oArco);
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

    private void eliminarObjetos() {
        oWorldBox.getBodies(arrBodies);
        Iterator<Body> i = arrBodies.iterator();

        while (i.hasNext()) {
            Body body = i.next();

            if (!oWorldBox.isLocked()) {

                if (body.getUserData() instanceof Ufo) {
                    Ufo obj = (Ufo) body.getUserData();
                    if (obj.state == Ufo.STATE_DEAD && obj.stateTime >= Ufo.TIEMPO_MUERTO) {
                        oWorldBox.destroyBody(body);
                        state = STATE_GAMEOVER;
                        continue;
                    }
                } else if (body.getUserData() instanceof Meteoro) {
                    Meteoro obj = (Meteoro) body.getUserData();
                    if (obj.state == Meteoro.STATE_DESTROY) {
                        oWorldBox.destroyBody(body);
                        continue;
                    }
                }
            }
        }
    }

    private void updateGato(Body body, float delta, boolean jump) {
        Ufo obj = (Ufo) body.getUserData();

        obj.update(delta, body);

        if (jump && obj.state == Ufo.STATE_NORMAL) {
            body.setLinearVelocity(0, Ufo.VELOCIDAD_JUMP);
            Assets.playSound(Assets.wing);
        } else
            body.setLinearVelocity(0, body.getLinearVelocity().y);
    }

    private void updateMetoro(Body body, float delta) {
        if (oUfo.state == Ufo.STATE_NORMAL) {
            Meteoro obj = (Meteoro) body.getUserData();
            if (obj != null) {

                obj.update(delta, body);
                if (obj.position.x <= -5)
                    obj.state = Meteoro.STATE_DESTROY;
            }
        } else
            body.setLinearVelocity(0, 0);
    }

    class ColisionesArcade implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Ufo)
                beginContactBirdOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Ufo)
                beginContactBirdOtraCosa(b, a);
        }

        private void beginContactBirdOtraCosa(Fixture bird, Fixture otraCosa) {
            Ufo oUfo = (Ufo) bird.getBody().getUserData();
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Contador) {
                Contador obj = (Contador) oOtraCosa;
                if (obj.state == Contador.STATE_NORMAL) {
                    obj.state = Contador.STATE_DESTROY;
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
            // TODO Auto-generated method stub

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            // TODO Auto-generated method stub

        }
    }
}
