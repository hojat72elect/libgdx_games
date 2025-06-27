package io.github.masamune.ai.task

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.tiledmap.ActionType
import ktx.ai.GdxAiDsl

class PerformTask(world: World, private val actionType: ActionType) : FleksTask(world) {
    override fun World.onExecute() {
        useAction { actionType() }
    }
}

@GdxAiDsl
inline fun Task<Entity>.perform(
    world: World,
    actionType: ActionType,
    init: (@GdxAiDsl PerformTask).() -> Unit = {}
): Int {
    val eatTask = PerformTask(world, actionType)
    eatTask.init()
    return addChild(eatTask)
}
