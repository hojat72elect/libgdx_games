package com.nopalsoft.lander.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.nopalsoft.lander.game.objetos.Bomba
import com.nopalsoft.lander.game.objetos.Estrella
import com.nopalsoft.lander.game.objetos.Gas
import com.nopalsoft.lander.game.objetos.Laser
import com.nopalsoft.lander.game.objetos.Nave
import com.nopalsoft.lander.game.objetos.Plataforma
import kotlin.math.abs

class Colisiones(var oWorld: WorldGame) : ContactListener {
    override fun beginContact(contact: Contact) {
        if (contact.fixtureA.body.userData is Nave) beginContactNaveOtraCosa(contact.fixtureA, contact.fixtureB)
        else if (contact.fixtureB.body.userData is Nave) beginContactNaveOtraCosa(contact.fixtureB, contact.fixtureA)
        else if (contact.fixtureB.body.userData is Bomba) beginContactBombaOtraCosa(contact.fixtureB)
        else if (contact.fixtureA.body.userData is Bomba) beginContactBombaOtraCosa(contact.fixtureA)
    }

    fun beginContactNaveOtraCosa(nave: Fixture, otraCosa: Fixture) {
        val bodyNave = nave.body
        val oNave = bodyNave.userData as Nave

        val bodyOtraCosa = otraCosa.body
        val oOtraCosa = bodyOtraCosa.userData

        when (oOtraCosa) {
            is Gas -> {
                val obj = oOtraCosa

                if (obj.state == Gas.STATE_NORMAL) {
                    oNave.gas += 100f
                    obj.state = Gas.STATE_TOMADA
                }
                return
            }

            is Estrella -> {
                val obj = oOtraCosa

                if (obj.state == Estrella.STATE_NORMAL) {
                    oWorld.estrellasTomadas++
                    obj.state = Estrella.STATE_TOMADA
                }
                return
            }

            is Bomba -> {
                val obj = oOtraCosa
                if (obj.state == Bomba.STATE_NORMAL) {
                    obj.state = Bomba.STATE_TOMADA
                    oNave.getHurtByBomb(15f)
                    val blastDirection = bodyNave.getWorldCenter().sub(bodyOtraCosa.getWorldCenter())
                    blastDirection.nor()
                    bodyNave.applyLinearImpulse(blastDirection.scl(.1f), bodyNave.getWorldCenter(), true)
                }
                return
            }

            is Laser -> {
                val obj = oOtraCosa
                obj.isTouchingShip = true
                return
            }

            else -> {
                val velocidadImpacto = abs(bodyNave.getLinearVelocity().x) + abs(bodyNave.getLinearVelocity().y)
                if (velocidadImpacto > 1.5f) {
                    oNave.colision(velocidadImpacto * 2.5f)
                }
                Gdx.app.log("Velocidad Impacto", velocidadImpacto.toString() + "")

                if (oOtraCosa is Plataforma) if (oOtraCosa.isFinal) oNave.isLanded = true
            }
        }
    }

    private fun beginContactBombaOtraCosa(bomba: Fixture) {
        val bodyBomba = bomba.body
        val oBomba = bodyBomba.userData as Bomba
        oBomba.cambioDireccion()
    }

    override fun endContact(contact: Contact) {
        val a = contact.fixtureA
        val b = contact.fixtureB

        if (a != null && a.body.userData is Nave) endContactNaveOtraCosa(contact.fixtureA, contact.fixtureB)
        else if (b != null && b.body.userData is Nave) endContactNaveOtraCosa(contact.fixtureB, contact.fixtureA)
    }

    private fun endContactNaveOtraCosa(nave: Fixture?, otraCosa: Fixture?) {
        if (nave == null || otraCosa == null) return

        val bodyNave = nave.body
        val oNave = bodyNave.userData as Nave

        val bodyOtraCosa = otraCosa.body
        val oOtraCosa = bodyOtraCosa.userData

        if (oOtraCosa is Laser) {
            val obj = oOtraCosa
            obj.isTouchingShip = false
        } else if (oOtraCosa is Plataforma) {
            if (oOtraCosa.isFinal) oNave.isLanded = false
        }
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {
        if (contact.fixtureA.body.userData is Nave) postSolveNaveOtraCosa(contact.fixtureB)
        else if (contact.fixtureB.body.userData is Nave) postSolveNaveOtraCosa(contact.fixtureA)
    }

    fun postSolveNaveOtraCosa(otraCosa: Fixture) {
        val bodyOtraCosa = otraCosa.body
        val oOtraCosa = bodyOtraCosa.userData

        if (oOtraCosa is Bomba) {
            otraCosa.isSensor = true
        }
    }
}
