package com.bitfire.uracer.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.bitfire.uracer.configuration.GraphicsUtils

object ScaleUtils {

    @JvmField
    var PlayWidth = 0

    @JvmField
    var PlayHeight = 0

    @JvmField
    var CropX = 0

    @JvmField
    var CropY = 0

    @JvmField
    var PlayViewport: Rectangle? = null

    @JvmField
    var Scale = 0F

    @JvmField
    var RefAspect = 0F

    @JvmStatic
    fun initialize(displayWidth: Int, displayHeight: Int) {
        val refW = GraphicsUtils.ReferenceScreenWidth.toFloat()
        val refH = GraphicsUtils.ReferenceScreenHeight.toFloat()

        // Maintain the aspect ratio by letterboxing.
        RefAspect = refW / refH
        val physicalWidth = displayWidth.toFloat()
        val physicalHeight = displayHeight.toFloat()
        val aspect = physicalWidth / physicalHeight
        CropX = 0
        CropY = 0

        if (aspect > RefAspect) {
            // Letterbox left and right
            Scale = physicalHeight / refH
            CropX = ((physicalWidth - refW * Scale) / 2F).toInt()
        } else if (aspect < RefAspect) {

            // Letterbox above and below
            Scale = physicalWidth / refW
            CropY = ((physicalHeight - refH * Scale) / 2f).toInt()
        } else {
            Scale = physicalWidth / refW
        }

        PlayWidth = (refW * Scale).toInt()
        PlayHeight = (refH * Scale).toInt()
        PlayViewport = Rectangle(CropX.toFloat(), CropY.toFloat(), PlayWidth.toFloat(), PlayHeight.toFloat())

        Gdx.app.log("ScaleUtils", "Scale=$Scale")
        Gdx.app.log("ScaleUtils", "Play=${PlayWidth}x${PlayHeight}")
        Gdx.app.log("ScaleUtils", "Crop=${CropX}x${CropY}")
    }
}
