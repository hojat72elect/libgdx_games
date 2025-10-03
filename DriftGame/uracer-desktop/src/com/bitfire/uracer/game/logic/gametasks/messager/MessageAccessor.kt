package com.bitfire.uracer.game.logic.gametasks.messager

import aurelienribon.tweenengine.TweenAccessor

class MessageAccessor : TweenAccessor<Message> {

    override fun getValues(target: Message, tweenType: Int, returnValues: FloatArray): Int {
        return when (tweenType) {
            POSITION_XY -> {
                returnValues[0] = target.x
                returnValues[1] = target.y
                2
            }
            POSITION_X -> {
                returnValues[0] = target.x
                1
            }
            POSITION_Y -> {
                returnValues[0] = target.y
                1
            }
            SCALE_XY -> {
                returnValues[0] = target.scaleX
                returnValues[1] = target.scaleY
                2
            }
            OPACITY -> {
                returnValues[0] = target.alpha
                1
            }
            else -> {
                assert(false)
                -1
            }
        }
    }

    override fun setValues(target: Message, tweenType: Int, newValues: FloatArray) {
        when (tweenType) {
            POSITION_XY -> target.setPosition(newValues[0], newValues[1])
            POSITION_X -> target.x = newValues[0]
            POSITION_Y -> target.y = newValues[0]
            SCALE_XY -> target.setScale(newValues[0], newValues[1])
            OPACITY -> target.alpha = newValues[0]
            else -> assert(false)
        }
    }

    companion object {
        private const val POSITION_XY = 1
        private const val POSITION_X = 2
        const val POSITION_Y = 3
        const val SCALE_XY = 4
        const val OPACITY = 5
    }
}
