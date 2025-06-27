package io.github.masamune

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.github.masamune.event.GameResizeEvent
import io.github.masamune.screen.LoadingScreen
import io.github.masamune.screen.TransitionType
import io.github.masamune.tiledmap.DefaultMapTransitionService
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.log.logger

typealias PhysicWorld = World

fun scheduledTask(delay: Float, action: () -> Unit): Timer.Task =
    Timer.schedule(object : Timer.Task() {
        override fun run() = action()
    }, delay)

fun scheduledTask(delay: Float, intervalSeconds: Float, action: (Timer.Task) -> Unit): Timer.Task =
    Timer.schedule(object : Timer.Task() {
        override fun run() = action(this)
    }, delay, intervalSeconds)

fun Input.isAnyKeyPressed(): Boolean {
    return isKeyJustPressed(Keys.ANY_KEY) ||
        isKeyJustPressed(Keys.W) || isKeyJustPressed(Keys.A) || isKeyJustPressed(Keys.S) || isKeyJustPressed(Keys.D) ||
        isKeyJustPressed(Keys.SPACE) || isKeyJustPressed(Keys.ESCAPE) || isKeyJustPressed(Keys.CONTROL_LEFT)
}

class Masamune(
    val webLauncher: Boolean = false,
    private val serviceLocator: ServiceLocator = LazyServiceLocator(
        // replace with ImmediateMapTransitionService to change maps without any special effect
        mapTransitionServiceInitializer = { tiled -> DefaultMapTransitionService(tiled) }
        // mapTransitionServiceInitializer = { tiled -> ImmediateMapTransitionService(tiled) }
    ),
) : KtxGame<KtxScreen>(), ServiceLocator by serviceLocator {

    val inputProcessor: InputMultiplexer by lazy { InputMultiplexer() }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        Gdx.input.inputProcessor = inputProcessor
        serviceLocator.event += serviceLocator.audio

        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun <Type : KtxScreen> setScreen(type: Class<Type>) {
        currentScreen.hide()
        currentScreen = getScreen(type)
        currentScreen.show()
        currentScreen.resize(Gdx.graphics.width, Gdx.graphics.height)
        event.fire(GameResizeEvent(Gdx.graphics.width, Gdx.graphics.height))
    }

    override fun resize(width: Int, height: Int) {
        // 1) resize shader service because of blur/tmp FrameBuffer that might be used by other screen logic.
        //    CombatScreen uses it to render GameScreen as blurred background.
        shader.resize(width, height)
        // 2) resize any screens that are currently in an active transition. The normal resize method of the game
        //    class only resizes the active screen.
        screenTransition.resize(width, height)
        // 3) resize active screen
        super.resize(width, height)
        // 4) resize any optional stuff
        event.fire(GameResizeEvent(width, height))
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)
        val deltaTime = Gdx.graphics.deltaTime.coerceAtMost(1 / 30f)

        if (screenTransition.hasActiveTransition) {
            screenTransition.render(deltaTime)
        } else {
            currentScreen.render(deltaTime)
        }
    }

    inline fun <reified T : KtxScreen> transitionScreen(
        fromType: TransitionType,
        toType: TransitionType,
        fromFirst: Boolean = true,
        crossinline onTransitionEnd: (T) -> Unit = {},
    ) {
        val toScreen = getScreen<T>()
        toScreen.resize(Gdx.graphics.width, Gdx.graphics.height)
        screenTransition.transition(shownScreen, fromType, toScreen, toType, fromFirst) {
            setScreen<T>()
            onTransitionEnd(getScreen<T>())
        }
    }

    override fun dispose() {
        log.info { "Maximum sprites in batch: ${(serviceLocator.batch as SpriteBatch).maxSpritesInBatch}" }

        super.dispose()
        serviceLocator.dispose()
    }

    fun hasScreenTransition(): Boolean = screenTransition.hasActiveTransition

    companion object {
        private val log = logger<Masamune>()

        /**
         * pixels per meter for physic world and scaling value for tiled maps and graphics.
         */
        const val UNIT_SCALE = 1 / 16f

        fun uiViewport(): Viewport = ExtendViewport(960f, 540f)
    }

}
