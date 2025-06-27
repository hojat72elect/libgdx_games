package io.github.masamune.ai.guard

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

abstract class FleksGuard(val world: World) : LeafTask<Entity>() {
    val entity: Entity
        get() = `object`

    override fun copyTo(task: Task<Entity>) = task

    override fun execute(): Status {
        return when {
            world.run { onExecute() } -> Status.SUCCEEDED
            else -> Status.FAILED
        }
    }

    abstract fun World.onExecute(): Boolean
}
