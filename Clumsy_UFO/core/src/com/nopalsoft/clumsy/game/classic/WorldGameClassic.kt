package com.nopalsoft.clumsy.game.classic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.nopalsoft.clumsy.Assets;
import com.nopalsoft.clumsy.objects.Pipes;
import com.nopalsoft.clumsy.objects.ScoreKeeper;
import com.nopalsoft.clumsy.objects.Tail;
import com.nopalsoft.clumsy.objects.Ufo;
import com.nopalsoft.clumsy.screens.Screens;

import java.util.Iterator;
import java.util.Random;

public class WorldGameClassic {

    static final int STATE_RUNNING = 0;
    static final int STATE_GAMEOVER = 1;
    final float WIDTH = Screens.WORLD_SCREEN_WIDTH;
    final float HEIGHT = Screens.WORLD_SCREEN_HEIGHT;
    final float TIME_TO_SPAWN_PIPE = 1.35f;// Time between pipes, change this to increase or decrase gap between pipes.
    final float TIME_TO_SPAWN_ARCOIRIS = .005f;
    private final Pool<Tail> arcoirisPool = new Pool<Tail>() {
        @Override
        protected Tail newObject() {
            return new Tail();
        }
    };
    public int state;
    public World oWorldBox;
    public int score;
    float timeToSpawnPipe;
    float timeToSpawnArcoiris;
    Ufo oUfo;
    Array<Pipes> arrTuberias;
    Array<Body> arrBodies;
    Array<Tail> arrTail;
    Random oRan;


    public WorldGameClassic() {
        oWorldBox = new World(new Vector2(0, -13.0f), true);
        oWorldBox.setContactListener(new ClassicGameCollisionHandler(this));

        arrTuberias = new Array<>();
        arrBodies = new Array<>();
        arrTail = new Array<>();

        timeToSpawnPipe = 1.5f;

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

    private void agregarTuberias() {
        float x = WIDTH + 3;
        float y = oRan.nextFloat() * (2.5f) + .5f;

        Pipes obj = new Pipes(x, y, Pipes.LOWER_PIPE);

        BodyDef bd = new BodyDef();
        bd.position.x = obj.position.x;
        bd.position.y = obj.position.y;
        bd.type = BodyType.KinematicBody;
        Body oBody = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Pipes.WIDTH / 2f, Pipes.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 0f;
        fixture.restitution = 0;
        fixture.friction = 0;

        oBody.createFixture(fixture);
        oBody.setFixedRotation(true);
        oBody.setUserData(obj);

        arrTuberias.add(obj);

        // Tuberia arriba
        obj = new Pipes(x, y + 1.7f + Pipes.HEIGHT, Pipes.UPPER_PIPE);
        bd.position.x = obj.position.x;
        bd.position.y = obj.position.y;
        oBody = oWorldBox.createBody(bd);
        oBody.createFixture(fixture);
        oBody.setUserData(obj);
        oBody.setFixedRotation(true);

        arrTuberias.add(obj);

        // Cuandro entre las tuberias
        BodyDef bd2 = new BodyDef();
        bd2.position.x = obj.position.x;
        bd2.position.y = obj.position.y - ScoreKeeper.HEIGHT / 2f - Pipes.HEIGHT / 2f - .035f;
        bd2.type = BodyType.KinematicBody;
        oBody = oWorldBox.createBody(bd2);

        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(ScoreKeeper.WIDTH / 2f, ScoreKeeper.HEIGHT / 2f);
        fixture.isSensor = true;
        fixture.shape = shape2;
        oBody.createFixture(fixture);
        oBody.setUserData(new ScoreKeeper());
        oBody.setFixedRotation(true);

        // Fin Cuadro entre las tuberias

        shape.dispose();
        shape2.dispose();
    }

    public void update(float delta, boolean jump) {
        oWorldBox.step(delta, 8, 4); // para hacer mas lento el juego 1/300f

        eliminarObjetos();

        timeToSpawnPipe += delta;

        if (timeToSpawnPipe >= TIME_TO_SPAWN_PIPE) {
            timeToSpawnPipe -= TIME_TO_SPAWN_PIPE;
            agregarTuberias();
        }

        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (body.getUserData() instanceof Ufo) {
                updateGato(body, delta, jump);
            } else if (body.getUserData() instanceof Pipes) {
                updatePlataforma(body, delta);
            } else if (body.getUserData() instanceof ScoreKeeper) {
                updateContador(body);
            }
        }

        updateArcoiris(delta);
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

        for (Body body : arrBodies) {
            if (!oWorldBox.isLocked()) {

                if (body.getUserData() instanceof Ufo) {
                    Ufo obj = (Ufo) body.getUserData();
                    if (obj.state == Ufo.STATE_DEAD && obj.stateTime >= Ufo.DEATH_DURATION) {
                        oWorldBox.destroyBody(body);
                        state = STATE_GAMEOVER;
                    }
                } else if (body.getUserData() instanceof Pipes) {
                    Pipes obj = (Pipes) body.getUserData();
                    if (obj.state == Pipes.STATE_DESTROY) {
                        arrTuberias.removeValue(obj, true);
                        oWorldBox.destroyBody(body);
                    }
                } else if (body.getUserData() instanceof ScoreKeeper) {
                    ScoreKeeper obj = (ScoreKeeper) body.getUserData();
                    if (obj.state == ScoreKeeper.STATE_DESTROY) {
                        oWorldBox.destroyBody(body);
                    }
                }
            }
        }
    }

    private void updateGato(Body body, float delta, boolean jump) {
        Ufo obj = (Ufo) body.getUserData();

        obj.update(delta, body);

        if (jump && obj.state == Ufo.STATE_NORMAL) {
            body.setLinearVelocity(0, Ufo.JUMP_SPEED);
            Assets.playSound(Assets.wing);
        } else
            body.setLinearVelocity(0, body.getLinearVelocity().y);
    }

    private void updatePlataforma(Body body, float delta) {
        if (oUfo.state == Ufo.STATE_NORMAL) {
            Pipes obj = (Pipes) body.getUserData();
            if (obj != null) {

                obj.update(delta, body);
                if (obj.position.y <= -5)
                    obj.state = Pipes.STATE_DESTROY;

                body.setLinearVelocity(Pipes.SPEED_X, 0);
            }
        } else
            body.setLinearVelocity(0, 0);
    }

    private void updateContador(Body body) {
        if (oUfo.state == Ufo.STATE_NORMAL) {
            ScoreKeeper obj = (ScoreKeeper) body.getUserData();

            if (obj.position.x <= -5)
                obj.state = ScoreKeeper.STATE_DESTROY;

            body.setLinearVelocity(ScoreKeeper.SPEED_X, 0);
        } else
            body.setLinearVelocity(0, 0);
    }
}
