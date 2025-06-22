package com.nopalsoft.slamthebird.game_objects

import com.badlogic.gdx.math.Vector2

class Platform(x: Float, y: Float) {

    var state: Int
    var position = Vector2(x, y)
    var stateTime = 0f

    private var isFire = false
    private var isBreakable = false

    var animationScale: Float // When it changes you will see an animation starting at .5 so that everything doesn't end small

    init {
        state = STATE_NORMAL
        animationScale = .5f
    }

    fun update(delta: Float) {
        stateTime += delta

        if (state == STATE_CHANGING) {
            animationScale = stateTime / TIME_TO_BE_ACTIVE // 1.2 maximum scale

            if (stateTime >= TIME_TO_BE_ACTIVE) {
                if (isFire) state = STATE_FIRE
                else if (isBreakable) state = STATE_BREAKABLE
                stateTime = 0f
            }
        }

        if ((state == STATE_FIRE || state == STATE_BREAKABLE || state == STATE_BROKEN) && stateTime >= DURATION_ACTIVE) {
            isFire = false
            isBreakable = isFire
            state = STATE_NORMAL
            stateTime = 0f
            animationScale = .5f
        }
    }

    fun setFire() {
        state = STATE_CHANGING
        isFire = true
        stateTime = 0f
    }

    fun setBreakable() {
        state = STATE_CHANGING
        isBreakable = true
        stateTime = 0f
    }

    fun setBroken() {
        state = STATE_BROKEN
        stateTime = 0f
    }

    companion object {

        private const val TIME_TO_BE_ACTIVE: Float = 1.25f
        private const val DURATION_ACTIVE: Float = 10f // This time must be less than TIME_TO_CHANGE_STATE_PLATFORM in the WorldGame class
        var WIDTH = .75f
        var HEIGHT = .2f
        const val STATE_NORMAL = 0
        const val STATE_CHANGING = 1
        const val STATE_FIRE = 2
        const val STATE_BREAKABLE = 3
        const val STATE_BROKEN = 4
    }
}
