package com.nopalsoft.slamthebird.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.slamthebird.game.WorldGame

class PowerUp : Poolable {

    var state = 0
    var position = Vector2()
    private var stateTime = 0f
    var type = 0

    fun init(oWorld: WorldGame, x: Float, y: Float, type: Int) {
        this.type = type
        position[x] = y
        stateTime = 0f
        state = STATE_NORMAL

        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = x
        bodyDefinition.position.y = y
        bodyDefinition.type = BodyType.KinematicBody

        val body = oWorld.world.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(.15f, .15f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.density = 8f
        fixtureDefinition.restitution = 0f
        fixtureDefinition.friction = 0f
        fixtureDefinition.isSensor = true

        body.createFixture(fixtureDefinition)

        body.userData = this
        shape.dispose()
    }

    fun update(delta: Float, body: Body) {
        position.x = body.position.x
        position.y = body.position.y
        stateTime += delta

        if (stateTime >= DURATION_AVAILABLE) {
            state = STATE_TAKEN
            stateTime = 0f
        }
    }

    fun hit() {
        state = STATE_TAKEN
        stateTime = 0f
    }

    override fun reset() {
    }

    companion object {
        const val TYPE_SUPER_JUMP = 0
        const val TYPE_INVINCIBLE = 1
        const val TYPE_COIN_RAIN = 2
        const val TYPE_FREEZE = 3
        var DURATION_AVAILABLE = 5f
        var STATE_NORMAL = 0
        var STATE_TAKEN = 1
    }
}
