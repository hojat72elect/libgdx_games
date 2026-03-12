package com.nopalsoft.ninjarunner.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pools
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.Settings
import com.nopalsoft.ninjarunner.game_objects.Item
import com.nopalsoft.ninjarunner.game_objects.ItemCandyBean
import com.nopalsoft.ninjarunner.game_objects.ItemCandyCorn
import com.nopalsoft.ninjarunner.game_objects.ItemCandyJelly
import com.nopalsoft.ninjarunner.game_objects.ItemCoin
import com.nopalsoft.ninjarunner.game_objects.ItemEnergy
import com.nopalsoft.ninjarunner.game_objects.ItemHeart
import com.nopalsoft.ninjarunner.game_objects.ItemMagnet
import com.nopalsoft.ninjarunner.game_objects.Mascot
import com.nopalsoft.ninjarunner.game_objects.Missile
import com.nopalsoft.ninjarunner.game_objects.Obstacle
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes4
import com.nopalsoft.ninjarunner.game_objects.ObstacleBoxes7
import com.nopalsoft.ninjarunner.game_objects.Platform
import com.nopalsoft.ninjarunner.game_objects.Player
import com.nopalsoft.ninjarunner.game_objects.Wall

class GameWorld {
    var state: Int = 0

    var timeToSpawnMissile: Float

    var physicsManager: ObjectManagerBox2d
    var world: World

    var player: Player? = null
    var mascot: Mascot? = null

    var arrayBody: Array<Body>
    var arrayPlatform: Array<Platform>
    var arrayWall: Array<Wall>
    var arrayItem: Array<Item>
    var arrayObstacle: Array<Obstacle>
    var arrayMissile: Array<Missile>

    /**
     * Variable that indicates how far the world has been created.
     */
    var worldCreatedUpToX: Float

    var coinsTaken: Int = 0
    var scores: Long = 0

    private fun createNextPart() {
        val x = worldCreatedUpToX

        while (worldCreatedUpToX < (x + 1)) {
            // First I create the platform

            val platformWidth = 25
            val separation = MathUtils.random(1f, 3f)
            val y = MathUtils.random(0f, 1.5f)

            worldCreatedUpToX = physicsManager.createPlatforms(worldCreatedUpToX + separation, y, platformWidth)

            var xAuxiliary = x + separation

            while (xAuxiliary < worldCreatedUpToX - 2) {
                if (xAuxiliary < x + separation + 2) xAuxiliary = addRandomItems(xAuxiliary, y)

                if (MathUtils.randomBoolean(.1f)) {
                    xAuxiliary = physicsManager.createBox4(xAuxiliary, y + .8f)
                    xAuxiliary = addRandomItems(xAuxiliary, y)
                } else if (MathUtils.randomBoolean(.1f)) {
                    xAuxiliary = physicsManager.createBox7(xAuxiliary, y + 1f)
                    xAuxiliary = addRandomItems(xAuxiliary, y)
                } else if (MathUtils.randomBoolean(.1f)) {
                    xAuxiliary = physicsManager.createWall(xAuxiliary, y + 3.17f)
                    xAuxiliary = addRandomItems(xAuxiliary, y)
                } else {
                    xAuxiliary = addRandomItems(xAuxiliary, y)
                }
            }
        }
    }

    private fun addRandomItems(xAux: Float, y: Float): Float {
        var xAux = xAux
        if (MathUtils.randomBoolean(.3f)) {
            for (i in 0..4) {
                physicsManager.createItem(ItemCoin::class.java, xAux, y + 1.5f)
                xAux = physicsManager.createItem(ItemCoin::class.java, xAux, y + 1f)
            }
        } else if (MathUtils.randomBoolean(.5f)) {
            for (i in 0..4) {
                physicsManager.createItem(ItemCandyBean::class.java, xAux, y + .8f)
                physicsManager.createItem(ItemCandyBean::class.java, xAux, y + 1.1f)
                xAux = physicsManager.createItem(ItemCandyJelly::class.java, xAux, y + 1.5f)
            }
        } else if (MathUtils.randomBoolean(.5f)) {
            for (i in 0..4) {
                physicsManager.createItem(ItemCandyCorn::class.java, xAux, y + .8f)
                physicsManager.createItem(ItemCandyCorn::class.java, xAux, y + 1.1f)
                xAux = physicsManager.createItem(ItemCandyCorn::class.java, xAux, y + 1.5f)
            }
        }

        if (MathUtils.randomBoolean(.025f)) {
            xAux = physicsManager.createItem(ItemHeart::class.java, xAux, y + 1.5f)
            xAux = physicsManager.createItem(ItemEnergy::class.java, xAux, y + 1.5f)
        } else if (MathUtils.randomBoolean(.025f)) {
            xAux = physicsManager.createItem(ItemMagnet::class.java, xAux, y + 1.5f)
        }

        return xAux
    }

    fun update(delta: Float, didJump: Boolean, dash: Boolean, didSlide: Boolean) {
        world.step(delta, 8, 4)

        world.getBodies(arrayBody)
        removeObjects()
        world.getBodies(arrayBody)

        for (body in arrayBody) {
            if (body.userData is Player) {
                updatePlayer(delta, body, didJump, dash, didSlide)
            } else if (body.userData is Mascot) {
                updateMascot(delta, body)
            } else if (body.userData is Platform) {
                updatePlatform(body)
            } else if (body.userData is Wall) {
                updatePared(body)
            } else if (body.userData is Item) {
                updateItem(delta, body)
            } else if (body.userData is Obstacle) {
                updateObstacles(delta, body)
            } else if (body.userData is Missile) {
                updateMissile(delta, body)
            }
        }

        if (player!!.position.x > worldCreatedUpToX - 5) createNextPart()

        if (player!!.state == Player.STATE_DEAD && player!!.stateTime >= Player.DURATION_DEAD) state = STATE_GAMEOVER

        timeToSpawnMissile += delta
        val TIME_TO_SPAWN_MISSILE = 15f
        if (timeToSpawnMissile >= TIME_TO_SPAWN_MISSILE) {
            timeToSpawnMissile -= TIME_TO_SPAWN_MISSILE

            physicsManager.createMissile(player!!.position.x + 10, player!!.position.y)
        }
    }

    private fun removeObjects() {
        for (body in arrayBody) {
            val obj = body.userData
            if (obj is Platform) {
                if (obj.state == Platform.STATE_DESTROY) {
                    arrayPlatform.removeValue(obj, true)
                    Pools.free(obj)
                    world.destroyBody(body)
                }
            } else if (obj is Wall) {
                if (obj.state == Wall.STATE_DESTROY) {
                    arrayWall.removeValue(obj, true)
                    Pools.free(obj)
                    world.destroyBody(body)
                }
            } else if (obj is Item) {
                if (obj.state == Item.STATE_DESTROY && obj.stateTime >= Item.DURATION_PICK) {
                    arrayItem.removeValue(obj, true)
                    Pools.free(obj)
                    world.destroyBody(body)
                }
            } else if (obj is ObstacleBoxes4) {
                if (obj.state == Obstacle.STATE_DESTROY && obj.effect!!.isComplete()) {
                    obj.effect?.free()
                    arrayObstacle.removeValue(obj, true)
                    Pools.free(obj)
                    world.destroyBody(body)
                }
            } else if (obj is ObstacleBoxes7) {
                if (obj.state == Obstacle.STATE_DESTROY && obj.effect!!.isComplete()) {
                    obj.effect?.free()
                    arrayObstacle.removeValue(obj, true)
                    Pools.free(obj)
                    world.destroyBody(body)
                }
            } else if (obj is Missile) {
                if (obj.state == Missile.STATE_DESTROY) {
                    arrayMissile.removeValue(obj, true)
                    Pools.free(obj)
                    world.destroyBody(body)
                }
            }
        }
    }

    var bodyIsSLide: Boolean = false // Indicates whether the body you have right now is sliding.
    var recreateFixture: Boolean = false

    init {
        world = World(Vector2(0f, -9.8f), true)
        world.setContactListener(CollisionHandler())

        physicsManager = ObjectManagerBox2d(this)

        arrayBody = Array<Body>()
        arrayPlatform = Array<Platform>()
        arrayItem = Array<Item>()
        arrayWall = Array<Wall>()
        arrayObstacle = Array<Obstacle>()
        arrayMissile = Array<Missile>()

        timeToSpawnMissile = 0f

        physicsManager.createStandingPlayer(2f, 1f, Settings.selectedSkin)
        physicsManager.createMascot(player!!.position.x - 1, player!!.position.y + .75f)

        worldCreatedUpToX = physicsManager.createPlatforms(0f, 0f, 3)

        createNextPart()
    }

    private fun updatePlayer(delta: Float, body: Body, didJump: Boolean, dash: Boolean, didSlide: Boolean) {
        player!!.update(delta, body, didJump, false, dash, didSlide)

        if (player!!.position.y < -1) {
            player!!.die()
        } else if (player!!.isSlide && !bodyIsSLide) {
            recreateFixture = true
            bodyIsSLide = true
            physicsManager.recreateFixtureSlidingPlayer(body)
        } else if (!player!!.isSlide && bodyIsSLide) {
            recreateFixture = true
            bodyIsSLide = false
            physicsManager.recreateFixtureStandingPlayer(body)
        }
    }

    private fun updateMascot(delta: Float, body: Body) {
        var targetPositionX = player!!.position.x - .75f
        var targetPositionY = player!!.position.y + .25f

        if (mascot!!.mascotType == Mascot.MascotType.BOMB) {
            val oMissile = this.closestMissile
            if (oMissile != null && oMissile.distanceFromPlayer < 5f && oMissile.state == Missile.STATE_NORMAL) {
                targetPositionX = oMissile.position.x
                targetPositionY = oMissile.position.y
            }
        } else {
            if (player!!.isDash) {
                targetPositionX = player!!.position.x + 4.25f
                targetPositionY = player!!.position.y
            }
        }

        mascot!!.update(body, delta, targetPositionX, targetPositionY)
    }

    private fun updatePlatform(body: Body) {
        val obj = body.userData as Platform

        if (obj.position.x < player!!.position.x - 3) obj.setDestroy()
    }

    private fun updatePared(body: Body) {
        val obj = body.userData as Wall

        if (obj.position.x < player!!.position.x - 3) obj.setDestroy()
    }

    private fun updateItem(delta: Float, body: Body) {
        val obj = body.userData as Item
        obj.update(delta, body, player!!)

        if (obj.position.x < player!!.position.x - 3) obj.setPicked()
    }

    private fun updateObstacles(delta: Float, body: Body) {
        val obj = body.userData as Obstacle
        obj.update(delta)

        if (obj.position.x < player!!.position.x - 3) obj.setDestroy()
    }

    private fun updateMissile(delta: Float, body: Body) {
        val obj = body.userData as Missile
        obj.update(delta, body, player!!)

        if (obj.position.x < player!!.position.x - 3) obj.setDestroy()

        arrayMissile.sort()
    }

    private val closestMissile: Missile?
        /**
         * Returns the closest missile to the character who is in normal state.
         */
        get() {
            for (i in 0..<arrayMissile.size) {
                if (arrayMissile.get(i)!!.state == Missile.STATE_NORMAL) return arrayMissile.get(i)
            }
            return null
        }

    internal inner class CollisionHandler : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Player) startCheckingCollisionsOfPlayer(a, b)
            else if (b.body.userData is Player) startCheckingCollisionsOfPlayer(b, a)

            if (a.body.userData is Mascot) startCheckingCollisionsOfMascot(b)
            else if (b.body.userData is Mascot) startCheckingCollisionsOfMascot(a)
        }

        private fun startCheckingCollisionsOfPlayer(playerFixture: Fixture, otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData

            if (otherObject is Platform) {
                if (playerFixture.getUserData() == "pies") {
                    if (recreateFixture) recreateFixture = false
                    else player!!.touchFloor()
                }
            } else if (otherObject is Item) {
                if (otherObject.state == Item.STATE_NORMAL) {
                    if (otherObject is ItemCoin) {
                        coinsTaken++
                        scores++
                        Assets.playSound(Assets.coinSound!!, 1)
                    } else if (otherObject is ItemMagnet) {
                        player!!.setPickUpMagnet()
                    } else if (otherObject is ItemHeart) {
                        player!!.lives++
                    } else if (otherObject is ItemCandyJelly) {
                        Assets.playSound(Assets.candySound!!, 1)
                        scores += 2
                    } else if (otherObject is ItemCandyBean) {
                        Assets.playSound(Assets.candySound!!, 1)
                        scores += 5
                    } else if (otherObject is ItemCandyCorn) {
                        Assets.playSound(Assets.candySound!!, 1)
                        scores += 15
                    }

                    otherObject.setPicked()
                }
            } else if (otherObject is Wall) {
                if (otherObject.state == Wall.STATE_NORMAL) {
                    player!!.dizzy
                }
            } else if (otherObject is Obstacle) {
                if (otherObject.state == Obstacle.STATE_NORMAL) {
                    otherObject.setDestroy()
                    player!!.hurt
                }
            } else if (otherObject is Missile) {
                if (otherObject.state == Obstacle.STATE_NORMAL) {
                    otherObject.setHitTarget()
                    player!!.dizzy
                }
            }
        }

        fun startCheckingCollisionsOfMascot(otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData

            if (otherObject is Wall && player!!.isDash) {
                otherObject.setDestroy()
            } else if (otherObject is Obstacle && player!!.isDash) {
                otherObject.setDestroy()
            } else if (otherObject is ItemCoin) {
                if (otherObject.state == Item.STATE_NORMAL) {
                    otherObject.setPicked()
                    coinsTaken++
                    Assets.playSound(Assets.coinSound!!, 1)
                }
            } else if (otherObject is Missile) {
                if (otherObject.state == Obstacle.STATE_NORMAL) {
                    otherObject.setHitTarget()
                }
            }
        }

        override fun endContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a == null || b == null) return

            if (a.body.userData is Player) stopCheckingCollisionsOfPlayer(a, b)
            else if (b.body.userData is Player) stopCheckingCollisionsOfPlayer(b, a)
        }

        private fun stopCheckingCollisionsOfPlayer(playerFixture: Fixture, otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData

            if (otherObject is Platform) {
                if (playerFixture.getUserData() == "pies") player!!.endTouchFloor()
            }
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold?) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Player) preSolvePlayer(a, b, contact)
            else if (b.body.userData is Player) preSolvePlayer(b, a, contact)
        }

        private fun preSolvePlayer(playerFixture: Fixture, otherFixture: Fixture, contact: Contact) {
            val otherObject = otherFixture.body.userData

            if (otherObject is Platform) {
                val ponyY = playerFixture.body.getPosition().y - .30f
                val pisY = otherObject.position.y + Platform.HEIGHT / 2f

                if (ponyY < pisY) contact.isEnabled = false
            }
        }

        override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        }
    }

    companion object {
        const val STATE_GAMEOVER: Int = 1
    }
}
