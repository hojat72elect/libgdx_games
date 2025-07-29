package com.gamestudio24.martianrun.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Body
import com.gamestudio24.martianrun.box2d.GroundUserData
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.AssetsManager.getTextureRegion
import com.gamestudio24.martianrun.utils.Constants
import com.gamestudio24.martianrun.utils.GameManager.Companion.instance

class Ground(body: Body) : GameActor(body) {
    private val textureRegion: TextureRegion? = getTextureRegion(Constants.GROUND_ASSETS_ID)
    private val speed = 10
    private var textureRegionBounds1: Rectangle
    private var textureRegionBounds2: Rectangle

    init {
        textureRegionBounds1 = Rectangle(
            0 - getUserData()!!.width / 2, 0f, getUserData()!!.width,
            getUserData()!!.height
        )
        textureRegionBounds2 = Rectangle(
            getUserData()!!.width / 2, 0f, getUserData()!!.width,
            getUserData()!!.height
        )
    }

    override fun getUserData(): GroundUserData? {
        return userData as GroundUserData?
    }

    override fun act(delta: Float) {
        super.act(delta)

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
            textureRegion, textureRegionBounds1.x, screenRectangle.y, screenRectangle.getWidth(),
            screenRectangle.getHeight()
        )
        batch.draw(
            textureRegion, textureRegionBounds2.x, screenRectangle.y, screenRectangle.getWidth(),
            screenRectangle.getHeight()
        )
    }

    private fun leftBoundsReached(delta: Float): Boolean {
        return (textureRegionBounds2.x - transformToScreen(delta * speed)) <= 0
    }

    private fun updateXBounds(delta: Float) {
        textureRegionBounds1.x += transformToScreen(delta * speed)
        textureRegionBounds2.x += transformToScreen(delta * speed)
    }

    private fun resetBounds() {
        textureRegionBounds1 = textureRegionBounds2
        textureRegionBounds2 = Rectangle(
            textureRegionBounds1.x + screenRectangle.width, 0f, screenRectangle.width,
            screenRectangle.height
        )
    }
}
