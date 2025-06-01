package com.nopalsoft.sokoban.objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.Settings


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

    override fun draw(batch: Batch, parentAlpha: Float) {
        val keyFrame = if (Settings.animationWalkIsON) {
            if (state == STATE_DOWN) {
                Assets.playerDownAnimation.getKeyFrame(stateTime, true)
            } else if (state == STATE_UP) {
                Assets.playerUpAnimation.getKeyFrame(stateTime, true)
            } else if (state == STATE_LEFT) {
                Assets.playerLeftAnimation.getKeyFrame(stateTime, true)
            } else if (state == STATE_RIGHT) {
                Assets.playerRightAnimation.getKeyFrame(stateTime, true)
            } else {
                Assets.playerStand
            }
        } else {
            Assets.playerStand
        }

        batch.draw(keyFrame, x, y, SIZE, SIZE)
    }

    override fun onMovementToPositionCompleted() {
        state = STATE_STAND
        stateTime = 0F
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
