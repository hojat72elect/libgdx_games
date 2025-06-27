package io.github.masamune

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.component.FollowPath
import io.github.masamune.component.Player
import io.github.masamune.event.EventService
import io.github.masamune.event.PlayerInteractBeginContactEvent
import io.github.masamune.event.PlayerInteractEndContactEvent

private interface ContactAdapter : ContactListener {
    override fun beginContact(contact: Contact) = Unit

    override fun endContact(contact: Contact) = Unit

    override fun preSolve(contact: Contact, oldManifold: Manifold) = Unit

    override fun postSolve(contact: Contact, impulse: ContactImpulse) = Unit
}

class PhysicContactHandler(
    private val eventService: EventService,
    private val world: World,
) : ContactAdapter {
    override fun beginContact(contact: Contact) {
        if (!contact.isPlayerInteract()) {
            return
        }

        eventService.fire(PlayerInteractBeginContactEvent(contact.playerEntity, contact.interactEntity))
    }

    override fun endContact(contact: Contact) {
        if (!contact.isPlayerInteract()) {
            return
        }

        eventService.fire(PlayerInteractEndContactEvent(contact.playerEntity, contact.interactEntity))
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) = with(world) {
        val userDataA = contact.fixtureA.body.userData
        val userDataB = contact.fixtureB.body.userData

        // entities that follow a path will not collide with static environment collision
        if (userDataA is Entity && userDataA has FollowPath) {
            contact.isEnabled = contact.fixtureB.body.type != BodyDef.BodyType.StaticBody
        } else if (userDataB is Entity && userDataB has FollowPath) {
            contact.isEnabled = contact.fixtureA.body.type != BodyDef.BodyType.StaticBody
        }
    }

    private fun Contact.isPlayerInteract(): Boolean {
        return (fixtureA.isInteract() && fixtureA.isPlayerEntity() && fixtureB.isNonPlayerEntity())
            || (fixtureB.isInteract() && fixtureB.isPlayerEntity() && fixtureA.isNonPlayerEntity())
    }

    private fun Fixture.isInteract(): Boolean {
        return "interact" == userData
    }

    private fun Fixture.isPlayerEntity(): Boolean = with(world) {
        val userData = body.userData
        return userData is Entity && userData has Player
    }

    private fun Fixture.isNonPlayerEntity(): Boolean = with(world) {
        val userData = body.userData
        return userData is Entity && userData hasNo Player
    }

    private val Contact.playerEntity: Entity
        get() = if (fixtureA.isPlayerEntity()) fixtureA.body.userData as Entity else fixtureB.body.userData as Entity

    private val Contact.interactEntity: Entity
        get() = if (fixtureA.isNonPlayerEntity()) fixtureA.body.userData as Entity else fixtureB.body.userData as Entity

    companion object {
        fun Body.testPoint(point: Vector2): Boolean {
            return fixtureList.any { it.testPoint(point) }
        }
    }
}
