package com.nopalsoft.invaders.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.Settings
import com.nopalsoft.invaders.game_objects.AlienShip
import com.nopalsoft.invaders.game_objects.Boost
import com.nopalsoft.invaders.game_objects.Missile
import com.nopalsoft.invaders.game_objects.SpaceShip
import com.nopalsoft.invaders.screens.Screens

class WorldRenderer(batch: SpriteBatch, private var world: World) {
    private var camera = OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT)
    private var batch: SpriteBatch

    init {
        camera.position[FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2f] = 0f
        this.batch = batch
    }

    fun render(deltaTime: Float) {
        camera.update()
        batch.projectionMatrix = camera.combined
        renderBackground(deltaTime)
        renderObjects()
    }

    private fun renderBackground(deltaTime: Float) {
        batch.disableBlending()
        batch.begin()

        batch.end()
        if (world.state == World.STATE_RUNNING) {
            Assets.backgroundLayer!!.render(deltaTime)
        } else { // GAMEOVER, PAUSE, READY, ETC
            Assets.backgroundLayer!!.render(0f)
        }
    }

    private fun renderObjects() {
        batch.enableBlending()
        batch.begin()
        renderNave()
        renderAliens()
        renderShipBullet()
        renderAlienBullet()
        renderMissil()
        renderBoost()
        batch.end()
        if (Settings.drawDebugLines) {
            renderDebugBounds()
        }
    }

    private fun renderNave() {
        val keyFrame = if (world.oSpaceShip.state == SpaceShip.SPACESHIP_STATE_NORMAL) {
            if (world.oSpaceShip.velocity.x < -3) Assets.spaceShipLeft
            else if (world.oSpaceShip.velocity.x > 3) Assets.spaceShipRight
            else Assets.spaceShip
        } else {
            Assets.explosionAnimation!!.getKeyFrame(world.oSpaceShip.stateTime, false)
        }
        batch.draw(
            keyFrame, world.oSpaceShip.position.x - SpaceShip.DRAW_WIDTH / 2f, world.oSpaceShip.position.y - SpaceShip.DRAW_HEIGHT
                    / 2f, SpaceShip.DRAW_WIDTH, SpaceShip.DRAW_HEIGHT
        )

        // Draw the ship's shield
        if (world.oSpaceShip.shieldCount > 0) {
            batch.draw(
                Assets.shieldAnimation!!.getKeyFrame(world.oSpaceShip.stateTime, true), world.oSpaceShip.position.x - 5.5f,
                world.oSpaceShip.position.y - 5.5f, 11f, 11f
            )
        }
    }

    private fun renderAliens() {
        val len = world.alienShips.size
        for (i in 0..<len) {
            val oAlienShip = world.alienShips[i]
            val keyFrame = if (oAlienShip.state == AlienShip.EXPLODING) {
                Assets.explosionAnimation!!.getKeyFrame(oAlienShip.stateTime, false)
            } else {
                if (oAlienShip.remainingLives >= 10) Assets.alien4
                else if (oAlienShip.remainingLives >= 5) Assets.alien3
                else if (oAlienShip.remainingLives >= 2) Assets.alien2
                else Assets.alien1
            }

            batch.draw(
                keyFrame, oAlienShip.position.x - AlienShip.DRAW_WIDTH / 2f, oAlienShip.position.y
                        - AlienShip.DRAW_HEIGHT / 2f, AlienShip.DRAW_WIDTH, AlienShip.DRAW_HEIGHT
            )
        }
    }

    private fun renderShipBullet() {
        for (i in 0..<world.shipBullets.size) {
            val bullet = world.shipBullets[i]

            if (bullet.level <= 1) {
                batch.draw(Assets.normalBullet, bullet.position.x - 0.15f, bullet.position.y - 0.45f, 0.3f, 0.9f)
            } else if (bullet.level == 2) {
                batch.draw(Assets.bulletLevel1, bullet.position.x - 1.05f, bullet.position.y - 0.75f, 2.1f, 1.5f)
            } else if (bullet.level == 3) {
                batch.draw(Assets.bulletLevel2, bullet.position.x - 1.05f, bullet.position.y - 0.75f, 2.1f, 1.5f)
            } else if (bullet.level == 4) {
                batch.draw(Assets.bulletLevel3, bullet.position.x - 1.05f, bullet.position.y - 0.75f, 2.1f, 1.5f)
            } else {
                batch.draw(Assets.bulletLevel4, bullet.position.x - 1.05f, bullet.position.y - 0.75f, 2.1f, 1.5f)
            }
        }
    }


    private fun renderAlienBullet() {
        val len = world.alienBullets.size
        for (i in 0..<len) {
            val oAlienBullet = world.alienBullets[i]
            batch.draw(
                Assets.normalEnemyBullet, oAlienBullet.position.x - 0.15f, oAlienBullet.position.y - 0.45f, 0.3f,
                0.9f
            )
        }
    }

    private fun renderMissil() {
        val len = world.missiles.size
        for (i in 0..<len) {
            val oMissile = world.missiles[i]
            val widht: Float
            val heigth: Float
            val keyFrame: TextureRegion
            when (oMissile.state) {
                Missile.STATE_LAUNCHED -> {
                    keyFrame = Assets.missileAnimation!!.getKeyFrame(oMissile.stateTime, true)!!
                    widht = .8f
                    heigth = 2.5f
                }

                Missile.STATE_EXPLODING -> {
                    keyFrame = Assets.explosionAnimation!!.getKeyFrame(oMissile.stateTime, false)!!
                    run {
                        heigth = 15.0f
                        widht = heigth
                    }
                }

                else -> {
                    keyFrame = Assets.explosionAnimation!!.getKeyFrame(oMissile.stateTime, false)!!
                    run {
                        heigth = 15.0f
                        widht = heigth
                    }
                }
            }

            batch.draw(keyFrame, oMissile.position.x - widht / 2f, oMissile.position.y - heigth / 2f, widht, heigth)
        }
    }

    private fun renderBoost() {
        val len = world.boosts.size
        for (i in 0..<len) {
            val oBoost = world.boosts[i]

            val keyFrame: TextureRegion? = when (oBoost.type) {
                Boost.EXTRA_LIFE_BOOST -> Assets.upgLife

                Boost.EXTRA_MISSILE_BOOST -> Assets.boost1
                Boost.EXTRA_SHIELD_BOOST -> Assets.boost2
                else -> Assets.boost3
            }

            batch.draw(
                keyFrame, oBoost.position.x - Boost.DRAW_SIZE / 2f, oBoost.position.y - Boost.DRAW_SIZE / 2f,
                Boost.DRAW_SIZE, Boost.DRAW_SIZE
            )
        }
    }

    private fun renderDebugBounds() {
        val render = ShapeRenderer()
        render.projectionMatrix = camera.combined
        render.begin(ShapeType.Line)

        val naveBounds = world.oSpaceShip.boundsRectangle
        render.rect(naveBounds!!.x, naveBounds.y, naveBounds.width, naveBounds.height)

        for (obj in world.alienShips) {
            val objBounds = obj.boundsCircle
            render.circle(objBounds!!.x, objBounds.y, objBounds.radius)
        }

        for (obj in world.boosts) {
            val objBounds = obj.boundsCircle
            render.circle(objBounds!!.x, objBounds.y, objBounds.radius)
        }

        render.end()
    }

    companion object {
        const val FRUSTUM_WIDTH: Float = Screens.WORLD_SCREEN_WIDTH.toFloat()
        const val FRUSTUM_HEIGHT: Float = Screens.WORLD_SCREEN_HEIGHT.toFloat()
    }
}
