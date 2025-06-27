package io.github.masamune.ai.task

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.canPerformAction
import ktx.ai.GdxAiDsl

class UseDefensiveMagicTask(world: World) : FleksTask(world) {
    override fun World.onExecute() {
        useAction {
            magicActions
                .filter { it.defensive && canPerformAction(entity, it) }
                .random()
        }
    }
}

@GdxAiDsl
inline fun Task<Entity>.useDefensiveMagic(
    world: World,
    init: (@GdxAiDsl UseDefensiveMagicTask).() -> Unit = {}
): Int {
    val eatTask = UseDefensiveMagicTask(world)
    eatTask.init()
    return addChild(eatTask)
}
