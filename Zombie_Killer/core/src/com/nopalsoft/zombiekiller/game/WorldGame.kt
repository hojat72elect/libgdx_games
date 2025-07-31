package com.nopalsoft.zombiekiller.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
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
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.Settings
import com.nopalsoft.zombiekiller.game_objects.Bullet
import com.nopalsoft.zombiekiller.game_objects.Crate
import com.nopalsoft.zombiekiller.game_objects.Hero
import com.nopalsoft.zombiekiller.game_objects.ItemGem
import com.nopalsoft.zombiekiller.game_objects.ItemHeart
import com.nopalsoft.zombiekiller.game_objects.ItemMeat
import com.nopalsoft.zombiekiller.game_objects.ItemShield
import com.nopalsoft.zombiekiller.game_objects.ItemSkull
import com.nopalsoft.zombiekiller.game_objects.ItemStar
import com.nopalsoft.zombiekiller.game_objects.Items
import com.nopalsoft.zombiekiller.game_objects.Platform
import com.nopalsoft.zombiekiller.game_objects.Saw
import com.nopalsoft.zombiekiller.game_objects.Zombie

class WorldGame {
    var state: Int
    var tiledWidth: Int
    var tiledHeight: Int

    /**
     * At the moment I did a test with 110 tiles width and everything works fine, the background does not go through.
     */
    var world: World = World(Vector2(0f, -9.8f), true)
    var gems: Int = 0
    var skulls: Int = 0

    var totalZombiesKilled: Int = 0
    var bonus: Int = 0 // If all zombies are killed, the collected gems x2

    var timeToFireAgain: Float = 0f

    /*
     * My tiles are 32px, so the unit would be 1/32 with a 10x15 orthographic camera to see 10 tiles wide and 15 tiles high. The problem is that my camera
     *  is 8x4.8px, so I have to change the scale. With 1/32, I would only see 8 tiles wide and 4.8 tiles high, due to the way the camera is configured.
     * With 1/96, I see 24 tiles wide.
     */
    var unitScale: Float = 1 / 76f
    var hero: Hero? = null

    var zombies: Array<Zombie?>
    var items: Array<Items?>
    var crates: Array<Crate?>?
    var bullets: Array<Bullet?>
    var saws: Array<Saw?>?
    var bodies: Array<Body>

    init {
        world.setContactListener(CollisionHandler())

        items = Array<Items?>()
        zombies = Array<Zombie?>()
        bullets = Array<Bullet?>()
        crates = Array<Crate?>()
        saws = Array<Saw?>()
        bodies = Array<Body>()

        TiledMapManagerBox2d(this, unitScale).createObjectsFromTiled(Assets.map!!)
        tiledWidth = (Assets.map!!.layers.get("1") as TiledMapTileLayer).width
        tiledHeight = (Assets.map!!.layers.get("1") as TiledMapTileLayer).height

        if (tiledWidth * tiledHeight > 2500) {
            Gdx.app.log(
                "Advertencia de rendimiento", ("Hay mas de 2500 tiles " + tiledWidth + " x " + tiledHeight + " = "
                        + (tiledWidth * tiledHeight))
            )
        }

        Gdx.app.log("Tile Width", tiledWidth.toString() + "")
        Gdx.app.log("Tile Height", tiledHeight.toString() + "")

        createHero()

        state = STATE_RUNNING
    }

    private fun createHero() {
        hero = Hero(1.35f, 1.6f, Settings.skinSeleccionada)

        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = hero!!.position.x
        bodyDefinition.position.y = hero!!.position.y
        bodyDefinition.type = BodyType.DynamicBody

        val body = world.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(.17f, .32f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.density = 8f
        fixtureDefinition.friction = 0f
        val heroFixture = body.createFixture(fixtureDefinition)
        heroFixture.setUserData("cuerpo")

        val sensorPiesShape = PolygonShape()
        sensorPiesShape.setAsBox(.11f, .025f, Vector2(0f, -.32f), 0f)
        fixtureDefinition.shape = sensorPiesShape
        fixtureDefinition.density = 0f
        fixtureDefinition.restitution = 0f
        fixtureDefinition.friction = 0f
        fixtureDefinition.isSensor = true
        val sensorPies = body.createFixture(fixtureDefinition)
        sensorPies.setUserData("pies")

        body.isFixedRotation = true
        body.userData = hero
        body.isBullet = true

        shape.dispose()
    }

    private fun createBullet(isFiring: Boolean, delta: Float) {
        // Can't shoot if climbing
        if (hero!!.isClimbing || hero!!.state == Hero.STATE_HURT || hero!!.state == Hero.STATE_DEAD) return

        if (isFiring) {
            timeToFireAgain += delta
            if (timeToFireAgain >= TIME_TO_FIRE_AGAIN) {
                timeToFireAgain -= TIME_TO_FIRE_AGAIN
                createBullet()
                Assets.playSound(Assets.shoot1!!, .75f)
            }
        } else {
            timeToFireAgain = TIME_TO_FIRE_AGAIN
        }
    }

    private fun createBullet() {
        val isFacingLeft = hero!!.isFacingLeft

        val obj = if (isFacingLeft) {
            Bullet(hero!!.position.x - .42f, hero!!.position.y - .14f, true)
        } else {
            Bullet(hero!!.position.x + .42f, hero!!.position.y - .14f, false)
        }

        if (!hero!!.isWalking) hero!!.fire() // Puts the state on fire and the animation appears


        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = obj.position.x
        bodyDefinition.position.y = obj.position.y
        bodyDefinition.type = BodyType.DynamicBody

        val body = world.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(.1f, .1f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.density = 1f
        fixtureDefinition.isSensor = true
        body.createFixture(fixtureDefinition)

        body.isFixedRotation = true
        body.userData = obj
        body.isBullet = true
        body.gravityScale = 0f
        bullets.add(obj)

        if (isFacingLeft) body.setLinearVelocity(-Bullet.VELOCIDAD, 0f)
        else body.setLinearVelocity(Bullet.VELOCIDAD, 0f)
    }

    private fun createItemFromZombie(x: Float, y: Float) {
        val obj: Items?
        val tipo = MathUtils.random(4)

        obj = when (tipo) {
            0 -> ItemGem(x, y)
            1 -> ItemHeart(x, y)
            2 -> ItemShield(x, y)
            else -> ItemMeat(x, y)
        }

        val bodyDefinition = BodyDef()
        bodyDefinition.position.y = obj.position.y
        bodyDefinition.position.x = obj.position.x
        bodyDefinition.type = BodyType.DynamicBody

        val shape = CircleShape()
        shape.radius = .15f

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.restitution = .3f
        fixtureDefinition.density = 1f
        fixtureDefinition.friction = 1f
        fixtureDefinition.filter.groupIndex = -1

        val body = world.createBody(bodyDefinition)
        body.createFixture(fixtureDefinition)

        body.userData = obj
        items.add(obj)
        shape.dispose()
    }

    fun update(delta: Float, didJump: Boolean, isFiring: Boolean, accelX: Float, accelY: Float) {
        world.step(delta, 8, 4)

        removeObjects()

        createBullet(isFiring, delta)

        world.getBodies(bodies)

        for (body in bodies) {
            when (body.userData) {
                is Hero -> {
                    updateHeroPlayer(delta, body, didJump, accelX, accelY)
                }

                is Zombie -> {
                    updateZombieMalo(delta, body)
                }

                is Bullet -> {
                    updateBullet(delta, body)
                }

                is Crate -> {
                    updateCrate(delta, body)
                }

                is Saw -> {
                    updateSaw(delta, body)
                }

                is Items -> {
                    updateItems(delta, body)
                }
            }
        }

        if (hero!!.state == Hero.STATE_DEAD && hero!!.stateTime >= Hero.DURATION_DEAD) state = STATE_GAMEOVER
    }

    private fun updateZombieMalo(delta: Float, body: Body) {
        val obj = body.userData as Zombie

        if (obj.position.x > hero!!.position.x - 2 && obj.position.x < hero!!.position.x + 2 && obj.position.y < hero!!.position.y + .5f && obj.position.y > hero!!.position.y - .5f && !obj.canUpdate) {
            obj.canUpdate = true
            var sound: Sound? = null
            when (obj.type) {
                Zombie.TYPE_CUASY -> sound = Assets.zombieCuasy
                Zombie.TYPE_FRANK -> sound = Assets.zombieFrank
                Zombie.TYPE_KID -> sound = Assets.zombieKid
                Zombie.TYPE_MUMMY -> sound = Assets.zombieMummy
                Zombie.TYPE_PAN -> sound = Assets.zombiePan
            }
            Assets.playSound(sound!!, 1f)
        }

        obj.update(delta, body, 0f, hero!!)

        if (obj.position.y < -.5f) {
            obj.die()
        }
    }

    private fun updateHeroPlayer(delta: Float, body: Body, didJump: Boolean, accelX: Float, accelY: Float) {
        hero!!.update(delta, body, didJump, accelX, accelY)

        if (hero!!.position.y < -.5f) {
            hero!!.die()
        }
    }

    private fun updateBullet(delta: Float, body: Body) {
        val obj = body.userData as Bullet
        obj.update(delta, body)

        if (obj.position.x > hero!!.position.x + 4 || obj.position.x < hero!!.position.x - 4) obj.state = Bullet.STATE_DESTROY
    }

    private fun updateCrate(delta: Float, body: Body) {
        val obj = body.userData as Crate
        obj.update(delta, body)
    }

    private fun updateSaw(delta: Float, body: Body) {
        val obj = body.userData as Saw
        obj.update(delta, body)
    }

    private fun updateItems(delta: Float, body: Body) {
        val obj = body.userData as Items
        obj.update(delta, body)
    }

    private fun removeObjects() {
        world.getBodies(bodies)

        for (body in bodies) {
            if (!world.isLocked) {
                if (body.userData is Items) {
                    val obj = body.userData as Items
                    if (obj.state == Items.STATE_TAKEN) {
                        items.removeValue(obj, true)
                        world.destroyBody(body)
                    }
                } else if (body.userData is Zombie) {
                    val obj = body.userData as Zombie
                    if (obj.state == Zombie.STATE_DEAD && obj.stateTime >= Zombie.DEAD_DURATION) {
                        val x = obj.position.x
                        val y = obj.position.y
                        zombies.removeValue(obj, true)
                        world.destroyBody(body)
                        totalZombiesKilled++
                        Settings.gemsTotal += 3
                        gems += 3

                        if (MathUtils.random(50) <= ((Settings.LEVEL_CHANCE_DROP + 1) * 2)) createItemFromZombie(x, y)
                    }
                } else if (body.userData is Bullet) {
                    val obj = body.userData as Bullet
                    if (obj.state == Bullet.STATE_DESTROY) {
                        bullets.removeValue(obj, true)
                        world.destroyBody(body)
                    }
                }
            }
        }
    }

    internal inner class CollisionHandler : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Hero) handleHeroCollisions(a, b)
            else if (b.body.userData is Hero) handleHeroCollisions(b, a)

            if (a.body.userData is Bullet) beginContactBulletOtraCosa(a, b)
            else if (b.body.userData is Bullet) beginContactBulletOtraCosa(b, a)
        }

        private fun handleHeroCollisions(heroFixture: Fixture, otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData

            if (otherObject == "ladder") {
                hero!!.isOnStairs = true
            } else if (otherObject == "suelo" || otherObject is Platform) {
                if (heroFixture.getUserData() == "pies") hero!!.canJump = true
            } else if (otherObject is Crate) {
                if (heroFixture.getUserData() == "pies") {
                    hero!!.canJump = true
                    hero!!.bodyCrate = otherFixture.body
                }
            } else if (otherObject == "spikes") {
                hero!!.die()
            } else if (otherObject is Saw) {
                hero!!.die()
            } else if (otherObject is ItemGem) {
                val obj = otherObject as Items
                if (hero!!.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken()
                    Settings.gemsTotal++
                    gems++

                    Assets.playSound(Assets.gem!!, .075f)
                }
            } else if (otherObject is ItemHeart) {
                val obj = otherObject as Items
                if (hero!!.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken()
                    hero!!.heart

                    Assets.playSound(Assets.hearth!!, 1f)
                }
            } else if (otherObject is ItemSkull) {
                val obj = otherObject as Items
                if (hero!!.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken()
                    skulls++

                    Assets.playSound(Assets.skull!!, .3f)
                }
            } else if (otherObject is ItemMeat) {
                val obj = otherObject as Items
                if (hero!!.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken()

                    Assets.playSound(Assets.hearth!!, 1f)
                }
            } else if (otherObject is ItemShield) {
                val obj = otherObject as Items
                if (hero!!.state != Hero.STATE_DEAD && obj.state == Items.STATE_NORMAL) {
                    obj.taken()
                    hero!!.getShield()
                    Assets.playSound(Assets.shield!!, 1f)
                }
            } else if (otherObject is ItemStar) {
                val obj = otherObject as Items
                if (hero!!.state != Hero.STATE_DEAD && state == STATE_RUNNING) {
                    obj.taken()
                    state = STATE_NEXT_LEVEL
                    if (totalZombiesKilled == TOTAL_ZOMBIES_LEVEL) {
                        bonus = (gems * 2.5f).toInt()
                        Settings.gemsTotal += bonus
                    }
                }
            } else if (otherObject is Zombie) {
                val obj = otherObject
                if (obj.state == Zombie.STATE_NORMAL || obj.state == Zombie.STATE_HURT) {
                    hero!!.hurt()
                    val sound: Sound? = when (hero!!.type) {
                        Hero.TYPE_FORCE, Hero.TYPE_RAMBO -> Assets.hurt1
                        Hero.TYPE_SWAT -> Assets.hurt2
                        else -> Assets.hurt3
                    }
                    Assets.playSound(sound!!, 1f)

                    val impulseX = if (obj.isFacingLeft) -obj.FORCE_IMPACT else obj.FORCE_IMPACT
                    val impulseY = 2.5f

                    heroFixture.body.setLinearVelocity(impulseX, impulseY)
                }
                obj.isTouchingPlayer = true
            }
        }

        private fun beginContactBulletOtraCosa(fixBullet: Fixture, otraCosa: Fixture) {
            val oOtraCosa = otraCosa.body.userData
            val oBullet = fixBullet.body.userData as Bullet

            if (oOtraCosa is Zombie) {
                if (oBullet.state == Bullet.STATE_NORMAL || oBullet.state == Bullet.STATE_MUZZLE) {
                    val obj = oOtraCosa
                    if (obj.state != Zombie.STATE_RISE && obj.state != Zombie.STATE_DEAD) {
                        obj.getHurt(oBullet.damage)
                        oBullet.hit()

                        var impulse = 0f
                        when (obj.type) {
                            Zombie.TYPE_KID, Zombie.TYPE_MUMMY, Zombie.TYPE_CUASY -> impulse = if (oBullet.isFacingLeft) -oBullet.forceImpact else oBullet.forceImpact
                        }
                        otraCosa.body.setLinearVelocity(impulse, .5f)
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
            if (oOtraCosa == "ladder") {
                hero!!.isOnStairs = false
            } else if (oOtraCosa is Zombie) {
                val obj = oOtraCosa
                obj.isTouchingPlayer = false
            } else if (oOtraCosa is Crate) {
                if (fixHero.getUserData() == "pies") {
                    hero!!.bodyCrate = null
                }
            }
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold?) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Hero) preSolveHero(a, b, contact)
            else if (b.body.userData is Hero) preSolveHero(b, a, contact)

            if (a.body.userData is Zombie) preSolveZombie(a, b, contact)
            else if (b.body.userData is Zombie) preSolveZombie(b, a, contact)
        }

        private fun preSolveHero(fixHero: Fixture, otraCosa: Fixture, contact: Contact) {
            val oOtraCosa = otraCosa.body.userData

            if (oOtraCosa is Platform) {
                val obj = oOtraCosa

                if (hero!!.isClimbing) {
                    contact.isEnabled = false
                    return
                }

                // Si el pony su centro - la mitad de su altura y el piso su centro mas la mitad de su altura
                // Si ponyY es menor significa q esta por abajo.
                val ponyY = fixHero.body.getPosition().y - .30f
                val pisY = obj.position.y + obj.height / 2f

                if (ponyY < pisY) contact.isEnabled = false
            } else if (oOtraCosa is Zombie) {
                contact.isEnabled = false
            }
        }

        // Para que el zombie no se atore con las plataformas si va caminando
        private fun preSolveZombie(fixZombie: Fixture, otraCosa: Fixture, contact: Contact) {
            val oOtraCosa = otraCosa.body.userData
            val oZombie = fixZombie.body.userData as Zombie
            if (oOtraCosa is Platform) {
                val obj = oOtraCosa

                val ponyY = fixZombie.body.getPosition().y - .30f
                val pisY = obj.position.y + obj.height / 2f

                if (ponyY < pisY) contact.isEnabled = false
            } else if (oOtraCosa is Crate) {
                if (oZombie.state == Zombie.STATE_RISE) contact.isEnabled = false
            }
        }

        override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        }
    }

    companion object {
        var TIME_TO_FIRE_AGAIN: Float = .3f
        var TOTAL_ZOMBIES_LEVEL = 0
        const val STATE_RUNNING: Int = 0
        const val STATE_GAMEOVER: Int = 1
        const val STATE_NEXT_LEVEL: Int = 2
    }
}
