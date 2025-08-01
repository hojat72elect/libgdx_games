package com.nopalsoft.zombiedash.game

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.nopalsoft.zombiedash.Achievements
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.Settings
import com.nopalsoft.zombiedash.objects.Bullet
import com.nopalsoft.zombiedash.objects.Hero
import com.nopalsoft.zombiedash.objects.ItemGem
import com.nopalsoft.zombiedash.objects.ItemHearth
import com.nopalsoft.zombiedash.objects.ItemShield
import com.nopalsoft.zombiedash.objects.ItemWeapon
import com.nopalsoft.zombiedash.objects.Items
import com.nopalsoft.zombiedash.objects.Pisable
import com.nopalsoft.zombiedash.objects.Piso
import com.nopalsoft.zombiedash.objects.Saw
import com.nopalsoft.zombiedash.objects.Spike
import com.nopalsoft.zombiedash.objects.Zombie
import com.nopalsoft.zombiedash.screens.Screens

class WorldGame {
    val WIDTH: Float = Screens.WORLD_WIDTH
    var state: Int = 0
    var oWorldBox: World
    var gems: Int = 0
    var distance: Float = 0f
    var zombiesKilled: Int = 0
    var oHero: Hero? = null
    var TIME_TO_SPAWN_PISO: Float = 0f
    var timeToSpawnPiso: Float
    var TIME_TO_SPAWN_ZOMBIE: Float = 2.0f
    var timeToSpawnZombie: Float = 0f
    var TIME_TO_SPAWN_SPIKE: Float = 5f
    var timeToSpawnSpike: Float = 0f
    var TIME_TO_SPAWN_SAW: Float = 12f
    var timeToSpawnSaw: Float = 0f
    var TIME_TO_SPAWN_ITEM: Float = 3.0f // Items son corazones o escudos
    var timeToSpawnItem: Float = 0f
    var TIME_TO_SPAWN_GEMS: Float = 1.8f
    var timeToSpawnGems: Float = 0f
    var TIME_TO_INCREASE_SPAWN: Float = 5f
    var timeToIncreaseSpawn: Float = 0f
    var arrBodies: Array<Body>
    var arrPisables: Array<Pisable?>
    var arrZombies: Array<Zombie?>
    var arrBullets: Array<Bullet?>
    var arrItems: Array<Items?>
    var arrSpikes: Array<Spike?>
    var arrSaws: Array<Saw?>

    init {
        oWorldBox = World(Vector2(0f, -9.8f), true)
        oWorldBox.setContactListener(Colisiones())

        arrBodies = Array<Body>()
        arrPisables = Array<Pisable?>()
        arrZombies = Array<Zombie?>()
        arrBullets = Array<Bullet?>()
        arrItems = Array<Items?>()
        arrSpikes = Array<Spike?>()
        arrSaws = Array<Saw?>()

        timeToSpawnPiso = -.15f

        crearPiso(3.5f)
        crearHeroPrueba()
    }

    fun crearPiso() {
        crearPiso(13f)
    }

    private fun crearPiso(x: Float) {
        val y = MathUtils.random(1f)
        val obj = Piso(x, y)

        val bd = BodyDef()
        bd.position.x = x
        bd.position.y = obj.position.y
        bd.type = BodyType.KinematicBody

        val oBody = oWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(4f, 1.13f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 8f
        fixture.friction = 0f
        oBody.createFixture(fixture)
        oBody.userData = obj
        arrPisables.add(obj)
        oBody.setLinearVelocity(velocidadX, 0f)

        shape.dispose()
    }

    private fun createItem() {
        var obj: Items? = null
        val tipo = MathUtils.random(2)

        val y = MathUtils.random(2.15f, 3f)
        val x = 9f

        when (tipo) {
            0 -> obj = ItemShield(x, y)
            1 -> obj = ItemHearth(x, y)
            2 -> obj = ItemWeapon(x, y)
        }

        val bd = BodyDef()
        bd.position.y = obj!!.position.y
        bd.position.x = obj.position.x
        bd.type = BodyType.KinematicBody

        val shape = CircleShape()
        shape.radius = .15f

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.restitution = .3f
        fixture.density = 1f
        fixture.friction = 1f
        fixture.isSensor = true

        val oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)

        oBody.userData = obj
        arrItems.add(obj)
        oBody.setLinearVelocity(velocidadX, 0f)

        shape.dispose()
    }

    private fun createGem(x: Float, y: Float) {
        val obj: Items = ItemGem(x, y)

        val bd = BodyDef()
        bd.position.y = obj.position.y
        bd.position.x = obj.position.x
        bd.type = BodyType.KinematicBody

        val shape = CircleShape()
        shape.radius = .15f

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.restitution = .3f
        fixture.density = 1f
        fixture.friction = 1f
        fixture.isSensor = true

        val oBody = oWorldBox.createBody(bd)
        oBody.createFixture(fixture)

        oBody.userData = obj
        arrItems.add(obj)
        oBody.setLinearVelocity(velocidadX, 0f)

        shape.dispose()
    }

    private fun createFiguraGema() {
        val yHeight = MathUtils.random(2.1f, 2.85f)

        val tipo = MathUtils.random(3)

        when (tipo) {
            0 ->                 // 5x2
            {
                var col = 0
                while (col < 5) {
                    val x = 9 + col * .37f
                    var ren = 0
                    while (ren < 2) {
                        val y = yHeight + ren * .35f
                        createGem(x, y)
                        ren++
                    }
                    col++
                }
            }

            1 -> {
                // 10x1
                var col = 0
                while (col < 10) {
                    val x = 9 + col * .37f
                    createGem(x, yHeight)
                    col++
                }
                timeToSpawnGems -= 3f // Tengo que ajustar el tiempo un poco
            }

            else -> {}
        }
    }

    private fun createSpike() {
        val x = 9.4f
        val y = 5f

        val obj = Spike(x, y)

        val bd = BodyDef()
        bd.position.x = obj.position.x
        bd.position.y = obj.position.y
        bd.type = BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(.45f, .07f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 8f
        fixture.friction = 0f
        fixture.filter.groupIndex = -1
        oBody.createFixture(fixture).setUserData("spikeBody")

        // Sensor Izq
        val sensorPiesShape = CircleShape()
        sensorPiesShape.radius = .05f
        sensorPiesShape.position = Vector2(-.44f, -.10f)

        fixture.shape = sensorPiesShape
        fixture.density = 0f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true
        var sensorPies = oBody.createFixture(fixture)
        sensorPies.setUserData("spikeLeft")

        sensorPiesShape.position = Vector2(.44f, -.11f)
        sensorPies = oBody.createFixture(fixture)
        sensorPies.setUserData("spikeRight")

        oBody.isFixedRotation = true
        oBody.userData = obj
        arrSpikes.add(obj)

        shape.dispose()
    }

    private fun crearHeroPrueba() {
        oHero = Hero(1.35f, 2.5f, Settings.skinSeleccionada)

        val bd = BodyDef()
        bd.position.x = oHero!!.position.x
        bd.position.y = oHero!!.position.y
        bd.type = BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(.17f, .32f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 8f
        fixture.friction = 0f
        val cuerpo = oBody.createFixture(fixture)
        cuerpo.setUserData("cuerpo")

        val sensorPiesShape = PolygonShape()
        sensorPiesShape.setAsBox(.11f, .025f, Vector2(0f, -.32f), 0f)
        fixture.shape = sensorPiesShape
        fixture.density = 0f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true
        val sensorPies = oBody.createFixture(fixture)
        sensorPies.setUserData("pies")

        oBody.isFixedRotation = true
        oBody.userData = oHero
        oBody.isBullet = true

        shape.dispose()
    }

    private fun crearZombieMalo() {
        var obj: Zombie? = null

        val tipo = MathUtils.random(4)
        val x = 7.5f
        val y = 4f

        when (tipo) {
            0 -> obj = Zombie(x, y, Zombie.TIPO_CUASY)
            1 -> obj = Zombie(x, y, Zombie.TIPO_FRANK)
            2 -> obj = Zombie(x, y, Zombie.TIPO_KID)
            3 -> obj = Zombie(x, y, Zombie.TIPO_MUMMY)
            4 -> obj = Zombie(x, y, Zombie.TIPO_PAN)
        }

        val bd = BodyDef()
        bd.position.x = obj!!.position.x
        bd.position.y = obj.position.y
        bd.type = BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(.17f, .32f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 8f
        fixture.friction = 0f
        fixture.filter.groupIndex = -1
        oBody.createFixture(fixture)

        oBody.isFixedRotation = true
        oBody.userData = obj
        arrZombies.add(obj)

        shape.dispose()
    }

    private fun createBullet() {
        val obj = Bullet(oHero!!.position.x + .42f, oHero!!.position.y - .14f)

        val bd = BodyDef()
        bd.position.x = obj.position.x
        bd.position.y = obj.position.y
        bd.type = BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(.1f, .1f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 1f
        fixture.isSensor = true
        oBody.createFixture(fixture)

        oBody.isFixedRotation = true
        oBody.userData = obj
        oBody.isBullet = true
        oBody.gravityScale = 0f
        arrBullets.add(obj)

        oBody.setLinearVelocity(Bullet.VELOCIDAD, 0f)
    }

    private fun createSaw() {
        val x = 9.4f
        val y = oHero!!.position.y

        val obj = Saw(x, y)

        val bd = BodyDef()
        bd.position.x = obj.position.x
        bd.position.y = obj.position.y
        bd.type = BodyType.KinematicBody

        val oBody = oWorldBox.createBody(bd)

        val shape = CircleShape()
        shape.radius = Saw.SIZE / 2f

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 8f
        fixture.friction = 0f
        fixture.isSensor = true
        oBody.createFixture(fixture).setUserData("sawBody")

        oBody.isFixedRotation = true
        oBody.userData = obj
        arrSaws.add(obj)
        oBody.angularVelocity = Math.toRadians(360.0).toFloat()

        shape.dispose()
    }

    fun update(delta: Float, didJump: Boolean, isJumpPressed: Boolean, isFiring: Boolean) {
        oWorldBox.step(delta, 8, 4)

        eliminarObjetos()

        // Spwan Objects
        timeToIncreaseSpawn += delta
        if (timeToIncreaseSpawn >= TIME_TO_INCREASE_SPAWN) {
            timeToIncreaseSpawn -= TIME_TO_INCREASE_SPAWN

            TIME_TO_SPAWN_ZOMBIE -= .025f
            if (TIME_TO_SPAWN_ZOMBIE < .5f) TIME_TO_SPAWN_ZOMBIE = .5f

            TIME_TO_SPAWN_SPIKE -= .05f
            if (TIME_TO_SPAWN_SPIKE < 2.5f) TIME_TO_SPAWN_SPIKE = 2.5f
        }

        spawnStuff(delta)

        if (isFiring && oHero!!.state == Hero.STATE_NORMAL) {
            if (Settings.numBullets > 0) {
                createBullet()
                Settings.numBullets--
                Assets.playSound(Assets.shoot1!!, .75f)
            } else {
                Assets.playSound(Assets.noBullet!!, .85f)
            }
        }

        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (body.userData is Hero) {
                updateHeroPlayer(delta, body, didJump, isJumpPressed)
            } else if (body.userData is Pisable) {
                updatePisable(delta, body)
            } else if (body.userData is Zombie) {
                updateZombie(delta, body)
            } else if (body.userData is Bullet) {
                updateBullet(delta, body)
            } else if (body.userData is Spike) {
                updateSpike(delta, body)
            } else if (body.userData is Saw) {
                updateSaw(delta, body)
            } else if (body.userData is Items) {
                updateItems(delta, body)
            }
        }

        if (oHero!!.state == Hero.STATE_DEAD && oHero!!.stateTime >= Hero.DURATION_DEAD) state = STATE_GAMEOVER

        distance += 5 * delta
    }

    private fun spawnStuff(delta: Float) {
        timeToSpawnPiso += delta
        if (timeToSpawnPiso >= TIME_TO_SPAWN_PISO) {
            timeToSpawnPiso -= TIME_TO_SPAWN_PISO
            TIME_TO_SPAWN_PISO = MathUtils.random(0f, 1.1f) + 4.1f

            crearPiso()
        }

        timeToSpawnZombie += delta
        if (timeToSpawnZombie >= TIME_TO_SPAWN_ZOMBIE) {
            timeToSpawnZombie -= TIME_TO_SPAWN_ZOMBIE
            if (MathUtils.randomBoolean()) crearZombieMalo()
        }

        timeToSpawnItem += delta
        if (timeToSpawnItem >= TIME_TO_SPAWN_ITEM) {
            timeToSpawnItem -= TIME_TO_SPAWN_ITEM
            if (MathUtils.randomBoolean(.4f)) createItem()
        }

        timeToSpawnGems += delta
        if (timeToSpawnGems >= TIME_TO_SPAWN_GEMS) {
            timeToSpawnGems -= TIME_TO_SPAWN_GEMS
            createFiguraGema()
        }

        timeToSpawnSpike += delta
        if (timeToSpawnSpike >= TIME_TO_SPAWN_SPIKE) {
            timeToSpawnSpike -= TIME_TO_SPAWN_SPIKE
            if (MathUtils.randomBoolean(.5f)) createSpike()
        }

        timeToSpawnSaw += delta
        if (timeToSpawnSaw >= TIME_TO_SPAWN_SAW) {
            timeToSpawnSaw -= TIME_TO_SPAWN_SAW
            TIME_TO_SPAWN_SAW = MathUtils.random(1.3f, 20f)
            createSaw()
        }
    }

    private fun updateHeroPlayer(delta: Float, body: Body, didJump: Boolean, isJumpPressed: Boolean) {
        oHero!!.update(delta, body, didJump, isJumpPressed)

        if (oHero!!.position.y < -.5f || oHero!!.position.x < 0) {
            oHero!!.die()
        }
    }

    private fun updatePisable(delta: Float, body: Body) {
        val obj = body.userData as Pisable
        obj.update(delta, body)

        if (obj.position.x < -(obj.DRAW_WIDTH / 2f)) {
            obj.state = Pisable.STATE_DESTROY
        }
    }

    private fun updateZombie(delta: Float, body: Body) {
        val obj = body.userData as Zombie

        obj.update(delta, body)

        if (obj.position.y < -.75f) {
            obj.getHurt(999999)
        }
    }

    private fun updateBullet(delta: Float, body: Body) {
        val obj = body.userData as Bullet
        obj.update(delta, body)

        if (obj.position.x > WIDTH + 1) obj.state = Bullet.STATE_DESTROY
    }

    private fun updateSpike(delta: Float, body: Body) {
        val obj = body.userData as Spike
        obj.update(delta, body)

        if (obj.position.x < -.75) {
            obj.state = Spike.STATE_DESTROY
        }
    }

    private fun updateSaw(delta: Float, body: Body) {
        val obj = body.userData as Saw
        obj.update(delta, body, oHero!!)

        if (obj.position.x < -1) {
            obj.state = Saw.STATE_DESTROY
        }
    }

    private fun updateItems(delta: Float, body: Body) {
        val obj = body.userData as Items
        obj.update(delta, body)

        if (obj.position.x < -.75) {
            obj.state = Items.STATE_TAKEN
        }
    }

    private fun eliminarObjetos() {
        oWorldBox.getBodies(arrBodies)

        for (body in arrBodies) {
            if (!oWorldBox.isLocked) {
                if (body.userData is Zombie) {
                    val obj = body.userData as Zombie
                    if (obj.state == Zombie.STATE_DEAD && obj.stateTime >= Zombie.DURATION_DEAD || oHero!!.state == Hero.STATE_REVIVE) {
                        arrZombies.removeValue(obj, true)
                        oWorldBox.destroyBody(body)
                    }
                } else if (body.userData is Bullet) {
                    val obj = body.userData as Bullet
                    if (obj.state == Bullet.STATE_DESTROY) {
                        arrBullets.removeValue(obj, true)
                        oWorldBox.destroyBody(body)
                    }
                } else if (body.userData is Items) {
                    val obj = body.userData as Items
                    if (obj.state == Items.STATE_TAKEN) {
                        arrItems.removeValue(obj, true)
                        oWorldBox.destroyBody(body)
                    }
                } else if (body.userData is Pisable) {
                    val obj = body.userData as Pisable
                    if (obj.state == Pisable.STATE_DESTROY) {
                        arrPisables.removeValue(obj, true)
                        oWorldBox.destroyBody(body)
                    }
                } else if (body.userData is Spike) {
                    val obj = body.userData as Spike
                    if (obj.state == Spike.STATE_DESTROY || oHero!!.state == Hero.STATE_REVIVE) {
                        arrSpikes.removeValue(obj, true)
                        oWorldBox.destroyBody(body)
                    }
                } else if (body.userData is Saw) {
                    val obj = body.userData as Saw
                    if (obj.state == Saw.STATE_DESTROY || oHero!!.state == Hero.STATE_REVIVE) {
                        arrSaws.removeValue(obj, true)
                        oWorldBox.destroyBody(body)
                    }
                }
            }
        }
    }

    internal inner class Colisiones : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Hero) beginContactHeroOtraCosa(a, b)
            else if (b.body.userData is Hero) beginContactHeroOtraCosa(b, a)

            if (a.body.userData is Pisable) beginContactPisableOtraCosa(b)
            else if (b.body.userData is Pisable) beginContactPisableOtraCosa(a)

            if (a.body.userData is Bullet) beginContactBulletOtraCosa(a, b)
            else if (b.body.userData is Bullet) beginContactBulletOtraCosa(b, a)
        }

        private fun beginContactHeroOtraCosa(fixHero: Fixture, otraCosa: Fixture) {
            val oOtraCosa = otraCosa.body.userData

            if (oOtraCosa is Pisable) {
                if (fixHero.getUserData() == "pies") oHero!!.touchFloor()
            } else if (oOtraCosa is ItemGem) {
                val obj = oOtraCosa as Items
                if (oHero!!.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken()
                    Settings.gemsTotal++
                    gems++
                    Assets.playSound(Assets.gem!!, .075f)
                }
            } else if (oOtraCosa is ItemHearth) {
                val obj = oOtraCosa as Items
                if (oHero!!.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken()
                    oHero!!.hearth

                    Assets.playSound(Assets.hearth!!, 1f)
                }
            } else if (oOtraCosa is ItemShield) {
                val obj = oOtraCosa as Items
                if (oHero!!.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken()
                    oHero!!.getShield()
                    Assets.playSound(Assets.shield!!, 1f)
                }
            } else if (oOtraCosa is ItemWeapon) {
                val obj = oOtraCosa as Items
                if (oHero!!.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken()
                    Settings.numBullets += 25
                    Assets.playSound(Assets.shield!!, 1f)
                }
            } else if (oOtraCosa is Spike) {
                oHero!!.hurt
            } else if (oOtraCosa is Saw) {
                oHero!!.hurt
            } else if (oOtraCosa is Zombie) {
                val obj = oOtraCosa
                if (obj.state == Zombie.STATE_NORMAL || obj.state == Zombie.STATE_HURT) {
                    oHero!!.hurt
                    val sound: Sound?
                    when (oHero!!.tipo) {
                        Hero.TIPO_FORCE, Hero.TIPO_RAMBO -> sound = Assets.hurt1
                        Hero.TIPO_SWAT -> sound = Assets.hurt2
                        else -> sound = Assets.hurt3
                    }
                    Assets.playSound(sound!!, 1f)
                }
            }
        }

        private fun beginContactPisableOtraCosa(fixOtraCosa: Fixture) {
            val objOtraCosa = fixOtraCosa.body.userData

            if (objOtraCosa is Zombie) {
                val obj = objOtraCosa
                obj.canUpdate = true
                var sound: Sound? = null
                when (obj.tipo) {
                    Zombie.TIPO_CUASY -> sound = Assets.zombieCuasy
                    Zombie.TIPO_FRANK -> sound = Assets.zombieFrank
                    Zombie.TIPO_KID -> sound = Assets.zombieKid
                    Zombie.TIPO_MUMMY -> sound = Assets.zombieMummy
                    Zombie.TIPO_PAN -> sound = Assets.zombiePan
                }
                Assets.playSound(sound!!, 1f)
            } else if (objOtraCosa is Spike) {
                val obj = objOtraCosa
                if (fixOtraCosa.getUserData() == "spikeLeft") {
                    obj.didTouchLeft = true
                } else if (fixOtraCosa.getUserData() == "spikeRight") {
                    obj.didTouchRight = true
                }
            } else if (objOtraCosa is Items) {
                val obj = objOtraCosa
                obj.state = Items.STATE_TAKEN
            }
        }

        private fun beginContactBulletOtraCosa(fixBullet: Fixture, otraCosa: Fixture) {
            val oOtraCosa = otraCosa.body.userData
            val oBullet = fixBullet.body.userData as Bullet

            if (oOtraCosa is Zombie) {
                if (oBullet.state == Bullet.STATE_NORMAL || oBullet.state == Bullet.STATE_MUZZLE) {
                    val obj = oOtraCosa
                    if (obj.state != Zombie.STATE_RISE && obj.state != Zombie.STATE_DEAD) {
                        obj.getHurt(oBullet.DAMAGE)
                        oBullet.hit()
                        fixBullet.body.setLinearVelocity(otraCosa.body.getLinearVelocity().x, 0f)

                        if (obj.state == Zombie.STATE_DEAD) {
                            zombiesKilled++
                            Achievements.unlockKilledZombies()
                        }
                    }
                }
            }
        }

        override fun endContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a == null || b == null) return

            if (a.body.userData is Hero) endContactHeroOtraCosa(a, b)
            else if (b.body.userData is Hero) endContactHeroOtraCosa(b, a)
        }

        private fun endContactHeroOtraCosa(fixHero: Fixture, otraCosa: Fixture) {
            val oOtraCosa = otraCosa.body.userData

            if (oOtraCosa is Pisable) {
                if (fixHero.getUserData() == "pies") oHero!!.endTouchFloor()
            }
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold?) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Hero) preSolveHero(b, contact)
            else if (b.body.userData is Hero) preSolveHero(a, contact)
        }

        private fun preSolveHero(otraCosa: Fixture, contact: Contact) {
            val oOtraCosa = otraCosa.body.userData

            if (oOtraCosa is Zombie) {
                contact.isEnabled = false
            } else if (oOtraCosa is Spike) {
                contact.isEnabled = false
            }
        }

        override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
            // TODO Auto-generated method stub
        }
    }

    companion object {
        val velocidadX: Float = -2f
        const val STATE_RUNNING: Int = 0
        const val STATE_GAMEOVER: Int = 1
    }
}
