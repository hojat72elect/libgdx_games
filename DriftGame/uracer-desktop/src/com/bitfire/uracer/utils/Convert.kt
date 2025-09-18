package com.bitfire.uracer.utils

import com.badlogic.gdx.math.Vector2

object ConvertUtils {
    private var invPixelsPerMeter = 0F
    private var pixelsPerMeter = 0F

    private val retMt = Vector2()
    private val retPx = Vector2()

    fun initialize(pixelsPerMeter: Float) {
        ConvertUtils.pixelsPerMeter = pixelsPerMeter
        invPixelsPerMeter = 1F / pixelsPerMeter
    }

    fun mt2px(v: Float) = v * pixelsPerMeter

    fun mt2px(v: Vector2): Vector2 {
        retPx.set(v.x * pixelsPerMeter, v.y * pixelsPerMeter)
        return retPx
    }

    fun px2mt(v: Float) = v * invPixelsPerMeter

    fun px2mt(v: Vector2): Vector2 {
        retMt.set(v.x * invPixelsPerMeter, v.y * invPixelsPerMeter)
        return retMt
    }
}
