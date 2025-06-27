package io.github.masamune.ai.guard

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.canPerformAction
import io.github.masamune.combat.action.isPoison
import io.github.masamune.component.Combat
import ktx.ai.GdxAiDsl

class HasPoisonMagic(world: World) : FleksGuard(world) {
    override fun World.onExecute(): Boolean {
        return entity[Combat].magicActions
            .filter { it.isPoison }
            .any { canPerformAction(entity, it) }
    }
}

@GdxAiDsl
inline fun Task<Entity>.hasPoisonMagic(
    world: World,
    init: (@GdxAiDsl HasPoisonMagic).() -> Unit = {}
): Int {
    val eatTask = HasPoisonMagic(world)
    eatTask.init()
    return addChild(eatTask)
}
