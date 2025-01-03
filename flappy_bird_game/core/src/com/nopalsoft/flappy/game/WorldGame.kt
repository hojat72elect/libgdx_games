package com.nopalsoft.flappy.game

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
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.nopalsoft.flappy.game_objects.Bird
import com.nopalsoft.flappy.game_objects.Counter
import com.nopalsoft.flappy.game_objects.Pipe
import com.nopalsoft.flappy.screens.Screens
import com.badlogic.gdx.utils.Array as GdxArray

class WorldGame {

    private val width = Screens.WORLD_WIDTH
    private val height = Screens.WORLD_HEIGHT
    var state = STATE_RUNNING
    private var worldGame = World(Vector2(0F, -13F), true)
    var score = 0
    private var timeToSpawnPipe = 1.5F

    lateinit var bird: Bird
    var pipes = GdxArray<Pipe>()

    // All bodies including bird, pipes, and also the counter object.
    private var bodies = GdxArray<Body>()

    init {
        worldGame.setContactListener(Collisions())
        createBird()
        createRoof()
        createFloor()
    }

    private fun createBird() {
        bird = Bird(1.35F, 4.75F)

        val bd = BodyDef()
        bd.position.x = bird.position.x
        bd.position.y = bird.position.y
        bd.type = BodyType.DynamicBody

        val oBody = worldGame.createBody(bd)

        val shape = CircleShape()
        shape.radius = .25f

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 8f
        oBody.createFixture(fixture)

        oBody.isFixedRotation = true
        oBody.userData = bird
        oBody.isBullet = true

        shape.dispose()
    }

    private fun createRoof() {
        val bd = BodyDef()
        bd.position.x = 0f
        bd.position.y = height
        bd.type = BodyType.StaticBody
        val oBody = worldGame.createBody(bd)

        val shape = EdgeShape()
        shape[0f, 0f, width] = 0f

        val fixture = FixtureDef()
        fixture.shape = shape

        oBody.createFixture(fixture)
        shape.dispose()
    }

    private fun createFloor() {
        val bd = BodyDef()
        bd.position.x = 0f
        bd.position.y = 1.1f
        bd.type = BodyType.StaticBody
        val body = worldGame.createBody(bd)

        val shape = EdgeShape()
        shape[0f, 0f, width] = 0f

        val fixture = FixtureDef()
        fixture.shape = shape

        body.createFixture(fixture)
        shape.dispose()
    }

    private fun addPipe() {
        val x = width + 2.5f
        val y = MathUtils.random() * (1.5f) + .4f

        // Add the bottom pipe
        addPipe(x, y, false)

        // Add the top pipe
        addPipe(x, y + 2f + Pipe.HEIGHT, true)

        //add counter object (between the two pipes)
        addCounter(x, y + Counter.HEIGHT / 2f + Pipe.HEIGHT / 2f + .1f)
    }

    private fun addPipe(x: Float, y: Float, isTopPipe: Boolean) {
        val obj = if (isTopPipe) Pipe(x, y, Pipe.TYPE_UP)
        else Pipe(x, y, Pipe.TYPE_DOWN)

        val bd = BodyDef()
        bd.position.x = x
        bd.position.y = y
        bd.type = BodyType.KinematicBody
        val oBody = worldGame.createBody(bd)
        oBody.setLinearVelocity(Pipe.SPEED_X, 0f)

        val shape = PolygonShape()
        shape.setAsBox(Pipe.WIDTH / 2f, Pipe.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape

        oBody.createFixture(fixture)
        oBody.isFixedRotation = true
        oBody.userData = obj
        pipes.add(obj)
        shape.dispose()
    }

    private fun addCounter(x: Float, y: Float) {
        val obj = Counter()
        val bd = BodyDef()
        bd.position.x = x
        bd.position.y = y
        bd.type = BodyType.KinematicBody
        val oBody = worldGame.createBody(bd)
        oBody.setLinearVelocity(Counter.SPEED_X, 0f)

        val shape = PolygonShape()
        shape.setAsBox(Counter.WIDTH / 2f, Counter.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true

        oBody.createFixture(fixture)
        oBody.isFixedRotation = true
        oBody.userData = obj

        shape.dispose()
    }

    fun update(delta: Float, jump: Boolean) {
        worldGame.step(delta, 8, 4)

        deleteObjects()

        timeToSpawnPipe += delta

        if (timeToSpawnPipe >= TIME_TO_SPAWN_PIPE) {
            timeToSpawnPipe -= TIME_TO_SPAWN_PIPE
            addPipe()
        }

        worldGame.getBodies(bodies)

        for (body in bodies) {
            if (body.userData is Bird) {
                updateBird(body, delta, jump)
            } else if (body.userData is Pipe) {
                updatePipes(body)
            } else if (body.userData is Counter) {
                updateCounter(body)
            }
        }

        if (bird.state == Bird.STATE_DEAD) state = STATE_GAME_OVER
    }

    private fun updateBird(body: Body, delta: Float, jump: Boolean) {
        bird.update(delta, body)

        if (jump && bird.state == Bird.STATE_NORMAL) {
            body.setLinearVelocity(0f, Bird.JUMP_SPEED)
        }
    }

    private fun updatePipes(body: Body) {
        if (bird.state == Bird.STATE_NORMAL) {
            val obj = body.userData as Pipe

            obj.update(body)
            if (obj.position.x <= -5) obj.state = Pipe.STATE_REMOVE
        } else body.setLinearVelocity(0f, 0f)
    }

    private fun updateCounter(body: Body) {
        if (bird.state == Bird.STATE_NORMAL) {
            val obj = body.userData as Counter

            obj.update(body)
            if (obj.position.x <= -5) obj.state = Counter.STATE_REMOVE
        } else body.setLinearVelocity(0f, 0f)
    }

    private fun deleteObjects() {
        worldGame.getBodies(bodies)

        for (body in bodies) {
            if (!worldGame.isLocked) {
                if (body.userData is Pipe) {
                    val obj = body.userData as Pipe
                    if (obj.state == Pipe.STATE_REMOVE) {
                        pipes.removeValue(obj, true)
                        worldGame.destroyBody(body)
                    }
                } else if (body.userData is Counter) {
                    val obj = body.userData as Counter
                    if (obj.state == Counter.STATE_REMOVE) {
                        worldGame.destroyBody(body)
                    }
                }
            }
        }
    }

    internal inner class Collisions : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Bird) beginContactBird(b)
            else if (b.body.userData is Bird) beginContactBird(a)
        }

        private fun beginContactBird(otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData

            if (otherObject is Counter) {
                val obj = otherObject
                if (obj.state == Counter.STATE_NORMAL) {
                    obj.state = Counter.STATE_REMOVE
                    score++
                }
            } else {
                if (bird.state == Bird.STATE_NORMAL) {
                    bird.hurt()
                }
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
        const val STATE_RUNNING: Int = 0
        const val STATE_GAME_OVER: Int = 1

        // If you increase this number the space between pipes will increase
        private const val TIME_TO_SPAWN_PIPE = 1.5F
    }
}
