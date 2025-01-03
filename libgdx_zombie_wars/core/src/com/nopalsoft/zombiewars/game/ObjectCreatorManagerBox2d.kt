package com.nopalsoft.zombiewars.game

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.nopalsoft.zombiewars.objects.Bullet
import com.nopalsoft.zombiewars.objects.HeroFarmer
import com.nopalsoft.zombiewars.objects.HeroForce
import com.nopalsoft.zombiewars.objects.HeroLumber
import com.nopalsoft.zombiewars.objects.GameCharacter
import com.nopalsoft.zombiewars.objects.ZombieCuasy
import com.nopalsoft.zombiewars.objects.ZombieFrank
import com.nopalsoft.zombiewars.objects.ZombieKid
import com.nopalsoft.zombiewars.objects.ZombieMummy
import com.nopalsoft.zombiewars.objects.ZombiePan

class ObjectCreatorManagerBox2d(private val oWorld: WorldGame) {

    private val oWorldBox = oWorld.oWorldBox

    fun createZombieKid() {
        createZombieMalo(ZombieKid::class.java)
    }

    fun createZombieCuasy() {
        createZombieMalo(ZombieCuasy::class.java)
    }

    fun createZombieMummy() {
        createZombieMalo(ZombieMummy::class.java)
    }

    fun createZombiePan() {
        createZombieMalo(ZombiePan::class.java)
    }

    fun createZombieFrank() {
        createZombieMalo(ZombieFrank::class.java)
    }

    fun createHeroForce() {
        createHero(HeroForce::class.java)
    }

    fun createHeroFarmer() {
        createHero(HeroFarmer::class.java)
    }

    fun createHeroLumber() {
        createHero(HeroLumber::class.java)
    }

    private fun createZombieMalo(zombieType: Class<*>) {
        var obj: GameCharacter? = null

        val bd = BodyDef()
        bd.position.set(16f, 1.6f)
        bd.type = BodyDef.BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        when (zombieType) {
            ZombieKid::class.java -> {
                obj = ZombieKid(oBody)
            }

            ZombieCuasy::class.java -> {
                obj = ZombieCuasy(oBody)
            }

            ZombieMummy::class.java -> {
                obj = ZombieMummy(oBody)
            }

            ZombiePan::class.java -> {
                obj = ZombiePan(oBody)
            }

            ZombieFrank::class.java -> {
                obj = ZombieFrank(oBody)
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
        oWorld.arrFacingLeft.add(obj)

        shape.dispose()
    }

    private fun createHero(heroType: Class<*>) {
        var obj: GameCharacter? = null

        val bd = BodyDef()
        bd.position.set(1f, 1.6f)
        bd.type = BodyDef.BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        when (heroType) {
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
        oWorld.arrFacingRight.add(obj)

        shape.dispose()
    }

    fun createBullet(oPerWhoFired: GameCharacter) {
        val obj: Bullet
        val bd = BodyDef()

        if (oPerWhoFired.type == GameCharacter.TYPE_RANGE) {
            if (oPerWhoFired.isFacingLeft) {
                bd.position.set(oPerWhoFired.position.x - .42f, oPerWhoFired.position.y - .14f)
            } else {
                bd.position.set(oPerWhoFired.position.x + .42f, oPerWhoFired.position.y - .14f)
            }
        } else bd.position.set(oPerWhoFired.position.x, oPerWhoFired.position.y)

        bd.type = BodyDef.BodyType.DynamicBody
        val oBody = oWorldBox.createBody(bd)

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
        oWorld.arrBullets.add(obj)
    }
}