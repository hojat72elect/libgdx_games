package com.nopalsoft.sharkadventure.game

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
import com.nopalsoft.sharkadventure.Achievements
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.Settings
import com.nopalsoft.sharkadventure.objects.Barrel
import com.nopalsoft.sharkadventure.objects.Blast
import com.nopalsoft.sharkadventure.objects.Chain
import com.nopalsoft.sharkadventure.objects.GameItem
import com.nopalsoft.sharkadventure.objects.Mine
import com.nopalsoft.sharkadventure.objects.Missile
import com.nopalsoft.sharkadventure.objects.Shark
import com.nopalsoft.sharkadventure.objects.Submarine
import com.nopalsoft.sharkadventure.screens.BaseScreen

class WorldGame {
    var state: Int

    private var timeToGameOver: Float
    private var timeToSpawnBarrel: Float
    private var timeToSpawnMine: Float = 0f
    private var timeToSpawnMineChain: Float = 0f
    private var timeToSpawnSubmarine: Float = 0f
    private var timeToSpawnItems: Float = 0f

    var world: World = World(Vector2(0f, -4f), true)
    var shark: Shark
    var arrayBarrels: Array<Barrel>
    var arrayBodies: Array<Body>
    var arrayMines: Array<Mine>
    var arrayChains: Array<Chain>
    var arrayBlasts: Array<Blast>
    var arrayMissiles: Array<Missile>
    var arraySubmarines: Array<Submarine>
    var arrayItems: Array<GameItem>

    var score: Double

    var destroyedSubmarines: Int

    init {
        world.setContactListener(Colisiones())

        state = STATE_RUNNING
        timeToGameOver = 0f
        score = 0.0
        destroyedSubmarines = 0

        arrayBodies = Array()
        arrayBarrels = Array()
        arrayMines = Array()
        arrayChains = Array()
        arrayBlasts = Array()
        arrayMissiles = Array()
        arraySubmarines = Array()
        arrayItems = Array()
        shark = Shark(3.5f, 2f)

        timeToSpawnBarrel = 0f
        createMineChain()
        createWalls()
        createCharacter(false)
    }

    private fun createWalls() {
        val bd = BodyDef()
        bd.type = BodyType.StaticBody

        val body = world.createBody(bd)

        val shape = EdgeShape()

        // Below
        shape[0f, 0f, BaseScreen.WORLD_WIDTH] = 0f
        body.createFixture(shape, 0f)

        // Right
        shape[BaseScreen.WORLD_WIDTH + .5f, 0f, BaseScreen.WORLD_WIDTH + .5f] = BaseScreen.WORLD_HEIGHT
        body.createFixture(shape, 0f)

        // Above
        shape[0f, BaseScreen.WORLD_HEIGHT + .5f, BaseScreen.WORLD_WIDTH] = BaseScreen.WORLD_HEIGHT + .5f
        body.createFixture(shape, 0f)

        // Left
        shape[-.5f, 0f, -.5f] = BaseScreen.WORLD_HEIGHT
        body.createFixture(shape, 0f)

        for (fix in body.fixtureList) {
            fix.friction = 0f
            val filterData = Filter()
            filterData.groupIndex = -1
            fix.filterData = filterData
        }

        body.userData = "piso"

        shape.dispose()
    }

    private fun createCharacter(isFacingLeft: Boolean) {
        val bd = BodyDef()
        bd.position[shark.position.x] = shark.position.y
        bd.type = BodyType.DynamicBody

        val body = world.createBody(bd)
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

        body.userData = shark
        body.isFixedRotation = true
        body.gravityScale = .45f

        shape.dispose()
    }

    private fun crearBarril(x: Float, y: Float) {
        val obj = Pools.obtain(Barrel::class.java)
        obj.initialize(x, y)

        val bd = BodyDef()
        bd.position[obj.position.x] = obj.position.y
        bd.type = BodyType.DynamicBody

        val body = world.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Barrel.WIDTH / 2f, Barrel.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true

        body.createFixture(fixture)
        body.userData = obj
        body.isFixedRotation = true
        body.gravityScale = .15f
        body.angularVelocity = MathUtils.degRad * MathUtils.random(-50, 50)

        arrayBarrels.add(obj)
        shape.dispose()
    }

    private fun createItem() {
        val obj = Pools.obtain(GameItem::class.java)
        obj.initialize(BaseScreen.WORLD_WIDTH + 1, MathUtils.random(BaseScreen.WORLD_HEIGHT))

        val bd = BodyDef()
        bd.position[obj.position.x] = obj.position.y
        bd.type = BodyType.KinematicBody

        val body = world.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(GameItem.WIDTH / 2f, GameItem.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true

        body.createFixture(fixture)
        body.userData = obj
        body.isFixedRotation = true
        body.setLinearVelocity(GameItem.SPEED_X, 0f)

        arrayItems.add(obj)
        shape.dispose()
    }

    private fun createBlast() {
        var velX = Blast.SPEED_X
        var x = shark.position.x + .3f

        if (shark.isFacingLeft) {
            velX = -Blast.SPEED_X
            x = shark.position.x - .3f
        }
        val obj = Pools.obtain(Blast::class.java)

        obj.initialize(x, shark.position.y - .15f)

        val bd = BodyDef()
        bd.position[obj.position.x] = obj.position.y
        bd.type = BodyType.KinematicBody

        val body = world.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Blast.WIDTH / 2f, Blast.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true

        body.isBullet = true
        body.createFixture(fixture)
        body.userData = obj
        body.isFixedRotation = true
        body.setLinearVelocity(velX, 0f)

        arrayBlasts.add(obj)
        shape.dispose()
    }

    private fun crearTorpedo(x: Float, y: Float, goLeft: Boolean) {
        var velX = Missile.SPEED_X
        if (goLeft) {
            velX = -Missile.SPEED_X
        }
        val obj = Pools.obtain(Missile::class.java)
        obj.initialize(x, y, goLeft)

        val bd = BodyDef()
        bd.position[obj.position.x] = obj.position.y
        bd.type = BodyType.DynamicBody

        val body = world.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Blast.WIDTH / 2f, Blast.HEIGHT / 2f)

        val fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.isSensor = true

        body.createFixture(fixutre)
        body.userData = obj
        body.gravityScale = 0f
        body.isFixedRotation = true
        body.setLinearVelocity(velX, 0f)

        arrayMissiles.add(obj)
        shape.dispose()
    }

    private fun crearMina(x: Float, y: Float) {
        val obj = Pools.obtain(Mine::class.java)
        obj.initialize(x, y)

        val bd = BodyDef()
        bd.position[obj.position.x] = obj.position.y
        bd.type = BodyType.DynamicBody

        val body = world.createBody(bd)

        val shape = CircleShape()
        shape.radius = Mine.WIDTH / 2f

        val fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.isSensor = true

        body.createFixture(fixutre)
        body.userData = obj
        body.isFixedRotation = true
        body.gravityScale = 0f
        body.setLinearVelocity(Mine.SPEED_X.toFloat(), 0f)

        arrayMines.add(obj)
        shape.dispose()
    }

    private fun createSubmarine() {
        val obj = Pools.obtain(Submarine::class.java)
        val x: Float
        val y: Float
        val xTarget: Float
        val yTarget: Float
        when (MathUtils.random(1, 4)) {
            1 -> {
                x = -1f
                y = -1f
                xTarget = BaseScreen.WORLD_WIDTH + 6
                yTarget = BaseScreen.WORLD_HEIGHT + 6
            }

            2 -> {
                x = -1f
                y = BaseScreen.WORLD_HEIGHT + 1
                xTarget = BaseScreen.WORLD_WIDTH + 6
                yTarget = -6f
            }

            3 -> {
                x = BaseScreen.WORLD_WIDTH + 1
                y = BaseScreen.WORLD_HEIGHT + 1
                xTarget = -6f
                yTarget = -6f
            }

            4 -> {
                x = BaseScreen.WORLD_WIDTH + 1
                y = -1f
                xTarget = -6f
                yTarget = BaseScreen.WORLD_HEIGHT + 6
            }

            else -> {
                x = BaseScreen.WORLD_WIDTH + 1
                y = -1f
                xTarget = -6f
                yTarget = BaseScreen.WORLD_HEIGHT + 6
            }
        }
        obj.initialize(x, y, xTarget, yTarget)

        val bd = BodyDef()
        bd.position[obj.position.x] = obj.position.y
        bd.type = BodyType.DynamicBody

        val body = world.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Submarine.WIDTH / 2f, Submarine.HEIGHT / 2f)

        val fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.isSensor = true

        body.createFixture(fixutre)
        body.userData = obj
        body.isFixedRotation = true
        body.gravityScale = 0f
        arraySubmarines.add(obj)
        shape.dispose()
    }

    private fun createMineChain() {
        val x = 10f
        val obj = Pools.obtain(Mine::class.java)
        obj.initialize(x, 1f)
        obj.type = Mine.TYPE_GRAY

        val bodyDef = BodyDef()
        bodyDef.position[obj.position.x] = obj.position.y
        bodyDef.type = BodyType.DynamicBody

        val body = world.createBody(bodyDef)

        val shape = CircleShape()
        shape.radius = Mine.WIDTH / 2f

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true
        fixture.density = 1f

        body.createFixture(fixture)
        body.userData = obj
        body.isFixedRotation = true
        body.gravityScale = -3.5f

        arrayMines.add(obj)
        shape.dispose()

        val chainShape = PolygonShape()
        chainShape.setAsBox(Chain.WIDTH / 2f, Chain.HEIGHT / 2f)

        fixture.isSensor = false
        fixture.shape = chainShape
        fixture.filter.groupIndex = -1

        val numLinks = MathUtils.random(5, 15)
        var link: Body? = null
        for (i in 0 until numLinks) {
            val objChain = Pools.obtain(Chain::class.java)
            objChain.initialize(x, Chain.HEIGHT * i)
            bodyDef.position[objChain.position.x] = objChain.position.y
            if (i == 0) {
                objChain.initialize(x, -.12f) // It makes the kinematic body appear a little lower so as not to be colliding with it.
                bodyDef.position[objChain.position.x] = objChain.position.y
                bodyDef.type = BodyType.KinematicBody
                link = world.createBody(bodyDef)
                link.createFixture(fixture)
                link.setLinearVelocity(Chain.SPEED_X, 0f)
            } else {
                bodyDef.type = BodyType.DynamicBody
                val newLink = world.createBody(bodyDef)
                newLink.createFixture(fixture)
                createRotationJoint(link, newLink, -Chain.HEIGHT / 2f)
                link = newLink
            }
            arrayChains.add(objChain)
            link!!.userData = objChain
        }

        createRotationJoint(link, body, -Mine.HEIGHT / 2f)
    }

    private fun createRotationJoint(bodyA: Body?, bodyB: Body, anchorBY: Float) {
        val jointDef = RevoluteJointDef()
        jointDef.localAnchorA[0f] = 0.105.toFloat()
        jointDef.localAnchorB[0f] = anchorBY
        jointDef.bodyA = bodyA
        jointDef.bodyB = bodyB
        world.createJoint(jointDef)
    }

    fun update(delta: Float, accelX: Float, didSwimUp: Boolean, didFire: Boolean) {
        world.step(delta, 8, 4)

        eliminarObjetos()

        timeToSpawnBarrel += delta
        if (timeToSpawnBarrel >= TIME_TO_SPAWN_BARREL) {
            timeToSpawnBarrel -= TIME_TO_SPAWN_BARREL

            if (MathUtils.randomBoolean()) {
                for (i in 0..5) {
                    crearBarril(MathUtils.random(BaseScreen.WORLD_WIDTH), MathUtils.random(5.5f, 8.5f))
                }
            }
        }

        timeToSpawnMine += delta
        if (timeToSpawnMine >= TIME_TO_SPAWN_MINE) {
            timeToSpawnMine -= TIME_TO_SPAWN_MINE
            for (i in 0..4) {
                if (MathUtils.randomBoolean()) crearMina(MathUtils.random(9f, 10f), MathUtils.random(BaseScreen.WORLD_HEIGHT))
            }
        }

        if (arrayMines.size == 0) crearMina(9f, MathUtils.random(BaseScreen.WORLD_HEIGHT))

        timeToSpawnMineChain += delta
        if (timeToSpawnMineChain >= TIME_TO_SPAWN_MINE_CHAIN) {
            timeToSpawnMineChain -= TIME_TO_SPAWN_MINE_CHAIN
            if (MathUtils.randomBoolean(.75f)) createMineChain()
        }

        timeToSpawnSubmarine += delta
        if (timeToSpawnSubmarine >= TIME_TO_SPAWN_SUBMARINE) {
            timeToSpawnSubmarine -= TIME_TO_SPAWN_SUBMARINE
            if (MathUtils.randomBoolean(.65f)) {
                createSubmarine()
                if (Settings.isSoundOn) Assets.soundSonar!!.play()
            }
        }

        timeToSpawnItems += delta
        if (timeToSpawnItems >= TIME_TO_SPAWN_ITEMS) {
            timeToSpawnItems -= TIME_TO_SPAWN_ITEMS
            if (MathUtils.randomBoolean()) {
                createItem()
            }
        }

        world.getBodies(arrayBodies)

        val i: Iterator<Body> = arrayBodies.iterator()
        while (i.hasNext()) {
            val body = i.next()

            when (body.userData) {
                is Shark -> {
                    updatePersonaje(body, delta, accelX, didSwimUp, didFire)
                }

                is Barrel -> {
                    updateBarrels(body, delta)
                }

                is Mine -> {
                    updateMine(body, delta)
                }

                is Chain -> {
                    updateChain(body)
                }

                is Blast -> {
                    updateBlast(body, delta)
                }

                is Missile -> {
                    updateMissile(body, delta)
                }

                is Submarine -> {
                    updateSubmarine(body, delta)
                }

                is GameItem -> {
                    updateItems(body)
                }
            }
        }

        if (shark.state == Shark.STATE_DEAD) {
            timeToGameOver += delta
            if (timeToGameOver >= TIME_TO_GAME_OVER) {
                state = STATE_GAME_OVER
            }
        } else {
            score += (delta * 15).toDouble()
        }

        Achievements.distance(score.toLong(), shark.didGetHurtOnce)
    }

    private fun eliminarObjetos() {
        val i: Iterator<Body> = arrayBodies.iterator()
        while (i.hasNext()) {
            val body = i.next()

            if (!world.isLocked) {
                if (body.userData is Barrel) {
                    val obj = body.userData as Barrel
                    if (obj.state == Barrel.STATE_REMOVE) {
                        arrayBarrels.removeValue(obj, true)
                        Pools.free(obj)
                        world.destroyBody(body)
                    }
                } else if (body.userData is Mine) {
                    val obj = body.userData as Mine
                    if (obj.state == Mine.STATE_REMOVE) {
                        arrayMines.removeValue(obj, true)
                        Pools.free(obj)
                        world.destroyBody(body)
                    }
                } else if (body.userData is Chain) {
                    val obj = body.userData as Chain
                    if (obj.state == Chain.STATE_REMOVE) {
                        arrayChains.removeValue(obj, true)
                        Pools.free(obj)
                        world.destroyBody(body)
                    }
                } else if (body.userData is Blast) {
                    val obj = body.userData as Blast
                    if (obj.state == Blast.STATE_REMOVE) {
                        arrayBlasts.removeValue(obj, true)
                        Pools.free(obj)
                        world.destroyBody(body)
                    }
                } else if (body.userData is Missile) {
                    val obj = body.userData as Missile
                    if (obj.state == Missile.STATE_REMOVE) {
                        arrayMissiles.removeValue(obj, true)
                        Pools.free(obj)
                        world.destroyBody(body)
                    }
                } else if (body.userData is Submarine) {
                    val obj = body.userData as Submarine
                    if (obj.state == Submarine.STATE_REMOVE) {
                        arraySubmarines.removeValue(obj, true)
                        Pools.free(obj)
                        world.destroyBody(body)
                    }
                } else if (body.userData is GameItem) {
                    val obj = body.userData as GameItem
                    if (obj.state == GameItem.STATE_REMOVE) {
                        arrayItems.removeValue(obj, true)
                        Pools.free(obj)
                        world.destroyBody(body)
                    }
                }
            }
        }
    }

    private fun updatePersonaje(body: Body, delta: Float, accelX: Float, didSwimUp: Boolean, didFire: Boolean) {
        // If I change position I have to do the body again.
        if (shark.didFlipX) {
            shark.didFlipX = false
            world.destroyBody(body)
            createCharacter(shark.isFacingLeft)
        }

        if (didFire && shark.state == Shark.STATE_NORMAL) {
            if (shark.energy > 0) {
                createBlast()
                if (Settings.isSoundOn) {
                    Assets.soundBlast!!.play()
                }
            }
            shark.fire()
        }

        shark.update(body, delta, accelX, didSwimUp)
    }

    private fun updateBarrels(body: Body, delta: Float) {
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

    private fun updateMissile(body: Body, delta: Float) {
        val obj = body.userData as Missile
        obj.update(body, delta)
    }

    private fun updateSubmarine(body: Body, delta: Float) {
        val obj = body.userData as Submarine
        obj.update(body, delta)

        if (obj.didFire) {
            obj.didFire = false

            if (obj.velocity.x > 0) crearTorpedo(obj.position.x, obj.position.y, false)
            else crearTorpedo(obj.position.x, obj.position.y, true)
        }
    }

    private fun updateItems(body: Body) {
        val obj = body.userData as GameItem
        obj.update(body)
    }

    internal inner class Colisiones : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Shark) {
                beginContactTiburon(b)
            } else if (b.body.userData is Shark) {
                beginContactTiburon(a)
            } else if (a.body.userData is Blast) {
                beginContactBlast(a, b)
            } else if (b.body.userData is Blast) {
                beginContactBlast(b, a)
            } else {
                beginContactOtrasCosas(a, b)
            }
        }

        private fun beginContactBlast(fixBlast: Fixture, fixOtraCosa: Fixture) {
            val otraCosa = fixOtraCosa.body.userData
            val oBlast = fixBlast.body.userData as Blast

            if (otraCosa is Barrel) {
                if (otraCosa.state == Barrel.STATE_NORMAL) {
                    otraCosa.hit()
                    oBlast.hit()
                }
            } else if (otraCosa is Mine) {
                if (otraCosa.state == Mine.STATE_NORMAL) {
                    otraCosa.hit()
                    oBlast.hit()
                }
            } else if (otraCosa is Chain) {
                if (otraCosa.state == Chain.STATE_NORMAL) {
                    otraCosa.hit()
                    oBlast.hit()
                }
            } else if (otraCosa is Submarine) {
                if (otraCosa.state == Submarine.STATE_NORMAL) {
                    otraCosa.hit()
                    oBlast.hit()

                    if (otraCosa.state == Submarine.STATE_EXPLODE) {
                        destroyedSubmarines++
                        Achievements.unlockKilledSubmarines()
                    }
                }
            }
        }

        private fun beginContactTiburon(fixOtraCosa: Fixture) {
            val otraCosa = fixOtraCosa.body.userData

            if (otraCosa is Barrel) {
                if (otraCosa.state == Barrel.STATE_NORMAL) {
                    otraCosa.hit()
                    shark.hit()
                }
            } else if (otraCosa is Mine) {
                if (otraCosa.state == Mine.STATE_NORMAL) {
                    otraCosa.hit()
                    shark.hit()
                }
            } else if (otraCosa is Missile) {
                if (otraCosa.state == Missile.STATE_NORMAL) {
                    otraCosa.hit()
                    shark.hit()
                    shark.hit()
                    shark.hit()
                }
            } else if (otraCosa is GameItem) {
                if (otraCosa.state == GameItem.STATE_NORMAL) {
                    if (otraCosa.type == GameItem.TYPE_MEAT) {
                        shark.energy += 15f
                    } else {
                        shark.life += 1
                    }
                    otraCosa.hit()
                }
            }
        }

        private fun beginContactOtrasCosas(fixA: Fixture, fixB: Fixture) {
            val objA = fixA.body.userData
            val objB = fixB.body.userData

            if (objA is Barrel && objB is Mine) {
                objA.hit()
                objB.hit()
            } else if (objA is Mine && objB is Barrel) {
                objB.hit()
                objA.hit()
            }
        }

        override fun endContact(contact: Contact) {
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold) {
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse) {
        }
    }

    companion object {
        const val STATE_RUNNING = 0
        const val STATE_GAME_OVER = 1

        const val TIME_TO_SPAWN_BARREL = 5f
        const val TIME_TO_SPAWN_MINE = 5f
        const val TIME_TO_SPAWN_MINE_CHAIN = 7f
        const val TIME_TO_SPAWN_SUBMARINE = 15f
        const val TIME_TO_SPAWN_ITEMS = 10f
        private const val TIME_TO_GAME_OVER = 2f
    }
}
