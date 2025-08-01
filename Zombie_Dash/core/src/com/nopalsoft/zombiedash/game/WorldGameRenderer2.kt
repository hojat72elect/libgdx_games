package com.nopalsoft.zombiedash.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.zombiedash.AnimationSprite
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.objects.Bullet
import com.nopalsoft.zombiedash.objects.Hero
import com.nopalsoft.zombiedash.objects.ItemGem
import com.nopalsoft.zombiedash.objects.ItemHearth
import com.nopalsoft.zombiedash.objects.ItemShield
import com.nopalsoft.zombiedash.objects.ItemWeapon
import com.nopalsoft.zombiedash.objects.Piso
import com.nopalsoft.zombiedash.objects.Saw
import com.nopalsoft.zombiedash.objects.Zombie
import com.nopalsoft.zombiedash.screens.Screens

class WorldGameRenderer2(batcher: SpriteBatch, oWorld: WorldGame) {
    val WIDTH: Float = Screens.WORLD_WIDTH
    val HEIGHT: Float = Screens.WORLD_HEIGHT

    var batcher: SpriteBatch
    var oWorld: WorldGame
    var oCam: OrthographicCamera

    var renderBox: Box2DDebugRenderer?

    init {
        this.oCam = OrthographicCamera(WIDTH, HEIGHT)
        this.oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0f)
        this.batcher = batcher
        this.oWorld = oWorld
        this.renderBox = Box2DDebugRenderer()
    }

    fun render() {
        oCam.update()
        batcher.setProjectionMatrix(oCam.combined)
        batcher.begin()
        batcher.enableBlending()

        drawPisables()
        drawItems()
        drawCrates()
        drawZombie()
        drawSpikes()
        drawSaw()
        drawBullets()
        drawPlayer()

        batcher.end()

        // renderBox.render(oWorld.oWorldBox, oCam.combined);
    }

    private fun drawPisables() {
        var keyframe: AtlasRegion? = null

        for (obj in oWorld.arrPisables) {
            if (obj is Piso) {
                keyframe = Assets.pisoVerde
            }

            batcher.draw(keyframe, obj!!.position.x - obj.DRAW_WIDTH / 2f, obj.position.y - obj.DRAW_HEIGHT / 2f, obj.DRAW_WIDTH, obj.DRAW_HEIGHT)
        }
    }

    private fun drawCrates() {}

    private fun drawSaw() {
        for (obj in oWorld.arrSaws) {
            if (obj!!.state == Saw.STATE_NORMAL) {
                val halfSize = (Saw.SIZE + .2f) / 2f
                batcher.draw(
                    Assets.saw, obj.position.x - halfSize, obj.position.y - halfSize, halfSize, halfSize, Saw.SIZE + .2f, Saw.SIZE + .2f, 1f,
                    1f, obj.angleDeg
                )
            } else if (obj.state == Saw.STATE_DIALOG) {
                batcher.draw(Assets.sawDialog, 7.35f, obj.position.y - .3f, .7f, .6f)
            }
        }
    }

    private fun drawItems() {
        var keyframe: TextureRegion? = null

        for (obj in oWorld.arrItems) {
            if (obj is ItemGem) {
                keyframe = Assets.itemGem
            } else if (obj is ItemHearth) {
                keyframe = Assets.itemHeart
            } else if (obj is ItemShield) {
                keyframe = Assets.itemShield
            } else if (obj is ItemWeapon) {
                keyframe = Assets.weaponSmall
            }

            batcher.draw(keyframe, obj!!.position.x - obj.DRAW_WIDTH / 2f, obj.position.y - obj.DRAW_HEIGHT / 2f, obj.DRAW_WIDTH, obj.DRAW_HEIGHT)
        }
    }

    private fun drawZombie() {
        for (obj in oWorld.arrZombies) {
            if (!obj!!.canUpdate) continue
            var animWalk: AnimationSprite? = null
            var animRise: AnimationSprite? = null
            var animDie: AnimationSprite? = null
            var zombieHurt: Sprite? = null

            var ajusteY = 0f

            when (obj.tipo) {
                Zombie.TIPO_CUASY -> {
                    animWalk = Assets.zombieCuasyWalk
                    animRise = Assets.zombieCuasyRise
                    animDie = Assets.zombieCuasyDie
                    zombieHurt = Assets.zombieCuasyHurt
                    ajusteY = -.035f
                }

                Zombie.TIPO_FRANK -> {
                    animWalk = Assets.zombieFrankWalk
                    animRise = Assets.zombieFrankRise
                    animDie = Assets.zombieFrankDie
                    zombieHurt = Assets.zombieFrankHurt
                    ajusteY = -.033f
                }

                Zombie.TIPO_KID -> {
                    animWalk = Assets.zombieKidWalk
                    animRise = Assets.zombieKidRise
                    animDie = Assets.zombieKidDie
                    zombieHurt = Assets.zombieKidHurt
                }

                Zombie.TIPO_MUMMY -> {
                    animWalk = Assets.zombieMummyWalk
                    animRise = Assets.zombieMummyRise
                    animDie = Assets.zombieMummyDie
                    zombieHurt = Assets.zombieMummyHurt
                    ajusteY = -.035f
                }

                Zombie.TIPO_PAN -> {
                    animWalk = Assets.zombiePanWalk
                    animRise = Assets.zombiePanRise
                    animDie = Assets.zombiePanDie
                    zombieHurt = Assets.zombiePanHurt
                    ajusteY = -.038f
                }
            }

            val spriteFrame: Sprite?

            if (obj.state == Zombie.STATE_NORMAL) {
                spriteFrame = animWalk!!.getKeyFrame(obj.stateTime, true)
            } else if (obj.state == Zombie.STATE_RISE) {
                spriteFrame = animRise!!.getKeyFrame(obj.stateTime, false)
            } else if (obj.state == Zombie.STATE_DEAD) {
                spriteFrame = animDie!!.getKeyFrame(obj.stateTime, false)
            } else if (obj.state == Zombie.STATE_HURT) {
                spriteFrame = zombieHurt
            } else spriteFrame = null

            if (obj.isFacingLeft) {
                spriteFrame!!.setPosition(obj.position.x + .29f, obj.position.y - .34f + ajusteY)
                spriteFrame.setSize(-.8f, .8f)
                spriteFrame.draw(batcher)
            } else {
                spriteFrame!!.setPosition(obj.position.x - .29f, obj.position.y - .34f + ajusteY)
                spriteFrame.setSize(.8f, .8f)
                spriteFrame.draw(batcher)
            }

            // // Barra de vidas
            if (obj.vidas > 0 && (obj.state == Zombie.STATE_NORMAL || obj.state == Zombie.STATE_HURT)) batcher.draw(
                Assets.redBar,
                obj.position.x - .33f,
                obj.position.y + .36f,
                .65f * (obj.vidas.toFloat() / obj.MAX_LIFE),
                .075f
            )
        }
    }

    private fun drawSpikes() {
        val keyframe: TextureRegion? = Assets.spike

        for (obj in oWorld.arrSpikes) {
            batcher.draw(keyframe, obj!!.position.x - obj.DRAW_WIDTH / 2f, obj.position.y - obj.DRAW_HEIGHT / 2f, obj.DRAW_WIDTH, obj.DRAW_HEIGHT)
        }
    }

    private fun drawBullets() {
        for (obj in oWorld.arrBullets) {
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
                    spriteFrame.draw(batcher)
                } else {
                    spriteFrame!!.setPosition(obj.position.x - .1f, obj.position.y - .1f)
                    spriteFrame.setSize(.2f, .2f)
                    spriteFrame.draw(batcher)
                }
            }

            // MUZZLE FIRE
            if (obj.state == Bullet.STATE_MUZZLE) {
                val spriteFrame = Assets.muzzle.getKeyFrame(obj.stateTime, false)
                if (obj.isFacingLeft) {
                    spriteFrame!!.setPosition(oWorld.oHero!!.position.x + .1f - .42f, oWorld.oHero!!.position.y - .1f - .14f)
                    spriteFrame.setSize(-.2f, .2f)
                } else {
                    spriteFrame!!.setPosition(oWorld.oHero!!.position.x - .1f + .42f, oWorld.oHero!!.position.y - .1f - .14f)
                    spriteFrame.setSize(.2f, .2f)
                }
                spriteFrame.draw(batcher)
            }

            // MUZZLE HIT
            if (obj.state == Bullet.STATE_HIT) {
                val spriteFrame = Assets.muzzle.getKeyFrame(obj.stateTime, false)
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

    private fun drawPlayer() {
        val obj = oWorld.oHero

        var heroRun: AnimationSprite? = null
        var heroDie: AnimationSprite? = null
        var heroJump: AnimationSprite? = null
        var heroHurt: Sprite? = null

        when (obj!!.tipo) {
            Hero.TIPO_FORCE -> {
                heroRun = Assets.heroForceRun
                heroDie = Assets.heroForceDie
                heroHurt = Assets.heroForceHurt
                heroJump = Assets.heroForceJump
            }

            Hero.TIPO_RAMBO -> {
                heroRun = Assets.heroRamboRun
                heroDie = Assets.heroRamboDie
                heroHurt = Assets.heroRamboHurt
                heroJump = Assets.heroRamboJump
            }

            Hero.TIPO_SOLDIER -> {
                heroRun = Assets.heroSoldierRun
                heroDie = Assets.heroSoldierDie
                heroHurt = Assets.heroSoldierHurt
                heroJump = Assets.heroSoldierJump
            }

            Hero.TIPO_SWAT -> {
                heroRun = Assets.heroSwatRun
                heroDie = Assets.heroSwatDie
                heroHurt = Assets.heroSwatHurt
                heroJump = Assets.heroSwatJump
            }

            Hero.TIPO_VADER -> {
                heroRun = Assets.heroVaderRun
                heroDie = Assets.heroVaderDie
                heroHurt = Assets.heroVaderHurt
                heroJump = Assets.heroVaderJump
            }
        }

        val spriteFrame: Sprite?

        if (obj.state == Hero.STATE_NORMAL) {
            if (obj.isJumping) {
                spriteFrame = heroJump!!.getKeyFrame(obj.stateTime, false)
            } else spriteFrame = heroRun!!.getKeyFrame(obj.stateTime, true)
        } else if (obj.state == Hero.STATE_DEAD) {
            spriteFrame = heroDie!!.getKeyFrame(obj.stateTime, false)
        } else if (obj.state == Hero.STATE_HURT) {
            spriteFrame = heroHurt
        } else spriteFrame = null

        if (spriteFrame == null) return

        spriteFrame.setPosition(obj.position.x - .29f, obj.position.y - .34f)
        spriteFrame.setSize(.7f, .7f)
        spriteFrame.draw(batcher)
    }
}
