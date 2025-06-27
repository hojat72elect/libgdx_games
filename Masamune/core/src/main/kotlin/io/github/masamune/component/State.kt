package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import io.github.masamune.ai.FleksStateMachine

data class State(
    val animationFsm: FleksStateMachine,
) : Component<State> {
    override fun type() = State

    companion object : ComponentType<State>()
}
