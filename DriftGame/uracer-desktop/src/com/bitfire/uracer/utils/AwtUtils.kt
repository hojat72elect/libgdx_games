package com.bitfire.uracer.utils

import java.awt.DisplayMode
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment

fun getNativeDisplayMode(): DisplayMode? {
    val env = GraphicsEnvironment.getLocalGraphicsEnvironment()
    val primary: GraphicsDevice? = env.defaultScreenDevice
    if (primary != null) {
        return primary.getDisplayMode()
    }

    return null
}

fun getCenteredXOnDisplay(width: Int): Int {
    val mode = getNativeDisplayMode()
    return (mode!!.width - width) / 2
}

fun getCenteredYOnDisplay(height: Int): Int {
    val mode: DisplayMode? = getNativeDisplayMode()
    return (mode!!.height - height) / 2
}
