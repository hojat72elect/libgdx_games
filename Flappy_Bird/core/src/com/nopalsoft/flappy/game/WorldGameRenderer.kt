package com.nopalsoft.flappy.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.nopalsoft.flappy.Assets
import com.nopalsoft.flappy.game_objects.Bird
import com.nopalsoft.flappy.game_objects.Pipe
import com.nopalsoft.flappy.screens.Screens

class WorldGameRenderer(private val batcher: SpriteBatch, private val oWorld: WorldGame) {
    private val worldWidth = Screens.WORLD_WIDTH
    private val worldHeight = Screens.WORLD_HEIGHT


    private var camera = OrthographicCamera(worldWidth, worldHeight)

    init {
        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0f)
    }

    fun render() {
        camera.update()
        batcher.projectionMatrix = camera.combined

        batcher.begin()
        batcher.disableBlending()
        drawBackground()
        batcher.enableBlending()
        drawPipe()
        drawBird()

        batcher.end()
    }

    private fun drawBackground() {
        batcher.draw(Assets.background, 0f, 0f, worldWidth, worldHeight)
    }

    private fun drawPipe() {
        for (obj in oWorld.pipes) {
            if (obj.type == Pipe.TYPE_DOWN) batcher.draw(
                Assets.downPipe, obj.position.x - .5f,
                obj.position.y - 2f, 1f, 4f
            )
            else batcher.draw(
                Assets.upPipe, obj.position.x - .5f,
                obj.position.y - 2f, 1f, 4f
            )
        }
    }

    private fun drawBird() {
        val obj = oWorld.bird

        val keyFrame: TextureRegion = if (obj!!.state == Bird.STATE_NORMAL) {
            Assets.bird.getKeyFrame(obj.stateTime, true)
        } else {
            Assets.bird.getKeyFrame(obj.stateTime, false)
        }
        batcher.draw(keyFrame, obj.position.x - .3f, obj.position.y - .25f, .6f, .5f)
    }
}
