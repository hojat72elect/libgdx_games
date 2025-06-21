package com.nopalsoft.slamthebird.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Pool.Poolable

class Coin : Poolable {
    @JvmField
    var state: Int = 0

    @JvmField
    var position: Vector2 = Vector2()

    @JvmField
    var stateTime: Float = 0f

    fun init(x: Float, y: Float) {
        position[x] = y
        stateTime = 0f
        state = STATE_NORMAL
    }

    fun update(delta: Float, body: Body) {
        position.x = body.position.x
        position.y = body.position.y
        stateTime += delta
    }

    override fun reset() {
    }

    companion object {
        private const val RADIUS = .15f

        @JvmField
        var MOVE_SPEED = 1f

        const val STATE_NORMAL = 0
        const val STATE_TAKEN = 1

        @JvmStatic
        fun createCoinBody(world: World, x: Float, y: Float, velocityX: Float): Body {
            val bodyDefinition = BodyDef()
            bodyDefinition.position.x = x
            bodyDefinition.position.y = y
            bodyDefinition.type = BodyType.DynamicBody

            val body = world.createBody(bodyDefinition)

            val shape = CircleShape()
            shape.radius = RADIUS

            val fixtureDefinition = FixtureDef()
            fixtureDefinition.shape = shape
            fixtureDefinition.density = 1f
            fixtureDefinition.restitution = .5f
            fixtureDefinition.friction = 0f
            fixtureDefinition.filter.groupIndex = -1

            body.gravityScale = 0f
            body.createFixture(fixtureDefinition)
            body.setLinearVelocity(velocityX, 0f)

            return body
        }
    }
}
