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

    private val physicsListener = PhysicsStepEvent.Listener { _: Any, type: PhysicsStepEvent.Type, _: PhysicsStepEvent.Order ->
        when (type) {
            PhysicsStepEvent.Type.OnBeforeTimestep -> onBeforePhysicsSubstep()
            PhysicsStepEvent.Type.OnAfterTimestep -> onAfterPhysicsSubstep()
            PhysicsStepEvent.Type.OnSubstepCompleted -> onSubstepCompleted()
        }
    }

    private val renderListener = GameRendererEvent.Listener { _: Any, type: GameRendererEvent.Type, _: GameRendererEvent.Order ->
        if (type == GameRendererEvent.Type.SubframeInterpolate) {
            onSubframeInterpolate(GameEvents.gameRenderer.timeAliasingFactor)
        }
    }

    init {
        GameEvents.physicsStep.addListener(physicsListener, PhysicsStepEvent.Type.OnBeforeTimestep)
        GameEvents.physicsStep.addListener(physicsListener, PhysicsStepEvent.Type.OnAfterTimestep)
        GameEvents.physicsStep.addListener(physicsListener, PhysicsStepEvent.Type.OnSubstepCompleted)
        GameEvents.gameRenderer.addListener(renderListener, GameRendererEvent.Type.SubframeInterpolate, GameRendererEvent.Order.DEFAULT)
    }

    override fun dispose() {
        GameEvents.physicsStep.removeListener(physicsListener, PhysicsStepEvent.Type.OnBeforeTimestep)
        GameEvents.physicsStep.removeListener(physicsListener, PhysicsStepEvent.Type.OnAfterTimestep)
        GameEvents.physicsStep.removeListener(physicsListener, PhysicsStepEvent.Type.OnSubstepCompleted)
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
