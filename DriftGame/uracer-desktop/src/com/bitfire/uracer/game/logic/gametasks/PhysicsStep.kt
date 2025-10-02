package com.bitfire.uracer.game.logic.gametasks

import com.badlogic.gdx.physics.box2d.World
import com.bitfire.uracer.configuration.PhysicsUtils
import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.events.PhysicsStepEvent
import com.bitfire.uracer.game.events.TaskManagerEvent

class PhysicsStep(private val world: World, order: TaskManagerEvent.Order) : GameTask(order) {

    override fun dispose() {
        super.dispose()
        GameEvents.physicsStep.removeAllListeners()
    }

    override fun onTick() {
        GameEvents.physicsStep.trigger(this, PhysicsStepEvent.Type.OnBeforeTimestep)
        world.step(PhysicsUtils.Dt, 10, 10)
        GameEvents.physicsStep.trigger(this, PhysicsStepEvent.Type.OnAfterTimestep)
    }

    override fun onTickCompleted() {
        world.clearForces()
        GameEvents.physicsStep.trigger(this, PhysicsStepEvent.Type.OnSubstepCompleted)
    }

    override fun onGameReset() {
        world.clearForces()
    }

    override fun onGameRestart() {
        onGameReset()
    }
}
