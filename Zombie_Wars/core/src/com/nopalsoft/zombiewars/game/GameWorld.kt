package com.nopalsoft.zombiewars.game

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
import com.nopalsoft.zombiewars.objetos.BasePlayer
import com.nopalsoft.zombiewars.objetos.Bullet
import com.badlogic.gdx.utils.Array as GdxArray

class GameWorld() {

    var state = 0
    var tiledWidth = 0
    var tiledHeight = 0
    var oWorldBox = World(Vector2(0f, -9.8f), true)
    var objectCreatorManager = ObjectCreatorManagerBox2d(this)
    var posCamara: Vector2

    var timeToSpwanZombieFrank = 0F
    var timeToSpwanZombieCuasy = 0F
    var timeToSpwanZombieKid = 0F
    var timeToSpwanZombieMummy = 0F
    var timeToSpwanZombiePan = 0F

    /*
     * Mis tiles son de 32px, asi que la unidad seria 1/32 con una camara ortograpicha de 10x15 para ver 10 tiles de ancho y 15 de alto. El probema es que mi camara es de 8x4.8f por eso tengo que
     * cambiar la escala, con 1/32 solo veria 8 tiles a lo ancho y de altura 4.8 por como esta configurada la camara.
     *
     * con 1/96 veo 24 tiles a lo ancho
     */
    var unitScale = 1 / 76F
    var xMin = 4.0f // Inicia en 4 porque la camara esta centrada no en el origen
    var xMax = 0F
    var yMin = 0F

    var arrFacingRight = GdxArray<BasePlayer>()
    var arrFacingLeft = GdxArray<BasePlayer>()
    var arrBullets = GdxArray<Bullet>()
    var arrBodies = GdxArray<Body>()

    init {
        oWorldBox.setContactListener(CollisionHandler())

        TiledMapManagerBox2d(this, unitScale).createObjetosDesdeTiled(Assets.map)
        tiledWidth = (Assets.map.layers.get("1") as TiledMapTileLayer).width
        tiledHeight = (Assets.map.layers.get("1") as TiledMapTileLayer).height

        if (tiledWidth * tiledHeight > 2500) {
            Gdx.app.log(
                "Advertencia de rendimiento", ("Hay mas de 2500 tiles " + tiledWidth + " x " + tiledHeight + " = "
                        + (tiledWidth * tiledHeight))
            )
        }

        Gdx.app.log("Tile Width", tiledWidth.toString() + "")
        Gdx.app.log("Tile Height", tiledHeight.toString() + "")

        xMax = unitScale * tiledWidth * 32 - 4 // Menos 4 porque la camara esta centrada en el origen
        yMin = 2.4f

        posCamara = Vector2(xMin, yMin)
        state = STATE_RUNNING


    }

    fun update(delta: Float, accelCamX: Float) {
        oWorldBox.step(delta, 8, 4)
        updateCamara(delta, accelCamX)

        removeObjects()

        spawnStuff(delta)

        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (body.userData is BasePlayer) {
                val obj = body.userData as BasePlayer
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
        timeToSpwanZombieKid += delta
        if (timeToSpwanZombieKid >= TIME_TO_SPAWN_ZOMBIE_KID) {
            timeToSpwanZombieKid -= TIME_TO_SPAWN_ZOMBIE_KID
            objectCreatorManager.createZombieKid()
        }

        timeToSpwanZombieCuasy += delta
        if (timeToSpwanZombieCuasy >= TIME_TO_SPAWN_ZOMBIE_CUASY) {
            timeToSpwanZombieCuasy -= TIME_TO_SPAWN_ZOMBIE_CUASY
            objectCreatorManager.createZombieCuasy()
        }

        timeToSpwanZombieMummy += delta
        if (timeToSpwanZombieMummy >= TIME_TO_SPAWN_ZOMBIE_MUMMY) {
            timeToSpwanZombieMummy -= TIME_TO_SPAWN_ZOMBIE_MUMMY
            objectCreatorManager.createZombieMummy()
        }

        timeToSpwanZombiePan += delta
        if (timeToSpwanZombiePan >= TIME_TO_SPAWN_ZOMBIE_PAN) {
            timeToSpwanZombiePan -= TIME_TO_SPAWN_ZOMBIE_PAN
            objectCreatorManager.createZombiePan()
        }

        timeToSpwanZombieFrank += delta
        if (timeToSpwanZombieFrank >= TIME_TO_SPAWN_ZOMBIE_FRANK) {
            timeToSpwanZombieFrank -= TIME_TO_SPAWN_ZOMBIE_FRANK
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

    private fun updateFacingRight(delta: Float, obj: BasePlayer) {
        obj.update(delta)

        val len = arrFacingLeft.size
        for (i in 0..<len) {
            val objMalo = arrFacingLeft.get(i)

            if (obj.position.dst(objMalo.position.x, objMalo.position.y) <= obj.DISTANCE_ATTACK) {
                if (obj.attack()) objectCreatorManager.createBullet(obj)
            }
        }
    }

    private fun updateFacingLeft(delta: Float, obj: BasePlayer) {
        obj.update(delta)

        val len = arrFacingRight.size
        for (i in 0..<len) {
            val objBueno = arrFacingRight.get(i)
            if (obj.position.dst(objBueno.position.x, objBueno.position.y) <= obj.DISTANCE_ATTACK) {
                if (obj.attack()) objectCreatorManager.createBullet(obj)
            }
        }
    }

    private fun updateBullet(delta: Float, body: Body) {
        val obj = body.userData as Bullet
        obj.update(delta)

        if (obj.position.x > xMax + 3 || obj.position.x < xMin - 3) obj.state = Bullet.STATE_DESTROY
    }

    private fun removeObjects() {
        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (!oWorldBox.isLocked) {
                if (body.userData is BasePlayer) {
                    val obj = body.userData as BasePlayer
                    if (obj.state == BasePlayer.STATE_DEAD && obj.stateTime >= obj.DURATION_DEAD) {
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

    internal class CollisionHandler : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB
            if (a.body.userData is Bullet) handleBulletCollision(a, b)
            else if (b.body.userData is Bullet) handleBulletCollision(b, a)
        }

        private fun handleBulletCollision(bulletFixture: Fixture, otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData
            val bullet = bulletFixture.body.userData as Bullet

            if (otherObject is BasePlayer) {
                if (bullet.state == Bullet.STATE_NORMAL || bullet.state == Bullet.STATE_MUZZLE) {
                    val obj = otherObject

                    if (obj.isFacingLeft == bullet.isFacingLeft)  // Si van hacia el mismo lado son amigos
                        return

                    if (obj.state != BasePlayer.STATE_DEAD) {
                        obj.getHurt(bullet.DAMAGE)
                        bullet.hit()
                    }
                }
            }
        }

        override fun endContact(contact: Contact?) {
        }

        override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
        }

        override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        }
    }

    companion object {
        private const val STATE_RUNNING = 0
        private const val TIME_TO_SPAWN_ZOMBIE_KID = 3f
        private const val TIME_TO_SPAWN_ZOMBIE_CUASY = 10f
        private const val TIME_TO_SPAWN_ZOMBIE_MUMMY = 15f
        private const val TIME_TO_SPAWN_ZOMBIE_PAN = 20f
        private const val TIME_TO_SPAWN_ZOMBIE_FRANK = 25f
    }
}
