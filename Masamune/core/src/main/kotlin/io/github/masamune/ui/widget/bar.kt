package io.github.masamune.ui.widget

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import ktx.graphics.component1
import ktx.graphics.component2
import ktx.graphics.component3
import ktx.graphics.component4
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * A special **horizontal** [ProgressBar] implementation used as combat health bars.
 * The bar itself is animated.
 *
 * When moving to the left it shows a tinted version of the bar
 * that slowly moves towards the final value. The left tint color can be specified via [negativeTintColor].
 *
 * When moving to the right it shows a tinted version of the bar
 * that moves faster towards the final value than the normal bar version.
 * The right tint color can be specified via [positiveTintColor].
 */
class Bar(
    initialValue: Float,
    min: Float,
    max: Float,
    stepSize: Float,
    barColor: Color,
    skin: Skin,
) : ProgressBar(min, max, stepSize, false, skin, "white") {

    private var tintInterpolation: Interpolation = SLOW_FAST_INTERPOLATION
    private var tintDuration = SLOW_DURATION
    private var tintFromValue = 0f
    private var tintTime = 0f
    var negativeTintColor = Color(0.7f, 0f, 0f, 0.6f)
    var positiveTintColor = Color(0f, 0.7f, 0f, 0.6f)
    private var tintColor = negativeTintColor

    val tintPercent: Float
        get() {
            if (minValue == maxValue) {
                return 0f
            }
            return Interpolation.linear.apply((tintValue - minValue) / (maxValue - minValue))
        }


    val tintValue: Float
        get() {
            if (tintTime > 0f) {
                return tintInterpolation.apply(tintFromValue, value, 1f - tintTime / tintDuration)
            }
            return value
        }

    init {
        setAnimateInterpolation(FAST_SLOW_INTERPOLATION)
        setAnimateDuration(FAST_DURATION)
        color = barColor

        setValue(initialValue)
        updateVisualValue()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        val color = this.getColor()
        val x = this.getX()
        val y = this.getY()
        var width = this.getWidth()
        val height = this.getHeight()
        val knobWidth = 0f
        var bgLeftWidth = 0f
        var bgRightWidth = 0f

        // render background
        this.backgroundDrawable?.let { bg ->
            batch.setColor(1f, 1f, 1f, color.a * parentAlpha)
            this.drawRound(
                batch, bg,
                x, (y + (height - bg.minHeight) * 0.5f).roundToInt().toFloat(),
                width, bg.minHeight.roundToInt().toFloat()
            )
            bgLeftWidth = bg.leftWidth
            bgRightWidth = bg.rightWidth
            width -= bgLeftWidth + bgRightWidth
        }

        val total = width - knobWidth
        val actualBeforeWidth = MathUtils.clamp(total * visualPercent, 0.0f, total)
        val tintBeforeWidth = MathUtils.clamp(total * tintPercent, 0.0f, total)
        val knobWidthHalf = knobWidth * 0.5f

        // render "knobBefore" two times. Once normally and once in a tinted version
        this.knobBeforeDrawable?.let { knobBefore ->
            // first the tinted version as background
            // it either slowly moves to the left AFTER the normal bar,
            // or it moves fast to the right BEFORE the normal bar
            val (tintR, tintG, tintB, tintA) = tintColor
            batch.setColor(tintR, tintG, tintB, parentAlpha * tintA)
            this.drawRound(
                batch, knobBefore,
                x + bgLeftWidth, y + (height - knobBefore.minHeight) * 0.5f,
                tintBeforeWidth + knobWidthHalf, knobBefore.minHeight
            )

            // then the normal version
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha)
            this.drawRound(
                batch, knobBefore,
                x + bgLeftWidth, y + (height - knobBefore.minHeight) * 0.5f,
                actualBeforeWidth + knobWidthHalf, knobBefore.minHeight
            )
        }
    }

    private fun drawRound(batch: Batch, drawable: Drawable, x: Float, y: Float, w: Float, h: Float) {
        drawable.draw(batch, floor(x), floor(y), ceil(w), ceil(h))
    }

    override fun setValue(value: Float): Boolean {
        if (this.value > value) {
            // new value is smaller than current value
            // tinting interpolation is slow and normal interpolation is fast
            tintDuration = SLOW_DURATION
            tintInterpolation = SLOW_FAST_INTERPOLATION
            tintColor = negativeTintColor
            setAnimateDuration(FAST_DURATION)
            setAnimateInterpolation(FAST_SLOW_INTERPOLATION)
        } else {
            // new value is larger than current value
            // -> tinting interpolation is fast and normal interpolation is slow
            tintDuration = FAST_DURATION
            tintInterpolation = FAST_SLOW_INTERPOLATION
            tintColor = positiveTintColor
            setAnimateDuration(SLOW_DURATION)
            setAnimateInterpolation(SLOW_FAST_INTERPOLATION)
        }

        if (tintDuration > 0) {
            tintFromValue = tintValue
            tintTime = tintDuration
        }

        return super.setValue(value)
    }

    override fun act(delta: Float) {
        if (tintTime > 0) {
            tintTime -= delta
        }
        super.act(delta)
    }

    override fun updateVisualValue() {
        super.updateVisualValue()
        tintTime = 0f
    }

    companion object {
        private val SLOW_FAST_INTERPOLATION = Interpolation.pow5In
        private const val SLOW_DURATION = 2f
        private val FAST_SLOW_INTERPOLATION = Interpolation.fastSlow
        private const val FAST_DURATION = 1f
    }
}

@Scene2dDsl
fun <S> KWidget<S>.bar(
    skin: Skin,
    initialValue: Float,
    min: Float,
    max: Float,
    stepSize: Float,
    barColor: Color,
    init: (@Scene2dDsl Bar).(S) -> Unit = {},
): Bar = actor(Bar(initialValue, min, max, stepSize, barColor, skin), init)
