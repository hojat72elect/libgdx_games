package io.github.masamune.ai.guard

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.canPerformAction
import io.github.masamune.component.Combat
import ktx.ai.GdxAiDsl

class HasOffensiveMagic(world: World) : FleksGuard(world) {
    override fun World.onExecute(): Boolean {
        return entity[Combat].magicActions
            .filter { !it.defensive }
            .any { canPerformAction(entity, it) }
    }
}

@GdxAiDsl
inline fun Task<Entity>.hasOffensiveMagic(
    world: World,
    init: (@GdxAiDsl HasOffensiveMagic).() -> Unit = {}
): Int {
    val eatTask = HasOffensiveMagic(world)
    eatTask.init()
    return addChild(eatTask)
}
