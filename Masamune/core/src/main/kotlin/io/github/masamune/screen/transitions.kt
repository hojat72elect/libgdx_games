package io.github.masamune.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.HdpiUtils
import com.badlogic.gdx.math.Interpolation
import io.github.masamune.asset.ShaderService
import io.github.masamune.asset.ShaderService.Companion.renderToFbo
import ktx.app.KtxScreen
import ktx.graphics.component1
import ktx.graphics.component2
import ktx.graphics.component3
import ktx.graphics.component4
import ktx.graphics.use

sealed class Transition(val screen: Screen) {
    abstract fun render(delta: Float)

    abstract fun isDone(): Boolean

    fun isNotDone(): Boolean = !isDone()
}

data object DefaultTransition : Transition(object : KtxScreen {}) {
    override fun isDone(): Boolean = true

    override fun render(delta: Float) = Unit
}

class BlurTransition(
    type: BlurTransitionType,
    screen: Screen,
    private val batch: Batch,
    private val shaderService: ShaderService,
) : Transition(screen) {

    private val startBlur: Float = type.startBlur
    private val endBlur: Float = type.endBlur
    private val startAlpha: Float = type.startAlpha
    private val endAlpha: Float = type.endAlpha
    private val interpolation: Interpolation = type.interpolation
    private val speed = 1f / type.time
    private var currentBlur = startBlur
    private var currentAlpha = startAlpha
    private var alpha: Float = 0f

    override fun isDone(): Boolean = alpha >= 1f

    override fun render(delta: Float) {
        alpha = (alpha + delta * speed).coerceAtMost(1f)
        currentBlur = interpolation.apply(startBlur, endBlur, alpha)
        currentAlpha = interpolation.apply(startAlpha, endAlpha, alpha)

        shaderService.useBlurShader(batch, radius = currentBlur, shaderService.tmpFbo) {
            screen.render(0f)
        }

        val (r, g, b, a) = batch.color
        batch.setColor(r, g, b, currentAlpha)
        HdpiUtils.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        batch.use(batch.projectionMatrix.idt()) {
            it.draw(shaderService.tmpFbo.colorBufferTexture, -1f, 1f, 2f, -2f)
        }
        batch.setColor(r, g, b, a)
    }
}

class FadeTransition(
    type: FadeTransitionType,
    screen: Screen,
    private val batch: Batch,
    private val shaderService: ShaderService,
) : Transition(screen) {

    private val startAlpha: Float = type.startAlpha
    private val endAlpha: Float = type.endAlpha
    private val interpolation: Interpolation = type.interpolation
    private val speed = 1f / type.time
    private var currentAlpha = startAlpha
    private var alpha: Float = 0f
    private var delayInSeconds = type.delayInSeconds

    override fun isDone(): Boolean = alpha >= 1f

    override fun render(delta: Float) {
        if (delayInSeconds > 0f) {
            delayInSeconds -= delta
            screen.render(0f)
            return
        }

        alpha = (alpha + delta * speed).coerceAtMost(1f)
        currentAlpha = interpolation.apply(startAlpha, endAlpha, alpha)
        shaderService.tmpFbo.renderToFbo { screen.render(0f) }

        val (r, g, b, a) = batch.color
        batch.setColor(r, g, b, currentAlpha)
        HdpiUtils.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        batch.use(batch.projectionMatrix.idt()) {
            it.draw(shaderService.tmpFbo.colorBufferTexture, -1f, 1f, 2f, -2f)
        }
        batch.setColor(r, g, b, a)
    }
}
