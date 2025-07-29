package com.gamestudio24.martianrun.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.AssetsManager.getTextureRegion
import com.gamestudio24.martianrun.utils.Constants
import com.gamestudio24.martianrun.utils.GameManager.Companion.instance

class Background : Actor() {
    private val textureRegion = getTextureRegion(Constants.BACKGROUND_ASSETS_ID)
    private val speed = 100
    private var textureRegionBounds1: Rectangle
    private var textureRegionBounds2: Rectangle

    init {
        textureRegionBounds1 = Rectangle(-Constants.APP_WIDTH / 2f, 0f, Constants.APP_WIDTH.toFloat(), Constants.APP_HEIGHT.toFloat())
        textureRegionBounds2 = Rectangle(Constants.APP_WIDTH / 2f, 0f, Constants.APP_WIDTH.toFloat(), Constants.APP_HEIGHT.toFloat())
    }

    override fun act(delta: Float) {
        if (instance.gameState != GameState.RUNNING) {
            return
        }

        if (leftBoundsReached(delta)) {
            resetBounds()
        } else {
            updateXBounds(-delta)
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(
            textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, Constants.APP_WIDTH.toFloat(),
            Constants.APP_HEIGHT.toFloat()
        )
        batch.draw(
            textureRegion, textureRegionBounds2.x, textureRegionBounds2.y, Constants.APP_WIDTH.toFloat(),
            Constants.APP_HEIGHT.toFloat()
        )
    }

    private fun leftBoundsReached(delta: Float): Boolean {
        return (textureRegionBounds2.x - (delta * speed)) <= 0
    }

    private fun updateXBounds(delta: Float) {
        textureRegionBounds1.x += delta * speed
        textureRegionBounds2.x += delta * speed
    }

    private fun resetBounds() {
        textureRegionBounds1 = textureRegionBounds2
        textureRegionBounds2 = Rectangle(Constants.APP_WIDTH.toFloat(), 0f, Constants.APP_WIDTH.toFloat(), Constants.APP_HEIGHT.toFloat())
    }
}
