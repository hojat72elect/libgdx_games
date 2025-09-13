package com.salvador.bricks.game_objects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

class Background(val backgroundX: Float, val backgroundY: Float) : Actor() {

    val background: Texture = ResourceManager.getTexture("background.png")

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        batch.draw(background, backgroundX, backgroundY, Constants.SCREEN_WIDTH.toFloat(), Constants.SCREEN_HEIGHT.toFloat())
    }
}
