package com.bitfire.uracer.game.actors

import com.bitfire.uracer.entities.Entity
import com.bitfire.uracer.entities.EntityRenderState
import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.events.GameRendererEvent
import com.bitfire.uracer.game.events.PhysicsStepEvent

abstract class SubframeInterpolableEntity : Entity() {

    // world coordinates
    protected var statePrevious = EntityRenderState()
    protected var stateCurrent = EntityRenderState()

    private val physicsListener = PhysicsStepEvent.Listener { source: Any, type: PhysicsStepEvent.Type, order: PhysicsStepEvent.Order ->
        when (type) {
            PhysicsStepEvent.Type.onBeforeTimestep -> onBeforePhysicsSubstep()
            PhysicsStepEvent.Type.onAfterTimestep -> onAfterPhysicsSubstep()
            PhysicsStepEvent.Type.onSubstepCompleted -> onSubstepCompleted()
        }
    }

    private val renderListener = GameRendererEvent.Listener { source: Any, type: GameRendererEvent.Type, order: GameRendererEvent.Order ->
        if (type == GameRendererEvent.Type.SubframeInterpolate) {
            onSubframeInterpolate(GameEvents.gameRenderer.timeAliasingFactor)
        }
    }

    init {
        GameEvents.physicsStep.addListener(physicsListener, PhysicsStepEvent.Type.onBeforeTimestep)
        GameEvents.physicsStep.addListener(physicsListener, PhysicsStepEvent.Type.onAfterTimestep)
        GameEvents.physicsStep.addListener(physicsListener, PhysicsStepEvent.Type.onSubstepCompleted)
        GameEvents.gameRenderer.addListener(renderListener, GameRendererEvent.Type.SubframeInterpolate, GameRendererEvent.Order.DEFAULT)
    }

    override fun dispose() {
        GameEvents.physicsStep.removeListener(physicsListener, PhysicsStepEvent.Type.onBeforeTimestep)
        GameEvents.physicsStep.removeListener(physicsListener, PhysicsStepEvent.Type.onAfterTimestep)
        GameEvents.physicsStep.removeListener(physicsListener, PhysicsStepEvent.Type.onSubstepCompleted)
        GameEvents.gameRenderer.removeListener(renderListener, GameRendererEvent.Type.SubframeInterpolate, GameRendererEvent.Order.DEFAULT)
    }

    abstract val isVisible: Boolean

    abstract fun saveStateTo(state: EntityRenderState)

    abstract val isSubframeInterpolated: Boolean

    protected fun resetState() {
        saveStateTo(stateCurrent)
        statePrevious.set(stateCurrent)
        stateRender.set(stateCurrent)
        stateRender.toPixels()
    }

   open fun onBeforePhysicsSubstep() {
        saveStateTo(statePrevious)
    }

    open fun onAfterPhysicsSubstep() {
        saveStateTo(stateCurrent)
    }

    open fun onSubstepCompleted() {}

    /**
     * Issued after a tick/physicsStep but before render
     */
    fun onSubframeInterpolate(aliasingFactor: Float) {
        if (this.isSubframeInterpolated) {
            if (!EntityRenderState.isEqual(statePrevious, stateCurrent)) {
                stateRender.set(EntityRenderState.interpolate(statePrevious, stateCurrent, aliasingFactor))
            } else {
                stateRender.set(stateCurrent)
            }
        } else {
            stateRender.set(stateCurrent)
        }

        stateRender.toPixels()
    }
}
