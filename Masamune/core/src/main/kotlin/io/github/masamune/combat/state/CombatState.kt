package io.github.masamune.combat.state

sealed interface CombatState {
    fun onEnter() = Unit

    fun onUpdate(deltaTime: Float) = Unit

    fun onExit() = Unit
}
