package com.nopalsoft.clumsy.objects

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.nopalsoft.clumsy.game.arcade.WorldGameArcade

class Asteroid4 : Asteroid0() {
    var ROTATION_SPEED: Float = 200f
    var X_SPEED: Float = -1.7f

    override fun init(worldGameArcade: WorldGameArcade, x: Float, y: Float) {
        position.set(x, y)
        stateTime = 0f
        state = STATE_NORMAL

        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = x
        bodyDefinition.position.y = y
        bodyDefinition.type = BodyType.KinematicBody

        val body = worldGameArcade.oWorldBox.createBody(bodyDefinition)

        val shape = CircleShape()
        shape.radius = .12f

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.density = 8f
        fixtureDefinition.restitution = 0f
        fixtureDefinition.friction = 0f
        body.createFixture(fixtureDefinition)

        body.userData = this
        body.setLinearVelocity(X_SPEED, 0f)
        body.angularVelocity = Math.toRadians(ROTATION_SPEED.toDouble()).toFloat()

        shape.dispose()
    }

    override fun update(delta: Float, body: Body) {
        position.x = body.getPosition().x
        position.y = body.getPosition().y
        angleDeg = Math.toDegrees(body.angle.toDouble()).toFloat()
        stateTime += delta
    }
}
