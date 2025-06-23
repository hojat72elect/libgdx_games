package com.nopalsoft.flappy.game;

import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.flappy.objetos.Bird;
import com.nopalsoft.flappy.objetos.Counter;
import com.nopalsoft.flappy.objetos.Pipe;
import com.nopalsoft.flappy.screens.Screens;

public class WorldGame {
    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    static final int STATE_RUNNING = 0;
    static final int STATE_GAME_OVER = 1;
    public int state;

    /**
     * Time between pipes, if you increase this number the space between pipes will increase
     */
    final float TIME_TO_SPAWN_PIPE = 1.5f;
    float timeToSpawnPipe;

    public World oWorldBox;
    public int score;

    /**
     * Save the information about the bird
     */
    Bird oBird;

    /**
     * Save the information about the pipes
     */
    Array<Pipe> arrPipes;

    /**
     * Save the information about the bodies (box2d). Includes: Birds, pipes & counter object
     */
    Array<Body> arrBodies;

    public WorldGame() {
        oWorldBox = new World(new Vector2(0, -13.0f), true);
        oWorldBox.setContactListener(new Collisions());

        arrPipes = new Array<Pipe>();
        arrBodies = new Array<Body>();

        timeToSpawnPipe = 1.5f;

        createBird();
        createRoof();
        createFloor();

        state = STATE_RUNNING;

    }

    private void createBird() {
        oBird = new Bird(1.35f, 4.75f);

        BodyDef bd = new BodyDef();
        bd.position.x = oBird.position.x;
        bd.position.y = oBird.position.y;
        bd.type = BodyType.DynamicBody;

        Body oBody = oWorldBox.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(.25f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 8;
        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setUserData(oBird);
        oBody.setBullet(true);

        shape.dispose();
    }

    private void createRoof() {
        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = HEIGHT;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, WIDTH, 0);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;

        oBody.createFixture(fixture);
        shape.dispose();
    }

    private void createFloor() {

        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = 1.1f;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, WIDTH, 0);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;

        oBody.createFixture(fixture);
        shape.dispose();
    }

    public void addPipe() {
        float x = WIDTH + 2.5f;
        float y = MathUtils.random() * (1.5f) + .4f;

        // Add the bottom pipe
        addPipe(x, y, false);

        // Add the top pipe
        addPipe(x, y + 2f + Pipe.HEIGHT, true);

        //add counter object (between the two pipes)
        addCounter(x, y + Counter.HEIGHT / 2f + Pipe.HEIGHT / 2f + .1f);

    }

    private void addPipe(float x, float y, boolean isTopPipe) {
        Pipe obj;
        if (isTopPipe)
            obj = new Pipe(x, y, Pipe.TYPE_UP);
        else
            obj = new Pipe(x, y, Pipe.TYPE_DOWN);

        BodyDef bd = new BodyDef();
        bd.position.x = x;
        bd.position.y = y;
        bd.type = BodyType.KinematicBody;
        Body oBody = oWorldBox.createBody(bd);
        oBody.setLinearVelocity(Pipe.SPEED_X, 0);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Pipe.WIDTH / 2f, Pipe.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;

        oBody.createFixture(fixture);
        oBody.setFixedRotation(true);
        oBody.setUserData(obj);
        arrPipes.add(obj);
        shape.dispose();

    }

    private void addCounter(float x, float y) {
        Counter obj = new Counter();
        BodyDef bd = new BodyDef();
        bd.position.x = x;
        bd.position.y = y;
        bd.type = BodyType.KinematicBody;
        Body oBody = oWorldBox.createBody(bd);
        oBody.setLinearVelocity(Counter.SPEED_X, 0);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Counter.WIDTH / 2f, Counter.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;

        oBody.createFixture(fixture);
        oBody.setFixedRotation(true);
        oBody.setUserData(obj);

        shape.dispose();
    }

    public void update(float delta, boolean jump) {
        oWorldBox.step(delta, 8, 4);

        deleteObjects();

        timeToSpawnPipe += delta;

        if (timeToSpawnPipe >= TIME_TO_SPAWN_PIPE) {
            timeToSpawnPipe -= TIME_TO_SPAWN_PIPE;
            addPipe();
        }

        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (body.getUserData() instanceof Bird) {
                updateBird(body, delta, jump);
            } else if (body.getUserData() instanceof Pipe) {
                updatePipes(body);
            } else if (body.getUserData() instanceof Counter) {
                updateCounter(body);
            }
        }

        if (oBird.state == Bird.STATE_DEAD)
            state = STATE_GAME_OVER;
    }

    private void updateBird(Body body, float delta, boolean jump) {

        oBird.update(delta, body);

        if (jump && oBird.state == Bird.STATE_NORMAL) {
            body.setLinearVelocity(0, Bird.JUMP_SPEED);
        }

    }

    private void updatePipes(Body body) {
        if (oBird.state == Bird.STATE_NORMAL) {
            Pipe obj = (Pipe) body.getUserData();

            obj.update(body);
            if (obj.position.x <= -5)
                obj.state = Pipe.STATE_REMOVE;

        } else
            body.setLinearVelocity(0, 0);

    }

    private void updateCounter(Body body) {
        if (oBird.state == Bird.STATE_NORMAL) {
            Counter obj = (Counter) body.getUserData();

            obj.update(body);
            if (obj.position.x <= -5)
                obj.state = Counter.STATE_REMOVE;
        } else
            body.setLinearVelocity(0, 0);
    }

    private void deleteObjects() {
        oWorldBox.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (!oWorldBox.isLocked()) {

                if (body.getUserData() instanceof Pipe) {
                    Pipe obj = (Pipe) body.getUserData();
                    if (obj.state == Pipe.STATE_REMOVE) {
                        arrPipes.removeValue(obj, true);
                        oWorldBox.destroyBody(body);
                    }
                } else if (body.getUserData() instanceof Counter) {
                    Counter obj = (Counter) body.getUserData();
                    if (obj.state == Counter.STATE_REMOVE) {
                        oWorldBox.destroyBody(body);
                    }
                }
            }
        }
    }

    class Collisions implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Bird)
                beginContactBird(a, b);
            else if (b.getBody().getUserData() instanceof Bird)
                beginContactBird(b, a);

        }

        private void beginContactBird(Fixture bird, Fixture otraCosa) {
            Object somethingElse = otraCosa.getBody().getUserData();

            if (somethingElse instanceof Counter) {
                Counter obj = (Counter) somethingElse;
                if (obj.state == Counter.STATE_NORMAL) {
                    obj.state = Counter.STATE_REMOVE;
                    score++;

                }
            } else {
                if (oBird.state == Bird.STATE_NORMAL) {
                    oBird.hurt();

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
