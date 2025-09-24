package com.bitfire.uracer.game.debug.player

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.utils.AlgebraMath.clamp
import com.bitfire.uracer.utils.SpriteBatchUtils.drawString
import kotlin.math.abs

class DebugMeter(val width: Int, val height: Int) : Disposable {

    @JvmField
    var color = Color(1f, 1f, 1f, 1f)
    private val pixels = Pixmap(this.width, this.height, Pixmap.Format.RGBA8888)
    private val texture = Texture(width, height, Pixmap.Format.RGBA8888)
    private val region = TextureRegion(texture, 0, 0, pixels.width, pixels.height)
    var value = 0F
    private var minValue = 0F
    private var maxValue = 0F
    private var name = ""
    private val pos = Vector2()
    private var showLabel = true

    init {
        assert(width < 256 && height < 256)
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest)
    }

    override fun dispose() {
        texture.dispose()
        pixels.dispose()
    }

    fun setName(name: String) {
        this.name = "$name = "
    }

    fun setLimits(min: Float, max: Float) {
        minValue = min
        maxValue = max
    }

    fun setShowLabel(show: Boolean) {
        this.showLabel = show
    }

    val message: String
        get() = name + String.format("%.04f", abs(value))

    fun setPosition(position: Vector2) {
        pos.set(position)
    }

    fun setPosition(x: Float, y: Float) {
        pos.set(x, y)
    }

    fun render(batch: SpriteBatch) {
        drawMeter()
        if (showLabel) drawString(batch, message, pos.x, pos.y)
        batch.draw(region, pos.x, pos.y + (if (showLabel) Art.DebugFontHeight else 0))
    }

    private fun drawMeter() {
        pixels.setColor(0.25F, 0.25F, 0.25F, color.a)
        pixels.fill()

        val range = maxValue - minValue
        var ratio = abs(value) / range
        ratio = clamp(ratio, 0F, 1F)

        pixels.setColor(color)
        pixels.fillRectangle(1, 1, (width * ratio.toInt()) - 2, height - 2)
        texture.draw(pixels, 0, 0)
    }
}
