package com.bitfire.uracer.game.logic.gametasks.hud.elements.player

import aurelienribon.tweenengine.Timeline
import aurelienribon.tweenengine.Tween
import aurelienribon.tweenengine.equations.Linear.Companion.INOUT
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.bitfire.uracer.configuration.GraphicsUtils
import com.bitfire.uracer.game.logic.gametasks.hud.Positionable
import com.bitfire.uracer.game.tween.GameTweener.start
import com.bitfire.uracer.game.tween.GameTweener.stop
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.utils.AlgebraMath.isZero
import com.bitfire.uracer.utils.BoxedFloat
import com.bitfire.uracer.utils.BoxedFloatAccessor

class WrongWay : Positionable() {

    private val w: Float
    private val h: Float
    private val offX: Float
    private val offY: Float
    private val sign = Sprite(Art.wrongWay)
    private val bfAlpha: BoxedFloat
    private var isShown: Boolean

    init {
        val scale = 0.4F
        w = Art.wrongWay.width * scale
        h = Art.wrongWay.height * scale
        offX = w / 2
        offY = h / 2

        sign.setSize(w, h)
        sign.setOrigin(offX, offY)
        sign.flip(false, true)
        bfAlpha = BoxedFloat(0F)
        isShown = false
    }

    override val width = w
    override val height = h

    @JvmOverloads
    fun fadeIn(millisecs: Int = GraphicsUtils.DefaultFadeMilliseconds) {
        if (!isShown) {
            isShown = true
            stop(bfAlpha)
            val seq = Timeline.createSequence()
            seq.push(Tween.to(bfAlpha, BoxedFloatAccessor.VALUE, millisecs.toFloat()).target(1f).ease(INOUT))
            start(seq)
        }
    }

    fun fadeOut(millisecs: Int) {
        if (isShown) {
            isShown = false
            stop(bfAlpha)
            val seq = Timeline.createSequence()
            seq.push(Tween.to(bfAlpha, BoxedFloatAccessor.VALUE, millisecs.toFloat()).target(0F).ease(INOUT))
            start(seq)
        }
    }

    fun render(batch: SpriteBatch) {
        if (!isZero(bfAlpha.value)) {
            val px = position.x - offX
            val py = position.y - offY
            sign.setPosition(px, py)
            sign.draw(batch, bfAlpha.value)
        }
    }
}
