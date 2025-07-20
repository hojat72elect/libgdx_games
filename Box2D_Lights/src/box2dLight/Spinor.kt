package box2dLight

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.StringBuilder
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Spinor {
    var real = 0f
    var complex = 0f

    constructor()

    constructor(angle: Float) {
        set(angle)
    }

    constructor(copyFrom: Spinor) {
        set(copyFrom)
    }

    constructor(real: Float, complex: Float) {
        set(real, complex)
    }

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

    fun scale(t: Float): Spinor {
        real *= t
        complex *= t
        return this
    }

    fun invert(): Spinor {
        complex = -complex
        scale(len2())
        return this
    }

    fun add(other: Spinor): Spinor {
        real += other.real
        complex += other.complex
        return this
    }

    fun add(angle: Float): Spinor {
        var angle = angle
        angle /= 2f
        real += cos(angle.toDouble()).toFloat()
        complex += sin(angle.toDouble()).toFloat()
        return this
    }

    fun sub(other: Spinor): Spinor {
        real -= other.real
        complex -= other.complex
        return this
    }

    fun sub(angle: Float): Spinor {
        var angle = angle
        angle /= 2f
        real -= cos(angle.toDouble()).toFloat()
        complex -= sin(angle.toDouble()).toFloat()
        return this
    }

    fun len(): Float {
        return sqrt((real * real + complex * complex).toDouble()).toFloat()
    }

    fun len2(): Float {
        return real * real + complex * complex
    }

    fun mul(other: Spinor): Spinor {
        set(
            real * other.real - complex * other.complex, real * other.complex
                    + complex * other.real
        )
        return this
    }

    fun nor(): Spinor {
        val length = len()
        real /= length
        complex /= length
        return this
    }

    fun angle(): Float {
        return atan2(complex.toDouble(), real.toDouble()).toFloat() * 2
    }

    fun lerp(end: Spinor, alpha: Float, tmp: Spinor): Spinor {
        scale(1 - alpha)
        tmp.set(end).scale(alpha)
        add(tmp)
        nor()
        return this
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
