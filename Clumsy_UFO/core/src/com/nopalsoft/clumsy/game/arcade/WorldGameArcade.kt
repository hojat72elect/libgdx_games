package com.nopalsoft.clumsy.game.arcade

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.nopalsoft.clumsy.Assets
import com.nopalsoft.clumsy.objects.Asteroid0
import com.nopalsoft.clumsy.objects.Asteroid1
import com.nopalsoft.clumsy.objects.Asteroid2
import com.nopalsoft.clumsy.objects.Asteroid3
import com.nopalsoft.clumsy.objects.Asteroid4
import com.nopalsoft.clumsy.objects.Asteroid5
import com.nopalsoft.clumsy.objects.Asteroid6
import com.nopalsoft.clumsy.objects.ScoreKeeper
import com.nopalsoft.clumsy.objects.Tail
import com.nopalsoft.clumsy.objects.Ufo
import com.nopalsoft.clumsy.screens.Screens
import java.util.Random

class WorldGameArcade {
    val WIDTH: Float = Screens.WORLD_SCREEN_WIDTH.toFloat()
    val HEIGHT: Float = Screens.WORLD_SCREEN_HEIGHT.toFloat()
    val TIME_TO_SPAWN_METEOR: Float = .17f // Time between pipes, change this to increase or decrase gap between pipes.
    val TIME_TO_SPAWN_ARCOIRIS: Float = .005f
    private val arcoirisPool: Pool<Tail> = object : Pool<Tail>() {
        override fun newObject(): Tail {
            return Tail()
        }
    }
    private val meteoro1Pool: Pool<Asteroid1> = object : Pool<Asteroid1>() {
        override fun newObject(): Asteroid1 {
            return Asteroid1()
        }
    }
    private val meteoro2Pool: Pool<Asteroid2> = object : Pool<Asteroid2>() {
        override fun newObject(): Asteroid2 {
            return Asteroid2()
        }
    }
    private val meteoro3Pool: Pool<Asteroid3> = object : Pool<Asteroid3>() {
        override fun newObject(): Asteroid3 {
            return Asteroid3()
        }
    }
    private val meteoro4Pool: Pool<Asteroid4> = object : Pool<Asteroid4>() {
        override fun newObject(): Asteroid4 {
            return Asteroid4()
        }
    }
    private val meteoro5Pool: Pool<Asteroid5> = object : Pool<Asteroid5>() {
        override fun newObject(): Asteroid5 {
            return Asteroid5()
        }
    }
    private val meteoro6Pool: Pool<Asteroid6> = object : Pool<Asteroid6>() {
        override fun newObject(): Asteroid6 {
            return Asteroid6()
        }
    }
    var oWorldBox: World
    var score: Float = 0f
    var state: Int
    var timeToSpawnMeteor: Float = 0f
    var timeToSpawnArcoiris: Float = 0f

    @JvmField
    var oUfo: Ufo? = null

    @JvmField
    var arrMeteoros: Array<Asteroid0?>
    var arrBodies: Array<Body>

    @JvmField
    var arrTail: Array<Tail?>
    var oRan: Random

    init {
        oWorldBox = World(Vector2(0f, -12.8f), true)
        oWorldBox.setContactListener(ColisionesArcade())

        arrMeteoros = Array<Asteroid0?>()
        arrBodies = Array<Body>()
        arrTail = Array<Tail?>()

        createGato()
        createPiso()
        crearTecho()

        state = STATE_RUNNING

        oRan = Random()
    }

    private fun crearTecho() {
        val bd = BodyDef()
        bd.position.x = 0f
        bd.position.y = HEIGHT
        bd.type = BodyType.StaticBody
        val oBody = oWorldBox.createBody(bd)

        val shape = EdgeShape()
        shape.set(0f, 0f, WIDTH, 0f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 0f
        fixture.restitution = 0f
        fixture.friction = 0f

        oBody.createFixture(fixture)

        oBody.isFixedRotation = true

        shape.dispose()
    }

    private fun createPiso() {
        val bd = BodyDef()
        bd.position.x = 0f
        bd.position.y = 1.4f
        bd.type = BodyType.StaticBody
        val oBody = oWorldBox.createBody(bd)

        val shape = EdgeShape()
        shape.set(0f, 0f, WIDTH, 0f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 0f
        fixture.restitution = 0f
        fixture.friction = 0f

        oBody.createFixture(fixture)

        oBody.isFixedRotation = true

        shape.dispose()
    }

    private fun createGato() {
        oUfo = Ufo(WIDTH / 3.2f, HEIGHT / 2f)

        val bd = BodyDef()
        bd.position.x = oUfo!!.position.x
        bd.position.y = oUfo!!.position.y
        bd.type = BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        val shape = CircleShape()
        shape.radius = .19f

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 8f
        fixture.restitution = 0f
        fixture.friction = 0f
        oBody.createFixture(fixture)

        oBody.isFixedRotation = true
        oBody.userData = oUfo
        oBody.isBullet = true

        shape.dispose()
    }

    private fun agregarMetoro() {
        val obj: Asteroid0

        when (oRan.nextInt(6)) {
            0 -> obj = meteoro1Pool.obtain()
            1 -> obj = meteoro2Pool.obtain()
            2 -> obj = meteoro3Pool.obtain()
            3 -> obj = meteoro4Pool.obtain()
            4 -> obj = meteoro5Pool.obtain()
            5 -> obj = meteoro6Pool.obtain()
            else -> obj = meteoro6Pool.obtain()
        }

        obj.init(this, 5f, oRan.nextInt(9).toFloat())
        arrMeteoros.add(obj)
    }

    fun update(delta: Float, jump: Boolean) {
        oWorldBox.step(delta, 8, 4) // para hacer mas lento el juego 1/300f

        cleanupGameObjects()

        timeToSpawnMeteor += delta

        if (timeToSpawnMeteor >= TIME_TO_SPAWN_METEOR) {
            timeToSpawnMeteor -= TIME_TO_SPAWN_METEOR
            agregarMetoro()
        }

        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (body.userData is Ufo) {
                updateUfo(body, delta, jump)
            } else if (body.userData is Asteroid0) {
                updateMetoro(body, delta)
            }
        }

        updateRainbowTail(delta)

        if (oUfo!!.state == Ufo.STATE_NORMAL) {
            score += delta * 5
        }
    }

    private fun updateRainbowTail(delta: Float) {
        timeToSpawnArcoiris += delta

        if (timeToSpawnArcoiris >= TIME_TO_SPAWN_ARCOIRIS) {
            timeToSpawnArcoiris -= TIME_TO_SPAWN_ARCOIRIS
            val tail = arcoirisPool.obtain()
            tail.init(oUfo!!.position.x, oUfo!!.position.y)
            arrTail.add(tail)
        }

        val i: MutableIterator<Tail> = arrTail.iterator() as MutableIterator<Tail>
        while (i.hasNext()) {
            val obj = i.next()
            obj.update(delta)

            if (obj.position.x < -3) {
                i.remove()
                arcoirisPool.free(obj)
            }
        }
    }

    private fun cleanupGameObjects() {
        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (!oWorldBox.isLocked) {
                if (body.userData is Ufo) {
                    val obj = body.userData as Ufo
                    if (obj.state == Ufo.STATE_DEAD && obj.stateTime >= Ufo.DEATH_DURATION) {
                        oWorldBox.destroyBody(body)
                        state = STATE_GAMEOVER
                    }
                } else if (body.userData is Asteroid0) {
                    val obj = body.userData as Asteroid0
                    if (obj.state == Asteroid0.STATE_DESTROY) {
                        oWorldBox.destroyBody(body)
                    }
                }
            }
        }
    }

    private fun updateUfo(body: Body, delta: Float, jump: Boolean) {
        val obj = body.userData as Ufo

        obj.update(delta, body)

        if (jump && obj.state == Ufo.STATE_NORMAL) {
            body.setLinearVelocity(0f, Ufo.JUMP_SPEED)
            Assets.playSound(Assets.wing)
        } else body.setLinearVelocity(0f, body.getLinearVelocity().y)
    }

    private fun updateMetoro(body: Body, delta: Float) {
        if (oUfo!!.state == Ufo.STATE_NORMAL) {
            val obj = body.userData as Asteroid0?
            if (obj != null) {
                obj.update(delta, body)
                if (obj.position.x <= -5) obj.state = Asteroid0.STATE_DESTROY
            }
        } else body.setLinearVelocity(0f, 0f)
    }

    internal class ColisionesArcade : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Ufo) handlePlayerCollisions(a, b)
            else if (b.body.userData is Ufo) handlePlayerCollisions(b, a)
        }

        private fun handlePlayerCollisions(playerFixture: Fixture, otherFixture: Fixture) {
            val oUfo = playerFixture.body.userData as Ufo
            val otherObject = otherFixture.body.userData

            if (otherObject is ScoreKeeper) {
                val obj = otherObject
                if (obj.state == ScoreKeeper.STATE_NORMAL) {
                    obj.state = ScoreKeeper.STATE_DESTROY
                    Assets.playSound(Assets.point)
                }
            } else {
                if (oUfo.state == Ufo.STATE_NORMAL) {
                    oUfo.hurt
                    Assets.playSound(Assets.hit)
                }
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
        const val STATE_RUNNING: Int = 0
        const val STATE_GAMEOVER: Int = 1
    }
}
