package com.bitfire.uracer.game.collisions

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.Fixture
import com.bitfire.uracer.game.actors.Car
import com.bitfire.uracer.game.actors.CarType

/**
 * Manages to distinguish and filter out Car-to-entity collisions only, raising the associated events on the correct entities.*/
class CarImpactManager {
    private val tmpVec2 = Vector2()

    fun process(contact: Contact, impulse: ContactImpulse) {
        val a = contact.fixtureA
        val b = contact.fixtureB

        ifCarThenCollide(contact, a, b, impulse)
        ifCarThenCollide(contact, b, a, impulse)
    }

    private fun ifCarThenCollide(contact: Contact, f: Fixture, other: Fixture, impulse: ContactImpulse) {
        val body: Body? = f.body
        val userData = f.getUserData()
        if ((body != null) && (userData === CarType.PlayerCar || userData === CarType.ReplayCar)) {
            val car = body.userData as Car
            val impulses = impulse.getNormalImpulses()
            tmpVec2.set(impulses[0], impulses[1])

            var fcar: Fixture? = null
            if (contact.fixtureA.getUserData() === CarType.PlayerCar) {
                fcar = contact.fixtureA
            } else if (contact.fixtureB.getUserData() === CarType.PlayerCar) {
                fcar = contact.fixtureB
            }

            var frontRatio = 0.5F

            // compute median front/rear ratio for collision points
            if (fcar != null) {
                frontRatio = 0F
                val ml = car.carModel.length
                val halfModelLength = ml * 0.5F
                val pts = contact.getWorldManifold().getPoints()

                val numberOfPoints = contact.getWorldManifold().numberOfContactPoints
                for (i in 0..<numberOfPoints) {
                    val lp = fcar.body.getLocalPoint(pts[i])

                    var r = MathUtils.clamp(lp.y + halfModelLength, 0F, ml)
                    r /= ml
                    frontRatio += r
                }

                frontRatio /= numberOfPoints.toFloat()
            }

            car.onCollide(other, tmpVec2, frontRatio)
        }
    }
}
