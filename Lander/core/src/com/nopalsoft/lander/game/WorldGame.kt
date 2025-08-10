package com.nopalsoft.lander.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.nopalsoft.lander.Assets
import com.nopalsoft.lander.game.objetos.Bomba
import com.nopalsoft.lander.game.objetos.Estrella
import com.nopalsoft.lander.game.objetos.Gas
import com.nopalsoft.lander.game.objetos.Laser
import com.nopalsoft.lander.game.objetos.Nave
import com.nopalsoft.lander.game.objetos.Plataforma
import java.util.Random

class WorldGame {

    @JvmField
    var oWorldBox: World
    var graivity: Vector2? = Vector2(0f, -4.9f)

    @JvmField
    var state: Int = 0

    @JvmField
    var estrellasTomadas: Int

    @JvmField
    var unitScale: Float = 1 / 100f
    var timeOutOfGas: Float = 0f
    var timeforNextLevel: Float = 0f

    @JvmField
    var oNave: Nave? = null

    @JvmField
    var arrPlataformas: Array<Plataforma?>?

    @JvmField
    var arrEstrellas: Array<Estrella?>

    @JvmField
    var arrGas: Array<Gas?>

    @JvmField
    var arrLaser: Array<Laser?>?

    @JvmField
    var arrBombas: Array<Bomba?>
    var arrBodies: Array<Body>
    var oRan: Random?

    init {
        oWorldBox = World(graivity, true)
        oWorldBox.setContactListener(Colisiones(this))

        arrBodies = Array<Body>()
        arrPlataformas = Array<Plataforma?>()
        arrEstrellas = Array<Estrella?>()
        arrGas = Array<Gas?>()
        arrLaser = Array<Laser?>()
        arrBombas = Array<Bomba?>()
        estrellasTomadas = 0

        TiledMapManagerBox2d(this, unitScale).createObjetosDesdeTiled(Assets.map)
        oRan = Random()
    }

    fun update(delta: Float, accelY: Float, accelX: Float) {
        oWorldBox.step(1 / 60f, 8, 4) // para hacer mas lento el juego 1/300f
        oWorldBox.clearForces()

        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (body.userData is Nave) {
                updateNave(body, delta, accelY, accelX)
            } else if (body.userData is Gas) {
                val obj = body.userData as Gas
                if (obj.state == Gas.STATE_TOMADA && !oWorldBox.isLocked) {
                    oWorldBox.destroyBody(body)
                    arrGas.removeValue(obj, true)
                }
            } else if (body.userData is Estrella) {
                val obj = body.userData as Estrella
                if (obj.state == Estrella.STATE_TOMADA && !oWorldBox.isLocked) {
                    oWorldBox.destroyBody(body)
                    arrEstrellas.removeValue(obj, true)
                }
            } else if (body.userData is Bomba) {
                val obj = body.userData as Bomba
                updateBomba(delta, body)
                if (obj.state == Bomba.STATE_EXPLOSION && obj.stateTime >= Bomba.TIME_EXPLOSION && !oWorldBox.isLocked) {
                    oWorldBox.destroyBody(body)
                    arrBombas.removeValue(obj, true)
                }
            } else if (body.userData is Laser) {
                updateLaser(delta, body)
            }
        }

        if (oNave!!.gas <= 0 && state == STATE_RUNNING) {
            timeOutOfGas += delta
            if (timeOutOfGas >= TIME_OUT_OF_GAS) state = STATE_GAME_OVER
        }

        if (oNave!!.isLanded) {
            timeforNextLevel += delta
            if (timeforNextLevel >= TIME_FOR_NEXT_LEVEL) state = STATE_NEXT_LEVEL
        } else {
            timeforNextLevel = 0f
        }
    }

    private fun updateNave(body: Body, delta: Float, accelY: Float, accelX: Float) {
        val obj = body.userData as Nave
        if (obj.state == Nave.STATE_EXPLODE && obj.stateTime > Nave.EXPLODE_TIME && !oWorldBox.isLocked) {
            oWorldBox.destroyBody(body)
            state = STATE_GAME_OVER
            return
        }

        obj.update(delta, body, accelX, accelY)
    }

    private fun updateBomba(delta: Float, body: Body) {
        val obj = body.userData as Bomba
        obj.update(delta, body)

        if (obj.state == Bomba.STATE_NORMAL) body.linearVelocity = obj.velocidad
        else body.setLinearVelocity(0f, 0f)
    }

    private fun updateLaser(delta: Float, body: Body) {
        val obj = body.userData as Laser
        obj.update(delta, body)

        if (obj.isTouchingShip && oNave!!.state == Nave.STATE_NORMAL) {
            if (obj.state == Laser.STATE_FIRE) {
                oNave!!.getHurtByLaser(25f)
                val blastDirection = body.getWorldCenter().sub(body.getWorldCenter())
                blastDirection.nor()
                body.applyLinearImpulse(blastDirection.scl(.1f), body.getWorldCenter(), true)
            }
        }
    }

    companion object {
        var STATE_RUNNING = 0

        @JvmField
        var STATE_GAME_OVER = 2

        @JvmField
        var STATE_NEXT_LEVEL = 3
        private const val TIME_OUT_OF_GAS = 1.5f
        private const val TIME_FOR_NEXT_LEVEL = .75f
    }
}
