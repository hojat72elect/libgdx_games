package com.bitfire.uracer.game.debug

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Array
import com.bitfire.uracer.game.debug.DebugHelper.RenderFlags
import com.bitfire.uracer.game.debug.player.DebugMeter
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.PlayerTensiveMusic
import com.bitfire.uracer.game.player.PlayerCar
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.utils.SpriteBatchUtils.drawString
import com.bitfire.uracer.utils.paletteRYG

class DebugMusicVolumes(flag: RenderFlags, private val tensiveMusic: PlayerTensiveMusic?) : DebugRenderable(flag) {

    private val meters = Array<DebugMeter>()
    private val idt = Matrix4()
    private var totalVolume = 0f

    init {
        for (i in 0..<PlayerTensiveMusic.NumTracks) {
            val m = DebugMeter(64, Art.DebugFontHeight)
            m.setLimits(0f, 1f)
            m.setShowLabel(false)
            meters.add(m)
        }
    }

    override fun dispose() {
        for (m in meters) m.dispose()
    }

    override fun player(player: PlayerCar?) {
        super.player(player)
        if (hasPlayer) this.player = player
    }

    private val isActive: Boolean
        get() = hasPlayer && tensiveMusic != null

    override fun tick() {
        if (this.isActive) {
            totalVolume = 0f
            val volumes = tensiveMusic!!.volumes
            for (i in tensiveMusic.volumes.indices) {
                val v = volumes!![i]
                meters.get(i).value = v
                totalVolume += v
            }
        }
    }

    override fun renderBatch(batch: SpriteBatch) {
        if (this.isActive && meters.size > 0) {
            val prev = batch.transformMatrix
            batch.transformMatrix = idt
            batch.enableBlending()

            var prevHeight = 0f
            var index = 0
            val drawx = 275
            val drawy = 0

            val maxMusicIndex = tensiveMusic!!.currentMusicIndexLimit
            drawString(batch, "music tracks max=$maxMusicIndex", drawx.toFloat(), drawy.toFloat())
            drawString(batch, "==================", drawx.toFloat(), (drawy + Art.DebugFontHeight).toFloat())

            var text: String?
            for (m in meters) {
                var y = drawy + Art.DebugFontHeight * 2

                // offset by index
                y += (index * (prevHeight + 1)).toInt()

                // compute color
                val alpha = if (index > maxMusicIndex) 0.5f else 1f
                val c = paletteRYG(1.5f - m.value * 1.5f, alpha)

                // render track number
                text = "T" + (index + 1)
                batch.setColor(1f, 1f, 1f, alpha)
                drawString(batch, text, drawx.toFloat(), y.toFloat())
                batch.setColor(1f, 1f, 1f, 1f)

                // render meter after text
                val meter_x = drawx + (text.length * Art.DebugFontWidth) + 2
                m.color.set(c)
                m.setPosition(meter_x.toFloat(), y.toFloat())
                m.render(batch)

                // render volume numerical value
                text = String.format("%.02f", m.value)
                batch.setColor(1f, 1f, 1f, alpha)
                drawString(batch, text, (meter_x + m.width + 2).toFloat(), y.toFloat())
                batch.setColor(1f, 1f, 1f, 1f)

                index++
                prevHeight = m.height.toFloat()
            }

            drawString(
                batch, "total volume = " + String.format("%.02f", totalVolume), drawx.toFloat(), (Art.DebugFontHeight
                        * (meters.size + 3)).toFloat()
            )

            batch.transformMatrix = prev
            batch.disableBlending()
        }
    }
}
