package com.bitfire.uracer.game.debug

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Array
import com.bitfire.uracer.game.debug.DebugHelper.RenderFlags
import com.bitfire.uracer.game.debug.player.DebugMeter
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.engines.EngineSoundSet
import com.bitfire.uracer.game.player.PlayerCar
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.utils.SpriteBatchUtils.drawString
import com.bitfire.uracer.utils.paletteRYG

class DebugCarEngineVolumes(flag: RenderFlags, private val soundset: EngineSoundSet?) : DebugRenderable(flag) {

    private val meters = Array<DebugMeter>()
    private val idt = Matrix4()
    private val sampleNames = arrayOf("idle    ", "on-low  ", "on-mid  ", "on-high ", "off-low ", "off-mid ", "off-high")

    init {
        for (i in 0..<EngineSoundSet.NumSamples) {
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
        get() = hasPlayer && soundset != null

    override fun tick() {
        if (this.isActive) {
            val volumes = soundset!!.getVolumes()
            for (i in soundset.getVolumes().indices) {
                val v = volumes[i]
                meters.get(i).value = v
            }
        }
    }

    override fun renderBatch(batch: SpriteBatch) {
        if (this.isActive && meters.size > 0) {
            val prev = batch.transformMatrix
            batch.transformMatrix = idt
            batch.enableBlending()

            var prevHeight = 0F
            var index = 0
            val drawx = 410
            val drawy = 0

            drawString(batch, "car engine soundset", drawx.toFloat(), drawy.toFloat())
            drawString(batch, "=======================", drawx.toFloat(), (drawy + Art.DebugFontHeight).toFloat())

            var text: String
            for (m in meters) {
                var y = drawy + Art.DebugFontHeight * 2

                // offset by index
                y += (index * (prevHeight + 1)).toInt()

                // compute color
                val alpha = 1f
                val c = paletteRYG(1.5f - m.value * 1.5f, alpha)


                // render track number
                text = sampleNames[index]
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

            batch.transformMatrix = prev
            batch.disableBlending()
        }
    }
}
