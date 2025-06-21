package com.nopalsoft.slamthebird.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.game_objects.Enemy
import com.nopalsoft.slamthebird.game_objects.Platform
import com.nopalsoft.slamthebird.game_objects.Player
import com.nopalsoft.slamthebird.game_objects.PowerUp
import com.nopalsoft.slamthebird.screens.BaseScreen

class WorldGameRender(batch: SpriteBatch, worldGame: WorldGame) {

    private var batch: SpriteBatch
    private var worldGame: WorldGame

    private var camera: OrthographicCamera = OrthographicCamera(WIDTH, HEIGHT)

    private var renderBox: Box2DDebugRenderer

    init {
        camera.position[WIDTH / 2f, HEIGHT / 2f] = 0f

        this.batch = batch
        this.worldGame = worldGame
        this.renderBox = Box2DDebugRenderer()
    }

    fun render() {
        camera.position.y = worldGame.player!!.position.y

        if (camera.position.y < HEIGHT / 2f) camera.position.y = HEIGHT / 2f
        else if (camera.position.y > HEIGHT / 2f + 3) camera.position.y = HEIGHT / 2f + 3

        camera.update()
        batch.projectionMatrix = camera.combined

        batch.begin()

        batch.enableBlending()

        drawBackground()
        drawPlatforms()
        drawPowerUps()
        drawCoins()
        drawEnemies()
        drawPlayer()

        batch.end()
    }

    private fun drawBackground() {
        batch.draw(Assets.background, 0f, 0f, WIDTH, HEIGHT + 3)
    }

    private fun drawPlatforms() {
        for (obj in worldGame.arrayPlatforms) {
            var keyFrame: TextureRegion? = Assets.platform

            if (obj.state == Platform.STATE_BROKEN) {
                if (obj.stateTime < Assets.platformBreakAnimation!!.animationDuration) keyFrame = Assets.platformBreakAnimation!!.getKeyFrame(
                    obj.stateTime, false
                )
                else continue
            }

            if (obj.state == Platform.STATE_BREAKABLE) keyFrame = Assets.platformBreakAnimation!!.getKeyFrame(0f)

            if (obj.state == Platform.STATE_CHANGING) batch.draw(
                keyFrame, obj.position.x - .5f,
                obj.position.y - .1f, .5f, .15f, 1f, .3f,
                obj.animationScale, obj.animationScale, 0f
            )
            else batch.draw(
                keyFrame, obj.position.x - .5f,
                obj.position.y - .15f, 1f, .3f
            )

            if (obj.state == Platform.STATE_FIRE) batch.draw(
                Assets.platformFireAnimation!!.getKeyFrame(
                    obj.stateTime, true
                ), obj.position.x - .5f,
                obj.position.y + .1f, 1f, .3f
            )
        }
    }

    private fun drawPowerUps() {
        for (powerUp in worldGame.arrayPowerUps) {
            val keyFrame: TextureRegion? = when (powerUp.type) {
                PowerUp.TYPE_COIN_RAIN -> Assets.coinRainBoost
                PowerUp.TYPE_FREEZE -> Assets.freezeBoost
                PowerUp.TYPE_SUPER_JUMP -> Assets.superJumpBoost
                else -> Assets.invincibilityBoost
            }

            batch.draw(
                keyFrame, powerUp.position.x - .175f,
                powerUp.position.y - .15f, .35f, .3f
            )
        }
    }

    private fun drawCoins() {
        for (coin in worldGame.arrayCoins) {
            batch.draw(
                Assets.coinAnimation!!.getKeyFrame(coin.stateTime, true),
                coin.position.x - .15f, coin.position.y - .15f, .3f, .34f
            )
        }
    }

    private fun drawEnemies() {
        for (enemy in worldGame.arrayEnemies) {
            if (enemy.state == Enemy.STATE_JUST_APPEARED) {
                batch.draw(
                    Assets.flapSpawnRegion, enemy.position.x - .25f,
                    enemy.position.y - .25f, .25f, .25f, .5f, .5f,
                    enemy.visualScale, enemy.visualScale, 0f
                )
                continue
            }
            val keyFrame = if (enemy.state == Enemy.STATE_FLYING) {
                if (enemy.lives >= 3) Assets.redWingsFlapAnimation!!.getKeyFrame(
                    enemy.stateTime, true
                )
                else Assets.blueWingsFlapAnimation!!.getKeyFrame(
                    enemy.stateTime, true
                )
            } else if (enemy.state == Enemy.STATE_EVOLVING) {
                Assets.evolvingFlapAnimation!!.getKeyFrame(enemy.stateTime, true)
            } else {
                Assets.flapBlueRegion
            }

            if (enemy.velocity.x > 0) batch.draw(
                keyFrame, enemy.position.x - .285f,
                enemy.position.y - .21f, .57f, .42f
            )
            else batch.draw(
                keyFrame, enemy.position.x + .285f,
                enemy.position.y - .21f, -.57f, .42f
            )
        }
    }

    private fun drawPlayer() {
        val obj = worldGame.player
        val keyFrame: TextureRegion

        if (obj!!.slam && obj.state == Player.STATE_FALLING) {
            keyFrame = Assets.playerSlamAnimation!!.getKeyFrame(obj.stateTime)
            batch.draw(
                Assets.slamAnimation!!.getKeyFrame(obj.stateTime, true),
                obj.position.x - .4f, obj.position.y - .55f, .8f, .5f
            )
        } else if (obj.state == Player.STATE_FALLING
            || obj.state == Player.STATE_JUMPING
        ) {
            keyFrame = Assets.playerJumpAnimation!!.getKeyFrame(obj.stateTime, true)
        } else keyFrame = Assets.playerHitAnimation!!.getKeyFrame(obj.stateTime, true)

        if (obj.velocity.x > .1f) batch.draw(
            keyFrame, obj.position.x - .3f, obj.position.y - .3f,
            .3f, .3f, .6f, .6f, 1f, 1f, obj.angleDegrees
        )
        else if (obj.velocity.x < -.1f) batch.draw(
            keyFrame, obj.position.x + .3f, obj.position.y - .3f,
            -.3f, .3f, -.6f, .6f, 1f, 1f, obj.angleDegrees
        )
        else batch.draw(
            Assets.player, obj.position.x - .3f,
            obj.position.y - .3f, .3f, .3f, .6f, .6f, 1f, 1f,
            obj.angleDegrees
        )

        // This will render the boosts above the character's head.
        drawActivePowerUp(obj)
    }

    private fun drawActivePowerUp(obj: Player) {
        if (obj.isInvincible || obj.isSuperJump) {
            val timeToAlert = 2.5f // Time for the boost to start flashing
            val boostKeyFrame = if (obj.isInvincible) {
                if (Player.INVINCIBLE_DURATION - obj.invincibilityDuration <= timeToAlert) {
                    Assets.invincibilityBoostEndAnimation!!.getKeyFrame(
                        obj.stateTime, true
                    ) // anim
                } else Assets.invincibilityBoost
            } else { // jump
                if (Player.DURATION_SUPER_JUMP - obj.durationSuperJump <= timeToAlert) {
                    Assets.superJumpBoostEndAnimation!!.getKeyFrame(
                        obj.stateTime, true
                    ) // anim
                } else Assets.superJumpBoost
            }
            batch.draw(
                boostKeyFrame, obj.position.x - .175f,
                obj.position.y + .3f, .35f, .3f
            )
        }
    }

    companion object {
        private const val WIDTH = BaseScreen.WORLD_SCREEN_WIDTH
        private const val HEIGHT = BaseScreen.WORLD_SCREEN_HEIGHT
    }
}
