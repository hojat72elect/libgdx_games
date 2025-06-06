package com.nopalsoft.ninjarunner.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.ninjarunner.AnimationSprite
import com.nopalsoft.ninjarunner.Assets
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
import com.nopalsoft.ninjarunner.screens.Screens

class WorldGameRenderer(batch: SpriteBatch, gameWorld: GameWorld) {
    val WIDTH = Screens.WORLD_WIDTH
    val HEIGHT = Screens.WORLD_HEIGHT

    var batch: SpriteBatch
    var gameWorld: GameWorld
    var camera: OrthographicCamera = OrthographicCamera(WIDTH, HEIGHT)
    var renderBox: Box2DDebugRenderer?

    init {
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0f)
        this.batch = batch
        this.gameWorld = gameWorld
        this.renderBox = Box2DDebugRenderer()
    }

    fun render(delta: Float) {
        camera.position.set(gameWorld.player!!.position.x + 1.5f, gameWorld.player!!.position.y, 0f)

        if (camera.position.y < HEIGHT / 2f) camera.position.y = HEIGHT / 2f
        else if (camera.position.y > HEIGHT / 2f) camera.position.y = HEIGHT / 2f

        if (camera.position.x < WIDTH / 2f) camera.position.x = WIDTH / 2f

        camera.update()
        batch.setProjectionMatrix(camera.combined)
        batch.begin()
        batch.enableBlending()

        renderPlatforms()
        renderPared()

        renderItems()

        renderPlayer()
        renderMascot()

        renderObstacles(delta)
        renderMissile()

        batch.end()
    }

    private fun renderItems() {
        for (item in gameWorld.arrayItem) {
            var spriteFrame: Sprite? = null

            if (item.state == Item.STATE_NORMAL) {
                if (item is ItemCoin) {
                    spriteFrame = Assets.coinAnimation!!.getKeyFrame(item.stateTime, true)
                } else if (item is ItemMagnet) {
                    spriteFrame = Assets.magnet
                } else if (item is ItemEnergy) {
                    spriteFrame = Assets.energy
                } else if (item is ItemHeart) {
                    spriteFrame = Assets.hearth
                } else if (item is ItemCandyJelly) {
                    spriteFrame = Assets.jellyRed
                } else if (item is ItemCandyBean) {
                    spriteFrame = Assets.beanRed
                } else if (item is ItemCandyCorn) {
                    spriteFrame = Assets.candyCorn
                }
            } else {
                spriteFrame = if (item is ItemCandyJelly) {
                    Assets.candyExplosionRed!!.getKeyFrame(item.stateTime, false)
                } else if (item is ItemCandyBean) {
                    Assets.candyExplosionRed!!.getKeyFrame(item.stateTime, false)
                } else {
                    Assets.pickUpAnimation!!.getKeyFrame(item.stateTime, false)
                }
            }

            if (spriteFrame != null) {
                spriteFrame.setPosition(item.position.x - item.WIDTH / 2f, item.position.y - item.HEIGHT / 2f)
                spriteFrame.setSize(item.WIDTH, item.HEIGHT)
                spriteFrame.draw(batch)
            }
        }
    }

    private fun renderPlatforms() {
        for (obj in gameWorld.arrayPlatform) {

            val spriteFrame = Assets.platform

            spriteFrame!!.setPosition(obj.position.x - Platform.WIDTH / 2f, obj.position.y - Platform.HEIGHT / 2f)
            spriteFrame.setSize(Platform.WIDTH, Platform.HEIGHT)
            spriteFrame.draw(batch)
        }
    }

    private fun renderMascot() {
        val oMas = gameWorld.mascot

        val spriteFrame: Sprite?

        var width = oMas!!.drawWidth
        var height = oMas.drawHeight

        if (oMas.mascotType == Mascot.MascotType.BOMB) {
            spriteFrame = Assets.mascotBombFlyAnimation!!.getKeyFrame(oMas.stateTime, true)
        } else {
            if (gameWorld.player!!.isDash) {
                spriteFrame = Assets.mascotBirdDashAnimation!!.getKeyFrame(oMas.stateTime, true)
                width = oMas.dashDrawWidth
                height = oMas.dashDrawHeight
            } else spriteFrame = Assets.mascotBirdFlyAnimation!!.getKeyFrame(oMas.stateTime, true)
        }

        spriteFrame!!.setPosition(oMas.position.x - width + Mascot.RADIUS, gameWorld.mascot!!.position.y - height / 2f)
        spriteFrame.setSize(width, height)
        spriteFrame.draw(batch)
    }

    private fun renderPared() {
        for (obj in gameWorld.arrayWall) {
            val spriteFrame = Assets.wall
            spriteFrame!!.setPosition(obj.position.x - Wall.WIDTH / 2f, obj.position.y - Wall.HEIGHT / 2f)
            spriteFrame.setSize(Wall.WIDTH, Wall.HEIGHT)
            spriteFrame.draw(batch)
        }
    }

    private fun renderObstacles(delta: Float) {
        for (obj in gameWorld.arrayObstacle) {
            if (obj.state == Obstacle.STATE_NORMAL) {
                val width: Float
                val height: Float
                val spriteFrame: Sprite?

                if (obj is ObstacleBoxes4) {
                    width = ObstacleBoxes4.DRAW_WIDTH
                    height = ObstacleBoxes4.DRAW_HEIGHT
                    spriteFrame = Assets.boxes4Sprite
                } else {
                    width = ObstacleBoxes7.DRAW_WIDTH
                    height = ObstacleBoxes7.DRAW_HEIGHT
                    spriteFrame = Assets.boxes7Sprite
                }
                spriteFrame!!.setPosition(obj.position.x - width / 2f, obj.position.y - height / 2f)
                spriteFrame.setSize(width, height)
                spriteFrame.draw(batch)
            } else {
                obj.effect!!.draw(batch, delta)
            }
        }
    }

    private fun renderMissile() {
        for (obj in gameWorld.arrayMissile) {
            val spriteFrame: Sprite?
            val width: Float
            val height: Float

            if (obj.state == Missile.STATE_NORMAL) {
                width = Missile.WIDTH
                height = Missile.HEIGHT
                spriteFrame = Assets.missileAnimation!!.getKeyFrame(obj.stateTime, true)
            } else if (obj.state == Missile.STATE_EXPLODE) {
                width = 1f
                height = .84f
                spriteFrame = Assets.explosionAnimation!!.getKeyFrame(obj.stateTime, false)
            } else continue

            spriteFrame!!.setPosition(obj.position.x - width / 2f, obj.position.y - height / 2f)
            spriteFrame.setSize(width, height)
            spriteFrame.draw(batch)
        }
    }

    private fun renderPlayer() {
        val oPer = gameWorld.player

        val spriteFrame: Sprite?
        val offsetY: Float

        val animIdle: AnimationSprite?
        val animJump: AnimationSprite?
        val animRun: AnimationSprite?
        val animSlide: AnimationSprite?
        val animDash: AnimationSprite?
        val animHurt: AnimationSprite?
        val animDizzy: AnimationSprite?
        val animDead: AnimationSprite?

        when (oPer!!.type) {
            Player.TYPE_GIRL, Player.TYPE_BOY -> {
                animIdle = Assets.girlIdleAnimation
                animJump = Assets.girlJumpAnimation
                animRun = Assets.girlRunAnimation
                animSlide = Assets.girlSlideAnimation
                animDash = Assets.girlDashAnimation
                animHurt = Assets.girlHurtAnimation
                animDizzy = Assets.girlDizzyAnimation
                animDead = Assets.girlDeathAnimation
            }

            Player.TYPE_NINJA -> {
                animIdle = Assets.ninjaIdleAnimation
                animJump = Assets.ninjaJumpAnimation
                animRun = Assets.ninjaRunAnimation
                animSlide = Assets.ninjaSlideAnimation
                animDash = Assets.ninjaDashAnimation
                animHurt = Assets.ninjaHurtAnimation
                animDizzy = Assets.ninjaDizzyAnimation
                animDead = Assets.ninjaDeathAnimation
            }

            else -> {
                animIdle = Assets.ninjaIdleAnimation
                animJump = Assets.ninjaJumpAnimation
                animRun = Assets.ninjaRunAnimation
                animSlide = Assets.ninjaSlideAnimation
                animDash = Assets.ninjaDashAnimation
                animHurt = Assets.ninjaHurtAnimation
                animDizzy = Assets.ninjaDizzyAnimation
                animDead = Assets.ninjaDeathAnimation
            }
        }

        if (oPer.state == Player.STATE_NORMAL) {
            spriteFrame = if (oPer.isIdle) {
                animIdle!!.getKeyFrame(oPer.stateTime, true)
            } else if (oPer.isJumping) {
                animJump!!.getKeyFrame(oPer.stateTime, false)
            } else if (oPer.isSlide) {
                animSlide!!.getKeyFrame(oPer.stateTime, true)
            } else if (oPer.isDash) {
                animDash!!.getKeyFrame(oPer.stateTime, true)
            } else {
                animRun!!.getKeyFrame(oPer.stateTime, true)
            }
            offsetY = .1f
        } else if (oPer.state == Player.STATE_HURT) {
            spriteFrame = animHurt!!.getKeyFrame(oPer.stateTime, false)
            offsetY = .1f
        } else if (oPer.state == Player.STATE_DIZZY) {
            spriteFrame = animDizzy!!.getKeyFrame(oPer.stateTime, true)
            offsetY = .1f
        } else {
            spriteFrame = animDead!!.getKeyFrame(oPer.stateTime, false)
            offsetY = .1f
        }

        spriteFrame!!.setPosition(gameWorld.player!!.position.x - .75f, gameWorld.player!!.position.y - .52f - offsetY)
        spriteFrame.setSize(Player.DRAW_WIDTH, Player.DRAW_HEIGHT)
        spriteFrame.draw(batch)
    }
}
