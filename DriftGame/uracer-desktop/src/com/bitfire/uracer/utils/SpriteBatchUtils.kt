package com.bitfire.uracer.utils

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import java.util.Locale

object SpriteBatchUtils {

    private val chars = arrayOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", ".,!?:;\"'+-=/\\< ")
    private var debugFont: Array<Array<TextureRegion>>? = null
    private var debugFontW = 0

    @JvmStatic
    fun init(debugFont: Array<Array<TextureRegion>>, debugFontWidth: Int) {
        this.debugFont = debugFont
        this.debugFontW = debugFontWidth
    }

    fun draw(batch: SpriteBatch, region: TextureRegion, x: Float, y: Float) {
        var width = region.regionWidth
        if (width < 0) {
            width = -width
        }

        batch.draw(region, x, y, width.toFloat(), region.regionHeight.toFloat())
    }

    fun draw(batch: SpriteBatch, region: TextureRegion, x: Float, y: Float, width: Float, height: Float) {
        batch.draw(region, x, y, width, height)
    }

    @JvmStatic
    fun drawString(batch: SpriteBatch, string: String, x: Float, y: Float) {
        val upstring = string.uppercase(Locale.getDefault())
        for (i in 0..<string.length) {
            val ch = upstring[i]
            for (ys in chars.indices) {
                val xs = chars[ys].indexOf(ch)
                if (xs >= 0) {
                    draw(batch, debugFont!![xs][ys], x + i * debugFontW, y)
                }
            }
        }
    }

    @JvmStatic
    fun drawString(batch: SpriteBatch, string: String, x: Float, y: Float, w: Float, h: Float) {
        val upstring = string.uppercase(Locale.getDefault())
        for (i in 0..<string.length) {
            val ch = upstring[i]
            for (ys in chars.indices) {
                val xs = chars[ys].indexOf(ch)
                if (xs >= 0) {
                    draw(batch, debugFont!![xs][ys], x + i * w, y, w, h)
                }
            }
        }
    }
}
