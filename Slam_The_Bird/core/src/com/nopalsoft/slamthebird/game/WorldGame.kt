package com.nopalsoft.slamthebird.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
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
import com.badlogic.gdx.utils.Pool
import com.nopalsoft.slamthebird.Achievements.unlockCombos
import com.nopalsoft.slamthebird.Achievements.unlockSuperJump
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Assets.playSound
import com.nopalsoft.slamthebird.Settings
import com.nopalsoft.slamthebird.game_objects.Coin
import com.nopalsoft.slamthebird.game_objects.Coin.Companion.createCoinBody
import com.nopalsoft.slamthebird.game_objects.Enemy
import com.nopalsoft.slamthebird.game_objects.Platform
import com.nopalsoft.slamthebird.game_objects.Player
import com.nopalsoft.slamthebird.game_objects.PowerUp
import com.nopalsoft.slamthebird.screens.BaseScreen
import java.util.Random

class WorldGame {
    val WIDTH: Float = BaseScreen.WORLD_SCREEN_WIDTH

    var state: Int

    val TIME_TO_SPAWN_ENEMY: Float = 7f
    var timeToSpawnEnemy: Float

    var TIME_TO_SPAWN_BOOST: Float = 15f
    var timeToSpawnBoost: Float = 0f

    val TIME_TO_CHANGE_STATE_PLATFORM: Float = 16f // Este tiempo debe ser mayor que DURATION_ACTIVE en la clase plataformas.
    var timeToChangeStatePlatform: Float = 0f

    val TIME_TO_SPAWN_COIN: Float = .75f
    var timeToSpawnCoin: Float = 0f

    var world: World = World(Vector2(0f, -9.8f), true)

    var player: Player? = null
    var arrayPlatforms: Array<Platform>
    var arrayEnemies: Array<Enemy>
    var arrayBodies: Array<Body>
    var arrayPowerUps: Array<PowerUp>
    var arrayCoins: Array<Coin>

    var random: Random

    var scoreForSlammingEnemies: Int
    var coinsCollected: Int

    var combo: Int = 0
    var isCoinRain: Boolean

    private val boostPool: Pool<PowerUp> = object : Pool<PowerUp>() {
        override fun newObject(): PowerUp {
            return PowerUp()
        }
    }

    private val monedaPool: Pool<Coin> = object : Pool<Coin>() {
        override fun newObject(): Coin {
            return Coin()
        }
    }

    init {
        world.setContactListener(CollisionHandler())

        state = STATE_RUNNING
        arrayBodies = Array()
        arrayEnemies = Array()
        arrayPlatforms = Array()
        arrayPowerUps = Array()
        arrayCoins = Array()

        random = Random()

        timeToSpawnEnemy = 5f
        isCoinRain = false

        scoreForSlammingEnemies = 0
        coinsCollected = scoreForSlammingEnemies

        val posPiso = .6f
        createWalls(posPiso) // .05
        createPlayer(posPiso + .251f)

        createPlatforms(0 + Platform.WIDTH / 2f, 1.8f + posPiso) // Left Down
        createPlatforms(WIDTH - Platform.WIDTH / 2f + .1f, 1.8f + posPiso) // Down right

        createPlatforms(0 + Platform.WIDTH / 2f, 1.8f * 2f + posPiso) // Left Top
        createPlatforms(
            WIDTH - Platform.WIDTH / 2f + .1f,
            1.8f * 2f + posPiso
        ) // Top Right

        // PowerUp stuff
        TIME_TO_SPAWN_BOOST -= Settings.BOOST_DURATION.toFloat()
    }

    private fun createWalls(floorYPosition: Float) {
        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = 0f
        bodyDefinition.position.y = 0f
        bodyDefinition.type = BodyType.StaticBody
        val body = world.createBody(bodyDefinition)

        val shape = ChainShape()
        val vertices = arrayOfNulls<Vector2>(4)
        vertices[0] = Vector2(0.005f, 50f)
        vertices[1] = Vector2(0.005f, 0f)
        vertices[2] = Vector2(WIDTH, 0f)
        vertices[3] = Vector2(WIDTH, 50f)
        shape.createChain(vertices)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.restitution = 0f
        fixtureDefinition.friction = 0f

        body.createFixture(fixtureDefinition)
        body.userData = "pared"
        shape.dispose()

        val groundShape = EdgeShape()
        groundShape[0f, 0f, WIDTH] = 0f
        bodyDefinition.position.y = floorYPosition
        val groundBody = world.createBody(bodyDefinition)

        fixtureDefinition.shape = groundShape
        groundBody.createFixture(fixtureDefinition)
        groundBody.userData = "piso"

        groundShape.dispose()
    }

    private fun createPlayer(y: Float) {
        player = Player(2.4.toFloat(), y)
        val bd = BodyDef()
        bd.position.x = 2.4.toFloat()
        bd.position.y = y
        bd.type = BodyType.DynamicBody

        val body = world.createBody(bd)

        val shape = CircleShape()
        shape.radius = Player.RADIUS

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.density = 5f
        fixtureDefinition.restitution = 0f
        fixtureDefinition.friction = 0f
        body.createFixture(fixtureDefinition)

        body.isFixedRotation = true
        body.userData = player
        body.isBullet = true

        shape.dispose()
    }

    private fun createEnemies() {
        val x = random.nextFloat() * (WIDTH - 1) + .5f // Para que no apareza
        val y = random.nextFloat() * 4f + .6f

        val obj = Enemy(x, y)
        arrayEnemies.add(obj)
        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = x
        bodyDefinition.position.y = y
        bodyDefinition.type = BodyType.DynamicBody

        val body = world.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.filter.groupIndex = -1
        body.createFixture(fixture)

        body.isFixedRotation = true
        body.gravityScale = 0f
        body.userData = obj

        shape.dispose()
    }

    private fun createPlatforms(x: Float, y: Float) {
        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = x
        bodyDefinition.position.y = y
        bodyDefinition.type = BodyType.StaticBody
        val body = world.createBody(bodyDefinition)

        val shape = PolygonShape()

        shape.setAsBox(Platform.WIDTH / 2f, Platform.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.restitution = 0f
        fixtureDefinition.friction = 0f
        body.createFixture(fixtureDefinition)

        val platform = Platform(bodyDefinition.position.x, bodyDefinition.position.y)
        body.userData = platform
        shape.dispose()

        arrayPlatforms.add(platform)
    }

    private fun createPowerUps() {
        val obj = boostPool.obtain()

        val platformIndex = random.nextInt(4) // above which platform
        val powerUpType = random.nextInt(4) // ice, invincible, coin, etc.
        obj.init(
            this, arrayPlatforms[platformIndex].position.x,
            arrayPlatforms[platformIndex].position.y + .3f, powerUpType
        )

        arrayPowerUps.add(obj)
    }

    private fun createCoins() {
        for (i in 0..5) {
            var x = 0f
            val y = 8.4f + (i * .5f)
            var velocity = Coin.MOVE_SPEED
            if (i % 2f != 0f) {
                velocity *= -1f
                x = WIDTH
            }

            val body = createCoinBody(world, x, y, velocity)
            val obj = monedaPool.obtain()
            obj.init(body.position.x, body.position.y)
            arrayCoins.add(obj)
            body.userData = obj
        }
    }

    fun updateReady(delta: Float, acelX: Float) {
        world.step(delta, 8, 4)
        world.getBodies(arrayBodies)
        for (body in arrayBodies) {
            if (body.userData is Player) {
                player!!.updateReady(body, acelX)
                break
            }
        }
    }

    fun update(delta: Float, accelerationX: Float, slam: Boolean) {
        world.step(delta, 8, 4)

        removeGameObjects()

        timeToSpawnEnemy += delta
        timeToSpawnBoost += delta
        timeToChangeStatePlatform += delta
        timeToSpawnCoin += delta

        if (timeToSpawnEnemy >= TIME_TO_SPAWN_ENEMY) {
            timeToSpawnEnemy -= TIME_TO_SPAWN_ENEMY
            timeToSpawnEnemy += (scoreForSlammingEnemies * .025f) // Hace que aparezcan mas rapido los malos
            if (arrayEnemies.size < 7 + (scoreForSlammingEnemies * .15f)) {
                if (scoreForSlammingEnemies <= 15) {
                    createEnemies()
                } else if (scoreForSlammingEnemies <= 50) {
                    createEnemies()
                    createEnemies()
                } else {
                    createEnemies()
                    createEnemies()
                    createEnemies()
                }
            }
        }

        if (timeToSpawnBoost >= TIME_TO_SPAWN_BOOST) {
            timeToSpawnBoost -= TIME_TO_SPAWN_BOOST
            if (random.nextBoolean()) createPowerUps()
        }

        if (timeToSpawnCoin >= TIME_TO_SPAWN_COIN) {
            timeToSpawnCoin -= TIME_TO_SPAWN_COIN
            createCoins()
        }

        if (timeToChangeStatePlatform >= TIME_TO_CHANGE_STATE_PLATFORM) {
            timeToChangeStatePlatform -= TIME_TO_CHANGE_STATE_PLATFORM
            if (random.nextBoolean()) {
                val plat = random.nextInt(4)
                val state = random.nextInt(2)
                val obj = arrayPlatforms[plat]
                if (state == 0) {
                    obj.setBreakable()
                } else {
                    obj.setFire()
                }
            }
        }

        world.getBodies(arrayBodies)

        for (body in arrayBodies) {
            if (body.userData is Player) {
                updatePlayer(delta, body, accelerationX, slam)
            } else if (body.userData is Enemy) {
                updateEnemy(delta, body)
            } else if (body.userData is PowerUp) {
                updatePowerUp(delta, body)
            } else if (body.userData is Platform) {
                updatePlatform(delta, body)
            } else if (body.userData is Coin) {
                updateCoin(delta, body)
            }
        }

        isCoinRain = false
    }

    private fun removeGameObjects() {
        world.getBodies(arrayBodies)

        for (body in arrayBodies) {
            if (!world.isLocked) {
                val obj = body.userData
                if (obj is Player) {
                    if (obj.state == Player.STATE_DEAD
                        && obj.stateTime >= Player.DEAD_ANIMATION_DURATION
                    ) {
                        world.destroyBody(body)
                        state = STATE_GAME_OVER
                    }
                } else if (obj is Enemy) {
                    if (obj.state == Enemy.STATE_DEAD) {
                        world.destroyBody(body)
                        arrayEnemies.removeValue(obj, true)
                        scoreForSlammingEnemies++

                        /*
                         * If there are no enemies at least I create one, I put this here so that it does not affect the time in which the first bad guy appears.
                         */
                        if (arrayEnemies.size == 0) createEnemies()
                    }
                } else if (obj is PowerUp) {
                    if (obj.state == PowerUp.STATE_TAKEN) {
                        world.destroyBody(body)
                        arrayPowerUps.removeValue(obj, true)
                        boostPool.free(obj)
                    }
                } else if (obj is Coin) {
                    if (obj.state == Coin.STATE_TAKEN) {
                        world.destroyBody(body)
                        arrayCoins.removeValue(obj, true)
                        monedaPool.free(obj)
                    }
                }
            }
        }
    }

    private fun updatePlayer(delta: Float, body: Body, acelX: Float, slam: Boolean) {
        val obj = body.userData as Player
        obj.update(delta, body, acelX, slam)

        if (obj.position.y > 12) {
            unlockSuperJump()
            Gdx.app.log("ACHIIIII", "Asdsadasd")
        }
    }

    private fun updateEnemy(delta: Float, body: Body) {
        val obj = body.userData as Enemy
        obj.update(delta, body, random)
    }

    private fun updatePowerUp(delta: Float, body: Body) {
        val obj = body.userData as PowerUp
        obj.update(delta, body)
    }

    private fun updatePlatform(delta: Float, body: Body) {
        val obj = body.userData as Platform
        obj.update(delta)
    }

    private fun updateCoin(delta: Float, body: Body) {
        val obj = body.userData as Coin
        obj.update(delta, body)

        if (obj.position.x < -3 || obj.position.x > WIDTH + 3) {
            obj.state = Coin.STATE_TAKEN
        }

        if (isCoinRain) {
            body.gravityScale = 1f
            body.setLinearVelocity(body.linearVelocity.x * .25f, 0f)
        }
    }

    internal inner class CollisionHandler : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Player) handlePlayerCollisions(a, b)
            else if (b.body.userData is Player) handlePlayerCollisions(b, a)

            if (a.body.userData is Enemy) handleEnemyCollisions(a, b)
            else if (b.body.userData is Enemy) handleEnemyCollisions(b, a)
        }

        /**
         * Begin contact PLAYER with OTHER-THING
         */
        private fun handlePlayerCollisions(playerFixture: Fixture, otherFixture: Fixture) {
            val player = playerFixture.body.userData as Player
            val otherObject = otherFixture.body.userData

            if (otherObject == "piso") {
                player.jump()

                if (!player.isInvincible)  // If he is invincible I don't take away the combo.
                    combo = 0
            } else if (otherObject is Platform) {
                if (otherObject.state == Platform.STATE_FIRE && !player.isInvincible) {
                    player.hit()
                    return
                } else if (otherObject.state == Platform.STATE_BREAKABLE) {
                    otherObject.setBroken()
                } else if (otherObject.state == Platform.STATE_BROKEN) {
                    return
                }
                if (!player.isInvincible && player.state == Player.STATE_FALLING)  // If he is invincible I don't take away the combo.
                    combo = 0
                player.jump()
            } else if (otherObject is PowerUp) {
                otherObject.hit()
                playSound(Assets.soundBoost!!)

                if (otherObject.type == PowerUp.TYPE_SUPER_JUMP) {
                    player.isSuperJump = true
                } else if (otherObject.type == PowerUp.TYPE_INVINCIBLE) {
                    player.isInvincible = true
                } else if (otherObject.type == PowerUp.TYPE_COIN_RAIN) {
                    isCoinRain = true
                } else if (otherObject.type == PowerUp.TYPE_FREEZE) {
                    for (enemy in arrayEnemies) {
                        enemy.setFrozen()
                    }
                }
            } else if (otherObject is Coin) {
                if (otherObject.state == Coin.STATE_NORMAL) {
                    otherObject.state = Coin.STATE_TAKEN
                    coinsCollected++
                    Settings.currentCoins++
                    playSound(Assets.soundCoin!!)
                }
            } else if (otherObject is Enemy) {
                // I can touch from the middle of the enemy up

                val posRobot = player.position.y - Player.RADIUS
                val pisY = otherObject.position.y

                if (otherObject.state != Enemy.STATE_JUST_APPEARED) {
                    if (player.isInvincible) {
                        otherObject.die()
                        combo++
                    } else if (posRobot > pisY) {
                        otherObject.hit()
                        player.jump()
                        combo++
                    } else if (player.state != Player.STATE_DEAD) {
                        player.hit()
                        combo = 0
                    }
                    if (combo >= COMBO_TO_START_GETTING_COINS) {
                        coinsCollected += combo
                        Settings.currentCoins += combo
                    }

                    unlockCombos()
                }
            }
        }

        private fun handleEnemyCollisions(enemyFixture: Fixture, otherFixture: Fixture) {
            val oEnemy = enemyFixture.body.userData as Enemy
            val otherObject = otherFixture.body.userData

            if (otherObject == "pared") {
                enemyFixture.body.setLinearVelocity(
                    enemyFixture.body.linearVelocity.x * -1,
                    enemyFixture.body.linearVelocity.y
                )
            } else if (otherObject == "piso") {
                if (oEnemy.state == Enemy.STATE_FLYING) {
                    enemyFixture.body.setLinearVelocity(
                        enemyFixture.body.linearVelocity.x,
                        enemyFixture.body.linearVelocity.y * -1
                    )
                }
            }
        }

        override fun endContact(contact: Contact) {
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Player) preSolvePlayer(a, b, contact)
            else if (b.body.userData is Player) preSolvePlayer(b, a, contact)

            if (a.body.userData is Enemy) preSolveEnemy(a, b, contact)
            else if (b.body.userData is Enemy) preSolveEnemy(b, a, contact)

            if (a.body.userData is Coin) preSolveCoins(b, contact)
            else if (b.body.userData is Coin) preSolveCoins(a, contact)
        }

        private fun preSolvePlayer(
            playerFixture: Fixture, otherFixture: Fixture,
            contact: Contact
        ) {
            val otherObject = otherFixture.body.userData
            val player = playerFixture.body.userData as Player

            // Platform oneSide
            if (otherObject is Platform) {
                val playerPosition = player.position.y - Player.RADIUS + .05f
                val platformPosition = otherObject.position.y + (Platform.HEIGHT / 2f)

                if (playerPosition < platformPosition || otherObject.state == Platform.STATE_BROKEN) contact.isEnabled = false
            } else if (otherObject is Enemy) {
                if (otherObject.state == Enemy.STATE_JUST_APPEARED
                    || player.isInvincible
                ) contact.isEnabled = false
            } else if (otherObject is Coin) {
                contact.isEnabled = false
            }
        }

        private fun preSolveEnemy(
            enemyFixture: Fixture, otherFixture: Fixture,
            contact: Contact
        ) {
            val otherObject = otherFixture.body.userData
            val enemy = enemyFixture.body.userData as Enemy

            // Enemy cannot touch platforms if he is flying
            if (otherObject is Platform) {
                if (enemy.state == Enemy.STATE_FLYING) contact.isEnabled = false
            }
        }

        private fun preSolveCoins(
            otherFixture: Fixture,
            contact: Contact
        ) {
            val otherObject = otherFixture.body.userData

            if (otherObject == "pared") {
                contact.isEnabled = false
            }
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse) {
        }
    }

    companion object {
        const val COMBO_TO_START_GETTING_COINS: Float = 3f

        const val STATE_RUNNING: Int = 0
        const val STATE_GAME_OVER: Int = 1
    }
}
