package com.nopalsoft.zombiewars.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.zombiewars.AnimationSprite
import com.nopalsoft.zombiewars.Assets
import com.nopalsoft.zombiewars.Settings
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
import com.nopalsoft.zombiewars.screens.Screens

class WorldGameRenderer2(batcher: SpriteBatch, oWorld: WorldGame) {


    private var batcher: SpriteBatch
    private var oWorld: WorldGame

    private var oCam: OrthographicCamera = OrthographicCamera(WIDTH, HEIGHT)
    private var tiledRender: OrthogonalTiledMapRenderer

    private var renderBox: Box2DDebugRenderer

    private var map1: TiledMapTileLayer?
    private var map2: TiledMapTileLayer?
    private var map3: TiledMapTileLayer?
    private var map4: TiledMapTileLayer?

    private var mapInFront: TiledMapTileLayer? // Enfrente del mono

    init {
        oCam.position[WIDTH / 2f, HEIGHT / 2f] = 0f
        this.batcher = batcher
        this.oWorld = oWorld
        this.renderBox = Box2DDebugRenderer()
        tiledRender = OrthogonalTiledMapRenderer(Assets.map, oWorld.unitScale)

        /*
         * Entre mas chico el numero se renderean primero.
         */
        map1 = tiledRender.map.layers["1"] as TiledMapTileLayer?
        map2 = tiledRender.map.layers["2"] as TiledMapTileLayer?
        map3 = tiledRender.map.layers["3"] as TiledMapTileLayer?
        map4 = tiledRender.map.layers["4"] as TiledMapTileLayer?
        mapInFront = tiledRender.map.layers["inFront"] as TiledMapTileLayer?
    }

    fun render() {
        oCam.zoom = Settings.zoom
        oCam.position.x = oWorld.posCamara.x
        oCam.position.y = oWorld.posCamara.y

        oCam.update()

        drawTiled()

        batcher.projectionMatrix = oCam.combined
        batcher.begin()
        batcher.enableBlending()
        drawFacingRight()
        drawMalos()
        drawBullets()
        batcher.end()

        drawTiledInfront()
    }

    private fun drawTiledInfront() {
        tiledRender.setView(oCam)

        tiledRender.batch.begin()
        if (mapInFront != null) tiledRender.renderTileLayer(mapInFront)
        tiledRender.batch.end()
    }

    private fun drawTiled() {
        tiledRender.setView(oCam)
        tiledRender.batch.begin()
        if (map1 != null) tiledRender.renderTileLayer(map1)
        if (map2 != null) tiledRender.renderTileLayer(map2)
        if (map3 != null) tiledRender.renderTileLayer(map3)
        if (map4 != null) tiledRender.renderTileLayer(map4)


        tiledRender.batch.end()
    }

    private fun drawMalos() {
        for (obj in oWorld.arrFacingLeft) {
            var animWalk: AnimationSprite? = null
            var animAttack: AnimationSprite? = null
            var animDie: AnimationSprite? = null
            var spriteHurt: Sprite? = null

            var ajusteY = 0f
            var size = 0f

            when (obj) {
                is ZombieKid -> {
                    animWalk = Assets.zombieKidWalk
                    animAttack = Assets.zombieKidAttack
                    animDie = Assets.zombieKidDie
                    spriteHurt = Assets.zombieKidHurt
                    ajusteY = -.033f
                    size = .8f
                }

                is ZombieCuasy -> {
                    animWalk = Assets.zombieCuasyWalk
                    animAttack = Assets.zombieCuasyAttack
                    animDie = Assets.zombieCuasyDie
                    spriteHurt = Assets.zombieCuasyHurt
                    ajusteY = -.035f
                    size = .8f
                }

                is ZombieMummy -> {
                    animWalk = Assets.zombieMummyWalk
                    animAttack = Assets.zombieMummyAttack
                    animDie = Assets.zombieMummyDie
                    spriteHurt = Assets.zombieMummyHurt
                    ajusteY = -.035f
                    size = .8f
                }

                is ZombiePan -> {
                    animWalk = Assets.zombiePanWalk
                    animAttack = Assets.zombiePanAttack
                    animDie = Assets.zombiePanDie
                    spriteHurt = Assets.zombiePanHurt
                    ajusteY = -.038f
                    size = .8f
                }

                is ZombieFrank -> {
                    animWalk = Assets.zombieFrankWalk
                    animAttack = Assets.zombieFrankAttack
                    animDie = Assets.zombieFrankDie
                    spriteHurt = Assets.zombieFrankHurt
                    ajusteY = -.033f
                    size = .8f
                }

                is HeroForce -> {
                    animWalk = Assets.heroForceWalk
                    animAttack = Assets.heroForceShoot
                    animDie = Assets.heroForceDie
                    spriteHurt = Assets.heroForceHurt
                    size = .7f
                }
            }

            var spriteFrame: Sprite? = null

            when (obj.state) {
                GameCharacter.STATE_NORMAL -> {
                    spriteFrame = animWalk!!.getKeyFrame(obj.stateTime, true)
                }
                GameCharacter.STATE_ATTACK -> {
                    spriteFrame = animAttack!!.getKeyFrame(obj.stateTime, false)
                }
                GameCharacter.STATE_DEAD -> {
                    spriteFrame = animDie!!.getKeyFrame(obj.stateTime, false)
                }
                GameCharacter.STATE_HURT -> {
                    spriteFrame = spriteHurt
                }
            }

            // facing left
            spriteFrame!!.setPosition(obj.position.x + .29f, obj.position.y - .34f + ajusteY)
            spriteFrame.setSize(-size, size)
            spriteFrame.draw(batcher)
        }
    }

    private fun drawFacingRight() {
        for (obj in oWorld.arrFacingRight) {
            var animWalk: AnimationSprite? = null
            var animAttack: AnimationSprite? = null
            var animDie: AnimationSprite? = null
            var spriteHurt: Sprite? = null

            val ajusteY = 0f
            var ajusteX = 0f
            var sizeX = 0f
            var sizeY = 0f

            when (obj) {
                is HeroForce -> {
                    animWalk = Assets.heroForceWalk
                    animAttack = Assets.heroForceShoot
                    animDie = Assets.heroForceDie
                    spriteHurt = Assets.heroForceHurt
                    sizeY = .7f
                    sizeX = sizeY
                }

                is HeroFarmer -> {
                    animWalk = Assets.heroFarmerWalk
                    animAttack = Assets.heroFarmerShoot
                    animDie = Assets.heroFarmerDie
                    spriteHurt = Assets.heroFarmerHurt
                    sizeY = .7f
                    sizeX = sizeY
                }

                is HeroLumber -> {
                    animWalk = Assets.heroLumberWalk
                    animAttack = Assets.heroLumberShoot
                    animDie = Assets.heroLumberDie
                    spriteHurt = Assets.heroLumberHurt
                    sizeX = 1f
                    sizeY = .85f
                    ajusteX = -.15f
                }
            }

            var spriteFrame: Sprite? = null

            when (obj.state) {
                GameCharacter.STATE_NORMAL -> {
                    spriteFrame = animWalk!!.getKeyFrame(obj.stateTime, true)
                }
                GameCharacter.STATE_ATTACK -> {
                    spriteFrame = animAttack!!.getKeyFrame(obj.stateTime, false)
                }
                GameCharacter.STATE_DEAD -> {
                    spriteFrame = animDie!!.getKeyFrame(obj.stateTime, false)
                }
                GameCharacter.STATE_HURT -> {
                    spriteFrame = spriteHurt
                }
            }

            spriteFrame!!.setPosition(obj.position.x - .29f + ajusteX, obj.position.y - .34f + ajusteY)
            spriteFrame.setSize(sizeX, sizeY)
            spriteFrame.draw(batcher)
        }
    }

    private fun drawBullets() {
        for (obj in oWorld.arrBullets) {
            if (!obj.isVisible) continue

            val animBullet = Assets.bullet1

            if (obj.state == Bullet.STATE_DESTROY) continue

            // BALA
            run {
                val spriteFrame = animBullet!!.getKeyFrame(obj.stateTime, false)
                if (obj.isFacingLeft) {
                    spriteFrame!!.setPosition(obj.position.x + .1f, obj.position.y - .1f)
                    spriteFrame.setSize(-.2f, .2f)
                    spriteFrame.draw(batcher)
                } else {
                    spriteFrame!!.setPosition(obj.position.x - .1f, obj.position.y - .1f)
                    spriteFrame.setSize(.2f, .2f)
                    spriteFrame.draw(batcher)
                }
            }

            // MUZZLE FIRE
            if (obj.state == Bullet.STATE_MUZZLE) {
                val spriteFrame = Assets.muzzle!!.getKeyFrame(obj.stateTime, false)
                if (obj.isFacingLeft) {
                    spriteFrame!!.setPosition(obj.oPerWhoFired.position.x + .1f - .42f, obj.oPerWhoFired.position.y - .1f - .14f)
                    spriteFrame.setSize(-.2f, .2f)
                } else {
                    spriteFrame!!.setPosition(obj.oPerWhoFired.position.x - .1f + .42f, obj.oPerWhoFired.position.y - .1f - .14f)
                    spriteFrame.setSize(.2f, .2f)
                }
                spriteFrame.draw(batcher)
            }

            // MUZZLE HIT
            if (obj.state == Bullet.STATE_HIT) {
                val spriteFrame = Assets.muzzle!!.getKeyFrame(obj.stateTime, false)
                if (obj.isFacingLeft) { // Aqui es lo mismo que muzzle fire pero alreves
                    spriteFrame!!.setPosition(obj.position.x - .1f, obj.position.y - .1f)
                    spriteFrame.setSize(.2f, .2f)
                } else {
                    spriteFrame!!.setPosition(obj.position.x + .1f, obj.position.y - .1f)
                    spriteFrame.setSize(-.2f, .2f)
                }
                spriteFrame.draw(batcher)
            }
        }
    }

    companion object {
        private const val WIDTH = Screens.WORLD_WIDTH
        private const val HEIGHT = Screens.WORLD_HEIGHT
    }
}
