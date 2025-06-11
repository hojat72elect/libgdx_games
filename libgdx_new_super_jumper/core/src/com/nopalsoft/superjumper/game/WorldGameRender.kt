package com.nopalsoft.superjumper.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.objects.Bullet
import com.nopalsoft.superjumper.objects.Cloud
import com.nopalsoft.superjumper.objects.Coin
import com.nopalsoft.superjumper.objects.Enemy
import com.nopalsoft.superjumper.objects.LightningBolt
import com.nopalsoft.superjumper.objects.Platform
import com.nopalsoft.superjumper.objects.PlatformPiece
import com.nopalsoft.superjumper.objects.Player
import com.nopalsoft.superjumper.objects.PowerUpItem
import com.nopalsoft.superjumper.screens.Screens

class WorldGameRender(private var batcher: SpriteBatch, private var oWorld: WorldGame) {
    private val WIDTH: Float = Screens.WORLD_WIDTH
    private val HEIGHT: Float = Screens.WORLD_HEIGHT

    private var camera: OrthographicCamera = OrthographicCamera(WIDTH, HEIGHT)
    private var boxRender: Box2DDebugRenderer

    init {
        camera.position[WIDTH / 2f, HEIGHT / 2f] = 0f

        boxRender = Box2DDebugRenderer()
    }

    fun unprojectToWorldCoordinates(touchPoint: Vector3?) {
        camera.unproject(touchPoint)
    }

    fun render() {
        if (oWorld.state == WorldGame.STATE_RUNNING) camera.position.y = oWorld.player!!.position.y

        if (camera.position.y < Screens.WORLD_HEIGHT / 2f) {
            camera.position.y = Screens.WORLD_HEIGHT / 2f
        }

        camera.update()
        batcher.projectionMatrix = camera.combined

        batcher.begin()

        renderPlayer()
        renderPlatforms()
        renderPlatformPieces()
        renderCoins()
        renderItems()
        renderEnemies()
        renderClouds()
        renderLightningBolts()
        renderBullet()

        batcher.end()
    }

    private fun renderPlayer() {
        val keyframe: AtlasRegion

        val obj = oWorld.player

        keyframe = if (obj!!.speed.y > 0) Assets.playerJump!!
        else Assets.playerStand!!

        if (obj.speed.x > 0) batcher.draw(
            keyframe, obj.position.x + Player.DRAW_WIDTH / 2f, obj.position.y - Player.DRAW_HEIGHT / 2f,
            -Player.DRAW_WIDTH / 2f, Player.DRAW_HEIGHT / 2f, -Player.DRAW_WIDTH, Player.DRAW_HEIGHT, 1f, 1f, obj.angleDegree
        )
        else batcher.draw(
            keyframe, obj.position.x - Player.DRAW_WIDTH / 2f, obj.position.y - Player.DRAW_HEIGHT / 2f,
            Player.DRAW_WIDTH / 2f, Player.DRAW_HEIGHT / 2f, Player.DRAW_WIDTH, Player.DRAW_HEIGHT, 1f, 1f, obj.angleDegree
        )

        if (obj.isJetPack) {
            batcher.draw(Assets.jetpack, obj.position.x - .45f / 2f, obj.position.y - .7f / 2f, .45f, .7f)

            val fireFrame = Assets.jetpackFire!!.getKeyFrame(obj.durationJetPack, true)
            batcher.draw(fireFrame, obj.position.x - .35f / 2f, obj.position.y - .95f, .35f, .6f)
        }
        if (obj.isBubble) {
            batcher.draw(Assets.bubble, obj.position.x - .5f, obj.position.y - .5f, 1f, 1f)
        }
    }

    private fun renderPlatforms() {
        for (obj in oWorld.arrayPlatforms) {
            var keyframe: AtlasRegion? = null

            if (obj.type == Platform.TYPE_BREAKABLE) {
                when (obj.color) {
                    Platform.COLOR_BEIGE -> keyframe = Assets.platformBeigeBroken
                    Platform.COLOR_BLUE -> keyframe = Assets.platformBlueBroken
                    Platform.COLOR_GRAY -> keyframe = Assets.platformGrayBroken
                    Platform.COLOR_GREEN -> keyframe = Assets.platformGreenBroken
                    Platform.COLOR_MULTICOLOR -> keyframe = Assets.platformRainbowBroken
                    Platform.COLOR_PINK -> keyframe = Assets.platformPinkBroken
                }
            } else {
                when (obj.color) {
                    Platform.COLOR_BEIGE -> keyframe = Assets.platformBeige
                    Platform.COLOR_BLUE -> keyframe = Assets.platformBlue
                    Platform.COLOR_GRAY -> keyframe = Assets.platformGray
                    Platform.COLOR_GREEN -> keyframe = Assets.platformGreen
                    Platform.COLOR_MULTICOLOR -> keyframe = Assets.platformRainbow
                    Platform.COLOR_PINK -> keyframe = Assets.platformPink
                    Platform.COLOR_BEIGE_LIGHT -> keyframe = Assets.platformBeigeLight
                    Platform.COLOR_BLUE_LIGHT -> keyframe = Assets.platformBlueLight
                    Platform.COLOR_GRAY_LIGHT -> keyframe = Assets.platformGrayLight
                    Platform.COLOR_GREEN_LIGHT -> keyframe = Assets.platformGreenLight
                    Platform.COLOR_MULTICOLOR_LIGHT -> keyframe = Assets.platformRainbowLight
                    Platform.COLOR_PINK_LIGHT -> keyframe = Assets.platformPinkLight
                }
            }
            batcher.draw(
                keyframe, obj.position.x - Platform.DRAW_WIDTH_NORMAL / 2f, obj.position.y - Platform.DRAW_HEIGHT_NORMAL / 2f,
                Platform.DRAW_WIDTH_NORMAL, Platform.DRAW_HEIGHT_NORMAL
            )
        }
    }

    private fun renderPlatformPieces() {
        for (obj in oWorld.arrayPlatformPieces) {
            var keyframe: AtlasRegion? = null

            if (obj.type == PlatformPiece.TYPE_LEFT) {
                when (obj.color) {
                    Platform.COLOR_BEIGE -> keyframe = Assets.platformBeigeLeft
                    Platform.COLOR_BLUE -> keyframe = Assets.platformBlueLeft
                    Platform.COLOR_GRAY -> keyframe = Assets.platformGrayLeft
                    Platform.COLOR_GREEN -> keyframe = Assets.platformGreenLeft
                    Platform.COLOR_MULTICOLOR -> keyframe = Assets.platformRainbowLeft
                    Platform.COLOR_PINK -> keyframe = Assets.platformPinkLeft
                }
            } else {
                when (obj.color) {
                    Platform.COLOR_BEIGE -> keyframe = Assets.platformBeigeRight
                    Platform.COLOR_BLUE -> keyframe = Assets.platformBlueRight
                    Platform.COLOR_GRAY -> keyframe = Assets.platformGrayRight
                    Platform.COLOR_GREEN -> keyframe = Assets.platformGreenRight
                    Platform.COLOR_MULTICOLOR -> keyframe = Assets.platformRainbowRight
                    Platform.COLOR_PINK -> keyframe = Assets.platformPinkRight
                }
            }

            batcher.draw(
                keyframe, obj.position.x - PlatformPiece.DRAW_WIDTH_NORMAL / 2f, obj.position.y - PlatformPiece.DRAW_HEIGHT_NORMAL
                        / 2f, PlatformPiece.DRAW_WIDTH_NORMAL / 2f, PlatformPiece.DRAW_HEIGHT_NORMAL / 2f, PlatformPiece.DRAW_WIDTH_NORMAL,
                PlatformPiece.DRAW_HEIGHT_NORMAL, 1f, 1f, obj.angleDegree
            )
        }
    }

    private fun renderCoins() {
        for (obj in oWorld.arrayCoins) {
            batcher.draw(
                Assets.coin, obj.position.x - Coin.DRAW_WIDTH / 2f, obj.position.y - Coin.DRAW_HEIGHT / 2f, Coin.DRAW_WIDTH,
                Coin.DRAW_HEIGHT
            )
        }
    }

    private fun renderItems() {
        for (obj in oWorld.arrayItems) {
            var keyframe: TextureRegion? = null

            when (obj.type) {
                PowerUpItem.TYPE_BUBBLE -> keyframe = Assets.bubbleSmall
                PowerUpItem.TYPE_JETPACK -> keyframe = Assets.jetpackSmall
                PowerUpItem.TYPE_GUN -> keyframe = Assets.gun
            }

            batcher.draw(keyframe, obj.position.x - PowerUpItem.DRAW_WIDTH / 2f, obj.position.y - PowerUpItem.DRAW_HEIGHT / 2f, PowerUpItem.DRAW_WIDTH, PowerUpItem.DRAW_HEIGHT)
        }
    }

    private fun renderEnemies() {
        for (obj in oWorld.arrayEnemies) {
            val keyframe = Assets.enemyAnimation!!.getKeyFrame(obj.stateTime, true)

            batcher.draw(
                keyframe, obj.position.x - Enemy.DRAW_WIDTH / 2f, obj.position.y - Enemy.DRAW_HEIGHT / 2f, Enemy.DRAW_WIDTH,
                Enemy.DRAW_HEIGHT
            )
        }
    }

    private fun renderClouds() {
        for (obj in oWorld.arrayClouds) {
            var keyframe: TextureRegion? = null

            when (obj.type) {
                Cloud.TYPE_ANGRY -> keyframe = Assets.angryCloud
                Cloud.TYPE_HAPPY -> keyframe = Assets.happyCloud
            }

            batcher.draw(keyframe, obj.position.x - Cloud.DRAW_WIDTH / 2f, obj.position.y - Cloud.DRAW_HEIGHT / 2f, Cloud.DRAW_WIDTH, Cloud.DRAW_HEIGHT)

            if (obj.isBlowing) {
                batcher.draw(Assets.windyCloud, obj.position.x - .35f, obj.position.y - .85f, .6f, .8f)
            }
        }
    }

    private fun renderLightningBolts() {
        for (obj in oWorld.arrayLightningBolts) {
            val keyframe = Assets.lightningBoltAnimation!!.getKeyFrame(obj.stateTime, true)

            batcher.draw(keyframe, obj.position.x - LightningBolt.DRAW_WIDTH / 2f, obj.position.y - LightningBolt.DRAW_HEIGHT / 2f, LightningBolt.DRAW_WIDTH, LightningBolt.DRAW_HEIGHT)
        }
    }

    private fun renderBullet() {
        for (obj in oWorld.arrayBullets) {
            batcher.draw(Assets.bullet, obj.position.x - Bullet.SIZE / 2f, obj.position.y - Bullet.SIZE / 2f, Bullet.SIZE, Bullet.SIZE)
        }
    }
}
