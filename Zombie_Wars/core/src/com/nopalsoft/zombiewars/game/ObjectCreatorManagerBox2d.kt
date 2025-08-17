package com.nopalsoft.zombiewars.game

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.nopalsoft.zombiewars.game_objects.BasePlayer
import com.nopalsoft.zombiewars.game_objects.Bullet
import com.nopalsoft.zombiewars.game_objects.HeroFarmer
import com.nopalsoft.zombiewars.game_objects.HeroForce
import com.nopalsoft.zombiewars.game_objects.HeroLumber
import com.nopalsoft.zombiewars.game_objects.ZombieCuasy
import com.nopalsoft.zombiewars.game_objects.ZombieFrank
import com.nopalsoft.zombiewars.game_objects.ZombieKid
import com.nopalsoft.zombiewars.game_objects.ZombieMummy
import com.nopalsoft.zombiewars.game_objects.ZombiePan

class ObjectCreatorManagerBox2d(private val gameWorld: GameWorld) {
    private val worldPhysicsHandler = gameWorld.oWorldBox

    fun createZombieKid() {
        crearZombieMalo(ZombieKid::class.java)
    }

    fun createZombieCuasy() {
        crearZombieMalo(ZombieCuasy::class.java)
    }

    fun createZombieMummy() {
        crearZombieMalo(ZombieMummy::class.java)
    }

    fun createZombiePan() {
        crearZombieMalo(ZombiePan::class.java)
    }

    fun createZombieFrank() {
        crearZombieMalo(ZombieFrank::class.java)
    }

    fun creatHeroForce() {
        crearHero(HeroForce::class.java)
    }

    fun creatHeroFarmer() {
        crearHero(HeroFarmer::class.java)
    }

    fun creatHeroLumber() {
        crearHero(HeroLumber::class.java)
    }

    private fun crearZombieMalo(tipoZombie: Class<*>) {
//        var obj: Personajes? = null

        val bodyDefinition = BodyDef()
        bodyDefinition.position.set(16f, 1.6f)
        bodyDefinition.type = BodyType.DynamicBody

        val physicsBody = worldPhysicsHandler.createBody(bodyDefinition)

        val obj: BasePlayer? = when (tipoZombie) {
            ZombieKid::class.java -> {
                ZombieKid(physicsBody)
            }

            ZombieCuasy::class.java -> {
                ZombieCuasy(physicsBody)
            }

            ZombieMummy::class.java -> {
                ZombieMummy(physicsBody)
            }

            ZombiePan::class.java -> {
                ZombiePan(physicsBody)
            }

            ZombieFrank::class.java -> {
                ZombieFrank(physicsBody)
            }

            else -> {
                null
            }
        }

        val shape = PolygonShape()
        shape.setAsBox(.17f, .32f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 8f
        fixture.friction = 0f
        fixture.filter.groupIndex = -1
        physicsBody.createFixture(fixture)

        physicsBody.isFixedRotation = true
        physicsBody.userData = obj
        gameWorld.arrFacingLeft.add(obj)

        shape.dispose()
    }

    private fun crearHero(tipoHero: Class<*>) {
        var obj: BasePlayer? = null

        val bd = BodyDef()
        bd.position.set(1f, 1.6f)
        bd.type = BodyType.DynamicBody

        val oBody = worldPhysicsHandler.createBody(bd)

        when (tipoHero) {
            HeroForce::class.java -> {
                obj = HeroForce(oBody)
            }

            HeroFarmer::class.java -> {
                obj = HeroFarmer(oBody)
            }

            HeroLumber::class.java -> {
                obj = HeroLumber(oBody)
            }
        }

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
        gameWorld.arrFacingRight.add(obj)

        shape.dispose()
    }

    fun createBullet(oPerWhoFired: BasePlayer) {
        val obj: Bullet?
        val bd = BodyDef()

        if (oPerWhoFired.tipo == BasePlayer.TIPO_RANGO) {
            if (oPerWhoFired.isFacingLeft) {
                bd.position.set(oPerWhoFired.position.x - .42f, oPerWhoFired.position.y - .14f)
            } else {
                bd.position.set(oPerWhoFired.position.x + .42f, oPerWhoFired.position.y - .14f)
            }
        } else bd.position.set(oPerWhoFired.position.x, oPerWhoFired.position.y)

        bd.type = BodyType.DynamicBody
        val oBody = worldPhysicsHandler.createBody(bd)

        obj = Bullet(oBody, oPerWhoFired)

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
        gameWorld.arrBullets.add(obj)
    }
}
