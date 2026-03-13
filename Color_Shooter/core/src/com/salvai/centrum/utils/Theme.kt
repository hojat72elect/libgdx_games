package com.salvai.centrum.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * Defines the color palette of the GAME
 */
val GameTheme = GdxArray<Color>().apply {
    add(Color(1f, 1f, 1f, 1f))  // white
    add(Color(52 / 255f, 152 / 255f, 219 / 255f, 1f))  // blue
    add(Color(241 / 255f, 196 / 255f, 15 / 255f, 1f))  // yellow
    add(Color(46 / 255f, 204 / 255f, 113 / 255f, 1f))  // green
    add(Color(231 / 255f, 76 / 255f, 60 / 255f, 1f)) // red
}

