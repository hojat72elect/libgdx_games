package io.github.masamune.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.Batch
import io.github.masamune.asset.ShaderService
import ktx.log.logger

class ScreenTransitionService(
    val batch: Batch,
    val shaderService: ShaderService,
) {
    private var fromTransition: Transition = DefaultTransition
    private var toTransition: Transition = DefaultTransition
    private var transitionEnd: () -> Unit = {}

    val hasActiveTransition: Boolean
        get() {
            val fromDone = fromTransition.isDone()
            val toDone = toTransition.isDone()
            return !fromDone || !toDone
        }

    private fun transition(screen: Screen, type: TransitionType): Transition {
        return when (type) {
            is DefaultTransitionType -> DefaultTransition
            is BlurTransitionType -> BlurTransition(type, screen, batch, shaderService)
            is FadeTransitionType -> FadeTransition(type, screen, batch, shaderService)
        }
    }

    fun transition(
        fromScreen: Screen,
        fromType: TransitionType,
        toScreen: Screen,
        toType: TransitionType,
        fromFirst: Boolean = true,
        onTransitionEnd: () -> Unit
    ) {
        if (fromFirst) {
            fromTransition = transition(fromScreen, fromType)
            toTransition = transition(toScreen, toType)
        } else {
            fromTransition = transition(toScreen, toType)
            toTransition = transition(fromScreen, fromType)
        }
        transitionEnd = onTransitionEnd
    }

    fun render(delta: Float) {
        if (fromTransition.isNotDone()) {
            fromTransition.render(delta)
        }
        if (toTransition.isNotDone()) {
            toTransition.render(delta)
        }

        if (fromTransition.isDone() && toTransition.isDone()) {
            // transition finished -> set new screen
            log.debug { "Screen transition finished" }
            fromTransition = DefaultTransition
            toTransition = DefaultTransition
            transitionEnd()
        }
    }

    fun resize(width: Int, height: Int) {
        if (hasActiveTransition) {
            fromTransition.screen.resize(width, height)
            toTransition.screen.resize(width, height)
        }
    }

    companion object {
        private val log = logger<ScreenTransitionService>()
    }
}
