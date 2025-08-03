package com.nopalsoft.ponyrace.game_objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.game.TileMapHandler

class Wood(x: Float, y: Float, oWorld: TileMapHandler) : BaseGameObject(x, y) {

    @JvmField
    var stateTime = 0f

    @JvmField
    val TIEMPO_HURT = when (Settings.woodLevel) {
        1 -> 2.5f
        2 -> 2.7f
        3 -> 3f
        4 -> 3.25f
        5 -> 3.5f
        0 -> 2f
        else -> 2f
    }
    var lastStatetime = stateTime

    var angulo = 0F

    @JvmField
    var state = State.NORMAL

    @JvmField
    var tipo = if (oWorld.random.nextBoolean()) Tipo.PLATANO else Tipo.TACHUELA

    fun update(delta: Float, obj: Body) {
        lastStatetime = stateTime
        stateTime += delta

        position.x = obj.getPosition().x
        position.y = obj.getPosition().y
        angulo = MathUtils.radiansToDegrees * obj.angle

        if (state == State.NORMAL && stateTime >= TIEMPO_NORMAL) {
            state = State.HIT
            stateTime = 0f
        }
    }

    fun hitByPony(obj: Body) {
        if (state == State.NORMAL) {
            state = State.HIT
            stateTime = 0f
            obj.setLinearVelocity(0f, 0f)
        }
    }

    enum class Tipo {
        PLATANO, TACHUELA
    }

    enum class State {
        NORMAL, HIT
    }

    companion object {
        const val TIEMPO_NORMAL: Float = 8f
    }
}
