package com.salvador.bricks.game_objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.utils.TimeUtils
import com.salvador.bricks.game_objects.Constants.PADDLE_HEIGHT
import com.salvador.bricks.game_objects.Constants.PADDLE_WIDTH
import com.salvador.bricks.game_objects.Constants.SCREEN_WIDTH

class Paddle(x: Float, y: Float) : Actor() {

    @JvmField
    var position = Vector2(x, y)
    var paddleWidth = PADDLE_WIDTH.toFloat()
    var paddleHeight = PADDLE_HEIGHT.toFloat()
    var didSizeChange = false
    var tPaddle: Texture = ResourceManager.getTexture("paddle.png")
    var start = 0L

    init {

        setBounds(x, y, PADDLE_WIDTH.toFloat(), PADDLE_HEIGHT.toFloat())
        addListener(object : InputListener() {

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = true

            override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {

                super.touchDragged(event, x, y, pointer)
                val screenW = Gdx.graphics.width.toFloat()
                val xx = Gdx.input.x.toFloat()

                val x1 = (xx) * (450F / screenW)

                setPosition(x1, getY())
                position.x = x1
            }
        })
    }

    fun getBounds() = Rectangle(position.x, position.y, paddleWidth, paddleHeight)

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        position.x = x
        position.y = y
        if (position.x < 0) position.x = 0f

        if (position.x + 130 > 450) position.x = SCREEN_WIDTH - 130F

        if (didSizeChange) {
            val diffInMillis = TimeUtils.timeSinceMillis(start)
            if (diffInMillis > 5000) {
                didSizeChange = false
                paddleWidth = PADDLE_WIDTH.toFloat()
                paddleHeight = PADDLE_HEIGHT.toFloat()
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(tPaddle, position.x, position.y, paddleWidth, paddleHeight)
    }

    override fun setSize(width: Float, height: Float) {
        this.paddleWidth = width
        this.paddleHeight = height
        didSizeChange = true
        start = TimeUtils.millis()
    }
}
