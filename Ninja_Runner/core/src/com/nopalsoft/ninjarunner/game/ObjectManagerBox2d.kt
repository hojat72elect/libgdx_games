package com.nopalsoft.ninjarunner.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Pools
import com.nopalsoft.ninjarunner.Settings
import com.nopalsoft.ninjarunner.game_objects.Item
import com.nopalsoft.ninjarunner.game_objects.Mascot
import com.nopalsoft.ninjarunner.game_objects.Missile
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes4
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes7
import com.nopalsoft.ninjarunner.game_objects.Platform
import com.nopalsoft.ninjarunner.game_objects.Player
import com.nopalsoft.ninjarunner.game_objects.Wall

class ObjectManagerBox2d(var gameWorld: GameWorld) {
    var worldBox: World = gameWorld.world

    fun createStandingPlayer(x: Float, y: Float, playerType: Int) {
        gameWorld.player = Player(x, y, playerType)

        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = x
        bodyDefinition.position.y = y
        bodyDefinition.type = BodyType.DynamicBody

        val body = worldBox.createBody(bodyDefinition)

        recreateFixtureStandingPlayer(body)

        body.isFixedRotation = true
        body.userData = gameWorld.player
        body.isBullet = true
        body.setLinearVelocity(Player.RUN_SPEED, 0f)
    }

    private fun destroyAllFixturesFromBody(body: Body) {
        for (fix in body.fixtureList) {
            body.destroyFixture(fix)
        }
        body.fixtureList.clear()
    }

    fun recreateFixtureStandingPlayer(body: Body) {
        destroyAllFixturesFromBody(body) // First I remove all the ones I have

        val shape = PolygonShape()
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.density = 10f
        fixtureDefinition.friction = 0f
        val bodyFixture = body.createFixture(fixtureDefinition)
        bodyFixture.setUserData("cuerpo")

        val sensorPiesShape = PolygonShape()
        sensorPiesShape.setAsBox(Player.WIDTH / 2.2f, .025f, Vector2(0f, -.51f), 0f)
        fixtureDefinition.shape = sensorPiesShape
        fixtureDefinition.density = 0f
        fixtureDefinition.restitution = 0f
        fixtureDefinition.friction = 0f
        fixtureDefinition.isSensor = true
        val sensorPies = body.createFixture(fixtureDefinition)
        sensorPies.setUserData("pies")

        shape.dispose()
        sensorPiesShape.dispose()
    }

    fun recreateFixtureSlidingPlayer(body: Body) {
        destroyAllFixturesFromBody(body) // First I remove all the ones I have

        val shape = PolygonShape()
        // By the time the cube is created, the smaller it is, the better, so that it remains in the correct position.
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT_SLIDE / 2f, Vector2(0f, -.25f), 0f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 10f
        fixture.friction = 0f
        val bodyFixture = body.createFixture(fixture)
        bodyFixture.setUserData("cuerpo")

        val sensorPiesShape = PolygonShape()
        sensorPiesShape.setAsBox(Player.WIDTH / 2.2f, .025f, Vector2(0f, -.51f), 0f)
        fixture.shape = sensorPiesShape
        fixture.density = 0f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true
        val sensorPies = body.createFixture(fixture)
        sensorPies.setUserData("pies")

        shape.dispose()
        sensorPiesShape.dispose()
    }

    fun createMascot(x: Float, y: Float) {
        gameWorld.mascot = Mascot(x, y, Settings.selectedMascot)

        val bodyDefinition = BodyDef()
        bodyDefinition.position.set(x, y)
        bodyDefinition.type = BodyType.DynamicBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = CircleShape()
        shape.radius = Mascot.RADIUS

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)
        body.userData = gameWorld.mascot

        shape.dispose()
    }

    fun createItem(itemClass: Class<out Item>?, x: Float, y: Float): Float {
        var x = x
        val obj: Item = Pools.obtain(itemClass)
        x += obj.WIDTH / 2f

        obj.init(x, y)

        val bd = BodyDef()
        bd.position.set(obj.position.x, obj.position.y)
        bd.type = BodyType.KinematicBody

        val body = worldBox.createBody(bd)

        val shape = CircleShape()
        shape.radius = obj.WIDTH / 2f

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)
        body.userData = obj
        gameWorld.arrayItem.add(obj)

        shape.dispose()

        return x + obj.WIDTH / 2f
    }

    /**
     * Returns the position of the right edge of the box in X
     */
    fun createBox4(x: Float, y: Float): Float {
        var x = x
        val obj = Pools.obtain<ObstacleBoxes4>(ObstacleBoxes4::class.java)

        x += ObstacleBoxes4.DRAW_WIDTH / 2f

        obj.initialize(x, y)

        val bodyDefinition = BodyDef()
        bodyDefinition.position.set(x, y)
        bodyDefinition.type = BodyType.StaticBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(.35f, .19f, Vector2(0f, -.19f), 0f)

        var fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true
        body.createFixture(fixtureDefinition)

        shape.setAsBox(.18f, .19f, Vector2(0f, .19f), 0f)
        fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true
        body.createFixture(fixtureDefinition)

        body.userData = obj
        gameWorld.arrayObstacle.add(obj)

        shape.dispose()

        return x + ObstacleBoxes4.DRAW_WIDTH / 2f
    }

    fun createBox7(x: Float, y: Float): Float {
        var x = x
        val obj = Pools.obtain<ObstacleBoxes7>(ObstacleBoxes7::class.java)

        x += ObstacleBoxes7.DRAW_WIDTH / 2f

        obj.initialize(x, y)

        val bodyDefinition = BodyDef()
        bodyDefinition.position.set(x, y)
        bodyDefinition.type = BodyType.StaticBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(.35f, .38f, Vector2(0f, -.19f), 0f)

        var fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true
        body.createFixture(fixtureDefinition)

        shape.setAsBox(.18f, .19f, Vector2(0f, .38f), 0f)
        fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true
        body.createFixture(fixtureDefinition)

        body.userData = obj
        gameWorld.arrayObstacle.add(obj)

        shape.dispose()

        return x + ObstacleBoxes7.DRAW_WIDTH / 2f
    }

    /**
     * @param x        lower left position
     * @param y        lower left position
     * @param numberOfPlatforms number of platforms glued
     */
    fun createPlatforms(x: Float, y: Float, numberOfPlatforms: Int): Float {
        var x = x
        val yCenter = Platform.HEIGHT / 2f + y

        var xInicio = x
        var oPlat: Platform? = null
        for (i in 0..<numberOfPlatforms) {
            oPlat = Pools.obtain<Platform?>(Platform::class.java)
            x += Platform.WIDTH / 2f
            oPlat.initialize(x, yCenter)
            gameWorld.arrayPlatform.add(oPlat)
            // I subtract the -.01 so that it is one pixel to the left and the line does not appear when two platforms are stuck together
            x += Platform.WIDTH / 2f - .01f
        }

        xInicio += Platform.WIDTH / 2f * numberOfPlatforms - (.005f * numberOfPlatforms)

        val bodyDefinition = BodyDef()
        bodyDefinition.position.set(xInicio, yCenter)
        bodyDefinition.type = BodyType.StaticBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Platform.WIDTH / 2f * numberOfPlatforms - (.005f * numberOfPlatforms), Platform.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.friction = 0f

        body.createFixture(fixtureDefinition)
        body.userData = oPlat

        shape.dispose()

        return xInicio + Platform.WIDTH * numberOfPlatforms / 2f
    }

    fun createWall(x: Float, y: Float): Float {
        var x = x
        val wall = Pools.obtain<Wall>(Wall::class.java)

        x += Wall.WIDTH / 2f
        wall.initialize(x, y)

        val bd = BodyDef()
        bd.position.set(wall.position.x, wall.position.y)
        bd.type = BodyType.StaticBody

        val body = worldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Wall.WIDTH / 2f, Wall.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)
        body.userData = wall
        gameWorld.arrayWall.add(wall)

        shape.dispose()

        return x + Wall.WIDTH / 2f
    }

    fun createMissile(x: Float, y: Float) {
        val obj = Pools.obtain<Missile>(Missile::class.java)
        obj.initialize(x, y)

        val bd = BodyDef()
        bd.position.set(obj.position.x, obj.position.y)
        bd.type = BodyType.KinematicBody

        val body = worldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Missile.WIDTH / 2f, Missile.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)
        body.userData = obj
        body.setLinearVelocity(Missile.SPEED_X, 0f)
        gameWorld.arrayMissile.add(obj)

        shape.dispose()
    }
}
