package box2dLight

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.StringBuilder
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Spinor {
    var real = 0f
    var complex = 0f

    fun set(angle: Float): Spinor {
        var angle = angle
        angle /= 2f
        set(cos(angle.toDouble()).toFloat(), sin(angle.toDouble()).toFloat())
        return this
    }

    fun set(copyFrom: Spinor): Spinor {
        set(copyFrom.real, copyFrom.complex)
        return this
    }

    fun set(real: Float, complex: Float): Spinor {
        this.real = real
        this.complex = complex

        return this
    }

    fun angle(): Float {
        return atan2(complex.toDouble(), real.toDouble()).toFloat() * 2
    }

    fun slerp(dest: Spinor, t: Float): Spinor {
        val tr: Float
        val tc: Float
        val omega: Float
        var cosom: Float
        val sinom: Float
        val scale0: Float
        val scale1: Float

        // cosine
        cosom = real * dest.real + complex * dest.complex

        // adjust signs
        if (cosom < 0) {
            cosom = -cosom
            tc = -dest.complex
            tr = -dest.real
        } else {
            tc = dest.complex
            tr = dest.real
        }

        // coefficients
        if (1f - cosom > COSINE_THRESHOLD) {
            omega = acos(cosom.toDouble()).toFloat()
            sinom = sin(omega.toDouble()).toFloat()
            scale0 = sin(((1f - t) * omega).toDouble()).toFloat() / sinom
            scale1 = sin((t * omega).toDouble()).toFloat() / sinom
        } else {
            scale0 = 1f - t
            scale1 = t
        }

        // final calculation
        complex = scale0 * complex + scale1 * tc
        real = scale0 * real + scale1 * tr

        return this
    }

    override fun toString(): String {
        val result = StringBuilder()
        val radians = angle()
        result.append("radians: ")
        result.append(radians)
        result.append(", degrees: ")
        result.append(radians * MathUtils.radiansToDegrees)
        return result.toString()
    }

    companion object {
        private const val COSINE_THRESHOLD = 0.001f
    }
}
