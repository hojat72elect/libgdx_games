package com.bitfire.uracer.utils

import com.badlogic.gdx.utils.TimeUtils

class Timer {

    private var nsStartTime = 0L
    private var stopped = false
    private var nsStopTime = 0L
    private var elapsedSecs = 0F

    init {
        reset()
    }

    fun pause() {
        stopped = true
        nsStopTime = TimeUtils.nanoTime()
    }

    fun resume() {
        stopped = false
    }

    fun reset() {
        stopped = false
        nsStartTime = TimeUtils.nanoTime()
        nsStopTime = 0
    }

    fun update() {
        val now = (if (stopped) nsStopTime else TimeUtils.nanoTime())
        elapsedSecs = (now - nsStartTime).toFloat() * oneOnOneBillion
    }

    /**
     * Returns the elapsed time in seconds.
     */
    fun elapsed() = elapsedSecs

    companion object {
        private const val oneOnOneBillion = 1.0F / 1000000000.0F
    }
}
