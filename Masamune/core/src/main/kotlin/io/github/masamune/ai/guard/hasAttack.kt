package io.github.masamune.ai.guard

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.canPerformAction
import io.github.masamune.component.Combat
import ktx.ai.GdxAiDsl

class HasAttack(world: World) : FleksGuard(world) {
    override fun World.onExecute(): Boolean {
        return canPerformAction(entity, entity[Combat].attackAction)
    }
}

@GdxAiDsl
inline fun Task<Entity>.hasAttack(
    world: World,
    init: (@GdxAiDsl HasAttack).() -> Unit = {}
): Int {
    val eatTask = HasAttack(world)
    eatTask.init()
    return addChild(eatTask)
}
