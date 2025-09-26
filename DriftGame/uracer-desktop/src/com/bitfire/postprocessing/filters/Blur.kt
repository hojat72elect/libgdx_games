package com.bitfire.postprocessing.filters

import com.badlogic.gdx.utils.IntMap
import com.bitfire.postprocessing.utils.PingPongBuffer
import kotlin.math.exp
import kotlin.math.sqrt

class Blur(width: Int, height: Int) : MultipassFilter() {

    private val convolve = IntMap<Convolve2D>(Tap.entries.size)
    private var type: BlurType? = null
    private var amount = 1F
    var passes = 1
    private val invWidth = 1F / width
    private val invHeight = 1F / height

    init {
        for (tap in Tap.entries) convolve.put(tap.radius, Convolve2D(tap.radius))

        setType(BlurType.Gaussian5x5)
    }

    fun dispose() {
        for (c in convolve.values()) c.dispose()
    }

    fun setType(type: BlurType) {
        if (this.type != type) {
            this.type = type
            computeBlurWeightings()
        }
    }

    // not all blur types support custom amounts at this time
    fun setAmount(amount: Float) {
        this.amount = amount
        computeBlurWeightings()
    }

    override fun render(srcdest: PingPongBuffer) {
        val c = convolve.get(this.type!!.tap.radius)
        for (i in 0..<this.passes) c.render(srcdest)
    }

    private fun computeBlurWeightings() {
        var hasdata = true
        val c = convolve.get(this.type!!.tap.radius)

        val outWeights = c.weights
        val outOffsetsH = c.offsetsHor
        val outOffsetsV = c.offsetsVert

        val dx = this.invWidth
        val dy = this.invHeight

        when (this.type) {
            BlurType.Gaussian3x3, BlurType.Gaussian5x5 -> {
                computeKernel(this.type!!.tap.radius, this.amount, outWeights)
                computeOffsets(this.type!!.tap.radius, this.invWidth, this.invHeight, outOffsetsH, outOffsetsV)
            }

            BlurType.Gaussian3x3b -> {
                // weights and offsets are computed from a binomial distribution
                // and reduced to be used *only* with bilinearly-filtered texture lookups
                //
                // with radius = 1f

                // weights
                outWeights[0] = 0.352941f
                outWeights[1] = 0.294118f
                outWeights[2] = 0.352941f

                // horizontal offsets
                outOffsetsH[0] = -1.33333f
                outOffsetsH[1] = 0f
                outOffsetsH[2] = 0f
                outOffsetsH[3] = 0f
                outOffsetsH[4] = 1.33333f
                outOffsetsH[5] = 0f

                // vertical offsets
                outOffsetsV[0] = 0f
                outOffsetsV[1] = -1.33333f
                outOffsetsV[2] = 0f
                outOffsetsV[3] = 0f
                outOffsetsV[4] = 0f
                outOffsetsV[5] = 1.33333f

                // scale offsets from binomial space to screen space
                var i = 0
                while (i < c.length * 2) {
                    outOffsetsH[i] *= dx
                    outOffsetsV[i] *= dy
                    i++
                }
            }

            BlurType.Gaussian5x5b -> {
                // weights and offsets are computed from a binomial distribution
                // and reduced to be used *only* with bilinearly-filtered texture lookups
                //
                // with radius = 2f

                // weights
                outWeights[0] = 0.0702703f
                outWeights[1] = 0.316216f
                outWeights[2] = 0.227027f
                outWeights[3] = 0.316216f
                outWeights[4] = 0.0702703f

                // horizontal offsets
                outOffsetsH[0] = -3.23077f
                outOffsetsH[1] = 0f
                outOffsetsH[2] = -1.38462f
                outOffsetsH[3] = 0f
                outOffsetsH[4] = 0f
                outOffsetsH[5] = 0f
                outOffsetsH[6] = 1.38462f
                outOffsetsH[7] = 0f
                outOffsetsH[8] = 3.23077f
                outOffsetsH[9] = 0f

                // vertical offsets
                outOffsetsV[0] = 0f
                outOffsetsV[1] = -3.23077f
                outOffsetsV[2] = 0f
                outOffsetsV[3] = -1.38462f
                outOffsetsV[4] = 0f
                outOffsetsV[5] = 0f
                outOffsetsV[6] = 0f
                outOffsetsV[7] = 1.38462f
                outOffsetsV[8] = 0f
                outOffsetsV[9] = 3.23077f

                // scale offsets from binomial space to screen space
                var i = 0
                while (i < c.length * 2) {
                    outOffsetsH[i] *= dx
                    outOffsetsV[i] *= dy
                    i++
                }
            }

            else -> hasdata = false
        }

        if (hasdata) c.upload()
    }

    private fun computeKernel(blurRadius: Int, blurAmount: Float, outKernel: FloatArray) {
        val twoSigmaSquare = 2.0f * blurAmount * blurAmount
        val sigmaRoot = sqrt(twoSigmaSquare * Math.PI).toFloat()
        var total = 0.0F
        var distance: Float
        var index: Int

        for (i in -blurRadius..blurRadius) {
            distance = (i * i).toFloat()
            index = i + blurRadius
            outKernel[index] = exp(-distance / twoSigmaSquare) / sigmaRoot
            total += outKernel[index]
        }

        val size = (blurRadius * 2) + 1
        for (i in 0..<size) outKernel[i] /= total
    }

    private fun computeOffsets(blurRadius: Int, dx: Float, dy: Float, outOffsetH: FloatArray, outOffsetV: FloatArray) {
        val X = 0
        val Y = 1
        var i = -blurRadius
        var j = 0
        while (i <= blurRadius) {
            outOffsetH[j + X] = i * dx
            outOffsetH[j + Y] = 0f

            outOffsetV[j + X] = 0f
            outOffsetV[j + Y] = i * dy
            ++i
            j += 2
        }
    }

    override fun rebind() {
        computeBlurWeightings()
    }

    enum class Tap(val radius: Int) {
        Tap3x3(1), Tap5x5(2)
    }

    enum class BlurType(val tap: Tap) {
        Gaussian3x3(Tap.Tap3x3), Gaussian3x3b(Tap.Tap3x3),  // R=5 (11x11, policy "higher-then-discard")
        Gaussian5x5(Tap.Tap5x5), Gaussian5x5b(Tap.Tap5x5), // R=9 (19x19, policy "higher-then-discard")
    }
}
