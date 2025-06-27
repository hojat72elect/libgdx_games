package io.github.masamune

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.StretchViewport
import io.github.masamune.screen.BlurTransitionType
import io.github.masamune.screen.ScreenTransitionService
import io.github.masamune.screen.TransitionType
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.toClasspathFile
import ktx.graphics.use
import ktx.log.Logger
import ktx.math.vec2

private val log = Logger("TransitionTest")

/**
 * Test for [ScreenTransitionService] and blur shader.
 * Press Space to transition between screens.
 *
 * The current screen gets blurred out.
 * The new screen gets blurred in.
 */

fun main() = gdxTest("Screen Transition Test", TransitionTest(), windowSize = vec2(300f, 300f))

private class Screen1(private val batch: Batch) : KtxScreen {
    private val viewport = StretchViewport(300f, 300f)
    private val texture by lazy { Texture("screen1.png".toClasspathFile()) }

    override fun show() {
        log.debug { "Screen 1 show" }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        log.debug { "Resize Screen 1" }
    }

    override fun render(delta: Float) {
        viewport.apply()
        batch.use(viewport.camera) {
            it.draw(texture, 0f, 0f)
        }
    }

    override fun dispose() {
        texture.dispose()
    }
}

private class Screen2(private val batch: Batch) : KtxScreen {
    private val viewport = StretchViewport(300f, 300f)
    private val texture by lazy { Texture("screen2.png".toClasspathFile()) }

    override fun show() {
        log.debug { "Screen 2 show" }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        log.debug { "Resize Screen 2" }
    }

    override fun render(delta: Float) {
        viewport.apply()
        batch.use(viewport.camera) {
            it.draw(texture, 0f, 0f)
        }
    }

    override fun dispose() {
        texture.dispose()
    }
}

private class TransitionTest : KtxGame<KtxScreen>() {
    private val serviceLocator by lazy { LazyServiceLocator() }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(Screen1(serviceLocator.batch))
        addScreen(Screen2(serviceLocator.batch))
        setScreen<Screen1>()

        serviceLocator.shader.loadAllShader()
        startTransition()
    }

    private fun startTransition() {
        val time = 2f
        val fromTransitionType = BlurTransitionType(0f, 6f, time, endAlpha = 0.9f)
        val toTransitionType = BlurTransitionType(6f, 0f, time, startAlpha = 0.9f)
        if (currentScreen is Screen1) {
            log.debug { "Transition Screen 2" }
            transitionScreen<Screen2>(fromTransitionType, toTransitionType)
        } else {
            log.debug { "Transition Screen 1" }
            transitionScreen<Screen1>(fromTransitionType, toTransitionType, fromFirst = false)
        }
    }

    override fun resize(width: Int, height: Int) {
        // 1) resize shader service because of blur/tmp FrameBuffer that might be used by other screen logic.
        serviceLocator.shader.resize(width, height)
        // 2) resize any screens that are currently in an active transition. The normal resize method of the game
        //    class only resizes the active screen.
        serviceLocator.screenTransition.resize(width, height)
        // 3) resize active screen
        super.resize(width, height)
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)
        val deltaTime = Gdx.graphics.deltaTime.coerceAtMost(1 / 30f)

        if (serviceLocator.screenTransition.hasActiveTransition) {
            serviceLocator.screenTransition.render(deltaTime)
        } else {
            super.render()
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !serviceLocator.screenTransition.hasActiveTransition) {
            startTransition()
        }
    }

    inline fun <reified T : KtxScreen> transitionScreen(
        fromType: TransitionType,
        toType: TransitionType,
        fromFirst: Boolean = true,
    ) {
        val toScreen = getScreen<T>()
        toScreen.resize(Gdx.graphics.width, Gdx.graphics.height)
        serviceLocator.screenTransition.transition(shownScreen, fromType, toScreen, toType, fromFirst) {
            setScreen<T>()
        }
    }

    override fun dispose() {
        super.dispose()
        serviceLocator.dispose()
    }
}
