package com.salvador.bricks.game_objects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.salvador.bricks.game_objects.Constants.BRICK_HEIGHT
import com.salvador.bricks.game_objects.Constants.BRICK_WIDTH

class Brick(@JvmField var type: Int, @JvmField var positionX: Int, @JvmField var positionY: Int) : Actor() {

    @JvmField
    var live = true

    @JvmField
    var brickWidth = 75F

    @JvmField
    var brickHeight = 30F
    var brick1: Texture = ResourceManager.getTexture("brick1.png")

    init {
        when (type) {
            1, 11 -> brick1 = ResourceManager.getTexture("brick1.png")
            2, 12 -> brick1 = ResourceManager.getTexture("brick2.png")
            3, 13 -> brick1 = ResourceManager.getTexture("brick3.png")
            4, 14 -> brick1 = ResourceManager.getTexture("brick4.png")
            5, 15 -> brick1 = ResourceManager.getTexture("brick5.png")
            6, 16 -> brick1 = ResourceManager.getTexture("brick6.png")
        }
    }

    fun getBounds() = Rectangle(positionX.toFloat(), positionY.toFloat(), 75F, 30F)

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(brick1, positionX.toFloat(), positionY.toFloat(), BRICK_WIDTH.toFloat(), BRICK_HEIGHT.toFloat())
    }
}
