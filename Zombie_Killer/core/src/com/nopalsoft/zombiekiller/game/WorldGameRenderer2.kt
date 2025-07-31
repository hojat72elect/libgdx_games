package com.nopalsoft.zombiekiller.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.zombiekiller.AnimationSprite
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.game_objects.Bullet
import com.nopalsoft.zombiekiller.game_objects.Hero
import com.nopalsoft.zombiekiller.game_objects.ItemGem
import com.nopalsoft.zombiekiller.game_objects.ItemHeart
import com.nopalsoft.zombiekiller.game_objects.ItemMeat
import com.nopalsoft.zombiekiller.game_objects.ItemShield
import com.nopalsoft.zombiekiller.game_objects.ItemSkull
import com.nopalsoft.zombiekiller.game_objects.ItemStar
import com.nopalsoft.zombiekiller.game_objects.Zombie
import com.nopalsoft.zombiekiller.screens.Screens

class WorldGameRenderer2(batch: SpriteBatch, worldGame: WorldGame) {
    val WIDTH: Float = Screens.WORLD_WIDTH
    val HEIGHT: Float = Screens.WORLD_HEIGHT

    var batch: SpriteBatch
    var worldGame: WorldGame
    var camera: ParallaxCamera
    var tiledRenderer: OrthogonalTiledMapRenderer

    var physicsDebugRenderer: Box2DDebugRenderer?

    var xMin: Float
    var xMax: Float
    var yMin: Float
    var yMax: Float

    var map1: TiledMapTileLayer?
    var map2: TiledMapTileLayer?
    var map3: TiledMapTileLayer?
    var map4: TiledMapTileLayer?

    var mapInFront: TiledMapTileLayer? // In front of the monkey

    var showMoon: Boolean

    init {
        this.camera = ParallaxCamera(WIDTH, HEIGHT)
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0f)
        this.batch = batch
        this.worldGame = worldGame
        this.physicsDebugRenderer = Box2DDebugRenderer()
        tiledRenderer = OrthogonalTiledMapRenderer(Assets.map, worldGame.unitScale)

        // The smaller the number, the first they are rendered.
        map1 = tiledRenderer.getMap().layers.get("1") as TiledMapTileLayer?
        map2 = tiledRenderer.getMap().layers.get("2") as TiledMapTileLayer?
        map3 = tiledRenderer.getMap().layers.get("3") as TiledMapTileLayer?
        map4 = tiledRenderer.getMap().layers.get("4") as TiledMapTileLayer?
        mapInFront = tiledRenderer.getMap().layers.get("inFront") as TiledMapTileLayer?

        xMin = 4.0f // It starts at 4 because the camera is centered not at the origin
        xMax = worldGame.unitScale * worldGame.tiledWidth * 32 - 4 // Minus 4 because the camera is centered on the origin
        yMin = 2.4f
        yMax = worldGame.unitScale * worldGame.tiledHeight * 32 - 1f // Here I'm not going to subtract the -2.4, just -1f so that it has a little more freedom when going up.

        showMoon = MathUtils.randomBoolean()
    }

    fun render() {
        camera.position.x = worldGame.hero!!.position.x
        camera.position.y = worldGame.hero!!.position.y

        // I update the camera so that it doesn't go out of bounds
        if (camera.position.x < xMin) camera.position.x = xMin
        else if (camera.position.x > xMax) camera.position.x = xMax

        if (camera.position.y < yMin) camera.position.y = yMin
        else if (camera.position.y > yMax) camera.position.y = yMax

        camera.update()
        batch.setProjectionMatrix(camera.combined)
        batch.begin()
        batch.disableBlending()
        drawBackGround()
        batch.end()

        batch.setProjectionMatrix(camera.calculateParallaxMatrix(0.5f, 1f))
        batch.begin()
        drawParallaxBackground()
        batch.end()

        if (showMoon) {
            batch.setProjectionMatrix(camera.calculateParallaxMatrix(0.25f, .8f))
            batch.begin()
            batch.enableBlending()
            drawMoon()
            batch.end()
        }

        drawTiled()

        batch.setProjectionMatrix(camera.combined)
        batch.begin()
        batch.enableBlending()
        drawItems()
        drawCrates()
        drawSaw()
        drawZombie()
        drawBullets()
        drawPlayer()

        batch.end()

        drawTiledInfront()
    }

    private fun drawBackGround() {
        batch.draw(Assets.backBackground, camera.position.x - 4f, camera.position.y - 2.4f, 8.0f, 4.8f)
    }

    private fun drawParallaxBackground() {
        var i = 0
        while (i < 2) {
            batch.draw(Assets.background, (-xMin / 2f) + (i * 16), 0f, 8.0f, 4.8f)
            batch.draw(Assets.background, (-xMin / 2f) + ((i + 1) * 16), 0f, -8.0f, 4.8f)
            i += 1
        }
    }

    private fun drawMoon() {
        batch.draw(Assets.moon, 4f, 2.3f, 3.5f, 2.55f)
    }

    private fun drawTiledInfront() {
        tiledRenderer.setView(camera)

        tiledRenderer.getBatch().begin()
        if (mapInFront != null) tiledRenderer.renderTileLayer(mapInFront)
        tiledRenderer.getBatch().end()
    }

    private fun drawTiled() {
        tiledRenderer.setView(camera)
        tiledRenderer.getBatch().begin()
        if (map1 != null) tiledRenderer.renderTileLayer(map1)
        if (map2 != null) tiledRenderer.renderTileLayer(map2)
        if (map3 != null) tiledRenderer.renderTileLayer(map3)
        if (map4 != null) tiledRenderer.renderTileLayer(map4)

        tiledRenderer.getBatch().end()
    }

    private fun drawCrates() {
        for (obj in worldGame.crates!!) {
            val halfSize = obj!!.SIZE / 2f
            batch.draw(
                Assets.crate, obj.position.x - halfSize, obj.position.y - halfSize, halfSize, halfSize, obj.SIZE, obj.SIZE, 1f, 1f,
                obj.angleDeg
            )
        }
    }

    private fun drawSaw() {
        for (obj in worldGame.saws!!) {
            val halfSize = (obj!!.SIZE + .2f) / 2f
            batch.draw(
                Assets.saw, obj.position.x - halfSize, obj.position.y - halfSize, halfSize, halfSize, obj.SIZE + .2f, obj.SIZE + .2f, 1f, 1f,
                obj.angleDeg
            )
        }
    }

    private fun drawItems() {
        var keyframe: TextureRegion? = null

        for (obj in worldGame.items) {
            if (obj is ItemGem) {
                keyframe = Assets.itemGem
            } else if (obj is ItemHeart) {
                keyframe = Assets.itemHeart
            } else if (obj is ItemMeat) {
                keyframe = Assets.itemMeat
            } else if (obj is ItemSkull) {
                keyframe = Assets.itemSkull
            } else if (obj is ItemShield) {
                keyframe = Assets.itemShield
            } else if (obj is ItemStar) {
                keyframe = Assets.itemStar
            }

            batch.draw(keyframe, obj!!.position.x - obj.DRAW_WIDTH / 2f, obj.position.y - obj.DRAW_HEIGHT / 2f, obj.DRAW_WIDTH, obj.DRAW_HEIGHT)
        }
    }

    private fun drawZombie() {
        for (zombie in worldGame.zombies) {
            var animWalk: AnimationSprite? = null
            var animIdle: AnimationSprite? = null
            var animRise: AnimationSprite? = null
            var animDie: AnimationSprite? = null
            var zombieHurt: Sprite? = null

            var ajusteY = 0f

            when (zombie!!.type) {
                Zombie.TYPE_CUASY -> {
                    animWalk = Assets.zombieCuasyWalk
                    animIdle = Assets.zombieCuasyIdle
                    animRise = Assets.zombieCuasyRise
                    animDie = Assets.zombieCuasyDie
                    zombieHurt = Assets.zombieCuasyHurt
                    ajusteY = -.035f
                }

                Zombie.TYPE_FRANK -> {
                    animWalk = Assets.zombieFrankWalk
                    animIdle = Assets.zombieFrankIdle
                    animRise = Assets.zombieFrankRise
                    animDie = Assets.zombieFrankDie
                    zombieHurt = Assets.zombieFrankHurt
                    ajusteY = -.033f
                }

                Zombie.TYPE_KID -> {
                    animWalk = Assets.zombieKidWalk
                    animIdle = Assets.zombieKidIdle
                    animRise = Assets.zombieKidRise
                    animDie = Assets.zombieKidDie
                    zombieHurt = Assets.zombieKidHurt
                }

                Zombie.TYPE_MUMMY -> {
                    animWalk = Assets.zombieMummyWalk
                    animIdle = Assets.zombieMummyIdle
                    animRise = Assets.zombieMummyRise
                    animDie = Assets.zombieMummyDie
                    zombieHurt = Assets.zombieMummyHurt
                    ajusteY = -.035f
                }

                Zombie.TYPE_PAN -> {
                    animWalk = Assets.zombiePanWalk
                    animIdle = Assets.zombiePanIdle
                    animRise = Assets.zombiePanRise
                    animDie = Assets.zombiePanDie
                    zombieHurt = Assets.zombiePanHurt
                    ajusteY = -.038f
                }
            }

            val spriteFrame: Sprite?

            if (zombie.state == Zombie.STATE_NORMAL) {
                if (zombie.isWalking) spriteFrame = animWalk!!.getKeyFrame(zombie.stateTime, true)
                else {
                    spriteFrame = animIdle!!.getKeyFrame(zombie.stateTime, true)
                }
            } else if (zombie.state == Zombie.STATE_RISE) {
                spriteFrame = animRise!!.getKeyFrame(zombie.stateTime, false)
            } else if (zombie.state == Zombie.STATE_DEAD) {
                spriteFrame = animDie!!.getKeyFrame(zombie.stateTime, false)
            } else if (zombie.state == Zombie.STATE_HURT) {
                spriteFrame = zombieHurt
            } else spriteFrame = null

            if (zombie.isFacingLeft) {
                spriteFrame!!.setPosition(zombie.position.x + .29f, zombie.position.y - .34f + ajusteY)
                spriteFrame.setSize(-.8f, .8f)
                spriteFrame.draw(batch)
            } else {
                spriteFrame!!.setPosition(zombie.position.x - .29f, zombie.position.y - .34f + ajusteY)
                spriteFrame.setSize(.8f, .8f)
                spriteFrame.draw(batch)
            }

            // Life bar
            if (zombie.lives > 0 && (zombie.state == Zombie.STATE_NORMAL || zombie.state == Zombie.STATE_HURT)) batch.draw(
                Assets.redBar,
                zombie.position.x - .33f,
                zombie.position.y + .36f,
                .65f * (zombie.lives.toFloat() / zombie.MAX_LIFE),
                .075f
            )
        }
    }

    private fun drawBullets() {
        for (obj in worldGame.bullets) {
            var animBullet: AnimationSprite? = null

            when (obj!!.tipo) {
                Bullet.LEVEL_0 -> animBullet = Assets.bullet1
                Bullet.LEVEL_1 -> animBullet = Assets.bullet2
                Bullet.LEVEL_2 -> animBullet = Assets.bullet3
                Bullet.LEVEL_3 -> animBullet = Assets.bullet4
                Bullet.LEVEL_4_AND_UP -> animBullet = Assets.bullet5
            }

            if (obj.state == Bullet.STATE_DESTROY) continue

            // BALA
            run {
                val spriteFrame = animBullet!!.getKeyFrame(obj.stateTime, false)
                if (obj.isFacingLeft) {
                    spriteFrame!!.setPosition(obj.position.x + .1f, obj.position.y - .1f)
                    spriteFrame.setSize(-.2f, .2f)
                    spriteFrame.draw(batch)
                } else {
                    spriteFrame!!.setPosition(obj.position.x - .1f, obj.position.y - .1f)
                    spriteFrame.setSize(.2f, .2f)
                    spriteFrame.draw(batch)
                }
            }

            // MUZZLE FIRE
            if (obj.state == Bullet.STATE_MUZZLE) {
                val spriteFrame = Assets.muzzle!!.getKeyFrame(obj.stateTime, false)
                if (obj.isFacingLeft) {
                    spriteFrame!!.setPosition(worldGame.hero!!.position.x + .1f - .42f, worldGame.hero!!.position.y - .1f - .14f)
                    spriteFrame.setSize(-.2f, .2f)
                } else {
                    spriteFrame!!.setPosition(worldGame.hero!!.position.x - .1f + .42f, worldGame.hero!!.position.y - .1f - .14f)
                    spriteFrame.setSize(.2f, .2f)
                }
                spriteFrame.draw(batch)
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
                spriteFrame.draw(batch)
            }
        }
    }

    private fun drawPlayer() {
        val obj = worldGame.hero

        var heroClimb: AnimationSprite? = null
        var heroDie: AnimationSprite? = null
        var heroHurt: Sprite? = null
        var heroIdle: Sprite? = null
        var heroShoot: AnimationSprite? = null
        var heroWalk: AnimationSprite? = null

        when (obj!!.type) {
            Hero.TYPE_FORCE -> {
                heroClimb = Assets.heroForceClimb
                heroDie = Assets.heroForceDie
                heroHurt = Assets.heroForceHurt
                heroIdle = Assets.heroForceIdle
                heroShoot = Assets.heroForceShoot
                heroWalk = Assets.heroForceWalk
            }

            Hero.TYPE_RAMBO -> {
                heroClimb = Assets.heroRamboClimb
                heroDie = Assets.heroRamboDie
                heroHurt = Assets.heroRamboHurt
                heroIdle = Assets.heroRamboIdle
                heroShoot = Assets.heroRamboShoot
                heroWalk = Assets.heroRamboWalk
            }

            Hero.TYPE_SOLDIER -> {
                heroClimb = Assets.heroSoldierClimb
                heroDie = Assets.heroSoldierDie
                heroHurt = Assets.heroSoldierHurt
                heroIdle = Assets.heroSoldierIdle
                heroShoot = Assets.heroSoldierShoot
                heroWalk = Assets.heroSoldierWalk
            }

            Hero.TYPE_SWAT -> {
                heroClimb = Assets.heroSwatClimb
                heroDie = Assets.heroSwatDie
                heroHurt = Assets.heroSwatHurt
                heroIdle = Assets.heroSwatIdle
                heroShoot = Assets.heroSwatShoot
                heroWalk = Assets.heroSwatWalk
            }

            Hero.TYPE_VADER -> {
                heroClimb = Assets.heroVaderClimb
                heroDie = Assets.heroVaderDie
                heroHurt = Assets.heroVaderHurt
                heroIdle = Assets.heroVaderIdle
                heroShoot = Assets.heroVaderShoot
                heroWalk = Assets.heroVaderWalk
            }
        }

        val spriteFrame: Sprite?

        if (obj.state == Hero.STATE_NORMAL) {
            if (obj.isClimbing) {
                spriteFrame = heroClimb!!.getKeyFrame(obj.stateTime, true)
            } else if (obj.isWalking) spriteFrame = heroWalk!!.getKeyFrame(obj.stateTime, true)
            else if (obj.isFiring) {
                spriteFrame = heroShoot!!.getKeyFrame(obj.stateTime, true)
            } else {
                spriteFrame = heroIdle
            }
        } else if (obj.state == Hero.STATE_DEAD) {
            spriteFrame = heroDie!!.getKeyFrame(obj.stateTime, false)
        } else if (obj.state == Hero.STATE_HURT) {
            spriteFrame = heroHurt
        } else spriteFrame = null

        // If he is climbing I always draw him on the same side
        if (obj.isClimbing) {
            spriteFrame!!.setPosition(obj.position.x + .35f, obj.position.y - .34f)
            spriteFrame.setSize(-.7f, .77f)
            spriteFrame.draw(batch)
        } else if (obj.isFacingLeft) {
            spriteFrame!!.setPosition(obj.position.x + .29f, obj.position.y - .34f)
            spriteFrame.setSize(-.7f, .7f)
            spriteFrame.draw(batch)
        } else {
            spriteFrame!!.setPosition(obj.position.x - .29f, obj.position.y - .34f)
            spriteFrame.setSize(.7f, .7f)
            spriteFrame.draw(batch)
        }
    }

    class ParallaxCamera(viewportWidth: Float, viewportHeight: Float) : OrthographicCamera(viewportWidth, viewportHeight) {
        var parallaxView: Matrix4 = Matrix4()
        var parallaxCombined: Matrix4 = Matrix4()
        var tmp: Vector3 = Vector3()
        var tmp2: Vector3 = Vector3()

        fun calculateParallaxMatrix(parallaxX: Float, parallaxY: Float): Matrix4 {
            update()
            tmp.set(position)
            tmp.x *= parallaxX
            tmp.y *= parallaxY

            parallaxView.setToLookAt(tmp, tmp2.set(tmp).add(direction), up)
            parallaxCombined.set(projection)
            Matrix4.mul(parallaxCombined.`val`, parallaxView.`val`)
            return parallaxCombined
        }
    }
}
