package com.nopalsoft.clumsy.game.classic

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.nopalsoft.clumsy.Assets
import com.nopalsoft.clumsy.objects.ScoreKeeper
import com.nopalsoft.clumsy.objects.Ufo

class ClassicGameCollisionHandler(var oWorld: WorldGameClassic) : ContactListener {
    override fun beginContact(contact: Contact) {
        val a = contact.fixtureA
        val b = contact.fixtureB

        if (a.body.userData is Ufo) beginContactBirdOtraCosa(a, b)
        else if (b.body.userData is Ufo) beginContactBirdOtraCosa(b, a)
    }

    private fun beginContactBirdOtraCosa(bird: Fixture, otraCosa: Fixture) {
        val oUfo = bird.body.userData as Ufo
        val oOtraCosa = otraCosa.body.userData

        if (oOtraCosa is ScoreKeeper) {
            val obj = oOtraCosa
            if (obj.state == ScoreKeeper.STATE_NORMAL) {
                obj.state = ScoreKeeper.STATE_DESTROY
                oWorld.score++
                Assets.playSound(Assets.point)
            }
        } else {
            if (oUfo.state == Ufo.STATE_NORMAL) {
                oUfo.hurt
                Assets.playSound(Assets.hit)
            }
        }
    }

    override fun endContact(contact: Contact?) {
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
        // TODO Auto-generated method stub
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        // TODO Auto-generated method stub
    }
}
