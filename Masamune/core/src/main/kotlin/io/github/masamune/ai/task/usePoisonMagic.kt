package io.github.masamune.ai.task

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.canPerformAction
import io.github.masamune.combat.action.isPoison
import ktx.ai.GdxAiDsl

class UsePoisonMagic(world: World) : FleksTask(world) {
    override fun World.onExecute() {
        useAction {
            magicActions
                .filter { it.isPoison && canPerformAction(entity, it) }
                .random()
        }
    }
}

@GdxAiDsl
inline fun Task<Entity>.usePoisonMagic(
    world: World,
    init: (@GdxAiDsl UsePoisonMagic).() -> Unit = {}
): Int {
    val eatTask = UsePoisonMagic(world)
    eatTask.init()
    return addChild(eatTask)
}
