package com.nopalsoft.ponyrace.game_objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Body
import com.esotericsoftware.spine.Skeleton
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.game.TileMapHandler

class Bomb(x: Float, y: Float, world: TileMapHandler) : BaseGameObject(x, y) {
    @JvmField
    val bombTimerSeconds = when (Settings.bombLevel) {
        1 -> 2.5F
        2 -> 2.7F
        3 -> 3F
        4 -> 3.25F
        5 -> 3.5F
        0 -> 2F
        else -> 2F
    }

    @JvmField
    var stateTime = 0F

    @JvmField
    var lastStatetime = stateTime

    var angulo = 0F

    @JvmField
    var state = State.NORMAL

    @JvmField
    var skelBomb = Skeleton(world.game.assetsHandler!!.skeletonBombData)

    init {
        skelBomb.setToSetupPose()
    }

    fun update(delta: Float, obj: Body) {
        lastStatetime = stateTime
        stateTime += delta

        position.x = obj.getPosition().x
        position.y = obj.getPosition().y
        angulo = MathUtils.radiansToDegrees * obj.angle

        if (state == State.NORMAL && stateTime >= TIEMPO_NORMAL) {
            state = State.EXPLODE
            stateTime = 0f
        }
    }

    fun explode(obj: Body) {
        if (state == State.NORMAL) {
            state = State.EXPLODE
            stateTime = 0f
            obj.setLinearVelocity(0f, 0f)
        }
    }

    enum class State {
        NORMAL,
        EXPLODE
    }

    companion object {
        const val TIEMPO_NORMAL = 1.5F
        const val TIEMPO_EXPLOSION = .3F
    }
}
