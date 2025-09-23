package com.bitfire.uracer.game.world

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.bitfire.uracer.game.collisions.CarImpactManager

class GameWorldContactListener : ContactListener {
    private val impactManager = CarImpactManager()

    override fun beginContact(contact: Contact) {}

    override fun endContact(contact: Contact) {}

    override fun preSolve(contact: Contact, oldManifold: Manifold) {}

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {
        impactManager.process(contact, impulse)
    }
}
