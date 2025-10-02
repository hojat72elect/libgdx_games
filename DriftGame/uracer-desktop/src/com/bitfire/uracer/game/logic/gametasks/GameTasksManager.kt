package com.bitfire.uracer.game.logic.gametasks

import com.badlogic.gdx.utils.Array
import com.bitfire.uracer.game.events.TaskManagerEvent
import com.bitfire.uracer.game.world.GameWorld

/**
 * Manages the creation and destruction of the main game tasks.
 */
class GameTasksManager(gameWorld: GameWorld) {

    private val tasks = Array<GameTask>(10)

    /**
     * keeps track of the concrete game tasks (note that they are all publicly accessible for performance reasons)
     */
    var physicsStep = PhysicsStep(gameWorld.box2DWorld, TaskManagerEvent.Order.MINUS_4)

    @JvmField
    var sound = SoundManager()

    @JvmField
    var hud = Hud()

    @JvmField
    var effects = TrackEffects()

    init {
        add(physicsStep)
        add(sound)
        add(hud)
        add(effects)
    }

    private fun add(task: GameTask) {
        tasks.add(task)
    }

    fun dispose() {
        for (task in tasks) task.dispose()
    }
}
