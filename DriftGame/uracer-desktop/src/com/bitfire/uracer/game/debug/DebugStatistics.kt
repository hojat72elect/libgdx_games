package com.bitfire.uracer.game.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.WindowedMean
import com.badlogic.gdx.utils.TimeUtils
import com.bitfire.uracer.URacer
import com.bitfire.uracer.configuration.PhysicsUtils
import com.bitfire.uracer.utils.AlgebraMath.clamp

class DebugStatistics(val width: Int, val height: Int, updateHz: Float) {

    private val pixels: Pixmap
    private val texture: Texture
    val region: TextureRegion
    private val ratioRtime: Float
    private val ratioPtime: Float
    private val fpsRatio: Float
    private val intervalNs: Long
    private val dataRenderTime: FloatArray
    private val dataFps: FloatArray
    private val dataPhysicsTime: FloatArray
    private val dataTimeAliasing: FloatArray

    @JvmField
    var meanPhysics = WindowedMean(16)

    @JvmField
    var meanRender = WindowedMean(16)

    @JvmField
    var meanTickCount = WindowedMean(16)
    private var startTime: Long

    init {
        val oneOnUpdHz = 1F / updateHz

        intervalNs = (1_000_000_000L * oneOnUpdHz).toLong()
        pixels = Pixmap(width, height, Pixmap.Format.RGBA8888)
        texture = Texture(width, height, Pixmap.Format.RGBA8888)
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest)
        region = TextureRegion(texture, 0, 0, pixels.width, pixels.height)
        dataRenderTime = FloatArray(width)
        dataFps = FloatArray(width)
        dataPhysicsTime = FloatArray(width)
        dataTimeAliasing = FloatArray(width)
        ratioRtime = (height / 2F) * PhysicsUtils.TimestepHz
        ratioPtime = (height / 2F) * PhysicsUtils.TimestepHz
        fpsRatio = (height / 2F) * oneOnUpdHz

        for (i in 0..<width) {
            dataRenderTime[i] = 0F
            dataPhysicsTime[i] = 0F
            dataFps[i] = 0F
            dataTimeAliasing[i] = 0F
        }

        plot()
        startTime = TimeUtils.nanoTime()
    }

    fun dispose() {
        pixels.dispose()
        texture.dispose()
    }

    fun update() {
        if (collect()) {
            plot()
        }
    }

    private fun plot() {
        pixels.setColor(0f, 0f, 0f, 0.8f)
        pixels.fill()

        val alpha = 0.5f
        for (x in 0..<this.width) {
            val xc = this.width - x - 1

            var value = (dataRenderTime[x] * ratioRtime).toInt()
            if (value > 0) {
                pixels.setColor(0f, 0.5f, 1f, alpha)
                pixels.drawLine(xc, 0, xc, value)
            }

            value = (dataPhysicsTime[x] * ratioPtime).toInt()
            pixels.setColor(1f, 1f, 1f, alpha)
            pixels.drawLine(xc, 0, xc, value)

            value = (dataFps[x] * fpsRatio).toInt()
            if (value > 0) {
                pixels.setColor(0f, 1f, 1f, .8f)
                pixels.drawPixel(xc, value)
            }

            value = (clamp(dataTimeAliasing[x] * this.height, 0f, height.toFloat())).toInt()
            if (value > 0) {
                pixels.setColor(1f, 0f, 1f, .8f)
                pixels.drawPixel(xc, value)
            }
        }

        texture.draw(pixels, 0, 0)
    }

    private fun collect(): Boolean {
        val time = TimeUtils.nanoTime()

        if (time - startTime > intervalNs) {
            for (i in this.width - 1 downTo 1) {
                dataRenderTime[i] = dataRenderTime[i - 1]
                dataPhysicsTime[i] = dataPhysicsTime[i - 1]
                dataFps[i] = dataFps[i - 1]
                dataTimeAliasing[i] = dataTimeAliasing[i - 1]
            }

            meanPhysics.addValue(URacer.Game.getPhysicsTime())
            meanRender.addValue(URacer.Game.getRenderTime())
            meanTickCount.addValue(URacer.Game.getLastTicksCount().toFloat())

            dataPhysicsTime[0] = meanPhysics.getMean()
            dataRenderTime[0] = meanRender.getMean()
            dataFps[0] = Gdx.graphics.framesPerSecond.toFloat()
            dataTimeAliasing[0] = URacer.Game.getTemporalAliasing()

            startTime = time
            return true
        }

        return false
    }
}
