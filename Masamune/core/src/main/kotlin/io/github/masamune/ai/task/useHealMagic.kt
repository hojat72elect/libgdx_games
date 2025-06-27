package io.github.masamune.ai.task

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.canPerformAction
import io.github.masamune.combat.action.isHealing
import io.github.masamune.isEntityHurt
import ktx.ai.GdxAiDsl

class UseHealMagicTask(world: World) : FleksTask(world) {
    override fun World.onExecute() {
        useAction(targetCondition = { this.isEntityHurt(it) }) {
            magicActions
                .filter { it.isHealing && canPerformAction(entity, it) }
                .random()
        }
    }
}

@GdxAiDsl
inline fun Task<Entity>.useHealMagic(
    world: World,
    init: (@GdxAiDsl UseHealMagicTask).() -> Unit = {}
): Int {
    val eatTask = UseHealMagicTask(world)
    eatTask.init()
    return addChild(eatTask)
}
