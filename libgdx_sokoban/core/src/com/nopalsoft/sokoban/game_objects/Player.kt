package com.nopalsoft.sokoban.game_objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.Settings

/**
 * The character in the game that the player is controlling.
 */
class Player(position: Int) : Tile(position) {

    private var state = STATE_STAND
    private var stateTime = 0F

    override fun act(delta: Float) {
        super.act(delta)
        stateTime += delta
    }

    fun moveToPosition(pos: Int, up: Boolean, down: Boolean, right: Boolean, left: Boolean) {
        super.moveToPosition(pos, false)

        if (up) {
            state = STATE_UP
        } else if (down) {
            state = STATE_DOWN
        } else if (right) {
            state = STATE_RIGHT
        } else if (left) {
            state = STATE_LEFT
        }
        stateTime = 0f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val keyFrame = if (Settings.animationWalkIsON) {
            when (state) {
                STATE_DOWN -> {
                    Assets.animationMoveDown.getKeyFrame(stateTime, true)
                }

                STATE_UP -> {
                    Assets.animationMoveUp.getKeyFrame(stateTime, true)
                }

                STATE_LEFT -> {
                    Assets.animationMoveLeft.getKeyFrame(stateTime, true)
                }

                STATE_RIGHT -> {
                    Assets.animationMoveRight.getKeyFrame(stateTime, true)
                }

                else -> {
                    Assets.playerStand
                }
            }
        } else {
            Assets.playerStand
        }

        batch?.draw(keyFrame, x, y, size, size)

    }

    override fun endMovingToPosition() {
        state = STATE_STAND
        stateTime = 0f
    }

    fun canMove() = state == STATE_STAND


    companion object {
        private const val STATE_LEFT = 0
        private const val STATE_UP = 1
        private const val STATE_DOWN = 2
        private const val STATE_RIGHT = 3
        private const val STATE_STAND = 4
    }
}