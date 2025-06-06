package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.game_objects.Item;
import com.nopalsoft.ninjarunner.game_objects.ItemCandyBean;
import com.nopalsoft.ninjarunner.game_objects.ItemCandyCorn;
import com.nopalsoft.ninjarunner.game_objects.ItemCandyJelly;
import com.nopalsoft.ninjarunner.game_objects.ItemCoin;
import com.nopalsoft.ninjarunner.game_objects.ItemEnergy;
import com.nopalsoft.ninjarunner.game_objects.ItemHeart;
import com.nopalsoft.ninjarunner.game_objects.ItemMagnet;
import com.nopalsoft.ninjarunner.game_objects.Mascot;
import com.nopalsoft.ninjarunner.game_objects.Missile;
import com.nopalsoft.ninjarunner.game_objects.Obstacle;
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes4;
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes7;
import com.nopalsoft.ninjarunner.game_objects.Platform;
import com.nopalsoft.ninjarunner.game_objects.Player;
import com.nopalsoft.ninjarunner.game_objects.Wall;

public class GameWorld {
    static final int STATE_GAMEOVER = 1;
    public int state;

    float timeToSpawnMissile;

    ObjectManagerBox2d physicsManager;
    public World world;

    public Player player;
    public Mascot mascot;

    Array<Body> arrayBody;
    Array<Platform> arrayPlatform;
    Array<Wall> arrayWall;
    Array<Item> arrayItem;
    Array<Obstacle> arrayObstacle;
    Array<Missile> arrayMissile;

    /**
     * Variable that indicates how far the world has been created.
     */
    float worldCreatedUpToX;

    int coinsTaken;
    long scores;

    public GameWorld() {
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(new CollisionHandler());

        physicsManager = new ObjectManagerBox2d(this);

        arrayBody = new Array<>();
        arrayPlatform = new Array<>();
        arrayItem = new Array<>();
        arrayWall = new Array<>();
        arrayObstacle = new Array<>();
        arrayMissile = new Array<>();

        timeToSpawnMissile = 0;

        physicsManager.createStandingPlayer(2f, 1f, Settings.selectedSkin);
        physicsManager.createMascot(player.position.x - 1, player.position.y + .75f);

        worldCreatedUpToX = physicsManager.createPlatforms(0, 0, 3);

        createNextPart();
    }

    private void createNextPart() {
        float x = worldCreatedUpToX;

        while (worldCreatedUpToX < (x + 1)) {

            // First I create the platform
            int platformWidth = 25;
            float separation = MathUtils.random(1f, 3f);
            float y = MathUtils.random(0, 1.5f);

            worldCreatedUpToX = physicsManager.createPlatforms(worldCreatedUpToX + separation, y, platformWidth);

            float xAuxiliary = x + separation;

            while (xAuxiliary < worldCreatedUpToX - 2) {
                if (xAuxiliary < x + separation + 2)
                    xAuxiliary = addRandomItems(xAuxiliary, y);

                if (MathUtils.randomBoolean(.1f)) {
                    xAuxiliary = physicsManager.createBox4(xAuxiliary, y + .8f);
                    xAuxiliary = addRandomItems(xAuxiliary, y);
                } else if (MathUtils.randomBoolean(.1f)) {
                    xAuxiliary = physicsManager.createBox7(xAuxiliary, y + 1f);
                    xAuxiliary = addRandomItems(xAuxiliary, y);
                } else if (MathUtils.randomBoolean(.1f)) {
                    xAuxiliary = physicsManager.createWall(xAuxiliary, y + 3.17f);
                    xAuxiliary = addRandomItems(xAuxiliary, y);
                } else {
                    xAuxiliary = addRandomItems(xAuxiliary, y);
                }
            }
        }
    }

    private float addRandomItems(float xAux, float y) {

        if (MathUtils.randomBoolean(.3f)) {
            for (int i = 0; i < 5; i++) {
                physicsManager.createItem(ItemCoin.class, xAux, y + 1.5f);
                xAux = physicsManager.createItem(ItemCoin.class, xAux, y + 1f);
            }
        } else if (MathUtils.randomBoolean(.5f)) {

            for (int i = 0; i < 5; i++) {
                physicsManager.createItem(ItemCandyBean.class, xAux, y + .8f);
                physicsManager.createItem(ItemCandyBean.class, xAux, y + 1.1f);
                xAux = physicsManager.createItem(ItemCandyJelly.class, xAux, y + 1.5f);
            }
        } else if (MathUtils.randomBoolean(.5f)) {

            for (int i = 0; i < 5; i++) {
                physicsManager.createItem(ItemCandyCorn.class, xAux, y + .8f);
                physicsManager.createItem(ItemCandyCorn.class, xAux, y + 1.1f);
                xAux = physicsManager.createItem(ItemCandyCorn.class, xAux, y + 1.5f);
            }
        }

        if (MathUtils.randomBoolean(.025f)) {

            xAux = physicsManager.createItem(ItemHeart.class, xAux, y + 1.5f);
            xAux = physicsManager.createItem(ItemEnergy.class, xAux, y + 1.5f);
        } else if (MathUtils.randomBoolean(.025f)) {

            xAux = physicsManager.createItem(ItemMagnet.class, xAux, y + 1.5f);
        }

        return xAux;
    }

    public void update(float delta, boolean didJump, boolean dash, boolean didSlide) {
        world.step(delta, 8, 4);

        world.getBodies(arrayBody);
        removeObjects();
        world.getBodies(arrayBody);

        for (Body body : arrayBody) {
            if (body.getUserData() instanceof Player) {
                updatePlayer(delta, body, didJump, dash, didSlide);
            } else if (body.getUserData() instanceof Mascot) {
                updateMascot(delta, body);
            } else if (body.getUserData() instanceof Platform) {
                updatePlatform(body);
            } else if (body.getUserData() instanceof Wall) {
                updatePared(body);
            } else if (body.getUserData() instanceof Item) {
                updateItem(delta, body);
            } else if (body.getUserData() instanceof Obstacle) {
                updateObstacles(delta, body);
            } else if (body.getUserData() instanceof Missile) {
                updateMissile(delta, body);
            }
        }

        if (player.position.x > worldCreatedUpToX - 5)
            createNextPart();

        if (player.state == Player.STATE_DEAD && player.stateTime >= Player.DURATION_DEAD)
            state = STATE_GAMEOVER;

        timeToSpawnMissile += delta;
        float TIME_TO_SPAWN_MISSILE = 15;
        if (timeToSpawnMissile >= TIME_TO_SPAWN_MISSILE) {
            timeToSpawnMissile -= TIME_TO_SPAWN_MISSILE;

            physicsManager.createMissile(player.position.x + 10, player.position.y);
        }
    }

    private void removeObjects() {
        for (Body body : arrayBody) {
            if (body.getUserData() instanceof Platform obj) {
                if (obj.state == Platform.STATE_DESTROY) {
                    arrayPlatform.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            } else if (body.getUserData() instanceof Wall obj) {
                if (obj.state == Wall.STATE_DESTROY) {
                    arrayWall.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            } else if (body.getUserData() instanceof Item obj) {
                if (obj.state == Item.STATE_DESTROY && obj.stateTime >= Item.DURATION_PICK) {
                    arrayItem.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            } else if (body.getUserData() instanceof ObstacleBoxes4 obj) {

                if (obj.state == ObstacleBoxes4.STATE_DESTROY && obj.effect.isComplete()) {
                    obj.effect.free();
                    arrayObstacle.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            } else if (body.getUserData() instanceof ObstacleBoxes7 obj) {

                if (obj.state == ObstacleBoxes7.STATE_DESTROY && obj.effect.isComplete()) {
                    obj.effect.free();
                    arrayObstacle.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            } else if (body.getUserData() instanceof Missile obj) {
                if (obj.state == Missile.STATE_DESTROY) {
                    arrayMissile.removeValue(obj, true);
                    Pools.free(obj);
                    world.destroyBody(body);
                }
            }
        }
    }

    boolean bodyIsSLide; // Indicates whether the body you have right now is sliding.
    boolean recreateFixture = false;

    private void updatePlayer(float delta, Body body, boolean didJump, boolean dash, boolean didSlide) {
        player.update(delta, body, didJump, false, dash, didSlide);

        if (player.position.y < -1) {
            player.die();
        } else if (player.isSlide && !bodyIsSLide) {
            recreateFixture = true;
            bodyIsSLide = true;
            physicsManager.recreateFixtureSlidingPlayer(body);
        } else if (!player.isSlide && bodyIsSLide) {
            recreateFixture = true;
            bodyIsSLide = false;
            physicsManager.recreateFixtureStandingPlayer(body);
        }
    }

    private void updateMascot(float delta, Body body) {

        float targetPositionX = player.position.x - .75f;
        float targetPositionY = player.position.y + .25f;

        if (mascot.mascotType == Mascot.MascotType.BOMB) {
            Missile oMissile = getClosestMissile();
            if (oMissile != null && oMissile.distanceFromPlayer < 5f && oMissile.state == Missile.STATE_NORMAL) {
                targetPositionX = oMissile.position.x;
                targetPositionY = oMissile.position.y;
            }
        } else {
            if (player.isDash) {
                targetPositionX = player.position.x + 4.25f;
                targetPositionY = player.position.y;
            }
        }

        mascot.update(body, delta, targetPositionX, targetPositionY);
    }

    private void updatePlatform(Body body) {
        Platform obj = (Platform) body.getUserData();

        if (obj.position.x < player.position.x - 3)
            obj.setDestroy();
    }

    private void updatePared(Body body) {
        Wall obj = (Wall) body.getUserData();

        if (obj.position.x < player.position.x - 3)
            obj.setDestroy();
    }

    private void updateItem(float delta, Body body) {
        Item obj = (Item) body.getUserData();
        obj.update(delta, body, player);

        if (obj.position.x < player.position.x - 3)
            obj.setPicked();
    }

    private void updateObstacles(float delta, Body body) {
        Obstacle obj = (Obstacle) body.getUserData();
        obj.update(delta);

        if (obj.position.x < player.position.x - 3)
            obj.setDestroy();
    }

    private void updateMissile(float delta, Body body) {
        Missile obj = (Missile) body.getUserData();
        obj.update(delta, body, player);

        if (obj.position.x < player.position.x - 3)
            obj.setDestroy();

        arrayMissile.sort();
    }

    /**
     * Returns the closest missile to the character who is in normal state.
     */
    private Missile getClosestMissile() {
        for (int i = 0; i < arrayMissile.size; i++) {
            if (arrayMissile.get(i).state == Missile.STATE_NORMAL)
                return arrayMissile.get(i);
        }
        return null;
    }

    class CollisionHandler implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Player)
                startCheckingCollisionsOfPlayer(a, b);
            else if (b.getBody().getUserData() instanceof Player)
                startCheckingCollisionsOfPlayer(b, a);

            if (a.getBody().getUserData() instanceof Mascot)
                startCheckingCollisionsOfMascot(b);
            else if (b.getBody().getUserData() instanceof Mascot)
                startCheckingCollisionsOfMascot(a);
        }

        private void startCheckingCollisionsOfPlayer(Fixture playerFixture, Fixture otherFixture) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject instanceof Platform) {
                if (playerFixture.getUserData().equals("pies")) {
                    if (recreateFixture)
                        recreateFixture = false;
                    else
                        player.touchFloor();
                }
            } else if (otherObject instanceof Item obj) {
                if (obj.state == Item.STATE_NORMAL) {
                    if (obj instanceof ItemCoin) {
                        coinsTaken++;
                        scores++;
                        Assets.playSound(Assets.coinSound, 1);
                    } else if (obj instanceof ItemMagnet) {
                        player.setPickUpMagnet();
                    } else if (obj instanceof ItemHeart) {
                        player.lives++;
                    } else if (obj instanceof ItemCandyJelly) {
                        Assets.playSound(Assets.candySound, 1);
                        scores += 2;
                    } else if (obj instanceof ItemCandyBean) {
                        Assets.playSound(Assets.candySound, 1);
                        scores += 5;
                    } else if (obj instanceof ItemCandyCorn) {
                        Assets.playSound(Assets.candySound, 1);
                        scores += 15;
                    }

                    obj.setPicked();
                }
            } else if (otherObject instanceof Wall obj) {
                if (obj.state == Wall.STATE_NORMAL) {
                    player.getDizzy();
                }
            } else if (otherObject instanceof Obstacle obj) {
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setDestroy();
                    player.getHurt();
                }
            } else if (otherObject instanceof Missile obj) {
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setHitTarget();
                    player.getDizzy();
                }
            }
        }

        public void startCheckingCollisionsOfMascot(Fixture otherFixture) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject instanceof Wall obj && player.isDash) {
                obj.setDestroy();
            } else if (otherObject instanceof Obstacle obj && player.isDash) {
                obj.setDestroy();
            } else if (otherObject instanceof ItemCoin obj) {
                if (obj.state == ItemCoin.STATE_NORMAL) {
                    obj.setPicked();
                    coinsTaken++;
                    Assets.playSound(Assets.coinSound, 1);
                }
            } else if (otherObject instanceof Missile obj) {
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setHitTarget();
                }
            }
        }

        @Override
        public void endContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a == null || b == null)
                return;

            if (a.getBody().getUserData() instanceof Player)
                stopCheckingCollisionsOfPlayer(a, b);
            else if (b.getBody().getUserData() instanceof Player)
                stopCheckingCollisionsOfPlayer(b, a);
        }

        private void stopCheckingCollisionsOfPlayer(Fixture playerFixture, Fixture otherFixture) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject instanceof Platform) {
                if (playerFixture.getUserData().equals("pies"))
                    player.endTouchFloor();
            }
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

            if (otherObject instanceof Platform obj) {

                float ponyY = playerFixture.getBody().getPosition().y - .30f;
                float pisY = obj.position.y + Platform.HEIGHT / 2f;

                if (ponyY < pisY)
                    contact.setEnabled(false);
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }
    }
}
