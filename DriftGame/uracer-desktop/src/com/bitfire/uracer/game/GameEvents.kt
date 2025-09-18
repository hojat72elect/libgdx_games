package com.bitfire.uracer.game

import com.bitfire.uracer.game.events.*

object GameEvents {
    @JvmField
    val gameRenderer: GameRendererEvent = GameRendererEvent()

    @JvmField
    val physicsStep = PhysicsStepEvent()

    @JvmField
    val driftState = PlayerDriftStateEvent()

    @JvmField
    val playerCar = CarEvent()

    @JvmField
    val ghostCars = GhostCarEvent()
    val taskManager = TaskManagerEvent()

    @JvmField
    val wrongWay = WrongWayMonitorEvent()

    @JvmField
    val lapCompletion = PlayerLapCompletionMonitorEvent()

    @JvmField
    val ghostLapCompletion = GhostLapCompletionMonitorEvent()

    @JvmField
    val logicEvent = GameLogicEvent()
}
