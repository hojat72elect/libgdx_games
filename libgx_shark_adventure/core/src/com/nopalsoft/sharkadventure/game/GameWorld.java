package com.nopalsoft.sharkadventure.game;

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
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.sharkadventure.Achievements;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.Settings;
import com.nopalsoft.sharkadventure.objects.Barrel;
import com.nopalsoft.sharkadventure.objects.Blast;
import com.nopalsoft.sharkadventure.objects.Chain;
import com.nopalsoft.sharkadventure.objects.Items;
import com.nopalsoft.sharkadventure.objects.Mine;
import com.nopalsoft.sharkadventure.objects.Shark;
import com.nopalsoft.sharkadventure.objects.Submarine;
import com.nopalsoft.sharkadventure.objects.Torpedo;
import com.nopalsoft.sharkadventure.screens.Screens;

public class GameWorld {
    static final int STATE_RUNNING = 0;
    static final int STATE_GAME_OVER = 1;
    public int state;

    float TIME_TO_GAMEOVER = 2f;
    float timeToGameOver;

    static final float TIME_TO_SPAWN_BARREL = 5;
    float timeToSpawnBarrel;

    static final float TIME_TO_SPAWN_MINE = 5;
    float timeToSpawnMine;

    static final float TIME_TO_SPAWN_CHAIN_MINE = 7;
    float timeToSpawnChainMine;

    static final float TIME_TO_SPAWN_SUBMARINE = 15;
    float timeToSpawnSubmarine;

    static final float TIME_TO_SPAWN_ITEMS = 10;
    float timeToSpawnItems;

    World worldPhysics;
    Shark oShark;

    Array<Barrel> arrayBarrels;
    Array<Body> arrayBodies;
    Array<Mine> arrayMines;
    Array<Chain> arrayChains;
    Array<Blast> arrayBlasts;
    Array<Torpedo> arrayTorpedoes;
    Array<Submarine> arraySubmarines;
    Array<Items> arrayItems;

    double score;

    int destroyedSubmarines;

    public GameWorld() {
        worldPhysics = new World(new Vector2(0, -4f), true);
        worldPhysics.setContactListener(new CollisionHandler());

        state = STATE_RUNNING;
        timeToGameOver = 0;
        score = 0;
        destroyedSubmarines = 0;

        arrayBodies = new Array<>();
        arrayBarrels = new Array<>();
        arrayMines = new Array<>();
        arrayChains = new Array<>();
        arrayBlasts = new Array<>();
        arrayTorpedoes = new Array<>();
        arraySubmarines = new Array<>();
        arrayItems = new Array<>();
        oShark = new Shark(3.5f, 2f);

        timeToSpawnBarrel = 0;
        createMineChain();
        createWalls();
        createPlayer(false);
    }

    private void createWalls() {
        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.type = BodyType.StaticBody;

        Body body = worldPhysics.createBody(bodyDefinition);

        EdgeShape shape = new EdgeShape();

        // Down
        shape.set(0, 0, Screens.WORLD_WIDTH, 0);
        body.createFixture(shape, 0);

        // Right
        shape.set(Screens.WORLD_WIDTH + .5f, 0, Screens.WORLD_WIDTH + .5f, Screens.WORLD_HEIGHT);
        body.createFixture(shape, 0);

        // Up
        shape.set(0, Screens.WORLD_HEIGHT + .5f, Screens.WORLD_WIDTH, Screens.WORLD_HEIGHT + .5f);
        body.createFixture(shape, 0);

        // Left
        shape.set(-.5f, 0, -.5f, Screens.WORLD_HEIGHT);
        body.createFixture(shape, 0);

        for (Fixture fix : body.getFixtureList()) {
            fix.setFriction(0);
            Filter filterData = new Filter();
            filterData.groupIndex = -1;
            fix.setFilterData(filterData);
        }

        body.setUserData("piso");

        shape.dispose();
    }

    private void createPlayer(boolean isFacingLeft) {

        BodyDef bd = new BodyDef();
        bd.position.set(oShark.position.x, oShark.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = worldPhysics.createBody(bd);
        PolygonShape shape = new PolygonShape();

        if (isFacingLeft) {
            shape.set(new float[]{.05f, .34f, -.12f, .18f, .13f, .19f, .18f, .37f});
            body.createFixture(shape, 0);

            shape.set(new float[]{-.12f, .18f, -.40f, .09f, -.40f, -.18f, -.25f, -.37f, .29f, -.39f, .36f, -.19f, .27f, -.03f, .13f, .19f});
            body.createFixture(shape, 0);

            shape.set(new float[]{.59f, .12f, .43f, -.06f, .36f, -.19f, .52f, -.33f});
            body.createFixture(shape, 0);

            shape.set(new float[]{-.40f, -.18f, -.40f, .09f, -.58f, -.05f, -.59f, -.12f});
            body.createFixture(shape, 0);

            shape.set(new float[]{.36f, -.19f, .29f, -.39f, .33f, -.34f});
            body.createFixture(shape, 0);

            shape.set(new float[]{.36f, -.19f, .43f, -.06f, .27f, -.03f});
            body.createFixture(shape, 0);
        } else {
            shape.set(new float[]{-.13f, .19f, .12f, .18f, -.05f, .34f, -.18f, .37f});
            body.createFixture(shape, 0);

            shape.set(new float[]{-.27f, -.03f, -.36f, -.19f, -.29f, -.39f, .25f, -.37f, .40f, -.18f, .40f, .09f, .12f, .18f, -.13f, .19f});
            body.createFixture(shape, 0);

            shape.set(new float[]{-.36f, -.19f, -.43f, -.06f, -.59f, .12f, -.52f, -.33f});
            body.createFixture(shape, 0);

            shape.set(new float[]{.58f, -.05f, .40f, .09f, .40f, -.18f, .59f, -.12f});
            body.createFixture(shape, 0);

            shape.set(new float[]{-.29f, -.39f, -.36f, -.19f, -.33f, -.34f});
            body.createFixture(shape, 0);

            shape.set(new float[]{-.43f, -.06f, -.36f, -.19f, -.27f, -.03f});
            body.createFixture(shape, 0);
        }

        body.setUserData(oShark);
        body.setFixedRotation(true);
        body.setGravityScale(.45f);

        shape.dispose();
    }

    private void createBarrel(float x, float y) {
        Barrel obj = Pools.obtain(Barrel.class);
        obj.init(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(obj.position.x, obj.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = worldPhysics.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Barrel.WIDTH / 2f, Barrel.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);
        body.setUserData(obj);
        body.setFixedRotation(true);
        body.setGravityScale(.15f);
        body.setAngularVelocity(MathUtils.degRad * MathUtils.random(-50, 50));

        arrayBarrels.add(obj);
        shape.dispose();
    }

    private void createItem() {
        Items obj = Pools.obtain(Items.class);
        obj.init(Screens.WORLD_WIDTH + 1, MathUtils.random(Screens.WORLD_HEIGHT));

        BodyDef bd = new BodyDef();
        bd.position.set(obj.position.x, obj.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = worldPhysics.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Items.WIDTH / 2f, Items.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);
        body.setUserData(obj);
        body.setFixedRotation(true);
        body.setLinearVelocity(Items.VELOCIDAD_X, 0);

        arrayItems.add(obj);
        shape.dispose();
    }

    private void createBlast() {
        float speedX = Blast.SPEED_X;
        float x = oShark.position.x + .3f;

        if (oShark.isFacingLeft) {
            speedX = -Blast.SPEED_X;
            x = oShark.position.x - .3f;
        }
        Blast obj = Pools.obtain(Blast.class);

        obj.init(x, oShark.position.y - .15f);

        BodyDef bd = new BodyDef();
        bd.position.set(obj.position.x, obj.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = worldPhysics.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Blast.WIDTH / 2f, Blast.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.setBullet(true);
        body.createFixture(fixtureDefinition);
        body.setUserData(obj);
        body.setFixedRotation(true);
        body.setLinearVelocity(speedX, 0);

        arrayBlasts.add(obj);
        shape.dispose();
    }

    private void createTorpedo(float x, float y, boolean goLeft) {
        float velX = Torpedo.SPEED_X;
        if (goLeft) {
            velX = -Torpedo.SPEED_X;
        }
        Torpedo obj = Pools.obtain(Torpedo.class);
        obj.init(x, y, goLeft);

        BodyDef bd = new BodyDef();
        bd.position.set(obj.position.x, obj.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = worldPhysics.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Blast.WIDTH / 2f, Blast.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);
        body.setUserData(obj);
        body.setGravityScale(0);
        body.setFixedRotation(true);
        body.setLinearVelocity(velX, 0);

        arrayTorpedoes.add(obj);
        shape.dispose();
    }

    private void createMine(float x, float y) {
        Mine obj = Pools.obtain(Mine.class);
        obj.init(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(obj.position.x, obj.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = worldPhysics.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(Mine.WIDTH / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);
        body.setUserData(obj);
        body.setFixedRotation(true);
        body.setGravityScale(0);
        body.setLinearVelocity(Mine.SPEED_X, 0);

        arrayMines.add(obj);
        shape.dispose();
    }

    private void createSubmarine() {
        Submarine obj = Pools.obtain(Submarine.class);
        float x, y, xTarget, yTarget;
        switch (MathUtils.random(1, 4)) {
            case 1:
                x = -1;
                y = -1;
                xTarget = Screens.WORLD_WIDTH + 6;
                yTarget = Screens.WORLD_HEIGHT + 6;
                break;
            case 2:
                x = -1;
                y = Screens.WORLD_HEIGHT + 1;
                xTarget = Screens.WORLD_WIDTH + 6;
                yTarget = -6;
                break;
            case 3:
                x = Screens.WORLD_WIDTH + 1;
                y = Screens.WORLD_HEIGHT + 1;
                xTarget = -6;
                yTarget = -6;
                break;
            case 4:
            default:
                x = Screens.WORLD_WIDTH + 1;
                y = -1;
                xTarget = -6;
                yTarget = Screens.WORLD_HEIGHT + 6;
                break;
        }

        obj.init(x, y, xTarget, yTarget);

        BodyDef bd = new BodyDef();
        bd.position.set(obj.position.x, obj.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = worldPhysics.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Submarine.WIDTH / 2f, Submarine.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);
        body.setUserData(obj);
        body.setFixedRotation(true);
        body.setGravityScale(0);
        arraySubmarines.add(obj);
        shape.dispose();
    }

    private void createMineChain() {
        float x = 10;
        Mine obj = Pools.obtain(Mine.class);
        obj.init(x, 1);
        obj.type = Mine.TYPE_GRAY;

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(obj.position.x, obj.position.y);
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = worldPhysics.createBody(bodyDefinition);

        CircleShape shape = new CircleShape();
        shape.setRadius(Mine.WIDTH / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;
        fixtureDefinition.density = 1;

        body.createFixture(fixtureDefinition);
        body.setUserData(obj);
        body.setFixedRotation(true);
        body.setGravityScale(-3.5f);

        arrayMines.add(obj);
        shape.dispose();

        PolygonShape chainShape = new PolygonShape();
        chainShape.setAsBox(Chain.WIDTH / 2f, Chain.HEIGHT / 2f);

        fixtureDefinition.isSensor = false;
        fixtureDefinition.shape = chainShape;
        fixtureDefinition.filter.groupIndex = -1;

        int numberOfLinks = MathUtils.random(5, 15);
        Body link = null;
        for (int i = 0; i < numberOfLinks; i++) {
            Chain objChain = Pools.obtain(Chain.class);
            objChain.init(x, Chain.HEIGHT * i);
            bodyDefinition.position.set(objChain.position.x, objChain.position.y);
            if (i == 0) {
                objChain.init(x, -.12f);// Makes the kinematic body appear a little lower so as not to collide with it.
                bodyDefinition.position.set(objChain.position.x, objChain.position.y);
                bodyDefinition.type = BodyType.KinematicBody;
                link = worldPhysics.createBody(bodyDefinition);
                link.createFixture(fixtureDefinition);
                link.setLinearVelocity(Chain.VELOCIDAD_X, 0);
            } else {
                bodyDefinition.type = BodyType.DynamicBody;
                Body newLink = worldPhysics.createBody(bodyDefinition);
                newLink.createFixture(fixtureDefinition);
                createRevoluteJoint(link, newLink, -Chain.HEIGHT / 2f);
                link = newLink;
            }
            arrayChains.add(objChain);
            link.setUserData(objChain);
        }

        createRevoluteJoint(link, body, -Mine.HEIGHT / 2f);
    }

    private void createRevoluteJoint(Body bodyA, Body bodyB, float anchorBY) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.localAnchorA.set((float) 0, (float) 0.105);
        jointDef.localAnchorB.set((float) 0, anchorBY);
        jointDef.bodyA = bodyA;
        jointDef.bodyB = bodyB;
        worldPhysics.createJoint(jointDef);
    }

    public void update(float delta, float accelX, boolean didSwimUp, boolean didFire) {
        worldPhysics.step(delta, 8, 4);

        removeGameObjects();

        timeToSpawnBarrel += delta;
        if (timeToSpawnBarrel >= TIME_TO_SPAWN_BARREL) {
            timeToSpawnBarrel -= TIME_TO_SPAWN_BARREL;

            if (MathUtils.randomBoolean()) {
                for (int i = 0; i < 6; i++) {
                    createBarrel(MathUtils.random(Screens.WORLD_WIDTH), MathUtils.random(5.5f, 8.5f));
                }
            }
        }

        timeToSpawnMine += delta;
        if (timeToSpawnMine >= TIME_TO_SPAWN_MINE) {
            timeToSpawnMine -= TIME_TO_SPAWN_MINE;
            for (int i = 0; i < 5; i++) {
                if (MathUtils.randomBoolean())
                    createMine(MathUtils.random(9, 10f), MathUtils.random(Screens.WORLD_HEIGHT));
            }
        }

        if (arrayMines.size == 0)
            createMine(9, MathUtils.random(Screens.WORLD_HEIGHT));

        timeToSpawnChainMine += delta;
        if (timeToSpawnChainMine >= TIME_TO_SPAWN_CHAIN_MINE) {
            timeToSpawnChainMine -= TIME_TO_SPAWN_CHAIN_MINE;
            if (MathUtils.randomBoolean(.75f))
                createMineChain();
        }

        timeToSpawnSubmarine += delta;
        if (timeToSpawnSubmarine >= TIME_TO_SPAWN_SUBMARINE) {
            timeToSpawnSubmarine -= TIME_TO_SPAWN_SUBMARINE;
            if (MathUtils.randomBoolean(.65f)) {
                createSubmarine();
                if (Settings.isSoundOn)
                    Assets.sSonar.play();
            }
        }

        timeToSpawnItems += delta;
        if (timeToSpawnItems >= TIME_TO_SPAWN_ITEMS) {
            timeToSpawnItems -= TIME_TO_SPAWN_ITEMS;
            if (MathUtils.randomBoolean()) {
                createItem();
            }
        }

        worldPhysics.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (body.getUserData() instanceof Shark) {
                updatePlayer(body, delta, accelX, didSwimUp, didFire);
            } else if (body.getUserData() instanceof Barrel) {
                updateBarrel(body, delta);
            } else if (body.getUserData() instanceof Mine) {
                updateMine(body, delta);
            } else if (body.getUserData() instanceof Chain) {
                updateChain(body);
            } else if (body.getUserData() instanceof Blast) {
                updateBlast(body, delta);
            } else if (body.getUserData() instanceof Torpedo) {
                updateTorpedo(body, delta);
            } else if (body.getUserData() instanceof Submarine) {
                updateSubmarine(body, delta);
            } else if (body.getUserData() instanceof Items) {
                updateItems(body, delta);
            }
        }

        if (oShark.state == Shark.STATE_DEAD) {
            timeToGameOver += delta;
            if (timeToGameOver >= TIME_TO_GAMEOVER) {
                state = STATE_GAME_OVER;
            }
        } else {
            score += delta * 15;
        }

        Achievements.distance((long) score, oShark.didGetHurtOnce);
    }

    private void removeGameObjects() {
        for (Body body : arrayBodies) {
            if (!worldPhysics.isLocked()) {
                if (body.getUserData() instanceof Barrel) {
                    Barrel obj = (Barrel) body.getUserData();
                    if (obj.state == Barrel.STATE_REMOVE) {
                        arrayBarrels.removeValue(obj, true);
                        Pools.free(obj);
                        worldPhysics.destroyBody(body);
                    }
                } else if (body.getUserData() instanceof Mine) {
                    Mine obj = (Mine) body.getUserData();
                    if (obj.state == Mine.STATE_REMOVE) {
                        arrayMines.removeValue(obj, true);
                        Pools.free(obj);
                        worldPhysics.destroyBody(body);
                    }
                } else if (body.getUserData() instanceof Chain) {
                    Chain obj = (Chain) body.getUserData();
                    if (obj.state == Chain.STATE_REMOVE) {
                        arrayChains.removeValue(obj, true);
                        Pools.free(obj);
                        worldPhysics.destroyBody(body);
                    }
                } else if (body.getUserData() instanceof Blast) {
                    Blast obj = (Blast) body.getUserData();
                    if (obj.state == Blast.STATE_REMOVE) {
                        arrayBlasts.removeValue(obj, true);
                        Pools.free(obj);
                        worldPhysics.destroyBody(body);
                    }
                } else if (body.getUserData() instanceof Torpedo) {
                    Torpedo obj = (Torpedo) body.getUserData();
                    if (obj.state == Torpedo.STATE_REMOVE) {
                        arrayTorpedoes.removeValue(obj, true);
                        Pools.free(obj);
                        worldPhysics.destroyBody(body);
                    }
                } else if (body.getUserData() instanceof Submarine) {
                    Submarine obj = (Submarine) body.getUserData();
                    if (obj.state == Submarine.STATE_REMOVE) {
                        arraySubmarines.removeValue(obj, true);
                        Pools.free(obj);
                        worldPhysics.destroyBody(body);
                    }
                } else if (body.getUserData() instanceof Items) {
                    Items obj = (Items) body.getUserData();
                    if (obj.state == Items.STATE_REMOVE) {
                        arrayItems.removeValue(obj, true);
                        Pools.free(obj);
                        worldPhysics.destroyBody(body);
                    }
                }
            }
        }
    }

    private void updatePlayer(Body body, float delta, float accelX, boolean didSwimUp, boolean didFire) {
        // If you change position I have to do the body again.
        if (oShark.didFlipX) {
            oShark.didFlipX = false;
            worldPhysics.destroyBody(body);
            createPlayer(oShark.isFacingLeft);
        }

        if (didFire && oShark.state == Shark.STATE_NORMAL) {
            if (oShark.energy > 0) {
                createBlast();
                if (Settings.isSoundOn) {
                    Assets.sBlast.play();
                }
            }
            oShark.fire();
        }

        oShark.update(body, delta, accelX, didSwimUp);
    }

    private void updateBarrel(Body body, float delta) {
        Barrel obj = (Barrel) body.getUserData();
        obj.update(body, delta);
    }

    private void updateMine(Body body, float delta) {
        Mine obj = (Mine) body.getUserData();
        obj.update(body, delta);
    }

    private void updateChain(Body body) {
        Chain obj = (Chain) body.getUserData();
        obj.update(body);
    }

    private void updateBlast(Body body, float delta) {
        Blast obj = (Blast) body.getUserData();
        obj.update(body, delta);
    }

    private void updateTorpedo(Body body, float delta) {
        Torpedo obj = (Torpedo) body.getUserData();
        obj.update(body, delta);
    }

    private void updateSubmarine(Body body, float delta) {
        Submarine obj = (Submarine) body.getUserData();
        obj.update(body, delta);

        if (obj.didFire) {
            obj.didFire = false;

            createTorpedo(obj.position.x, obj.position.y, !(obj.velocity.x > 0));
        }
    }

    private void updateItems(Body body, float delta) {
        Items obj = (Items) body.getUserData();
        obj.update(body, delta);
    }

    class CollisionHandler implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Shark) {
                collisionCheckOnShark(b);
            } else if (b.getBody().getUserData() instanceof Shark) {
                collisionCheckOnShark(a);
            } else if (a.getBody().getUserData() instanceof Blast) {
                beginContactBlast(a, b);
            } else if (b.getBody().getUserData() instanceof Blast) {
                beginContactBlast(b, a);
            } else {
                startCollisionCheck(a, b);
            }
        }

        private void beginContactBlast(Fixture blastFixture, Fixture otherFixture) {
            Object otherObject = otherFixture.getBody().getUserData();
            Blast blast = (Blast) blastFixture.getBody().getUserData();

            if (otherObject instanceof Barrel) {
                Barrel obj = (Barrel) otherObject;
                if (obj.state == Barrel.STATE_NORMAL) {
                    obj.hit();
                    blast.hit();
                }
            } else if (otherObject instanceof Mine) {
                Mine obj = (Mine) otherObject;
                if (obj.state == Mine.STATE_NORMAL) {
                    obj.hit();
                    blast.hit();
                }
            } else if (otherObject instanceof Chain) {
                Chain obj = (Chain) otherObject;
                if (obj.state == Chain.STATE_NORMAL) {
                    obj.hit();
                    blast.hit();
                }
            } else if (otherObject instanceof Submarine) {
                Submarine obj = (Submarine) otherObject;
                if (obj.state == Submarine.STATE_NORMAL) {
                    obj.hit();
                    blast.hit();

                    if (obj.state == Submarine.STATE_EXPLODE) {
                        destroyedSubmarines++;
                        Achievements.unlockKilledSubmarines();
                    }
                }
            }
        }

        private void collisionCheckOnShark(Fixture otherFixture) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject instanceof Barrel) {
                Barrel obj = (Barrel) otherObject;
                if (obj.state == Barrel.STATE_NORMAL) {
                    obj.hit();
                    oShark.hit();
                }
            } else if (otherObject instanceof Mine) {
                Mine obj = (Mine) otherObject;
                if (obj.state == Mine.STATE_NORMAL) {
                    obj.hit();
                    oShark.hit();
                }
            } else if (otherObject instanceof Torpedo) {
                Torpedo obj = (Torpedo) otherObject;
                if (obj.state == Torpedo.STATE_NORMAL) {
                    obj.hit();
                    oShark.hit();
                    oShark.hit();
                    oShark.hit();
                }
            } else if (otherObject instanceof Items) {
                Items obj = (Items) otherObject;
                if (obj.state == Items.STATE_NORMAL) {
                    if (obj.tipo == Items.TIPO_CARNE) {
                        oShark.energy += 15;
                    } else {
                        oShark.life += 1;
                    }
                    obj.hit();
                }
            }
        }

        public void startCollisionCheck(Fixture fixtureA, Fixture fixtureB) {
            Object objA = fixtureA.getBody().getUserData();
            Object objB = fixtureB.getBody().getUserData();

            if (objA instanceof Barrel && objB instanceof Mine) {
                ((Barrel) objA).hit();
                ((Mine) objB).hit();
            } else if (objA instanceof Mine && objB instanceof Barrel) {
                ((Barrel) objB).hit();
                ((Mine) objA).hit();
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
