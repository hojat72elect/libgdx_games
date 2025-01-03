package com.nopalsoft.flappy.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.nopalsoft.flappy.Assets
import com.nopalsoft.flappy.game_objects.Bird
import com.nopalsoft.flappy.game_objects.Pipe
import com.nopalsoft.flappy.screens.Screens

class WorldGameRenderer(private val spriteBatch: SpriteBatch, private val worldGame: WorldGame) {

    private val width = Screens.WORLD_WIDTH
    private val height = Screens.WORLD_HEIGHT
    private val camera = OrthographicCamera(width, height)

    init {
        camera.position.set(width / 2F, height / 2F, 0F)
    }

    fun render() {
        camera.update()
        spriteBatch.projectionMatrix = camera.combined
        spriteBatch.begin()
        spriteBatch.disableBlending()
        drawBackground()
        spriteBatch.enableBlending()
        drawPipe()
        drawBird()
        spriteBatch.end()
    }

    private fun drawBackground() {
        spriteBatch.draw(Assets.background, 0F, 0F, width, height)
    }

    private fun drawPipe() {
        for (obj in worldGame.pipes) {
            if (obj.type == Pipe.TYPE_DOWN) spriteBatch.draw(
                Assets.downPipe, obj.position.x - .5f,
                obj.position.y - 2f, 1f, 4f
            )
            else spriteBatch.draw(
                Assets.upPipe, obj.position.x - .5f,
                obj.position.y - 2f, 1f, 4f
            )
        }
    }

    private fun drawBird() {
        val obj = worldGame.bird

        val keyFrame: TextureRegion = if (obj.state == Bird.STATE_NORMAL) {
            Assets.bird!!.getKeyFrame(obj.stateTime, true)
        } else {
            Assets.bird!!.getKeyFrame(obj.stateTime, false)
        }
        spriteBatch.draw(keyFrame, obj.position.x - .3f, obj.position.y - .25f, .6f, .5f)
    }

}