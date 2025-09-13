package com.salvador.bricks.game_objects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.TimeUtils
import com.salvador.bricks.game_objects.Constants.BALL_HEIGHT
import com.salvador.bricks.game_objects.Constants.BALL_SPEED
import com.salvador.bricks.game_objects.Constants.BALL_WIDTH

class Ball(x: Float, y: Float) : Actor() {

    @JvmField
    var position = Vector2(x, y)

    @JvmField
    var speedX = 300F

    @JvmField
    var speedY = 300F

    @JvmField
    var isFireballActive = false
    var isVelocityModified = false

    @JvmField
    var pause = false
    private var ball: Texture = ResourceManager.getTexture("ball.png")
    private var startX = 0L
    private var start = 0L

    fun getBounds() = Rectangle(position.x, position.y, BALL_WIDTH.toFloat(), BALL_HEIGHT.toFloat())

    fun setVelocity(speed: Float) {
        if (speedY > 0) this.speedY = speed
        if (speedY < 0) this.speedY = -speed
        if (speedX > 0) this.speedX = speed
        if (speedY < 0) this.speedX = -speed

        isVelocityModified = true
        startX = TimeUtils.millis()
    }

    fun setDefaultSpeed() {
        if (speedY > 0)
            this.speedY = BALL_SPEED
        else
            this.speedY = -BALL_SPEED

        if (speedX > 0)
            this.speedX = BALL_SPEED
        else
            this.speedX = -BALL_SPEED

        isVelocityModified = true
        startX = TimeUtils.millis()
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (pause.not()) {
            position.x += speedX * delta
            position.y += speedY * delta
        }
        if (isFireballActive) {
            val diffInMillis = TimeUtils.timeSinceMillis(start)
            if (diffInMillis > 2_000) {
                isFireballActive = false
                ball = ResourceManager.getTexture("ball.png")
            }
        }
        if (isVelocityModified) {
            val diffInMillis = TimeUtils.timeSinceMillis(startX)
            if (diffInMillis > 5_000) {
                isVelocityModified = false
                setDefaultSpeed()
            }
        }
    }

    fun setFireBall() {
        isFireballActive = true
        ball = ResourceManager.getTexture("ball_fire.png")
        start = TimeUtils.millis()
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        position.x = x
        position.y = y
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(ball, position.x, position.y, BALL_WIDTH.toFloat(), BALL_HEIGHT.toFloat())
    }
}
