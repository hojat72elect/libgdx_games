package com.nopalsoft.superjumper.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pools
import com.nopalsoft.superjumper.Settings
import com.nopalsoft.superjumper.objects.Bullet
import com.nopalsoft.superjumper.objects.Cloud
import com.nopalsoft.superjumper.objects.Coin
import com.nopalsoft.superjumper.objects.Coin.Companion.createCoin
import com.nopalsoft.superjumper.objects.Coin.Companion.createOneCoin
import com.nopalsoft.superjumper.objects.Enemy
import com.nopalsoft.superjumper.objects.LightningBolt
import com.nopalsoft.superjumper.objects.Platform
import com.nopalsoft.superjumper.objects.PlatformPiece
import com.nopalsoft.superjumper.objects.Player
import com.nopalsoft.superjumper.objects.PowerUpItem
import com.nopalsoft.superjumper.screens.Screens

class WorldGame {

    var state: Int
    private val arrayBodies: Array<Body>
    private var worldBox: World = World(Vector2(0f, -9.8f), true)
    var maxDistance: Int = 0
    private var TIME_UNTIL_NEXT_CLOUD: Float = 15f
    private var timeUntilNextCloud: Float
    var player: Player? = null
    var arrayPlatforms: Array<Platform>
    var arrayPlatformPieces: Array<PlatformPiece>
    var arrayCoins: Array<Coin>

    var arrayEnemies: Array<Enemy>

    var arrayItems: Array<PowerUpItem>

    var arrayClouds: Array<Cloud>

    var arrayLightningBolts: Array<LightningBolt>
    var coins: Int = 0

    var arrayBullets: Array<Bullet>
    private var gameWorldCreatedUntilY: Float

    init {
        worldBox.setContactListener(CollisionHandler())

        arrayBodies = Array()
        arrayPlatforms = Array()
        arrayPlatformPieces = Array()
        arrayCoins = Array()
        arrayEnemies = Array()
        arrayItems = Array()
        arrayClouds = Array()
        arrayLightningBolts = Array()
        arrayBullets = Array()

        timeUntilNextCloud = 0f

        state = STATE_RUNNING

        createFloor()
        createPlayer()

        gameWorldCreatedUntilY = player!!.position.y
        createNextSection()
    }

    private fun createNextSection() {
        val y = gameWorldCreatedUntilY + 2

        var i = 0
        while (gameWorldCreatedUntilY < (y + 10)) {
            gameWorldCreatedUntilY = y + (i * 2)

            createPlatform(gameWorldCreatedUntilY)
            createPlatform(gameWorldCreatedUntilY)

            if (MathUtils.random(100) < 5) createCoin(worldBox, arrayCoins, gameWorldCreatedUntilY)

            if (MathUtils.random(20) < 5) createOneCoin(worldBox, arrayCoins, gameWorldCreatedUntilY + .5f)

            if (MathUtils.random(20) < 5) createEnemy(gameWorldCreatedUntilY + .5f)

            if (timeUntilNextCloud >= TIME_UNTIL_NEXT_CLOUD) {
                createCloud(gameWorldCreatedUntilY + .7f)
                timeUntilNextCloud = 0f
            }

            if (MathUtils.random(50) < 5) createItem(gameWorldCreatedUntilY + .5f)
            i++
        }
    }

    /**
     * The floor only appears once, at the beginning of the game.
     */
    private fun createFloor() {
        val bd = BodyDef()
        bd.type = BodyType.StaticBody

        val body = worldBox.createBody(bd)

        val shape = EdgeShape()
        shape[0f, 0f, Screens.WORLD_WIDTH] = 0f

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape

        body.createFixture(fixtureDefinition)
        body.userData = "piso"

        shape.dispose()
    }

    private fun createPlayer() {
        player = Player(2.4f, .5f)

        val bd = BodyDef()
        bd.position[player!!.position.x] = player!!.position.y
        bd.type = BodyType.DynamicBody

        val body = worldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.density = 10f
        fixtureDefinition.friction = 0f
        fixtureDefinition.restitution = 0f

        body.createFixture(fixtureDefinition)
        body.userData = player
        body.isFixedRotation = true

        shape.dispose()
    }

    private fun createPlatform(y: Float) {
        val platform = Pools.obtain(Platform::class.java)
        platform.initialize(MathUtils.random(Screens.WORLD_WIDTH), y, MathUtils.random(1))

        val bodyDefinition = BodyDef()
        bodyDefinition.position[platform.position.x] = platform.position.y
        bodyDefinition.type = BodyType.KinematicBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Platform.WIDTH_NORMAL / 2f, Platform.HEIGHT_NORMAL / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape

        body.createFixture(fixtureDefinition)
        body.userData = platform
        arrayPlatforms.add(platform)

        shape.dispose()
    }

    /**
     * The breakable platform is 2 frames.
     */
    private fun createPlatformPieces(oPlat: Platform) {
        createPlatformPieces(oPlat, PlatformPiece.TYPE_LEFT)
        createPlatformPieces(oPlat, PlatformPiece.TYPE_RIGHT)
    }

    private fun createPlatformPieces(platform: Platform, type: Int) {
        val x: Float
        var angularVelocity = 100f

        if (type == PlatformPiece.TYPE_LEFT) {
            x = platform.position.x - PlatformPiece.WIDTH_NORMAL / 2f
            angularVelocity *= -1f
        } else {
            x = platform.position.x + PlatformPiece.WIDTH_NORMAL / 2f
        }

        val platformPiece = Pools.obtain(PlatformPiece::class.java)
        platformPiece.initialize(x, platform.position.y, type, platform.color)

        val bd = BodyDef()
        bd.position[platformPiece.position.x] = platformPiece.position.y
        bd.type = BodyType.DynamicBody

        val body = worldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(PlatformPiece.WIDTH_NORMAL / 2f, PlatformPiece.HEIGHT_NORMAL / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)
        body.userData = platformPiece
        body.angularVelocity = MathUtils.degRad * angularVelocity
        arrayPlatformPieces.add(platformPiece)

        shape.dispose()
    }

    private fun createEnemy(y: Float) {
        val oEn = Pools.obtain(Enemy::class.java)
        oEn.initialize(MathUtils.random(Screens.WORLD_WIDTH), y)

        val bd = BodyDef()
        bd.position[oEn.position.x] = oEn.position.y
        bd.type = BodyType.DynamicBody

        val body = worldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f)

        val enemyFixtureDefinition = FixtureDef()
        enemyFixtureDefinition.shape = shape
        enemyFixtureDefinition.isSensor = true

        body.createFixture(enemyFixtureDefinition)
        body.userData = oEn
        body.gravityScale = 0f

        val speed = MathUtils.random(1f, Enemy.SPEED_X)

        if (MathUtils.randomBoolean()) body.setLinearVelocity(speed, 0f)
        else body.setLinearVelocity(-speed, 0f)
        arrayEnemies.add(oEn)

        shape.dispose()
    }

    private fun createItem(y: Float) {
        val oPowerUpItem = Pools.obtain(PowerUpItem::class.java)
        oPowerUpItem.init(MathUtils.random(Screens.WORLD_WIDTH), y)

        val bd = BodyDef()
        bd.position[oPowerUpItem.position.x] = oPowerUpItem.position.y
        bd.type = BodyType.StaticBody
        val oBody = worldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(PowerUpItem.WIDTH / 2f, PowerUpItem.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true
        oBody.createFixture(fixture)
        oBody.userData = oPowerUpItem
        shape.dispose()
        arrayItems.add(oPowerUpItem)
    }

    private fun createCloud(y: Float) {
        val cloud = Pools.obtain(Cloud::class.java)
        cloud.init(MathUtils.random(Screens.WORLD_WIDTH), y)

        val cloudBodyDefinition = BodyDef()
        cloudBodyDefinition.position[cloud.position.x] = cloud.position.y
        cloudBodyDefinition.type = BodyType.DynamicBody

        val cloudBody = worldBox.createBody(cloudBodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Cloud.WIDTH / 2f, Cloud.HEIGHT / 2f)

        val cloudFixtureDefinition = FixtureDef()
        cloudFixtureDefinition.shape = shape
        cloudFixtureDefinition.isSensor = true

        cloudBody.createFixture(cloudFixtureDefinition)
        cloudBody.userData = cloud
        cloudBody.gravityScale = 0f

        val speed = MathUtils.random(1f, Cloud.SPEED_X)

        if (MathUtils.randomBoolean()) cloudBody.setLinearVelocity(speed, 0f)
        else cloudBody.setLinearVelocity(-speed, 0f)
        arrayClouds.add(cloud)

        shape.dispose()
    }

    private fun createLightningBolt(x: Float, y: Float) {
        val oLightningBolt = Pools.obtain(LightningBolt::class.java)
        oLightningBolt.init(x, y)

        val bd = BodyDef()
        bd.position[oLightningBolt.position.x] = oLightningBolt.position.y
        bd.type = BodyType.KinematicBody

        val body = worldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(LightningBolt.WIDTH / 2f, LightningBolt.HEIGHT / 2f)

        val lightningBoltFixtureDefinition = FixtureDef()
        lightningBoltFixtureDefinition.shape = shape
        lightningBoltFixtureDefinition.isSensor = true

        body.createFixture(lightningBoltFixtureDefinition)
        body.userData = oLightningBolt

        body.setLinearVelocity(0f, LightningBolt.SPEED_Y)
        arrayLightningBolts.add(oLightningBolt)

        shape.dispose()
    }

    private fun createBullet(origenX: Float, origenY: Float, destinationX: Float, destinationY: Float) {
        val bullet = Pools.obtain(Bullet::class.java)
        bullet.init(origenX, origenY)

        val bulletBodyDefinition = BodyDef()
        bulletBodyDefinition.position[bullet.position.x] = bullet.position.y
        bulletBodyDefinition.type = BodyType.KinematicBody

        val bulletBody = worldBox.createBody(bulletBodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Bullet.SIZE / 2f, Bullet.SIZE / 2f)

        val bulletFixtureDefinition = FixtureDef()
        bulletFixtureDefinition.shape = shape
        bulletFixtureDefinition.isSensor = true

        bulletBody.createFixture(bulletFixtureDefinition)
        bulletBody.userData = bullet
        bulletBody.isBullet = true

        val destination = Vector2(destinationX, destinationY)
        destination.sub(bullet.position).nor().scl(Bullet.SPEED)

        bulletBody.setLinearVelocity(destination.x, destination.y)

        arrayBullets.add(bullet)

        shape.dispose()
    }

    fun update(delta: Float, accelerationX: Float, fire: Boolean, touchPositionWorldCoordinates: Vector3) {
        worldBox.step(delta, 8, 4)

        removeObjects()

        /*
         * I check if it is necessary to generate the next part of the world
         */
        if (player!!.position.y + 10 > gameWorldCreatedUntilY) {
            createNextSection()
        }

        timeUntilNextCloud += delta // I update the time to create a cloud.

        worldBox.getBodies(arrayBodies)

        for (body in arrayBodies) {
            when (body.userData) {
                is Player -> {
                    updatePlayer(body, delta, accelerationX, fire, touchPositionWorldCoordinates)
                }

                is Platform -> {
                    updatePlatform(body, delta)
                }

                is PlatformPiece -> {
                    updatePlatformPiece(body, delta)
                }

                is Coin -> {
                    updateCoin(body, delta)
                }

                is Enemy -> {
                    updateEnemy(body, delta)
                }

                is PowerUpItem -> {
                    updateItem(body, delta)
                }

                is Cloud -> {
                    updateCloud(body, delta)
                }

                is LightningBolt -> {
                    updateLightningBolt(body, delta)
                }

                is Bullet -> {
                    updateBullet(body, delta)
                }
            }
        }

        if (maxDistance < (player!!.position.y * 10)) {
            maxDistance = (player!!.position.y * 10).toInt()
        }

        // If the character is 5.5f below the maximum height, he dies (It is multiplied by 10 because the distance is multiplied by 10).
        if (player!!.state == Player.STATE_NORMAL && maxDistance - (5.5f * 10) > (player!!.position.y * 10)) {
            player!!.die()
        }
        if (player!!.state == Player.STATE_DEAD && maxDistance - (25 * 10) > (player!!.position.y * 10)) {
            state = STATE_GAMEOVER
        }
    }

    private fun removeObjects() {
        worldBox.getBodies(arrayBodies)

        for (body in arrayBodies) {
            if (!worldBox.isLocked) {
                if (body.userData is Platform) {
                    val oPlat = body.userData as Platform
                    if (oPlat.state == Platform.STATE_DESTROY) {
                        arrayPlatforms.removeValue(oPlat, true)
                        worldBox.destroyBody(body)
                        if (oPlat.type == Platform.TYPE_BREAKABLE) createPlatformPieces(oPlat)
                        Pools.free(oPlat)
                    }
                } else if (body.userData is Coin) {
                    val oMon = body.userData as Coin
                    if (oMon.state == Coin.STATE_TAKEN) {
                        arrayCoins.removeValue(oMon, true)
                        worldBox.destroyBody(body)
                        Pools.free(oMon)
                    }
                } else if (body.userData is PlatformPiece) {
                    val platformPiece = body.userData as PlatformPiece
                    if (platformPiece.state == PlatformPiece.STATE_DESTROY) {
                        arrayPlatformPieces.removeValue(platformPiece, true)
                        worldBox.destroyBody(body)
                        Pools.free(platformPiece)
                    }
                } else if (body.userData is Enemy) {
                    val oEnemy = body.userData as Enemy
                    if (oEnemy.state == Enemy.STATE_DEAD) {
                        arrayEnemies.removeValue(oEnemy, true)
                        worldBox.destroyBody(body)
                        Pools.free(oEnemy)
                    }
                } else if (body.userData is PowerUpItem) {
                    val oPowerUpItem = body.userData as PowerUpItem
                    if (oPowerUpItem.state == PowerUpItem.STATE_TAKEN) {
                        arrayItems.removeValue(oPowerUpItem, true)
                        worldBox.destroyBody(body)
                        Pools.free(oPowerUpItem)
                    }
                } else if (body.userData is Cloud) {
                    val oCloud = body.userData as Cloud
                    if (oCloud.state == Cloud.STATE_DEAD) {
                        arrayClouds.removeValue(oCloud, true)
                        worldBox.destroyBody(body)
                        Pools.free(oCloud)
                    }
                } else if (body.userData is LightningBolt) {
                    val oLightningBolt = body.userData as LightningBolt
                    if (oLightningBolt.state == LightningBolt.STATE_DESTROY) {
                        arrayLightningBolts.removeValue(oLightningBolt, true)
                        worldBox.destroyBody(body)
                        Pools.free(oLightningBolt)
                    }
                } else if (body.userData is Bullet) {
                    val oBullet = body.userData as Bullet
                    if (oBullet.state == Bullet.STATE_DESTROY) {
                        arrayBullets.removeValue(oBullet, true)
                        worldBox.destroyBody(body)
                        Pools.free(oBullet)
                    }
                } else if (body.userData == "piso") {
                    if (player!!.position.y - 5.5f > body.position.y || player!!.state == Player.STATE_DEAD) {
                        worldBox.destroyBody(body)
                    }
                }
            }
        }
    }

    private fun updatePlayer(body: Body, delta: Float, accelerationX: Float, fire: Boolean, touchPositionWorldCoordinates: Vector3) {
        player!!.update(body, delta, accelerationX)

        if (Settings.numBullets > 0 && fire) {
            createBullet(player!!.position.x, player!!.position.y, touchPositionWorldCoordinates.x, touchPositionWorldCoordinates.y)
            Settings.numBullets--
        }
    }

    private fun updatePlatform(body: Body, delta: Float) {
        val obj = body.userData as Platform
        obj.update(delta)
        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.setDestroy()
        }
    }

    private fun updatePlatformPiece(body: Body, delta: Float) {
        val obj = body.userData as PlatformPiece
        obj.update(delta, body)
        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.setDestroy()
        }
    }

    private fun updateCoin(body: Body, delta: Float) {
        val obj = body.userData as Coin
        obj.update(delta)
        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.take()
        }
    }

    private fun updateEnemy(body: Body, delta: Float) {
        val obj = body.userData as Enemy
        obj.update(body, delta)
        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.hit()
        }
    }

    private fun updateItem(body: Body, delta: Float) {
        val obj = body.userData as PowerUpItem
        obj.update(delta)
        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.take()
        }
    }

    private fun updateCloud(body: Body, delta: Float) {
        val obj = body.userData as Cloud
        obj.update(body, delta)

        if (obj.isLightning) {
            createLightningBolt(obj.position.x, obj.position.y - .65f)
            obj.fireLighting()
        }

        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.destroy()
        }
    }

    private fun updateLightningBolt(body: Body, delta: Float) {
        val obj = body.userData as LightningBolt
        obj.update(body, delta)

        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.destroy()
        }
    }

    private fun updateBullet(body: Body, delta: Float) {
        val obj = body.userData as Bullet
        obj.update(body, delta)

        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.destroy()
        }
    }

    internal inner class CollisionHandler : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Player) checkPlayerCollisions(b)
            else if (b.body.userData is Player) checkPlayerCollisions(a)

            if (a.body.userData is Bullet) checkBulletCollisions(a, b)
            else if (b.body.userData is Bullet) checkBulletCollisions(b, a)
        }

        private fun checkPlayerCollisions(otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData

            if (otherObject == "piso") {
                player!!.jump()

                if (player!!.state == Player.STATE_DEAD) {
                    state = STATE_GAMEOVER
                }
            } else if (otherObject is Platform) {

                if (player!!.speed.y <= 0) {
                    player!!.jump()
                    if (otherObject.type == Platform.TYPE_BREAKABLE) {
                        otherObject.setDestroy()
                    }
                }
            } else if (otherObject is Coin) {
                otherObject.take()
                coins++
                player!!.jump()
            } else if (otherObject is Enemy) {
                player!!.hit()
            } else if (otherObject is LightningBolt) {
                player!!.hit()
            } else if (otherObject is PowerUpItem) {
                otherObject.take()

                when (otherObject.type) {
                    PowerUpItem.TYPE_BUBBLE -> player!!.setBubble()
                    PowerUpItem.TYPE_JETPACK -> player!!.setJetPack()
                    PowerUpItem.TYPE_GUN -> Settings.numBullets += 10
                }
            }
        }

        private fun checkBulletCollisions(bulletFixture: Fixture, otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData
            val bullet = bulletFixture.body.userData as Bullet

            if (otherObject is Enemy) {
                otherObject.hit()
                bullet.destroy()
            } else if (otherObject is Cloud) {
                otherObject.hit()
                bullet.destroy()
            }
        }

        override fun endContact(contact: Contact) {
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Player) preSolvePlayer(a, b, contact)
            else if (b.body.userData is Player) preSolvePlayer(b, a, contact)
        }

        private fun preSolvePlayer(playerFixture: Fixture, otherFixture: Fixture, contact: Contact) {
            val otherObject = otherFixture.body.userData

            if (otherObject is Platform) {
                // If you go up, cross the platform.

                val obj = otherObject

                val ponyY = playerFixture.body.position.y - .30f
                val pisY = obj.position.y + Platform.HEIGHT_NORMAL / 2f

                if (ponyY < pisY) contact.isEnabled = false

                if (obj.type == Platform.TYPE_NORMAL && player!!.state == Player.STATE_DEAD) {
                    contact.isEnabled = false
                }
            }
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse) {
            // TODO Auto-generated method stub
        }
    }

    companion object {
        const val STATE_RUNNING: Int = 0
        const val STATE_GAMEOVER: Int = 1
    }
}
