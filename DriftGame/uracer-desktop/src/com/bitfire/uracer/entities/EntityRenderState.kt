package com.bitfire.uracer.entities

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.bitfire.uracer.utils.AlgebraMath.TWO_PI
import com.bitfire.uracer.utils.AlgebraMath.isZero
import com.bitfire.uracer.utils.NewConvert.mt2px
import kotlin.math.abs

class EntityRenderState {

    @JvmField
    var orientation = 0F

    @JvmField
    var position = Vector2(0F, 0F)

    fun set(state: EntityRenderState) {
        this.orientation = state.orientation
        this.position.set(state.position)
    }

    fun set(position: Vector2, orientation: Float) {
        this.position.set(position)
        this.orientation = orientation
    }

    /**
     * Transform the world position from meters to pixels.
     */
    fun toPixels() {
        this.position.x = mt2px(this.position.x)
        this.position.y = mt2px(this.position.y)
        this.orientation = this.orientation * MathUtils.radiansToDegrees
    }

    override fun toString() = "$position, orient=$orientation"

    companion object {
        private val result = EntityRenderState()

        /**
         * Interpolate between the specified render states, by the specified quantity.
         */
        @JvmStatic
        fun interpolate(previous: EntityRenderState, current: EntityRenderState, alpha: Float): EntityRenderState {
            result.position.set(previous.position)
            result.position.set(result.position.lerp(current.position, alpha))

            var curr = current.orientation
            var prev = previous.orientation

            val hasWrapped = ((curr > 0 && prev < 0) || (prev > 0 && curr < 0))
            val needWrap = hasWrapped && (abs(curr) + abs(prev) > 1F)

            if (needWrap) {
                if (prev < 0) {
                    prev += TWO_PI
                } else {
                    curr += TWO_PI
                }

                result.orientation = curr * alpha + prev * (1 - alpha)
                result.orientation = -(TWO_PI - result.orientation)
            } else {
                result.orientation = current.orientation * alpha + previous.orientation * (1 - alpha)
            }

            return result
        }

        /**
         * Returns whether the specified render states are equal with a bias of AMath.CMP_EPSILON
         */
        @JvmStatic
        fun isEqual(first: EntityRenderState, second: EntityRenderState): Boolean {
            val xIsEqual = isZero(abs(first.position.x) - abs(second.position.x))

            if (!xIsEqual) return false

            return isZero(abs(first.position.y) - abs(second.position.y))
        }
    }
}
