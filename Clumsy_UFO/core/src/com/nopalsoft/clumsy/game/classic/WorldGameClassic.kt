package com.nopalsoft.clumsy.game.classic

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.nopalsoft.clumsy.Assets
import com.nopalsoft.clumsy.objects.Pipes
import com.nopalsoft.clumsy.objects.ScoreKeeper
import com.nopalsoft.clumsy.objects.Tail
import com.nopalsoft.clumsy.objects.Ufo
import com.nopalsoft.clumsy.screens.Screens
import java.util.Random

class WorldGameClassic {
    val WIDTH: Float = Screens.WORLD_SCREEN_WIDTH.toFloat()
    val HEIGHT: Float = Screens.WORLD_SCREEN_HEIGHT.toFloat()
    val TIME_TO_SPAWN_PIPE: Float = 1.35f // Time between pipes, change this to increase or decrase gap between pipes.
    val TIME_TO_SPAWN_ARCOIRIS: Float = .005f
    private val arcoirisPool: Pool<Tail> = object : Pool<Tail>() {
        override fun newObject(): Tail {
            return Tail()
        }
    }

    @JvmField
    var state: Int
    var oWorldBox: World

    @JvmField
    var score: Int = 0
    var timeToSpawnPipe: Float
    var timeToSpawnArcoiris: Float = 0f

    @JvmField
    var oUfo: Ufo? = null
    var arrTuberias: Array<Pipes?>
    var arrBodies: Array<Body>
    var arrTail: Array<Tail?>
    var oRan: Random


    init {
        oWorldBox = World(Vector2(0f, -13.0f), true)
        oWorldBox.setContactListener(ClassicGameCollisionHandler(this))

        arrTuberias = Array<Pipes?>()
        arrBodies = Array<Body>()
        arrTail = Array<Tail?>()

        timeToSpawnPipe = 1.5f

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

    private fun agregarTuberias() {
        val x = WIDTH + 3
        val y = oRan.nextFloat() * (2.5f) + .5f

        var obj = Pipes(x, y, Pipes.LOWER_PIPE)

        val bd = BodyDef()
        bd.position.x = obj.position.x
        bd.position.y = obj.position.y
        bd.type = BodyType.KinematicBody
        var oBody = oWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Pipes.WIDTH / 2f, Pipes.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 0f
        fixture.restitution = 0f
        fixture.friction = 0f

        oBody.createFixture(fixture)
        oBody.isFixedRotation = true
        oBody.userData = obj

        arrTuberias.add(obj)

        // Tuberia arriba
        obj = Pipes(x, y + 1.7f + Pipes.HEIGHT, Pipes.UPPER_PIPE)
        bd.position.x = obj.position.x
        bd.position.y = obj.position.y
        oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)
        oBody.userData = obj
        oBody.isFixedRotation = true

        arrTuberias.add(obj)

        // Cuandro entre las tuberias
        val bd2 = BodyDef()
        bd2.position.x = obj.position.x
        bd2.position.y = obj.position.y - ScoreKeeper.HEIGHT / 2f - Pipes.HEIGHT / 2f - .035f
        bd2.type = BodyType.KinematicBody
        oBody = oWorldBox.createBody(bd2)

        val shape2 = PolygonShape()
        shape2.setAsBox(ScoreKeeper.WIDTH / 2f, ScoreKeeper.HEIGHT / 2f)
        fixture.isSensor = true
        fixture.shape = shape2
        oBody.createFixture(fixture)
        oBody.userData = ScoreKeeper()
        oBody.isFixedRotation = true

        // Fin Cuadro entre las tuberias
        shape.dispose()
        shape2.dispose()
    }

    fun update(delta: Float, jump: Boolean) {
        oWorldBox.step(delta, 8, 4) // para hacer mas lento el juego 1/300f

        eliminarObjetos()

        timeToSpawnPipe += delta

        if (timeToSpawnPipe >= TIME_TO_SPAWN_PIPE) {
            timeToSpawnPipe -= TIME_TO_SPAWN_PIPE
            agregarTuberias()
        }

        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (body.userData is Ufo) {
                updateGato(body, delta, jump)
            } else if (body.userData is Pipes) {
                updatePlataforma(body, delta)
            } else if (body.userData is ScoreKeeper) {
                updateContador(body)
            }
        }

        updateArcoiris(delta)
    }

    private fun updateArcoiris(delta: Float) {
        timeToSpawnArcoiris += delta

        if (timeToSpawnArcoiris >= TIME_TO_SPAWN_ARCOIRIS) {
            timeToSpawnArcoiris -= TIME_TO_SPAWN_ARCOIRIS
            val oArco = arcoirisPool.obtain()
            oArco.init(oUfo!!.position.x, oUfo!!.position.y)
            arrTail.add(oArco)
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

    private fun eliminarObjetos() {
        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (!oWorldBox.isLocked) {
                if (body.userData is Ufo) {
                    val obj = body.userData as Ufo
                    if (obj.state == Ufo.STATE_DEAD && obj.stateTime >= Ufo.DEATH_DURATION) {
                        oWorldBox.destroyBody(body)
                        state = STATE_GAMEOVER
                    }
                } else if (body.userData is Pipes) {
                    val obj = body.userData as Pipes
                    if (obj.state == Pipes.STATE_DESTROY) {
                        arrTuberias.removeValue(obj, true)
                        oWorldBox.destroyBody(body)
                    }
                } else if (body.userData is ScoreKeeper) {
                    val obj = body.userData as ScoreKeeper
                    if (obj.state == ScoreKeeper.STATE_DESTROY) {
                        oWorldBox.destroyBody(body)
                    }
                }
            }
        }
    }

    private fun updateGato(body: Body, delta: Float, jump: Boolean) {
        val obj = body.userData as Ufo

        obj.update(delta, body)

        if (jump && obj.state == Ufo.STATE_NORMAL) {
            body.setLinearVelocity(0f, Ufo.JUMP_SPEED)
            Assets.playSound(Assets.wing)
        } else body.setLinearVelocity(0f, body.getLinearVelocity().y)
    }

    private fun updatePlataforma(body: Body, delta: Float) {
        if (oUfo!!.state == Ufo.STATE_NORMAL) {
            val obj = body.userData as Pipes?
            if (obj != null) {
                obj.update(delta, body)
                if (obj.position.y <= -5) obj.state = Pipes.STATE_DESTROY

                body.setLinearVelocity(Pipes.SPEED_X, 0f)
            }
        } else body.setLinearVelocity(0f, 0f)
    }

    private fun updateContador(body: Body) {
        if (oUfo!!.state == Ufo.STATE_NORMAL) {
            val obj = body.userData as ScoreKeeper

            if (obj.position.x <= -5) obj.state = ScoreKeeper.STATE_DESTROY

            body.setLinearVelocity(ScoreKeeper.SPEED_X, 0f)
        } else body.setLinearVelocity(0f, 0f)
    }

    companion object {
        const val STATE_RUNNING: Int = 0
        const val STATE_GAMEOVER: Int = 1
    }
}
