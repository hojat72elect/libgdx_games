package com.nopalsoft.ponyrace.game_objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Body
import com.esotericsoftware.spine.Skeleton
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.game.TileMapHandler

class Bomb(x: Float, y: Float, world: TileMapHandler) : BaseGameObject(x, y) {
    @JvmField
    val TIEMPO_HURT = when (Settings.bombLevel) {
        1 -> 2.5f
        2 -> 2.7f
        3 -> 3f
        4 -> 3.25f
        5 -> 3.5f
        0 -> 2f
        else -> 2f
    }

    @JvmField
    var lastStatetime: Float

    @JvmField
    var stateTime: Float = 0f
    var angulo: Float = 0f

    @JvmField
    var state: State?

    @JvmField
    var skelBomb: Skeleton?

    init {
        lastStatetime = stateTime
        state = State.NORMAL
        skelBomb = Skeleton(world.game.assetsHandler.skeletonBombData)
        skelBomb!!.setToSetupPose()


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
