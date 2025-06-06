package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.game_objects.Item;
import com.nopalsoft.ninjarunner.game_objects.Mascot;
import com.nopalsoft.ninjarunner.game_objects.Missile;
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes4;
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes7;
import com.nopalsoft.ninjarunner.game_objects.Platform;
import com.nopalsoft.ninjarunner.game_objects.Player;
import com.nopalsoft.ninjarunner.game_objects.Wall;

public class ObjectManagerBox2d {

    GameWorld gameWorld;
    World worldBox;

    public ObjectManagerBox2d(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        worldBox = gameWorld.world;
    }

    public void createStandingPlayer(float x, float y, int playerType) {
        gameWorld.player = new Player(x, y, playerType);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.x = x;
        bodyDefinition.position.y = y;
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = worldBox.createBody(bodyDefinition);

        recreateFixtureStandingPlayer(body);

        body.setFixedRotation(true);
        body.setUserData(gameWorld.player);
        body.setBullet(true);
        body.setLinearVelocity(Player.RUN_SPEED, 0);
    }

    private void destroyAllFixturesFromBody(Body body) {
        for (Fixture fix : body.getFixtureList()) {
            body.destroyFixture(fix);
        }
        body.getFixtureList().clear();
    }

    public void recreateFixtureStandingPlayer(Body body) {
        destroyAllFixturesFromBody(body);// First I remove all the ones I have

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 10;
        fixtureDefinition.friction = 0;
        Fixture bodyFixture = body.createFixture(fixtureDefinition);
        bodyFixture.setUserData("cuerpo");

        PolygonShape sensorPiesShape = new PolygonShape();
        sensorPiesShape.setAsBox(Player.WIDTH / 2.2f, .025f, new Vector2(0, -.51f), 0);
        fixtureDefinition.shape = sensorPiesShape;
        fixtureDefinition.density = 0;
        fixtureDefinition.restitution = 0f;
        fixtureDefinition.friction = 0;
        fixtureDefinition.isSensor = true;
        Fixture sensorPies = body.createFixture(fixtureDefinition);
        sensorPies.setUserData("pies");

        shape.dispose();
        sensorPiesShape.dispose();
    }

    public void recreateFixtureSlidingPlayer(Body body) {
        destroyAllFixturesFromBody(body);// First I remove all the ones I have

        PolygonShape shape = new PolygonShape();
        // By the time the cube is created, the smaller it is, the better, so that it remains in the correct position.
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT_SLIDE / 2f, new Vector2(0, -.25f), 0);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 10;
        fixture.friction = 0;
        Fixture bodyFixture = body.createFixture(fixture);
        bodyFixture.setUserData("cuerpo");

        PolygonShape sensorPiesShape = new PolygonShape();
        sensorPiesShape.setAsBox(Player.WIDTH / 2.2f, .025f, new Vector2(0, -.51f), 0);
        fixture.shape = sensorPiesShape;
        fixture.density = 0;
        fixture.restitution = 0f;
        fixture.friction = 0;
        fixture.isSensor = true;
        Fixture sensorPies = body.createFixture(fixture);
        sensorPies.setUserData("pies");

        shape.dispose();
        sensorPiesShape.dispose();
    }

    public void createMascot(float x, float y) {
        gameWorld.mascot = new Mascot(x, y, Settings.selectedMascot);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(x, y);
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = worldBox.createBody(bodyDefinition);

        CircleShape shape = new CircleShape();
        shape.setRadius(Mascot.RADIUS);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);
        body.setUserData(gameWorld.mascot);

        shape.dispose();
    }

    public float createItem(Class<? extends Item> itemClass, float x, float y) {
        Item obj = Pools.obtain(itemClass);
        x += obj.WIDTH / 2f;

        obj.init(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(obj.position.x, obj.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = worldBox.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(obj.WIDTH / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);
        body.setUserData(obj);
        gameWorld.arrayItem.add(obj);

        shape.dispose();

        return x + obj.WIDTH / 2f;
    }

    /**
     * Returns the position of the right edge of the box in X
     */
    public float createBox4(float x, float y) {
        ObstacleBoxes4 obj = Pools.obtain(ObstacleBoxes4.class);

        x += ObstacleBoxes4.DRAW_WIDTH / 2f;

        obj.init(x, y);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(x, y);
        bodyDefinition.type = BodyType.StaticBody;

        Body body = worldBox.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.35f, .19f, new Vector2(0, -.19f), 0);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;
        body.createFixture(fixtureDefinition);

        shape.setAsBox(.18f, .19f, new Vector2(0, .19f), 0);
        fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;
        body.createFixture(fixtureDefinition);

        body.setUserData(obj);
        gameWorld.arrayObstacle.add(obj);

        shape.dispose();

        return x + ObstacleBoxes4.DRAW_WIDTH / 2f;
    }

    public float createBox7(float x, float y) {
        ObstacleBoxes7 obj = Pools.obtain(ObstacleBoxes7.class);

        x += ObstacleBoxes7.DRAW_WIDTH / 2f;

        obj.init(x, y);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(x, y);
        bodyDefinition.type = BodyType.StaticBody;

        Body body = worldBox.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.35f, .38f, new Vector2(0, -.19f), 0);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;
        body.createFixture(fixtureDefinition);

        shape.setAsBox(.18f, .19f, new Vector2(0, .38f), 0);
        fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;
        body.createFixture(fixtureDefinition);

        body.setUserData(obj);
        gameWorld.arrayObstacle.add(obj);

        shape.dispose();

        return x + ObstacleBoxes7.DRAW_WIDTH / 2f;
    }

    /**
     * @param x        lower left position
     * @param y        lower left position
     * @param numberOfPlatforms number of platforms glued
     */
    public float createPlatforms(float x, float y, int numberOfPlatforms) {

        float yCenter = Platform.HEIGHT / 2f + y;

        float xInicio = x;
        Platform oPlat = null;
        for (int i = 0; i < numberOfPlatforms; i++) {
            oPlat = Pools.obtain(Platform.class);
            x += Platform.WIDTH / 2f;
            oPlat.init(x, yCenter);
            gameWorld.arrayPlatform.add(oPlat);
            // I subtract the -.01 so that it is one pixel to the left and the line does not appear when two platforms are stuck together
            x += Platform.WIDTH / 2f - .01f;
        }

        xInicio += Platform.WIDTH / 2f * numberOfPlatforms - (.005f * numberOfPlatforms);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(xInicio, yCenter);
        bodyDefinition.type = BodyType.StaticBody;

        Body body = worldBox.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Platform.WIDTH / 2f * numberOfPlatforms - (.005f * numberOfPlatforms), Platform.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.friction = 0;

        body.createFixture(fixtureDefinition);
        body.setUserData(oPlat);

        shape.dispose();

        return xInicio + Platform.WIDTH * numberOfPlatforms / 2f;
    }

    public float createWall(float x, float y) {
        Wall wall = Pools.obtain(Wall.class);

        x += Wall.WIDTH / 2f;
        wall.init(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(wall.position.x, wall.position.y);
        bd.type = BodyType.StaticBody;

        Body body = worldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Wall.WIDTH / 2f, Wall.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);
        body.setUserData(wall);
        gameWorld.arrayWall.add(wall);

        shape.dispose();

        return x + Wall.WIDTH / 2f;
    }

    public void createMissile(float x, float y) {
        Missile obj = Pools.obtain(Missile.class);
        obj.init(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(obj.position.x, obj.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = worldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Missile.WIDTH / 2f, Missile.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.isSensor = true;

        body.createFixture(fixtureDefinition);
        body.setUserData(obj);
        body.setLinearVelocity(Missile.SPEED_X, 0);
        gameWorld.arrayMissile.add(obj);

        shape.dispose();
    }
}
