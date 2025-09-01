package com.bitfire.postprocessing.filters

import com.bitfire.postprocessing.utils.PingPongBuffer

/**
 * The base class for any multi-pass filter. Usually a multi-pass filter will make use of one or more single-pass filters,
 * promoting composition over inheritance.
 */
abstract class MultipassFilter {
    abstract fun rebind()

    abstract fun render(srcdest: PingPongBuffer)
}
