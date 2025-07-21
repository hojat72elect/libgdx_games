package com.nopalsoft.clumsy.game.arcade

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.clumsy.Assets
import com.nopalsoft.clumsy.objects.Asteroid1
import com.nopalsoft.clumsy.objects.Asteroid2
import com.nopalsoft.clumsy.objects.Asteroid3
import com.nopalsoft.clumsy.objects.Asteroid4
import com.nopalsoft.clumsy.objects.Asteroid5
import com.nopalsoft.clumsy.objects.Asteroid6
import com.nopalsoft.clumsy.objects.Ufo
import com.nopalsoft.clumsy.screens.Screens

class WorldGameRendererArcade(batcher: SpriteBatch, oWorld: WorldGameArcade) {
    val WIDTH: Float = Screens.WORLD_SCREEN_WIDTH.toFloat()
    val HEIGHT: Float = Screens.WORLD_SCREEN_HEIGHT.toFloat()

    var batcher: SpriteBatch
    var oWorld: WorldGameArcade
    var camera: OrthographicCamera

    var renderBox: Box2DDebugRenderer?

    init {
        this.camera = OrthographicCamera(WIDTH, HEIGHT)
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0f)
        this.batcher = batcher
        this.oWorld = oWorld
        this.renderBox = Box2DDebugRenderer()
    }

    fun render() {
        camera.update()
        batcher.setProjectionMatrix(camera.combined)

        batcher.begin()
        batcher.disableBlending()
        batcher.enableBlending()
        drawAsteroids()
        drawRainbowTail()
        drawBird()
        batcher.end()
    }

    private fun drawRainbowTail() {
        for (obj in oWorld.arrTail) {
            batcher.draw(Assets.rainbowLight, obj!!.position.x - .15f, obj.position.y - .12f, .3f, .24f)
        }
    }

    private fun drawBird() {
        val obj = oWorld.oUfo
        val keyFrame: TextureRegion?

        if (obj!!.state == Ufo.STATE_NORMAL) {
            keyFrame = Assets.bird.getKeyFrame(obj.stateTime, true)
        } else {
            keyFrame = Assets.bird.getKeyFrame(obj.stateTime, false)
        }
        batcher.draw(
            keyFrame, obj.position.x - .25f, obj.position.y - .2f, .25f, .2f, .5f, .4f, 1f, 1f,
            Math.toDegrees(obj.angleRad.toDouble()).toFloat()
        )
    }

    private fun drawAsteroids() {
        for (obj in oWorld.arrMeteoros) {
            if (obj is Asteroid1) {
                batcher.draw(
                    Assets.meteor1, obj.position.x - .07f, obj.position.y - .07f, .07f, .07f, .14f, .14f, 1f,
                    1f, obj.angleDeg
                )
            } else if (obj is Asteroid2) {
                batcher.draw(
                    Assets.meteor2, obj.position.x - .11f, obj.position.y - .11f, .11f, .11f, .22f, .22f, 1f,
                    1f, obj.angleDeg
                )
            } else if (obj is Asteroid3) {
                batcher.draw(
                    Assets.meteor3, obj.position.x - .14f, obj.position.y - .13f, .14f, .13f, .18f, .26f, 1f,
                    1f, obj.angleDeg
                )
            } else if (obj is Asteroid4) {
                batcher.draw(
                    Assets.meteor4, obj.position.x - .15f, obj.position.y - .15f, .15f, .15f, .3f, .3f, 1f, 1f,
                    obj.angleDeg
                )
            } else if (obj is Asteroid5) {
                batcher.draw(
                    Assets.meteor5, obj.position.x - .24f, obj.position.y - .21f, .24f, .21f, .48f, .42f, 1f,
                    1f, obj.angleDeg
                )
            } else if (obj is Asteroid6) {
                batcher.draw(
                    Assets.meteor6, obj.position.x - .28f, obj.position.y - .27f, .28f, .27f, .56f, .54f, 1f,
                    1f, obj.angleDeg
                )
            }
        }
    }
}
