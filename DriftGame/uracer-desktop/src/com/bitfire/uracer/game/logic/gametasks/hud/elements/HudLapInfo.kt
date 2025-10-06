package com.bitfire.uracer.game.logic.gametasks.hud.elements

import aurelienribon.tweenengine.Timeline
import aurelienribon.tweenengine.Tween
import aurelienribon.tweenengine.equations.Linear.Companion.INOUT
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.bitfire.uracer.configuration.GraphicsUtils
import com.bitfire.uracer.game.logic.gametasks.hud.HudElement
import com.bitfire.uracer.game.logic.gametasks.hud.HudLabel
import com.bitfire.uracer.game.logic.replaying.LapManager
import com.bitfire.uracer.game.tween.GameTweener.start
import com.bitfire.uracer.game.tween.GameTweener.stop
import com.bitfire.uracer.resources.BitmapFontFactory
import com.bitfire.uracer.utils.BoxedFloat
import com.bitfire.uracer.utils.BoxedFloatAccessor
import com.bitfire.uracer.utils.ReplayUtils.ticksToSeconds

class HudLapInfo(private val lapManager: LapManager) : HudElement() {

    private val curr: HudLabel = HudLabel(BitmapFontFactory.FontFace.LcdWhite, "99.99", true)
    private val r = BoxedFloat(1f)
    private val g = BoxedFloat(1f)
    private val b = BoxedFloat(1f)
    var isValid: Boolean = true

    init {
        curr.scale = 1.5F
        curr.setPosition((GraphicsUtils.REFERENCE_SCREEN_WIDTH / 2F), GraphicsUtils.REFERENCE_SCREEN_HEIGHT - curr.height / 2 - 10)
    }

    override fun dispose() {}

    fun toDefaultColor() {
        toColor(1F, 1F, 1F)
    }

    fun toColor(red: Float, green: Float, blue: Float) {
        toColor(500, red, green, blue)
    }

    fun toColor(millisecs: Int, red: Float, green: Float, blue: Float) {
        val seq = Timeline.createParallel()
        stop(r)
        stop(g)
        stop(b)

        seq
            .push(Tween.to(r, BoxedFloatAccessor.VALUE, millisecs.toFloat()).target(red).ease(INOUT))
            .push(Tween.to(g, BoxedFloatAccessor.VALUE, millisecs.toFloat()).target(green).ease(INOUT))
            .push(Tween.to(b, BoxedFloatAccessor.VALUE, millisecs.toFloat()).target(blue).ease(INOUT))

        start(seq)
    }

    fun setInvalid(message: String) {
        isValid = false
        curr.setString(message, true)
    }

    override fun onRender(batch: SpriteBatch, cameraZoom: Float) {
        // current lap time
        if (isValid) {
            val elapsed = String.format("%.03f", ticksToSeconds(lapManager.currentReplayTicks))
            curr.setString(elapsed, true)
        }

        curr.setColor(r.value, g.value, b.value)
        curr.render(batch)
    }

    override fun onRestart() {
        toDefaultColor()
        isValid = true
    }
}
