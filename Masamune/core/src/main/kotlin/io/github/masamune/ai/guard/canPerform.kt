package io.github.masamune.ai.guard

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.canPerformAction
import io.github.masamune.component.Combat
import io.github.masamune.tiledmap.ActionType
import ktx.ai.GdxAiDsl

class CanPerform(world: World, private val actionType: ActionType) : FleksGuard(world) {
    override fun World.onExecute(): Boolean {
        val combatCmp = entity[Combat]
        if (actionType !in combatCmp.availableActionTypes) {
            return false
        }

        return canPerformAction(entity, actionType())
    }
}

@GdxAiDsl
inline fun Task<Entity>.canPerform(
    world: World,
    actionType: ActionType,
    init: (@GdxAiDsl CanPerform).() -> Unit = {}
): Int {
    val eatTask = CanPerform(world, actionType)
    eatTask.init()
    return addChild(eatTask)
}
