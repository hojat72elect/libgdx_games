package com.bitfire.postprocessing.filters

import com.bitfire.postprocessing.utils.PingPongBuffer

/**
 * Implements a separable 2D convolution filter.
 * This filter is often used for effects like blurring, sharpening, or edge detection.
 *
 * In here we have a two-pass rendering process; first a horizontal convolution is applied to the input image,
 * then the result of the horizontal pass will be passed to the vertical convolution.
 */
class Convolve2D(radius: Int) : MultipassFilter() {

    @JvmField
    val length = (radius * 2) + 1
    private val horizontalPass = Convolve1D(length)
    private val verticalPass = Convolve1D(length, horizontalPass.weights)

    @JvmField
    val weights = horizontalPass.weights!!

    @JvmField
    val offsetsHor = horizontalPass.offsets!!

    @JvmField
    val offsetsVert = verticalPass.offsets!!

    fun dispose() {
        horizontalPass.dispose()
        verticalPass.dispose()
    }

    fun upload() {
        rebind()
    }

    override fun rebind() {
        horizontalPass.rebind()
        verticalPass.rebind()
    }

    override fun render(srcdest: PingPongBuffer) {
        horizontalPass.setInput(srcdest.capture()).render()
        verticalPass.setInput(srcdest.capture()).render()
    }
}
