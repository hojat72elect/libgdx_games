package com.salvador.bricks.game_objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.salvador.bricks.game_objects.Constants.POWER_UP_ADD_ONE_BALL
import com.salvador.bricks.game_objects.Constants.POWER_UP_FIREBALL
import com.salvador.bricks.game_objects.Constants.POWER_UP_PADDLE_SIZE

class PowerUp(@JvmField var type: Int, var positionX: Float, var positionY: Float) : Actor() {

    private val powerUp = when (type) {
        POWER_UP_ADD_ONE_BALL -> Texture(Gdx.files.internal("powerupBlue.png"))
        POWER_UP_PADDLE_SIZE -> Texture(Gdx.files.internal("powerupGreen.png"))
        POWER_UP_FIREBALL -> Texture(Gdx.files.internal("powerupRed.png"))
        else -> Texture(Gdx.files.internal("powerupYellow.png"))
    }

    var speedX = 150F

    @JvmField
    var live = true

    fun getBounds() = Rectangle(positionX, positionY, 30F, 30F)

    override fun act(delta: Float) {
        super.act(delta)
        positionY -= speedX * delta
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        this.positionX = x
        this.positionY = y
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(powerUp, positionX, positionY, 30F, 30F)
    }
}
