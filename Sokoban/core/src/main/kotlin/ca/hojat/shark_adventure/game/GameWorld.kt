package ca.hojat.shark_adventure.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.physics.box2d.Filter
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pools
import ca.hojat.shark_adventure.Achievements
import ca.hojat.shark_adventure.Assets
import ca.hojat.shark_adventure.Settings
import ca.hojat.shark_adventure.objects.Barrel
import ca.hojat.shark_adventure.objects.Blast
import ca.hojat.shark_adventure.objects.Chain
import ca.hojat.shark_adventure.objects.Items
import ca.hojat.shark_adventure.objects.Mine
import ca.hojat.shark_adventure.objects.Shark
import ca.hojat.shark_adventure.objects.Submarine
import ca.hojat.shark_adventure.objects.Torpedo
import ca.hojat.shark_adventure.screens.Screens

class GameWorld {
    var state: Int


    var timeToGameOver: Float

    var timeToSpawnBarrel: Float

    var timeToSpawnMine: Float = 0f

    var timeToSpawnChainMine: Float = 0f

    var timeToSpawnSubmarine: Float = 0f

    var timeToSpawnItems: Float = 0f

    var worldPhysics = World(Vector2(0f, -4f), true)

    @JvmField
    var oShark: Shark

    @JvmField
    var arrayBarrels: Array<Barrel?>
    var arrayBodies: Array<Body>

    @JvmField
    var arrayMines: Array<Mine?>

    @JvmField
    var arrayChains: Array<Chain?>

    @JvmField
    var arrayBlasts: Array<Blast?>

    @JvmField
    var arrayTorpedoes: Array<Torpedo?>

    @JvmField
    var arraySubmarines: Array<Submarine?>

    @JvmField
    var arrayItems: Array<Items?>

    var score: Double

    var destroyedSubmarines: Int

    init {
        worldPhysics.setContactListener(CollisionHandler())

        state = STATE_RUNNING
        timeToGameOver = 0f
        score = 0.0
        destroyedSubmarines = 0

        arrayBodies = Array<Body>()
        arrayBarrels = Array<Barrel?>()
        arrayMines = Array<Mine?>()
        arrayChains = Array<Chain?>()
        arrayBlasts = Array<Blast?>()
        arrayTorpedoes = Array<Torpedo?>()
        arraySubmarines = Array<Submarine?>()
        arrayItems = Array<Items?>()
        oShark = Shark(3.5f, 2f)

        timeToSpawnBarrel = 0f
        createMineChain()
        createWalls()
        createPlayer(false)
    }

    private fun createWalls() {
        val bodyDefinition = BodyDef()
        bodyDefinition.type = BodyType.StaticBody

        val body = worldPhysics.createBody(bodyDefinition)

        val shape = EdgeShape()

        // Down
        shape.set(0f, 0f, Screens.WORLD_WIDTH, 0f)
        body.createFixture(shape, 0f)

        // Right
        shape.set(Screens.WORLD_WIDTH + .5f, 0f, Screens.WORLD_WIDTH + .5f, Screens.WORLD_HEIGHT)
        body.createFixture(shape, 0f)

        // Up
        shape.set(0f, Screens.WORLD_HEIGHT + .5f, Screens.WORLD_WIDTH, Screens.WORLD_HEIGHT + .5f)
        body.createFixture(shape, 0f)

        // Left
        shape.set(-.5f, 0f, -.5f, Screens.WORLD_HEIGHT)
        body.createFixture(shape, 0f)

        for (fix in body.fixtureList) {
            fix.friction = 0f
            val filterData = Filter()
            filterData.groupIndex = -1
            fix.setFilterData(filterData)
        }

        body.userData = "piso"

        shape.dispose()
    }

    private fun createPlayer(isFacingLeft: Boolean) {
        val bd = BodyDef()
        bd.position.set(oShark.position.x, oShark.position.y)
        bd.type = BodyType.DynamicBody

        val body = worldPhysics.createBody(bd)
        val shape = PolygonShape()

        if (isFacingLeft) {
            shape.set(floatArrayOf(.05f, .34f, -.12f, .18f, .13f, .19f, .18f, .37f))
            body.createFixture(shape, 0f)

            shape.set(floatArrayOf(-.12f, .18f, -.40f, .09f, -.40f, -.18f, -.25f, -.37f, .29f, -.39f, .36f, -.19f, .27f, -.03f, .13f, .19f))
            body.createFixture(shape, 0f)

            shape.set(floatArrayOf(.59f, .12f, .43f, -.06f, .36f, -.19f, .52f, -.33f))
            body.createFixture(shape, 0f)

            shape.set(floatArrayOf(-.40f, -.18f, -.40f, .09f, -.58f, -.05f, -.59f, -.12f))
            body.createFixture(shape, 0f)

            shape.set(floatArrayOf(.36f, -.19f, .29f, -.39f, .33f, -.34f))
            body.createFixture(shape, 0f)

            shape.set(floatArrayOf(.36f, -.19f, .43f, -.06f, .27f, -.03f))
            body.createFixture(shape, 0f)
        } else {
            shape.set(floatArrayOf(-.13f, .19f, .12f, .18f, -.05f, .34f, -.18f, .37f))
            body.createFixture(shape, 0f)

            shape.set(floatArrayOf(-.27f, -.03f, -.36f, -.19f, -.29f, -.39f, .25f, -.37f, .40f, -.18f, .40f, .09f, .12f, .18f, -.13f, .19f))
            body.createFixture(shape, 0f)

            shape.set(floatArrayOf(-.36f, -.19f, -.43f, -.06f, -.59f, .12f, -.52f, -.33f))
            body.createFixture(shape, 0f)

            shape.set(floatArrayOf(.58f, -.05f, .40f, .09f, .40f, -.18f, .59f, -.12f))
            body.createFixture(shape, 0f)

            shape.set(floatArrayOf(-.29f, -.39f, -.36f, -.19f, -.33f, -.34f))
            body.createFixture(shape, 0f)

            shape.set(floatArrayOf(-.43f, -.06f, -.36f, -.19f, -.27f, -.03f))
            body.createFixture(shape, 0f)
        }

        body.userData = oShark
        body.isFixedRotation = true
        body.gravityScale = .45f

        shape.dispose()
    }

    private fun createBarrel(x: Float, y: Float) {
        val obj = Pools.obtain<Barrel>(Barrel::class.java)
        obj.init(x, y)

        val bd = BodyDef()
        bd.position.set(obj.position.x, obj.position.y)
        bd.type = BodyType.DynamicBody

        val body = worldPhysics.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Barrel.WIDTH / 2f, Barrel.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)
        body.userData = obj
        body.isFixedRotation = true
        body.gravityScale = .15f
        body.angularVelocity = MathUtils.degRad * MathUtils.random(-50, 50)

        arrayBarrels.add(obj)
        shape.dispose()
    }

    private fun createItem() {
        val obj = Pools.obtain<Items>(Items::class.java)
        obj.init(Screens.WORLD_WIDTH + 1, MathUtils.random(Screens.WORLD_HEIGHT))

        val bd = BodyDef()
        bd.position.set(obj.position.x, obj.position.y)
        bd.type = BodyType.KinematicBody

        val body = worldPhysics.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Items.WIDTH / 2f, Items.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)
        body.userData = obj
        body.isFixedRotation = true
        body.setLinearVelocity(Items.SPEED_X, 0f)

        arrayItems.add(obj)
        shape.dispose()
    }

    private fun createBlast() {
        var speedX = Blast.SPEED_X
        var x = oShark.position.x + .3f

        if (oShark.isFacingLeft) {
            speedX = -Blast.SPEED_X
            x = oShark.position.x - .3f
        }
        val obj = Pools.obtain<Blast>(Blast::class.java)

        obj.init(x, oShark.position.y - .15f)

        val bd = BodyDef()
        bd.position.set(obj.position.x, obj.position.y)
        bd.type = BodyType.KinematicBody

        val body = worldPhysics.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Blast.WIDTH / 2f, Blast.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.isBullet = true
        body.createFixture(fixtureDefinition)
        body.userData = obj
        body.isFixedRotation = true
        body.setLinearVelocity(speedX, 0f)

        arrayBlasts.add(obj)
        shape.dispose()
    }

    private fun createTorpedo(x: Float, y: Float, goLeft: Boolean) {
        var velX = Torpedo.SPEED_X
        if (goLeft) {
            velX = -Torpedo.SPEED_X
        }
        val obj = Pools.obtain<Torpedo>(Torpedo::class.java)
        obj.init(x, y, goLeft)

        val bd = BodyDef()
        bd.position.set(obj.position.x, obj.position.y)
        bd.type = BodyType.DynamicBody

        val body = worldPhysics.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Blast.WIDTH / 2f, Blast.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)
        body.userData = obj
        body.gravityScale = 0f
        body.isFixedRotation = true
        body.setLinearVelocity(velX, 0f)

        arrayTorpedoes.add(obj)
        shape.dispose()
    }

    private fun createMine(x: Float, y: Float) {
        val obj = Pools.obtain<Mine>(Mine::class.java)
        obj.init(x, y)

        val bd = BodyDef()
        bd.position.set(obj.position.x, obj.position.y)
        bd.type = BodyType.DynamicBody

        val body = worldPhysics.createBody(bd)

        val shape = CircleShape()
        shape.radius = Mine.WIDTH / 2f

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)
        body.userData = obj
        body.isFixedRotation = true
        body.gravityScale = 0f
        body.setLinearVelocity(Mine.SPEED_X.toFloat(), 0f)

        arrayMines.add(obj)
        shape.dispose()
    }

    private fun createSubmarine() {
        val obj = Pools.obtain<Submarine>(Submarine::class.java)
        val x: Float
        val y: Float
        val xTarget: Float
        val yTarget: Float
        when (MathUtils.random(1, 4)) {
            1 -> {
                x = -1f
                y = -1f
                xTarget = Screens.WORLD_WIDTH + 6
                yTarget = Screens.WORLD_HEIGHT + 6
            }

            2 -> {
                x = -1f
                y = Screens.WORLD_HEIGHT + 1
                xTarget = Screens.WORLD_WIDTH + 6
                yTarget = -6f
            }

            3 -> {
                x = Screens.WORLD_WIDTH + 1
                y = Screens.WORLD_HEIGHT + 1
                xTarget = -6f
                yTarget = -6f
            }

            4 -> {
                x = Screens.WORLD_WIDTH + 1
                y = -1f
                xTarget = -6f
                yTarget = Screens.WORLD_HEIGHT + 6
            }

            else -> {
                x = Screens.WORLD_WIDTH + 1
                y = -1f
                xTarget = -6f
                yTarget = Screens.WORLD_HEIGHT + 6
            }
        }

        obj.init(x, y, xTarget, yTarget)

        val bd = BodyDef()
        bd.position.set(obj.position.x, obj.position.y)
        bd.type = BodyType.DynamicBody

        val body = worldPhysics.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Submarine.WIDTH / 2f, Submarine.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)
        body.userData = obj
        body.isFixedRotation = true
        body.gravityScale = 0f
        arraySubmarines.add(obj)
        shape.dispose()
    }

    private fun createMineChain() {
        val x = 10f
        val obj = Pools.obtain<Mine>(Mine::class.java)
        obj.init(x, 1f)
        obj.type = Mine.TYPE_GRAY

        val bodyDefinition = BodyDef()
        bodyDefinition.position.set(obj.position.x, obj.position.y)
        bodyDefinition.type = BodyType.DynamicBody

        val body = worldPhysics.createBody(bodyDefinition)

        val shape = CircleShape()
        shape.radius = Mine.WIDTH / 2f

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.isSensor = true
        fixtureDefinition.density = 1f

        body.createFixture(fixtureDefinition)
        body.userData = obj
        body.isFixedRotation = true
        body.gravityScale = -3.5f

        arrayMines.add(obj)
        shape.dispose()

        val chainShape = PolygonShape()
        chainShape.setAsBox(Chain.WIDTH / 2f, Chain.HEIGHT / 2f)

        fixtureDefinition.isSensor = false
        fixtureDefinition.shape = chainShape
        fixtureDefinition.filter.groupIndex = -1

        val numberOfLinks = MathUtils.random(5, 15)
        var link: Body? = null
        for (i in 0..<numberOfLinks) {
            val objChain = Pools.obtain<Chain>(Chain::class.java)
            objChain.init(x, Chain.HEIGHT * i)
            bodyDefinition.position.set(objChain.position.x, objChain.position.y)
            if (i == 0) {
                objChain.init(x, -.12f) // Makes the kinematic body appear a little lower so as not to collide with it.
                bodyDefinition.position.set(objChain.position.x, objChain.position.y)
                bodyDefinition.type = BodyType.KinematicBody
                link = worldPhysics.createBody(bodyDefinition)
                link.createFixture(fixtureDefinition)
                link.setLinearVelocity(Chain.SPEED_X, 0f)
            } else {
                bodyDefinition.type = BodyType.DynamicBody
                val newLink = worldPhysics.createBody(bodyDefinition)
                newLink.createFixture(fixtureDefinition)
                createRevoluteJoint(link, newLink, -Chain.HEIGHT / 2f)
                link = newLink
            }
            arrayChains.add(objChain)
            link.userData = objChain
        }

        createRevoluteJoint(link, body, -Mine.HEIGHT / 2f)
    }

    private fun createRevoluteJoint(bodyA: Body?, bodyB: Body?, anchorBY: Float) {
        val jointDef = RevoluteJointDef()
        jointDef.localAnchorA.set(0f, 0.105.toFloat())
        jointDef.localAnchorB.set(0f, anchorBY)
        jointDef.bodyA = bodyA
        jointDef.bodyB = bodyB
        worldPhysics.createJoint(jointDef)
    }

    fun update(delta: Float, accelX: Float, didSwimUp: Boolean, didFire: Boolean) {
        worldPhysics.step(delta, 8, 4)

        removeGameObjects()

        timeToSpawnBarrel += delta
        if (timeToSpawnBarrel >= TIME_TO_SPAWN_BARREL) {
            timeToSpawnBarrel -= TIME_TO_SPAWN_BARREL

            if (MathUtils.randomBoolean()) {
                repeat(6) {
                    createBarrel(MathUtils.random(Screens.WORLD_WIDTH), MathUtils.random(5.5f, 8.5f))
                }
            }
        }

        timeToSpawnMine += delta
        if (timeToSpawnMine >= TIME_TO_SPAWN_MINE) {
            timeToSpawnMine -= TIME_TO_SPAWN_MINE
            repeat(5) {
                if (MathUtils.randomBoolean()) createMine(MathUtils.random(9f, 10f), MathUtils.random(Screens.WORLD_HEIGHT))
            }
        }

        if (arrayMines.size == 0) createMine(9f, MathUtils.random(Screens.WORLD_HEIGHT))

        timeToSpawnChainMine += delta
        if (timeToSpawnChainMine >= TIME_TO_SPAWN_CHAIN_MINE) {
            timeToSpawnChainMine -= TIME_TO_SPAWN_CHAIN_MINE
            if (MathUtils.randomBoolean(.75f)) createMineChain()
        }

        timeToSpawnSubmarine += delta
        if (timeToSpawnSubmarine >= TIME_TO_SPAWN_SUBMARINE) {
            timeToSpawnSubmarine -= TIME_TO_SPAWN_SUBMARINE
            if (MathUtils.randomBoolean(.65f)) {
                createSubmarine()
                if (Settings.isSoundOn) Assets.sonarSound!!.play()
            }
        }

        timeToSpawnItems += delta
        if (timeToSpawnItems >= TIME_TO_SPAWN_ITEMS) {
            timeToSpawnItems -= TIME_TO_SPAWN_ITEMS
            if (MathUtils.randomBoolean()) {
                createItem()
            }
        }

        worldPhysics.getBodies(arrayBodies)

        for (body in arrayBodies) {
            if (body.userData is Shark) {
                updatePlayer(body, delta, accelX, didSwimUp, didFire)
            } else if (body.userData is Barrel) {
                updateBarrel(body, delta)
            } else if (body.userData is Mine) {
                updateMine(body, delta)
            } else if (body.userData is Chain) {
                updateChain(body)
            } else if (body.userData is Blast) {
                updateBlast(body, delta)
            } else if (body.userData is Torpedo) {
                updateTorpedo(body, delta)
            } else if (body.userData is Submarine) {
                updateSubmarine(body, delta)
            } else if (body.userData is Items) {
                updateItems(body)
            }
        }

        if (oShark.state == Shark.STATE_DEAD) {
            timeToGameOver += delta
            if (timeToGameOver >= TIME_TO_GAMEOVER) {
                state = STATE_GAME_OVER
            }
        } else {
            score += (delta * 15).toDouble()
        }

        Achievements.distance(score.toLong(), oShark.didGetHurtOnce)
    }

    private fun removeGameObjects() {
        for (body in arrayBodies) {
            if (!worldPhysics.isLocked) {
                if (body.userData is Barrel) {
                    val obj = body.userData as Barrel
                    if (obj.state == Barrel.STATE_REMOVE) {
                        arrayBarrels.removeValue(obj, true)
                        Pools.free(obj)
                        worldPhysics.destroyBody(body)
                    }
                } else if (body.userData is Mine) {
                    val obj = body.userData as Mine
                    if (obj.state == Mine.STATE_REMOVE) {
                        arrayMines.removeValue(obj, true)
                        Pools.free(obj)
                        worldPhysics.destroyBody(body)
                    }
                } else if (body.userData is Chain) {
                    val obj = body.userData as Chain
                    if (obj.state == Chain.STATE_REMOVE) {
                        arrayChains.removeValue(obj, true)
                        Pools.free(obj)
                        worldPhysics.destroyBody(body)
                    }
                } else if (body.userData is Blast) {
                    val obj = body.userData as Blast
                    if (obj.state == Blast.STATE_REMOVE) {
                        arrayBlasts.removeValue(obj, true)
                        Pools.free(obj)
                        worldPhysics.destroyBody(body)
                    }
                } else if (body.userData is Torpedo) {
                    val obj = body.userData as Torpedo
                    if (obj.state == Torpedo.STATE_REMOVE) {
                        arrayTorpedoes.removeValue(obj, true)
                        Pools.free(obj)
                        worldPhysics.destroyBody(body)
                    }
                } else if (body.userData is Submarine) {
                    val obj = body.userData as Submarine
                    if (obj.state == Submarine.STATE_REMOVE) {
                        arraySubmarines.removeValue(obj, true)
                        Pools.free(obj)
                        worldPhysics.destroyBody(body)
                    }
                } else if (body.userData is Items) {
                    val obj = body.userData as Items
                    if (obj.state == Items.STATE_REMOVE) {
                        arrayItems.removeValue(obj, true)
                        Pools.free(obj)
                        worldPhysics.destroyBody(body)
                    }
                }
            }
        }
    }

    private fun updatePlayer(body: Body, delta: Float, accelX: Float, didSwimUp: Boolean, didFire: Boolean) {
        // If you change position I have to do the body again.
        if (oShark.didFlipX) {
            oShark.didFlipX = false
            worldPhysics.destroyBody(body)
            createPlayer(oShark.isFacingLeft)
        }

        if (didFire && oShark.state == Shark.STATE_NORMAL) {
            if (oShark.energy > 0) {
                createBlast()
                if (Settings.isSoundOn) {
                    Assets.blastSound!!.play()
                }
            }
            oShark.fire()
        }

        oShark.update(body, delta, accelX, didSwimUp)
    }

    private fun updateBarrel(body: Body, delta: Float) {
        val obj = body.userData as Barrel
        obj.update(body, delta)
    }

    private fun updateMine(body: Body, delta: Float) {
        val obj = body.userData as Mine
        obj.update(body, delta)
    }

    private fun updateChain(body: Body) {
        val obj = body.userData as Chain
        obj.update(body)
    }

    private fun updateBlast(body: Body, delta: Float) {
        val obj = body.userData as Blast
        obj.update(body, delta)
    }

    private fun updateTorpedo(body: Body, delta: Float) {
        val obj = body.userData as Torpedo
        obj.update(body, delta)
    }

    private fun updateSubmarine(body: Body, delta: Float) {
        val obj = body.userData as Submarine
        obj.update(body, delta)

        if (obj.didFire) {
            obj.didFire = false

            createTorpedo(obj.position.x, obj.position.y, !(obj.speed.x > 0))
        }
    }

    private fun updateItems(body: Body) {
        val obj = body.userData as Items
        obj.update(body)
    }

    internal inner class CollisionHandler : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Shark) {
                collisionCheckOnShark(b)
            } else if (b.body.userData is Shark) {
                collisionCheckOnShark(a)
            } else if (a.body.userData is Blast) {
                beginContactBlast(a, b)
            } else if (b.body.userData is Blast) {
                beginContactBlast(b, a)
            } else {
                startCollisionCheck(a, b)
            }
        }

        private fun beginContactBlast(blastFixture: Fixture, otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData
            val blast = blastFixture.body.userData as Blast

            if (otherObject is Barrel) {
                val obj = otherObject
                if (obj.state == Barrel.STATE_NORMAL) {
                    obj.hit()
                    blast.hit()
                }
            } else if (otherObject is Mine) {
                val obj = otherObject
                if (obj.state == Mine.STATE_NORMAL) {
                    obj.hit()
                    blast.hit()
                }
            } else if (otherObject is Chain) {
                val obj = otherObject
                if (obj.state == Chain.STATE_NORMAL) {
                    obj.hit()
                    blast.hit()
                }
            } else if (otherObject is Submarine) {
                val obj = otherObject
                if (obj.state == Submarine.STATE_NORMAL) {
                    obj.hit()
                    blast.hit()

                    if (obj.state == Submarine.STATE_EXPLODE) {
                        destroyedSubmarines++
                        Achievements.unlockKilledSubmarines()
                    }
                }
            }
        }

        private fun collisionCheckOnShark(otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData

            if (otherObject is Barrel) {
                val obj = otherObject
                if (obj.state == Barrel.STATE_NORMAL) {
                    obj.hit()
                    oShark.hit()
                }
            } else if (otherObject is Mine) {
                val obj = otherObject
                if (obj.state == Mine.STATE_NORMAL) {
                    obj.hit()
                    oShark.hit()
                }
            } else if (otherObject is Torpedo) {
                val obj = otherObject
                if (obj.state == Torpedo.STATE_NORMAL) {
                    obj.hit()
                    oShark.hit()
                    oShark.hit()
                    oShark.hit()
                }
            } else if (otherObject is Items) {
                val obj = otherObject
                if (obj.state == Items.STATE_NORMAL) {
                    if (obj.type == Items.TYPE_MEAT) {
                        oShark.energy += 15f
                    } else {
                        oShark.life += 1
                    }
                    obj.hit()
                }
            }
        }

        fun startCollisionCheck(fixtureA: Fixture, fixtureB: Fixture) {
            val objA = fixtureA.body.userData
            val objB = fixtureB.body.userData

            if (objA is Barrel && objB is Mine) {
                objA.hit()
                objB.hit()
            } else if (objA is Mine && objB is Barrel) {
                objB.hit()
                objA.hit()
            }
        }

        override fun endContact(contact: Contact?) {
        }

        override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
        }

        override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        }
    }

    companion object {

        private const val TIME_TO_GAMEOVER = 2f
        const val STATE_RUNNING = 0
        const val STATE_GAME_OVER = 1
        const val TIME_TO_SPAWN_BARREL = 5f
        const val TIME_TO_SPAWN_MINE = 5f
        const val TIME_TO_SPAWN_CHAIN_MINE = 7f
        const val TIME_TO_SPAWN_SUBMARINE = 15f
        const val TIME_TO_SPAWN_ITEMS = 10f
    }
}
