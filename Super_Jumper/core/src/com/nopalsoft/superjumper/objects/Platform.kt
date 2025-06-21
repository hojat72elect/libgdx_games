package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable

class Platform : Poolable {
    @JvmField
    var state: Int = 0

    @JvmField
    var type: Int = 0

    @JvmField
    var color: Int = 0

    @JvmField
    val position: Vector2 = Vector2()
    var stateTime: Float = 0f

    fun initialize(x: Float, y: Float, platformType: Int) {
        position[x] = y
        this.type = platformType

        color = if (platformType == TYPE_NORMAL) {
            MathUtils.random(11)
        } else {
            MathUtils.random(5)
        }
        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float) {
        stateTime += delta
    }

    fun setDestroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DESTROY
            stateTime = 0f
        }
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_DESTROY: Int = 1
        const val TYPE_NORMAL: Int = 0
        const val TYPE_BREAKABLE: Int = 1
        const val DRAW_HEIGHT_NORMAL: Float = .45f

        const val DRAW_WIDTH_NORMAL: Float = 1.25f
        const val HEIGHT_NORMAL: Float = .45f
        const val WIDTH_NORMAL: Float = 1.25f
        const val COLOR_BEIGE: Int = 0
        const val COLOR_BLUE: Int = 1
        const val COLOR_GRAY: Int = 2
        const val COLOR_GREEN: Int = 3
        const val COLOR_MULTICOLOR: Int = 4
        const val COLOR_PINK: Int = 5
        const val COLOR_BEIGE_LIGHT: Int = 6
        const val COLOR_BLUE_LIGHT: Int = 7
        const val COLOR_GRAY_LIGHT: Int = 8
        const val COLOR_GREEN_LIGHT: Int = 9
        const val COLOR_MULTICOLOR_LIGHT: Int = 10
        const val COLOR_PINK_LIGHT: Int = 11
    }
}
