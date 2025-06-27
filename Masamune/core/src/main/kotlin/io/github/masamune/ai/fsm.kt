package io.github.masamune.ai

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

/**
 * A state of a [FleksStateMachine]. A single state can have:
 * - [onEnter]
 * - [onUpdate]
 * - [onExit]
 */
sealed interface FsmState {
    /**
     * Called whenever the [entity] enters the state.
     */
    fun World.onEnter(entity: Entity) = Unit

    /**
     * Periodically called as long as the [entity] remains in the state.
     */
    fun World.onUpdate(entity: Entity) = Unit

    /**
     * Called whenever the [entity] leaves the state.
     */
    fun World.onExit(entity: Entity) = Unit
}

/**
 * An empty default [FsmState] implementation.
 */
data object DefaultFsmState : FsmState

/**
 * A state machine implementation for Fleks ECS.
 * It is owned by an [entity] and has a [current][currentState] and [global][globalState] state.
 */
class FleksStateMachine(
    /**
     * The [World] context of the [FleksStateMachine].
     */
    private val world: World,
    /**
     * The owner of the [FleksStateMachine].
     */
    private val entity: Entity,
    /**
     * The initial [FsmState] of the [entity].
     */
    initialState: FsmState,
    /**
     * The initial global [FsmState] of the [entity].
     */
    initialGlobalState: FsmState = DefaultFsmState,
) {
    /**
     * The previous [FsmState] of the [entity].
     */
    var previousState: FsmState = DefaultFsmState
        private set

    /**
     * The current [FsmState] of the [entity].
     */
    var currentState: FsmState = DefaultFsmState
        private set

    /**
     * The global [FsmState] of the [entity].
     */
    var globalState: FsmState = initialGlobalState
        private set

    init {
        changeState(initialState)
        initialGlobalState.run { world.onEnter(entity) }
    }

    /**
     * Updates the state machine by first updating its [global][globalState] state, if there is any,
     * and afterward its [current][currentState] state. This is done by calling [onUpdate][FsmState.onUpdate]
     * for both states.
     */
    fun update() {
        // Execute the global state (if any)
        globalState.run { world.onUpdate(entity) }

        // Execute the current state (if any)
        currentState.run { world.onUpdate(entity) }
    }

    /**
     * Updates the [current][currentState] state of the state machine.
     * If there is already a [current][currentState] then its [onExit][FsmState.onExit] function is called.
     * Afterward, the [onEnter][FsmState.onEnter] function of the given [newState] is called.
     */
    fun changeState(newState: FsmState) {
        // Keep a record of the previous state
        previousState = currentState

        // Call the exit method of the existing state
        previousState.run { world.onExit(entity) }

        // Change state to the new state
        currentState = newState

        // Call the entry method of the new state
        currentState.run { world.onEnter(entity) }
    }
}
