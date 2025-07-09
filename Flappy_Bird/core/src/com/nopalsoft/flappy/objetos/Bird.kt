package com.nopalsoft.flappy.objetos

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Bird(x: Float, y: Float) {

    @JvmField
    var state = STATE_NORMAL

    @JvmField
    var position = Vector2(x, y)

    @JvmField
    var stateTime = 0f

    // Update object position to match with the Box2D body.
    fun update(delta: Float, body: Body) {
        position.x = body.position.x
        position.y = body.position.y
        stateTime += delta
    }

    // Called when the bird crash with a pipe
    fun hurt() {
        state = STATE_DEAD
        stateTime = 0f
    }

    companion object {
        @JvmField
        var JUMP_SPEED = 5f

        @JvmField
        var STATE_NORMAL = 0

        @JvmField
        var STATE_DEAD = 1
    }
}
