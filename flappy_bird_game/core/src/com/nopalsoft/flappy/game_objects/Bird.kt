package com.nopalsoft.flappy.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Bird(x: Float, y: Float) {

    var state = STATE_NORMAL
    val position = Vector2(x, y)
    var stateTime = 0F

    // Update object position to match with the Box2D body.
    fun update(delta: Float, body: Body) {
        position.x = body.position.x
        position.y = body.position.y
        stateTime += delta
    }

    // Is called when the bird crashes with a pipe or ground/ceiling.
    fun hurt() {
        state = STATE_DEAD
        stateTime = 0F
    }

    companion object {
        const val JUMP_SPEED = 5F
        const val STATE_NORMAL = 0
        const val STATE_DEAD = 1
    }
}