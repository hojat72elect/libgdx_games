package com.bitfire.uracer.game.logic.gametasks.hud

import aurelienribon.tweenengine.TweenAccessor

class HudLabelAccessor : TweenAccessor<HudLabel> {

    override fun getValues(target: HudLabel, tweenType: Int, returnValues: FloatArray) = when (tweenType) {
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

        SCALE -> {
            returnValues[0] = target.scale
            1
        }

        OPACITY -> {
            returnValues[0] = target.getAlpha()
            1
        }

        else -> {
            assert(false)
            -1
        }
    }

    override fun setValues(target: HudLabel, tweenType: Int, newValues: FloatArray) {
        when (tweenType) {
            POSITION_XY -> target.setPosition(newValues[0], newValues[1])
            POSITION_X -> target.x = newValues[0]
            POSITION_Y -> target.y = newValues[0]
            SCALE -> target.scale = newValues[0]
            OPACITY -> target.setAlpha(newValues[0])
            else -> assert(false)
        }
    }

    companion object {
        const val POSITION_XY = 1
        const val POSITION_X = 2
        const val POSITION_Y = 3
        const val SCALE = 4
        const val OPACITY = 5
    }
}
