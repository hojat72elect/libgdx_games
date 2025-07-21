package com.nopalsoft.clumsy.game.classic

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.clumsy.Assets
import com.nopalsoft.clumsy.objects.Pipes
import com.nopalsoft.clumsy.objects.Ufo
import com.nopalsoft.clumsy.screens.Screens

class WorldGameRenderer(batch: SpriteBatch, worldGameClassic: WorldGameClassic) {
    val WIDTH: Float = Screens.WORLD_SCREEN_WIDTH.toFloat()
    val HEIGHT: Float = Screens.WORLD_SCREEN_HEIGHT.toFloat()

    var batch: SpriteBatch
    var worldGameClassic: WorldGameClassic
    var camera: OrthographicCamera

    var renderBox: Box2DDebugRenderer?

    init {
        this.camera = OrthographicCamera(WIDTH, HEIGHT)
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0f)
        this.batch = batch
        this.worldGameClassic = worldGameClassic
        this.renderBox = Box2DDebugRenderer()
    }

    fun render() {
        camera.update()
        batch.setProjectionMatrix(camera.combined)

        batch.begin()
        batch.disableBlending()
        batch.enableBlending()
        drawTuberia()
        drawArcoiris()
        drawBird()
        batch.end()
    }

    private fun drawArcoiris() {
        for (obj in worldGameClassic.arrTail) {
            batch.draw(Assets.rainbowLight, obj.position.x - .15f, obj.position.y - .12f, .3f, .24f)
        }
    }

    private fun drawBird() {
        val obj = worldGameClassic.oUfo
        val keyFrame: TextureRegion?

        if (obj.state == Ufo.STATE_NORMAL) {
            keyFrame = Assets.bird.getKeyFrame(obj.stateTime, true)
        } else {
            keyFrame = Assets.bird.getKeyFrame(obj.stateTime, false)
        }
        batch.draw(
            keyFrame, obj.position.x - .25f, obj.position.y - .2f, .25f, .2f, .5f, .4f, 1f, 1f,
            Math.toDegrees(obj.angleRad.toDouble()).toFloat()
        )
    }

    private fun drawTuberia() {
        for (obj in worldGameClassic.arrTuberias) {
            if (obj.type == Pipes.LOWER_PIPE) batch.draw(Assets.lowerTube, obj.position.x - .35f, obj.position.y - 2f, .7f, 4f)
            else batch.draw(Assets.upperTube, obj.position.x - .35f, obj.position.y - 2f, .7f, 4f)
        }
    }
}
