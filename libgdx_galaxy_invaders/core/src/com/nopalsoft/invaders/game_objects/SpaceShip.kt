package com.nopalsoft.invaders.game_objects

import com.badlogic.gdx.Gdx
import com.nopalsoft.invaders.game.World

class SpaceShip(x: Float, y: Float) : DynamicGameObject(x, y, WIDTH, HEIGHT) {
    @JvmField
    var shieldCount: Int = 1

    @JvmField
    var lives: Int = 3

    @JvmField
    var state: Int

    @JvmField
    var stateTime: Float = 0f

    init {
        // You start with 1 shield in case the bastards hit you.
        state = SPACESHIP_STATE_NORMAL
        Gdx.app.log("State", "The ship was created")
    }

    fun update(deltaTime: Float) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)
        boundsRectangle!!.x = position.x - boundsRectangle.width / 2
        boundsRectangle.y = position.y - boundsRectangle.height / 2

        if (state == SPACESHIP_STATE_BEING_HIT && stateTime > BEING_HIT_DURATION) {
            state = SPACESHIP_STATE_NORMAL
            stateTime = 0f
            Gdx.app.log("State", "It changed to normal")
        }

        if (position.x < WIDTH / 2) position.x = WIDTH / 2
        if (position.x > World.WIDTH - WIDTH / 2) position.x = World.WIDTH - WIDTH / 2
        stateTime += deltaTime
    }

    fun beingHit() {
        if (shieldCount > 0) {
            shieldCount--
        } else {
            lives--
            if (lives <= 0) {
                state = SPACESHIP_STATE_EXPLODE
                stateTime = 0f
                velocity[0f] = 0f
            } else {
                state = SPACESHIP_STATE_BEING_HIT
                stateTime = 0f
            }
        }
    }

    fun hitVidaExtra() {
        if (lives < 99) {
            lives++
        }
    }

    fun hitEscudo() {
        stateTime = 0f
        shieldCount = 3
    }

    companion object {
        const val DRAW_WIDTH: Float = 4.5f
        const val DRAW_HEIGHT: Float = 3.6f

        const val WIDTH: Float = 4f
        const val HEIGHT: Float = 2.5f

        const val SPACESHIP_SPEED: Float = 50f

        const val SPACESHIP_STATE_NORMAL: Int = 0
        const val SPACESHIP_STATE_EXPLODE: Int = 1
        const val SPACESHIP_STATE_BEING_HIT: Int = 2

        const val EXPLOSION_DURATION: Float = 0.05f * 19
        const val BEING_HIT_DURATION: Float = 0.05f * 21 // One more so that I have a little time to think haha
    }
}
