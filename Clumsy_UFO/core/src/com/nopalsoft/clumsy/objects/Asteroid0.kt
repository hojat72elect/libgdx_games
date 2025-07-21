package com.nopalsoft.clumsy.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.clumsy.game.arcade.WorldGameArcade

abstract class Asteroid0 : Poolable {
    @JvmField
    var state = 0

    @JvmField
    var position = Vector2()

    @JvmField
    var stateTime = 0f

    @JvmField
    var angleDeg = 0f

    abstract fun init(worldGameArcade: WorldGameArcade, x: Float, y: Float)

    abstract fun update(delta: Float, body: Body)

    override fun reset() {
    }

    companion object {
        @JvmField
        var STATE_NORMAL = 0

        @JvmField
        var STATE_DESTROY = 1
    }
}
