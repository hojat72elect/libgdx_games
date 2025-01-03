package com.nopalsoft.zombiewars.game

import com.badlogic.gdx.utils.Array as GdxArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.World
import com.nopalsoft.zombiewars.Assets
import com.nopalsoft.zombiewars.Settings
import com.nopalsoft.zombiewars.objects.Bullet
import com.nopalsoft.zombiewars.objects.GameCharacter

class WorldGame(level: Int) {

    private val state = STATE_RUNNING
    private val tiledWidth = (Assets.map!!.layers["1"] as TiledMapTileLayer).width
    private val tiledHeight = (Assets.map!!.layers["1"] as TiledMapTileLayer).height
    val oWorldBox = World(Vector2(0f, -9.8f), true)
    val objectCreatorManager = ObjectCreatorManagerBox2d(this)
    private val xMin = 4.0F // Starts at 4 because the camera is centered not at the origin
    private val yMin = 2.4F
    val posCamara = Vector2(xMin, yMin)

    private var timeToSpawnZombieFrank = 0F
    private var timeToSpawnZombieCuasy = 0F
    private var timeToSpawnZombieKid = 0F
    private var timeToSpawnZombieMummy = 0F
    private var timeToSpawnZombiePan = 0F

    /*
     * My tiles are 32px, so the unit would be 1/32 with a 10x15 orthographic camera to see 10 tiles wide and 15 high. The problem is that my camera is 8x4.8f so I have
     * to change the scale, with 1/32 I would only see 8 tiles wide and 4.8 high because of how the camera is configured.
     * <p>
     * with 1/96 I see 24 tiles wide
     */
    val unitScale = 1 / 76F

    private val xMax = unitScale * tiledWidth * 32 - 4 // Minus 4 because the camera is centered at the origin

    val arrFacingRight = GdxArray<GameCharacter>()
    val arrFacingLeft = GdxArray<GameCharacter>()
    val arrBullets = GdxArray<Bullet>()
    private val arrBodies = GdxArray<Body>()

    init {
        oWorldBox.setContactListener(Collision())
        TiledMapManagerBox2d(this, unitScale).createTiledObjects(Assets.map!!)

        if (tiledWidth * tiledHeight > 2_500)
            Gdx.app.log("Advertencia de rendimiento", "Hay mas de 2500 tiles $tiledWidth x $tiledHeight = ${tiledWidth * tiledHeight}")

        Gdx.app.log("Tile Width", tiledWidth.toString())
        Gdx.app.log("Tile Height", tiledHeight.toString())

        if (level == 0) {
            TIME_TO_SPAWN_ZOMBIE_KID = 3f
            TIME_TO_SPAWN_ZOMBIE_CUASY = 10f
            TIME_TO_SPAWN_ZOMBIE_MUMMY = 15f
            TIME_TO_SPAWN_ZOMBIE_PAN = 20f
            TIME_TO_SPAWN_ZOMBIE_FRANK = 25f
        } else {
            TIME_TO_SPAWN_ZOMBIE_KID = 0f
            TIME_TO_SPAWN_ZOMBIE_CUASY = 0f
            TIME_TO_SPAWN_ZOMBIE_MUMMY = 0f
            TIME_TO_SPAWN_ZOMBIE_PAN = 0f
            TIME_TO_SPAWN_ZOMBIE_FRANK = 0f
        }
    }

    fun update(delta: Float, accelCamX: Float) {
        oWorldBox.step(delta, 8, 4)
        updateCamara(delta, accelCamX)

        deleteObjects()

        spawnStuff(delta)

        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (body.userData is GameCharacter) {
                val obj = body.userData as GameCharacter
                if (obj.isFacingLeft) updateFacingLeft(delta, obj)
                else updateFacingRight(delta, obj)
            } else if (body.userData is Bullet) {
                updateBullet(delta, body)
            }
        }
    }

    fun attackAll() {
        for (obj in arrFacingLeft) {
            if (obj.attack()) objectCreatorManager.createBullet(obj)
        }
    }

    fun dieAll() {
        for (obj in arrFacingLeft) {
            obj.die()
        }
    }

    private fun spawnStuff(delta: Float) {
        timeToSpawnZombieKid += delta
        if (timeToSpawnZombieKid >= TIME_TO_SPAWN_ZOMBIE_KID) {
            timeToSpawnZombieKid -= TIME_TO_SPAWN_ZOMBIE_KID
            objectCreatorManager.createZombieKid()
        }

        timeToSpawnZombieCuasy += delta
        if (timeToSpawnZombieCuasy >= TIME_TO_SPAWN_ZOMBIE_CUASY) {
            timeToSpawnZombieCuasy -= TIME_TO_SPAWN_ZOMBIE_CUASY
            objectCreatorManager.createZombieCuasy()
        }

        timeToSpawnZombieMummy += delta
        if (timeToSpawnZombieMummy >= TIME_TO_SPAWN_ZOMBIE_MUMMY) {
            timeToSpawnZombieMummy -= TIME_TO_SPAWN_ZOMBIE_MUMMY
            objectCreatorManager.createZombieMummy()
        }

        timeToSpawnZombiePan += delta
        if (timeToSpawnZombiePan >= TIME_TO_SPAWN_ZOMBIE_PAN) {
            timeToSpawnZombiePan -= TIME_TO_SPAWN_ZOMBIE_PAN
            objectCreatorManager.createZombiePan()
        }

        timeToSpawnZombieFrank += delta
        if (timeToSpawnZombieFrank >= TIME_TO_SPAWN_ZOMBIE_FRANK) {
            timeToSpawnZombieFrank -= TIME_TO_SPAWN_ZOMBIE_FRANK
            objectCreatorManager.createZombieFrank()
        }
    }

    private fun updateCamara(delta: Float, accelCamX: Float) {
        if (accelCamX != 0f) posCamara.x += (delta * accelCamX)

        if (posCamara.x < xMin * Settings.zoom) {
            posCamara.x = xMin * Settings.zoom
        } else if (posCamara.x > (xMax - (xMin * (Settings.zoom - 1)))) {
            posCamara.x = xMax - (xMin * (Settings.zoom - 1))
        }

        posCamara.y = yMin * Settings.zoom
    }

    private fun updateFacingRight(delta: Float, obj: GameCharacter) {
        obj.update(delta)

        val len = arrFacingLeft.size
        for (i in 0 until len) {
            val objMalo = arrFacingLeft[i]

            if (obj.position.dst(objMalo.position.x, objMalo.position.y) <= obj.attackDistance) {
                if (obj.attack()) objectCreatorManager.createBullet(obj)
            }
        }
    }

    private fun updateFacingLeft(delta: Float, obj: GameCharacter) {
        obj.update(delta)

        val len = arrFacingRight.size
        for (i in 0 until len) {
            val objBueno = arrFacingRight[i]
            if (obj.position.dst(objBueno.position.x, objBueno.position.y) <= obj.attackDistance) {
                if (obj.attack()) objectCreatorManager.createBullet(obj)
            }
        }
    }

    private fun updateBullet(delta: Float, body: Body) {
        val obj = body.userData as Bullet
        obj.update(delta)

        if (obj.position.x > xMax + 3 || obj.position.x < xMin - 3) obj.state = Bullet.STATE_DESTROY
    }

    private fun deleteObjects() {
        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (!oWorldBox.isLocked) {
                if (body.userData is GameCharacter) {
                    val obj = body.userData as GameCharacter
                    if (obj.state == GameCharacter.STATE_DEAD && obj.stateTime >= obj.deathAnimationDuration) {
                        if (obj.isFacingLeft) arrFacingLeft.removeValue(obj, true)
                        else arrFacingRight.removeValue(obj, true)

                        oWorldBox.destroyBody(body)
                    }
                } else if (body.userData is Bullet) {
                    val obj = body.userData as Bullet
                    if (obj.state == Bullet.STATE_DESTROY) {
                        arrBullets.removeValue(obj, true)
                        oWorldBox.destroyBody(body)
                    }
                }
            }
        }
    }

    companion object {

        private const val STATE_RUNNING = 0
        private var TIME_TO_SPAWN_ZOMBIE_FRANK = 0f
        private var TIME_TO_SPAWN_ZOMBIE_CUASY = 0f
        private var TIME_TO_SPAWN_ZOMBIE_KID = 0f
        private var TIME_TO_SPAWN_ZOMBIE_MUMMY = 0f
        private var TIME_TO_SPAWN_ZOMBIE_PAN = 0f
    }


    class Collision : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB
            if (a.body.userData is Bullet) beginContactBulletOtraCosa(a, b)
            else if (b.body.userData is Bullet) beginContactBulletOtraCosa(b, a)
        }

        private fun beginContactBulletOtraCosa(fixBullet: Fixture, otraCosa: Fixture) {
            val oOtraCosa = otraCosa.body.userData
            val oBullet = fixBullet.body.userData as Bullet

            if (oOtraCosa is GameCharacter) {
                if (oBullet.state == Bullet.STATE_NORMAL || oBullet.state == Bullet.STATE_MUZZLE) {
                    val obj = oOtraCosa

                    if (obj.isFacingLeft == oBullet.isFacingLeft)  // Si van hacia el mismo lado son amigos
                        return

                    if (obj.state != GameCharacter.STATE_DEAD) {
                        obj.getHurt(oBullet.DAMAGE)
                        oBullet.hit()
                    }
                }
            }
        }

        override fun endContact(contact: Contact) {
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold) {
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse) {
        }
    }
}